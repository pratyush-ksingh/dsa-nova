"""
Problem: Surrounded Regions (LeetCode #130)
Difficulty: MEDIUM | XP: 25

Given an m x n board of 'X' and 'O', capture all regions of 'O' that are
completely surrounded by 'X'. A region is captured by flipping all 'O's
into 'X's. Any 'O' on the border or connected to a border 'O' is safe.
"""
from typing import List
from collections import deque
import copy


DIRS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS from every 'O', check border touch
# Time: O((m*n)^2)  |  Space: O(m*n)
# For each 'O', flood-fill its connected component and check if
# any cell in the component touches the border. If not, flip all.
# Processes every O-cell independently => heavily redundant.
# ============================================================
def brute_force(board: List[List[str]]) -> None:
    rows, cols = len(board), len(board[0])

    def touches_border(r: int, c: int) -> bool:
        """BFS from (r, c); returns True if the component reaches a border cell."""
        queue = deque([(r, c)])
        visited = {(r, c)}
        component = [(r, c)]
        safe = (r == 0 or r == rows - 1 or c == 0 or c == cols - 1)

        while queue:
            cr, cc = queue.popleft()
            for dr, dc in DIRS:
                nr, nc = cr + dr, cc + dc
                if 0 <= nr < rows and 0 <= nc < cols and (nr, nc) not in visited and board[nr][nc] == 'O':
                    visited.add((nr, nc))
                    component.append((nr, nc))
                    queue.append((nr, nc))
                    if nr == 0 or nr == rows - 1 or nc == 0 or nc == cols - 1:
                        safe = True
        return safe, component

    processed = set()
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O' and (r, c) not in processed:
                safe, component = touches_border(r, c)
                for cr, cc in component:
                    processed.add((cr, cc))
                if not safe:
                    for cr, cc in component:
                        board[cr][cc] = 'X'


# ============================================================
# APPROACH 2: OPTIMAL -- BFS/DFS from border O's, mark safe, flip rest
# Time: O(m*n)  |  Space: O(m*n)
# Any 'O' connected to a border 'O' is safe. Start BFS/DFS from all
# border 'O's, marking them safe. Then a single scan flips remaining 'O's.
# ============================================================
def optimal(board: List[List[str]]) -> None:
    rows, cols = len(board), len(board[0])

    def bfs_safe(sr: int, sc: int) -> None:
        """Mark all O's reachable from (sr, sc) as safe (#)."""
        queue = deque([(sr, sc)])
        board[sr][sc] = '#'
        while queue:
            r, c = queue.popleft()
            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] == 'O':
                    board[nr][nc] = '#'
                    queue.append((nr, nc))

    # Step 1: Mark all border-connected O's as '#' (safe)
    for r in range(rows):
        for c in range(cols):
            if (r == 0 or r == rows - 1 or c == 0 or c == cols - 1) and board[r][c] == 'O':
                bfs_safe(r, c)

    # Step 2: Flip remaining O's to X (they are surrounded), restore # to O
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O':
                board[r][c] = 'X'
            elif board[r][c] == '#':
                board[r][c] = 'O'


# ============================================================
# APPROACH 3: BEST -- Same border-first DFS (stack-based, no recursion limit)
# Time: O(m*n)  |  Space: O(m*n)
# Identical approach to Approach 2 but uses an explicit stack instead of
# deque/recursion, which avoids Python recursion limit on large grids.
# ============================================================
def best(board: List[List[str]]) -> None:
    rows, cols = len(board), len(board[0])

    def dfs_safe(sr: int, sc: int) -> None:
        """Iterative DFS to mark border-connected O's as safe."""
        stack = [(sr, sc)]
        board[sr][sc] = '#'
        while stack:
            r, c = stack.pop()
            for dr, dc in DIRS:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] == 'O':
                    board[nr][nc] = '#'
                    stack.append((nr, nc))

    # Mark all border-connected O's
    for r in range(rows):
        for c in [0, cols - 1]:
            if board[r][c] == 'O':
                dfs_safe(r, c)
    for c in range(cols):
        for r in [0, rows - 1]:
            if board[r][c] == 'O':
                dfs_safe(r, c)

    # Flip captured O's, restore safe O's
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O':
                board[r][c] = 'X'
            elif board[r][c] == '#':
                board[r][c] = 'O'


def board_str(board: List[List[str]]) -> str:
    return '\n'.join(' '.join(row) for row in board)


if __name__ == "__main__":
    print("=== Surrounded Regions ===\n")

    b1 = [["X","X","X","X"],
          ["X","O","O","X"],
          ["X","X","O","X"],
          ["X","O","X","X"]]
    b2 = copy.deepcopy(b1)
    b3 = copy.deepcopy(b1)

    brute_force(b1)
    print("Brute:\n" + board_str(b1))
    # Expected: all O's except (3,1) flipped to X; (3,1) connects to border

    optimal(b2)
    print("\nOptimal:\n" + board_str(b2))

    best(b3)
    print("\nBest:\n" + board_str(b3))

    print()
    b4 = [["O"]]
    optimal(b4)
    print("Single O (border, safe):", board_str(b4))  # O
