"""
Problem: Connected Components (LeetCode #323 equivalent)
Difficulty: EASY | XP: 10

Given n nodes and undirected edges, find the number of connected components.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: DFS
# Time: O(V + E) | Space: O(V + E)
# ============================================================
def count_components_dfs(n: int, edges: List[List[int]]) -> int:
    """Launch DFS from each unvisited node. Each launch = 1 component."""
    adj = [[] for _ in range(n)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    visited = [False] * n
    count = 0

    def dfs(node: int):
        visited[node] = True
        for neighbor in adj[node]:
            if not visited[neighbor]:
                dfs(neighbor)

    for i in range(n):
        if not visited[i]:
            count += 1
            dfs(i)

    return count


# ============================================================
# APPROACH 2: BFS
# Time: O(V + E) | Space: O(V + E)
# ============================================================
def count_components_bfs(n: int, edges: List[List[int]]) -> int:
    """Launch BFS from each unvisited node. Each launch = 1 component."""
    adj = [[] for _ in range(n)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    visited = [False] * n
    count = 0

    for i in range(n):
        if not visited[i]:
            count += 1
            queue = deque([i])
            visited[i] = True
            while queue:
                node = queue.popleft()
                for neighbor in adj[node]:
                    if not visited[neighbor]:
                        visited[neighbor] = True
                        queue.append(neighbor)

    return count


# ============================================================
# APPROACH 3: UNION-FIND (Disjoint Set Union)
# Time: O(V + E * alpha(V)) ~ O(V + E) | Space: O(V)
# ============================================================
def count_components_uf(n: int, edges: List[List[int]]) -> int:
    """Process edges with Union-Find. Count remaining distinct roots."""
    parent = list(range(n))
    rank = [0] * n

    def find(x: int) -> int:
        if parent[x] != x:
            parent[x] = find(parent[x])  # path compression
        return parent[x]

    def union(x: int, y: int) -> bool:
        root_x, root_y = find(x), find(y)
        if root_x == root_y:
            return False
        if rank[root_x] < rank[root_y]:
            parent[root_x] = root_y
        elif rank[root_x] > rank[root_y]:
            parent[root_y] = root_x
        else:
            parent[root_y] = root_x
            rank[root_x] += 1
        return True

    components = n
    for u, v in edges:
        if union(u, v):
            components -= 1

    return components


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Connected Components ===\n")

    test_cases = [
        (5, [[0, 1], [1, 2], [3, 4]], 2),
        (5, [[0, 1], [1, 2], [2, 3], [3, 4]], 1),
        (4, [], 4),
        (3, [[0, 1]], 2),
        (1, [], 1),
    ]

    for n, edges, expected in test_cases:
        d = count_components_dfs(n, edges)
        b = count_components_bfs(n, edges)
        u = count_components_uf(n, edges)
        status = "PASS" if d == expected and b == expected and u == expected else "FAIL"
        print(f"n={n}, edges={edges}")
        print(f"  DFS: {d}  BFS: {b}  UF: {u}  Expected: {expected}  [{status}]\n")
