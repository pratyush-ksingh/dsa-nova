# Path with Minimum Effort

> **Step 15.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

**(LeetCode #1631)** You are a hiker preparing for an upcoming hike. You are given `heights`, a 2D array of size `rows x columns`, where `heights[row][col]` represents the height of cell `(row, col)`. You are situated in the top-left cell `(0, 0)`, and you hope to travel to the bottom-right cell `(rows-1, columns-1)`.

You can move up, down, left, or right. The **effort** of a route is the **maximum absolute difference** in heights between two consecutive cells of the route.

Return the **minimum effort** required to travel from the top-left to the bottom-right cell.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| heights = [[1,2,2],[3,8,2],[5,3,5]] | 2 | Path 1->2->2->2->5, max diff = \|2-2\| or \|5-3\| = 2 |
| heights = [[1,2,3],[3,8,4],[5,3,5]] | 1 | Path 1->2->3->4->5->3->5, max diff = 1 |
| heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]] | 0 | Path exists using only cells with height 1 |
| heights = [[5]] | 0 | Already at destination |

## Constraints

- rows == heights.length
- columns == heights[i].length
- 1 <= rows, columns <= 100
- 1 <= heights[i][j] <= 10^6

---

## Approach 1: Brute Force (DFS trying all paths)

**Intuition:** Explore all possible paths from (0,0) to (rows-1, cols-1) using DFS with backtracking. For each path, track the maximum height difference between consecutive cells. The answer is the minimum of these maximum differences across all paths. Prune branches where the current max effort already exceeds the best known result.

**Steps:**
1. Start DFS from (0,0) with maxEffort = 0
2. At each cell, try all 4 directions
3. For each valid unvisited neighbor, compute new effort = max(current effort, |height difference|)
4. If new effort >= best result so far, prune
5. When reaching (rows-1, cols-1), update the global minimum
6. Backtrack: unmark visited

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(4^(m*n)) worst case | Exponential: try all paths (with pruning in practice) |
| Space  | O(m * n) | Visited array + recursion stack |

---

## Approach 2: Optimal (Dijkstra with priority queue)

**Intuition:** This is a shortest path problem with a twist: instead of summing edge weights, the "distance" is the maximum edge weight along the path (a **minimax path** problem). Dijkstra's algorithm works here because:
1. We always process the cell with the smallest max-effort first
2. Once we pop a cell from the min-heap, we have found the optimal effort to reach it
3. This greedy property holds because increasing the max-effort never decreases the cost

**Key Insight:** Define `dist[r][c]` = minimum possible maximum effort to reach cell (r,c). Initialize `dist[0][0] = 0`, all others infinity. For each cell processed, update neighbors: `new_effort = max(dist[r][c], |heights[nr][nc] - heights[r][c]|)`. If `new_effort < dist[nr][nc]`, update and push to heap.

**Steps:**
1. Initialize dist[][] to infinity, dist[0][0] = 0
2. Push (0, 0, 0) to min-heap [effort, row, col]
3. While heap not empty:
   - Pop minimum effort cell
   - If it is the destination, return effort
   - Skip if effort > dist[r][c] (outdated)
   - For each neighbor: compute new effort, update if better
4. Return dist[rows-1][cols-1]

**Dry-Run Trace:**
```
heights = [[1,2,2],[3,8,2],[5,3,5]]

Heap: [(0,0,0)]  dist[0][0]=0
Pop (0,0,0): neighbors (0,1) diff=1, (1,0) diff=2
  dist[0][1]=1, dist[1][0]=2
Heap: [(1,0,1), (2,1,0)]

Pop (1,0,1): neighbors (0,0) skip, (0,2) diff=max(1,0)=1, (1,1) diff=max(1,6)=6
  dist[0][2]=1, dist[1][1]=6
Heap: [(1,0,2), (2,1,0), (6,1,1)]

Pop (1,0,2): neighbors (0,1) skip, (1,2) diff=max(1,0)=1
  dist[1][2]=1
Heap: [(1,1,2), (2,1,0), (6,1,1)]

Pop (1,1,2): neighbors (0,2) skip, (2,2) diff=max(1,3)=3, (1,1) diff=6 skip
  dist[2][2]=3
  ... but continue, next is (2,1,0)

Pop (2,1,0): neighbors (2,1) diff=max(2,0)=2
  dist[2][1]=2
  ...
Pop (2,2,1): neighbors (2,2) diff=max(2,2)=2 < 3
  dist[2][2]=2

Eventually pop (2,2,2) -> return 2
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(m * n * log(m * n)) | Each cell pushed to heap at most O(4) times, heap ops are log(m*n) |
| Space  | O(m * n) | Distance array + heap |

---

## Approach 3: Best (Binary search + BFS feasibility check)

**Intuition:** The answer has a monotonic property: if we can reach the destination with effort `k`, we can certainly reach it with effort `k+1` (just use the same path). This means we can binary search on the answer.

For a given effort threshold `mid`, run BFS/DFS to check if a path exists where every consecutive height difference is <= `mid`. If yes, try smaller. If no, try larger.

**Why this can be better:** The binary search range is [0, max_height_in_grid]. For grids with small height values, `log(max_height)` can be smaller than `log(m*n)`, making this faster. Also, BFS is simpler than Dijkstra (no priority queue needed).

**Steps:**
1. Binary search: lo = 0, hi = max height in grid
2. For each mid = (lo + hi) / 2:
   - BFS from (0,0): only traverse edges where |height diff| <= mid
   - If (rows-1, cols-1) is reachable: hi = mid
   - Else: lo = mid + 1
3. Return lo

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(m * n * log(max_height)) | log(10^6) ~ 20 BFS runs, each O(m*n) |
| Space  | O(m * n) | Visited array + BFS queue |

---

## Real-World Use Case

**Terrain Navigation / Hiking Route Planning:** GPS navigation apps for hikers (like AllTrails) need to find routes that minimize the steepest climb. The "effort" metric (maximum elevation change between consecutive points) directly models this. A hiker might prefer a longer route that avoids steep climbs over a shorter route with a difficult ascent. Dijkstra-based minimax pathfinding is used in such route optimization.

## Interview Tips

- Recognize this as a **minimax path** problem, not a standard shortest path. The "distance" is max edge weight, not sum
- Start by explaining why Dijkstra works: the greedy property holds because max(a, b) >= a (monotonically non-decreasing)
- The binary search approach is a great "think outside the box" solution -- shows you can recognize monotonic properties
- Know the complexity tradeoff: Dijkstra is O(mn log mn), binary search is O(mn log H). For large grids with small heights, binary search wins
- Common mistake: using BFS without a priority queue for the Dijkstra approach (plain BFS does not work because effort is not uniform)
- Edge case: single cell grid -> return 0 immediately
- Follow-up: "What if you can also move diagonally?" -> Same algorithm, just 8 directions instead of 4
- Union-Find is another valid approach: sort all edges by weight, union them until (0,0) and (m-1,n-1) are connected
