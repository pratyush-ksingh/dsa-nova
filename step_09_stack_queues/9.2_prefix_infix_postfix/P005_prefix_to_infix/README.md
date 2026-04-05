# Prefix to Infix Conversion

> **Step 09.9.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a **prefix** (Polish Notation) expression, convert it to its equivalent **fully parenthesized infix** expression. Operands are single alphanumeric characters. Operators are `+`, `-`, `*`, `/`, `^`.

- **Prefix:** Operator **before** operands. E.g., `+AB` means `A+B`.
- **Infix:** Operator **between** operands, with parentheses for clarity. E.g., `(A+B)`.

### Examples

| # | Prefix | Infix | Explanation |
|---|--------|-------|-------------|
| 1 | `+AB` | `(A+B)` | Simple binary operation |
| 2 | `*+AB-CD` | `((A+B)*(C-D))` | Composed expression |
| 3 | `+A*BC` | `(A+(B*C))` | Operator precedence preserved by parens |
| 4 | `*+ABC` | `((A+B)*C)` | Parentheses explicitly show grouping |
| 5 | `A` | `A` | Single operand, no change |

### Constraints
- Expression contains only single-character operands (A-Z, 0-9)
- Operators: `+`, `-`, `*`, `/`, `^`
- Expression is valid and well-formed

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Build expression tree (recursive descent on prefix), inorder traverse | Stack + Binary Tree |
| Optimal | Scan right-to-left, stack of strings; on operator wrap in parens | Stack |
| Best | Same right-to-left stack with StringBuilder for efficiency | Stack + StringBuilder |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Expression Tree + Inorder Traversal

**Intuition:** Prefix notation IS a preorder traversal of the expression tree. We can reconstruct the tree by reading the prefix string left to right using recursive descent: an operator node reads its left then right sub-tree. Then inorder traversal (left-root-right) with parentheses gives us infix.

**Steps:**
1. Use a position pointer `pos = 0` scanning the prefix string left to right.
2. `build()`: read `prefix[pos++]`. If operand, return leaf node. If operator, recursively build left subtree, then right subtree, return node.
3. `inorder(node)`: if leaf, return `node.val`. Else return `"(" + inorder(left) + node.val + inorder(right) + ")"`.

**Dry-Run Trace** (`*+AB-CD`):
```
pos=0: '*' is operator
  left: pos=1: '+' is operator
    left: pos=2: 'A' -> leaf A
    right: pos=3: 'B' -> leaf B
    -> Node(+, A, B)
  right: pos=4: '-' is operator
    left: pos=5: 'C' -> leaf C
    right: pos=6: 'D' -> leaf D
    -> Node(-, C, D)
  -> Node(*, Node(+,A,B), Node(-,C,D))

Inorder: ((A+B)*(C-D))
```

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for tree nodes + recursion stack |

---

### Approach 2: Optimal -- Stack-Based Right-to-Left Scan

**Intuition:** The key insight: prefix is read left-to-right, but when using a stack for conversion, we need to process it **right-to-left**. Why? Because operands appear after operators in prefix notation. By scanning right-to-left, we encounter operands first and push them. When we hit an operator, its two operands are already on the stack.

**Steps:**
1. Initialize an empty stack of strings.
2. Scan prefix from **right to left** (i = len-1 down to 0):
   - If operand: push as a string.
   - If operator: pop `op1`, pop `op2`, push `"(" + op1 + operator + op2 + ")"`.
3. Return `stack.top()`.

**Dry-Run Trace** (`*+AB-CD`), scanning right-to-left: `D C - B A + *`
```
D -> stack: ["D"]
C -> stack: ["D","C"]
- -> op1="C", op2="D" -> push "(C-D)" -> stack: ["(C-D)"]
B -> stack: ["(C-D)","B"]
A -> stack: ["(C-D)","B","A"]
+ -> op1="A", op2="B" -> push "(A+B)" -> stack: ["(C-D)","(A+B)"]
* -> op1="(A+B)", op2="(C-D)" -> push "((A+B)*(C-D))" -> stack: ["((A+B)*(C-D))"]
Result: "((A+B)*(C-D))"
```

**Note the order:** We pop `op1` first (top of stack = left operand), then `op2` (right operand). Result: `"(" + op1 + op + op2 + ")"`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Same Stack with StringBuilder

**Intuition:** Identical algorithm to Approach 2. In Java, `String + String` inside a loop creates O(n) temporary objects. Using `StringBuilder.append()` reduces allocation pressure and makes total string building O(n) character operations.

**Steps:** Same as Approach 2, but construct the parenthesized expression with `StringBuilder`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n):** Each character is visited exactly once. Each string is pushed and popped exactly once. Total concatenation work across all operations is O(total output length) = O(n) (since each character from the input appears once in the output plus 2 parentheses per operator).

**Space O(n):** Maximum stack depth occurs with a deep right-skewed tree (e.g., `+A+B+C...`). The stack holds O(n) strings.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Single operand `A` | `A` | No operators; stack has one item |
| Two operands `+AB` | `(A+B)` | Basic case; pop A then B -- but scan R to L so B pushed first |
| Right-skewed tree `+A+B+CD` | `(A+(B+(C+D)))` | Deep recursion in brute force; stack handles it fine |
| All same operator | `((A+B)+C)` | Left-assoc; parens correctly show grouping |

**Common Mistakes:**
- **Scanning left to right** (wrong direction for stack approach) -- you get the wrong operand order.
- **Pop order:** pop `op1` (left, pushed second from right scan) then `op2` (right, pushed first). Form `op1 + operator + op2`.
- Writing `op2 + operator + op1` instead of `op1 + operator + op2` (swapped).
- Forgetting parentheses: `A+B` is ambiguous without them in a compound expression.

---

## 6. REAL-WORLD USE CASE

**Abstract Syntax Tree (AST) Display:** Programming language debuggers and IDE inspectors often store expressions as ASTs (which are naturally prefix trees). When displaying these to users, they convert to infix notation with parentheses so humans can read them naturally.

**Mathematical Formula Rendering:** CAS (Computer Algebra Systems) like Mathematica internally use prefix tree representations. When rendering formulas in a human-readable way, prefix-to-infix conversion is used for output.

---

## 7. INTERVIEW TIPS

- **Direction matters:** Prefix is processed right-to-left for the stack approach. Postfix is processed left-to-right. This asymmetry often trips candidates up.
- **Draw the tree** first -- it makes both the brute force and stack approaches obvious.
- **Pop order in R-to-L scan:** `op1 = pop()` gives the left operand (it was pushed last when scanning right-to-left), `op2 = pop()` gives the right operand.
- **All conversions summary:**
  - Postfix to Infix: scan L to R, `op2=pop, op1=pop`, push `(op1 op op2)`
  - Prefix to Infix: scan R to L, `op1=pop, op2=pop`, push `(op1 op op2)`
  - Postfix to Prefix: scan L to R, `op2=pop, op1=pop`, push `op+op1+op2`
  - Prefix to Postfix: scan R to L, `op1=pop, op2=pop`, push `op1+op2+op`

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Postfix to Infix | Same stack logic, but scan left-to-right |
| Prefix to Postfix | Similar right-to-left scan, different output format |
| Postfix to Prefix | Companion problem |
| Expression Tree Construction | Brute force builds one explicitly |
| Basic Calculator (LC #224) | Evaluating infix expressions using similar stack logic |

---

## Real-World Use Case

LISP interpreters convert prefix notation (S-expressions) to infix for human-readable output. Database query optimizers like PostgreSQL's planner convert internal prefix-form expression trees back to infix SQL for EXPLAIN output.
