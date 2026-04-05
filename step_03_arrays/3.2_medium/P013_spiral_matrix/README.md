# Spiral Matrix

> **LeetCode 54** | **Step 03 - Arrays (Medium)** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an `m x n` matrix, return all elements of the matrix in **spiral order** (clockwise, starting from the top-left corner).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[1,2,3],[4,5,6],[7,8,9]]` | `[1,2,3,6,9,8,7,4,5]` | Right → Down → Left → Up, then inner ring |
| `[[1,2,3,4],[5,6,7,8],[9,10,11,12]]` | `[1,2,3,4,8,12,11,10,9,5,6,7]` | 3×4 matrix |
| `[[1]]` | `[1]` | Single element |
| `[[1,2],[3,4]]` | `[1,2,4,3]` | 2×2 matrix |

## Constraints

- `m == matrix.length`
- `n == matrix[0].length`
- `1 <= m, n <= 10`
- `-100 <= matrix[i][j] <= 100`

---

## Approach 1: Brute Force — Simulation with Visited Array

**Intuition:** Mimic the movement of a person walking in a spiral. Use a `visited[][]` boolean matrix to know when to turn. Direction cycle is: right → down → left → up. When the next cell would be out-of-bounds or already visited, rotate direction clockwise by one step.

**Steps:**
1. Create a `visited[m][n]` matrix initialised to `false`.
2. Define direction vectors: `(0,1)`, `(1,0)`, `(0,-1)`, `(-1,0)`.
3. Start at `(0, 0)`, direction index `0` (right).
4. For each of the `m*n` cells: record value, mark visited, compute next cell.
5. If next cell is invalid or visited, rotate direction and recompute.
6. Move to next cell.

| Metric | Value |
|--------|-------|
| Time   | O(m·n) |
| Space  | O(m·n) — visited matrix |

---

## Approach 2: Optimal — 4 Shrinking Boundaries

**Intuition:** Instead of tracking visited cells, track four boundaries: `top`, `bottom`, `left`, `right`. Each time we finish traversing one side we shrink the corresponding boundary inward. No extra memory needed.

**Steps:**
1. Initialise `top=0`, `bottom=m-1`, `left=0`, `right=n-1`.
2. While `top <= bottom` and `left <= right`:
   a. Traverse `top` row left→right; `top++`.
   b. Traverse `right` column top→bottom; `right--`.
   c. If `top <= bottom`: traverse `bottom` row right→left; `bottom--`.
   d. If `left <= right`: traverse `left` column bottom→top; `left++`.
3. Return the collected list.

The guards in steps (c) and (d) prevent double-counting when a single row or column remains.

| Metric | Value |
|--------|-------|
| Time   | O(m·n) |
| Space  | O(1) — excluding the output list |

---

## Approach 3: Best — Same Boundary Approach (Cleaner Form)

**Intuition:** Identical algorithm to Approach 2. The code is written using list comprehensions (Python) / separate single-line loops (Java) to make each of the four traversal directions immediately obvious during an interview explanation.

**Steps:** Same as Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O(m·n) |
| Space  | O(1) |

---

## Real-World Use Case

**Image processing / raster scanning:** Printers and scanners read pixels in a raster (row-by-row) pattern, but many compression algorithms process image blocks in a spiral/zigzag order (e.g., JPEG's zigzag quantisation table scan). Spiral traversal is also used in robotics path planning for area coverage (lawn mowing robots, floor-cleaning robots) where the robot spirals inward to guarantee complete coverage.

## Interview Tips

- The boundary-shrinking approach is the gold standard — know it cold.
- Always handle the `top <= bottom` and `left <= right` guards before the bottom-row and left-column traversals; forgetting them causes elements to be emitted twice on non-square matrices.
- If asked for an in-place rotation variant (LeetCode 48), this boundary intuition also applies.
- Edge cases: single row, single column, 1×1 matrix — all handled naturally by the boundary guards.
- Time is always O(m·n) regardless of approach; the only tradeoff is the O(m·n) space of the visited array vs. O(1) for boundaries.
