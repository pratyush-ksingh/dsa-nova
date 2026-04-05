# Cherry Pickup II

> **Step 16.16.3** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 1463

## Problem Statement

Given an `rows x cols` grid filled with non-negative integers representing cherries:
- **Robot 1** starts at `(0, 0)` (top-left corner).
- **Robot 2** starts at `(0, cols-1)` (top-right corner).
- Both robots move **downward simultaneously**, each moving to one of 3 cells in the next row: diagonally left, straight down, or diagonally right.
- Each robot collects all cherries in the cells it visits. If both robots visit the **same cell**, cherries are counted **only once**.

Return the **maximum total cherries** both robots can collect.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| grid=[[3,1,1],[2,5,1],[1,5,5],[2,1,1]] | 24 | Robot1: (0,0)→(1,1)→(2,1)→(3,0) picks 3+5+5+2=15; Robot2: (0,2)→(1,1)→(2,2)→(3,2) picks 1+0+5+1=7... optimal paths give 24 total |
| grid=[[1,0,0,0,0,0,1],[2,0,0,0,0,3,0],[2,0,9,0,0,0,0],[0,3,0,5,4,0,0],[1,0,2,3,0,0,6]] | 28 | Best coordination yields 28 |

## Constraints

- `rows == grid.length`
- `cols == grid[i].length`
- 2 <= rows, cols <= 70
- 0 <= grid[i][j] <= 100

---

## Approach 1: Brute Force — Pure Recursion

**Intuition:** At every row, each robot independently chooses one of 3 directions — giving 9 combinations per row. Recurse through all possibilities. Without memoization, many states are recomputed exponentially.

**Steps:**
1. Define `rec(row, c1, c2)`: max cherries from row `row` onwards when robot1 is at column `c1` and robot2 at `c2`.
2. Collect `grid[row][c1] + grid[row][c2]` (subtract one if `c1 == c2`).
3. If `row == rows - 1`, return collected cherries.
4. Try all 9 direction combinations `(d1, d2)` for the two robots.
5. Return collected + max of valid next states.

| Metric | Value |
|--------|-------|
| Time   | O(9^rows) |
| Space  | O(rows) |

---

## Approach 2: Optimal — 3D Memoized DP (Top-Down)

**Intuition:** State `(row, c1, c2)` fully captures the problem — both robots always move to the same next row, so `row` is shared. There are at most `rows * cols * cols` states; memoize to avoid recomputation.

**Steps:**
1. Define `dp(row, c1, c2)` with a 3D memo table initialized to -1.
2. Base case: `row == rows - 1` → return cherries at current row.
3. Collect current row cherries (count cell once if `c1 == c2`).
4. Try all 9 `(d1, d2)` combos for `(nc1, nc2)` in bounds.
5. Return current cherries + max of valid `dp(row+1, nc1, nc2)`.

| Metric | Value |
|--------|-------|
| Time   | O(rows * cols^2) |
| Space  | O(rows * cols^2) |

---

## Approach 3: Best — 2D Space-Optimized Bottom-Up DP

**Intuition:** Since we always move from row `r` to row `r+1`, we only need the **next row's** DP values to compute the current row. Replace the 3D table with two rolling 2D arrays (`curr` and `nxt`), reducing space from O(rows * cols^2) to O(cols^2).

**Steps:**
1. Initialize `curr[c1][c2]` for the last row: `grid[rows-1][c1] + grid[rows-1][c2]` (or just `grid[rows-1][c1]` if `c1 == c2`).
2. For each row from `rows-2` down to 0:
   a. Create `nxt[c1][c2]` for each valid `(c1, c2)` pair.
   b. For each `(c1, c2)`, try all 9 direction combinations. Pick max from `curr[nc1][nc2]`.
   c. `nxt[c1][c2] = grid[row][c1] + (c2 != c1 ? grid[row][c2] : 0) + best_next`.
   d. Swap: `curr = nxt`.
3. Answer is `curr[0][cols-1]` (starting positions of robot1 and robot2).

| Metric | Value |
|--------|-------|
| Time   | O(rows * cols^2) |
| Space  | O(cols^2) |

---

## Real-World Use Case

**Multi-agent path optimization:** This problem models two agents simultaneously traversing a shared environment, maximizing collective reward while avoiding double-counting shared resources. Real applications include:
- **Warehouse robotics:** Two picking robots collecting items from shelves — optimizing total throughput while avoiding collisions and redundant picks.
- **Multi-drone search and rescue:** Two drones scanning a grid for survivors — maximize coverage without both flying over the same area.
- **Game AI:** Two allied characters collecting bonuses in a level, coordinated to maximize total score.

The key insight (shared resources count once) maps directly to resource contention in distributed systems.

---

## Interview Tips

- The crucial state reduction: since both robots always move **down by one row together**, the row index is shared. State is `(row, c1, c2)` — not `(r1, c1, r2, c2)`.
- Handle the "same cell" case carefully: `cherries = grid[row][c1] + (c1 != c2 ? grid[row][c2] : 0)`. Missing this is the most common bug.
- By symmetry, we can assume `c1 <= c2` always (robot1 starts left of robot2) to halve the state space — but this optimization is not necessary for correctness.
- The bottom-up approach fills from the **last row upward** (easier to initialize boundary), unlike many grid DPs that fill top-down.
- Space optimization from O(rows*cols^2) to O(cols^2) uses the same "two rolling arrays" trick as 2D knapsack — important to mention in interviews.
- Time complexity: O(rows * cols^2 * 9) = O(rows * cols^2) since 9 is a constant.
