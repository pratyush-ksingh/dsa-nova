"""
Problem: Grid Unique Paths
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an m x n grid, count the number of unique paths from the top-left
corner to the bottom-right corner. You can only move right or down.

Mathematical insight: You need exactly (m-1) down moves and (n-1) right moves,
for a total of (m+n-2) moves. Choose which (m-1) of them are down moves:
C(m+n-2, m-1).
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursion
# Time: O(2^(m+n))  |  Space: O(m+n) recursion stack
# ============================================================
def brute_force(m: int, n: int) -> int:
    """
    At each cell, recursively count paths going right and going down.
    Exponential due to repeated subproblems (no memoization).
    Base case: when at bottom row or rightmost column, only 1 path remains.
    """
    def recurse(r: int, c: int) -> int:
        if r == m - 1 or c == n - 1:
            return 1
        return recurse(r + 1, c) + recurse(r, c + 1)

    return recurse(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — Dynamic Programming
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def optimal(m: int, n: int) -> int:
    """
    dp[i][j] = number of unique paths to cell (i, j).
    dp[i][j] = dp[i-1][j] + dp[i][j-1]  (from above + from left)
    Base: entire first row and first column = 1.
    """
    dp = [[1] * n for _ in range(m)]
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
    return dp[m - 1][n - 1]


# ============================================================
# APPROACH 3: BEST — Combinatorics O(min(m,n))
# Time: O(min(m, n))  |  Space: O(1)
# ============================================================
def best(m: int, n: int) -> int:
    """
    The answer is C(m+n-2, min(m,n)-1).
    Total moves = m+n-2. We choose min(m,n)-1 of them for the shorter direction.
    Compute using multiplicative formula to avoid factorial overflow:
      C(N, k) = N*(N-1)*...*(N-k+1) / k!
    where k = min(m,n) - 1.
    """
    N = m + n - 2
    k = min(m, n) - 1
    return math.comb(N, k)


if __name__ == "__main__":
    print("=== Grid Unique Paths ===")
    tests = [(3, 7), (3, 3), (2, 2), (1, 1), (4, 4)]
    for m, n in tests:
        print(f"m={m}, n={n}: Brute={brute_force(m,n)}, "
              f"Optimal={optimal(m,n)}, Best={best(m,n)}")
