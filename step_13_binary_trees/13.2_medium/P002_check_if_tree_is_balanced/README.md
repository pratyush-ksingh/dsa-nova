# Check if Tree is Balanced

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #110**

---

## 1. UNDERSTAND

### Problem Statement
Given a binary tree, determine if it is **height-balanced**. A height-balanced binary tree is one in which the depth of the two subtrees of **every** node never differs by more than **1**.

### Analogy
Imagine a **mobile hanging from the ceiling** (like a baby's mobile). For the mobile to hang level without tilting, **every** horizontal bar must be roughly balanced -- neither side can be much heavier (deeper) than the other. If any single bar is lopsided by more than one unit, the whole mobile is "unbalanced." You must check **every** bar, not just the top one.

### Key Observations
1. A naive check computes height at every node, then checks the balance condition. **Aha:** This recomputes heights repeatedly -- O(n^2) on skewed trees.
2. Height computation is postorder (need both children first). Balance checking also needs both subtree heights. **Aha:** We can combine height computation and balance checking into one postorder pass -- return -1 to signal "unbalanced."
3. The condition is **global**: the tree is balanced only if **every** subtree is balanced. **Aha:** The moment any subtree fails, we can short-circuit and return immediately.

### Examples

```
        3           Balanced: true
       / \          Left height = 1, Right height = 2
      9  20         Difference <= 1 at every node
        /  \
       15   7
```

```
        1           Balanced: false
       / \          Node 2: left height=2, right=0
      2   2         Difference = 2 > 1
     / \
    3   3
   / \
  4   4
```

| Input | Output |
|-------|--------|
| [3,9,20,null,null,15,7] | true |
| [1,2,2,3,3,null,null,4,4] | false |
| [] | true |
| [1] | true |
| [1,2,null,3] | false |

### Constraints
- 0 <= number of nodes <= 5000
- -10^4 <= Node.val <= 10^4

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (top-down) | Call stack + height function | Check balance at each node using a separate height call. |
| Optimal (bottom-up) | Call stack | Single postorder pass: return -1 for unbalanced, else height. |
| Best (same as optimal) | Call stack | Cannot beat O(n) -- must check every node. |

**Pattern cue:** "Check property at every node using subtree info" -> postorder DFS with combined return value.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Top-Down)
**Intuition:** At each node, compute height of left and right subtrees. If they differ by more than 1, return false. Then recursively check that left and right subtrees are also balanced.

**Steps:**
1. Define `height(node)`: if null return 0, else `1 + max(height(left), height(right))`.
2. Define `isBalanced(node)`: if null return true.
3. Compute `leftH = height(left)`, `rightH = height(right)`.
4. Return `abs(leftH - rightH) <= 1 AND isBalanced(left) AND isBalanced(right)`.

| Metric | Value |
|--------|-------|
| Time | O(n^2) worst case -- height recomputed at every level |
| Space | O(h) recursion stack |

### BUD Transition
**Bottleneck:** We recompute height for overlapping subproblems. **Upgrade:** Combine height computation and balance check in one pass -- return -1 as a sentinel for "unbalanced."

### Approach 2 -- Optimal (Bottom-Up Single Pass)
**Intuition:** In a single postorder traversal, compute height bottom-up. If any subtree is unbalanced, propagate -1 upward immediately. Otherwise return the actual height.

**Steps:**
1. Define `check(node)`:
   - If null, return 0.
   - `leftH = check(left)` -- if -1, return -1.
   - `rightH = check(right)` -- if -1, return -1.
   - If `abs(leftH - rightH) > 1`, return -1.
   - Else return `1 + max(leftH, rightH)`.
2. `isBalanced(root)` = `check(root) != -1`.

**Dry-Run Trace -- Tree [1,2,2,3,3,null,null,4,4]:**

| Call | Node | leftH | rightH | |leftH-rightH| | Return |
|------|------|-------|--------|----------------|--------|
| check(4L) | 4 | 0 | 0 | 0 | 1 |
| check(4R) | 4 | 0 | 0 | 0 | 1 |
| check(3L) | 3 | 1 | 1 | 0 | 2 |
| check(3R) | 3 | 0 | 0 | 0 | 1 |
| check(2L) | 2 | 2 | 1 | 1 | 3 |
| check(2R) | 2 | 0 | 0 | 0 | 1 |
| check(1) | 1 | 3 | 1 | **2 > 1** | **-1** |

Result: -1, so tree is **not** balanced.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node visited exactly once |
| Space | O(h) recursion stack |

### Approach 3 -- Best (Same as Optimal)
O(n) is optimal because every node must be inspected (the unbalanced subtree could be anywhere). The bottom-up approach achieves this.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- O(log n) balanced, O(n) skewed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time (optimal):** One postorder visit per node. Height and balance computed simultaneously.
- **O(n^2) time (brute):** For a skewed tree of depth n, height() is called at each of the n nodes, each call traversing up to n nodes.
- **O(h) space:** Recursion depth equals tree height. Balanced: O(log n). Skewed: O(n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null) | Return true (vacuously balanced) |
| Single node | Return true (height 0 on both sides) |
| Left-skewed tree (depth n) | Unbalanced at the root |
| Perfect binary tree | Always balanced |
| Only one child at one deep node | Must check at that specific node |

**Common mistakes:**
- Only checking balance at the root, not at every node.
- Returning `abs(height(left) - height(right)) <= 1` without recursively checking subtrees (brute approach).
- Using `height == 0` as sentinel instead of -1 (0 is a valid height for null).

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Clarify that balanced means *every* node's subtrees differ by at most 1, not just the root.
- **Match:** This is a postorder (bottom-up) tree property check.
- **Plan:** Single-pass bottom-up with -1 sentinel.
- **Implement:** Clean recursive function.
- **Review:** Trace through an unbalanced example.
- **Evaluate:** O(n) time, O(h) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| What if we need to make it balanced? | DSW algorithm or convert to sorted array + build balanced BST. |
| Can you do it iteratively? | Yes, iterative postorder with a height map, but more complex. |
| AVL vs Red-Black trees? | AVL is stricter (balance factor <= 1); Red-Black allows up to 2x height difference. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Height of Binary Tree (LC #104) | This problem builds on height computation |
| Diameter of Binary Tree (LC #543) | Same postorder pattern: compute + combine in one pass |
| Maximum Path Sum (LC #124) | Postorder with combined computation |
| Minimum Depth (LC #111) | Height variant but stops at nearest leaf |
| Validate BST (LC #98) | Another "check property at every node" pattern |
