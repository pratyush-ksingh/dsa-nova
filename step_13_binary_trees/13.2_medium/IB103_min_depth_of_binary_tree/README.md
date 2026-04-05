# Min Depth of Binary Tree

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #111**

---

## 1. UNDERSTAND

### Problem Statement
Given a binary tree, find its **minimum depth**. The minimum depth is the number of **nodes** along the shortest path from the root node down to the **nearest leaf** node. A leaf is a node with no children.

**Critical edge case:** A node with only one child is NOT a leaf. If root has only a left child, you cannot say min depth = 1 (the right side is null but that is not a leaf path).

### Analogy
Imagine you are in a **building shaped like a tree** and there is a fire alarm. You need to find the **nearest emergency exit** (leaf). You start at the root (lobby) and want the shortest number of floors to reach any exit. Crucially, a dead-end hallway (null child) is NOT an exit -- you must reach an actual door (leaf node). BFS naturally finds the first exit level-by-level.

### Key Observations
1. Unlike max depth, you cannot just do `min(left, right) + 1` at every node. **Aha:** If one child is null, you must go through the other child -- the null side has no leaf.
2. BFS is ideal: the first leaf you encounter is at the minimum depth. **Aha:** BFS stops early without visiting the entire tree.
3. For DFS: if one child is null, min depth = depth of the non-null child + 1. If both children exist, min depth = min(left, right) + 1. **Aha:** This three-case logic prevents the null-side trap.

### Examples

```
        3           Min Depth: 2 (path: 3->9)
       / \          Node 9 is a leaf at depth 2.
      9  20
        /  \
       15   7
```

```
        2           Min Depth: 5 (NOT 1!)
         \          Node 2 has only right child.
          3         Must go all the way to leaf 6.
           \
            4
             \
              5
               \
                6
```

| Input | Output |
|-------|--------|
| [3,9,20,null,null,15,7] | 2 |
| [2,null,3,null,4,null,5,null,6] | 5 |
| [1] | 1 |
| [1,2] | 2 |

### Constraints
- 0 <= number of nodes <= 10^5
- -1000 <= Node.val <= 1000

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive DFS) | Call stack | Postorder with three-case logic. Visits all nodes. |
| Optimal (BFS) | Queue | Stops at first leaf encountered = shortest path. |
| Best (BFS) | Queue | Early termination makes it fastest in practice. |

**Pattern cue:** "Shortest path / nearest X in tree" -> BFS (level order) with early termination.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive DFS
**Intuition:** Compute min depth recursively, but handle the single-child case carefully. If one child is null, the answer must come from the other side.

**Steps:**
1. If root is null, return 0.
2. If root has no left child, return `1 + minDepth(right)`.
3. If root has no right child, return `1 + minDepth(left)`.
4. Otherwise, return `1 + min(minDepth(left), minDepth(right))`.

**Dry-Run Trace -- Tree [2,null,3,null,4,null,5,null,6]:**

| Call | Node | Left | Right | Case | Return |
|------|------|------|-------|------|--------|
| f(6) | 6 | null | null | Leaf | 1 |
| f(5) | 5 | null | 6 | No left | 1 + f(6) = 2 |
| f(4) | 4 | null | 5 | No left | 1 + f(5) = 3 |
| f(3) | 3 | null | 4 | No left | 1 + f(4) = 4 |
| f(2) | 2 | null | 3 | No left | 1 + f(3) = **5** |

| Metric | Value |
|--------|-------|
| Time | O(n) -- visits every node |
| Space | O(h) recursion stack |

### BUD Transition
**Bottleneck:** DFS visits every node even if a shallow leaf exists. **Upgrade:** BFS stops at the first leaf found.

### Approach 2 -- BFS (Level-Order) with Early Termination
**Intuition:** Process nodes level by level. The first time we encounter a leaf, return the current depth. No need to explore deeper levels.

**Steps:**
1. If root is null, return 0.
2. Initialize queue with root, depth = 1.
3. While queue is not empty:
   - Process all nodes in current level.
   - If any node is a leaf, return depth.
   - Enqueue children of each node.
   - Increment depth.

**Dry-Run Trace -- Tree [3,9,20,null,null,15,7]:**

| Level | Depth | Nodes Processed | Leaf Found? | Action |
|-------|-------|-----------------|-------------|--------|
| 0 | 1 | [3] | No (has children) | Enqueue 9, 20 |
| 1 | 2 | [9, 20] | **Yes (node 9)** | **Return 2** |

BFS stops without exploring level 2 (15, 7).

| Metric | Value |
|--------|-------|
| Time | O(n) worst case, but often O(k) where k = nodes up to shallowest leaf |
| Space | O(w) where w = max width |

### Approach 3 -- Best (BFS with Early Termination)
BFS is the best practical approach. It can return without visiting the entire tree if a shallow leaf exists. Worst case (all leaves at same depth) is still O(n).

| Metric | Value |
|--------|-------|
| Time | O(n) worst, O(k) best where k << n |
| Space | O(w) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) DFS:** Must visit every node because the shallowest leaf could be anywhere (no early exit in DFS).
- **O(k) BFS best case:** If a leaf is at depth 2, BFS processes only the first 2 levels.
- **O(h) DFS space:** Recursion stack depth = tree height.
- **O(w) BFS space:** Queue holds at most one full level.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return 0 |
| Single node (leaf) | Return 1 |
| Left-skewed (no right children) | Min depth = n (go all the way down) |
| Right-skewed | Same as left-skewed |
| Only root has both children, rest skewed | Min depth = 2 |

**Common mistakes:**
- Using `min(left, right) + 1` without handling the one-child case. For tree `[1, 2]`, this incorrectly gives 1 (treating null right as depth 0).
- Treating null as a leaf. Null is the absence of a node, not a leaf node.
- In BFS: forgetting to check if both children are null (leaf check).

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Min depth = shallowest leaf. Node with one child is NOT a leaf.
- **Match:** BFS for "shortest path" problems; DFS works but visits everything.
- **Plan:** BFS with early termination at first leaf.
- **Implement:** Level-order traversal, leaf check at each node.
- **Review:** Trace the [2,null,3,...] edge case.
- **Evaluate:** O(n) worst, O(k) best. O(w) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| Max depth instead? | Simple recursion: `1 + max(left, right)`. (LC #104) |
| Count nodes at min depth? | BFS: count all leaves at the first level that has a leaf. |
| Balanced tree check? | Uses height at each node, related but different. (LC #110) |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Maximum Depth (LC #104) | Max instead of min, simpler recursion |
| Balanced Binary Tree (LC #110) | Uses height computation at each node |
| Path Sum (LC #112) | Also root-to-leaf with same leaf definition |
| Binary Tree Level Order (LC #102) | BFS infrastructure reused here |
| Shortest Path in Binary Matrix (LC #1091) | BFS for shortest path, same principle |

---

## Real-World Use Case
**Shortest click path in UI navigation:** UX tools like Hotjar analyze site navigation trees to find the minimum number of clicks to reach any page. The minimum depth of the navigation tree directly measures the best-case user experience -- shallow trees mean faster access to content.
