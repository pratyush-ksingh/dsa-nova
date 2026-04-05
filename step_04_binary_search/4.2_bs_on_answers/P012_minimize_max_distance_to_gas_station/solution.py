"""
Problem: Minimize Max Distance to Gas Station
Difficulty: HARD | XP: 50

Given existing gas stations on a number line and K new stations to add,
place the new stations to minimize the maximum distance between adjacent stations.
Return the answer with precision 1e-6.
"""
from typing import List
import heapq
import math


# ============================================================
# APPROACH 1: BRUTE FORCE  (Greedy with max-heap)
# Time: O(K * n log n)  |  Space: O(n)
# ============================================================
def brute_force(stations: List[int], k: int) -> float:
    """
    Greedily place each new station in the currently largest gap using a max-heap.
    Real-life: Optimally placing cell towers to minimize coverage gaps.
    """
    n = len(stations)
    counts = [0] * (n - 1)  # extra stations in each gap
    # max-heap: store (-section_length, gap_index)
    heap = []
    for i in range(n - 1):
        gap = stations[i + 1] - stations[i]
        heapq.heappush(heap, (-gap, i))

    for _ in range(k):
        neg_len, idx = heapq.heappop(heap)
        counts[idx] += 1
        new_len = (stations[idx + 1] - stations[idx]) / (counts[idx] + 1)
        heapq.heappush(heap, (-new_len, idx))

    max_dist = 0.0
    for i in range(n - 1):
        max_dist = max(max_dist, (stations[i + 1] - stations[i]) / (counts[i] + 1))
    return max_dist


# ============================================================
# APPROACH 2: OPTIMAL  (Binary Search on Answer)
# Time: O(n * 100) = O(n)  |  Space: O(1)
# ============================================================
def optimal(stations: List[int], k: int) -> float:
    """
    Binary search on the answer (max distance D). For a given D, count minimum
    new stations needed: sum of floor(gap/D) for each gap.
    If total <= k, D is achievable.
    Real-life: Minimizing maximum travel distance between distribution centers.
    """
    def can_place(max_dist: float) -> bool:
        needed = 0
        for i in range(len(stations) - 1):
            gap = stations[i + 1] - stations[i]
            needed += int(gap / max_dist)
        return needed <= k

    lo, hi = 0.0, float(stations[-1] - stations[0])
    for _ in range(100):
        mid = (lo + hi) / 2
        if can_place(mid):
            hi = mid
        else:
            lo = mid
    return hi


# ============================================================
# APPROACH 3: BEST
# Time: O(n log(max_gap / 1e-6))  |  Space: O(1)
# ============================================================
def best(stations: List[int], k: int) -> float:
    """
    Same binary search with explicit epsilon termination (hi - lo < 1e-6).
    Real-life: Precision-critical placement in geographic information systems.
    """
    def can_place(max_dist: float) -> bool:
        needed = 0
        for i in range(len(stations) - 1):
            gap = stations[i + 1] - stations[i]
            needed += int(gap / max_dist)
        return needed <= k

    lo, hi = 0.0, float(stations[-1] - stations[0])
    while hi - lo > 1e-6:
        mid = (lo + hi) / 2
        if can_place(mid):
            hi = mid
        else:
            lo = mid
    return hi


if __name__ == "__main__":
    print("=== Minimize Max Distance to Gas Station ===")
    tests = [
        ([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 9, 0.5),
        ([1, 5, 10],                        1, 2.5),
    ]
    for stations, k, exp in tests:
        print(f"\nStations: {stations}  K={k}  =>  expected: {exp:.6f}")
        print(f"  Brute:   {brute_force(stations, k):.6f}")
        print(f"  Optimal: {optimal(stations, k):.6f}")
        print(f"  Best:    {best(stations, k):.6f}")
