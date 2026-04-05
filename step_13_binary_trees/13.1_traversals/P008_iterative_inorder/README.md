# Iterative Inorder Traversal

> **Batch 3 of 12** | **Topic:** Binary Trees -- Traversals | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given the root of a binary tree, return its **inorder traversal** (Left -> Root -> Right) using an **explicit stack** -- no recursion allowed. Return the values as a list.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,null,2,3]` | `[1,3,2]` | Go left from 1 (null), visit 1, go right to 2, go left to 3, visit 3, visit 2 |
| `[1,2,3,4,5]` | `[4,2,5,1,3]` | Full left descent to 4, then unwind up through 2, 5, 1, 3 |
| `[]` | `[]` | Empty tree |
| `[1]` | `[1]` | Single node |

### Analogy
Imagine reading a book's table of contents that's organized as a tree. You must read every section in sorted order (for a BST, inorder gives sorted output). You can't just read what you see -- you must dive to the deepest left section first, read it, come back up, read the parent, then dive into the right section. Your bookmark stack tracks where you need to return to.

### 3 Key Observations
1. **"Aha" -- Push ALL left nodes first:** Unlike preorder (pop and process), inorder requires you to keep going left without processing. You push the entire left spine onto the stack before processing anything.
2. **"Aha" -- Process happens on POP, not PUSH:** In preorder, you process when you first see a node. In inorder, you process only when you pop (after all left descendants are done). This is the fundamental difference.
3. **"Aha" -- After processing, go right:** Once you pop and process a node, set `curr = node.right`. The outer loop's "push all left" phase will then handle the right subtree's left spine.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion (call stack) | Recursive DFS | Simple baseline |
| Optimal | Explicit Stack | Iterative "push-left" pattern | O(h) space, no recursion |
| Best | Morris Traversal | Threaded tree | O(1) space |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursive Inorder
**Intuition:** The natural recursive solution: go left, process current, go right.

**Steps:**
1. If node is null, return.
2. Recurse on left child.
3. Add node's value to result.
4. Recurse on right child.

**Dry-run trace** with `[1,2,3,4,5]`:
```
inorder(1) -> inorder(2) -> inorder(4) -> left null, add 4, right null
  back to 2: add 2
  inorder(5) -> left null, add 5, right null
