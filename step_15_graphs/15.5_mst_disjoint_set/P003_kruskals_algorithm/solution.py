"""
Problem: Kruskal's Algorithm
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Tuple
from itertools import combinations


# ============================================================
# UNION-FIND (Disjoint Set Union) helper
# ============================================================
class DSU:
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        px, py = self.find(x), self.find(y)
        if px == py:
            return False  # same component -- would create cycle
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check all spanning tree combinations
# Time: O(C(E,V-1) * V) -- exponential  |  Space: O(V + E)
# Enumerate all subsets of V-1 edges, check if each forms a spanning tree.
# ============================================================
def brute_force(V: int, edges: List[Tuple[int, int, int]]) -> int:
    min_weight = float('inf')

    for combo in combinations(range(len(edges)), V - 1):
        dsu = DSU(V)
        weight = 0
        valid = True
        for idx in combo:
            u, v, w = edges[idx]
            if not dsu.union(u, v):
                valid = False
                break
            weight += w
        if valid:
            # Check all vertices connected (single root)
            roots = set(dsu.find(i) for i in range(V))
            if len(roots) == 1:
                min_weight = min(min_weight, weight)

    return min_weight if min_weight != float('inf') else -1


# ============================================================
# APPROACH 2: OPTIMAL -- Kruskal's with Union-Find
# Time: O(E log E)  |  Space: O(V + E)
# Sort edges by weight, greedily add lightest non-cycle-forming edge.
# ============================================================
def optimal(V: int, edges: List[Tuple[int, int, int]]) -> int:
    # Sort edges by weight
    edges_sorted = sorted(edges, key=lambda x: x[2])

    dsu = DSU(V)
    mst_weight = 0
    edges_added = 0

    for u, v, w in edges_sorted:
        if dsu.union(u, v):
            mst_weight += w
            edges_added += 1
            if edges_added == V - 1:
                break

    return mst_weight if edges_added == V - 1 else -1


# ============================================================
# APPROACH 3: BEST -- Kruskal's with Optimized DSU + Edge Tracking
# Time: O(E log E + E * alpha(V))  |  Space: O(V + E)
# Same algorithm with full optimizations and MST edge tracking.
# ============================================================
def best(V: int, edges: List[Tuple[int, int, int]]) -> Tuple[int, List[Tuple[int, int, int]]]:
    edges_sorted = sorted(edges, key=lambda x: x[2])

    dsu = DSU(V)
    mst_weight = 0
    mst_edges = []

    for u, v, w in edges_sorted:
        if dsu.union(u, v):
            mst_weight += w
            mst_edges.append((u, v, w))
            if len(mst_edges) == V - 1:
                break

    if len(mst_edges) != V - 1:
        return -1, []

    return mst_weight, mst_edges


if __name__ == "__main__":
    print("=== Kruskal's Algorithm ===\n")

    # Example 1
    edges1 = [(0, 1, 10), (0, 2, 6), (0, 3, 5), (1, 3, 15), (2, 3, 4)]
    print(f"Brute:   {brute_force(4, edges1)}")
    print(f"Optimal: {optimal(4, edges1)}")
    weight, mst_edges = best(4, edges1)
    print(f"Best:    {weight}, edges: {mst_edges}")
    # Expected: 19

    # Example 2: triangle
    edges2 = [(0, 1, 1), (1, 2, 2), (0, 2, 3)]
    print(f"\nTriangle: {optimal(3, edges2)}")
    # Expected: 3

    # Edge case: disconnected
    edges3 = [(0, 1, 5)]
    print(f"Disconnected (V=3): {optimal(3, edges3)}")
    # Expected: -1
