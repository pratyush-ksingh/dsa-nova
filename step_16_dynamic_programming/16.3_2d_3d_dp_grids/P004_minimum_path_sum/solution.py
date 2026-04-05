"""
Problem: Minimum Path Sum (LeetCode #64)
Difficulty: MEDIUM | XP: 25

Min path sum from top-left to bottom-right, moving right or down only.
All 4 DP approaches.
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^(m+n)) | Space: O(m+n) stack
# ============================================================
def min_path_recursive(grid: list[list[int]]) -> int:
    """Recursively try coming from above or left."""
    m, n = len(grid), len(grid[0])

    def solve(i: int, j: int) -> int:
        if i == 0 and j == 0:
            return grid[0][0]
        if i < 0 or j < 0:
            return float('inf')

        from_above = solve(i - 1, j)
        from_left = solve(i, j - 1)
        return grid[i][j] + min(from_above, from_left)

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def min_path_memo(grid: list[list[int]]) -> int:
    """Cache results for each cell."""
    m, n = len(grid), len(grid[0])
    cache = {}

    def solve(i: int, j: int) -> int:
        if i == 0 and j == 0:
            return grid[0][0]
        if i < 0 or j < 0:
            return float('inf')
        if (i, j) in cache:
            return cache[(i, j)]

        from_above = solve(i - 1, j)
        from_left = solve(i, j - 1)
        cache[(i, j)] = grid[i][j] + min(from_above, from_left)
        return cache[(i, j)]

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def min_path_tab(grid: list[list[int]]) -> int:
    """Fill dp table row by row, left to right."""
    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]

    dp[0][0] = grid[0][0]

    # First row: can only come from left
    for j in range(1, n):
        dp[0][j] = dp[0][j - 1] + grid[0][j]

    # First column: can only come from above
    for i in range(1, m):
        dp[i][0] = dp[i - 1][0] + grid[i][0]

    # Inner cells
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = grid[i][j] + min(dp[i - 1][j], dp[i][j - 1])

    return dp[m - 1][n - 1]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED (1D array)
# Time: O(m * n) | Space: O(n)
# ============================================================
def min_path_space(grid: list[list[int]]) -> int:
    """
    Single 1D array. dp[j] before update = from above. dp[j-1] after update = from left.
    Process j left-to-right (opposite direction from longest common substring!).
    """
    m, n = len(grid), len(grid[0])
    dp = [0] * n

    # First row
    dp[0] = grid[0][0]
    for j in range(1, n):
        dp[j] = dp[j - 1] + grid[0][j]

    # Remaining rows
    for i in range(1, m):
        dp[0] += grid[i][0]  # first col: only from above
        for j in range(1, n):
            dp[j] = grid[i][j] + min(dp[j], dp[j - 1])

    return dp[n - 1]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Minimum Path Sum (LeetCode #64) ===\n")

    test_cases = [
        ([[1, 3, 1], [1, 5, 1], [4, 2, 1]], 7),
        ([[1, 2, 3], [4, 5, 6]], 12),
        ([[5]], 5),
        ([[1, 2], [1, 1]], 3),
        ([[0, 0, 0], [0, 0, 0]], 0),
    ]

    header = f"{'Grid':<30} {'Rec':<8} {'Memo':<8} {'Tab':<8} {'Space':<8} {'Exp':<8} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for grid, expected in test_cases:
        r = min_path_recursive(grid)
        m = min_path_memo(grid)
        t = min_path_tab(grid)
        s = min_path_space(grid)

        passes = r == expected and m == expected and t == expected and s == expected
        desc = f"{len(grid)}x{len(grid[0])}"
        print(f"{desc:<30} {r:<8} {m:<8} {t:<8} {s:<8} {expected:<8} {passes}")

    # Visualize DP table
    print("\n--- DP Table Visualization ---")
    grid = [[1, 3, 1], [1, 5, 1], [4, 2, 1]]
    print("Input grid:")
    for row in grid:
        print(f"  {row}")

    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]
    dp[0][0] = grid[0][0]
    for j in range(1, n):
        dp[0][j] = dp[0][j - 1] + grid[0][j]
    for i in range(1, m):
        dp[i][0] = dp[i - 1][0] + grid[i][0]
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = grid[i][j] + min(dp[i - 1][j], dp[i][j - 1])

    print("DP table:")
    for row in dp:
        print(f"  {row}")
    print(f"Min path sum = {dp[m - 1][n - 1]}")

    # Path reconstruction
    print("\n--- Path Reconstruction ---")
    path = []
    pi, pj = m - 1, n - 1
    path.append((pi, pj))
    while pi > 0 or pj > 0:
        if pi == 0:
            pj -= 1
        elif pj == 0:
            pi -= 1
        elif dp[pi - 1][pj] <= dp[pi][pj - 1]:
            pi -= 1
        else:
            pj -= 1
        path.append((pi, pj))
    path.reverse()
    print(f"Path: {' -> '.join(f'({r},{c})' for r, c in path)}")
    print(f"Values: {' -> '.join(str(grid[r][c]) for r, c in path)}")
