"""
Problem: Intersecting Chords in a Circle
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given N pairs of points on a circle (2N points total), count the number
of ways to connect them with N non-intersecting chords modulo 10^9+7.
The answer is the Nth Catalan number.

Catalan recurrence: C(0)=1, C(n) = sum_{i=0}^{n-1} C(i)*C(n-1-i)

Real-life analogy: counting valid bracket sequences, non-crossing handshakes,
triangulations of a polygon, or valid parse trees in compilers.
"""
from typing import List
import sys
sys.setrecursionlimit(10000)

MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE (Recursive Catalan, no memoization)
# Time: O(4^n)  |  Space: O(n) recursion stack
# ============================================================
# Fix the first point and enumerate which other point it pairs with.
# The chord divides remaining points into two independent groups.
# No cache -> exponential re-computation.
def brute_force(n: int) -> int:
    def catalan(k: int) -> int:
        if k <= 1:
            return 1
        result = 0
        for i in range(k):
            result = (result + catalan(i) * catalan(k - 1 - i)) % MOD
        return result

    return catalan(n)


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
# Time: O(n^2)  |  Space: O(n)
# ============================================================
# Same recursion but cache each catalan(k) result using lru_cache.
def optimal(n: int) -> int:
    from functools import lru_cache

    @lru_cache(maxsize=None)
    def catalan(k: int) -> int:
        if k <= 1:
            return 1
        result = 0
        for i in range(k):
            result = (result + catalan(i) * catalan(k - 1 - i)) % MOD
        return result

    return catalan(n)


# ============================================================
# APPROACH 3: BEST (Bottom-Up DP)
# Time: O(n^2)  |  Space: O(n)
# ============================================================
# Iteratively fill dp[0..n]. For each k, sum over all split-points i.
# dp[k] = sum of dp[i] * dp[k-1-i] for i in [0, k-1].
def best(n: int) -> int:
    if n == 0:
        return 1
    dp = [0] * (n + 1)
    dp[0] = 1
    dp[1] = 1

    for k in range(2, n + 1):
        for i in range(k):
            dp[k] = (dp[k] + dp[i] * dp[k - 1 - i]) % MOD

    return dp[n]


if __name__ == "__main__":
    print("=== Intersecting Chords in a Circle ===")
    # Catalan: C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42, C(10)=16796
    test_cases = [
        (0,  1),
        (1,  1),
        (2,  2),
        (3,  5),
        (4,  14),
        (5,  42),
        (10, 16796),
    ]

    for n, expected in test_cases:
        bf = brute_force(n)
        op = optimal(n)
        be = best(n)
        status = "PASS" if bf == op == be == expected else "FAIL"
        print(f"[{status}] N={n:<3} | Brute: {bf:<8} | Optimal: {op:<8} | Best: {be:<8} | Expected: {expected}")
