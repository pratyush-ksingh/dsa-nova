"""
Problem: Clone Graph
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Deep copy an undirected, connected graph where each node has
a value and a list of neighbors.
"""
from typing import Optional, Dict
from collections import deque


class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive DFS with memoization
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def brute_force(node: Optional[Node]) -> Optional[Node]:
    if not node:
        return None
    visited: Dict[Node, Node] = {}

    def dfs(n: Node) -> Node:
        if n in visited:
            return visited[n]
        clone = Node(n.val)
        visited[n] = clone
        for nb in n.neighbors:
            clone.neighbors.append(dfs(nb))
        return clone

    return dfs(node)


# ============================================================
# APPROACH 2: OPTIMAL - BFS with HashMap
# Time: O(V + E)  |  Space: O(V)
# ============================================================
# BFS ensures each node is cloned exactly once. Dequeue a node,
# iterate its neighbors; clone & enqueue if unseen, then wire
# neighbor edge on the clone regardless.
# ============================================================
def optimal(node: Optional[Node]) -> Optional[Node]:
    if not node:
        return None

    clone_map: Dict[Node, Node] = {node: Node(node.val)}
    q = deque([node])

    while q:
        curr = q.popleft()
        for nb in curr.neighbors:
            if nb not in clone_map:
                clone_map[nb] = Node(nb.val)
                q.append(nb)
            clone_map[curr].neighbors.append(clone_map[nb])

    return clone_map[node]


# ============================================================
# APPROACH 3: BEST - Iterative DFS with explicit stack
# Time: O(V + E)  |  Space: O(V)
# ============================================================
# Iterative DFS avoids Python's recursion limit for large graphs.
# Explicit stack processes each node once; HashMap maps original
# to clone to handle back-edges.
# Real-life use: duplicating in-memory dependency graphs in
# build systems, or snapshotting a social graph for analytics.
# ============================================================
def best(node: Optional[Node]) -> Optional[Node]:
    if not node:
        return None

    clone_map: Dict[Node, Node] = {node: Node(node.val)}
    stack = [node]

    while stack:
        curr = stack.pop()
        for nb in curr.neighbors:
            if nb not in clone_map:
                clone_map[nb] = Node(nb.val)
                stack.append(nb)
            clone_map[curr].neighbors.append(clone_map[nb])

    return clone_map[node]


# ============================================================
# TEST HELPERS
# ============================================================

def build_graph(adj: list) -> Optional[Node]:
    """adj: 0-indexed adjacency list of 1-indexed values"""
    if not adj:
        return None
    nodes = [Node(i + 1) for i in range(len(adj))]
    for i, neighbors in enumerate(adj):
        nodes[i].neighbors = [nodes[v - 1] for v in neighbors]
    return nodes[0]


def print_graph(start: Optional[Node]) -> str:
    if not start:
        return "None"
    visited = set()
    q = deque([start])
    visited.add(id(start))
    parts = []
    while q:
        n = q.popleft()
        nb_vals = [nb.val for nb in n.neighbors]
        parts.append(f"{n.val}->{nb_vals}")
        for nb in n.neighbors:
            if id(nb) not in visited:
                visited.add(id(nb))
                q.append(nb)
    return ", ".join(parts)


if __name__ == "__main__":
    print("=== Clone Graph ===")

    # Test 1: 4-node cycle 1-2-3-4-1
    g1 = build_graph([[2, 4], [1, 3], [2, 4], [3, 1]])
    clone_bfs = optimal(g1)
    print(f"Original:  {print_graph(g1)}")
    print(f"BFS Clone: {print_graph(clone_bfs)}")
    print(f"Different objects? {g1 is not clone_bfs}")

    clone_dfs = best(g1)
    print(f"DFS Clone: {print_graph(clone_dfs)}")

    # Test 2: Single node
    single = Node(1)
    c = best(single)
    print(f"Single clone val (expect 1): {c.val}, different obj: {single is not c}")

    # Test 3: null
    print(f"Null input (expect None): {best(None)}")
