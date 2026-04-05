# Number of Islands

> **Batch 3 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
**(LeetCode #200)** Given an `m x n` 2D binary grid where `'1'` represents land and `'0'` represents water, count the number of **islands**. An island is surrounded by water and is formed by connecting adjacent lands **horizontally or vertically**. You may assume all four edges of the grid are surrounded by water.

### Examples

| Input | Output | Explanation |
|---|---|---|
| `[["1","1","1","1","0"],`<br>`["1","1","0","1","0"],`<br>`["1","1","0","0","0"],`<br>`["0","0","0","0","0"]]` | 1 | All land cells are connected into one island |
| `[["1","1","0","0","0"],`<br>`["1","1","0","0","0"],`<br>`["0","0","1","0","0"],`<br>`["0","0","0","1","1"]]` | 3 | Three separate groups of connected land |

### Real-World Analogy
Imagine you're a satellite analyst looking at an aerial photo of an ocean. You need to count distinct landmasses. Two patches of land that touch horizontally or vertically are the same island, but diagonal-only contact doesn't count (they're separated by a thin water channel). You systematically "paint" each discovered island a different color -- the number of colors used equals the island count.

### Three Key Observations

1. **This is "count connected components" on a 2D grid** -- Each island is one connected component. Every time we find an unvisited `'1'`, that's a new island.
   - *Aha:* The grid IS the graph. Each cell is a node; edges connect adjacent `'1'` cells (up/down/left/right).

2. **Flood-fill eliminates an entire island in one pass** -- Starting from any `'1'`, DFS/BFS marks all connected `'1'`s as visited, so we never recount them.
   - *Aha:* We can mark cells by changing `'1'` to `'0'` (sink the island) to avoid a separate visited array.

3. **Union-Find works too: union adjacent land cells, count distinct roots** -- But it's overkill for a static grid; DFS is simpler and equally fast.
   - *Aha:* For dynamic island problems (adding land one-by-one), Union-Find becomes the optimal choice.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Graph model | Implicit grid graph | Each cell `(r,c)` connects to 4 neighbors |
| Traversal | DFS or BFS | Flood-fill to explore one island completely |
| Visited tracking | Modify grid in-place or `boolean[][]` | In-place saves O(mn) space |
| Alternative | Union-Find | Natural for dynamic variants |

---

## APPROACH LADDER

### Approach 1: DFS Flood Fill (Brute/Standard)

**Intuition:** Scan the grid cell by cell. When we hit a `'1'`, we've found a new island -- increment the count and use DFS to "sink" (mark as `'0'`) every connected land cell so it won't be counted again.

**Steps:**
1. Initialize `count = 0`
2. For each cell `(r, c)` in the grid:
   - If `grid[r][c] == '1'`: increment `count`, call `dfs(r, c)`
3. `dfs(r, c)`: if out of bounds or `grid[r][c] != '1'`, return. Otherwise set `grid[r][c] = '0'` and recurse on 4 neighbors.
4. Return `count`

**ASCII Diagram:**
```
Input grid:              After DFS from (0,0):      After DFS from (2,2):
1 1 0 0 0               0 0 0 0 0                  0 0 0 0 0
1 1 0 0 0    count=1     0 0 0 0 0     count=2      0 0 0 0 0
0 0 1 0 0   -------->   0 0 1 0 0    -------->     0 0 0 0 0
0 0 0 1 1               0 0 0 1 1                  0 0 0 1 1

After DFS from (3,3):    Final count = 3
0 0 0 0 0
0 0 0 0 0
0 0 0 0 0
0 0 0 0 0
```

**Dry-Run Trace:**
```
grid = [["1","1","0"],
        ["0","1","0"],
        ["0","0","1"]]

Scan (0,0): grid[0][0]='1' -> count=1, dfs(0,0)
  Sink (0,0)->'0', recurse neighbors:
    dfs(0,1): sink (0,1)->'0'
      dfs(0,2): '0', return
      dfs(1,1): sink (1,1)->'0'
        dfs(1,0): '0', return
        dfs(1,2): '0', return
        dfs(2,1): '0', return
    dfs(1,0): '0', return

Scan (0,1): '0', skip
Scan (0,2): '0', skip
Scan (1,0): '0', skip
Scan (1,1): '0', skip (already sunk)
Scan (1,2): '0', skip
Scan (2,0): '0', skip
Scan (2,1): '0', skip
Scan (2,2): '1' -> count=2, dfs(2,2)
  Sink (2,2)->'0', no unvisited neighbors

Return count = 2  ✓
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(m * n) | Each cell visited at most once by DFS |
| Space  | O(m * n) | Worst-case recursion depth for a grid of all `'1'`s |

---

### Approach 2: BFS Flood Fill (Optimal)

**Intuition:** Same island-counting logic but use BFS (queue) instead of DFS. This avoids deep recursion stacks on large grids.

**Steps:**
1. Scan grid for `'1'`; increment count
2. Add `(r, c)` to queue, mark as `'0'`
3. Process queue: dequeue cell, enqueue all 4-directional `'1'` neighbors (marking them `'0'` immediately)
4. When queue empties, the island is fully explored

**ASCII Diagram (BFS wave expansion):**
```
Starting BFS at (0,0):

Wave 0:     Wave 1:       Wave 2:
X . . .     0 X . .       0 0 . .
. . . .     X . . .       0 X . .
. . . .     . . . .       . . . .

X = just discovered this wave
0 = already sunk
```

**BUD Analysis:**
- **B**ottleneck: We must visit every cell once -- O(mn) is unavoidable
- **U**nnecessary: A separate `visited[][]` array when we can modify grid in-place
- **D**uplicate: Mark cells as `'0'` BEFORE enqueueing to prevent duplicate enqueues

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(m * n) | Each cell enqueued at most once |
| Space  | O(min(m, n)) | Queue size bounded by shorter grid dimension (BFS explores in wavefronts) |

---

### Approach 3: Union-Find (Best for Extensions)

**Intuition:** Treat each `'1'` cell as a node. Union adjacent `'1'` cells. The number of distinct roots among land cells = number of islands.

**Steps:**
1. Initialize parent array: each `'1'` cell maps to itself. Track `count` of land cells.
2. For each `'1'` cell, check right and down neighbors (to avoid double-processing):
   - If neighbor is `'1'`: `union(cell, neighbor)`. On successful union, decrement `count`.
3. Return `count`

**Dry-Run Trace (Union-Find):**
```
grid = [["1","1"],
        ["0","1"]]

Flatten: cell 0=(0,0), cell 1=(0,1), cell 3=(1,1)
Initial: parent=[0, 1, _, 3], count=3

(0,0) right neighbor (0,1): both '1' -> union(0,1), count=2
  parent=[0, 0, _, 3]
(0,0) down neighbor (1,0): '0', skip
(0,1) down neighbor (1,1): both '1' -> union(1,3)
  find(1)->0, find(3)->3 -> parent=[0, 0, _, 0], count=1

Return count = 1  ✓  (all three '1's connected)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(m * n * alpha(mn)) ~ O(m * n) | Process each cell; union/find nearly O(1) |
| Space  | O(m * n) | Parent and rank arrays |

---

## COMPLEXITY INTUITIVELY

**Why O(m * n)?** Every cell must be examined at least once to determine if it's land or water. Since the grid has `m * n` cells, O(m * n) is a tight lower bound -- and all three approaches achieve it.

**DFS vs BFS:** DFS risks stack overflow on huge grids (e.g., 300x300 all-land creates 90,000 recursion depth). BFS is safer with O(min(m,n)) queue space. Both have O(mn) time.

**Union-Find trade-off:** Slightly more code and constant-factor overhead, but naturally extends to "Number of Islands II" (dynamic land additions).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Empty grid `[]` or `[[]]` | 0 | Check grid dimensions before processing |
| All water | 0 | No DFS/BFS launches |
| All land | 1 | One massive island; DFS recursion depth = m*n |
| Single cell `[["1"]]` | 1 | Boundary check in DFS must handle 1x1 |
| Long thin grid (1 x 10000) | Varies | DFS recursion depth = 10000; BFS preferred |

**Common Mistakes:**
- Checking diagonal neighbors (problem says horizontal/vertical only)
- Forgetting to mark cells BEFORE enqueueing in BFS (causes duplicate processing and TLE)
- Using `int` comparison instead of `char` comparison (`grid[r][c] == 1` vs `grid[r][c] == '1'`)
- Not handling the grid modification correctly when a separate visited array is needed

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Can you solve it without modifying the grid?" | Yes, use a separate `visited[][]` boolean matrix -- costs O(mn) extra space |
| "What if diagonal connections also count?" | Add 4 more directions (8-directional flood fill); same algorithm, different neighbor list |
| "How does this relate to Number of Provinces?" | Same pattern! Provinces uses adjacency matrix; Islands uses implicit grid graph |
| "What if we add land cells one at a time?" | That's LeetCode #305 "Number of Islands II" -- Union-Find is optimal there |
| "Can you count island perimeters too?" | Yes, during DFS count boundary edges (water or grid edge). That's LC #463 |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Number of Provinces (LC #547) | Same "count components" but on adjacency matrix |
| Max Area of Island (LC #695) | Instead of counting, track largest component size during DFS |
| Surrounded Regions (LC #130) | Flood fill from borders; then flip remaining |
| Number of Islands II (LC #305) | Dynamic version -- Union-Find is essential |
| Flood Fill (LC #733) | Same DFS/BFS mechanics, different goal (color change) |
| Pacific Atlantic Water Flow (LC #417) | Multi-source BFS on grid, same traversal pattern |
