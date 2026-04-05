"""
Problem: Region in Binary Matrix
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a binary matrix, find the size of the largest region of
connected 1s. Connectivity is 8-directional (including diagonals).
"""
from typing import List
from collections import deque
import copy


DIRS = [(-1,-1),(-1,0),(-1,1),(0,-1),(0,1),(1,-1),(1,0),(1,1)]


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive DFS with visited array
# Time: O(R * C)  |  Space: O(R * C)
# ============================================================
def brute_force(A: List[List[int]]) -> int:
    R, C = len(A), len(A[0])
    vis = [[False]*C for _ in range(R)]

    def dfs(r, c):
        if r < 0 or r >= R or c < 0 or c >= C: return 0
        if vis[r][c] or A[r][c] == 0: return 0
        vis[r][c] = True
        return 1 + sum(dfs(r+dr, c+dc) for dr, dc in DIRS)

    return max((dfs(i, j) for i in range(R) for j in range(C)
                if not vis[i][j] and A[i][j] == 1), default=0)


# ============================================================
# APPROACH 2: OPTIMAL - Iterative BFS
# Time: O(R * C)  |  Space: O(R * C)
# ============================================================
# BFS avoids Python recursion depth issues on large matrices.
# Mark on enqueue. Count queue pops as region size.
# ============================================================
def optimal(A: List[List[int]]) -> int:
    R, C = len(A), len(A[0])
    vis = [[False]*C for _ in range(R)]
    best_size = 0

    for i in range(R):
        for j in range(C):
            if not vis[i][j] and A[i][j] == 1:
                size = 0
                q = deque([(i, j)])
                vis[i][j] = True
                while q:
                    r, c = q.popleft()
                    size += 1
                    for dr, dc in DIRS:
                        nr, nc = r+dr, c+dc
                        if 0 <= nr < R and 0 <= nc < C and not vis[nr][nc] and A[nr][nc] == 1:
                            vis[nr][nc] = True
                            q.append((nr, nc))
                best_size = max(best_size, size)

    return best_size


# ============================================================
# APPROACH 3: BEST - In-place DFS (no separate visited array)
# Time: O(R * C)  |  Space: O(R * C) worst-case stack
# ============================================================
# Flip visited 1s to 0 in a copy of the grid, avoiding an extra
# boolean grid. Saves memory constant factor.
# Real-life use: counting land masses in satellite raster images,
# blob detection in computer vision preprocessing pipelines.
# ============================================================
def best(A: List[List[int]]) -> int:
    grid = [row[:] for row in A]  # work on a copy
    R, C = len(grid), len(grid[0])
    best_size = 0

    def dfs(r, c):
        if r < 0 or r >= R or c < 0 or c >= C or grid[r][c] == 0:
            return 0
        grid[r][c] = 0  # mark visited
        return 1 + sum(dfs(r+dr, c+dc) for dr, dc in DIRS)

    for i in range(R):
        for j in range(C):
            if grid[i][j] == 1:
                best_size = max(best_size, dfs(i, j))

    return best_size


if __name__ == "__main__":
    print("=== Region in Binary Matrix ===")

    m1 = [
        [0,0,1,1,0],
        [1,0,1,1,0],
        [0,1,0,0,0],
        [0,0,0,0,1]
    ]
    print(f"Test1 Brute   (expect 6): {brute_force(m1)}")
    print(f"Test1 Optimal (expect 6): {optimal(m1)}")
    print(f"Test1 Best    (expect 6): {best(m1)}")

    m2 = [[0,0],[0,0]]
    print(f"Test2 Best    (expect 0): {best(m2)}")

    m3 = [[1,1,1],[1,1,1],[1,1,1]]
    print(f"Test3 Best    (expect 9): {best(m3)}")

    m4 = [[1,0,1,1,0,1]]
    print(f"Test4 Best    (expect 2): {best(m4)}")
