"""Problem: Number of Islands II (Online)
Difficulty: HARD | XP: 50

Add land cells one at a time to an m x n grid. After each addition,
return the number of islands. Use Union-Find for efficient online processing.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - BFS count after each addition
# Time: O(K * M * N)  |  Space: O(M * N)
# ============================================================
def brute_force(m: int, n: int, positions: List[List[int]]) -> List[int]:
    grid = [[0] * n for _ in range(m)]
    result = []

    def count_islands():
        visited = [[False] * n for _ in range(m)]
        count = 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] and not visited[i][j]:
                    count += 1
                    stack = [(i, j)]
                    visited[i][j] = True
                    while stack:
                        r, c = stack.pop()
                        for dr, dc in ((0,1),(0,-1),(1,0),(-1,0)):
                            nr, nc = r+dr, c+dc
                            if 0 <= nr < m and 0 <= nc < n and grid[nr][nc] and not visited[nr][nc]:
                                visited[nr][nc] = True
                                stack.append((nr, nc))
        return count

    for r, c in positions:
        grid[r][c] = 1
        result.append(count_islands())
    return result


# ============================================================
# APPROACH 2: OPTIMAL - Union-Find (DSU)
# Time: O(K * alpha(M*N))  |  Space: O(M * N)
# ============================================================
def optimal(m: int, n: int, positions: List[List[int]]) -> List[int]:
    parent = [-1] * (m * n)
    rank = [0] * (m * n)
    islands = [0]

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px == py:
            return
        if rank[px] < rank[py]:
            px, py = py, px
        parent[py] = px
        if rank[px] == rank[py]:
            rank[px] += 1
        islands[0] -= 1

    result = []
    dirs = ((0, 1), (0, -1), (1, 0), (-1, 0))
    for r, c in positions:
        idx = r * n + c
        if parent[idx] == -1:
            parent[idx] = idx
            islands[0] += 1
            for dr, dc in dirs:
                nr, nc = r + dr, c + dc
                nidx = nr * n + nc
                if 0 <= nr < m and 0 <= nc < n and parent[nidx] != -1:
                    union(idx, nidx)
        result.append(islands[0])
    return result


# ============================================================
# APPROACH 3: BEST - DSU class with cleaner interface
# Time: O(K * alpha(M*N))  |  Space: O(M * N)
# ============================================================
def best(m: int, n: int, positions: List[List[int]]) -> List[int]:
    class DSU:
        def __init__(self, size):
            self.parent = [-1] * size
            self.rank = [0] * size
            self.count = 0

        def find(self, x):
            root = x
            while self.parent[root] != root:
                root = self.parent[root]
            while self.parent[x] != root:
                self.parent[x], x = root, self.parent[x]
            return root

        def add(self, x):
            self.parent[x] = x
            self.count += 1

        def is_land(self, x):
            return self.parent[x] != -1

        def union(self, x, y):
            px, py = self.find(x), self.find(y)
            if px == py:
                return
            if self.rank[px] < self.rank[py]:
                px, py = py, px
            self.parent[py] = px
            if self.rank[px] == self.rank[py]:
                self.rank[px] += 1
            self.count -= 1

    dsu = DSU(m * n)
    result = []
    dirs = ((0, 1), (0, -1), (1, 0), (-1, 0))
    for r, c in positions:
        idx = r * n + c
        if not dsu.is_land(idx):
            dsu.add(idx)
            for dr, dc in dirs:
                nr, nc = r + dr, c + dc
                nidx = nr * n + nc
                if 0 <= nr < m and 0 <= nc < n and dsu.is_land(nidx):
                    dsu.union(idx, nidx)
        result.append(dsu.count)
    return result


if __name__ == "__main__":
    tests = [
        (3, 3, [[0,0],[0,1],[1,2],[2,1],[1,1]], [1,1,2,3,1]),
        (3, 3, [[0,0],[0,1],[1,2],[1,2]], [1,1,2,2]),  # duplicate
        (1, 1, [[0,0]], [1]),
    ]
    for m, n, pos, expected in tests:
        bf = brute_force(m, n, [p[:] for p in pos])
        opt = optimal(m, n, pos)
        be = best(m, n, pos)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] m={m} n={n} pos={pos} -> BF={bf}, Opt={opt}, Best={be} (expected={expected})")
