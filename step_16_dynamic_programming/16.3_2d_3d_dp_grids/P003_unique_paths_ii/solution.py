"""
Problem: Unique Paths II (LeetCode #63)
Difficulty: MEDIUM | XP: 25

Grid with obstacles. Count paths from top-left to bottom-right.
Only move right or down. Obstacles block paths.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^(m+n)) | Space: O(m+n)
# ============================================================
def unique_paths_recursive(grid: List[List[int]]) -> int:
    """Recursively count paths, returning 0 at obstacles."""
    m, n = len(grid), len(grid[0])
    if grid[0][0] == 1 or grid[m - 1][n - 1] == 1:
        return 0

    def solve(i: int, j: int) -> int:
        if i < 0 or j < 0:
            return 0
        if grid[i][j] == 1:
            return 0
        if i == 0 and j == 0:
            return 1
        return solve(i - 1, j) + solve(i, j - 1)

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def unique_paths_memo(grid: List[List[int]]) -> int:
    """Cache (i, j) states to avoid recomputation."""
    m, n = len(grid), len(grid[0])
    if grid[0][0] == 1 or grid[m - 1][n - 1] == 1:
        return 0

    dp = {}

    def solve(i: int, j: int) -> int:
        if i < 0 or j < 0:
            return 0
        if grid[i][j] == 1:
            return 0
        if i == 0 and j == 0:
            return 1
        if (i, j) in dp:
            return dp[(i, j)]

        dp[(i, j)] = solve(i - 1, j) + solve(i, j - 1)
        return dp[(i, j)]

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def unique_paths_tab(grid: List[List[int]]) -> int:
    """Build dp[m][n] table iteratively."""
    m, n = len(grid), len(grid[0])
    if grid[0][0] == 1 or grid[m - 1][n - 1] == 1:
        return 0

    dp = [[0] * n for _ in range(m)]
    dp[0][0] = 1

    # First row
    for j in range(1, n):
        dp[0][j] = dp[0][j - 1] if grid[0][j] == 0 else 0

    # First column
    for i in range(1, m):
        dp[i][0] = dp[i - 1][0] if grid[i][0] == 0 else 0

    # Fill rest
    for i in range(1, m):
        for j in range(1, n):
            if grid[i][j] == 1:
                dp[i][j] = 0
            else:
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]

    return dp[m - 1][n - 1]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(m * n) | Space: O(n)
# ============================================================
def unique_paths_space(grid: List[List[int]]) -> int:
    """Use single 1D array for previous row. O(n) space."""
    m, n = len(grid), len(grid[0])
    if grid[0][0] == 1 or grid[m - 1][n - 1] == 1:
        return 0

    prev = [0] * n
    prev[0] = 1

    # First row
    for j in range(1, n):
        prev[j] = prev[j - 1] if grid[0][j] == 0 else 0

    for i in range(1, m):
        curr = [0] * n
        curr[0] = prev[0] if grid[i][0] == 0 else 0

        for j in range(1, n):
            if grid[i][j] == 1:
                curr[j] = 0
            else:
                curr[j] = prev[j] + curr[j - 1]

        prev = curr

    return prev[n - 1]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Unique Paths II ===\n")

    test_cases = [
        ([[0, 0, 0], [0, 1, 0], [0, 0, 0]], 2),
        ([[0, 1], [0, 0]], 1),
        ([[1, 0]], 0),
        ([[0, 0], [0, 1]], 0),
        ([[0]], 1),
        ([[1]], 0),
        ([[0, 1, 0]], 0),
        ([[0, 0, 0, 0]], 1),
    ]

    for grid, expected in test_cases:
        r = unique_paths_recursive(grid)
        m = unique_paths_memo(grid)
        t = unique_paths_tab(grid)
        s = unique_paths_space(grid)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"Grid: {grid}")
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
