"""
Problem: Course Schedule I (LeetCode #207)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import deque, defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS with visited + recStack
# Time: O(V + E)  |  Space: O(V + E)
# Detect cycle using recursion stack tracking.
# ============================================================
def brute_force(numCourses: int, prerequisites: List[List[int]]) -> bool:
    adj = defaultdict(list)
    for a, b in prerequisites:
        adj[b].append(a)  # b -> a

    visited = [False] * numCourses
    rec_stack = [False] * numCourses

    def has_cycle(node: int) -> bool:
        visited[node] = True
        rec_stack[node] = True

        for neighbor in adj[node]:
            if not visited[neighbor]:
                if has_cycle(neighbor):
                    return True
            elif rec_stack[neighbor]:
                return True  # back edge = cycle

        rec_stack[node] = False  # backtrack
        return False

    for i in range(numCourses):
        if not visited[i]:
            if has_cycle(i):
                return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL -- Kahn's Algorithm (BFS Topological Sort)
# Time: O(V + E)  |  Space: O(V + E)
# Process nodes with in-degree 0 layer by layer.
# If all processed, no cycle; otherwise cycle exists.
# ============================================================
def optimal(numCourses: int, prerequisites: List[List[int]]) -> bool:
    adj = defaultdict(list)
    in_degree = [0] * numCourses

    for a, b in prerequisites:
        adj[b].append(a)  # b -> a
        in_degree[a] += 1

    # Enqueue all nodes with in-degree 0
    queue = deque(i for i in range(numCourses) if in_degree[i] == 0)
    processed = 0

    while queue:
        course = queue.popleft()
        processed += 1

        for dependent in adj[course]:
            in_degree[dependent] -= 1
            if in_degree[dependent] == 0:
                queue.append(dependent)

    return processed == numCourses


# ============================================================
# APPROACH 3: BEST -- DFS with 3-State Coloring
# Time: O(V + E)  |  Space: O(V + E)
# WHITE(0)=unvisited, GRAY(1)=in-progress, BLACK(2)=done.
# GRAY->GRAY edge = back edge = cycle.
# ============================================================
def best(numCourses: int, prerequisites: List[List[int]]) -> bool:
    adj = defaultdict(list)
    for a, b in prerequisites:
        adj[b].append(a)

    color = [0] * numCourses  # 0=WHITE, 1=GRAY, 2=BLACK

    def has_cycle(node: int) -> bool:
        color[node] = 1  # GRAY -- exploring

        for neighbor in adj[node]:
            if color[neighbor] == 1:  # back edge = cycle
                return True
            if color[neighbor] == 0:  # WHITE -- unvisited
                if has_cycle(neighbor):
                    return True
            # BLACK (2) -- fully processed, safe to skip

        color[node] = 2  # BLACK -- done
        return False

    for i in range(numCourses):
        if color[i] == 0:
            if has_cycle(i):
                return False
    return True


if __name__ == "__main__":
    print("=== Course Schedule I ===\n")

    # Example 1: no cycle
    print(f"Brute:   {brute_force(2, [[1, 0]])}")    # True
    print(f"Optimal: {optimal(2, [[1, 0]])}")          # True
    print(f"Best:    {best(2, [[1, 0]])}")             # True

    # Example 2: cycle
    print(f"\nCycle:")
    print(f"Brute:   {brute_force(2, [[1, 0], [0, 1]])}")   # False
    print(f"Optimal: {optimal(2, [[1, 0], [0, 1]])}")        # False
    print(f"Best:    {best(2, [[1, 0], [0, 1]])}")           # False

    # Example 3: diamond
    print(f"\nDiamond: {optimal(4, [[1,0],[2,0],[3,1],[3,2]])}")  # True

    # Edge case: no prerequisites
    print(f"No prereqs: {optimal(5, [])}")  # True

    # Edge case: self-loop
    print(f"Self-loop: {optimal(1, [[0, 0]])}")  # False
