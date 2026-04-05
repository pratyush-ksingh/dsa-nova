"""
Problem: Black Shapes
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a character grid of 'X' (black) and 'O' (white),
count the number of connected components formed by 'X' characters.
"""

from typing import List
from collections import deque
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE
# DFS flood fill — recursively mark connected 'X' cells
# Time: O(M*N)  |  Space: O(M*N) recursion stack
# ============================================================
def brute_force(A: List[List[str]]) -> int:
    if not A or not A[0]:
        return 0
    grid = [row[:] for row in A]  # deep copy
    rows, cols = len(grid), len(grid[0])

    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != 'X':
            return
        grid[r][c] = '#'
        dfs(r + 1, c)
        dfs(r - 1, c)
        dfs(r, c + 1)
        dfs(r, c - 1)

    count = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 'X':
                dfs(r, c)
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL
# BFS flood fill — iterative, no recursion limit issues
# Time: O(M*N)  |  Space: O(min(M,N))
# ============================================================
def optimal(A: List[List[str]]) -> int:
    if not A or not A[0]:
        return 0
    grid = [row[:] for row in A]
    rows, cols = len(grid), len(grid[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
    count = 0

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 'X':
                count += 1
                queue = deque([(r, c)])
                grid[r][c] = '#'
                while queue:
                    cr, cc = queue.popleft()
                    for dr, dc in directions:
                        nr, nc = cr + dr, cc + dc
                        if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 'X':
                            grid[nr][nc] = '#'
                            queue.append((nr, nc))
    return count


# ============================================================
# APPROACH 3: BEST
# Union-Find for connected components
# Time: O(M*N * alpha)  |  Space: O(M*N)
# ============================================================
def best(A: List[List[str]]) -> int:
    if not A or not A[0]:
        return 0
    rows, cols = len(A), len(A[0])
    parent = list(range(rows * cols))

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px != py:
            parent[px] = py

    for r in range(rows):
        for c in range(cols):
            if A[r][c] == 'X':
                for dr, dc in [(0, 1), (1, 0)]:
                    nr, nc = r + dr, c + dc
                    if nr < rows and nc < cols and A[nr][nc] == 'X':
                        union(r * cols + c, nr * cols + nc)

    roots = {find(r * cols + c) for r in range(rows) for c in range(cols) if A[r][c] == 'X'}
    return len(roots)


if __name__ == "__main__":
    print("=== Black Shapes ===")

    grid1 = [
        ['X', 'O', 'X'],
        ['O', 'O', 'O'],
        ['X', 'O', 'X']
    ]
    print(f"BruteForce grid1: {brute_force(grid1)}")  # 4
    print(f"Optimal    grid1: {optimal(grid1)}")       # 4
    print(f"Best       grid1: {best(grid1)}")          # 4

    grid2 = [['X', 'X'], ['X', 'O']]
    print(f"BruteForce grid2: {brute_force(grid2)}")  # 1
    print(f"Optimal    grid2: {optimal(grid2)}")       # 1

    grid3 = [['O', 'O'], ['O', 'O']]
    print(f"All O's: {optimal(grid3)}")  # 0
