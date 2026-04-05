# Postfix to Prefix Conversion

> **Step 09.9.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a **postfix** (Reverse Polish Notation) expression, convert it to its equivalent **prefix** (Polish Notation) expression. Operands are single alphanumeric characters. Operators are `+`, `-`, `*`, `/`, `^`.

- **Postfix:** Operator comes **after** its operands. E.g., `AB+` means `A+B`.
- **Prefix:** Operator comes **before** its operands. E.g., `+AB` means `A+B`.

### Examples

| # | Postfix | Prefix | Explanation |
|---|---------|--------|-------------|
| 1 | `AB+` | `+AB` | Simple: A+B |
| 2 | `AB+C*` | `*+ABC` | (A+B)*C |
| 3 | `AB+CD-*` | `*+AB-CD` | (A+B)*(C-D) |
| 4 | `ABC*+` | `+A*BC` | A+(B*C) |
| 5 | `A` | `A` | Single operand |

### Constraints
- Expression contains only single-character operands (A-Z, 0-9)
- Operators: `+`, `-`, `*`, `/`, `^`
- Expression is valid and well-formed

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Build expression tree, preorder traverse | Stack + Binary Tree |
| Optimal | Stack of strings: on operator, form `op+op1+op2` | Stack |
| Best | Same stack with StringBuilder for efficiency | Stack + StringBuilder |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Expression Tree + Preorder Traversal

**Intuition:** Every expression can be represented as a binary tree where internal nodes are operators and leaves are operands. Postfix order = postorder traversal (left-right-root). Prefix order = preorder traversal (root-left-right). So: build the tree from postfix, then traverse it preorder.

**Steps:**
1. Create a stack of tree nodes.
2. Scan postfix left to right:
   - If operand: create leaf node, push it.
   - If operator: pop two nodes (right first, then left), create internal node with operator, push.
3. The stack now holds the root of the expression tree.
4. Do a preorder traversal: print root, then recursively left, then right.

**Dry-Run Trace** (`AB+CD-*`):
```
A -> push Node(A)
B -> push Node(B)
+ -> pop B (right), pop A (left) -> push Node(+, A, B)
C -> push Node(C)
D -> push Node(D)
- -> pop D (right), pop C (left) -> push Node(-, C, D)
* -> pop Node(-,C,D) (right), pop Node(+,A,B) (left) -> push Node(*,+,-))

Preorder: * -> +AB -> -CD
Result: "*+AB-CD"
```

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for tree nodes + stack |

---

### Approach 2: Optimal -- Stack of Strings

**Intuition:** We don't actually need to build a tree. A stack of strings works directly. When we see an operator, we combine the two top strings into a new prefix sub-expression and push it back. The key insight: for prefix, the operator goes **first**.

**Steps:**
1. Initialize an empty stack of strings.
2. Scan postfix left to right:
   - If operand: push `str(ch)`.
   - If operator: pop `op2`, pop `op1`, push `operator + op1 + op2`.
3. Return `stack.top()`.

**Dry-Run Trace** (`AB+CD-*`):
```
A -> stack: ["A"]
B -> stack: ["A","B"]
+ -> op2="B", op1="A" -> push "+AB" -> stack: ["+AB"]
C -> stack: ["+AB","C"]
D -> stack: ["+AB","C","D"]
- -> op2="D", op1="C" -> push "-CD" -> stack: ["+AB","-CD"]
* -> op2="-CD", op1="+AB" -> push "*+AB-CD" -> stack: ["*+AB-CD"]
Result: "*+AB-CD"
```

**Note the order:** `op2 = stack.pop()` first, then `op1 = stack.pop()`. The string is `operator + op1 + op2` (not op2+op1).

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Stack with StringBuilder

**Intuition:** Identical to Approach 2. In languages like Java, string concatenation inside loops can create many intermediate strings. Using `StringBuilder` makes the concatenation O(n) total instead of O(n^2) in the worst case with naive concatenation.

**Steps:** Same as Approach 2, but concatenate using `StringBuilder.append()`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n):** We scan each character exactly once. Each operand/operator is pushed and popped from the stack exactly once. The total work is proportional to the length of the expression.

**Space O(n):** In the worst case (e.g., all operands then one operator), the stack holds O(n) strings.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Single operand `A` | `A` | Stack has one item, return it directly |
| Two operands `AB+` | `+AB` | Simplest two-operand case |
| Pop order | Critical | Pop `op2` first, then `op1`; string is `op + op1 + op2` |
| Right-assoc `^` | Handled naturally | Stack approach doesn't need to know associativity |

**Common Mistakes:**
- **Swapping op1 and op2:** Popping `op1` first gives wrong order. Always pop `op2` first from the stack (it was pushed last = right operand in postfix).
- Building `op1 + op + op2` instead of `op + op1 + op2` (that's infix, not prefix).
- Not handling single-character operands that are operators (e.g., using `isalpha` only -- remember digits are operands too).

---

## 6. REAL-WORLD USE CASE

**Compiler Code Generation:** Compilers often generate intermediate representations in postfix form (easier to evaluate with a stack). When translating to a different target architecture that uses prefix calling conventions, postfix-to-prefix conversion is needed.

**Spreadsheet Engines:** Formula parsers internally use postfix evaluation (e.g., Excel formula engine). Converting to prefix helps in formula serialization, display, and debug output in some systems.

---

## 7. INTERVIEW TIPS

- **Draw the expression tree** on the whiteboard -- it makes the transformation immediately obvious.
- The **stack approach** (Approach 2) is the cleanest to code. Know it by heart.
- **Remember the pop order:** in postfix `AB+`, B was pushed last so it's the right operand. Pop order: right (op2) first, then left (op1).
- **Compare all three notations** -- good talking point: Infix needs parentheses, Prefix/Postfix don't.
- **Follow-up:** "What's the time complexity of evaluating the prefix expression?" -> O(n) with a stack, same approach but push values instead of strings.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Postfix to Infix | Similar stack approach, but wrap with parentheses |
| Prefix to Postfix | Scan prefix right-to-left with a stack |
| Prefix to Infix | Scan right-to-left, wrap sub-expressions in parentheses |
| Evaluate Postfix | Same stack scan, push values not strings |
| Expression Tree | The brute force approach builds one explicitly |
