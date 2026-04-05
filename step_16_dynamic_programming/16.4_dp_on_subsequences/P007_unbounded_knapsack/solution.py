"""
Problem: Unbounded Knapsack
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion (with repetition)
# Time: O(n^W)  |  Space: O(W) recursion stack
# Each item can be taken unlimited times; try all picks.
# ============================================================
def brute_force(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)

    def solve(idx: int, cap: int) -> int:
        if idx == 0:
            # Take as many of item 0 as possible
            return (cap // weights[0]) * values[0]
        # Not take current item
        not_take = solve(idx - 1, cap)
        # Take current item (stay at idx -- can take again)
        take = 0
        if weights[idx] <= cap:
            take = values[idx] + solve(idx, cap - weights[idx])
        return max(not_take, take)

    return solve(n - 1, W)


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(n * W)  |  Space: O(n * W)
# dp[i][w] = max value using items 0..i with capacity w,
# each item usable unlimited times.
# ============================================================
def optimal(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)
    dp = [[0] * (W + 1) for _ in range(n)]

    # Base case: item 0 can be taken as many times as fits
    for w in range(W + 1):
        dp[0][w] = (w // weights[0]) * values[0]

    for i in range(1, n):
        for w in range(W + 1):
            not_take = dp[i - 1][w]
            take = 0
            if weights[i] <= w:
                # Stay on row i (not i-1) -- allows reuse
                take = values[i] + dp[i][w - weights[i]]
            dp[i][w] = max(not_take, take)

    return dp[n - 1][W]


# ============================================================
# APPROACH 3: BEST -- Space-Optimized 1D DP
# Time: O(n * W)  |  Space: O(W)
# Unlike 0/1 knapsack, iterate LEFT to RIGHT so that when
# dp[w - wt[i]] is read it already includes item i again
# (allowing unlimited reuse).
# ============================================================
def best(weights: List[int], values: List[int], W: int) -> int:
    n = len(weights)
    dp = [0] * (W + 1)

    # Base case: item 0
    for w in range(W + 1):
        dp[w] = (w // weights[0]) * values[0]

    for i in range(1, n):
        for w in range(weights[i], W + 1):  # left to right
            dp[w] = max(dp[w], values[i] + dp[w - weights[i]])

    return dp[W]


if __name__ == "__main__":
    print("=== Unbounded Knapsack ===\n")

    wt = [2, 4, 6]
    val = [5, 11, 13]
    W = 10
    print(f"Brute:   {brute_force(wt, val, W)}")   # 27
    print(f"Optimal: {optimal(wt, val, W)}")         # 27
    print(f"Best:    {best(wt, val, W)}")            # 27

    wt2 = [1, 3, 4, 5]
    val2 = [1, 4, 5, 7]
    W2 = 8
    print(f"\nBrute:   {brute_force(wt2, val2, W2)}")  # 11
    print(f"Optimal: {optimal(wt2, val2, W2)}")          # 11
    print(f"Best:    {best(wt2, val2, W2)}")             # 11

    # Edge: only one item type
    print(f"\nSingle item: {best([3], [10], 9)}")  # 30
