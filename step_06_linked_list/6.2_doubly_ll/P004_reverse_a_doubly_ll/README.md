# Reverse a Doubly Linked List

> **Batch 3 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given the head of a doubly linked list, reverse the entire list so that the last node becomes the first and all `prev`/`next` pointers are swapped. Return the new head.

**Example:**
```
Input:  null <-> 1 <-> 2 <-> 3 <-> 4 <-> null
Output: null <-> 4 <-> 3 <-> 2 <-> 1 <-> null
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 1 <-> 2 <-> 3 <-> 4 | 4 <-> 3 <-> 2 <-> 1 | All links reversed |
| 1 <-> 2 | 2 <-> 1 | Two-node swap |
| 5 | 5 | Single node stays the same |
| null | null | Empty list stays empty |

### Real-Life Analogy
Think of a **two-way street** with signs pointing forward and backward at each intersection. Reversing the list is like flipping every sign at every intersection: every "forward" sign becomes the "backward" sign and vice versa. After you flip all signs, the old last intersection becomes the new entry point. You walk through once, flipping one pair of signs per intersection.

### Key Observations
1. **Observation:** In a doubly linked list, each node has two pointers: `prev` and `next`. Reversing means swapping these two pointers for every single node.
2. **Observation:** After swapping all pointers, the old tail (whose `next` was null) becomes the new head (now its `prev` is null, its `next` points backward).
3. **Aha moment:** Unlike reversing a singly linked list (which requires careful pointer juggling with a `prev` variable), a DLL reversal is simpler: just swap `prev` and `next` at each node. The structure already has both pointers. The new head is the last node you visit.

### Constraints
- 0 <= number of nodes <= 10^4
- -10^5 <= Node.val <= 10^5
- Standard doubly linked list (no sentinel/dummy nodes unless specified)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why In-Place Pointer Swap?
We already have both forward and backward pointers. Reversing just means swapping them. No extra data structure is needed. An auxiliary stack approach works but wastes O(n) space unnecessarily.

### Pattern Recognition
**Classification cue:** "Reverse a linked list" --> pointer reversal pattern. For DLL specifically, the swap-prev-next pattern is the canonical approach. This is simpler than singly-linked reversal because you don't need a separate `prev` tracking variable -- each node already has one.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Stack-Based Reversal
**Intuition:** Traverse the list, push all node values onto a stack. Then traverse again, popping values and overwriting each node's data. This "reverses" the values but not the pointers. Works but is inelegant and uses O(n) space.

**Steps:**
1. Traverse the list, pushing each node's value onto a stack.
2. Traverse the list again from head, popping values from the stack and assigning to each node.
3. Return head (same head node, but values are reversed).

**Dry Run Trace (1 <-> 2 <-> 3):**

| Phase | Action | Stack |
|-------|--------|-------|
| Push pass | Visit 1,2,3 | [1,2,3] (top=3) |
| Pop pass | Node1.val = pop(3), Node2.val = pop(2), Node3.val = pop(1) | [] |
| Result | 3 <-> 2 <-> 1 (values reversed) | |

| Metric | Value |
|--------|-------|
| Time | O(n) -- two passes |
| Space | O(n) -- stack stores all values |

**BUD Transition:** We are wasting O(n) space and doing two passes. Also, swapping values is fragile if nodes contain more than just an integer. We should reverse the actual pointers in a single pass.

---

### Approach 2: Optimal -- Iterative Pointer Swap
**Intuition:** Walk through each node and swap its `prev` and `next` pointers. After processing all nodes, the last node processed becomes the new head.

**Steps:**
1. Initialize `current = head`.
2. While `current` is not null:
   - Swap `current.prev` and `current.next`.
   - Move to `current.prev` (which was the original `next` before swapping).
   - Track `current` as potential new head.
3. Return the new head (the last non-null `current`).

**Dry Run Trace (1 <-> 2 <-> 3 <-> 4):**

| Step | current | Before Swap | After Swap | Move to |
|------|---------|-------------|------------|---------|
| 1 | Node(1) | prev=null, next=2 | prev=2, next=null | current.prev = Node(2) |
| 2 | Node(2) | prev=1, next=3 | prev=3, next=1 | current.prev = Node(3) |
| 3 | Node(3) | prev=2, next=4 | prev=4, next=2 | current.prev = Node(4) |
| 4 | Node(4) | prev=3, next=null | prev=null, next=3 | current.prev = null --> stop |

New head = Node(4). List: 4 <-> 3 <-> 2 <-> 1.

| Metric | Value |
|--------|-------|
| Time | O(n) -- single pass |
| Space | O(1) -- only pointer variables |

---

### Approach 3: Best -- Clean Iterative with Last-Node Tracking
**Intuition:** Same algorithm as Approach 2 but written in the cleanest possible form. Track the `last` valid node explicitly so you never have to do a post-loop adjustment. This is the production-ready version.

**Steps:**
1. Initialize `current = head`, `last = null`.
2. While `current` is not null:
   - Store `last = current`.
   - Swap: `temp = current.prev; current.prev = current.next; current.next = temp`.
   - Advance: `current = current.prev` (original next).
3. Return `last`.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal:** Every node's pointers must be modified. You cannot reverse a list without touching each node at least once. Therefore O(n) is the lower bound.

**Why O(1) space:** Each swap is purely local -- you only need the current node and a temp variable. No history of previous nodes is required since the DLL already stores both directions.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Moving to `current.next` after swap | After swapping, `next` is now the OLD `prev` | Move to `current.prev` (which is the original next) |
| Forgetting to return the new head | Return old head instead of new | Track the last non-null node during traversal |
| Not handling null/single node | Crashes on empty list | While-loop condition handles it naturally |
| Swapping values instead of pointers | Seems simpler but is fragile | Always swap the actual pointer fields |

### Edge Cases Checklist
- Empty list (null) --> return null
- Single node --> return the same node (prev and next are both null, swapping does nothing)
- Two nodes --> simple swap, both become each other's neighbors
- Already reversed list --> reversing again gives the original

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is it a standard doubly linked list? Any sentinel nodes? Return new head?"
2. **M**atch: "DLL reversal --> swap prev and next at every node."
3. **P**lan: "Single pass, swap both pointers at each node, track new head."
4. **I**mplement: Write the clean iterative version.
5. **R**eview: Trace with 3-4 nodes, verify all pointers.
6. **E**valuate: "O(n) time, O(1) space. Optimal."

### Follow-Up Questions
- "Reverse a singly linked list." --> Need a `prev` variable since nodes lack a `prev` pointer.
- "Reverse between positions l and r in a DLL." --> Isolate the sublist, reverse it, reconnect.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | DLL insertion, deletion, traversal basics |
| **Same pattern** | Reverse a Singly Linked List (similar logic, slightly harder) |
| **Next step** | Reverse nodes in k-group in DLL |
| **Harder variant** | Reverse alternating k-groups in DLL |
| **Unlocks** | Palindrome check in DLL, DLL sorting algorithms |
