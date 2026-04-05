"""Problem: Kosaraju Algorithm for Strongly Connected Components
Difficulty: HARD | XP: 50

Find all Strongly Connected Components (SCCs) in a directed graph.
SCC: maximal set of vertices such that there is a path between every pair.
"""
from collections import defaultdict
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive Kosaraju's
# Time: O(V + E)  |  Space: O(V + E)
# Pass 1: DFS on original graph, push nodes in finish order
# Pass 2: DFS on reversed graph in reverse finish order
# ============================================================
def brute_force(v: int, edges: List[List[int]]) -> List[List[int]]:
    adj = defaultdict(list)
    radj = defaultdict(list)
    for u, w in edges:
        adj[u].append(w)
        radj[w].append(u)

    visited = [False] * v
    order = []

    def dfs1(node):
        visited[node] = True
        for nb in adj[node]:
            if not visited[nb]:
                dfs1(nb)
        order.append(node)

    for i in range(v):
        if not visited[i]:
            dfs1(i)

    visited = [False] * v
    sccs = []

    def dfs2(node, scc):
        visited[node] = True
        scc.append(node)
        for nb in radj[node]:
            if not visited[nb]:
                dfs2(nb, scc)

    for node in reversed(order):
        if not visited[node]:
            scc = []
            dfs2(node, scc)
            sccs.append(scc)
    return sccs


# ============================================================
# APPROACH 2: OPTIMAL - Iterative Kosaraju's (avoids recursion limit)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def optimal(v: int, edges: List[List[int]]) -> List[List[int]]:
    adj = defaultdict(list)
    radj = defaultdict(list)
    for u, w in edges:
        adj[u].append(w)
        radj[w].append(u)

    visited = [False] * v
    order = []

    # Iterative DFS pass 1
    for i in range(v):
        if not visited[i]:
            stack = [(i, 0)]
            visited[i] = True
            while stack:
                node, idx = stack[-1]
                nbrs = adj[node]
                if idx < len(nbrs):
                    stack[-1] = (node, idx + 1)
                    nb = nbrs[idx]
                    if not visited[nb]:
                        visited[nb] = True
                        stack.append((nb, 0))
                else:
                    order.append(node)
                    stack.pop()

    # Iterative DFS pass 2 on reverse graph
    visited = [False] * v
    sccs = []
    for node in reversed(order):
        if not visited[node]:
            scc = []
            stack = [node]
            visited[node] = True
            while stack:
                cur = stack.pop()
                scc.append(cur)
                for nb in radj[cur]:
                    if not visited[nb]:
                        visited[nb] = True
                        stack.append(nb)
            sccs.append(scc)
    return sccs


# ============================================================
# APPROACH 3: BEST - Returns count of SCCs with component details
# Time: O(V + E)  |  Space: O(V + E)
# Same as Optimal but returns (count, sccs) for easier use
# ============================================================
def best(v: int, edges: List[List[int]]) -> int:
    return len(optimal(v, edges))


if __name__ == "__main__":
    # Graph: 1->0, 0->2, 2->1, 0->3, 3->4
    # SCCs: {0,1,2}, {3}, {4} -> 3 SCCs
    edges1 = [[1,0],[0,2],[2,1],[0,3],[3,4]]
    sccs1_bf = brute_force(5, edges1)
    sccs1_opt = optimal(5, edges1)
    print(f"Test 1 (5 vertices): BF={len(sccs1_bf)} SCCs {sccs1_bf}")
    print(f"           Optimal:  {len(sccs1_opt)} SCCs {sccs1_opt}")
    print(f"           Best:     {best(5, edges1)} SCCs")

    # No edges -> each node its own SCC
    sccs2 = optimal(4, [])
    print(f"Test 2 (4 isolated): {len(sccs2)} SCCs {sccs2} (expected 4)")

    # Fully connected (all in one SCC)
    edges3 = [[0,1],[1,2],[2,3],[3,0]]
    sccs3 = optimal(4, edges3)
    print(f"Test 3 (4-cycle): {len(sccs3)} SCCs {sccs3} (expected 1)")

    # Two disjoint cycles
    edges4 = [[0,1],[1,0],[2,3],[3,2]]
    sccs4 = optimal(4, edges4)
    print(f"Test 4 (two cycles): {len(sccs4)} SCCs {sccs4} (expected 2)")
