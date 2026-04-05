"""
Problem: Network Delay Time (LeetCode #743)
Difficulty: MEDIUM | XP: 25

You have a network of n nodes labeled 1 to n. Given directed weighted edges
times[i] = [u, v, w] (signal travel time from u to v), a signal is sent from
node k. Return the minimum time for ALL nodes to receive the signal.
Return -1 if it's impossible.
"""
from typing import List
from collections import defaultdict
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE -- Bellman-Ford (relax V-1 times)
# Time: O(V * E)  |  Space: O(V)
# Relax all edges V-1 times. Correct for non-negative weights
# but wasteful compared to Dijkstra.
# ============================================================
def brute_force(times: List[List[int]], n: int, k: int) -> int:
    INF = float('inf')
    dist = [INF] * (n + 1)
    dist[k] = 0

    for _ in range(n - 1):
        updated = False
        for u, v, w in times:
            if dist[u] != INF and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:
            break

    max_dist = max(dist[1:])  # nodes 1..n (dist[0] unused)
    return max_dist if max_dist != INF else -1


# ============================================================
# APPROACH 2: OPTIMAL -- Dijkstra with Min-Heap
# Time: O((V + E) log V)  |  Space: O(V + E)
# Greedy: always process the nearest unvisited node.
# The answer is the maximum shortest distance across all nodes.
# ============================================================
def optimal(times: List[List[int]], n: int, k: int) -> int:
    # Build adjacency list (1-indexed nodes)
    adj = defaultdict(list)
    for u, v, w in times:
        adj[u].append((v, w))

    INF = float('inf')
    dist = [INF] * (n + 1)
    dist[k] = 0

    # Min-heap: (distance, node)
    heap = [(0, k)]

    while heap:
        d, u = heapq.heappop(heap)

        # Lazy deletion: skip stale entries
        if d > dist[u]:
            continue

        for v, w in adj[u]:
            new_dist = dist[u] + w
            if new_dist < dist[v]:
                dist[v] = new_dist
                heapq.heappush(heap, (new_dist, v))

    max_dist = max(dist[1:])
    return max_dist if max_dist != INF else -1


# ============================================================
# APPROACH 3: BEST -- Dijkstra with early termination
# Time: O((V + E) log V)  |  Space: O(V + E)
# Same Dijkstra but tracks a "visited count". Once all n nodes
# are finalized, we stop early. Returns answer immediately when
# the last unvisited node is popped from the heap.
# ============================================================
def best(times: List[List[int]], n: int, k: int) -> int:
    adj = defaultdict(list)
    for u, v, w in times:
        adj[u].append((v, w))

    INF = float('inf')
    dist = [INF] * (n + 1)
    dist[k] = 0
    heap = [(0, k)]
    visited = 0

    while heap:
        d, u = heapq.heappop(heap)

        if d > dist[u]:
            continue  # stale

        visited += 1
        if visited == n:
            return d  # all nodes finalized; this is the max dist

        for v, w in adj[u]:
            new_dist = d + w
            if new_dist < dist[v]:
                dist[v] = new_dist
                heapq.heappush(heap, (new_dist, v))

    # Some nodes unreachable
    return -1


if __name__ == "__main__":
    print("=== Network Delay Time ===\n")

    # Example 1: times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2
    times1 = [[2,1,1],[2,3,1],[3,4,1]]
    print(f"Brute:   {brute_force(times1, 4, 2)}")   # 2
    print(f"Optimal: {optimal(times1, 4, 2)}")        # 2
    print(f"Best:    {best(times1, 4, 2)}")           # 2

    print()
    # Example 2: n=1, k=1
    print(f"Single node: {optimal([], 1, 1)}")        # 0

    print()
    # Unreachable: signal from 1 but node 2 has no incoming edges
    times3 = [[1,3,1]]
    print(f"Unreachable: {optimal(times3, 3, 1)}")    # -1
