# Valid Parentheses

> **Batch 3 of 12** | **Topic:** Stack | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a string `s` containing only the characters `(`, `)`, `{`, `}`, `[`, `]`, determine if the input string is **valid**. A string is valid if:
1. Every open bracket is closed by the same type of bracket.
2. Open brackets are closed in the correct order (innermost first).
3. Every close bracket has a corresponding open bracket of the same type.

**LeetCode #20 - Valid Parentheses**

**Example:**
```
Input: "()[]{}"
Output: true

Input: "(]"
Output: false

Input: "([)]"
Output: false

Input: "{[]}"
Output: true
```

| Input | Output | Explanation |
|-------|--------|-------------|
| "()" | true | Single matched pair |
| "()[]{}" | true | Three consecutive matched pairs |
| "(]" | false | Mismatched types |
| "([)]" | false | Correct types but wrong nesting order |
| "{[]}" | true | Nested brackets, inner closes before outer |
| "" | true | Empty string is trivially valid |
| "(" | false | Unmatched open bracket |

### Real-Life Analogy
Think of **Russian nesting dolls (Matryoshka)**. Each doll must close (be capped) before the next outer doll can close. If you open a large doll, then a medium, then a small, you must close (cap) the small first, then medium, then large. `{[()]}` is valid nesting. `{[(})]` is trying to cap the large before the medium -- impossible. A stack naturally models this: the most recently opened doll is on top and must be the first to close.

### Key Observations
1. **Observation:** Brackets must be closed in LIFO (Last-In-First-Out) order. This screams "stack."
2. **Observation:** When we see a closing bracket, the top of the stack must be the matching opening bracket. Any mismatch means invalid.
3. **Aha moment:** At the end, the stack must be empty. A non-empty stack means some opening brackets were never closed. Also, if we encounter a closing bracket with an empty stack, there is no matching opener.

### Constraints
- 1 <= s.length <= 10^4
- `s` consists of only `()[]{}` characters

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Stack?
The LIFO property of a stack perfectly matches the nesting requirement: the most recently opened bracket must be closed first. When we encounter an opener, push it. When we encounter a closer, pop and check if it matches.

### Pattern Recognition
**Classification cue:** "Matching / nesting / balanced brackets" --> Stack. This is the textbook stack application. The same pattern applies to: matching HTML/XML tags, validating mathematical expressions, and checking balanced code blocks.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Repeated Removal
**Intuition:** Repeatedly scan the string and remove adjacent matched pairs: `()`, `[]`, `{}`. If the string reduces to empty, it is valid. If no more pairs can be removed and the string is non-empty, it is invalid.

**Steps:**
1. While the string changes:
   - Replace all occurrences of `()`, `[]`, `{}` with empty string.
2. Return `s.isEmpty()`.

**Dry Run Trace ("{[()]}"):**

| Pass | String | Removed |
|------|--------|---------|
| Start | {[()]} | - |
| 1 | {[]} | Removed () |
| 2 | {} | Removed [] |
| 3 | (empty) | Removed {} |

Return true.

| Metric | Value |
|--------|-------|
| Time | O(n^2) -- each pass is O(n), up to n/2 passes |
| Space | O(n) -- string manipulation creates copies |

**BUD Transition:** The repeated scanning is wasteful. We can validate in a single pass using a stack to track unmatched openers.

---

### Approach 2: Optimal -- Stack-Based Matching
**Intuition:** Traverse the string character by character. Push every opening bracket onto the stack. When we encounter a closing bracket, pop the stack and verify it is the matching opener. If the stack is empty at a closer, or the match fails, return false. At the end, the stack must be empty.

**Steps:**
1. Create an empty stack.
2. For each character `c` in `s`:
   - If `c` is `(`, `[`, or `{`: push `c`.
   - Else (it is a closer):
     - If stack is empty, return false (no matching opener).
     - Pop the top. If it does not match `c`, return false.
3. Return `stack.isEmpty()`.

**Matching pairs:** `(` matches `)`, `[` matches `]`, `{` matches `}`.

**Dry Run Trace ("([)]"):**

| Step | Char | Stack | Action |
|------|------|-------|--------|
| 1 | ( | [(] | Push ( |
| 2 | [ | [(, [] | Push [ |
| 3 | ) | [(, [] | Pop [, but [ != match for ) --> return false |

Return false.

**Dry Run Trace ("{[]}"):**

| Step | Char | Stack | Action |
|------|------|-------|--------|
| 1 | { | [{] | Push { |
| 2 | [ | [{, [] | Push [ |
| 3 | ] | [{] | Pop [, matches ] -- OK |
| 4 | } | [] | Pop {, matches } -- OK |

Stack empty --> return true.

| Metric | Value |
|--------|-------|
| Time | O(n) -- single pass through the string |
| Space | O(n) -- stack can hold up to n/2 openers |

---

### Approach 3: Best -- Stack with Map Lookup (Clean Production Code)
**Intuition:** Same algorithm as Approach 2, but use a hash map to define bracket pairs for cleaner, more extensible code. Instead of if-else chains, map each closer to its expected opener. This makes the code trivially extensible to new bracket types.

**Steps:**
1. Create map: `)` -> `(`, `]` -> `[`, `}` -> `{`.
2. For each character:
   - If it is NOT in the map (it is an opener): push to stack.
   - Else: pop stack and check if popped == map[char].
3. Return stack is empty.

**Early exit optimization:** If string length is odd, return false immediately (cannot be balanced).

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) time:** Every character is processed exactly once -- push or pop, each O(1). No character is revisited.

**Why O(n) space:** In the worst case (all openers like `((((`), every character goes on the stack. However, for valid strings, the max stack depth is n/2 (when all openers come before all closers).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Popping from empty stack | Closing bracket with no opener | Check stack non-empty before popping |
| Forgetting final empty check | "((" has all pushes, no pops, but is invalid | Return `stack.isEmpty()` not just `true` |
| Wrong matching logic | Matching ( with ] instead of ) | Use a map or careful if-else |
| Not handling odd-length strings | Odd-length can never be valid | Optional early exit: `if (s.length() % 2 != 0) return false` |

### Edge Cases Checklist
- Empty string "" --> true
- Single character "(" --> false
- All openers "(((" --> false
- All closers ")))" --> false
- Interleaved wrong: "([)]" --> false
- Deeply nested: "((([[]])))" --> true

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Only bracket characters? Can the string be empty? What should empty return?"
2. **M**atch: "Bracket matching with nesting --> classic stack problem."
3. **P**lan: "Push openers, pop on closers, verify match. Check empty stack at end."
4. **I**mplement: Use a map for clean bracket matching.
5. **R**eview: Trace with "{[]}", "([)]", and "(".
6. **E**valuate: "O(n) time, O(n) space."

### Follow-Up Questions
- "What if the string contains other characters too?" --> Skip non-bracket characters during traversal.
- "What about `<` and `>`?" --> Add them to the map. The algorithm is the same.
- "Find the longest valid parentheses substring." --> Much harder, LeetCode #32 (Stack + DP).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Stack basics (push, pop, isEmpty) |
| **Same pattern** | Redundant Braces, Min Add to Make Valid, Score of Parentheses |
| **Next step** | Longest Valid Parentheses (#32) |
| **Harder variant** | Remove Invalid Parentheses (#301) |
| **Unlocks** | All bracket/nesting stack problems, expression evaluation |
