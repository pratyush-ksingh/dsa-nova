#!/usr/bin/env python3
"""
DSA Nova - Horizontal Batch Engine (batch_engine.py)

PHILOSOPHY: Every batch covers ALL of DSA. Each successive batch is harder.
After Batch 1 alone, you have basic coverage of EVERY topic.
After Batch 6 (~316 problems), you're interview-ready (~60%).
After Batch 12, you're in the top 1-2% of candidates.

Batches:
  B1:  Foundation     (90% Easy, 10% Med)  → 8-12% interview ready
  B2:  Easy+          (75% Easy, 25% Med)  → 15-20%
  B3:  Easy-Medium    (50% Easy, 50% Med)  → 25-30%
  B4:  Medium-Light   (25% Easy, 75% Med)  → 35-42%
  B5:  Medium         (100% Med)           → 48-55%
  B6:  Medium+        (85% Med, 15% Hard)  → 58-65%  ← INFLECTION POINT
  B7:  Medium-Hard    (70% Med, 30% Hard)  → 68-74%
  B8:  Hard-Light     (50% Med, 50% Hard)  → 76-82%
  B9:  Hard           (25% Med, 75% Hard)  → 84-88%
  B10: Hard+          (10% Med, 90% Hard)  → 89-92%
  B11: Expert         (100% Hard)          → 93-95%
  B12: Master         (100% Hard top-tier) → 96-98%

Within each batch, topics are ordered by dependency:
  Math → Arrays → Strings → Sorting → Bits → SlidingWindow → BinarySearch
  → LinkedList → Stack/Queue → Recursion → Heaps → Greedy
  → Trees → BST → Graphs → DP

Usage:
    python scripts/batch_engine.py assign
    python scripts/batch_engine.py status
    python scripts/batch_engine.py batch 3
    python scripts/batch_engine.py next
    python scripts/batch_engine.py unlock
    python scripts/batch_engine.py readiness
"""

import os
import sys
import json
import math
import random
from pathlib import Path
from collections import defaultdict

sys.path.insert(0, str(Path(__file__).resolve().parent))
from shared import get_base_dir
BASE_DIR = get_base_dir()
BATCH_FILE = BASE_DIR / "batch_state.json"
BRAIN_FILE = BASE_DIR / "player_brain.json"

# ============================================================
# TOPIC ORDER (dependency-based, used for within-batch sorting)
# ============================================================
TOPIC_ORDER = {
    "math": 1, "arrays": 2, "strings": 3, "sorting": 4,
    "bit-manipulation": 5, "sliding-window": 6, "two-pointers": 6,
    "binary-search": 7, "linked-list": 8, "stack": 9, "queues": 9,
    "recursion": 10, "backtracking": 10, "heaps": 11, "greedy": 12,
    "trees": 13, "bst": 14, "graphs": 15, "bfs": 15, "dfs": 15,
    "topological-sort": 15, "shortest-path": 15, "mst": 15,
    "dp": 16,
}

# ============================================================
# BATCH BLUEPRINTS - How many from each topic per batch
# ============================================================
# Distribution table: topic -> [B1, B2, B3, ..., B12] counts
DISTRIBUTION = {
    "math":         [6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5],  # 65
    "arrays":       [5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4],  # 58
    "strings":      [3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2],  # 33
    "sorting":      [1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0],  # 7
    "bit-manipulation": [2, 2, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1],  # 18
    "sliding-window": [2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 1, 1],  # 19
    "binary-search": [3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3],  # 37
    "linked-list":  [4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3],  # 38
    "stack":        [3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3],  # 36
    "recursion":    [3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 2, 2],  # 28
    "heaps":        [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],  # 24
    "greedy":       [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2],  # 24
    "trees":        [5, 5, 5, 5, 5, 5, 5, 4, 4, 5, 4, 4],  # 56
    "bst":          [2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 2, 1],  # 17
    "graphs":       [4, 5, 5, 6, 6, 6, 6, 6, 6, 6, 5, 6],  # 67
    "dp":           [5, 6, 7, 7, 7, 7, 8, 8, 7, 7, 7, 9],  # 85
}

