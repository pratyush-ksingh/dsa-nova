"""
Problem: K Largest Elements
InterviewBit | Difficulty: EASY | XP: 10

Given an array of N integers and a number k, find the k largest elements
and return them in descending order.

Key Insight: A min-heap of size k keeps the k largest seen so far.
             Quickselect finds the kth-largest in O(n) average.
"""
from typing import List
import heapq
import random


# ============================================================
# APPROACH 1: BRUTE FORCE  (Sort descending)
# Time: O(n log n)  |  Space: O(n) or O(1) in-place
# ============================================================
def brute_force(nums: List[int], k: int) -> List[int]:
    """
    Sort the entire array in descending order and return the first k elements.
    Simple and correct; wastes work sorting elements we don't need.
    """
    return sorted(nums, reverse=True)[:k]


# ============================================================
# APPROACH 2: OPTIMAL  (Min-Heap of size k)
# Time: O(n log k)  |  Space: O(k)
# ============================================================
def optimal(nums: List[int], k: int) -> List[int]:
    """
    Maintain a min-heap of exactly k elements.
    For each new element: if it is larger than the heap's minimum, replace.
    At the end, the heap holds the k largest elements.
    Sort descending before returning (O(k log k)).
    """
    if k <= 0:
        return []
    heap: List[int] = []
    for num in nums:
        heapq.heappush(heap, num)
        if len(heap) > k:
            heapq.heappop(heap)  # evict the current smallest
    return sorted(heap, reverse=True)


# ============================================================
# APPROACH 3: BEST  (Quickselect — O(n) average)
# Time: O(n) average, O(n^2) worst  |  Space: O(1) in-place
# ============================================================
def best(nums: List[int], k: int) -> List[int]:
    """
    Partition the array (like quicksort pivot) so that elements to the
    right of the pivot index are the k largest.
    Then sort just those k elements for the required descending order.

    Uses Lomuto partition on a copy to avoid mutating the original.
    Random pivot avoids worst-case O(n^2) on sorted input.
    """
    if k <= 0:
        return []
    arr = nums[:]
    n = len(arr)

    def partition(lo: int, hi: int) -> int:
        pivot_idx = random.randint(lo, hi)
        arr[pivot_idx], arr[hi] = arr[hi], arr[pivot_idx]
        pivot = arr[hi]
        store = lo
        for i in range(lo, hi):
            if arr[i] <= pivot:
                arr[store], arr[i] = arr[i], arr[store]
                store += 1
        arr[store], arr[hi] = arr[hi], arr[store]
        return store

    lo, hi = 0, n - 1
    target = n - k          # index of the kth largest after partitioning
    while lo < hi:
        pivot = partition(lo, hi)
        if pivot == target:
            break
        elif pivot < target:
            lo = pivot + 1
        else:
            hi = pivot - 1

    return sorted(arr[n - k:], reverse=True)


if __name__ == "__main__":
    test_cases = [
        ([3, 1, 4, 1, 5, 9, 2, 6], 3, [9, 6, 5]),
        ([1, 2, 3, 4, 5], 2, [5, 4]),
        ([7, 7, 7], 2, [7, 7]),
        ([10], 1, [10]),
    ]
    print("=== K Largest Elements ===")
    for nums, k, expected in test_cases:
        b   = brute_force(nums, k)
        o   = optimal(nums, k)
        bst = best(nums, k)
        status = "OK" if b == o == expected else "FAIL"
        # best uses randomisation so verify separately
        bst_ok = (bst == expected)
        print(f"  nums={nums} k={k} => brute={b}, optimal={o}, best={bst} "
              f"(expected {expected}) [brute/opt:{status} best:{'OK' if bst_ok else 'FAIL'}]")
