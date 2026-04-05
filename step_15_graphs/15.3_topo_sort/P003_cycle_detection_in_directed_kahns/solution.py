"""
Problem: Cycle Detection in Directed Graph (Kahn's Algorithm)
Difficulty: MEDIUM | XP: 25

Detect cycle in a directed graph using Kahn's algorithm.
If topological sort doesn't include all nodes, a cycle exists.
"""
from typing import List, Set
from collections import deque


# ============================================================
# APPROACH 1: DFS 3-COLOR MARKING
# Time: O(V + E)  |  Space: O(V)
# WHITE=0, GRAY=1, BLACK=2
# ============================================================
def has_cycle_dfs(V: int, adj: List[List[int]]) -> bool:
    """
    3-color DFS: WHITE (unvisited), GRAY (in current path), BLACK (done).
    Finding a GRAY neighbor = back edge = cycle.
    """
    WHITE, GRAY, BLACK = 0, 1, 2
    color = [WHITE] * V

    def dfs(u: int) -> bool:
        color[u] = GRAY
        for v in adj[u]:
            if color[v] == GRAY:
                return True  # Back edge -> cycle
            if color[v] == WHITE:
                if dfs(v):
                    return True
        color[u] = BLACK
        return False

    for i in range(V):
        if color[i] == WHITE:
            if dfs(i):
                return True
    return False


# ============================================================
# APPROACH 2: KAHN'S ALGORITHM (OPTIMAL)
# Time: O(V + E)  |  Space: O(V + E)
# If topo sort processes fewer than V nodes -> cycle
# ============================================================
def has_cycle_kahns(V: int, adj: List[List[int]]) -> bool:
    """
    Kahn's Algorithm for cycle detection:
    1. Compute in-degrees
    2. BFS from in-degree-0 nodes
    3. If processed count < V, cycle exists
    """
    # Step 1: Compute in-degrees
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    # Step 2: Enqueue all in-degree-0 nodes
    queue = deque()
    for i in range(V):
        if in_degree[i] == 0:
            queue.append(i)

    # Step 3: BFS processing
    count = 0
    while queue:
        u = queue.popleft()
        count += 1
        for v in adj[u]:
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)

    # Step 4: Cycle detection
    return count < V  # True if cycle exists


# ============================================================
# APPROACH 3: KAHN'S WITH CYCLE NODE IDENTIFICATION (BEST)
# Time: O(V + E)  |  Space: O(V + E)
# Returns the set of nodes stuck in/downstream of cycles
# ============================================================
def find_cyclic_nodes(V: int, adj: List[List[int]]) -> List[int]:
    """
    Run Kahn's and collect processed nodes.
    Any node NOT processed is trapped in or downstream of a cycle.
    """
    in_degree = [0] * V
    for u in range(V):
        for v in adj[u]:
            in_degree[v] += 1

    queue = deque()
    for i in range(V):
        if in_degree[i] == 0:
            queue.append(i)

    processed: Set[int] = set()
    while queue:
        u = queue.popleft()
        processed.add(u)
        for v in adj[u]:
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)

    # Nodes NOT processed are stuck
    return [i for i in range(V) if i not in processed]


def build_adj(V: int, edges: List[List[int]]) -> List[List[int]]:
    """Helper: build adjacency list from edge pairs."""
    adj: List[List[int]] = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
    return adj


if __name__ == "__main__":
    print("=== Cycle Detection in Directed Graph (Kahn's) ===\n")

    # Test 1: No cycle (linear chain)
    adj1 = build_adj(4, [[0,1],[1,2],[2,3]])
    print("Test 1 (linear chain V=4): Expected False")
    print(f"  DFS:   {has_cycle_dfs(4, adj1)}")
    print(f"  Kahns: {has_cycle_kahns(4, build_adj(4, [[0,1],[1,2],[2,3]]))}")

    # Test 2: Simple cycle
    adj2 = build_adj(3, [[0,1],[1,2],[2,0]])
    print(f"\nTest 2 (3-node cycle): Expected True")
    print(f"  DFS:   {has_cycle_dfs(3, adj2)}")
    print(f"  Kahns: {has_cycle_kahns(3, build_adj(3, [[0,1],[1,2],[2,0]]))}")
    print(f"  Cyclic nodes: {find_cyclic_nodes(3, build_adj(3, [[0,1],[1,2],[2,0]]))}")

    # Test 3: Partial cycle with acyclic component
    adj3 = build_adj(5, [[0,1],[1,2],[2,0],[3,4]])
    print(f"\nTest 3 (partial cycle V=5): Expected True")
    print(f"  Kahns: {has_cycle_kahns(5, adj3)}")
    print(f"  Cyclic nodes: {find_cyclic_nodes(5, build_adj(5, [[0,1],[1,2],[2,0],[3,4]]))}")

    # Test 4: Self-loop
    adj4 = build_adj(1, [[0,0]])
    print(f"\nTest 4 (self-loop): Expected True")
    print(f"  Kahns: {has_cycle_kahns(1, adj4)}")

    # Test 5: No edges
    adj5 = build_adj(3, [])
    print(f"\nTest 5 (no edges V=3): Expected False")
    print(f"  Kahns: {has_cycle_kahns(3, adj5)}")

    # Test 6: Diamond DAG (no cycle)
    adj6 = build_adj(4, [[0,1],[0,2],[1,3],[2,3]])
    print(f"\nTest 6 (diamond DAG V=4): Expected False")
    print(f"  Kahns: {has_cycle_kahns(4, adj6)}")

    # Test 7: Two-node cycle
    adj7 = build_adj(2, [[0,1],[1,0]])
    print(f"\nTest 7 (two-node cycle): Expected True")
    print(f"  Kahns: {has_cycle_kahns(2, adj7)}")
    print(f"  Cyclic nodes: {find_cyclic_nodes(2, build_adj(2, [[0,1],[1,0]]))}")

    # Test 8: Cycle with downstream nodes
    adj8 = build_adj(5, [[0,1],[1,2],[2,0],[2,3],[3,4]])
    print(f"\nTest 8 (cycle + downstream V=5): Expected True")
    print(f"  Kahns: {has_cycle_kahns(5, adj8)}")
    cyclic = find_cyclic_nodes(5, build_adj(5, [[0,1],[1,2],[2,0],[2,3],[3,4]]))
    print(f"  Cyclic nodes: {cyclic}")
    print(f"  (Nodes 0,1,2 in cycle; 3,4 downstream)")
