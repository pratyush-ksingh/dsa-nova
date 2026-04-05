"""
Problem: DFS Traversal
Difficulty: EASY | XP: 10

Implement DFS on a graph. Return nodes in DFS order.
Handle disconnected graphs.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: RECURSIVE DFS
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def dfs_recursive(V: int, adj: List[List[int]]) -> List[int]:
    """
    Standard recursive DFS with outer loop for disconnected components.
    """
    visited = [False] * V
    result = []

    def dfs(node: int):
        visited[node] = True
        result.append(node)
        for neighbor in adj[node]:
            if not visited[neighbor]:
                dfs(neighbor)

    # Outer loop: handles disconnected components
    for i in range(V):
        if not visited[i]:
            dfs(i)

    return result


# ============================================================
# APPROACH 2: ITERATIVE DFS (Explicit Stack)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def dfs_iterative(V: int, adj: List[List[int]]) -> List[int]:
    """
    Iterative DFS using explicit stack. Avoids recursion depth issues.
    """
    visited = [False] * V
    result = []

    for i in range(V):
        if not visited[i]:
            stack = [i]
            while stack:
                node = stack.pop()
                if visited[node]:
                    continue
                visited[node] = True
                result.append(node)

                # Push in reverse order so smallest neighbor is popped first
                for neighbor in reversed(adj[node]):
                    if not visited[neighbor]:
                        stack.append(neighbor)

    return result


# ============================================================
# HELPER: Build adjacency list
# ============================================================
def build_graph(V: int, edges: List[List[int]]) -> List[List[int]]:
    adj = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)
    return adj


if __name__ == "__main__":
    print("=== DFS Traversal ===\n")

    # Test 1: Connected graph
    #   0 --- 1
    #   |     |
    #   2     4
    adj1 = build_graph(5, [[0,1], [0,2], [1,4]])
    print("Test 1 (connected):")
    print(f"  Recursive:  {dfs_recursive(5, adj1)}")
    adj1 = build_graph(5, [[0,1], [0,2], [1,4]])
    print(f"  Iterative:  {dfs_iterative(5, adj1)}")

    # Test 2: Disconnected graph
    #   0 --- 1       3
    #   |     |       |
    #   2     4       5
    adj2 = build_graph(6, [[0,1], [0,2], [1,4], [3,5]])
    print("\nTest 2 (disconnected):")
    print(f"  Recursive:  {dfs_recursive(6, adj2)}")
    adj2 = build_graph(6, [[0,1], [0,2], [1,4], [3,5]])
    print(f"  Iterative:  {dfs_iterative(6, adj2)}")

    # Test 3: Single node
    adj3 = build_graph(1, [])
    print(f"\nTest 3 (single node): {dfs_recursive(1, adj3)}")

    # Test 4: No edges
    adj4 = build_graph(4, [])
    print(f"Test 4 (no edges):    {dfs_recursive(4, adj4)}")
