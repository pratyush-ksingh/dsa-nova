# Insert Node in Doubly LL

> **Batch 2 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a doubly linked list, implement four insertion operations:
1. **Insert at Beginning** -- Add a new node before the current head.
2. **Insert at End** -- Add a new node after the current tail.
3. **Insert Before a Given Node** -- Insert a new node immediately before a specified node.
4. **Insert After a Given Node** -- Insert a new node immediately after a specified node.

### Analogy
Picture a **conga line** where every dancer holds the shoulders of the person in front AND the person behind. Inserting someone at the front means the new dancer grabs the old leader's shoulders, and the old leader reaches back. Inserting in the middle means two adjacent dancers release their mutual grip, the newcomer steps in, and both dancers reconnect to the newcomer. Two links to update instead of one -- that is the doubly linked list difference.

### Key Observations
1. **Every insertion updates at most 4 pointers.** Two `next` pointers and two `prev` pointers get rewired. *Aha: Draw the "before" and "after" picture and you see exactly which 4 pointers change -- no guesswork needed.*
2. **Insert at beginning may change the head.** The head reference must be updated to point to the new node. *Aha: Unlike singly LL where only `next` is set, here you must also set `old_head.prev = newNode`.*
3. **Insert before/after a given node does NOT require traversal if you already have the node reference.** *Aha: This is the superpower of doubly linked lists -- with a direct reference, insertion is O(1) because you can reach both neighbors instantly.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Doubly Linked List (Node with `val`, `prev`, `next`) |
| **Why** | We need bidirectional traversal for before-node insertion without scanning from head |
| **Pattern cue** | "Insert before" or "delete a given node in O(1)" signals doubly linked list |

---

## 3. APPROACH LADDER

### Approach 1: Insert at Beginning
**Intuition:** Create a new node, make it point forward to the old head, make the old head point backward to the new node, and update the head.

**Steps:**
1. Create `newNode` with the given value.
2. Set `newNode.next = head`.
3. If `head` is not null, set `head.prev = newNode`.
4. Update `head = newNode`.
5. Return new head.

### Approach 2: Insert at End
**Intuition:** Walk to the last node, then attach the new node after it.

**Steps:**
1. Create `newNode` with the given value.
2. If list is empty, `head = newNode`, return.
3. Traverse to the last node (`tail`).
4. Set `tail.next = newNode`.
5. Set `newNode.prev = tail`.
6. Return head (unchanged).

### Approach 3: Insert Before a Given Node
**Intuition:** You have a reference to node `target`. The new node must slip in between `target.prev` and `target`.

**Steps:**
1. Create `newNode` with the given value.
2. Set `newNode.prev = target.prev`.
3. Set `newNode.next = target`.
4. If `target.prev` is not null, set `target.prev.next = newNode`. Otherwise, update `head = newNode`.
5. Set `target.prev = newNode`.
6. Return head.

### Approach 4: Insert After a Given Node
**Intuition:** Slip the new node between `target` and `target.next`.

**Steps:**
1. Create `newNode` with the given value.
2. Set `newNode.next = target.next`.
3. Set `newNode.prev = target`.
4. If `target.next` is not null, set `target.next.prev = newNode`.
5. Set `target.next = newNode`.
6. Return head (unchanged).

**BUD Analysis (applies to all):**
- **B**ottleneck: Insert at end requires O(n) traversal; could be O(1) with a tail pointer.
- **U**nnecessary work: None if you have direct node references.
- **D**uplicate work: None.

**Dry Run Trace (Insert Before):**
```
List: 1 <-> 3 <-> 5        Insert 2 before node(3)

Before:
  node(1).next = node(3)     node(3).prev = node(1)
  node(3).next = node(5)     node(5).prev = node(3)

Step 1: newNode = Node(2)
Step 2: newNode.prev = node(3).prev = node(1)
Step 3: newNode.next = node(3)
Step 4: node(1).next = newNode       (target.prev.next = newNode)
Step 5: node(3).prev = newNode

After:
  1 <-> 2 <-> 3 <-> 5
  node(1).next = node(2)     node(2).prev = node(1)
  node(2).next = node(3)     node(3).prev = node(2)
```

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Insert at beginning | O(1) | O(1) | Only pointer updates, no traversal |
| Insert at end | O(n) | O(1) | Must traverse to find the tail (O(1) with tail pointer) |
| Insert before node | O(1) | O(1) | Direct access via `prev` pointer -- no scanning needed |
| Insert after node | O(1) | O(1) | Direct access via `next` pointer |

*"With a doubly linked list, if you already have the node reference, insertion is always O(1). The only cost is finding the node, which is a separate concern."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list (head = null) | Inserting creates a single-node list | newNode becomes head; prev and next stay null |
| Insert before head | New node becomes the new head | Check `target.prev == null` and update head |
| Insert after tail | New node becomes the new tail | `target.next` is null, so skip `target.next.prev` |
| Single-node list | It is both head and tail | All insertions work if you check null neighbors |

**Common Mistakes:**
- Forgetting to update `head` when inserting before the current head.
- Setting pointers in the wrong order -- causing you to lose reference to the old `prev` or `next`.
- Not handling null checks on `target.prev` or `target.next` -- leads to NullPointerException.
- Forgetting the `prev` pointer entirely (accidentally writing singly-linked logic).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why doubly LL over singly LL?** | Insert-before and delete-given-node become O(1) instead of O(n) because you can access the previous node directly. |
| **Why not always use doubly LL?** | Extra memory per node (one more pointer). For problems that only need forward traversal, singly LL suffices. |
| **What if you maintain a tail pointer?** | Insert at end becomes O(1). LRU Cache does exactly this. |
| **Order of pointer updates?** | Set the new node's pointers first, then update neighbors. This way you never lose access to existing nodes. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Delete Node in Doubly LL | Mirror operation -- same 4 pointer updates in reverse |
| LRU Cache (LC #146) | Doubly LL + HashMap; relies heavily on insert-after and delete-node |
| Flatten a Multilevel DLL (LC #430) | DLL with child pointers; insertion logic is the foundation |
| Browser Forward/Back | Doubly linked list of pages with insert-after on navigation |
| Insert in Singly LL | Same idea but only 2 pointers to manage; simpler but less capable |

---

## Real-World Use Case
**Text editor cursor operations:** In editors like Sublime Text, the undo/redo buffer is a doubly linked list. Inserting a new edit node between the current state and the next state is exactly doubly-linked-list insertion -- rewiring both `prev` and `next` pointers to splice in the new node.
