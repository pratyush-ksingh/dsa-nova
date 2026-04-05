"""
Problem: Bellman-Ford Algorithm
Difficulty: MEDIUM | XP: 25

Given a directed weighted graph with V vertices and E edges (possibly with
negative weights), find the shortest distance from a source vertex to all
other vertices.

If a negative-weight cycle is reachable from the source, report it (distances
become -infinity for affected vertices).

Input format: edges = [(u, v, weight), ...], V = num vertices, src = source
"""
from typing import List, Tuple, Dict
from collections import defaultdict, deque


INF = float('inf')


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS/DFS all simple paths
# Time: O(V! * E) in the worst case  |  Space: O(V)
# ============================================================
def bellman_ford_brute(V: int, edges: List[Tuple[int, int, int]], src: int) -> List[float]:
    """
    Intuition: Enumerate all possible simple paths from the source using DFS,
    tracking the running cost. For each destination, keep the minimum path cost
    found. This is correct but exponentially slow — meant to demonstrate why
    a smarter algorithm is needed.

    Note: negative cycle detection is not handled here (infinite loops possible).
    Only works correctly on DAGs or graphs without negative cycles.
    """
    dist = [INF] * V
    dist[src] = 0

    # Build adjacency list
    adj: Dict[int, List[Tuple[int, int]]] = defaultdict(list)
    for u, v, w in edges:
        adj[u].append((v, w))

    def dfs(u: int, cost: float, visited: set) -> None:
        if cost >= dist[u]:
            return  # prune: not improving
        dist[u] = cost
        for v, w in adj[u]:
            if v not in visited:
                dfs(v, cost + w, visited | {v})

    dfs(src, 0, {src})
    return dist


# ============================================================
# APPROACH 2: OPTIMAL — Classic Bellman-Ford: V-1 relaxations
# Time: O(V * E)  |  Space: O(V)
# ============================================================
def bellman_ford_optimal(V: int, edges: List[Tuple[int, int, int]], src: int) -> List[float]:
    """
    Intuition: Any shortest path in a graph with no negative cycles uses at
    most V-1 edges (a path with V or more edges would repeat a vertex, forming
    a cycle). So we relax ALL edges V-1 times. After each pass, we are
    guaranteed to have found all shortest paths using at most k edges for k
    passes done.

    After V-1 relaxations, run one more pass. If any distance still improves,
    a negative cycle exists (mark those vertices as -infinity).

    Bellman-Ford works where Dijkstra fails: negative edge weights.
    """
    dist = [INF] * V
    dist[src] = 0

    # V-1 relaxation passes
    for _ in range(V - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != INF and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:
            break  # early termination: no change, already converged

    # Detect negative cycles: one more pass
    for u, v, w in edges:
        if dist[u] != INF and dist[u] + w < dist[v]:
            dist[v] = -INF  # vertex affected by negative cycle

    return dist


# ============================================================
# APPROACH 3: BEST — Bellman-Ford with early termination + cycle marking
# Time: O(V * E)  |  Space: O(V)
# ============================================================
def bellman_ford_best(V: int, edges: List[Tuple[int, int, int]], src: int) -> List[float]:
    """
    Intuition: Same as Approach 2 but with two optimizations:
      1. Early termination: if a full pass produces no updates, we've converged
         before V-1 iterations — stop immediately.
      2. Full negative-cycle propagation: instead of just flagging one step
         of the negative cycle, we BFS/propagate the -INF marking to ALL
         vertices reachable FROM the negative cycle.

    This gives a more complete picture: every vertex whose shortest path goes
    through a negative cycle gets dist = -INF.
    """
    dist = [INF] * V
    dist[src] = 0

    # V-1 passes with early stop
    for i in range(V - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != INF and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:
            break

    # Identify all nodes directly on or reachable via negative cycles
    in_neg_cycle = set()
    for u, v, w in edges:
        if dist[u] != INF and dist[u] + w < dist[v]:
            in_neg_cycle.add(v)

    # Propagate -INF to all vertices reachable from negative cycle nodes
    if in_neg_cycle:
        adj: Dict[int, List[int]] = defaultdict(list)
        for u, v, w in edges:
            adj[u].append(v)

        queue = deque(in_neg_cycle)
        while queue:
            node = queue.popleft()
            for neighbor in adj[node]:
                if neighbor not in in_neg_cycle:
                    in_neg_cycle.add(neighbor)
                    queue.append(neighbor)

        for node in in_neg_cycle:
            dist[node] = -INF

    return dist


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Bellman-Ford Algorithm ===\n")

    # Graph: 5 vertices, edges with weights (some negative)
    # 0 -> 1 (4), 0 -> 2 (5), 1 -> 3 (-3), 2 -> 1 (-4), 3 -> 4 (2)
    V1 = 5
    E1 = [(0, 1, 4), (0, 2, 5), (1, 3, -3), (2, 1, -4), (3, 4, 2)]
    src1 = 0
    # Expected from 0: [0, 1, 5, -2, 0]

    for name, fn in [("Brute (DFS)", bellman_ford_brute),
                     ("Optimal (V-1 relax)", bellman_ford_optimal),
                     ("Best (early stop)", bellman_ford_best)]:
        result = fn(V1, E1, src1)
        print(f"{name:25s}: {result}")

    # Graph with negative cycle: 0->1->2->1 (cycle weight = -1)
    print("\n--- Negative Cycle Detection ---")
    V2 = 3
    E2 = [(0, 1, 1), (1, 2, -2), (2, 1, 1)]  # cycle 1->2->1 has weight -1
    src2 = 0
    # Expected: dist[1] and dist[2] should be -inf

    for name, fn in [("Optimal (V-1 relax)", bellman_ford_optimal),
                     ("Best (propagate -inf)", bellman_ford_best)]:
        result = fn(V2, E2, src2)
        print(f"{name:25s}: {result}")
