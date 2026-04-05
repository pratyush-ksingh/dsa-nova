# Grid Unique Paths

> **Step 01 | 1.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an `m x n` grid, count the number of unique paths from the **top-left corner** (0,0) to the **bottom-right corner** (m-1, n-1). You can only move **right** or **down** at each step.

## Examples

| Input (m, n) | Output | Explanation |
|-------------|--------|-------------|
| `3, 7`      | `28`   | C(8, 2) = 28 |
| `3, 3`      | `6`    | C(4, 2) = 6 |
| `2, 2`      | `2`    | Right-Down or Down-Right |
| `1, 1`      | `1`    | Already at destination |
| `4, 4`      | `20`   | C(6, 3) = 20 |

## Constraints

- `1 <= m, n <= 100`
- The answer will fit in a 64-bit integer for given constraints

---

## Approach 1: Brute Force — Recursion

**Intuition:** At every cell, you have two choices: move right or move down. Recursively count paths from both neighbors and sum them. The base case is any cell on the bottom row or rightmost column — only one path exists from there (keep going in one direction).

**Steps:**
1. Define `recurse(r, c)` returning number of paths from `(r, c)` to `(m-1, n-1)`.
2. Base: if `r == m-1` or `c == n-1`, return `1`.
3. Return `recurse(r+1, c) + recurse(r, c+1)`.

| Metric | Value |
|--------|-------|
| Time   | O(2^(m+n)) |
| Space  | O(m+n) recursion stack |

---

## Approach 2: Optimal — Dynamic Programming

**Intuition:** The brute force recomputes the same cells many times. Memoize by storing results in a `dp` table. `dp[i][j]` = number of paths to cell `(i,j)`. Fill row by row using the recurrence: `dp[i][j] = dp[i-1][j] + dp[i][j-1]`.

**Steps:**
1. Create `dp[m][n]` initialized to 1 (first row and column all = 1).
2. For `i` from `1` to `m-1`, `j` from `1` to `n-1`:
   `dp[i][j] = dp[i-1][j] + dp[i][j-1]`.
3. Return `dp[m-1][n-1]`.

**Space optimization:** Only one row is needed at a time — slide the dp array. Space reduces to O(n).

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) or O(n) with optimization |

---

## Approach 3: Best — Combinatorics

**Intuition:** Every path from top-left to bottom-right consists of exactly `(m-1)` down moves and `(n-1)` right moves, for a total of `(m+n-2)` moves. The number of distinct paths equals the number of ways to choose which `(m-1)` of the `(m+n-2)` moves are down moves: `C(m+n-2, m-1)`.

Computed multiplicatively to avoid large factorials: `C(N, k) = N * (N-1) * ... * (N-k+1) / k!` where `k = min(m-1, n-1)`.

**Steps:**
1. Set `N = m + n - 2`, `k = min(m, n) - 1`.
2. Compute `result = product of (N-i)/(i+1)` for `i` from `0` to `k-1`.
3. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(min(m, n)) |
| Space  | O(1) |

---

## Real-World Use Case

**Robot navigation and routing:** This classic problem models any scenario where movement is constrained to two directions, such as a delivery robot on a grid street network, counting the number of distinct routes a packet can take through a network with only forward/downward hops, or analyzing the number of distinct game paths in strategy games. The combinatorial formula is used in probability theory to calculate the number of monotonic lattice paths, which appears in ballot problems and stock price models (Catalan number relatives).

## Interview Tips

- The combinatorial formula `C(m+n-2, m-1)` is the most elegant solution — memorize it.
- The DP approach is the most commonly expected in interviews when the problem has obstacles (a variant where some cells are blocked). The combinatorial approach doesn't generalize to obstacles.
- Space optimization: DP can be done in O(n) space using a 1D rolling array.
- For the combinatorial approach, use `min(m-1, n-1)` as `k` to minimize the number of multiplications.
- Be careful with integer overflow — for m,n up to 100, N = 198, and C(198, 99) is astronomically large; use long or BigInteger if the problem doesn't say modulo.
- With obstacles: use DP only. Set `dp[i][j] = 0` for blocked cells.
