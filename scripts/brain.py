#!/usr/bin/env python3
"""
DSA Nova - Intelligent Recommendation Engine (brain.py)

An adaptive problem recommendation system that tracks skill levels per topic,
uses spaced repetition, difficulty progression, and related-topic chaining
to suggest the best next problem for the learner.

Usage:
    python scripts/brain.py next                          # Get next recommended problem
    python scripts/brain.py next --topic arrays           # Next problem in a specific topic
    python scripts/brain.py complete IB001 15 easy        # Mark problem done (id, mins, felt-difficulty)
    python scripts/brain.py similar IB001                 # Find similar problems
    python scripts/brain.py stats                         # Show skill stats
    python scripts/brain.py weak                          # Show weakest topics
    python scripts/brain.py reset                         # Reset brain state
"""

import os
import sys
import json
import math
import random
import datetime
from collections import defaultdict
from pathlib import Path

from shared import get_base_dir
BASE_DIR = get_base_dir()
BRAIN_FILE = BASE_DIR / "player_brain.json"
PROGRESS_FILE = BASE_DIR / "progress.json"
CONFIG_FILE = BASE_DIR / "config.json"


def _load_config():
    """Load config.json for XP rules and settings."""
    if CONFIG_FILE.exists():
        try:
            with open(CONFIG_FILE, "r", encoding="utf-8") as f:
                return json.load(f)
        except (json.JSONDecodeError, KeyError) as e:
            print(f"[brain] Warning: config.json is malformed, using defaults: {e}", file=sys.stderr)
    return {
        "xp_rules": {
            "base_xp": {"EASY": 10, "MEDIUM": 25, "HARD": 50},
            "bonus_multipliers": {"all_three_approaches": 1.5, "both_languages": 1.3},
            "streak_bonus": {"3": 5, "7": 15, "14": 30, "30": 100},
        },
        "settings": {"unlock_threshold_percent": 78},
    }


CONFIG = _load_config()

# Import revision and batch systems
try:
    from revision import add_to_revision, get_due_reviews, process_review, get_revision_info
    from batch_engine import (
        load_batch_state, update_batch_progress, get_next_from_batch,
        get_batch_problems, assign_problems_to_batches
    )
    HAS_REVISION = True
    HAS_BATCHES = True
except ImportError:
    HAS_REVISION = False
    HAS_BATCHES = False

# ============================================================
# TOPIC RELATIONSHIPS (for chaining recommendations)
# ============================================================
TOPIC_RELATIONS = {
    "arrays": ["sorting", "two-pointers", "prefix-sum", "hashing", "sliding-window"],
    "sorting": ["arrays", "two-pointers", "binary-search"],
    "binary-search": ["arrays", "sorting", "two-pointers"],
    "strings": ["hashing", "two-pointers", "dp"],
    "linked-list": ["two-pointers", "stack", "recursion"],
    "stack": ["queues", "strings", "recursion", "monotonic-stack"],
    "queues": ["stack", "bfs", "sliding-window"],
    "hashing": ["arrays", "strings", "two-pointers"],
    "two-pointers": ["arrays", "sorting", "binary-search", "sliding-window"],
    "sliding-window": ["two-pointers", "hashing", "arrays"],
    "recursion": ["backtracking", "trees", "dp"],
    "backtracking": ["recursion", "dp", "trees"],
    "trees": ["recursion", "bfs", "dfs", "bst"],
    "bst": ["trees", "binary-search", "recursion"],
    "heaps": ["sorting", "greedy", "arrays"],
    "greedy": ["sorting", "arrays", "dp"],
    "graphs": ["bfs", "dfs", "shortest-path", "topological-sort"],
    "bfs": ["graphs", "trees", "queues"],
    "dfs": ["graphs", "trees", "recursion", "backtracking"],
    "dp": ["recursion", "arrays", "strings", "trees"],
    "bit-manipulation": ["math", "arrays"],
    "math": ["arrays", "number-theory", "combinatorics"],
    "prefix-sum": ["arrays", "hashing"],
    "divide-conquer": ["recursion", "binary-search", "sorting"],
}

# Difficulty levels in order
DIFFICULTIES = ["EASY", "MEDIUM", "HARD"]
DIFF_SCORES = {"EASY": 1, "MEDIUM": 2, "HARD": 3}

# ELO-like constants
INITIAL_RATING = 1000
K_FACTOR = 32
EASY_THRESHOLD = 1100
MEDIUM_THRESHOLD = 1300

# Spaced repetition intervals (days since last solved in topic)
SPACED_REP_INTERVALS = [1, 3, 7, 14, 30]


def _get_unique_id(meta, meta_path):
    """Path-based unique ID to avoid P001 collisions across steps."""
    try:
        parts = Path(meta_path).parent.relative_to(BASE_DIR).parts
        if len(parts) >= 3:
            return f"{parts[0]}/{parts[1]}/{meta['id']}"
        elif len(parts) >= 2:
            return f"{parts[0]}/{meta['id']}"
    except (ValueError, IndexError):
        pass  # Fall through to return raw meta["id"]
    return meta["id"]


