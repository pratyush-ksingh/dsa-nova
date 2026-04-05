#!/usr/bin/env python3
"""
DSA Nova - Bulk Meta.json Sync (sync_meta.py)

Scans all Solution.java and solution.py files to detect which approaches
are implemented vs stubs, then updates meta.json accordingly.

Detection strategy:
  - Uses "APPROACH N:" comment markers (present in 98%+ of files)
  - Each approach section runs from its marker to the next marker (or EOF)
  - A section is a stub if it contains "TODO: implement" or "O(?)" complexity
  - Files with no APPROACH markers but real code are treated as single-approach

Run: python scripts/sync_meta.py
"""

import json
import re
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

# Regex to find approach markers (case-insensitive)
APPROACH_RE = re.compile(r'(?://|#)\s*APPROACH\s+(\d+):', re.IGNORECASE)

# Stub indicators
STUB_INDICATORS = [
    re.compile(r'//\s*TODO:\s*implement', re.IGNORECASE),
    re.compile(r'"""TODO:\s*implement', re.IGNORECASE),
    re.compile(r'#\s*TODO:\s*implement', re.IGNORECASE),
    re.compile(r'Time:\s*O\(\?\)', re.IGNORECASE),
]


def find_approach_sections(content):
    """
    Split file content into approach sections using APPROACH N: markers.
    Returns dict: {1: "section text", 2: "section text", ...}
    """
    matches = list(APPROACH_RE.finditer(content))
    if not matches:
        return {}

    sections = {}
    for i, match in enumerate(matches):
        num = int(match.group(1))
        start = match.start()
        end = matches[i + 1].start() if i + 1 < len(matches) else len(content)
        sections[num] = content[start:end]

    return sections


def is_section_stub(section_text):
    """Check if an approach section is a stub (not implemented)."""
    for pattern in STUB_INDICATORS:
        if pattern.search(section_text):
            return True
    return False


def analyze_file(filepath):
    """
    Analyze a solution file and return which approaches are implemented.
    Returns: (is_implemented, {1: True/False, 2: True/False, 3: True/False})
    """
    if not filepath.exists():
        return False, {}

    content = filepath.read_text(encoding="utf-8", errors="ignore")
    if len(content.strip()) < 50:
        return False, {}

    sections = find_approach_sections(content)

    if not sections:
        # No APPROACH markers — check if there's real code at all
        # These are single-implementation utility problems
        has_any_stub = any(p.search(content) for p in STUB_INDICATORS)
        if has_any_stub:
            return False, {}
        # Has real code but no approach markers — mark approach 1 as done
        if len(content.strip()) > 200:
            return True, {1: True}
        return False, {}

    # Check each section
    approach_status = {}
    for num, section in sections.items():
        approach_status[num] = not is_section_stub(section)

    any_implemented = any(approach_status.values())
    return any_implemented, approach_status


def map_approaches_to_meta(approach_status):
    """
    Map approach numbers to meta.json structure.
    Approach 1 -> brute_force, Approach 2 -> optimal, Approach 3 -> best
    """
    return {
        "brute_force": approach_status.get(1, False),
        "optimal": approach_status.get(2, False),
        "best": approach_status.get(3, False),
    }


def main():
    updated = 0
    already_correct = 0
    errors = 0

    for meta_path in sorted(BASE_DIR.rglob("meta.json")):
        if "templates" in str(meta_path):
            continue
        try:
            with open(meta_path, "r", encoding="utf-8") as f:
                meta = json.load(f)

            folder = meta_path.parent
            java_file = folder / "Solution.java"
            python_file = folder / "solution.py"

            java_impl, java_approaches = analyze_file(java_file)
            python_impl, python_approaches = analyze_file(python_file)

            java_mapped = map_approaches_to_meta(java_approaches)
            python_mapped = map_approaches_to_meta(python_approaches)

            any_implemented = java_impl or python_impl

            # Build new approaches_completed
            new_ap = {
                "brute_force": {
                    "java": java_mapped["brute_force"],
                    "python": python_mapped["brute_force"],
                },
                "optimal": {
                    "java": java_mapped["optimal"],
                    "python": python_mapped["optimal"],
                },
                "best": {
                    "java": java_mapped["best"],
                    "python": python_mapped["best"],
                },
            }

            new_status = "SOLVED" if any_implemented else "UNSOLVED"
            changed = False

            if new_status != meta.get("status"):
                meta["status"] = new_status
                changed = True

            if new_ap != meta.get("approaches_completed"):
                meta["approaches_completed"] = new_ap
                changed = True

            if changed:
                with open(meta_path, "w", encoding="utf-8") as f:
                    json.dump(meta, f, indent=2)
                updated += 1
            else:
                already_correct += 1

        except (json.JSONDecodeError, KeyError) as e:
            errors += 1
            continue

    print(f"Meta.json sync complete:")
    print(f"  Updated: {updated}")
    print(f"  Already correct: {already_correct}")
    if errors:
        print(f"  Errors: {errors}")

    # Print summary stats
    solved = 0
    all_three_java = 0
    all_three_python = 0
    all_three_any = 0
    for meta_path in BASE_DIR.rglob("meta.json"):
        if "templates" in str(meta_path):
            continue
        try:
            with open(meta_path) as f:
                m = json.load(f)
            if m["status"] == "SOLVED":
                solved += 1
            ap = m["approaches_completed"]
            j3 = all(ap[k]["java"] for k in ap)
            p3 = all(ap[k]["python"] for k in ap)
            a3 = all(ap[k]["java"] or ap[k]["python"] for k in ["brute_force", "optimal", "best"])
            if j3: all_three_java += 1
            if p3: all_three_python += 1
            if a3: all_three_any += 1
        except Exception:
            pass

    print(f"\n  Summary:")
    print(f"    SOLVED: {solved} / 612")
    print(f"    All 3 approaches (Java): {all_three_java}")
    print(f"    All 3 approaches (Python): {all_three_python}")
    print(f"    All 3 approaches (any lang): {all_three_any}")


if __name__ == "__main__":
    main()
