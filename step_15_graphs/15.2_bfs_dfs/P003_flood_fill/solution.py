"""
Problem: Flood Fill (LeetCode #733)
Difficulty: EASY | XP: 10

Given image (2D grid), starting pixel, and new color, flood fill.
"""
from typing import List
from collections import deque
import copy


# ============================================================
# APPROACH 1: DFS (Recursive)
# Time: O(m * n) | Space: O(m * n) recursion stack
# ============================================================
def flood_fill_dfs(image: List[List[int]], sr: int, sc: int, color: int) -> List[List[int]]:
    """DFS flood fill. Coloring the pixel serves as the visited marker."""
    original_color = image[sr][sc]
    if original_color == color:
        return image  # crucial: avoids infinite recursion

    m, n = len(image), len(image[0])

    def dfs(r: int, c: int):
        if r < 0 or r >= m or c < 0 or c >= n:
            return
        if image[r][c] != original_color:
            return

        image[r][c] = color  # fill (also marks visited)
        dfs(r + 1, c)  # down
        dfs(r - 1, c)  # up
        dfs(r, c + 1)  # right
        dfs(r, c - 1)  # left

    dfs(sr, sc)
    return image


# ============================================================
# APPROACH 2: BFS (Iterative)
# Time: O(m * n) | Space: O(m * n) queue
# ============================================================
def flood_fill_bfs(image: List[List[int]], sr: int, sc: int, color: int) -> List[List[int]]:
    """BFS flood fill. Better for very large grids (no stack overflow)."""
    original_color = image[sr][sc]
    if original_color == color:
        return image

    m, n = len(image), len(image[0])
    directions = [(1, 0), (-1, 0), (0, 1), (0, -1)]

    queue = deque([(sr, sc)])
    image[sr][sc] = color

    while queue:
        r, c = queue.popleft()
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and image[nr][nc] == original_color:
                image[nr][nc] = color
                queue.append((nr, nc))

    return image


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Flood Fill ===\n")

    test_cases = [
        {
            "image": [[1,1,1],[1,1,0],[1,0,1]],
            "sr": 1, "sc": 1, "color": 2,
            "expected": [[2,2,2],[2,2,0],[2,0,1]],
            "desc": "Standard case"
        },
        {
            "image": [[0,0,0],[0,0,0]],
            "sr": 0, "sc": 0, "color": 0,
            "expected": [[0,0,0],[0,0,0]],
            "desc": "Same color (no change)"
        },
        {
            "image": [[5]],
            "sr": 0, "sc": 0, "color": 3,
            "expected": [[3]],
            "desc": "1x1 grid"
        },
        {
            "image": [[0,1,0],[1,1,1],[0,1,0]],
            "sr": 1, "sc": 1, "color": 3,
            "expected": [[0,3,0],[3,3,3],[0,3,0]],
            "desc": "Cross pattern"
        },
    ]

    for tc in test_cases:
        img_dfs = copy.deepcopy(tc["image"])
        img_bfs = copy.deepcopy(tc["image"])

        result_dfs = flood_fill_dfs(img_dfs, tc["sr"], tc["sc"], tc["color"])
        result_bfs = flood_fill_bfs(img_bfs, tc["sr"], tc["sc"], tc["color"])

        dfs_pass = result_dfs == tc["expected"]
        bfs_pass = result_bfs == tc["expected"]
        status = "PASS" if dfs_pass and bfs_pass else "FAIL"

        print(f"{tc['desc']}:")
        print(f"  Input:    {tc['image']}")
        print(f"  DFS:      {result_dfs}")
        print(f"  BFS:      {result_bfs}")
        print(f"  Expected: {tc['expected']}  [{status}]\n")
