# Delete Node in BST

> **LeetCode 450** | **Step 14 — BST Concepts** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

Given the `root` of a Binary Search Tree and an integer `key`, delete the node with value `key` from the BST and return the **root of the modified tree**.

It is guaranteed that all values in the BST are **unique**. The resulting tree must still be a valid BST.

---

## Examples

| Input BST (insert order) | Key | Inorder after deletion | Explanation |
|--------------------------|-----|------------------------|-------------|
| `[5, 3, 6, 2, 4, 7]`    | `3` | `[2, 4, 5, 6, 7]`     | Node 3 has two children; replaced by inorder successor 4 |
| `[5, 3, 6, 2, 4, 7]`    | `0` | `[2, 3, 4, 5, 6, 7]`  | Key 0 not found; tree unchanged |
| `[5, 3, 6, 2, 4, 7]`    | `5` | `[2, 3, 4, 6, 7]`     | Root deleted; right subtree promoted |

**Visual (delete key=3):**
```
      5                   5
     / \                 / \
    3   6    --->        4   6
   / \   \              /     \
  2   4   7            2       7
```

---

## Constraints

- Number of nodes: `[0, 10^4]`
- `-10^5 <= Node.val <= 10^5`
- All node values are **unique**
- The tree is a valid BST
- `-10^5 <= key <= 10^5`

---

## Approach 1: Brute Force — Inorder Array, Remove, Rebuild

**Intuition:** The safest but costliest approach. Extract all BST values via inorder traversal (giving a sorted array), delete the key from the array, then reconstruct a balanced BST from the sorted array. Easy to reason about but wastes O(n) memory and loses original tree structure.

**Steps:**
1. Traverse the tree in inorder and collect all values into a list.
2. Remove `key` from the list (if present).
3. Build a new balanced BST from the sorted list using the "median-as-root" technique:
   - Pick `list[mid]` as the root.
   - Recurse on `list[lo..mid-1]` for the left subtree.
   - Recurse on `list[mid+1..hi]` for the right subtree.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Approach 2: Optimal — Recursive 3-Case Deletion

**Intuition:** Use the BST property to navigate directly to the target node in O(h) time. Once found, handle the three standard cases. The recursive structure means the parent's pointer is automatically updated via the return value.

**Steps:**
1. If `root == null`, return `null` (key not found).
2. If `key < root.val`, recurse left: `root.left = delete(root.left, key)`.
3. If `key > root.val`, recurse right: `root.right = delete(root.right, key)`.
4. Otherwise (`key == root.val`), the node is found:
   - **Case 1 / 2 — At most one child:** return whichever child is non-null (or `null` for leaf).
   - **Case 3 — Two children:**
     a. Find the **inorder successor**: walk right once, then left until the end.
     b. Copy the successor's value into the current node.
     c. Recursively delete the successor from `root.right`.

| Metric | Value |
|--------|-------|
| Time   | O(h) — O(log n) balanced, O(n) worst case skewed |
| Space  | O(h) — recursion call stack |

---

## Approach 3: Best — Iterative Deletion (O(1) Extra Space)

**Intuition:** Exactly the same 3-case logic, but implemented with an explicit loop and a parent pointer instead of recursion. Eliminates call-stack overhead.

For the two-children case, instead of copying values (which could be tricky with complex node types), we **physically re-attach** the deleted node's left subtree to the leftmost position of its right subtree — this preserves BST ordering without any value copying.

**Steps:**
1. Iterate to find `curr` (the node to delete) and `parent`.
2. If `curr == null`, key not found — return original root.
3. Compute `replacement`:
   - No left child: `replacement = curr.right`
   - No right child: `replacement = curr.left`
   - Two children: walk to the leftmost of `curr.right`, attach `curr.left` there; `replacement = curr.right`
4. Update `parent.left` or `parent.right` to `replacement` (or return `replacement` if deleting the root).

| Metric | Value |
|--------|-------|
| Time   | O(h)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Database index maintenance:** B-trees and BSTs underpin database indexes (e.g., MySQL InnoDB). When a row is deleted, the corresponding key must be removed from the index tree while preserving sorted order for range queries — exactly this algorithm, scaled to disk pages.

**Auto-complete / dictionary trie deletion:** Deleting a word from a BST-backed sorted dictionary uses the same 3-case logic and is a building block in search-engine suggestion engines.

---

## Interview Tips

- Always mention all three cases upfront before coding — interviewers want to see that you've thought through the problem.
- The inorder successor (leftmost of right subtree) is the go-to for the two-children case. Alternatively you can use the inorder predecessor (rightmost of left subtree) — both are valid.
- For the iterative solution, clearly explain why attaching the left subtree to the leftmost of the right subtree preserves BST ordering: every value in the left subtree is smaller than every value in the right subtree.
- Edge cases: empty tree, deleting the root, key not present in tree.
- Follow-up: "Can you delete a node in O(1) if you are given a pointer to the node (not the root)?" — Yes, overwrite the node with its successor/predecessor value and delete that leaf.
