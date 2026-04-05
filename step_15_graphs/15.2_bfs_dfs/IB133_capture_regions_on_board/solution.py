"""
Problem: Capture Regions on Board
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a 2D board with 'X' and 'O', capture all 'O' regions not connected to the border.
Surrounded regions (not touching border) become 'X'.
"""

from typing import List
from collections import deque
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE
# DFS from border O's to mark safe cells, then flip the rest
# Time: O(M*N)  |  Space: O(M*N) recursion stack
# ============================================================
def brute_force(board: List[List[str]]) -> None:
    if not board or not board[0]:
        return
    rows, cols = len(board), len(board[0])

    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or board[r][c] != 'O':
            return
        board[r][c] = 'S'  # safe
        dfs(r + 1, c)
        dfs(r - 1, c)
        dfs(r, c + 1)
        dfs(r, c - 1)

    # Mark safe O's connected to border
    for r in range(rows):
        dfs(r, 0)
        dfs(r, cols - 1)
    for c in range(cols):
        dfs(0, c)
        dfs(rows - 1, c)

    # Flip
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O':
                board[r][c] = 'X'
            elif board[r][c] == 'S':
                board[r][c] = 'O'


# ============================================================
# APPROACH 2: OPTIMAL
# BFS from border O's — iterative, avoids recursion limit
# Time: O(M*N)  |  Space: O(M*N)
# ============================================================
def optimal(board: List[List[str]]) -> None:
    if not board or not board[0]:
        return
    rows, cols = len(board), len(board[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
    queue = deque()

    # Seed with border O's
    for r in range(rows):
        for c in [0, cols - 1]:
            if board[r][c] == 'O':
                board[r][c] = 'S'
                queue.append((r, c))
    for c in range(cols):
        for r in [0, rows - 1]:
            if board[r][c] == 'O':
                board[r][c] = 'S'
                queue.append((r, c))

    # BFS
    while queue:
        r, c = queue.popleft()
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] == 'O':
                board[nr][nc] = 'S'
                queue.append((nr, nc))

    # Flip
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O':
                board[r][c] = 'X'
            elif board[r][c] == 'S':
                board[r][c] = 'O'


# ============================================================
# APPROACH 3: BEST
# Union-Find — connect border O's to dummy node; safe = same component as dummy
# Time: O(M*N * alpha)  |  Space: O(M*N)
# ============================================================
def best(board: List[List[str]]) -> None:
    if not board or not board[0]:
        return
    rows, cols = len(board), len(board[0])
    dummy = rows * cols
    parent = list(range(dummy + 1))

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(x, y):
        px, py = find(x), find(y)
        if px != py:
            parent[px] = py

    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O':
                idx = r * cols + c
                if r == 0 or r == rows - 1 or c == 0 or c == cols - 1:
                    union(idx, dummy)
                for dr, dc in directions:
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] == 'O':
                        union(idx, nr * cols + nc)

    dummy_root = find(dummy)
    for r in range(rows):
        for c in range(cols):
            if board[r][c] == 'O' and find(r * cols + c) != dummy_root:
                board[r][c] = 'X'


def make_board():
    return [
        ['X', 'X', 'X', 'X'],
        ['X', 'O', 'O', 'X'],
        ['X', 'X', 'O', 'X'],
        ['X', 'O', 'X', 'X']
    ]


if __name__ == "__main__":
    print("=== Capture Regions on Board ===")

    b1 = make_board()
    print("Before:", b1)
    brute_force(b1)
    print("BruteForce After:", b1)

    b2 = make_board()
    optimal(b2)
    print("Optimal After:", b2)

    b3 = make_board()
    best(b3)
    print("Best After:", b3)

    # Border O should NOT be captured
    b4 = [['O', 'X'], ['X', 'O']]
    optimal(b4)
    print("Border O's preserved:", b4)  # [['O','X'],['X','O']]
