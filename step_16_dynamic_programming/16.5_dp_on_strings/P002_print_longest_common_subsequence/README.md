# Print Longest Common Subsequence

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given two strings `s1` and `s2`, **print** (not just find the length of) the **Longest Common Subsequence (LCS)**. A subsequence is a sequence that appears in the same relative order but not necessarily contiguous.

*(Coding Ninjas / GFG)*

**Constraints:**
- `1 <= |s1|, |s2| <= 1000`
- Strings contain lowercase English letters

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `s1 = "abcde", s2 = "ace"` | `"ace"` | LCS length = 3, the actual string is "ace" |
| `s1 = "acd", s2 = "ced"` | `"cd"` | Common subsequences: "c", "d", "cd". Longest = "cd" |
| `s1 = "abc", s2 = "def"` | `""` | No common characters at all |
| `s1 = "abcba", s2 = "abcbcba"` | `"abcba"` | s1 itself is a subsequence of s2 |

### Real-Life Analogy
> *Imagine two people independently listing their favorite movies in order. The LCS is the longest sequence of movies they both like, preserving each person's ranking order. Finding the LENGTH tells you how compatible they are. PRINTING the LCS tells you which specific movies to watch together on movie night.*

### Key Observations
1. **Build the DP table first, then backtrack:** First compute the standard LCS length table `dp[i][j]` (length of LCS of s1[0..i-1] and s2[0..j-1]). Then trace back from `dp[m][n]` to reconstruct the actual string. <-- This is the "aha" insight
2. **Backtracking logic:** At `dp[i][j]`, if `s1[i-1] == s2[j-1]`, this character is part of the LCS -- add it and move diagonally to `dp[i-1][j-1]`. Otherwise, move to the larger of `dp[i-1][j]` or `dp[i][j-1]`. <-- This is the "aha" insight
3. **Reverse at the end:** Since we backtrack from bottom-right to top-left, the characters are collected in reverse order. Reverse the result before returning.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** `lcs(i, j)` depends on `lcs(i-1, j-1)`, `lcs(i-1, j)`, `lcs(i, j-1)` -- heavy overlap.
- **Optimal substructure:** LCS of s1[0..i] and s2[0..j] can be built from solutions to smaller prefixes.
- The **printing** part requires the full 2D table (cannot space-optimize away the table if you need backtracking).

### Pattern Recognition
- **Pattern:** DP on strings with backtracking for reconstruction
- **Classification Cue:** "When you see _print the actual LCS_ --> think _build DP table + backtrack diagonally on matches_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion (Length only)
**Idea:** Recursively compute LCS length. If characters match, add 1 and move both pointers. Otherwise, try skipping one character from each string and take max.

**State Definition:** `solve(i, j)` = length of LCS of s1[0..i-1] and s2[0..j-1]

**Recurrence:**
```
solve(i, j):
  if i == 0 or j == 0: return 0
  if s1[i-1] == s2[j-1]: return 1 + solve(i-1, j-1)
  else: return max(solve(i-1, j), solve(i, j-1))
```

**Steps:**
1. Call `solve(m, n)` where m = len(s1), n = len(s2).
2. To print: collect all LCS subsequences recursively (exponential).

| Time | Space |
|------|-------|
| O(2^(m+n)) | O(m+n) recursion stack |

**BUD Transition:** Many overlapping (i, j) states. Memoize.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][j]` to avoid recomputation.

**Dry Run:** `s1 = "acd", s2 = "ced"`

| Call | i | j | s1[i-1] vs s2[j-1] | Result |
|------|---|---|---------------------|--------|
| solve(3,3) | 3 | 3 | d == d | 1 + solve(2,2) |
| solve(2,2) | 2 | 2 | c == e? No | max(solve(1,2), solve(2,1)) |
| solve(1,2) | 1 | 2 | a == e? No | max(solve(0,2)=0, solve(1,1)) |
| solve(1,1) | 1 | 1 | a == c? No | max(solve(0,1)=0, solve(1,0)=0) = 0 |
| back(1,2) | | | max(0, 0) = 0 | 0 |
| solve(2,1) | 2 | 1 | c == c | 1 + solve(1,0) = 1 |
| back(2,2) | | | max(0, 1) = 1 | 1 |
| back(3,3) | | | 1 + 1 = 2 | **2** |

LCS length = 2. Now backtrack to print "cd".

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Build table bottom-up, then backtrack for the string.

### Approach 3: Tabulation (Bottom-Up DP) + Backtracking
**What changed:** Build `dp[m+1][n+1]` table iteratively, then trace back to print the LCS string.

**Steps (Build Table):**
1. `dp[i][0] = 0` and `dp[0][j] = 0` for all i, j.
2. For `i = 1` to `m`, for `j = 1` to `n`:
   - If `s1[i-1] == s2[j-1]`: `dp[i][j] = 1 + dp[i-1][j-1]`
   - Else: `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**Steps (Backtrack to Print):**
