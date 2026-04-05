"""
Problem: M Coloring Problem
Difficulty: HARD | XP: 50

Given an undirected graph with V vertices and a list of edges,
determine if the graph can be colored with at most M colors such that
no two adjacent vertices share the same color.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Backtracking with adjacency matrix
# Time: O(M^V)  |  Space: O(V)
# Try each color for each vertex; backtrack when conflict found
# ============================================================
def brute_force(graph: List[List[int]], m: int, v: int) -> bool:
    colors = [0] * v

    def is_safe(node: int, color: int) -> bool:
        for nb in range(v):
            if graph[node][nb] == 1 and colors[nb] == color:
                return False
        return True

    def solve(node: int) -> bool:
        if node == v:
            return True
        for color in range(1, m + 1):
            if is_safe(node, color):
                colors[node] = color
                if solve(node + 1):
                    return True
                colors[node] = 0
        return False

    return solve(0)


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking with adjacency list
# Time: O(M^V)  |  Space: O(V + E)
# Adjacency list avoids scanning all V neighbors; O(degree) per node
# ============================================================
def optimal(adj: List[List[int]], m: int, v: int) -> bool:
    colors = [0] * v

    def is_safe(node: int, color: int) -> bool:
        return all(colors[nb] != color for nb in adj[node])

    def solve(node: int) -> bool:
        if node == v:
            return True
        for color in range(1, m + 1):
            if is_safe(node, color):
                colors[node] = color
                if solve(node + 1):
                    return True
                colors[node] = 0
        return False

    return solve(0)


# ============================================================
# APPROACH 3: BEST - Backtracking + bitmask forbidden-color tracking
# Time: O(M^V)  |  Space: O(V + E)
# Each node keeps a bitmask of colors used by its already-colored neighbors;
# checking and updating forbidden colors is O(degree) but very cache-friendly
# ============================================================
def best(adj: List[List[int]], m: int, v: int) -> bool:
    colors = [0] * v
    forbidden = [0] * v  # bitmask: bit c set means color c is taken by a neighbor

    def solve(node: int) -> bool:
        if node == v:
            return True
        for color in range(1, m + 1):
            if not (forbidden[node] & (1 << color)):
                colors[node] = color
                for nb in adj[node]:
                    forbidden[nb] |= (1 << color)
                if solve(node + 1):
                    return True
                for nb in adj[node]:
                    forbidden[nb] &= ~(1 << color)
                colors[node] = 0
        return False

    return solve(0)


def matrix_to_adj(graph: List[List[int]]) -> List[List[int]]:
    n = len(graph)
    return [[j for j in range(n) if graph[i][j] == 1] for i in range(n)]


if __name__ == "__main__":
    print("=== M Coloring Problem ===")

    # 4-node graph with edges: 0-1,0-2,0-3,1-2,2-3
    graph = [
        [0, 1, 1, 1],
        [1, 0, 1, 0],
        [1, 1, 0, 1],
        [1, 0, 1, 0]
    ]
    v = 4
    adj = matrix_to_adj(graph)

    print("4-node graph:")
    print(f"  M=3 -> Brute:{brute_force(graph,3,v)}  Optimal:{optimal(adj,3,v)}  Best:{best(adj,3,v)}")
    print(f"  M=2 -> Brute:{brute_force(graph,2,v)}  Optimal:{optimal(adj,2,v)}  Best:{best(adj,2,v)}")

    # Complete graph K4: needs exactly 4 colors
    k4 = [[0,1,1,1],[1,0,1,1],[1,1,0,1],[1,1,1,0]]
    k4_adj = matrix_to_adj(k4)
    print("\nK4 (complete graph):")
    print(f"  M=3 -> {brute_force(k4,3,4)}  |  M=4 -> {brute_force(k4,4,4)}")
