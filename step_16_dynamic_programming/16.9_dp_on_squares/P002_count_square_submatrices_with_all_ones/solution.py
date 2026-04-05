"""
Problem: Count Square Submatrices with All Ones (LeetCode #1277)
Difficulty: MEDIUM | XP: 25

dp[i][j] = size of largest all-ones square ending at (i,j).
Answer = sum of all dp[i][j].
Approaches: Brute Force -> Recursive DP -> Tabulation -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check every possible square)
# Time: O(m * n * min(m,n)^2) | Space: O(1)
# ============================================================
def count_squares_brute(matrix: List[List[int]]) -> int:
    """For each cell, try all square sizes and check if all ones."""
    m, n = len(matrix), len(matrix[0])
    count = 0

    for i in range(m):
        for j in range(n):
            for k in range(1, min(m - i, n - j) + 1):
                if all(matrix[r][c] == 1
                       for r in range(i, i + k)
                       for c in range(j, j + k)):
                    count += 1
                else:
                    break  # Larger squares will also fail
    return count


# ============================================================
# APPROACH 2: MEMOIZATION (Recursive DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def count_squares_memo(matrix: List[List[int]]) -> int:
    """Recursive solve(i,j) = largest square ending at (i,j)."""
    m, n = len(matrix), len(matrix[0])
    dp = {}

    def solve(i: int, j: int) -> int:
        if i < 0 or j < 0:
            return 0
        if matrix[i][j] == 0:
            return 0
        if (i, j) in dp:
            return dp[(i, j)]

        up = solve(i - 1, j)
        left = solve(i, j - 1)
        diag = solve(i - 1, j - 1)

        dp[(i, j)] = min(up, left, diag) + 1
        return dp[(i, j)]

    count = 0
    for i in range(m):
        for j in range(n):
            count += solve(i, j)
    return count


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def count_squares_tab(matrix: List[List[int]]) -> int:
    """Build dp table: dp[i][j] = min(up, left, diag) + 1. Sum all."""
    m, n = len(matrix), len(matrix[0])
    dp = [[0] * n for _ in range(m)]
    count = 0

    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 1:
                if i == 0 or j == 0:
                    dp[i][j] = 1
                else:
                    dp[i][j] = min(dp[i - 1][j], dp[i][j - 1],
                                   dp[i - 1][j - 1]) + 1
            count += dp[i][j]

    return count


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(m * n) | Space: O(n)
# ============================================================
def count_squares_space(matrix: List[List[int]]) -> int:
    """Use single 1D prev array. O(n) space."""
    m, n = len(matrix), len(matrix[0])
    prev = list(matrix[0])
    count = sum(prev)

    for i in range(1, m):
        curr = [0] * n
        curr[0] = matrix[i][0]
        count += curr[0]

        for j in range(1, n):
            if matrix[i][j] == 1:
                curr[j] = min(prev[j], curr[j - 1], prev[j - 1]) + 1
            count += curr[j]

        prev = curr

    return count


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Count Square Submatrices with All Ones ===\n")

    test_cases = [
        ([[0, 1, 1, 1], [1, 1, 1, 1], [0, 1, 1, 1]], 15),
        ([[1, 0, 1], [1, 1, 0], [1, 1, 0]], 7),
        ([[1]], 1),
        ([[0]], 0),
        ([[1, 1], [1, 1]], 5),
        ([[0, 0], [0, 0]], 0),
    ]

    for matrix, expected in test_cases:
        b = count_squares_brute(matrix)
        m = count_squares_memo(matrix)
        t = count_squares_tab(matrix)
        s = count_squares_space(matrix)

        passes = b == expected and m == expected and t == expected and s == expected
        print(f"Matrix: {matrix}")
        print(f"  Brute={b} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
