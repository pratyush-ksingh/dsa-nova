# LCA of BST (Lowest Common Ancestor of a Binary Search Tree)

> **Batch 2 of 12** | **Topic:** Binary Search Trees | **Difficulty:** MEDIUM | **XP:** 25

**LeetCode #235**

---

## 1. UNDERSTAND

### Problem Statement
Given a Binary Search Tree (BST) and two nodes `p` and `q`, find the **Lowest Common Ancestor** (LCA). The LCA is defined as the lowest (deepest) node that has both `p` and `q` as descendants (a node can be a descendant of itself).

Both `p` and `q` are guaranteed to exist in the BST.

### Analogy
Think of a **company org chart** ordered by employee ID (like a BST). You want to find the lowest-level manager who oversees both employee P and employee Q. If P's ID < manager's ID and Q's ID > manager's ID, that manager is the split point -- one employee is in the left branch, the other in the right. That is the LCA. If both are on the same side, go deeper into that branch.

### Key Observations
1. In a BST, all left descendants < node < all right descendants. **Aha:** If `p.val < node.val` and `q.val < node.val`, both are in the left subtree. If both are greater, both are in the right.
2. The LCA is the first node where `p` and `q` **split** -- one goes left, the other goes right. **Aha:** This happens when `min(p,q) <= node.val <= max(p,q)`.
3. No need to search both subtrees (unlike general binary tree LCA). **Aha:** BST property gives us a directed search: O(h) instead of O(n).

### Examples

```
        6           p=2, q=8 -> LCA=6
       / \          (2 < 6, 8 > 6: split at 6)
      2   8
     / \ / \
    0  4 7  9
      / \
     3   5
```

```
        6           p=2, q=4 -> LCA=2
       / \          (both < 6: go left)
      2   8         (at 2: 2==p, 4>2 but 2 is ancestor of 4)
     / \ / \        (2 is the LCA since a node is its own descendant)
    0  4 7  9
```

| Input | p | q | Output |
|-------|---|---|--------|
| [6,2,8,0,4,7,9,null,null,3,5] | 2 | 8 | 6 |
| [6,2,8,0,4,7,9,null,null,3,5] | 2 | 4 | 2 |
| [2,1] | 2 | 1 | 2 |

### Constraints
- 2 <= number of nodes <= 10^5
- -10^9 <= Node.val <= 10^9
- All values are unique
- p != q
- p and q exist in the BST

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (general BT LCA) | Call stack | Ignore BST property. Postorder DFS checking both subtrees. O(n). |
| Optimal (BST-guided recursive) | Call stack | Use BST property to choose one direction. O(h). |
| Best (BST-guided iterative) | No extra DS | Same logic, iterative. O(h) time, O(1) space. |

**Pattern cue:** "LCA in BST" -> exploit ordering to navigate to the split point. O(h) instead of O(n).

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (General Binary Tree LCA)
**Intuition:** Treat the tree as a regular binary tree. Recursively search left and right. If a node matches p or q, return it. If left and right both return non-null, current node is LCA.

**Steps:**
1. If root is null or root == p or root == q, return root.
2. `left = lca(root.left, p, q)`.
3. `right = lca(root.right, p, q)`.
4. If both left and right are non-null, return root (split point).
5. Return whichever is non-null.

| Metric | Value |
|--------|-------|
| Time | O(n) -- may visit every node |
| Space | O(h) recursion stack |

### BUD Transition
**Unnecessary work:** We search both subtrees, but BST ordering tells us which side to go. **Upgrade:** Compare p and q values with current node to choose direction.

### Approach 2 -- Optimal (BST-Guided Recursive)
**Intuition:** At each node, if both p and q are smaller, go left. If both are larger, go right. Otherwise, the current node is the split point (the LCA).

**Steps:**
1. If `p.val < root.val` AND `q.val < root.val`, recurse left.
2. If `p.val > root.val` AND `q.val > root.val`, recurse right.
3. Otherwise, return root (this is where they split).

**Dry-Run Trace -- BST [6,2,8,0,4,7,9], p=2, q=8:**

| Step | Node | p(2) vs Node | q(8) vs Node | Action |
|------|------|-------------|-------------|--------|
| 1 | 6 | 2 < 6 | 8 > 6 | **Split! Return 6** |

Trace for p=2, q=4:

| Step | Node | p(2) vs Node | q(4) vs Node | Action |
|------|------|-------------|-------------|--------|
| 1 | 6 | 2 < 6 | 4 < 6 | Both < node, go left |
| 2 | 2 | 2 == 2 | 4 > 2 | **Split! Return 2** |

| Metric | Value |
|--------|-------|
| Time | O(h) -- only traverses root to LCA |
| Space | O(h) recursion stack |

### Approach 3 -- Best (BST-Guided Iterative)
**Intuition:** Same logic as recursive, but use a while loop instead of recursion. Eliminates call stack overhead.

**Steps:**
1. Start at root.
2. While true:
   - If `p.val < node.val` AND `q.val < node.val`, move to `node.left`.
   - Else if `p.val > node.val` AND `q.val > node.val`, move to `node.right`.
   - Else, return node.

| Metric | Value |
|--------|-------|
| Time | O(h) |
| Space | O(1) -- no recursion stack |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) brute:** Without BST property, must potentially search the entire tree.
- **O(h) optimal/best:** BST ordering guides us straight to the answer. For a balanced BST, h = log n. For a skewed BST, h = n.
- **O(1) iterative space:** Just a pointer moving down the tree. No extra storage.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| One of p, q is the root | Root is the LCA |
| p is ancestor of q | p is the LCA (a node is its own descendant) |
| p and q are siblings | Their parent is the LCA |
| p and q are at max depth | LCA could be anywhere from root to their level |
| Skewed BST | O(n) worst case for O(h) solution |

**Common mistakes:**
- Comparing node references instead of values. Use `p.val` and `q.val`, not `p == node`.
- Forgetting that a node can be a descendant of itself.
- Using general BT LCA approach and missing the O(h) BST optimization.

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** LCA in a BST. Both nodes guaranteed to exist. A node is its own descendant.
- **Match:** BST property -> directed search to split point.
- **Plan:** Compare p,q values to current node; go left, right, or found.
- **Implement:** Iterative loop for O(1) space.
- **Review:** Trace p=2, q=4 example showing p itself is the LCA.
- **Evaluate:** O(h) time, O(1) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| What about LCA in a general binary tree? | Must check both subtrees. O(n). (LC #236) |
| What if nodes might not exist? | Must verify existence first, then find LCA. |
| LCA of multiple nodes? | Find LCA pairwise, or check if all values split. |
| What if the BST is very large (on disk)? | Iterative O(1) space is crucial. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| LCA of Binary Tree (LC #236) | General BT version, requires checking both sides |
| Validate BST (LC #98) | Uses same BST ordering property |
| Search in BST (LC #700) | Same O(h) directed search pattern |
| Inorder Successor in BST (LC #285) | BST navigation |
| Ceil in BST | Same left/right decision based on value comparison |
