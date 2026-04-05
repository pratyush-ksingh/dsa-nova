"""
Problem: Bipartite Graph using DFS
Difficulty: MEDIUM | XP: 25
LeetCode 785

A graph is bipartite if we can split its nodes into two independent sets A and B
such that every edge connects a node in A to a node in B.
Equivalently: the graph is 2-colorable (no odd-length cycles).
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — Try all possible 2-colorings
# Time: O(2^n * (V+E))  |  Space: O(n)
# For each of the 2^n possible color assignments, verify validity.
# Only feasible for tiny graphs; purely illustrative.
# ============================================================
def brute_force(graph: List[List[int]]) -> bool:
    """
    Enumerate all 2^n color assignments and check if any is valid.
    A coloring is valid if no edge connects two same-colored nodes.
    """
    n = len(graph)

    for mask in range(1 << n):  # each bit = color (0 or 1) for node i
        valid = True
        for u in range(n):
            for v in graph[u]:
                color_u = (mask >> u) & 1
                color_v = (mask >> v) & 1
                if color_u == color_v:
                    valid = False
                    break
            if not valid:
                break
        if valid:
            return True

    return False  # no valid 2-coloring found


# ============================================================
# APPROACH 2: OPTIMAL — DFS coloring (2-color via DFS)
# Time: O(V + E)  |  Space: O(V) — color array + recursion stack
# Assign color 0 to each unvisited node; DFS assigns alternating colors.
# If we find a neighbor already colored the same as current -> not bipartite.
# ============================================================
def optimal(graph: List[List[int]]) -> bool:
    """
    DFS-based 2-coloring. Use color[] array: -1=unvisited, 0=red, 1=blue.
    For each unvisited node, start a DFS and alternate colors.
    If any neighbor has the same color as current node, return False.
    Handle disconnected graphs by starting DFS from every unvisited node.
    """
    n = len(graph)
    color = [-1] * n  # -1 = unvisited

    def dfs(node: int, c: int) -> bool:
        color[node] = c
        for neighbor in graph[node]:
            if color[neighbor] == -1:
                # Unvisited: recurse with opposite color
                if not dfs(neighbor, 1 - c):
                    return False
            elif color[neighbor] == c:
                # Same color as current -> odd cycle -> not bipartite
                return False
        return True

    for start in range(n):
        if color[start] == -1:
            if not dfs(start, 0):
                return False

    return True


# ============================================================
# APPROACH 3: BEST — BFS coloring (iterative, avoids stack overflow)
# Time: O(V + E)  |  Space: O(V)
# Same idea as DFS but uses BFS, which is safer for very deep graphs
# (no recursion limit issues) and often preferred in interviews.
# ============================================================
def best(graph: List[List[int]]) -> bool:
    """
    BFS-based 2-coloring. Start BFS from each unvisited node.
    Color the starting node 0, then alternate colors for neighbors.
    If neighbor already has same color as current -> not bipartite.
    """
    n = len(graph)
    color = [-1] * n

    for start in range(n):
        if color[start] != -1:
            continue

        # BFS from this component
        queue = deque([start])
        color[start] = 0

        while queue:
            node = queue.popleft()
            for neighbor in graph[node]:
                if color[neighbor] == -1:
                    color[neighbor] = 1 - color[node]
                    queue.append(neighbor)
                elif color[neighbor] == color[node]:
                    return False

    return True


if __name__ == "__main__":
    print("=== Bipartite Graph DFS ===")

    # Bipartite: 0-1-2-3-0 (even cycle of length 4)
    g1 = [[1, 3], [0, 2], [1, 3], [0, 2]]
    print(f"Even cycle (bipartite):    Brute={brute_force(g1)}  Optimal={optimal(g1)}  Best={best(g1)}")

    # Not bipartite: 0-1-2-0 (odd cycle of length 3)
    g2 = [[1, 2], [0, 2], [0, 1]]
    print(f"Odd cycle (not bipartite): Brute={brute_force(g2)}  Optimal={optimal(g2)}  Best={best(g2)}")

    # Disconnected bipartite graph
    g3 = [[1], [0], [3], [2]]
    print(f"Disconnected bipartite:    Brute={brute_force(g3)}  Optimal={optimal(g3)}  Best={best(g3)}")

    # Single node (trivially bipartite)
    g4 = [[]]
    print(f"Single node:               Brute={brute_force(g4)}  Optimal={optimal(g4)}  Best={best(g4)}")
