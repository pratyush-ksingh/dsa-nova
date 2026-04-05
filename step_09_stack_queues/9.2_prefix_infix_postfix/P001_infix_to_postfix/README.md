# Infix to Postfix

> **Batch 3 of 12** | **Topic:** Stack | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND

### Problem Statement
Given a valid infix expression (operators between operands, e.g., `a+b*c`), convert it to **postfix** (Reverse Polish Notation), where operators follow their operands (e.g., `abc*+`). Handle operator **precedence** (`*` `/` before `+` `-`) and **associativity** (left-to-right for all four basic operators). Parentheses override precedence.

**Example:**
```
Input:  "a+b*c"
Output: "abc*+"     (* has higher precedence, so b*c is grouped first)

Input:  "(a+b)*c"
Output: "ab+c*"     (parentheses force a+b first)

Input:  "a+b+c"
Output: "ab+c+"     (left-to-right associativity)

Input:  "a*(b+c*d)+e"
Output: "abcd*+*e+"
```

| Input | Output | Explanation |
|-------|--------|-------------|
| a+b | ab+ | Simple: operand operand operator |
| a+b*c | abc*+ | * binds tighter, so b c * first, then a + |
| (a+b)*c | ab+c* | Parens force a+b first |
| a+b+c | ab+c+ | Left-associative: (a+b)+c |
| a*(b+c*d)+e | abcd*+*e+ | Nested precedence and parentheses |
| ((a+b)) | ab+ | Redundant parens do not affect output |
| a^b^c | abc^^ | Right-associative exponent (if supported) |

### Real-Life Analogy
Think of a **traffic controller** at a roundabout (the stack). Cars (operands) go directly to the highway (output). But delivery trucks (operators) must wait at the roundabout. A higher-priority truck (like `*`) can enter freely. But when a lower-priority truck (like `+`) arrives, all equal-or-higher priority trucks already waiting must leave to the highway first. Parentheses are like closing a lane -- when `)` arrives, all trucks in the roundabout leave until the `(` marker is cleared. This is the Shunting Yard algorithm.

### Key Observations
1. **Observation:** Operands always go directly to the output -- their relative order is preserved. Only operator placement changes.
2. **Observation:** A stack holds operators "in limbo." Higher-precedence or equal-precedence operators on the stack must be flushed to output before a new lower/equal-precedence operator is pushed. This enforces correct precedence.
3. **Aha moment:** Parentheses reset the precedence context. `(` acts as a "barrier" on the stack -- nothing is popped past it until `)` arrives, which pops everything down to `(`. This isolates sub-expressions.

### Constraints
- Valid expression (balanced parentheses, correct syntax)
- Operands: single lowercase letters or digits
- Operators: `+`, `-`, `*`, `/`, optionally `^`
- Left-to-right associativity for `+`, `-`, `*`, `/`; right-to-left for `^`

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Stack (Shunting Yard)?
Dijkstra's Shunting Yard algorithm uses a stack to defer operators until their correct position in the output is determined. The stack's LIFO property ensures that operators are applied in the correct order: higher-precedence operators that were deferred get output before lower-precedence ones.

### Pattern Recognition
**Classification cue:** "Convert expression notation" or "Handle operator precedence" --> Shunting Yard algorithm with a stack. This is the standard algorithm used by compilers and calculators to parse infix expressions. The same stack-based approach extends to expression evaluation, infix-to-prefix, and building expression trees.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Fully Parenthesize Then Convert
**Intuition:** First, add explicit parentheses based on precedence so that every operator has surrounding parens: `a+b*c` becomes `(a+(b*c))`. Then, for each pair of parentheses, move the operator after the closing paren: `(a+(bc*))` then `(abc*+)` then `abc*+`. This is conceptually clear but extremely hard to implement correctly.

**Steps:**
1. Parse the expression and insert full parentheses based on precedence rules.
2. For each innermost parenthesized group `(X op Y)`, rewrite as `XY op`.
3. Repeat until no parentheses remain.

| Metric | Value |
|--------|-------|
| Time | O(n^2) -- repeated string manipulation |
| Space | O(n) -- intermediate strings |

**BUD Transition:** Manual parenthesization and repeated rewriting is fragile and slow. The Shunting Yard algorithm handles everything in a single pass.

---

### Approach 2: Optimal -- Shunting Yard Algorithm
**Intuition:** Process tokens left-to-right. Operands go to output immediately. Operators are pushed to a stack, but first, pop any operators on the stack that have higher or equal precedence (respecting associativity) to ensure correct ordering. Parentheses manage sub-expression scope.

**Steps:**
1. Define precedence: `+` `-` = 1, `*` `/` = 2, `^` = 3.
2. Create empty stack and output list.
3. For each token:
   - **Operand:** append to output.
   - **`(`:** push to stack.
   - **`)`:** pop from stack to output until `(` is found. Discard `(`.
   - **Operator `op`:** while stack is not empty AND top is not `(` AND (top has higher precedence, OR top has equal precedence and `op` is left-associative): pop to output. Then push `op`.
4. Pop all remaining operators from stack to output.
5. Join output as the postfix string.

**Dry Run Trace ("a+b*c"):**

