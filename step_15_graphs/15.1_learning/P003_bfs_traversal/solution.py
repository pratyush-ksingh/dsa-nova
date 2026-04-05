"""
Problem: BFS Traversal
Difficulty: EASY | XP: 10

Implement BFS on a graph. Return nodes in BFS order from source.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: STANDARD BFS (Single Component)
# Time: O(V + E) | Space: O(V)
# ============================================================
def bfs(V: int, adj: List[List[int]], src: int) -> List[int]:
    """Standard BFS from source. Returns nodes in visit order."""
    result = []
    visited = [False] * V

    queue = deque([src])
    visited[src] = True  # mark visited at ENQUEUE time

    while queue:
        node = queue.popleft()
        result.append(node)

        for neighbor in adj[node]:
            if not visited[neighbor]:
                visited[neighbor] = True  # mark before enqueue!
                queue.append(neighbor)

    return result


# ============================================================
# APPROACH 2: BFS WITH LEVEL TRACKING
# Time: O(V + E) | Space: O(V)
# ============================================================
def bfs_levels(V: int, adj: List[List[int]], src: int) -> List[List[int]]:
    """BFS returning nodes grouped by level/distance from source."""
    levels = []
    visited = [False] * V

    queue = deque([src])
    visited[src] = True

    while queue:
        level_size = len(queue)
        level = []
        for _ in range(level_size):
            node = queue.popleft()
            level.append(node)
            for neighbor in adj[node]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append(neighbor)
        levels.append(level)

    return levels


# ============================================================
# APPROACH 3: BFS FOR DISCONNECTED GRAPH (All Components)
# Time: O(V + E) | Space: O(V)
# ============================================================
def bfs_all(V: int, adj: List[List[int]]) -> List[int]:
    """BFS that covers all components of a disconnected graph."""
    result = []
    visited = [False] * V

    for i in range(V):
        if not visited[i]:
            queue = deque([i])
            visited[i] = True
            while queue:
                node = queue.popleft()
                result.append(node)
                for neighbor in adj[node]:
                    if not visited[neighbor]:
                        visited[neighbor] = True
                        queue.append(neighbor)

    return result


# ============================================================
# HELPER: Build adjacency list from edge list
# ============================================================
def build_adj(V: int, edges: List[List[int]]) -> List[List[int]]:
    adj = [[] for _ in range(V)]
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)
    return adj


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== BFS Traversal ===\n")

    # Test 1: Tree-like graph
    adj1 = build_adj(5, [[0,1],[0,2],[1,3],[2,4]])
    result1 = bfs(5, adj1, 0)
    print(f"Test 1 - Tree graph:")
    print(f"  BFS from 0: {result1}")
    print(f"  Levels:     {bfs_levels(5, adj1, 0)}")
    print(f"  Expected:   [0, 1, 2, 3, 4]")
    print(f"  Pass: {result1 == [0, 1, 2, 3, 4]}\n")

    # Test 2: Cyclic graph
    adj2 = build_adj(4, [[0,1],[1,2],[2,0],[1,3]])
    print(f"Test 2 - Cyclic graph:")
    print(f"  BFS from 0: {bfs(4, adj2, 0)}")
    print(f"  Levels:     {bfs_levels(4, adj2, 0)}\n")

    # Test 3: Disconnected graph
    adj3 = build_adj(4, [[0,1],[2,3]])
    print(f"Test 3 - Disconnected graph:")
    print(f"  BFS from 0:   {bfs(4, adj3, 0)}")
    print(f"  BFS all:      {bfs_all(4, adj3)}")
    print(f"  Expected all: [0, 1, 2, 3]\n")

    # Test 4: Single node
    adj4 = build_adj(1, [])
    print(f"Test 4 - Single node:")
    print(f"  BFS from 0: {bfs(1, adj4, 0)}")
    print(f"  Expected:   [0]")
    print(f"  Pass: {bfs(1, adj4, 0) == [0]}\n")

    # Test 5: Star graph
    adj5 = build_adj(5, [[0,1],[0,2],[0,3],[0,4]])
    print(f"Test 5 - Star graph:")
    print(f"  BFS from 0: {bfs(5, adj5, 0)}")
    print(f"  Levels:     {bfs_levels(5, adj5, 0)}")
    print(f"  Expected:   [0, 1, 2, 3, 4]")
