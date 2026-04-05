# Rotten Oranges

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
**(LeetCode #994)** You are given an `m x n` grid where each cell has one of three values:
- `0` = empty cell
- `1` = fresh orange
- `2` = rotten orange

Every minute, any fresh orange that is **4-directionally adjacent** to a rotten orange becomes rotten. Return the **minimum number of minutes** until no fresh orange remains. If it is **impossible** to rot all oranges, return `-1`.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [[2,1,1],[1,1,0],[0,1,1]] | 4 | Takes 4 minutes for rot to spread to bottom-right |
| [[2,1,1],[0,1,1],[1,0,1]] | -1 | Bottom-left orange (2,0) is isolated |
| [[0,2]] | 0 | No fresh oranges, already done |

```
Example 1: grid = [[2,1,1],[1,1,0],[0,1,1]]

Time 0 (initial):     Time 1:            Time 2:            Time 3:            Time 4:
+---+---+---+         +---+---+---+      +---+---+---+      +---+---+---+      +---+---+---+
| 2 | 1 | 1 |         | 2 | 2 | 1 |     | 2 | 2 | 2 |     | 2 | 2 | 2 |     | 2 | 2 | 2 |
+---+---+---+         +---+---+---+      +---+---+---+      +---+---+---+      +---+---+---+
| 1 | 1 | 0 |         | 2 | 1 | 0 |     | 2 | 2 | 0 |     | 2 | 2 | 0 |     | 2 | 2 | 0 |
+---+---+---+         +---+---+---+      +---+---+---+      +---+---+---+      +---+---+---+
| 0 | 1 | 1 |         | 0 | 1 | 1 |     | 0 | 1 | 1 |     | 0 | 2 | 1 |     | 0 | 2 | 2 |
+---+---+---+         +---+---+---+      +---+---+---+      +---+---+---+      +---+---+---+

Fresh left:  6          3                  1                  1(wait...)          0 => Answer: 4
```

### Real-World Analogy
Think of a virus spreading through a population. Multiple infected people (rotten oranges) are the initial sources. Each minute, the virus spreads to everyone in direct contact (adjacent cells). We want to know how many minutes until everyone is infected -- or whether some people are unreachable (isolated, return -1). This is classic **multi-source BFS**: all sources start spreading simultaneously.

### Three Key Observations

1. **Multi-source BFS: all rotten oranges spread simultaneously** -- This is NOT a single-source problem. All initially rotten oranges are the "first wave" in BFS. They all go into the queue at time 0.
   - *Aha:* If you do DFS or single-source BFS from one rotten orange at a time, you get the wrong answer because spreading is simultaneous.

2. **BFS levels = minutes** -- Each "level" of BFS corresponds to one minute. When we finish processing all nodes at the current level, one minute has passed.
   - *Aha:* Track the level count by processing the queue in "batches" (process all nodes currently in the queue before incrementing time).

3. **Check for remaining fresh oranges after BFS** -- If any `1` remains in the grid, return -1. This handles isolated fresh oranges that no rotten orange can reach.
   - *Aha:* Count fresh oranges at the start, decrement as they rot. If count > 0 at the end, return -1.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Traversal | BFS (not DFS) | Level-by-level = time simulation |
| Queue init | Multi-source (all rotten at once) | Simultaneous spreading |
| Fresh tracking | Counter | Decrement on rot, check 0 at end |
| Grid modification | In-place | Mark rotted cells as 2 |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Simulation with Repeated Scans

**Intuition:** Each minute, scan the entire grid. For every rotten orange, try to rot its fresh neighbors. Repeat until no changes occur.

**Steps:**
1. Count fresh oranges
2. Repeat:
   a. Scan entire grid, mark fresh neighbors of rotten as "newly rotten" (use temp marker 3)
   b. Convert all 3s to 2s, decrement fresh count, increment time
   c. If no new oranges rotted this pass, break
3. If fresh > 0, return -1; else return time

**BUD Analysis:**
- **Bottleneck:** Full grid scan every minute -- O(m*n) per pass
- **Unnecessary:** We re-scan already-rotten cells that have no fresh neighbors
- **Duplicated:** Same cells checked repeatedly across passes

| Metric | Value |
|--------|-------|
| Time   | O((m*n)^2) -- up to m*n passes, each O(m*n) |
| Space  | O(1) -- in-place |

---

### Approach 2: Optimal -- Multi-Source BFS

**Intuition:** Start BFS from ALL rotten oranges simultaneously. Process level by level. Each level = one minute. Track fresh count and decrement as oranges rot.

**Steps:**
1. Scan grid: enqueue all rotten oranges `(r, c)`, count fresh oranges
2. If fresh == 0, return 0
3. BFS level-by-level:
   a. For each cell in current level, check 4 neighbors
   b. If neighbor is fresh (1): mark as rotten (2), decrement fresh, enqueue
   c. After processing level, increment minutes
4. If fresh == 0, return minutes; else return -1

```
Dry-run: grid = [[2,1,1],[1,1,0],[0,1,1]]

Queue init: [(0,0)]     fresh=6, time=0

Level 0 (process (0,0)):
  (0,0) rots (0,1) and (1,0)
  Queue: [(0,1),(1,0)]   fresh=4, time=1

Level 1 (process (0,1),(1,0)):
  (0,1) rots (0,2)
  (1,0) rots (1,1) -- (0,0) already rotten
  Queue: [(0,2),(1,1)]   fresh=2, time=2

Level 2 (process (0,2),(1,1)):
  (0,2) -- no fresh neighbors
  (1,1) rots (2,1)
  Queue: [(2,1)]         fresh=1, time=3

Level 3 (process (2,1)):
  (2,1) rots (2,2)
  Queue: [(2,2)]         fresh=0, time=4

fresh=0 => return 4 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(m * n) -- each cell processed at most once |
| Space  | O(m * n) -- queue can hold all cells |

---

### Approach 3: Best -- Multi-Source BFS (Space Optimized)

**Intuition:** Same BFS algorithm but with minor optimizations:
- Use a flat index `r * cols + c` instead of storing pairs to reduce memory overhead
- Process in-place without extra visited array (grid itself is the visited marker)
- Early termination: stop immediately when fresh reaches 0

The time and space complexity remain the same asymptotically, but constant factors improve.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) worst case for queue, O(1) extra beyond queue |

---

## COMPLEXITY INTUITIVELY

**Why O(m*n)?** Every cell is visited at most once during BFS. When a cell turns rotten, it is enqueued exactly once and dequeued exactly once. The initial scan to find rotten oranges and count fresh ones is also O(m*n). Total: two passes = O(m*n).

**Why not O(m*n * max(m,n))?** Because BFS processes cells in waves, not repeatedly. Each cell transitions from fresh to rotten exactly once.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| No fresh oranges | Return 0 immediately | Don't run BFS unnecessarily |
| No rotten oranges but fresh exist | Return -1 | Queue is empty, BFS never starts |
| All rotten | Return 0 | fresh count = 0 from the start |
| Grid is all 0s | Return 0 | No fresh oranges |
| Single cell [1] | Return -1 | No rotten to start spreading |
| Single cell [2] | Return 0 | Already all rotten |
| Isolated fresh orange | Return -1 | Fresh count > 0 after BFS |

**Common Mistakes:**
- Off-by-one in time counting (time should not increment for the last level that empties the queue)
- Forgetting to handle the "no fresh oranges" case (return 0, not -1)
- Using DFS instead of BFS (DFS does not simulate simultaneous spreading)

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Tests multi-source BFS -- a pattern that shows up in many problems
- Grid traversal is bread-and-butter for coding interviews
- The -1 edge case tests careful thinking about unreachable states

**Follow-ups to expect:**
1. "What if oranges can rot diagonally too?" -- Add 4 diagonal directions (8-directional BFS)
2. "What if some walls block spreading?" -- Skip cells with walls
3. "What if rotting takes different times per cell?" -- Use Dijkstra instead of BFS
4. "Find which oranges will never rot" -- After BFS, scan for remaining 1s

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| 01 Matrix (LC 542) | Multi-source BFS from all 0s to find distance to nearest 0 |
| Walls and Gates (LC 286) | Multi-source BFS from all gates |
| Shortest Path in Binary Maze | BFS on grid, same traversal pattern |
| Number of Islands (LC 200) | Grid BFS/DFS, but counting components instead of time |
| Flood Fill (LC 733) | Single-source BFS/DFS on grid |
| Pacific Atlantic Water Flow (LC 417) | Multi-source BFS from ocean borders |
