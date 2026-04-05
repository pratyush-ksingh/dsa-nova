#!/usr/bin/env python3
"""
Creates all InterviewBit problem folders with proper structure.
"""
import os
import json

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# All InterviewBit problems mapped to their target directories
PROBLEMS = {
    # Arrays -> step_03_arrays/3.2_medium (most are medium-level IB problems)
    "step_03_arrays/3.2_medium": [
        ("Pick from Both Sides", "MEDIUM", ["arrays", "interviewbit", "prefix-sum"]),
        ("Min Steps in Infinite Grid", "EASY", ["arrays", "interviewbit", "math"]),
        ("Minimum Lights to Activate", "MEDIUM", ["arrays", "interviewbit", "greedy"]),
        ("Maximum Sum Triplet", "MEDIUM", ["arrays", "interviewbit", "dp"]),
        ("Maximum Absolute Difference", "MEDIUM", ["arrays", "interviewbit", "math"]),
        ("Flip", "MEDIUM", ["arrays", "interviewbit", "kadane"]),
        ("Perfect Peak of Array", "MEDIUM", ["arrays", "interviewbit", "prefix-suffix"]),
        ("Balance Array", "MEDIUM", ["arrays", "interviewbit", "prefix-sum"]),
        ("Maximum Consecutive Gap", "HARD", ["arrays", "interviewbit", "bucket-sort"]),
        ("Largest Number", "MEDIUM", ["arrays", "interviewbit", "sorting", "strings"]),
        ("Find Permutation", "MEDIUM", ["arrays", "interviewbit", "greedy"]),
        ("Noble Integer", "EASY", ["arrays", "interviewbit", "sorting"]),
        ("Wave Array", "EASY", ["arrays", "interviewbit", "sorting"]),
        ("Hotel Bookings Possible", "MEDIUM", ["arrays", "interviewbit", "sorting"]),
        ("Max Distance", "MEDIUM", ["arrays", "interviewbit", "two-pointers"]),
        ("Maximum Unsorted Subarray", "MEDIUM", ["arrays", "interviewbit", "sorting"]),
        ("Maximum Sum Square SubMatrix", "MEDIUM", ["arrays", "interviewbit", "prefix-sum", "matrix"]),
        ("First Missing Integer", "MEDIUM", ["arrays", "interviewbit", "hashing"]),
    ],

    # Math -> step_01_basics/1.2_basic_math
    "step_01_basics/1.2_basic_math": [
        ("Prime Sum", "EASY", ["math", "interviewbit", "prime-numbers"]),
        ("Sum of Pairwise Hamming Distance", "MEDIUM", ["math", "interviewbit", "bit-manipulation"]),
        ("FizzBuzz", "EASY", ["math", "interviewbit", "implementation"]),
        ("Power of Two Integers", "MEDIUM", ["math", "interviewbit", "number-theory"]),
        ("Excel Column Number", "EASY", ["math", "interviewbit", "base-conversion"]),
        ("Excel Column Title", "EASY", ["math", "interviewbit", "base-conversion"]),
        ("Trailing Zeros in Factorial", "EASY", ["math", "interviewbit", "number-theory"]),
        ("Sorted Permutation Rank", "MEDIUM", ["math", "interviewbit", "combinatorics"]),
        ("Largest Coprime Divisor", "MEDIUM", ["math", "interviewbit", "gcd"]),
        ("Grid Unique Paths", "MEDIUM", ["math", "interviewbit", "combinatorics", "dp"]),
        ("Next Similar Number", "MEDIUM", ["math", "interviewbit", "permutation"]),
        ("Rearrange Array", "MEDIUM", ["math", "interviewbit", "arrays"]),
    ],

    # Binary Search -> step_04_binary_search
    "step_04_binary_search": [
        ("Search in Bitonic Array", "MEDIUM", ["binary-search", "interviewbit"]),
        ("WoodCutting Made Easy", "MEDIUM", ["binary-search", "interviewbit", "greedy"]),
        ("Red Zone", "HARD", ["binary-search", "interviewbit"]),
        ("Simple Queries", "HARD", ["binary-search", "interviewbit", "number-theory"]),
        ("Implement Power Function", "EASY", ["binary-search", "interviewbit", "math"]),
    ],

    # Strings -> step_05_strings/5.2_medium
    "step_05_strings/5.2_medium": [
        ("Remove Consecutive Characters", "EASY", ["strings", "interviewbit"]),
        ("Bulls and Cows", "MEDIUM", ["strings", "interviewbit", "hashing"]),
        ("Count And Say", "MEDIUM", ["strings", "interviewbit", "simulation"]),
        ("Amazing Subarrays", "EASY", ["strings", "interviewbit"]),
        ("Implement StrStr", "MEDIUM", ["strings", "interviewbit", "pattern-matching"]),
        ("Stringoholics", "HARD", ["strings", "interviewbit", "math", "kmp"]),
        ("Minimum Characters for Palindrome", "MEDIUM", ["strings", "interviewbit", "kmp", "palindrome"]),
        ("Convert to Palindrome", "MEDIUM", ["strings", "interviewbit", "two-pointers", "palindrome"]),
        ("Minimum Appends for Palindrome", "MEDIUM", ["strings", "interviewbit", "palindrome"]),
        ("Power of 2", "MEDIUM", ["strings", "interviewbit", "math", "big-number"]),
        ("Multiply Strings", "MEDIUM", ["strings", "interviewbit", "math", "big-number"]),
        ("Add Binary Strings", "EASY", ["strings", "interviewbit", "math"]),
        ("Compare Version Numbers", "MEDIUM", ["strings", "interviewbit", "parsing"]),
        ("Valid IP Addresses", "MEDIUM", ["strings", "interviewbit", "backtracking"]),
        ("Length of Last Word", "EASY", ["strings", "interviewbit"]),
        ("Zigzag String", "MEDIUM", ["strings", "interviewbit", "simulation"]),
        ("Justified Text", "HARD", ["strings", "interviewbit", "simulation"]),
        ("Pretty Json", "MEDIUM", ["strings", "interviewbit", "parsing"]),
    ],

    # Two Pointers -> step_10_sliding_window/10.3_two_pointer
    "step_10_sliding_window/10.3_two_pointer": [
        ("Pair With Given Difference", "EASY", ["two-pointers", "interviewbit", "sorting"]),
        ("Counting Triangles", "MEDIUM", ["two-pointers", "interviewbit", "sorting"]),
        ("Diffk", "EASY", ["two-pointers", "interviewbit"]),
        ("Maximum Ones After Modification", "MEDIUM", ["two-pointers", "interviewbit", "sliding-window"]),
        ("Counting Subarrays", "MEDIUM", ["two-pointers", "interviewbit"]),
        ("Array 3 Pointers", "MEDIUM", ["two-pointers", "interviewbit"]),
        ("Container With Most Water", "MEDIUM", ["two-pointers", "interviewbit", "greedy"]),
    ],

    # Linked Lists -> step_06_linked_list/6.3_medium_problems
    "step_06_linked_list/6.3_medium_problems": [
        ("Sort Binary Linked List", "EASY", ["linked-list", "interviewbit", "sorting"]),
        ("Partition List", "MEDIUM", ["linked-list", "interviewbit"]),
        ("Insertion Sort List", "MEDIUM", ["linked-list", "interviewbit", "sorting"]),
        ("Even Reverse", "MEDIUM", ["linked-list", "interviewbit"]),
        ("Swap List Nodes in Pairs", "MEDIUM", ["linked-list", "interviewbit"]),
        ("Kth Node From Middle", "EASY", ["linked-list", "interviewbit"]),
        ("Reverse Alternate K Nodes", "MEDIUM", ["linked-list", "interviewbit"]),
        ("Reorder List", "MEDIUM", ["linked-list", "interviewbit"]),
        ("List Cycle", "MEDIUM", ["linked-list", "interviewbit", "two-pointers"]),
    ],

    # Stacks & Queues -> step_09_stack_queues/9.4_implementation
    "step_09_stack_queues/9.4_implementation": [
        ("Simplify Directory Path", "MEDIUM", ["stack", "interviewbit", "strings"]),
        ("Redundant Braces", "EASY", ["stack", "interviewbit"]),
        ("MAXSPPROD", "HARD", ["stack", "interviewbit"]),
        ("Hotel Service", "MEDIUM", ["queues", "interviewbit", "sorting"]),
        ("First Non-Repeating Character in Stream", "MEDIUM", ["queues", "interviewbit", "hashing"]),
        ("Evaluate Expression", "MEDIUM", ["stack", "interviewbit", "postfix"]),
    ],

    # Hashing -> step_01_basics/1.4_basic_hashing
    "step_01_basics/1.4_basic_hashing": [
        ("Colorful Number", "EASY", ["hashing", "interviewbit"]),
        ("Largest Continuous Sequence Zero Sum", "MEDIUM", ["hashing", "interviewbit", "prefix-sum"]),
        ("First Repeating Element", "EASY", ["hashing", "interviewbit"]),
        ("Valid Sudoku", "MEDIUM", ["hashing", "interviewbit", "matrix"]),
        ("Diffk II", "EASY", ["hashing", "interviewbit"]),
        ("Pairs With Given Xor", "MEDIUM", ["hashing", "interviewbit", "bit-manipulation"]),
        ("Equal", "MEDIUM", ["hashing", "interviewbit"]),
        ("Fraction", "MEDIUM", ["hashing", "interviewbit", "math"]),
        ("Points on Straight Line", "HARD", ["hashing", "interviewbit", "math"]),
        ("An Increment Problem", "MEDIUM", ["hashing", "interviewbit"]),
        ("Two Out of Three", "EASY", ["hashing", "interviewbit"]),
        ("Substring Concatenation", "HARD", ["hashing", "interviewbit", "strings", "sliding-window"]),
        ("Subarray with B Odd Numbers", "MEDIUM", ["hashing", "interviewbit", "prefix-sum"]),
        ("Window String", "HARD", ["hashing", "interviewbit", "sliding-window", "strings"]),
    ],

    # Backtracking -> step_07_recursion_backtracking/7.3_hard_problems
    "step_07_recursion_backtracking/7.3_hard_problems": [
        ("Maximal String", "MEDIUM", ["backtracking", "interviewbit", "greedy"]),
        ("Kth Permutation Sequence", "MEDIUM", ["backtracking", "interviewbit", "math"]),
        ("Gray Code", "MEDIUM", ["backtracking", "interviewbit", "bit-manipulation"]),
        ("Letter Phone", "MEDIUM", ["backtracking", "interviewbit", "strings"]),
        ("All Possible Combinations", "EASY", ["backtracking", "interviewbit"]),
    ],

    # Trees -> step_13_binary_trees/13.2_medium
    "step_13_binary_trees/13.2_medium": [
        ("Diagonal Traversal", "MEDIUM", ["trees", "interviewbit", "traversal"]),
        ("Vertical Sum of BT", "MEDIUM", ["trees", "interviewbit", "traversal"]),
        ("Covered Uncovered Nodes", "MEDIUM", ["trees", "interviewbit"]),
        ("Cousins in Binary Tree", "MEDIUM", ["trees", "interviewbit", "bfs"]),
        ("Reverse Level Order", "EASY", ["trees", "interviewbit", "bfs"]),
        ("Populate Next Right Pointers", "MEDIUM", ["trees", "interviewbit", "bfs"]),
        ("Sum Root to Leaf Numbers", "MEDIUM", ["trees", "interviewbit", "dfs"]),
        ("Path Sum", "EASY", ["trees", "interviewbit", "dfs"]),
        ("Min Depth of Binary Tree", "EASY", ["trees", "interviewbit", "bfs", "dfs"]),
        ("Root to Leaf Paths With Sum", "MEDIUM", ["trees", "interviewbit", "dfs", "backtracking"]),
        ("Invert Binary Tree", "EASY", ["trees", "interviewbit", "recursion"]),
        ("Order of People Heights", "HARD", ["trees", "interviewbit", "segment-tree"]),
        ("Remove Half Nodes", "EASY", ["trees", "interviewbit", "recursion"]),
        ("Last Node in Complete Binary Tree", "MEDIUM", ["trees", "interviewbit", "binary-search"]),
        ("Consecutive Parent Child", "MEDIUM", ["trees", "interviewbit", "dfs"]),
        ("Maximum Edge Removal", "HARD", ["trees", "interviewbit", "dfs"]),
        ("Merge Two Binary Trees", "EASY", ["trees", "interviewbit", "recursion"]),
        ("Inorder Traversal of Cartesian Tree", "MEDIUM", ["trees", "interviewbit", "recursion"]),
    ],

    # BST -> step_14_bst/14.2_practice
    "step_14_bst/14.2_practice": [
        ("Valid BST from Preorder", "MEDIUM", ["bst", "interviewbit", "stack"]),
        ("Sorted Array to Balanced BST", "EASY", ["bst", "interviewbit", "recursion", "divide-conquer"]),
    ],

    # Heaps -> step_11_heaps/11.2_medium
    "step_11_heaps/11.2_medium": [
        ("Ways to Form Max Heap", "HARD", ["heaps", "interviewbit", "combinatorics"]),
        ("N Max Pair Combinations", "MEDIUM", ["heaps", "interviewbit"]),
        ("K Largest Elements", "EASY", ["heaps", "interviewbit"]),
        ("Profit Maximisation", "EASY", ["heaps", "interviewbit", "greedy"]),
        ("Connect Ropes", "MEDIUM", ["heaps", "interviewbit", "greedy"]),
        ("Magician and Chocolates", "EASY", ["heaps", "interviewbit", "greedy"]),
        ("Distinct Numbers in Window", "MEDIUM", ["heaps", "interviewbit", "sliding-window", "hashing"]),
    ],

    # Greedy -> step_12_greedy/12.2_medium_hard
    "step_12_greedy/12.2_medium_hard": [
        ("Highest Product", "EASY", ["greedy", "interviewbit"]),
        ("Bulbs", "EASY", ["greedy", "interviewbit"]),
        ("Disjoint Intervals", "MEDIUM", ["greedy", "interviewbit", "sorting"]),
        ("Largest Permutation", "MEDIUM", ["greedy", "interviewbit"]),
        ("Meeting Rooms", "MEDIUM", ["greedy", "interviewbit", "sorting"]),
        ("Seats", "MEDIUM", ["greedy", "interviewbit"]),
        ("Assign Mice to Holes", "EASY", ["greedy", "interviewbit", "sorting"]),
        ("Gas Station", "MEDIUM", ["greedy", "interviewbit"]),
    ],

    # Graphs -> step_15_graphs/15.2_bfs_dfs
    "step_15_graphs/15.2_bfs_dfs": [
        ("Commutable Islands", "MEDIUM", ["graphs", "interviewbit", "mst"]),
        ("Possibility of Finishing All Courses", "MEDIUM", ["graphs", "interviewbit", "topological-sort"]),
        ("Black Shapes", "MEDIUM", ["graphs", "interviewbit", "bfs", "dfs"]),
        ("Capture Regions on Board", "MEDIUM", ["graphs", "interviewbit", "bfs", "dfs"]),
        ("Convert Sorted List to BST", "MEDIUM", ["graphs", "interviewbit", "linked-list", "bst"]),
        ("Sum of Fibonacci", "HARD", ["graphs", "interviewbit", "math", "matrix-exponentiation"]),
        ("Stepping Numbers", "MEDIUM", ["graphs", "interviewbit", "bfs"]),
        ("Clone Graph", "MEDIUM", ["graphs", "interviewbit", "bfs", "dfs", "hashing"]),
        ("Level Order", "EASY", ["graphs", "interviewbit", "bfs"]),
        ("Smallest Multiple With 0 and 1", "HARD", ["graphs", "interviewbit", "bfs"]),
        ("Region in Binary Matrix", "MEDIUM", ["graphs", "interviewbit", "bfs"]),
        ("Useful Extra Edges", "HARD", ["graphs", "interviewbit", "shortest-path"]),
        ("Cycle in Undirected Graph", "MEDIUM", ["graphs", "interviewbit", "dfs"]),
        ("Cycle in Directed Graph", "MEDIUM", ["graphs", "interviewbit", "dfs"]),
        ("Two Teams", "MEDIUM", ["graphs", "interviewbit", "bipartite", "bfs"]),
        ("Valid Path", "MEDIUM", ["graphs", "interviewbit", "bfs"]),
        ("Snake Ladder Problem", "MEDIUM", ["graphs", "interviewbit", "bfs"]),
    ],

    # DP -> step_16_dynamic_programming/16.2_1d_dp (and spread across)
    "step_16_dynamic_programming/16.2_1d_dp": [
        ("Length of Longest Subsequence", "MEDIUM", ["dp", "interviewbit"]),
        ("Smallest Sequence with Given Primes", "MEDIUM", ["dp", "interviewbit", "math"]),
        ("Tiling With Dominoes", "MEDIUM", ["dp", "interviewbit"]),
        ("Paint House", "MEDIUM", ["dp", "interviewbit"]),
        ("Ways to Decode", "MEDIUM", ["dp", "interviewbit", "strings"]),
        ("Intersecting Chords", "MEDIUM", ["dp", "interviewbit", "catalan"]),
        ("Tushars Birthday Bombs", "MEDIUM", ["dp", "interviewbit", "greedy"]),
        ("Longest Arithmetic Progression", "MEDIUM", ["dp", "interviewbit", "hashing"]),
        ("N Digit Numbers with Digit Sum S", "MEDIUM", ["dp", "interviewbit"]),
        ("Ways to Color 3xN Board", "HARD", ["dp", "interviewbit"]),
        ("Kth Manhattan Distance", "HARD", ["dp", "interviewbit", "matrix"]),
        ("Coins in a Line", "MEDIUM", ["dp", "interviewbit", "game-theory"]),
        ("Egg Drop Problem", "HARD", ["dp", "interviewbit"]),
        ("Longest Valid Parentheses", "HARD", ["dp", "interviewbit", "stack"]),
        ("Kingdom War", "MEDIUM", ["dp", "interviewbit", "matrix"]),
        ("Increasing Path in Matrix", "MEDIUM", ["dp", "interviewbit", "dfs"]),
        ("Dungeon Princess", "HARD", ["dp", "interviewbit", "matrix"]),
        ("Queen Attack", "HARD", ["dp", "interviewbit", "matrix"]),
        ("Dice Throw", "MEDIUM", ["dp", "interviewbit"]),
        ("Sub Matrices with Sum Zero", "HARD", ["dp", "interviewbit", "hashing", "matrix"]),
        ("Arrange II", "HARD", ["dp", "interviewbit"]),
        ("Chain of Pairs", "MEDIUM", ["dp", "interviewbit"]),
        ("Merge Elements", "MEDIUM", ["dp", "interviewbit", "interval"]),
        ("Flip Array", "MEDIUM", ["dp", "interviewbit", "knapsack"]),
        ("Tushars Birthday Party", "MEDIUM", ["dp", "interviewbit", "knapsack"]),
        ("Equal Average Partition", "HARD", ["dp", "interviewbit", "backtracking"]),
        ("Potions", "MEDIUM", ["dp", "interviewbit"]),
        ("Unique BST II", "MEDIUM", ["dp", "interviewbit", "trees", "catalan"]),
        ("Count Permutations of BST", "HARD", ["dp", "interviewbit", "trees", "combinatorics"]),
        ("Scramble String", "HARD", ["dp", "interviewbit", "strings", "recursion"]),
        ("Regular Expression Match", "HARD", ["dp", "interviewbit", "strings"]),
        ("Interleaving Strings", "HARD", ["dp", "interviewbit", "strings"]),
    ],
}


