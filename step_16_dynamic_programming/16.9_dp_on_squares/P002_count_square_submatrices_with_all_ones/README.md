# Count Square Submatrices with All Ones

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an `m x n` binary matrix `matrix`, return the total number of **square submatrices** that have **all ones**.

*(LeetCode #1277)*

**Constraints:**
- `1 <= m, n <= 300`
- `matrix[i][j]` is `0` or `1`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[0,1,1,1],[1,1,1,1],[0,1,1,1]]` | `15` | Ten 1x1 squares, four 2x2 squares, one 3x3 square = 15 |
| `[[1,0,1],[1,1,0],[1,1,0]]` | `7` | Six 1x1 squares + one 2x2 square = 7 |
| `[[1]]` | `1` | Single 1x1 square |
| `[[0]]` | `0` | No all-ones squares |

### Real-Life Analogy
> *Imagine a tiled floor where some tiles are white (1) and some are black (0). You want to count how many square regions of ALL white tiles exist. A 1x1 white tile counts as one. A 2x2 all-white region counts as another. A 3x3 all-white region is yet another. The key insight: if a cell can be the bottom-right corner of a 3x3 all-white square, it is ALSO the corner of a 2x2 and a 1x1 -- so that cell alone contributes 3 squares.*

### Key Observations
1. **dp[i][j] = size of largest all-ones square with (i,j) as bottom-right corner.** The value dp[i][j] also tells us exactly how many squares end at that cell (a cell that can anchor a 3x3 square also anchors 2x2 and 1x1). <-- This is the "aha" insight
2. **Recurrence:** `dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1` when `matrix[i][j] == 1`. The minimum of the three neighbors limits the square size. <-- This is the "aha" insight
3. **Answer = sum of all dp[i][j]:** Since dp[i][j] counts squares ending at (i,j), the total count is the sum across the entire dp table.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** The square at (i,j) depends on squares at (i-1,j), (i,j-1), (i-1,j-1).
- **Optimal substructure:** The largest square at (i,j) is limited by the smallest of its three neighbors.
- This is the same recurrence as **Maximal Square** (LeetCode #221), but here we sum instead of taking the max.

### Pattern Recognition
- **Pattern:** 2D DP on matrix with neighbor-dependent recurrence
- **Classification Cue:** "When you see _count all-ones squares_ --> think _dp[i][j] = min of three neighbors + 1, sum the dp table_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Plain Recursion / Enumeration)
**Idea:** For every cell (i,j) and every possible square size `k`, check if the `k x k` square starting at (i,j) is all ones. Count all valid squares.

**Steps:**
1. For each cell (i,j) where matrix[i][j] == 1:
   - For `k = 1, 2, 3, ...` (as long as square fits in bounds):
     - Check if the entire `k x k` region starting at (i,j) is all 1s.
     - If yes, increment count. If no, break (larger squares will also fail).
2. Return total count.

| Time | Space |
|------|-------|
| O(m * n * min(m,n)^2) | O(1) |

**BUD Transition:** We recheck many overlapping regions. Use DP to build on previous results.

### Approach 2: Memoization / Recursive DP
**What changed:** Define `solve(i, j)` = size of the largest all-ones square ending at (i,j). Recursively compute from three neighbors.

**Recurrence:**
```
solve(i, j):
  if i < 0 or j < 0: return 0
  if matrix[i][j] == 0: return 0
  return min(solve(i-1,j), solve(i,j-1), solve(i-1,j-1)) + 1
```

**Steps:**
1. For each cell (i,j), call `solve(i,j)`.
2. Sum all results.

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) cache + recursion stack |

**BUD Transition:** Convert to bottom-up to avoid recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[m][n]` table iteratively from top-left to bottom-right.

**Steps:**
1. Initialize `dp[i][j] = matrix[i][j]` for first row and first column.
2. For `i = 1` to `m-1`, for `j = 1` to `n-1`:
   - If `matrix[i][j] == 1`:
     - `dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1`
   - Else: `dp[i][j] = 0`
3. Return sum of all `dp[i][j]`.

**Dry Run:** `matrix = [[0,1,1,1],[1,1,1,1],[0,1,1,1]]`

|   | j=0 | j=1 | j=2 | j=3 |
|---|-----|-----|-----|-----|
| i=0 | 0 | 1 | 1 | 1 |
| i=1 | 1 | 1 | 2 | 2 |
| i=2 | 0 | 1 | 2 | 3 |

Sum = 0+1+1+1+1+1+2+2+0+1+2+3 = **15**

Verification: 10 ones (1x1) + 4 two-by-two squares + 1 three-by-three = 15.

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** We only need the previous row. Optimize to O(n) space.

### Approach 4: Space Optimized
**What changed:** Use a single 1D array `prev[n]` plus one variable for the diagonal.

**Steps:**
1. Process first row: `prev[j] = matrix[0][j]`, add to count.
2. For `i = 1` to `m-1`:
   - Create `curr[n]`.
   - `curr[0] = matrix[i][0]`, add to count.
   - For `j = 1` to `n-1`:
     - If `matrix[i][j] == 1`:
       - `curr[j] = min(prev[j], curr[j-1], prev[j-1]) + 1`
     - Else: `curr[j] = 0`
     - Add `curr[j]` to count.
   - `prev = curr`
3. Return count.

| Time | Space |
|------|-------|
| O(m * n) | **O(n)** |

---

## COMPLEXITY -- INTUITIVELY
**Brute Force:** "For each cell, check up to min(m,n) square sizes, each check is O(k). Total roughly O(m * n * min(m,n)^2)."
**DP:** "Each cell computed once in O(1) from three neighbors. Total O(m * n)."
**Space Optimized:** "Same time, but only store one row plus current row: O(n) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting first row/column base cases** -- cells on the boundary can only be 1x1 squares.
2. **Using max instead of min** -- the square size is LIMITED by the smallest neighbor, not the largest.
3. **Not summing dp values** -- dp[i][j] counts squares ending at (i,j). The TOTAL count is the sum across all cells.
4. **Modifying the input matrix** -- the in-place approach works but the interviewer may prefer a separate dp table.

### Edge Cases to Test
- [ ] All zeros `[[0,0],[0,0]]` --> 0
- [ ] All ones `[[1,1],[1,1]]` --> 5 (four 1x1 + one 2x2)
- [ ] Single cell `[[1]]` --> 1
- [ ] Single row `[[1,1,1]]` --> 3
- [ ] Single column `[[1],[1],[1]]` --> 3
- [ ] L-shape of ones (no 2x2 possible) --> count of 1s

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Count total squares, not just the maximal one? Binary matrix?"
2. **Key insight:** "dp[i][j] = min of three neighbors + 1. This gives the largest square ending at (i,j), and that same value tells us how many squares end there."
3. **Code tabulation** -- clean, simple, and efficient.
4. **Mention:** "This is the same recurrence as Maximal Square (LeetCode #221), just sum instead of max."

### Follow-Up Questions
- "Largest square?" --> Take max of dp[i][j] instead of sum (LeetCode #221).
- "Count rectangles?" --> Much harder, different approach (histogram-based).
- "What if matrix values can be > 1?" --> Only consider cells with value 1, same logic.

---

## CONNECTIONS
- **Prerequisite:** Maximal Rectangle (P001 in this section), Maximal Square (LeetCode #221)
- **Same Pattern:** 2D DP with min-of-neighbors recurrence
- **This Unlocks:** Understanding how square DP generalizes to rectangle problems
- **Harder Variant:** Count submatrices with all ones (rectangles, not just squares)
