"""
Problem: Number of Provinces (LeetCode #547)
Difficulty: MEDIUM | XP: 25

Given an adjacency matrix isConnected, find the number of provinces
(connected components).
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: DFS
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def findCircleNum_dfs(isConnected: List[List[int]]) -> int:
    """
    Count provinces by counting DFS launches from outer loop.
    """
    n = len(isConnected)
    visited = [False] * n
    provinces = 0

    def dfs(node: int):
        visited[node] = True
        for j in range(n):
            if isConnected[node][j] == 1 and not visited[j]:
                dfs(j)

    for i in range(n):
        if not visited[i]:
            provinces += 1
            dfs(i)

    return provinces


# ============================================================
# APPROACH 2: BFS
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def findCircleNum_bfs(isConnected: List[List[int]]) -> int:
    """
    Same logic as DFS but uses a queue for level-order exploration.
    """
    n = len(isConnected)
    visited = [False] * n
    provinces = 0

    for i in range(n):
        if not visited[i]:
            provinces += 1
            queue = deque([i])
            visited[i] = True

            while queue:
                node = queue.popleft()
                for j in range(n):
                    if isConnected[node][j] == 1 and not visited[j]:
                        visited[j] = True
                        queue.append(j)

    return provinces


# ============================================================
# APPROACH 3: UNION-FIND
# Time: O(n^2 * alpha(n)) ~ O(n^2)  |  Space: O(n)
# ============================================================
def findCircleNum_union_find(isConnected: List[List[int]]) -> int:
    """
    Union-Find with path compression and union by rank.
    """
    n = len(isConnected)
    parent = list(range(n))
    rank = [0] * n

    def find(x: int) -> int:
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(x: int, y: int):
        rx, ry = find(x), find(y)
        if rx == ry:
            return
        if rank[rx] < rank[ry]:
            parent[rx] = ry
        elif rank[rx] > rank[ry]:
            parent[ry] = rx
        else:
            parent[ry] = rx
            rank[rx] += 1

    for i in range(n):
        for j in range(i + 1, n):
            if isConnected[i][j] == 1:
                union(i, j)

    return sum(1 for i in range(n) if find(i) == i)


if __name__ == "__main__":
    print("=== Number of Provinces ===\n")

    m1 = [[1,1,0], [1,1,0], [0,0,1]]
    print(f"Test 1: Expected 2 -> DFS={findCircleNum_dfs(m1)}, BFS={findCircleNum_bfs(m1)}, UF={findCircleNum_union_find(m1)}")

    m2 = [[1,0,0], [0,1,0], [0,0,1]]
    print(f"Test 2: Expected 3 -> DFS={findCircleNum_dfs(m2)}")

    m3 = [[1,1,1], [1,1,1], [1,1,1]]
    print(f"Test 3: Expected 1 -> DFS={findCircleNum_dfs(m3)}")

    m4 = [[1]]
    print(f"Test 4: Expected 1 -> DFS={findCircleNum_dfs(m4)}")
