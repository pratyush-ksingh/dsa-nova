# Detect Loop in Linked List

> **Batch 3 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given the head of a singly linked list, determine whether the linked list contains a **cycle** (loop). A cycle exists if some node's `next` pointer points back to a previously visited node, causing infinite traversal.

**LeetCode #141 - Linked List Cycle**

**Example:**
```
Input:  1 -> 2 -> 3 -> 4 -> 5 -> 3 (back to node with value 3)
Output: true

Input:  1 -> 2 -> 3 -> null
Output: false
```

| Input | Cycle? | Output | Explanation |
|-------|--------|--------|-------------|
| 3->2->0->-4->2 (tail connects to index 1) | Yes | true | Node -4 points back to node 2 |
| 1->2->1 (tail connects to index 0) | Yes | true | Node 2 points back to node 1 |
| 1->null | No | false | Single node, no cycle |
| empty list | No | false | Nothing to traverse |

### Real-Life Analogy
Imagine running on a **circular track** vs. a straight road. On a straight road you eventually reach the end. On a circular track, a fast runner will eventually lap a slow runner -- they will meet. That is exactly Floyd's algorithm: send two runners (slow and fast) around the list. If there is a loop, the fast one laps the slow one. If there is no loop, the fast one hits the finish line (null).

### Key Observations
1. **Observation:** Without a cycle, traversal always terminates at `null`. With a cycle, traversal never ends -- we need a detection mechanism.
2. **Observation:** Storing every visited node in a HashSet lets us detect the first repeated visit, but costs O(n) space.
3. **Aha moment:** Two pointers moving at different speeds (1 step vs. 2 steps) are guaranteed to meet inside a cycle. This is Floyd's Tortoise and Hare -- O(1) space and O(n) time. *Why must they meet?* Once both are in the cycle, the gap closes by 1 each step, so they collide within one full loop.

### Constraints
- 0 <= number of nodes <= 10^4
- -10^5 <= Node.val <= 10^5
- `pos` (cycle entry index) is -1 if there is no cycle

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Two Pointers?
The problem is fundamentally about detecting revisitation in a sequence. A HashSet solves it but uses O(n) space. The **fast-and-slow pointer** technique exploits the structure of the cycle itself to detect revisitation with O(1) extra space.

### Pattern Recognition
**Classification cue:** "Detect cycle / loop" --> Floyd's cycle detection algorithm. This pattern recurs in: finding the start of a cycle, finding the length of a cycle, detecting cycles in number sequences (happy number), and detecting duplicate numbers (LeetCode #287).

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashSet Visited Tracking
**Intuition:** As we traverse each node, store it in a set. If we ever encounter a node already in the set, there is a cycle. If we reach `null`, there is no cycle.

**Steps:**
1. Create an empty HashSet of node references.
2. Start at `head`. While current node is not null:
   - If current node is in the set, return `true`.
   - Add current node to the set.
   - Move to `current.next`.
3. Return `false` (reached null, no cycle).

**Dry Run Trace (1->2->3->4->2, cycle at node 2):**

| Step | Current | Set Contents | Action |
|------|---------|-------------|--------|
| 1 | Node(1) | {} | Not in set, add. Set: {1} |
| 2 | Node(2) | {1} | Not in set, add. Set: {1,2} |
| 3 | Node(3) | {1,2} | Not in set, add. Set: {1,2,3} |
| 4 | Node(4) | {1,2,3} | Not in set, add. Set: {1,2,3,4} |
| 5 | Node(2) | {1,2,3,4} | Already in set! Return true |

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node at most once |
| Space | O(n) -- store up to n nodes in the set |

**BUD Transition:** The bottleneck is O(n) space. Can we detect the cycle without storing visited nodes? Yes -- use two pointers at different speeds.

---

### Approach 2: Optimal -- Floyd's Tortoise and Hare
**Intuition:** Use two pointers: `slow` moves 1 step, `fast` moves 2 steps. If there is a cycle, `fast` enters it first and keeps looping. Meanwhile `slow` enters. Once both are in the cycle, the distance between them decreases by 1 each step, so they must collide. If `fast` reaches `null`, there is no cycle.

