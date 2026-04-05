#!/usr/bin/env python3
"""
DSA Nova - Sync Brain State from meta.json
Populates player_brain.json solved_problems from all SOLVED meta.json files.
Run: python scripts/sync_brain.py
"""

import json
import os
import glob
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
BRAIN_FILE = BASE_DIR / "player_brain.json"

def main():
    with open(BRAIN_FILE) as f:
        brain = json.load(f)

    # Find all meta.json
    pattern1 = str(BASE_DIR / "step_*" / "*" / "*" / "meta.json")
    pattern2 = str(BASE_DIR / "step_*" / "*" / "meta.json")

    meta_files = sorted(glob.glob(pattern1))
    seen = set(meta_files)
    for p in sorted(glob.glob(pattern2)):
        if p not in seen:
            try:
                with open(p) as f:
                    m = json.load(f)
                if "id" in m:
                    meta_files.append(p)
            except Exception:
                pass

    today = "2026-04-04"
    solved_problems = {}
    topic_counts = {}
    topic_last = {}

    for mf in meta_files:
        try:
            with open(mf) as f:
                meta = json.load(f)
        except Exception:
            continue

        if meta.get("status") != "SOLVED":
            continue

        # Build uid matching batch_engine.py format: step_folder/subfolder/id
        rel = os.path.relpath(mf, BASE_DIR).replace("\\", "/")
        parts = rel.split("/")
        pid = meta.get("id", "")
        if len(parts) >= 4:
            uid = f"{parts[0]}/{parts[1]}/{pid}"
        elif len(parts) >= 3:
            uid = f"{parts[0]}/{pid}"
        else:
            continue

        tags = meta.get("tags", [])
        diff = meta.get("difficulty", "MEDIUM")

        solved_problems[uid] = {
            "first_solved": today,
            "times_solved": 1,
            "tags": tags,
            "difficulty": diff,
            "last_solved": today,
            "last_time_mins": 15.0,
            "last_felt": "medium",
        }

        for tag in tags:
            topic_counts[tag] = topic_counts.get(tag, 0) + 1
            topic_last[tag] = today

    brain["solved_problems"] = solved_problems
    brain["topic_solve_counts"] = topic_counts
    brain["topic_last_solved"] = topic_last
    brain["today_date"] = today

    # Update ELO ratings
    for tag, count in topic_counts.items():
        base = 1000
        boost = min(count * 8, 400)
        brain["topic_ratings"][tag] = base + boost

    # Update difficulty comfort
    for tag, count in topic_counts.items():
        if count >= 15:
            brain["difficulty_comfort"][tag] = "HARD"
        elif count >= 8:
            brain["difficulty_comfort"][tag] = "MEDIUM"
        else:
            brain["difficulty_comfort"][tag] = "EASY"

    with open(BRAIN_FILE, "w") as f:
        json.dump(brain, f, indent=2)

    print(f"Brain sync complete:")
    print(f"  Solved problems: {len(solved_problems)}")
    print(f"  Topic counts: {len(topic_counts)} topics")
    print(f"  ELO ratings updated: {len(brain['topic_ratings'])} topics")


if __name__ == "__main__":
    main()
