"""
Problem: Cycle in Undirected Graph
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given V vertices and a list of edges forming an undirected graph,
detect whether a cycle exists. Return 1 if yes, 0 if no.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive DFS with parent tracking
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def brute_force(V: int, edges: List[List[int]]) -> int:
    adj = [[] for _ in range(V + 1)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    vis = [False] * (V + 1)

    def dfs(node, parent):
        vis[node] = True
        for nb in adj[node]:
            if not vis[nb]:
                if dfs(nb, node):
                    return True
            elif nb != parent:
                return True
        return False

    for i in range(1, V + 1):
        if not vis[i] and dfs(i, -1):
            return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - Iterative BFS with parent tracking
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def optimal(V: int, edges: List[List[int]]) -> int:
    adj = [[] for _ in range(V + 1)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    vis = [False] * (V + 1)

    for start in range(1, V + 1):
        if vis[start]:
            continue
        q = deque([(start, -1)])
        vis[start] = True
        while q:
            node, parent = q.popleft()
            for nb in adj[node]:
                if not vis[nb]:
                    vis[nb] = True
                    q.append((nb, node))
                elif nb != parent:
                    return 1
    return 0


# ============================================================
# APPROACH 3: BEST - Union-Find with path compression + rank
# Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
# ============================================================
# Union-Find is the most elegant solution: iterate edges,
# if both endpoints share a root => cycle. No adj list needed.
# Real-life use: Kruskal's MST cycle detection, package
# dependency cycle detection in npm/pip.
# ============================================================
def best(V: int, edges: List[List[int]]) -> int:
    parent = list(range(V + 1))
    rank = [0] * (V + 1)

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]  # path compression (halving)
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px == py:
            return False  # cycle
        if rank[px] < rank[py]:
            px, py = py, px
        parent[py] = px
        if rank[px] == rank[py]:
            rank[px] += 1
        return True

    for u, v in edges:
        if not union(u, v):
            return 1
    return 0


if __name__ == "__main__":
    print("=== Cycle in Undirected Graph ===")

    # Test 1: 4-cycle
    e1 = [[1,2],[2,3],[3,4],[4,1]]
    print(f"Test1 Brute   (expect 1): {brute_force(4, e1)}")
    print(f"Test1 Optimal (expect 1): {optimal(4, e1)}")
    print(f"Test1 Best    (expect 1): {best(4, e1)}")

    # Test 2: tree, no cycle
    e2 = [[1,2],[2,3],[3,4]]
    print(f"Test2 Brute   (expect 0): {brute_force(4, e2)}")
    print(f"Test2 Optimal (expect 0): {optimal(4, e2)}")
    print(f"Test2 Best    (expect 0): {best(4, e2)}")

    # Test 3: disconnected, cycle in one component
    e3 = [[1,2],[3,4],[4,5],[5,3]]
    print(f"Test3 Best    (expect 1): {best(5, e3)}")

    # Test 4: no edges
    print(f"Test4 Best    (expect 0): {best(1, [])}")
