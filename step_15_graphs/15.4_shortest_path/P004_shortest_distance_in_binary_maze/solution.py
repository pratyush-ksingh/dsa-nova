"""
Problem: Shortest Distance in Binary Maze
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Tuple
from collections import deque
import copy


DIRS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS Explore All Paths
# Time: O(4^(n*n)) worst case  |  Space: O(n*n)
# Explore every path via DFS, track minimum distance.
# ============================================================
def brute_force(grid: List[List[int]], src: Tuple[int, int], dst: Tuple[int, int]) -> int:
    n = len(grid)
    if grid[src[0]][src[1]] == 1 or grid[dst[0]][dst[1]] == 1:
        return -1
    if src == dst:
        return 0

    min_dist = [float('inf')]
    visited = [[False] * n for _ in range(n)]
    visited[src[0]][src[1]] = True

    def dfs(r: int, c: int, dist: int):
        if (r, c) == dst:
            min_dist[0] = min(min_dist[0], dist)
            return

        for dr, dc in DIRS:
            nr, nc = r + dr, c + dc
            if (0 <= nr < n and 0 <= nc < n
                    and grid[nr][nc] == 0 and not visited[nr][nc]
                    and dist + 1 < min_dist[0]):  # pruning
                visited[nr][nc] = True
                dfs(nr, nc, dist + 1)
                visited[nr][nc] = False  # backtrack

    dfs(src[0], src[1], 0)
    return min_dist[0] if min_dist[0] != float('inf') else -1


# ============================================================
# APPROACH 2: OPTIMAL -- BFS (Standard)
# Time: O(n * n)  |  Space: O(n * n)
# Level-by-level BFS gives shortest path in unweighted grid.
# ============================================================
def optimal(grid: List[List[int]], src: Tuple[int, int], dst: Tuple[int, int]) -> int:
    n = len(grid)
    if grid[src[0]][src[1]] == 1 or grid[dst[0]][dst[1]] == 1:
        return -1
    if src == dst:
        return 0

    visited = [[False] * n for _ in range(n)]
    queue = deque([(src[0], src[1])])
    visited[src[0]][src[1]] = True
    dist = 0

    while queue:
        size = len(queue)
        dist += 1

        for _ in range(size):
            r, c = queue.popleft()

            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if (0 <= nr < n and 0 <= nc < n
                        and grid[nr][nc] == 0 and not visited[nr][nc]):
                    if (nr, nc) == dst:
                        return dist
                    visited[nr][nc] = True
                    queue.append((nr, nc))

    return -1


# ============================================================
# APPROACH 3: BEST -- BFS with In-Place Marking + Flat Index
# Time: O(n * n)  |  Space: O(n * n) for queue, O(1) extra
# Marks grid cells in-place. Uses flat index for reduced overhead.
# ============================================================
def best(grid: List[List[int]], src: Tuple[int, int], dst: Tuple[int, int]) -> int:
    n = len(grid)
    if grid[src[0]][src[1]] == 1 or grid[dst[0]][dst[1]] == 1:
        return -1
    if src == dst:
        return 0

    target = dst[0] * n + dst[1]
    start = src[0] * n + src[1]
    queue = deque([start])
    grid[src[0]][src[1]] = 1  # mark visited in-place
    dist = 0

    while queue:
        size = len(queue)
        dist += 1

        for _ in range(size):
            idx = queue.popleft()
            r, c = divmod(idx, n)

            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 0:
                    n_idx = nr * n + nc
                    if n_idx == target:
                        return dist
                    grid[nr][nc] = 1  # mark visited
                    queue.append(n_idx)

    return -1


if __name__ == "__main__":
    print("=== Shortest Distance in Binary Maze ===\n")

    grid1 = [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
    src1, dst1 = (0, 0), (2, 2)
    print(f"Brute:   {brute_force(copy.deepcopy(grid1), src1, dst1)}")  # 4
    print(f"Optimal: {optimal(copy.deepcopy(grid1), src1, dst1)}")       # 4
    print(f"Best:    {best(copy.deepcopy(grid1), src1, dst1)}")          # 4

    # No path
    grid2 = [[0, 1], [1, 0]]
    print(f"\nBlocked: {optimal(copy.deepcopy(grid2), (0, 0), (1, 1))}")  # -1

    # Same source and destination
    print(f"Same:    {optimal(copy.deepcopy(grid1), (0, 0), (0, 0))}")    # 0

    # Source is wall
    grid3 = [[1, 0], [0, 0]]
    print(f"Wall src: {optimal(copy.deepcopy(grid3), (0, 0), (1, 1))}")   # -1
