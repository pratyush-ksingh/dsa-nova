"""
Problem: Kth Largest Element in an Array (LeetCode #215)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
import heapq
import random


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sort
# Time: O(n log n)  |  Space: O(n)
# Sort and return element at index n-k.
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    nums.sort()
    return nums[len(nums) - k]


# ============================================================
# APPROACH 2: OPTIMAL -- Min-Heap of Size k
# Time: O(n log k)  |  Space: O(k)
# Maintain k largest elements. Heap root = kth largest.
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    # Python heapq is a min-heap by default
    min_heap = []

    for num in nums:
        if len(min_heap) < k:
            heapq.heappush(min_heap, num)
        elif num > min_heap[0]:
            heapq.heapreplace(min_heap, num)  # pop + push in one operation

    return min_heap[0]


# ============================================================
# APPROACH 3: BEST -- Quickselect (Randomized)
# Time: O(n) average  |  Space: O(1)
# Partition-based selection. Find element at index n-k.
# ============================================================
def best(nums: List[int], k: int) -> int:
    target = len(nums) - k

    def partition(lo, hi):
        # Random pivot to avoid O(n^2) on sorted input
        rand_idx = random.randint(lo, hi)
        nums[rand_idx], nums[hi] = nums[hi], nums[rand_idx]

        pivot = nums[hi]
        store = lo

        for i in range(lo, hi):
            if nums[i] <= pivot:
                nums[i], nums[store] = nums[store], nums[i]
                store += 1

        nums[store], nums[hi] = nums[hi], nums[store]
        return store

    lo, hi = 0, len(nums) - 1
    while lo <= hi:
        pivot_idx = partition(lo, hi)
        if pivot_idx == target:
            return nums[pivot_idx]
        elif pivot_idx < target:
            lo = pivot_idx + 1
        else:
            hi = pivot_idx - 1

    return nums[lo]


if __name__ == "__main__":
    print("=== Kth Largest Element ===")

    nums1 = [3, 2, 1, 5, 6, 4]
    print(f"Brute (k=2):   {brute_force(nums1[:], 2)}")     # 5
    print(f"Optimal (k=2): {optimal(nums1[:], 2)}")          # 5
    print(f"Best (k=2):    {best(nums1[:], 2)}")              # 5

    nums2 = [3, 2, 3, 1, 2, 4, 5, 5, 6]
    print(f"Optimal (k=4): {optimal(nums2[:], 4)}")          # 4
    print(f"Best (k=4):    {best(nums2[:], 4)}")              # 4

    # Edge: single element
    print(f"Single:        {optimal([1], 1)}")                # 1

    # Edge: all same
    print(f"All same:      {optimal([7,7,7], 2)}")            # 7
