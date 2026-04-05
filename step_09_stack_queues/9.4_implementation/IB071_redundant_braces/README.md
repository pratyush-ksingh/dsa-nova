# Redundant Braces

> **Batch 3 of 12** | **Topic:** Stack | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a string expression containing only `(`, `)`, lowercase letters, and operators `+`, `-`, `*`, `/`, determine if the expression contains **redundant braces**. A pair of braces is redundant if it surrounds no operator -- i.e., the braces can be removed without changing the expression's meaning.

**InterviewBit: Redundant Braces**

**Example:**
```
Input: "((a+b))"
Output: true    (outer braces are redundant -- they wrap (a+b) which already has braces)

Input: "(a+b)"
Output: false   (braces around a+b contain an operator, not redundant)

Input: "(a)"
Output: true    (braces around a single variable -- no operator inside)

Input: "a+(b*c)"
Output: false   (braces around b*c contain *, not redundant)
```

| Input | Output | Explanation |
|-------|--------|-------------|
| ((a+b)) | true | Outer () wraps a sub-expression already in (), no new operator at that level |
| (a+b) | false | The braces contain operator + |
| (a) | true | No operator between the braces |
| a+(b*c) | false | Braces around b*c contain * |
| ((a)) | true | Both layers of braces are redundant |
| a+b | false | No braces at all |

### Real-Life Analogy
Think of **gift wrapping**. If a present is already wrapped in a box, putting another box around it is redundant -- it adds nothing. The "operator" is the actual gift inside. If you open a box and find another box with no gift between them, the outer box was pointless. Similarly, braces that do not contain any operator between matching parentheses are redundant.

### Key Observations
1. **Observation:** Between every pair of matching parentheses, there must be at least one operator (`+`, `-`, `*`, `/`). If there is no operator, the braces are redundant.
2. **Observation:** When we encounter `)`, we need to know what was between it and its matching `(`. A stack lets us "peek back" to the matching `(`.
3. **Aha moment:** Push everything onto the stack. When we see `)`, pop until we hit `(`. If we popped nothing (or only non-operator characters like letters), the braces were redundant. If we popped at least one operator, they were necessary.

### Constraints
- Expression is valid (balanced parentheses, correct syntax)
- Contains only lowercase letters, `+`, `-`, `*`, `/`, `(`, `)`
- At least one character in the expression

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Stack?
We need to track what lies between each pair of matching parentheses. A stack naturally accumulates characters until a `)` triggers a pop-back-to-`(` scan. This mirrors how we mentally evaluate nesting.

### Pattern Recognition
**Classification cue:** "Check what is inside matching brackets" --> Stack-based bracket processing. This is a variation of the valid parentheses pattern where we also inspect content between bracket pairs.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Repeated Simplification
**Intuition:** Find the innermost pair of parentheses (no nested parens inside). Check if removing them changes the expression's semantics (i.e., check if they contain an operator). If they contain no operator, return true (redundant found). Otherwise, remove that pair and repeat.

**Steps:**
1. Find the innermost `(...)` with no nested parens inside.
2. Extract the content between them.
3. If the content contains no operator, return true.
4. Remove the parentheses pair and repeat from step 1.
5. If no redundant braces found after all pairs are processed, return false.

**Dry Run Trace ("((a+b))"):**

| Pass | Expression | Innermost | Content | Has operator? |
|------|-----------|-----------|---------|--------------|
| 1 | ((a+b)) | (a+b) | a+b | Yes (+) -- not redundant, remove parens |
| 2 | (a+b) | (a+b) | a+b | Yes (+) -- wait, this was the OUTER pair originally wrapping (a+b). After removing inner, the outer now directly wraps a+b with operator. |

This approach gets complex with tracking. The stack approach is much cleaner.

| Metric | Value |
|--------|-------|
| Time | O(n^2) -- repeated string scanning |
| Space | O(n) -- string manipulation |

**BUD Transition:** Repeated scanning is wasteful and tricky. A single-pass stack approach handles this cleanly.

---

### Approach 2: Optimal -- Stack Scan
**Intuition:** Push every character except `)` onto the stack. When we encounter `)`, pop characters until we reach `(`. If between `(` and `)` we found at least one operator, the braces are justified. If we found zero operators (just letters or nothing), the braces are redundant.