# Tag inference imported from shared module (single source of truth)
from shared import infer_tags as _infer_tags


def load_all_problems():
    """Scan all meta.json files and return list of problem dicts."""
    problems = []
    for meta_path in BASE_DIR.rglob("meta.json"):
        if "templates" in str(meta_path):
            continue
        try:
            with open(meta_path, "r", encoding="utf-8") as f:
                meta = json.load(f)
            meta["_path"] = str(meta_path.parent)
            meta["_meta_path"] = str(meta_path)
            meta["_uid"] = _get_unique_id(meta, meta_path)
            # Fallback tag inference if tags are empty
            if not meta.get("tags"):
                meta["tags"] = _infer_tags(str(meta_path))
            problems.append(meta)
        except (json.JSONDecodeError, KeyError) as e:
            print(f"[brain] Warning: skipping malformed {meta_path}: {e}", file=sys.stderr)
            continue
    return problems


def load_brain():
    """Load or initialize the player brain state."""
    if BRAIN_FILE.exists():
        with open(BRAIN_FILE, "r", encoding="utf-8") as f:
            brain = json.load(f)
        _migrate_brain_to_uids(brain)
        return brain
    return create_default_brain()


def _migrate_brain_to_uids(brain):
    """Migrate solved_problems from raw id keys to uid keys (one-time)."""
    solved = brain.get("solved_problems", {})
    if not solved:
        return

    # Check if any keys are raw ids (no "/" = not yet migrated)
    raw_keys = [k for k in solved if "/" not in k]
    if not raw_keys:
        return

    # Build id -> uid map from all problems
    id_to_uid = {}
    for meta_path in BASE_DIR.rglob("meta.json"):
        if "templates" in str(meta_path):
            continue
        try:
            with open(meta_path, "r", encoding="utf-8") as f:
                meta = json.load(f)
            uid = _get_unique_id(meta, meta_path)
            # For IB problems (globally unique), map id -> uid
            # For P problems, map id -> first uid found (best effort)
            if meta["id"] not in id_to_uid or meta["id"].startswith("IB"):
                id_to_uid[meta["id"]] = uid
        except (json.JSONDecodeError, KeyError):
            continue  # Skip malformed meta.json during migration

    new_solved = {}
    for key, value in solved.items():
        if "/" in key:
            new_solved[key] = value  # Already a uid
        elif key in id_to_uid:
            new_solved[id_to_uid[key]] = value
        else:
            new_solved[key] = value  # Can't migrate, keep as-is

    brain["solved_problems"] = new_solved

    # Also migrate today_solved list
    today_solved = brain.get("today_solved", [])
    brain["today_solved"] = [
        id_to_uid.get(pid, pid) if "/" not in pid else pid
        for pid in today_solved
    ]


def create_default_brain():
    """Create a fresh brain state."""
    return {
        "topic_ratings": {},       # tag -> ELO rating
        "solved_problems": {},     # problem_id -> {date, time_taken, felt_difficulty, times_solved}
        "topic_last_solved": {},   # tag -> ISO date string
        "topic_solve_counts": {},  # tag -> count
        "session_history": [],     # list of {date, problems: [{id, time, topic}]}
        "today_solved": [],        # problem IDs solved today
        "today_date": None,
        "total_sessions": 0,
        "difficulty_comfort": {},  # tag -> highest comfortable difficulty
    }


def save_brain(brain):
    """Persist brain state."""
    with open(BRAIN_FILE, "w", encoding="utf-8") as f:
        json.dump(brain, f, indent=2)


def get_topic_rating(brain, topic):
    """Get ELO rating for a topic, initializing if needed."""
    if topic not in brain["topic_ratings"]:
        brain["topic_ratings"][topic] = INITIAL_RATING
    return brain["topic_ratings"][topic]


def update_topic_rating(brain, topic, problem_difficulty, time_taken_mins, felt_difficulty):
    """
    Update ELO-like rating for a topic based on performance.

    Performance is judged by:
    - Problem difficulty vs current rating
    - Time taken (faster = better)
    - Felt difficulty (if they found it easy, they're above this level)
    """
    current = get_topic_rating(brain, topic)

    # Expected score based on problem difficulty
    problem_elo = {
        "EASY": 1000,
        "MEDIUM": 1200,
        "HARD": 1500
    }[problem_difficulty]

    expected = 1.0 / (1.0 + math.pow(10, (problem_elo - current) / 400.0))

    # Actual score based on felt difficulty and time
    felt_map = {"easy": 1.0, "medium": 0.6, "hard": 0.3, "failed": 0.0}
    actual = felt_map.get(felt_difficulty, 0.5)

    # Time bonus: if solved quickly relative to difficulty
    expected_time = {"EASY": 15, "MEDIUM": 30, "HARD": 60}[problem_difficulty]
    if time_taken_mins <= expected_time * 0.5:
        actual = min(1.0, actual + 0.15)
    elif time_taken_mins <= expected_time * 0.75:
        actual = min(1.0, actual + 0.05)
    elif time_taken_mins > expected_time * 1.5:
        actual = max(0.0, actual - 0.1)

    new_rating = current + K_FACTOR * (actual - expected)
    brain["topic_ratings"][topic] = round(new_rating, 1)

    return round(new_rating - current, 1)