1. Start at `i = m, j = n`. Initialize empty result string.
2. While `i > 0` and `j > 0`:
   - If `s1[i-1] == s2[j-1]`: append `s1[i-1]` to result, move `i--, j--`
   - Else if `dp[i-1][j] > dp[i][j-1]`: move `i--`
   - Else: move `j--`
3. Reverse the result string.

**Dry Run (Backtrack):** `s1 = "acd", s2 = "ced"`, dp table:

|   | "" | c | e | d |
|---|---|---|---|---|
| "" | 0 | 0 | 0 | 0 |
| a | 0 | 0 | 0 | 0 |
| c | 0 | 1 | 1 | 1 |
| d | 0 | 1 | 1 | **2** |

Backtrack from dp[3][3] = 2:
- i=3, j=3: s1[2]='d' == s2[2]='d' --> add 'd', go to (2,2)
- i=2, j=2: s1[1]='c' != s2[1]='e', dp[1][2]=0 < dp[2][1]=1 --> go to (2,1)
- i=2, j=1: s1[1]='c' == s2[0]='c' --> add 'c', go to (1,0)
- i=1, j=0: stop

Collected (reverse order): "dc" --> reverse --> **"cd"**

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Space optimization is NOT possible for printing because we need the full table for backtracking. But we can optimize the table construction approach slightly.

### Approach 4: Space Optimized (Length Only) + Full Table for Print
**What changed:** For length-only queries, use two 1D arrays. For printing, we must keep the full table.

**For length only:**
1. Use `prev[n+1]` and `curr[n+1]`.
2. For each row `i`, compute `curr[j]` from `prev`.
3. Return `prev[n]`.

**For printing:** We must build the full 2D table (Approach 3), but we can be clever about the backtracking -- it runs in O(m+n) time and O(m+n) space for the result.

| Variant | Time | Space |
|---------|------|-------|
| Length only | O(m * n) | **O(n)** |
| With printing | O(m * n) | O(m * n) (table needed) |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "At each cell we branch into 2 directions, giving exponential paths."
**Memo/Tab:** "There are m * n unique states, each O(1). Total: O(m * n)."
**Backtracking:** "From (m,n) we move at most m + n steps. O(m + n) for reconstruction."
**Space for printing:** "We need the full table to know which direction to go at each cell. Cannot compress."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting to reverse the backtracked string** -- characters are collected from end to start.
2. **Using 1-indexed vs 0-indexed incorrectly** -- the DP table is (m+1) x (n+1) with row 0 and col 0 as base cases. String indices are off by 1.
3. **Tie-breaking direction in backtracking** -- when `dp[i-1][j] == dp[i][j-1]`, either direction works (different valid LCS strings exist). Pick one consistently.
4. **Trying to space-optimize and still print** -- you need the full table for backtracking.

### Edge Cases to Test
- [ ] One string is empty `"", "abc"` --> ""
- [ ] Identical strings `"abc", "abc"` --> "abc"
- [ ] No common characters `"abc", "def"` --> ""
- [ ] One string is a subsequence of the other `"ace", "abcde"` --> "ace"
- [ ] Single characters `"a", "a"` --> "a"
- [ ] Single characters, different `"a", "b"` --> ""

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Subsequence, not substring? Case sensitive? What if multiple LCS exist -- return any one?"
2. **Two-phase approach:** "Phase 1: Build the standard LCS DP table. Phase 2: Backtrack from dp[m][n] to reconstruct the string."
3. **Code the tabulation + backtracking** -- this is the expected solution.
4. **Mention:** "Space optimization to O(min(m,n)) is possible for length-only, but printing requires the full table."

### Follow-Up Questions
- "Print ALL LCS strings?" --> Backtrack with recursion, collecting all paths.
- "Shortest Common Supersequence?" --> Use LCS as backbone, interleave remaining characters.
- "Longest Common Substring (contiguous)?" --> Different DP: reset to 0 on mismatch.

---

## CONNECTIONS
- **Prerequisite:** Longest Common Subsequence (length only, P001 in this section)
- **Same Pattern:** Shortest Common Supersequence, Edit Distance (backtracking through DP table)
- **This Unlocks:** Diff algorithms (file comparison), DNA sequence alignment
- **Harder Variant:** Print all LCS strings, Longest Palindromic Subsequence (LCS of string with its reverse)
