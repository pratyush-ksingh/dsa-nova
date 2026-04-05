# Preorder Traversal

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #144**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return its **preorder** traversal as a list. Preorder means: **Root, Left, Right** -- visit the current node first, then the left subtree, then the right subtree.

### Analogy
Think of **exploring a building** floor by floor. When you enter a room (root), you first note what is in it, then explore the left hallway, then the right hallway. Preorder is "process first, explore later" -- you always record the current room before diving deeper. This is why preorder is used to **serialize trees** (the root comes first, giving structure info upfront).

### Key Observations
1. Preorder visits the **root before its subtrees**. **Aha:** The first element of a preorder sequence is always the root. This property is essential for tree reconstruction from traversals.
2. Recursive implementation is trivial: visit, recurse left, recurse right. **Aha:** Just swap the order from inorder (move the visit before the left recurse).
3. Iterative preorder is the **easiest** iterative traversal: push root, pop and visit, push right then left (so left is processed first). **Aha:** Simpler than iterative inorder because we process the node immediately on popping.

### Examples

```
    1            Preorder: [1,2,3]
     \
      2
     /
    3
```

```
      1          Preorder: [1,2,4,5,3,6]
     / \
    2   3
   / \   \
  4   5   6
```

| Input (tree) | Output |
|-------------|--------|
| [] | [] |
| [1] | [1] |
| [1,null,2,3] | [1,2,3] |
| [1,2,3,4,5,null,6] | [1,2,4,5,3,6] |

### Constraints
- 0 <= number of nodes <= 100
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive) | Call stack | Direct Root-L-R definition. |
| Optimal (iterative with stack) | Explicit stack | Pop, visit, push right then left. Simplest iterative traversal. |
| Best (Morris traversal) | Threaded tree | O(1) extra space. |

**Pattern cue:** Preorder = "do something at each node before children." Think DFS with early processing.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive
**Intuition:** Visit the node, then recurse left, then recurse right.

**Steps:**
1. If node is null, return.
2. Add `node.val` to result.
3. Recurse on `node.left`.
4. Recurse on `node.right`.

**Dry-Run Trace -- Tree [1,2,3,4,5,null,6]:**

| Call | Node | Action | Result |
|------|------|--------|--------|
| pre(1) | 1 | add 1, go left | [1] |
| pre(2) | 2 | add 2, go left | [1,2] |
| pre(4) | 4 | add 4, left=null, right=null | [1,2,4] |
| back to 2 | 2 | go right | |
| pre(5) | 5 | add 5, left=null, right=null | [1,2,4,5] |
| back to 1 | 1 | go right | |
| pre(3) | 3 | add 3, left=null, go right | [1,2,4,5,3] |
| pre(6) | 6 | add 6 | [1,2,4,5,3,6] |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) recursion stack |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Same as inorder -- recursion depth can cause stack overflow. Iterative version gives explicit control.

### Approach 2 -- Iterative with Stack
**Intuition:** Use a stack. Pop a node, visit it immediately, then push right child first, then left child (so left is popped and processed first).

**Steps:**
1. Push root onto stack.
2. While stack is not empty:
   - Pop node, add `node.val` to result.
   - If node.right exists, push it.
   - If node.left exists, push it.

**Dry-Run Trace -- Tree [1,2,3]:**

| Step | Pop | Stack After Push | Result |
|------|-----|-----------------|--------|
| 1 | 1 | [3, 2] | [1] |
| 2 | 2 | [3] | [1,2] |
| 3 | 3 | [] | [1,2,3] |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) stack space |

### Approach 3 -- Best (Morris Preorder)
**Intuition:** Similar to Morris inorder, but visit the node when creating the thread (not when removing it).

**Steps:**
1. While current is not null:
   - If no left child: visit current, go right.
   - Else: find inorder predecessor.
     - If predecessor.right is null: visit current, create thread, go left.
     - If predecessor.right is current: remove thread, go right.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) extra |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n):** Visit each node exactly once.
- **O(h) stack:** At most one node per level on the stack. Iterative preorder is particularly elegant because the stack never holds more than the width of the tree at any frontier.
- **O(1) Morris:** No stack; tree structure serves as the "stack."

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return [] |
| Single node | Return [val] |
| Left-only tree | Preorder = top to bottom left |
| Right-only tree | Preorder = top to bottom right |

**Common mistakes:**
- Iterative: pushing left before right (would give Root-R-L instead of Root-L-R).
- Morris preorder: visiting at thread removal instead of thread creation.
- Confusing preorder (Root-L-R) with inorder (L-Root-R).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Why is preorder useful? | Tree serialization: root first makes deserialization straightforward. |
| Preorder + Inorder = unique tree? | Yes. Preorder tells you the root, inorder tells you left/right split. |
| DFS vs preorder? | DFS is the general pattern; preorder is a specific DFS ordering. |
| Preorder on a graph? | Not meaningful. Preorder is tree-specific due to the unique root and acyclic structure. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Inorder Traversal (LC #94) | L-Root-R; compare with Root-L-R |
| Postorder Traversal (LC #145) | L-R-Root; reverse of a modified preorder |
| Construct Tree from Pre+In (LC #105) | Uses preorder's root-first property |
| Serialize/Deserialize (LC #297) | Preorder serialization is common |
| Flatten BT to Linked List (LC #114) | Preorder gives the flattened order |