def get_recommended_difficulty(brain, topic):
    """Determine appropriate difficulty for a topic based on rating."""
    rating = get_topic_rating(brain, topic)
    if rating < EASY_THRESHOLD:
        return "EASY"
    elif rating < MEDIUM_THRESHOLD:
        return "MEDIUM"
    else:
        return "HARD"


def get_weak_topics(brain, top_n=5):
    """Return the N weakest topics by rating."""
    if not brain["topic_ratings"]:
        return []
    sorted_topics = sorted(brain["topic_ratings"].items(), key=lambda x: x[1])
    # Filter out meta-tags
    meta_tags = {"interviewbit", "implementation", "simulation", "parsing"}
    filtered = [(t, r) for t, r in sorted_topics if t not in meta_tags]
    return filtered[:top_n]


def calculate_topic_urgency(brain, topic):
    """
    Calculate how urgently a topic needs practice.
    Higher = more urgent. Factors:
    - Low rating = more urgent
    - Long time since last solved = more urgent (spaced repetition)
    - Low solve count = more urgent
    """
    rating = get_topic_rating(brain, topic)

    # Rating urgency (lower rating = higher urgency)
    rating_urgency = max(0, (1400 - rating) / 400.0)

    # Spaced repetition urgency
    last_solved = brain["topic_last_solved"].get(topic)
    if last_solved is None:
        time_urgency = 1.0  # Never solved = very urgent
    else:
        try:
            last_date = datetime.date.fromisoformat(last_solved)
            days_since = (datetime.date.today() - last_date).days
            # Urgency increases as we pass spaced repetition intervals
            time_urgency = 0
            for interval in SPACED_REP_INTERVALS:
                if days_since >= interval:
                    time_urgency += 0.2
        except ValueError:
            time_urgency = 0.5

    # Solve count urgency (fewer solves = more urgent)
    solve_count = brain["topic_solve_counts"].get(topic, 0)
    count_urgency = 1.0 / (1.0 + solve_count * 0.1)

    return round(rating_urgency * 0.4 + time_urgency * 0.35 + count_urgency * 0.25, 3)


def get_related_topics(topic):
    """Get related topics for chaining."""
    return TOPIC_RELATIONS.get(topic, [])


