"""
Problem: Detect Cycle in Directed Graph using DFS
Difficulty: MEDIUM | XP: 25
GFG / Striver Graph Series

Given a directed graph with V vertices and adjacency list adj[],
return True if the graph contains a cycle, False otherwise.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — Check all paths (DFS from every node)
# Time: O(V * (V + E))  |  Space: O(V)
# From every node, do a DFS and track visited-in-current-path set.
# If we revisit a node in the current path, a cycle exists.
# This is redundant (revisits nodes) but illustrates the concept clearly.
# ============================================================
def brute_force(V: int, adj: List[List[int]]) -> bool:
    """
    For every node, do an independent DFS tracking the current path.
    If we encounter a node already in the current path, it's a cycle.
    Inefficient because we restart from every node without memoization.
    """
    def dfs_from(start: int) -> bool:
        path = set()
        visited = set()

        def dfs(node: int) -> bool:
            if node in path:
                return True   # back edge -> cycle
            if node in visited:
                return False  # already fully explored, no cycle via this node
            path.add(node)
            for neighbor in adj[node]:
                if dfs(neighbor):
                    return True
            path.remove(node)
            visited.add(node)
            return False

        return dfs(start)

    for v in range(V):
        if dfs_from(v):
            return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL — DFS with 3-color (visited + recursion stack)
# Time: O(V + E)  |  Space: O(V)
# 3 states: WHITE(0)=unvisited, GRAY(1)=in recursion stack, BLACK(2)=done.
# A back edge (edge to GRAY node) means a cycle exists.
# ============================================================
def optimal(V: int, adj: List[List[int]]) -> bool:
    """
    Standard DFS cycle detection for directed graphs.
    - vis[node] = 0: not visited (WHITE)
    - vis[node] = 1: currently in DFS stack (GRAY) — means we're exploring it
    - vis[node] = 2: fully processed (BLACK) — no cycle through this node

    If we reach a node with vis = 1 (GRAY), we found a back edge = cycle.
    """
    vis = [0] * V   # 0=unvisited, 1=in-stack (gray), 2=done (black)

    def dfs(node: int) -> bool:
        vis[node] = 1  # mark GRAY (in recursion stack)
        for neighbor in adj[node]:
            if vis[neighbor] == 0:
                if dfs(neighbor):
                    return True
            elif vis[neighbor] == 1:
                return True  # back edge -> cycle
            # vis[neighbor] == 2: already fully explored, safe to skip
        vis[node] = 2  # mark BLACK (done)
        return False

    for v in range(V):
        if vis[v] == 0:
            if dfs(v):
                return True

    return False


# ============================================================
# APPROACH 3: BEST — Kahn's Algorithm (Topological Sort via BFS)
# Time: O(V + E)  |  Space: O(V)
# Idea: if a valid topological ordering exists, no cycle.
# Run BFS-based topo sort (Kahn's). If we process fewer than V nodes,
# a cycle prevented some nodes from ever reaching in-degree 0.
# ============================================================
def best(V: int, adj: List[List[int]]) -> bool:
    """
    Kahn's Algorithm (BFS topo sort):
    1. Compute in-degree for every node.
    2. Enqueue all nodes with in-degree 0.
    3. Process: dequeue a node, reduce neighbors' in-degree. If any hits 0, enqueue.
    4. Count processed nodes. If count < V, cycle exists (those nodes could never be dequeued).
    """
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    queue = deque()
    for v in range(V):
        if in_degree[v] == 0:
            queue.append(v)

    count = 0  # nodes successfully processed in topo order
    while queue:
        node = queue.popleft()
        count += 1
        for neighbor in adj[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    return count != V  # if count < V, some nodes have unresolved cycle


if __name__ == "__main__":
    print("=== Detect Cycle in Directed Graph ===")

    # Cycle: 0->1->2->0
    adj1 = [[1], [2], [0]]
    print(f"Graph with cycle (0->1->2->0):")
    print(f"  Brute={brute_force(3, adj1)}  Optimal={optimal(3, adj1)}  Best={best(3, adj1)}")

    # No cycle: 0->1->2, 0->2
    adj2 = [[1, 2], [2], []]
    print(f"DAG (no cycle):")
    print(f"  Brute={brute_force(3, adj2)}  Optimal={optimal(3, adj2)}  Best={best(3, adj2)}")

    # Disconnected graph with cycle in one component
    adj3 = [[1], [2], [0], [4], []]  # 0->1->2->0 (cycle), 3->4 (no cycle)
    print(f"Disconnected graph (cycle in component 0-1-2):")
    print(f"  Brute={brute_force(5, adj3)}  Optimal={optimal(5, adj3)}  Best={best(5, adj3)}")

    # Self-loop
    adj4 = [[0]]   # node 0 points to itself
    print(f"Self-loop:")
    print(f"  Brute={brute_force(1, adj4)}  Optimal={optimal(1, adj4)}  Best={best(1, adj4)}")
