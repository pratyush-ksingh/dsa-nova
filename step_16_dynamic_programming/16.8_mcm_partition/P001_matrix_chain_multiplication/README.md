# Matrix Chain Multiplication

> **Step 16.8** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

Given an array `arr[]` of `n+1` integers where `arr[i-1]` and `arr[i]` represent the number of rows and columns of the i-th matrix respectively, find the minimum number of scalar multiplications needed to compute the product of the entire matrix chain.

Parenthesization matters: `(A*B)*C` and `A*(B*C)` give the same result but may require very different numbers of multiplications. Choose the optimal split.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [10, 30, 5, 60]` | `4500` | Matrices: A(10x30), B(30x5), C(5x60). `(AB)C` = 10*30*5 + 10*5*60 = 1500+3000 = 4500. `A(BC)` = 30*5*60 + 10*30*60 = 27000. Minimum = 4500 |
| `arr = [40, 20, 30, 10, 30]` | `26000` | 4 matrices; optimal split gives 26000 |
| `arr = [10, 20, 30]` | `6000` | 2 matrices: 10*20*30 = 6000 |
| `arr = [1, 2, 3, 4]` | `18` | Best: `(AB)C` = 1*2*3 + 1*3*4 = 6+12 = 18 |

## Constraints

- `2 <= n <= 100` (n = number of matrices = len(arr) - 1)
- `1 <= arr[i] <= 500`
- Answer fits in a 32-bit integer

---

## Approach 1: Brute Force (Pure Recursion)

**Intuition:** For every sub-chain of matrices from index `i` to `j`, try every possible split point `k`. The cost of a split is: cost(i..k) + cost(k+1..j) + rows[i] * cols[k] * cols[j]. The number of distinct parenthesizations is the Catalan number C(n-1), which grows exponentially.

**Steps:**
1. Define `recurse(i, j)` = minimum multiplications for chain i..j.
2. Base case: `i == j` → return 0 (single matrix, no work).
3. For each split `k` from `i` to `j-1`: compute cost and take minimum.
4. The outer multiply cost at split k is `arr[i-1] * arr[k] * arr[j]`.

| Metric | Value |
|--------|-------|
| Time   | O(2^n) — exponential, Catalan growth |
| Space  | O(n) recursion depth |

---

## Approach 2: Optimal (Top-Down DP / Memoization)

**Intuition:** Many sub-problems in the brute-force are recomputed (e.g., `dp[2][4]` is needed for multiple outer splits). Cache results in a 2-D table so each sub-problem is solved only once. Total distinct sub-problems = O(n^2), each takes O(n) work at the split loop → O(n^3) total.

**Steps:**
1. Allocate `memo[n+1][n+1]` initialized to -1.
2. Same recursive structure as brute force, but check `memo[i][j]` first.
3. Store the result in `memo[i][j]` before returning.
4. Call `dp(1, n)` for the full chain (1-indexed to match arr[] indexing).

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(n^2) memo table + O(n) stack |

---

## Approach 3: Best (Bottom-Up DP on chain length)

**Intuition:** Same recurrence as Approach 2, but build the table iteratively by sub-chain length. Start with length 2 (pairs), then 3 (triples), and so on up to n. When we compute `dp[i][j]` for a chain of length L, all sub-chains of length < L are already computed — so we never hit an uninitialized cell. Eliminates recursion overhead and is the standard interview answer.

**Steps:**
1. Allocate `dp[n+1][n+1]`, initialize all cells to 0.
2. Outer loop: `length` from 2 to n (chain length).
3. Middle loop: `i` from 1 to `n - length + 1`; set `j = i + length - 1`.
4. Inner loop: try each split `k` from `i` to `j-1`, updating `dp[i][j]`.
5. Answer is `dp[1][n]`.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(n^2) — no stack overhead |

---

## Real-World Use Case

**Compiler optimization / numerical libraries:** When a program multiplies a sequence of matrices (e.g., in graphics pipelines for 3-D transformations, or in deep-learning frameworks computing Jacobians), the runtime depends heavily on the order of operations. BLAS/LAPACK and frameworks like NumPy and TensorFlow use MCM-style analysis internally to reorder operations and minimize FLOPs. The same technique applies to optimizing relational database query plans, where "joining tables" in the wrong order can be orders of magnitude slower.

---

## Interview Tips

- The key insight is recognizing `dp[i][j]` and that the split cost uses the **outer dimensions** `arr[i-1]`, `arr[k]`, `arr[j]` — not the inner dimensions of the sub-chains.
- Always 1-index the matrices to align with the arr[] convention (arr[i-1] x arr[i] for matrix i).
- Bottom-up by chain length is the cleanest solution; start by writing the recurrence and then flip it to iterative.
- Be careful: the split point `k` runs from `i` to `j-1` (not `j`), and the outer multiply `arr[i-1] * arr[k] * arr[j]` represents multiplying the result of left sub-chain (rows `arr[i-1]`, cols `arr[k]`) with the result of right sub-chain (rows `arr[k]`, cols `arr[j]`).
- Related problems: Burst Balloons, Min Cost to Cut Stick, Palindrome Partitioning II — all use the same interval DP template.
- Catalan number C(n) ≈ 4^n / (n^1.5 * sqrt(π)) — good to know for explaining brute-force complexity.
