"""
Problem: Course Schedule II (LeetCode #210)
Difficulty: MEDIUM | XP: 25

There are numCourses courses labeled 0 to numCourses-1. Given prerequisites
where prerequisites[i] = [a, b] means you must take b before a, return an
ordering of courses. Return empty array if impossible (cycle exists).
"""
from typing import List
from collections import deque, defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE (DFS-based topological sort)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def findOrder_dfs(numCourses: int, prerequisites: List[List[int]]) -> List[int]:
    """
    DFS-based topo sort: run DFS from each unvisited node, add to stack
    on backtrack. Use 3-state coloring to detect cycles:
    - 0 (white): unvisited
    - 1 (gray): in current DFS path (cycle if revisited)
    - 2 (black): fully processed
    """
    adj = defaultdict(list)
    for a, b in prerequisites:
        adj[b].append(a)  # b -> a (b must come before a)

    color = [0] * numCourses  # 0=white, 1=gray, 2=black
    order = []

    def dfs(node: int) -> bool:
        """Returns False if cycle detected."""
        color[node] = 1  # gray - in progress
        for neighbor in adj[node]:
            if color[neighbor] == 1:
                return False  # back edge -> cycle
            if color[neighbor] == 0:
                if not dfs(neighbor):
                    return False
        color[node] = 2  # black - done
        order.append(node)
        return True

    for i in range(numCourses):
        if color[i] == 0:
            if not dfs(i):
                return []  # cycle detected

    return order[::-1]  # reverse post-order = topological order


# ============================================================
# APPROACH 2: OPTIMAL (Kahn's Algorithm - BFS with indegree)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def findOrder_kahns(numCourses: int, prerequisites: List[List[int]]) -> List[int]:
    """
    Kahn's algorithm: compute indegree for each node. Start BFS from
    nodes with indegree 0. As we process a node, decrement indegree
    of its neighbors. If neighbor reaches 0, add to queue.
    If we process all nodes, we have a valid order; otherwise cycle exists.
    """
    adj = defaultdict(list)
    indegree = [0] * numCourses

    for a, b in prerequisites:
        adj[b].append(a)
        indegree[a] += 1

    # Start with all nodes having indegree 0
    queue = deque()
    for i in range(numCourses):
        if indegree[i] == 0:
            queue.append(i)

    order = []
    while queue:
        node = queue.popleft()
        order.append(node)

        for neighbor in adj[node]:
            indegree[neighbor] -= 1
            if indegree[neighbor] == 0:
                queue.append(neighbor)

    # If all courses are in the order, valid; otherwise cycle exists
    return order if len(order) == numCourses else []


# ============================================================
# APPROACH 3: BEST (Kahn's Algorithm - clean version)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def findOrder_best(numCourses: int, prerequisites: List[List[int]]) -> List[int]:
    """
    Same Kahn's approach, cleaner implementation.
    Kahn's is preferred for this problem because:
    1. It naturally produces the topological order (no reversal needed)
    2. Cycle detection is trivial (count processed nodes)
    3. It's iterative (no stack overflow risk)
    """
    adj = [[] for _ in range(numCourses)]
    indegree = [0] * numCourses

    for course, prereq in prerequisites:
        adj[prereq].append(course)
        indegree[course] += 1

    queue = deque(i for i in range(numCourses) if indegree[i] == 0)
    order = []

    while queue:
        node = queue.popleft()
        order.append(node)
        for nei in adj[node]:
            indegree[nei] -= 1
            if indegree[nei] == 0:
                queue.append(nei)

    return order if len(order) == numCourses else []


if __name__ == "__main__":
    print("=== Course Schedule II ===\n")

    # Test 1: 4 courses, valid order exists
    n1, p1 = 4, [[1, 0], [2, 0], [3, 1], [3, 2]]
    print(f"Test 1: numCourses=4, prereqs={p1}")
    print(f"  DFS:    {findOrder_dfs(n1, p1)}")
    print(f"  Kahn's: {findOrder_kahns(n1, p1)}")
    print(f"  Best:   {findOrder_best(n1, p1)}")

    # Test 2: 2 courses, simple dependency
    n2, p2 = 2, [[1, 0]]
    print(f"\nTest 2: numCourses=2, prereqs={p2}")
    print(f"  DFS:    {findOrder_dfs(n2, p2)}")
    print(f"  Kahn's: {findOrder_kahns(n2, p2)}")
    print(f"  Best:   {findOrder_best(n2, p2)}")

    # Test 3: Cycle - impossible
    n3, p3 = 2, [[1, 0], [0, 1]]
    print(f"\nTest 3 (cycle): numCourses=2, prereqs={p3}")
    print(f"  DFS:    {findOrder_dfs(n3, p3)}")
    print(f"  Kahn's: {findOrder_kahns(n3, p3)}")
    print(f"  Best:   {findOrder_best(n3, p3)}")

    # Test 4: No prerequisites
    n4, p4 = 3, []
    print(f"\nTest 4 (no prereqs): numCourses=3, prereqs={p4}")
    print(f"  DFS:    {findOrder_dfs(n4, p4)}")
    print(f"  Kahn's: {findOrder_kahns(n4, p4)}")
    print(f"  Best:   {findOrder_best(n4, p4)}")

    # Test 5: Single course
    n5, p5 = 1, []
    print(f"\nTest 5 (single): numCourses=1, prereqs={p5}")
    print(f"  DFS:    {findOrder_dfs(n5, p5)}")
    print(f"  Kahn's: {findOrder_kahns(n5, p5)}")
    print(f"  Best:   {findOrder_best(n5, p5)}")
