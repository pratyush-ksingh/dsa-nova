"""
Problem: Kth Largest Element in Stream (LeetCode #703)
Difficulty: EASY | XP: 10
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sorted List
# Time: O(n log n) per add  |  Space: O(n)
# ============================================================
class KthLargestBrute:
    def __init__(self, k: int, nums: List[int]):
        self.k = k
        self.data = list(nums)

    def add(self, val: int) -> int:
        self.data.append(val)
        self.data.sort()
        return self.data[-self.k]


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Min-Heap of size k
# Time: O(log k) per add  |  Space: O(k)
# ============================================================
class KthLargest:
    def __init__(self, k: int, nums: List[int]):
        self.k = k
        self.min_heap: List[int] = []
        for n in nums:
            heapq.heappush(self.min_heap, n)
            if len(self.min_heap) > k:
                heapq.heappop(self.min_heap)

    def add(self, val: int) -> int:
        heapq.heappush(self.min_heap, val)
        if len(self.min_heap) > self.k:
            heapq.heappop(self.min_heap)
        return self.min_heap[0]  # root = kth largest


if __name__ == "__main__":
    print("=== Kth Largest Element in Stream ===\n")

    # --- Brute Force ---
    print("--- Approach 1: Brute Force ---")
    brute = KthLargestBrute(3, [4, 5, 8, 2])
    print(f"add(3):  {brute.add(3)}")    # 4
    print(f"add(5):  {brute.add(5)}")    # 5
    print(f"add(10): {brute.add(10)}")   # 5
    print(f"add(9):  {brute.add(9)}")    # 8
    print(f"add(4):  {brute.add(4)}")    # 8

    # --- Optimal ---
    print("\n--- Approach 2/3: Min-Heap ---")
    opt = KthLargest(3, [4, 5, 8, 2])
    print(f"add(3):  {opt.add(3)}")      # 4
    print(f"add(5):  {opt.add(5)}")      # 5
    print(f"add(10): {opt.add(10)}")     # 5
    print(f"add(9):  {opt.add(9)}")      # 8
    print(f"add(4):  {opt.add(4)}")      # 8

    # Edge: empty initial array, k=1
    print("\n--- Edge: empty nums, k=1 ---")
    edge = KthLargest(1, [])
    print(f"add(-3): {edge.add(-3)}")    # -3
    print(f"add(-2): {edge.add(-2)}")    # -2
    print(f"add(-4): {edge.add(-4)}")    # -2
    print(f"add(0):  {edge.add(0)}")     # 0
