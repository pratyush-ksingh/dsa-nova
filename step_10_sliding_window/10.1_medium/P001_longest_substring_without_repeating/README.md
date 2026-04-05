# Longest Substring Without Repeating Characters

> **Batch 2 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a string `s`, find the length of the **longest substring** without repeating characters.

A **substring** is a contiguous sequence of characters within the string.

**LeetCode #3**

**Constraints:**
- `0 <= s.length <= 5 * 10^4`
- `s` consists of English letters, digits, symbols, and spaces

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `"abcabcbb"` | `3` | `"abc"` is the longest without repeats |
| `"bbbbb"` | `1` | `"b"` -- all characters are the same |
| `"pwwkew"` | `3` | `"wke"` -- note `"pwke"` is a subsequence, not a substring |
| `""` | `0` | Empty string |

### Real-Life Analogy
> *Imagine you are reading a book and highlighting a passage with a marker. You want the longest stretch of text where no letter appears twice. You start highlighting from the left. The moment you see a repeated letter, you lift the marker and start again from just after where that letter first appeared. You keep track of the longest highlighted section you ever had. This "start from just after the repeat" logic is the sliding window.*

### Key Observations
1. A brute-force approach checks all O(n^2) substrings and validates each for uniqueness -- that is O(n^3) total.
2. If the current window `[left, right]` has no repeats, we can try expanding `right`. If a repeat is found, we shrink from `left`. This is the sliding window pattern.
3. Using a hash map to store the **last seen index** of each character lets us jump `left` directly to `lastSeen[char] + 1`, avoiding one-by-one shrinking. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Hash Map / Hash Set:** To track which characters are currently in the window (and optionally where they last appeared), giving O(1) lookup per character.
- **Two Pointers (left, right):** Define the sliding window boundaries.

### Pattern Recognition
- **Pattern:** Variable-Size Sliding Window
- **Classification Cue:** "When you see _longest/shortest substring/subarray with a condition_ --> think _sliding window with two pointers_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Check All Substrings)
**Idea:** Generate every possible substring, check if it has all unique characters, track the longest.

**Steps:**
1. For each starting index `i` from 0 to n-1:
   - For each ending index `j` from `i` to n-1:
     - Check if substring `s[i..j]` has all unique characters (using a set).
     - If yes, update `maxLen = max(maxLen, j - i + 1)`.
2. Return `maxLen`.

**Why we move on:** **Bottleneck** -- O(n^2) substrings, each taking O(n) to validate = O(n^3) total.

| Time | Space |
|------|-------|
| O(n^3) | O(min(n, 26)) for the set |

### Approach 2: Sliding Window with Hash Set
**What changed:** Instead of restarting from scratch, maintain a window `[left, right]`. Expand `right` when no repeats; shrink `left` when there is a repeat.

**Steps:**
1. Initialize `left = 0`, `maxLen = 0`, and a hash set `window`.
2. Iterate `right` from 0 to n-1:
   - While `s[right]` is in `window`: remove `s[left]` from set, increment `left`.
   - Add `s[right]` to `window`.
   - Update `maxLen = max(maxLen, right - left + 1)`.
3. Return `maxLen`.

**Why we move on:** Each character might be added and removed multiple times in the worst case. While amortized O(n), the inner while loop can be eliminated.

| Time | Space |
|------|-------|
| O(2n) = O(n) | O(min(n, 26)) |

### Approach 3: Best -- Sliding Window with Hash Map (Jump Optimization)
**What changed:** Store the **last index** where each character was seen. When we encounter a repeat, jump `left` directly past the previous occurrence instead of removing characters one by one.

**Steps:**
1. Initialize `left = 0`, `maxLen = 0`, and a hash map `lastSeen` (char -> index).
2. Iterate `right` from 0 to n-1:
   - If `s[right]` is in `lastSeen` AND `lastSeen[s[right]] >= left`:
     - Jump `left = lastSeen[s[right]] + 1`.
   - Update `lastSeen[s[right]] = right`.
   - Update `maxLen = max(maxLen, right - left + 1)`.
3. Return `maxLen`.

**Why the `>= left` check matters:** If a character was seen before `left`, it is no longer in our current window, so we should not jump.

**Dry Run:** Input = `"abcabcbb"`

| right | char | lastSeen before | left before | action | left after | window | maxLen |
|-------|------|-----------------|-------------|--------|------------|--------|--------|
| 0     | a    | {}              | 0           | add a:0 | 0          | `a` | 1 |
| 1     | b    | {a:0}           | 0           | add b:1 | 0          | `ab` | 2 |
| 2     | c    | {a:0,b:1}       | 0           | add c:2 | 0          | `abc` | 3 |
| 3     | a    | {a:0,b:1,c:2}   | 0           | a at 0 >= left=0, jump left=1 | 1 | `bca` | 3 |
| 4     | b    | {a:3,b:1,c:2}   | 1           | b at 1 >= left=1, jump left=2 | 2 | `cab` | 3 |
| 5     | c    | {a:3,b:4,c:2}   | 2           | c at 2 >= left=2, jump left=3 | 3 | `abc` | 3 |
| 6     | b    | {a:3,b:4,c:5}   | 3           | b at 4 >= left=3, jump left=5 | 5 | `cb` | 3 |
| 7     | b    | {a:3,b:6,c:5}   | 5           | b at 6 >= left=5, jump left=7 | 7 | `b` | 3 |

**Result:** 3

| Time | Space |
|------|-------|
| O(n) | O(min(n, 26)) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "The `right` pointer visits each character exactly once. The `left` pointer only moves forward. Total work is linear."
**Space:** O(min(n, charset_size)) -- "The hash map stores at most one entry per unique character. For lowercase English letters, that is at most 26."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting the `lastSeen[char] >= left` check -- without it, you might jump `left` backward, which shrinks the window incorrectly and gives wrong answers.
2. Not handling the empty string -- should return 0.
3. Off-by-one: the window length is `right - left + 1`, not `right - left`.
4. Using an array of size 26 when the input can contain digits, symbols, and spaces -- use size 128 (ASCII) or a hash map.

### Edge Cases to Test
- [ ] Empty string `""` --> return 0
- [ ] Single character `"a"` --> return 1
- [ ] All same characters `"aaaa"` --> return 1
- [ ] All unique characters `"abcdef"` --> return 6
- [ ] Repeats at the very end `"abcda"` --> return 4
- [ ] String with spaces and special chars `"a b c"` --> return 3

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Can the string be empty? What characters can it contain -- just lowercase, or full ASCII?"
2. **Approach:** "I will use a sliding window with two pointers. A hash map stores the last index of each character so I can jump the left pointer directly past duplicates."
3. **Code:** Write the optimal hash map version. Mention the set version as a stepping stone.
4. **Test:** Walk through `"abcabcbb"` step by step, showing how `left` jumps.

### Follow-Up Questions
- "What if you need the actual substring, not just the length?" --> Track `start` index when updating `maxLen`.
- "What if at most k repeats are allowed?" --> Sliding window + frequency map; shrink when any frequency exceeds k.

---

## CONNECTIONS
- **Prerequisite:** Two pointers, hash map basics
- **Same Pattern:** Minimum Window Substring (#76), Longest Substring with At Most K Distinct Characters
- **Harder Variant:** Longest Substring with At Most K Distinct Characters, Minimum Window Substring
- **This Unlocks:** Foundation for all variable-size sliding window problems on strings
