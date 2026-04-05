"""
Problem: Increasing Path in Matrix
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Find the length of the longest strictly increasing path in a matrix.
Can move in 4 directions (up/down/left/right). No cycles since path is strictly increasing.
"""

from typing import List
from functools import lru_cache
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE
# DFS with memoization from each cell
# Time: O(M*N)  |  Space: O(M*N)
# ============================================================
def brute_force(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
    memo = {}

    def dfs(r, c):
        if (r, c) in memo:
            return memo[(r, c)]
        best = 1
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] > matrix[r][c]:
                best = max(best, 1 + dfs(nr, nc))
        memo[(r, c)] = best
        return best

    return max(dfs(r, c) for r in range(rows) for c in range(cols))


# ============================================================
# APPROACH 2: OPTIMAL
# Topological sort (BFS from local maxima)
# outdegree = # neighbors with strictly larger value
# Process from cells with outdegree 0 (no increasing neighbors)
# Time: O(M*N)  |  Space: O(M*N)
# ============================================================
def optimal(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    outdegree = [[0] * cols for _ in range(rows)]
    for r in range(rows):
        for c in range(cols):
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] > matrix[r][c]:
                    outdegree[r][c] += 1

    queue = deque((r, c) for r in range(rows) for c in range(cols) if outdegree[r][c] == 0)
    length = 0

    while queue:
        length += 1
        for _ in range(len(queue)):
            r, c = queue.popleft()
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] < matrix[r][c]:
                    outdegree[nr][nc] -= 1
                    if outdegree[nr][nc] == 0:
                        queue.append((nr, nc))

    return length


# ============================================================
# APPROACH 3: BEST
# Sort cells by value descending; process and fill dp in one pass
# dp[r][c] = length of longest path starting from (r, c)
# Since we process larger values first, neighbors are already computed
# Time: O(M*N log(M*N))  |  Space: O(M*N)
# ============================================================
def best(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    # Sort all cells by value descending
    cells = sorted(
        ((r, c) for r in range(rows) for c in range(cols)),
        key=lambda x: matrix[x[0]][x[1]],
        reverse=True
    )

    dp = [[0] * cols for _ in range(rows)]
    ans = 0

    for r, c in cells:
        dp[r][c] = 1
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] > matrix[r][c]:
                dp[r][c] = max(dp[r][c], 1 + dp[nr][nc])
        ans = max(ans, dp[r][c])

    return ans


if __name__ == "__main__":
    print("=== Increasing Path in Matrix ===")

    m1 = [[9, 9, 4], [6, 6, 8], [2, 1, 1]]
    print(f"BruteForce m1: {brute_force(m1)}")  # 4: 1->2->6->9
    print(f"Optimal    m1: {optimal(m1)}")       # 4
    print(f"Best       m1: {best(m1)}")          # 4

    m2 = [[3, 4, 5], [3, 2, 6], [2, 2, 1]]
    print(f"BruteForce m2: {brute_force(m2)}")  # 4
    print(f"Optimal    m2: {optimal(m2)}")       # 4
    print(f"Best       m2: {best(m2)}")          # 4

    m3 = [[1]]
    print(f"Single: {optimal(m3)}")  # 1
