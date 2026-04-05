"""
Problem: Best Time to Buy and Sell Stock IV
Difficulty: HARD | XP: 50
LeetCode: 188

You are given an integer k and an array prices where prices[i] is the price
of a stock on day i. Find the maximum profit using at most k transactions.
You may not engage in multiple transactions simultaneously (must sell before buying again).
"""
from typing import List
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursion
# Time: O(2^n)  |  Space: O(n) recursion stack
# ============================================================
def brute_force(k: int, prices: List[int]) -> int:
    """
    Try every possible combination of buy/sell decisions at each day.
    State: (day, transactions_left, holding_stock)
    At each day: if holding -> sell or skip; if not holding -> buy or skip.
    Exponential because every day has 2 choices branching recursively.
    """
    n = len(prices)

    def recurse(day: int, txns_left: int, holding: bool) -> int:
        # Base cases
        if day == n or txns_left == 0:
            return 0
        # Option 1: do nothing today
        result = recurse(day + 1, txns_left, holding)
        if holding:
            # Option 2: sell today (completing one transaction)
            result = max(result, prices[day] + recurse(day + 1, txns_left - 1, False))
        else:
            # Option 2: buy today
            result = max(result, -prices[day] + recurse(day + 1, txns_left, True))
        return result

    return recurse(0, k, False)


# ============================================================
# APPROACH 2: OPTIMAL - 3D DP (Memoization -> Tabulation)
# Time: O(n * k)  |  Space: O(n * k)
# ============================================================
def optimal(k: int, prices: List[int]) -> int:
    """
    DP with states: dp[i][t][hold]
      i    = current day index
      t    = transactions remaining (0..k)
      hold = 0 (not holding) or 1 (holding stock)

    Transition:
      If not holding: max(skip, buy) = max(dp[i+1][t][0], -prices[i] + dp[i+1][t][1])
      If holding:     max(skip, sell) = max(dp[i+1][t][1],  prices[i] + dp[i+1][t-1][0])

    We count a "transaction" as completed on SELL.
    Fill bottom-up from day n-1 to 0.

    Special case: if k >= n//2, we can make unlimited transactions
    (we can't make more than n//2 transactions anyway), so use greedy.
    """
    n = len(prices)
    if n == 0 or k == 0:
        return 0

    # Special case: unlimited transactions
    if k >= n // 2:
        profit = 0
        for i in range(1, n):
            if prices[i] > prices[i - 1]:
                profit += prices[i] - prices[i - 1]
        return profit

    # dp[t][hold]: max profit with t transactions left, holding status
    # Build for each day from right to left
    # Use 2D array: (k+1) x 2
    # Initialize: dp[t][hold] = 0 for day == n (no days left)
    dp = [[0] * 2 for _ in range(k + 1)]

    for day in range(n - 1, -1, -1):
        new_dp = [[0] * 2 for _ in range(k + 1)]
        for t in range(1, k + 1):
            # Not holding: skip or buy
            new_dp[t][0] = max(dp[t][0], -prices[day] + dp[t][1])
            # Holding: skip or sell (sell completes transaction -> t-1)
            new_dp[t][1] = max(dp[t][1], prices[day] + dp[t - 1][0])
        dp = new_dp

    return dp[k][0]


# ============================================================
# APPROACH 3: BEST - Space-Optimized DP + Unlimited Greedy Special Case
# Time: O(n * k)  |  Space: O(k)
# ============================================================
def best(k: int, prices: List[int]) -> int:
    """
    Key insight: track 'buy[t]' and 'sell[t]' for each transaction index t.
      buy[t]  = best profit achievable after the t-th buy  (negative running cost)
      sell[t] = best profit achievable after the t-th sell

    For each price p:
      buy[t]  = max(buy[t],  sell[t-1] - p)   # either keep old state or buy using profit from (t-1) sells
      sell[t] = max(sell[t], buy[t] + p)       # either keep old state or sell today

    Initialize:
      buy[t]  = -infinity (haven't bought yet)
      sell[t] = 0         (no profit before any sell)

    Process prices left-to-right in O(n*k) time, O(k) space.
    Special case when k >= n//2: unlimited greedy in O(n).
    """
    n = len(prices)
    if n == 0 or k == 0:
        return 0

    # Special case: treat as unlimited transactions
    if k >= n // 2:
        profit = 0
        for i in range(1, n):
            if prices[i] > prices[i - 1]:
                profit += prices[i] - prices[i - 1]
        return profit

    buy = [-sys.maxsize] * (k + 1)   # buy[t]: best profit after t-th buy
    sell = [0] * (k + 1)             # sell[t]: best profit after t-th sell

    for price in prices:
        for t in range(1, k + 1):
            buy[t] = max(buy[t], sell[t - 1] - price)
            sell[t] = max(sell[t], buy[t] + price)

    return sell[k]


if __name__ == "__main__":
    print("=== Best Time to Buy and Sell Stock IV ===")

    # Example 1: k=2, prices=[2,4,1]  -> 2
    k1, p1 = 2, [2, 4, 1]
    # Example 2: k=2, prices=[3,2,6,5,0,3] -> 7
    k2, p2 = 2, [3, 2, 6, 5, 0, 3]
    # Example 3: k=1, prices=[1,2] -> 1
    k3, p3 = 1, [1, 2]

    for k, p, expected in [(k1, p1, 2), (k2, p2, 7), (k3, p3, 1)]:
        b = brute_force(k, p)
        o = optimal(k, p)
        be = best(k, p)
        status = "OK" if b == o == be == expected else "MISMATCH"
        print(f"k={k}, prices={p} => Brute={b}, Optimal={o}, Best={be} | Expected={expected} [{status}]")
