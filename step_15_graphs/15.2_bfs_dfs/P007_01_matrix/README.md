# 01 Matrix

> **Step 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #542)** Given an `m x n` binary matrix `mat`, return a matrix of the same size where each cell contains the **distance to the nearest 0**. Distance is measured in 4-directional (up/down/left/right) steps.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [[0,0,0],[0,1,0],[0,0,0]] | [[0,0,0],[0,1,0],[0,0,0]] | Center 1 is 1 step from a 0 |
| [[0,0,0],[0,1,0],[1,1,1]] | [[0,0,0],[0,1,0],[1,2,1]] | Bottom-center 1 is 2 steps away |

```
Example 2 walkthrough:
Matrix:       Distances:
0  0  0       0  0  0
0  1  0  -->  0  1  0
1  1  1       1  2  1

(2,1) is the furthest: must go up to (1,1) then up to (0,1) = 2 steps.
```

### Constraints
- `m == mat.length`, `n == mat[0].length`
- `1 <= m, n <= 10^4`
- `1 <= m * n <= 10^4`
- `mat[i][j]` is either `0` or `1`
- There is at least one `0` in the matrix

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Traversal | Multi-source BFS | All 0s are equidistant sources |
| Starting point | From 0s (not 1s) | 0s are sources; expanding outward fills 1s naturally |
| Distance tracking | dist[][] initialized to INF | BFS guarantees first update is shortest |

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS from every 1-cell

**Intuition:** For every cell containing `1`, run an independent BFS until you find the nearest `0`. Simple but deeply redundant -- adjacent cells re-explore the same paths.

**Steps:**
1. For each cell `(r, c)` where `mat[r][c] == 1`, launch a BFS
2. BFS expands 4-directionally, tracking distance
3. The first time a cell containing `0` is reached, record that distance
4. Zero-cells trivially get distance `0`

**BUD Analysis:**
- **Bottleneck:** Each of up to m*n cells runs O(m*n) BFS
- **Unnecessary:** Nearby 1-cells traverse identical paths to the same 0
- **Duplicated:** Same 0 is discovered repeatedly by adjacent 1-cells

| Metric | Value |
|--------|-------|
| Time   | O((m*n)^2) -- up to m*n BFS calls, each O(m*n) |
| Space  | O(m*n) -- BFS visited array per call |

---

### Approach 2: Optimal -- Multi-Source BFS from all 0s

**Intuition:** Reverse the problem. Instead of each `1` searching for its nearest `0`, start from ALL `0` cells simultaneously and flood outward. The first wave that reaches a `1` cell has traveled the minimum distance. This is the same insight as Rotten Oranges (LC 994).

**Steps:**
1. Initialize `dist[r][c] = 0` for all `0`-cells; `dist[r][c] = INF` for `1`-cells
2. Enqueue all `0`-cells into the BFS queue
3. BFS: dequeue cell, try all 4 neighbors
4. If `dist[neighbor] > dist[current] + 1`, update and enqueue
5. Return the filled `dist` matrix

```
Dry-run: mat = [[0,0,0],[0,1,0],[1,1,1]]

Init queue: (0,0),(0,1),(0,2),(1,0),(1,2)  dist=0 for all 0s

Process (0,0): neighbors already 0 or dist=0, no update
Process (0,1): same
Process (0,2): same
Process (1,0): neighbor (2,0) gets dist=1, enqueue
Process (1,2): neighbor (2,2) gets dist=1, enqueue

Process (2,0) dist=1: neighbor (2,1) gets dist=2, enqueue
Process (2,2) dist=1: neighbor (2,1) already dist=2, no update
                       neighbor (1,1) gets dist=2... wait, (1,1) should be 1

Actually (1,1) is reached from (0,1) or (1,0) or (1,2):
- From (0,1): dist = 0+1 = 1
- From (1,0): dist = 0+1 = 1
- From (1,2): dist = 0+1 = 1
Result: [[0,0,0],[0,1,0],[1,2,1]] ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(m*n) -- each cell enqueued and processed exactly once |
| Space  | O(m*n) -- queue holds at most all cells |

---

### Approach 3: Best -- Multi-Source BFS with Flat Index

**Intuition:** Identical algorithm to Approach 2. The only change is storing cells as flat integer indices (`r * cols + c`) rather than `(r, c)` pairs, which reduces the per-entry memory footprint and avoids tuple construction overhead in Python / array allocation in Java.

**Steps:** Same as Approach 2; convert between flat index and `(row, col)` with `divmod` (Python) or `/` and `%` (Java).

| Metric | Value |
|--------|-------|
| Time   | O(m*n) |
| Space  | O(m*n) -- smaller constant than Approach 2 due to flat queue |

---

## COMPLEXITY INTUITIVELY

**Why O(m*n) for multi-source BFS?** Each cell is processed at most once. Once a cell's `dist` is set it can only be updated to a smaller value -- but because BFS processes cells in non-decreasing distance order, the first update IS the minimum, so no cell is ever re-enqueued. Total work = O(m*n) initialization + O(m*n) BFS = O(m*n).

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| All 0s | Every cell stays 0, queue processes quickly | Handled automatically |
| Single 0 in corner | Valid; BFS floods the entire grid | INF initialization ensures correctness |
| All 1s except one 0 | Max distances up to m+n-2 | No issue; BFS handles it |
| 1x1 matrix with 0 | Returns [[0]] | Trivial case |

**Common Mistakes:**
- Doing BFS from each 1-cell (brute force) instead of reversing to multi-source from 0s
- Forgetting to initialize 1-cells' distance to INF (they won't be updated correctly otherwise)
- Using DFS instead of BFS (DFS does not guarantee shortest path)

---

## Real-World Use Case

**Emergency Services Routing:** A city map has fire stations (0s) and buildings (1s). The `dist` matrix tells every building how many blocks away the nearest fire station is. Multi-source BFS from all stations simultaneously gives all distances in a single O(m*n) pass, used in GIS shortest-path preprocessing.

**Proximity Maps in Games:** Compute heatmaps showing distance from every tile to the nearest resource, enemy base, or hazard zone.

---

## Interview Tips
- This is the canonical example of **reversing BFS direction**: start from the known sources (0s) rather than from the unknowns (1s). Mention this insight early.
- Connect to Rotten Oranges (LC 994) and Walls & Gates (LC 286) -- they all share the multi-source BFS pattern.
- If asked "can you do it with DP?": yes -- two-pass DP (top-left then bottom-right) also achieves O(m*n) with O(1) extra space, but BFS is more intuitive.
- Emphasize that BFS guarantees shortest distance on unweighted grids; DFS does not.
