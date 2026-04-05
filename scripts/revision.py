#!/usr/bin/env python3
"""
DSA Nova - FSRS-Based Spaced Repetition System (revision.py)

Implements the Free Spaced Repetition Scheduler (FSRS v4) algorithm
adapted for DSA problem practice. Based on the Ebbinghaus forgetting
curve and the DSR (Difficulty-Stability-Retrievability) model.

Each solved problem gets a "memory card" with:
  D = Difficulty (1-10)
  S = Stability (days until 90% recall probability)
  R = Retrievability (current recall probability)

Usage:
    python scripts/revision.py due              # Show problems due for review
    python scripts/revision.py review P001 good # Grade a review (again/hard/good/easy)
    python scripts/revision.py stats            # Revision statistics
    python scripts/revision.py calendar         # Show upcoming review schedule
"""

import os
import sys
import json
import math
import datetime
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
REVISION_FILE = BASE_DIR / "revision_state.json"
CONFIG_FILE = BASE_DIR / "config.json"

# ============================================================
# FSRS v4 PARAMETERS (tuned for DSA practice)
# ============================================================
# These are adapted from FSRS defaults but tuned for coding problems
# where procedural memory decays differently than factual recall.
# Values can be overridden via config.json "fsrs" section.

_DEFAULT_FSRS_WEIGHTS = {
    "w0": 0.3,   "w1": 0.7,   "w2": 2.0,   "w3": 5.0,
    "w4": 7.0,   "w5": 0.5,   "w6": 0.9,   "w7": 0.05,
    "w8": 1.49,  "w9": 0.11,  "w10": 0.5,
    "w11": 2.0,  "w12": 0.02, "w13": 0.34,  "w14": 0.3,
    "w15": 0.52, "w16": 2.0,  "w17": 1.0,
}

_DEFAULT_TARGET_RETENTION = 0.90
_DEFAULT_EXPECTED_TIMES = {"EASY": 15, "MEDIUM": 30, "HARD": 45}


def _load_fsrs_config():
    """Load FSRS config from config.json, falling back to defaults."""
    weights = dict(_DEFAULT_FSRS_WEIGHTS)
    target_r = _DEFAULT_TARGET_RETENTION
    expected_times = dict(_DEFAULT_EXPECTED_TIMES)
    try:
        if CONFIG_FILE.exists():
            with open(CONFIG_FILE, "r", encoding="utf-8") as f:
                cfg = json.load(f)
            fsrs_cfg = cfg.get("fsrs", {})
            if "weights" in fsrs_cfg:
                weights.update(fsrs_cfg["weights"])
            if "target_retention" in fsrs_cfg:
                target_r = float(fsrs_cfg["target_retention"])
            if "expected_solve_times" in fsrs_cfg:
                expected_times.update(fsrs_cfg["expected_solve_times"])
    except (json.JSONDecodeError, KeyError, ValueError) as e:
        print(f"[revision] Warning: config.json fsrs section malformed, using defaults: {e}", file=sys.stderr)
    return weights, target_r, expected_times


FSRS_WEIGHTS, TARGET_RETENTION, EXPECTED_TIMES = _load_fsrs_config()

# Grade mapping: user feedback -> FSRS grade (1-4)
GRADE_MAP = {
    "again": 1,   # Couldn't solve / completely forgot the approach
    "hard": 2,    # Solved but struggled, needed hints, or brute force only
    "good": 3,    # Solved optimally within expected time
    "easy": 4,    # Solved instantly, could explain in sleep
}


