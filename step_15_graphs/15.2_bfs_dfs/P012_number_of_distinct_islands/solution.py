"""
Problem: Number of Distinct Islands (LeetCode 694)
Difficulty: MEDIUM | XP: 25

Given a 2D binary grid (1=land, 0=water), count the number of distinct islands.
Two islands are considered the same if one can be translated (not rotated or
reflected) to match the other exactly.

Key insight: encode each island's shape as a path signature (sequence of DFS
direction moves + backtrack marks). Two islands with the same signature are the
same shape.
"""
from typing import List, Set, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE — Normalize coordinates per island
# Time: O(mn * k log k) where k is max island size  |  Space: O(mn)
# ============================================================
def num_distinct_islands_brute(grid: List[List[int]]) -> int:
    """
    Intuition: For each island, collect absolute (r, c) coordinates of all
    its cells. Normalize by subtracting the minimum row and column (translate
    to origin). Store the frozenset of translated coordinates in a set.
    Two islands with identical normalized coordinate sets are the same shape.
    """
    m, n = len(grid), len(grid[0])
    visited = [[False] * n for _ in range(m)]

    def dfs_collect(r: int, c: int, coords: List[Tuple[int, int]]) -> None:
        if r < 0 or r >= m or c < 0 or c >= n:
            return
        if visited[r][c] or grid[r][c] != 1:
            return
        visited[r][c] = True
        coords.append((r, c))
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            dfs_collect(r + dr, c + dc, coords)

    shapes: Set[frozenset] = set()

    for r in range(m):
        for c in range(n):
            if grid[r][c] == 1 and not visited[r][c]:
                coords: List[Tuple[int, int]] = []
                dfs_collect(r, c, coords)
                if coords:
                    # Normalize: translate so top-left is at (0,0)
                    min_r = min(x[0] for x in coords)
                    min_c = min(x[1] for x in coords)
                    normalized = frozenset((x[0] - min_r, x[1] - min_c) for x in coords)
                    shapes.add(normalized)

    return len(shapes)


# ============================================================
# APPROACH 2: OPTIMAL — DFS with direction encoding (path signature)
# Time: O(mn)  |  Space: O(mn)
# ============================================================
def num_distinct_islands_optimal(grid: List[List[int]]) -> int:
    """
    Intuition: Encode the island shape as the DFS traversal path string.
    When we enter a cell from direction d, append 'd' to the path.
    When we backtrack, append a special 'b' (backtrack) symbol.

    Two islands produce the same path string if and only if they have
    the same shape (under translation). The backtrack markers prevent
    ambiguous encodings where different shapes could produce the same
    sequence of forward steps.

    Directions: U=up, D=down, L=left, R=right, B=backtrack
    """
    m, n = len(grid), len(grid[0])
    visited = [[False] * n for _ in range(m)]
    dir_map = {(-1, 0): 'U', (1, 0): 'D', (0, -1): 'L', (0, 1): 'R'}

    def dfs(r: int, c: int, direction: str, path: List[str]) -> None:
        if r < 0 or r >= m or c < 0 or c >= n:
            return
        if visited[r][c] or grid[r][c] != 1:
            return
        visited[r][c] = True
        path.append(direction)
        for (dr, dc), sym in dir_map.items():
            dfs(r + dr, c + dc, sym, path)
        path.append('B')  # backtrack marker

    shapes: Set[str] = set()

    for r in range(m):
        for c in range(n):
            if grid[r][c] == 1 and not visited[r][c]:
                path: List[str] = []
                dfs(r, c, 'S', path)  # 'S' = start
                shapes.add(''.join(path))

    return len(shapes)


# ============================================================
# APPROACH 3: BEST — DFS with relative coordinate set (clean & fast)
# Time: O(mn)  |  Space: O(mn)
# ============================================================
def num_distinct_islands_best(grid: List[List[int]]) -> int:
    """
    Intuition: Similar to Approach 1 but without the overhead of sorting/
    frozenset creation from absolute coords. We record relative offsets from
    the island's first-visited cell (the DFS root). Store as frozenset of
    (delta_r, delta_c) tuples. Equivalent to Approach 1 but the relative
    offset is computed on the fly during DFS, avoiding a post-processing step.

    This avoids the backtrack-marker complexity of Approach 2 while still
    being O(mn) and avoiding sorting.
    """
    m, n = len(grid), len(grid[0])
    visited = [[False] * n for _ in range(m)]

    def dfs(r: int, c: int, r0: int, c0: int, offsets: List[Tuple[int, int]]) -> None:
        if r < 0 or r >= m or c < 0 or c >= n:
            return
        if visited[r][c] or grid[r][c] != 1:
            return
        visited[r][c] = True
        offsets.append((r - r0, c - c0))
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            dfs(r + dr, c + dc, r0, c0, offsets)

    shapes: Set[frozenset] = set()

    for r in range(m):
        for c in range(n):
            if grid[r][c] == 1 and not visited[r][c]:
                offsets: List[Tuple[int, int]] = []
                dfs(r, c, r, c, offsets)
                shapes.add(frozenset(offsets))

    return len(shapes)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Number of Distinct Islands ===\n")

    grid1 = [
        [1, 1, 0, 0, 0],
        [1, 1, 0, 0, 0],
        [0, 0, 0, 1, 1],
        [0, 0, 0, 1, 1],
    ]
    # Expected: 1 (both 2x2 squares are the same shape)

    grid2 = [
        [1, 1, 0, 1, 1],
        [1, 0, 0, 0, 1],
        [0, 0, 0, 0, 1],
        [1, 1, 0, 1, 1],
    ]
    # Expected: 3 (different shapes)

    for name, fn in [("Brute (normalize coords)", num_distinct_islands_brute),
                     ("Optimal (path encoding)", num_distinct_islands_optimal),
                     ("Best (relative offsets)", num_distinct_islands_best)]:
        print(f"grid1 {name:30s}: {fn([row[:] for row in grid1])}")
    print()
    for name, fn in [("Brute (normalize coords)", num_distinct_islands_brute),
                     ("Optimal (path encoding)", num_distinct_islands_optimal),
                     ("Best (relative offsets)", num_distinct_islands_best)]:
        print(f"grid2 {name:30s}: {fn([row[:] for row in grid2])}")
