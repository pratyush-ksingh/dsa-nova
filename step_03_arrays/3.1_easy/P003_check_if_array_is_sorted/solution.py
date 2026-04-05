"""
Problem: Check if Array is Sorted
Difficulty: EASY | XP: 10

Given an array, check if it is sorted in non-decreasing order.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Pairs)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(arr: List[int]) -> bool:
    """Check every pair (i, j) where i < j to see if arr[i] <= arr[j]."""
    n = len(arr)
    for i in range(n - 1):
        for j in range(i + 1, n):
            if arr[i] > arr[j]:
                return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL (Single Pass Adjacent Comparison)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> bool:
    """Check only adjacent pairs -- transitivity handles the rest."""
    for i in range(len(arr) - 1):
        if arr[i] > arr[i + 1]:
            return False
    return True


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Check if Array is Sorted ===\n")

    test_cases = [
        ([1, 2, 3, 4, 5], True),
        ([1, 3, 2, 4, 5], False),
        ([5, 5, 5, 5], True),
        ([42], True),
        ([2, 1], False),
        ([1, 2, 3, 1], False),
        ([5, 4, 3, 2, 1], False),
        ([-3, -1, 0, 4], True),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        status = "PASS" if b == expected and o == expected else "FAIL"
        print(f"Input: {arr}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Expected: {expected}  [{status}]\n")
