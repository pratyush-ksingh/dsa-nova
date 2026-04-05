# Inorder Traversal

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #94**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return its **inorder** traversal as a list. Inorder means: **Left, Root, Right** -- visit the left subtree first, then the current node, then the right subtree.

### Analogy
Imagine reading a **book's table of contents** where each chapter has subsections. Inorder is like reading the leftmost subsection first, then the chapter title, then the right subsection. For a BST, this produces values in **sorted ascending order** -- like reading a phone book from A to Z.

### Key Observations
1. Inorder on a **BST** gives a sorted sequence. **Aha:** This is the most important property of inorder traversal -- it is the natural way to read BST data in order.
2. The recursive approach directly mirrors the definition: recurse left, visit node, recurse right. **Aha:** Base case is null node; recursion depth = tree height.
3. The iterative approach uses an explicit stack to simulate the call stack. **Aha:** Push all left children onto the stack first, then process, then go right. This is the standard iterative DFS pattern.

### Examples

```
    1            Inorder: [1,3,2]
     \
      2
     /
    3
```

```
      4          Inorder: [2,4,5,6,7,8]
     / \
    2   6
     \ / \
     5 7  8
```

| Input (tree) | Output |
|-------------|--------|
| [] | [] |
| [1] | [1] |
| [1,null,2,3] | [1,3,2] |
| [4,2,6,null,5,7,8] | [2,5,4,7,6,8] |

### Constraints
- 0 <= number of nodes <= 100
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive) | Call stack | Direct translation of L-Root-R definition. Simplest. |
| Optimal (iterative with stack) | Explicit stack | Avoids recursion overhead; same O(n) time, O(h) space. |
| Best (Morris traversal) | Threaded tree (no extra space) | O(1) extra space by temporarily modifying tree pointers. |

**Pattern cue:** "Tree traversal" -> recursion or stack-based iteration. Morris for O(1) space.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Recursive)
**Intuition:** Recursion naturally follows the L-Root-R definition.

**Steps:**
1. If node is null, return.
2. Recurse on `node.left`.
3. Add `node.val` to result.
4. Recurse on `node.right`.

**Dry-Run Trace -- Tree [1,null,2,3]:**
```
    1
     \
      2
     /
    3
```

| Call | Node | Action | Result |
|------|------|--------|--------|
| inorder(1) | 1 | go left -> null, return | |
| | 1 | add 1 | [1] |
| | 1 | go right -> inorder(2) | |
| inorder(2) | 2 | go left -> inorder(3) | |
| inorder(3) | 3 | left null, add 3, right null | [1,3] |
| inorder(2) | 2 | add 2 | [1,3,2] |
| | 2 | right null | [1,3,2] |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) recursion stack, worst O(n) |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Recursion uses implicit call stack. For very deep trees, this risks stack overflow. Using an explicit stack gives the same performance with more control.

### Approach 2 -- Optimal (Iterative with Stack)
**Intuition:** Use a stack. Keep pushing left children. When you cannot go left, pop, visit, then go right.

**Steps:**
1. Initialize `stack = []`, `current = root`.
2. While current is not null OR stack is not empty:
   - Push current and go left until null.
   - Pop from stack, add val to result.
   - Set current to popped node's right child.

**Dry-Run Trace -- Tree [1,null,2,3]:**

| Step | current | Stack | Action | Result |
|------|---------|-------|--------|--------|
| 1 | 1 | [] | push 1, go left | |
| 2 | null | [1] | pop 1, add 1, go right to 2 | [1] |
| 3 | 2 | [] | push 2, go left to 3 | |
| 4 | 3 | [2] | push 3, go left | |
| 5 | null | [2,3] | pop 3, add 3, right=null | [1,3] |
| 6 | null | [2] | pop 2, add 2, right=null | [1,3,2] |
| 7 | null | [] | stack empty, done | [1,3,2] |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) explicit stack |

### Approach 3 -- Best (Morris Traversal)
**Intuition:** Use the tree structure itself for backtracking by creating temporary "threads" (pointers from rightmost node in left subtree back to current node). No stack needed.

**Steps:**
1. While current is not null:
   - If no left child: visit current, go right.
   - Else: find **inorder predecessor** (rightmost node in left subtree).
     - If predecessor's right is null: set it to current (create thread), go left.
     - If predecessor's right is current: remove thread, visit current, go right.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each edge traversed at most twice |
| Space | O(1) extra (modifies tree temporarily) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** Every node is visited exactly once. In Morris, some edges are traversed twice (to find predecessors), but the total work is still O(n).
- **O(h) space for recursive/iterative:** The stack holds at most one node per level. For balanced trees h = log n; skewed h = n.
- **O(1) space for Morris:** No stack at all; uses in-place pointer manipulation.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null root) | Return empty list |
| Single node | Return [node.val] |
| Left-skewed tree | Stack grows to O(n); Morris still O(1) |
| Right-skewed tree | Stack stays small; each node popped immediately |

**Common mistakes:**
- Iterative: forgetting to check `current != null` in the while condition (not just stack non-empty).
- Morris: forgetting to remove the thread (results in infinite loop).
- Confusing inorder (L-Root-R) with preorder (Root-L-R).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Why is inorder special for BSTs? | It produces sorted output. Validates BST if result is strictly increasing. |
| Recursive vs iterative preference? | Recursive is cleaner; iterative avoids stack overflow for deep trees. |
| When to use Morris? | When O(1) space is critical and temporary tree modification is acceptable. |
| Can you do inorder without recursion and without stack? | Yes, Morris traversal. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Preorder Traversal (LC #144) | Root-L-R; same recursive/iterative patterns |
| Postorder Traversal (LC #145) | L-R-Root; trickier iteratively |
| Validate BST (LC #98) | Inorder must be strictly increasing |
| Kth Smallest in BST (LC #230) | Inorder traversal, stop at kth element |
| Flatten BST to Sorted List | Inorder traversal produces the sorted list |
