"""Problem: Search in Bitonic Array
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

A bitonic array first increases then decreases. Find the index
of a target element. Return -1 if not found.
Real-life use: searching in unimodal datasets (elevation profiles, profit curves).
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(1)
# Linear scan
# ============================================================
def brute_force(A: List[int], target: int) -> int:
    for i, v in enumerate(A):
        if v == target:
            return i
    return -1


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log n)  |  Space: O(1)
# 1. Binary search to find peak index
# 2. Binary search (ascending) in [0..peak]
# 3. Binary search (descending) in [peak+1..n-1]
# ============================================================
def optimal(A: List[int], target: int) -> int:
    n = len(A)

    # Find peak
    lo, hi = 0, n - 1
    while lo < hi:
        mid = (lo + hi) // 2
        if A[mid] < A[mid + 1]:
            lo = mid + 1
        else:
            hi = mid
    peak = lo

    # Ascending binary search
    lo, hi = 0, peak
    while lo <= hi:
        mid = (lo + hi) // 2
        if A[mid] == target:
            return mid
        elif A[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1

    # Descending binary search
    lo, hi = peak + 1, n - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        if A[mid] == target:
            return mid
        elif A[mid] > target:
            lo = mid + 1
        else:
            hi = mid - 1

    return -1


# ============================================================
# APPROACH 3: BEST
# Time: O(log n)  |  Space: O(1)
# Same 3-phase approach, refactored into helper functions
# for cleaner structure.
# ============================================================
def best(A: List[int], target: int) -> int:
    def find_peak(arr):
        lo, hi = 0, len(arr) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if arr[mid] < arr[mid + 1]:
                lo = mid + 1
            else:
                hi = mid
        return lo

    def bs_asc(arr, lo, hi, t):
        while lo <= hi:
            mid = (lo + hi) // 2
            if arr[mid] == t: return mid
            if arr[mid] < t: lo = mid + 1
            else: hi = mid - 1
        return -1

    def bs_desc(arr, lo, hi, t):
        while lo <= hi:
            mid = (lo + hi) // 2
            if arr[mid] == t: return mid
            if arr[mid] > t: lo = mid + 1
            else: hi = mid - 1
        return -1

    peak = find_peak(A)
    res = bs_asc(A, 0, peak, target)
    return res if res != -1 else bs_desc(A, peak + 1, len(A) - 1, target)


if __name__ == "__main__":
    cases = [
        ([1, 3, 8, 12, 4, 2], 4, 4),
        ([3, 9, 10, 20, 17, 5, 1], 20, 3),
        ([5, 6, 7, 8, 9, 10, 3, 2, 1], 3, 6),
        ([10, 20, 30, 40, 50], 5, -1),
        ([1, 2, 3], 1, 0),
    ]
    print("=== Search in Bitonic Array ===")
    for A, target, exp in cases:
        b   = brute_force(A[:], target)
        o   = optimal(A[:], target)
        bst = best(A[:], target)
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"A={A}  target={target} => {ok}  EXP={exp}")
