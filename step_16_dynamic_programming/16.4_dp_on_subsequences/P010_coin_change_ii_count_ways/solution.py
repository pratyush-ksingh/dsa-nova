"""
Problem: Coin Change II - Count Ways (LeetCode 518)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(2^(amount/min_coin))  |  Space: O(amount) stack
# Count all combinations (not permutations) to make amount.
# ============================================================
def brute_force(coins: List[int], amount: int) -> int:
    n = len(coins)

    def solve(idx: int, remaining: int) -> int:
        if remaining == 0:
            return 1
        if idx == 0:
            return 1 if remaining % coins[0] == 0 else 0
        # Not take
        not_take = solve(idx - 1, remaining)
        # Take (stay at idx -- unlimited)
        take = 0
        if coins[idx] <= remaining:
            take = solve(idx, remaining - coins[idx])
        return not_take + take

    return solve(n - 1, amount)


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(n * amount)  |  Space: O(n * amount)
# dp[i][a] = number of ways to make amount a using coins 0..i.
# ============================================================
def optimal(coins: List[int], amount: int) -> int:
    n = len(coins)
    dp = [[0] * (amount + 1) for _ in range(n)]

    # Base: amount 0 has exactly 1 way (pick nothing)
    for i in range(n):
        dp[i][0] = 1

    # Base: only coin 0
    for a in range(1, amount + 1):
        if a % coins[0] == 0:
            dp[0][a] = 1

    for i in range(1, n):
        for a in range(amount + 1):
            not_take = dp[i - 1][a]
            take = 0
            if coins[i] <= a:
                take = dp[i][a - coins[i]]  # stay on row i (reuse)
            dp[i][a] = not_take + take

    return dp[n - 1][amount]


# ============================================================
# APPROACH 3: BEST -- 1D Space-Optimized DP
# Time: O(n * amount)  |  Space: O(amount)
# Left-to-right ensures each coin can be reused (unbounded).
# ============================================================
def best(coins: List[int], amount: int) -> int:
    dp = [0] * (amount + 1)
    dp[0] = 1  # one way to make amount 0

    for coin in coins:
        for a in range(coin, amount + 1):  # left to right
            dp[a] += dp[a - coin]

    return dp[amount]


if __name__ == "__main__":
    print("=== Coin Change II - Count Ways ===\n")

    coins1 = [1, 2, 5]
    amt1 = 5
    print(f"Brute:   {brute_force(coins1, amt1)}")   # 4
    print(f"Optimal: {optimal(coins1, amt1)}")         # 4
    print(f"Best:    {best(coins1, amt1)}")            # 4

    coins2 = [2]
    amt2 = 3
    print(f"\nBrute:   {brute_force(coins2, amt2)}")  # 0
    print(f"Optimal: {optimal(coins2, amt2)}")          # 0
    print(f"Best:    {best(coins2, amt2)}")             # 0

    coins3 = [10]
    amt3 = 10
    print(f"\nSingle coin: {best(coins3, amt3)}")  # 1

    # Amount = 0
    print(f"Zero amount: {best([1, 2, 5], 0)}")  # 1
