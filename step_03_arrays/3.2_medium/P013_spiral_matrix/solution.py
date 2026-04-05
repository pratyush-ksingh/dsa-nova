"""
Problem: Spiral Matrix
LeetCode 54 | Difficulty: MEDIUM | XP: 25

Given an m x n matrix, return all elements in spiral order (clockwise from top-left).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Simulation with visited array
# Time: O(m*n)  |  Space: O(m*n)  (visited matrix)
# ============================================================
def brute_force(matrix: List[List[int]]) -> List[int]:
    """
    Simulate spiral movement using a visited[][] array.
    Direction cycle: right -> down -> left -> up.
    When we would go out-of-bounds or hit a visited cell, we turn.
    """
    if not matrix or not matrix[0]:
        return []

    m, n = len(matrix), len(matrix[0])
    visited = [[False] * n for _ in range(m)]
    result = []

    # dr, dc for: right, down, left, up
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    direction_idx = 0
    r, c = 0, 0

    for _ in range(m * n):
        result.append(matrix[r][c])
        visited[r][c] = True

        dr, dc = directions[direction_idx]
        nr, nc = r + dr, c + dc

        # If next cell is invalid or visited, turn clockwise
        if not (0 <= nr < m and 0 <= nc < n) or visited[nr][nc]:
            direction_idx = (direction_idx + 1) % 4
            dr, dc = directions[direction_idx]
            nr, nc = r + dr, c + dc

        r, c = nr, nc

    return result


# ============================================================
# APPROACH 2: OPTIMAL  -  4-boundary shrinking (no extra space)
# Time: O(m*n)  |  Space: O(1)  (output list not counted)
# ============================================================
def optimal(matrix: List[List[int]]) -> List[int]:
    """
    Maintain top, bottom, left, right boundaries.
    Traverse one layer of the spiral, then shrink the boundary inward.
    Repeat until all elements are collected.
    """
    if not matrix or not matrix[0]:
        return []

    result = []
    top, bottom = 0, len(matrix) - 1
    left, right = 0, len(matrix[0]) - 1

    while top <= bottom and left <= right:
        # Traverse right across the top row
        for col in range(left, right + 1):
            result.append(matrix[top][col])
        top += 1

        # Traverse down the right column
        for row in range(top, bottom + 1):
            result.append(matrix[row][right])
        right -= 1

        # Traverse left across the bottom row (if still valid)
        if top <= bottom:
            for col in range(right, left - 1, -1):
                result.append(matrix[bottom][col])
            bottom -= 1

        # Traverse up the left column (if still valid)
        if left <= right:
            for row in range(bottom, top - 1, -1):
                result.append(matrix[row][left])
            left += 1

    return result


# ============================================================
# APPROACH 3: BEST  -  Same 4-boundary, cleaner variable names
# Time: O(m*n)  |  Space: O(1)
# ============================================================
def best(matrix: List[List[int]]) -> List[int]:
    """
    Identical algorithm to optimal; presented with slightly
    different loop structure for clarity in interviews.
    """
    if not matrix or not matrix[0]:
        return []

    result = []
    top, bottom, left, right = 0, len(matrix) - 1, 0, len(matrix[0]) - 1

    while top <= bottom and left <= right:
        result += [matrix[top][c] for c in range(left, right + 1)]
        top += 1
        result += [matrix[r][right] for r in range(top, bottom + 1)]
        right -= 1
        if top <= bottom:
            result += [matrix[bottom][c] for c in range(right, left - 1, -1)]
            bottom -= 1
        if left <= right:
            result += [matrix[r][left] for r in range(bottom, top - 1, -1)]
            left += 1

    return result


if __name__ == "__main__":
    print("=== Spiral Matrix ===")
    m1 = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    m2 = [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12]]
    print(f"Brute  (3x3): {brute_force(m1)}")   # [1,2,3,6,9,8,7,4,5]
    print(f"Optimal(3x4): {optimal(m2)}")        # [1,2,3,4,8,12,11,10,9,5,6,7]
    print(f"Best   (3x3): {best(m1)}")           # [1,2,3,6,9,8,7,4,5]
