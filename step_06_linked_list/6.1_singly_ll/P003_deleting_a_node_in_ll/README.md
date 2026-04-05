# Deleting a Node in LL

> **Batch 2 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a singly linked list, implement three deletion operations:
1. **Delete from Beginning** -- Remove the head node.
2. **Delete from End** -- Remove the last node.
3. **Delete at a Given Position** -- Remove the node at position `k` (1-indexed).

### Analogy
Think of a **chain of paper clips**. To remove the first clip, just grab the second one -- it becomes the new start. To remove the last clip, walk to the second-to-last clip and unhook it from the final one. To remove a clip in the middle, find the clip just before it, reach past the target, and reconnect to the one after it. The target clip falls away.

### Key Observations
1. **Deleting from the beginning is O(1).** Just move the head forward. *Aha: This is why singly linked lists excel at stack operations -- pop from head is instant.*
2. **Deleting from the end requires finding the second-to-last node.** You must traverse to `n-1` because there is no `prev` pointer. *Aha: This O(n) cost is the key weakness of singly LL vs doubly LL.*
3. **Deleting at position k means stopping at position k-1.** You need the node BEFORE the target to rewire the `next` pointer. *Aha: The "stop one before" pattern appears everywhere in linked list deletion and insertion.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List |
| **Why** | We are learning the fundamental delete operations on the simplest linked list |
| **Pattern cue** | "Remove from head" = O(1); "Remove from tail or middle" = traverse to predecessor |

---

## 3. APPROACH LADDER

### Approach 1: Delete from Beginning
**Intuition:** The head pointer simply moves to `head.next`. The old head is garbage collected.

**Steps:**
1. If list is empty, return null.
2. Save `head.next` as `newHead`.
3. (Optional) set `head.next = null` to help GC.
4. Return `newHead`.

### Approach 2: Delete from End
**Intuition:** Walk to the second-to-last node, then set its `next` to null.

**Steps:**
1. If list is empty, return null.
2. If list has one node, return null (list becomes empty).
3. Traverse until `current.next.next == null` (current is second-to-last).
4. Set `current.next = null`.
5. Return head.

### Approach 3: Delete at Given Position (1-indexed)
**Intuition:** Traverse to position `k-1`, then skip over the k-th node.

**Steps:**
1. If position is 1, delete from beginning (return `head.next`).
2. Traverse `k-2` steps to reach the node before position `k`.
3. If the target exists: `prev.next = prev.next.next`.
4. Return head.

**BUD Analysis:**
- **B**ottleneck: Must traverse to find predecessor -- cannot be avoided in singly LL.
- **U**nnecessary work: None.
- **D**uplicate work: None.

**Dry Run Trace (Delete at position 3):**
```
List: 10 -> 20 -> 30 -> 40 -> 50       position = 3

Step 1: position != 1, so traverse.
Step 2: Start at head (10). Move 1 step to 20. (k-2 = 1 step)
        prev = node(20), prev.next = node(30) <-- target
Step 3: prev.next = prev.next.next = node(40)

Result: 10 -> 20 -> 40 -> 50
        node(30) is disconnected and garbage collected.
```

---

## 4. COMPLEXITY INTUITIVELY

| Operation | Time | Space | Why |
|-----------|------|-------|-----|
| Delete from beginning | O(1) | O(1) | Just move head pointer |
| Delete from end | O(n) | O(1) | Must find second-to-last node |
| Delete at position k | O(k) | O(1) | Must traverse k-1 nodes to find predecessor |

*"Deletion itself is O(1) -- it is just a pointer reassignment. The cost comes from finding the right node to delete, which requires traversal in a singly linked list."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Empty list | Nothing to delete | Return null |
| Single node, delete head | List becomes empty | Return null |
| Single node, delete tail | Same as delete head | Return null |
| Position out of bounds | Target does not exist | Return list unchanged or handle gracefully |
| Position = 1 | Delete head | Special case: return head.next |

**Common Mistakes:**
- Not handling the "delete head" case separately -- trying to find `prev` of head causes null pointer.
- Off-by-one in traversal -- stopping too early or too late.
- Forgetting to return the new head when the head itself is deleted.
- Not checking if `prev.next` is null before accessing `prev.next.next`.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why is delete-tail O(n) in singly LL?** | No prev pointer means we must traverse from head to find the second-to-last node. Doubly LL fixes this. |
| **What about LC #237 (Delete Node)?** | There you are given the node itself (not head). Copy next node's value and skip it. O(1) but cannot delete the tail. |
| **Sentinel/dummy node trick?** | Prepend a dummy node before head. This eliminates the "delete head" special case since every real node has a predecessor. |
| **Memory leak concern?** | In languages without GC (C/C++), you must explicitly free the deleted node. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Delete Node in LL (LC #237) | Given node directly -- different technique (value copy) |
| Remove Nth from End (LC #19) | Two-pointer technique to find nth-from-end |
| Delete in Doubly LL | Same logic but O(1) for delete-given-node thanks to prev pointer |
| Remove Linked List Elements (LC #203) | Delete all nodes with a given value |
| Insert in Singly LL | Mirror operation -- same traversal pattern |
