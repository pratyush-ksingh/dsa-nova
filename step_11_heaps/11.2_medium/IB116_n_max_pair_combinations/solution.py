"""
Problem: N Max Pair Combinations
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given two arrays A and B of size N, find the N largest possible pair sums
A[i] + B[j]. Return them in non-increasing order.
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE - Generate all N^2 sums, sort, take top N
# Time: O(N^2 log N)  |  Space: O(N^2)
# ============================================================
def brute_force(A: List[int], B: List[int]) -> List[int]:
    n = len(A)
    all_sums = sorted([a + b for a in A for b in B], reverse=True)
    return all_sums[:n]


# ============================================================
# APPROACH 2: OPTIMAL - Sort + max-heap with visited set
# Time: O(N log N)  |  Space: O(N)
# Sort both desc. Start heap with (A[0]+B[0], 0, 0).
# Each extraction gives next largest; push neighbors (i+1,j) and (i,j+1).
# ============================================================
def optimal(A: List[int], B: List[int]) -> List[int]:
    n = len(A)
    A.sort(reverse=True)
    B.sort(reverse=True)

    heap = [-(A[0] + B[0]), 0, 0]  # start with largest pair
    heap = [(-(A[0] + B[0]), 0, 0)]
    visited = {(0, 0)}
    res = []
    while len(res) < n:
        neg_sum, i, j = heapq.heappop(heap)
        res.append(-neg_sum)
        if i + 1 < n and (i + 1, j) not in visited:
            visited.add((i + 1, j))
            heapq.heappush(heap, (-(A[i+1] + B[j]), i + 1, j))
        if j + 1 < n and (i, j + 1) not in visited:
            visited.add((i, j + 1))
            heapq.heappush(heap, (-(A[i] + B[j+1]), i, j + 1))
    return res


# ============================================================
# APPROACH 3: BEST - Sort A & B; initialize heap with each A[i] paired with B[0]
# Time: O(N log N)  |  Space: O(N)
# No visited set needed: each A[i] independently walks down sorted B
# ============================================================
def best(A: List[int], B: List[int]) -> List[int]:
    n = len(A)
    A_sorted = sorted(A, reverse=True)
    B_sorted = sorted(B, reverse=True)

    # Each row i: A_sorted[i] starts paired with B_sorted[0]
    heap = [-(A_sorted[i] + B_sorted[0]) for i in range(n)]
    # Also track which B index each A index is at
    heap_full = [(-( A_sorted[i] + B_sorted[0]), i, 0) for i in range(n)]
    heapq.heapify(heap_full)

    res = []
    while len(res) < n:
        neg_sum, ai, bi = heapq.heappop(heap_full)
        res.append(-neg_sum)
        if bi + 1 < n:
            heapq.heappush(heap_full, (-(A_sorted[ai] + B_sorted[bi + 1]), ai, bi + 1))
    return res


if __name__ == "__main__":
    print("=== N Max Pair Combinations ===")

    A, B = [1, 4, 2, 3], [2, 5, 1, 6]
    print(f"A={A}, B={B}")
    print(f"Brute:   {brute_force(A[:], B[:])}")
    print(f"Optimal: {optimal(A[:], B[:])}")
    print(f"Best:    {best(A[:], B[:])}")
    # Expected: top 4 = [10, 9, 9, 8] (4+6, 3+6 or 4+5, ...)

    A2, B2 = [3, 2], [1, 4]
    print(f"\nA={A2}, B={B2}")
    print(f"Brute:   {brute_force(A2[:], B2[:])}")
    print(f"Optimal: {optimal(A2[:], B2[:])}")
    print(f"Best:    {best(A2[:], B2[:])}")
    # Expected: [7, 6]
