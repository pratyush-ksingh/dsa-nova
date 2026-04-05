"""
Problem: Number of Islands (LeetCode #200)
Difficulty: MEDIUM | XP: 25

Given a 2D grid of '1's (land) and '0's (water), count the number
of islands. An island is formed by connecting adjacent lands
horizontally or vertically.
"""
from typing import List
from collections import deque
import copy


# ============================================================
# APPROACH 1: DFS FLOOD FILL
# Time: O(m * n)  |  Space: O(m * n) recursion stack worst case
# ============================================================
def numIslands_dfs(grid: List[List[str]]) -> int:
    """
    For each unvisited '1', increment count and DFS-sink the entire island.
    """
    if not grid or not grid[0]:
        return 0

    m, n = len(grid), len(grid[0])
    count = 0

    def dfs(r: int, c: int):
        if r < 0 or r >= m or c < 0 or c >= n or grid[r][c] != '1':
            return
        grid[r][c] = '0'  # Sink (mark visited)
        dfs(r + 1, c)
        dfs(r - 1, c)
        dfs(r, c + 1)
        dfs(r, c - 1)

    for r in range(m):
        for c in range(n):
            if grid[r][c] == '1':
                count += 1
                dfs(r, c)

    return count


# ============================================================
# APPROACH 2: BFS FLOOD FILL
# Time: O(m * n)  |  Space: O(min(m, n)) queue
# ============================================================
def numIslands_bfs(grid: List[List[str]]) -> int:
    """
    Same logic as DFS but uses a queue. Avoids deep recursion.
    Key: mark cell as '0' BEFORE enqueueing to prevent duplicate entries.
    """
    if not grid or not grid[0]:
        return 0

    m, n = len(grid), len(grid[0])
    count = 0
    dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]

    for r in range(m):
        for c in range(n):
            if grid[r][c] == '1':
                count += 1
                grid[r][c] = '0'  # Mark BEFORE enqueue
                queue = deque([(r, c)])

                while queue:
                    cr, cc = queue.popleft()
                    for dr, dc in dirs:
                        nr, nc = cr + dr, cc + dc
                        if 0 <= nr < m and 0 <= nc < n and grid[nr][nc] == '1':
                            grid[nr][nc] = '0'  # Mark BEFORE enqueue
                            queue.append((nr, nc))

    return count


# ============================================================
# APPROACH 3: UNION-FIND
# Time: O(m * n * alpha(mn)) ~ O(m * n)  |  Space: O(m * n)
# ============================================================
def numIslands_union_find(grid: List[List[str]]) -> int:
    """
    Union-Find with path compression and union by rank.
    Each land cell starts as its own component. Union adjacent land cells.
    Count = initial land cells - number of successful unions.
    """
    if not grid or not grid[0]:
        return 0

    m, n = len(grid), len(grid[0])
    parent = list(range(m * n))
    rank = [0] * (m * n)
    count = 0

    def find(x: int) -> int:
        if parent[x] != x:
            parent[x] = find(parent[x])  # Path compression
        return parent[x]

    def union(x: int, y: int) -> bool:
        rx, ry = find(x), find(y)
        if rx == ry:
            return False
        if rank[rx] < rank[ry]:
            parent[rx] = ry
        elif rank[rx] > rank[ry]:
            parent[ry] = rx
        else:
            parent[ry] = rx
            rank[rx] += 1
        return True

    # Count land cells and initialize
    for r in range(m):
        for c in range(n):
            if grid[r][c] == '1':
                count += 1

    # Union adjacent land cells (right and down only to avoid duplicates)
    for r in range(m):
        for c in range(n):
            if grid[r][c] == '1':
                idx = r * n + c
                # Right neighbor
                if c + 1 < n and grid[r][c + 1] == '1':
                    if union(idx, idx + 1):
                        count -= 1
                # Down neighbor
                if r + 1 < m and grid[r + 1][c] == '1':
                    if union(idx, idx + n):
                        count -= 1

    return count


if __name__ == "__main__":
    print("=== Number of Islands ===\n")

    g1 = [["1","1","1","1","0"],
          ["1","1","0","1","0"],
          ["1","1","0","0","0"],
          ["0","0","0","0","0"]]
    print(f"Test 1: Expected 1 -> DFS={numIslands_dfs(copy.deepcopy(g1))}, "
          f"BFS={numIslands_bfs(copy.deepcopy(g1))}, "
          f"UF={numIslands_union_find(copy.deepcopy(g1))}")

    g2 = [["1","1","0","0","0"],
          ["1","1","0","0","0"],
          ["0","0","1","0","0"],
          ["0","0","0","1","1"]]
    print(f"Test 2: Expected 3 -> DFS={numIslands_dfs(copy.deepcopy(g2))}, "
          f"BFS={numIslands_bfs(copy.deepcopy(g2))}, "
          f"UF={numIslands_union_find(copy.deepcopy(g2))}")

    g3 = [["1"]]
    print(f"Test 3: Expected 1 -> DFS={numIslands_dfs(copy.deepcopy(g3))}")

    g4 = [["0"]]
    print(f"Test 4: Expected 0 -> DFS={numIslands_dfs(copy.deepcopy(g4))}")

    g5 = []
    print(f"Test 5 (empty): Expected 0 -> DFS={numIslands_dfs(g5)}")
