"""
Problem: Find Median from Data Stream
Difficulty: HARD | XP: 50

Design a data structure that supports:
- add_num(num): Add a number to the data stream.
- find_median(): Return the median of all numbers added so far.
Real-life use: Real-time analytics, streaming statistics, sensor monitoring.
"""
from typing import List
import heapq
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE
# Store all numbers in a list; sort on every find_median call.
# Time: add_num O(1), find_median O(N log N)  |  Space: O(N)
# ============================================================
class MedianFinderBrute:
    def __init__(self) -> None:
        self.nums: List[int] = []

    def add_num(self, num: int) -> None:
        self.nums.append(num)

    def find_median(self) -> float:
        s = sorted(self.nums)
        n = len(s)
        if n % 2 == 1:
            return float(s[n // 2])
        return (s[n // 2 - 1] + s[n // 2]) / 2.0


# ============================================================
# APPROACH 2: OPTIMAL
# Two heaps: max-heap (lower half) + min-heap (upper half).
# Python's heapq is min-heap, so negate values for max-heap.
# Invariant: len(lower) == len(upper) OR len(lower) == len(upper) + 1.
# Time: add_num O(log N), find_median O(1)  |  Space: O(N)
# ============================================================
class MedianFinderOptimal:
    def __init__(self) -> None:
        self.lower: List[int] = []  # max-heap (negate values)
        self.upper: List[int] = []  # min-heap

    def add_num(self, num: int) -> None:
        heapq.heappush(self.lower, -num)
        # Move max of lower to upper
        heapq.heappush(self.upper, -heapq.heappop(self.lower))
        # Keep lower size >= upper size
        if len(self.upper) > len(self.lower):
            heapq.heappush(self.lower, -heapq.heappop(self.upper))

    def find_median(self) -> float:
        if len(self.lower) > len(self.upper):
            return float(-self.lower[0])
        return (-self.lower[0] + self.upper[0]) / 2.0


# ============================================================
# APPROACH 3: BEST
# Two heaps but insert to correct side first (fewer swaps).
# Time: add_num O(log N), find_median O(1)  |  Space: O(N)
# ============================================================
class MedianFinder:
    def __init__(self) -> None:
        self.lower: List[int] = []  # max-heap (negated)
        self.upper: List[int] = []  # min-heap

    def add_num(self, num: int) -> None:
        if not self.lower or num <= -self.lower[0]:
            heapq.heappush(self.lower, -num)
        else:
            heapq.heappush(self.upper, num)
        # Rebalance
        if len(self.lower) > len(self.upper) + 1:
            heapq.heappush(self.upper, -heapq.heappop(self.lower))
        elif len(self.upper) > len(self.lower):
            heapq.heappush(self.lower, -heapq.heappop(self.upper))

    def find_median(self) -> float:
        if len(self.lower) == len(self.upper):
            return (-self.lower[0] + self.upper[0]) / 2.0
        return float(-self.lower[0])


if __name__ == "__main__":
    print("=== Find Median from Data Stream ===")

    nums = [5, 15, 1, 3]

    print("\n--- Brute Force ---")
    bf = MedianFinderBrute()
    for n in nums:
        bf.add_num(n)
        print(f"  add_num({n}) -> median={bf.find_median()}")

    print("\n--- Optimal ---")
    opt = MedianFinderOptimal()
    for n in nums:
        opt.add_num(n)
        print(f"  add_num({n}) -> median={opt.find_median()}")

    print("\n--- Best ---")
    best = MedianFinder()
    for n in nums:
        best.add_num(n)
        print(f"  add_num({n}) -> median={best.find_median()}")
