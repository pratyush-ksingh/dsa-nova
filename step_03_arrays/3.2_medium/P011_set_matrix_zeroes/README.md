# Set Matrix Zeroes

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 73 | **Status:** UNSOLVED

## Problem Statement

Given an `m x n` integer matrix `matrix`, if an element is `0`, set its **entire row** and **entire column** to `0`. Perform this operation **in-place**.

**Critical constraint:** Do not use a separate matrix of the same size (O(m*n) space) — the best solution uses O(1) extra space.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [[1,1,1],[1,0,1],[1,1,1]] | [[1,0,1],[0,0,0],[1,0,1]] | Zero at (1,1) zeroes row 1 and column 1 |
| [[0,1,2,0],[3,4,5,2],[1,3,1,5]] | [[0,0,0,0],[0,4,5,0],[0,3,1,0]] | Zeros at (0,0) and (0,3) zero row 0, col 0, col 3 |

## Constraints

- m == matrix.length, n == matrix[0].length
- 1 <= m, n <= 200
- -2^31 <= matrix[i][j] <= 2^31 - 1
- Follow-up: Can you do it in O(1) extra space?

---

## Approach 1: Brute Force

**Intuition:** Copy the entire matrix first. Then scan the copy for zeros. For each zero found, zero out the corresponding row and column in the **original** matrix. Using a copy prevents the cascade problem where newly set zeros trigger incorrect additional zeroing.

**Steps:**
1. Create a deep copy of the matrix
2. Iterate over every cell (i, j) of the copy
3. If `copy[i][j] == 0`:
   - Set all elements in row i of the original to 0
   - Set all elements in column j of the original to 0
4. No return value (in-place modification)

| Metric | Value |
|--------|-------|
| Time   | O(m*n*(m+n)) |
| Space  | O(m*n) |

---

## Approach 2: Optimal — Row/Column Index Sets

**Intuition:** Instead of copying the whole matrix, just record *which* rows and columns need to be zeroed. Two sets suffice. Then do a second pass to apply the zeroing.

**Steps:**
1. First pass: iterate over all cells; if `matrix[i][j] == 0`, add i to `zero_rows` set and j to `zero_cols` set
2. Second pass: iterate over all cells; if `i in zero_rows` OR `j in zero_cols`, set `matrix[i][j] = 0`

| Metric | Value |
|--------|-------|
| Time   | O(m*n) |
| Space  | O(m+n) |

---

## Approach 3: Best — O(1) Space Using First Row/Column as Markers

**Intuition:** The first row and first column of the matrix can serve as the marker arrays that the Optimal approach stored in separate sets. Use `matrix[0][j]` as a flag for "column j needs zeroing" and `matrix[i][0]` as a flag for "row i needs zeroing". The catch: we need to handle the first row and first column themselves separately, since they double as both data and marker storage.

**Steps:**
1. Check if the first row has any zero (`first_row_has_zero` boolean)
2. Check if the first column has any zero (`first_col_has_zero` boolean)
3. Scan rows 1..m-1, cols 1..n-1: if `matrix[i][j] == 0`, set `matrix[i][0] = 0` and `matrix[0][j] = 0`
4. Second scan of rows 1..m-1, cols 1..n-1: if `matrix[i][0] == 0` OR `matrix[0][j] == 0`, set `matrix[i][j] = 0`
5. If `first_row_has_zero`: zero out entire row 0
6. If `first_col_has_zero`: zero out entire column 0

| Metric | Value |
|--------|-------|
| Time   | O(m*n) |
| Space  | O(1) |

---

## Real-World Use Case

**Sparse Matrix Compression:** In database systems and scientific computing, matrices are sometimes stored in compressed formats. When a zero is introduced at a position (indicating "no data" or "blocked"), entire rows or columns may need invalidation — e.g., in dependency matrices where a failed node nullifies all dependencies in its row and column.

**Game Boards / Constraint Propagation:** In constraint-satisfaction problems (like Sudoku solvers), placing a value at a cell can zero-out (eliminate) possibilities across a row, column, or region — the same conceptual operation.

## Interview Tips

- The cascade problem (using the same matrix to read and write simultaneously) is the core difficulty. Explain this clearly before jumping to a solution.
- The O(1) space solution is the expected "best" answer. Walk through why step 1 and 2 (checking the first row/col first) are essential — if you set markers before checking them, you lose the original information.
- A common bug: forgetting to handle the first row/column separately in the O(1) approach.
- The order of steps in Approach 3 matters: handle inner matrix → then first row → then first column (or vice versa, but be consistent).
- Follow-up: "Can you do this for a matrix with a custom sentinel value instead of 0?" — the same approach works with minor modification.