**Steps:**
1. Create an empty stack.
2. For each character `c` in the expression:
   - If `c` is `)`:
     - Set `hasOperator = false`.
     - Pop from stack until `(` is found:
       - If popped character is an operator (`+`, `-`, `*`, `/`), set `hasOperator = true`.
     - Pop the `(` itself.
     - If `hasOperator` is false, return true (redundant braces found).
   - Else: push `c` onto the stack.
3. Return false (no redundant braces).

**Dry Run Trace ("((a+b))"):**

| Step | Char | Stack | Action |
|------|------|-------|--------|
| 1 | ( | [(] | Push |
| 2 | ( | [(, (] | Push |
| 3 | a | [(, (, a] | Push |
| 4 | + | [(, (, a, +] | Push |
| 5 | b | [(, (, a, +, b] | Push |
| 6 | ) | Pop until (: b, +. Found operator +. hasOp=true. Pop (. Stack: [(] | Not redundant |
| 7 | ) | Pop until (: nothing popped before hitting (. hasOp=false. Pop (. Stack: [] | REDUNDANT! Return true |

Return true.

**Dry Run Trace ("(a+b)"):**

| Step | Char | Stack | Action |
|------|------|-------|--------|
| 1 | ( | [(] | Push |
| 2 | a | [(, a] | Push |
| 3 | + | [(, a, +] | Push |
| 4 | b | [(, a, +, b] | Push |
| 5 | ) | Pop until (: b, +. Found operator +. hasOp=true. Pop (. Stack: [] | Not redundant |

Return false.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each character is pushed and popped at most once |
| Space | O(n) -- stack stores characters |

---

### Approach 3: Best -- Stack with Operator Set (Clean Production Code)
**Intuition:** Same algorithm with a clean operator check using a set. Also handles the edge case where there are no braces at all (trivially no redundant braces).

**Steps:**
1. Define `operators = {'+', '-', '*', '/'}`.
2. Same logic as Approach 2, but use `operators.contains(popped)` for clarity.
3. Count elements popped between `(` and `)`. If count == 0, braces are empty `()` -- also redundant.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) time:** Each character enters the stack at most once and leaves at most once. Even though we pop multiple elements on `)`, the total number of pops across all `)` characters cannot exceed n.

**Why O(n) space:** In the worst case (deeply nested expression), the stack holds nearly all characters.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting that empty parens () are redundant | Only checking for operators, not for empty content | Also check if nothing was popped before hitting ( |
| Treating letters as operators | Both are non-bracket characters | Explicitly check for +, -, *, / only |
| Returning false too early | Finding one non-redundant pair does not mean none are redundant | Only return true early (on first redundancy). Return false at the very end |
| Not handling expressions without any braces | No parens means no redundancy possible | The loop naturally handles this (never encounters `)`) |

### Edge Cases Checklist
- No braces: "a+b" --> false
- Empty braces: "()" --> true (nothing inside, redundant)
- Single variable in braces: "(a)" --> true
- Nested operators: "((a+b)*(c+d))" --> false
- Double-wrapped: "((a+b))" --> true

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is the expression always valid? What characters can appear? Can there be spaces?"
2. **M**atch: "Checking content between brackets --> stack-based bracket processing."
3. **P**lan: "Push everything. On ), pop until (. Check if any operator was found."
4. **I**mplement: Write the stack approach with operator set.
5. **R**eview: Trace with "((a+b))" and "(a+b)".
6. **E**valuate: "O(n) time, O(n) space."

### Follow-Up Questions
- "Remove all redundant braces from the expression." --> Track positions, rebuild string without redundant pairs.
- "What about unary minus like `(-a)`?" --> Depends on definition. If `-` counts as operator, `(-a)` is not redundant.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Valid Parentheses (basic stack bracket matching) |
| **Same pattern** | Valid Parentheses, Min Remove to Make Valid |
| **Next step** | Expression evaluation (infix to postfix) |
| **Harder variant** | Remove redundant braces from expression (output the cleaned string) |
| **Unlocks** | Expression parsing and simplification problems |
