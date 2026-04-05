# Infix to Prefix

> **Batch 4 of 12** | **Topic:** Prefix / Infix / Postfix Conversion | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a fully parenthesized or standard **infix** expression (e.g., `A+B*C`), convert it to its equivalent **prefix** (Polish notation) form (e.g., `+A*BC`). Operators are `+`, `-`, `*`, `/`, `^`. Operands are single characters (A-Z or 0-9). Respect standard operator precedence and associativity.

**Prefix notation:** The operator comes **before** its operands. No parentheses are needed because the order of operations is unambiguous.

### Examples

| # | Infix | Prefix | Explanation |
|---|-------|--------|-------------|
| 1 | `A+B` | `+AB` | Simple binary operation |
| 2 | `A+B*C` | `+A*BC` | `*` binds tighter: prefix of `B*C` is `*BC`, then `+A(*BC)` |
| 3 | `(A+B)*C` | `*+ABC` | Parens force `+` first: `+AB`, then `*` with C |
| 4 | `A+B*C-D` | `-+A*BCD` | `B*C` first, `A+(B*C)`, then `- ... D` |
| 5 | `A^B^C` | `^A^BC` | `^` is right-associative: `A^(B^C)` -> `^A^BC` |
| 6 | `(A+B)*(C-D)` | `*+AB-CD` | Two sub-expressions combined |

### Real-Life Analogy
Think of writing a **recipe** in two styles. Infix is like saying "take flour AND mix with eggs THEN bake at 350." Prefix is like giving the instruction first: "MIX flour eggs" or "BAKE 350 (MIX flour eggs)." The operator (action) comes first, followed by what it acts on. Compilers prefer prefix/postfix because they eliminate ambiguity without parentheses.

### Three Key Observations (the "Aha!" Moments)
1. **Prefix is the reverse of postfix on the reversed string** -- The classic trick: reverse the infix string, swap `(` with `)`, run the standard infix-to-postfix algorithm, then reverse the result. This gives prefix.
2. **Operator precedence and associativity matter** -- `^` is right-associative, all others are left-associative. This affects when operators are pushed/popped from the stack.
3. **The stack holds pending operators** -- Just like infix-to-postfix, but with the reversal trick, the comparison direction for same-precedence operators flips for left-associative operators.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Fully parenthesize, then recursively extract prefix | Recursion / string manipulation |
| Optimal | Reverse + infix-to-postfix + reverse | Stack + string builder |
| Best | Direct right-to-left scan (no explicit reverse) | Stack + string builder |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Full Parenthesization + Recursive Extraction

**Intuition:** First, fully parenthesize the expression respecting precedence. Then, recursively convert: for each `(A op B)`, produce `op [prefix(A)] [prefix(B)]`.

**BUD Analysis:**
- **B**ottleneck: Parenthesization requires parsing with precedence, which is essentially the same work as the optimal approach.
- **U**nnecessary work: Building a fully parenthesized string creates extra allocations.
- **D**uplicate work: Scanning the string multiple times for matching parentheses.

**Steps:**
1. Parse and fully parenthesize the infix expression.
2. Recursively process: find the main operator of each sub-expression, extract left and right operands, produce `operator + prefix(left) + prefix(right)`.

