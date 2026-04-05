"""
Problem: Commutable Islands
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given A islands and B bridges (weighted edges), find minimum cost to connect all islands.
Classic Minimum Spanning Tree (MST) problem.
"""

from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE
# Kruskal's MST with Union-Find
# Time: O(E log E)  |  Space: O(V)
# ============================================================
def brute_force(A: int, B: List[List[int]]) -> int:
    parent = list(range(A + 1))
    rank = [0] * (A + 1)

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px == py:
            return False
        if rank[px] < rank[py]:
            px, py = py, px
        parent[py] = px
        if rank[px] == rank[py]:
            rank[px] += 1
        return True

    edges = sorted(B, key=lambda e: e[2])
    total_cost = 0
    edges_used = 0

    for u, v, w in edges:
        if union(u, v):
            total_cost += w
            edges_used += 1
            if edges_used == A - 1:
                break

    return total_cost


# ============================================================
# APPROACH 2: OPTIMAL
# Prim's MST using min-heap (adjacency list)
# Time: O(E log V)  |  Space: O(V + E)
# ============================================================
def optimal(A: int, B: List[List[int]]) -> int:
    adj = [[] for _ in range(A + 1)]
    for u, v, w in B:
        adj[u].append((w, v))
        adj[v].append((w, u))

    visited = [False] * (A + 1)
    heap = [(0, 1)]  # (cost, node) - start from island 1
    total_cost = 0

    while heap:
        cost, node = heapq.heappop(heap)
        if visited[node]:
            continue
        visited[node] = True
        total_cost += cost
        for w, neighbor in adj[node]:
            if not visited[neighbor]:
                heapq.heappush(heap, (w, neighbor))

    return total_cost


# ============================================================
# APPROACH 3: BEST
# Kruskal's with iterative path compression — most concise
# Time: O(E log E)  |  Space: O(V)
# ============================================================
def best(A: int, B: List[List[int]]) -> int:
    parent = list(range(A + 1))

    def find(x):
        root = x
        while parent[root] != root:
            root = parent[root]
        while parent[x] != root:  # path compression
            parent[x], x = root, parent[x]
        return root

    total = 0
    for u, v, w in sorted(B, key=lambda e: e[2]):
        pu, pv = find(u), find(v)
        if pu != pv:
            parent[pu] = pv
            total += w

    return total


if __name__ == "__main__":
    print("=== Commutable Islands ===")

    # 4 islands, MST = 6
    A = 4
    B = [[1, 2, 1], [1, 3, 4], [1, 4, 3], [2, 4, 2], [3, 4, 5]]
    print(f"BruteForce (Kruskal): {brute_force(A, B)}")  # 6
    print(f"Optimal    (Prim's):  {optimal(A, B)}")       # 6
    print(f"Best       (Kruskal): {best(A, B)}")           # 6

    # 3 islands, MST = 5
    A2 = 3
    B2 = [[1, 2, 6], [1, 3, 3], [2, 3, 2]]
    print(f"Test2 BruteForce: {brute_force(A2, B2)}")  # 5
    print(f"Test2 Optimal:    {optimal(A2, B2)}")       # 5
