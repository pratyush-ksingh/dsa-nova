# Longest Common Prefix

> **Batch 2 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Write a function to find the **longest common prefix** string amongst an array of strings. If there is no common prefix, return an empty string `""`.

**LeetCode #14**

**Constraints:**
- `1 <= strs.length <= 200`
- `0 <= strs[i].length <= 200`
- `strs[i]` consists of only lowercase English letters.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `["flower","flow","flight"]` | `"fl"` | "fl" is common to all three |
| `["dog","racecar","car"]` | `""` | No common prefix |
| `["interspecies","interstellar","interstate"]` | `"inters"` | All share "inters" |
| `["a"]` | `"a"` | Single string is its own prefix |
| `["","abc"]` | `""` | Empty string means no prefix |

### Real-Life Analogy
> *Imagine you have a stack of mailing labels and you want to find the longest common address prefix (e.g., "123 Main St, Apt"). You line them all up vertically and read column by column from left to right. As long as every label has the same character in that column, you keep going. The moment any label disagrees (or runs out of characters), you stop. Everything you read before that point is the longest common prefix.*

### Key Observations
1. The common prefix can never be longer than the **shortest string** in the array.
2. We can compare character-by-character across all strings at the same position. The first position where any string disagrees (or ends) gives us our answer. <-- This is the "aha" insight
3. If the array has only one string, the answer is that entire string.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We work directly with the **String array** input. Each string supports O(1) character access by index.
- No need for tries, hashmaps, or sorting for this simple case.

### Pattern Recognition
- **Pattern:** Vertical Scanning (compare same position across all strings)
- **Classification Cue:** "When you see _find common property across multiple strings_ --> think _character-by-character comparison at each position_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Horizontal Scanning
**Idea:** Start with the first string as the prefix. Compare it with the second string and trim it. Then compare the trimmed prefix with the third string and trim again. Continue until all strings are processed.

**Steps:**
1. Set `prefix = strs[0]`.
2. For each string `s` in `strs[1:]`:
   - While `s` does not start with `prefix`:
     - Remove the last character from `prefix`.
     - If `prefix` is empty, return `""`.
3. Return `prefix`.

**Why it works:** We progressively narrow the prefix to match each string.

**Why we move on:** Not a major issue, but vertical scanning is more intuitive and can short-circuit faster when strings diverge early.

| Time | Space |
|------|-------|
| O(S) where S = sum of all character counts | O(m) where m = length of first string |

### Approach 2: Optimal -- Vertical Scanning
**What changed:** Instead of comparing string-by-string horizontally, compare position-by-position vertically across all strings.

**Steps:**
1. Find `minLen` = length of shortest string (the prefix can't exceed this).
2. For each column index `col` from 0 to minLen-1:
   - Let `c = strs[0][col]`.
   - For each string `s` in `strs`:
     - If `s[col] != c`, return `strs[0][0:col]`.
3. Return `strs[0][0:minLen]`.

**Dry Run:** Input = `["flower", "flow", "flight"]`

| col | char from strs[0] | flower[col] | flow[col] | flight[col] | All match? |
|-----|-------------------|-------------|-----------|-------------|------------|
| 0 | 'f' | f | f | f | Yes |
| 1 | 'l' | l | l | l | Yes |
| 2 | 'o' | o | o | i | No -- stop! |

**Result:** `"fl"` (strs[0][0:2])

| Time | Space |
|------|-------|
| O(n * m) where n = number of strings, m = min string length | O(1) extra (just return a substring) |

### Approach 3: Sort-Based Approach
**Idea:** Sort the array lexicographically. The common prefix of the entire array is just the common prefix of the first and last strings after sorting (since they are the most different).

**Steps:**
1. Sort `strs` lexicographically.
2. Compare `strs[0]` (first) and `strs[n-1]` (last) character by character.
3. Return the matching prefix.

**Why it works:** After sorting, all strings between first and last are "sandwiched" lexicographically, so any prefix shared by first and last is shared by all.

| Time | Space |
|------|-------|
| O(n * m * log n) for sorting | O(m) for the result or O(n * m) depending on sort |

*Note:* Vertical scanning (Approach 2) is the best for interviews. The sort approach is clever but slower due to the O(n * m * log n) sort.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n * m) -- "For each character position (up to m), we check all n strings. We stop early at the first mismatch."
**Space:** O(1) extra -- "We only track the column index. The returned substring is the output, not auxiliary space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Not handling the empty array case (though constraints guarantee >= 1 string).
2. Not handling a string of length 0 in the array -- the answer is immediately `""`.
3. Index out of bounds: accessing `s[col]` without checking that `col < len(s)`.

### Edge Cases to Test
- [ ] Single string `["hello"]` --> `"hello"`
- [ ] Empty string in array `["", "abc"]` --> `""`
- [ ] All identical `["abc", "abc", "abc"]` --> `"abc"`
- [ ] No common prefix `["abc", "xyz"]` --> `""`
- [ ] Common prefix is one character `["apple", "ape", "art"]` --> `"a"`
- [ ] All single-character strings `["a", "a", "a"]` --> `"a"`

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the longest prefix common to all strings. What if the array is empty or contains empty strings?"
2. **Match:** "This is a vertical scanning problem -- compare column by column across all strings."
3. **Plan:** "I'll iterate over character positions. At each position, I check if all strings have the same character. First mismatch means I stop."
4. **Implement:** Write the nested loop.
5. **Review:** Trace through `["flower", "flow", "flight"]`.
6. **Evaluate:** "O(n * m) time, O(1) space. Optimal since we must read all relevant characters."

### Follow-Up Questions
- "What if the strings are stored in a distributed system?" --> Divide and conquer: split array in half, find LCP of each half, then find LCP of the two results.
- "What if you need to do this query many times?" --> Build a Trie. Finding the LCP is just following single-child nodes from the root.
- "What about binary search on the prefix length?" --> Binary search on the length [0, minLen], checking if all strings share that prefix. Same time complexity but interesting approach.

---

## CONNECTIONS
- **Prerequisite:** String traversal, nested loops
- **Same Pattern:** String comparison problems, vertical scanning
- **Related:** Trie data structure (stores common prefixes efficiently)
- **This Unlocks:** Understanding Trie motivation, divide-and-conquer on strings
