"""
Problem: Useful Extra Edges
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a weighted undirected graph (N nodes, M edges), source S, destination T,
and extra edges [(u, v, w)], find shortest path from S to T optionally using
at most ONE extra edge.

Approach: Run Dijkstra from S and from T. For each extra edge (u,v,w):
  candidate = dist_from_s[u] + w + dist_from_t[v]  (or via v->u)
Return min(original_dist, all_candidates).

Real-life use: Road network augmentation, relay link planning.
"""
import heapq
from typing import List, Tuple, Dict
from collections import defaultdict

INF = float('inf')


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(E * (V+E) log V)  |  Space: O(V + E)
# For each extra edge, temporarily add it and run Dijkstra.
# ============================================================
def brute_force(n: int, edges: List[Tuple], src: int, dst: int,
                extra_edges: List[Tuple]) -> int:
    """Add each extra edge one at a time and run Dijkstra."""
    def dijkstra(adj, source, target):
        dist = [INF] * (n + 1)
        dist[source] = 0
        pq = [(0, source)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w
                    heapq.heappush(pq, (dist[v], v))
        return dist[target]

    def build_adj(edge_list):
        adj = defaultdict(list)
        for u, v, w in edge_list:
            adj[u].append((v, w))
            adj[v].append((u, w))
        return adj

    base = dijkstra(build_adj(edges), src, dst)
    best_val = base

    for extra in extra_edges:
        new_edges = edges + [extra]
        res = dijkstra(build_adj(new_edges), src, dst)
        best_val = min(best_val, res)

    return -1 if best_val == INF else best_val


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O((V + E) log V)  |  Space: O(V + E)
# Two Dijkstra runs (from src and dst). Check each extra edge once.
# ============================================================
def optimal(n: int, edges: List[Tuple], src: int, dst: int,
            extra_edges: List[Tuple]) -> int:
    """Two-source Dijkstra + linear scan over extra edges."""
    adj = defaultdict(list)
    for u, v, w in edges:
        adj[u].append((v, w))
        adj[v].append((u, w))

    def dijkstra_full(source):
        dist = [INF] * (n + 1)
        dist[source] = 0
        pq = [(0, source)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w
                    heapq.heappush(pq, (dist[v], v))
        return dist

    dist_s = dijkstra_full(src)
    dist_t = dijkstra_full(dst)

    result = dist_s[dst]
    for u, v, w in extra_edges:
        if dist_s[u] < INF and dist_t[v] < INF:
            result = min(result, dist_s[u] + w + dist_t[v])
        if dist_s[v] < INF and dist_t[u] < INF:
            result = min(result, dist_s[v] + w + dist_t[u])

    return -1 if result == INF else result


# ============================================================
# APPROACH 3: BEST
# Time: O((V + E) log V)  |  Space: O(V + E)
# Same as optimal, clean modular implementation.
# ============================================================
def best(n: int, edges: List[Tuple], src: int, dst: int,
         extra_edges: List[Tuple]) -> int:
    """Two Dijkstra + extra edge scan. Standard interview solution."""
    adj = [[] for _ in range(n + 1)]
    for u, v, w in edges:
        adj[u].append((v, w))
        adj[v].append((u, w))

    def dijkstra(source: int) -> List[float]:
        dist = [INF] * (n + 1)
        dist[source] = 0
        pq = [(0, source)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            for v, w in adj[u]:
                nd = d + w
                if nd < dist[v]:
                    dist[v] = nd
                    heapq.heappush(pq, (nd, v))
        return dist

    ds = dijkstra(src)
    dt = dijkstra(dst)
    ans = ds[dst]

    for u, v, w in extra_edges:
        if ds[u] < INF and dt[v] < INF:
            ans = min(ans, ds[u] + w + dt[v])
        if ds[v] < INF and dt[u] < INF:
            ans = min(ans, ds[v] + w + dt[u])

    return -1 if ans == INF else ans


if __name__ == "__main__":
    print("=== Useful Extra Edges ===\n")

    # Test 1: line graph 1-2-3-4, extra edge 1->4 weight 2
    n = 4
    edges = [(1, 2, 5), (2, 3, 5), (3, 4, 5)]
    extra = [(1, 4, 2)]
    print("Test 1 (extra edge shortcut):")
    print(f"  Brute:   {brute_force(n, edges, 1, 4, extra)}")   # 2
    print(f"  Optimal: {optimal(n, edges, 1, 4, extra)}")        # 2
    print(f"  Best:    {best(n, edges, 1, 4, extra)}")           # 2

    extra2 = [(2, 3, 100)]
    print("\nTest 2 (extra edge useless):")
    print(f"  Best: {best(n, edges, 1, 4, extra2)}")             # 15

    edges3 = [(1, 2, 5)]
    extra3 = [(2, 3, 3)]
    print("\nTest 3 (extra makes path possible):")
    print(f"  Best: {best(3, edges3, 1, 3, extra3)}")            # 8
