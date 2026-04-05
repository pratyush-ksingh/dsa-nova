# Evaluate Boolean Expression to True

> **Step 16.8** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED
> **GFG:** [Boolean Parenthesization](https://www.geeksforgeeks.org/boolean-parenthesization-problem-dp-37/)

## Problem Statement

Given a boolean expression string consisting of:
- **Operands:** `T` (True) and `F` (False)
- **Operators:** `&` (AND), `|` (OR), `^` (XOR)

The expression always has the form: `operand op operand op operand ...` (length always odd).

Count the **number of ways to parenthesize** the expression so that the whole thing evaluates to **True**.

Two parenthesizations are different if the grouping structure (parse tree) is different, even if they produce the same result.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"T\|F"` | `1` | Only one way: `(T\|F)` = True |
| `"T&F"` | `0` | Only one way: `(T&F)` = False. Zero ways yield True. |
| `"T^T"` | `0` | XOR of same values is False. |
| `"T\|F&T^F"` | `5` | All 5 distinct parenthesizations evaluate to True. |
| `"T"` | `1` | Single operand T = True. One way. |

## Constraints

- `1 <= len(expression) <= 200` (always odd: alternating operands and operators)
- Expression only contains `T`, `F`, `&`, `|`, `^`
- Operands are at even indices (0, 2, 4, ...), operators at odd indices (1, 3, 5, ...)

---

## Approach 1: Brute Force — Exhaustive Recursion

**Intuition:** Try every possible split point (operator) to divide the expression into a left and right sub-expression. Recursively count how many ways each sub-expression evaluates to True and False, then combine the counts based on the operator. No caching — the same sub-expressions are recomputed many times.

**Operator combination rules:**

| Operator | True count | False count |
|----------|-----------|-------------|
| `&` | `lt * rt` | `lt*rf + lf*rt + lf*rf` |
| `\|` | `lt*rt + lt*rf + lf*rt` | `lf * rf` |
| `^` | `lt*rf + lf*rt` | `lt*rt + lf*rf` |

Where `lt/lf` = left true/false counts, `rt/rf` = right true/false counts.

**Steps:**
1. Define `recurse(lo, hi)` → `(trueCount, falseCount)` for `expression[lo..hi]`.
2. Base case: `lo == hi` → single operand; return `(1,0)` if T, `(0,1)` if F.
3. For each operator index `k` in `range(lo+1, hi, 2)`:
   - Recurse on `[lo, k-1]` and `[k+1, hi]`.
   - Apply combination table based on `expression[k]`.
4. Sum across all splits, return total `(trueCount, falseCount)`.
5. Final answer is `trueCount` for the full expression.

| Metric | Value |
|--------|-------|
| Time   | O(4^n) — Catalan number of parenthesizations, exponential splits |
| Space  | O(n) — recursion depth |

---

## Approach 2: Optimal — Top-Down Memoization

**Intuition:** The recursive calls in Approach 1 repeat the same `(lo, hi)` sub-problems multiple times. Cache the result of each `(lo, hi)` interval. There are only O(n^2) distinct intervals, and each takes O(n) work to solve (iterating over split points), giving O(n^3) total.

**Steps:**
1. Create `memo[lo][hi] = (trueCount, falseCount)`, initialized to unvisited.
2. In `recurse(lo, hi)`: check memo first, return cached value if present.
3. Otherwise compute exactly as in Approach 1, store in memo before returning.
4. Same base cases and combination logic.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) — n^2 sub-problems × O(n) splits each |
| Space  | O(n^2) — memo table + O(n) recursion stack |

---

## Approach 3: Best — Bottom-Up Tabulation DP

**Intuition:** Eliminate recursion entirely by filling a 2D DP table in order of increasing interval length (same pattern as Matrix Chain Multiplication). For gap = 0 (single operands), fill base cases directly. For gap = 2, 4, ..., combine sub-intervals using the operator in between.

**Table definition:**
- `dp_true[i][j]` = number of ways `expression[i..j]` evaluates to True
- `dp_false[i][j]` = number of ways `expression[i..j]` evaluates to False
- `i` and `j` are character indices into the expression string, both pointing to operands (even positions)

**Steps:**
1. Initialize base cases: for each operand index `i` (0, 2, 4, ...):
   - `dp_true[i][i] = 1, dp_false[i][i] = 0` if `expression[i] == 'T'`
   - `dp_true[i][i] = 0, dp_false[i][i] = 1` if `expression[i] == 'F'`
2. For `gap = 2, 4, ..., n-1`:
   - For each starting index `i` where `i + gap < n`:
     - `j = i + gap`
     - For each operator index `k` in `range(i+1, j, 2)`:
       - Read `lt, lf` from `dp[i][k-1]` and `rt, rf` from `dp[k+1][j]`
       - Apply combination rules, accumulate into `dp[i][j]`
3. Return `dp_true[0][n-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) — same asymptotic as Approach 2, but better constants (no recursion overhead) |
| Space  | O(n^2) — two n×n tables |

---

## Visualization (Example: `"T|F&T"`)

Expression indices:
```
index:  0   1   2   3   4
char:   T   |   F   &   T
```

Operands at 0, 2, 4. Operators at 1, 3.

Sub-problems (gap = 0, base):
```
[0,0]: T -> (1, 0)
[2,2]: F -> (0, 1)
[4,4]: T -> (1, 0)
```

Gap = 2:
```
[0,2]: split at k=1 (|), left=[0,0]=(1,0), right=[2,2]=(0,1)
  true  = 1*0 + 1*1 + 0*0 = 1
  false = 0*1 = 0
  -> (1, 0)

[2,4]: split at k=3 (&), left=[2,2]=(0,1), right=[4,4]=(1,0)
  true  = 0*1 = 0
  false = 0*0 + 1*1 + 1*0 = 1
  -> (0, 1)
```

Gap = 4:
```
[0,4]: split at k=1 (|): left=[0,0]=(1,0), right=[2,4]=(0,1)
         true = 1*0 + 1*1 + 0*0 = 1
       split at k=3 (&): left=[0,2]=(1,0), right=[4,4]=(1,0)
         true = 1*1 = 1
  total true = 2
```

---

## Real-World Use Case

**Query Optimizer / Boolean Satisfiability Counting**

Database query optimizers and constraint solvers face the problem of counting satisfying assignments of complex boolean expressions. For example, in logical circuit verification, you need to count the number of input configurations that make a combinational logic circuit output `1` (True). The Interval DP approach here is the foundation for polynomial-time algorithms on certain circuit topologies, and relates to the `#P`-completeness of general SAT counting.

In compiler construction, expression evaluators use similar interval decomposition when optimizing boolean short-circuit evaluation or building expression trees for code generation.

---

## Interview Tips

- The key observation is that this is **Matrix Chain Multiplication in disguise**: instead of minimizing cost of matrix multiplication order, you're counting True parenthesizations. The structure (interval DP with a split point) is identical.
- Always work with **both** trueCount and falseCount simultaneously — you need falseCount to compute trueCount when the operator is `|` or `^`.
- Memorize the combination table for the three operators. Under time pressure, derive them from first principles: total combinations for two sub-intervals = (lt+lf) * (rt+rf), and partition into True/False cases.
- The expression always has the structure `operand [op operand]*`, so operands are at even indices and operators at odd indices. Make sure your loop `k in range(lo+1, hi, 2)` steps by 2 to hit only operator positions.
- If asked to also handle parentheses in the input string, you'd need to parse it first — but usually the problem gives you a clean alternating string.
- The answer can be very large for long expressions. In practice (GFG), results may need modulo 1003. Always clarify if modular arithmetic is required.
