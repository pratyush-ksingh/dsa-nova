"""Problem: Magician and Chocolates
Difficulty: EASY | XP: 10
Source: InterviewBit

K time units, N bags of chocolates. Each time unit: take the bag with most chocolates,
eat them all, put back floor(eaten/2). Maximize total chocolates eaten in K turns.
"""
import heapq
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(K * N log N)  |  Space: O(N)
# Sort each round and pick max manually
# ============================================================
def brute_force(bags: List[int], k: int) -> int:
    b = bags[:]
    total = 0
    for _ in range(k):
        b.sort()
        max_val = b[-1]
        total += max_val
        b[-1] = max_val // 2
    return total % (10**9 + 7)


# ============================================================
# APPROACH 2: OPTIMAL - Max-Heap
# Time: O(K log N)  |  Space: O(N)
# Use max-heap (negate for Python min-heap)
# ============================================================
def optimal(bags: List[int], k: int) -> int:
    MOD = 10**9 + 7
    heap = [-x for x in bags]
    heapq.heapify(heap)
    total = 0
    for _ in range(k):
        max_val = -heapq.heappop(heap)
        total += max_val
        heapq.heappush(heap, -(max_val // 2))
    return total % MOD


# ============================================================
# APPROACH 3: BEST - Max-Heap with early exit when all zero
# Time: O(K log N)  |  Space: O(N)
# Stop early once the max bag is 0 (no more chocolates)
# ============================================================
def best(bags: List[int], k: int) -> int:
    MOD = 10**9 + 7
    heap = [-x for x in bags]
    heapq.heapify(heap)
    total = 0
    for _ in range(k):
        max_val = -heap[0]
        if max_val == 0:
            break
        heapq.heapreplace(heap, -(max_val // 2))
        total += max_val
    return total % MOD


if __name__ == "__main__":
    tests = [
        ([6, 5], 3, 14),       # 6+5+3=14
        ([1, 2, 3], 4, 7),     # 3+2+1+1=7
        ([10], 1, 10),
        ([0, 0], 5, 0),        # all zero bags
    ]
    for bags, k, expected in tests:
        bf = brute_force(bags, k)
        opt = optimal(bags, k)
        be = best(bags, k)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] bags={bags} k={k} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
