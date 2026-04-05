# Minimum Falling Path Sum

> **Step 16.16.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #931)** Given an `n x n` array of integers `matrix`, return the minimum sum of any **falling path** through `matrix`. A falling path starts at any element in the first row and chooses the element in the next row that is either directly below or diagonally left/right below (from `matrix[r][c]` you can move to `matrix[r+1][c-1]`, `matrix[r+1][c]`, or `matrix[r+1][c+1]`).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [[2,1,3],[6,5,4],[7,8,9]] | 13 | Path: 1 -> 5 -> 7 = 13 |
| [[-19,57],[-40,-5]] | -59 | Path: -19 -> -40 = -59 |
| [[-48]] | -48 | Single element |

```
Example: [[2,1,3],[6,5,4],[7,8,9]]

    2   1   3
    6   5   4
    7   8   9

Best path: 1 -> 5 -> 7 = 13
```

### Constraints
- `n == matrix.length == matrix[i].length`
- `1 <= n <= 100`
- `-100 <= matrix[i][j] <= 100`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** From each cell in the last row, recurse upward trying all 3 parent cells (up-left, up, up-right). Return the minimum across all starting columns in the last row.

**Steps:**
1. Define `solve(r, c)`: min falling path sum ending at `(r, c)`
2. Base: `r == 0` returns `matrix[0][c]`; out of bounds returns infinity
3. Recurse: `matrix[r][c] + min(solve(r-1, c-1), solve(r-1, c), solve(r-1, c+1))`
4. Answer: `min(solve(n-1, c) for all c)`

| Metric | Value |
|--------|-------|
| Time   | O(3^n) -- 3 branches at each of n levels |
| Space  | O(n) -- recursion stack depth |

---

### Approach 2: Optimal -- DP Tabulation

**Intuition:** Build a 2D DP table row by row. `dp[r][c]` stores the minimum falling path sum ending at cell `(r, c)`. Each cell looks at the 3 cells above it. Final answer is the minimum of the last row.

**Steps:**
1. Initialize `dp[0][c] = matrix[0][c]` for all columns
2. For row `r` from 1 to n-1, for each column `c`:
   - `dp[r][c] = matrix[r][c] + min(dp[r-1][c-1], dp[r-1][c], dp[r-1][c+1])`
   - Handle boundaries (first/last column have only 2 parents)
3. Return `min(dp[n-1])`

```
Dry-run: [[2,1,3],[6,5,4],[7,8,9]]

dp[0] = [2, 1, 3]
dp[1][0] = 6 + min(INF, 2, 1) = 7
dp[1][1] = 5 + min(2, 1, 3)   = 6
dp[1][2] = 4 + min(1, 3, INF) = 5
dp[2][0] = 7 + min(INF, 7, 6) = 13
dp[2][1] = 8 + min(7, 6, 5)   = 13
dp[2][2] = 9 + min(6, 5, INF) = 14

Answer = min(13, 13, 14) = 13
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n^2) |

---

### Approach 3: Best -- Space-Optimized 1D DP

**Intuition:** Each row only depends on the previous row, so maintain just one 1D array `prev`. Create a new `curr` array for each row to avoid corrupting values mid-computation.

**Steps:**
1. `prev = matrix[0][:]`
2. For each row `r` from 1 to n-1:
   - Create new `curr` array
   - `curr[c] = matrix[r][c] + min(prev[c-1], prev[c], prev[c+1])`
   - Set `prev = curr`
3. Return `min(prev)`

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) |

---

## Real-World Use Case
**Manufacturing pipeline optimization:** A factory has n stages with n machines each. Each stage's machine has a processing cost, and transferring between stages depends on adjacency. Finding the minimum falling path is like finding the cheapest route through the production pipeline.

## Interview Tips
- Classic grid DP problem -- similar to triangle minimum path sum
- Start with recursion, add memoization, then show bottom-up tabulation
- Space optimization from O(n^2) to O(n) is a common follow-up
- Must use a separate `curr` array (not in-place) since left-to-right filling would corrupt needed values
- Mention that the problem is equivalent to "minimum cost path with restricted movement"