def recommend(brain=None, topic_filter=None, count=1, mode="smart"):
    """
    Main recommendation function with 3 modes:

    mode="smart" (default):
        Hybrid: checks revision queue first, then batch, then open pool.
        Daily session: 2 reviews + 1 from batch + 1 stretch

    mode="batch":
        Only from current batch (structured learning)

    mode="open":
        Original algorithm - any unsolved problem (exam cram mode)

    Algorithm for "smart" mode:
    1. Check FSRS revision queue - if problems are due, prioritize those
    2. Check current batch for unsolved problems
    3. Fall back to open pool with full scoring
    """
    if brain is None:
        brain = load_brain()

    results = []

    # ── PHASE 1: Revision Queue (if available) ──
    # Problems you've solved before but might be forgetting
    if HAS_REVISION and mode == "smart":
        try:
            due_reviews = get_due_reviews(include_upcoming_days=1)
            if due_reviews:
                problems = load_all_problems()
                # Build dual lookup: both raw id and uid -> problem
                problem_map = {}
                for p in problems:
                    problem_map[p["id"]] = p
                    problem_map[p["_uid"]] = p
                for urgency, card in due_reviews[:min(2, count)]:
                    pid = card["problem_id"]
                    if pid in problem_map:
                        p = problem_map[pid].copy()
                        p["_recommendation_type"] = "REVISION"
                        p["_revision_urgency"] = round(urgency, 2)
                        p["_revision_info"] = card
                        results.append(p)
                        count -= 1
        except Exception as e:
            print(f"[brain] Warning: revision lookup failed: {e}", file=sys.stderr)

    if count <= 0:
        return results

    # ── PHASE 2: Batch-Aware Selection ──
    # Pick from current batch if batches are set up
    if HAS_BATCHES and mode in ("smart", "batch"):
        try:
            batch_state = load_batch_state()
            if batch_state.get("assigned"):
                batch_problem = get_next_from_batch(brain=brain)
                if batch_problem:
                    # Check topic filter
                    if topic_filter:
                        tags = batch_problem.get("tags", [])
                        if any(topic_filter.lower() in t.lower() for t in tags):
                            batch_problem["_recommendation_type"] = "BATCH"
                            batch_problem["_batch_id"] = batch_state["current_batch"]
                            results.append(batch_problem)
                            count -= 1
                    else:
                        batch_problem["_recommendation_type"] = "BATCH"
                        batch_problem["_batch_id"] = batch_state["current_batch"]
                        results.append(batch_problem)
                        count -= 1
        except Exception as e:
            print(f"[brain] Warning: batch lookup failed: {e}", file=sys.stderr)

    if count <= 0:
        return results

    if mode == "batch":
        return results

    # ── PHASE 3: Open Pool Scoring (original algorithm enhanced) ──
    problems = load_all_problems()
    solved_keys = set(brain["solved_problems"].keys())
    # Build set of solved uids (handles both old id-based and new uid-based keys)
    solved_uids = set()
    for p in problems:
        if p["_uid"] in solved_keys or p["id"] in solved_keys:
            solved_uids.add(p["_uid"])
    already_recommended = {p.get("_uid", p["id"]) for p in results}
    unsolved = [p for p in problems if p["_uid"] not in solved_uids and p["_uid"] not in already_recommended]

    if not unsolved:
        return results

    # Apply topic filter if specified
    if topic_filter:
        topic_filter_lower = topic_filter.lower()
        unsolved = [p for p in unsolved if any(topic_filter_lower in t.lower() for t in p.get("tags", []))]
        if not unsolved:
            return results

    # Get batch assignment info for bonus scoring
    batch_assignments = {}
    current_batch = 1
    if HAS_BATCHES:
        try:
            batch_state = load_batch_state()
            batch_assignments = batch_state.get("assignments", {})
            current_batch = batch_state.get("current_batch", 1)
        except Exception as e:
            print(f"[brain] Warning: batch state load failed for scoring: {e}", file=sys.stderr)

    # Get recent topics for variety scoring
    recent_topics = set()
    for pid in brain.get("today_solved", [])[-3:]:
        if pid in brain["solved_problems"]:
            info = brain["solved_problems"][pid]
            recent_topics.update(info.get("tags", []))

    # Last solved topic for chaining
    last_topic = None
    if brain.get("today_solved"):
        last_pid = brain["today_solved"][-1]
        if last_pid in brain["solved_problems"]:
            tags = brain["solved_problems"][last_pid].get("tags", [])
            last_topic = tags[0] if tags else None

    # Score each problem
    scored = []
    for p in unsolved:
        score = 0.0
        tags = p.get("tags", [])
        difficulty = p.get("difficulty", "MEDIUM")

        scoring_tags = [t for t in tags if t not in ("interviewbit",)]
        if not scoring_tags:
            scoring_tags = tags

        # 1. Topic urgency (35% weight)
        max_urgency = 0
        for tag in scoring_tags:
            urgency = calculate_topic_urgency(brain, tag)
            max_urgency = max(max_urgency, urgency)
        score += max_urgency * 35

        # 2. Difficulty match (20% weight)
        best_match = 0
        for tag in scoring_tags:
            rec_diff = get_recommended_difficulty(brain, tag)
            if rec_diff == difficulty:
                best_match = 1.0
            elif abs(DIFF_SCORES.get(rec_diff, 2) - DIFF_SCORES.get(difficulty, 2)) == 1:
                best_match = max(best_match, 0.5)
        score += best_match * 20

        # 3. Related topic chaining (12% weight)
        if last_topic:
            related = get_related_topics(last_topic)
            for tag in scoring_tags:
                if tag in related:
                    score += 12
                    break

        # 4. Variety bonus (10% weight)
        if not any(t in recent_topics for t in scoring_tags):
            score += 10

        # 5. Batch proximity bonus (15% weight) - prefer current/next batch
        pid_batch = batch_assignments.get(p.get("_uid", p["id"]), 99)
        if pid_batch == current_batch:
            score += 15
        elif pid_batch == current_batch + 1:
            score += 8
        elif pid_batch <= current_batch + 2:
            score += 4

        # 6. Source variety (3% weight)
        if "interviewbit" in tags:
            score += 3

        # 7. Random factor (5% weight)
        score += random.uniform(0, 5)

        scored.append((score, p))

    scored.sort(key=lambda x: -x[0])

    for _, p in scored[:count]:
        p["_recommendation_type"] = "OPEN"
        results.append(p)

    return results