# Difficulty distribution per batch (% of easy, medium, hard)
BATCH_DIFFICULTY = {
    1:  (0.90, 0.10, 0.00),
    2:  (0.75, 0.25, 0.00),
    3:  (0.50, 0.50, 0.00),
    4:  (0.25, 0.75, 0.00),
    5:  (0.00, 1.00, 0.00),
    6:  (0.00, 0.85, 0.15),
    7:  (0.00, 0.70, 0.30),
    8:  (0.00, 0.50, 0.50),
    9:  (0.00, 0.25, 0.75),
    10: (0.00, 0.10, 0.90),
    11: (0.00, 0.00, 1.00),
    12: (0.00, 0.00, 1.00),
}

BATCH_NAMES = {
    1:  ("Foundation",     "ALL topics at Easy. Build your base across everything.", "8-12%"),
    2:  ("Easy+",          "Slight twists on fundamentals. ALL topics.", "15-20%"),
    3:  ("Easy-Medium",    "Two-step thinking. ALL topics getting deeper.", "25-30%"),
    4:  ("Medium-Light",   "Standard interview warm-ups. ALL topics.", "35-42%"),
    5:  ("Medium",         "Solid medium-level across ALL topics.", "48-55%"),
    6:  ("Medium+",        "Mediums with tricks + first Hard problems. INFLECTION POINT.", "58-65%"),
    7:  ("Medium-Hard",    "Multi-technique combos. ALL topics.", "68-74%"),
    8:  ("Hard-Light",     "Optimization-heavy. ALL topics at depth.", "76-82%"),
    9:  ("Hard",           "Complex constraints. ALL topics.", "84-88%"),
    10: ("Hard+",          "Advanced patterns. Few surprises left.", "89-92%"),
    11: ("Expert",         "Competition-grade. ALL topics at peak.", "93-95%"),
    12: ("Master",         "Top-tier Hard. The last stand.", "96-98%"),
}

INTERVIEW_READINESS = {
    1: 10, 2: 18, 3: 28, 4: 39, 5: 52, 6: 62,
    7: 71, 8: 79, 9: 86, 10: 91, 11: 94, 12: 97,
}


# ============================================================
# TOPIC INFERENCE FROM PATH (imported from shared module)
# ============================================================
from shared import PATH_TOPIC_MAP


def get_primary_topic(problem):
    """Get the single primary topic for batch assignment."""
    tags = problem.get("tags", [])
    meta_tags = {"interviewbit", "implementation", "simulation", "parsing"}
    meaningful = [t for t in tags if t not in meta_tags]

    # Use first meaningful tag if available
    if meaningful:
        # Map to our canonical topics
        for t in meaningful:
            if t in DISTRIBUTION:
                return t
            # Map sub-topics to main topics
            sub_map = {
                "bfs": "graphs", "dfs": "graphs", "topological-sort": "graphs",
                "shortest-path": "graphs", "mst": "graphs", "bipartite": "graphs",
                "monotonic-stack": "stack", "queues": "stack",
                "two-pointers": "sliding-window", "prefix-sum": "arrays",
                "backtracking": "recursion", "divide-conquer": "recursion",
                "trie": "trees", "segment-tree": "trees", "traversal": "trees",
                "number-theory": "math", "combinatorics": "math",
                "prime-numbers": "math", "gcd": "math", "base-conversion": "math",
                "palindrome": "strings", "pattern-matching": "strings",
                "kmp": "strings", "big-number": "strings",
                "kadane": "arrays", "prefix-suffix": "arrays",
                "bucket-sort": "sorting", "knapsack": "dp",
                "game-theory": "dp", "catalan": "dp",
                "matrix": "arrays", "interval": "greedy",
                "matrix-exponentiation": "dp", "postfix": "stack",
            }
            if t in sub_map and sub_map[t] in DISTRIBUTION:
                return sub_map[t]
        return meaningful[0] if meaningful[0] in DISTRIBUTION else "arrays"

    # Infer from path
    path = problem.get("_path", "")
    for folder_key, topic in PATH_TOPIC_MAP.items():
        if folder_key in path:
            return topic

    return "arrays"  # Ultimate fallback


