# Minimum Path Sum

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an `m x n` grid filled with non-negative numbers, find a path from the **top-left** corner to the **bottom-right** corner that minimizes the sum of all numbers along the path. You can only move **right** or **down** at each step.

**LeetCode #64**

**Constraints:**
- `1 <= m, n <= 200`
- `0 <= grid[i][j] <= 200`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[1,3,1],[1,5,1],[4,2,1]]` | `7` | Path: 1->3->1->1->1 = 7. Or 1->1->5->1->1 = 9. Min = 7 |
| `[[1,2,3],[4,5,6]]` | `12` | Path: 1->2->3->6 = 12. Or 1->4->5->6 = 16. Or 1->2->5->6 = 14 |
| `[[5]]` | `5` | Single cell |

### Real-Life Analogy
> *Imagine you are driving from the northwest corner of a city to the southeast corner. The streets form a grid, and each block has a different toll fee. You can only drive east or south. You want to find the route that minimizes your total toll. This is exactly GPS route optimization in a grid-based city like Manhattan.*

### Key Observations
1. **State Definition:** `dp[i][j]` = minimum path sum to reach cell `(i, j)` from `(0, 0)`.
2. **Recurrence:** `dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])` (come from above or left).
3. **Base Cases:** First row can only come from the left. First column can only come from above.
4. **Answer:** `dp[m-1][n-1]`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Recursion** explores all paths from (0,0) to (m-1,n-1). Exponential due to overlapping subproblems.
- **Memoization** caches `dp[i][j]`, reducing to O(m*n).
- **Tabulation** fills the grid top-to-bottom, left-to-right.
- **Space Optimization** recognizes that row `i` only depends on row `i-1` and the current row, so one 1D array suffices.

### Pattern Recognition
- **Pattern:** 2D Grid DP (fixed start, fixed end, limited moves)
- **Classification Cue:** "When you see _min/max path in grid with only right/down moves_ --> think _2D DP where dp[i][j] = grid[i][j] + min(top, left)_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Start from `(m-1, n-1)` and recursively compute the minimum path sum by trying both directions (from above and from left).

**Steps:**
1. `solve(i, j)` = minimum path sum to reach `(i, j)`.
2. Base case: `solve(0, 0) = grid[0][0]`.
3. If `i == 0`: can only come from left, `solve(0, j) = grid[0][j] + solve(0, j-1)`.
4. If `j == 0`: can only come from above, `solve(i, 0) = grid[i][0] + solve(i-1, 0)`.
5. Otherwise: `solve(i, j) = grid[i][j] + min(solve(i-1, j), solve(i, j-1))`.

**Why it is slow:** Overlapping subproblems. `solve(1,1)` is called from both `solve(1,2)` and `solve(2,1)`.

| Time | Space |
|------|-------|
| O(2^(m+n)) | O(m+n) recursion stack |

**BUD Transition:** Cache `solve(i, j)` results.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Store results in `dp[i][j]`. Check before computing.

**Steps:**
1. Create `dp[m][n]`, initialized to -1.
2. Same recursion with cache check.

**Dry Run:** `grid = [[1,3,1],[1,5,1],[4,2,1]]`

| Call | (i,j) | From | Value | dp |
|------|--------|------|-------|----|
| solve(2,2) | (2,2) | min(solve(1,2), solve(2,1)) | - | |
| solve(1,2) | (1,2) | min(solve(0,2), solve(1,1)) | - | |
| solve(0,2) | (0,2) | solve(0,1) + 1 | - | |
| solve(0,1) | (0,1) | solve(0,0) + 3 | 4 | dp[0][1]=4 |
| solve(0,0) | (0,0) | base | 1 | dp[0][0]=1 |
| back | (0,2) | 4 + 1 = 5 | 5 | dp[0][2]=5 |
| solve(1,1) | (1,1) | min(dp[0][1], solve(1,0)) + 5 | - | |
| solve(1,0) | (1,0) | solve(0,0) + 1 = 2 | 2 | dp[1][0]=2 |
| back | (1,1) | min(4, 2) + 5 = 7 | 7 | dp[1][1]=7 |
| back | (1,2) | min(5, 7) + 1 = 6 | 6 | dp[1][2]=6 |
| solve(2,1) | (2,1) | min(dp[1][1], solve(2,0)) + 2 | - | |
| solve(2,0) | (2,0) | dp[1][0] + 4 = 6 | 6 | dp[2][0]=6 |
| back | (2,1) | min(7, 6) + 2 = 8 | 8 | dp[2][1]=8 |
| back | (2,2) | min(6, 8) + 1 = **7** | 7 | dp[2][2]=7 |

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) cache + O(m+n) stack |

**BUD Transition:** Eliminate recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill `dp[][]` iteratively, row by row, left to right.

**Steps:**
1. `dp[0][0] = grid[0][0]`.
2. First row: `dp[0][j] = dp[0][j-1] + grid[0][j]`.
3. First col: `dp[i][0] = dp[i-1][0] + grid[i][0]`.
4. Inner cells: `dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])`.
5. Return `dp[m-1][n-1]`.

**Dry Run:** `grid = [[1,3,1],[1,5,1],[4,2,1]]`

```
dp table:
  1  4  5
  2  7  6
  6  8  7