def complete(problem_id, time_taken_mins, felt_difficulty, brain=None):
    """
    Mark a problem as completed and update all tracking.

    Args:
        problem_id: The problem ID (e.g., "IB001" or "P001")
        time_taken_mins: How long it took in minutes
        felt_difficulty: "easy", "medium", "hard", or "failed"

    Returns:
        dict with rating changes and XP earned
    """
    # Input validation
    if not problem_id or not isinstance(problem_id, str):
        return {"error": "problem_id must be a non-empty string"}
    if not isinstance(time_taken_mins, (int, float)) or time_taken_mins <= 0:
        return {"error": f"time_taken_mins must be positive, got {time_taken_mins}"}
    if time_taken_mins > 600:
        return {"error": f"time_taken_mins={time_taken_mins} seems unrealistic (>10 hours)"}
    valid_feelings = ("easy", "medium", "hard", "failed")
    if felt_difficulty not in valid_feelings:
        return {"error": f"felt_difficulty must be one of {valid_feelings}, got '{felt_difficulty}'"}

    if brain is None:
        brain = load_brain()

    problems = load_all_problems()
    problem = None

    # Try uid match first (path-based, e.g. "step_03_arrays/3.1_easy/P001")
    for p in problems:
        if p.get("_uid") == problem_id:
            problem = p
            break

    # Fall back to raw id match (works for unique IB ids)
    if not problem:
        matches = [p for p in problems if p["id"] == problem_id]
        if len(matches) == 1:
            problem = matches[0]
        elif len(matches) > 1:
            # Ambiguous P-prefixed id - use first match but warn
            problem = matches[0]

    if not problem:
        return {"error": f"Problem {problem_id} not found"}

    uid = problem["_uid"]
    today = datetime.date.today().isoformat()
    tags = problem.get("tags", [])
    difficulty = problem.get("difficulty", "MEDIUM")

    # Reset today tracking if new day
    if brain.get("today_date") != today:
        brain["today_date"] = today
        brain["today_solved"] = []

    # Migrate old id-based entry to uid if exists
    old_key = problem["id"]
    if old_key in brain["solved_problems"] and old_key != uid:
        brain["solved_problems"][uid] = brain["solved_problems"].pop(old_key)

    # Update solved problems (keyed by uid)
    is_first_solve = uid not in brain["solved_problems"]
    if is_first_solve:
        brain["solved_problems"][uid] = {
            "first_solved": today,
            "times_solved": 0,
            "tags": tags,
            "difficulty": difficulty,
        }

    brain["solved_problems"][uid]["last_solved"] = today
    brain["solved_problems"][uid]["last_time_mins"] = time_taken_mins
    brain["solved_problems"][uid]["last_felt"] = felt_difficulty
    brain["solved_problems"][uid]["times_solved"] += 1

    # Track today
    if uid not in brain["today_solved"]:
        brain["today_solved"].append(uid)

    # Update topic ratings
    rating_changes = {}
    scoring_tags = [t for t in tags if t != "interviewbit"]

    for tag in scoring_tags:
        delta = update_topic_rating(brain, tag, difficulty, time_taken_mins, felt_difficulty)
        rating_changes[tag] = delta

        # Update topic last solved
        brain["topic_last_solved"][tag] = today

        # Update solve counts
        brain["topic_solve_counts"][tag] = brain["topic_solve_counts"].get(tag, 0) + 1

    # Update difficulty comfort
    if felt_difficulty in ("easy", "medium"):
        for tag in scoring_tags:
            current_comfort = brain["difficulty_comfort"].get(tag, "EASY")
            if DIFF_SCORES.get(difficulty, 0) >= DIFF_SCORES.get(current_comfort, 0):
                brain["difficulty_comfort"][tag] = difficulty

    # Calculate XP (from config)
    xp_map = CONFIG["xp_rules"]["base_xp"]
    base_xp = xp_map.get(difficulty, 10)

    # Bonus XP for speed
    speed_bonus = 0
    expected_time = {"EASY": 15, "MEDIUM": 30, "HARD": 60}[difficulty]
    if time_taken_mins <= expected_time * 0.5:
        speed_bonus = int(base_xp * 0.5)
    elif time_taken_mins <= expected_time * 0.75:
        speed_bonus = int(base_xp * 0.25)

    total_xp = base_xp + speed_bonus

    # Update progress.json (only count XP and solved on first solve)
    if is_first_solve:
        _update_progress(problem_id, total_xp)

    # Apply streak bonus from config
    streak_bonus = 0
    try:
        with open(PROGRESS_FILE, "r", encoding="utf-8") as f:
            prog = json.load(f)
        streak = prog["player"].get("current_streak", 0)
        streak_bonuses = CONFIG["xp_rules"].get("streak_bonus", {})
        for threshold_str, bonus in sorted(streak_bonuses.items(), key=lambda x: int(x[0]), reverse=True):
            if streak >= int(threshold_str):
                streak_bonus = bonus
                break
    except (FileNotFoundError, json.JSONDecodeError, KeyError) as e:
        print(f"[brain] Warning: streak bonus calculation failed: {e}", file=sys.stderr)
    total_xp += streak_bonus

    # Update the problem's meta.json status
    _update_problem_meta(problem)

    # Add to FSRS revision system (use uid for future lookups)
    revision_card = None
    if HAS_REVISION:
        try:
            revision_card = add_to_revision(
                uid, problem["title"], difficulty,
                felt_difficulty, time_taken_mins
            )
        except Exception as e:
            print(f"[brain] Warning: failed to add to revision: {e}", file=sys.stderr)

    # Update session tracking
    today = datetime.date.today().isoformat()
    if not brain["session_history"] or brain["session_history"][-1].get("date") != today:
        brain["session_history"].append({"date": today, "problems": []})
        brain["total_sessions"] = brain.get("total_sessions", 0) + 1
    brain["session_history"][-1]["problems"].append({
        "id": uid, "time": time_taken_mins, "topic": tags[0] if tags else "unknown"
    })
    # Keep only last 30 sessions
    if len(brain["session_history"]) > 30:
        brain["session_history"] = brain["session_history"][-30:]

    save_brain(brain)

    result = {
        "problem": problem["title"],
        "difficulty": difficulty,
        "rating_changes": rating_changes,
        "xp_earned": total_xp,
        "base_xp": base_xp,
        "speed_bonus": speed_bonus,
        "streak_bonus": streak_bonus,
        "problems_solved_today": len(brain["today_solved"]),
    }

    if revision_card:
        result["next_review"] = revision_card.get("due_date", "?")
        result["stability"] = revision_card.get("stability", 0)

    return result


