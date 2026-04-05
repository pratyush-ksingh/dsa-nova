# Maximum Sum Square SubMatrix

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an N x N integer matrix `A` and an integer `B`, find the maximum possible sum of any B x B contiguous square submatrix within `A`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| A=[[1,2],[3,4]], B=2 | 10 | Only one 2x2 submatrix: 1+2+3+4=10 |
| A=[[1,2,3,4],[5,6,7,8],[9,10,11,12],[13,14,15,16]], B=2 | 54 | Bottom-right 2x2: 11+12+15+16=54 |
| A=[[1,-2,3],[-4,5,-6],[7,-8,9]], B=2 | 5 | Top-right 2x2: -2+3+5+(-6)=0; center 2x2: 5-6-8+9=0; bottom-right: -6+9-8+... varies — max is 7 (bottom-left: -4+5+7+(-8)=0, top-left: 1-2-4+5=0) |

## Constraints

- 1 <= N <= 1000
- 1 <= B <= N
- -10^9 <= A[i][j] <= 10^9

---

## Approach 1: Brute Force

**Intuition:** Enumerate every possible B x B submatrix by iterating over all valid top-left corners. For each corner, sum all B*B elements directly. Keep track of the maximum.

**Steps:**
1. Loop `i` from 0 to `n-B` (valid top-left row)
2. Loop `j` from 0 to `n-B` (valid top-left column)
3. For each `(i, j)`, sum all elements `A[r][c]` where `r in [i, i+B)` and `c in [j, j+B)`
4. Update `max_sum` if the current sum is larger
5. Return `max_sum`

| Metric | Value |
|--------|-------|
| Time   | O(n^2 * B^2) |
| Space  | O(1) |

---

## Approach 2: Optimal — 2D Prefix Sum

**Intuition:** Precompute a 2D prefix sum table so that any rectangle sum can be retrieved in O(1) using the inclusion-exclusion formula. Building the table takes O(n^2) and querying all (n-B+1)^2 submatrices takes O(n^2) total.

**Steps:**
1. Build `prefix[n+1][n+1]` (1-indexed) where `prefix[i][j]` = sum of all elements in rows [1..i], columns [1..j]
   - `prefix[i][j] = A[i-1][j-1] + prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1]`
2. For each bottom-right corner `(i, j)` where `i >= B` and `j >= B`:
   - `sum = prefix[i][j] - prefix[i-B][j] - prefix[i][j-B] + prefix[i-B][j-B]`
3. Track and return the maximum sum

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n^2) |

---

## Approach 3: Best — Column Prefix Sums + Sliding Window

**Intuition:** Reduce space to O(n) by computing column prefix sums. For each band of B consecutive rows, derive each column's contribution in O(1), then use a sliding window of width B across the columns to evaluate each B x B block in amortized O(1).

**Steps:**
1. Build `col_prefix[n+1][n]` where `col_prefix[i][j]` = sum of column j from row 0 to i-1
2. For each bottom row `i` of a B-row band (i from B-1 to n-1):
   - Compute `col_sums[j] = col_prefix[i+1][j] - col_prefix[i-B+1][j]` for all j
   - Initialize sliding window sum over `col_sums[0..B-1]`
   - Slide the window right: add `col_sums[j+B-1]`, subtract `col_sums[j-1]`
   - Update max at each step
3. Return max

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) extra (column prefix table uses O(n^2) but can be replaced with O(n) rolling array) |

---

## Real-World Use Case

**Image Processing / Computer Vision:** Finding the brightest or most information-dense B x B region in a grayscale image is exactly this problem. Algorithms like sliding-window object detection (pre-deep-learning) and thermal hotspot detection in circuit boards use 2D prefix sums to efficiently scan every fixed-size window.

**Heatmap Analysis:** In data analytics dashboards (e.g., web click heatmaps, sensor grids), finding the most active B x B geographic cell uses the same prefix-sum technique.

## Interview Tips

- The 2D prefix sum formula `sum(r1,c1,r2,c2) = P[r2][c2] - P[r1-1][c2] - P[r2][c1-1] + P[r1-1][c1-1]` is a must-know; practice it until it's automatic.
- The brute force is O(n^2 * B^2) — for large B, this is much worse than O(n^2). Make sure to mention this distinction.
- The column-prefix + sliding-window approach (Approach 3) is the most memory-efficient and shows deep algorithmic thinking. It impresses senior interviewers.
- A common mistake is off-by-one errors in the prefix table (1-indexed vs 0-indexed). Be explicit about your indexing convention.
- If the matrix is not square (M x N), the same technique applies — just adjust the loop bounds.
