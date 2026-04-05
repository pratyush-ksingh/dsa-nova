# Unique Paths II

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
A robot is located at the top-left corner of an `m x n` grid. The robot can only move **right** or **down** at any point. Some cells contain **obstacles** (marked as `1`). The robot is trying to reach the **bottom-right corner**. How many unique paths are there?

*(LeetCode #63)*

**Constraints:**
- `1 <= m, n <= 100`
- `obstacleGrid[i][j]` is `0` (empty) or `1` (obstacle)

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[0,0,0],[0,1,0],[0,0,0]]` | `2` | Two paths: right-right-down-down and down-down-right-right |
| `[[0,1],[0,0]]` | `1` | Only one path: down then right |
| `[[1,0]]` | `0` | Start is blocked, no path exists |
| `[[0,0],[0,1]]` | `0` | Destination is blocked |

### Real-Life Analogy
> *You are navigating a city grid from the northwest corner to the southeast corner. You can only go east or south. Some intersections are under construction (obstacles) and completely blocked. How many different routes can you take? Each obstacle forces you to reroute, and if an obstacle blocks ALL paths (like blocking the only entrance to a row), that entire region becomes unreachable.*

### Key Observations
1. **Same as Unique Paths I but with obstacles:** When a cell has an obstacle, `dp[i][j] = 0` (no paths through it). Otherwise, `dp[i][j] = dp[i-1][j] + dp[i][j-1]`. <-- This is the "aha" insight
2. **Obstacle at start or end = 0 paths:** If `grid[0][0] == 1` or `grid[m-1][n-1] == 1`, return 0 immediately.
3. **First row/column handling:** In Unique Paths I, the entire first row and column are 1. Here, once you hit an obstacle in the first row (or column), ALL cells after it in that row (or column) are 0 -- the obstacle blocks all paths to those cells. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** Paths to (i,j) come from (i-1,j) and (i,j-1) -- heavy overlap in counting.
- **Optimal substructure:** Number of paths to (i,j) = sum of paths to its two predecessors.
- The obstacle constraint adds a simple check: if obstacle, paths = 0.

### Pattern Recognition
- **Pattern:** 2D Grid DP with obstacles (sum of top + left, zeroed by obstacles)
- **Classification Cue:** "When you see _count paths in grid with obstacles_ --> think _dp[i][j] = dp[i-1][j] + dp[i][j-1] with obstacle check_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** From (m-1, n-1), recursively count paths from (0,0). At each cell, paths = paths from above + paths from left. Obstacle cells return 0.

**State Definition:** `solve(i, j)` = number of unique paths from (0,0) to (i,j).

**Recurrence:**
```
solve(i, j):
  if i < 0 or j < 0: return 0        (out of bounds)
  if grid[i][j] == 1: return 0        (obstacle)
  if i == 0 and j == 0: return 1      (reached start)
  return solve(i-1, j) + solve(i, j-1)
```

**Steps:**
1. If `grid[0][0] == 1` or `grid[m-1][n-1] == 1`, return 0.
2. Call `solve(m-1, n-1)`.

| Time | Space |
|------|-------|
| O(2^(m+n)) | O(m+n) recursion stack |

**BUD Transition:** Many overlapping (i, j) states. Memoize.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][j]` so each cell is computed at most once.

**Dry Run:** `grid = [[0,0,0],[0,1,0],[0,0,0]]`

