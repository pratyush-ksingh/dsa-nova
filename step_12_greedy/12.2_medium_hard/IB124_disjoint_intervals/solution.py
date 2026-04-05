"""
Problem: Disjoint Intervals
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a list of intervals, find the maximum number of non-overlapping intervals.
Classic Activity Selection Problem solved with greedy.
Real-life use: Meeting room scheduling, CPU task scheduling, resource allocation.
"""
from typing import List, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE
# Check all 2^N subsets; find largest disjoint subset.
# Time: O(2^N * N^2)  |  Space: O(N)
# ============================================================
def brute_force(intervals: List[List[int]]) -> int:
    n = len(intervals)
    best = 0
    for mask in range(1 << n):
        chosen = [intervals[i] for i in range(n) if mask & (1 << i)]
        # Check if all chosen intervals are disjoint
        disjoint = True
        for i in range(len(chosen)):
            for j in range(i + 1, len(chosen)):
                a, b = chosen[i], chosen[j]
                if a[0] < b[1] and b[0] < a[1]:
                    disjoint = False
                    break
            if not disjoint:
                break
        if disjoint:
            best = max(best, len(chosen))
    return best


# ============================================================
# APPROACH 2: OPTIMAL
# Greedy: sort by end time. Pick earliest-ending non-overlapping interval.
# Time: O(N log N)  |  Space: O(1)
# ============================================================
def optimal(intervals: List[List[int]]) -> int:
    if not intervals:
        return 0
    sorted_ivs = sorted(intervals, key=lambda x: x[1])
    count = 1
    last_end = sorted_ivs[0][1]
    for start, end in sorted_ivs[1:]:
        if start >= last_end:
            count += 1
            last_end = end
    return count


# ============================================================
# APPROACH 3: BEST
# Same greedy but also returns the selected intervals.
# Time: O(N log N)  |  Space: O(N) for output
# ============================================================
def best(intervals: List[List[int]]) -> int:
    if not intervals:
        return 0
    sorted_ivs = sorted(intervals, key=lambda x: x[1])
    selected = [sorted_ivs[0]]
    last_end = sorted_ivs[0][1]
    for iv in sorted_ivs[1:]:
        if iv[0] >= last_end:
            selected.append(iv)
            last_end = iv[1]
    return len(selected)


def best_with_intervals(intervals: List[List[int]]) -> Tuple[int, List[List[int]]]:
    if not intervals:
        return 0, []
    sorted_ivs = sorted(intervals, key=lambda x: x[1])
    selected = [sorted_ivs[0]]
    last_end = sorted_ivs[0][1]
    for iv in sorted_ivs[1:]:
        if iv[0] >= last_end:
            selected.append(iv)
            last_end = iv[1]
    return len(selected), selected


if __name__ == "__main__":
    print("=== Disjoint Intervals ===")

    tests = [
        [[1, 4], [2, 3], [4, 6], [8, 11], [9, 12]],
        [[1, 2], [2, 3], [3, 4], [1, 3]],
        [[1, 10]],
    ]
    for ivs in tests:
        print(f"\nIntervals: {ivs}")
        print(f"  Brute  : {brute_force(ivs)}")
        print(f"  Optimal: {optimal(ivs)}")
        count, selected = best_with_intervals(ivs)
        print(f"  Best   : {count} -> selected: {selected}")
