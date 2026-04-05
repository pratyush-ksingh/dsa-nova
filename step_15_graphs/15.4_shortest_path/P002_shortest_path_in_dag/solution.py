"""
Problem: Shortest Path in DAG
Difficulty: MEDIUM | XP: 25

Find shortest paths from a source vertex in a weighted DAG
using topological sort + edge relaxation. O(V + E) time.
Handles negative edge weights (no cycles in a DAG).
"""
from typing import List, Tuple
from collections import deque
import math


# ============================================================
# APPROACH 1: DFS EXPLORE ALL PATHS (BRUTE FORCE)
# Time: O(2^V) worst case  |  Space: O(V)
# ============================================================
def shortest_path_brute(V: int, adj: List[List[Tuple[int, int]]], src: int) -> List[int]:
    """
    DFS from source, exploring all paths. Update dist when shorter path found.
    Exponential in worst case but simple to understand.
    """
    dist = [math.inf] * V
    dist[src] = 0

    def dfs(u: int, curr_dist: int):
        for v, w in adj[u]:
            new_dist = curr_dist + w
            if new_dist < dist[v]:
                dist[v] = new_dist
                dfs(v, new_dist)

    dfs(src, 0)
    return [d if d != math.inf else -1 for d in dist]


# ============================================================
# APPROACH 2: TOPOLOGICAL SORT (DFS) + RELAXATION (OPTIMAL)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def shortest_path_dag(V: int, adj: List[List[Tuple[int, int]]], src: int) -> List[int]:
    """
    1. Topo sort all vertices using DFS post-order
    2. Initialize dist[src]=0, rest=INF
    3. Process vertices in topo order, relaxing each outgoing edge
    """
    # Step 1: Topological sort using DFS
    visited = [False] * V
    stack = []

    def topo_dfs(u: int):
        visited[u] = True
        for v, _ in adj[u]:
            if not visited[v]:
                topo_dfs(v)
        stack.append(u)  # Post-order

    for i in range(V):
        if not visited[i]:
            topo_dfs(i)

    # Step 2: Initialize distances
    dist = [math.inf] * V
    dist[src] = 0

    # Step 3: Process in topo order (reverse of post-order stack)
    while stack:
        u = stack.pop()
        if dist[u] != math.inf:  # Only relax from reachable nodes
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w

    return [d if d != math.inf else -1 for d in dist]


# ============================================================
# APPROACH 3: KAHN'S BFS TOPO SORT + RELAXATION (BEST VARIANT)
# Time: O(V + E)  |  Space: O(V + E)
# No recursion; built-in cycle detection
# ============================================================
def shortest_path_dag_kahns(V: int, adj: List[List[Tuple[int, int]]], src: int) -> List[int]:
    """
    Same relaxation step, but topo sort uses Kahn's BFS (in-degree method).
    Advantage: iterative (no recursion depth limit) and detects cycles.
    """
    # Step 1: Compute in-degrees
    in_degree = [0] * V
    for u in range(V):
        for v, _ in adj[u]:
            in_degree[v] += 1

    # Step 2: Kahn's BFS
    queue = deque()
    for i in range(V):
        if in_degree[i] == 0:
            queue.append(i)

    topo_order = []
    while queue:
        u = queue.popleft()
        topo_order.append(u)
        for v, _ in adj[u]:
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)

    # Cycle detection
    if len(topo_order) != V:
        print("  WARNING: Graph has a cycle -- not a valid DAG!")
        return []

    # Step 3: Relax edges in topo order
    dist = [math.inf] * V
    dist[src] = 0

    for u in topo_order:
        if dist[u] != math.inf:
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w

    return [d if d != math.inf else -1 for d in dist]


def build_adj(V: int, edges: List[List[int]]) -> List[List[Tuple[int, int]]]:
    """Helper: build weighted adjacency list from [from, to, weight] triples."""
    adj: List[List[Tuple[int, int]]] = [[] for _ in range(V)]
    for u, v, w in edges:
        adj[u].append((v, w))
    return adj


if __name__ == "__main__":
    print("=== Shortest Path in DAG ===\n")

    # Test 1: Standard DAG
    edges1 = [[0,1,2],[0,4,1],[1,2,3],[2,3,6],[4,2,2],[4,5,4],[5,3,1]]
    adj1 = build_adj(6, edges1)
    print("Test 1 (V=6, src=0): Expected [0, 2, 3, 6, 1, 5]")
    print(f"  Brute: {shortest_path_brute(6, build_adj(6, edges1), 0)}")
    print(f"  DFS:   {shortest_path_dag(6, build_adj(6, edges1), 0)}")
    print(f"  Kahns: {shortest_path_dag_kahns(6, adj1, 0)}")

    # Test 2: Unreachable nodes
    adj2 = build_adj(4, [[0,1,1],[1,2,2]])
    print(f"\nTest 2 (V=4, src=0): Expected [0, 1, 3, -1]")
    print(f"  DFS:   {shortest_path_dag(4, adj2, 0)}")

    # Test 3: Source in the middle
    adj3 = build_adj(4, [[0,1,5],[1,2,3],[2,3,1]])
    print(f"\nTest 3 (V=4, src=1): Expected [-1, 0, 3, 4]")
    print(f"  DFS:   {shortest_path_dag(4, adj3, 1)}")

    # Test 4: Negative weights
    adj4 = build_adj(3, [[0,1,2],[0,2,5],[1,2,-3]])
    print(f"\nTest 4 (negative weights, src=0): Expected [0, 2, -1]")
    print(f"  DFS:   {shortest_path_dag(3, adj4, 0)}")

    # Test 5: Single node
    adj5 = build_adj(1, [])
    print(f"\nTest 5 (single node, src=0): Expected [0]")
    print(f"  DFS:   {shortest_path_dag(1, adj5, 0)}")
