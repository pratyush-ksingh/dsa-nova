"""
Problem: City with Smallest Neighbors at Threshold (LeetCode 1334)
Difficulty: MEDIUM | XP: 25

There are n cities numbered 0 to n-1. Given an array of edges where
edges[i] = [from_i, to_i, weight_i] represents a bidirectional weighted edge,
and an integer distanceThreshold, return the city with the smallest number of
cities reachable with a distance <= distanceThreshold.
If there is a tie, return the city with the greatest number (largest index).
"""
from typing import List
import heapq

INF = int(1e9)


# ============================================================
# APPROACH 1: BRUTE FORCE — Dijkstra from each city
# Time: O(V * (V + E) * log V)  |  Space: O(V + E)
# ============================================================
def brute_force(n: int, edges: List[List[int]], distanceThreshold: int) -> int:
    """
    Run Dijkstra from every city. Count how many other cities are reachable
    within distanceThreshold. Track the city with the minimum count
    (tie-break: pick the larger city index).
    """
    # Build undirected adjacency list
    adj = [[] for _ in range(n)]
    for u, v, w in edges:
        adj[u].append((v, w))
        adj[v].append((u, w))

    def dijkstra(src: int) -> List[int]:
        dist = [INF] * n
        dist[src] = 0
        pq = [(0, src)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w
                    heapq.heappush(pq, (dist[v], v))
        return dist

    result_city = -1
    min_count = INF

    for city in range(n):
        dists = dijkstra(city)
        # Count cities (other than itself) reachable within threshold
        count = sum(1 for other in range(n) if other != city and dists[other] <= distanceThreshold)
        # Pick city with fewest reachable; on tie pick larger index
        if count <= min_count:
            min_count = count
            result_city = city

    return result_city


# ============================================================
# APPROACH 2: OPTIMAL — Floyd-Warshall + count reachable cities
# Time: O(V^3)  |  Space: O(V^2)
# ============================================================
def optimal(n: int, edges: List[List[int]], distanceThreshold: int) -> int:
    """
    Build an all-pairs shortest path matrix using Floyd-Warshall.
    Then for each city, count how many other cities have distance <= threshold.
    Return the city with minimum count (largest index on tie).

    Floyd-Warshall is simpler to implement than repeated Dijkstra and handles
    all edge cases cleanly for dense, small graphs (n <= 100).
    """
    # Initialize distance matrix
    dist = [[INF] * n for _ in range(n)]
    for i in range(n):
        dist[i][i] = 0
    for u, v, w in edges:
        dist[u][v] = min(dist[u][v], w)
        dist[v][u] = min(dist[v][u], w)  # undirected

    # Floyd-Warshall
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] != INF and dist[k][j] != INF:
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    result_city = -1
    min_count = INF

    for city in range(n):
        count = sum(1 for other in range(n) if other != city and dist[city][other] <= distanceThreshold)
        if count <= min_count:
            min_count = count
            result_city = city

    return result_city


# ============================================================
# APPROACH 3: BEST — Floyd-Warshall with early-exit counting
# Time: O(V^3)  |  Space: O(V^2)
# ============================================================
def best(n: int, edges: List[List[int]], distanceThreshold: int) -> int:
    """
    Identical algorithm to Optimal, but the counting loop iterates cities
    in reverse (n-1 down to 0) and returns immediately once we find the city
    with the minimum count. Because we iterate from the largest index downward,
    the first time we see the minimum count we automatically satisfy the
    tie-breaking rule (return largest index) — so we can stop early.

    In the worst case still O(V^3) for Floyd-Warshall, but the final pass
    is slightly cleaner / can exit early.
    """
    dist = [[INF] * n for _ in range(n)]
    for i in range(n):
        dist[i][i] = 0
    for u, v, w in edges:
        dist[u][v] = min(dist[u][v], w)
        dist[v][u] = min(dist[v][u], w)

    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] != INF and dist[k][j] != INF:
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    result_city = -1
    min_count = INF

    # Iterate from largest city to smallest; first city achieving min_count wins tie
    for city in range(n - 1, -1, -1):
        count = sum(1 for other in range(n) if other != city and dist[city][other] <= distanceThreshold)
        if count < min_count:
            min_count = count
            result_city = city

    return result_city


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== City with Smallest Neighbors at Threshold ===\n")

    # LeetCode Example 1
    # n=4, edges=[[0,1,3],[1,2,1],[1,3,4],[2,3,1]], distanceThreshold=4
    # Expected output: 3
    n1 = 4
    edges1 = [[0, 1, 3], [1, 2, 1], [1, 3, 4], [2, 3, 1]]
    thresh1 = 4
    print(f"Example 1 (expected 3):")
    print(f"  Brute:   {brute_force(n1, edges1, thresh1)}")
    print(f"  Optimal: {optimal(n1, edges1, thresh1)}")
    print(f"  Best:    {best(n1, edges1, thresh1)}")

    # LeetCode Example 2
    # n=5, edges=[[0,1,2],[0,4,8],[1,2,3],[1,4,2],[2,3,1],[3,4,1]], distanceThreshold=2
    # Expected output: 0
    n2 = 5
    edges2 = [[0, 1, 2], [0, 4, 8], [1, 2, 3], [1, 4, 2], [2, 3, 1], [3, 4, 1]]
    thresh2 = 2
    print(f"\nExample 2 (expected 0):")
    print(f"  Brute:   {brute_force(n2, edges2, thresh2)}")
    print(f"  Optimal: {optimal(n2, edges2, thresh2)}")
    print(f"  Best:    {best(n2, edges2, thresh2)}")
