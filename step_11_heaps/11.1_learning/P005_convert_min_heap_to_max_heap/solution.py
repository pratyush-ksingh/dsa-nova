"""
Problem: Convert Min Heap to Max Heap
Difficulty: EASY | XP: 10

Given an array representing a min-heap, convert it to a max-heap in-place.
Uses bottom-up build max-heap (Floyd's algorithm).
"""
from typing import List


# ============================================================
# APPROACH 1: BOTTOM-UP BUILD MAX-HEAP (Recursive heapify)
# Time: O(n) | Space: O(log n) recursion stack
# ============================================================
def max_heapify(arr: List[int], n: int, i: int) -> None:
    """Sift down node at index i to maintain max-heap property."""
    largest = i
    left = 2 * i + 1
    right = 2 * i + 2

    if left < n and arr[left] > arr[largest]:
        largest = left
    if right < n and arr[right] > arr[largest]:
        largest = right

    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]
        max_heapify(arr, n, largest)


def convert_to_max_heap(arr: List[int]) -> None:
    """Convert min-heap array to max-heap in-place."""
    n = len(arr)
    # Start from last internal node, heapify down to root
    for i in range(n // 2 - 1, -1, -1):
        max_heapify(arr, n, i)


# ============================================================
# APPROACH 2: ITERATIVE HEAPIFY (no recursion stack)
# Time: O(n) | Space: O(1)
# ============================================================
def max_heapify_iterative(arr: List[int], n: int, i: int) -> None:
    """Iterative sift-down."""
    while True:
        largest = i
        left = 2 * i + 1
        right = 2 * i + 2

        if left < n and arr[left] > arr[largest]:
            largest = left
        if right < n and arr[right] > arr[largest]:
            largest = right

        if largest == i:
            break

        arr[i], arr[largest] = arr[largest], arr[i]
        i = largest


def convert_to_max_heap_iterative(arr: List[int]) -> None:
    """Convert min-heap array to max-heap in-place (iterative)."""
    n = len(arr)
    for i in range(n // 2 - 1, -1, -1):
        max_heapify_iterative(arr, n, i)


# ============================================================
# Helper: verify max-heap property
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
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Convert Min Heap to Max Heap ===\n")

    test_cases = [
        [1, 2, 3, 4, 5],
        [1, 5, 6, 9, 10, 8],
        [1, 2, 3],
        [7],
        [1, 1, 1, 1, 1],
    ]

    for tc in test_cases:
        # Recursive approach
        arr1 = tc[:]
        print(f"Min-Heap:  {arr1}")
        convert_to_max_heap(arr1)
        print(f"Max-Heap:  {arr1}")
        print(f"Valid Max: {is_max_heap(arr1)}")

        # Iterative approach
        arr2 = tc[:]
        convert_to_max_heap_iterative(arr2)
        print(f"Iterative: {arr2}")
        print(f"Valid Max: {is_max_heap(arr2)}\n")
