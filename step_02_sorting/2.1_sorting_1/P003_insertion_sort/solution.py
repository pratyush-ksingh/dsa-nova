"""
Problem: Insertion Sort
Difficulty: EASY | XP: 10

Key Insight: Save the key, shift larger elements right, place key in gap.
Best case O(n) for nearly sorted arrays.
"""
from typing import List, Optional
from bisect import bisect_right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Swap-Based Insertion
# Time: O(n^2) worst/avg, O(n) best  |  Space: O(1)
#
# Repeatedly swap element with its left neighbor until positioned.
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    arr = arr[:]  # work on a copy
    n = len(arr)
    for i in range(1, n):
        j = i
        while j > 0 and arr[j] < arr[j - 1]:
            arr[j], arr[j - 1] = arr[j - 1], arr[j]
            j -= 1
    return arr


# ============================================================
# APPROACH 2: OPTIMAL -- Key-and-Shift
# Time: O(n^2) worst/avg, O(n) best  |  Space: O(1)
#
# Save key, shift larger elements right, place key once.
# Fewer writes than swap-based approach.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    arr = arr[:]
    n = len(arr)
    for i in range(1, n):
        key = arr[i]
        j = i - 1

        # Shift elements > key to the right
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1

        # Place key in the gap
        arr[j + 1] = key
    return arr


# ============================================================
# APPROACH 3: BEST -- Binary Search + Shift
# Time: O(n^2) shifts, O(n log n) comparisons  |  Space: O(1)
#
# Use bisect to find insertion position, then shift.
# ============================================================
def best(arr: List[int]) -> List[int]:
    arr = arr[:]
    n = len(arr)
    for i in range(1, n):
        key = arr[i]

        # Binary search for correct position in arr[0..i-1]
        pos = bisect_right(arr, key, 0, i)

        # Shift elements arr[pos..i-1] one position right
        for j in range(i, pos, -1):
            arr[j] = arr[j - 1]

        arr[pos] = key
    return arr


if __name__ == "__main__":
    print("=== Insertion Sort ===")
    tests = [
        [12, 11, 13, 5, 6],
        [4, 3, 2, 1],
        [1, 2, 3, 4],
        [5],
    ]
    for t in tests:
        print(f"Input: {t}")
        print(f"  Brute:   {brute_force(t)}")
        print(f"  Optimal: {optimal(t)}")
        print(f"  Best:    {best(t)}")
        print()
