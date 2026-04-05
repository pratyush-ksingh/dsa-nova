# Flatten Binary Tree to Linked List

> **LeetCode 114** | **Step 13 — Binary Trees (Hard)** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

Given the `root` of a binary tree, flatten the tree into a **linked list in-place**.

The linked list should be in the same order as a **preorder traversal** of the binary tree.
Every node's `left` pointer must be set to `null`; the sequence is represented using the `right` pointers.

---

## Examples

| Input (level-order) | Output (right-chain) | Explanation |
|---------------------|----------------------|-------------|
| `[1, 2, 5, 3, 4, null, 6]` | `[1, 2, 3, 4, 5, 6]` | Preorder of the tree is 1→2→3→4→5→6 |
| `[]` | `[]` | Empty tree stays empty |
| `[0]` | `[0]` | Single node, nothing to do |

**Visual:**
```
      1                 1
     / \                 \
    2   5    --->         2
   / \   \                 \
  3   4   6                 3
                             \
                              4
                               \
                                5
                                 \
                                  6
```

---

## Constraints

- Number of nodes: `[0, 2000]`
- `-100 <= Node.val <= 100`
- Must be done **in-place** (O(1) extra space for the best approach)

---

## Approach 1: Brute Force — Preorder Array then Relink

**Intuition:** The simplest strategy is to separate the two concerns: first *collect* the preorder sequence, then *rewire* the pointers. Because we store every node, we can index them directly.

**Steps:**
1. Run a standard recursive preorder traversal and append each `TreeNode` to a list.
2. Iterate through the list. For index `i`, set `nodes[i].left = null` and `nodes[i].right = nodes[i+1]`.
3. Set the last node's `left` and `right` to `null`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) — the node list |

---

## Approach 2: Optimal — Recursive Reverse Preorder

**Intuition:** Instead of storing the nodes, process them in **reverse preorder** (right → left → root). Maintain a `prev` pointer. When we process the root last, `prev` already points to the already-flattened head of the right portion — so we simply set `root.right = prev` and `root.left = null`.

Think of it like building the linked list from tail to head: every time we process a node, `prev` is the correct successor.

**Steps:**
1. Recursively flatten the **right** subtree first.
2. Recursively flatten the **left** subtree next.
3. Set `node.right = prev`, `node.left = null`, then `prev = node`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(h) — recursion stack (h = tree height) |

---

## Approach 3: Best — Morris-Style Iterative (Truly O(1) Space)

**Intuition:** At any node with a left child, the last node visited in preorder before the right subtree is the **rightmost node of the left subtree**. Thread the right subtree onto that rightmost node, then move the entire left subtree to the right and clear the left pointer. This is identical in spirit to Morris traversal.

**Steps:**
1. Start `curr = root`.
2. While `curr != null`:
   - If `curr.left != null`:
     a. Walk to the **rightmost node** of `curr.left` — call it `pred`.
     b. Set `pred.right = curr.right` (thread the right subtree).
     c. Set `curr.right = curr.left` (shift left to right).
     d. Set `curr.left = null`.
   - Advance `curr = curr.right`.

**Why it works:** After step 2c the left subtree is now on the right. The original right subtree is appended after it via `pred.right`. We advance and repeat until no more left children exist anywhere.

| Metric | Value |
|--------|-------|
| Time   | O(n) — each node is touched at most twice |
| Space  | O(1) — no stack, no recursion |

---

## Real-World Use Case

**XML / HTML serialization:** A DOM tree is flattened to a linear sequence of tokens (open-tag, children, close-tag) when writing XML to a stream. The preorder flattening maps directly to how an XML serializer walks the tree and emits tokens into a flat output buffer.

**Thread pools / task scheduling:** Dependency trees are sometimes flattened into a linear execution queue in preorder to ensure parents are dispatched before children.

---

## Interview Tips

- The interviewer often asks for O(1) space — lead with Approach 2 (recursive) to show you understand the structure, then pivot to Approach 3 (Morris) when they ask for no recursion.
- Approach 2's "reverse preorder" trick is a common pattern for in-place tree surgery; the same idea appears in converting BST to doubly-linked list.
- For the Morris approach, draw the threading step on a small example (3-node tree) before coding — it makes the logic crystal clear.
- Edge cases: empty tree (return immediately), single node (already flat), right-skewed tree (left is always null, just advance).
- The problem says "in-place" but does NOT restrict auxiliary space for the recursive call stack — so Approach 2 is fully acceptable in interviews unless the interviewer explicitly says O(1) space including stack.
