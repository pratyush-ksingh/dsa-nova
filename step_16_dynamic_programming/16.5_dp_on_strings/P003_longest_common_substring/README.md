# Longest Common Substring

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given two strings `s1` and `s2`, find the **length of the longest common substring**. A substring is a contiguous sequence of characters within a string.

This is different from Longest Common Subsequence (LCS): in LCS, characters don't need to be contiguous. Here, they must form a contiguous block in **both** strings.

**Key Difference from LCS:** In LCS, when characters don't match, we take `max(dp[i-1][j], dp[i][j-1])`. In Longest Common Substring, when characters don't match, `dp[i][j] = 0` -- the streak resets!

**Constraints:**
- `1 <= len(s1), len(s2) <= 1000`
- Strings contain lowercase English letters

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `s1 = "abcde", s2 = "abfce"` | `2` | Common substrings: "ab" (len 2), "ce" (len 2). Longest = 2 |
| `s1 = "abc", s2 = "abc"` | `3` | Entire string matches |
| `s1 = "abc", s2 = "def"` | `0` | No common substring |
| `s1 = "abcdxyz", s2 = "xyzabcd"` | `4` | "abcd" appears in both (also "xyz" len 3). Longest = 4 |

### Real-Life Analogy
> *Imagine two people describing the same event. You want to find the longest stretch of words that both descriptions share, word-for-word. Not scattered matching words (that's LCS), but a continuous run of identical text. This is exactly what plagiarism detection software does -- it finds the longest contiguous text shared between documents.*

### Key Observations
1. **State Definition:** `dp[i][j]` = length of longest common substring ending at `s1[i-1]` and `s2[j-1]`.
2. **Recurrence:** If `s1[i-1] == s2[j-1]`, then `dp[i][j] = dp[i-1][j-1] + 1`. Otherwise, `dp[i][j] = 0` (streak breaks).
3. **Answer:** The maximum value anywhere in the dp table (not just `dp[n][m]`!).
4. **Critical insight:** Unlike LCS where we propagate the max, here we **reset to 0** on mismatch because substrings must be contiguous.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Brute force** checks all O(n^2) substrings of s1 against s2, each check taking O(m) time.
- **Recursion** on (i, j) with a "current length" parameter explores matching streaks.
- **Tabulation** fills a 2D grid where each cell tracks the current matching streak length.
- **Space Optimization** recognizes that row `i` only depends on row `i-1`, so we only need two rows (or even one row traversed right-to-left).

### Pattern Recognition
- **Pattern:** 2D DP on strings with contiguous constraint
- **Classification Cue:** "When you see _longest common contiguous_ between two strings --> think _dp[i][j] resets to 0 on mismatch_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** For each pair `(i, j)`, if characters match, extend the current streak. Track the maximum streak seen.

**Steps:**
1. Define `solve(i, j, count)` where `count` = current matching streak length.
2. Base case: if `i == 0` or `j == 0`, return `count`.
3. If `s1[i-1] == s2[j-1]`: explore `solve(i-1, j-1, count+1)`.
4. Always also try breaking the streak: `max(solve(i-1, j, 0), solve(i, j-1, 0))`.
5. Return max of all options.

**Why it is slow:** Three-way branching with no caching. Exponential calls.

| Time | Space |
|------|-------|
| O(3^(n+m)) worst case | O(n+m) recursion stack |

**BUD Transition:** Many `(i, j, count)` states repeat. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache results for `(i, j, count)`. But this is a 3D state which uses O(n*m*min(n,m)) space -- not great.

**Better approach -- restructure:** Instead of tracking `count`, define `solve(i, j)` = length of longest common substring ending exactly at `s1[i-1]` and `s2[j-1]`. Then sweep all `(i, j)` pairs to find the global max.

**Steps:**
1. `solve(i, j)`: if `s1[i-1] == s2[j-1]`, return `1 + solve(i-1, j-1)`. Else return `0`.
2. Memoize with a 2D cache.
3. Call `solve(i, j)` for all valid `i, j` and track the maximum.

**Dry Run:** `s1 = "abcde", s2 = "abfce"`

| (i,j) | s1[i-1] | s2[j-1] | Match? | solve(i,j) |
|--------|---------|---------|--------|------------|
| (1,1) | a | a | Yes | 1 + solve(0,0) = 1 |
| (2,2) | b | b | Yes | 1 + solve(1,1) = 2 |
| (3,3) | c | f | No | 0 |
| (4,4) | d | c | No | 0 |
| (5,5) | e | e | Yes | 1 + solve(4,4) = 1 |
| (3,4) | c | c | Yes | 1 + solve(2,3) = 1 |
| (4,5) | d | e | No | 0 |
| (5,4) | e | c | No | 0 |
| Max = 2 from (2,2) |

| Time | Space |
|------|-------|
| O(n * m) | O(n * m) cache + O(n+m) stack |

**BUD Transition:** Recursion overhead. Build bottom-up.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill a 2D table iteratively.

**Steps:**
1. Create `dp[n+1][m+1]`, initialized to 0.
2. For `i = 1` to `n`, for `j = 1` to `m`:
   - If `s1[i-1] == s2[j-1]`: `dp[i][j] = dp[i-1][j-1] + 1`. Update `maxLen`.
   - Else: `dp[i][j] = 0` (streak breaks).
3. Return `maxLen`.

**Dry Run:** `s1 = "abcde", s2 = "abfce"`

```
    ""  a  b  f  c  e
""   0  0  0  0  0  0
 a   0  1  0  0  0  0
 b   0  0  2  0  0  0
 c   0  0  0  0  1  0
 d   0  0  0  0  0  0
 e   0  0  0  0  0  1
```

`maxLen = 2` (at position dp[2][2])

| Time | Space |
|------|-------|
| O(n * m) | O(n * m) |

**BUD Transition:** Row `i` only depends on row `i-1`. Use a single row!

### Approach 4: Space Optimized
**What changed:** Use a single 1D array. **Critical:** traverse `j` from right to left to avoid overwriting `dp[j-1]` before it is needed (since `dp[i][j]` depends on `dp[i-1][j-1]`, which is the old `dp[j-1]`).

**Steps:**
1. Create `dp[m+1]`, initialized to 0.
2. For `i = 1` to `n`:
   - Traverse `j` from `m` down to `1`:
     - If `s1[i-1] == s2[j-1]`: `dp[j] = dp[j-1] + 1`. Update `maxLen`.
     - Else: `dp[j] = 0`.
3. Return `maxLen`.

**Why right-to-left?** When we compute `dp[j]`, we need `dp[j-1]` from the previous row. If we go left-to-right, `dp[j-1]` would already be overwritten with the current row's value. Going right-to-left preserves it.

| Time | Space |
|------|-------|
| O(n * m) | **O(m)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Every pair branches into multiple states. Without caching, exponential."
**Memo/Tab:** "n*m cells, O(1) work per cell. Total: O(n*m)."
**Space Optimized:** "Same O(n*m) time, but only one row of size m at a time -- O(m) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Confusing with LCS:** LCS takes `max(dp[i-1][j], dp[i][j-1])` on mismatch. Longest Common Substring sets `dp[i][j] = 0`.
2. **Returning dp[n][m]:** The answer is the **maximum value in the entire table**, not `dp[n][m]`. The longest common substring might not end at the last characters.
3. **Space optimization direction:** Must go right-to-left in the inner loop when using 1D array.
4. **Empty string handling:** If either string is empty, answer is 0.

### Edge Cases to Test
- [ ] One string empty --> 0
- [ ] Identical strings --> length of the string
- [ ] No common character --> 0
- [ ] Common substring at the very end
- [ ] Single character strings

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Substring means contiguous, unlike subsequence."
2. **Start with recursion:** "I'll try all pairs (i,j) and extend matches."
3. **Key insight:** "dp[i][j] represents the length of matching streak ending at those positions. On mismatch, it resets to 0."
4. **Optimize space:** "Only the previous row is needed, so I use 1D array with right-to-left traversal."

### Follow-Up Questions
- "Can you also return the actual substring?" --> Track the ending index of the max value and extract `s1[end-maxLen+1..end]`.
- "What about binary search + rolling hash for O(n log n)?" --> Yes, binary search on length, use Rabin-Karp rolling hash to check if a common substring of that length exists.
- "How does this relate to suffix arrays?" --> The LCP (Longest Common Prefix) array from a suffix array solves this in O(n+m) after O(n+m) construction.

---

## CONNECTIONS
- **Prerequisite:** Longest Common Subsequence (LCS)
- **Same Pattern:** Edit Distance, Shortest Common Supersequence
- **This Unlocks:** Palindromic substrings, Repeated Substring Pattern
- **Harder Variant:** Longest Repeated Substring (suffix array), Longest Common Substring of K strings
