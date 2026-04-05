"""
Problem: Two Teams (Graph Bipartiteness / 2-Coloring)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import List, Optional
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS 2-coloring
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def brute_force(n: int, edges: List[List[int]]) -> bool:
    """
    Check if a graph is bipartite using BFS. Try to 2-color the graph.
    If any adjacent nodes share the same color, return False.
    n = number of nodes (1-indexed), edges = list of [u, v] pairs.
    """
    adj = [[] for _ in range(n + 1)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    color = [-1] * (n + 1)
    for start in range(1, n + 1):
        if color[start] != -1:
            continue
        queue = deque([start])
        color[start] = 0
        while queue:
            node = queue.popleft()
            for nei in adj[node]:
                if color[nei] == -1:
                    color[nei] = 1 - color[node]
                    queue.append(nei)
                elif color[nei] == color[node]:
                    return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL — DFS 2-coloring
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def optimal(n: int, edges: List[List[int]]) -> bool:
    """
    Check bipartiteness using iterative DFS with a stack.
    """
    adj = [[] for _ in range(n + 1)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    color = [-1] * (n + 1)
    for start in range(1, n + 1):
        if color[start] != -1:
            continue
        stack = [start]
        color[start] = 0
        while stack:
            node = stack.pop()
            for nei in adj[node]:
                if color[nei] == -1:
                    color[nei] = 1 - color[node]
                    stack.append(nei)
                elif color[nei] == color[node]:
                    return False
    return True


# ============================================================
# APPROACH 3: BEST — Union-Find bipartite check
# Time: O(V + E * alpha(V))  |  Space: O(V)
# ============================================================
def best(n: int, edges: List[List[int]]) -> bool:
    """
    Use Union-Find: for each node, union all its neighbors together.
    If a node ends up in the same set as any of its neighbors, the graph
    is not bipartite.
    """
    parent = list(range(n + 1))
    rank = [0] * (n + 1)

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(a, b):
        ra, rb = find(a), find(b)
        if ra == rb:
            return
        if rank[ra] < rank[rb]:
            ra, rb = rb, ra
        parent[rb] = ra
        if rank[ra] == rank[rb]:
            rank[ra] += 1

    adj = [[] for _ in range(n + 1)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    for node in range(1, n + 1):
        for nei in adj[node]:
            if find(node) == find(nei):
                return False
            union(adj[node][0], nei)
    return True


if __name__ == "__main__":
    print("=== Two Teams ===")

    # Bipartite graph: 1-2, 2-3, 3-4, 4-1 (even cycle)
    print(f"Brute:   {brute_force(4, [[1,2],[2,3],[3,4],[4,1]])}")   # True
    print(f"Optimal: {optimal(4, [[1,2],[2,3],[3,4],[4,1]])}")       # True
    print(f"Best:    {best(4, [[1,2],[2,3],[3,4],[4,1]])}")          # True

    # Non-bipartite: triangle 1-2, 2-3, 3-1 (odd cycle)
    print(f"Brute:   {brute_force(3, [[1,2],[2,3],[3,1]])}")         # False
    print(f"Optimal: {optimal(3, [[1,2],[2,3],[3,1]])}")             # False
    print(f"Best:    {best(3, [[1,2],[2,3],[3,1]])}")                # False
