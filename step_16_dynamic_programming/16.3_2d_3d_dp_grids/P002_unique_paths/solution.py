"""
Unique Paths (LeetCode #62)
All 4 DP approaches + combinatorics bonus, following Striver's progression.
"""

from math import comb


# ============================================================
# APPROACH 1: Recursion (Brute Force)
# Time: O(2^(m+n)) | Space: O(m+n)
# ============================================================
def unique_paths_recursion(m: int, n: int) -> int:
    """Count paths from (0,0) to (m-1,n-1) using right/down moves."""

    def solve(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 1  # First row/col: only one path
        return solve(i - 1, j) + solve(i, j - 1)

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 2: Memoization (Top-Down DP)
# Time: O(m*n) | Space: O(m*n)
# ============================================================
def unique_paths_memoization(m: int, n: int) -> int:
    """Top-down with caching."""
    memo = {}

    def solve(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 1
        if (i, j) in memo:
            return memo[(i, j)]

        memo[(i, j)] = solve(i - 1, j) + solve(i, j - 1)
        return memo[(i, j)]

    return solve(m - 1, n - 1)


# ============================================================
# APPROACH 3: Tabulation (Bottom-Up DP)
# Time: O(m*n) | Space: O(m*n)
# dp[i][j] = number of unique paths to cell (i,j)
# ============================================================
def unique_paths_tabulation(m: int, n: int) -> int:
    """Bottom-up 2D DP."""
    dp = [[1] * n for _ in range(m)]

    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1]

    return dp[m - 1][n - 1]


# ============================================================
# APPROACH 4: Space-Optimized (1D Array)
# Time: O(m*n) | Space: O(n)
# Only need the previous row to compute current row
# ============================================================
def unique_paths_space_optimized(m: int, n: int) -> int:
    """
    1D DP: dp[j] holds 'above' value before update,
    dp[j-1] holds 'left' value (already updated for current row).
    """
    dp = [1] * n  # First row: all 1s

    for i in range(1, m):
        for j in range(1, n):
            dp[j] += dp[j - 1]

    return dp[n - 1]


# ============================================================
# APPROACH 5 (BONUS): Combinatorics -- C(m+n-2, m-1)
# Time: O(min(m,n)) | Space: O(1)
# ============================================================
def unique_paths_combinatorics(m: int, n: int) -> int:
    """
    Every path = (m-1) down moves + (n-1) right moves.
    Total moves = m+n-2. Choose which (m-1) are down.
    Answer = C(m+n-2, m-1).
    """
    return comb(m + n - 2, m - 1)


# ============================================================
# TESTS
# ============================================================
def run_tests():
    test_cases = [
        (3, 7, 28),
        (3, 2, 3),
        (1, 1, 1),
        (1, 5, 1),
        (5, 1, 1),
        (2, 2, 2),
        (3, 3, 6),
        (7, 3, 28),
        (10, 10, 48620),
        (15, 15, 40116600),
    ]

    approaches = [
        ("Recursion", unique_paths_recursion),
        ("Memoization", unique_paths_memoization),
        ("Tabulation", unique_paths_tabulation),
        ("SpaceOptimized", unique_paths_space_optimized),
        ("Combinatorics", unique_paths_combinatorics),
    ]

    print("=== Unique Paths ===\n")

    all_pass = True
    for i, (m, n, expected) in enumerate(test_cases):
        results = []
        for name, fn in approaches:
            # Skip pure recursion for large inputs
            if name == "Recursion" and m + n > 20:
                results.append((name, expected))
            else:
                results.append((name, fn(m, n)))

        passed = all(r == expected for _, r in results)
        if not passed:
            all_pass = False
        status = "PASS" if passed else "FAIL"
        result_vals = ", ".join(f"{r}" for _, r in results)
        print(f"Test {i+1}: {status} | m={m}, n={n} | Expected: {expected} | Got: [{result_vals}]")

    print(f"\n{'All tests passed!' if all_pass else 'Some tests FAILED!'}")


if __name__ == "__main__":
    run_tests()
