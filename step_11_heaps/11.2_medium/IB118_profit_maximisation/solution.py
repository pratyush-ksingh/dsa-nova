"""Problem: Profit Maximisation
Difficulty: EASY | XP: 10
Source: InterviewBit

N seats in a row. Person buying seat k will pay A[k] if at least k seats are occupied.
With B people, maximize total profit. Greedy: always fill the seat slot with highest marginal value.
"""
import heapq
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N log N)  |  Space: O(N)
# Sort desc, compute cumulative sum for first B elements
# ============================================================
def brute_force(seats: List[int], b: int) -> int:
    s = sorted(seats, reverse=True)
    n = len(s)
    # Total profit = sum of marginals for first min(b,n) people
    # Marginal of person i (0-indexed) = s[i] - s[i+1] (s[n]=0)
    # Sum telescopes to s[0] - s[b]
    people = min(b, n)
    return s[0] - (s[people] if people < n else 0)


# ============================================================
# APPROACH 2: OPTIMAL - Max-Heap Greedy
# Time: O(N log N + B log N)  |  Space: O(N)
# Build max-heap of marginal gains, pick greedily B times
# ============================================================
def optimal(seats: List[int], b: int) -> int:
    s = sorted(seats, reverse=True)
    n = len(s)
    # Max-heap (negate for Python's min-heap)
    heap = []
    for i in range(n):
        mg = s[i] - (s[i + 1] if i + 1 < n else 0)
        if mg > 0:
            heapq.heappush(heap, -mg)
    profit = 0
    remaining = b
    while remaining > 0 and heap:
        profit += -heapq.heappop(heap)
        remaining -= 1
    return profit


# ============================================================
# APPROACH 3: BEST - Direct math (sorted desc, telescoping sum)
# Time: O(N log N)  |  Space: O(1) extra
# Sum of marginals[0..b-1] = s[0] - s[b] (when b <= n)
# ============================================================
def best(seats: List[int], b: int) -> int:
    s = sorted(seats, reverse=True)
    n = len(s)
    people = min(b, n)
    # Telescoping: sum of (s[i]-s[i+1]) for i in 0..people-1 = s[0] - s[people]
    return s[0] - (s[people] if people < n else 0)


if __name__ == "__main__":
    tests = [
        ([1, 3], 2, 3),           # IB example: sorted [3,1], b=2 -> 3-0=3
        ([1, 2, 3, 4, 5], 3, 3),  # sorted [5,4,3,2,1], b=3 -> 5-2=3
        ([5, 1, 4, 2, 3], 2, 2),  # sorted [5,4,3,2,1], b=2 -> 5-3=2
        ([10], 1, 10),            # single seat, 1 person
    ]
    for seats, b, expected in tests:
        bf = brute_force(seats, b)
        opt = optimal(seats, b)
        be = best(seats, b)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] seats={seats} b={b} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
