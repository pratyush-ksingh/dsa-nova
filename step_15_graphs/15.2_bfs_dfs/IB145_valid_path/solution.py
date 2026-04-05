"""
Problem: Valid Path
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an NxN grid with circular obstacles defined by (x, y, radius),
determine if there is a valid path from top-left (0,0) to
bottom-right (N-1, N-1) without passing through any obstacle.
"""
from typing import List, Optional
from collections import deque
import math


def _is_safe(x, y, n, circles):
    """Check if point (x, y) is within grid and not inside any circle."""
    if x < 0 or x >= n or y < 0 or y >= n:
        return False
    for cx, cy, r in circles:
        if (x - cx) ** 2 + (y - cy) ** 2 <= r ** 2:
            return False
    return True


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS on grid
# Time: O(N^2 * C) where C = number of circles  |  Space: O(N^2)
# ============================================================
def brute_force(n: int, circles: List[List[int]]) -> str:
    """
    BFS from (0,0) to (N-1, N-1) on the NxN grid.
    A cell is blocked if it falls inside any circle obstacle.
    Move in 8 directions (including diagonals).
    Returns "YES" or "NO".
    """
    if not _is_safe(0, 0, n, circles) or not _is_safe(n - 1, n - 1, n, circles):
        return "NO"

    visited = [[False] * n for _ in range(n)]
    queue = deque([(0, 0)])
    visited[0][0] = True
    dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    while queue:
        x, y = queue.popleft()
        if x == n - 1 and y == n - 1:
            return "YES"
        for dx, dy in dirs:
            nx, ny = x + dx, y + dy
            if 0 <= nx < n and 0 <= ny < n and not visited[nx][ny] and _is_safe(nx, ny, n, circles):
                visited[nx][ny] = True
                queue.append((nx, ny))
    return "NO"


# ============================================================
# APPROACH 2: OPTIMAL — DFS on grid with early termination
# Time: O(N^2 * C)  |  Space: O(N^2)
# ============================================================
def optimal(n: int, circles: List[List[int]]) -> str:
    """
    Iterative DFS from (0,0) to (N-1, N-1).
    """
    if not _is_safe(0, 0, n, circles) or not _is_safe(n - 1, n - 1, n, circles):
        return "NO"

    visited = [[False] * n for _ in range(n)]
    stack = [(0, 0)]
    visited[0][0] = True
    dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    while stack:
        x, y = stack.pop()
        if x == n - 1 and y == n - 1:
            return "YES"
        for dx, dy in dirs:
            nx, ny = x + dx, y + dy
            if 0 <= nx < n and 0 <= ny < n and not visited[nx][ny] and _is_safe(nx, ny, n, circles):
                visited[nx][ny] = True
                stack.append((nx, ny))
    return "NO"


# ============================================================
# APPROACH 3: BEST — Precompute blocked cells + BFS
# Time: O(N^2 + C*R^2)  |  Space: O(N^2)
# ============================================================
def best(n: int, circles: List[List[int]]) -> str:
    """
    Precompute a blocked grid so we don't re-check circles at every step.
    Then BFS on the precomputed grid.
    """
    blocked = [[False] * n for _ in range(n)]
    for cx, cy, r in circles:
        # Only iterate over bounding box of the circle
        for x in range(max(0, cx - r), min(n, cx + r + 1)):
            for y in range(max(0, cy - r), min(n, cy + r + 1)):
                if (x - cx) ** 2 + (y - cy) ** 2 <= r ** 2:
                    blocked[x][y] = True

    if blocked[0][0] or blocked[n - 1][n - 1]:
        return "NO"

    visited = [[False] * n for _ in range(n)]
    queue = deque([(0, 0)])
    visited[0][0] = True
    dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

    while queue:
        x, y = queue.popleft()
        if x == n - 1 and y == n - 1:
            return "YES"
        for dx, dy in dirs:
            nx, ny = x + dx, y + dy
            if 0 <= nx < n and 0 <= ny < n and not visited[nx][ny] and not blocked[nx][ny]:
                visited[nx][ny] = True
                queue.append((nx, ny))
    return "NO"


if __name__ == "__main__":
    print("=== Valid Path ===")
    # 5x5 grid, one obstacle at center
    n = 5
    circles = [[2, 2, 1]]
    print(f"Brute:   {brute_force(n, circles)}")
    print(f"Optimal: {optimal(n, circles)}")
    print(f"Best:    {best(n, circles)}")

    # Blocked path
    n = 3
    circles = [[1, 1, 2]]
    print(f"Brute:   {brute_force(n, circles)}")
    print(f"Optimal: {optimal(n, circles)}")
    print(f"Best:    {best(n, circles)}")
