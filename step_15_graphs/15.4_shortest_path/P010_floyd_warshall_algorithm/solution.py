"""
Problem: Floyd Warshall Algorithm
Difficulty: MEDIUM | XP: 25

Given a weighted directed graph with V vertices represented as an adjacency matrix,
find the shortest path between every pair of vertices.
Use INF = 1e8 (not float('inf')) to allow intermediate additions without overflow issues.
"""
from typing import List
import heapq

INF = int(1e8)


# ============================================================
# APPROACH 1: BRUTE FORCE — Run Dijkstra from every vertex
# Time: O(V * (V + E) * log V)  |  Space: O(V^2)
# ============================================================
def brute_force(matrix: List[List[int]]) -> List[List[int]]:
    """
    Run Dijkstra from each source vertex to compute all-pairs shortest paths.
    Works correctly only on graphs with non-negative edge weights.
    """
    V = len(matrix)

    # Build adjacency list from matrix (skip INF = no edge)
    adj = [[] for _ in range(V)]
    for u in range(V):
        for v in range(V):
            if u != v and matrix[u][v] != INF:
                adj[u].append((v, matrix[u][v]))

    def dijkstra(src: int) -> List[int]:
        dist = [INF] * V
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

    result = []
    for src in range(V):
        result.append(dijkstra(src))
    return result


# ============================================================
# APPROACH 2: OPTIMAL — Floyd-Warshall DP (triple nested loop)
# Time: O(V^3)  |  Space: O(V^2)
# ============================================================
def optimal(matrix: List[List[int]]) -> List[List[int]]:
    """
    Classic Floyd-Warshall: dp[i][j] = min cost from i to j.
    Relaxation: for each intermediate vertex k, try i -> k -> j.
    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])
    Diagonal is always 0 (self-loop costs nothing).
    """
    V = len(matrix)
    # Work on a copy; convert -1 sentinel to INF if needed
    dp = [row[:] for row in matrix]

    # Self-distance is 0
    for i in range(V):
        dp[i][i] = 0

    # Enumerate each intermediate vertex k
    for k in range(V):
        for i in range(V):
            for j in range(V):
                if dp[i][k] != INF and dp[k][j] != INF:
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])

    return dp


# ============================================================
# APPROACH 3: BEST — Floyd-Warshall + Negative Cycle Detection
# Time: O(V^3)  |  Space: O(V^2)
# ============================================================
def best(matrix: List[List[int]]) -> List[List[int]]:
    """
    Same as Optimal but also detects negative-weight cycles.
    After running Floyd-Warshall, if dp[i][i] < 0 for any vertex i,
    a negative cycle exists that passes through i.
    Returns the distance matrix, or raises ValueError if a negative cycle
    is detected (distances would be meaningless / -infinity).

    This is the production-ready version interviewers expect.
    """
    V = len(matrix)
    dp = [row[:] for row in matrix]

    for i in range(V):
        dp[i][i] = 0

    for k in range(V):
        for i in range(V):
            for j in range(V):
                if dp[i][k] != INF and dp[k][j] != INF:
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])

    # Negative cycle check: any vertex reachable from itself via a shorter path
    for i in range(V):
        if dp[i][i] < 0:
            raise ValueError(f"Negative cycle detected involving vertex {i}")

    return dp


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Floyd Warshall Algorithm ===\n")

    # Example graph (V=4), INF means no direct edge
    # Edges: 0->1 (3), 0->3 (7), 1->0 (8), 1->2 (2), 2->0 (5), 2->3 (1), 3->0 (2)
    V = 4
    graph = [
        [0,   3,   INF, 7  ],
        [8,   0,   2,   INF],
        [5,   INF, 0,   1  ],
        [2,   INF, INF, 0  ],
    ]

    print("Input adjacency matrix (INF=1e8 shown as INF):")
    for row in graph:
        print(["INF" if x == INF else x for x in row])

    print("\nBrute Force (Dijkstra from each vertex):")
    bf = brute_force(graph)
    for row in bf:
        print(["INF" if x == INF else x for x in row])

    print("\nOptimal (Floyd-Warshall):")
    opt = optimal(graph)
    for row in opt:
        print(["INF" if x == INF else x for x in row])

    print("\nBest (Floyd-Warshall + Negative Cycle Detection):")
    b = best(graph)
    for row in b:
        print(["INF" if x == INF else x for x in row])

    # Test negative cycle detection
    print("\nTesting negative cycle detection...")
    neg_graph = [
        [0,   1,   INF],
        [INF, 0,   -2 ],
        [-1,  INF, 0  ],
    ]
    try:
        best(neg_graph)
        print("No negative cycle (unexpected)")
    except ValueError as e:
        print(f"Caught: {e}")
