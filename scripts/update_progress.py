#!/usr/bin/env python3
"""
DSA Nova - Progress Engine
Scans all meta.json files, computes XP/level/badges, updates progress.json and README.
Run: python scripts/update_progress.py
"""

import os
import sys
import json
import glob
from datetime import datetime, date

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from shared import get_base_dir
BASE_DIR = str(get_base_dir())
PROGRESS_FILE = os.path.join(BASE_DIR, "progress.json")
BRAIN_FILE = os.path.join(BASE_DIR, "player_brain.json")
CONFIG_FILE = os.path.join(BASE_DIR, "config.json")


def _load_config():
    """Load config.json for XP multipliers."""
    try:
        with open(CONFIG_FILE, "r") as f:
            return json.load(f)
    except (FileNotFoundError, json.JSONDecodeError):
        return {
            "xp_rules": {
                "bonus_multipliers": {"all_three_approaches": 1.5, "both_languages": 1.3}
            }
        }


CONFIG = _load_config()


def _load_brain():
    """Load player_brain.json for solved problem tracking."""
    try:
        with open(BRAIN_FILE, "r") as f:
            return json.load(f)
    except (FileNotFoundError, json.JSONDecodeError):
        return {"solved_problems": {}, "topic_solve_counts": {}}


def load_progress():
    with open(PROGRESS_FILE, "r") as f:
        return json.load(f)


def save_progress(data):
    with open(PROGRESS_FILE, "w") as f:
        json.dump(data, f, indent=2)


def find_all_meta():
    """Find all meta.json files in the project (both P-prefixed and IB-prefixed)."""
    pattern = os.path.join(BASE_DIR, "step_*", "*", "*", "meta.json")
    # Also catch IB problems that may live directly under step folders
    pattern2 = os.path.join(BASE_DIR, "step_*", "*", "meta.json")
    results = glob.glob(pattern)
    for f in glob.glob(pattern2):
        if f not in results and "meta.json" in os.path.basename(f):
            # Only include if the parent is actually a problem folder (has an id)
            try:
                import json as _json
                with open(f, "r") as fh:
                    m = _json.load(fh)
                if "id" in m:
                    results.append(f)
            except Exception:
                pass
    return results


def compute_xp(meta):
    """Compute XP for a single problem based on completion."""
    if meta["status"] != "SOLVED":
        return 0

    base = meta["xp_reward"]
    multiplier = 1.0

    # Check all 3 approaches
    ap = meta["approaches_completed"]
    all_three = all(
        ap[k]["java"] or ap[k]["python"]
        for k in ["brute_force", "optimal", "best"]
    )
    multipliers = CONFIG["xp_rules"]["bonus_multipliers"]
    if all_three:
        multiplier *= multipliers.get("all_three_approaches", 1.5)

    # Check both languages
    any_java = any(ap[k]["java"] for k in ap)
    any_python = any(ap[k]["python"] for k in ap)
    if any_java and any_python:
        multiplier *= multipliers.get("both_languages", 1.3)

    return int(base * multiplier)


def get_level_and_title(xp, thresholds, titles):
    """Determine level and title from XP."""
    level = 1
    for lvl_str, req in sorted(thresholds.items(), key=lambda x: int(x[0])):
        if xp >= req:
            level = int(lvl_str)

    title = "Novice"
    for lvl_str, t in sorted(titles.items(), key=lambda x: int(x[0])):
        if level >= int(lvl_str):
            title = t

    return level, title


