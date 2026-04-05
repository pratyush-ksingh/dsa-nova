"""
Problem: Is Graph Bipartite? (LeetCode 785)
Difficulty: MEDIUM | XP: 25

Given an undirected graph represented as an adjacency list (graph[u] = list of
neighbors of u), determine if the graph is bipartite.

A graph is bipartite if we can split its vertices into two independent sets A
and B such that every edge connects a vertex in A to one in B — equivalently,
the graph is 2-colorable (no odd-length cycles).
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — Try all 2^n colorings
# Time: O(2^n * (V + E))  |  Space: O(n)
# ============================================================
def is_bipartite_brute(graph: List[List[int]]) -> bool:
    """
    Intuition: Enumerate all possible 2-colorings (each vertex gets color 0
    or 1). For each assignment, check whether every edge connects vertices of
    different colors. Return True if any valid coloring exists.

    Impractical for large n but demonstrates the definition directly.
    """
    n = len(graph)

    for mask in range(1 << n):
        # color[v] = (mask >> v) & 1
        valid = True
        for u in range(n):
            color_u = (mask >> u) & 1
            for v in graph[u]:
                color_v = (mask >> v) & 1
                if color_u == color_v:
                    valid = False
                    break
            if not valid:
                break
        if valid:
            return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL — BFS 2-coloring
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def is_bipartite_optimal(graph: List[List[int]]) -> bool:
    """
    Intuition: Try to 2-color the graph. Start from any uncolored vertex,
    assign it color 0, then assign all its neighbors color 1, their neighbors
    color 0, and so on (BFS level-by-level alternation). If we ever find a
    neighbor already colored with the same color as the current vertex, the
    graph is NOT bipartite (odd cycle detected).

    Handle disconnected graphs by repeating for each unvisited component.
    """
    n = len(graph)
    color = [-1] * n  # -1 = uncolored

    for start in range(n):
        if color[start] != -1:
            continue  # already colored as part of another component
        queue = deque([start])
        color[start] = 0
        while queue:
            u = queue.popleft()
            for v in graph[u]:
                if color[v] == -1:
                    color[v] = 1 - color[u]  # opposite color
                    queue.append(v)
                elif color[v] == color[u]:
                    return False  # same color on both ends of an edge

    return True


# ============================================================
# APPROACH 3: BEST — DFS 2-coloring (recursive)
# Time: O(V + E)  |  Space: O(V) stack
# ============================================================
def is_bipartite_best(graph: List[List[int]]) -> bool:
    """
    Intuition: Same 2-coloring logic as Approach 2 but implemented with DFS.
    DFS is often slightly more concise and equally efficient.

    Assign color c to the current vertex and recurse with color 1-c for each
    uncolored neighbor. If a neighbor is already colored the same as c, return
    False immediately.

    Both BFS and DFS are optimal; BFS is slightly better for very deep graphs
    (avoids Python recursion limit), but DFS is cleaner to write.
    """
    n = len(graph)
    color = [-1] * n

    def dfs(u: int, c: int) -> bool:
        color[u] = c
        for v in graph[u]:
            if color[v] == -1:
                if not dfs(v, 1 - c):
                    return False
            elif color[v] == c:
                return False
        return True

    for start in range(n):
        if color[start] == -1:
            if not dfs(start, 0):
                return False

    return True


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Is Graph Bipartite? ===\n")

    # Bipartite: cycle of 4 (even cycle)
    # 0 - 1
    # |   |
    # 3 - 2
    g1 = [[1, 3], [0, 2], [1, 3], [0, 2]]
    # Expected: True

    # Not bipartite: odd triangle 0-1-2-0
    g2 = [[1, 2], [0, 2], [0, 1]]
    # Expected: False

    # Disconnected graph: two isolated nodes (trivially bipartite)
    g3 = [[], []]
    # Expected: True

    for name, fn in [("Brute (2^n colorings)", is_bipartite_brute),
                     ("Optimal (BFS)", is_bipartite_optimal),
                     ("Best (DFS)", is_bipartite_best)]:
        print(f"g1 (4-cycle) {name:25s}: {fn(g1)}")
    print()
    for name, fn in [("Brute (2^n colorings)", is_bipartite_brute),
                     ("Optimal (BFS)", is_bipartite_optimal),
                     ("Best (DFS)", is_bipartite_best)]:
        print(f"g2 (triangle) {name:24s}: {fn(g2)}")
    print()
    for name, fn in [("Optimal (BFS)", is_bipartite_optimal),
                     ("Best (DFS)", is_bipartite_best)]:
        print(f"g3 (isolated) {name:24s}: {fn(g3)}")
