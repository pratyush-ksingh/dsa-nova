"""
Problem: Disjoint Set Union Find
Difficulty: MEDIUM | XP: 25

Implement Union-Find with path compression and union by rank.
Near O(1) amortized per operation (O(alpha(n)) technically).
"""
from typing import List


# ============================================================
# APPROACH 1: NAIVE UNION-FIND (No Optimizations)
# Time: O(n) per find/union  |  Space: O(n)
# ============================================================
class NaiveDSU:
    """
    Simple parent-pointer forest. No optimizations.
    find() walks to root -- O(n) worst case (linked list tree).
    """
    def __init__(self, n: int):
        self.parent = list(range(n))

    def find(self, x: int) -> int:
        while self.parent[x] != x:
            x = self.parent[x]
        return x

    def union(self, x: int, y: int) -> None:
        root_x, root_y = self.find(x), self.find(y)
        if root_x != root_y:
            self.parent[root_x] = root_y  # Arbitrary attachment

    def connected(self, x: int, y: int) -> bool:
        return self.find(x) == self.find(y)


# ============================================================
# APPROACH 2: UNION BY RANK (Without Path Compression)
# Time: O(log n) per operation  |  Space: O(n)
# ============================================================
class RankDSU:
    """
    Union by rank keeps tree height O(log n).
    Still no path compression, so find walks O(log n) each time.
    """
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x: int) -> int:
        while self.parent[x] != x:
            x = self.parent[x]
        return x

    def union(self, x: int, y: int) -> bool:
        root_x, root_y = self.find(x), self.find(y)
        if root_x == root_y:
            return False

        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        return True

    def connected(self, x: int, y: int) -> bool:
        return self.find(x) == self.find(y)


# ============================================================
# APPROACH 3: PATH COMPRESSION + UNION BY RANK (BEST)
# Time: O(alpha(n)) amortized  |  Space: O(n)
# ============================================================
class OptimalDSU:
    """
    Path compression + union by rank = O(alpha(n)) amortized.
    alpha(n) <= 4 for all practical inputs (inverse Ackermann).
    """
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.components = n

    def find(self, x: int) -> int:
        """
        Find with PATH COMPRESSION.
        Every node on the path to root gets its parent set directly to root.
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # Recursive compression
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        """
        Union by RANK.
        Attach shorter tree under taller tree.
        Returns True if merge happened (different components).
        """
        root_x, root_y = self.find(x), self.find(y)
        if root_x == root_y:
            return False  # Already same set

        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1

        self.components -= 1
        return True

    def connected(self, x: int, y: int) -> bool:
        return self.find(x) == self.find(y)

    def get_components(self) -> int:
        return self.components


# ============================================================
# BONUS: UNION BY SIZE (Alternative to rank)
# Same amortized complexity, but tracks actual subtree sizes
# ============================================================
class SizeDSU:
    """
    Union by size: always merge smaller set into larger.
    size[] tracks actual element count (unlike rank which is height bound).
    """
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.size = [1] * n

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        root_x, root_y = self.find(x), self.find(y)
        if root_x == root_y:
            return False

        # Merge smaller into larger
        if self.size[root_x] < self.size[root_y]:
            self.parent[root_x] = root_y
            self.size[root_y] += self.size[root_x]
        else:
            self.parent[root_y] = root_x
            self.size[root_x] += self.size[root_y]
        return True

    def get_size(self, x: int) -> int:
        return self.size[self.find(x)]


if __name__ == "__main__":
    print("=== Disjoint Set Union Find ===\n")

    # --- Test Naive DSU ---
    print("--- Naive DSU ---")
    naive = NaiveDSU(5)
    naive.union(0, 1)
    naive.union(2, 3)
    print(f"0-1 connected: {naive.connected(0, 1)}")   # True
    print(f"0-2 connected: {naive.connected(0, 2)}")   # False
    naive.union(1, 3)
    print(f"0-3 connected: {naive.connected(0, 3)}")   # True
    print(f"0-4 connected: {naive.connected(0, 4)}")   # False

    # --- Test Optimal DSU ---
    print("\n--- Optimal DSU (Path Compression + Union by Rank) ---")
    dsu = OptimalDSU(7)
    print(f"Initial components: {dsu.get_components()}")  # 7

    dsu.union(0, 1)
    dsu.union(2, 3)
    dsu.union(4, 5)
    print(f"After 3 unions: {dsu.get_components()}")     # 4

    dsu.union(0, 2)
    print(f"After union(0,2): {dsu.get_components()}")   # 3

    dsu.union(3, 5)
    print(f"After union(3,5): {dsu.get_components()}")   # 2

    print(f"0-5 connected: {dsu.connected(0, 5)}")      # True
    print(f"0-6 connected: {dsu.connected(0, 6)}")      # False

    dsu.union(0, 6)
    print(f"After union(0,6): {dsu.get_components()}")   # 1
    print(f"All connected: {dsu.connected(3, 6)}")       # True

    # Verify path compression
    for i in range(7):
        dsu.find(i)
    print(f"Parents after compression: {dsu.parent}")

    # --- Test Size DSU ---
    print("\n--- Size DSU ---")
    size_dsu = SizeDSU(5)
    size_dsu.union(0, 1)
    size_dsu.union(2, 3)
    size_dsu.union(0, 2)
    print(f"Size of group containing 0: {size_dsu.get_size(0)}")  # 4
    print(f"Size of group containing 4: {size_dsu.get_size(4)}")  # 1

    # --- Redundant union test ---
    print("\n--- Redundant Union Test ---")
    dsu2 = OptimalDSU(3)
    print(f"union(0,1) merged: {dsu2.union(0, 1)}")   # True
    print(f"union(0,1) merged: {dsu2.union(0, 1)}")   # False (redundant)
    print(f"Components: {dsu2.get_components()}")       # 2
