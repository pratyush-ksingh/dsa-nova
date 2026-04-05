"""
Problem: Triangle Minimum Path Sum (LeetCode #120)
Difficulty: MEDIUM | XP: 25

Min path sum from top to bottom of triangle.
Bottom-up approach makes space optimization elegant.
All 4 DP approaches.
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION (Top-Down from peak)
# Time: O(2^n) | Space: O(n) stack
# ============================================================
def min_total_recursive(triangle: list[list[int]]) -> int:
    """From each position, try going down or down-right. Return min path sum."""
    n = len(triangle)

    def solve(i: int, j: int) -> int:
        if i == n - 1:
            return triangle[i][j]  # base: bottom row
        go_down = solve(i + 1, j)
        go_right = solve(i + 1, j + 1)
        return triangle[i][j] + min(go_down, go_right)

    return solve(0, 0)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def min_total_memo(triangle: list[list[int]]) -> int:
    """Cache results for each (row, col) position."""
    n = len(triangle)
    cache = {}

    def solve(i: int, j: int) -> int:
        if i == n - 1:
            return triangle[i][j]
        if (i, j) in cache:
            return cache[(i, j)]

        go_down = solve(i + 1, j)
        go_right = solve(i + 1, j + 1)
        cache[(i, j)] = triangle[i][j] + min(go_down, go_right)
        return cache[(i, j)]

    return solve(0, 0)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def min_total_tab(triangle: list[list[int]]) -> int:
    """Start from bottom row, work upward. dp[i][j] = min path from (i,j) to base."""
    n = len(triangle)
    dp = [[0] * (i + 1) for i in range(n)]

    # Initialize bottom row
    for j in range(n):
        dp[n - 1][j] = triangle[n - 1][j]

    # Fill from bottom to top
    for i in range(n - 2, -1, -1):
        for j in range(i + 1):
            dp[i][j] = triangle[i][j] + min(dp[i + 1][j], dp[i + 1][j + 1])

    return dp[0][0]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED (1D array, bottom-up)
# Time: O(n^2) | Space: O(n)
# ============================================================
def min_total_space(triangle: list[list[int]]) -> int:
    """
    Use single 1D array = bottom row. Fold upward in place.
    Process j left-to-right: dp[j+1] hasn't been overwritten yet.
    """
    n = len(triangle)

    # Start with bottom row
    dp = triangle[n - 1][:]

    # Fold upward
    for i in range(n - 2, -1, -1):
        for j in range(i + 1):
            dp[j] = triangle[i][j] + min(dp[j], dp[j + 1])

    return dp[0]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Triangle Minimum Path Sum (LeetCode #120) ===\n")

    test_cases = [
        ([[2], [3, 4], [6, 5, 7], [4, 1, 8, 3]], 11),
        ([[-10]], -10),
        ([[1], [2, 3]], 3),
        ([[-1], [2, 3], [1, -1, -3]], -1),
        ([[1], [-2, -3]], -2),
    ]

    header = f"{'Triangle':<40} {'Rec':<8} {'Memo':<8} {'Tab':<8} {'Space':<8} {'Exp':<8} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for triangle, expected in test_cases:
        r = min_total_recursive(triangle)
        m = min_total_memo(triangle)
        t = min_total_tab(triangle)
        s = min_total_space(triangle)

        passes = r == expected and m == expected and t == expected and s == expected
        desc = f"{len(triangle)} rows"
        print(f"{desc:<40} {r:<8} {m:<8} {t:<8} {s:<8} {expected:<8} {passes}")

    # Visualization
    print("\n--- Bottom-Up DP Visualization ---")
    tri = [[2], [3, 4], [6, 5, 7], [4, 1, 8, 3]]
    print("Triangle:")
    for i, row in enumerate(tri):
        indent = " " * (len(tri) - i - 1)
        print(f"  {indent}{' '.join(str(x) for x in row)}")

    print("\nDP (bottom to top):")
    dp = tri[-1][:]
    print(f"  dp = {dp}   <- bottom row")
    for i in range(len(tri) - 2, -1, -1):
        details = []
        for j in range(i + 1):
            old_j, old_j1 = dp[j], dp[j + 1]
            dp[j] = tri[i][j] + min(dp[j], dp[j + 1])
            details.append(f"dp[{j}]={tri[i][j]}+min({old_j},{old_j1})={dp[j]}")
        print(f"  i={i}: {', '.join(details)}")
        print(f"       dp = {dp}")

    print(f"\n  Answer: dp[0] = {dp[0]}")

    # Path reconstruction
    print("\n--- Path Reconstruction ---")
    tri = [[2], [3, 4], [6, 5, 7], [4, 1, 8, 3]]
    n = len(tri)
    dp_full = [[0] * (i + 1) for i in range(n)]
    for j in range(n):
        dp_full[n - 1][j] = tri[n - 1][j]
    for i in range(n - 2, -1, -1):
        for j in range(i + 1):
            dp_full[i][j] = tri[i][j] + min(dp_full[i + 1][j], dp_full[i + 1][j + 1])

    path = []
    j = 0
    for i in range(n):
        path.append(tri[i][j])
        if i < n - 1:
            if dp_full[i + 1][j + 1] < dp_full[i + 1][j]:
                j += 1
    print(f"Best path: {' -> '.join(map(str, path))} = {sum(path)}")
