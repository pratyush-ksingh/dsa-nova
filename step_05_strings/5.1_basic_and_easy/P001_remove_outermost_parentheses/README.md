# Remove Outermost Parentheses

> **Batch 1 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
A valid parentheses string `s` is **primitive** if it is nonempty and cannot be split into two nonempty valid parentheses strings. Given a valid parentheses string `s`, find its **primitive decomposition** `s = p1 + p2 + ... + pk`, then remove the outermost parentheses of every primitive component and return the concatenation.

**LeetCode #1021**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"(()())(())"` | `"()()()"` | Primitives: `(()())` and `(())`. Remove outer: `()()` + `()` = `"()()()"` |
| `"(()())(())(()(()))"` | `"()()()()(())"` | Three primitives. Strip each outer pair. |
| `"()()"` | `""` | Two primitives `()` and `()`. Each becomes empty after stripping. |

### Constraints
- `1 <= s.length <= 10^5`
- `s` is a valid parentheses string
- `s[i]` is either `'('` or `')'`

### Real-Life Analogy
Think of nested gift boxes. Each primitive is one top-level box. You want to unwrap only the outermost packaging of each box, keeping everything inside intact. A single `()` is a box with nothing inside, so unwrapping it leaves nothing.

### 3 Key Observations
1. **"aha" -- depth tracking:** A primitive starts when the running balance goes from 0 to 1, and ends when it returns to 0. So a simple counter tells you exactly where each primitive begins and ends.
2. **"aha" -- skip at depth 1:** The outermost `(` is the one that takes depth from 0->1, and the outermost `)` is the one that takes depth from 1->0. Every other character is "inner" and should be kept.
3. **"aha" -- no stack needed:** Since we only care about depth (not matching), a single integer counter replaces a full stack.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Counter (int):** Tracks nesting depth. Increment on `(`, decrement on `)`. This is the minimal state needed.
- A stack would work but is overkill -- we never need to know *which* parenthesis matched, just the current depth.

### Pattern Recognition Cue
Whenever a problem says "valid parentheses" and asks about grouping or nesting, think **depth counter**. If you need matching pairs, upgrade to a stack.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Stack-Based Primitive Extraction
**Intuition:** Use a stack to find each primitive substring, then slice off the first and last character of each.

**Steps:**
1. Walk through `s`. Push indices of `(` onto a stack.
2. On `)`, pop from the stack. If the stack is now empty, we found a complete primitive from the last start to the current index.
3. For each primitive substring, take `substring(1, len-1)` and append to result.

**BUD Transition:** We are storing indices on a stack and creating substrings -- unnecessary allocations. We can stream characters directly.

**Dry Run:** `s = "(()())(())"`
```
i=0 '(' stack=[0]
i=1 '(' stack=[0,1]
i=2 ')' stack=[0]       (not empty, continue)
i=3 '(' stack=[0,3]
i=4 ')' stack=[0]       (not empty, continue)
i=5 ')' stack=[]        -> primitive s[0..5] = "(()())" -> strip -> "()()"
i=6 '(' stack=[6]
i=7 '(' stack=[6,7]
i=8 ')' stack=[6]       (not empty, continue)
i=9 ')' stack=[]        -> primitive s[6..9] = "(())"   -> strip -> "()"
Result = "()()" + "()" = "()()()"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) -- stack + substrings |

---

### Approach 2: Optimal -- Depth Counter with Conditional Append
**Intuition:** Track depth with an integer. Skip characters at depth transitions 0->1 and 1->0 (those are the outermost parens). Append everything else.

**Steps:**
1. Initialize `depth = 0`, `result = StringBuilder`.
2. For each char `c`:
   - If `c == '('`: increment depth. If depth > 1, append `c`.
   - If `c == ')'`: if depth > 1, append `c`. Decrement depth.
3. Return result.

**BUD Transition from Brute:** Eliminated the stack entirely. No substring slicing. Single pass, constant auxiliary state.

**Dry Run:** `s = "(()())(())"`
```
i=0 '(' depth 0->1  (skip, outermost open)
i=1 '(' depth 1->2  append '('   result="("
i=2 ')' depth=2>1   append ')'   result="()" then depth->1
i=3 '(' depth 1->2  append '('   result="()("
i=4 ')' depth=2>1   append ')'   result="()()" then depth->1
i=5 ')' depth=1->0  (skip, outermost close)
i=6 '(' depth 0->1  (skip)
i=7 '(' depth 1->2  append '('   result="()()("
i=8 ')' depth=2>1   append ')'   result="()()()" then depth->1
i=9 ')' depth=1->0  (skip)
Final: "()()()"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1) extra (excluding output string) |

---

### Approach 3: Best -- Same as Optimal (Proven Lower Bound)
**Intuition:** We must read every character at least once (O(n) is mandatory). The counter approach uses O(1) extra space. This is provably optimal.

The approach is identical to Approach 2. No further optimization is possible since we already achieve the information-theoretic lower bound.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1) extra |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** We look at each character exactly once, doing O(1) work (compare, increment/decrement, maybe append).
- **Space O(1):** We only keep a single integer `depth`. The output string is required by the problem and does not count as extra space.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `"()"` | `""` | Single primitive with nothing inside |
| `"(())"` | `"()"` | One primitive, one layer of nesting inside |
| `"()()()"` | `""` | Three primitives, each trivial |
| Max length 10^5 all `()` | 50000 `()` pairs, result is `""` | Must handle large input efficiently |

**Common Mistakes:**
- Forgetting to check depth *before* vs *after* incrementing for `(` -- the order of increment vs check matters.
- Using a stack when a counter suffices, wasting O(n) space.
- Off-by-one: including the outermost parens instead of excluding them.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** Restate: "We decompose into primitives and strip each outer layer."
2. **Match:** "This is a parentheses depth-tracking problem -- a counter is the right tool."
3. **Plan:** Describe the depth-counter approach. Mention the brute-force stack alternative first to show you considered options.
4. **Implement:** Write the single-pass counter solution.
5. **Review:** Trace through the first example on the whiteboard.
6. **Evaluate:** State O(n) time, O(1) space.

### Follow-Up Questions
- *"What if the string might be invalid?"* -- Add validation: if depth ever goes negative or is nonzero at the end, return an error.
- *"Can you do this without StringBuilder?"* -- Use a char array of size n and a write pointer, then convert at the end.
- *"What if we need to return the primitives themselves?"* -- Collect start/end indices when depth transitions through 0.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Valid Parentheses (LC #20) -- basic stack matching |
| **Same Pattern** | Max Nesting Depth of Parentheses (LC #1614) -- same depth counter |
| **Harder** | Minimum Remove to Make Valid Parentheses (LC #1249) |
| **Unlocks** | Score of Parentheses (LC #856) -- depth-based scoring |