def get_unique_id(problem):
    """Path-based unique ID to avoid P001 collisions across steps."""
    path = problem.get("_path", "")
    try:
        parts = Path(path).relative_to(BASE_DIR).parts
        if len(parts) >= 3:
            return f"{parts[0]}/{parts[1]}/{problem['id']}"
        elif len(parts) >= 2:
            return f"{parts[0]}/{problem['id']}"
    except (ValueError, IndexError):
        pass
    return problem.get("_meta_path", problem["id"])


def get_difficulty_rank(problem):
    """
    Get a numeric difficulty rank for sorting within a topic.
    EASY=1, MEDIUM=2, HARD=3. Uses title heuristics as tiebreaker.
    """
    d = problem.get("difficulty", "MEDIUM")
    base = {"EASY": 1, "MEDIUM": 2, "HARD": 3}.get(d, 2)
    # Problems earlier in their step tend to be easier (P001 < P010)
    pid = problem.get("id", "P999")
    try:
        num = int("".join(c for c in pid if c.isdigit()) or "50")
    except ValueError:
        num = 50
    return base * 100 + num  # e.g., EASY P003 = 103, MEDIUM P003 = 203


def load_all_problems():
    """Load all problem meta.json files with unique IDs."""
    problems = []
    for meta_path in BASE_DIR.rglob("meta.json"):
        if "templates" in str(meta_path) or "node_modules" in str(meta_path):
            continue
        try:
            with open(meta_path, "r", encoding="utf-8") as f:
                meta = json.load(f)
            meta["_path"] = str(meta_path.parent)
            meta["_meta_path"] = str(meta_path)
            meta["_uid"] = get_unique_id(meta)
            meta["_primary_topic"] = get_primary_topic(meta)
            meta["_diff_rank"] = get_difficulty_rank(meta)
            problems.append(meta)
        except (json.JSONDecodeError, KeyError):
            continue
    return problems


