# Kth Smallest Element in a BST

> **Step 14 - BST Concepts** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED
> LeetCode 230

## Problem Statement

Given the `root` of a Binary Search Tree and an integer `k`, return the **kth smallest** value (1-indexed) of all node values in the tree.

**Key property to exploit:** Inorder traversal of a BST yields values in **sorted ascending order**.

## Examples

**Example 1:**
```
Input:  root = [3,1,4,null,2], k = 1
        3
       / \
      1   4
       \
        2
Output: 1
Explanation: Inorder = [1, 2, 3, 4]. 1st smallest = 1.
```

**Example 2:**
```
Input:  root = [5,3,6,2,4,null,null,1], k = 3
            5
           / \
          3   6
         / \
        2   4
       /
      1
Output: 3
Explanation: Inorder = [1, 2, 3, 4, 5, 6]. 3rd smallest = 3.
```

## Constraints

- Number of nodes: `1 <= n <= 10^4`
- `0 <= Node.val <= 10^4`
- `1 <= k <= n`
- All node values are **unique**.

---

## Approach 1: Brute Force — Collect Inorder into Array

**Intuition:** BST inorder traversal gives a sorted list. Collect everything then index.

**Steps:**
1. Do a recursive inorder traversal (left -> root -> right).
2. Append each node value to a list.
3. Return `list[k-1]` (convert from 1-indexed to 0-indexed).

| Metric | Value |
|--------|-------|
| Time   | O(n) — visit every node |
| Space  | O(n) — list stores all values + O(h) stack |

**Weakness:** Wastes time/memory visiting nodes beyond the kth one.

---

## Approach 2: Optimal — Iterative Inorder with Early Exit

**Intuition:** Use an explicit stack to simulate inorder traversal. Stop the moment we have processed k nodes. For small k, this is much faster.

**Steps:**
1. Initialize `stack = []`, `count = 0`, `curr = root`.
2. While `curr != null` or stack not empty:
   a. Push all left children onto stack (descend left).
   b. Pop from stack — this is the next inorder node.
   c. Increment `count`; if `count == k`, return `curr.val`.
   d. Move `curr = curr.right`.

| Metric | Value |
|--------|-------|
| Time   | O(k + h) — at most k nodes visited + height of leftmost path |
| Space  | O(h) — stack depth equals tree height |

**Best practical approach** for interviews and the follow-up "frequent updates" scenario.

---

## Approach 3: Best — Morris Inorder Traversal (O(1) Space)

**Intuition:** Morris traversal threads the tree: the rightmost node of a node's left subtree temporarily points back to the node itself. This allows inorder traversal without any stack or recursion.

**Steps:**
1. `curr = root`, `count = 0`.
2. While `curr != null`:
   - If `curr.left == null`: visit (count++), check if `count == k`, move right.
   - Else find `predecessor` = rightmost node of `curr.left`.
     * If `predecessor.right == null`: create thread (`predecessor.right = curr`), go left.
     * If `predecessor.right == curr`: remove thread, visit, go right.

| Metric | Value |
|--------|-------|
| Time   | O(n) worst case (threads are created and removed) |
| Space  | O(1) — no stack, no recursion |

**Trade-off:** Temporarily modifies the tree structure (threads), which can be a problem in concurrent environments. Approach 2 is preferred in practice unless memory is extremely constrained.

---

## Real-World Use Case

**Database range queries / leaderboard rankings:** In a database implementing an indexed B-tree, finding the kth ranked element (e.g., "who is 50th on the leaderboard?" or "what is the median salary?") is a classic order-statistic problem. The BST inorder approach corresponds directly to navigating a B-tree index. The Optimal approach's early-exit is exactly what a DB cursor does — it doesn't load the entire result set.

---

## Interview Tips

- Lead with the inorder property: "BST inorder gives sorted order, so I just need the kth element of that traversal."
- The interviewer will likely ask: "Can you do better than O(n) space?" -> pivot to iterative inorder.
- Follow-up: "What if the BST is modified often and you need kth smallest frequently?" -> Augment each node with `left_subtree_size` (order-statistics tree), giving O(log n) per query.
- Morris traversal is impressive to mention but risky in an interview if you don't know it cold — stick to iterative if unsure.
- Know the difference: kth **smallest** = inorder; kth **largest** = reverse inorder (right -> root -> left).
