"""Problem: Tiling With Dominoes
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Count ways to tile a 3xN board with 2x1 dominoes.
Recurrence: dp[n] = 4*dp[n-2] - dp[n-4] (for even n >= 4)
Only even N has non-zero solutions (3*N must be divisible by 2).
"""
from functools import lru_cache

MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE - Memoized recursion
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(n: int) -> int:
    if n % 2 != 0:
        return 0

    @lru_cache(maxsize=None)
    def dp(k):
        if k == 0:
            return 1
        if k == 2:
            return 3
        if k < 0:
            return 0
        return (4 * dp(k - 2) - dp(k - 4)) % MOD

    return dp(n)


# ============================================================
# APPROACH 2: OPTIMAL - Bottom-up DP array
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(n: int) -> int:
    if n % 2 != 0:
        return 0
    if n == 0:
        return 1
    dp = [0] * (n + 1)
    dp[0] = 1
    if n >= 2:
        dp[2] = 3
    for i in range(4, n + 1, 2):
        dp[i] = (4 * dp[i-2] - dp[i-4]) % MOD
    return dp[n]


# ============================================================
# APPROACH 3: BEST - O(1) space rolling variables
# Time: O(N)  |  Space: O(1)
# ============================================================
def best(n: int) -> int:
    if n % 2 != 0:
        return 0
    if n == 0:
        return 1
    if n == 2:
        return 3
    prev2, prev1 = 1, 3  # dp[i-4], dp[i-2]
    curr = 0
    for _ in range(4, n + 1, 2):
        curr = (4 * prev1 - prev2) % MOD
        prev2, prev1 = prev1, curr
    return curr


if __name__ == "__main__":
    tests = [
        (0, 1), (1, 0), (2, 3), (4, 11), (6, 41), (8, 153)
    ]
    for n, expected in tests:
        bf = brute_force(n)
        opt = optimal(n)
        be = best(n)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] n={n}: Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