**Steps:**
1. Initialize `slow = head`, `fast = head`.
2. While `fast` is not null AND `fast.next` is not null:
   - Move `slow` one step: `slow = slow.next`.
   - Move `fast` two steps: `fast = fast.next.next`.
   - If `slow == fast`, return `true`.
3. Return `false`.

**Why do they meet?** Once both pointers are inside the cycle of length C, think of the relative speed: fast approaches slow by 1 node per iteration. The gap decreases: C-1, C-2, ..., 1, 0. They meet in at most C iterations.

**Dry Run Trace (1->2->3->4->2, cycle at node 2):**

| Step | slow | fast | Meet? |
|------|------|------|-------|
| init | 1 | 1 | - |
| 1 | 2 | 3 | No |
| 2 | 3 | 2 (4->2) | No |
| 3 | 4 | 4 (3->4) | Yes! Return true |

| Metric | Value |
|--------|-------|
| Time | O(n) -- at most n + C iterations (C = cycle length) |
| Space | O(1) -- only two pointer variables |

---

### Approach 3: Best -- Floyd's with Early Exit (Same Asymptotic)
**Intuition:** Floyd's algorithm is already optimal at O(n) time and O(1) space. No algorithm can do better because you must potentially visit every node. The "best" refinement is a clean, production-ready implementation that also handles edge cases elegantly and can be extended to find the cycle start or cycle length.

**Steps:**
1. Same as Approach 2 (Floyd's is the best known algorithm for this).
2. Additional utility: once a meeting point is found, you can find the cycle start by resetting one pointer to head and advancing both at speed 1 until they meet.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) time is inevitable:** In the worst case (no cycle, or cycle at the very end), you must visit all n nodes before concluding. You cannot skip nodes without potentially missing the cycle entry point.

**Why Floyd's is O(n) not O(n^2):** Once both pointers enter the cycle, the fast pointer "catches up" to the slow pointer by 1 position per step. The cycle has at most n nodes, so collision happens within n steps of both entering the cycle. Total steps: at most 2n.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Checking `fast.next` without checking `fast` first | `fast` can be null | Check `fast != null && fast.next != null` |
| Comparing node values instead of references | Two nodes can have the same value | Compare object references (`==` in Java, `is` in Python) |
| Starting with `slow == fast` check before first move | Both start at head, would immediately return true | Check AFTER moving, not before |
| Forgetting empty list / single node | Head is null or head.next is null | The while condition handles both naturally |

### Edge Cases Checklist
- Empty list (head is null) --> false
- Single node, no cycle --> false
- Single node pointing to itself --> true
- Two nodes forming a cycle --> true
- Very long list, cycle at the very end --> true
- No cycle at all --> false

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is the input a singly linked list? Can it be empty? Should I return boolean?"
2. **M**atch: "Cycle detection --> Floyd's Tortoise and Hare."
3. **P**lan: "Two pointers at different speeds. If they meet, cycle exists."
4. **I**mplement: Write Floyd's. Mention HashSet as brute force alternative.
5. **R**eview: Trace with a cycle case and a no-cycle case.
6. **E**valuate: "O(n) time, O(1) space. HashSet alternative is O(n) space."

### Follow-Up Questions
- "Find the start of the cycle." --> After detection, reset one pointer to head, move both at speed 1. They meet at the cycle start (LeetCode #142).
- "Find the length of the cycle." --> After detection, keep one pointer fixed, move the other counting steps until they meet again.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Linked list traversal basics |
| **Same pattern** | Happy Number (cycle in number sequence), Find Duplicate Number (#287) |
| **Next step** | Find Starting Point of Loop (P004 in 6.3) |
| **Harder variant** | Length of Loop in LL (P005 in 6.3) |
| **Unlocks** | All cycle-related linked list problems |
