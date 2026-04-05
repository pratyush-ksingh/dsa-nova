"""
Problem: Check if Array is Heap
Difficulty: EASY | XP: 10

Given an array, determine if it represents a valid min-heap, max-heap, or neither.
For 0-based index: parent i has children at 2i+1 and 2i+2.
"""
from typing import List


# ============================================================
# Check if array is a valid Min-Heap
# Time: O(n) | Space: O(1)
# ============================================================
def is_min_heap(arr: List[int]) -> bool:
    n = len(arr)
    for i in range(n // 2):
        left = 2 * i + 1
        right = 2 * i + 2

        if left < n and arr[i] > arr[left]:
            return False
        if right < n and arr[i] > arr[right]:
            return False
    return True


# ============================================================
# Check if array is a valid Max-Heap
# Time: O(n) | Space: O(1)
# ============================================================
def is_max_heap(arr: List[int]) -> bool:
    n = len(arr)
    for i in range(n // 2):
        left = 2 * i + 1
        right = 2 * i + 2

        if left < n and arr[i] < arr[left]:
            return False
        if right < n and arr[i] < arr[right]:
            return False
    return True


# ============================================================
# Combined: returns "Min-Heap", "Max-Heap", "Both", or "Neither"
# Time: O(n) | Space: O(1)
# ============================================================
def check_heap(arr: List[int]) -> str:
    mn = is_min_heap(arr)
    mx = is_max_heap(arr)

    if mn and mx:
        return "Both"
    if mn:
        return "Min-Heap"
    if mx:
        return "Max-Heap"
    return "Neither"


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Check if Array is Heap ===\n")

    test_cases = [
        ([10, 20, 30, 25, 35], "Min-Heap"),
        ([90, 70, 60, 50, 40], "Max-Heap"),
        ([10, 50, 20, 55, 5], "Neither"),
        ([5, 5, 5, 5], "Both"),
        ([42], "Both"),
        ([1, 2], "Min-Heap"),
        ([2, 1], "Max-Heap"),
        ([1, 3, 2, 7, 5, 4, 6], "Min-Heap"),
    ]

    for arr, expected in test_cases:
        result = check_heap(arr)
        passes = result == expected
        print(f"arr = {arr}")
        print(f"  Result: {result} | Expected: {expected} | Pass: {passes}\n")
