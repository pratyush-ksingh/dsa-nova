"""
Problem: Minimum Falling Path Sum (LeetCode #931)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(3^n)  |  Space: O(n) recursion stack
# Try all paths from top row going down/down-left/down-right.
# ============================================================
def brute_force(matrix: List[List[int]]) -> int:
    n = len(matrix)

    def solve(r: int, c: int) -> int:
        if c < 0 or c >= n:
            return math.inf
        if r == 0:
            return matrix[0][c]
        return matrix[r][c] + min(
            solve(r - 1, c - 1),
            solve(r - 1, c),
            solve(r - 1, c + 1)
        )

    return min(solve(n - 1, c) for c in range(n))


# ============================================================
# APPROACH 2: OPTIMAL -- DP Tabulation
# Time: O(n^2)  |  Space: O(n^2)
# Build dp table bottom-up: dp[r][c] = min path sum ending at (r,c).
# ============================================================
def optimal(matrix: List[List[int]]) -> int:
    n = len(matrix)
    dp = [[0] * n for _ in range(n)]

    for c in range(n):
        dp[0][c] = matrix[0][c]

    for r in range(1, n):
        for c in range(n):
            up = dp[r - 1][c]
            up_left = dp[r - 1][c - 1] if c > 0 else math.inf
            up_right = dp[r - 1][c + 1] if c < n - 1 else math.inf
            dp[r][c] = matrix[r][c] + min(up, up_left, up_right)

    return min(dp[n - 1])


# ============================================================
# APPROACH 3: BEST -- Space-Optimized DP (1D array)
# Time: O(n^2)  |  Space: O(n)
# Only keep previous row's DP values.
# ============================================================
def best(matrix: List[List[int]]) -> int:
    n = len(matrix)
    prev = matrix[0][:]

    for r in range(1, n):
        curr = [0] * n
        for c in range(n):
            up = prev[c]
            up_left = prev[c - 1] if c > 0 else math.inf
            up_right = prev[c + 1] if c < n - 1 else math.inf
            curr[c] = matrix[r][c] + min(up, up_left, up_right)
        prev = curr

    return min(prev)


if __name__ == "__main__":
    print("=== Minimum Falling Path Sum ===\n")

    m1 = [[2, 1, 3], [6, 5, 4], [7, 8, 9]]
    print(f"Brute:   {brute_force(m1)}")   # 13
    print(f"Optimal: {optimal(m1)}")        # 13
    print(f"Best:    {best(m1)}")           # 13

    m2 = [[-19, 57], [-40, -5]]
    print(f"\nBrute:   {brute_force(m2)}")  # -59
    print(f"Optimal: {optimal(m2)}")        # -59
    print(f"Best:    {best(m2)}")           # -59

    m3 = [[-48]]
    print(f"\nSingle:  {best(m3)}")          # -48
