"""
Problem: Minimum Spanning Tree (Prim's Algorithm)
Difficulty: MEDIUM | XP: 25

Find MST using Prim's algorithm.
Return total weight and MST edges.
"""
from typing import List, Tuple
import heapq


# ============================================================
# APPROACH 1: NAIVE PRIM'S (No Heap)
# Time: O(V^2)  |  Space: O(V)
#
# Good for dense graphs. At each step, scan all vertices
# for the minimum key value.
# ============================================================
def prims_naive(V: int, adj: List[List[Tuple[int, int]]]) -> Tuple[int, List[Tuple[int, int, int]]]:
    """
    Returns (total_weight, list of MST edges as (u, v, weight)).
    adj[u] contains (v, weight) tuples.
    """
    key = [float('inf')] * V     # min weight edge to connect to MST
    in_mst = [False] * V
    parent = [-1] * V

    key[0] = 0  # start from node 0

    total_weight = 0

    for _ in range(V):
        # Find vertex with minimum key not in MST
        u = -1
        for v in range(V):
            if not in_mst[v] and (u == -1 or key[v] < key[u]):
                u = v

        in_mst[u] = True
        total_weight += key[u]

        # Update keys of neighbors
        for v, w in adj[u]:
            if not in_mst[v] and w < key[v]:
                key[v] = w
                parent[v] = u

    # Collect MST edges
    mst_edges = []
    for i in range(1, V):
        mst_edges.append((parent[i], i, key[i]))

    return total_weight, mst_edges


# ============================================================
# APPROACH 2: PRIM'S WITH MIN-HEAP (Optimal for Sparse)
# Time: O(E log V)  |  Space: O(V + E)
#
# Priority queue for efficient minimum edge selection.
# Lazy deletion: skip nodes already in MST.
# ============================================================
def prims_heap(V: int, adj: List[List[Tuple[int, int]]]) -> int:
    """
    Returns total MST weight.
    """
    in_mst = [False] * V
    # Heap stores (weight, node)
    heap = [(0, 0)]  # start from node 0 with weight 0

    total_weight = 0
    edges_added = 0

    while heap and edges_added < V:
        weight, node = heapq.heappop(heap)

        if in_mst[node]:
            continue  # skip stale entry

        in_mst[node] = True
        total_weight += weight
        edges_added += 1

        for v, w in adj[node]:
            if not in_mst[v]:
                heapq.heappush(heap, (w, v))

    return total_weight


# ============================================================
# APPROACH 3: PRIM'S WITH MST EDGE COLLECTION
# Time: O(E log V)  |  Space: O(V + E)
#
# Same as Approach 2 but collects actual MST edges.
# Heap stores (weight, node, parent).
# ============================================================
def prims_with_edges(V: int, adj: List[List[Tuple[int, int]]]) -> Tuple[int, List[Tuple[int, int, int]]]:
    """
    Returns (total_weight, list of MST edges as (parent, node, weight)).
    """
    in_mst = [False] * V
    # Heap stores (weight, node, parent)
    heap = [(0, 0, -1)]

    total_weight = 0
    mst_edges = []

    while heap:
        weight, node, parent = heapq.heappop(heap)

        if in_mst[node]:
            continue

        in_mst[node] = True
        total_weight += weight

        if parent != -1:
            mst_edges.append((parent, node, weight))

        for v, w in adj[node]:
            if not in_mst[v]:
                heapq.heappush(heap, (w, v, node))

    return total_weight, mst_edges


# ============================================================
# HELPER: Build weighted undirected adjacency list
# ============================================================
def build_graph(V: int, edges: List[Tuple[int, int, int]]) -> List[List[Tuple[int, int]]]:
    """
    edges: list of (u, v, weight)
    Returns adj list where adj[u] = [(v, weight), ...]
    """
    adj = [[] for _ in range(V)]
    for u, v, w in edges:
        adj[u].append((v, w))
        adj[v].append((u, w))
    return adj


if __name__ == "__main__":
    print("=== Minimum Spanning Tree (Prim's) ===\n")

    # Test 1:
    #     (1)
    # 0 ------- 1
    # |  \      |
    # (4) (3)  (2)
    # |    \    |
    # 3 ------- 2
    #     (5)
    #
    # MST: 0-1(1), 1-2(2), 0-3(4) = 7
    edges1 = [(0,1,1), (0,2,3), (0,3,4), (1,2,2), (2,3,5)]
    adj1 = build_graph(4, edges1)

    print("Test 1:")
    w_naive, e_naive = prims_naive(4, adj1)
    print(f"  Naive:  weight={w_naive}, edges={e_naive}")

    adj1 = build_graph(4, edges1)
    print(f"  Heap:   weight={prims_heap(4, adj1)}")

    adj1 = build_graph(4, edges1)
    w_edges, mst_edges = prims_with_edges(4, adj1)
    print(f"  Edges:  weight={w_edges}")
    for u, v, w in mst_edges:
        print(f"    {u} -- {v} (w={w})")

    # Test 2: Triangle
    edges2 = [(0,1,10), (0,2,6), (1,2,5)]
    adj2 = build_graph(3, edges2)
    print(f"\nTest 2 (triangle): weight={prims_heap(3, adj2)}")  # 11

    # Test 3: Larger graph
    edges3 = [
        (0,1,4), (0,3,8), (1,2,8), (1,4,11),
        (1,5,7), (2,5,2), (3,4,7), (4,5,4)
    ]
    adj3 = build_graph(6, edges3)
    w3, e3 = prims_with_edges(6, adj3)
    print(f"\nTest 3 (6 nodes): weight={w3}")
    for u, v, w in e3:
        print(f"  {u} -- {v} (w={w})")

    # Test 4: Single node
    adj4 = build_graph(1, [])
    print(f"\nTest 4 (single node): weight={prims_heap(1, adj4)}")  # 0

    # Test 5: Two nodes
    adj5 = build_graph(2, [(0,1,7)])
    print(f"Test 5 (two nodes): weight={prims_heap(2, adj5)}")  # 7
