"""
Problem: Articulation Points
Difficulty: HARD | XP: 50

An articulation point (or cut vertex) is a vertex in an undirected graph whose
removal increases the number of connected components. Finding all articulation
points efficiently is key to network reliability analysis.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(V * (V + E))  |  Space: O(V + E)
# Remove each vertex, check if graph remains connected via BFS/DFS.
# ============================================================
def brute_force(n: int, adj: List[List[int]]) -> List[int]:
    """
    For every vertex, remove it temporarily and check if the remaining
    graph is still connected using DFS. If not, it is an articulation point.
    """
    def is_connected(skip: int) -> bool:
        visited = [False] * n
        # Find first non-skip node to start DFS
        start = -1
        for i in range(n):
            if i != skip:
                start = i
                break
        if start == -1:
            return True  # only one node total

        stack = [start]
        visited[start] = True
        count = 1
        while stack:
            node = stack.pop()
            for nb in adj[node]:
                if nb != skip and not visited[nb]:
                    visited[nb] = True
                    count += 1
                    stack.append(nb)

        total = sum(1 for i in range(n) if i != skip)
        return count == total

    result = []
    for v in range(n):
        if not is_connected(v):
            result.append(v)
    return sorted(result)


# ============================================================
# APPROACH 2: OPTIMAL — Tarjan's Algorithm
# Time: O(V + E)  |  Space: O(V)
# Use discovery time (disc) and low values to detect APs in one DFS pass.
# ============================================================
def optimal(n: int, adj: List[List[int]]) -> List[int]:
    """
    Tarjan's algorithm:
    - disc[u]: discovery time of u
    - low[u]:  lowest disc reachable from subtree rooted at u
    A node u is an AP if:
      - u is root of DFS tree and has >= 2 children, OR
      - u is NOT root and low[child] >= disc[u] for some child
    """
    disc = [-1] * n
    low = [-1] * n
    visited = [False] * n
    is_ap = [False] * n
    timer = [0]

    def dfs(u: int, parent: int) -> None:
        visited[u] = True
        disc[u] = low[u] = timer[0]
        timer[0] += 1
        children = 0

        for v in adj[u]:
            if not visited[v]:
                children += 1
                dfs(v, u)
                low[u] = min(low[u], low[v])
                # Non-root: low[v] >= disc[u] means v cannot reach above u
                if parent != -1 and low[v] >= disc[u]:
                    is_ap[u] = True
                # Root with >= 2 children
                if parent == -1 and children > 1:
                    is_ap[u] = True
            elif v != parent:
                # Back edge: update low
                low[u] = min(low[u], disc[v])

    for i in range(n):
        if not visited[i]:
            dfs(i, -1)

    return [i for i in range(n) if is_ap[i]]


# ============================================================
# APPROACH 3: BEST — Tarjan's (Iterative to avoid stack overflow)
# Time: O(V + E)  |  Space: O(V)
# Same algorithm but iterative DFS — safe for large graphs.
# ============================================================
def best(n: int, adj: List[List[int]]) -> List[int]:
    """
    Iterative version of Tarjan's AP algorithm using an explicit stack.
    Avoids Python's recursion limit on large graphs.
    Each stack entry: (node, parent, iterator_over_neighbors, child_count)
    """
    disc = [-1] * n
    low = [0] * n
    is_ap = [False] * n
    timer = [0]

    for start in range(n):
        if disc[start] != -1:
            continue

        # Stack entries: [node, parent, neighbor_index, children_count]
        stack = [[start, -1, 0, 0]]
        disc[start] = low[start] = timer[0]
        timer[0] += 1

        while stack:
            u, parent, idx, children = stack[-1]

            if idx < len(adj[u]):
                stack[-1][2] += 1  # advance neighbor index
                v = adj[u][idx]

                if disc[v] == -1:
                    # Tree edge: push child
                    stack[-1][3] += 1  # increment children count
                    disc[v] = low[v] = timer[0]
                    timer[0] += 1
                    stack.append([v, u, 0, 0])
                elif v != parent:
                    # Back edge
                    low[u] = min(low[u], disc[v])
            else:
                # Done processing u — pop and update parent
                stack.pop()
                if stack:
                    pu, pp, _, _ = stack[-1]
                    low[pu] = min(low[pu], low[u])
                    # Check AP condition for parent
                    if pp == -1 and stack[-1][3] > 1:
                        is_ap[pu] = True
                    if pp != -1 and low[u] >= disc[pu]:
                        is_ap[pu] = True

    return [i for i in range(n) if is_ap[i]]


if __name__ == "__main__":
    print("=== Articulation Points ===")

    # Graph 1: 0-1-2-3 with 1-3 edge  => AP: 1, 2 ... classic example
    # Edges: 0-1, 1-2, 2-3
    n1 = 5
    adj1 = [[] for _ in range(n1)]
    edges1 = [(0, 1), (1, 2), (2, 3), (3, 4)]
    for u, v in edges1:
        adj1[u].append(v)
        adj1[v].append(u)

    print(f"Graph (chain 0-1-2-3-4):")
    print(f"  Brute:   {brute_force(n1, adj1)}")
    print(f"  Optimal: {optimal(n1, adj1)}")
    print(f"  Best:    {best(n1, adj1)}")

    # Graph 2: 0-1, 0-2, 1-2, 1-3, 3-4
    n2 = 5
    adj2 = [[] for _ in range(n2)]
    edges2 = [(0, 1), (0, 2), (1, 2), (1, 3), (3, 4)]
    for u, v in edges2:
        adj2[u].append(v)
        adj2[v].append(u)

    print(f"\nGraph (triangle + chain):")
    print(f"  Brute:   {brute_force(n2, adj2)}")
    print(f"  Optimal: {optimal(n2, adj2)}")
    print(f"  Best:    {best(n2, adj2)}")
