"""
Problem: Coins in a Line
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Two players alternately pick coins from either end of array. Both play optimally.
Return maximum coins the FIRST player can collect.
Classic interval DP / game theory problem.
"""

from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE
# Recursive memoization: dp[i][j] = max coins first player collects from A[i..j]
# Time: O(N^2)  |  Space: O(N^2)
# ============================================================
def brute_force(A: List[int]) -> int:
    n = len(A)

    @lru_cache(maxsize=None)
    def dp(i, j):
        if i > j:
            return 0
        if i == j:
            return A[i]
        # Pick left: opponent then plays optimally on [i+1, j]
        # The current player gets what's left after opponent's best play
        pick_left = A[i] + min(dp(i + 2, j), dp(i + 1, j - 1))
        pick_right = A[j] + min(dp(i, j - 2), dp(i + 1, j - 1))
        return max(pick_left, pick_right)

    return dp(0, n - 1)


# ============================================================
# APPROACH 2: OPTIMAL
# Bottom-up DP: build from small intervals to large
# dp[i][j] = max coins first player collects from subarray A[i..j]
# Time: O(N^2)  |  Space: O(N^2)
# ============================================================
def optimal(A: List[int]) -> int:
    n = len(A)
    dp = [[0] * n for _ in range(n)]

    for i in range(n):
        dp[i][i] = A[i]

    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            if length == 2:
                dp[i][j] = max(A[i], A[j])
            else:
                pick_left = A[i] + min(dp[i + 2][j], dp[i + 1][j - 1])
                pick_right = A[j] + min(dp[i][j - 2], dp[i + 1][j - 1])
                dp[i][j] = max(pick_left, pick_right)

    return dp[0][n - 1]


# ============================================================
# APPROACH 3: BEST
# For even-length arrays: first player always wins by controlling parity.
# They can guarantee all even-indexed or all odd-indexed coins (pick the larger sum).
# Time: O(N)  |  Space: O(1) for even n; O(N^2) fallback for odd n
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    if n % 2 == 0:
        sum_even = sum(A[i] for i in range(0, n, 2))
        sum_odd = sum(A[i] for i in range(1, n, 2))
        return max(sum_even, sum_odd)
    # Odd length: use full DP
    return optimal(A)


if __name__ == "__main__":
    print("=== Coins in a Line ===")

    A1 = [6, 9, 1, 2, 16, 8]
    print(f"A1 BruteForce: {brute_force(A1)}")  # 23
    print(f"A1 Optimal:    {optimal(A1)}")       # 23
    print(f"A1 Best:       {best(A1)}")          # 23

    A2 = [1, 2, 3, 4]
    print(f"A2 Optimal: {optimal(A2)}")  # 6
    print(f"A2 Best:    {best(A2)}")     # 6

    A3 = [8, 15, 3, 7]  # even: 11, odd: 22 => 22
    print(f"A3 Best: {best(A3)}")  # 22

    A4 = [5, 3, 7, 10]  # even: 12, odd: 13 => 13
    print(f"A4 Best: {best(A4)}")  # 13