def check_badges(progress, metas_by_step, total_solved, all_metas):
    """Check and award badges based on user-solved problems (brain state)."""
    badges = {b["id"]: b for b in progress["badges"]}

    # First Blood
    badges["first_blood"]["earned"] = total_solved >= 1

    # Array Slayer - all step 3 solved by user
    step3_solved = len(metas_by_step.get("step_03", []))
    step3_total = progress["steps_summary"].get("step_03", {}).get("total", 0)
    badges["array_slayer"]["earned"] = step3_total > 0 and step3_solved >= step3_total

    # DP Demon - all step 16 solved by user
    step16_solved = len(metas_by_step.get("step_16", []))
    step16_total = progress["steps_summary"].get("step_16", {}).get("total", 0)
    badges["dp_demon"]["earned"] = step16_total > 0 and step16_solved >= step16_total

    # Graph Guru - all step 15 solved by user
    step15_solved = len(metas_by_step.get("step_15", []))
    step15_total = progress["steps_summary"].get("step_15", {}).get("total", 0)
    badges["graph_guru"]["earned"] = step15_total > 0 and step15_solved >= step15_total

    # Streak badges
    streak = progress["player"]["current_streak"]
    badges["streak_7"]["earned"] = streak >= 7
    badges["streak_30"]["earned"] = streak >= 30

    # Polyglot - 50 user-solved problems in both languages
    both_lang_count = 0
    for m in metas_by_step.get("_all_solved", []):
        ap = m["approaches_completed"]
        has_java = any(ap[k]["java"] for k in ap)
        has_python = any(ap[k]["python"] for k in ap)
        if has_java and has_python:
            both_lang_count += 1
    badges["polyglot"]["earned"] = both_lang_count >= 50

    # Triple Threat - all 3 approaches for 50 user-solved problems
    triple_count = 0
    for m in metas_by_step.get("_all_solved", []):
        ap = m["approaches_completed"]
        all_done = all(ap[k]["java"] or ap[k]["python"] for k in ["brute_force", "optimal", "best"])
        if all_done:
            triple_count += 1
    badges["triple_threat"]["earned"] = triple_count >= 50

    # Completionist
    badges["completionist"]["earned"] = total_solved >= progress["global_stats"]["total_problems"]

    progress["badges"] = list(badges.values())


def update_streak(progress):
    """Update streak based on last solved date. Does NOT reset streaks that brain.py maintains."""
    last = progress["player"]["last_solved_date"]
    if not last:
        return

    today = date.today()
    last_date = datetime.strptime(last, "%Y-%m-%d").date()
    diff = (today - last_date).days

    if diff == 0:
        pass  # Same day, streak continues
    elif diff == 1:
        # Only increment if brain.py hasn't already (avoid double-counting)
        pass  # brain.py handles streak increments on complete()
    else:
        progress["player"]["current_streak"] = 0  # Broken streak

    progress["player"]["longest_streak"] = max(
        progress["player"]["longest_streak"],
        progress["player"]["current_streak"]
    )


def generate_progress_bar(solved, total, width=10):
    """Generate ASCII progress bar."""
    if total == 0:
        return "[..........]"
    filled = int((solved / total) * width)
    return "[" + "#" * filled + "." * (width - filled) + "]"


