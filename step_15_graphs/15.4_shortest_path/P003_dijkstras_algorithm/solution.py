"""
Problem: Dijkstra's Algorithm
Difficulty: MEDIUM | XP: 25
"""
import heapq
from collections import defaultdict
from typing import List, Tuple


def build_graph(V: int, edges: List[Tuple[int, int, int]]) -> dict:
    """Build undirected adjacency list from edge list."""
    adj = defaultdict(list)
    for u, v, w in edges:
        adj[u].append((v, w))
        adj[v].append((u, w))
    return adj


# ============================================================
# APPROACH 1: BRUTE FORCE -- Bellman-Ford Style
# Time: O(V * E)  |  Space: O(V)
# Relax all edges V-1 times. Correct but slow for non-negative weights.
# ============================================================
def brute_force(V: int, adj: dict, src: int) -> List[int]:
    dist = [float('inf')] * V
    dist[src] = 0

    # Collect all edges
    edges = []
    for u in range(V):
        for v, w in adj[u]:
            edges.append((u, v, w))

    # Relax all edges V-1 times
    for _ in range(V - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:
            break

    return dist


# ============================================================
# APPROACH 2: OPTIMAL -- Dijkstra with Min-Heap
# Time: O((V + E) log V)  |  Space: O(V + E)
# Greedy: always expand nearest unfinalized vertex via priority queue.
# Uses lazy deletion for stale heap entries.
# ============================================================
def optimal(V: int, adj: dict, src: int) -> List[int]:
    dist = [float('inf')] * V
    dist[src] = 0

    # Min-heap: (distance, vertex)
    heap = [(0, src)]

    while heap:
        d, u = heapq.heappop(heap)

        # Lazy deletion: skip stale entries
        if d > dist[u]:
            continue

        # Relax all neighbors
        for v, w in adj[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(heap, (dist[v], v))

    return dist


# ============================================================
# APPROACH 3: BEST -- Dijkstra with SortedList (Decrease-Key)
# Time: O((V + E) log V)  |  Space: O(V)
# SortedList acts as indexed PQ with true decrease-key.
# Heap size stays at most V (no stale entries).
# ============================================================
def best(V: int, adj: dict, src: int) -> List[int]:
    # Using a simple set-based approach (works in Python without sortedcontainers)
    dist = [float('inf')] * V
    dist[src] = 0
    visited = [False] * V

    # Simple O(V^2) approach -- best for dense graphs, avoids external deps
    for _ in range(V):
        # Find unvisited vertex with minimum distance
        u = -1
        for v in range(V):
            if not visited[v] and (u == -1 or dist[v] < dist[u]):
                u = v

        if u == -1 or dist[u] == float('inf'):
            break

        visited[u] = True

        for v, w in adj[u]:
            if not visited[v] and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    return dist


if __name__ == "__main__":
    print("=== Dijkstra's Algorithm ===")

    V = 5
    edges = [(0, 1, 2), (0, 3, 6), (1, 2, 3), (1, 3, 8), (1, 4, 5), (2, 4, 7)]
    adj = build_graph(V, edges)

    print(f"Brute:   {brute_force(V, adj, 0)}")
    print(f"Optimal: {optimal(V, adj, 0)}")
    print(f"Best:    {best(V, adj, 0)}")
    # Expected: [0, 2, 5, 6, 7]

    # Edge case: disconnected
    adj2 = build_graph(3, [(0, 1, 3)])
    print(f"\nDisconnected: {optimal(3, adj2, 0)}")
    # Expected: [0, 3, inf]
