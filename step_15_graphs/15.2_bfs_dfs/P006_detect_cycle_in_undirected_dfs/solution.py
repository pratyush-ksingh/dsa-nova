"""
Problem: Detect Cycle in Undirected Graph using DFS
Difficulty: MEDIUM | XP: 25

Given an adjacency list for an undirected graph with V vertices and E edges,
detect whether the graph contains a cycle or not.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE (BFS-based cycle detection)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def detect_cycle_bfs(V: int, adj: List[List[int]]) -> bool:
    """
    BFS approach: use parent tracking to detect back edges.
    If a visited neighbor is not the parent, cycle exists.
    """
    visited = [False] * V

    for start in range(V):
        if visited[start]:
            continue
        queue = deque([(start, -1)])
        visited[start] = True

        while queue:
            node, parent = queue.popleft()
            for neighbor in adj[node]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append((neighbor, node))
                elif neighbor != parent:
                    return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL (DFS with parent tracking)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def detect_cycle_dfs(V: int, adj: List[List[int]]) -> bool:
    """
    DFS approach: for each unvisited node, run DFS tracking
    the parent. If we visit a neighbor that is already visited
    and is not our parent, a cycle exists.
    """
    visited = [False] * V

    def dfs(node: int, parent: int) -> bool:
        visited[node] = True
        for neighbor in adj[node]:
            if not visited[neighbor]:
                if dfs(neighbor, node):
                    return True
            elif neighbor != parent:
                return True
        return False

    for i in range(V):
        if not visited[i]:
            if dfs(i, -1):
                return True
    return False


# ============================================================
# APPROACH 3: BEST (Union-Find with path compression)
# Time: O(V + E * alpha(V)) ~ O(V + E)  |  Space: O(V)
# ============================================================
def detect_cycle_union_find(V: int, adj: List[List[int]]) -> bool:
    """
    Union-Find: initially each node is its own component.
    For each edge (u, v) where u < v (to avoid double counting),
    if find(u) == find(v), they are already connected => cycle.
    Otherwise union them.
    Uses path compression + union by rank for near O(1) operations.
    """
    parent = list(range(V))
    rank = [0] * V

    def find(x: int) -> int:
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]

    def union(x: int, y: int) -> bool:
        """Returns False if x and y already in same set (cycle detected)."""
        rx, ry = find(x), find(y)
        if rx == ry:
            return False  # cycle!
        if rank[rx] < rank[ry]:
            parent[rx] = ry
        elif rank[rx] > rank[ry]:
            parent[ry] = rx
        else:
            parent[ry] = rx
            rank[rx] += 1
        return True

    # Process each edge once (u < v to avoid double-counting)
    for u in range(V):
        for v in adj[u]:
            if u < v:
                if not union(u, v):
                    return True
    return False


if __name__ == "__main__":
    print("=== Detect Cycle in Undirected DFS ===\n")

    # Test 1: Graph with cycle: 0-1-2-0
    adj1 = [[1, 2], [0, 2], [0, 1]]
    print(f"Test 1 (cycle 0-1-2-0): Expected True")
    print(f"  BFS:        {detect_cycle_bfs(3, adj1)}")
    print(f"  DFS:        {detect_cycle_dfs(3, adj1)}")
    print(f"  Union-Find: {detect_cycle_union_find(3, adj1)}")

    # Test 2: Tree (no cycle): 0-1, 0-2
    adj2 = [[1, 2], [0], [0]]
    print(f"\nTest 2 (tree): Expected False")
    print(f"  BFS:        {detect_cycle_bfs(3, adj2)}")
    print(f"  DFS:        {detect_cycle_dfs(3, adj2)}")
    print(f"  Union-Find: {detect_cycle_union_find(3, adj2)}")

    # Test 3: Disconnected with cycle
    adj3 = [[1, 2], [0, 2], [0, 1], [4], [3]]
    print(f"\nTest 3 (disconnected, has cycle): Expected True")
    print(f"  BFS:        {detect_cycle_bfs(5, adj3)}")
    print(f"  DFS:        {detect_cycle_dfs(5, adj3)}")
    print(f"  Union-Find: {detect_cycle_union_find(5, adj3)}")

    # Test 4: Single node
    adj4 = [[]]
    print(f"\nTest 4 (single node): Expected False")
    print(f"  BFS:        {detect_cycle_bfs(1, adj4)}")
    print(f"  DFS:        {detect_cycle_dfs(1, adj4)}")
    print(f"  Union-Find: {detect_cycle_union_find(1, adj4)}")
