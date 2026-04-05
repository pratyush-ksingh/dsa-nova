# Wildcard Matching

> **Step 16.16.5** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 44

## Problem Statement

Given an input string `s` and a pattern `p`, implement **wildcard pattern matching** with support for:
- `'?'` — matches any **single** character.
- `'*'` — matches any **sequence** of characters (including the empty sequence).

The matching must cover the **entire** input string (not just a prefix or suffix).

**Input:** Strings `s` (input) and `p` (pattern).
**Output:** `true` if `s` matches `p` entirely, `false` otherwise.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| s="aa", p="a" | false | Pattern doesn't cover both characters |
| s="aa", p="*" | true | '*' matches "aa" |
| s="cb", p="?a" | false | '?' matches 'c', but 'a' != 'b' |
| s="adceb", p="*a*b" | true | '*' matches "", 'a' matches 'a', '*' matches "dce", 'b' matches 'b' |
| s="acdcb", p="a*c?b" | false | No valid expansion |
| s="", p="*" | true | '*' matches empty string |
| s="", p="?" | false | '?' requires exactly one character |

## Constraints

- 0 <= s.length <= 2000
- 0 <= p.length <= 2000
- `s` contains only lowercase English letters
- `p` contains lowercase English letters, `'?'`, and `'*'`

---

## Approach 1: Brute Force — Pure Recursion

**Intuition:** Recursively match `s[i..]` against `p[j..]`. When `p[j] == '*'`, branch into: matching empty (advance `j` only) or matching one character of `s` (advance `i` only, keep `j` to match more). Exponential due to the branching factor at each `'*'`.

**Steps:**
1. `rec(i, j)`: returns true if `s[i:]` matches `p[j:]`.
2. Base cases:
   - Both exhausted: return true.
   - `p` exhausted but `s` remains: false.
   - `s` exhausted: true only if remaining `p` is all `'*'`.
3. If `p[j] == '*'`: return `rec(i, j+1)` OR `rec(i+1, j)`.
4. If `p[j] == '?'` or `p[j] == s[i]`: return `rec(i+1, j+1)`.
5. Else: return false.

| Metric | Value |
|--------|-------|
| Time   | O(2^(m+n)) worst case |
| Space  | O(m+n) recursion stack |

---

## Approach 2: Optimal — 2D Bottom-Up DP

**Intuition:** Build a 2D table where `dp[i][j]` = true if `s[0..i-1]` matches `p[0..j-1]`.

**Recurrence:**
- `dp[0][0] = true` (empty s matches empty p).
- `dp[0][j] = dp[0][j-1]` if `p[j-1] == '*'` (leading stars match empty).
- `dp[i][0] = false` for i > 0 (non-empty s can't match empty p).
- For i >= 1, j >= 1:
  - If `p[j-1] == '*'`: `dp[i][j] = dp[i-1][j]` (match one char) OR `dp[i][j-1]` (match empty).
  - If `p[j-1] == '?'` or `p[j-1] == s[i-1]`: `dp[i][j] = dp[i-1][j-1]`.
  - Else: `dp[i][j] = false`.

**Steps:**
1. Initialize `(m+1) x (n+1)` table with `dp[0][0] = true`.
2. Fill leading `'*'` in the first row.
3. Fill remaining cells row by row.
4. Return `dp[m][n]`.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) |

---

## Approach 3: Best — 1D Space-Optimized DP

**Intuition:** The 2D recurrence for `'*'` uses `dp[i-1][j]` (row above, same column) and `dp[i][j-1]` (same row, previous column). For non-`'*'` characters it uses `dp[i-1][j-1]` (diagonal). We can compress to a 1D array by:
- Saving `dp[i-1][j-1]` into a `prev` variable before overwriting.
- Processing left to right (normal order works because `dp[i-1][j]` is `dp[j]` before update, and `dp[i][j-1]` is `dp[j-1]` already updated this row).

**Steps:**
1. `dp[0] = true`, fill leading `'*'` in dp.
2. For each `s[i]`:
   - `prev = dp[0]` (saves dp[i-1][0] = false for i>0, but dp[0] was true only for i=0).
   - Set `dp[0] = false` (dp[i][0] = false for i >= 1).
   - For j from 1 to n:
     - Save `temp = dp[j]` (= dp[i-1][j]).
     - Apply recurrence using `dp[j]` (old = dp[i-1][j]), `dp[j-1]` (= dp[i][j-1]), and `prev` (= dp[i-1][j-1]).
     - `prev = temp`.
3. Return `dp[n]`.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(n) |

---

## Real-World Use Case

**File system glob patterns:** The Unix shell glob (`ls *.py`, `find . -name "test_*"`) is a direct application of wildcard matching. The shell expands `*.py` to match any filename ending in `.py`. Similarly:
- **Database LIKE queries:** SQL `LIKE 'a%b'` (% = any sequence, _ = one char) is structurally equivalent to wildcard matching.
- **Log filtering:** Monitoring tools use wildcard patterns to filter log messages (e.g., `"ERROR:*timeout*"`).
- **URL routing:** Web frameworks match URL patterns like `/users/*/posts` against request paths.
- **DNA probes:** In bioinformatics, wildcard patterns match degenerate nucleotide probes against genome sequences.

---

## Interview Tips

- Wildcard Matching (LeetCode 44) and Regular Expression Matching (LeetCode 10) are frequently confused. Key difference: in regex, `*` means "zero or more of the preceding element"; in wildcard, `*` means "any sequence" independently.
- The `'*'` transition in DP: `dp[i][j] = dp[i-1][j] || dp[i][j-1]`. The first term "extends star to consume s[i]"; the second "uses star as empty". This OR logic is what makes `'*'` powerful.
- An alternative O(m) space, O(mn) time approach uses a **greedy two-pointer**: track the last `'*'` position in `p` and backtrack when mismatch occurs. This avoids the DP table entirely but is harder to implement correctly.
- A common edge case: `p = "***"` matching any string — ensure leading stars are initialized correctly in `dp[0][j]`.
- Another edge case: `s = ""` and `p = "*"` → true. `s = ""` and `p = "?"` → false.
- After coding, always walk through the `s="aa", p="*"` and `s="cb", p="?a"` cases to verify boundary conditions.
