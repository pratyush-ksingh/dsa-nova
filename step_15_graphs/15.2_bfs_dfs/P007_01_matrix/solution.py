"""
Problem: 01 Matrix (LeetCode #542)
Difficulty: MEDIUM | XP: 25

Given a binary matrix, find the distance of the nearest 0 for each cell.
Distance is measured in 4-directional (Manhattan) steps.
"""
from typing import List
from collections import deque
import copy


DIRS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS from each non-zero cell
# Time: O((m*n)^2)  |  Space: O(m*n)
# For every cell that is 1, run BFS until the nearest 0 is found.
# ============================================================
def brute_force(mat: List[List[int]]) -> List[List[int]]:
    rows, cols = len(mat), len(mat[0])
    result = [[0] * cols for _ in range(rows)]

    def bfs_from(sr: int, sc: int) -> int:
        """BFS from (sr, sc) until first 0 is reached. Returns distance."""
        visited = [[False] * cols for _ in range(rows)]
        queue = deque([(sr, sc, 0)])
        visited[sr][sc] = True
        while queue:
            r, c, dist = queue.popleft()
            if mat[r][c] == 0:
                return dist
            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and not visited[nr][nc]:
                    visited[nr][nc] = True
                    queue.append((nr, nc, dist + 1))
        return float('inf')  # unreachable (shouldn't happen in valid input)

    for r in range(rows):
        for c in range(cols):
            result[r][c] = bfs_from(r, c) if mat[r][c] != 0 else 0

    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Multi-Source BFS from all 0s
# Time: O(m*n)  |  Space: O(m*n)
# Seed the BFS queue with every 0. Expand outward; first time a 1
# is reached gives shortest distance. Each cell processed once.
# ============================================================
def optimal(mat: List[List[int]]) -> List[List[int]]:
    rows, cols = len(mat), len(mat[0])
    dist = [[float('inf')] * cols for _ in range(rows)]
    queue = deque()

    # Seed all 0s at distance 0
    for r in range(rows):
        for c in range(cols):
            if mat[r][c] == 0:
                dist[r][c] = 0
                queue.append((r, c))

    # BFS outward
    while queue:
        r, c = queue.popleft()
        for dr, dc in DIRS:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                new_dist = dist[r][c] + 1
                if new_dist < dist[nr][nc]:
                    dist[nr][nc] = new_dist
                    queue.append((nr, nc))

    return dist


# ============================================================
# APPROACH 3: BEST -- Multi-Source BFS with flat index (same O(mn))
# Time: O(m*n)  |  Space: O(m*n)
# Identical algorithm to optimal; uses flat integer index instead of
# (row, col) tuple to reduce per-entry memory and tuple unpacking cost.
# ============================================================
def best(mat: List[List[int]]) -> List[List[int]]:
    rows, cols = len(mat), len(mat[0])
    INF = float('inf')
    dist = [INF] * (rows * cols)
    queue = deque()

    for r in range(rows):
        for c in range(cols):
            if mat[r][c] == 0:
                idx = r * cols + c
                dist[idx] = 0
                queue.append(idx)

    while queue:
        idx = queue.popleft()
        r, c = divmod(idx, cols)
        d = dist[idx]
        for dr, dc in DIRS:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                nidx = nr * cols + nc
                if d + 1 < dist[nidx]:
                    dist[nidx] = d + 1
                    queue.append(nidx)

    # Reshape flat list back to 2-D
    return [dist[r * cols:(r + 1) * cols] for r in range(rows)]


if __name__ == "__main__":
    print("=== 01 Matrix ===\n")

    mat1 = [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
    print(f"Brute:   {brute_force(copy.deepcopy(mat1))}")
    print(f"Optimal: {optimal(copy.deepcopy(mat1))}")
    print(f"Best:    {best(copy.deepcopy(mat1))}")
    # Expected: [[0,0,0],[0,1,0],[0,0,0]]

    print()
    mat2 = [[0, 0, 0], [0, 1, 0], [1, 1, 1]]
    print(f"Brute:   {brute_force(copy.deepcopy(mat2))}")
    print(f"Optimal: {optimal(copy.deepcopy(mat2))}")
    print(f"Best:    {best(copy.deepcopy(mat2))}")
    # Expected: [[0,0,0],[0,1,0],[1,2,1]]

    print()
    mat3 = [[1, 1, 1], [1, 1, 1], [1, 1, 0]]
    print(f"Far corner: {optimal(copy.deepcopy(mat3))}")
    # Expected: [[4,3,2],[3,2,1],[2,1,0]]
