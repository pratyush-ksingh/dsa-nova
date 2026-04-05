"""
Problem: Merge Sort
Difficulty: MEDIUM | XP: 25

Sort an array using the Merge Sort algorithm.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Built-in Sort (baseline)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    return sorted(arr)


# ============================================================
# APPROACH 2: OPTIMAL -- Classic Top-Down Merge Sort
# Time: O(n log n)  |  Space: O(n)
# Recursively divide and merge sorted halves.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    result = arr[:]
    if len(result) <= 1:
        return result
    _merge_sort(result, 0, len(result) - 1)
    return result


def _merge_sort(arr: List[int], low: int, high: int):
    if low >= high:
        return
    mid = (low + high) // 2
    _merge_sort(arr, low, mid)
    _merge_sort(arr, mid + 1, high)
    _merge(arr, low, mid, high)


def _merge(arr: List[int], low: int, mid: int, high: int):
    temp = []
    i, j = low, mid + 1

    while i <= mid and j <= high:
        if arr[i] <= arr[j]:
            temp.append(arr[i])
            i += 1
        else:
            temp.append(arr[j])
            j += 1

    while i <= mid:
        temp.append(arr[i])
        i += 1
    while j <= high:
        temp.append(arr[j])
        j += 1

    arr[low:high + 1] = temp


# ============================================================
# APPROACH 3: BEST -- Bottom-Up Iterative Merge Sort
# Time: O(n log n)  |  Space: O(n) but no recursion stack
# Merge sub-arrays of increasing width: 1, 2, 4, 8, ...
# ============================================================
def best(arr: List[int]) -> List[int]:
    result = arr[:]
    n = len(result)
    if n <= 1:
        return result

    width = 1
    while width < n:
        left = 0
        while left < n:
            mid = min(left + width - 1, n - 1)
            right = min(left + 2 * width - 1, n - 1)
            _merge(result, left, mid, right)
            left += 2 * width
        width *= 2
    return result


if __name__ == "__main__":
    tests = [
        [5, 3, 8, 4, 2],
        [38, 27, 43, 3, 9, 82, 10],
        [1],
        [2, 1],
        [1, 2, 3, 4, 5],
    ]
    print("=== Merge Sort ===")
    for t in tests:
        print(f"Input:  {t}")
        print(f"  Brute:   {brute_force(t)}")
        print(f"  Optimal: {optimal(t)}")
        print(f"  Best:    {best(t)}")
