"""
Problem: Connect Ropes
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given N ropes with different lengths, connect them into one rope with minimum cost.
Cost of connecting two ropes = sum of their lengths.

Greedy: always merge the two shortest ropes first (Huffman coding principle).
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE - Sort repeatedly after each merge
# Time: O(N^2 log N)  |  Space: O(N)
# After each merge of two shortest ropes, re-sort to find new minimum pair
# ============================================================
def brute_force(ropes: List[int]) -> int:
    ropes = sorted(ropes)
    total = 0
    while len(ropes) > 1:
        merged = ropes[0] + ropes[1]
        total += merged
        ropes = sorted(ropes[2:] + [merged])
    return total


# ============================================================
# APPROACH 2: OPTIMAL - Min-Heap (Priority Queue)
# Time: O(N log N)  |  Space: O(N)
# heapq.heappop twice to get two smallest; push their sum back
# ============================================================
def optimal(ropes: List[int]) -> int:
    heapq.heapify(ropes)
    total = 0
    while len(ropes) > 1:
        a = heapq.heappop(ropes)
        b = heapq.heappop(ropes)
        merged = a + b
        total += merged
        heapq.heappush(ropes, merged)
    return total


# ============================================================
# APPROACH 3: BEST - Same min-heap, but with explicit copy to avoid mutation
# Time: O(N log N)  |  Space: O(N)
# Pure function variant; also shows the Huffman optimality argument in comments
# ============================================================
def best(ropes: List[int]) -> int:
    """
    Huffman insight: each rope's contribution to total cost equals
    rope_length * (number of times it gets merged).
    Min-heap ensures shorter ropes are merged fewer times -> minimizes total.
    """
    heap = list(ropes)
    heapq.heapify(heap)
    total = 0
    while len(heap) > 1:
        a = heapq.heappop(heap)
        b = heapq.heappop(heap)
        cost = a + b
        total += cost
        heapq.heappush(heap, cost)
    return total


if __name__ == "__main__":
    print("=== Connect Ropes ===")

    ropes = [4, 3, 2, 6]
    print(f"ropes={ropes}")
    print(f"Brute:   {brute_force(ropes[:])}")   # 29
    print(f"Optimal: {optimal(ropes[:])}")         # 29
    print(f"Best:    {best(ropes[:])}")            # 29

    ropes = [1, 2, 3, 4, 5]
    print(f"\nropes={ropes}")
    print(f"Brute:   {brute_force(ropes[:])}")   # 33
    print(f"Optimal: {optimal(ropes[:])}")
    print(f"Best:    {best(ropes[:])}")

    ropes = [5]
    print(f"\nropes={ropes} (single rope)")
    print(f"Brute:   {brute_force(ropes[:])}")   # 0
    print(f"Optimal: {optimal(ropes[:])}")
    print(f"Best:    {best(ropes[:])}")