def slugify(title):
    return title.lower().replace(" ", "_").replace("'", "").replace("-", "_").replace("(", "").replace(")", "")


def make_readme(title, step_label, difficulty, xp):
    return f"""# {title}

> **{step_label}** | **Difficulty:** {difficulty} | **XP:** {xp} | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement
TODO - Add problem description here

## Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| TODO  | TODO   | TODO        |

## Constraints
- TODO

---

## Approach 1: Brute Force
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 2: Optimal
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 3: Best
**Intuition:** TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Real-World Use Case
TODO

## Interview Tips
- TODO
"""


def make_java(title, difficulty, xp):
    return f"""/**
 * Problem: {title}
 * Difficulty: {difficulty} | XP: {xp}
 * Source: InterviewBit
 *
 * @author DSA_Nova
 */
public class Solution {{

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void bruteForce() {{
        // TODO: implement
    }}

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void optimal() {{
        // TODO: implement
    }}

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void best() {{
        // TODO: implement
    }}

    public static void main(String[] args) {{
        System.out.println("=== {title} ===");
        bruteForce();
        optimal();
        best();
    }}
}}
"""


def make_python(title, difficulty, xp):
    return f'''"""
Problem: {title}
Difficulty: {difficulty} | XP: {xp}
Source: InterviewBit
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(?)  |  Space: O(?)
# ============================================================
def brute_force():
    """TODO: implement"""
    pass


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(?)  |  Space: O(?)
# ============================================================
def optimal():
    """TODO: implement"""
    pass


# ============================================================
# APPROACH 3: BEST
# Time: O(?)  |  Space: O(?)
# ============================================================
def best():
    """TODO: implement"""
    pass


if __name__ == "__main__":
    print("=== {title} ===")
    print(f"Brute:   {{brute_force()}}")
    print(f"Optimal: {{optimal()}}")
    print(f"Best:    {{best()}}")
'''