| Token | Action | Stack | Output |
|-------|--------|-------|--------|
| a | Operand --> output | [] | a |
| + | Stack empty, push + | [+] | a |
| b | Operand --> output | [+] | ab |
| * | Top is + (prec 1), * is prec 2. 2 > 1, so do NOT pop. Push * | [+, *] | ab |
| c | Operand --> output | [+, *] | abc |
| END | Pop all: *, + | [] | abc*+ |

Result: `abc*+`

**Dry Run Trace ("(a+b)*c"):**

| Token | Action | Stack | Output |
|-------|--------|-------|--------|
| ( | Push ( | [(] | |
| a | Output | [(] | a |
| + | Push + (( is barrier) | [(, +] | a |
| b | Output | [(, +] | ab |
| ) | Pop until (: pop +. Discard ( | [] | ab+ |
| * | Stack empty, push * | [*] | ab+ |
| c | Output | [*] | ab+c |
| END | Pop *  | [] | ab+c* |

Result: `ab+c*`

**Dry Run Trace ("a*(b+c*d)+e"):**

| Token | Action | Stack | Output |
|-------|--------|-------|--------|
| a | Output | [] | a |
| * | Push * | [*] | a |
| ( | Push ( | [*, (] | a |
| b | Output | [*, (] | ab |
| + | Push + (barrier at () | [*, (, +] | ab |
| c | Output | [*, (, +] | abc |
| * | Top is + (prec 1), * is prec 2. Don't pop. Push * | [*, (, +, *] | abc |
| d | Output | [*, (, +, *] | abcd |
| ) | Pop until (: pop *, pop +. Discard ( | [*] | abcd*+ |
| + | Top is * (prec 2), + is prec 1. Pop *. Stack empty, push + | [+] | abcd*+* |
| e | Output | [+] | abcd*+*e |
| END | Pop + | [] | abcd*+*e+ |

Result: `abcd*+*e+`

| Metric | Value |
|--------|-------|
| Time | O(n) -- each token is pushed/popped at most once |
| Space | O(n) -- stack and output |

---

### Approach 3: Best -- Shunting Yard with Full Operator Support (Production-Ready)
**Intuition:** The same Shunting Yard algorithm, extended to handle right-associative operators (like `^`), unary operators, and multi-character operands/numbers. Uses a clean configuration-driven precedence table.

**Steps:**
1. Define precedence map and associativity map:
   - `{'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}`
   - `{'^': 'R', others: 'L'}`
2. For equal precedence: pop if left-associative, do NOT pop if right-associative.
3. Everything else is the same as Approach 2.

**Right-associativity example: "a^b^c"**
- `^` is right-associative. When second `^` arrives and top is `^` (equal precedence), do NOT pop because right-associative. Result: `abc^^` (meaning `a^(b^c)`).

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) time:** Each character/token is processed exactly once during the left-to-right scan. Each operator is pushed onto the stack at most once and popped at most once. So total operations across all tokens is at most 2n.

**Why O(n) space:** The output holds all operands (up to n). The stack holds operators and parentheses (bounded by n). Total space is O(n).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Not popping equal-precedence operators | Treats equal precedence like lower | For left-associative ops, pop when `stackTop >= current` |
| Popping past `(` | Forgetting that `(` is a barrier | Always check `stack top != '('` before popping |
| Including `(` in output | Parentheses should be discarded, not output | When `)` triggers popping, discard the matched `(` |
| Wrong precedence for `^` | Treating it like `*` or `/` | Give `^` its own higher precedence level |
| Right-associative error | Popping `^` when another `^` arrives | For right-associative: only pop when `stackTop > current`, not `>=` |

### Edge Cases Checklist
- Single operand: "a" --> "a"
- No operators: "a" --> "a"
- All same precedence: "a+b-c+d" --> "ab+c-d+"
- Deeply nested parentheses: "((a+b))" --> "ab+"
- Right-associative operator: "a^b^c" --> "abc^^"
- Mixed precedence: "a+b*c-d/e" --> "abc*+de/-"

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Are operands single characters? Which operators? Is `^` included? Associativity rules?"
2. **M**atch: "Infix to postfix conversion --> Dijkstra's Shunting Yard algorithm."
3. **P**lan: "Operands to output. Operators on stack with precedence-based popping. Parens as barriers."
4. **I**mplement: Write precedence function, then the main loop.
5. **R**eview: Trace with "a+b*c" and "(a+b)*c".
6. **E**valuate: "O(n) time, O(n) space."

### Follow-Up Questions
- "Now evaluate the postfix expression." --> Use a stack: push operands, pop two on operator, compute, push result.
- "Convert to prefix instead." --> Reverse the string, swap ( and ), run infix-to-postfix, reverse the result.
- "Build an expression tree." --> Same Shunting Yard logic but create tree nodes instead of outputting to string.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Valid Parentheses (stack bracket matching), operator precedence rules |
| **Same pattern** | Infix to Prefix, Postfix Evaluation, Basic Calculator (#224, #227) |
| **Next step** | Postfix to Infix, Prefix to Infix conversions |
| **Harder variant** | Basic Calculator II (LeetCode #227 -- evaluate infix directly) |
| **Unlocks** | Expression evaluation, compiler design, calculator applications |
