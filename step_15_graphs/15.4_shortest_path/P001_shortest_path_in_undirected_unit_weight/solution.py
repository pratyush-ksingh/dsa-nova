"""
Problem: Shortest Path in Undirected Unit Weight Graph
Difficulty: MEDIUM | XP: 25

Find shortest distance from source to all vertices in
an unweighted undirected graph using BFS.
"""
from typing import List, Optional
from collections import deque


# ============================================================
# APPROACH 1: BFS (OPTIMAL)
# Time: O(V + E)  |  Space: O(V)
#
# BFS explores nodes in order of increasing distance.
# First visit = shortest path for unweighted graphs.
# ============================================================
def shortest_path(V: int, adj: List[List[int]], src: int) -> List[int]:
    """
    Returns distance array where dist[i] = shortest distance from src to i.
    dist[i] = -1 if i is unreachable from src.
    """
    dist = [-1] * V
    dist[src] = 0

    queue = deque([src])

    while queue:
        node = queue.popleft()

        for neighbor in adj[node]:
            if dist[neighbor] == -1:  # not visited
                dist[neighbor] = dist[node] + 1
                queue.append(neighbor)

    return dist


# ============================================================
# APPROACH 2: BFS WITH PATH RECONSTRUCTION
# Time: O(V + E)  |  Space: O(V)
#
# Same BFS but tracks parent[] to reconstruct actual path.
# ============================================================
def shortest_path_with_reconstruction(
    V: int, adj: List[List[int]], src: int
) -> tuple:
    """
    Returns (dist[], parent[]) for path reconstruction.
    """
    dist = [-1] * V
    parent = [-1] * V
    dist[src] = 0

    queue = deque([src])

    while queue:
        node = queue.popleft()

        for neighbor in adj[node]:
            if dist[neighbor] == -1:
                dist[neighbor] = dist[node] + 1
                parent[neighbor] = node
                queue.append(neighbor)

    return dist, parent


def reconstruct_path(parent: List[int], src: int, dest: int) -> List[int]:
    """
    Trace back from dest to src using parent pointers.
    Returns empty list if dest is unreachable.
    """
    if parent[dest] == -1 and dest != src:
        return []  # unreachable

    path = []
    node = dest
    while node != -1:
        path.append(node)
        node = parent[node]

    return path[::-1]


# ============================================================
# HELPER: Build undirected adjacency list
# ============================================================
def build_graph(V: int, edges: List[List[int]]) -> List[List[int]]:
    adj = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)
    return adj


if __name__ == "__main__":
    print("=== Shortest Path in Undirected Unit Weight Graph ===\n")

    # Test 1:
    #   0 --- 1 --- 2
    #   |           |
    #   3           4
    #         |
    #         5
    adj1 = build_graph(6, [[0,1], [1,2], [0,3], [2,4], [3,5]])
    print("Test 1 (src=0):")
    print(f"  Distances: {shortest_path(6, adj1, 0)}")
    # Expected: [0, 1, 2, 1, 3, 2]

    dist1, parent1 = shortest_path_with_reconstruction(6, adj1, 0)
    print(f"  Path 0->4: {reconstruct_path(parent1, 0, 4)}")
    # Expected: [0, 1, 2, 4]

    # Test 2: Disconnected
    adj2 = build_graph(5, [[0,1], [1,2], [3,4]])
    print(f"\nTest 2 (disconnected, src=0):")
    print(f"  Distances: {shortest_path(5, adj2, 0)}")
    # Expected: [0, 1, 2, -1, -1]

    # Test 3: Single node
    adj3 = build_graph(1, [])
    print(f"\nTest 3 (single node): {shortest_path(1, adj3, 0)}")
    # Expected: [0]

    # Test 4: Chain, source in middle
    adj4 = build_graph(5, [[0,1], [1,2], [2,3], [3,4]])
    print(f"\nTest 4 (chain, src=2): {shortest_path(5, adj4, 2)}")
    # Expected: [2, 1, 0, 1, 2]

    # Test 5: Star graph
    #     1
    #     |
    # 2 - 0 - 3
    #     |
    #     4
    adj5 = build_graph(5, [[0,1], [0,2], [0,3], [0,4]])
    print(f"\nTest 5 (star, src=0): {shortest_path(5, adj5, 0)}")
    # Expected: [0, 1, 1, 1, 1]
    print(f"  From src=1: {shortest_path(5, adj5, 1)}")
    # Expected: [1, 0, 2, 2, 2]
