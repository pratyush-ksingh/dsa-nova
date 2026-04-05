# Shortest Common Supersequence

> **Step 16.5** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

Given two strings `str1` and `str2`, return the **shortest string** that has both `str1` and `str2` as **subsequences**. If there are multiple valid answers, return any of them.

A string `s` is a subsequence of `t` if you can delete some characters of `t` (without reordering the remaining ones) to get `s`.

**Key formula:** `SCS_length = len(str1) + len(str2) - LCS_length`

Every character that appears in the LCS need only appear **once** in the SCS; all non-LCS characters from both strings must appear separately.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `str1="abac", str2="cab"` | `"cabac"` (length 5) | LCS="ab" (len 2); 4+3-2=5 |
| `str1="geek", str2="eke"` | `"geeke"` (length 5) | LCS="eke" (len 3); 4+3-3=4... but "geeke" has len 5 — LCS is actually "ee" |
| `str1="AGGTAB", str2="GXTXAYB"` | `"AGXTXAYB"` (length 9) | LCS="GTAB" (len 4); 6+7-4=9 |
| `str1="abc", str2="abc"` | `"abc"` (length 3) | Both identical — LCS = full string |
| `str1="", str2="abc"` | `"abc"` | Empty string is subsequence of anything |

## Constraints

- `1 <= str1.length, str2.length <= 1000`
- `str1` and `str2` consist only of lowercase English letters

---

## Approach 1: Brute Force — Plain Recursion

**Intuition:** At each step we compare the current characters of both strings. If they match, we must include that character once and advance both pointers. If they differ, we have two choices: take from `str1` and advance its pointer, or take from `str2` and advance its pointer. Recursively pick the option that yields the shorter supersequence.

**Steps:**
1. Define `helper(i, j)` returning the SCS of `str1[i:]` and `str2[j:]`.
2. Base cases: if one string is exhausted, return the remainder of the other.
3. If `str1[i] == str2[j]`: return `str1[i] + helper(i+1, j+1)`.
4. Else: return shorter of `str1[i] + helper(i+1, j)` and `str2[j] + helper(i, j+1)`.
5. This has exponential overlap — no memoisation here (pure brute force).

| Metric | Value |
|--------|-------|
| Time   | O(2^(m+n)) |
| Space  | O(m+n) — recursion depth |

---

## Approach 2: Optimal — LCS DP + Backtrack Reconstruction

**Intuition:** Compute the full LCS DP table in O(m*n). Then backtrack from `dp[m][n]` to construct the SCS: whenever `str1[i-1] == str2[j-1]` (LCS character), emit it once and move diagonally; otherwise emit the character from whichever string has the longer remaining LCS and advance that pointer. Finally drain any remaining characters from either string.

**Steps:**
1. Build `dp[m+1][n+1]` where `dp[i][j] = LCS length of str1[0..i-1] and str2[0..j-1]`.
2. Fill: `dp[i][j] = dp[i-1][j-1]+1` if match, else `max(dp[i-1][j], dp[i][j-1])`.
3. Backtrack from `(m, n)`:
   - Match: append `str1[i-1]`, decrement both `i` and `j`.
   - `dp[i-1][j] >= dp[i][j-1]`: append `str1[i-1]`, decrement `i`.
   - Else: append `str2[j-1]`, decrement `j`.
4. Append leftover characters from `str1[:i]` and `str2[:j]`.
5. Reverse the collected characters.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) — full table needed for backtracking |

---

## Approach 3: Best — Same O(m*n) with Cleaner Tie-breaking

**Intuition:** Identical to Approach 2 but uses strict `>` (instead of `>=`) when comparing `dp[i-1][j]` vs `dp[i][j-1]` during backtracking. This can produce a lexicographically different but equally valid SCS. The full DP table cannot be reduced to O(min(m,n)) when we need to reconstruct the actual string (only the length can be computed in O(min(m,n)) space).

**Steps:** Same as Approach 2 with `dp[i-1][j] > dp[i][j-1]` for the tie-breaking branch.

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) |

---

## Real-World Use Case

**Version control / diff tools:** When merging two diverged versions of a file, a tool like `git merge` needs to produce the shortest unified file that preserves both versions as subsequences. The SCS is the theoretical lower bound on merged file length, and the LCS DP table is the core computation inside diff algorithms (GNU diff, Myers diff).

Other applications:
- **Bioinformatics:** Constructing a consensus DNA sequence that contains two sample sequences as subsequences (sequence assembly).
- **XML/HTML merging:** Creating the minimal document that preserves the structure of two independently edited documents.
- **Spell-checker candidate generation:** Finding the shortest string from which both a mistyped word and its correction are derivable.

## Interview Tips

- State the formula `SCS = len(s1) + len(s2) - LCS` before writing any code — it demonstrates insight and saves time.
- The reconstruction step is where most candidates slip up. Walk through a tiny example (`"ab"` and `"bc"`) on the whiteboard to show the diagonal vs. up/left movement.
- You **cannot** compress the DP table to O(min(m,n)) space when you need to recover the actual string, only when you need the length. Be ready to explain this trade-off.
- The brute force approach is the same structure as LCS memoised recursion — adding memoisation on `(i, j)` brings it to O(m*n) time with O(m*n) space, which is equivalent to the bottom-up DP.
- Clarify: "Is any valid answer acceptable?" LeetCode 1092 says yes. If lexicographically smallest is required, you need a different tie-breaking rule during backtracking.
- Edge cases: either string empty (answer is the other), both identical (answer is either), completely disjoint strings (answer is concatenation).
