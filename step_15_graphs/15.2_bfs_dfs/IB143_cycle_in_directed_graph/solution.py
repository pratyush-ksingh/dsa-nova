"""
Problem: Cycle in Directed Graph
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given V vertices and directed edges, detect if a cycle exists.
Return 1 if yes, 0 if no.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive DFS with recursion stack
# Time: O(V + E)  |  Space: O(V)
# ============================================================
def brute_force(V: int, edges: List[List[int]]) -> int:
    adj = [[] for _ in range(V + 1)]
    for u, v in edges:
        adj[u].append(v)

    visited = [False] * (V + 1)
    in_stack = [False] * (V + 1)

    def dfs(node):
        visited[node] = True
        in_stack[node] = True
        for nb in adj[node]:
            if not visited[nb]:
                if dfs(nb):
                    return True
            elif in_stack[nb]:
                return True
        in_stack[node] = False
        return False

    for i in range(1, V + 1):
        if not visited[i] and dfs(i):
            return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - 3-color DFS (WHITE/GRAY/BLACK)
# Time: O(V + E)  |  Space: O(V)
# ============================================================
# 0=unvisited, 1=in progress (gray), 2=done (black).
# Finding a gray node during DFS means a back edge => cycle.
# ============================================================
def optimal(V: int, edges: List[List[int]]) -> int:
    adj = [[] for _ in range(V + 1)]
    for u, v in edges:
        adj[u].append(v)

    color = [0] * (V + 1)  # 0=WHITE, 1=GRAY, 2=BLACK

    def dfs(node):
        color[node] = 1  # GRAY
        for nb in adj[node]:
            if color[nb] == 1:  # back edge
                return True
            if color[nb] == 0 and dfs(nb):
                return True
        color[node] = 2  # BLACK
        return False

    for i in range(1, V + 1):
        if color[i] == 0 and dfs(i):
            return 1
    return 0


# ============================================================
# APPROACH 3: BEST - Kahn's Algorithm (Topological BFS)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
# Build in-degree table. Process nodes with 0 in-degree. If we
# can't process all V nodes, a cycle prevents some from ever
# reaching 0 in-degree.
# Real-life use: circular import detection in Python/Node.js,
# deadlock detection in OS process scheduling.
# ============================================================
def best(V: int, edges: List[List[int]]) -> int:
    adj = [[] for _ in range(V + 1)]
    indegree = [0] * (V + 1)

    for u, v in edges:
        adj[u].append(v)
        indegree[v] += 1

    q = deque(i for i in range(1, V + 1) if indegree[i] == 0)
    processed = 0

    while q:
        node = q.popleft()
        processed += 1
        for nb in adj[node]:
            indegree[nb] -= 1
            if indegree[nb] == 0:
                q.append(nb)

    return 0 if processed == V else 1


if __name__ == "__main__":
    print("=== Cycle in Directed Graph ===")

    # Test 1: 1->2->3->1 (cycle)
    e1 = [[1,2],[2,3],[3,1]]
    print(f"Test1 Brute   (expect 1): {brute_force(3, e1)}")
    print(f"Test1 Optimal (expect 1): {optimal(3, e1)}")
    print(f"Test1 Best    (expect 1): {best(3, e1)}")

    # Test 2: DAG: 1->2->3, 1->3
    e2 = [[1,2],[2,3],[1,3]]
    print(f"Test2 Brute   (expect 0): {brute_force(3, e2)}")
    print(f"Test2 Optimal (expect 0): {optimal(3, e2)}")
    print(f"Test2 Best    (expect 0): {best(3, e2)}")

    # Test 3: self-loop
    e3 = [[1,1]]
    print(f"Test3 Best    (expect 1): {best(1, e3)}")

    # Test 4: disconnected DAG
    e4 = [[1,2],[3,4]]
    print(f"Test4 Best    (expect 0): {best(4, e4)}")

    # Test 5: disconnected, one cycle
    e5 = [[1,2],[3,4],[4,3]]
    print(f"Test5 Best    (expect 1): {best(4, e5)}")
