"""
Problem: Best Time to Buy and Sell Stock I (LeetCode #121)
Difficulty: EASY | XP: 10

Given array prices, find max profit from one buy and one sell.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (All Pairs)
# Time: O(n^2) | Space: O(1)
# ============================================================
def max_profit_brute(prices: List[int]) -> int:
    """Check every (buy, sell) pair where buy < sell."""
    max_profit = 0
    n = len(prices)

    for i in range(n - 1):
        for j in range(i + 1, n):
            profit = prices[j] - prices[i]
            max_profit = max(max_profit, profit)

    return max_profit


# ============================================================
# APPROACH 2: OPTIMAL (Single Pass with Running Minimum)
# Time: O(n) | Space: O(1)
# ============================================================
def max_profit_optimal(prices: List[int]) -> int:
    """Track running minimum price. At each day, check if selling gives best profit."""
    min_price = prices[0]
    max_profit = 0

    for i in range(1, len(prices)):
        # Could we profit by selling today?
        profit_today = prices[i] - min_price
        max_profit = max(max_profit, profit_today)

        # Update minimum price seen so far
        min_price = min(min_price, prices[i])

    return max_profit


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Best Time to Buy and Sell Stock I ===\n")

    test_cases = [
        ([7, 1, 5, 3, 6, 4], 5),
        ([7, 6, 4, 3, 1], 0),
        ([2, 4, 1], 2),
        ([1, 2], 1),
        ([5], 0),
        ([3, 3, 3, 3], 0),
        ([1, 2, 3, 4, 5], 4),
    ]

    for prices, expected in test_cases:
        b = max_profit_brute(prices)
        o = max_profit_optimal(prices)
        status = "PASS" if b == expected and o == expected else "FAIL"
        print(f"prices = {prices}")
        print(f"  Brute: {b} | Optimal: {o} | Expected: {expected}  [{status}]\n")
