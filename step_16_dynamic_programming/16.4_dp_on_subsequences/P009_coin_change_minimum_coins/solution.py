"""
Problem: Coin Change - Minimum Coins (LeetCode 322)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(amount^n)  |  Space: O(amount) recursion stack
# For each coin, try taking it unlimited times; find minimum.
# ============================================================
def brute_force(coins: List[int], amount: int) -> int:
    n = len(coins)

    def solve(idx: int, remaining: int) -> int:
        if remaining == 0:
            return 0
        if idx == 0:
            if remaining % coins[0] == 0:
                return remaining // coins[0]
            return math.inf
        # Not take current coin
        not_take = solve(idx - 1, remaining)
        # Take current coin (stay at idx -- unlimited)
        take = math.inf
        if coins[idx] <= remaining:
            sub = solve(idx, remaining - coins[idx])
            if sub != math.inf:
                take = 1 + sub
        return min(not_take, take)

    ans = solve(n - 1, amount)
    return -1 if ans == math.inf else ans


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(n * amount)  |  Space: O(n * amount)
# dp[i][a] = min coins to make amount a using coins 0..i.
# ============================================================
def optimal(coins: List[int], amount: int) -> int:
    n = len(coins)
    INF = float('inf')
    dp = [[INF] * (amount + 1) for _ in range(n)]

    # Base column: amount = 0 needs 0 coins
    for i in range(n):
        dp[i][0] = 0

    # Base row: only coin 0
    for a in range(1, amount + 1):
        if a % coins[0] == 0:
            dp[0][a] = a // coins[0]

    for i in range(1, n):
        for a in range(amount + 1):
            not_take = dp[i - 1][a]
            take = INF
            if coins[i] <= a and dp[i][a - coins[i]] != INF:
                take = 1 + dp[i][a - coins[i]]
            dp[i][a] = min(not_take, take)

    return -1 if dp[n - 1][amount] == INF else dp[n - 1][amount]


# ============================================================
# APPROACH 3: BEST -- 1D Space-Optimized DP
# Time: O(n * amount)  |  Space: O(amount)
# Left-to-right traversal allows reuse (unbounded).
# ============================================================
def best(coins: List[int], amount: int) -> int:
    INF = float('inf')
    dp = [INF] * (amount + 1)
    dp[0] = 0

    for coin in coins:
        for a in range(coin, amount + 1):  # left to right
            if dp[a - coin] != INF:
                dp[a] = min(dp[a], 1 + dp[a - coin])

    return -1 if dp[amount] == INF else dp[amount]


if __name__ == "__main__":
    print("=== Coin Change - Minimum Coins ===\n")

    coins1 = [1, 5, 6, 9]
    amt1 = 11
    print(f"Brute:   {brute_force(coins1, amt1)}")   # 2
    print(f"Optimal: {optimal(coins1, amt1)}")         # 2
    print(f"Best:    {best(coins1, amt1)}")            # 2

    coins2 = [1, 2, 5]
    amt2 = 11
    print(f"\nBrute:   {brute_force(coins2, amt2)}")  # 3
    print(f"Optimal: {optimal(coins2, amt2)}")          # 3
    print(f"Best:    {best(coins2, amt2)}")             # 3

    # Impossible case
    print(f"\nImpossible: {best([2], 3)}")  # -1

    # Amount = 0
    print(f"Zero amount: {best([1, 2, 5], 0)}")  # 0
