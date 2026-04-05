"""
Problem: 0/1 Knapsack
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(2^n)  |  Space: O(n) recursion stack
# For each item, try include/exclude and pick max value.
# ============================================================
def brute_force(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)

    def solve(idx: int, cap: int) -> int:
        if idx == 0:
            return values[0] if weights[0] <= cap else 0
        # Not take
        not_take = solve(idx - 1, cap)
        # Take
        take = 0
        if weights[idx] <= cap:
            take = values[idx] + solve(idx - 1, cap - weights[idx])
        return max(not_take, take)

    return solve(n - 1, W)


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(n * W)  |  Space: O(n * W)
# dp[i][w] = max value using items 0..i with capacity w.
# ============================================================
def optimal(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)
    dp = [[0] * (W + 1) for _ in range(n)]

    # Base case: item 0
    for w in range(weights[0], W + 1):
        dp[0][w] = values[0]

    for i in range(1, n):
        for w in range(W + 1):
            not_take = dp[i - 1][w]
            take = 0
            if weights[i] <= w:
                take = values[i] + dp[i - 1][w - weights[i]]
            dp[i][w] = max(not_take, take)

    return dp[n - 1][W]


# ============================================================
# APPROACH 3: BEST -- Space-Optimized 1D DP
# Time: O(n * W)  |  Space: O(W)
# Single array, iterate capacity RIGHT to LEFT to avoid
# using updated values from the same row.
# ============================================================
def best(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)
    dp = [0] * (W + 1)

    # Base case: item 0
    for w in range(weights[0], W + 1):
        dp[w] = values[0]

    for i in range(1, n):
        for w in range(W, weights[i] - 1, -1):  # right to left
            dp[w] = max(dp[w], values[i] + dp[w - weights[i]])

    return dp[W]


if __name__ == "__main__":
    print("=== 0/1 Knapsack ===\n")

    wt = [1, 2, 4, 5]
    val = [1, 4, 5, 7]
    W = 7

    print(f"Brute:   {brute_force(wt, val, W)}")   # 11
    print(f"Optimal: {optimal(wt, val, W)}")         # 11
    print(f"Best:    {best(wt, val, W)}")            # 11

    wt2 = [3, 4, 5]
    val2 = [30, 50, 60]
    W2 = 8

    print(f"\nBrute:   {brute_force(wt2, val2, W2)}")  # 90
    print(f"Optimal: {optimal(wt2, val2, W2)}")          # 90
    print(f"Best:    {best(wt2, val2, W2)}")             # 90

    # Edge: capacity 0
    print(f"\nZero cap: {best([1, 2], [3, 4], 0)}")     # 0
