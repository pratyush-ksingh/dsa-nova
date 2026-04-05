"""
Problem: Topological Sort (DFS)
Difficulty: MEDIUM | XP: 25

Implement topological sort using DFS. Detect cycles.
Return valid ordering for a DAG.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: DFS WITH POST-ORDER STACK
# Time: O(V + E)  |  Space: O(V)
#
# 3-state coloring: 0=unvisited, 1=in-progress, 2=done
# Push to stack AFTER all descendants processed.
# ============================================================
def topological_sort_dfs(V: int, adj: List[List[int]]) -> List[int]:
    """
    Returns topological order, or empty list if cycle detected.
    """
    state = [0] * V  # 0=unvisited, 1=in-progress, 2=done
    stack = []
    has_cycle = [False]

    def dfs(node: int):
        if has_cycle[0]:
            return
        state[node] = 1  # in-progress

        for neighbor in adj[node]:
            if state[neighbor] == 1:
                has_cycle[0] = True
                return
            if state[neighbor] == 0:
                dfs(neighbor)
                if has_cycle[0]:
                    return

        state[node] = 2  # done
        stack.append(node)  # post-order

    for i in range(V):
        if state[i] == 0:
            dfs(i)
            if has_cycle[0]:
                return []

    return stack[::-1]  # reverse post-order = topological order


# ============================================================
# APPROACH 2: KAHN'S ALGORITHM (BFS / IN-DEGREE)
# Time: O(V + E)  |  Space: O(V)
#
# Repeatedly remove nodes with in-degree 0.
# If result size < V, cycle exists.
# ============================================================
def topological_sort_kahn(V: int, adj: List[List[int]]) -> List[int]:
    """
    BFS-based topological sort using in-degree tracking.
    """
    in_degree = [0] * V

    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    queue = deque()
    for i in range(V):
        if in_degree[i] == 0:
            queue.append(i)

    result = []
    while queue:
        node = queue.popleft()
        result.append(node)

        for neighbor in adj[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    if len(result) < V:
        return []  # cycle detected

    return result


# ============================================================
# HELPER: Build directed adjacency list
# ============================================================
def build_graph(V: int, edges: List[List[int]]) -> List[List[int]]:
    adj = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
    return adj


# ============================================================
# VERIFICATION: Check if ordering is valid
# ============================================================
def is_valid_topo_order(V: int, adj: List[List[int]], order: List[int]) -> bool:
    if len(order) != V:
        return False
    pos = {node: i for i, node in enumerate(order)}
    for u in range(V):
        for v in adj[u]:
            if pos[u] > pos[v]:  # u should come before v
                return False
    return True


if __name__ == "__main__":
    print("=== Topological Sort (DFS) ===\n")

    # Test 1: DAG
    #   5 --> 0 <-- 4
    #   |           |
    #   v           v
    #   2 --> 3 --> 1
    adj1 = build_graph(6, [[5,0], [5,2], [4,0], [4,1], [2,3], [3,1]])
    res1_dfs = topological_sort_dfs(6, adj1)
    adj1 = build_graph(6, [[5,0], [5,2], [4,0], [4,1], [2,3], [3,1]])
    res1_kahn = topological_sort_kahn(6, adj1)
    print(f"Test 1 (DAG):")
    print(f"  DFS:   {res1_dfs}  valid={is_valid_topo_order(6, adj1, res1_dfs)}")
    print(f"  Kahn:  {res1_kahn}  valid={is_valid_topo_order(6, adj1, res1_kahn)}")

    # Test 2: Linear chain
    adj2 = build_graph(4, [[0,1], [1,2], [2,3]])
    print(f"\nTest 2 (chain): {topological_sort_dfs(4, adj2)}")

    # Test 3: Cycle
    adj3 = build_graph(3, [[0,1], [1,2], [2,0]])
    res3 = topological_sort_dfs(3, adj3)
    print(f"\nTest 3 (cycle): {'CYCLE DETECTED' if not res3 else res3}")
    adj3 = build_graph(3, [[0,1], [1,2], [2,0]])
    res3k = topological_sort_kahn(3, adj3)
    print(f"  Kahn:  {'CYCLE DETECTED' if not res3k else res3k}")

    # Test 4: Single node
    adj4 = build_graph(1, [])
    print(f"\nTest 4 (single): {topological_sort_dfs(1, adj4)}")

    # Test 5: Disconnected
    adj5 = build_graph(4, [[0,1], [2,3]])
    print(f"\nTest 5 (disconnected): {topological_sort_dfs(4, adj5)}")
