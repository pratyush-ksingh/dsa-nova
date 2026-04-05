"""
Problem: Selection Sort
Difficulty: EASY | XP: 10
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Standard Selection Sort
# Time: O(n^2)  |  Space: O(1)
#
# For each position i, find the minimum in arr[i..n-1]
# and swap it into position i.
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    n = len(arr)
    for i in range(n - 1):
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        # Swap arr[i] and arr[min_idx]
        if min_idx != i:
            arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr


# ============================================================
# APPROACH 2: OPTIMAL -- Stable Selection Sort (shift-based)
# Time: O(n^2)  |  Space: O(1)
#
# Instead of swapping (which breaks stability), shift elements
# right and insert the minimum at position i. Preserves the
# relative order of equal elements.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    n = len(arr)
    for i in range(n - 1):
        # Find minimum in arr[i..n-1]
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j

        # Save minimum value, shift arr[i..min_idx-1] right, insert
        min_val = arr[min_idx]
        for k in range(min_idx, i, -1):
            arr[k] = arr[k - 1]
        arr[i] = min_val
    return arr


# ============================================================
# APPROACH 3: BEST -- Double-Ended Selection Sort
# Time: O(n^2)  |  Space: O(1)
#
# Each pass finds BOTH the min and max, placing the min at
# the front and the max at the back. Halves the number of
# passes (same asymptotic complexity, better constant).
# ============================================================
def best(arr: List[int]) -> List[int]:
    left = 0
    right = len(arr) - 1

    while left < right:
        min_idx = left
        max_idx = left

        # Find min and max in arr[left..right]
        for i in range(left, right + 1):
            if arr[i] < arr[min_idx]:
                min_idx = i
            if arr[i] > arr[max_idx]:
                max_idx = i

        # Place minimum at left
        arr[left], arr[min_idx] = arr[min_idx], arr[left]

        # If the maximum was at 'left', it got swapped to min_idx
        if max_idx == left:
            max_idx = min_idx

        # Place maximum at right
        arr[right], arr[max_idx] = arr[max_idx], arr[right]

        left += 1
        right -= 1

    return arr


if __name__ == "__main__":
    print("=== Selection Sort ===\n")

    tests = [
        [64, 25, 12, 22, 11],
        [5, 1, 4, 2, 8],
        [1, 2, 3],
        [3, 2, 1],
        [1],
        [],
    ]

    for test in tests:
        print(f"Input:   {test}")

        arr1 = test.copy()
        brute_force(arr1)
        print(f"Brute:   {arr1}")

        arr2 = test.copy()
        optimal(arr2)
        print(f"Optimal: {arr2}")

        arr3 = test.copy()
        best(arr3)
        print(f"Best:    {arr3}")

        print()
