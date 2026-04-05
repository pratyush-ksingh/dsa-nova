# Max Nesting Depth of Parentheses

> **Batch 1 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
A string is a **valid parentheses string (VPS)** if it is `""`, a single non-parenthesis character, the concatenation of two VPS strings, or `"(" + A + ")"` where A is a VPS. The **nesting depth** `depth(s)` is defined recursively: `depth("") = 0`, `depth(C) = 0` for non-paren chars, `depth(A + B) = max(depth(A), depth(B))`, and `depth("(" + A + ")") = 1 + depth(A)`.

Given a VPS `s`, return the **maximum nesting depth**.

**LeetCode #1614**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"(1+(2*3)+((8)/4))+1"` | `3` | The `8` is inside `((8)/4)` which is inside the outer `(...)`, giving depth 3. |
| `"(1)+((2))+(((3)))"` | `3` | The `3` is nested three levels deep. |
| `"1+(2*3)/(2-1)"` | `1` | Maximum one level of nesting. |
| `"1"` | `0` | No parentheses at all. |

### Constraints
- `1 <= s.length <= 100`
- `s` consists of digits `0-9`, `+`, `-`, `*`, `/`, `(`, `)`
- `s` is a valid parentheses string

### Real-Life Analogy
Imagine stacking bowls inside each other (Russian nesting dolls). Each `(` adds a bowl on top, each `)` removes one. The maximum nesting depth is the tallest the stack of bowls ever gets.

### 3 Key Observations
1. **"aha" -- depth = open count:** At any position, the current nesting depth equals the number of currently unmatched `(` characters. Just count them.
2. **"aha" -- ignore non-parens:** Characters that are not `(` or `)` have no effect on depth. Skip them entirely.
3. **"aha" -- one pass is enough:** We only need the maximum depth seen during the scan, so a running max suffices.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Single integer counter:** All we need is the current depth (number of open parens) and the running maximum. No stack, no array, no map.
- A stack would work (push on `(`, pop on `)`, track max stack size) but is overkill.

### Pattern Recognition Cue
"Nesting depth" in a valid parentheses string always means: track the running count of unmatched opens. The pattern is identical to Remove Outermost Parentheses -- depth counter.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Stack
**Intuition:** Push `(` onto a stack, pop on `)`. Track the maximum stack size at any point.

**Steps:**
1. Initialize an empty stack and `maxDepth = 0`.
2. For each character in `s`:
   - If `(`, push onto stack. Update `maxDepth = max(maxDepth, stack.size())`.
   - If `)`, pop from stack.
   - Otherwise, skip.
3. Return `maxDepth`.

**BUD Transition:** The stack stores actual characters but we never inspect them. Replace with an integer counter to save O(n) space.

**Dry Run:** `s = "(1+(2*3)+((8)/4))+1"`
```
i=0  '('  stack=['(']       maxDepth=1
i=1  '1'  skip
i=2  '+'  skip
i=3  '('  stack=['(','(']   maxDepth=2
i=4  '2'  skip
i=5  '*'  skip
i=6  '3'  skip
i=7  ')'  stack=['(']
i=8  '+'  skip
i=9  '('  stack=['(','(']   maxDepth=2
i=10 '('  stack=['(','(','('] maxDepth=3
i=11 '8'  skip
i=12 ')'  stack=['(','(']
i=13 '/'  skip
i=14 '4'  skip
i=15 ')'  stack=['(']
i=16 ')'  stack=[]
i=17 '+'  skip
i=18 '1'  skip
Result: 3
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) -- stack |

---

### Approach 2: Optimal -- Counter-Based
**Intuition:** Replace the stack with a single integer `depth`. Increment on `(`, decrement on `)`, track the max.

**Steps:**
1. `depth = 0`, `maxDepth = 0`.
2. For each char `c` in `s`:
   - If `c == '('`: `depth++`, update `maxDepth = max(maxDepth, depth)`.
   - If `c == ')'`: `depth--`.
3. Return `maxDepth`.

**BUD Transition from Brute:** Eliminated the stack. O(1) space.

**Dry Run:** `s = "(1+(2*3)+((8)/4))+1"`
```
'(' depth=1 max=1
'(' depth=2 max=2
')' depth=1
'(' depth=2 max=2
'(' depth=3 max=3
')' depth=2
')' depth=1
')' depth=0
Final maxDepth=3
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Same as Optimal (Proven Lower Bound)
**Intuition:** We must read every character to know if it is a parenthesis. O(n) time is mandatory. O(1) space is minimal. The counter approach is provably optimal.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** One pass over the string, O(1) work per character (a comparison and possibly an increment).
- **Space O(1):** Two integers: `depth` and `maxDepth`. Nothing else.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `"1"` (no parens) | `0` | Must handle strings with no parentheses |
| `"()"` | `1` | Simplest nesting |
| `"(((())))"` | `4` | Deeply nested single chain |
| `"()()()"` | `1` | Sequential but not nested -- max is still 1 |

**Common Mistakes:**
- Forgetting to handle non-parenthesis characters (treating every char as a paren).
- Updating max *after* decrementing on `)` instead of only on `(`, which would miss the correct depth.
- Returning `depth` instead of `maxDepth` -- at the end, depth is 0 for valid input.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need the maximum number of nested open parens at any point in the string."
2. **Match:** "This is a running-depth problem. A counter gives O(1) space."
3. **Plan:** Describe the counter approach. Mention the stack alternative briefly.
4. **Implement:** Write the 5-line counter solution.
5. **Review:** Trace through example 1 showing depth changes.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What if parens might be invalid?"* -- Check if depth ever goes negative, or is nonzero at end.
- *"Can you split the string into two halves with equal max depth?"* -- Related to LC #1111 (Maximum Nesting Depth of Two Valid Parentheses Strings).
- *"What if brackets `[]` and braces `{}` are also present?"* -- Need a stack (type matters).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Valid Parentheses (LC #20) |
| **Same Pattern** | Remove Outermost Parentheses (LC #1021) -- same depth counter technique |
| **Harder** | Maximum Nesting Depth of Two Valid Parentheses Strings (LC #1111) |
| **Unlocks** | Score of Parentheses (LC #856), Minimum Add to Make Parentheses Valid (LC #921) |
