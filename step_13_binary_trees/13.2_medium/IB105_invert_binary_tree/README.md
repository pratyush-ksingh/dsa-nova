# Invert Binary Tree

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #226**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, **invert** the tree (mirror it) and return its root. Inverting means swapping the left and right children of **every** node in the tree.

### Analogy
Imagine looking at a tree in a **mirror**. Every left branch becomes the right branch and vice versa. If you hold a family tree chart up to a mirror, the entire structure flips. You need to flip at **every** level, not just the root -- it is a recursive mirror operation, like facing two mirrors at each other.

### Key Observations
1. At each node, swap left and right children. Then recursively invert the subtrees. **Aha:** This is a simple preorder (or postorder) operation -- swap first, then recurse (or recurse first, then swap).
2. The operation is in-place: no new tree needed. **Aha:** Just reassign pointers.
3. BFS also works: process each node in the queue and swap its children. **Aha:** Any traversal order works because swapping at each node is independent of other swaps.

### Examples

```
Input:       Output:
     4            4
    / \          / \
   2   7   ->  7   2
  / \ / \     / \ / \
 1  3 6  9   9  6 3  1
```

```
Input:     Output:
   2          2
  / \   ->  / \
 1   3     3   1
```

| Input | Output |
|-------|--------|
| [4,2,7,1,3,6,9] | [4,7,2,9,6,3,1] |
| [2,1,3] | [2,3,1] |
| [1] | [1] |
| [] | [] |

### Constraints
- 0 <= number of nodes <= 100
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute / Optimal (recursive) | Call stack | Swap children, recurse. Cleanest 3-line solution. |
| Optimal alt (iterative BFS) | Queue | Level-by-level swap. Good for avoiding recursion. |
| Best (same as recursive) | Call stack | O(n) is optimal. Recursive is most concise. |

**Pattern cue:** "Transform every node in tree" -> any traversal (pre/post/level) applying the transformation.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive DFS (Preorder Swap)
**Intuition:** At each node, swap left and right children. Then recurse into both subtrees.

**Steps:**
1. If root is null, return null.
2. Swap `root.left` and `root.right`.
3. Recursively invert `root.left` (which was originally right).
4. Recursively invert `root.right` (which was originally left).
5. Return root.

**Dry-Run Trace -- Tree [4,2,7,1,3,6,9]:**

| Call | Node | Before Swap | After Swap | Next Calls |
|------|------|-------------|------------|------------|
| f(4) | 4 | L=2, R=7 | L=7, R=2 | f(7), f(2) |
| f(7) | 7 | L=6, R=9 | L=9, R=6 | f(9), f(6) |
| f(9) | 9 | L=null, R=null | No change | -- |
| f(6) | 6 | L=null, R=null | No change | -- |
| f(2) | 2 | L=1, R=3 | L=3, R=1 | f(3), f(1) |
| f(3) | 3 | L=null, R=null | No change | -- |
| f(1) | 1 | L=null, R=null | No change | -- |

Result: [4,7,2,9,6,3,1].

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node once |
| Space | O(h) recursion stack |

### BUD Transition
Already O(n). BFS provides an iterative alternative.

### Approach 2 -- Iterative BFS
**Intuition:** Use a queue. For each dequeued node, swap its children, then enqueue them.

**Steps:**
1. If root is null, return null.
2. Enqueue root.
3. While queue is not empty:
   - Dequeue node.
   - Swap `node.left` and `node.right`.
   - Enqueue left and right children (if they exist).
4. Return root.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(w) where w = max width |

### Approach 3 -- Best (Recursive DFS)
O(n) is optimal since every node must be visited. Recursive DFS is the most concise at 4 lines.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- O(log n) balanced, O(n) skewed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** Every node needs its children swapped. No node can be skipped.
- **O(h) recursive space:** At most h frames on the stack, one per level.
- **O(w) BFS space:** Queue holds at most one full level.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null) | Return null |
| Single node | Return as-is (no children to swap) |
| Already symmetric | Inversion produces the same tree |
| Left-skewed tree | Becomes right-skewed |
| Perfect binary tree | Clean mirror at every level |

**Common mistakes:**
- Inverting only the root's children without recursing (only swaps one level).
- Creating new nodes instead of swapping pointers (unnecessary allocation).
- Trying to invert in-place during inorder traversal (visits the same subtree twice after swap).

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Swap left and right at every node. In-place transformation.
- **Match:** Any tree traversal with a swap operation.
- **Plan:** Recursive preorder: swap, recurse left, recurse right.
- **Implement:** 4 lines of code.
- **Review:** Verify on single node, empty tree, and small example.
- **Evaluate:** O(n) time, O(h) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| Check if two trees are mirrors? | Compare left of one with right of other recursively. (LC #101 Symmetric Tree) |
| Invert without modifying original? | Create new nodes during recursion. |
| Invert N-ary tree? | Reverse the children list at each node. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Symmetric Tree (LC #101) | Check if tree is its own inverse |
| Same Tree (LC #100) | Compare two trees structurally |
| Merge Two Binary Trees (LC #617) | Node-by-node transformation |
| Flip Equivalent Trees (LC #951) | Check if one tree can become another via flips |
| Binary Tree Level Order (LC #102) | BFS infrastructure reused for iterative approach |

---

## Real-World Use Case
**Mirror image generation in graphics software:** Adobe Illustrator's "Reflect" tool inverts a tree of grouped SVG elements. Swapping left and right children at every node produces the mirror image. This same operation is used in CSS `transform: scaleX(-1)` to flip component trees in web rendering engines.
