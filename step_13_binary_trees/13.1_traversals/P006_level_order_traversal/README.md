# Level Order Traversal

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** MEDIUM | **XP:** 25

**LeetCode #102**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return the **level order traversal** of its nodes' values (i.e., from left to right, level by level). Return the result as a **list of lists**, where each inner list contains all node values at that depth level.

### Analogy
Imagine a **classroom photo** taken row by row. The photographer starts at the front row (root), photographs everyone in that row left to right, then moves to the second row, and so on. Each row in the photo is one "level" of the tree. The key challenge is knowing when one row ends and the next begins -- we use the queue's current size as a delimiter.

### Key Observations
1. BFS with a queue naturally processes nodes level by level. **Aha:** The trick is to snapshot `queue.size()` at the start of each level to know how many nodes belong to the current level.
2. DFS can also produce level-order output by passing the depth as a parameter and appending to the correct list. **Aha:** DFS with depth tracking = "fake BFS" in terms of output.
3. The output structure (list of lists) requires grouping by level. **Aha:** This is what separates level-order from plain BFS -- we need the level boundaries.

### Examples

```
        3           Level Order: [[3], [9, 20], [15, 7]]
       / \
      9  20
        /  \
       15   7
```

```
        1           Level Order: [[1]]
```

| Input | Output |
|-------|--------|
| [3,9,20,null,null,15,7] | [[3],[9,20],[15,7]] |
| [1] | [[1]] |
| [] | [] |
| [1,2,3,4,5] | [[1],[2,3],[4,5]] |

### Constraints
- 0 <= number of nodes <= 2000
- -1000 <= Node.val <= 1000

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (DFS with depth) | Call stack + result list | Preorder DFS tracking depth, append to result[depth]. |
| Optimal (BFS with queue) | Queue | Natural level-by-level processing. |
| Best (same as BFS) | Queue | Both are O(n). BFS is the canonical approach. |

**Pattern cue:** "Level by level" / "layer by layer" -> BFS with queue, snapshotting queue size per level.

---

## 3. APPROACH LADDER

### Approach 1 -- DFS with Depth Tracking
**Intuition:** Perform preorder DFS, passing the current depth. Maintain a list of lists. When visiting a node at depth `d`, append its value to `result[d]`. Create a new list when entering a new depth for the first time.

**Steps:**
1. Initialize `result = []`.
2. Define `dfs(node, depth)`:
   - If node is null, return.
   - If `depth == len(result)`, append a new empty list.
   - Append `node.val` to `result[depth]`.
   - `dfs(node.left, depth + 1)`.
   - `dfs(node.right, depth + 1)`.
3. Call `dfs(root, 0)`. Return result.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) recursion stack + O(n) result |

### BUD Transition
DFS works but is not the natural fit. BFS processes nodes in the exact order we want.

### Approach 2 -- BFS with Queue (Standard)
**Intuition:** Use a queue. At each level, snapshot the queue size to know how many nodes are in the current level. Process exactly that many nodes, adding their children to the queue.

**Steps:**
1. If root is null, return [].
2. Initialize queue with root. Initialize result = [].
3. While queue is not empty:
   - `levelSize = queue.size()`.
   - Create `currentLevel = []`.
   - For i = 0 to levelSize - 1:
     - Dequeue node.
     - Append `node.val` to `currentLevel`.
     - Enqueue node.left and node.right (if they exist).
   - Append `currentLevel` to result.
4. Return result.

**Dry-Run Trace -- Tree [3,9,20,null,null,15,7]:**

| Step | Queue (before) | Level Size | Nodes Processed | Values | Queue (after) |
|------|---------------|------------|-----------------|--------|---------------|
| 1 | [3] | 1 | 3 | [3] | [9, 20] |
| 2 | [9, 20] | 2 | 9, 20 | [9, 20] | [15, 7] |
| 3 | [15, 7] | 2 | 15, 7 | [15, 7] | [] |

Result: [[3], [9, 20], [15, 7]].

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node enqueued and dequeued once |
| Space | O(w) queue + O(n) result |

### Approach 3 -- Best (BFS)
O(n) is optimal since every node must be visited. BFS is the canonical, most readable approach for level-order.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(w) + O(n) for result. For a complete tree, w ~ n/2. |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** Each node is enqueued once and dequeued once. O(1) work per node.
- **O(w) queue space:** The queue holds at most one full level. The widest level of a complete binary tree has ~n/2 nodes.
- **O(n) result space:** The result stores all n node values grouped by level.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return [] |
| Single node | Return [[val]] |
| Left-skewed | Each level has one node: [[1],[2],[3],...] |
| Perfect tree | Last level has n/2 nodes |
| Two nodes | [[root], [child]] |

**Common mistakes:**
- Not snapshotting queue size before the inner loop. If you check `queue.size()` inside the loop, it changes as you add children.
- Forgetting to check for null children before enqueueing.
- In DFS approach: using inorder instead of preorder (still works but less intuitive left-to-right order).

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Return node values grouped by level, left to right.
- **Match:** BFS with queue, size snapshot per level.
- **Plan:** Standard BFS loop with level grouping.
- **Implement:** Queue-based iteration with inner loop bounded by level size.
- **Review:** Trace with a 3-level example.
- **Evaluate:** O(n) time, O(n) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| Reverse level order? | Append each level to front of result, or reverse at end. (LC #107) |
| Zigzag level order? | Alternate left-to-right and right-to-left per level. (LC #103) |
| Average of each level? | Compute average instead of collecting values. (LC #637) |
| Right side view? | Last element of each level. (LC #199) |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Zigzag Level Order (LC #103) | Level order with alternating direction |
| Right Side View (LC #199) | Last node per level = rightmost in BFS |
| Minimum Depth (LC #111) | BFS stops at first leaf |
| Vertical Order Traversal (LC #987) | BFS with column tracking |
| Binary Tree Level Order II (LC #107) | Reverse the result |
| N-ary Tree Level Order (LC #429) | Same BFS, iterate children list |
