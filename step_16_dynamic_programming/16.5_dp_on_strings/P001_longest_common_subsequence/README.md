# Longest Common Subsequence

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given two strings `text1` and `text2`, return the **length** of their **longest common subsequence** (LCS). A subsequence is a sequence that can be derived from the string by deleting some (or no) characters without changing the relative order of the remaining characters.

*(LeetCode #1143)*

**Constraints:**
- `1 <= text1.length, text2.length <= 1000`
- `text1` and `text2` consist of only lowercase English characters.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `text1 = "abcde", text2 = "ace"` | `3` | LCS is "ace" |
| `text1 = "abc", text2 = "abc"` | `3` | LCS is "abc" (identical strings) |
| `text1 = "abc", text2 = "def"` | `0` | No common subsequence |
| `text1 = "aab", text2 = "azb"` | `2` | LCS is "ab" |

### Real-Life Analogy
> *Think of two DNA strands. Scientists want to find the longest sequence of genes that appear in the same relative order in both strands -- this reveals evolutionary similarity. At each position, if both strands have the same gene, it is part of the common ancestry (match!). If not, we try skipping a gene from one strand or the other and see which gives a longer shared sequence. This "match or skip" decision at every pair of positions is exactly LCS.*

### Key Observations
1. **Two-pointer comparison:** Compare characters at positions `i` in text1 and `j` in text2. If they match, both are part of the LCS. If not, try advancing one pointer or the other. <-- This is the "aha" insight
2. **Recurrence:** If `text1[i] == text2[j]`: `LCS(i,j) = 1 + LCS(i-1, j-1)`. Otherwise: `LCS(i,j) = max(LCS(i-1,j), LCS(i,j-1))`.
3. **State = (i, j):** Two indices, one into each string. This gives an `m x n` DP table. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** `LCS(i,j)` calls `LCS(i-1,j)` and `LCS(i,j-1)`, both of which call `LCS(i-1,j-1)`. Massive overlap.
- **Optimal substructure:** The LCS of the full strings is built from LCS of shorter prefixes.
- This is THE canonical "DP on strings" problem. Nearly all string DP problems are variations of LCS.

### Pattern Recognition
- **Pattern:** 2D DP on two strings
- **Classification Cue:** "When you see _longest/shortest common something_ between _two strings_ --> think _dp[i][j] with match/mismatch recurrence_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Compare characters from the end of both strings. Match -> both move left + 1. Mismatch -> try moving left in each string, take the max.

**State Definition:** `solve(i, j)` = length of LCS of `text1[0..i]` and `text2[0..j]`.

**Recurrence:**
```
If text1[i] == text2[j]:  solve(i, j) = 1 + solve(i-1, j-1)
Else:                      solve(i, j) = max(solve(i-1, j), solve(i, j-1))
Base case: if i < 0 or j < 0: return 0
```

**Steps:**
1. Call `solve(m-1, n-1)`.
2. If characters match, add 1 and recurse on both shortened.
3. If not, try skipping one character from each string, take max.

| Time | Space |
|------|-------|
| O(2^(m+n)) | O(m+n) recursion stack |

**BUD Transition:** Same (i,j) pairs recomputed. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Store results in `dp[i][j]`. Using 1-based indexing (shifting indices by 1) simplifies base cases.

**Dry Run:** `text1 = "ace", text2 = "abcde"`

Using 1-based: `solve(i, j)` where i, j are 1-indexed lengths.

| Call | i | j | text1[i-1] | text2[j-1] | Action | Result |
|------|---|---|------------|------------|--------|--------|
| solve(3,5) | 3 | 5 | 'e' | 'e' | Match! 1+solve(2,4) | |
| solve(2,4) | 2 | 4 | 'c' | 'd' | Mismatch: max(solve(1,4), solve(2,3)) | |
| solve(2,3) | 2 | 3 | 'c' | 'c' | Match! 1+solve(1,2) | |
| solve(1,2) | 1 | 2 | 'a' | 'b' | Mismatch: max(solve(0,2), solve(1,1)) | |
| solve(1,1) | 1 | 1 | 'a' | 'a' | Match! 1+solve(0,0) = 1 | 1 |
| solve(0,2) | 0 | 2 | - | - | Base: 0 | 0 |
| back(1,2) | | | max(0, 1) = 1 | 1 |
| back(2,3) | | | 1 + 1 = 2 | 2 |
| solve(1,4) | 1 | 4 | 'a' | 'd' | ... eventually = 1 | 1 |
| back(2,4) | | | max(1, 2) = 2 | 2 |
| back(3,5) | | | 1 + 2 = **3** | **3** |

LCS = "ace", length = 3.

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Convert to bottom-up tabulation.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[m+1][n+1]` table iteratively. Using 1-based indexing: `dp[0][j] = dp[i][0] = 0`.

**Steps:**
1. Initialize `dp[0][*] = 0` and `dp[*][0] = 0`.
2. For `i = 1` to `m`, for `j = 1` to `n`:
   - If `text1[i-1] == text2[j-1]`: `dp[i][j] = 1 + dp[i-1][j-1]`
   - Else: `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`
3. Return `dp[m][n]`.

**Dry Run:** `text1 = "abcde", text2 = "ace"`

|   | "" | a | c | e |
|---|---|---|---|---|
| "" | 0 | 0 | 0 | 0 |
| a | 0 | 1 | 1 | 1 |
| b | 0 | 1 | 1 | 1 |
| c | 0 | 1 | 2 | 2 |
| d | 0 | 1 | 2 | 2 |
| e | 0 | 1 | 2 | **3** |

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) |

**BUD Transition:** Each row only depends on the previous row. Use two 1D arrays.

### Approach 4: Space Optimized
**What changed:** Use `prev[n+1]` and `curr[n+1]` instead of full 2D table.

**Steps:**
1. `prev = [0] * (n+1)`.
2. For each row `i`:
   - `curr = [0] * (n+1)`.
   - For `j = 1` to `n`:
     - If match: `curr[j] = 1 + prev[j-1]`
     - Else: `curr[j] = max(prev[j], curr[j-1])`
   - `prev = curr`
3. Return `prev[n]`.

| Time | Space |
|------|-------|
| O(m * n) | **O(min(m, n))** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "At each (i,j) we branch into 2 paths (skip from text1 or text2). The tree can be exponentially deep."
**Memo/Tab:** "There are m * n unique (i,j) states. Each takes O(1). Total O(m*n)."
**Space Optimized:** "Same O(m*n) time. Only keep 2 rows of length n+1, so O(n) space. Swap text1 and text2 if m < n to get O(min(m,n))."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **0-based vs 1-based indexing confusion** -- the tabulation uses 1-based (dp size m+1 x n+1) while strings are 0-indexed. Off-by-one is the #1 bug.
2. **Forgetting `dp[i-1][j-1]` on match** -- must go diagonally, not just up or left.
3. **Space optimization: overwriting `prev[j-1]` needed for diagonal** -- when using a single array, you must save the diagonal value before overwriting.

### Edge Cases to Test
- [ ] One string empty --> 0
- [ ] Identical strings --> length of the string
- [ ] No common characters --> 0
- [ ] One string is a subsequence of the other --> length of shorter
- [ ] Single character strings

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Subsequence, not substring? Characters must maintain relative order but need not be contiguous?"
2. **Recurrence:** "Compare text1[i] and text2[j]. Match -> 1 + diagonal. Mismatch -> max(up, left)."
3. **Progression:** Recursion -> Memo -> Tab -> Space. Draw the 2D table for the example.
4. **Code:** Write tabulation with 1-based indexing for clarity.

### Follow-Up Questions
- "Print the actual LCS, not just the length?" --> Backtrack through the dp table following match/mismatch decisions.
- "Longest Common Substring instead?" --> Only count consecutive matches: `dp[i][j] = 1 + dp[i-1][j-1]` on match, else 0.
- "Edit Distance?" --> Same 2D DP structure but with insert/delete/replace operations.
- "Shortest Common Supersequence?" --> Length = m + n - LCS.

---

## CONNECTIONS
- **Prerequisite:** 2D DP concepts (Unique Paths, Ninja's Training)
- **Same Pattern:** Edit Distance, Longest Common Substring, Shortest Common Supersequence
- **This Unlocks:** Print LCS, LCS of 3 strings, all string DP problems
- **Harder Variant:** Edit Distance (LeetCode #72), Longest Palindromic Subsequence (LCS with reversed string)
