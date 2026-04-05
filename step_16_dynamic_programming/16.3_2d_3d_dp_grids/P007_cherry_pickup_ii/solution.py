"""
Problem: Cherry Pickup II (LeetCode 1463)
Difficulty: HARD | XP: 50

Given an rows x cols grid of non-negative integers, robot1 starts at (0,0)
and robot2 starts at (0, cols-1). Both move downward simultaneously, each
moving to one of 3 cells in the next row (diag-left, down, diag-right).
They collect cherries at each cell. If both robots are in the same cell,
cherries are only counted once. Return the maximum cherries both can collect.
"""
from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure Recursion
# Time: O(9^rows)  |  Space: O(rows) recursion stack
# At each row, both robots independently choose among 3 moves = 9 combos.
# No memoization, so many states are recomputed.
# ============================================================
def brute_force(grid: List[List[int]]) -> int:
    """
    Recursive exploration: at each row, try all 9 combinations of moves
    for both robots. Base case is when we reach the last row.
    """
    rows = len(grid)
    cols = len(grid[0])
    directions = [-1, 0, 1]

    def rec(row: int, c1: int, c2: int) -> int:
        cherries = grid[row][c1] + (grid[row][c2] if c2 != c1 else 0)
        if row == rows - 1:
            return cherries
        best = 0
        for d1 in directions:
            for d2 in directions:
                nc1, nc2 = c1 + d1, c2 + d2
                if 0 <= nc1 < cols and 0 <= nc2 < cols:
                    best = max(best, rec(row + 1, nc1, nc2))
        return cherries + best

    return rec(0, 0, cols - 1)


# ============================================================
# APPROACH 2: OPTIMAL — Top-Down 3D DP with Memoization
# Time: O(rows * cols^2)  |  Space: O(rows * cols^2)
# State: (row, c1, c2). At most rows*cols^2 unique states.
# ============================================================
def optimal(grid: List[List[int]]) -> int:
    """
    Memoized recursion over (row, c1, c2).
    Since both robots always move down together, row is the same for both.
    c1 = robot1 col, c2 = robot2 col.
    """
    rows = len(grid)
    cols = len(grid[0])
    directions = [-1, 0, 1]

    @lru_cache(maxsize=None)
    def dp(row: int, c1: int, c2: int) -> int:
        cherries = grid[row][c1] + (grid[row][c2] if c2 != c1 else 0)
        if row == rows - 1:
            return cherries
        best = 0
        for d1 in directions:
            for d2 in directions:
                nc1, nc2 = c1 + d1, c2 + d2
                if 0 <= nc1 < cols and 0 <= nc2 < cols:
                    best = max(best, dp(row + 1, nc1, nc2))
        return cherries + best

    return dp(0, 0, cols - 1)


# ============================================================
# APPROACH 3: BEST — Bottom-Up 3D DP, Space-Optimized to 2D
# Time: O(rows * cols^2)  |  Space: O(cols^2)
# Process row by row. Only need the next row's DP values.
# ============================================================
def best(grid: List[List[int]]) -> int:
    """
    Bottom-up DP, iterating from the last row upward.
    curr[c1][c2] = max cherries collectible from (row, c1, c2) to bottom.
    We roll two 2D arrays (curr, nxt) instead of the full 3D table.
    """
    rows = len(grid)
    cols = len(grid[0])
    directions = [-1, 0, 1]
    NEG_INF = float('-inf')

    # Initialize last row
    curr = [[NEG_INF] * cols for _ in range(cols)]
    for c1 in range(cols):
        for c2 in range(cols):
            curr[c1][c2] = grid[rows - 1][c1] + (
                grid[rows - 1][c2] if c2 != c1 else 0
            )

    # Fill from second-last row up to row 0
    for row in range(rows - 2, -1, -1):
        nxt = [[NEG_INF] * cols for _ in range(cols)]
        for c1 in range(cols):
            for c2 in range(cols):
                best_next = NEG_INF
                for d1 in directions:
                    for d2 in directions:
                        nc1, nc2 = c1 + d1, c2 + d2
                        if 0 <= nc1 < cols and 0 <= nc2 < cols:
                            best_next = max(best_next, curr[nc1][nc2])
                if best_next != NEG_INF:
                    cherries = grid[row][c1] + (
                        grid[row][c2] if c2 != c1 else 0
                    )
                    nxt[c1][c2] = cherries + best_next
        curr = nxt

    return curr[0][cols - 1]


if __name__ == "__main__":
    print("=== Cherry Pickup II ===")

    test_cases = [
        (
            [[3, 1, 1],
             [2, 5, 1],
             [1, 5, 5],
             [2, 1, 1]],
            24
        ),
        (
            [[1, 0, 0, 0, 0, 0, 1],
             [2, 0, 0, 0, 0, 3, 0],
             [2, 0, 9, 0, 0, 0, 0],
             [0, 3, 0, 5, 4, 0, 0],
             [1, 0, 2, 3, 0, 0, 6]],
            28
        ),
    ]

    for grid, expected in test_cases:
        b = brute_force(grid)
        o = optimal(grid)
        bt = best(grid)
        ok = "OK" if b == o == bt == expected else f"FAIL"
        print(f"  Brute={b}, Optimal={o}, Best={bt}, Expected={expected} [{ok}]")