def update_readme(progress):
    """Update the Quest Map in README.md."""
    readme_path = os.path.join(BASE_DIR, "README.md")
    with open(readme_path, "r", encoding="utf-8") as f:
        content = f.read()

    player = progress["player"]
    level = player["level"]
    title = player["title"]
    xp = player["total_xp"]
    thresholds = progress["xp_thresholds"]
    next_level = str(level + 1) if str(level + 1) in thresholds else str(level)
    next_xp = thresholds.get(next_level, xp)

    total_solved = progress["global_stats"]["total_solved"]
    total_problems = progress["global_stats"]["total_problems"]

    # Update Player Card
    pct = int((total_solved / total_problems) * 100) if total_problems > 0 else 0
    bar_width = 50
    filled = int((pct / 100) * bar_width)
    global_bar = "[" + "#" * filled + "." * (bar_width - filled) + f"] {pct}% Complete"

    badges_earned = sum(1 for b in progress["badges"] if b["earned"])

    # Build new player card
    new_card = f"""## Player Card

| Stat | Value |
|------|-------|
| Level | **{level} - {title}** |
| XP | **{xp} / {next_xp}** |
| Problems Solved | **{total_solved} / {total_problems}** |
| Current Streak | **{player['current_streak']} days** |
| Badges | **{badges_earned} / {len(progress['badges'])}** |

```
{global_bar}
```"""

    # Replace player card section
    import re
    content = re.sub(
        r'## Player Card.*?```\n[^\n]*\n```',
        new_card,
        content,
        flags=re.DOTALL
    )

    # Update quest map rows
    step_names = {
        "step_01": "Basics", "step_02": "Sorting", "step_03": "Arrays",
        "step_04": "Binary Search", "step_05": "Strings", "step_06": "Linked List",
        "step_07": "Recursion & Backtracking", "step_08": "Bit Manipulation",
        "step_09": "Stack & Queues", "step_10": "Sliding Window",
        "step_11": "Heaps", "step_12": "Greedy",
        "step_13": "Binary Trees", "step_14": "BST",
        "step_15": "Graphs", "step_16": "Dynamic Programming"
    }
    step_dirs = {
        "step_01": "step_01_basics", "step_02": "step_02_sorting",
        "step_03": "step_03_arrays", "step_04": "step_04_binary_search",
        "step_05": "step_05_strings", "step_06": "step_06_linked_list",
        "step_07": "step_07_recursion_backtracking", "step_08": "step_08_bit_manipulation",
        "step_09": "step_09_stack_queues", "step_10": "step_10_sliding_window",
        "step_11": "step_11_heaps", "step_12": "step_12_greedy",
        "step_13": "step_13_binary_trees", "step_14": "step_14_bst",
        "step_15": "step_15_graphs", "step_16": "step_16_dynamic_programming"
    }

    rows = []
    for key in sorted(step_names.keys()):
        num = int(key.split("_")[1])
        summary = progress["steps_summary"].get(key, {"total": 0, "solved": 0, "xp_earned": 0})
        bar = generate_progress_bar(summary["solved"], summary["total"])
        rows.append(
            f"| {num} | [{step_names[key]}]({step_dirs[key]}/) | "
            f"{summary['total']} | {summary['solved']} | `{bar}` | {summary['xp_earned']} XP |"
        )

    quest_table = "## Topic Coverage (All 16 Topics)\n\n| # | Topic | Total | Solved | Progress | XP |\n|---|-------|-------|--------|----------|----|"
    quest_table += "\n" + "\n".join(rows)

    content = re.sub(
        r'## Topic Coverage.*?(?=\n---)',
        quest_table + "\n",
        content,
        flags=re.DOTALL
    )

    # Update Badges Showcase
    badge_info = {
        "first_blood": ("First Blood", "Solve your first problem"),
        "array_slayer": ("Array Slayer", "Complete all Array problems"),
        "dp_demon": ("DP Demon", "Complete all DP problems"),
        "graph_guru": ("Graph Guru", "Complete all Graph problems"),
        "streak_7": ("On Fire", "7-day solve streak"),
        "streak_30": ("Unstoppable", "30-day solve streak"),
        "polyglot": ("Polyglot", "50 problems in both Java + Python"),
        "triple_threat": ("Triple Threat", "All 3 approaches for 50 problems"),
        "completionist": ("A2Z Completionist", "Solve every single problem"),
    }
    badge_rows = []
    for b in progress["badges"]:
        name, req = badge_info.get(b["id"], (b["name"], ""))
        status = "Earned" if b["earned"] else "Locked"
        badge_rows.append(f"| -- | {name} | {req} | {status} |")

    badges_table = "## Badges Showcase\n\n| Badge | Name | Requirement | Status |\n|-------|------|-------------|--------|\n"
    badges_table += "\n".join(badge_rows)

    content = re.sub(
        r'## Badges Showcase.*?(?=\n---)',
        badges_table + "\n",
        content,
        flags=re.DOTALL
    )

    with open(readme_path, "w", encoding="utf-8") as f:
        f.write(content)


