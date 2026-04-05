"""
Problem: Dice Throw
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given N dice each with M faces (numbered 1..M), count the number of ways to get sum S.
Classic bounded knapsack / DP problem.
"""

from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE
# Recursive memoization: ways(n, s) = ways to get sum s using n dice
# Time: O(N * S * M)  |  Space: O(N * S)
# ============================================================
def brute_force(N: int, M: int, S: int) -> int:
    @lru_cache(maxsize=None)
    def dp(dice_left, sum_left):
        if dice_left == 0:
            return 1 if sum_left == 0 else 0
        if sum_left <= 0:
            return 0
        ways = 0
        for face in range(1, min(M, sum_left) + 1):
            ways += dp(dice_left - 1, sum_left - face)
        return ways

    return dp(N, S)


# ============================================================
# APPROACH 2: OPTIMAL
# Bottom-up 2D DP: dp[i][j] = ways to get sum j using i dice
# Time: O(N * S * M)  |  Space: O(N * S)
# ============================================================
def optimal(N: int, M: int, S: int) -> int:
    dp = [[0] * (S + 1) for _ in range(N + 1)]
    dp[0][0] = 1

    for i in range(1, N + 1):
        for j in range(i, S + 1):
            for face in range(1, min(M, j) + 1):
                dp[i][j] += dp[i - 1][j - face]

    return dp[N][S]


# ============================================================
# APPROACH 3: BEST
# Space-optimized 1D DP with prefix sums to avoid O(M) inner loop
# Time: O(N * S)  |  Space: O(S)
# ============================================================
def best(N: int, M: int, S: int) -> int:
    prev = [0] * (S + 1)
    prev[0] = 1

    for _ in range(N):
        curr = [0] * (S + 1)
        # Prefix sum of prev
        prefix = [0] * (S + 2)
        for j in range(S + 1):
            prefix[j + 1] = prefix[j] + prev[j]

        for j in range(1, S + 1):
            lo = max(0, j - M)
            curr[j] = prefix[j] - prefix[lo]

        prev = curr

    return prev[S]


if __name__ == "__main__":
    print("=== Dice Throw ===")

    # 2 dice, 6 faces, sum 7 => 6 ways
    print(f"BruteForce N=2 M=6 S=7: {brute_force(2, 6, 7)}")  # 6
    print(f"Optimal    N=2 M=6 S=7: {optimal(2, 6, 7)}")       # 6
    print(f"Best       N=2 M=6 S=7: {best(2, 6, 7)}")          # 6

    # 1 die, 6 faces, sum 3 => 1 way
    print(f"N=1 M=6 S=3: {optimal(1, 6, 3)}")   # 1

    # 3 dice, 6 faces, sum 8 => 21 ways
    print(f"N=3 M=6 S=8: {optimal(3, 6, 8)}")   # 21

    # 3 dice, 6 faces, sum 18 => 1 way (all 6s)
    print(f"N=3 M=6 S=18: {optimal(3, 6, 18)}")  # 1
