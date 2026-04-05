# Postfix to Infix

> **Step 09.9.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Convert a postfix (Reverse Polish Notation) expression to its equivalent infix expression. Operands are single characters (a-z, A-Z, 0-9) and operators are `+`, `-`, `*`, `/`, `^`.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `"ab+"` | `"(a+b)"` | Simple addition |
| `"ab+c*"` | `"((a+b)*c)"` | Add a+b first, then multiply by c |
| `"abc*+"` | `"(a+(b*c))"` | Multiply b*c first, then add a |
| `"ab+cd+*"` | `"((a+b)*(c+d))"` | Two sums multiplied together |

**Constraints:**
- Input is a valid postfix expression
- Operands are single characters
- Operators are binary: `+`, `-`, `*`, `/`, `^`
- `1 <= length <= 100`

### Real-Life Analogy
> *Postfix notation is how your calculator's internal stack works -- it processes numbers and then applies operations. Converting postfix to infix is like translating machine-friendly code back into human-readable mathematical notation with proper parentheses, so you can read it as "a plus b, times c" rather than "a b + c *".*

### Key Observations
1. In postfix, when we encounter an operator, the two most recently seen complete expressions are its operands.
2. A stack naturally tracks "most recently seen" items -- perfect for this problem.
3. We wrap each combined expression in parentheses to preserve evaluation order.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Build Expression Tree, Then Inorder Traverse
**Intuition:** Parse the postfix expression into a binary expression tree. Each operator becomes an internal node with its two operands as children. Then perform an inorder traversal to produce the infix string.

**Steps:**
1. Create a `Node` class with `val`, `left`, `right`.
2. Scan postfix left to right. For each character:
   - If operand: create a leaf node, push to stack.
   - If operator: pop two nodes (right then left), create a new node with operator and these children, push back.
3. The stack's final element is the root of the expression tree.
4. Inorder traverse: for each internal node, output `"(" + left + op + right + ")"`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) -- tree nodes + stack |

---

### Approach 2: Optimal -- Stack-Based Direct String Construction
**Intuition:** We do not need to build an actual tree. Instead, we store string expressions on the stack. When we see an operator, we pop two expression strings, combine them as `"(" + left + op + right + ")"`, and push the result back.

**Steps:**
1. Initialize an empty stack.
2. Scan postfix left to right:
   - If operand: push as a string.
   - If operator: pop `op2` (top), pop `op1`, push `"(" + op1 + operator + op2 + ")"`.
3. The stack's top is the final infix expression.

**Dry Run:** `"ab+c*"`
```
Scan 'a' -> stack: ["a"]
Scan 'b' -> stack: ["a", "b"]
Scan '+' -> pop "b", "a" -> push "(a+b)" -> stack: ["(a+b)"]
Scan 'c' -> stack: ["(a+b)", "c"]
Scan '*' -> pop "c", "(a+b)" -> push "((a+b)*c)" -> stack: ["((a+b)*c)"]
```
Result: `"((a+b)*c)"`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Stack with Minimal Parentheses
**Intuition:** The optimal approach wraps every sub-expression in parentheses, which can produce redundant ones. This approach tracks the root operator of each sub-expression and only adds parentheses when necessary based on operator precedence, producing cleaner output.

**Steps:**
1. Same stack-based scan, but each stack entry is `(expression, root_operator)`.
2. When combining, check if the sub-expression's root operator has lower precedence than the current operator. Only parenthesize if needed.
3. Push the combined expression with the current operator as its root.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Wrong pop order:** The first popped element is the RIGHT operand, the second is the LEFT. Swapping them gives wrong infix.
2. **Missing parentheses:** Without parentheses, operator precedence can change the meaning of the expression.
3. **Not handling single operand:** If the input is a single character like `"a"`, just return it as-is.

### Edge Cases
- Single operand: `"a"` -> `"a"`
- All same operators: `"abc++"`  -> `"(a+(b+c))"`
- Mixed precedence: `"abc*+"` -> `"(a+(b*c))"` (no extra parens needed around `b*c` in minimal version)

---

## Real-World Use Case
**Compilers and calculators** internally use postfix notation for expression evaluation. When displaying the expression back to the user (in an IDE debugger, symbolic math tool, or spreadsheet formula bar), the compiler must convert postfix back to infix with correct parenthesization. This conversion is also fundamental in **decompilers** that reconstruct source code from bytecode.

## Interview Tips
- Mention that postfix-to-infix is the reverse of the Shunting Yard algorithm (infix-to-postfix).
- The stack-based approach is the standard answer -- always demonstrate it.
- Clarify whether the interviewer wants full parenthesization or minimal parentheses.
- Follow-up: "How would you convert postfix to prefix?" -- Same idea, but combine as `op + left + right` instead of `left + op + right`.
