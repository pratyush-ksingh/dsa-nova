"""
Problem: Kth Missing Positive Number (LeetCode #1539)
Difficulty: EASY | XP: 10

Find the k-th missing positive integer from a sorted array.
Key insight: missing count at index i = arr[i] - (i+1), which is monotonic.
Binary search on this derived property.
"""
from typing import List
from bisect import bisect_left


# ============================================================
# APPROACH 1: BRUTE FORCE (Count One by One)
# Time: O(n + k) | Space: O(1)
# ============================================================
def brute_force(arr: List[int], k: int) -> int:
    """Walk through 1, 2, 3, ... skipping values in arr, count until k-th missing."""
    missing = 0
    current = 0  # index into arr
    num = 0

    while True:
        num += 1
        if current < len(arr) and arr[current] == num:
            current += 1  # num exists in array, skip
        else:
            missing += 1
            if missing == k:
                return num


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on Missing Count)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(arr: List[int], k: int) -> int:
    """Binary search: missing before arr[i] = arr[i] - (i+1). Find where missing >= k."""
    lo, hi = 0, len(arr) - 1

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        missing_count = arr[mid] - (mid + 1)

        if missing_count < k:
            lo = mid + 1
        else:
            hi = mid - 1

    # lo = first index where missing count >= k
    return k + lo


# ============================================================
# APPROACH 3: BEST (Cleaner lower-bound style)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(arr: List[int], k: int) -> int:
    """Lower bound binary search on the derived missing-count function."""
    lo, hi = 0, len(arr)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if arr[mid] - (mid + 1) < k:
            lo = mid + 1
        else:
            hi = mid
    return k + lo


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Kth Missing Positive Number ===\n")

    test_cases = [
        ([2, 3, 4, 7, 11], 5, 9),
        ([1, 2, 3, 4], 2, 6),
        ([5, 6, 7, 8, 9], 3, 3),
        ([1], 1, 2),
        ([2], 1, 1),
        ([1, 3], 2, 4),
    ]

    for arr, k, expected in test_cases:
        b = brute_force(arr, k)
        o = optimal(arr, k)
        r = best(arr, k)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {arr}, k={k}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
