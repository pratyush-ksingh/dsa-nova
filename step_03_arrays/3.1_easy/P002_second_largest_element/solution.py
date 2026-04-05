"""
Problem: Second Largest Element
Difficulty: EASY | XP: 10

Given an array of integers, find the second largest distinct element.
Return -1 if it does not exist.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Sort)
# Time: O(n log n) | Space: O(n) for sorted copy
# ============================================================
def brute_force(arr: List[int]) -> int:
    """Sort the array. Walk backwards from the max to find the second largest."""
    n = len(arr)
    if n < 2:
        return -1

    sorted_arr = sorted(arr)
    largest = sorted_arr[-1]

    for i in range(n - 2, -1, -1):
        if sorted_arr[i] != largest:
            return sorted_arr[i]
    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Two-Pass Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> int:
    """First pass finds the max. Second pass finds the largest value below the max."""
    n = len(arr)
    if n < 2:
        return -1

    # Pass 1: find largest
    largest = arr[0]
    for i in range(1, n):
        if arr[i] > largest:
            largest = arr[i]

    # Pass 2: find second largest (strictly less than largest)
    second = -1
    for i in range(n):
        if arr[i] != largest and arr[i] > second:
            second = arr[i]

    return second


# ============================================================
# APPROACH 3: BEST (Single-Pass with Two Variables)
# Time: O(n) | Space: O(1)
# ============================================================
def best(arr: List[int]) -> int:
    """Track first and second largest in a single pass."""
    n = len(arr)
    if n < 2:
        return -1

    first, second = -1, -1

    for x in arr:
        if x > first:
            second = first  # Old champion becomes silver
            first = x       # New champion
        elif x > second and x != first:
            second = x      # New silver medalist

    return second


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Second Largest Element ===\n")

    test_cases = [
        ([12, 35, 1, 10, 34, 1], 34),
        ([10, 10, 10], -1),
        ([5, 10], 5),
        ([7], -1),
        ([1, 2, 3, 4, 5], 4),
        ([5, 5, 5, 3], 3),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        be = best(arr)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input: {arr}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
