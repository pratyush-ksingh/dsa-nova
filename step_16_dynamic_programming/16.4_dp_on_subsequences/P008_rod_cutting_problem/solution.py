"""
Problem: Rod Cutting Problem
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(2^n)  |  Space: O(n) recursion stack
# Model as unbounded knapsack: lengths 1..n are item sizes,
# prices are values. Try including/excluding each length.
# ============================================================
def brute_force(prices: List[int], n: int) -> int:
    # lengths[i] = i+1, prices[i] = price for that length

    def solve(idx: int, remaining: int) -> int:
        if idx == 0:
            # Can only use length-1 pieces
            return remaining * prices[0]
        not_cut = solve(idx - 1, remaining)
        cut = 0
        piece_len = idx + 1
        if piece_len <= remaining:
            cut = prices[idx] + solve(idx, remaining - piece_len)
        return max(not_cut, cut)

    return solve(n - 1, n)


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(n^2)  |  Space: O(n^2)
# dp[i][j] = max profit for rod of length j using piece
# lengths 1..i+1 (each reusable unlimited times).
# ============================================================
def optimal(prices: List[int], n: int) -> int:
    dp = [[0] * (n + 1) for _ in range(n)]

    # Base: only length-1 pieces
    for j in range(n + 1):
        dp[0][j] = j * prices[0]

    for i in range(1, n):
        piece_len = i + 1
        for j in range(n + 1):
            not_cut = dp[i - 1][j]
            cut = 0
            if piece_len <= j:
                cut = prices[i] + dp[i][j - piece_len]  # stay on row i (reuse)
            dp[i][j] = max(not_cut, cut)

    return dp[n - 1][n]


# ============================================================
# APPROACH 3: BEST -- 1D Space-Optimized DP
# Time: O(n^2)  |  Space: O(n)
# Left-to-right traversal enables reuse (unbounded behavior).
# ============================================================
def best(prices: List[int], n: int) -> int:
    dp = [0] * (n + 1)

    # Base: only length-1 pieces
    for j in range(n + 1):
        dp[j] = j * prices[0]

    for i in range(1, n):
        piece_len = i + 1
        for j in range(piece_len, n + 1):  # left to right
            dp[j] = max(dp[j], prices[i] + dp[j - piece_len])

    return dp[n]


if __name__ == "__main__":
    print("=== Rod Cutting Problem ===\n")

    # prices[i] = price of rod of length i+1
    prices1 = [1, 5, 8, 9, 10, 17, 17, 20]
    n1 = 8
    print(f"Brute:   {brute_force(prices1, n1)}")   # 22
    print(f"Optimal: {optimal(prices1, n1)}")         # 22
    print(f"Best:    {best(prices1, n1)}")            # 22

    prices2 = [3, 5, 8, 9, 10, 17, 17, 20]
    n2 = 8
    print(f"\nBrute:   {brute_force(prices2, n2)}")  # 24
    print(f"Optimal: {optimal(prices2, n2)}")          # 24
    print(f"Best:    {best(prices2, n2)}")             # 24

    # Edge: length 1
    print(f"\nLength 1: {best([5], 1)}")  # 5
