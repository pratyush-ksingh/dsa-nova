"""
Problem: Insert Interval (LeetCode 57)
Difficulty: MEDIUM | XP: 25

Given a list of non-overlapping intervals sorted by start time, and a new
interval, insert it into the list and merge any overlapping intervals.
Return the resulting sorted list of non-overlapping intervals.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Add, Sort, Merge
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def brute_force(intervals: List[List[int]], newInterval: List[int]) -> List[List[int]]:
    """
    Simply add the new interval to the list, sort by start,
    then apply standard merge-intervals algorithm.
    Straightforward but doesn't exploit the fact that input is already sorted.
    """
    all_intervals = intervals + [newInterval]
    all_intervals.sort(key=lambda x: x[0])

    merged = [all_intervals[0]]
    for start, end in all_intervals[1:]:
        if start <= merged[-1][1]:
            # Overlaps with last interval — extend its end if needed
            merged[-1][1] = max(merged[-1][1], end)
        else:
            merged.append([start, end])
    return merged


# ============================================================
# APPROACH 2: OPTIMAL — Linear Scan (3-phase)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(intervals: List[List[int]], newInterval: List[int]) -> List[List[int]]:
    """
    Exploit the sorted order with a 3-phase scan:
    Phase 1: Add all intervals that end BEFORE newInterval starts (no overlap).
    Phase 2: Merge all intervals that overlap with newInterval.
    Phase 3: Add all intervals that start AFTER newInterval ends (no overlap).
    """
    result = []
    i = 0
    n = len(intervals)

    # Phase 1: intervals strictly before newInterval
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        i += 1

    # Phase 2: merge all overlapping intervals
    while i < n and intervals[i][0] <= newInterval[1]:
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i += 1
    result.append(newInterval)

    # Phase 3: intervals strictly after newInterval
    while i < n:
        result.append(intervals[i])
        i += 1

    return result


# ============================================================
# APPROACH 3: BEST — Same linear scan, cleaner one-pass style
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(intervals: List[List[int]], newInterval: List[int]) -> List[List[int]]:
    """
    Single-pass using explicit state: iterate all intervals and decide
    for each whether it goes before, merges into, or goes after newInterval.
    Uses the same logic as Approach 2 but written as a single loop.
    """
    result = []
    inserted = False

    for interval in intervals:
        if interval[1] < newInterval[0]:
            # Current interval ends before newInterval starts
            result.append(interval)
        elif interval[0] > newInterval[1]:
            # Current interval starts after newInterval ends
            if not inserted:
                result.append(newInterval)
                inserted = True
            result.append(interval)
        else:
            # Overlap: merge into newInterval
            newInterval[0] = min(newInterval[0], interval[0])
            newInterval[1] = max(newInterval[1], interval[1])

    if not inserted:
        result.append(newInterval)

    return result


if __name__ == "__main__":
    print("=== Insert Interval ===")

    tests = [
        ([[1, 3], [6, 9]], [2, 5]),                            # [[1,5],[6,9]]
        ([[1, 2], [3, 5], [6, 7], [8, 10], [12, 16]], [4, 8]), # [[1,2],[3,10],[12,16]]
        ([], [5, 7]),                                           # [[5,7]]
        ([[1, 5]], [2, 3]),                                     # [[1,5]]
        ([[1, 5]], [0, 0]),                                     # [[0,0],[1,5]]
    ]

    for intervals, new in tests:
        import copy
        iv = copy.deepcopy(intervals)
        nv = list(new)
        print(f"intervals={iv}, new={nv}")
        print(f"  Brute:   {brute_force(copy.deepcopy(iv), list(nv))}")
        print(f"  Optimal: {optimal(copy.deepcopy(iv), list(nv))}")
        print(f"  Best:    {best(copy.deepcopy(iv), list(nv))}")
