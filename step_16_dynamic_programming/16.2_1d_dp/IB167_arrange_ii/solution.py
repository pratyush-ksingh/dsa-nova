"""
Problem: Arrange II
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a string of characters (e.g., "aab") and an integer k, split the string
into k contiguous groups. The cost of a group is (length of group) * (number
of distinct characters in group). Minimize the total cost.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursion trying all split points
# Time: O(n^k * n)  |  Space: O(n * k)
# ============================================================
def brute_force(s: str, k: int) -> int:
    """
    Recursively try every way to split s into k contiguous groups.
    """
    n = len(s)
    if k > n:
        return 0  # or handle edge case

    def cost(l, r):
        """Cost of substring s[l:r+1]."""
        distinct = len(set(s[l:r + 1]))
        return (r - l + 1) * distinct

    def solve(idx, parts):
        if parts == 1:
            return cost(idx, n - 1)
        min_cost = float('inf')
        # Need at least 1 char per remaining part
        for split in range(idx, n - parts + 1):
            min_cost = min(min_cost, cost(idx, split) + solve(split + 1, parts - 1))
        return min_cost

    return solve(0, k)


# ============================================================
# APPROACH 2: OPTIMAL — DP with memoization
# Time: O(n^2 * k)  |  Space: O(n * k + n^2)
# ============================================================
def optimal(s: str, k: int) -> int:
    """
    dp[i][j] = min cost to split s[i:] into j groups.
    Precompute cost(l, r) for all pairs.
    """
    n = len(s)
    if k > n:
        return 0

    # Precompute costs
    cost_table = [[0] * n for _ in range(n)]
    for i in range(n):
        chars = set()
        for j in range(i, n):
            chars.add(s[j])
            cost_table[i][j] = (j - i + 1) * len(chars)

    memo = {}

    def dp(idx, parts):
        if parts == 1:
            return cost_table[idx][n - 1]
        if (idx, parts) in memo:
            return memo[(idx, parts)]
        min_cost = float('inf')
        for split in range(idx, n - parts + 1):
            min_cost = min(min_cost, cost_table[idx][split] + dp(split + 1, parts - 1))
        memo[(idx, parts)] = min_cost
        return min_cost

    return dp(0, k)


# ============================================================
# APPROACH 3: BEST — Bottom-up DP
# Time: O(n^2 * k)  |  Space: O(n * k)
# ============================================================
def best(s: str, k: int) -> int:
    """
    Bottom-up DP. dp[j][i] = min cost to partition s[0:i+1] into j groups.
    """
    n = len(s)
    if k > n:
        return 0

    # Precompute costs
    cost_table = [[0] * n for _ in range(n)]
    for i in range(n):
        chars = set()
        for j in range(i, n):
            chars.add(s[j])
            cost_table[i][j] = (j - i + 1) * len(chars)

    INF = float('inf')
    # dp[j][i] = min cost to split s[0..i] into j groups
    dp = [[INF] * n for _ in range(k + 1)]

    # Base: 1 group
    for i in range(n):
        dp[1][i] = cost_table[0][i]

    for j in range(2, k + 1):
        for i in range(j - 1, n):
            for split in range(j - 2, i):
                dp[j][i] = min(dp[j][i], dp[j - 1][split] + cost_table[split + 1][i])

    return dp[k][n - 1]


if __name__ == "__main__":
    print("=== Arrange II ===")

    s, k = "aab", 2
    print(f"Brute:   {brute_force(s, k)}")   # cost("a","ab")=1*1+2*2=5 or ("aa","b")=2*1+1*1=3 -> 3
    print(f"Optimal: {optimal(s, k)}")
    print(f"Best:    {best(s, k)}")

    s, k = "abcab", 3
    print(f"Brute:   {brute_force(s, k)}")
    print(f"Optimal: {optimal(s, k)}")
    print(f"Best:    {best(s, k)}")
