# Reverse a Stack using Recursion

> **Step 07 — 7.1** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given a stack, reverse its elements using **only recursion** — no loops and no extra data structures (auxiliary stacks, arrays, etc.) are allowed. Only the implicit call stack may be used.

## Examples

| Input Stack (bottom to top) | Output Stack (bottom to top) |
|-----------------------------|------------------------------|
| [1, 2, 3, 4, 5]             | [5, 4, 3, 2, 1]              |
| [10, 20, 30]                | [30, 20, 10]                 |
| [42]                        | [42]                         |

## Constraints

- Stack can hold any integers.
- Only recursion is allowed — no auxiliary data structure.
- No loops (while/for) permitted in the optimal/best approaches.

---

## Approach 1: Brute Force — Auxiliary Stack

**Intuition:** Transfer all elements to a helper stack (which naturally reverses them), then transfer back. Simple and O(n) time, but uses O(n) extra space explicitly.

**Steps:**
1. Pop all elements from the original stack, pushing each onto `aux`.
2. Pop all elements from `aux` back onto the original stack.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) extra (auxiliary stack) |

---

## Approach 2: Optimal — Recursive Reverse + insertAtBottom Helper

**Intuition:** To reverse a stack, pop the top element, reverse the remaining stack recursively, then insert the popped element at the **bottom**. The `insertAtBottom` helper itself uses recursion: pop everything, place the item at the base, then put everything back.

**Steps:**
1. Base case: if stack has 0 or 1 element, return.
2. Pop `top` from the stack.
3. Recursively call `reverseStack` on the remaining elements.
4. Call `insertAtBottom(stack, top)`:
   - If stack is empty, push `top`.
   - Else pop `held`, call `insertAtBottom` recursively, then push `held` back.

**Recursive trace for [1,2,3] (top=3):**
```
reverseStack([1,2,3])
  top=3, reverseStack([1,2])
    top=2, reverseStack([1])
      base -> [1]
    insertAtBottom([1], 2) -> [2,1]
  insertAtBottom([2,1], 3) -> [3,2,1]
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) — n calls each doing O(n) insertAtBottom work |
| Space  | O(n) — call stack depth |

---

## Approach 3: Best — Clean Recursive (Canonical Interview Answer)

**Intuition:** Identical to Approach 2 but presented as the single, self-contained "pure recursion" solution — the answer interviewers look for when they say "only recursion allowed."

The two mutually recursive functions are:
- `reverseStack(stack)` — pops top, reverses rest, inserts top at bottom.
- `insertAtBottom(stack, item)` — holds everything, places item at base, restores.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) call stack |

---

## Real-World Use Case

**Undo/Redo systems:** Reversing the order of recorded operations (command history) without creating a second buffer — the recursion itself acts as the temporary store during the swap.

**Expression evaluators:** When an expression tree is built in reverse order (right-to-left parsing), reversing the operator stack recursively re-orders operations without allocating a second stack.

## Interview Tips

- The trick is recognizing that you need **two** recursive functions: one to reverse and one to insert at the bottom. Many candidates try a single recursive function and get stuck.
- `insertAtBottom` is the real key — understand it first, then `reverseStack` becomes obvious.
- Time complexity is O(n^2): `reverseStack` is called n times, each triggering an `insertAtBottom` that is O(n). Be ready to explain this.
- If loops are allowed (Approach 1), the problem collapses to O(n) trivially — make this distinction clear.
- Edge cases: empty stack, single element — both should return unchanged.
