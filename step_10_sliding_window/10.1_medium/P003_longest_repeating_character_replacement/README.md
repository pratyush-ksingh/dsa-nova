# Longest Repeating Character Replacement

> **Batch 3 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are given a string `s` and an integer `k`. You can choose any character of the string and change it to any other uppercase English letter. You can perform this operation at most `k` times. Return the length of the **longest substring** containing the same letter you can get after performing the above operations.

**LeetCode #424**

**Constraints:**
- `1 <= s.length <= 10^5`
- `s` consists of only uppercase English letters
- `0 <= k <= s.length`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `s = "ABAB", k = 2` | `4` | Replace two A's with B's (or vice versa) to get "BBBB" |
| `s = "AABABBA", k = 1` | `4` | Replace one B at index 3 to get "AAAAABA", substring "AAAA" has length 4 |
| `s = "AAAA", k = 2` | `4` | Already all same, length is 4 |
| `s = "ABCDE", k = 0` | `1` | No replacements allowed, best is a single character |

### Real-Life Analogy
> *Imagine you have a chain of colored beads and a paint bucket that can repaint at most k beads. You want to find the longest contiguous segment where all beads end up the same color. The strategy: pick the most common color in a segment and repaint the rest. A segment is valid if the number of "non-dominant" beads is at most k. You slide a window along the chain, expanding when valid and shrinking when not.*

### Key Observations
1. For any window of length `L`, the minimum replacements needed is `L - maxFreq` where `maxFreq` is the count of the most frequent character in that window. <-- This is the "aha" insight
2. The window is valid when `L - maxFreq <= k`. This means we can replace all non-dominant characters.
3. We never need to shrink the window below a previous best -- if we found a valid window of size W, any future answer must be at least W, so we only grow or slide.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **frequency array** (size 26) tracks character counts in the current window -- O(1) lookup and update.
- A **sliding window** (two pointers) avoids recomputing counts from scratch for every substring.

### Pattern Recognition
- **Pattern:** Sliding Window with a validity condition
- **Classification Cue:** "Whenever you see _longest/shortest substring_ with some constraint --> think sliding window."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Substrings
**Idea:** For every pair (i, j), count the most frequent character in `s[i..j]` and check if `(j - i + 1) - maxFreq <= k`.

**Steps:**
1. For each starting index `i`:
   - For each ending index `j >= i`:
     - Count character frequencies in `s[i..j]`.
     - If `(j - i + 1) - maxFreq <= k`, update the answer.
2. Return the maximum valid window length.

**BUD Transition -- Bottleneck:** Recomputing frequencies from scratch for each (i, j) is wasteful. We can maintain a running frequency count.

| Time | Space |
|------|-------|
| O(n^2 * 26) = O(n^2) | O(26) = O(1) |

### Approach 2: Optimal -- Sliding Window (Shrinking)
**What changed:** Use two pointers `left` and `right`. Expand `right` to include a new character; if the window becomes invalid (`windowLen - maxFreq > k`), shrink from `left`.

**Steps:**
1. Initialize `freq[26] = {0}`, `maxFreq = 0`, `left = 0`, `ans = 0`.
2. For each `right` from 0 to n-1:
   - Increment `freq[s[right]]`.
   - Update `maxFreq = max(maxFreq, freq[s[right]])`.
   - While `(right - left + 1) - maxFreq > k`:
     - Decrement `freq[s[left]]`, increment `left`.
   - Update `ans = max(ans, right - left + 1)`.
3. Return `ans`.

**Dry Run:** `s = "AABABBA", k = 1`

