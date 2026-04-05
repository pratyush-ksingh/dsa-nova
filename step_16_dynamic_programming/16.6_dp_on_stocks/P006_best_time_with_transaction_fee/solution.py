"""
Problem: Best Time to Buy and Sell Stock with Transaction Fee (LeetCode #714)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion with State
# Time: O(2^n)  |  Space: O(n) recursion stack
# At each day, either buy/sell or skip based on current state.
# ============================================================
def brute_force(prices: List[int], fee: int) -> int:
    n = len(prices)

    def solve(i: int, can_buy: bool) -> int:
        if i >= n:
            return 0
        if can_buy:
            buy = -prices[i] + solve(i + 1, False)
            skip = solve(i + 1, True)
            return max(buy, skip)
        else:
            sell = prices[i] - fee + solve(i + 1, True)
            skip = solve(i + 1, False)
            return max(sell, skip)

    return solve(0, True)


# ============================================================
# APPROACH 2: OPTIMAL -- DP with Buy/Sell States
# Time: O(n)  |  Space: O(n)
# buy[i]  = max profit on day i while holding stock
# sell[i] = max profit on day i while not holding stock
# ============================================================
def optimal(prices: List[int], fee: int) -> int:
    n = len(prices)
    if n <= 1:
        return 0

    buy = [0] * n
    sell = [0] * n

    buy[0] = -prices[0]
    sell[0] = 0

    for i in range(1, n):
        buy[i] = max(buy[i - 1], sell[i - 1] - prices[i])
        sell[i] = max(sell[i - 1], buy[i - 1] + prices[i] - fee)

    return sell[n - 1]


# ============================================================
# APPROACH 3: BEST -- Space-Optimized Two Variables
# Time: O(n)  |  Space: O(1)
# Only need previous day's buy and sell values.
# ============================================================
def best(prices: List[int], fee: int) -> int:
    n = len(prices)
    if n <= 1:
        return 0

    hold = -prices[0]   # max profit when holding stock
    cash = 0             # max profit when not holding stock

    for i in range(1, n):
        new_hold = max(hold, cash - prices[i])
        new_cash = max(cash, hold + prices[i] - fee)
        hold = new_hold
        cash = new_cash

    return cash


if __name__ == "__main__":
    print("=== Best Time with Transaction Fee ===\n")

    print(f"Brute:   {brute_force([1,3,2,8,4,9], 2)}")   # 8
    print(f"Optimal: {optimal([1,3,2,8,4,9], 2)}")         # 8
    print(f"Best:    {best([1,3,2,8,4,9], 2)}")            # 8

    print(f"\nBrute:   {brute_force([1,3,7,5,10,3], 3)}")  # 6
    print(f"Optimal: {optimal([1,3,7,5,10,3], 3)}")         # 6
    print(f"Best:    {best([1,3,7,5,10,3], 3)}")            # 6

    print(f"\nSingle:     {best([1], 1)}")                   # 0
    print(f"Decreasing: {best([5,4,3,2,1], 1)}")            # 0