def make_meta(ib_id, title, step, substep, difficulty, xp, tags):
    return {
        "id": ib_id,
        "title": title,
        "step": step,
        "substep": substep,
        "difficulty": difficulty,
        "xp_reward": xp,
        "platforms": {
            "interviewbit": title.lower().replace(" ", "-"),
            "leetcode": "",
            "gfg": ""
        },
        "status": "UNSOLVED",
        "approaches_completed": {
            "brute_force": {"java": False, "python": False},
            "optimal": {"java": False, "python": False},
            "best": {"java": False, "python": False}
        },
        "date_started": None,
        "date_completed": None,
        "tags": tags
    }


XP = {"EASY": 10, "MEDIUM": 25, "HARD": 50}

ib_counter = 1
total_created = 0

for folder_path, problems in PROBLEMS.items():
    full_dir = os.path.join(BASE, folder_path)
    os.makedirs(full_dir, exist_ok=True)

    # Parse step/substep from folder_path
    parts = folder_path.split("/")
    step_num = parts[0].split("_")[1]  # e.g., "03" from "step_03_arrays"
    substep = parts[-1].split("_")[0] if len(parts) > 1 else step_num  # e.g., "3.2"

    for title, difficulty, tags in problems:
        ib_id = f"IB{ib_counter:03d}"
        slug = slugify(title)
        folder_name = f"{ib_id}_{slug}"
        problem_dir = os.path.join(full_dir, folder_name)
        os.makedirs(problem_dir, exist_ok=True)

        xp = XP[difficulty]
        step_label = f"Step {step_num} | {substep}"

        # Write files
        with open(os.path.join(problem_dir, "README.md"), "w", encoding="utf-8") as f:
            f.write(make_readme(title, step_label, difficulty, xp))

        with open(os.path.join(problem_dir, "Solution.java"), "w", encoding="utf-8") as f:
            f.write(make_java(title, difficulty, xp))

        with open(os.path.join(problem_dir, "solution.py"), "w", encoding="utf-8") as f:
            f.write(make_python(title, difficulty, xp))

        meta = make_meta(ib_id, title, step_num, substep, difficulty, xp, tags)
        with open(os.path.join(problem_dir, "meta.json"), "w", encoding="utf-8") as f:
            json.dump(meta, f, indent=2)

        ib_counter += 1
        total_created += 1
        print(f"  Created: {folder_name}")

print(f"\nTotal InterviewBit problems created: {total_created}")
