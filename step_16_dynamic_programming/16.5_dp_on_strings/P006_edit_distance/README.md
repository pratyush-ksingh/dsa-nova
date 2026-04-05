# Edit Distance

> **Step 16.16.5** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #72)** Given two strings `word1` and `word2`, return the minimum number of operations required to convert `word1` to `word2`. The three allowed operations are: **Insert** a character, **Delete** a character, **Replace** a character.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| word1="horse", word2="ros" | 3 | horse -> rorse (replace h->r) -> rose (delete r) -> ros (delete e) |
| word1="intention", word2="execution" | 5 | intention -> exention -> exection -> execuion -> executon -> execution |
| word1="", word2="abc" | 3 | Insert a, b, c |

### Constraints
- `0 <= word1.length, word2.length <= 500`
- `word1` and `word2` consist of lowercase English letters

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** Compare characters from the end of both strings. If they match, move both pointers. If not, try all 3 operations (insert, delete, replace) and take the minimum.

**Steps:**
1. `solve(i, j)` = min operations for `word1[0..i]` to `word2[0..j]`
2. If `i < 0`: need to insert `j+1` characters
3. If `j < 0`: need to delete `i+1` characters
4. If characters match: `solve(i-1, j-1)`
5. Else: `1 + min(solve(i, j-1), solve(i-1, j), solve(i-1, j-1))`

```
Dry-run: "horse" -> "ros"

solve(4,2): 'e' vs 's' -> mismatch
  insert:  1 + solve(4,1) = 1 + ...
  delete:  1 + solve(3,2) = 1 + ...
  replace: 1 + solve(3,1) = 1 + ...
  ... eventually resolves to 3
```

| Metric | Value |
|--------|-------|
| Time   | O(3^(m+n)) -- 3 branches per mismatch |
| Space  | O(m+n) -- recursion depth |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** Build a table `dp[i][j]` = edit distance between `word1[0..i-1]` and `word2[0..j-1]`. Base cases: converting empty string to/from the other requires inserting/deleting all characters.

**Steps:**
1. `dp[i][0] = i` (delete all), `dp[0][j] = j` (insert all)
2. For each `(i, j)`:
   - If `word1[i-1] == word2[j-1]`: `dp[i][j] = dp[i-1][j-1]`
   - Else: `dp[i][j] = 1 + min(dp[i][j-1], dp[i-1][j], dp[i-1][j-1])`
3. Return `dp[m][n]`

```
Dry-run: "horse" -> "ros"

     ""  r  o  s
""  [ 0  1  2  3]
h   [ 1  1  2  3]
o   [ 2  2  1  2]
r   [ 3  2  2  2]
s   [ 4  3  3  2]
e   [ 5  4  4  3]

Answer = dp[5][3] = 3
```

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(m * n) |

---

### Approach 3: Best -- Space-Optimized 1D DP

**Intuition:** Each row only depends on the previous row. Use two 1D arrays `prev` and `curr`. Swap `word1`/`word2` if needed so the shorter string is used for the inner loop, giving O(min(m,n)) space.

**Steps:**
1. Let word2 be the shorter string
2. `prev[j] = j` for base case
3. For each row `i`: `curr[0] = i`, fill `curr[j]` using `prev`
4. Return `prev[n]`

| Metric | Value |
|--------|-------|
| Time   | O(m * n) |
| Space  | O(min(m, n)) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| One string empty | Answer = length of the other string | All inserts or deletes |
| Strings identical | Answer = 0 | All characters match |
| Single character strings | 0 if same, 1 if different | Replace operation |
| Very different lengths | Dominated by length difference | Min ops >= abs(m-n) |

**Common Mistakes:**
- Off-by-one with 1-indexed vs 0-indexed dp table
- Confusing which operation maps to which dp transition
- Forgetting to handle empty string base cases

---

## Real-World Use Case
**Spell checkers and autocorrect:** Edit distance (Levenshtein distance) is the core algorithm behind spell-checking. Given a misspelled word, the dictionary words with smallest edit distance are suggested as corrections. It is also used in DNA sequence alignment in bioinformatics.

## Interview Tips
- This is a top-5 DP problem -- expect it in any serious interview
- Clearly map operations to DP transitions: insert=dp[i][j-1], delete=dp[i-1][j], replace=dp[i-1][j-1]
- Draw the DP table for a small example to build intuition
- The space optimization is a natural follow-up
- Know that edit distance is a metric (satisfies triangle inequality)
- Follow-up: reconstruct the actual sequence of operations (backtrack through DP table)
