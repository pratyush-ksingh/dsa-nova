# Palindrome Partitioning II

> **Step 16.8** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED
> **LeetCode:** [132. Palindrome Partitioning II](https://leetcode.com/problems/palindrome-partitioning-ii/)

## Problem Statement

Given a string `s`, partition it such that every substring of the partition is a **palindrome**.

Return the **minimum number of cuts** needed for a palindrome partitioning of `s`.

A cut separates two adjacent characters. A partition of a string into `k` palindromes requires exactly `k-1` cuts.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"aab"` | `1` | `["aa","b"]` — 1 cut. `"aa"` and `"b"` are palindromes. |
| `"a"` | `0` | Already a palindrome, no cuts needed. |
| `"ab"` | `1` | `["a","b"]` — must split into single characters. |
| `"aa"` | `0` | `"aa"` is a palindrome, no cut needed. |
| `"abcba"` | `0` | Whole string is a palindrome. |
| `"aabb"` | `1` | `["aa","bb"]` — 1 cut. |

## Constraints

- `1 <= s.length <= 2000`
- `s` consists of only lowercase English letters

---

## Approach 1: Brute Force — Recursive Exhaustive Search

**Intuition:** Try all possible cut positions recursively. Starting from position `start`, for every possible endpoint `end`, check if `s[start..end]` is a palindrome. If it is, make a cut here and recursively find the minimum cuts for the remaining suffix `s[end+1..n-1]`. The minimum over all valid cut positions is the answer.

**Steps:**
1. Define `recurse(start)` → minimum cuts for `s[start..n-1]`.
2. Base case: `start == n` (empty) → 0 cuts. Or if `s[start..n-1]` is a palindrome → 0 cuts.
3. For each `end` from `start` to `n-2`:
   - If `s[start..end]` is a palindrome: `cuts = 1 + recurse(end + 1)`
   - Track minimum cuts.
4. Return minimum cuts found.
5. Palindrome check is O(n) per call.

| Metric | Value |
|--------|-------|
| Time   | O(2^n * n) — exponential subsets × O(n) palindrome check per call |
| Space  | O(n) — recursion depth |

---

## Approach 2: Optimal — Palindrome Precomputation + 1D DP

**Intuition:** Two key observations:
1. We repeatedly check if a substring is a palindrome — precompute a 2D boolean table `pal[i][j]` in O(n^2) so each query is O(1).
2. Define `dp[i]` = minimum cuts for `s[0..i]`. Build this 1D array left to right: for each position `i`, check all split points `j` where `s[j..i]` is a palindrome, and update `dp[i] = min(dp[j-1] + 1)`.

**Steps:**
1. **Palindrome table** (fill bottom-up by length):
   - `pal[i][i] = true` for all i (single char)
   - `pal[i][i+1] = (s[i] == s[i+1])` for all i
   - `pal[i][j] = (s[i] == s[j]) && pal[i+1][j-1]` for length >= 3
2. **DP table**:
   - `dp[i] = 0` if `pal[0][i]` (no cuts needed, whole prefix is palindrome)
   - Otherwise `dp[i] = i` (worst case initialization: i cuts)
   - For each `j` from 1 to i: if `pal[j][i]` is true, `dp[i] = min(dp[i], dp[j-1] + 1)`
3. Return `dp[n-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) — O(n^2) for palindrome table + O(n^2) for DP |
| Space  | O(n^2) — palindrome table dominates |

---

## Approach 3: Best — Expand Around Center + DP

**Intuition:** Instead of building the full O(n^2) palindrome table upfront, discover palindromes "on the fly" by expanding around each center. For each center (there are 2n-1 centers for odd/even length palindromes), expand outward while characters match. Each expansion immediately gives us a new palindrome `s[left..right]` that we can use to update `dp[right]`.

This achieves the same O(n^2) time but with only O(n) space — no 2D palindrome table needed.

**Steps:**
1. Initialize `dp[i] = i` for all i (worst case: cut before every character).
2. For each center index `c` from 0 to n-1:
   - **Odd-length:** `left = c, right = c`. Expand while `s[left] == s[right]`.
   - **Even-length:** `left = c, right = c+1`. Expand while `s[left] == s[right]`.
   - At each valid `(left, right)`:
     - If `left == 0`: `dp[right] = 0` (palindrome starts at beginning, no cut needed)
     - Else: `dp[right] = min(dp[right], dp[left-1] + 1)`
     - Then `left--, right++`
3. Return `dp[n-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) — 2n centers × up to n/2 expansions each |
| Space  | O(n) — only the 1D dp array |

---

## Visualization (Example: `"aab"`, n=3)

**Approach 3 — Expand Around Center:**

Initialize: `dp = [0, 1, 2]` (worst case)

Center c=0 (char 'a'):
- Odd `[0,0]`: 'a'=='a' → `dp[0] = 0`. Expand → `[-1,1]` out of bounds. Stop.
- Even `[0,1]`: 'a'!='a'? 'a'=='a' yes → `dp[1] = min(1, 0+1) = 1`. Expand → `[-1,2]` out of bounds. Stop.

Center c=1 (char 'a'):
- Odd `[1,1]`: 'a'=='a' → `dp[1] = min(1, dp[0]+1) = min(1,1) = 1`. Expand `[0,2]`: 'a'!='b'. Stop.
- Even `[1,2]`: 'a'!='b'. Stop immediately.

Center c=2 (char 'b'):
- Odd `[2,2]`: 'b'=='b' → `dp[2] = min(2, dp[1]+1) = min(2, 1+1) = 2`. Expand `[1,3]` out of bounds. Stop.
- Even `[2,3]`: out of bounds immediately. Stop.

Final `dp = [0, 1, 2]`.  Answer: `dp[2] = 2`?

Wait — let me recheck. `"aab"` → `["aa","b"]` = 1 cut.

With c=0, even expansion: `s[0]='a', s[1]='a'` → match → `dp[1] = min(1, 0+1)=1`. Then expand: `s[-1]` out of bounds. Stop.

At c=1 odd: `s[1]='a'`, `dp[1] = min(1, dp[0]+1)=1`. Expand `[0,2]`: `s[0]='a', s[2]='b'` → mismatch. Stop.

At c=2 odd: `s[2]='b'`, `dp[2] = min(2, dp[1]+1) = min(2,2) = 2`. But expected is 1.

The issue: we need `dp[2]` to reflect the cut after `"aa"`. `"aa"` covers `[0,1]` = palindrome, then `"b"` at `[2,2]`. Since `"b"` is a palindrome, `dp[2] = dp[1] + 1 = 1 + 1 = 2`? No — `dp[1]` should be 0 not 1.

Let me retrace c=0 even: `s[0]='a', s[1]='a'` matches. `left=0`, so `dp[right] = dp[1] = 0`. dp becomes `[0, 0, 2]`.

Then at c=2 odd: `dp[2] = min(2, dp[1]+1) = min(2, 0+1) = 1`. Correct!

Final `dp = [0, 0, 1]`. Answer: `dp[2] = 1`. ✓

```
s = "a  a  b"
idx:  0  1  2

dp init:    [0, 1, 2]
c=0 even (s[0]='a', s[1]='a' match, left=0): dp[1] = 0
  -> dp:   [0, 0, 2]
c=1 odd  (s[1]='a', left=1):  dp[1] = min(0, dp[0]+1) = 0
  expand (s[0]='a', s[2]='b' mismatch): stop
c=2 odd  (s[2]='b', left=2):  dp[2] = min(2, dp[1]+1) = min(2, 1) = 1
  -> dp:   [0, 0, 1]

Answer: dp[2] = 1  ✓
```

---

## Real-World Use Case

**Text Segmentation and Data Compression**

In text compression (like LZ77 or genome sequence compression), finding optimal partitions of a string into repeated or symmetric patterns minimizes encoding cost. Palindrome partitioning is a simplified model of this — finding the fewest segments where each segment has perfect internal symmetry.

In bioinformatics, DNA sequences have palindromic subsequences (restriction enzyme recognition sites like `GAATTC` / `CTTAAG`). Efficient palindrome detection using the expand-around-center technique (Manacher-adjacent ideas) is used in genome analysis tools to locate these sites rapidly across millions of base pairs.

In UI/UX and natural language processing, balanced/symmetric phrase detection uses similar interval DP foundations.

---

## Interview Tips

- The naive solution of memoizing `recurse(start)` alone (without palindrome precomputation) is O(n^2) space and O(n^3) time — mention it, then upgrade to the full O(n^2) solution.
- The expand-around-center trick (Approach 3) is the key insight that drops space from O(n^2) to O(n). It's worth knowing because the same pattern solves "Longest Palindromic Substring" (LC 5) efficiently.
- Initialization of `dp[i] = i` is deliberate: `i` cuts mean every character is a separate palindrome (i+1 single chars need i cuts). This is a correct upper bound that gets overwritten.
- The condition `left == 0` in the expand step elegantly handles the case where the palindrome starts at the very beginning of the string — no cut before it means `dp[right] = 0`.
- This problem has a Manacher's algorithm + DP variant that also runs in O(n^2) but with better constant factors. For interviews, the expand-center approach is cleaner and equally accepted.
- Don't confuse with Palindrome Partitioning I (LC 131, return all partitions) — that's backtracking. This problem (LC 132) asks for minimum cuts — that's DP.
