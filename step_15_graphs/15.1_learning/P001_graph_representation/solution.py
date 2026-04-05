"""
Problem: Graph Representation
Difficulty: EASY | XP: 10

Implement graph using adjacency matrix and adjacency list.
Convert between them.
"""
from typing import List, Dict, Set


# ============================================================
# APPROACH 1: ADJACENCY MATRIX
# Build: O(V^2 + E) | Space: O(V^2)
# Edge lookup: O(1) | Neighbor scan: O(V)
# ============================================================
class AdjacencyMatrix:
    def __init__(self, n: int, edges: List[List[int]]):
        self.n = n
        self.matrix = [[0] * n for _ in range(n)]
        for u, v in edges:
            self.matrix[u][v] = 1
            self.matrix[v][u] = 1  # undirected

    def has_edge(self, u: int, v: int) -> bool:
        return self.matrix[u][v] == 1

    def get_neighbors(self, u: int) -> List[int]:
        return [v for v in range(self.n) if self.matrix[u][v] == 1]

    def to_adj_list(self) -> List[List[int]]:
        """Convert adjacency matrix to adjacency list."""
        adj = []
        for i in range(self.n):
            adj.append([j for j in range(self.n) if self.matrix[i][j] == 1])
        return adj

    def __repr__(self):
        lines = ["Adjacency Matrix:"]
        for row in self.matrix:
            lines.append(f"  {row}")
        return "\n".join(lines)


# ============================================================
# APPROACH 2: ADJACENCY LIST (Preferred for most problems)
# Build: O(V + E) | Space: O(V + E)
# Edge lookup: O(degree) | Neighbor scan: O(degree)
# ============================================================
class AdjacencyListGraph:
    def __init__(self, n: int, edges: List[List[int]]):
        self.n = n
        self.adj = [[] for _ in range(n)]
        for u, v in edges:
            self.adj[u].append(v)
            self.adj[v].append(u)  # undirected

    def has_edge(self, u: int, v: int) -> bool:
        return v in self.adj[u]

    def get_neighbors(self, u: int) -> List[int]:
        return self.adj[u]

    def to_matrix(self) -> List[List[int]]:
        """Convert adjacency list to adjacency matrix."""
        matrix = [[0] * self.n for _ in range(self.n)]
        for u in range(self.n):
            for v in self.adj[u]:
                matrix[u][v] = 1
        return matrix

    def __repr__(self):
        lines = ["Adjacency List:"]
        for i in range(self.n):
            lines.append(f"  {i} -> {self.adj[i]}")
        return "\n".join(lines)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Graph Representation ===\n")

    # Test 1: Linear graph 0-1-2-3
    edges1 = [[0, 1], [1, 2], [2, 3]]
    print("--- Test 1: Linear graph (n=4) ---")
    mat1 = AdjacencyMatrix(4, edges1)
    lst1 = AdjacencyListGraph(4, edges1)
    print(mat1)
    print(lst1)
    print(f"Edge (1,2)? Matrix: {mat1.has_edge(1,2)} | List: {lst1.has_edge(1,2)}")
    print(f"Edge (0,3)? Matrix: {mat1.has_edge(0,3)} | List: {lst1.has_edge(0,3)}")
    print(f"Neighbors of 1: {lst1.get_neighbors(1)}")
    print()

    # Test 2: Complete graph K3
    edges2 = [[0, 1], [0, 2], [1, 2]]
    print("--- Test 2: Complete graph K3 ---")
    mat2 = AdjacencyMatrix(3, edges2)
    lst2 = AdjacencyListGraph(3, edges2)
    print(mat2)
    print(lst2)
    print()

    # Test 3: No edges
    print("--- Test 3: No edges (n=3) ---")
    lst3 = AdjacencyListGraph(3, [])
    print(lst3)
    print()

    # Test 4: Conversions
    print("--- Test 4: Conversions ---")
    print("Matrix -> List:", mat1.to_adj_list())
    print("List -> Matrix:", lst1.to_matrix())
    print()

    # Test 5: Single node
    print("--- Test 5: Single node ---")
    lst5 = AdjacencyListGraph(1, [])
    print(lst5)
    print(f"Neighbors of 0: {lst5.get_neighbors(0)}")

    print("\nAll tests completed.")
