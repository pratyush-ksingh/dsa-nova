"""
Problem: Rat in a Maze
Difficulty: HARD | XP: 50

Find all paths from (0,0) to (n-1,n-1) in an N×N grid.
Cell 1 = passable, 0 = blocked. Can move U/D/L/R.
Return sorted list of direction strings (e.g., "DDRR").
Real-life use: Pathfinding, maze solving, game AI, robot navigation.
"""
from typing import List
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE
# DFS with separate visited matrix.
# Time: O(4^(N^2))  |  Space: O(N^2) visited + O(N^2) path
# ============================================================
def brute_force(maze: List[List[int]]) -> List[str]:
    n = len(maze)
    if not maze or maze[0][0] == 0 or maze[n-1][n-1] == 0:
        return []

    result = []
    visited = [[False] * n for _ in range(n)]
    visited[0][0] = True

    def dfs(r: int, c: int, path: List[str]) -> None:
        if r == n - 1 and c == n - 1:
            result.append("".join(path))
            return
        for dr, dc, d in [(1,0,'D'),(-1,0,'U'),(0,-1,'L'),(0,1,'R')]:
            nr, nc = r + dr, c + dc
            if 0 <= nr < n and 0 <= nc < n and not visited[nr][nc] and maze[nr][nc] == 1:
                visited[nr][nc] = True
                path.append(d)
                dfs(nr, nc, path)
                path.pop()
                visited[nr][nc] = False

    dfs(0, 0, [])
    return sorted(result)


# ============================================================
# APPROACH 2: OPTIMAL
# In-place backtracking: mark cell as 0 to avoid revisit, restore on backtrack.
# Saves O(N^2) extra space vs separate visited array.
# Time: O(4^(N^2))  |  Space: O(N^2) path
# ============================================================
def optimal(maze: List[List[int]]) -> List[str]:
    maze = copy.deepcopy(maze)
    n = len(maze)
    if not maze or maze[0][0] == 0 or maze[n-1][n-1] == 0:
        return []

    result = []

    def dfs(r: int, c: int, path: List[str]) -> None:
        if r == n - 1 and c == n - 1:
            result.append("".join(path))
            return
        maze[r][c] = 0  # mark visited
        for dr, dc, d in [(1,0,'D'),(-1,0,'U'),(0,-1,'L'),(0,1,'R')]:
            nr, nc = r + dr, c + dc
            if 0 <= nr < n and 0 <= nc < n and maze[nr][nc] == 1:
                path.append(d)
                dfs(nr, nc, path)
                path.pop()
        maze[r][c] = 1  # restore

    dfs(0, 0, [])
    return sorted(result)


# ============================================================
# APPROACH 3: BEST
# In-place backtracking with lexicographic direction order (D,L,R,U)
# so results come out sorted naturally — no post-sort needed.
# Time: O(4^(N^2))  |  Space: O(N^2)
# ============================================================
def best(maze: List[List[int]]) -> List[str]:
    maze = copy.deepcopy(maze)
    n = len(maze)
    if not maze or maze[0][0] == 0 or maze[n-1][n-1] == 0:
        return []

    result = []
    # Directions in lexicographic order: D < L < R < U
    DIRS = [(1, 0, 'D'), (0, -1, 'L'), (0, 1, 'R'), (-1, 0, 'U')]

    def dfs(r: int, c: int, path: List[str]) -> None:
        if r == n - 1 and c == n - 1:
            result.append("".join(path))
            return
        maze[r][c] = 0
        for dr, dc, d in DIRS:
            nr, nc = r + dr, c + dc
            if 0 <= nr < n and 0 <= nc < n and maze[nr][nc] == 1:
                path.append(d)
                dfs(nr, nc, path)
                path.pop()
        maze[r][c] = 1

    dfs(0, 0, [])
    return result  # already sorted by direction order


if __name__ == "__main__":
    print("=== Rat in a Maze ===")

    maze1 = [[1,0,0,0],[1,1,0,1],[1,1,0,0],[0,1,1,1]]
    print("\n4x4 maze:")
    print(f"  Brute  : {brute_force(maze1)}")
    print(f"  Optimal: {optimal(maze1)}")
    print(f"  Best   : {best(maze1)}")

    maze2 = [[1,1],[1,1]]
    print("\n2x2 fully open:")
    print(f"  Best: {best(maze2)}")

    maze3 = [[1,0],[0,1]]
    print("\n2x2 blocked (no path):")
    print(f"  Best: {best(maze3)}")
