# Height of Binary Tree (Maximum Depth)

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #104**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return its **maximum depth**. The maximum depth is the number of **nodes** along the longest path from the root node down to the farthest leaf node.

Note: LeetCode defines depth as node count (root has depth 1). Some definitions use edge count (root has depth 0). This solution follows the LeetCode convention.

### Analogy
Think of measuring the **depth of a cave** system. You explore every branch (left and right tunnels) and report the deepest point found. The depth of the cave is determined by the deepest branch, not the shallowest. You explore all the way down each branch, then compare.

### Key Observations
1. The height of a tree is a **postorder** computation: you need the heights of both subtrees before you can compute the height of the current node. **Aha:** `height(node) = 1 + max(height(left), height(right))`.
2. Base case: an empty tree (null) has depth 0. **Aha:** This makes the recursion clean -- a leaf node returns `1 + max(0, 0) = 1`.
3. BFS (level-order) counts levels directly: the number of levels = the depth. **Aha:** This gives an iterative alternative that is intuitive for level-counting.

### Examples

```
    3        Depth: 3
   / \
  9  20
    /  \
   15   7
```

```
    1        Depth: 2
     \
      2
```

| Input | Output |
|-------|--------|
| [3,9,20,null,null,15,7] | 3 |
| [1,null,2] | 2 |
| [] | 0 |
| [1] | 1 |

### Constraints
- 0 <= number of nodes <= 10^4
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute / Optimal (recursive DFS) | Call stack | Natural postorder: max(left depth, right depth) + 1. |
| Optimal alt (iterative BFS) | Queue | Count number of levels = depth. |
| Best (same as recursive DFS) | Call stack | Cannot beat O(n) -- must visit all nodes to find the deepest. |

**Pattern cue:** "Height/depth of tree" -> postorder recursion or BFS level counting.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive DFS (Postorder)
**Intuition:** The depth of a tree rooted at `node` is `1 + max(depth of left subtree, depth of right subtree)`.

**Steps:**
1. If node is null, return 0.
2. Recursively compute left depth.
3. Recursively compute right depth.
4. Return `1 + max(leftDepth, rightDepth)`.

**Dry-Run Trace -- Tree [3,9,20,null,null,15,7]:**

| Call | Node | Left Depth | Right Depth | Return |
|------|------|-----------|-------------|--------|
| depth(15) | 15 | 0 | 0 | 1 |
| depth(7) | 7 | 0 | 0 | 1 |
| depth(9) | 9 | 0 | 0 | 1 |
| depth(20) | 20 | 1 (from 15) | 1 (from 7) | 2 |
| depth(3) | 3 | 1 (from 9) | 2 (from 20) | 3 |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) recursion stack |

### BUD Transition
The recursive solution is already optimal at O(n). BFS offers an alternative with the same complexity.

### Approach 2 -- Iterative BFS (Level-Order)
**Intuition:** BFS processes the tree level by level. Count the number of levels.

**Steps:**
1. If root is null, return 0.
2. Initialize queue with root. Set depth = 0.
3. While queue is not empty:
   - Process all nodes in current level (loop `queue.size()` times).
   - For each node, enqueue its children.
   - Increment depth.
4. Return depth.

**Dry-Run Trace:**

| Level | Nodes | Queue After | Depth |
|-------|-------|-------------|-------|
| 0 | [3] | [9, 20] | 1 |
| 1 | [9, 20] | [15, 7] | 2 |
| 2 | [15, 7] | [] | 3 |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(w) where w = max width of tree |

### Approach 3 -- Best (Same as Recursive DFS)
O(n) is optimal since every node must be visited. Recursive DFS uses O(h) space which is O(log n) for balanced trees.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- O(log n) balanced, O(n) skewed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** Must check every node -- the deepest leaf could be anywhere.
- **O(h) recursive space:** At most h frames on the call stack, one per level.
- **O(w) BFS space:** Queue holds at most one full level. For a complete tree, the last level has ~n/2 nodes.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null) | Return 0 |
| Single node | Return 1 |
| Left-skewed | Depth = n |
| Right-skewed | Depth = n |
| Perfect binary tree | Depth = log2(n+1) |

**Common mistakes:**
- Returning -1 for null instead of 0 (depends on definition; LeetCode uses 0).
- Confusing depth (from root) with height (from leaves). For the root node, max depth == height + 1.
- BFS: forgetting to process all nodes at a level before incrementing depth.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Recursive or iterative? | Recursive is cleaner and shorter. BFS if you want to avoid recursion. |
| What about minimum depth? | Similar but return when you hit the first leaf (BFS is more natural for this). |
| Can you compute height without visiting all nodes? | Not in general. For a complete binary tree, you can in O(log^2 n). |
| Follow-up: balanced tree check? | At each node, check if abs(leftHeight - rightHeight) <= 1. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Minimum Depth (LC #111) | BFS to first leaf; not just max |
| Balanced Binary Tree (LC #110) | Uses height at each node |
| Diameter of Tree (LC #543) | Longest path through any node = leftHeight + rightHeight |
| Maximum Width (LC #662) | BFS with index tracking |
| Count Complete Tree Nodes (LC #222) | Leverages height for O(log^2 n) solution |

---

## Real-World Use Case
**Network hop count calculation:** Cloudflare uses tree height to measure the maximum number of hops from their root CDN node to any edge server. The height determines worst-case latency. Keeping the tree height low (balanced) ensures content reaches users in minimal hops.
