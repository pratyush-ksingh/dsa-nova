"""
Problem: Largest Element in Array
Difficulty: EASY | XP: 10

Given an array of integers, find and return the largest element.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Sort)
# Time: O(n log n) | Space: O(n) for sorted copy
# ============================================================
def brute_force(arr: List[int]) -> int:
    """Sort the array and return the last element."""
    sorted_arr = sorted(arr)
    return sorted_arr[-1]


# ============================================================
# APPROACH 2: OPTIMAL (Single Pass Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> int:
    """Track the running maximum in a single pass."""
    max_val = arr[0]
    for i in range(1, len(arr)):
        if arr[i] > max_val:
            max_val = arr[i]
    return max_val


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Largest Element in Array ===\n")

    test_cases = [
        ([3, 5, 7, 2, 8], 8),
        ([10], 10),
        ([5, 5, 5, 5], 5),
        ([1, 2, 3, 4, 5], 5),
        ([9, 7, 5, 3, 1], 9),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        status = "PASS" if b == expected and o == expected else "FAIL"
        print(f"Input: {arr}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Expected: {expected}  [{status}]\n")
