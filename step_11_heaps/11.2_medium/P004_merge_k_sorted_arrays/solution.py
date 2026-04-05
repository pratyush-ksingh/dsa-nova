"""
Problem: Merge K Sorted Arrays
Difficulty: MEDIUM | XP: 25

Given k sorted arrays of size n each, merge them into a single sorted array.
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE (Concatenate and Sort)
# Time: O(nk * log(nk)) | Space: O(nk)
# ============================================================
def brute_force(arrays: List[List[int]]) -> List[int]:
    """
    Concatenate all arrays into one list, then sort it.
    Simple but ignores the fact that the inputs are already sorted.
    """
    merged = []
    for arr in arrays:
        merged.extend(arr)
    merged.sort()
    return merged


# ============================================================
# APPROACH 2: OPTIMAL (Min-Heap with index tracking)
# Time: O(nk * log k) | Space: O(k)
# ============================================================
def optimal(arrays: List[List[int]]) -> List[int]:
    """
    Use a min-heap of size k. Each heap entry is (value, array_idx, elem_idx).
    - Initialize heap with the first element of each array.
    - Repeatedly extract the minimum and push the next element from the same array.
    This leverages the sorted property of each input array.
    """
    result = []
    # heap entry: (value, array_index, element_index)
    min_heap = []

    for i, arr in enumerate(arrays):
        if arr:
            heapq.heappush(min_heap, (arr[0], i, 0))

    while min_heap:
        val, arr_idx, elem_idx = heapq.heappop(min_heap)
        result.append(val)
        next_idx = elem_idx + 1
        if next_idx < len(arrays[arr_idx]):
            heapq.heappush(min_heap, (arrays[arr_idx][next_idx], arr_idx, next_idx))

    return result


# ============================================================
# APPROACH 3: BEST (Same Min-Heap — cleaner with enumerate)
# Time: O(nk * log k) | Space: O(k)
# ============================================================
def best(arrays: List[List[int]]) -> List[int]:
    """
    Identical algorithm to Approach 2 but uses Python's heapq.merge
    under the hood concept — or we write it out cleanly.
    The key insight: each of the k arrays contributes its elements one at a time
    in sorted order, so we only ever need k elements in the heap simultaneously.
    """
    result = []
    min_heap = []

    # Push (first_value, array_idx, element_idx) for non-empty arrays
    for i, arr in enumerate(arrays):
        if arr:
            heapq.heappush(min_heap, (arr[0], i, 0))

    while min_heap:
        val, i, j = heapq.heappop(min_heap)
        result.append(val)
        if j + 1 < len(arrays[i]):
            heapq.heappush(min_heap, (arrays[i][j + 1], i, j + 1))

    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Merge K Sorted Arrays ===\n")

    test_cases = [
        (
            [[1, 3, 5, 7], [2, 4, 6, 8], [0, 9, 10, 11]],
            [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11],
        ),
        (
            [[1, 3], [2, 4], [5, 6]],
            [1, 2, 3, 4, 5, 6],
        ),
        (
            [[10], [5], [1]],
            [1, 5, 10],
        ),
        (
            [[], [1, 2], [3]],
            [1, 2, 3],
        ),
        (
            [[1]],
            [1],
        ),
    ]

    for arrays, expected in test_cases:
        b = brute_force([a[:] for a in arrays])
        o = optimal([a[:] for a in arrays])
        h = best([a[:] for a in arrays])
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input:    {arrays}")
        print(f"Brute:    {b}")
        print(f"Optimal:  {o}")
        print(f"Best:     {h}")
        print(f"Expected: {expected}  [{status}]\n")
