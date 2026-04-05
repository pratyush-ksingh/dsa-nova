"""
Problem: Merge Intervals (LeetCode 56)
Difficulty: MEDIUM | XP: 25

Given an array of intervals, merge all overlapping intervals and return
an array of the non-overlapping intervals that cover all input intervals.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Nested comparison O(n²)
# Time: O(n²)  |  Space: O(n)
# ============================================================
def brute_force(intervals: List[List[int]]) -> List[List[int]]:
    """
    Repeatedly scan the list and merge any two overlapping intervals
    until no more merges are possible.
    """
    import copy
    intervals = copy.deepcopy(intervals)

    changed = True
    while changed:
        changed = False
        new_intervals = []
        used = [False] * len(intervals)
        for i in range(len(intervals)):
            if used[i]:
                continue
            a = intervals[i]
            for j in range(i + 1, len(intervals)):
                if used[j]:
                    continue
                b = intervals[j]
                # Check overlap: a and b overlap if a[0] <= b[1] and b[0] <= a[1]
                if a[0] <= b[1] and b[0] <= a[1]:
                    a = [min(a[0], b[0]), max(a[1], b[1])]
                    used[j] = True
                    changed = True
            new_intervals.append(a)
        intervals = new_intervals

    return sorted(intervals)


# ============================================================
# APPROACH 2: OPTIMAL — Sort by start, single-pass merge O(n log n)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(intervals: List[List[int]]) -> List[List[int]]:
    """
    Sort by start time. Then iterate: if the current interval overlaps
    the last one in the result (current.start <= last.end), merge them
    by extending last.end. Otherwise, add current as a new interval.
    """
    if not intervals:
        return []

    intervals.sort(key=lambda x: x[0])
    merged = [intervals[0][:]]

    for start, end in intervals[1:]:
        if start <= merged[-1][1]:
            merged[-1][1] = max(merged[-1][1], end)
        else:
            merged.append([start, end])

    return merged


# ============================================================
# APPROACH 3: BEST — Same approach, slightly optimized with in-place tracking
# Time: O(n log n)  |  Space: O(1) extra (output array O(n))
# ============================================================
def best(intervals: List[List[int]]) -> List[List[int]]:
    """
    Sort by start. Use index pointer into result to avoid append overhead.
    Same O(n log n) but shows awareness of constant-factor optimization.
    Actually identical in Python — the insight is that after sorting,
    a single linear pass suffices.
    """
    if not intervals:
        return []

    intervals.sort()  # sorts by first element by default
    result = []
    cur_start, cur_end = intervals[0]

    for start, end in intervals[1:]:
        if start <= cur_end:
            cur_end = max(cur_end, end)
        else:
            result.append([cur_start, cur_end])
            cur_start, cur_end = start, end

    result.append([cur_start, cur_end])
    return result


if __name__ == "__main__":
    print("=== Merge Intervals ===")

    tests = [
        [[1,3],[2,6],[8,10],[15,18]],   # [[1,6],[8,10],[15,18]]
        [[1,4],[4,5]],                   # [[1,5]]
        [[1,4],[2,3]],                   # [[1,4]]
        [[1,4],[0,2],[3,5]],             # [[0,5]]
        [[1,2]],                         # [[1,2]]
    ]

    for intervals in tests:
        import copy
        print(f"Input:   {intervals}")
        print(f"  Brute:   {brute_force(copy.deepcopy(intervals))}")
        print(f"  Optimal: {optimal(copy.deepcopy(intervals))}")
        print(f"  Best:    {best(copy.deepcopy(intervals))}")
