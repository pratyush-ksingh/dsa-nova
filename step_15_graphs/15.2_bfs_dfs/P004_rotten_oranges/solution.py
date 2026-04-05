"""
Problem: Rotten Oranges (LeetCode #994)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import deque
import copy


DIRS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


# ============================================================
# APPROACH 1: BRUTE FORCE -- Repeated Grid Scans
# Time: O((m*n)^2)  |  Space: O(1)
# Each minute, scan entire grid to spread rot. Repeat until stable.
# ============================================================
def brute_force(grid: List[List[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    minutes = 0

    while True:
        changed = False
        # Mark fresh neighbors of rotten as "newly rotten" (3)
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 2:
                    for dr, dc in DIRS:
                        nr, nc = r + dr, c + dc
                        if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1:
                            grid[nr][nc] = 3
                            changed = True

        if not changed:
            break

        # Convert all 3s to 2s
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 3:
                    grid[r][c] = 2
        minutes += 1

    # Check for remaining fresh oranges
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                return -1
    return minutes


# ============================================================
# APPROACH 2: OPTIMAL -- Multi-Source BFS
# Time: O(m * n)  |  Space: O(m * n)
# All rotten oranges start BFS simultaneously. Levels = minutes.
# ============================================================
def optimal(grid: List[List[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    queue = deque()
    fresh = 0

    # Initialize: enqueue all rotten, count fresh
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 2:
                queue.append((r, c))
            elif grid[r][c] == 1:
                fresh += 1

    if fresh == 0:
        return 0

    minutes = 0

    # BFS level by level
    while queue:
        size = len(queue)
        rotted = False

        for _ in range(size):
            r, c = queue.popleft()
            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1:
                    grid[nr][nc] = 2
                    fresh -= 1
                    queue.append((nr, nc))
                    rotted = True

        if rotted:
            minutes += 1

    return minutes if fresh == 0 else -1


# ============================================================
# APPROACH 3: BEST -- Multi-Source BFS (Flat Index, Early Exit)
# Time: O(m * n)  |  Space: O(m * n)
# Same BFS but flat index reduces tuple overhead. Early exit at fresh=0.
# ============================================================
def best(grid: List[List[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    queue = deque()
    fresh = 0

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 2:
                queue.append(r * cols + c)  # flat index
            elif grid[r][c] == 1:
                fresh += 1

    if fresh == 0:
        return 0

    minutes = 0

    while queue and fresh > 0:
        size = len(queue)
        for _ in range(size):
            idx = queue.popleft()
            r, c = divmod(idx, cols)
            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1:
                    grid[nr][nc] = 2
                    fresh -= 1
                    queue.append(nr * cols + nc)
        minutes += 1

    return minutes if fresh == 0 else -1


if __name__ == "__main__":
    print("=== Rotten Oranges ===\n")

    grid1 = [[2, 1, 1], [1, 1, 0], [0, 1, 1]]
    print(f"Brute:   {brute_force(copy.deepcopy(grid1))}")    # 4
    print(f"Optimal: {optimal(copy.deepcopy(grid1))}")         # 4
    print(f"Best:    {best(copy.deepcopy(grid1))}")            # 4

    grid2 = [[2, 1, 1], [0, 1, 1], [1, 0, 1]]
    print(f"\nIsolated: {optimal(copy.deepcopy(grid2))}")      # -1

    grid3 = [[0, 2]]
    print(f"No fresh: {optimal(copy.deepcopy(grid3))}")        # 0

    grid4 = [[1]]
    print(f"Single fresh: {optimal(copy.deepcopy(grid4))}")    # -1