def _update_progress(problem_id, xp):
    """Update progress.json with solve data."""
    if not PROGRESS_FILE.exists():
        return
    try:
        with open(PROGRESS_FILE, "r", encoding="utf-8") as f:
            progress = json.load(f)

        progress["player"]["total_xp"] = progress["player"].get("total_xp", 0) + xp
        progress["global_stats"]["total_solved"] = progress["global_stats"].get("total_solved", 0) + 1

        # Update level
        total_xp = progress["player"]["total_xp"]
        thresholds = progress.get("xp_thresholds", {})
        new_level = 1
        for lvl, threshold in sorted(thresholds.items(), key=lambda x: int(x[0])):
            if total_xp >= threshold:
                new_level = int(lvl)
        progress["player"]["level"] = new_level

        # Update title
        titles = progress.get("titles", {})
        for lvl, title in sorted(titles.items(), key=lambda x: int(x[0]), reverse=True):
            if new_level >= int(lvl):
                progress["player"]["title"] = title
                break

        # Update streak
        today = datetime.date.today().isoformat()
        last_solved = progress["player"].get("last_solved_date")
        if last_solved:
            try:
                last_date = datetime.date.fromisoformat(last_solved)
                delta = (datetime.date.today() - last_date).days
                if delta == 1:
                    progress["player"]["current_streak"] += 1
                elif delta > 1:
                    progress["player"]["current_streak"] = 1
                # delta == 0 means same day, don't change streak
            except ValueError:
                progress["player"]["current_streak"] = 1
        else:
            progress["player"]["current_streak"] = 1

        progress["player"]["last_solved_date"] = today
        progress["player"]["longest_streak"] = max(
            progress["player"].get("longest_streak", 0),
            progress["player"]["current_streak"]
        )

        with open(PROGRESS_FILE, "w", encoding="utf-8") as f:
            json.dump(progress, f, indent=2)
    except (json.JSONDecodeError, KeyError, FileNotFoundError) as e:
        print(f"[brain] Warning: progress update failed: {e}", file=sys.stderr)


def _update_problem_meta(problem):
    """Mark problem as solved in its meta.json."""
    meta_path = problem.get("_meta_path")
    if not meta_path or not os.path.exists(meta_path):
        return
    try:
        with open(meta_path, "r", encoding="utf-8") as f:
            meta = json.load(f)
        meta["status"] = "SOLVED"
        meta["date_completed"] = datetime.date.today().isoformat()
        if not meta.get("date_started"):
            meta["date_started"] = datetime.date.today().isoformat()
        with open(meta_path, "w", encoding="utf-8") as f:
            json.dump(meta, f, indent=2)
    except (json.JSONDecodeError, KeyError) as e:
        print(f"[brain] Warning: meta.json update failed for {meta_path}: {e}", file=sys.stderr)


def suggest_similar(problem_id, count=5, brain=None):
    """
    Find similar problems to the given one, based on shared tags,
    same difficulty, and topic relations.
    """
    if brain is None:
        brain = load_brain()

    problems = load_all_problems()
    target = None
    for p in problems:
        if p.get("_uid") == problem_id or p["id"] == problem_id:
            target = p
            break

    if not target:
        return []

    target_tags = set(target.get("tags", []))
    target_diff = target.get("difficulty", "MEDIUM")
    target_uid = target.get("_uid", target["id"])
    solved_keys = set(brain["solved_problems"].keys())

    # Get all related tags
    related_tags = set()
    for tag in target_tags:
        related_tags.update(get_related_topics(tag))

    scored = []
    for p in problems:
        if p.get("_uid", p["id"]) == target_uid:
            continue

        ptags = set(p.get("tags", []))
        score = 0

        # Shared tags (strongest signal)
        shared = len(target_tags & ptags)
        score += shared * 10

        # Related tags
        related_shared = len(related_tags & ptags)
        score += related_shared * 3

        # Same difficulty bonus
        if p.get("difficulty") == target_diff:
            score += 5

        # Prefer unsolved
        if p.get("_uid", p["id"]) not in solved_keys and p["id"] not in solved_keys:
            score += 8

        if score > 0:
            scored.append((score, p))

    scored.sort(key=lambda x: -x[0])
    return [item[1] for item in scored[:count]]


def get_stats(brain=None):
    """Get comprehensive player stats from brain."""
    if brain is None:
        brain = load_brain()

    total_solved = len(brain["solved_problems"])
    total_problems = len(load_all_problems())

    # Topic breakdown
    topic_stats = []
    meta_tags = {"interviewbit", "implementation", "simulation", "parsing"}
    for topic, rating in sorted(brain["topic_ratings"].items(), key=lambda x: -x[1]):
        if topic in meta_tags:
            continue
        solved_in_topic = brain["topic_solve_counts"].get(topic, 0)
        level = "Beginner"
        if rating >= 1300:
            level = "Advanced"
        elif rating >= 1100:
            level = "Intermediate"
        topic_stats.append({
            "topic": topic,
            "rating": round(rating),
            "level": level,
            "solved": solved_in_topic,
            "recommended_diff": get_recommended_difficulty(brain, topic),
        })

    return {
        "total_solved": total_solved,
        "total_problems": total_problems,
        "completion_pct": round(total_solved / max(total_problems, 1) * 100, 1),
        "today_count": len(brain.get("today_solved", [])),
        "topic_stats": topic_stats,
    }


