# Implement Min Stack

> **LeetCode 155** | **Step 09 — 9.1** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Design a stack that supports `push`, `pop`, `top`, and retrieving the **minimum element in constant time**.

Implement the `MinStack` class:
- `MinStack()` — initializes the stack object.
- `void push(int val)` — pushes element `val` onto the stack.
- `void pop()` — removes the element on top of the stack.
- `int top()` — gets the top element.
- `int getMin()` — retrieves the minimum element in the stack.

**All operations must run in O(1) time.**

## Examples

| Operations | Returns | Stack State |
|------------|---------|-------------|
| push(-2)   | —       | [-2]        |
| push(0)    | —       | [-2, 0]     |
| push(-3)   | —       | [-2, 0, -3] |
| getMin()   | -3      | —           |
| pop()      | —       | [-2, 0]     |
| top()      | 0       | —           |
| getMin()   | -2      | —           |

## Constraints

- `-2^31 <= val <= 2^31 - 1`
- Methods `pop`, `top`, and `getMin` will always be called on non-empty stacks.
- At most `3 * 10^4` calls will be made to `push`, `pop`, `top`, and `getMin`.

---

## Approach 1: Brute Force — Stack of (value, min) Pairs

**Intuition:** At every push, record not just the value but also the **current minimum at that stack depth**. When we pop, the previous minimum is automatically restored (it's stored one level down).

**Steps:**
1. Each stack entry is a pair `(value, running_min)`.
2. On `push(val)`: `running_min = min(val, previous_running_min)`.
3. On `getMin()`: read the second element of the top pair.
4. On `pop()`: simply remove the top pair.

| Metric | Value |
|--------|-------|
| Time   | O(1) for all operations |
| Space  | O(n) — two integers stored per element |

---

## Approach 2: Optimal — Encoded Value Trick (Single Stack)

**Intuition:** Encode the old minimum inside the stack when a new minimum is encountered. Use the equation `encoded = 2*val - old_min` (which is always < new_min since val < old_min). During pop, detect if the stored value is less than the current minimum (a signal that the old minimum is embedded) and recover it.

**Steps:**
1. Maintain `min_val` separately.
2. `push(val)`:
   - If `val < min_val`: push `2*val - min_val` and update `min_val = val`.
   - Else: push `val` normally.
3. `pop()`:
   - If `top < min_val`: restore `min_val = 2*min_val - top` before popping.
4. `top()`: if `stored < min_val`, real top is `min_val`; else `stored`.

**Caution:** `2*val - min_val` can overflow 32-bit integers. Use `long` in Java/C++.

| Metric | Value |
|--------|-------|
| Time   | O(1) for all operations |
| Space  | O(1) extra beyond the stack itself |

---

## Approach 3: Best — Two-Stack (Auxiliary Min-Stack)

**Intuition:** Maintain a parallel `min_stack` that only holds minimums. Push to `min_stack` whenever the new value is <= current minimum; pop from it whenever the popped value equals the current minimum. This way `min_stack[-1]` is always the current minimum.

**Steps:**
1. `push(val)`: push to main stack. If `min_stack` is empty or `val <= min_stack.top()`, also push to `min_stack`.
2. `pop()`: pop from main stack. If popped value == `min_stack.top()`, also pop from `min_stack`.
3. `top()`: return `stack.top()`.
4. `getMin()`: return `min_stack.top()`.

This is the **recommended interview answer** — clear, safe, no overflow risk.

| Metric | Value |
|--------|-------|
| Time   | O(1) for all operations |
| Space  | O(n) in worst case (all elements equal) |

---

## Real-World Use Case

**Trading systems:** A sliding window minimum tracker for stock prices — an order book needs to know the best (minimum) ask price at any time after any number of order insertions and cancellations (pushes and pops).

**Text editors / IDEs:** Undo stack where each state also tracks the "minimum error count" at that revision. Ctrl+Z restores state and the error count simultaneously in O(1).

## Interview Tips

- Start by acknowledging that a naive `getMin()` would scan the stack — O(n). Show why that fails for real-time systems.
- Approach 1 (pairs) is the safest to code correctly in an interview. Approach 3 (two stacks) is the most commonly expected.
- For Approach 2 (encoding), explain the invariant: **any stored value < current min is an "encoded" entry** that hides the old min inside it. Interviewers will probe why `2*val - old_min` works.
- Do NOT use `<` on the min_stack push check — use `<=`. If you use `<`, pushing duplicate minimums will cause the min_stack to lose track on pop.
- Always use `long` (Java) / `long long` (C++) for the encoding trick to avoid integer overflow.
