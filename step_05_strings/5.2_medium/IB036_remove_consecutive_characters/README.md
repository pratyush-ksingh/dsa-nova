# Remove Consecutive Characters

> **Batch 3 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a string `s`, remove all consecutive duplicate characters. Keep only the first occurrence of each consecutive group. Return the resulting string.

**Source:** InterviewBit

**Constraints:**
- `1 <= len(s) <= 10^5`
- `s` consists of lowercase English letters

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `"aabccba"` | `"abcba"` | aa->a, cc->c; non-consecutive duplicates (a,b) remain |
| `"aab"` | `"ab"` | Two consecutive 'a's become one |
| `"abcd"` | `"abcd"` | No consecutive duplicates -- unchanged |
| `"aaaa"` | `"a"` | All same characters collapse to one |
| `"aabbcc"` | `"abc"` | Each pair collapses |

### Real-Life Analogy
> *Imagine typing on a keyboard that is sticky, and each key press sometimes registers twice. You want to clean the output so that repeated keystrokes count as one. You walk through the text character by character: if the current character is the same as the previous one, you skip it. Otherwise, you keep it. This produces the "de-bounced" text.*

### Key Observations
1. We only remove **consecutive** duplicates, NOT all duplicates. `"abcba"` stays as-is because the a's are not adjacent.
2. We need to compare each character with its predecessor -- a single left-to-right scan suffices.
3. Building a result string by appending characters that differ from the previous one is the core logic. <-- This is the "aha" insight.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A StringBuilder (Java) or list (Python) to build the result efficiently. Appending to a string directly in Java is O(n^2) due to immutability.
- No need for stacks, maps, or sets -- a simple sequential comparison does the job.

### Pattern Recognition
- **Pattern:** Sequential Deduplication (compare with previous)
- **Classification Cue:** "When you see _remove consecutive duplicates_ --> think _compare current with last kept character_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Repeated Scanning)
**Idea:** Repeatedly scan the string, removing adjacent duplicates each pass, until no more changes occur.

**Steps:**
1. Set `changed = true`.
2. While `changed`:
   - Set `changed = false`.
   - Scan left to right. When `s[i] == s[i-1]`, skip `s[i]` and set `changed = true`.
   - Build the reduced string.
3. Return the result.

**Why it works:** Each pass removes at least some duplicates. Eventually the string stabilizes.

**BUD -- Unnecessary Work:** For simple consecutive deduplication, one pass is enough. Multiple passes are only needed for problems where removal creates NEW adjacencies (like "remove all adjacent duplicates recursively"). Here, a single pass suffices.

| Time | Space |
|------|-------|
| O(n) per pass, but only 1 pass needed = O(n) | O(n) for new string |

### Approach 2: Optimal -- Single Pass with StringBuilder
**What changed:** One left-to-right scan. Keep a character only if it differs from the previous character in the result.

**Steps:**
1. Initialize `result` with the first character.
2. For each character `c` from index 1 to n-1:
   - If `c != last character of result`, append `c` to result.
3. Return `result`.

**Dry Run:** Input = `"aabccba"`

| i | c | last in result | Action | result |
|---|---|---------------|--------|--------|
| 0 | a | (init) | Add | `"a"` |
| 1 | a | a | Skip | `"a"` |
| 2 | b | a | Add | `"ab"` |
| 3 | c | b | Add | `"abc"` |
| 4 | c | c | Skip | `"abc"` |
| 5 | b | c | Add | `"abcb"` |
| 6 | a | b | Add | `"abcba"` |

**Result:** `"abcba"`

| Time | Space |
|------|-------|
| O(n) | O(n) for the result string |

### Approach 3: Best -- In-Place with Write Pointer (for char arrays)
**What changed:** If we convert to a char array, we can use a write pointer to compact the result in-place, avoiding a separate builder.

**Steps:**
1. Convert string to char array.
2. Set `write = 0`.
3. For `i` from `1` to `n - 1`:
   - If `arr[i] != arr[write]`, increment `write` and set `arr[write] = arr[i]`.
4. Return new string from `arr[0..write]`.

| Time | Space |
|------|-------|
| O(n) | O(n) for char array (strings are immutable in Java/Python) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We process each character exactly once, comparing it with the previous character in O(1)."
**Space:** O(n) -- "We need space for the output string. In languages with mutable strings, this could be O(1) auxiliary."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Confusing this with "remove ALL duplicates" (which removes non-consecutive ones too).
2. Confusing with "remove all adjacent duplicates" (stack-based, where removal creates new adjacencies).
3. Starting the loop at index 0 and comparing with `s[-1]` -- off-by-one or wrap-around error.

### Edge Cases to Test
- [ ] No duplicates `"abcd"` --> `"abcd"`
- [ ] All same `"aaaa"` --> `"a"`
- [ ] Single character `"x"` --> `"x"`
- [ ] Alternating `"ababab"` --> `"ababab"` (no consecutive dups)
- [ ] Duplicates at start `"aabc"` --> `"abc"`
- [ ] Duplicates at end `"abcc"` --> `"abc"`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Remove only consecutive duplicates, not all duplicates globally? So 'abcba' stays unchanged?"
2. **Approach:** "Single pass: compare each character with the last one we kept. If different, keep it. O(n) time."
3. **Code:** Use StringBuilder in Java, list with join in Python.
4. **Test:** Walk through `"aabccba"` showing the result building character by character.

### Follow-Up Questions
- "What if you need to recursively remove all adjacent duplicates until none remain?" --> Stack-based approach: push if top differs, pop if matches. (LeetCode #1047)
- "What about removing adjacent duplicates that appear k times?" --> Stack with counts (LeetCode #1209).
- "What if this is a stream of characters?" --> Keep only the last character seen; emit when the next character differs.

---

## CONNECTIONS
- **Prerequisite:** Basic string traversal, character comparison
- **Same Pattern:** Remove Duplicates from Sorted Array (same write-pointer logic on arrays)
- **Harder Variant:** Remove All Adjacent Duplicates in String (stack-based, LeetCode #1047)
- **This Unlocks:** Understanding the "compare with previous" pattern for deduplication
