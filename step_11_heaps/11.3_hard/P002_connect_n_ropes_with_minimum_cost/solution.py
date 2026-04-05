"""
Connect N Ropes with Minimum Cost

Minimize total cost of connecting all ropes, where cost of
connecting two ropes = sum of their lengths.
"""
import heapq
import bisect
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sort + linear insert
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(ropes: List[int]) -> int:
    if len(ropes) <= 1:
        return 0

    arr = sorted(ropes)
    total_cost = 0

    while len(arr) > 1:
        a = arr.pop(0)
        b = arr.pop(0)
        merged = a + b
        total_cost += merged
        bisect.insort(arr, merged)

    return total_cost


# ============================================================
# APPROACH 2: OPTIMAL -- Min-Heap greedy
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def optimal(ropes: List[int]) -> int:
    if len(ropes) <= 1:
        return 0

    heap = ropes[:]
    heapq.heapify(heap)  # O(N) heapify

    total_cost = 0
    while len(heap) > 1:
        a = heapq.heappop(heap)
        b = heapq.heappop(heap)
        merged = a + b
        total_cost += merged
        heapq.heappush(heap, merged)

    return total_cost


# ============================================================
# APPROACH 3: BEST -- Min-Heap (same, clean implementation)
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def best(ropes: List[int]) -> int:
    if len(ropes) <= 1:
        return 0

    heapq.heapify(ropes := ropes[:])  # copy + heapify
    cost = 0

    while len(ropes) > 1:
        s = heapq.heappop(ropes) + heapq.heappop(ropes)
        cost += s
        heapq.heappush(ropes, s)

    return cost


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    ropes1 = [4, 3, 2, 6]
    print("=== Connect N Ropes ===")
    print(f"Ropes: {ropes1}")
    print(f"Brute:   {brute_force(ropes1)}")   # 29
    print(f"Optimal: {optimal(ropes1)}")        # 29
    print(f"Best:    {best(ropes1)}")           # 29

    ropes2 = [1, 2, 3]
    print(f"\nRopes: {ropes2}")
    print(f"Brute:   {brute_force(ropes2)}")   # 9
    print(f"Optimal: {optimal(ropes2)}")        # 9
    print(f"Best:    {best(ropes2)}")           # 9

    print(f"\nSingle rope: {brute_force([5])}")  # 0
