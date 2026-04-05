"""
Problem: Find Eventual Safe States (LeetCode #802)
Difficulty: MEDIUM | XP: 25

Given a directed graph of n nodes (0 to n-1) where graph[i] is a list of
nodes that node i has edges to, find all "safe" nodes. A node is safe if
every path starting from that node leads to a terminal node (node with no
outgoing edges). Return the safe nodes in sorted order.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE (DFS from each node)
# Time: O(V * (V + E))  |  Space: O(V)
# ============================================================
def eventualSafeNodes_brute(graph: List[List[int]]) -> List[int]:
    """
    For each node, run DFS to check if ALL paths from it lead to
    terminal nodes. A node is unsafe if any path from it reaches
    a cycle. Use memo to avoid recomputation.
    """
    n = len(graph)
    # 0 = unvisited, 1 = in-progress, 2 = safe, 3 = unsafe
    state = [0] * n

    def is_safe(node: int) -> bool:
        if state[node] == 2:
            return True
        if state[node] == 1 or state[node] == 3:
            return False

        state[node] = 1  # in progress
        for neighbor in graph[node]:
            if not is_safe(neighbor):
                state[node] = 3  # unsafe
                return False
        state[node] = 2  # safe
        return True

    result = []
    for i in range(n):
        if is_safe(i):
            result.append(i)
    return result


# ============================================================
# APPROACH 2: OPTIMAL (DFS with 3-state coloring)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def eventualSafeNodes_dfs(graph: List[List[int]]) -> List[int]:
    """
    DFS with coloring: white(0)/gray(1)/black(2).
    - Gray nodes are on the current path (potential cycle).
    - If DFS from a node reaches a gray node, it's in a cycle -> unsafe.
    - If DFS completes without hitting gray, node is safe (mark black).
    Each node is visited at most once due to memoization via colors.
    """
    n = len(graph)
    color = [0] * n  # 0=white, 1=gray, 2=black

    def dfs(node: int) -> bool:
        """Returns True if node is safe (not in any cycle)."""
        if color[node] != 0:
            return color[node] == 2  # black = safe
        color[node] = 1  # gray = in progress
        for neighbor in graph[node]:
            if color[neighbor] == 1:
                return False  # cycle
            if color[neighbor] == 0 and not dfs(neighbor):
                return False
        color[node] = 2  # black = safe
        return True

    return [i for i in range(n) if dfs(i)]


# ============================================================
# APPROACH 3: BEST (Reverse graph + Kahn's algorithm)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def eventualSafeNodes_topo(graph: List[List[int]]) -> List[int]:
    """
    Key insight: safe nodes are those NOT part of or leading to a cycle.
    Terminal nodes (outdegree 0) are trivially safe.

    Reverse the graph edges. Now terminal nodes have indegree 0 in the
    reversed graph. Run Kahn's algorithm on the reversed graph:
    - Start from terminal nodes (outdegree 0 in original = indegree 0 in reversed)
    - Process nodes layer by layer
    - All processed nodes are safe

    Why it works: in the reversed graph, Kahn's processes nodes that only
    point to already-safe nodes. Cycle nodes never reach indegree 0.
    """
    n = len(graph)
    # Build reverse graph and compute outdegree (= indegree in reverse)
    reverse_adj = [[] for _ in range(n)]
    outdegree = [0] * n

    for u in range(n):
        for v in graph[u]:
            reverse_adj[v].append(u)
        outdegree[u] = len(graph[u])

    # Start with terminal nodes (outdegree 0)
    queue = deque()
    for i in range(n):
        if outdegree[i] == 0:
            queue.append(i)

    safe = [False] * n
    while queue:
        node = queue.popleft()
        safe[node] = True
        for neighbor in reverse_adj[node]:
            outdegree[neighbor] -= 1
            if outdegree[neighbor] == 0:
                queue.append(neighbor)

    return [i for i in range(n) if safe[i]]


if __name__ == "__main__":
    print("=== Find Eventual Safe States ===\n")

    # Test 1: LeetCode example
    g1 = [[1, 2], [2, 3], [5], [0], [5], [], []]
    print(f"Test 1: graph={g1}")
    print(f"  Expected: [2, 4, 5, 6]")
    print(f"  Brute: {eventualSafeNodes_brute(g1)}")
    print(f"  DFS:   {eventualSafeNodes_dfs(g1)}")
    print(f"  Topo:  {eventualSafeNodes_topo(g1)}")

    # Test 2: All terminal nodes
    g2 = [[], [], []]
    print(f"\nTest 2: graph={g2}")
    print(f"  Expected: [0, 1, 2]")
    print(f"  Brute: {eventualSafeNodes_brute(g2)}")
    print(f"  DFS:   {eventualSafeNodes_dfs(g2)}")
    print(f"  Topo:  {eventualSafeNodes_topo(g2)}")

    # Test 3: Single cycle
    g3 = [[1], [2], [0]]
    print(f"\nTest 3 (all in cycle): graph={g3}")
    print(f"  Expected: []")
    print(f"  Brute: {eventualSafeNodes_brute(g3)}")
    print(f"  DFS:   {eventualSafeNodes_dfs(g3)}")
    print(f"  Topo:  {eventualSafeNodes_topo(g3)}")

    # Test 4: LeetCode example 2
    g4 = [[1, 2, 3, 4], [1, 2], [3, 4], [0, 4], []]
    print(f"\nTest 4: graph={g4}")
    print(f"  Expected: [4]")
    print(f"  Brute: {eventualSafeNodes_brute(g4)}")
    print(f"  DFS:   {eventualSafeNodes_dfs(g4)}")
    print(f"  Topo:  {eventualSafeNodes_topo(g4)}")
