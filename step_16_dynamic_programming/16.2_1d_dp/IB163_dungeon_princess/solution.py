"""
Problem: Dungeon Princess
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a 2D grid where each cell has a value (positive = health gain,
negative = damage), find the minimum initial health needed for a knight
starting at top-left to reach bottom-right princess, always keeping HP >= 1.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursion with memoization (top-down)
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def brute_force(dungeon: List[List[int]]) -> int:
    """
    Recursively compute minimum HP needed at each cell to reach (m-1, n-1).
    At each cell we can go right or down.
    min_hp(i,j) = max(1, min(min_hp(i+1,j), min_hp(i,j+1)) - dungeon[i][j])
    """
    if not dungeon or not dungeon[0]:
        return 1
    m, n = len(dungeon), len(dungeon[0])
    memo = {}

    def dp(i, j):
        if i >= m or j >= n:
            return float('inf')
        if (i, j) in memo:
            return memo[(i, j)]
        if i == m - 1 and j == n - 1:
            memo[(i, j)] = max(1, 1 - dungeon[i][j])
            return memo[(i, j)]

        right = dp(i, j + 1)
        down = dp(i + 1, j)
        min_needed = max(1, min(right, down) - dungeon[i][j])
        memo[(i, j)] = min_needed
        return min_needed

    return dp(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — Bottom-up DP table
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def optimal(dungeon: List[List[int]]) -> int:
    """
    Fill dp table from bottom-right to top-left.
    dp[i][j] = minimum HP needed when entering cell (i,j).
    """
    if not dungeon or not dungeon[0]:
        return 1
    m, n = len(dungeon), len(dungeon[0])
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    # Sentinel values: cells beyond grid need infinite HP (unreachable)
    for i in range(m + 1):
        dp[i][n] = float('inf')
    for j in range(n + 1):
        dp[m][j] = float('inf')
    # Except the two cells adjacent to the princess
    dp[m][n - 1] = dp[m - 1][n] = 1

    for i in range(m - 1, -1, -1):
        for j in range(n - 1, -1, -1):
            min_next = min(dp[i + 1][j], dp[i][j + 1])
            dp[i][j] = max(1, min_next - dungeon[i][j])

    return dp[0][0]


# ============================================================
# APPROACH 3: BEST — Bottom-up DP with O(n) space
# Time: O(m * n)  |  Space: O(n)
# ============================================================
def best(dungeon: List[List[int]]) -> int:
    """
    Space-optimized: only keep one row of dp at a time,
    processing from bottom-right to top-left.
    """
    if not dungeon or not dungeon[0]:
        return 1
    m, n = len(dungeon), len(dungeon[0])
    dp = [float('inf')] * (n + 1)

    for i in range(m - 1, -1, -1):
        new_dp = [float('inf')] * (n + 1)
        for j in range(n - 1, -1, -1):
            if i == m - 1 and j == n - 1:
                min_next = 1
            else:
                min_next = min(dp[j], new_dp[j + 1])
            new_dp[j] = max(1, min_next - dungeon[i][j])
        dp = new_dp

    return dp[0]


if __name__ == "__main__":
    print("=== Dungeon Princess ===")

    dungeon = [[-2, -3, 3], [-5, -10, 1], [10, 30, -5]]
    print(f"Brute:   {brute_force(dungeon)}")   # 7
    print(f"Optimal: {optimal(dungeon)}")       # 7
    print(f"Best:    {best(dungeon)}")          # 7

    dungeon = [[0]]
    print(f"Brute:   {brute_force(dungeon)}")   # 1
    print(f"Optimal: {optimal(dungeon)}")       # 1
    print(f"Best:    {best(dungeon)}")          # 1
