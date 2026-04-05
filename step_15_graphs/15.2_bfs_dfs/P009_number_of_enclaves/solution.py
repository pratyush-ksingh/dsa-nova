"""
Problem: Number of Enclaves (LeetCode 1020)
Difficulty: MEDIUM | XP: 25

Given a 2D grid of 0s (water) and 1s (land), return the number of land cells
from which you CANNOT walk off the boundary of the grid in any number of moves.

A cell is reachable from the boundary if there exists a 4-directional path of
land cells connecting it to any boundary land cell.
"""
from typing import List, Optional
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS/DFS from every unvisited land cell
# Time: O((mn)^2) worst case  |  Space: O(mn)
# ============================================================
def num_enclaves_brute(grid: List[List[int]]) -> int:
    """
    Intuition: For each unvisited land cell, do a BFS to find its connected
    component. Check if any cell in the component touches the boundary. If none
    does, add the component size to the answer.

    Inefficient because in the worst case we re-explore cells many times (each
    BFS rediscovers cells already explored by prior calls).
    """
    m, n = len(grid), len(grid[0])
    visited = [[False] * n for _ in range(m)]

    def bfs(sr: int, sc: int):
        """Returns (size, touches_boundary)."""
        queue = deque([(sr, sc)])
        visited[sr][sc] = True
        size = 0
        on_boundary = False
        while queue:
            r, c = queue.popleft()
            size += 1
            if r == 0 or r == m - 1 or c == 0 or c == n - 1:
                on_boundary = True
            for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and not visited[nr][nc] and grid[nr][nc] == 1:
                    visited[nr][nc] = True
                    queue.append((nr, nc))
        return size, on_boundary

    ans = 0
    for r in range(m):
        for c in range(n):
            if grid[r][c] == 1 and not visited[r][c]:
                size, on_boundary = bfs(r, c)
                if not on_boundary:
                    ans += size
    return ans


# ============================================================
# APPROACH 2: OPTIMAL — Multi-source BFS from boundary 1s
# Time: O(mn)  |  Space: O(mn)
# ============================================================
def num_enclaves_optimal(grid: List[List[int]]) -> int:
    """
    Intuition: Any land cell reachable from the boundary is NOT an enclave.
    Flood-fill (BFS) from all boundary land cells simultaneously, marking
    visited cells. Whatever land cells remain unvisited are enclaves — count them.

    This is the "reverse thinking" trick: instead of checking every cell for
    boundary reachability, we flood outward FROM the boundary and count leftovers.
    """
    m, n = len(grid), len(grid[0])
    visited = [[False] * n for _ in range(m)]
    queue = deque()

    # Seed the queue with all boundary land cells
    for r in range(m):
        for c in range(n):
            if (r == 0 or r == m - 1 or c == 0 or c == n - 1) and grid[r][c] == 1:
                visited[r][c] = True
                queue.append((r, c))

    # BFS to mark all land cells reachable from boundary
    while queue:
        r, c = queue.popleft()
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and not visited[nr][nc] and grid[nr][nc] == 1:
                visited[nr][nc] = True
                queue.append((nr, nc))

    # Count unvisited land cells — these are enclaves
    return sum(
        grid[r][c] == 1 and not visited[r][c]
        for r in range(m)
        for c in range(n)
    )


# ============================================================
# APPROACH 3: BEST — Multi-source DFS from boundary (in-place marking)
# Time: O(mn)  |  Space: O(mn) stack (O(1) extra if in-place)
# ============================================================
def num_enclaves_best(grid: List[List[int]]) -> int:
    """
    Intuition: Same boundary-flood idea as Approach 2, but uses DFS and
    modifies the grid in-place (sets boundary-reachable cells to 0) so no
    separate visited array is needed.

    After the DFS, simply count remaining 1s — they are all enclaves.
    Restore is not required since the problem doesn't ask us to preserve input.
    """
    m, n = len(grid), len(grid[0])

    def dfs(r: int, c: int) -> None:
        if r < 0 or r >= m or c < 0 or c >= n or grid[r][c] != 1:
            return
        grid[r][c] = 0  # mark as visited / flooded
        dfs(r - 1, c)
        dfs(r + 1, c)
        dfs(r, c - 1)
        dfs(r, c + 1)

    # Flood from all boundary land cells
    for r in range(m):
        if grid[r][0] == 1:  dfs(r, 0)
        if grid[r][n-1] == 1: dfs(r, n - 1)
    for c in range(n):
        if grid[0][c] == 1:  dfs(0, c)
        if grid[m-1][c] == 1: dfs(m - 1, c)

    # Remaining 1s are enclaves
    return sum(grid[r][c] for r in range(m) for c in range(n))


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Number of Enclaves ===\n")

    grid1 = [
        [0, 0, 0, 0],
        [1, 0, 1, 0],
        [0, 1, 1, 0],
        [0, 0, 0, 0],
    ]
    # Expected: 3  (the 3 inland land cells at (1,2),(2,1),(2,2))

    import copy
    for name, fn in [("Brute", num_enclaves_brute),
                     ("Optimal (BFS)", num_enclaves_optimal),
                     ("Best (DFS in-place)", num_enclaves_best)]:
        print(f"{name:25s}: {fn(copy.deepcopy(grid1))}")

    grid2 = [
        [0, 1, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 0],
    ]
    # Expected: 0  (all land cells touch the boundary)
    print()
    for name, fn in [("Brute", num_enclaves_brute),
                     ("Optimal (BFS)", num_enclaves_optimal),
                     ("Best (DFS in-place)", num_enclaves_best)]:
        print(f"grid2 {name:20s}: {fn(copy.deepcopy(grid2))}")
