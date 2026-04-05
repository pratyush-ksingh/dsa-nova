"""
Problem: Bridges in a Graph (Critical Connections)
Difficulty: HARD | XP: 50
LeetCode 1192

A bridge (or critical connection) in an undirected graph is an edge whose
removal increases the number of connected components.
Find and return all such bridges.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE — Remove each edge, check connectivity
# Time: O(E * (V + E))  |  Space: O(V + E)
# For each edge (u, v): remove it, do BFS/DFS to check if graph stays
# connected. If not, it's a bridge. Restore the edge.
# ============================================================
def brute_force(n: int, connections: List[List[int]]) -> List[List[int]]:
    """
    Try removing each edge one at a time and check connectivity via BFS.
    If removing edge (u, v) disconnects the graph -> it's a bridge.
    """
    # Build adjacency list as list of sets for easy removal
    adj = defaultdict(set)
    for u, v in connections:
        adj[u].add(v)
        adj[v].add(u)

    def is_connected_without(skip_u: int, skip_v: int) -> bool:
        """BFS to check if graph is connected when edge (skip_u, skip_v) is removed."""
        visited = set()
        queue = [0]
        visited.add(0)
        while queue:
            node = queue.pop()
            for neighbor in adj[node]:
                # Skip the removed edge in both directions
                if (node == skip_u and neighbor == skip_v) or \
                   (node == skip_v and neighbor == skip_u):
                    continue
                if neighbor not in visited:
                    visited.add(neighbor)
                    queue.append(neighbor)
        return len(visited) == n

    bridges = []
    for u, v in connections:
        if not is_connected_without(u, v):
            bridges.append([u, v])
    return bridges


# ============================================================
# APPROACH 2: OPTIMAL — Tarjan's Bridge-Finding Algorithm
# Time: O(V + E)  |  Space: O(V)
# Uses DFS with discovery time and low values.
# disc[u] = time when u was first visited.
# low[u]  = min discovery time reachable from subtree rooted at u
#           via back edges.
# Edge (u, v) is a bridge if low[v] > disc[u], meaning v cannot
# reach u or any of u's ancestors without going through edge (u, v).
# ============================================================
def optimal(n: int, connections: List[List[int]]) -> List[List[int]]:
    """
    Tarjan's algorithm for finding bridges.

    Key invariant:
      low[v] = min(disc[v],
                   min(disc[w] for back edges v->w),
                   min(low[child] for tree edges v->child))

    Edge (parent, child) is a bridge iff low[child] > disc[parent].
    This means child cannot reach parent (or any earlier node) except
    through this edge.
    """
    adj = defaultdict(list)
    for u, v in connections:
        adj[u].append(v)
        adj[v].append(u)

    disc = [-1] * n   # discovery time; -1 = unvisited
    low  = [ 0] * n   # lowest discovery time reachable
    timer = [0]        # mutable counter (wrapped in list for closure)
    bridges = []

    def dfs(node: int, parent: int) -> None:
        disc[node] = low[node] = timer[0]
        timer[0] += 1

        for neighbor in adj[node]:
            if disc[neighbor] == -1:
                # Tree edge: recurse
                dfs(neighbor, node)
                # After return, update low[node] with low[neighbor]
                low[node] = min(low[node], low[neighbor])
                # Bridge condition
                if low[neighbor] > disc[node]:
                    bridges.append([node, neighbor])
            elif neighbor != parent:
                # Back edge (not the edge we came from): update low
                low[node] = min(low[node], disc[neighbor])

    for v in range(n):
        if disc[v] == -1:
            dfs(v, -1)

    return bridges


# ============================================================
# APPROACH 3: BEST — Tarjan's (iterative, avoids recursion limit)
# Time: O(V + E)  |  Space: O(V + E)
# Same algorithm as Approach 2 but implemented iteratively using an
# explicit stack. Avoids Python recursion depth issues for large graphs.
# ============================================================
def best(n: int, connections: List[List[int]]) -> List[List[int]]:
    """
    Iterative Tarjan's algorithm.
    Stack stores (node, parent, iterator over neighbors).
    When we pop a node after processing all neighbors, we apply the
    low-value propagation and bridge check.
    """
    adj = [[] for _ in range(n)]
    for u, v in connections:
        adj[u].append(v)
        adj[v].append(u)

    disc = [-1] * n
    low  = [ 0] * n
    bridges = []
    timer = [0]

    for start in range(n):
        if disc[start] != -1:
            continue

        # Stack: (node, parent, index into adj[node])
        stack = [(start, -1, 0)]
        disc[start] = low[start] = timer[0]
        timer[0] += 1

        while stack:
            node, parent, idx = stack[-1]

            if idx < len(adj[node]):
                stack[-1] = (node, parent, idx + 1)  # advance iterator
                neighbor = adj[node][idx]

                if disc[neighbor] == -1:
                    # Tree edge: push neighbor
                    disc[neighbor] = low[neighbor] = timer[0]
                    timer[0] += 1
                    stack.append((neighbor, node, 0))
                elif neighbor != parent:
                    # Back edge: update low
                    low[node] = min(low[node], disc[neighbor])
            else:
                # All neighbors processed: pop and propagate to parent
                stack.pop()
                if stack:
                    par_node, par_parent, par_idx = stack[-1]
                    low[par_node] = min(low[par_node], low[node])
                    if low[node] > disc[par_node]:
                        bridges.append([par_node, node])

    return bridges


if __name__ == "__main__":
    print("=== Bridges in a Graph ===")

    # Graph: 0-1-2-0 (triangle) + 1-3
    # Edge 1-3 is the only bridge
    conns1 = [[0, 1], [1, 2], [2, 0], [1, 3]]
    print(f"Triangle + pendant edge:")
    print(f"  Brute:   {brute_force(4, conns1)}")
    print(f"  Optimal: {optimal(4, conns1)}")
    print(f"  Best:    {best(4, conns1)}")
    # Expected: [[1, 3]]

    # Graph: 0-1, 1-2, 2-3 (path graph - all edges are bridges)
    conns2 = [[0, 1], [1, 2], [2, 3]]
    print(f"\nPath graph (all bridges):")
    print(f"  Brute:   {brute_force(4, conns2)}")
    print(f"  Optimal: {optimal(4, conns2)}")
    print(f"  Best:    {best(4, conns2)}")
    # Expected: [[0,1],[1,2],[2,3]] in some order

    # LeetCode example: n=4, connections=[[0,1],[1,2],[2,0],[1,3]]
    # Same as conns1

    # Complete graph K4 - no bridges (highly connected)
    conns3 = [[0,1],[0,2],[0,3],[1,2],[1,3],[2,3]]
    print(f"\nComplete graph K4 (no bridges):")
    print(f"  Brute:   {brute_force(4, conns3)}")
    print(f"  Optimal: {optimal(4, conns3)}")
    print(f"  Best:    {best(4, conns3)}")
    # Expected: []
