# Longest Palindromic Substring

> **Step 05.5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given a string `s`, return the **longest substring** that is a **palindrome** (reads the same forwards and backwards). If there are multiple answers of the same length, return any one of them.

**LeetCode #5**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"babad"` | `"bab"` | `"bab"` is a palindrome (also `"aba"` is valid) |
| `"cbbd"` | `"bb"` | `"bb"` is the longest palindromic substring |
| `"a"` | `"a"` | Single character is always a palindrome |
| `"racecar"` | `"racecar"` | The whole string is a palindrome |

### Constraints
- `1 <= s.length <= 1000`
- `s` consists of only digits and English letters

### Real-Life Analogy
You have a string of holiday lights. A palindromic substring is a contiguous stretch that looks the same from both ends. You want to find the longest such stretch.

### 3 Key Observations
1. **"aha" -- every palindrome has a center:** An odd-length palindrome has a single center character; an even-length one has a center gap between two equal characters. Expanding outward from each possible center finds all palindromes.
2. **"aha" -- 2n-1 possible centers:** For a string of length n, there are n centers for odd-length and n-1 centers for even-length palindromes, giving 2n-1 total expansion points.
3. **"aha" -- Manacher reuses previous results:** If we already know a palindrome extends to position `right`, any position inside it has a mirror whose radius tells us the minimum expansion needed -- no re-checking required.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Two pointers for expansion**: simple O(1) space per center.
- **Manacher's P[] array**: stores palindrome radii to avoid redundant expansion.

### Pattern Recognition Cue
Whenever a problem involves finding palindromes in a string, think "expand around center." For O(n) you need Manacher's, but O(n²) is usually acceptable in interviews.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check All Substrings
**Intuition:** Generate every substring s[i..j] and verify if it is a palindrome using two pointers. Track the longest valid one.

**Steps:**
1. Iterate over all pairs (i, j) with i <= j.
2. Use a helper `isPalindrome(i, j)` that uses two pointers l=i, r=j.
3. If palindrome and length `j-i+1 > bestLen`, update best.
4. Return `s[bestStart..bestStart+bestLen]`.

**Dry Run:** `s = "cbbd"`
```
(0,0)="c" pal, len=1 best="c"
(0,1)="cb" not pal
(1,2)="bb" pal, len=2 best="bb"
(1,3)="bbd" not pal
...
Result: "bb"
```

| Metric | Value |
|--------|-------|
| Time   | O(n³) |
| Space  | O(1)  |

---

### Approach 2: Optimal -- Expand Around Center
**Intuition:** For each of the 2n-1 possible centers, expand outward as long as characters match. Track the longest expansion found.

**Steps:**
1. For each index `i` from 0 to n-1:
   a. Expand for **odd-length**: call `expand(i, i)`.
   b. Expand for **even-length**: call `expand(i, i+1)`.
2. `expand(l, r)` moves l left and r right while `s[l] == s[r]` and indices are valid.
3. After the loop, the palindrome is `s[l+1..r-1]`.
4. Track the pair (start, end) of the longest palindrome seen.
5. Return `s[start..end+1]`.

**BUD Transition from Brute:** Eliminated the inner O(n) palindrome check. Total is now O(n) expansions × O(n) each = O(n²).

**Dry Run:** `s = "babad"`
```
i=0: odd expand(0,0) -> "b" (len 1); even expand(0,1) -> 'b'!='a' -> ""
i=1: odd expand(1,1) -> expand to (0,2) "bab" (len 3); even expand(1,2) -> 'a'!='b' -> ""
i=2: odd expand(2,2) -> expand to (1,3) "aba" (len 3, tie)
Best: "bab" (first found with len 3)
```

| Metric | Value |
|--------|-------|
| Time   | O(n²) |
| Space  | O(1)  |

---

### Approach 3: Best -- Manacher's Algorithm
**Intuition:** Transform `s` into `t = "^#a#b#...#$"` to unify odd and even palindromes. Build array `P[i]` = radius of the longest palindrome centered at `i` in `t`. Use already-computed palindromes to skip re-expansion.

**Steps:**
1. Build `t`: insert `#` between all chars, sentinel `^` at start and `$` at end.
2. Maintain `center` and `right` (rightmost boundary of any known palindrome).
3. For each `i`: use mirror `mirror = 2*center - i`.
   - If `i < right`: `P[i] = min(right - i, P[mirror])` (safe inner bound).
4. Try to expand: increment `P[i]` while `t[i+P[i]+1] == t[i-P[i]-1]`.
5. Update `center` and `right` if `i + P[i] > right`.
6. Find max P[i]; convert back to original string index: `start = (i - P[i]) / 2`.

**BUD Transition from Optimal:** Each character is expanded at most once across the entire algorithm, giving O(n).

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) -- the P[] array and transformed string |

---

## 4. COMPLEXITY INTUITIVELY

- **Brute O(n³):** n² substrings × O(n) palindrome check each.
- **Expand O(n²):** n centers × O(n) expansion each.
- **Manacher O(n):** Each position's expansion is bounded by the center/right pointers -- amortized, the total expansion work across all centers is O(n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| Single character | itself | Must handle before expanding |
| All same chars `"aaaa"` | `"aaaa"` | Even-length center must be checked |
| Two chars `"ab"` | `"a"` or `"b"` | No even-length palindrome, fallback to len-1 |
| `"cbbd"` | `"bb"` | Even-length palindrome -- missed if only odd centers checked |

**Common Mistakes:**
- Only checking odd-length palindromes (missing even-length).
- Off-by-one in Manacher when converting back to original indices.
- Forgetting the sentinel characters in Manacher (`^` and `$`) which prevent out-of-bounds.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Find the longest palindromic substring."
2. **Match:** "Two classic approaches: expand-around-center O(n²) or Manacher's O(n). Expand-around-center is the interview sweet spot."
3. **Plan:** 2n-1 centers, expand each, track max.
4. **Implement:** Write `expand(l, r)` helper, loop over centers.
5. **Review:** Trace "babad".
6. **Evaluate:** O(n²) time, O(1) space; mention Manacher for follow-up.

### Follow-Up Questions
- *"Can you do it in O(n)?"* -- Yes, Manacher's algorithm. Explain the transform and mirror property.
- *"What if we need all longest palindromes, not just one?"* -- Track all with the same max length.
- *"What is the DP approach?"* -- `dp[i][j] = true if s[i..j] is palindrome`, using `dp[i][j] = (s[i]==s[j]) && dp[i+1][j-1]`. O(n²) time and space.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Valid Palindrome (LC #125) |
| **Same Pattern** | Palindromic Substrings Count (LC #647) -- count instead of longest |
| **Same Algorithm** | Longest Palindromic Subsequence (LC #516) -- DP variant |
| **Harder** | Palindrome Partitioning II (LC #132) -- Manacher + DP |