def load_revision_state():
    """Load or initialize the revision state."""
    if REVISION_FILE.exists():
        with open(REVISION_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return {"cards": {}, "review_log": [], "stats": {"total_reviews": 0, "total_lapses": 0}}


def save_revision_state(state):
    """Persist revision state."""
    with open(REVISION_FILE, "w", encoding="utf-8") as f:
        json.dump(state, f, indent=2)


# ============================================================
# FSRS CORE FORMULAS
# ============================================================

def retrievability(stability, elapsed_days):
    """
    Calculate current recall probability using power-law forgetting curve.
    R(t, S) = (1 + t / (9 * S))^(-1)

    Returns value between 0 and 1.
    """
    if stability <= 0:
        return 0.0
    if elapsed_days <= 0:
        return 1.0
    return math.pow(1 + elapsed_days / (9.0 * stability), -1)


def optimal_interval(stability, target_r=TARGET_RETENTION):
    """
    Calculate optimal review interval for target retention.
    I = 9 * S * (1/R_target - 1)

    For R=0.90: I ≈ S (review when stability = interval)
    For R=0.85: I ≈ 1.59 * S
    """
    if stability <= 0:
        return 1
    interval = 9.0 * stability * (1.0 / target_r - 1.0)
    return max(1, round(interval))


def initial_stability(grade):
    """
    Initial stability after first learning based on grade.
    S_0(G) = w[G-1]
    """
    w = FSRS_WEIGHTS
    return [w["w0"], w["w1"], w["w2"], w["w3"]][grade - 1]


def initial_difficulty(grade):
    """
    Initial difficulty after first learning.
    D_0(G) = w4 - e^(w5 * (G-1)) + 1
    Clamped to [1, 10]
    """
    w = FSRS_WEIGHTS
    d = w["w4"] - math.exp(w["w5"] * (grade - 1)) + 1
    return max(1.0, min(10.0, d))


def stability_after_recall(d, s, r, grade):
    """
    New stability after successful recall.
    S' = S * (1 + e^w8 * (11-D) * S^(-w9) * (e^(w10*(1-R)) - 1) * w17
          * (hard_mod) * (easy_mod))
    """
    w = FSRS_WEIGHTS

    hard_mod = w["w15"] if grade == 2 else 1.0
    easy_mod = w["w16"] if grade == 4 else 1.0

    increase = (
        math.exp(w["w8"])
        * (11 - d)
        * math.pow(s, -w["w9"])
        * (math.exp(w["w10"] * (1 - r)) - 1)
        * w["w17"]
        * hard_mod
        * easy_mod
    )

    new_s = s * (1 + increase)
    return max(0.1, new_s)


def stability_after_lapse(d, s, r):
    """
    New stability after forgetting (grade=Again).
    S' = w11 * D^(-w12) * ((S+1)^w13 - 1) * e^(w14 * (1-R))
    """
    w = FSRS_WEIGHTS
    new_s = (
        w["w11"]
        * math.pow(d, -w["w12"])
        * (math.pow(s + 1, w["w13"]) - 1)
        * math.exp(w["w14"] * (1 - r))
    )
    return max(0.1, min(new_s, s))  # Never increase stability on lapse


def update_difficulty(d, grade):
    """
    Update difficulty after a review.
    D' = w7 * D_0(3) + (1 - w7) * (D - w6 * (G - 3))
    Mean-reverts toward Good baseline while adjusting for grade.
    """
    w = FSRS_WEIGHTS
    d0_good = initial_difficulty(3)
    new_d = w["w7"] * d0_good + (1 - w["w7"]) * (d - w["w6"] * (grade - 3))
    return max(1.0, min(10.0, new_d))


# ============================================================
# CARD MANAGEMENT
# ============================================================

def create_card(problem_id, title, difficulty, grade, time_taken_mins=None):
    """
    Create a new revision card for a freshly solved problem.

    Args:
        problem_id: e.g., "P001" or "IB023"
        title: Problem title
        difficulty: "EASY", "MEDIUM", "HARD"
        grade: "again", "hard", "good", "easy" (how it felt)
        time_taken_mins: optional, for auto-grading
    """
    g = GRADE_MAP.get(grade, 3)

    # Auto-adjust grade based on time if provided
    if time_taken_mins is not None:
        expected = EXPECTED_TIMES.get(difficulty, 30)
        if time_taken_mins <= expected * 0.5 and g < 4:
            g = min(g + 1, 4)  # Bump up if very fast
        elif time_taken_mins > expected * 2.0 and g > 1:
            g = max(g - 1, 1)  # Bump down if very slow

    s = initial_stability(g)
    d = initial_difficulty(g)
    interval = optimal_interval(s)

    today = datetime.date.today().isoformat()
    due_date = (datetime.date.today() + datetime.timedelta(days=interval)).isoformat()

    card = {
        "problem_id": problem_id,
        "title": title,
        "problem_difficulty": difficulty,
        "fsrs_difficulty": round(d, 2),
        "stability": round(s, 2),
        "interval": interval,
        "review_count": 0,
        "lapse_count": 0,
        "created": today,
        "last_review": today,
        "due_date": due_date,
        "state": "learning",  # learning -> review -> mastered
    }
    return card


def review_card(card, grade, time_taken_mins=None):
    """
    Process a review of an existing card.

    Returns updated card and a result dict.
    """
    g = GRADE_MAP.get(grade, 3)

    # Auto-adjust grade based on time
    if time_taken_mins is not None:
        expected = EXPECTED_TIMES.get(card.get("problem_difficulty", "MEDIUM"), 30)
        if time_taken_mins <= expected * 0.4 and g < 4:
            g = min(g + 1, 4)
        elif time_taken_mins > expected * 2.0 and g > 1:
            g = max(g - 1, 1)

    # Calculate current retrievability
    last_review = datetime.date.fromisoformat(card["last_review"])
    elapsed = max(0, (datetime.date.today() - last_review).days)
    r = retrievability(card["stability"], elapsed)

    old_s = card["stability"]
    old_d = card["fsrs_difficulty"]

    # Update stability based on whether this was a lapse or recall
    if g == 1:  # Again = lapse
        new_s = stability_after_lapse(old_d, old_s, r)
        card["lapse_count"] = card.get("lapse_count", 0) + 1
        card["state"] = "relearning"
    else:  # Recall (Hard/Good/Easy)
        new_s = stability_after_recall(old_d, old_s, r, g)
        if new_s > 90:
            card["state"] = "mastered"
        else:
            card["state"] = "review"

    # Update difficulty
    new_d = update_difficulty(old_d, g)

    # Calculate new interval
    new_interval = optimal_interval(new_s)

    today = datetime.date.today().isoformat()
    due_date = (datetime.date.today() + datetime.timedelta(days=new_interval)).isoformat()

    card["stability"] = round(new_s, 2)
    card["fsrs_difficulty"] = round(new_d, 2)
    card["interval"] = new_interval
    card["last_review"] = today
    card["due_date"] = due_date
    card["review_count"] = card.get("review_count", 0) + 1

    result = {
        "grade": grade,
        "grade_num": g,
        "retrievability_before": round(r, 3),
        "stability_change": round(new_s - old_s, 2),
        "difficulty_change": round(new_d - old_d, 2),
        "new_interval": new_interval,
        "next_due": due_date,
        "state": card["state"],
    }

    return card, result


def get_due_cards(state, include_upcoming_days=0):
    """
    Get all cards that are due for review.

    Args:
        state: revision state dict
        include_upcoming_days: also include cards due within N days

    Returns sorted list of (urgency, card) - most urgent first.
    """
    today = datetime.date.today()
    cutoff = today + datetime.timedelta(days=include_upcoming_days)
    due = []

    for pid, card in state["cards"].items():
        due_date = datetime.date.fromisoformat(card["due_date"])
        if due_date <= cutoff:
            # Calculate urgency: how overdue is it?
            days_overdue = (today - due_date).days
            r = retrievability(card["stability"], max(0, (today - datetime.date.fromisoformat(card["last_review"])).days))

            # Urgency = combination of overdue-ness and low retrievability
            urgency = (1 - r) * 0.6 + max(0, days_overdue / 30) * 0.4
            due.append((urgency, card))

    # Sort by urgency (most urgent first)
    due.sort(key=lambda x: -x[0])
    return due


def get_review_forecast(state, days=14):
    """Show how many reviews are coming up each day."""
    today = datetime.date.today()
    forecast = {}
    for i in range(days):
        d = (today + datetime.timedelta(days=i)).isoformat()
        forecast[d] = 0

    for pid, card in state["cards"].items():
        due = card["due_date"]
        if due in forecast:
            forecast[due] += 1
        elif due < today.isoformat():
            # Overdue cards count as today
            forecast[today.isoformat()] = forecast.get(today.isoformat(), 0) + 1

    return forecast


def get_revision_stats(state):
    """Compute revision statistics."""
    cards = state["cards"]
    today = datetime.date.today()

    total = len(cards)
    if total == 0:
        return {
            "total_cards": 0, "due_today": 0, "overdue": 0,
            "learning": 0, "review": 0, "mastered": 0,
            "avg_stability": 0, "avg_difficulty": 0,
            "total_reviews": state["stats"]["total_reviews"],
            "total_lapses": state["stats"]["total_lapses"],
            "retention_rate": 0,
        }

    due_today = 0
    overdue = 0
    learning = 0
    review = 0
    mastered = 0
    total_stability = 0
    total_difficulty = 0
    total_retrievability = 0

    for pid, card in cards.items():
        due_date = datetime.date.fromisoformat(card["due_date"])
        last_review = datetime.date.fromisoformat(card["last_review"])
        elapsed = max(0, (today - last_review).days)

        if due_date <= today:
            due_today += 1
        if due_date < today:
            overdue += 1

        s = card.get("state", "learning")
        if s == "learning" or s == "relearning":
            learning += 1
        elif s == "mastered":
            mastered += 1
        else:
            review += 1

        total_stability += card["stability"]
        total_difficulty += card["fsrs_difficulty"]
        total_retrievability += retrievability(card["stability"], elapsed)

    total_reviews = state["stats"]["total_reviews"]
    total_lapses = state["stats"]["total_lapses"]
    retention_rate = ((total_reviews - total_lapses) / total_reviews * 100) if total_reviews > 0 else 0

    return {
        "total_cards": total,
        "due_today": due_today,
        "overdue": overdue,
        "learning": learning,
        "review": review,
        "mastered": mastered,
        "avg_stability": round(total_stability / total, 1),
        "avg_difficulty": round(total_difficulty / total, 1),
        "avg_retrievability": round(total_retrievability / total * 100, 1),
        "total_reviews": total_reviews,
        "total_lapses": total_lapses,
        "retention_rate": round(retention_rate, 1),
    }


# ============================================================
# PUBLIC API (used by brain.py and play.py)
# ============================================================

def add_to_revision(problem_id, title, difficulty, grade, time_taken_mins=None):
    """Add a newly solved problem to the revision system."""
    state = load_revision_state()
    card = create_card(problem_id, title, difficulty, grade, time_taken_mins)
    state["cards"][problem_id] = card
    save_revision_state(state)
    return card


def process_review(problem_id, grade, time_taken_mins=None):
    """Process a review for an existing card."""
    state = load_revision_state()
    if problem_id not in state["cards"]:
        return {"error": f"No revision card for {problem_id}"}

    card = state["cards"][problem_id]
    card, result = review_card(card, grade, time_taken_mins)
    state["cards"][problem_id] = card

    # Update stats
    state["stats"]["total_reviews"] += 1
    if result["grade_num"] == 1:
        state["stats"]["total_lapses"] += 1

    # Log the review
    state["review_log"].append({
        "problem_id": problem_id,
        "date": datetime.date.today().isoformat(),
        "grade": grade,
        "retrievability": result["retrievability_before"],
    })

    # Keep only last 500 log entries
    if len(state["review_log"]) > 500:
        state["review_log"] = state["review_log"][-500:]

    save_revision_state(state)
    return result


def get_due_reviews(include_upcoming_days=0):
    """Get problems due for review."""
    state = load_revision_state()
    return get_due_cards(state, include_upcoming_days)


def get_revision_info(problem_id):
    """Get revision card info for a specific problem."""
    state = load_revision_state()
    card = state["cards"].get(problem_id)
    if not card:
        return None

    today = datetime.date.today()
    last_review = datetime.date.fromisoformat(card["last_review"])
    elapsed = max(0, (today - last_review).days)
    r = retrievability(card["stability"], elapsed)

    return {
        **card,
        "current_retrievability": round(r, 3),
        "days_since_review": elapsed,
        "is_due": card["due_date"] <= today.isoformat(),
    }


# ============================================================
# CLI INTERFACE
# ============================================================

def cli_due(args):
    """Show problems due for review."""
    days = 0
    if args and args[0].isdigit():
        days = int(args[0])

    due = get_due_reviews(include_upcoming_days=days)

    print(f"\n{'='*60}")
    if days > 0:
        print(f"  REVIEWS DUE (today + next {days} days)")
    else:
        print(f"  REVIEWS DUE TODAY")
    print(f"{'='*60}")

    if not due:
        print("  No reviews due! You're all caught up.")
        print()
        return

    for i, (urgency, card) in enumerate(due[:20], 1):
        r_pct = retrievability(
            card["stability"],
            max(0, (datetime.date.today() - datetime.date.fromisoformat(card["last_review"])).days)
        ) * 100

        state_icon = {"learning": "L", "relearning": "!", "review": "R", "mastered": "M"}.get(card["state"], "?")
        diff = card.get("problem_difficulty", "?")

        urgency_bar = "#" * min(10, max(1, int(urgency * 10)))

        print(f"  {i:>2}. [{state_icon}] {card['title']}")
        print(f"      ID: {card['problem_id']} | {diff} | Recall: {r_pct:.0f}% | Due: {card['due_date']}")
        print(f"      Stability: {card['stability']:.1f}d | Urgency: {urgency_bar}")
    print()


def cli_review(args):
    """Process a review."""
    if len(args) < 2:
        print("Usage: revision.py review <problem_id> <grade>")
        print("  Grades: again, hard, good, easy")
        return

    pid = args[0]
    grade = args[1].lower()
    time_mins = float(args[2]) if len(args) > 2 else None

    if grade not in GRADE_MAP:
        print(f"Invalid grade: {grade}. Use: again, hard, good, easy")
        return

    result = process_review(pid, grade, time_mins)

    if "error" in result:
        print(f"  Error: {result['error']}")
        return

    print(f"\n{'='*60}")
    print(f"  REVIEW RECORDED")
    print(f"{'='*60}")
    print(f"  Grade:         {grade.upper()}")
    print(f"  Recall was:    {result['retrievability_before']*100:.0f}%")
    print(f"  Stability:     {'+' if result['stability_change'] > 0 else ''}{result['stability_change']:.1f} days")
    print(f"  Next review:   {result['next_due']} ({result['new_interval']} days)")
    print(f"  Card state:    {result['state']}")
    print()


def cli_stats(args):
    """Show revision statistics."""
    state = load_revision_state()
    stats = get_revision_stats(state)

    print(f"\n{'='*60}")
    print(f"  REVISION STATISTICS")
    print(f"{'='*60}")
    print(f"  Total Cards:     {stats['total_cards']}")
    print(f"  Due Today:       {stats['due_today']}")
    print(f"  Overdue:         {stats['overdue']}")
    print()
    print(f"  States:")
    print(f"    Learning:      {stats['learning']}")
    print(f"    In Review:     {stats['review']}")
    print(f"    Mastered:      {stats['mastered']} (stability > 90 days)")
    print()
    print(f"  Averages:")
    print(f"    Stability:     {stats['avg_stability']} days")
    print(f"    Difficulty:    {stats['avg_difficulty']} / 10")
    print(f"    Retrievability:{stats.get('avg_retrievability', 0)}%")
    print()
    print(f"  History:")
    print(f"    Total Reviews: {stats['total_reviews']}")
    print(f"    Total Lapses:  {stats['total_lapses']}")
    print(f"    Retention Rate:{stats['retention_rate']}%")
    print()


def cli_calendar(args):
    """Show upcoming review calendar."""
    state = load_revision_state()
    days = 14
    if args and args[0].isdigit():
        days = int(args[0])

    forecast = get_review_forecast(state, days)

    print(f"\n{'='*60}")
    print(f"  REVIEW CALENDAR (next {days} days)")
    print(f"{'='*60}")

    today = datetime.date.today().isoformat()
    for date_str, count in forecast.items():
        bar = "#" * count if count > 0 else "-"
        marker = " <-- TODAY" if date_str == today else ""
        day_name = datetime.date.fromisoformat(date_str).strftime("%a")
        print(f"  {date_str} ({day_name}): {count:>3} reviews  {bar}{marker}")
    print()


def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return

    command = sys.argv[1].lower()
    args = sys.argv[2:]

    commands = {
        "due": cli_due,
        "review": cli_review,
        "stats": cli_stats,
        "calendar": cli_calendar,
    }

    if command in commands:
        commands[command](args)
    else:
        print(f"Unknown command: {command}")
        print(f"Available: {', '.join(commands.keys())}")


if __name__ == "__main__":
    main()
