"""
Problem: Non Overlapping Intervals
LeetCode 435 | Difficulty: MEDIUM | XP: 25

Given an array of intervals, return the minimum number of intervals you need
to remove to make the rest non-overlapping.

Key Insight: Equivalent to finding the MAXIMUM number of non-overlapping
             intervals (like Activity Selection), then answer = n - max_kept.
             Greedy: sort by end time, always keep the interval that ends earliest.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  (Try all subsets)
# Time: O(2^n * n)  |  Space: O(n)
# ============================================================
def brute_force(intervals: List[List[int]]) -> int:
    """
    Generate every subset of intervals and check if it is non-overlapping.
    Track the maximum non-overlapping subset size.
    Answer = n - max_non_overlapping_size.
    Only feasible for very small n.
    """
    from itertools import combinations

    n = len(intervals)

    def is_non_overlapping(subset):
        sorted_sub = sorted(subset, key=lambda x: x[0])
        for i in range(1, len(sorted_sub)):
            if sorted_sub[i][0] < sorted_sub[i - 1][1]:
                return False
        return True

    max_kept = 0
    for size in range(n, -1, -1):
        for combo in combinations(intervals, size):
            if is_non_overlapping(list(combo)):
                max_kept = max(max_kept, size)
                break
    return n - max_kept


# ============================================================
# APPROACH 2: OPTIMAL  (Greedy — sort by end time)
# Time: O(n log n)  |  Space: O(1)
# ============================================================
def optimal(intervals: List[List[int]]) -> int:
    """
    Sort intervals by their END time.
    Greedily keep each interval whose start >= last kept end.
    This maximises the number kept (Activity Selection Theorem).
    Answer = total intervals - number kept.
    """
    if not intervals:
        return 0
    intervals.sort(key=lambda x: x[1])
    kept = 0
    last_end = float('-inf')
    for start, end in intervals:
        if start >= last_end:
            kept += 1
            last_end = end
    return len(intervals) - kept


# ============================================================
# APPROACH 3: BEST  (same greedy, count removals directly)
# Time: O(n log n)  |  Space: O(1)
# ============================================================
def best(intervals: List[List[int]]) -> int:
    """
    Equivalent greedy, but count removals directly instead of kept intervals.
    When an overlap is detected (current start < last_end), increment removals
    and keep the interval with the smaller end (to leave more room for future).
    """
    if not intervals:
        return 0
    intervals.sort(key=lambda x: x[1])
    removals = 0
    last_end = float('-inf')
    for start, end in intervals:
        if start < last_end:
            removals += 1
            # Keep the one with smaller end (already sorted so current end >= last_end;
            # last_end is already the smaller one — no update needed)
        else:
            last_end = end
    return removals


if __name__ == "__main__":
    test_cases = [
        ([[1, 2], [2, 3], [3, 4], [1, 3]], 1),
        ([[1, 2], [1, 2], [1, 2]], 2),
        ([[1, 2], [2, 3]], 0),
        ([], 0),
        ([[1, 100], [11, 22], [1, 11], [2, 12]], 2),
    ]
    print("=== Non Overlapping Intervals ===")
    for intervals, expected in test_cases:
        b   = brute_force([iv[:] for iv in intervals])
        o   = optimal([iv[:] for iv in intervals])
        bst = best([iv[:] for iv in intervals])
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  intervals={intervals} => brute={b}, optimal={o}, best={bst} "
              f"(expected {expected}) [{status}]")
