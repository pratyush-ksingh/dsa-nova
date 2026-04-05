# Maximal Rectangle

> **Step 16.9** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 85

## Problem Statement

Given a `rows x cols` binary matrix filled with `'0'`s and `'1'`s, find the largest rectangle containing only `'1'`s and return its area.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]` | `6` | The maximal rectangle is in rows 2-3, columns 1-3 (0-indexed): a 2x3 block of 1s |
| `matrix = [["0"]]` | `0` | No 1s present |
| `matrix = [["1"]]` | `1` | Single 1 |
| `matrix = [["1","1","1","1"],["1","1","1","1"],["1","1","1","1"]]` | `12` | Entire 3x4 matrix is 1s |

## Constraints

- `rows == matrix.length`
- `cols == matrix[i].length`
- `1 <= rows, cols <= 200`
- `matrix[i][j]` is `'0'` or `'1'`

---

## Approach 1: Brute Force

**Intuition:** For every possible top-left corner `(r1, c1)`, extend downward row by row. As we add each row, track the minimum width that is "all 1s" from `c1` rightward across all rows `r1..r2`. This width multiplied by the height `r2 - r1 + 1` gives the area of the rectangle anchored at `(r1, c1)` with bottom at `r2`. Take the maximum over all anchors.

**Steps:**
1. For each `(r1, c1)` with `matrix[r1][c1] == '1'`:
2. Initialize `minWidth = n`.
3. For each subsequent row `r2` starting at `r1`: compute the rightward run length from `c1`, update `minWidth = min(minWidth, run_length)`.
4. If `minWidth == 0`, stop extending this anchor downward.
5. Update `maxArea = max(maxArea, (r2 - r1 + 1) * minWidth)`.

| Metric | Value |
|--------|-------|
| Time   | O(m^2 * n^2) worst case |
| Space  | O(1) |

---

## Approach 2: Optimal (Row-by-row Histogram + Monotonic Stack)

**Intuition:** Reduce the 2-D problem to a sequence of 1-D problems. For each row `r`, build an array `heights[]` where `heights[c]` = the number of consecutive `'1'`s in column `c` ending at row `r` (including row `r`). This is the height of a "bar" in a histogram. The maximum rectangle in this histogram that uses row `r` as its bottom edge is exactly the largest rectangle in the histogram problem (LeetCode 84), solvable in O(n) with a monotonic stack. Take the max across all rows.

**Steps:**
1. Initialize `heights[0..n-1] = 0`.
2. For each row `r`:
   - Update: `heights[c] = heights[c] + 1` if `matrix[r][c] == '1'`, else `heights[c] = 0`.
   - Solve "Largest Rectangle in Histogram" on `heights[]` using a monotonic stack.
   - Update global max area.
3. Monotonic stack details: maintain a stack of indices with non-decreasing heights. When a shorter bar is encountered, pop and compute the rectangle (width = distance between the new top of stack and current index).

| Metric | Value |
|--------|-------|
| Time   | O(m * n) — O(n) per row for the histogram |
| Space  | O(n) for heights and stack |

---

## Approach 3: Best (DP with Left/Right Boundary Arrays)

**Intuition:** An alternative pure-DP formulation that avoids an explicit stack. For each cell `(r, c)`, maintain three quantities that together determine the largest rectangle with its bottom-right corner at `(r, c)` at the height `height[c]`:

- `height[c]`: number of consecutive `'1'`s above and including row `r`.
- `left[c]`: the leftmost column such that all cells in columns `left[c]..c` in the range of `height[c]` rows above are `'1'`.
- `right[c]`: the rightmost column + 1 such that all cells in columns `c..right[c]-1` in the same range are `'1'`.

Area = `height[c] * (right[c] - left[c])`.

`left[]` is computed with a left-to-right scan tracking `cur_left` (the leftmost boundary imposed by the current row). `right[]` is computed right-to-left with `cur_right`. When a `'0'` is encountered, it resets the boundary. Critically, `left[c]` carries the maximum (tightest from the right) across rows, and `right[c]` carries the minimum (tightest from the left) — this ensures the rectangle extends only as far as is valid vertically.

**Steps:**
1. Initialize `height[n]=0`, `left[n]=0`, `right[n]=n`.
2. For each row `r`:
   - Update `height[c]`.
   - Update `left[c]` scanning left to right with `cur_left` tracking `'0'` breaks.
   - Update `right[c]` scanning right to left with `cur_right`.
   - For each `c`, compute `area = height[c] * (right[c] - left[c])` and update max.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(n) — three O(n) arrays, no stack |

---

## Real-World Use Case

**Image processing and OCR:** Finding the largest rectangle of a single color (e.g., white background region in a scanned document) is used to identify margins, text blocks, and whitespace in optical character recognition (OCR) preprocessing. The maximal rectangle algorithm also appears in circuit board layout (finding the largest empty rectangular area for placing a component), warehouse floor planning (largest contiguous available storage area on a grid map), and satellite image analysis (largest contiguous land patch of a given type).

---

## Interview Tips

- This problem is a direct extension of "Largest Rectangle in Histogram" (LeetCode 84). Interviewers often ask that problem first as a warm-up, then ask this one. Know the monotonic stack histogram solution cold.
- The histogram insight: treat each row as the base of a histogram where bar heights accumulate consecutive 1s from above. A `'0'` in column `c` resets `heights[c]` to 0.
- For the monotonic stack: when you pop index `i`, the width is `current_index - stack_top - 1` (between the new top and the current bar). If the stack is empty, the width is `current_index` (the popped bar is the shortest so far and spans from the left edge).
- The DP variant (Approach 3) is elegant because it has no explicit stack and the three arrays `height/left/right` are easy to reason about separately.
- Common mistake in Approach 3: `left[c]` should be `max(left[c], cur_left)` (carry the tighter bound from previous rows); similarly `right[c]` should be `min(right[c], cur_right)`. Forgetting the `max`/`min` causes incorrect results for tall rectangles spanning multiple rows.
- Time complexity argument: O(m*n) because we do a constant amount of work per cell across all three O(n) sweeps per row.