| Call | i | j | Compute | Result |
|------|---|---|---------|--------|
| solve(2,2) | 2 | 2 | solve(1,2) + solve(2,1) | |
| solve(1,2) | 1 | 2 | solve(0,2) + solve(1,1) | |
| solve(1,1) | 1 | 1 | obstacle! | **0** |
| solve(0,2) | 0 | 2 | solve(-1,2)=0 + solve(0,1) | |
| solve(0,1) | 0 | 1 | solve(-1,1)=0 + solve(0,0)=1 | 1 |
| back(0,2) | | | 0 + 1 = 1 | 1 |
| back(1,2) | | | 1 + 0 = 1 | 1 |
| solve(2,1) | 2 | 1 | solve(1,1)=0 + solve(2,0) | |
| solve(2,0) | 2 | 0 | solve(1,0) + solve(2,-1)=0 | |
| solve(1,0) | 1 | 0 | solve(0,0)=1 + solve(1,-1)=0 | 1 |
| back(2,0) | | | 1 + 0 = 1 | 1 |
| back(2,1) | | | 0 + 1 = 1 | 1 |
| back(2,2) | | | 1 + 1 = **2** | **2** |

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Build bottom-up to remove recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[m][n]` table iteratively.

**Steps:**
1. If `grid[0][0] == 1`, return 0. Set `dp[0][0] = 1`.
2. Fill first row: `dp[0][j] = dp[0][j-1]` if no obstacle, else `dp[0][j] = 0`.
3. Fill first column: `dp[i][0] = dp[i-1][0]` if no obstacle, else `dp[i][0] = 0`.
4. For `i = 1` to `m-1`, for `j = 1` to `n-1`:
   - If `grid[i][j] == 1`: `dp[i][j] = 0`
   - Else: `dp[i][j] = dp[i-1][j] + dp[i][j-1]`
5. Return `dp[m-1][n-1]`.

**Dry Run:** `grid = [[0,0,0],[0,1,0],[0,0,0]]`

|   | j=0 | j=1 | j=2 |
|---|-----|-----|-----|
| i=0 | 1 | 1 | 1 |
| i=1 | 1 | **0** (obstacle) | 1 |
| i=2 | 1 | 1 | **2** |

dp[2][2] = dp[1][2] + dp[2][1] = 1 + 1 = **2**

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Row `i` only depends on row `i-1`. Compress to 1D.

### Approach 4: Space Optimized
**What changed:** Use a single 1D array `prev[n]` representing the previous row.

**Steps:**
1. Initialize `prev[0] = 1` if no obstacle at (0,0), else return 0.
2. Fill first row into `prev`.
3. For `i = 1` to `m-1`:
   - Create `curr[n]`. `curr[0] = 0 if obstacle else prev[0]`.
   - For `j = 1` to `n-1`:
     - If `grid[i][j] == 1`: `curr[j] = 0`
     - Else: `curr[j] = prev[j] + curr[j-1]`
   - `prev = curr`
4. Return `prev[n-1]`.

| Time | Space |
|------|-------|
| O(m * n) | **O(n)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "At each cell, we branch into two directions. Without caching, O(2^(m+n)) paths."
**Memo/Tab:** "m * n cells, each computed once in O(1). Total O(m * n)."
**Space Optimized:** "Same time, but only one row of size n in memory: O(n)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Not checking obstacle at start/end** -- if `grid[0][0] == 1` or `grid[m-1][n-1] == 1`, answer is 0.
2. **First row/column after obstacle** -- once you hit an obstacle in the first row, all cells to its right are 0 (not 1). Same for first column.
3. **Using `int` overflow** -- for very large grids, path counts can be large. Python handles this naturally; in Java, long may be needed for extreme cases (though constraints here are small).
4. **Forgetting obstacle check in the loop** -- must set dp[i][j] = 0 when grid has obstacle.

### Edge Cases to Test
- [ ] Obstacle at start `[[1,0]]` --> 0
- [ ] Obstacle at end `[[0,0],[0,1]]` --> 0
- [ ] No obstacles `[[0,0],[0,0]]` --> 2
- [ ] Single cell, no obstacle `[[0]]` --> 1
- [ ] Single cell, obstacle `[[1]]` --> 0
- [ ] Obstacle blocking only path `[[0,1],[1,0]]` --> 0
- [ ] 1xN grid `[[0,0,0,0]]` --> 1
- [ ] First row obstacle `[[0,1,0]]` --> 0 (obstacle blocks all paths)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Only move right or down? Obstacle value is 1? Start and end could be blocked?"
2. **Recurrence:** "dp[i][j] = dp[i-1][j] + dp[i][j-1], but 0 if obstacle."
3. **Progression:** Recursion -> Memo -> Tab -> Space. Code the space-optimized version.
4. **Emphasize:** "The tricky part is first row/column -- after an obstacle, all subsequent cells are unreachable."

### Follow-Up Questions
- "Minimum cost path with obstacles?" --> Dijkstra or DP with cost (LeetCode #64 variant).
- "Can move in 4 directions?" --> BFS/DFS, not DP (DP requires DAG structure).
- "Find the actual path, not just count?" --> Backtrack through DP table.

---

## CONNECTIONS
- **Prerequisite:** Unique Paths I (no obstacles, P001/P002 in this section)
- **Same Pattern:** Grid DP with constraints (obstacles, costs)
- **This Unlocks:** Minimum Path Sum with obstacles, Cherry Pickup
- **Harder Variant:** Grid with multiple robots / Cherry Pickup (LeetCode #741)
