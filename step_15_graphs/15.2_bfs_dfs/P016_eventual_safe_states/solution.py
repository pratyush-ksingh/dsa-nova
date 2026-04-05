"""
Problem: Eventual Safe States
Difficulty: MEDIUM | XP: 25
LeetCode 802

A node in a directed graph is a "safe node" if every path starting from
that node eventually leads to a terminal node (a node with no outgoing edges).
Return all safe nodes in ascending order.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — DFS from each node independently
# Time: O(V * (V + E))  |  Space: O(V)
# For each node, check if ALL paths from it lead to a terminal.
# A node is unsafe if ANY path from it enters a cycle.
# ============================================================
def brute_force(graph: List[List[int]]) -> List[int]:
    """
    For each node independently, run a DFS to check if it's safe.
    A node is safe if it has no outgoing edges (terminal) OR if all
    neighbors are safe. A node is unsafe if it's part of a cycle.
    Uses per-node visited set to detect cycles in each independent DFS.
    """
    n = len(graph)
    # memo: -1=unknown, 0=unsafe, 1=safe
    memo = [-1] * n

    def is_safe(node: int, path: set) -> bool:
        if node in path:
            return False   # cycle detected
        if memo[node] != -1:
            return memo[node] == 1

        path.add(node)
        for neighbor in graph[node]:
            if not is_safe(neighbor, path):
                memo[node] = 0
                path.remove(node)
                return False
        path.remove(node)
        memo[node] = 1
        return True

    result = []
    for v in range(n):
        if is_safe(v, set()):
            result.append(v)
    return result


# ============================================================
# APPROACH 2: OPTIMAL — DFS 3-coloring (detect cycle nodes)
# Time: O(V + E)  |  Space: O(V)
# Nodes in or leading to cycles are unsafe. Nodes from which all
# paths eventually reach a terminal (no cycle) are safe.
# Use 3-color DFS: 0=unvisited, 1=in-stack (GRAY), 2=safe (BLACK).
# ============================================================
def optimal(graph: List[List[int]]) -> List[int]:
    """
    3-color DFS:
    - 0 (WHITE): not yet visited.
    - 1 (GRAY):  currently on the DFS stack (could be in a cycle).
    - 2 (BLACK): fully explored and confirmed SAFE.

    If during DFS we reach a GRAY node -> cycle -> current node is UNSAFE.
    If all neighbors resolve to BLACK -> current node is SAFE (BLACK).
    """
    n = len(graph)
    color = [0] * n   # 0=white, 1=gray, 2=black(safe)

    def dfs(node: int) -> bool:
        """Returns True if node is safe."""
        if color[node] == 1:
            return False   # back edge -> cycle -> unsafe
        if color[node] == 2:
            return True    # already confirmed safe

        color[node] = 1    # mark GRAY (in stack)
        for neighbor in graph[node]:
            if not dfs(neighbor):
                # neighbor is unsafe -> current node is also unsafe
                return False
        color[node] = 2    # mark BLACK (safe)
        return True

    result = []
    for v in range(n):
        if dfs(v):
            result.append(v)
    return result


# ============================================================
# APPROACH 3: BEST — Reverse Graph + Kahn's (Topological Sort)
# Time: O(V + E)  |  Space: O(V + E)
# Key insight: safe nodes are those that are NOT part of or leading to a cycle.
# Reverse all edges. Terminal nodes (no outgoing in original = no incoming in reverse)
# have in-degree 0 in the reverse graph. Run Kahn's on the reverse graph.
# All nodes processed by Kahn's are safe in the original graph.
# ============================================================
def best(graph: List[List[int]]) -> List[int]:
    """
    Reverse graph + Kahn's Algorithm:
    1. Build reverse graph (all edges flipped).
    2. In reverse graph, compute out-degree of each node in ORIGINAL
       (= in-degree in reverse). Terminal nodes have out-degree 0.
    3. Start BFS from all terminal nodes (out-degree 0 in original).
    4. Process like Kahn's: when a node's out-degree drops to 0 in original,
       it becomes "safe" (all its paths lead to terminals).
    5. Collect all visited nodes (they are all safe), sort and return.
    """
    n = len(graph)
    reverse_graph = [[] for _ in range(n)]
    out_degree = [0] * n  # out-degree in original graph

    for u in range(n):
        out_degree[u] = len(graph[u])
        for v in graph[u]:
            reverse_graph[v].append(u)  # reverse edge: v -> u

    # Start from terminal nodes (out-degree = 0 in original)
    queue = deque()
    for v in range(n):
        if out_degree[v] == 0:
            queue.append(v)

    safe = [False] * n
    while queue:
        node = queue.popleft()
        safe[node] = True
        # In reverse graph: for each predecessor u of node (original: u -> node)
        for predecessor in reverse_graph[node]:
            out_degree[predecessor] -= 1  # one more path from predecessor is safe
            if out_degree[predecessor] == 0:
                queue.append(predecessor)

    return [v for v in range(n) if safe[v]]


if __name__ == "__main__":
    print("=== Eventual Safe States ===")

    # graph = [[1,2],[2,3],[5],[0],[5],[],[]]
    # Nodes 2,4,5,6 are safe; 0,1,3 are in or leading to cycles
    g1 = [[1, 2], [2, 3], [5], [0], [5], [], []]
    print(f"Example 1: Brute={brute_force(g1)}")
    print(f"Example 1: Optimal={optimal(g1)}")
    print(f"Example 1: Best={best(g1)}")
    # Expected: [2, 4, 5, 6]

    # All terminals (no edges)
    g2 = [[], [], []]
    print(f"\nAll terminals: Brute={brute_force(g2)}")
    print(f"All terminals: Optimal={optimal(g2)}")
    print(f"All terminals: Best={best(g2)}")
    # Expected: [0, 1, 2]

    # All in cycle
    g3 = [[1], [2], [0]]
    print(f"\nAll in cycle: Brute={brute_force(g3)}")
    print(f"All in cycle: Optimal={optimal(g3)}")
    print(f"All in cycle: Best={best(g3)}")
    # Expected: []
