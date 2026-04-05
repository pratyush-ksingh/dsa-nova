"""
Problem: Bubble Sort
Difficulty: EASY | XP: 10

Sort an array using bubble sort. Implement both basic and optimized versions.
"""
from typing import List


# ============================================================
# APPROACH 1: BASIC BUBBLE SORT (No Optimization)
# Time: O(n^2) always | Space: O(1)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Basic bubble sort -- always runs all n-1 passes."""
    arr = arr[:]  # work on a copy
    n = len(arr)
    for i in range(n - 1):
        for j in range(n - 1 - i):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr


# ============================================================
# APPROACH 2: OPTIMIZED BUBBLE SORT (Early Termination)
# Time: O(n^2) worst, O(n) best | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Optimized bubble sort -- stops early if no swaps in a pass."""
    arr = arr[:]  # work on a copy
    n = len(arr)
    for i in range(n - 1):
        swapped = False
        for j in range(n - 1 - i):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swapped = True
        if not swapped:
            break  # array is already sorted
    return arr


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Bubble Sort ===\n")

    test_cases = [
        ([64, 34, 25, 12, 22, 11, 90], [11, 12, 22, 25, 34, 64, 90]),
        ([5, 1, 4, 2, 8], [1, 2, 4, 5, 8]),
        ([1, 2, 3, 4, 5], [1, 2, 3, 4, 5]),
        ([5, 4, 3, 2, 1], [1, 2, 3, 4, 5]),
        ([2, 1], [1, 2]),
        ([42], [42]),
        ([3, 3, 3], [3, 3, 3]),
        ([-2, 3, -1, 5], [-2, -1, 3, 5]),
        ([4, 2, 4, 1, 2], [1, 2, 2, 4, 4]),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        status = "PASS" if b == expected and o == expected else "FAIL"
        print(f"Input: {arr}")
        print(f"  Basic:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Expected: {expected}  [{status}]\n")
