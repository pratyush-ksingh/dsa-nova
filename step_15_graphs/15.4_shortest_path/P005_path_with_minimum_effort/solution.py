"""
Problem: Path with Minimum Effort (LeetCode #1631)
Difficulty: MEDIUM | XP: 25

You are given a rows x columns grid of heights. The effort of a path is the
maximum absolute difference in heights between two consecutive cells of the
path. Return the minimum effort required to travel from the top-left cell
(0,0) to the bottom-right cell (rows-1, columns-1).
You can move up, down, left, or right.
"""
from typing import List
from collections import deque
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE (DFS trying all paths)
# Time: O(4^(m*n)) worst case  |  Space: O(m * n)
# ============================================================
def minimumEffortPath_brute(heights: List[List[int]]) -> int:
    """
    DFS with backtracking: try all paths from (0,0) to (m-1,n-1),
    tracking the max height difference along each path.
    Keep a global minimum of the max differences across all paths.
    Optimization: prune paths where current effort already exceeds
    the best known result.
    """
    rows, cols = len(heights), len(heights[0])
    result = [float('inf')]
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    def dfs(r: int, c: int, max_effort: int, visited: set):
        if r == rows - 1 and c == cols - 1:
            result[0] = min(result[0], max_effort)
            return
        if max_effort >= result[0]:
            return  # prune

        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and (nr, nc) not in visited:
                effort = abs(heights[nr][nc] - heights[r][c])
                new_max = max(max_effort, effort)
                if new_max < result[0]:  # prune
                    visited.add((nr, nc))
                    dfs(nr, nc, new_max, visited)
                    visited.remove((nr, nc))

    visited = {(0, 0)}
    dfs(0, 0, 0, visited)
    return result[0]


# ============================================================
# APPROACH 2: OPTIMAL (Dijkstra with priority queue)
# Time: O(m * n * log(m * n))  |  Space: O(m * n)
# ============================================================
def minimumEffortPath_dijkstra(heights: List[List[int]]) -> int:
    """
    Modified Dijkstra: instead of summing weights, take the max
    height difference along the path as the "distance".

    Key insight: this is a shortest path problem where the "distance"
    metric is the maximum edge weight along the path (minimax path).
    Dijkstra works because the priority queue always processes the
    path with the smallest max-effort first.

    dist[r][c] = minimum possible max-effort to reach (r,c).
    """
    rows, cols = len(heights), len(heights[0])
    if rows == 1 and cols == 1:
        return 0

    dist = [[float('inf')] * cols for _ in range(rows)]
    dist[0][0] = 0

    # Min-heap: (effort, row, col)
    heap = [(0, 0, 0)]
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    while heap:
        effort, r, c = heapq.heappop(heap)

        if r == rows - 1 and c == cols - 1:
            return effort

        if effort > dist[r][c]:
            continue  # outdated entry

        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                new_effort = max(effort, abs(heights[nr][nc] - heights[r][c]))
                if new_effort < dist[nr][nc]:
                    dist[nr][nc] = new_effort
                    heapq.heappush(heap, (new_effort, nr, nc))

    return dist[rows - 1][cols - 1]


# ============================================================
# APPROACH 3: BEST (Binary search + BFS feasibility check)
# Time: O(m * n * log(max_height))  |  Space: O(m * n)
# ============================================================
def minimumEffortPath_binary_search(heights: List[List[int]]) -> int:
    """
    Binary search on the answer: if we can reach (m-1,n-1) with
    max effort k, we can also reach it with max effort k+1.
    This monotonic property allows binary search.

    For a given effort threshold, BFS/DFS checks if a path exists
    where all consecutive height differences <= threshold.
    """
    rows, cols = len(heights), len(heights[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

    def can_reach(max_effort: int) -> bool:
        """BFS: can we reach (rows-1, cols-1) with max effort <= max_effort?"""
        if rows == 1 and cols == 1:
            return True
        visited = [[False] * cols for _ in range(rows)]
        visited[0][0] = True
        queue = deque([(0, 0)])

        while queue:
            r, c = queue.popleft()
            if r == rows - 1 and c == cols - 1:
                return True
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if (0 <= nr < rows and 0 <= nc < cols
                        and not visited[nr][nc]
                        and abs(heights[nr][nc] - heights[r][c]) <= max_effort):
                    visited[nr][nc] = True
                    queue.append((nr, nc))
        return False

    # Find min and max heights for binary search range
    lo, hi = 0, 0
    for row in heights:
        for h in row:
            hi = max(hi, h)

    # Binary search on the minimum effort
    while lo < hi:
        mid = (lo + hi) // 2
        if can_reach(mid):
            hi = mid
        else:
            lo = mid + 1

    return lo


if __name__ == "__main__":
    print("=== Path with Minimum Effort ===\n")

    # Test 1
    h1 = [[1, 2, 2], [3, 8, 2], [5, 3, 5]]
    print(f"Test 1: heights={h1}")
    print(f"  Expected: 2")
    print(f"  Brute:    {minimumEffortPath_brute(h1)}")
    print(f"  Dijkstra: {minimumEffortPath_dijkstra(h1)}")
    print(f"  BinSearch: {minimumEffortPath_binary_search(h1)}")

    # Test 2
    h2 = [[1, 2, 3], [3, 8, 4], [5, 3, 5]]
    print(f"\nTest 2: heights={h2}")
    print(f"  Expected: 1")
    print(f"  Brute:    {minimumEffortPath_brute(h2)}")
    print(f"  Dijkstra: {minimumEffortPath_dijkstra(h2)}")
    print(f"  BinSearch: {minimumEffortPath_binary_search(h2)}")

    # Test 3
    h3 = [[1, 2, 1, 1, 1], [1, 2, 1, 2, 1], [1, 2, 1, 2, 1], [1, 2, 1, 2, 1], [1, 1, 1, 2, 1]]
    print(f"\nTest 3:")
    print(f"  Expected: 0")
    print(f"  Brute:    {minimumEffortPath_brute(h3)}")
    print(f"  Dijkstra: {minimumEffortPath_dijkstra(h3)}")
    print(f"  BinSearch: {minimumEffortPath_binary_search(h3)}")

    # Test 4: Single cell
    h4 = [[5]]
    print(f"\nTest 4 (single cell): Expected 0")
    print(f"  Brute:    {minimumEffortPath_brute(h4)}")
    print(f"  Dijkstra: {minimumEffortPath_dijkstra(h4)}")
    print(f"  BinSearch: {minimumEffortPath_binary_search(h4)}")