**Dry-Run Trace** (`A+B*C`):
```
Fully parenthesized: (A+(B*C))
Main operator of (A+(B*C)): +
Left = A (base case: "A")
Right = (B*C), main operator: *, left=B, right=C -> "*BC"
Result: "+A*BC"
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) in naive implementation (repeated string scanning) |
| Space | O(n) for recursion + string copies |

---

### Approach 2: Optimal -- Reverse + Infix-to-Postfix + Reverse

**Intuition:** The well-known trick: to convert infix to prefix, (1) reverse the infix string, (2) swap every `(` with `)` and vice versa, (3) apply the standard infix-to-postfix (Shunting Yard) algorithm with one adjustment for associativity, (4) reverse the resulting postfix string. The result is the prefix expression.

**Why this works:** Reversing the string makes us process the expression from right to left. In this mirrored world, infix-to-postfix produces "reverse prefix" which, when reversed, gives the correct prefix.

**Associativity adjustment:** For left-associative operators (`+`, `-`, `*`, `/`), when we encounter an operator with the **same** precedence on the stack, we do **not** pop it (use `<` instead of `<=`). For right-associative `^`, we **do** pop same precedence (use `<=`). This is the opposite of standard infix-to-postfix.

**Steps:**
1. Reverse the infix string.
2. Swap `(` <-> `)` in the reversed string.
3. Apply Shunting Yard (infix-to-postfix) with modified associativity rules.
4. Reverse the output to get prefix.

**Dry-Run Trace** (`A+B*C`):
```
Step 1: Reverse -> "C*B+A"
Step 2: No parens to swap -> "C*B+A"
Step 3: Shunting Yard on "C*B+A":
  C -> output: "C"
  * -> stack: [*]
  B -> output: "CB"
  + -> prec(+) < prec(*), pop *. output: "CB*". push +. stack: [+]
  A -> output: "CB*A"
  End: pop +. output: "CB*A+"
Step 4: Reverse -> "+A*BC"
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) for stack and output |

---

### Approach 3: Best -- Direct Right-to-Left Scan

**Intuition:** Instead of explicitly reversing the string, scan the infix expression from **right to left** and build the prefix output directly. This is conceptually the same as Approach 2 but avoids two string reversals. When scanning right-to-left:
- Operands are prepended to the output.
- `)` is treated as an opening bracket (pushed to stack).
- `(` is treated as a closing bracket (pop until `)`).
- Operators are handled with same precedence logic as Approach 2.

**Steps:**
1. Scan infix from `i = n-1` down to `0`.
2. If operand: prepend to result.
3. If `)`: push to stack.
4. If `(`: pop and prepend until `)` is found.
5. If operator: pop and prepend operators with strictly higher precedence (for left-assoc) or higher-or-equal precedence (for right-assoc `^`), then push current operator.
6. After scan, pop remaining operators.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n):** Each character is processed exactly once. Each operator is pushed to the stack once and popped once. The total work across all push/pop operations is O(n).

**Space O(n):** The output string and the stack each hold at most n characters.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Single operand `A` | `A` | No operators, output is just the operand |
| All same precedence `A+B-C` | `-+ABC` | Left-associativity: (A+B)-C |
| Right-associative `A^B^C` | `^A^BC` | Must not pop same-precedence `^` in reversed scan |
| Nested parens `((A+B))` | `+AB` | Extra parens should not cause errors |
| Mixed precedence `A+B*C/D` | `+A/*BCD` | `*` and `/` bind before `+` |

**Common Mistakes:**
- Forgetting to swap parentheses after reversing (produces wrong grouping).
- Using the same associativity rules as infix-to-postfix (must flip for same-precedence comparison in the reversed scan).
- Not handling `^` right-associativity correctly -- it behaves differently from `+`, `-`, `*`, `/`.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** Tests deep understanding of operator precedence, associativity, stack-based parsing, and the relationship between prefix/infix/postfix notations.

**Follow-ups to expect:**
- "Now convert prefix to infix." -> Scan right-to-left; when you see an operator, pop two operands and form `(op1 operator op2)`.
- "Evaluate the prefix expression directly." -> Scan right-to-left with a stack of values.
- "What data structure does a compiler use for this?" -> Abstract Syntax Tree (AST), which is naturally a prefix tree.

**Talking points:**
- Explain why compilers prefer postfix/prefix: no ambiguity, no parentheses needed, easy to evaluate with a stack.
- The Shunting Yard algorithm was invented by Dijkstra -- mention this for bonus points.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Infix to Postfix | The foundation algorithm; prefix is derived from it |
| Postfix to Prefix | Direct conversion using a stack of strings |
| Prefix Evaluation | Natural follow-up: evaluate the prefix expression |
| Expression Tree Construction | Build a tree from prefix/postfix, infix is the in-order traversal |
| Basic Calculator (LC #224) | Same precedence/parentheses parsing in a different guise |
