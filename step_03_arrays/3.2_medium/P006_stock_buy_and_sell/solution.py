"""
Problem: Stock Buy and Sell (Multiple Transactions Allowed)
Difficulty: EASY | XP: 10

Find the maximum profit from unlimited buy/sell transactions.
Must sell before buying again.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Recursion -- try all states)
# Time: O(2^n) | Space: O(n) recursion stack
# ============================================================
def brute_force(prices: List[int]) -> int:
    """Recursively explore all buy/sell/skip decisions."""
    def solve(day: int, holding: bool) -> int:
        if day >= len(prices):
            return 0

        skip = solve(day + 1, holding)

        if holding:
            sell = prices[day] + solve(day + 1, False)
            return max(skip, sell)
        else:
            buy = -prices[day] + solve(day + 1, True)
            return max(skip, buy)

    return solve(0, False)


# ============================================================
# APPROACH 2: OPTIMAL (Greedy -- sum positive differences)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(prices: List[int]) -> int:
    """Sum all positive consecutive differences."""
    profit = 0
    for i in range(1, len(prices)):
        if prices[i] > prices[i - 1]:
            profit += prices[i] - prices[i - 1]
    return profit


# ============================================================
# APPROACH 3: BEST (Valley-Peak -- explicit trade pairs)
# Time: O(n) | Space: O(1)
# ============================================================
def best(prices: List[int]) -> int:
    """Find each valley-peak pair and sum the gains."""
    n = len(prices)
    i = 0
    profit = 0

    while i < n - 1:
        # Find valley
        while i < n - 1 and prices[i] >= prices[i + 1]:
            i += 1
        valley = prices[i]

        # Find peak
        while i < n - 1 and prices[i] <= prices[i + 1]:
            i += 1
        peak = prices[i]

        profit += peak - valley

    return profit


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Stock Buy and Sell ===\n")

    test_cases = [
        ([7, 1, 5, 3, 6, 4], 7),
        ([1, 2, 3, 4, 5], 4),
        ([7, 6, 4, 3, 1], 0),
        ([5], 0),
        ([1, 5], 4),
        ([3, 3, 3], 0),
        ([1, 5, 1, 5], 8),
    ]

    for prices, expected in test_cases:
        b = brute_force(prices)
        o = optimal(prices)
        be = best(prices)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    {prices}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