# ============================================================
# CLI INTERFACE
# ============================================================

def format_problem(p, index=None):
    """Pretty-print a problem for CLI output."""
    prefix = f"  [{index}] " if index else "  "
    source = "InterviewBit" if "interviewbit" in p.get("tags", []) else "Striver A2Z"
    diff = p.get("difficulty", "?")
    diff_colors = {"EASY": "\033[92m", "MEDIUM": "\033[93m", "HARD": "\033[91m"}
    reset = "\033[0m"
    cyan = "\033[96m"
    color = diff_colors.get(diff, "")
    tags_str = ", ".join(t for t in p.get("tags", []) if t != "interviewbit")

    # Show recommendation type if available
    rec_type = p.get("_recommendation_type", "")
    type_label = ""
    if rec_type == "REVISION":
        r_info = p.get("_revision_info", {})
        urgency = p.get("_revision_urgency", 0)
        type_label = f"  {cyan}[REVISION - recall fading, urgency: {urgency}]{reset}"
    elif rec_type == "BATCH":
        batch_id = p.get("_batch_id", "?")
        type_label = f"  {cyan}[BATCH {batch_id} - structured learning]{reset}"
    elif rec_type == "OPEN":
        type_label = f"  {cyan}[RECOMMENDED - best match for you]{reset}"

    lines = [
        f"{prefix}{color}{p['title']}{reset}{type_label}",
        f"      ID: {p['id']} | Difficulty: {color}{diff}{reset} | XP: {p.get('xp_reward', '?')}",
        f"      Source: {source} | Tags: {tags_str}",
        f"      Path: {p.get('_path', 'N/A')}",
    ]
    return "\n".join(lines)


def cli_next(args):
    brain = load_brain()
    topic = None
    if "--topic" in args:
        idx = args.index("--topic")
        if idx + 1 < len(args):
            topic = args[idx + 1]

    count = 1
    if "--count" in args:
        idx = args.index("--count")
        if idx + 1 < len(args):
            count = int(args[idx + 1])

    recs = recommend(brain=brain, topic_filter=topic, count=count)

    if not recs:
        print("\n  No problems to recommend! You might have solved them all. Amazing!")
        return

    print(f"\n{'='*60}")
    print("  RECOMMENDED PROBLEM(S)")
    print(f"{'='*60}")
    for i, p in enumerate(recs, 1):
        print(format_problem(p, i))
        print()


def cli_complete(args):
    if len(args) < 3:
        print("Usage: brain.py complete <problem_id> <time_mins> <felt_difficulty>")
        print("  felt_difficulty: easy, medium, hard, failed")
        return

    pid = args[0]
    try:
        time_mins = float(args[1])
    except ValueError:
        print(f"Invalid time: {args[1]}")
        return

    felt = args[2].lower()
    if felt not in ("easy", "medium", "hard", "failed"):
        print(f"Invalid felt difficulty: {felt}. Use: easy, medium, hard, failed")
        return

    result = complete(pid, time_mins, felt)

    if "error" in result:
        print(f"\n  Error: {result['error']}")
        return

    print(f"\n{'='*60}")
    print(f"  PROBLEM COMPLETED!")
    print(f"{'='*60}")
    print(f"  Problem:  {result['problem']}")
    print(f"  Difficulty: {result['difficulty']}")
    streak_info = f" + {result['streak_bonus']} streak" if result.get('streak_bonus', 0) > 0 else ""
    print(f"  XP Earned: +{result['xp_earned']} ({result['base_xp']} base + {result['speed_bonus']} speed{streak_info})")
    print(f"  Solved Today: {result['problems_solved_today']}")
    print()
    print("  Rating Changes:")
    for topic, delta in result["rating_changes"].items():
        arrow = "^" if delta > 0 else "v" if delta < 0 else "="
        print(f"    {topic}: {arrow} {abs(delta)}")
    print()


def cli_similar(args):
    if not args:
        print("Usage: brain.py similar <problem_id>")
        return

    pid = args[0]
    brain = load_brain()
    similar = suggest_similar(pid, brain=brain)

    if not similar:
        print(f"\n  No similar problems found for {pid}")
        return

    solved_set = set(brain.get("solved_problems", {}).keys())
    print(f"\n{'='*60}")
    print(f"  SIMILAR PROBLEMS to {pid}")
    print(f"{'='*60}")
    for i, p in enumerate(similar, 1):
        uid = p.get("_uid", p.get("id", ""))
        status = "[SOLVED]" if uid in solved_set else "[UNSOLVED]"
        print(format_problem(p, i) + f"  {status}")
        print()


