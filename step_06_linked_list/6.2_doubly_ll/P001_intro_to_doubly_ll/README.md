# Intro to Doubly Linked List

> **Batch 1 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
**Create a doubly linked list** from an array. Implement the three core operations: **insert** (at head, tail, and given position), **delete** (from head, tail, and given position), and **traverse** (forward and backward).

### Analogy
Imagine a **conga line** at a party. Each person (node) holds the shoulder of the person in front (`next`) AND the person behind holds theirs (`prev`). You can walk the line forwards or backwards. To remove someone from the middle, the person in front and behind simply connect to each other -- no one else moves.

### Key Observations
1. **Two pointers per node: `prev` and `next`.** This doubles the pointer overhead but gives you bidirectional traversal. *Aha: You can delete a node in O(1) if you already have a reference to it -- no need to find its predecessor like in a singly linked list.*
2. **Insertion/deletion requires updating TWO links.** Every operation must maintain both forward and backward pointers. *Aha: The most common bug is forgetting to update the `prev` pointer -- the list looks fine going forward but breaks going backward.*
3. **Symmetric structure.** The operations at head and tail are mirrors of each other. *Aha: If you understand insert-at-head, you already understand insert-at-tail by symmetry.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Doubly Linked List (Node with `val`, `prev`, `next`) |
| **Why** | Bidirectional traversal, O(1) delete with node reference |
| **Pattern cue** | "Navigate both directions," "delete given node," "LRU cache" -- think doubly linked list. |

---

## 3. APPROACH LADDER

### Approach 1: Build + Full Operations (Standard Implementation)
**Intuition:** Implement the DLL class with all fundamental operations. Each operation carefully maintains both `prev` and `next` pointers.

**Steps for Build from Array:**
1. If array is empty, head = tail = null.
2. Create head from `arr[0]`, set `prev = null`.
3. For each subsequent element, create node, link `current.next = node`, `node.prev = current`, advance current.
4. Set `tail = current` at the end.

**Steps for Insert at Head:**
1. Create new node with `val`.
2. Set `newNode.next = head`.
3. If head exists, set `head.prev = newNode`.
4. Update `head = newNode`.

**Steps for Insert at Tail:**
1. Create new node with `val`.
2. Set `newNode.prev = tail`.
3. If tail exists, set `tail.next = newNode`.
4. Update `tail = newNode`.

**Steps for Delete at Head:**
1. If head is null, return.
2. Move `head = head.next`.
3. If new head exists, set `head.prev = null`.
4. Else tail = null (list is now empty).

**Steps for Delete at Tail:**
1. If tail is null, return.
2. Move `tail = tail.prev`.
3. If new tail exists, set `tail.next = null`.
4. Else head = null (list is now empty).

**BUD Analysis:**
- **B**ottleneck: Insert/delete at given position requires O(n) traversal to find position.
- **U**nnecessary work: None for head/tail ops -- they are O(1).
- **D**uplicate work: None.

**Dry Run Trace (Build from [1, 2, 3]):**
```
Step 1: head = Node(1)          List: null <- 1 -> null
Step 2: new = Node(2)           List: null <- 1 <-> 2 -> null
        1.next = 2, 2.prev = 1
Step 3: new = Node(3)           List: null <- 1 <-> 2 <-> 3 -> null
        2.next = 3, 3.prev = 2
        tail = Node(3)

Insert at Head (0):
        new = Node(0), 0.next = 1, 1.prev = 0, head = 0
        List: null <- 0 <-> 1 <-> 2 <-> 3 -> null

Delete at Tail:
        tail = Node(2), 2.next = null
        List: null <- 0 <-> 1 <-> 2 -> null

Forward:  0 -> 1 -> 2
Backward: 2 -> 1 -> 0
```

### Approach 2: With Sentinel Nodes
**Intuition:** Use dummy head and dummy tail nodes to eliminate null checks. Every real node sits between the sentinels.

**Steps:**
1. Create `dummyHead` and `dummyTail`, link them: `dummyHead.next = dummyTail`, `dummyTail.prev = dummyHead`.
2. Insert always happens between two existing nodes -- no null checks needed.
3. Delete always has valid prev/next -- no edge case for head/tail.

*This is the technique used in LRU Cache (LeetCode #146).*

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Build from array | O(n) | O(n) | One pass, n nodes created |
| Insert at head/tail | O(1) | O(1) | Just pointer updates |
| Insert at position k | O(k) | O(1) | Walk k nodes, then O(1) insert |
| Delete at head/tail | O(1) | O(1) | Just pointer updates |
| Delete at position k | O(k) | O(1) | Walk k nodes, then O(1) delete |
| Forward/Backward traverse | O(n) | O(1) | Visit each node once |

*"Head/tail operations are instant because you have direct references. Positional operations pay the cost of walking there."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list | head and tail are null | Check before any operation |
| Single node | head == tail | After delete, both become null |
| Insert at position 0 | Same as insert at head | Redirect to insertAtHead |
| Insert at position = size | Same as insert at tail | Redirect to insertAtTail |
| Delete from empty list | Nothing to delete | Return early or throw |

**Common Mistakes:**
- Forgetting to update `prev` pointer -- list breaks on backward traversal.
- Not updating `tail` when deleting the last node.
- Off-by-one errors in positional insert/delete.
- Not handling the transition from 1 element to 0 elements (both head and tail must become null).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **DLL vs SLL tradeoff?** | DLL uses more memory (extra pointer per node) but enables O(1) delete-by-reference and backward traversal. |
| **Why sentinel nodes?** | Eliminates all null checks, making code cleaner and less error-prone. Used in production (e.g., Linux kernel linked lists). |
| **Real-world usage?** | Browser history (back/forward), undo/redo stacks, LRU cache, text editor cursor movement. |
| **Follow-up: Implement LRU Cache** | Use DLL + HashMap. DLL gives O(1) move-to-front, HashMap gives O(1) lookup. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| LRU Cache (LC #146) | DLL is the backbone -- move recently used to front in O(1) |
| Flatten a Multilevel DLL (LC #430) | DLL traversal with child pointers |
| Browser Back/Forward | Classic DLL application |
| Singly Linked List | Subset of DLL -- compare tradeoffs |
| Deque Implementation | DLL enables O(1) operations at both ends |
