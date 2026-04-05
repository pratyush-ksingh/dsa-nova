# Search a 2D Matrix II

> **Batch 1 of 12** | **Topic:** Binary Search on 2D Arrays | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Write an efficient algorithm that searches for a value `target` in an `m x n` integer matrix. This matrix has the following properties:
- Integers in each **row** are sorted in ascending order from left to right.
- Integers in each **column** are sorted in ascending order from top to bottom.

Return `true` if `target` is found in the matrix, otherwise return `false`.

**LeetCode #240**

**Constraints:**
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= m, n <= 300`
- `-10^9 <= matrix[i][j] <= 10^9`
- All integers in each row are sorted in ascending order.
- All integers in each column are sorted in ascending order.
- `-10^9 <= target <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5` | `true` | 5 is present at matrix[1][1] |
| `matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20` | `false` | 20 is not present anywhere in the matrix |

### Real-Life Analogy
> *Imagine a spreadsheet where employee salaries are listed. Rows are sorted by department (increasing budgets) and columns by seniority (increasing pay). You want to find if anyone earns exactly $75,000. Starting from the top-right corner: if the salary is too high, move left (lower seniority in the same department). If too low, move down (higher budget department). Each step eliminates an entire row or column, converging quickly.*

### Key Observations
1. Unlike "Search a 2D Matrix I" (LC #74), the first element of each row is NOT necessarily greater than the last element of the previous row. You cannot flatten the matrix into a single sorted array.
2. The **top-right corner** (or bottom-left corner) is special: moving left decreases the value, moving down increases it. This gives a binary-search-like elimination at each step.
3. Each step eliminates either one row or one column, so at most `m + n` steps.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No extra data structure needed. We exploit the sorted properties of the matrix itself.
- The staircase search uses the matrix's row and column ordering to prune the search space.

### Pattern Recognition
- **Pattern:** Staircase Search (Two-Pointer on 2D Sorted Matrix)
- **Classification Cue:** "Rows sorted and columns sorted independently (not globally) --> staircase from corner."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Check every element in the matrix.

**Steps:**
1. For each row in the matrix:
   - For each element in the row:
     - If element == target, return true.
2. Return false.

**BUD Transition -- Bottleneck:** We are ignoring the sorted properties entirely. Using the sorted order, we can eliminate large chunks of the matrix at each step.

| Time | Space |
|------|-------|
| O(m * n) | O(1) |

### Approach 2: Optimal -- Staircase Search (Top-Right Corner)
**What changed:** Start from the top-right corner `matrix[0][n-1]`. At each position:
- If current == target: found.
- If current > target: move left (eliminate current column -- all elements below are even larger).
- If current < target: move down (eliminate current row -- all elements to the left are even smaller).

**Steps:**
1. Set `row = 0`, `col = n - 1`.
2. While `row < m` and `col >= 0`:
   - If `matrix[row][col] == target`: return true.
   - If `matrix[row][col] > target`: `col--`.
   - Else: `row++`.
3. Return false.

**Dry Run:** `target = 5` on example matrix:

| Step | row | col | Value | Action |
|------|-----|-----|-------|--------|
| 1    | 0   | 4   | 15    | 15 > 5, col-- |
| 2    | 0   | 3   | 11    | 11 > 5, col-- |
| 3    | 0   | 2   | 7     | 7 > 5, col-- |
| 4    | 0   | 1   | 4     | 4 < 5, row++ |
| 5    | 1   | 1   | 5     | Found! return true |

**Result:** true (5 steps)

**Why top-right or bottom-left?** These corners have one direction that increases and one that decreases, enabling binary-search-like decisions. Top-left has both directions increasing (cannot decide), and bottom-right has both decreasing (same problem).

| Time | Space |
|------|-------|
| O(m + n) | O(1) |

### Approach 3: Best -- Binary Search Per Row
**What changed:** For each row, check if target could be in that row (between first and last element). If so, binary search within the row. Skip rows entirely if target is out of range.

**Steps:**
1. For each row:
   - If `row[0] > target`: break (all remaining rows have even larger values due to column sorting).
   - If `row[n-1] < target`: continue (target is not in this row).
   - Binary search for target in this row.
2. Return false if not found.

**Complexity Note:** This approach is O(m * log(n)), which is actually worse than O(m + n) for square matrices. However, it can be faster when `m` is much smaller than `n` (wide matrices), and the early-skip optimization prunes many rows. In practice, the staircase approach (Approach 2) is superior and is the intended solution. This approach is included as an alternative that demonstrates applying binary search to 2D problems.

| Time | Space |
|------|-------|
| O(m * log(n)) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(m + n) for the staircase approach -- "Each step moves either down or left. We can move down at most m times and left at most n times. So at most m + n steps total."
**Space:** O(1) -- "Only two integer variables for row and column."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Starting from the wrong corner:** Top-left or bottom-right does not work because both directions go the same way (both increase or both decrease).
2. **Confusing with LC #74:** In that problem, the matrix is globally sorted and you can treat it as a flat sorted array. Here, rows and columns are independently sorted.
3. **Forgetting bounds check:** Must check `row < m` and `col >= 0` before accessing matrix elements.

### Edge Cases to Test
- [ ] Target at a corner: `target = matrix[0][0]` or `target = matrix[m-1][n-1]`
- [ ] Target not present but within range
- [ ] Single-element matrix: `[[5]], target = 5` --> true
- [ ] Single row or single column matrix
- [ ] Target smaller than minimum or larger than maximum

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Each row and column is independently sorted. Find target."
2. **Match:** "The top-right corner lets me eliminate a row or column each step -- staircase search."
3. **Plan:** "Start at (0, n-1). Move left if too large, down if too small."
4. **Implement:** Simple while loop with two pointers.
5. **Review:** Walk through dry run for target = 5.
6. **Evaluate:** "O(m + n) time, O(1) space. Cannot do better since we might need to visit a full row + column."

### Follow-Up Questions
- "Can you prove O(m + n) is optimal?" --> Yes, by adversarial argument: an adversary can force you to visit at least m + n - 1 cells.
- "What about Search in 2D Matrix I?" --> Different structure (globally sorted), use single binary search on flattened index.
- "What if you need to find all occurrences?" --> Staircase still works; do not return on first match, record it and continue (move both left and down).
- "Can you solve this with divide and conquer?" --> Yes, split into quadrants, eliminate one quadrant, recurse on three. T(k) = 3T(k/4) + O(1), giving O(n^log_4(3)) ~ O(n^0.79). Interesting but impractical vs O(m+n).

---

## CONNECTIONS
- **Prerequisite:** Binary Search basics, Search in 2D Matrix I (LC #74)
- **Same Pattern:** Kth Smallest Element in Sorted Matrix (LC #378) uses similar matrix properties
- **Harder Variant:** Count Negative Numbers in Sorted Matrix (LC #1351) -- similar staircase
- **This Unlocks:** Understanding the staircase technique for any row-sorted + column-sorted matrix
