"""
Problem: Number of Operations to Make Network Connected (LeetCode #1319)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS to Count Components
# Time: O(V + E)  |  Space: O(V + E)
# Build adjacency list, DFS to count connected components.
# ============================================================
def brute_force(n: int, connections: List[List[int]]) -> int:
    if len(connections) < n - 1:
        return -1

    # Build adjacency list
    adj = defaultdict(list)
    for a, b in connections:
        adj[a].append(b)
        adj[b].append(a)

    # Count connected components via DFS
    visited = [False] * n
    components = 0

    def dfs(node: int):
        visited[node] = True
        for neighbor in adj[node]:
            if not visited[neighbor]:
                dfs(neighbor)

    for i in range(n):
        if not visited[i]:
            components += 1
            dfs(i)

    return components - 1


# ============================================================
# UNION-FIND helper
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


# ============================================================
# APPROACH 2: OPTIMAL -- Union-Find
# Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
# Process edges with Union-Find, count remaining components.
# ============================================================
def optimal(n: int, connections: List[List[int]]) -> int:
    if len(connections) < n - 1:
        return -1

    dsu = DSU(n)
    components = n

    for a, b in connections:
        if dsu.union(a, b):
            components -= 1

    return components - 1


# ============================================================
# APPROACH 3: BEST -- Union-Find with Redundant Cable Tracking
# Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
# Same Union-Find, also counts redundant cables for verification.
# ============================================================
def best(n: int, connections: List[List[int]]) -> int:
    if len(connections) < n - 1:
        return -1

    dsu = DSU(n)
    components = n
    redundant = 0

    for a, b in connections:
        if dsu.union(a, b):
            components -= 1
        else:
            redundant += 1  # failed union = redundant cable

    # Proof that redundant >= components - 1:
    # total_edges = successful_unions + redundant
    # successful_unions = n - components
    # total_edges >= n - 1 (checked above)
    # => redundant = total_edges - (n - components) >= (n-1) - (n - components) = components - 1
    return components - 1


if __name__ == "__main__":
    print("=== Number of Operations to Make Network Connected ===\n")

    # Example 1
    print(f"Brute:   {brute_force(4, [[0,1],[0,2],[1,2]])}")      # 1
    print(f"Optimal: {optimal(4, [[0,1],[0,2],[1,2]])}")            # 1
    print(f"Best:    {best(4, [[0,1],[0,2],[1,2]])}")               # 1

    # Example 2
    print(f"\nOptimal: {optimal(6, [[0,1],[0,2],[0,3],[1,2],[1,3]])}")  # 2

    # Impossible case
    print(f"Impossible: {optimal(6, [[0,1],[0,2],[0,3],[1,2]])}")  # -1

    # Already connected
    print(f"Connected: {optimal(4, [[0,1],[1,2],[2,3]])}")          # 0

    # Single node
    print(f"Single: {optimal(1, [])}")                              # 0

    # Two nodes, no cable
    print(f"Two isolated: {optimal(2, [])}")                        # -1