def main():
    progress = load_progress()
    brain = _load_brain()
    meta_files = find_all_meta()

    # Brain state is the source of truth for what the USER has solved
    brain_solved = set(brain.get("solved_problems", {}).keys())

    all_metas = []
    metas_by_step = {}  # step_key -> [meta dicts] (only user-solved)
    total_xp = 0
    total_solved = 0
    java_count = 0
    python_count = 0
    all_approaches_count = 0

    # Reset step summaries
    for key in progress["steps_summary"]:
        progress["steps_summary"][key]["solved"] = 0
        progress["steps_summary"][key]["xp_earned"] = 0

    for mf in meta_files:
        with open(mf, "r") as f:
            meta = json.load(f)

        # Determine step key and build uid (matching batch_engine format)
        rel = os.path.relpath(mf, BASE_DIR)
        parts = rel.replace(os.sep, "/").split("/")
        step_folder = parts[0]  # "step_03_arrays"
        step_key = "_".join(step_folder.split("_")[:2])  # "step_03"
        pid = meta.get("id", "")
        if len(parts) >= 4:
            uid = f"{parts[0]}/{parts[1]}/{pid}"
        elif len(parts) >= 3:
            uid = f"{parts[0]}/{pid}"
        else:
            uid = pid

        # Check if user has personally solved this (via brain.py complete)
        user_solved = uid in brain_solved

        all_metas.append(meta)

        if step_key not in metas_by_step:
            metas_by_step[step_key] = []
        # Only add to solved-by-step if user solved it
        if user_solved:
            metas_by_step[step_key].append(meta)

        if user_solved:
            xp = compute_xp(meta)
            total_xp += xp
            total_solved += 1
            if step_key in progress["steps_summary"]:
                progress["steps_summary"][step_key]["solved"] += 1
                progress["steps_summary"][step_key]["xp_earned"] += xp

            # Count languages (only for user-solved problems)
            ap = meta["approaches_completed"]
            if any(ap[k]["java"] for k in ap):
                java_count += 1
            if any(ap[k]["python"] for k in ap):
                python_count += 1
            if all(ap[k]["java"] or ap[k]["python"] for k in ["brute_force", "optimal", "best"]):
                all_approaches_count += 1

    # Update player stats
    progress["player"]["total_xp"] = total_xp
    level, title = get_level_and_title(
        total_xp, progress["xp_thresholds"], progress["titles"]
    )
    progress["player"]["level"] = level
    progress["player"]["title"] = title

    # Update global stats
    progress["global_stats"]["total_solved"] = total_solved
    progress["global_stats"]["java_solutions"] = java_count
    progress["global_stats"]["python_solutions"] = python_count
    progress["global_stats"]["problems_with_all_approaches"] = all_approaches_count

    # Update streak
    update_streak(progress)

    # Collect all user-solved metas for badge checks
    all_user_solved = []
    for step_metas in metas_by_step.values():
        all_user_solved.extend(step_metas)
    metas_by_step["_all_solved"] = all_user_solved

    # Check badges (pass metas_by_step which only contains user-solved)
    check_badges(progress, metas_by_step, total_solved, all_metas)

    # Save and update README
    save_progress(progress)
    update_readme(progress)

    # Print summary
    badges_earned = sum(1 for b in progress["badges"] if b["earned"])
    print(f"""
    ========================================
           DSA NOVA - PROGRESS UPDATE
    ========================================
    Level:    {level} - {title}
    XP:       {total_xp}
    Solved:   {total_solved} / {progress['global_stats']['total_problems']}
    Streak:   {progress['player']['current_streak']} days
    Badges:   {badges_earned} / {len(progress['badges'])}
    Java:     {java_count} solutions
    Python:   {python_count} solutions
    ========================================
    """)


if __name__ == "__main__":
    main()
