# Distinct Subsequences

> **Step 16.16.5** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 115

## Problem Statement

Given two strings `s` and `t`, return the **number of distinct subsequences** of `s` which equal `t`.

A **subsequence** is formed by deleting some (or no) characters from `s` without changing the relative order of the remaining characters. Two subsequences are **distinct** if they differ in at least one chosen index from `s`.

**Input:** Strings `s` (source) and `t` (target).
**Output:** Integer — number of distinct ways to form `t` as a subsequence of `s`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| s="rabbbit", t="rabbit" | 3 | Three ways to delete one of the three 'b's |
| s="babgbag", t="bag" | 5 | Five distinct index subsets produce "bag" |
| s="aaa", t="aa" | 3 | Choose any 2 of the 3 'a's: C(3,2)=3 ways |
| s="b", t="a" | 0 | 'a' not in s |
| s="a", t="" | 1 | Empty target matched by any string in exactly 1 way |

## Constraints

- 1 <= s.length <= 500
- 1 <= t.length <= 100
- `s` and `t` consist of lowercase English letters
- Answer fits in a 32-bit signed integer

---

## Approach 1: Brute Force — Pure Recursion

**Intuition:** For each character `s[i]`, decide: skip it (move to `s[i+1]`) or use it to match `t[j]` (if they're equal). Sum both choices when characters match. No memoization means exponential recomputation.

**Steps:**
1. Define `rec(i, j)`: number of ways to match `t[j:]` using characters from `s[i:]`.
2. Base cases:
   - `j == len(t)`: return 1 (full t matched).
   - `i == len(s)`: return 0 (s exhausted but t remains).
3. Always add `rec(i+1, j)` (skip `s[i]`).
4. If `s[i] == t[j]`, also add `rec(i+1, j+1)` (use `s[i]`).

| Metric | Value |
|--------|-------|
| Time   | O(2^m) where m = len(s) |
| Space  | O(m) recursion stack |

---

## Approach 2: Optimal — 2D Bottom-Up DP

**Intuition:** Build a 2D table where `dp[i][j]` = number of distinct subsequences of `s[0..i-1]` that equal `t[0..j-1]`.

**Recurrence:**
- `dp[i][0] = 1` for all i: empty `t` is always matched.
- `dp[0][j] = 0` for j > 0: empty `s` can't match non-empty `t`.
- If `s[i-1] == t[j-1]`: `dp[i][j] = dp[i-1][j-1] + dp[i-1][j]`
  (use `s[i-1]` to match `t[j-1]`) + (skip `s[i-1]`)
- Else: `dp[i][j] = dp[i-1][j]` (must skip `s[i-1]`)

**Steps:**
1. Initialize `dp[0..m][0] = 1`, rest = 0.
2. Fill table row by row (i from 1 to m, j from 1 to n).
3. Return `dp[m][n]`.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) |

---

## Approach 3: Best — 1D Space-Optimized DP

**Intuition:** In the 2D recurrence, `dp[i][j]` only depends on `dp[i-1][j]` and `dp[i-1][j-1]` — the current and previous value one column left. We can compress to a 1D array by traversing `j` from **right to left**, so `dp[j-1]` still holds the previous row's value when we compute `dp[j]`.

**Steps:**
1. `dp = [0] * (n+1)`, set `dp[0] = 1`.
2. For each character `s[i]` (i from 0 to m-1):
   - Traverse `j` from `n` down to `1`.
   - If `s[i] == t[j-1]`: `dp[j] += dp[j-1]`.
3. Return `dp[n]`.

Right-to-left traversal ensures `dp[j-1]` is still the "old" (previous row) value when used to update `dp[j]`.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(n) |

---

## Real-World Use Case

**Natural Language Processing — sentence generation counting:** In compiler design and NLP, counting the number of distinct parse trees (derivations) for a grammar is structurally similar to counting distinct subsequences. The problem also appears in:
- **DNA sequence analysis:** Counting distinct alignments of a short read (t) within a genome (s).
- **Fuzzy string matching:** Determining how many ways a pattern can appear as a scattered subsequence in a document (for plagiarism detection or fuzzy search).
- **Combinatorics / coding theory:** Counting codewords that are subsequences of a transmitted string — relevant in DNA storage systems.

---

## Interview Tips

- Clearly distinguish "substring" (contiguous) from "subsequence" (non-contiguous, order preserved). LeetCode 115 is the **subsequence** version.
- The base case `dp[i][0] = 1` is easy to forget: empty target `t` is always a valid subsequence (matched 0 times = delete everything from s).
- In the 1D version, traversing right-to-left is the same trick used in 0/1 Knapsack — the direction ensures you don't reuse a character from `s` twice.
- The answer can get large — use `long` (int64) intermediately in Java even if the final answer fits in int32.
- A common variant: "Does `t` appear as a subsequence?" — that's just checking if `best(s, t) >= 1`, equivalent to the two-pointer O(m) check.
- If asked to find the indices forming one such subsequence, use backtracking on the 2D DP table from `dp[m][n]`.