def cli_stats(args):
    brain = load_brain()
    stats = get_stats(brain)

    print(f"\n{'='*60}")
    print("  BRAIN STATS")
    print(f"{'='*60}")
    print(f"  Total Solved: {stats['total_solved']} / {stats['total_problems']} ({stats['completion_pct']}%)")
    print(f"  Solved Today: {stats['today_count']}")
    print()

    if stats["topic_stats"]:
        print("  Topic Ratings:")
        print(f"  {'Topic':<25} {'Rating':>7} {'Level':<15} {'Solved':>6} {'Next Diff':<10}")
        print(f"  {'-'*70}")
        for ts in stats["topic_stats"]:
            bar_len = max(0, (ts['rating'] - 900) // 20)
            bar = "#" * min(bar_len, 25)
            print(f"  {ts['topic']:<25} {ts['rating']:>7} {ts['level']:<15} {ts['solved']:>6} {ts['recommended_diff']:<10}")
    else:
        print("  No topic data yet. Start solving problems!")
    print()


def cli_weak(args):
    brain = load_brain()
    weak = get_weak_topics(brain, top_n=10)

    if not weak:
        print("\n  No topic data yet. Solve some problems first!")
        return

    print(f"\n{'='*60}")
    print("  WEAK AREAS (lowest rated topics)")
    print(f"{'='*60}")
    for i, (topic, rating) in enumerate(weak, 1):
        urgency = calculate_topic_urgency(brain, topic)
        rec_diff = get_recommended_difficulty(brain, topic)
        print(f"  {i}. {topic:<25} Rating: {rating:>7.0f} | Urgency: {urgency:.2f} | Try: {rec_diff}")
    print()


def cli_review(args):
    """Show and handle revision queue."""
    if not HAS_REVISION:
        print("  Revision system not available.")
        return

    from revision import get_due_reviews, get_revision_stats, load_revision_state

    due = get_due_reviews(include_upcoming_days=1)
    stats = get_revision_stats(load_revision_state())

    print(f"\n{'='*60}")
    print(f"  REVISION QUEUE (FSRS Spaced Repetition)")
    print(f"{'='*60}")
    print(f"  Total cards: {stats['total_cards']} | Due: {stats['due_today']} | "
          f"Mastered: {stats['mastered']} | Retention: {stats['retention_rate']}%")
    print()

    if not due:
        print("  No reviews due! Your memory is strong.")
    else:
        for i, (urgency, card) in enumerate(due[:10], 1):
            from revision import retrievability
            elapsed = max(0, (datetime.date.today() -
                    datetime.date.fromisoformat(card["last_review"])).days)
            r_pct = retrievability(card["stability"], elapsed) * 100
            state_icon = {"learning": "L", "relearning": "!", "review": "R",
                         "mastered": "M"}.get(card["state"], "?")
            print(f"  {i}. [{state_icon}] {card['title']}")
            print(f"     Recall: {r_pct:.0f}% | Stability: {card['stability']:.1f}d | "
                  f"Due: {card['due_date']}")
    print()


def cli_batch(args):
    """Show batch status."""
    if not HAS_BATCHES:
        print("  Batch system not available.")
        return

    from batch_engine import update_batch_progress, can_unlock_next

    state = update_batch_progress()

    print(f"\n{'='*60}")
    print(f"  BATCH STATUS (Spiral Curriculum)")
    print(f"{'='*60}")
    print(f"  Current Batch: {state['current_batch']}")
    print()

    for bid_str in sorted(state["batch_progress"].keys(), key=int):
        bp = state["batch_progress"][bid_str]
        bid = int(bid_str)
        total = bp["total"]
        solved = bp["solved"]
        pct = (solved / total * 100) if total > 0 else 0
        bar_w = 15
        filled = int(bar_w * pct / 100)
        bar = "#" * filled + "-" * (bar_w - filled)

        status = "ACTIVE" if bid == state["current_batch"] else (
            "DONE" if pct >= 100 else ("OPEN" if bp.get("unlocked") else "LOCKED"))

        print(f"  {bid:>2}. [{bar}] {solved:>3}/{total:<3} {status:<8} {bp.get('name', '')}")

    unlock = can_unlock_next()
    if unlock["problems_needed"] > 0:
        print(f"\n  Solve {unlock['problems_needed']} more to unlock next batch!")
    print()


def cli_reset(args):
    if BRAIN_FILE.exists():
        os.remove(BRAIN_FILE)
    print("  Brain state reset successfully.")


def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return

    command = sys.argv[1].lower()
    args = sys.argv[2:]

    commands = {
        "next": cli_next,
        "recommend": cli_next,
        "complete": cli_complete,
        "done": cli_complete,
        "similar": cli_similar,
        "stats": cli_stats,
        "weak": cli_weak,
        "review": cli_review,
        "revision": cli_review,
        "batch": cli_batch,
        "batches": cli_batch,
        "reset": cli_reset,
    }

    if command in commands:
        commands[command](args)
    else:
        print(f"  Unknown command: {command}")
        print(f"  Available: {', '.join(commands.keys())}")


if __name__ == "__main__":
    main()
