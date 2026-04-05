"""
Problem: Sort a Nearly Sorted Array (K-Sorted Array)
Difficulty: MEDIUM | XP: 25

Given an array where each element is at most k positions away from its
sorted position, sort the array efficiently.
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE (Regular Sort)
# Time: O(n log n) | Space: O(n)
# ============================================================
def brute_force(arr: List[int], k: int) -> List[int]:
    """Just use built-in sort. Ignores the k-sorted property."""
    return sorted(arr)


# ============================================================
# APPROACH 2: OPTIMAL (Min-Heap of size k+1)
# Time: O(n log k) | Space: O(k)
# ============================================================
def optimal(arr: List[int], k: int) -> List[int]:
    """
    Use a min-heap of size k+1. The correct element for position i
    must be among the first k+1 elements (since each element is at
    most k positions away). Pop the min to place at position i, then
    push the next element.
    """
    n = len(arr)
    result = []

    # Build initial heap with first k+1 elements
    heap = arr[:k + 1]
    heapq.heapify(heap)

    # For remaining elements, pop min and push next
    for i in range(k + 1, n):
        result.append(heapq.heappop(heap))
        heapq.heappush(heap, arr[i])

    # Drain remaining elements from heap
    while heap:
        result.append(heapq.heappop(heap))

    return result


# ============================================================
# APPROACH 3: BEST (Same Min-Heap, in-place variant)
# Time: O(n log k) | Space: O(k)
# ============================================================
def best(arr: List[int], k: int) -> List[int]:
    """
    Same min-heap approach but writes back to the original array
    for an in-place feel. This is the cleanest production form.
    """
    n = len(arr)

    # Build heap from first k+1 elements
    heap = arr[:k + 1]
    heapq.heapify(heap)

    idx = 0  # Write pointer into arr

    for i in range(k + 1, n):
        arr[idx] = heapq.heappop(heap)
        heapq.heappush(heap, arr[i])
        idx += 1

    while heap:
        arr[idx] = heapq.heappop(heap)
        idx += 1

    return arr


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Sort a Nearly Sorted Array ===\n")

    test_cases = [
        ([6, 5, 3, 2, 8, 10, 9], 3, [2, 3, 5, 6, 8, 9, 10]),
        ([10, 9, 8, 7, 4, 70, 60, 50], 4, [4, 7, 8, 9, 10, 50, 60, 70]),
        ([1, 2, 3, 4, 5], 0, [1, 2, 3, 4, 5]),
        ([3, 1, 2], 2, [1, 2, 3]),
        ([2, 1], 1, [1, 2]),
        ([1], 0, [1]),
    ]

    for arr, k, expected in test_cases:
        b = brute_force(arr[:], k)
        o = optimal(arr[:], k)
        h = best(arr[:], k)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: arr={arr}, k={k}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
