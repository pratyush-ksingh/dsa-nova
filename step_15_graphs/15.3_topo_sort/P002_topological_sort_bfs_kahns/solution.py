"""
Problem: Topological Sort BFS (Kahn's Algorithm)
Difficulty: MEDIUM | XP: 25

Given a DAG with V vertices and E edges, find a topological ordering
using BFS with in-degree tracking (Kahn's Algorithm).
Also detect cycles (if topo sort doesn't include all nodes -> cycle).
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: NAIVE SIMULATION (BRUTE FORCE)
# Time: O(V^2 + E)  |  Space: O(V + E)
# Repeatedly scan all nodes to find in-degree 0 node
# ============================================================
def topo_sort_brute(V: int, adj: List[List[int]]) -> List[int]:
    """
    Each iteration scans all V nodes to find one with in-degree 0.
    Simple but wasteful -- O(V) work per node extracted.
    """
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    processed = [False] * V
    result = []

    for _ in range(V):
        # Find any unprocessed node with in-degree 0
        picked = -1
        for u in range(V):
            if not processed[u] and in_degree[u] == 0:
                picked = u
                break

        if picked == -1:
            return []  # Cycle detected

        result.append(picked)
        processed[picked] = True

        for v in adj[picked]:
            in_degree[v] -= 1

    return result


# ============================================================
# APPROACH 2: KAHN'S ALGORITHM (OPTIMAL)
# Time: O(V + E)  |  Space: O(V + E)
# BFS with queue of in-degree-0 nodes
# ============================================================
def topo_sort_kahns(V: int, adj: List[List[int]]) -> List[int]:
    """
    Kahn's Algorithm:
    1. Compute in-degrees
    2. Enqueue all in-degree-0 nodes
    3. BFS: dequeue, add to result, decrement neighbors' in-degrees
    4. If result has fewer than V nodes, cycle exists
    """
    # Step 1: Compute in-degrees
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    # Step 2: Enqueue all nodes with in-degree 0
    queue = deque()
    for i in range(V):
        if in_degree[i] == 0:
            queue.append(i)

    # Step 3: BFS processing
    result = []
    while queue:
        u = queue.popleft()
        result.append(u)

        for v in adj[u]:
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)

    # Step 4: Cycle detection
    if len(result) != V:
        return []  # Cycle exists
    return result


# ============================================================
# APPROACH 3: KAHN'S WITH ALL VALID ORDERINGS (BACKTRACKING)
# Time: O(V! * V) worst case  |  Space: O(V + E)
# Enumerate every valid topological ordering
# ============================================================
def all_topo_sorts(V: int, adj: List[List[int]]) -> List[List[int]]:
    """
    Backtracking: at each step, try every in-degree-0 node.
    After recursion, undo the choice (restore in-degrees).
    """
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    all_results = []
    visited = [False] * V
    current = []

    def backtrack():
        if len(current) == V:
            all_results.append(current[:])
            return

        for u in range(V):
            if not visited[u] and in_degree[u] == 0:
                # Choose u
                visited[u] = True
                current.append(u)
                for v in adj[u]:
                    in_degree[v] -= 1

                # Recurse
                backtrack()

                # Undo
                visited[u] = False
                current.pop()
                for v in adj[u]:
                    in_degree[v] += 1

    backtrack()
    return all_results


def build_adj(V: int, edges: List[List[int]]) -> List[List[int]]:
    """Helper: build adjacency list from edge pairs."""
    adj = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
    return adj


if __name__ == "__main__":
    print("=== Topological Sort BFS (Kahn's) ===\n")

    # Test 1: Standard DAG
    adj1 = build_adj(6, [[5,2],[5,0],[4,0],[4,1],[2,3],[3,1]])
    print("Test 1 (V=6):")
    print(f"  Brute: {topo_sort_brute(6, build_adj(6, [[5,2],[5,0],[4,0],[4,1],[2,3],[3,1]]))}")
    print(f"  Kahns: {topo_sort_kahns(6, adj1)}")

    # Test 2: Linear chain
    adj2 = build_adj(4, [[0,1],[1,2],[2,3]])
    print(f"\nTest 2 (linear chain): {topo_sort_kahns(4, adj2)}")

    # Test 3: Cycle detection
    adj3 = build_adj(3, [[0,1],[1,2],[2,0]])
    res3 = topo_sort_kahns(3, adj3)
    print(f"\nTest 3 (cycle): {'CYCLE DETECTED' if not res3 else res3}")

    # Test 4: All valid orderings
    adj4 = build_adj(4, [[0,2],[1,2],[2,3]])
    print(f"\nTest 4 (all orderings): {all_topo_sorts(4, adj4)}")

    # Test 5: Single node
    adj5 = build_adj(1, [])
    print(f"\nTest 5 (single node): {topo_sort_kahns(1, adj5)}")

    # Test 6: No edges
    adj6 = build_adj(3, [])
    print(f"\nTest 6 (no edges V=3): {topo_sort_kahns(3, adj6)}")
