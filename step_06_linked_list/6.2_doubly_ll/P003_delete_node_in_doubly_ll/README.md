# Delete Node in Doubly LL

> **Batch 2 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a doubly linked list, implement three deletion operations:
1. **Delete from Beginning** -- Remove the head node.
2. **Delete from End** -- Remove the tail node.
3. **Delete at a Given Position** -- Remove the node at position `k` (1-indexed).

### Analogy
Think of a **line of train cars** where each car has couplers on both ends (front and back). To detach the first car, uncouple it from the second and the second becomes the engine. To detach the last car, the second-to-last car uncouples its rear hook. To detach a car in the middle, the cars before and after it reach across and couple directly to each other, and the removed car rolls away on a siding.

### Key Observations
1. **Doubly linked list deletion updates at most 4 pointers.** The predecessor's `next` and the successor's `prev` must be rewired, plus you null out the deleted node's pointers. *Aha: Draw the before/after picture -- every DLL deletion is the same 4-pointer pattern.*
2. **Delete from end is O(1) with a tail pointer, O(n) without.** Unlike singly LL, you CAN delete the tail in O(1) if you maintain a tail reference, because `tail.prev` gives you the predecessor. *Aha: This is the core advantage of doubly LL -- backward access eliminates the traversal.*
3. **Deleting a given node reference is O(1).** With both `prev` and `next` pointers available, you can unlink any node without traversal. *Aha: This makes DLL the backbone of LRU Cache where you delete arbitrary cached entries in O(1).*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Doubly Linked List (Node with `val`, `prev`, `next`) |
| **Why** | Bidirectional links allow O(1) deletion of any node given its reference |
| **Pattern cue** | "Delete a given node in O(1)" or "delete from both ends in O(1)" signals doubly linked list |

---

## 3. APPROACH LADDER

### Approach 1: Delete from Beginning
**Intuition:** Move head to `head.next` and sever the backward link.

**Steps:**
1. If list is empty, return null.
2. Save `newHead = head.next`.
3. If `newHead` is not null, set `newHead.prev = null`.
4. Set `head.next = null` (help GC).
5. Return `newHead`.

### Approach 2: Delete from End
**Intuition:** Traverse to the tail (or use a tail pointer), then unlink it using `tail.prev`.

**Steps:**
1. If list is empty, return null.
2. If single node, return null.
3. Find `tail` (last node).
4. Set `tail.prev.next = null`.
5. Set `tail.prev = null` (help GC).
6. Return head.

### Approach 3: Delete at Given Position (1-indexed)
**Intuition:** Traverse to the k-th node, then unlink it by connecting its predecessor and successor directly.

**Steps:**
1. If position is 1, delete from beginning.
2. Traverse to the k-th node.
3. If `node.prev` exists: `node.prev.next = node.next`.
4. If `node.next` exists: `node.next.prev = node.prev`.
5. Null out the deleted node's pointers.
6. Return head.

**BUD Analysis:**
- **B**ottleneck: Traversal to position k takes O(k). Could optimize by traversing from tail if k > n/2 (requires knowing n).
- **U**nnecessary work: None.
- **D**uplicate work: None.

**Dry Run Trace (Delete at position 3):**
```
List: 10 <-> 20 <-> 30 <-> 40 <-> 50     position = 3

Step 1: position != 1, so traverse.
Step 2: Start at head(10). Move to 20, then to 30. target = node(30).
Step 3: node(30).prev = node(20), so node(20).next = node(30).next = node(40)
Step 4: node(30).next = node(40), so node(40).prev = node(30).prev = node(20)
Step 5: node(30).prev = null, node(30).next = null

Result: 10 <-> 20 <-> 40 <-> 50
        node(30) is disconnected.
```

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Delete from beginning | O(1) | O(1) | Only pointer updates |
| Delete from end (with traversal) | O(n) | O(1) | Must find tail |
| Delete from end (with tail pointer) | O(1) | O(1) | Direct access to tail.prev |
| Delete at position k | O(k) | O(1) | Traverse k nodes |
| Delete given node reference | O(1) | O(1) | Direct access to both neighbors |

*"The magic of doubly linked list: if you HAVE the node, deletion is always O(1). The cost is only in FINDING the node."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list | Nothing to delete | Return null |
| Single node | Both head and tail | Return null (list becomes empty) |
| Delete head | Head changes | Return head.next, set new head's prev to null |
| Delete tail | Tail changes | Set tail.prev.next to null |
| Position out of bounds | Target doesn't exist | Return list unchanged |

**Common Mistakes:**
- Forgetting to set `newHead.prev = null` when deleting head -- leaves a dangling backward pointer.
- Not handling the single-node case -- trying to access `tail.prev.next` when `tail.prev` is null.
- Forgetting to check `node.next != null` before setting `node.next.prev` -- crashes on tail deletion.
- Off-by-one: traversing k steps instead of k-1 to reach the k-th node.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why not just use singly LL?** | Singly LL requires O(n) to delete from end or delete a given node. DLL does both in O(1). |
| **Memory overhead?** | One extra pointer per node. On 64-bit systems, 8 extra bytes. For most use cases, this is negligible. |
| **When is DLL essential?** | LRU Cache, text editors (cursor movement), browser history, undo/redo systems. |
| **Sentinel nodes?** | Using dummy head and tail sentinels eliminates all edge cases. The real nodes are always between sentinels. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Insert in Doubly LL | Mirror operation -- same pointer rewiring |
| LRU Cache (LC #146) | Deletion of least-recently-used node in O(1) |
| Design Linked List (LC #707) | Full implementation of all DLL operations |
| Flatten Multilevel DLL (LC #430) | Delete/re-link across levels |
| Delete in Singly LL | Simpler version -- only forward pointers to manage |

---

## Real-World Use Case
**LRU cache eviction:** Redis and Memcached implement LRU caches using a doubly linked list. When the cache is full, the least-recently-used node (tail) is deleted in O(1) by unlinking its `prev` and `next` pointers. This deletion pattern powers every high-performance caching layer on the web.
