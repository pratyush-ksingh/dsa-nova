# Search in Linked List

> **Batch 4 of 12** | **Topic:** Linked List Basics | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a singly linked list and a target value, determine whether the target exists in the list. Return `true` if found, `false` otherwise. As a follow-up, return the 0-based index of the first occurrence (or -1 if absent).

### Examples

| # | Input List | Target | Output (bool) | Output (index) | Explanation |
|---|-----------|--------|---------------|----------------|-------------|
| 1 | 1 -> 3 -> 5 -> 7 | 5 | true | 2 | 5 is at index 2 |
| 2 | 1 -> 3 -> 5 -> 7 | 4 | false | -1 | 4 is not in the list |
| 3 | (empty) | 1 | false | -1 | Empty list has no nodes |
| 4 | 9 | 9 | true | 0 | Single node matches |

### Real-Life Analogy
Think of a linked list as a **treasure hunt with clues**. Each clue (node) tells you the next location to visit but nothing about the rest of the chain. To find a specific treasure (target), you must follow every clue from the very first one -- you cannot skip ahead because there is no map (no random access).

### Three Key Observations (the "Aha!" Moments)
1. **No random access** -- Unlike arrays, you cannot jump to index `i` in O(1). You must traverse from the head one node at a time.
2. **Early termination** -- The moment you find the target, you can stop. In the best case this is O(1).
3. **Single-pass sufficiency** -- One traversal is both necessary and sufficient; there is no hidden structure to exploit because the list is unsorted.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force (Iterative) | Walk every node, compare with target | Pointer (runner) |
| Optimal (Recursive) | Let the call stack walk the list | Implicit call stack |
| Best (Iterative with Index) | Same traversal but also track position | Pointer + counter |

For an unsorted linked list, O(n) traversal is the theoretical minimum -- every element might need inspection. The "best" approach here is not faster asymptotically but solves the extended problem (return index) in one clean pass with O(1) space.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Scan

**Intuition:** Start at the head and walk forward. At each node, check `node.val == target`. Stop if found or if you reach null.

**BUD Analysis:**
- **B**ottleneck: The traversal itself is the bottleneck at O(n).
- **U**nnecessary work: None -- we must inspect each node.
- **D**uplicate work: None.

**Steps:**
1. Set `current = head`.
2. While `current != null`:
   - If `current.val == target`, return `true`.
   - Move `current = current.next`.
3. Return `false`.

**Dry-Run Trace** (list: 1->3->5->7, target: 5):
```
Step   current.val   Match?   Action
 1        1           No      Move to 3
 2        3           No      Move to 5
 3        5           Yes     Return true
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

### Approach 2: Optimal -- Recursive Search

**Intuition:** Recursion naturally mirrors linked-list traversal. Base cases: if `node` is null, return false; if `node.val == target`, return true. Otherwise recurse on `node.next`.

**BUD Analysis:**
- The time is the same O(n), but now we spend O(n) space on the call stack. This is a classic "recursion for practice" approach, not an efficiency improvement.

**Steps:**
1. Base case: `node == null` -> return false.
2. Base case: `node.val == target` -> return true.
3. Recursive case: return `search(node.next, target)`.

**Dry-Run Trace** (list: 1->3->5->7, target: 5):
```
search(1, 5) -> search(3, 5) -> search(5, 5) -> return true
   ^-- propagates true back up the chain
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) -- call stack |

---

### Approach 3: Best -- Iterative with Index Return

**Intuition:** Combine the boolean search with index tracking. Maintain a counter `idx` starting at 0. Return `idx` when found, `-1` when you exhaust the list. This solves both variants of the problem in one pass.

**Steps:**
1. Set `current = head`, `idx = 0`.
2. While `current != null`:
   - If `current.val == target`, return `idx`.
   - `current = current.next`, `idx++`.
3. Return `-1`.

**Dry-Run Trace** (list: 1->3->5->7, target: 5):
```
Step   current.val   idx   Match?
 1        1           0     No
 2        3           1     No
 3        5           2     Yes -> return 2
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is unavoidable:** In the worst case the target is the last node (or absent). With no indexing and no ordering, you must look at every node. This is analogous to finding a specific carriage on a train by walking through each one.

**Space trade-off:** The iterative approaches use O(1) because a single pointer suffices. The recursive approach turns each "step" into a function call on the stack, consuming O(n) space -- fine for learning, bad for a list with millions of nodes.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Empty list (head = null) | false / -1 | Forgetting the null check causes NullPointerException |
| Single node, matches | true / 0 | Loop should handle n=1 naturally |
| Single node, no match | false / -1 | Same as above |
| Target at tail | true / n-1 | Must not break prematurely |
| Duplicate values | First occurrence | Problem asks for first index |

**Common Mistakes:**
- Forgetting `current != null` guard and dereferencing null.
- Returning the node instead of boolean/index.
- Off-by-one: starting `idx` at 1 instead of 0.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** It tests your ability to traverse a linked list, handle edge cases, and articulate why O(n) is optimal for unsorted data.

**Follow-ups to expect:**
- "What if the list were sorted?" -> You could stop early when `node.val > target`, but still O(n) worst case (binary search needs random access).
- "Search and delete?" -> Combine traversal with prev-pointer tracking.
- "Thread-safe search?" -> Discuss read locks or copy-on-write patterns.

**Talking points:**
- Mention that for frequent lookups, converting to a hash set gives O(1) per query at O(n) space cost.
- Compare with array search to highlight linked-list trade-offs.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Delete a Node in LL | Same traversal pattern, but you track the previous node |
| Find Middle of LL | Traversal + two-pointer variation |
| Linked List Cycle Detection | Traversal with fast/slow pointers |
| Search in BST | Search in a *structured* linked structure -- O(log n) |
