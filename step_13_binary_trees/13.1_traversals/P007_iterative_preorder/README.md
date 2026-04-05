# Iterative Preorder Traversal

> **Batch 3 of 12** | **Topic:** Binary Trees -- Traversals | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given the root of a binary tree, return its **preorder traversal** (Root -> Left -> Right) using an **explicit stack** -- no recursion allowed. Return the values as a list.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,null,2,3]` | `[1,2,3]` | Visit 1 (root), no left, visit 2, visit 3 (left of 2) |
| `[1,2,3,4,5]` | `[1,2,4,5,3]` | Root 1, left subtree [2,4,5] fully before right 3 |
| `[]` | `[]` | Empty tree |
| `[1]` | `[1]` | Single node |

### Analogy
Imagine exploring a cave system. At each fork, you jot down your current room (process it immediately), then go left first. But before going left, you drop a breadcrumb (push onto stack) for the right path so you can come back to it. When you hit a dead end, you pick up your most recent breadcrumb and explore that right path.

### 3 Key Observations
1. **"Aha" -- Process BEFORE pushing children:** In preorder, you record the node's value the moment you see it (root first). This is unlike inorder where you must go left before processing.
2. **"Aha" -- Push right first, then left:** Since a stack is LIFO, pushing right before left ensures left is popped (and explored) first. This mirrors the "left before right" requirement.
3. **"Aha" -- The stack replaces the call stack:** Every recursive call saves state on the system call stack. Here, we manually manage that same state with an explicit stack. The logic is identical -- just the mechanism changes.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion (call stack) | Recursive DFS | Simple baseline, but uses recursion |
| Optimal | Explicit Stack | Iterative DFS | Stack simulates recursion explicitly |
| Best | Morris Traversal | Threaded tree | O(1) space by temporarily modifying tree |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursive Preorder
**Intuition:** The natural recursive solution. Process current node, recurse left, recurse right. This IS preorder, but the task asks for iterative -- so this is our baseline to understand the pattern we'll replicate.

**Steps:**
1. If node is null, return.
2. Add node's value to result.
3. Recurse on left child.
4. Recurse on right child.

**Dry-run trace** with `[1,2,3,4,5]`:
```
preorder(1): add 1, go left
  preorder(2): add 2, go left
    preorder(4): add 4, left=null, right=null
  back to 2, go right
    preorder(5): add 5, left=null, right=null
back to 1, go right
  preorder(3): add 3, left=null, right=null
Result: [1,2,4,5,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node once |
| Space | O(h) -- recursion stack depth (h = tree height) |

---

### Approach 2: Optimal -- Iterative with Explicit Stack
**Intuition:** Replace the implicit call stack with an explicit stack. Pop a node, process it (add to result), then push its right child first, then left child (so left is processed next due to LIFO).

**BUD Optimization:**
- **B**ottleneck: Recursion depth can cause stack overflow for skewed trees. Explicit stack avoids this.
- **U**nnecessary: No extra work -- each node is pushed and popped exactly once.

**Steps:**
1. If root is null, return empty list.
2. Push root onto stack.
3. While stack is not empty:
   - Pop node, add its value to result.
   - If node has right child, push it.
   - If node has left child, push it.
4. Return result.

**Dry-run trace** with `[1,2,3,4,5]`:
```
Stack: [1]
Pop 1 -> result=[1]. Push right=3, push left=2. Stack: [3,2]
Pop 2 -> result=[1,2]. Push right=5, push left=4. Stack: [3,5,4]
Pop 4 -> result=[1,2,4]. No children. Stack: [3,5]
Pop 5 -> result=[1,2,4,5]. No children. Stack: [3]
Pop 3 -> result=[1,2,4,5,3]. No children. Stack: []
Done -> [1,2,4,5,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node pushed and popped once |
| Space | O(h) -- stack holds at most O(h) nodes at once |

---

### Approach 3: Best -- Morris Preorder Traversal
**Intuition:** Achieve O(1) extra space by temporarily threading the tree. For each node with a left child, find its inorder predecessor (rightmost node in left subtree) and create a temporary link back. This lets you "return" to the current node after finishing the left subtree without a stack.

**Steps:**
1. Set `curr = root`.
2. While `curr` is not null:
   - If `curr` has no left child: add `curr.val` to result, move to `curr.right`.
   - Else: find inorder predecessor of `curr` (rightmost in left subtree).
     - If predecessor's right is null: set `predecessor.right = curr`, add `curr.val` to result, move to `curr.left`.
     - If predecessor's right is `curr`: remove the thread (`predecessor.right = null`), move to `curr.right`.
3. Return result.

**Key difference from Morris Inorder:** In preorder, you add the node to result when you FIRST visit it (when creating the thread), not when you revisit it.

**Dry-run trace** with `[1,2,3]` (2 is left of 1, 3 is right of 1):
```
curr=1, has left=2. Predecessor of 1 = 2 (rightmost in left subtree).
  2.right is null -> thread 2.right=1, add 1 to result. Move to left: curr=2.
curr=2, no left child. Add 2 to result. Move to right: curr=1 (via thread).
curr=1, has left=2. Predecessor = 2. 2.right == 1 (thread exists).
  Remove thread: 2.right=null. Move to right: curr=3.
curr=3, no left child. Add 3 to result. Move to right: curr=null.
Done -> [1,2,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each edge traversed at most 3 times |
| Space | O(1) -- no stack, only pointers (output list not counted) |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** Every node must be visited exactly once to collect its value. No shortcuts possible.

**Why stack approach uses O(h) space:** At any point, the stack holds nodes along the path from root to current position, plus their right siblings waiting to be explored. This is bounded by the height of the tree.

**Why Morris is O(1) space:** Instead of storing "where to go back" in a stack, it embeds that information directly in the tree structure (temporary threads). After traversal, the tree is restored to its original form.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `[]` | Check for null root first |
| Single node | `[val]` | Push/pop once, done |
| Left-skewed tree (linked list) | Stack grows to O(n) | Worst case for stack approach |
| Right-skewed tree | Stack always has 1 element | Best case for stack space |
| Complete binary tree | O(log n) stack depth | Balanced is the typical case |

### Common Mistakes
- Pushing left before right on the stack (reverses the traversal order).
- In Morris: forgetting to restore the tree (removing threads) -- corrupts the structure.
- In Morris: confusing when to add to result (preorder: first visit; inorder: second visit).

---

## INTERVIEW LENS

**Frequency:** Very high -- fundamental tree traversal question.
**Follow-ups the interviewer might ask:**
- "Now do iterative inorder" (trickier -- must go all the way left before processing)
- "Now do iterative postorder" (hardest -- two stacks or modified preorder reversed)
- "Can you do O(1) space?" (Morris traversal)
- "What's the relationship between iterative pre/in/post order?" (Same skeleton, different processing point)

**What they're really testing:** Do you truly understand how recursion maps to stack operations? Can you manually manage state?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Iterative Inorder | Same stack technique, but push-all-left-first pattern |
| Iterative Postorder | Two-stack approach or reverse-modified preorder |
| Morris Traversal (Inorder) | Same threading concept, different add-to-result timing |
| Binary Tree Level Order | BFS alternative using queue instead of stack |

### Real-World Use Case
**File system traversal:** Operating systems traverse directory trees iteratively (not recursively) to avoid stack overflow on deeply nested directories. The `find` command and `os.walk()` in Python use iterative approaches internally, maintaining an explicit stack/queue of directories to visit.
