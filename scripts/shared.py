#!/usr/bin/env python3
"""
DSA Nova - Shared Constants (shared.py)

Single source of truth for path-based tag inference used by
brain.py, batch_engine.py, and fix_tags.py.

IMPORTANT: Keys are sorted longest-first to prevent substring collisions.
           e.g., "13.2_medium" must match before "3.2_medium".
"""

import sys
from pathlib import Path


def get_base_dir():
    """
    Get the project root directory. Handles both:
    - Normal Python execution: scripts/ is one level below project root
    - Frozen .exe (PyInstaller): exe sits in project root alongside step_* folders
    """
    if getattr(sys, "frozen", False):
        # Running as packaged .exe — exe is in project root
        return Path(sys.executable).resolve().parent
    else:
        # Running as script — scripts/ is one level below root
        return Path(__file__).resolve().parent.parent

# Map folder name fragments to tags.
# Order matters: longer/more-specific keys MUST come first so that
# "13.2_medium" is checked before "3.2_medium" (substring match).
PATH_TAGS = {
    # Step 13 - Binary Trees (must precede 3.x keys)
    "13.1_traversals": ["trees", "traversal"],
    "13.2_medium": ["trees"],
    "13.3_hard": ["trees"],
    # Step 14 - BST
    "14.1_concepts": ["bst", "trees"],
    "14.2_practice": ["bst", "trees"],
    # Step 15 - Graphs
    "15.1_learning": ["graphs"],
    "15.2_bfs_dfs": ["graphs", "bfs", "dfs"],
    "15.3_topo": ["graphs", "topological-sort"],
    "15.4_shortest": ["graphs", "shortest-path"],
    "15.5_mst": ["graphs", "mst"],
    "15.6_other": ["graphs"],
    # Step 16 - DP (must precede 6.x keys)
    "16.1_introduction": ["dp"],
    "16.2_1d": ["dp"],
    "16.3_2d": ["dp", "arrays"],
    "16.4_dp_on_subseq": ["dp", "recursion"],
    "16.4_dp_on_subsequences": ["dp", "recursion"],
    "16.5_dp_on_strings": ["dp", "strings"],
    "16.5_strings": ["dp", "strings"],
    "16.6_dp_on_stocks": ["dp", "arrays"],
    "16.6_stocks": ["dp", "arrays"],
    "16.7_dp_on_lis": ["dp"],
    "16.7_lis": ["dp"],
    "16.8_mcm": ["dp", "recursion"],
    "16.9_dp_on_squares": ["dp"],
    "16.9_squares": ["dp"],
    # Step 10 - Sliding Window (must precede 0.x if any)
    "10.1_medium": ["sliding-window"],
    "10.2_hard": ["sliding-window"],
    "10.3_two_pointer": ["two-pointers", "sliding-window"],
    # Step 11 - Heaps
    "11.1_learning": ["heaps"],
    "11.2_medium": ["heaps"],
    "11.3_hard": ["heaps"],
    # Step 12 - Greedy
    "12.1_easy": ["greedy"],
    "12.2_medium": ["greedy"],
    # Step 1 - Basics
    "1.1_logic": ["math", "logic"],
    "1.2_basic_math": ["math", "number-theory"],
    "1.3_basic_recursion": ["recursion"],
    "1.4_basic_hashing": ["hashing"],
    # Step 2 - Sorting
    "2.1_sorting": ["sorting"],
    "2.2_sorting": ["sorting"],
    # Step 3 - Arrays
    "3.1_easy": ["arrays"],
    "3.2_medium": ["arrays"],
    "3.3_hard": ["arrays"],
    # Step 4 - Binary Search
    "step_04_binary_search": ["binary-search"],
    "4.1_bs_on_1d": ["binary-search", "arrays"],
    "4.2_bs_on_answers": ["binary-search"],
    "4.3_bs_on_2d": ["binary-search", "arrays"],
    # Step 5 - Strings
    "5.1_basic": ["strings"],
    "5.2_medium": ["strings"],
    # Step 6 - Linked List
    "6.1_singly": ["linked-list"],
    "6.2_doubly": ["linked-list"],
    "6.3_medium": ["linked-list"],
    "6.4_hard": ["linked-list"],
    # Step 7 - Recursion & Backtracking
    "7.1_strong": ["recursion"],
    "7.2_subsequences": ["recursion", "backtracking"],
    "7.3_hard": ["recursion", "backtracking"],
    # Step 8 - Bit Manipulation
    "8.1_learn": ["bit-manipulation"],
    "8.2_interview": ["bit-manipulation"],
    "8.3_advanced": ["math", "bit-manipulation"],
    # Step 9 - Stack & Queues
    "9.1_learning": ["stack", "queues"],
    "9.2_prefix": ["stack", "prefix-sum"],
    "9.3_monotonic": ["stack", "monotonic-stack"],
    "9.4_implementation": ["stack", "queues"],
}

# Single-value topic map for batch_engine.py (same order guarantees)
PATH_TOPIC_MAP = {
    # Step 13+ first to avoid substring collisions
    "13.1_traversals": "trees", "13.2_medium": "trees", "13.3_hard": "trees",
    "14.1_concepts": "bst", "14.2_practice": "bst",
    "15.1_learning": "graphs", "15.2_bfs_dfs": "graphs",
    "15.3_topo_sort": "graphs", "15.4_shortest_path": "graphs",
    "15.5_mst": "graphs", "15.6_other": "graphs",
    "16.1_introduction": "dp", "16.2_1d_dp": "dp",
    "16.3_2d_3d": "dp", "16.4_dp_on_subseq": "dp",
    "16.5_dp_on_strings": "dp", "16.6_dp_on_stocks": "dp",
    "16.7_dp_on_lis": "dp", "16.8_mcm": "dp", "16.9_dp_on_squares": "dp",
    "10.1_medium": "sliding-window", "10.2_hard": "sliding-window",
    "10.3_two_pointer": "sliding-window",
    "11.1_learning": "heaps", "11.2_medium": "heaps", "11.3_hard": "heaps",
    "12.1_easy": "greedy", "12.2_medium": "greedy",
    # Steps 1-9
    "1.1_logic": "math", "1.2_basic_math": "math",
    "1.3_basic_recursion": "recursion", "1.4_basic_hashing": "math",
    "2.1_sorting": "sorting", "2.2_sorting": "sorting",
    "3.1_easy": "arrays", "3.2_medium": "arrays", "3.3_hard": "arrays",
    "4.1_bs_on_1d": "binary-search", "4.2_bs_on_answers": "binary-search",
    "4.3_bs_on_2d": "binary-search",
    "5.1_basic": "strings", "5.2_medium": "strings",
    "6.1_singly": "linked-list", "6.2_doubly": "linked-list",
    "6.3_medium": "linked-list", "6.4_hard": "linked-list",
    "7.1_strong": "recursion", "7.2_subsequences": "recursion",
    "7.3_hard": "recursion",
    "8.1_learn": "bit-manipulation", "8.2_interview": "bit-manipulation",
    "8.3_advanced": "math",
    "9.1_learning": "stack", "9.2_prefix": "stack",
    "9.3_monotonic": "stack", "9.4_implementation": "stack",
}


def infer_tags(path_str):
    """Infer tags from folder path. Keys are ordered to avoid substring collisions."""
    for key, tags in PATH_TAGS.items():
        if key in path_str:
            return list(tags)
    return []


def infer_primary_topic(path_str):
    """Infer primary topic from folder path for batch assignment."""
    for key, topic in PATH_TOPIC_MAP.items():
        if key in path_str:
            return topic
    return "arrays"  # fallback
