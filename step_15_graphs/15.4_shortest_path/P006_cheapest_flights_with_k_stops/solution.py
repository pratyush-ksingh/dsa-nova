"""
Problem: Cheapest Flights Within K Stops (LeetCode #787)
Difficulty: MEDIUM | XP: 25

There are n cities connected by flights. Given flights as (from, to, price),
find the cheapest price from src to dst with at most k stops.
A "stop" is an intermediate city. Return -1 if no such route exists.
"""
from typing import List
from collections import defaultdict, deque
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS/DFS exploring all paths
# Time: O(n^k)  |  Space: O(n*k)
# Explore every possible path with <= k stops using BFS.
# No pruning beyond the stop-count limit.
# ============================================================
def brute_force(n: int, flights: List[List[int]], src: int, dst: int, k: int) -> int:
    # Build adjacency list
    adj = defaultdict(list)
    for u, v, w in flights:
        adj[u].append((v, w))

    # BFS: state = (current_node, cost_so_far, stops_used)
    # stops_used counts intermediate cities (not including src)
    queue = deque([(src, 0, 0)])  # (node, cost, stops)
    best_cost = float('inf')

    # Track minimum cost to reach each node at each stop count to prune cycles
    min_cost = [[float('inf')] * (k + 2) for _ in range(n)]
    min_cost[src][0] = 0

    while queue:
        node, cost, stops = queue.popleft()

        if node == dst:
            best_cost = min(best_cost, cost)
            continue

        if stops > k:
            continue

        for neighbor, price in adj[node]:
            new_cost = cost + price
            new_stops = stops + 1
            if new_cost < min_cost[neighbor][new_stops] and new_cost < best_cost:
                min_cost[neighbor][new_stops] = new_cost
                queue.append((neighbor, new_cost, new_stops))

    return best_cost if best_cost != float('inf') else -1


# ============================================================
# APPROACH 2: OPTIMAL -- Bellman-Ford (k+1 relaxation rounds)
# Time: O(k * E)  |  Space: O(n)
# Run Bellman-Ford for exactly k+1 iterations (k stops = k+1 edges).
# Use a temp copy each iteration to prevent multi-hop updates in one pass.
# ============================================================
def optimal(n: int, flights: List[List[int]], src: int, dst: int, k: int) -> int:
    INF = float('inf')
    dist = [INF] * n
    dist[src] = 0

    for _ in range(k + 1):  # k+1 edges = k intermediate stops
        temp = dist[:]       # snapshot prevents using updates from same round
        for u, v, w in flights:
            if dist[u] != INF and dist[u] + w < temp[v]:
                temp[v] = dist[u] + w
        dist = temp

    return dist[dst] if dist[dst] != INF else -1


# ============================================================
# APPROACH 3: BEST -- Dijkstra with stop-count in state
# Time: O(k * E * log(k*n))  |  Space: O(k*n)
# Standard Dijkstra but state = (cost, node, stops_remaining).
# A node can be visited multiple times at different stop counts.
# Only prune when we've seen (node, stops) with equal or less cost.
# ============================================================
def best(n: int, flights: List[List[int]], src: int, dst: int, k: int) -> int:
    adj = defaultdict(list)
    for u, v, w in flights:
        adj[u].append((v, w))

    # min_cost[node][stops] = cheapest cost to reach node using exactly that many stops
    INF = float('inf')
    min_cost = [[INF] * (k + 2) for _ in range(n)]
    min_cost[src][0] = 0

    # heap: (cost, node, stops_used)
    heap = [(0, src, 0)]

    while heap:
        cost, node, stops = heapq.heappop(heap)

        if node == dst:
            return cost

        if stops > k:
            continue

        # Prune stale entries
        if cost > min_cost[node][stops]:
            continue

        for neighbor, price in adj[node]:
            new_cost = cost + price
            new_stops = stops + 1
            if new_cost < min_cost[neighbor][new_stops]:
                min_cost[neighbor][new_stops] = new_cost
                heapq.heappush(heap, (new_cost, neighbor, new_stops))

    return -1


if __name__ == "__main__":
    print("=== Cheapest Flights Within K Stops ===\n")

    # Example 1: n=4, flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src=0, dst=3, k=1
    flights1 = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]]
    print(f"Brute:   {brute_force(4, flights1, 0, 3, 1)}")   # 700
    print(f"Optimal: {optimal(4, flights1, 0, 3, 1)}")        # 700
    print(f"Best:    {best(4, flights1, 0, 3, 1)}")           # 700

    print()
    # Example 2: n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=1
    flights2 = [[0,1,100],[1,2,100],[0,2,500]]
    print(f"Brute:   {brute_force(3, flights2, 0, 2, 1)}")   # 200
    print(f"Optimal: {optimal(3, flights2, 0, 2, 1)}")        # 200
    print(f"Best:    {best(3, flights2, 0, 2, 1)}")           # 200

    print()
    # k=0: only direct flights
    print(f"k=0:     {optimal(3, flights2, 0, 2, 0)}")        # 500

    print()
    # No path
    flights3 = [[0,1,100]]
    print(f"No path: {optimal(3, flights3, 0, 2, 1)}")        # -1
