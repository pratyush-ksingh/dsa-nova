"""
Problem: Number of Ways to Arrive at Destination (LeetCode #1976)
Difficulty: MEDIUM | XP: 25

You are in a city of n intersections (0 to n-1) with bidirectional roads.
Count the number of ways to go from 0 to n-1 via the shortest path.
Return the count modulo 10^9 + 7.
"""
from typing import List
from collections import defaultdict
import heapq


MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS counting all shortest paths
# Time: O(2^V) in worst case  |  Space: O(V + E)
# Find shortest dist first, then DFS counting paths of that exact length.
# ============================================================
def brute_force(n: int, roads: List[List[int]]) -> int:
    adj = defaultdict(list)
    for u, v, w in roads:
        adj[u].append((v, w))
        adj[v].append((u, w))

    # Step 1: Find shortest distance from 0 to n-1 using Dijkstra
    INF = float('inf')
    dist = [INF] * n
    dist[0] = 0
    heap = [(0, 0)]
    while heap:
        d, u = heapq.heappop(heap)
        if d > dist[u]:
            continue
        for v, w in adj[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(heap, (dist[v], v))

    target_dist = dist[n - 1]
    if target_dist == INF:
        return 0

    # Step 2: DFS counting paths of exactly target_dist length
    visited = [False] * n
    count = [0]

    def dfs(node: int, cost: int) -> None:
        if node == n - 1 and cost == target_dist:
            count[0] = (count[0] + 1) % MOD
            return
        if cost >= target_dist:
            return
        visited[node] = True
        for neighbor, w in adj[node]:
            if not visited[neighbor] and cost + w <= target_dist:
                dfs(neighbor, cost + w)
        visited[node] = False

    dfs(0, 0)
    return count[0]


# ============================================================
# APPROACH 2: OPTIMAL -- Dijkstra tracking dist[] and ways[]
# Time: O((V + E) log V)  |  Space: O(V + E)
# Run Dijkstra. Maintain ways[v] = number of shortest paths from 0 to v.
# When a shorter path is found: update dist, reset ways.
# When an equal path is found: add to ways (modular arithmetic).
# ============================================================
def optimal(n: int, roads: List[List[int]]) -> int:
    adj = defaultdict(list)
    for u, v, w in roads:
        adj[u].append((v, w))
        adj[v].append((u, w))

    INF = float('inf')
    dist = [INF] * n
    ways = [0] * n
    dist[0] = 0
    ways[0] = 1  # exactly 1 way to reach the source

    heap = [(0, 0)]  # (cost, node)

    while heap:
        d, u = heapq.heappop(heap)

        if d > dist[u]:
            continue  # stale

        for v, w in adj[u]:
            new_dist = dist[u] + w

            if new_dist < dist[v]:
                # Found strictly shorter path -> reset ways
                dist[v] = new_dist
                ways[v] = ways[u]
                heapq.heappush(heap, (new_dist, v))

            elif new_dist == dist[v]:
                # Found equally short path -> accumulate ways
                ways[v] = (ways[v] + ways[u]) % MOD

    return ways[n - 1]


# ============================================================
# APPROACH 3: BEST -- Same Dijkstra with long (64-bit aware) arithmetic
# Time: O((V + E) log V)  |  Space: O(V + E)
# Identical to Approach 2. The "best" improvement is using integer
# distances (avoiding float) and explicit modular addition, which
# avoids floating-point precision issues for very large weights.
# ============================================================
def best(n: int, roads: List[List[int]]) -> int:
    adj = [[] for _ in range(n)]
    for u, v, w in roads:
        adj[u].append((v, w))
        adj[v].append((u, w))

    INF = float('inf')
    dist = [INF] * n
    ways = [0] * n
    dist[0] = 0
    ways[0] = 1

    # Use integer-keyed heap to avoid float comparison issues
    heap = [(0, 0)]

    while heap:
        d, u = heapq.heappop(heap)

        if d > dist[u]:
            continue

        for v, w in adj[u]:
            new_dist = d + w
            if new_dist < dist[v]:
                dist[v] = new_dist
                ways[v] = ways[u]
                heapq.heappush(heap, (new_dist, v))
            elif new_dist == dist[v]:
                ways[v] = (ways[v] + ways[u]) % MOD

    return ways[n - 1]


if __name__ == "__main__":
    print("=== Number of Ways to Arrive at Destination ===\n")

    # Example 1: n=7, roads=[[0,6,7],[0,1,2],[1,2,3],[1,3,3],[6,3,3],[3,5,1],[6,5,1],[2,5,1],[0,4,5],[4,6,2]]
    roads1 = [[0,6,7],[0,1,2],[1,2,3],[1,3,3],[6,3,3],[3,5,1],[6,5,1],[2,5,1],[0,4,5],[4,6,2]]
    print(f"Brute:   {brute_force(7, roads1)}")   # 4
    print(f"Optimal: {optimal(7, roads1)}")        # 4
    print(f"Best:    {best(7, roads1)}")           # 4

    print()
    # Simple: n=2, direct road
    print(f"Two nodes: {optimal(2, [[0,1,5]])}")   # 1

    print()
    # Two equal paths
    roads3 = [[0,1,1],[0,2,1],[1,3,1],[2,3,1]]
    print(f"Two equal: {optimal(4, roads3)}")      # 2