```

Answer: `dp[2][2] = 7`

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Row `i` only depends on row `i-1`. Use single row.

### Approach 4: Space Optimized
**What changed:** Use a single 1D array of size `n`. Process row by row. `dp[j]` holds the value from the row above (before update) and from the left (after update in same row).

**Steps:**
1. `dp[0] = grid[0][0]`. Fill first row: `dp[j] = dp[j-1] + grid[0][j]`.
2. For each row `i >= 1`:
   - `dp[0] += grid[i][0]` (first column: only from above).
   - For `j = 1` to `n-1`: `dp[j] = grid[i][j] + min(dp[j], dp[j-1])`.
     - `dp[j]` (before update) = value from above.
     - `dp[j-1]` (already updated) = value from left.
3. Return `dp[n-1]`.

**Dry Run:** `grid = [[1,3,1],[1,5,1],[4,2,1]]`

| Row | dp after processing |
|-----|-------------------|
| 0 | [1, 4, 5] |
| 1 | [2, 7, 6] |
| 2 | [6, 8, 7] |

| Time | Space |
|------|-------|
| O(m * n) | **O(n)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Two choices at each cell, path length m+n-2, so roughly 2^(m+n) paths."
**Memo/Tab:** "m*n cells, O(1) work per cell. Total: O(m*n)."
**Space Optimized:** "Same O(m*n) time, but only one row at a time -- O(n) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting first row/column base cases:** First row has no "above" option, first column has no "left" option.
2. **Not handling single row or single column:** If `m == 1`, just sum the row. If `n == 1`, just sum the column.
3. **Space optimization order:** Process `j` left to right (since we need `dp[j-1]` already updated for current row).

### Edge Cases to Test
- [ ] `1x1` grid --> single element
- [ ] `1xn` or `mx1` --> sum of all elements
- [ ] Grid with all zeros --> 0
- [ ] Grid with all same values --> `(m+n-1) * value`

---

## INTERVIEW LENS

### How to Present
1. **Start with recursion:** "From each cell, I can come from above or left. I recursively compute both and take the min."
2. **Add memoization:** "Cells overlap -- (1,1) is reached from both (1,2) and (2,1). I cache results."
3. **Convert to tabulation:** "Fill row by row, left to right."
4. **Optimize space:** "Each row only depends on the previous row. I use a single 1D array."

### Follow-Up Questions
- "Can you do this in O(1) extra space?" --> Yes, modify the input grid in-place. But mention this changes the input.
- "What if you could also move diagonally?" --> Add `dp[i-1][j-1]` as a third option.
- "What if some cells are blocked?" --> Set blocked cells to infinity.
- "Print the actual path?" --> Backtrack from `(m-1, n-1)` choosing the direction with smaller dp value.

---

## CONNECTIONS
- **Prerequisite:** Unique Paths, Grid Traversal
- **Same Pattern:** Triangle Min Path Sum, Cherry Pickup
- **This Unlocks:** Dungeon Game (reverse direction), Cherry Pickup II
- **Harder Variant:** Maximum Path Sum with obstacles, 3D grid DP
