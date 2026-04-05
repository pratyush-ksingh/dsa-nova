"""
Problem: Best Time to Buy and Sell Stock III (LeetCode 123)
Difficulty: HARD | XP: 50

You are given an array `prices` where prices[i] is the stock price on day i.
You may complete AT MOST 2 transactions (buy and sell). You may not hold
more than one share at a time (must sell before buying again).
Return the maximum profit.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try every split point; left half = best 1-transaction profit,
# right half = best 1-transaction profit
# ============================================================
def brute_force(prices: List[int]) -> int:
    """
    For every split point k (0 <= k <= n-1), compute:
      - max profit from a SINGLE transaction in prices[0..k]
      - max profit from a SINGLE transaction in prices[k..n-1]
    The answer is the maximum over all k of their sum.

    Two nested O(n) scans per split point → O(n^2) overall.
    """
    n = len(prices)
    if n < 2:
        return 0

    def max_profit_range(lo: int, hi: int) -> int:
        """Best single-transaction profit in prices[lo..hi]."""
        min_price = prices[lo]
        profit = 0
        for i in range(lo, hi + 1):
            profit = max(profit, prices[i] - min_price)
            min_price = min(min_price, prices[i])
        return profit

    best = 0
    for k in range(n):
        left  = max_profit_range(0, k)
        right = max_profit_range(k, n - 1)
        best  = max(best, left + right)
    return best


# ============================================================
# APPROACH 2: OPTIMAL — DP with transaction-count states
# Time: O(n)  |  Space: O(n)
# dp[i][t][hold]: max profit on day i, having made t complete
# transactions so far, with hold=1 (holding) or 0 (not holding)
# ============================================================
def optimal(prices: List[int]) -> int:
    """
    State: (day, transactions_completed, holding_stock)
    Transitions:
      - Not holding, t completed: rest | sell today (t+1 completed)
      - Holding, t completed: rest | buy today (t stays until sold)
    Compress to 4 states since max transactions = 2:
      buy1, sell1, buy2, sell2
    This version uses explicit O(n) arrays for clarity.
    """
    n = len(prices)
    if n < 2:
        return 0

    # buy1[i]: max profit after buying stock once on/before day i
    # sell1[i]: max profit after selling stock once on/before day i
    # buy2[i]: max profit after buying stock second time on/before day i
    # sell2[i]: max profit after selling stock twice on/before day i
    buy1  = [0] * n
    sell1 = [0] * n
    buy2  = [0] * n
    sell2 = [0] * n

    buy1[0]  = -prices[0]
    sell1[0] = 0
    buy2[0]  = -prices[0]
    sell2[0] = 0

    for i in range(1, n):
        buy1[i]  = max(buy1[i - 1],  -prices[i])              # buy on day i or keep previous
        sell1[i] = max(sell1[i - 1], buy1[i - 1] + prices[i]) # sell on day i or keep previous
        buy2[i]  = max(buy2[i - 1],  sell1[i - 1] - prices[i])# buy 2nd time after 1st sell
        sell2[i] = max(sell2[i - 1], buy2[i - 1] + prices[i]) # sell 2nd time

    return sell2[n - 1]


# ============================================================
# APPROACH 3: BEST — 4 scalar variables, O(1) space
# Time: O(n)  |  Space: O(1)
# Compress the DP arrays to four running values
# ============================================================
def best(prices: List[int]) -> int:
    """
    Track four values as we iterate through prices once:
      buy1  = best profit after first buy  (negative cost so far)
      sell1 = best profit after first sell
      buy2  = best profit after second buy (sell1 - current_price)
      sell2 = best profit after second sell

    This is the same DP as Approach 2, but observing that each
    state only depends on the previous day's values.
    """
    buy1  = float('-inf')  # not yet bought
    sell1 = 0
    buy2  = float('-inf')
    sell2 = 0

    for price in prices:
        buy1  = max(buy1,  -price)            # buy for the first time
        sell1 = max(sell1, buy1  + price)     # sell after first buy
        buy2  = max(buy2,  sell1 - price)     # buy for the second time
        sell2 = max(sell2, buy2  + price)     # sell after second buy

    return sell2


if __name__ == "__main__":
    test_cases = [
        ([3, 3, 5, 0, 0, 3, 1, 4], 6),    # buy@0 sell@3 + buy@1 sell@4 = 6
        ([1, 2, 3, 4, 5], 4),             # buy@1 sell@5 (one txn is enough)
        ([7, 6, 4, 3, 1], 0),             # prices only fall
        ([1], 0),                          # single day
        ([1, 2], 1),                       # single transaction
        ([6, 1, 3, 2, 4, 7], 7),          # buy@1 sell@3 (2) + buy@2 sell@7 (5) = 7
    ]

    print("=== Best Time to Buy and Sell Stock III ===")
    for prices, expected in test_cases:
        b  = brute_force(prices)
        o  = optimal(prices)
        bs = best(prices)
        status = "PASS" if b == o == bs == expected else "FAIL"
        print(f"[{status}] prices={prices} | brute={b} optimal={o} best={bs} | expected={expected}")