def load_batch_state():
    if BATCH_FILE.exists():
        with open(BATCH_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return {"current_batch": 1, "assignments": {}, "batch_progress": {}, "assigned": False}


def save_batch_state(state):
    with open(BATCH_FILE, "w", encoding="utf-8") as f:
        json.dump(state, f, indent=2)


def load_brain():
    if BRAIN_FILE.exists():
        with open(BRAIN_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return {"solved_problems": {}, "topic_ratings": {}}


# ============================================================
# CORE: ASSIGN ALL PROBLEMS TO BATCHES
# ============================================================

def assign_problems_to_batches():
    """
    The horizontal-slice algorithm:
    1. Group all problems by primary topic
    2. Sort each topic's problems by difficulty (easy first)
    3. For each batch, pick N problems from each topic (per DISTRIBUTION table)
    4. Earlier batches get easier problems, later batches get harder
    """
    problems = load_all_problems()
    state = load_batch_state()

    # Group by primary topic, sorted by difficulty
    by_topic = defaultdict(list)
    for p in problems:
        by_topic[p["_primary_topic"]].append(p)

    # Sort each topic pool: easiest first
    for topic in by_topic:
        by_topic[topic].sort(key=lambda p: p["_diff_rank"])

    # Track what's been assigned
    assigned_uids = set()
    assignments = {}  # uid -> batch_number
    batch_progress = {}

    # Assign per batch
    for batch_id in range(1, 13):
        batch_problems = []
        idx = batch_id - 1  # 0-based index into DISTRIBUTION arrays

        for topic, counts in DISTRIBUTION.items():
            if idx >= len(counts):
                continue
            target = counts[idx]

            pool = by_topic.get(topic, [])
            picked = 0
            for p in pool:
                if p["_uid"] in assigned_uids:
                    continue
                if picked >= target:
                    break
                batch_problems.append(p)
                assigned_uids.add(p["_uid"])
                assignments[p["_uid"]] = batch_id
                picked += 1

        # Sort within batch by topic order (math first, DP last)
        batch_problems.sort(key=lambda p: TOPIC_ORDER.get(p["_primary_topic"], 99))

        name, desc, readiness = BATCH_NAMES[batch_id]
        batch_progress[str(batch_id)] = {
            "total": len(batch_problems),
            "solved": 0,
            "unlocked": batch_id == 1,
            "name": f"Batch {batch_id}: {name}",
            "description": desc,
            "readiness": readiness,
        }

    # Assign any remaining (overflow) to best-fit batches
    unassigned = [p for p in problems if p["_uid"] not in assigned_uids]
    if unassigned:
        batch_sizes = defaultdict(int)
        for uid, bid in assignments.items():
            batch_sizes[bid] += 1

        for p in unassigned:
            topic = p["_primary_topic"]
            diff_rank = p["_diff_rank"]

            # Pick batch based on difficulty (harder problems go to later batches)
            if diff_rank < 150:      # Easy
                candidates = range(1, 5)
            elif diff_rank < 250:    # Medium
                candidates = range(4, 10)
            else:                    # Hard
                candidates = range(7, 13)

            best_batch = min(candidates, key=lambda b: batch_sizes.get(b, 0))
            assignments[p["_uid"]] = best_batch
            assigned_uids.add(p["_uid"])
            batch_sizes[best_batch] += 1

        # Recount
        for bid in range(1, 13):
            count = sum(1 for uid, b in assignments.items() if b == bid)
            batch_progress[str(bid)]["total"] = count

    state["assignments"] = assignments
    state["batch_progress"] = batch_progress
    state["assigned"] = True
    save_batch_state(state)
    return state


def update_batch_progress():
    """Recalculate solved counts from brain state."""
    state = load_batch_state()
    if not state["assigned"]:
        return state

    brain = load_brain()
    solved_keys = set(brain.get("solved_problems", {}).keys())
    problems = load_all_problems()
    # Build uid -> id map for backward compat with old id-based solved keys
    uid_to_id = {p["_uid"]: p["id"] for p in problems}

    for bid_str, bp in state["batch_progress"].items():
        bid = int(bid_str)
        batch_uids = [uid for uid, b in state["assignments"].items() if b == bid]
        bp["total"] = len(batch_uids)
        bp["solved"] = len([uid for uid in batch_uids
                           if uid in solved_keys or uid_to_id.get(uid, "") in solved_keys])

    # Auto-unlock logic
    current = state["current_batch"]
    bp = state["batch_progress"].get(str(current), {})
    if bp:
        total = bp["total"]
        solved = bp["solved"]
        if total > 0 and solved / total >= 0.78:
            nxt = current + 1
            if str(nxt) in state["batch_progress"]:
                state["batch_progress"][str(nxt)]["unlocked"] = True
                if solved / total >= 0.90:
                    state["current_batch"] = nxt

    save_batch_state(state)
    return state


def get_batch_problems(batch_id):
    """Get all problems assigned to a batch, ordered by topic flow."""
    state = load_batch_state()
    if not state["assigned"]:
        return []

    problems = load_all_problems()
    problem_map = {p["_uid"]: p for p in problems}
    batch_uids = [uid for uid, bid in state["assignments"].items() if bid == batch_id]
    result = [problem_map[uid] for uid in batch_uids if uid in problem_map]

    # Sort: topic order first, then difficulty within topic
    result.sort(key=lambda p: (TOPIC_ORDER.get(p["_primary_topic"], 99), p["_diff_rank"]))
    return result


def get_next_from_batch(batch_id=None, brain=None):
    """Get the next unsolved problem from a batch, respecting topic flow."""
    state = load_batch_state()
    if brain is None:
        brain = load_brain()
    if batch_id is None:
        batch_id = state["current_batch"]

    problems = get_batch_problems(batch_id)
    solved_keys = set(brain.get("solved_problems", {}).keys())
    # Check by uid (handles both old id-based and new uid-based keys)
    unsolved = [p for p in problems if p["_uid"] not in solved_keys and p["id"] not in solved_keys]

    if not unsolved:
        return None

    # Return first unsolved in topic-order (natural progression)
    return unsolved[0]


def can_unlock_next():
    """Check unlock requirements for next batch."""
    state = load_batch_state()
    brain = load_brain()
    current = state["current_batch"]
    bp = state["batch_progress"].get(str(current), {})

    total = bp.get("total", 0)
    solved = bp.get("solved", 0)
    pct = (solved / total * 100) if total > 0 else 0
    needed = max(0, math.ceil(total * 0.78) - solved)

    nxt = current + 1
    nxt_info = state["batch_progress"].get(str(nxt))

    return {
        "current_batch": current,
        "current_name": bp.get("name", f"Batch {current}"),
        "progress_pct": round(pct, 1),
        "solved": solved,
        "total": total,
        "problems_needed": needed,
        "next_batch": nxt,
        "next_unlocked": nxt_info.get("unlocked", False) if nxt_info else False,
        "next_name": nxt_info.get("name", "N/A") if nxt_info else "Complete!",
        "readiness": INTERVIEW_READINESS.get(current, 0),
    }


# ============================================================
# CLI
# ============================================================

def cli_status(args):
    state = update_batch_progress()
    brain = load_brain()
    total_solved = len(brain.get("solved_problems", {}))

    # Calculate current readiness
    current = state["current_batch"]
    readiness = INTERVIEW_READINESS.get(current, 0)
    # Adjust based on partial progress in current batch
    bp = state["batch_progress"].get(str(current), {})
    if bp.get("total", 0) > 0:
        partial = bp["solved"] / bp["total"]
        prev_r = INTERVIEW_READINESS.get(current - 1, 0)
        readiness = int(prev_r + (readiness - prev_r) * partial)

    print(f"\n{'='*70}")
    print(f"  DSA NOVA - BATCH STATUS")
    print(f"  Interview Readiness: ~{readiness}%")
    print(f"{'='*70}\n")

    for bid_str in sorted(state["batch_progress"].keys(), key=int):
        bp = state["batch_progress"][bid_str]
        bid = int(bid_str)
        total = bp["total"]
        solved = bp["solved"]
        pct = (solved / total * 100) if total > 0 else 0

        bar_w = 20
        filled = int(bar_w * pct / 100)
        bar = "#" * filled + "-" * (bar_w - filled)

        if bid == state["current_batch"]:
            status = ">> ACTIVE"
        elif pct >= 100:
            status = "COMPLETE"
        elif bp.get("unlocked"):
            status = "UNLOCKED"
        else:
            status = "LOCKED"

        r = INTERVIEW_READINESS.get(bid, 0)
        print(f"  B{bid:>2} [{bar}] {solved:>3}/{total:<3} ({pct:>5.1f}%) {status:<10} ~{r}% ready")
        print(f"      {bp.get('name', '')} - {bp.get('description', '')[:60]}")

    unlock = can_unlock_next()
    print()
    if unlock["problems_needed"] > 0:
        print(f"  >> Solve {unlock['problems_needed']} more to unlock next batch")
    print()


def cli_batch(args):
    if not args:
        print("Usage: batch_engine.py batch <number>")
        return

    batch_id = int(args[0])
    problems = get_batch_problems(batch_id)
    brain = load_brain()
    solved_ids = set(brain.get("solved_problems", {}).keys())

    state = load_batch_state()
    bp = state["batch_progress"].get(str(batch_id), {})

    print(f"\n{'='*70}")
    print(f"  {bp.get('name', f'Batch {batch_id}')}")
    print(f"  {bp.get('description', '')}")
    print(f"  Interview readiness after this batch: ~{INTERVIEW_READINESS.get(batch_id, 0)}%")
    print(f"{'='*70}")

    current_topic = None
    for p in problems:
        topic = p["_primary_topic"]
        if topic != current_topic:
            current_topic = topic
            print(f"\n  --- {topic.upper()} ---")

        is_solved = p["_uid"] in solved_ids or p["id"] in solved_ids
        status = "[x]" if is_solved else "[ ]"
        diff = p.get("difficulty", "?")
        print(f"    {status} {p['id']:<8} {p['title']:<45} {diff}")

    solved = len([p for p in problems if p["_uid"] in solved_ids or p["id"] in solved_ids])
    print(f"\n  Progress: {solved}/{len(problems)}")
    print()


def cli_next(args):
    state = load_batch_state()
    if not state["assigned"]:
        print("  Run: batch_engine.py assign")
        return

    batch_id = int(args[0]) if args else None
    problem = get_next_from_batch(batch_id)

    if not problem:
        print(f"  Batch complete! Run 'batch_engine.py unlock' to check next.")
        return

    bid = batch_id or state["current_batch"]
    topic = problem["_primary_topic"]
    diff = problem.get("difficulty", "?")

    print(f"\n{'='*60}")
    print(f"  NEXT FROM BATCH {bid}")
    print(f"{'='*60}")
    print(f"  {problem['title']}")
    print(f"  ID: {problem['id']} | {diff} | Topic: {topic} | XP: {problem.get('xp_reward', '?')}")
    print(f"  Path: {problem.get('_path', 'N/A')}")
    print()


def cli_unlock(args):
    update_batch_progress()
    info = can_unlock_next()

    print(f"\n{'='*60}")
    print(f"  Current: {info['current_name']} ({info['solved']}/{info['total']} = {info['progress_pct']}%)")
    print(f"  Interview readiness: ~{info['readiness']}%")

    if info["problems_needed"] > 0:
        print(f"  Solve {info['problems_needed']} more to unlock Batch {info['next_batch']}")
    elif info["next_unlocked"]:
        print(f"  Batch {info['next_batch']} UNLOCKED: {info['next_name']}")
    print()


def cli_assign(args):
    problems = load_all_problems()
    print(f"  Assigning {len(problems)} problems to 12 horizontal batches...")
    print("  (Each batch covers ALL of DSA at increasing difficulty)")
    state = assign_problems_to_batches()
    total = len(state["assignments"])
    print(f"  Done! {total} problems assigned.\n")

    for bid_str in sorted(state["batch_progress"].keys(), key=int):
        bp = state["batch_progress"][bid_str]
        r = INTERVIEW_READINESS.get(int(bid_str), 0)
        print(f"  B{bid_str:>2}: {bp['total']:>3} problems | {bp['name']:<35} | ~{r}% ready")
    print()


def cli_readiness(args):
    """Show the interview readiness curve."""
    state = update_batch_progress()
    brain = load_brain()
    total_solved = len(brain.get("solved_problems", {}))

    print(f"\n{'='*60}")
    print(f"  INTERVIEW READINESS CURVE")
    print(f"{'='*60}\n")
    print(f"  Problems solved: {total_solved}")
    print()

    for bid in range(1, 13):
        bp = state["batch_progress"].get(str(bid), {})
        r = INTERVIEW_READINESS[bid]
        solved = bp.get("solved", 0)
        total = bp.get("total", 0)
        bar = "#" * (r // 5) + "-" * (20 - r // 5)
        marker = " <<<" if bid == state["current_batch"] else ""
        print(f"  After B{bid:>2}: [{bar}] {r}% ready  ({solved}/{total} done){marker}")

    print(f"\n  Key milestones:")
    print(f"    B3 = Can pass startup interviews")
    print(f"    B5 = Average FAANG candidate level")
    print(f"    B6 = INFLECTION POINT (60%+ ready, covers all patterns)")
    print(f"    B9 = Strong hire at any company")
    print(f"   B12 = Top 1-2% of all candidates")
    print()


def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return

    command = sys.argv[1].lower()
    args = sys.argv[2:]

    commands = {
        "status": cli_status, "batch": cli_batch, "next": cli_next,
        "unlock": cli_unlock, "assign": cli_assign, "readiness": cli_readiness,
    }

    if command in commands:
        commands[command](args)
    else:
        print(f"Unknown: {command}. Available: {', '.join(commands.keys())}")


if __name__ == "__main__":
    main()
