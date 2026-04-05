"""
Problem: Most Stones Removed with Same Row or Column (LeetCode #947)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS to Find Connected Components
# Time: O(n^2)  |  Space: O(n)
# Two stones are in the same component if they share a row or
# column. DFS over stones (not grid) with O(n^2) neighbor check.
# ============================================================
def brute_force(stones: List[List[int]]) -> int:
    n = len(stones)
    visited = [False] * n

    def dfs(i: int):
        visited[i] = True
        for j in range(n):
            if not visited[j]:
                if stones[i][0] == stones[j][0] or stones[i][1] == stones[j][1]:
                    dfs(j)

    components = 0
    for i in range(n):
        if not visited[i]:
            components += 1
            dfs(i)

    return n - components


# ============================================================
# APPROACH 2: OPTIMAL -- Union-Find Grouping Stones
# Time: O(n * alpha(n))  |  Space: O(n)
# Union stones that share a row or column using maps from
# row -> first stone index and col -> first stone index.
# ============================================================
class DSU:
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True


def optimal(stones: List[List[int]]) -> int:
    n = len(stones)
    dsu = DSU(n)
    row_map = {}  # row -> first stone index
    col_map = {}  # col -> first stone index

    for i, (r, c) in enumerate(stones):
        if r in row_map:
            dsu.union(i, row_map[r])
        else:
            row_map[r] = i
        if c in col_map:
            dsu.union(i, col_map[c])
        else:
            col_map[c] = i

    # Count unique components
    components = len(set(dsu.find(i) for i in range(n)))
    return n - components


# ============================================================
# APPROACH 3: BEST -- Union-Find with Coordinate Mapping
# Time: O(n * alpha(n))  |  Space: O(n)
# Map rows and columns into a single Union-Find namespace.
# Offset columns by a large value to avoid collisions.
# ============================================================
class DSUMap:
    def __init__(self):
        self.parent = {}
        self.rank = {}

    def find(self, x: int) -> int:
        if x not in self.parent:
            self.parent[x] = x
            self.rank[x] = 0
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x: int, y: int):
        px, py = self.find(x), self.find(y)
        if px == py:
            return
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1


def best(stones: List[List[int]]) -> int:
    dsu = DSUMap()
    OFFSET = 100001  # columns offset to avoid row/col collision

    for r, c in stones:
        dsu.union(r, c + OFFSET)

    # Count unique components among stone coordinates
    # Each stone contributes its row; count distinct roots
    components = len(set(dsu.find(r) for r, c in stones))
    return len(stones) - components


if __name__ == "__main__":
    print("=== Most Stones Removed ===\n")

    stones1 = [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]
    print(f"Brute:   {brute_force(stones1)}")   # 5
    print(f"Optimal: {optimal(stones1)}")         # 5
    print(f"Best:    {best(stones1)}")            # 5

    stones2 = [[0,0],[0,2],[1,1],[2,0],[2,2]]
    print(f"\nBrute:   {brute_force(stones2)}")   # 3
    print(f"Optimal: {optimal(stones2)}")          # 3
    print(f"Best:    {best(stones2)}")             # 3

    stones3 = [[0,0]]
    print(f"\nSingle:  {best(stones3)}")           # 0
