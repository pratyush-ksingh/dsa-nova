# Find Length of Linked List

> **Batch 3 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given the head of a singly linked list, find the **total number of nodes** in the list.

**Example:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5 -> null
Output: 5

Input:  null (empty list)
Output: 0
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 1->2->3->4->5 | 5 | Five nodes in the list |
| 10->20 | 2 | Two nodes |
| 7 | 1 | Single node |
| null | 0 | Empty list has zero nodes |

### Real-Life Analogy
Imagine counting **train carriages**. You start at the engine (head), walk through each carriage one by one, and count until you reach the last carriage (whose next door leads nowhere -- null). Alternatively, you could stand at the engine and shout "count yourself and pass it back!" through every carriage -- each carriage adds 1 to the count it receives from behind. That is the recursive approach.

### Key Observations
1. **Observation:** A linked list has no `.length` property. You must traverse to count -- this is fundamentally O(n).
2. **Observation:** The iterative approach uses a counter and a pointer walking to null. The recursive approach uses the call stack: `length(node) = 1 + length(node.next)` with base case `length(null) = 0`.
3. **Aha moment:** Recursion is elegant but uses O(n) stack space. For a very long list, recursion risks stack overflow. Iterative is always safe. In an interview, show both but recommend iterative for production.

### Constraints
- 0 <= number of nodes <= 10^4
- No cycles in the list (assume well-formed singly linked list)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Traversal?
There is no shortcut. A singly linked list only exposes sequential access. You must visit every node to count them. No data structure needed -- just a pointer and a counter.

### Pattern Recognition
**Classification cue:** "Count / measure a linked list" --> single-pass traversal. This is the most basic linked list operation and underpins nearly every other linked list algorithm (you often need the length first to solve harder problems like "remove nth node from end").

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Traversal
**Intuition:** Walk through the list node by node. Each time you move to the next node, increment a counter. When you reach null, the counter holds the length.

**Steps:**
1. Initialize `count = 0`, `current = head`.
2. While `current` is not null:
   - Increment `count`.
   - Move `current = current.next`.
3. Return `count`.

**Dry Run Trace (1->2->3->null):**

| Step | current | count |
|------|---------|-------|
| init | Node(1) | 0 |
| 1 | Node(2) | 1 |
| 2 | Node(3) | 2 |
| 3 | null | 3 |

Return 3.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node once |
| Space | O(1) -- only a pointer and counter |

**BUD Transition:** Time is already optimal (must visit all nodes). But can we express this differently? Yes -- recursion provides an alternative formulation.

---

### Approach 2: Optimal -- Recursive Length
**Intuition:** Define the length of a list starting at node `curr` as: if `curr` is null, return 0. Otherwise, return `1 + length(curr.next)`. The recursion naturally counts each node as the call stack unwinds.

**Steps:**
1. Base case: if `node == null`, return 0.
2. Recursive case: return `1 + length(node.next)`.

**Dry Run Trace (1->2->3->null):**

```
length(1) = 1 + length(2)
                 = 1 + length(3)
                            = 1 + length(null)
                            = 1 + 0 = 1
                 = 1 + 1 = 2
           = 1 + 2 = 3
```

Return 3.

| Metric | Value |
|--------|-------|
| Time | O(n) -- one recursive call per node |
| Space | O(n) -- recursion depth equals number of nodes |

**BUD Transition:** Recursion uses O(n) stack space. For production code, the iterative approach is preferred. Can we combine clarity with efficiency? Yes -- tail-recursive style with an accumulator.

---

### Approach 3: Best -- Tail-Recursive / Iterative (Production-Ready)
**Intuition:** Use tail recursion with an accumulator (which compilers/interpreters can optimize to iterative) or simply the clean iterative version. This is the recommended production approach: O(n) time, O(1) space, and crystal clear.

**Steps (Tail-Recursive):**
1. Define helper `length(node, acc)`.
2. Base case: if `node == null`, return `acc`.
3. Recursive case: return `length(node.next, acc + 1)`.
4. Call: `length(head, 0)`.

This is semantically identical to the iterative approach. In Java (no tail-call optimization), use the iterative form. In Python, also use iterative (Python has a recursion limit of ~1000 by default).

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) iterative / O(n) recursive without TCO |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal:** A linked list is a sequential structure. There is no random access, no skip pointers, no metadata storing the length. You physically must visit every node. No algorithm can do better than O(n) for an unseen linked list.

**Why O(1) space is achievable:** You only need to remember your current position and the running count. Nothing about past nodes needs to be stored.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Off-by-one: starting count at 1 | Assumes head is always non-null | Start count at 0, increment for each non-null node |
| Dereferencing null head | Forgetting empty list case | The while loop naturally handles null head (returns 0) |
| Stack overflow with recursion | Very long list exceeds recursion limit | Use iterative approach for production |
| Counting null as a node | Including the null terminator | Loop condition: `while (current != null)` not `while (current.next != null)` |

### Edge Cases Checklist
- Empty list (null) --> 0
- Single node --> 1
- Very long list (10,000 nodes) --> iterative handles fine, recursion may not
- All nodes have the same value --> does not affect count

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is it a singly linked list? Can it be empty? Any cycles?"
2. **M**atch: "Simple traversal -- count nodes one by one."
3. **P**lan: "Iterative: walk with pointer, count. Recursive: 1 + length(rest)."
4. **I**mplement: Write iterative first (O(1) space), then show recursive for elegance.
5. **R**eview: Trace with empty list, single node, and multi-node list.
6. **E**valuate: "Both O(n) time. Iterative is O(1) space, recursive is O(n) space."

### Follow-Up Questions
- "Now find the middle node." --> Use the length, or use slow/fast pointer for one-pass.
- "Find nth node from the end." --> Two pointers with n-gap, or use length - n + 1 from start.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic linked list traversal, recursion basics |
| **Same pattern** | Print linked list, sum of linked list elements |
| **Next step** | Middle of Linked List (uses length or two pointers) |
| **Harder variant** | Length of Loop in LL, Remove Nth from End |
| **Unlocks** | Any problem that needs length as a preprocessing step |
