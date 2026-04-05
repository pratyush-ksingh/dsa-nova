"""
Problem: Detect Cycle in Undirected Graph using BFS
Difficulty: MEDIUM | XP: 25

Given an adjacency list for an undirected graph with V vertices and E edges,
detect whether the graph contains a cycle or not.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE (Edge-list back-edge check via BFS)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def detect_cycle_brute(V: int, adj: List[List[int]]) -> bool:
    """
    BFS from each unvisited node. Track visited nodes and parent.
    For each neighbor, if already visited and not the parent of
    current node in BFS tree, we found a back edge => cycle.
    (Conceptually treating it as checking all edges for back edges.)
    """
    visited = [False] * V

    for start in range(V):
        if visited[start]:
            continue
        # BFS from start
        queue = deque()
        queue.append((start, -1))  # (node, parent)
        visited[start] = True

        while queue:
            node, parent = queue.popleft()
            for neighbor in adj[node]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append((neighbor, node))
                elif neighbor != parent:
                    # Visited and not parent => cycle
                    return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL (BFS with parent tracking)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def detect_cycle_optimal(V: int, adj: List[List[int]]) -> bool:
    """
    Standard BFS cycle detection: maintain a parent array.
    If we encounter a visited neighbor that is not our parent,
    a cycle exists. Handles disconnected components.
    """
    visited = [False] * V

    def bfs_check(src: int) -> bool:
        queue = deque()
        queue.append((src, -1))
        visited[src] = True

        while queue:
            node, parent = queue.popleft()
            for neighbor in adj[node]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append((neighbor, node))
                elif neighbor != parent:
                    return True
        return False

    for i in range(V):
        if not visited[i]:
            if bfs_check(i):
                return True
    return False


# ============================================================
# APPROACH 3: BEST (BFS with parent tracking - clean version)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def detect_cycle_best(V: int, adj: List[List[int]]) -> bool:
    """
    Same BFS parent approach - this is already optimal for BFS-based
    cycle detection. Clean single-function implementation.
    """
    visited = [False] * V
    parent = [-1] * V

    for start in range(V):
        if visited[start]:
            continue
        visited[start] = True
        queue = deque([start])

        while queue:
            node = queue.popleft()
            for neighbor in adj[node]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    parent[neighbor] = node
                    queue.append(neighbor)
                elif parent[node] != neighbor:
                    return True
    return False


if __name__ == "__main__":
    print("=== Detect Cycle in Undirected BFS ===\n")

    # Test 1: Graph with cycle: 0-1-2-0
    adj1 = [[1, 2], [0, 2], [0, 1]]
    print(f"Test 1 (cycle 0-1-2-0): Expected True")
    print(f"  Brute:   {detect_cycle_brute(3, adj1)}")
    print(f"  Optimal: {detect_cycle_optimal(3, adj1)}")
    print(f"  Best:    {detect_cycle_best(3, adj1)}")

    # Test 2: Tree (no cycle): 0-1, 0-2
    adj2 = [[1, 2], [0], [0]]
    print(f"\nTest 2 (tree, no cycle): Expected False")
    print(f"  Brute:   {detect_cycle_brute(3, adj2)}")
    print(f"  Optimal: {detect_cycle_optimal(3, adj2)}")
    print(f"  Best:    {detect_cycle_best(3, adj2)}")

    # Test 3: Disconnected with cycle: 0-1-2-0, 3-4
    adj3 = [[1, 2], [0, 2], [0, 1], [4], [3]]
    print(f"\nTest 3 (disconnected, has cycle): Expected True")
    print(f"  Brute:   {detect_cycle_brute(5, adj3)}")
    print(f"  Optimal: {detect_cycle_optimal(5, adj3)}")
    print(f"  Best:    {detect_cycle_best(5, adj3)}")

    # Test 4: Single node
    adj4 = [[]]
    print(f"\nTest 4 (single node): Expected False")
    print(f"  Brute:   {detect_cycle_brute(1, adj4)}")
    print(f"  Optimal: {detect_cycle_optimal(1, adj4)}")
    print(f"  Best:    {detect_cycle_best(1, adj4)}")