back to 1: add 1
inorder(3) -> left null, add 3, right null
Result: [4,2,5,1,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- recursion depth |

---

### Approach 2: Optimal -- Iterative "Push All Left" Pattern
**Intuition:** Maintain a `curr` pointer and a stack. Keep pushing `curr` and going left until null. Then pop, process, and go right. The two-phase loop (push left / pop and go right) captures the exact same behavior as the recursive call stack.

**BUD Optimization:**
- **B**ottleneck: Recursion depth for skewed trees. Explicit stack gives control.
- **U**nnecessary: No wasted work; each node pushed once, popped once.

**Steps:**
1. Initialize `curr = root`, create empty stack.
2. While `curr != null` OR stack is not empty:
   - While `curr != null`: push `curr`, move `curr = curr.left` (push all left).
   - Pop node from stack, add value to result.
   - Set `curr = node.right`.
3. Return result.

**Dry-run trace** with `[1,2,3,4,5]`:
```
curr=1: push 1, go left. curr=2: push 2, go left. curr=4: push 4, go left. curr=null.
Stack: [1,2,4]
Pop 4 -> result=[4]. curr=4.right=null.
Pop 2 -> result=[4,2]. curr=2.right=5.
  Push 5, go left. curr=null.
  Pop 5 -> result=[4,2,5]. curr=5.right=null.
Pop 1 -> result=[4,2,5,1]. curr=1.right=3.
  Push 3, go left. curr=null.
  Pop 3 -> result=[4,2,5,1,3]. curr=3.right=null.
Stack empty, curr=null. Done -> [4,2,5,1,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node pushed and popped once |
| Space | O(h) -- at most height nodes on stack |

---

### Approach 3: Best -- Morris Inorder Traversal
**Intuition:** Eliminate the stack entirely by temporarily threading the tree. For each node with a left child, find its inorder predecessor and create a temporary right pointer back to the current node. This "thread" lets you return to the current node after traversing its left subtree.

**Steps:**
1. Set `curr = root`.
2. While `curr` is not null:
   - If `curr` has no left child: add `curr.val` to result, move right.
   - Else: find predecessor (rightmost node in left subtree).
     - If predecessor's right is null: create thread (`predecessor.right = curr`), move left.
     - If predecessor's right is curr: remove thread, add `curr.val` to result, move right.
3. Return result.

**Key difference from Morris Preorder:** In inorder, you add the node to result on the SECOND visit (when the thread is found), not the first visit.

**Dry-run trace** with `[1,2,3]`:
```
curr=1, left=2. Predecessor=2 (rightmost in left subtree of 1).
  2.right is null -> thread: 2.right=1. Move left: curr=2.
curr=2, no left. Add 2 to result. Move right: curr=1 (via thread).
curr=1, left=2. Predecessor=2. 2.right==1 (thread found).
  Remove thread. Add 1 to result. Move right: curr=3.
curr=3, no left. Add 3 to result. Move right: curr=null.
Done -> [2,1,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each edge traversed at most 3 times |
| Space | O(1) -- no stack, only pointers |

---

## COMPLEXITY INTUITIVELY

**Why the "push all left" pattern gives O(h) space:** At any moment, the stack contains nodes along the current path from root to the deepest left descendant. Once you start popping and going right, the stack shrinks. The maximum stack size equals the tree height.

**Why Morris is O(n) time despite finding predecessors:** Finding each predecessor takes O(h) in the worst case, but the TOTAL work across all predecessor-finding steps is O(n). Each edge is traversed at most twice (once to find predecessor, once to remove thread). By amortized analysis, it's O(n) total.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `[]` | Both curr and stack are empty initially |
| Single node | `[val]` | Push, pop, done |
| Left-skewed (linked list) | Stack grows to O(n) | Entire left spine pushed at once |
| Right-skewed | Stack always has 1 | Each node: push, pop, go right |
| BST input | Sorted output | Inorder of BST = sorted; good validation |

### Common Mistakes
- Using the preorder pattern (pop and process immediately) -- that's wrong for inorder.
- Forgetting the outer `while` needs both conditions: `curr != null OR stack not empty`.
- In Morris: adding to result on first visit instead of second (that gives preorder, not inorder).
- Not handling `curr = node.right` after popping -- this is what advances the traversal.

---

## INTERVIEW LENS

**Frequency:** Very high -- often paired with or followed by iterative preorder.
**Follow-ups the interviewer might ask:**
- "What's the difference between iterative preorder and inorder?" (When you process: on push vs on pop)
- "Can you do Morris traversal?" (O(1) space)
- "If the tree is a BST, what does inorder give you?" (Sorted order -- key insight)
- "Can you find kth smallest element iteratively?" (Stop inorder at kth pop)

**What they're really testing:** Understanding of the "push all left" pattern and how it maps to recursion's implicit behavior.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Iterative Preorder | Same stack, but process on push, not pop |
| Kth Smallest in BST (LC #230) | Early-exit inorder traversal |
| Validate BST (LC #98) | Inorder should be strictly increasing |
| BST Iterator (LC #173) | Literally this algorithm, paused between next() calls |
| Morris Inorder | O(1) space version of this problem |

### Real-World Use Case
**Database index scan:** B-trees (used in MySQL, PostgreSQL) store data in a tree structure. A range query like `SELECT * FROM users WHERE age BETWEEN 20 AND 30` performs an inorder traversal of the B-tree index. The database engine uses an iterative approach (not recursive) for efficiency and to support cursor-based pagination -- exactly the same "push left, pop and go right" pattern.
