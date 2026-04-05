# Rotate String

> **Batch 3 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given two strings `s` and `goal`, return `true` if and only if `s` can become `goal` after some number of left rotations. A left rotation moves the first character to the end: `"abcde"` -> `"bcdea"`.

**LeetCode #796**

**Constraints:**
- `1 <= s.length, goal.length <= 100`
- `s` and `goal` consist of lowercase English letters

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `s = "abcde", goal = "cdeab"` | `true` | Rotate left twice: abcde -> bcdea -> cdeab |
| `s = "abcde", goal = "abced"` | `false` | No rotation of "abcde" produces "abced" |
| `s = "a", goal = "a"` | `true` | Zero rotations (or n rotations) |
| `s = "aa", goal = "aa"` | `true` | Any rotation of "aa" is "aa" |

### Real-Life Analogy
> *Think of a circular conveyor belt with letters printed on it. No matter where you start reading, you are reading a rotation of the same sequence. Two strings are rotations of each other if they represent the same circular belt. The trick: if you paste two copies of the belt end-to-end, every possible starting point appears as a substring.*

### Key Observations
1. If `len(s) != len(goal)`, the answer is immediately `false` -- rotations preserve length.
2. All rotations of `s` are substrings of `s + s`. <-- This is the "aha" insight.
3. So the check reduces to: `len(s) == len(goal)` AND `goal in (s + s)`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- Strings support concatenation and substring search. No auxiliary structures needed.
- The `s + s` trick converts rotation detection into substring detection, which languages handle efficiently.

### Pattern Recognition
- **Pattern:** String Doubling (concatenation trick for cyclic/rotation problems)
- **Classification Cue:** "When you see _is X a rotation of Y_ --> think _Y is a substring of X + X_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Try All Rotations)
**Idea:** Generate every possible left rotation of `s` and check if any equals `goal`.

**Steps:**
1. If `len(s) != len(goal)`, return `false`.
2. For `i` from `0` to `n - 1`:
   - Build rotation: `s[i:] + s[:i]`.
   - If it equals `goal`, return `true`.
3. Return `false`.

**Why it works:** There are exactly `n` distinct rotations; we check each one.

**BUD -- Unnecessary Work:** Each rotation creates a new string (O(n)) and comparison is O(n), so total is O(n^2). We can do better.

| Time | Space |
|------|-------|
| O(n^2) | O(n) for the rotated string |

### Approach 2: Optimal -- Concatenation Trick (s + s)
**What changed:** Instead of generating each rotation, concatenate `s` with itself. Every rotation of `s` appears as a contiguous substring in `s + s`. Just check if `goal` is a substring.

**Steps:**
1. If `len(s) != len(goal)`, return `false`.
2. Return `goal in (s + s)`.

**Why this works:** `s + s = "abcdeabcde"`. Substrings of length 5 starting at each index: `abcde`, `bcdea`, `cdeab`, `deabc`, `eabcd` -- exactly all rotations.

**Dry Run:** `s = "abcde", goal = "cdeab"`
- `s + s = "abcdeabcde"`
- Search for `"cdeab"` in `"abcdeabcde"` --> found at index 2.
- Return `true`.

| Time | Space |
|------|-------|
| O(n) using built-in substring search (KMP/Rabin-Karp internally) | O(n) for the concatenated string |

### Approach 3: Best -- KMP on Circular Text
**What changed:** Instead of physically concatenating, use KMP pattern matching treating the text as circular. This avoids creating the doubled string.

**Steps:**
1. If `len(s) != len(goal)`, return `false`.
2. Build KMP failure function for `goal`.
3. Run KMP on `s` treating it as circular (when you reach the end, wrap to the beginning). Stop after `2n` character checks.
4. If a full match is found, return `true`. Else `false`.

This is more code for the same complexity, so in practice Approach 2 is preferred. This variant is useful when memory is very tight or the string is on a circular buffer.

| Time | Space |
|------|-------|
| O(n) | O(n) for KMP failure array (but no doubled string) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We create a string of length 2n and do a substring search, both linear operations."
**Space:** O(n) -- "The concatenated string `s + s` is of length 2n."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting the length check -- `"abc"` and `"abcabc"` share a substring relationship but are not rotations.
2. Checking `s in (goal + goal)` instead of `goal in (s + s)` -- both work, but be consistent.
3. Empty strings -- both empty should return `true`.

### Edge Cases to Test
- [ ] Identical strings `s = goal = "abc"` --> true (0 rotations)
- [ ] Different lengths --> false
- [ ] Single character, same --> true
- [ ] Single character, different --> false
- [ ] All same characters `"aaa"` and `"aaa"` --> true
- [ ] Anagram but not rotation `"abc"` and `"bac"` --> false

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Left rotation only, or any direction? Does zero rotations count?"
2. **Approach:** "Key insight: every rotation of s appears as a substring of s+s. So I check `len(s) == len(goal)` and `goal in (s + s)`. This is O(n) time."
3. **Code:** Write the two-line solution. Mention the brute force first to show progression.
4. **Test:** Walk through `"abcde"` and `"cdeab"` showing the doubled string.

### Follow-Up Questions
- "What if you need to find the rotation amount?" --> Find the index where `goal` starts in `s + s`.
- "What about right rotations?" --> A right rotation by k is a left rotation by n - k. Same solution works.
- "How does the built-in substring search work?" --> Discuss KMP or Rabin-Karp.

---

## CONNECTIONS
- **Prerequisite:** String concatenation, substring search
- **Same Pattern:** Repeated Substring Pattern (LeetCode #459 uses the same s+s trick)
- **Harder Variant:** Minimum Rotations to Match, Cyclic Permutation Detection
- **This Unlocks:** Understanding the "double the string" technique for all cyclic string problems
