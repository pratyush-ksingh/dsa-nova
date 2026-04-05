# Reverse a Linked List

> **Batch 2 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
**LeetCode #206.** Given the head of a singly linked list, reverse the list and return the new head. Implement both **iterative** and **recursive** solutions.

**Example:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5
Output: 5 -> 4 -> 3 -> 2 -> 1
```

### Analogy
Imagine a line of people each pointing to the person in front of them. To "reverse" the line, you walk along the line and ask each person to turn around and point at the person who was behind them instead. When you are done, the last person in the original line becomes the new leader, and the original leader points at nobody.

### Key Observations
1. **You need three pointers: prev, current, next.** At each step you reverse one arrow. `current.next = prev`. But before you do that, you must save `current.next` or you lose the rest of the list. *Aha: The classic three-pointer dance -- save, reverse, advance.*
2. **The old tail becomes the new head.** When `current` becomes null, `prev` is the last node you processed -- it is the new head. *Aha: You do not need to find the tail separately; prev naturally ends up there.*
3. **Recursive reversal: the base case returns the new head and it propagates up unchanged.** Each recursive call just reverses one arrow: `current.next.next = current`. *Aha: The recursion does the same pointer flip but in reverse order -- from tail to head.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List |
| **Algorithm** | Iterative pointer reversal / Recursion |
| **Why** | We only need to reverse next-pointers in place; no extra data structure needed |
| **Pattern cue** | "Reverse a linked list" is a fundamental pattern used in reverse-k-groups, palindrome check, and reorder-list |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Use a Stack/Array
**Intuition:** Store all values in an array, then rewrite node values in reverse order.

**Steps:**
1. Traverse the list, push all values into an array.
2. Traverse the list again, overwrite each node's value from the array in reverse order.

*This works but changes values, not pointers. Some problems (like those involving node references) require actual pointer reversal.*

### Approach 2: Iterative (Optimal)
**Intuition:** Walk through the list, reversing each `next` pointer one by one. Use three pointers: `prev`, `curr`, `next`.

**Steps:**
1. Initialize `prev = null`, `curr = head`.
2. While `curr != null`:
   - Save `next = curr.next` (preserve the rest of the list).
   - Reverse the pointer: `curr.next = prev`.
   - Advance: `prev = curr`, `curr = next`.
3. Return `prev` (new head).

**BUD Analysis:**
- **B**ottleneck: Must visit every node once -- cannot do better than O(n).
- **U**nnecessary work: None. Each node is touched exactly once.
- **D**uplicate work: None.

**Dry Run Trace:**
```
List: 1 -> 2 -> 3 -> null

Step 0: prev=null, curr=1
  next = 2
  1.next = null       (reverse arrow)
  prev = 1, curr = 2
  State: null <- 1    2 -> 3 -> null

Step 1: prev=1, curr=2
  next = 3
  2.next = 1          (reverse arrow)
  prev = 2, curr = 3
  State: null <- 1 <- 2    3 -> null

Step 2: prev=2, curr=3
  next = null
  3.next = 2          (reverse arrow)
  prev = 3, curr = null
  State: null <- 1 <- 2 <- 3

curr is null -> return prev = 3
Result: 3 -> 2 -> 1 -> null
```

### Approach 3: Recursive
**Intuition:** Recursively reverse the rest of the list, then fix the link from the next node back to the current node.

**Steps:**
1. Base case: if `head == null` or `head.next == null`, return `head`.
2. `newHead = reverse(head.next)` -- recursively reverse the rest.
3. `head.next.next = head` -- the node after head should now point back to head.
4. `head.next = null` -- head is now the tail, so its next is null.
5. Return `newHead` (propagated from base case).

**Dry Run Trace (Recursive):**
```
reverse(1 -> 2 -> 3)
  reverse(2 -> 3)
    reverse(3)
      base case: return 3         newHead = 3
    3.next = null, so head.next.next = head means 3.next = 2
    2.next = null
    return 3                      newHead = 3
  2.next.next = 1 means 2 was already handled... let me retrace:

reverse(1): calls reverse(2)
  reverse(2): calls reverse(3)
    reverse(3): base case, return 3
  Back in reverse(2): newHead=3
    head=2, head.next=3, so head.next.next = 3.next = 2   (3 now points to 2)
    head.next = 2.next = null   (2 points to null for now)
    return newHead=3
Back in reverse(1): newHead=3
  head=1, head.next=2, so head.next.next = 2.next = 1   (2 now points to 1)
  head.next = 1.next = null   (1 points to null -- it's the tail)
  return newHead=3

Final: 3 -> 2 -> 1 -> null
```

---

## 4. COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| Brute (array) | O(n) | O(n) | Store all values, then rewrite |
| Iterative | O(n) | O(1) | Single pass, only 3 pointers |
| Recursive | O(n) | O(n) | n recursive calls on the call stack |

*"You must visit every node at least once to reverse it. The iterative approach does this with zero extra memory -- just pointer juggling. The recursive approach trades stack space for elegance."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list (null) | Nothing to reverse | Return null |
| Single node | Already reversed | Return as-is |
| Two nodes | One pointer swap | Works with general algorithm |
| Very long list | Recursive approach may stack overflow | Use iterative for large n |

**Common Mistakes:**
- Forgetting to save `curr.next` before overwriting it -- you lose the rest of the list.
- Returning `curr` instead of `prev` at the end -- `curr` is null!
- In recursive approach, forgetting `head.next = null` -- creates a cycle between the first two nodes.
- Not handling the empty list check -- NullPointerException.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Iterative vs Recursive?** | Iterative is preferred for production (O(1) space, no stack overflow risk). Recursive is great to demonstrate understanding. |
| **Can you reverse in groups of k?** | Yes -- LC #25. Apply this reversal to k-length segments and connect them. |
| **How does this help in other problems?** | Palindrome LL (reverse second half), Reorder List (reverse + merge), Add Two Numbers II |
| **Follow-up: Can you reverse between positions m and n?** | LC #92. Isolate the sublist, reverse it, reconnect the boundaries. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Reverse Nodes in k-Group (LC #25) | Apply this reversal to k-size segments |
| Palindrome Linked List (LC #234) | Reverse second half, then compare |
| Reorder List (LC #143) | Find middle, reverse second half, merge |
| Reverse LL II (LC #92) | Reverse only a subrange [m, n] |
| Swap Nodes in Pairs (LC #24) | Reversal in groups of 2 |
