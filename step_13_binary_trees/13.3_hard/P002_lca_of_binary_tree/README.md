# LCA of Binary Tree

> **Step 13.3** | **Difficulty:** HARD | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given a binary tree, find the **Lowest Common Ancestor (LCA)** of two given nodes `p` and `q`.

The LCA is defined as the **deepest node** in the tree that has both `p` and `q` as descendants. **A node can be a descendant of itself** — so if `p` is an ancestor of `q`, the LCA is `p`.

All node values are unique. Both `p` and `q` are guaranteed to exist in the tree.

## Examples

```
        3
       / \
      5   1
     / \ / \
    6  2 0  8
      / \
     7   4
```

| p | q | LCA | Explanation |
|---|---|-----|-------------|
| 5 | 1 | 3   | 5 is in left subtree, 1 in right subtree — root 3 is LCA |
| 5 | 4 | 5   | 4 is a descendant of 5, so 5 is its own ancestor of 4 |
| 3 | 5 | 3   | 3 is ancestor of 5, and is the root |

## Constraints

- Number of nodes: `[2, 10^5]`
- `-10^9 <= Node.val <= 10^9`
- All `Node.val` are **unique**
- `p != q`
- Both `p` and `q` exist in the tree

---

## Approach 1: Brute Force — Find Paths, Compare

**Intuition:** Find the path from root to `p` and from root to `q` as lists. Walk both paths simultaneously from the beginning. The last node where the paths agree is the LCA — because after the LCA, the two paths diverge.

**Steps:**
1. DFS from root to find `path_p = [root, ..., p]`.
2. DFS from root to find `path_q = [root, ..., q]`.
3. Walk both paths with index `i` while `path_p[i] == path_q[i]`.
4. The last equal node is the LCA.

**Path finding:** Backtracking DFS — append node, recurse, pop if target not found.

| Metric | Value |
|--------|-------|
| Time   | O(n) — two DFS traversals |
| Space  | O(n) — path lists of length up to n |

---

## Approach 2: Optimal — Single Recursive Pass

**Intuition:** Post-order recursion. At each node, ask: "Did I find `p` or `q` in my left subtree? In my right subtree?"

- If **both** subtrees return a non-null node: current node is the LCA (one target in each subtree).
- If **only one** returns non-null: bubble that result up (both targets in same subtree, or current node IS one of the targets).
- If **neither** returns non-null: neither target is in this subtree.

The base case handles: if the current node IS `p` or `q`, return it immediately (no need to go deeper — even if the other target is below, the current node is still the LCA by the "ancestor of itself" rule).

**Steps:**
1. If `root == null || root == p || root == q`: return `root`.
2. `left = lca(root.left, p, q)`, `right = lca(root.right, p, q)`.
3. If `left != null && right != null`: return `root`.
4. Return whichever of `left` or `right` is non-null.

| Metric | Value |
|--------|-------|
| Time   | O(n) — single traversal |
| Space  | O(h) — recursion stack where h = tree height |

---

## Approach 3: Best — Iterative with Parent-Pointer Map

**Intuition:** To avoid potential stack overflow in a highly skewed tree, use an iterative DFS to build a `parent` map: each node → its parent. Then collect all ancestors of `p` into a set. Walk up from `q` using parent pointers until hitting a node in `p`'s ancestor set — that's the LCA.

**Steps:**
1. Iterative DFS (stack-based) to build `parent_map[node] = parent`.
   Stop once both `p` and `q` are discovered (no need to traverse the whole tree).
2. Walk from `p` up to root, adding each node to `ancestors` set.
3. Walk from `q` up to root; return the first node found in `ancestors`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — parent map + ancestors set |

---

## Real-World Use Case

**Version Control Systems (Git):** Finding the LCA is equivalent to finding the "merge base" of two commits — the most recent common ancestor commit from which both branches diverged. `git merge-base A B` computes exactly this. Every `git merge` and `git rebase` uses LCA.

**Organizational Hierarchy:** Given a corporate org chart as a tree, LCA finds the closest common manager of two employees — used in HR systems and access-control delegation logic.

**DNS Resolution:** Domain name hierarchy (`a.b.com`, `x.b.com`) — the LCA of two domains is the deepest shared domain namespace.

## Interview Tips

- The **recursive single-pass** (Approach 2) is the expected optimal answer. Practice it until it's fluent.
- The key insight: returning the node itself when `root == p || root == q` covers the "ancestor of itself" case elegantly.
- The "both subtrees non-null → return root" case is the split point: one target went left, one went right.
- The "one subtree non-null → bubble up" case means either: (a) both targets are in that subtree (LCA is somewhere deeper and has already been found), or (b) only one target was found so far.
- Approach 3 (iterative) is worth knowing to handle follow-up: "What if the tree is very deep and you're worried about stack overflow?"
- If the problem changes to: "p or q may NOT be in the tree" — you must handle that: track whether each was found.
- Related: LCA of BST (LeetCode 235) is simpler because you can use BST properties to guide direction without full traversal.
- LCA with parent pointers (if nodes have parent pointers) — similar to Approach 3 but even simpler: just use two sets to find first common ancestor by walking both paths to root simultaneously.
