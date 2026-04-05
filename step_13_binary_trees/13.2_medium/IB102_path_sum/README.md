# Path Sum

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #112**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree and an integer `targetSum`, return `true` if the tree has a **root-to-leaf** path such that adding up all the values along the path equals `targetSum`. A **leaf** is a node with no children.

### Analogy
Imagine a **toll road system** shaped like a tree. You start at the root (highway entrance) and drive toward any leaf (exit). Each node is a toll booth that charges its value. You want to know: is there any route from entrance to exit where the total toll is exactly `targetSum`? You must reach an actual exit (leaf) -- stopping at an internal junction does not count.

### Key Observations
1. We need to check **root-to-leaf** paths only. A node with one child is NOT a leaf. **Aha:** The check `node.left == null && node.right == null` defines a leaf.
2. Instead of accumulating the sum going down, we can **subtract** the current node's value from targetSum. At a leaf, check if the remainder is 0. **Aha:** This avoids carrying an extra accumulator parameter.
3. BFS can also solve this by carrying `(node, remainingSum)` pairs in the queue. **Aha:** BFS finds the answer at the shallowest matching leaf first.

### Examples

```
         5             targetSum = 22
        / \            Path: 5->4->11->2 = 22
       4   8           Return: true
      /   / \
     11  13   4
    /  \       \
   7    2       1
```

```
    1              targetSum = 5
   / \             No path sums to 5.
  2   3            Paths: 1->2 = 3, 1->3 = 4
                   Return: false
```

| Input | Output |
|-------|--------|
| [5,4,8,11,null,13,4,7,2,null,null,null,1], target=22 | true |
| [1,2,3], target=5 | false |
| [1,2], target=1 | false (node 1 is not a leaf, it has child 2) |
| [], target=0 | false (empty tree has no leaves) |
| [1], target=1 | true |

### Constraints
- 0 <= number of nodes <= 5000
- -1000 <= Node.val <= 1000
- -1000 <= targetSum <= 1000

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (DFS recursive) | Call stack | Natural recursion: subtract val, check leaf. |
| Optimal (BFS iterative) | Queue | Level-by-level; finds shallowest match first. |
| Best (same as DFS recursive) | Call stack | O(n) is optimal; DFS uses O(h) space vs O(w) for BFS. |

**Pattern cue:** "Does any root-to-leaf path satisfy X?" -> DFS with target reduction.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive DFS (Subtract Target)
**Intuition:** At each node, subtract its value from targetSum. At a leaf, check if remainder is 0. Recurse left and right; return true if either side finds a valid path.

**Steps:**
1. If root is null, return false.
2. Subtract `root.val` from `targetSum`.
3. If root is a leaf (`left == null && right == null`), return `targetSum == 0`.
4. Return `hasPathSum(left, targetSum) OR hasPathSum(right, targetSum)`.

**Dry-Run Trace -- Tree [5,4,8,11,null,13,4,7,2,null,null,null,1], target=22:**

| Call | Node | Remaining | Leaf? | Action |
|------|------|-----------|-------|--------|
| f(5, 22) | 5 | 17 | No | Recurse left(4,17), right(8,17) |
| f(4, 17) | 4 | 13 | No | Recurse left(11,13) |
| f(11, 13) | 11 | 2 | No | Recurse left(7,2), right(2,2) |
| f(7, 2) | 7 | -5 | Leaf | -5 != 0 -> false |
| f(2, 2) | 2 | **0** | Leaf | **0 == 0 -> true** |

Result: **true** (path 5->4->11->2).

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node at most once |
| Space | O(h) recursion stack |

### BUD Transition
DFS is already O(n). BFS provides an iterative alternative.

### Approach 2 -- Iterative BFS
**Intuition:** Use a queue storing `(node, remainingSum)`. At each leaf, check if remaining is 0.

**Steps:**
1. If root is null, return false.
2. Enqueue `(root, targetSum - root.val)`.
3. While queue is not empty:
   - Dequeue `(node, remaining)`.
   - If node is a leaf and remaining == 0, return true.
   - Enqueue left child with `remaining - left.val` (if exists).
   - Enqueue right child with `remaining - right.val` (if exists).
4. Return false.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(w) where w = max width |

### Approach 3 -- Best (Same as Recursive DFS)
O(n) time cannot be improved (must potentially check all paths). DFS uses O(h) space which is better than BFS's O(w) for deep narrow trees.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- O(log n) balanced, O(n) skewed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** In the worst case, no path sums to target, so we visit every node.
- **O(h) DFS space:** At most h frames on the call stack, one per level from root to current node.
- **O(w) BFS space:** Queue holds nodes at the widest level. For a complete tree, last level has ~n/2 nodes.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null) | Return false (no leaves exist) |
| Single node = target | Return true (single node is a leaf) |
| Single node != target | Return false |
| Negative values | Subtraction handles naturally |
| Node with one child | NOT a leaf -- do not check sum here |
| targetSum = 0, tree = [0] | Return true |

**Common mistakes:**
- Checking sum at internal nodes (not just leaves). `[1,2]` with target=1 should return false because node 1 has a child.
- Returning true for empty tree with targetSum=0. Empty tree has no root-to-leaf path.
- Forgetting that values can be negative, so early termination (sum > target) is wrong.

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Root-to-leaf only. Leaf = no children. Empty tree = false.
- **Match:** DFS with target subtraction at each level.
- **Plan:** Recursive: subtract val, check at leaf.
- **Implement:** 4-line recursive function.
- **Review:** Trace the [1,2] target=1 edge case.
- **Evaluate:** O(n) time, O(h) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| Find all root-to-leaf paths with given sum? | Backtracking: maintain current path list. (LC #113) |
| Path sum starting/ending anywhere? | Prefix sum + DFS. (LC #437) |
| Return the path itself? | Carry a list, append on recurse, pop on backtrack. |
| What about root-to-any-node sum? | Remove the leaf check; check at every node. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Path Sum II (LC #113) | Find all paths, not just existence |
| Path Sum III (LC #437) | Any-to-any path, prefix sums |
| Sum Root to Leaf Numbers (LC #129) | Accumulate digit-number along path |
| Binary Tree Maximum Path Sum (LC #124) | Max sum path, any direction |
| Min Depth of Binary Tree (LC #111) | Same leaf-checking pattern |

---

## Real-World Use Case
**Network routing cost analysis:** Cisco routers compute whether a path exists between two nodes with a total cost under a threshold. Each edge has a weight (latency), and checking if a root-to-leaf path sums to a target mirrors how OSPF routing protocols validate feasible paths through a network topology.
