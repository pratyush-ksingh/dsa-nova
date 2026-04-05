"""
Problem: Lower Bound
Difficulty: EASY | XP: 10

Find the lower bound (first element >= target) in a sorted array.
Return array length if no such element exists.
"""
from typing import List
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(arr: List[int], target: int) -> int:
    """Scan left to right, return first index where arr[i] >= target."""
    for i in range(len(arr)):
        if arr[i] >= target:
            return i
    return len(arr)


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search with Answer Tracking)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(arr: List[int], target: int) -> int:
    """Binary search: track the best candidate and keep searching left."""
    lo, hi = 0, len(arr) - 1
    ans = len(arr)  # default: no element qualifies
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if arr[mid] >= target:
            ans = mid       # candidate found, try to find smaller index
            hi = mid - 1
        else:
            lo = mid + 1
    return ans


# ============================================================
# APPROACH 3: BEST (Built-in bisect_left)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(arr: List[int], target: int) -> int:
    """Use Python's built-in bisect_left which computes lower bound."""
    return bisect.bisect_left(arr, target)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Lower Bound ===\n")

    test_cases = [
        ([1, 2, 2, 3, 3, 5], 2, 1),
        ([3, 5, 8, 15, 19], 9, 3),
        ([1, 2, 3], 4, 3),
        ([3, 5, 8], 1, 0),
        ([5, 5, 5, 5], 5, 0),
        ([10], 10, 0),
    ]

    for arr, target, expected in test_cases:
        b = brute_force(arr, target)
        o = optimal(arr, target)
        bl = best(arr, target)
        status = "PASS" if b == expected and o == expected and bl == expected else "FAIL"
        print(f"Input: {arr}, target={target}")
        print(f"  Brute:     {b}")
        print(f"  Optimal:   {o}")
        print(f"  Bisect:    {bl}")
        print(f"  Expected:  {expected}  [{status}]\n")
