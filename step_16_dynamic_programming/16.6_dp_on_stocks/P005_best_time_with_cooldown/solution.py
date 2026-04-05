"""
Problem: Best Time to Buy and Sell Stock with Cooldown (LeetCode #309)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion with State
# Time: O(2^n)  |  Space: O(n) recursion stack
# State: (index, canBuy). After selling, skip next day (cooldown).
# ============================================================
def brute_force(prices: List[int]) -> int:
    n = len(prices)

    def solve(i: int, can_buy: bool) -> int:
        if i >= n:
            return 0
        if can_buy:
            buy = -prices[i] + solve(i + 1, False)
            skip = solve(i + 1, True)
            return max(buy, skip)
        else:
            sell = prices[i] + solve(i + 2, True)  # i+2 for cooldown
            skip = solve(i + 1, False)
            return max(sell, skip)

    return solve(0, True)


# ============================================================
# APPROACH 2: OPTIMAL -- DP with Three States
# Time: O(n)  |  Space: O(n)
# buy[i]  = max profit on day i if we hold a stock
# sell[i] = max profit on day i if we just sold
# cool[i] = max profit on day i if we are in cooldown/idle
# ============================================================
def optimal(prices: List[int]) -> int:
    n = len(prices)
    if n <= 1:
        return 0

    buy = [0] * n
    sell = [0] * n
    cool = [0] * n

    buy[0] = -prices[0]
    sell[0] = 0   # can't sell on day 0
    cool[0] = 0

    for i in range(1, n):
        buy[i] = max(buy[i - 1], cool[i - 1] - prices[i])
        sell[i] = buy[i - 1] + prices[i]
        cool[i] = max(cool[i - 1], sell[i - 1])

    return max(sell[n - 1], cool[n - 1])


# ============================================================
# APPROACH 3: BEST -- Space-Optimized 3 Variables
# Time: O(n)  |  Space: O(1)
# Only need previous day's buy, sell, cool values.
# ============================================================
def best(prices: List[int]) -> int:
    n = len(prices)
    if n <= 1:
        return 0

    prev_buy = -prices[0]
    prev_sell = 0
    prev_cool = 0

    for i in range(1, n):
        curr_buy = max(prev_buy, prev_cool - prices[i])
        curr_sell = prev_buy + prices[i]
        curr_cool = max(prev_cool, prev_sell)

        prev_buy = curr_buy
        prev_sell = curr_sell
        prev_cool = curr_cool

    return max(prev_sell, prev_cool)


if __name__ == "__main__":
    print("=== Best Time with Cooldown ===\n")

    print(f"Brute:   {brute_force([1,2,3,0,2])}")   # 3
    print(f"Optimal: {optimal([1,2,3,0,2])}")         # 3
    print(f"Best:    {best([1,2,3,0,2])}")            # 3

    print(f"\nBrute:   {brute_force([1])}")            # 0
    print(f"Optimal: {optimal([1])}")                   # 0
    print(f"Best:    {best([1])}")                      # 0

    print(f"\nDecreasing: {best([5,4,3,2,1])}")        # 0
    print(f"Two days:   {best([1,4])}")                 # 3
