"""Problem: Swim in Rising Water
Difficulty: HARD | XP: 50

Grid of N x N, grid[i][j] = elevation. At time T, can swim in cells with elevation <= T.
Find minimum T such that you can swim from (0,0) to (N-1,N-1).
"""
import heapq
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Binary search on T + BFS check
# Time: O(N^2 log N)  |  Space: O(N^2)
# ============================================================
def brute_force(grid: List[List[int]]) -> int:
    from collections import deque
    n = len(grid)
    dirs = ((0,1),(0,-1),(1,0),(-1,0))

    def can_reach(t):
        if grid[0][0] > t:
            return False
        visited = [[False]*n for _ in range(n)]
        q = deque([(0, 0)])
        visited[0][0] = True
        while q:
            r, c = q.popleft()
            if r == n-1 and c == n-1:
                return True
            for dr, dc in dirs:
                nr, nc = r+dr, c+dc
                if 0 <= nr < n and 0 <= nc < n and not visited[nr][nc] and grid[nr][nc] <= t:
                    visited[nr][nc] = True
                    q.append((nr, nc))
        return False

    lo, hi = grid[0][0], n*n - 1
    while lo < hi:
        mid = (lo + hi) // 2
        if can_reach(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo


# ============================================================
# APPROACH 2: OPTIMAL - Dijkstra (min-heap, track max elevation on path)
# Time: O(N^2 log N)  |  Space: O(N^2)
# ============================================================
def optimal(grid: List[List[int]]) -> int:
    n = len(grid)
    dist = [[float('inf')] * n for _ in range(n)]
    dist[0][0] = grid[0][0]
    # heap: (max_elevation, row, col)
    heap = [(grid[0][0], 0, 0)]
    dirs = ((0,1),(0,-1),(1,0),(-1,0))
    while heap:
        d, r, c = heapq.heappop(heap)
        if d > dist[r][c]:
            continue
        if r == n-1 and c == n-1:
            return d
        for dr, dc in dirs:
            nr, nc = r+dr, c+dc
            if 0 <= nr < n and 0 <= nc < n:
                nd = max(d, grid[nr][nc])
                if nd < dist[nr][nc]:
                    dist[nr][nc] = nd
                    heapq.heappush(heap, (nd, nr, nc))
    return dist[n-1][n-1]


# ============================================================
# APPROACH 3: BEST - Union-Find, process cells by elevation
# Time: O(N^2 * alpha(N^2))  |  Space: O(N^2)
# Add cells in order of elevation; stop when (0,0) connects to (N-1,N-1)
# ============================================================
def best(grid: List[List[int]]) -> int:
    n = len(grid)
    parent = list(range(n * n))
    rank = [0] * (n * n)
    in_grid = [False] * (n * n)
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
        if rank[px] < rank[py]:
            px, py = py, px
        parent[py] = px
        if rank[px] == rank[py]:
            rank[px] += 1

    # Sort cells by elevation
    cells = sorted(range(n*n), key=lambda idx: grid[idx//n][idx%n])
    for cell in cells:
        r, c = cell // n, cell % n
        in_grid[cell] = True
        for dr, dc in dirs:
            nr, nc = r+dr, c+dc
            nidx = nr*n+nc
            if 0 <= nr < n and 0 <= nc < n and in_grid[nidx]:
                union(cell, nidx)
        if find(0) == find(n*n - 1):
            return grid[r][c]
    return -1


if __name__ == "__main__":
    tests = [
        ([[0, 2], [1, 3]], 3),
        ([[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]], 16),
        ([[0]], 0),
        ([[0,1],[1,0]], 1),
    ]
    for grid, expected in tests:
        import copy
        bf = brute_force(copy.deepcopy(grid))
        opt = optimal(copy.deepcopy(grid))
        be = best(copy.deepcopy(grid))
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] n={len(grid)} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
