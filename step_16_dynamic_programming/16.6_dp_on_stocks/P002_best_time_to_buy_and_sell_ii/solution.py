"""
Problem: Best Time to Buy and Sell Stock II (LeetCode #122)
Difficulty: MEDIUM | XP: 25

Unlimited transactions. Maximize profit.
All 4 DP approaches + Greedy: Recursion -> Memo -> Tab -> Space -> Greedy
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def max_profit_recursive(prices: List[int]) -> int:
    """At each day: buy/sell or skip. canBuy tracks whether we hold stock."""
    n = len(prices)

    def solve(i: int, can_buy: bool) -> int:
        if i == n:
            return 0

        if can_buy:
            buy = -prices[i] + solve(i + 1, False)
            skip = solve(i + 1, True)
            return max(buy, skip)
        else:
            sell = prices[i] + solve(i + 1, True)
            skip = solve(i + 1, False)
            return max(sell, skip)

    return solve(0, True)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def max_profit_memo(prices: List[int]) -> int:
    """Cache (day, canBuy) states. Only 2n states total."""
    n = len(prices)
    dp = {}

    def solve(i: int, can_buy: bool) -> int:
        if i == n:
            return 0
        if (i, can_buy) in dp:
            return dp[(i, can_buy)]

        if can_buy:
            buy = -prices[i] + solve(i + 1, False)
            skip = solve(i + 1, True)
            dp[(i, can_buy)] = max(buy, skip)
        else:
            sell = prices[i] + solve(i + 1, True)
            skip = solve(i + 1, False)
            dp[(i, can_buy)] = max(sell, skip)

        return dp[(i, can_buy)]

    return solve(0, True)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def max_profit_tab(prices: List[int]) -> int:
    """Build dp[n+1][2] table from last day backwards."""
    n = len(prices)
    dp = [[0] * 2 for _ in range(n + 1)]

    for i in range(n - 1, -1, -1):
        # can_buy = 1 (True)
        dp[i][1] = max(-prices[i] + dp[i + 1][0], dp[i + 1][1])
        # can_buy = 0 (False, holding)
        dp[i][0] = max(prices[i] + dp[i + 1][1], dp[i + 1][0])

    return dp[0][1]


# ============================================================
# APPROACH 4a: SPACE OPTIMIZED DP
# Time: O(n) | Space: O(1)
# ============================================================
def max_profit_space(prices: List[int]) -> int:
    """Only need dp[i+1] values. Use 2 variables."""
    n = len(prices)
    ahead_buy = 0   # dp[i+1][1]
    ahead_sell = 0   # dp[i+1][0]

    for i in range(n - 1, -1, -1):
        curr_buy = max(-prices[i] + ahead_sell, ahead_buy)
        curr_sell = max(prices[i] + ahead_buy, ahead_sell)
        ahead_buy = curr_buy
        ahead_sell = curr_sell

    return ahead_buy


# ============================================================
# APPROACH 4b: GREEDY (Collect all upswings)
# Time: O(n) | Space: O(1)
# ============================================================
def max_profit_greedy(prices: List[int]) -> int:
    """Sum all positive day-to-day differences. Equivalent to optimal DP."""
    profit = 0
    for i in range(1, len(prices)):
        if prices[i] > prices[i - 1]:
            profit += prices[i] - prices[i - 1]
    return profit


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Best Time to Buy and Sell Stock II ===\n")

    test_cases = [
        ([7, 1, 5, 3, 6, 4], 7),
        ([1, 2, 3, 4, 5], 4),
        ([7, 6, 4, 3, 1], 0),
        ([5], 0),
        ([1, 5], 4),
        ([3, 3, 3, 3], 0),
        ([1, 5, 1, 5], 8),
    ]

    for prices, expected in test_cases:
        r = max_profit_recursive(prices)
        m = max_profit_memo(prices)
        t = max_profit_tab(prices)
        s = max_profit_space(prices)
        g = max_profit_greedy(prices)

        passes = (r == expected and m == expected and t == expected
                  and s == expected and g == expected)
        print(f"prices={prices}")
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s} | Greedy={g}")
        print(f"  Expected={expected} | Pass={passes}\n")
