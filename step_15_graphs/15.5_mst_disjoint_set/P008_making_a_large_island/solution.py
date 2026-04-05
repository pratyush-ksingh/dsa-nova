"""Problem: Making a Large Island
Difficulty: HARD | XP: 50

Flip at most one 0 to 1, find the largest possible island.
Island = connected component of 1s (4-directional).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Try flipping each 0, BFS to measure
# Time: O(N^4)  |  Space: O(N^2)
# ============================================================
def brute_force(grid: List[List[int]]) -> int:
    n = len(grid)
    dirs = ((0,1),(0,-1),(1,0),(-1,0))

    def bfs_size(g, sr, sc):
        from collections import deque
        q = deque([(sr, sc)])
        visited = {(sr, sc)}
        size = 0
        while q:
            r, c = q.popleft()
            size += 1
            for dr, dc in dirs:
                nr, nc = r+dr, c+dc
                if 0 <= nr < n and 0 <= nc < n and g[nr][nc] == 1 and (nr,nc) not in visited:
                    visited.add((nr,nc))
                    q.append((nr,nc))
        return size

    has_zero = any(grid[i][j] == 0 for i in range(n) for j in range(n))
    if not has_zero:
        return n * n

    max_size = 0
    for i in range(n):
        for j in range(n):
            if grid[i][j] == 0:
                grid[i][j] = 1
                max_size = max(max_size, bfs_size(grid, i, j))
                grid[i][j] = 0
    return max_size


# ============================================================
# APPROACH 2: OPTIMAL - DFS label islands + check each 0
# Time: O(N^2)  |  Space: O(N^2)
# Label each island with unique ID, precompute sizes.
# For each 0, sum sizes of distinct neighboring islands + 1.
# ============================================================
def optimal(grid: List[List[int]]) -> int:
    n = len(grid)
    label = [[0]*n for _ in range(n)]
    island_size = {}
    island_id = 2
    dirs = ((0,1),(0,-1),(1,0),(-1,0))

    def dfs(r, c, iid):
        if not (0 <= r < n and 0 <= c < n) or grid[r][c] != 1 or label[r][c] != 0:
            return 0
        label[r][c] = iid
        return 1 + sum(dfs(r+dr, c+dc, iid) for dr, dc in dirs)

    for i in range(n):
        for j in range(n):
            if grid[i][j] == 1 and label[i][j] == 0:
                sz = dfs(i, j, island_id)
                island_size[island_id] = sz
                island_id += 1

    max_size = max(island_size.values(), default=0)

    for i in range(n):
        for j in range(n):
            if grid[i][j] == 0:
                seen = set()
                total = 1
                for dr, dc in dirs:
                    nr, nc = i+dr, j+dc
                    if 0 <= nr < n and 0 <= nc < n and label[nr][nc] > 0:
                        lid = label[nr][nc]
                        if lid not in seen:
                            seen.add(lid)
                            total += island_size[lid]
                max_size = max(max_size, total)
    return max_size


# ============================================================
# APPROACH 3: BEST - Union-Find with component sizes
# Time: O(N^2 * alpha(N^2))  |  Space: O(N^2)
# ============================================================
def best(grid: List[List[int]]) -> int:
    n = len(grid)
    parent = list(range(n * n))
    size = [1] * (n * n)
    dirs = ((0,1),(0,-1),(1,0),(-1,0))

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px == py:
            return
        if size[px] < size[py]:
            px, py = py, px
        parent[py] = px
        size[px] += size[py]

    for i in range(n):
        for j in range(n):
            if grid[i][j] == 1:
                for dr, dc in dirs:
                    nr, nc = i+dr, j+dc
                    if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 1:
                        union(i*n+j, nr*n+nc)

    max_size = max((size[find(i*n+j)] for i in range(n) for j in range(n) if grid[i][j] == 1), default=0)

    for i in range(n):
        for j in range(n):
            if grid[i][j] == 0:
                seen = set()
                total = 1
                for dr, dc in dirs:
                    nr, nc = i+dr, j+dc
                    if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 1:
                        root = find(nr*n+nc)
                        if root not in seen:
                            seen.add(root)
                            total += size[root]
                max_size = max(max_size, total)
    return max_size


if __name__ == "__main__":
    import copy
    tests = [
        ([[1,0],[0,1]], 3),
        ([[1,1],[1,0]], 4),
        ([[1,1],[1,1]], 4),
        ([[0,0],[0,0]], 1),
    ]
    for grid, expected in tests:
        bf = brute_force(copy.deepcopy(grid))
        opt = optimal(copy.deepcopy(grid))
        be = best(copy.deepcopy(grid))
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] grid={grid} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
