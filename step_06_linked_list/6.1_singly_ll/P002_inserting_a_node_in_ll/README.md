# Inserting a Node in Linked List

> **Batch 1 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement three insertion operations on a singly linked list:
1. **Insert at the beginning** (before head)
2. **Insert at the end** (after the last node)
3. **Insert at a given position** (1-indexed)

### Analogy
Think of a **queue of people** (linked by hand-holding). Inserting at the beginning means a new person grabs the first person's hand and becomes the new front. Inserting at the end means the last person extends their hand to a newcomer. Inserting in the middle means two adjacent people let go, the newcomer holds both their hands, and the chain is restored.

### Key Observations
1. **Insert at head is O(1).** You just create a node and point it to the old head. *Aha: This is why stacks are naturally implemented with linked lists -- push is always O(1).*
2. **Insert at tail is O(n) without a tail pointer.** You must traverse to the end. *Aha: Maintaining a tail pointer makes this O(1), but adds complexity to every operation that might change the tail.*
3. **Insert at position k requires walking k-1 steps.** You need the node BEFORE the insertion point. *Aha: This is the key insight -- you stop one node early so you can manipulate its `next` pointer.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List |
| **Algorithm** | Iterative traversal + pointer manipulation |
| **Why** | Direct pointer updates are the natural way to modify linked structures |
| **Pattern cue** | "Insert/modify linked list" -- think about which node's `next` pointer needs to change. |

---

## 3. APPROACH LADDER

### Approach 1: Three Separate Methods
**Intuition:** Implement each insertion type as its own method with clear pointer manipulation.

**Insert at Beginning:**
1. Create `newNode` with given value.
2. Set `newNode.next = head`.
3. Update `head = newNode`.
4. Return new head.

**Insert at End:**
1. Create `newNode` with given value.
2. If list is empty, return newNode as head.
3. Traverse to the last node (`current.next != null`).
4. Set `lastNode.next = newNode`.
5. Return head (unchanged).

**Insert at Position k (1-indexed):**
1. If k == 1, insert at beginning.
2. Traverse to node at position k-1.
3. Set `newNode.next = prev.next`.
4. Set `prev.next = newNode`.
5. Return head.

**BUD Analysis:**
- **B**ottleneck: Insert at end/position requires traversal -- O(n) worst case.
- **U**nnecessary work: Could maintain tail pointer to avoid O(n) tail insertion.
- **D**uplicate work: None.

**Dry Run Trace:**
```
Initial list: 1 -> 2 -> 4 -> 5

Insert 0 at beginning:
  new(0).next = 1
  head = new(0)
  Result: 0 -> 1 -> 2 -> 4 -> 5

Insert 6 at end:
  Walk: 0 -> 1 -> 2 -> 4 -> 5 (5.next == null, stop)
  5.next = new(6)
  Result: 0 -> 1 -> 2 -> 4 -> 5 -> 6

Insert 3 at position 4:
  Walk to position 3: 0 -> 1 -> 2 (this is node at index 3)
  new(3).next = 2.next = 4
  2.next = new(3)
  Result: 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6
```

### Approach 2: Unified Insert with Sentinel
**Intuition:** Use a dummy head node to unify all cases. Insert at position k always means "walk k-1 steps from dummy, then insert after."

**Steps:**
1. Create dummy node, `dummy.next = head`.
2. Walk to position k-1 starting from dummy.
3. Insert after that node.
4. Return `dummy.next` as new head.

*This eliminates the special case for insert-at-head.*

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Insert at head | O(1) | O(1) | Just two pointer assignments |
| Insert at tail | O(n) | O(1) | Must walk entire list to find end |
| Insert at position k | O(k) | O(1) | Walk k-1 nodes, then O(1) insert |

*"Creating a node is O(1). The cost is getting to the right place. At head = free. At tail = walk the whole list. At position k = walk k steps."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list (head = null) | Insert becomes the head | Return new node as head |
| Position = 1 | Insert at head | Redirect to insertAtHead |
| Position > length + 1 | Invalid position | Return head unchanged or throw error |
| Position = length + 1 | Insert at tail | Traversal stops at last node, append |

**Common Mistakes:**
- Off-by-one: stopping at the wrong node (need to stop at the node BEFORE insertion point).
- Forgetting to return the new head after insert at beginning.
- Not handling empty list in insertAtEnd (causes NullPointerException).
- Setting `prev.next = newNode` before `newNode.next = prev.next` -- loses the rest of the list.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Order of pointer updates?** | Always set `newNode.next` FIRST, then `prev.next = newNode`. Reversing this loses the tail of the list. |
| **How to make insert-at-tail O(1)?** | Maintain a tail pointer. Update it on every insert at tail. |
| **Sentinel node approach?** | A dummy head eliminates the special case for empty list / insert at head. Cleaner code. |
| **Thread safety?** | In concurrent environments, insertion requires locking or CAS operations to prevent race conditions. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Delete a Node in LL | Inverse operation -- same traversal-to-position pattern |
| Insert in Sorted List | Insert at position, but position is determined by value comparison |
| Doubly Linked List Insert | Same logic but must update prev pointers too |
| Add Two Numbers (LC #2) | Builds a new list node by node -- essentially repeated insertion |
| Copy List with Random Pointer (LC #138) | Complex insertion + pointer manipulation |
