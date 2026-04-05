"""
Problem: Possibility of Finishing All Courses
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given N courses and prerequisites [a, b] meaning "must take b before a",
determine if all courses can be finished (detect cycle in directed graph).
"""

from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE
# DFS 3-color cycle detection (WHITE=0, GRAY=1, BLACK=2)
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def brute_force(num_courses: int, prerequisites: List[List[int]]) -> bool:
    adj = [[] for _ in range(num_courses)]
    for a, b in prerequisites:
        adj[b].append(a)  # b must come before a

    color = [0] * num_courses  # 0=white, 1=gray, 2=black

    def has_cycle(node):
        color[node] = 1
        for neighbor in adj[node]:
            if color[neighbor] == 1:
                return True
            if color[neighbor] == 0 and has_cycle(neighbor):
                return True
        color[node] = 2
        return False

    for i in range(num_courses):
        if color[i] == 0 and has_cycle(i):
            return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL
# Kahn's Algorithm (BFS Topological Sort)
# Count processed nodes — if == numCourses, no cycle
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def optimal(num_courses: int, prerequisites: List[List[int]]) -> bool:
    adj = [[] for _ in range(num_courses)]
    in_degree = [0] * num_courses

    for a, b in prerequisites:
        adj[b].append(a)
        in_degree[a] += 1

    queue = deque(i for i in range(num_courses) if in_degree[i] == 0)
    processed = 0

    while queue:
        node = queue.popleft()
        processed += 1
        for neighbor in adj[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    return processed == num_courses


# ============================================================
# APPROACH 3: BEST
# Iterative DFS using explicit stack — no recursion limit issues
# Time: O(V + E)  |  Space: O(V + E)
# ============================================================
def best(num_courses: int, prerequisites: List[List[int]]) -> bool:
    adj = [[] for _ in range(num_courses)]
    for a, b in prerequisites:
        adj[b].append(a)

    state = [0] * num_courses  # 0=unvisited, 1=in-stack, 2=done

    for start in range(num_courses):
        if state[start] != 0:
            continue
        # stack stores (node, neighbor_index)
        stack = [(start, 0)]
        state[start] = 1

        while stack:
            node, idx = stack[-1]
            neighbors = adj[node]
            if idx < len(neighbors):
                stack[-1] = (node, idx + 1)
                nxt = neighbors[idx]
                if state[nxt] == 1:
                    return False  # cycle detected
                if state[nxt] == 0:
                    state[nxt] = 1
                    stack.append((nxt, 0))
            else:
                state[node] = 2
                stack.pop()

    return True


if __name__ == "__main__":
    print("=== Possibility of Finishing All Courses ===")

    print(f"BruteForce [2, [[1,0]]]:     {brute_force(2, [[1,0]])}")         # True
    print(f"Optimal    [2, [[1,0]]]:     {optimal(2, [[1,0]])}")              # True
    print(f"Best       [2, [[1,0]]]:     {best(2, [[1,0]])}")                 # True

    print(f"BruteForce cycle:            {brute_force(2, [[1,0],[0,1]])}")    # False
    print(f"Optimal    cycle:            {optimal(2, [[1,0],[0,1]])}")         # False
    print(f"Best       cycle:            {best(2, [[1,0],[0,1]])}")            # False

    print(f"4 courses no cycle: {optimal(4, [[1,0],[2,0],[3,1],[3,2]])}")      # True
