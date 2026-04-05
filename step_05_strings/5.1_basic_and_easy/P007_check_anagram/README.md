# Check Anagram

> **Batch 3 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise. An anagram is a word formed by rearranging the letters of another word, using all the original letters exactly once.

**LeetCode #242**

**Constraints:**
- `1 <= s.length, t.length <= 5 * 10^4`
- `s` and `t` consist of lowercase English letters

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `s = "anagram", t = "nagaram"` | `true` | Same letters, same frequencies |
| `s = "rat", t = "car"` | `false` | 't' in "rat" vs 'c' in "car" differ |
| `s = "a", t = "a"` | `true` | Single identical character |
| `s = "ab", t = "ba"` | `true` | Same letters rearranged |

### Real-Life Analogy
> *Imagine you have a bag of Scrabble tiles spelling out "listen". Your friend has tiles spelling "silent". To check if they are anagrams, you dump both bags on the table and count each letter. If every letter appears the same number of times in both piles, they are anagrams. You do not care about the order -- only the inventory matters.*

### Key Observations
1. If the lengths differ, they cannot be anagrams -- different total letter counts.
2. Two strings are anagrams if and only if their character frequency distributions are identical.
3. For lowercase English only (26 letters), a fixed-size frequency array is enough. <-- This is the "aha" insight: we reduce string comparison to frequency comparison.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A frequency array of size 26 (for lowercase letters) gives O(1) access per character.
- A hash map generalizes to Unicode but is overkill when the alphabet is small and fixed.

### Pattern Recognition
- **Pattern:** Frequency Counting (character histogram)
- **Classification Cue:** "When you see _anagram / permutation / rearrangement_ --> think _compare frequency counts_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Sort Both Strings)
**Idea:** If two strings are anagrams, sorting them produces the same result.

**Steps:**
1. If `len(s) != len(t)`, return `false`.
2. Sort both strings.
3. Compare the sorted versions character by character.

**Why it works:** Sorting normalizes the order, so anagrams become identical.

**BUD -- Bottleneck:** Sorting is O(n log n). We only need to compare frequencies, not order.

| Time | Space |
|------|-------|
| O(n log n) | O(n) for sorted copies (or O(1) if in-place sort on char arrays) |

### Approach 2: Optimal -- Frequency Array (Two Passes)
**What changed:** Count character frequencies for each string and compare.

**Steps:**
1. If `len(s) != len(t)`, return `false`.
2. Create two frequency arrays of size 26, initialized to 0.
3. For each character in `s`, increment its count.
4. For each character in `t`, increment its count.
5. Compare both arrays. If identical, return `true`.

**Dry Run:** `s = "anagram", t = "nagaram"`
- freq_s: a=3, n=1, g=1, r=1, m=1
- freq_t: n=1, a=3, g=1, r=1, m=1
- Identical --> `true`

| Time | Space |
|------|-------|
| O(n) | O(1) -- fixed 26-element array |

### Approach 3: Best -- Single Frequency Array (One Pass Increment/Decrement)
**What changed:** Use a single frequency array. Increment for characters in `s`, decrement for characters in `t`. If all counts end at zero, the strings are anagrams.

**Steps:**
1. If `len(s) != len(t)`, return `false`.
2. Create one frequency array of size 26.
3. For each index `i`:
   - `freq[s[i] - 'a']++`
   - `freq[t[i] - 'a']--`
4. Check if all values in `freq` are zero.

This uses half the memory of Approach 2 and combines both counting passes into one loop.

| Time | Space |
|------|-------|
| O(n) | O(1) -- single 26-element array |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan each string once, doing O(1) work per character. The final check over the 26-element array is O(1)."
**Space:** O(1) -- "The frequency array has a fixed size of 26, independent of input length."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting the length check -- different-length strings are never anagrams.
2. Using a HashMap when a simple array suffices for lowercase letters.
3. Not handling case sensitivity -- "A" vs "a" depends on the problem statement.

### Edge Cases to Test
- [ ] Different lengths `"abc"` vs `"abcd"` --> false
- [ ] Single character, same --> true
- [ ] Single character, different --> false
- [ ] All same characters `"aaa"` vs `"aaa"` --> true
- [ ] Same characters, different counts `"aab"` vs `"abb"` --> false
- [ ] Empty strings (if allowed) --> true

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Are the strings lowercase only? Is it case-sensitive? Can they contain spaces or special characters?"
2. **Approach:** "Sorting works but is O(n log n). Instead I'll use a frequency array of size 26. Increment for s, decrement for t. If all zeros, they're anagrams. O(n) time, O(1) space."
3. **Code:** Write the single-array solution. It is concise and impressive.
4. **Test:** Walk through `"anagram"` and `"nagaram"` showing frequency deltas.

### Follow-Up Questions
- "What if the inputs contain Unicode?" --> Use a HashMap instead of fixed array.
- "What about checking if one string is a permutation of a substring of another?" --> Sliding window with frequency array (LeetCode #567 Permutation in String).
- "Can you group anagrams?" --> Sort each string as a key, group by key (LeetCode #49).

---

## CONNECTIONS
- **Prerequisite:** Character-to-index mapping, frequency counting
- **Same Pattern:** Ransom Note, First Unique Character, Valid Palindrome
- **Harder Variant:** Group Anagrams (LeetCode #49), Minimum Window Substring
- **This Unlocks:** Frequency counting as the backbone of sliding window and substring problems