| right | char | freq | maxFreq | winLen | winLen-maxFreq | valid? | left | ans |
|-------|------|------|---------|--------|----------------|--------|------|-----|
| 0 | A | A:1 | 1 | 1 | 0 | yes | 0 | 1 |
| 1 | A | A:2 | 2 | 2 | 0 | yes | 0 | 2 |
| 2 | B | A:2,B:1 | 2 | 3 | 1 | yes | 0 | 3 |
| 3 | A | A:3,B:1 | 3 | 4 | 1 | yes | 0 | 4 |
| 4 | B | A:3,B:2 | 3 | 5 | 2 | no | 1 | 4 |
| 5 | B | A:2,B:3 | 3 | 5 | 2 | no | 2 | 4 |
| 6 | A | A:3,B:2 | 3 | 5 | 2 | no | 3 | 4 |

**Result:** 4

| Time | Space |
|------|-------|
| O(n) | O(26) = O(1) |

### Approach 3: Best -- Sliding Window (Non-Shrinking)
**What changed:** A subtle optimization: we never decrease `maxFreq`. When the window becomes invalid, instead of shrinking with a `while` loop, we slide one step (increment `left` by 1). Since `maxFreq` only increases, the window size either grows or stays the same -- it never shrinks. The final answer is `n - left`.

**Key insight:** We do not need the exact `maxFreq` for the current window -- we only care whether a window of size `ans + 1` is achievable. A stale (too-large) `maxFreq` may make us keep an invalid window temporarily, but since we only slide (not shrink), we never lose a previously found answer.

**Steps:**
1. Initialize `freq[26] = {0}`, `maxFreq = 0`, `left = 0`.
2. For each `right` from 0 to n-1:
   - Increment `freq[s[right]]`.
   - Update `maxFreq = max(maxFreq, freq[s[right]])`.
   - If `(right - left + 1) - maxFreq > k`:
     - Decrement `freq[s[left]]`, increment `left`.
3. Return `n - left`.

**Why this works:** The window size `right - left + 1` only grows when `maxFreq` increases (making a larger window valid). When `maxFreq` does not increase, the window slides without growing. The answer is the largest window we ever achieved, which equals `n - left` at the end.

| Time | Space |
|------|-------|
| O(n) | O(26) = O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "Each character is visited at most twice (once by right, once by left). The inner while loop across all iterations moves left at most n times total."
**Space:** O(1) -- "The frequency array is fixed at 26 slots regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Trying to decrease `maxFreq` when shrinking the window -- this would require scanning all 26 frequencies each time (still O(1) per step, but unnecessary complexity). The non-shrinking approach avoids this entirely.
2. Off-by-one in window length: `right - left + 1`, not `right - left`.
3. Forgetting that `k` can be 0 (no replacements allowed) or `k >= n` (can make entire string uniform).

### Edge Cases to Test
- [ ] All same characters (`"AAAA"`, k=1 --> answer is n)
- [ ] All different characters (`"ABCDE"`, k=0 --> answer is 1)
- [ ] k >= n (answer is always n)
- [ ] Single character string
- [ ] k = 0 (longest run of a single character)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the longest substring where, after at most k replacements, all characters are the same."
2. **Match:** "Longest substring with a constraint --> sliding window pattern."
3. **Plan:** "Track character frequencies in the window. A window is valid if `windowLen - maxFreq <= k`. Expand right, shrink left when invalid."
4. **Implement:** Write the non-shrinking version. Explain why `maxFreq` never needs to decrease.
5. **Review:** Walk through the dry run.
6. **Evaluate:** "O(n) time, O(1) space."

### Follow-Up Questions
- "What if the string contains lowercase letters too?" --> Expand frequency array to 52 or use a hashmap. Same algorithm.
- "What if you need to return the actual substring, not just the length?" --> Track `bestLeft` and `bestRight` when updating the answer.
- "What about minimum window instead of maximum?" --> Different pattern (like minimum window substring LC #76).

---

## CONNECTIONS
- **Prerequisite:** Basic sliding window, frequency counting
- **Same Pattern:** Longest Substring Without Repeating Characters (LC #3), Max Consecutive Ones III (LC #1004)
- **Harder Variant:** Minimum Window Substring (LC #76), Substring with Concatenation of All Words (LC #30)
- **This Unlocks:** Advanced sliding window problems where the validity condition involves frequency analysis
