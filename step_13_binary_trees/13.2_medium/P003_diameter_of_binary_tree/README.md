# Diameter of Binary Tree

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #543**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return the length of the **diameter** of the tree. The diameter is the **longest path** between any two nodes in the tree. This path may or may not pass through the root. The length of a path is measured by the **number of edges** between nodes.

### Analogy
Think of a tree-shaped **road network** between villages. The diameter is the longest drive you would ever need to make between any two villages, sticking to tree roads. For any junction (node), the farthest drive *through* that junction is the depth of its left road plus the depth of its right road. The overall diameter is the maximum such drive across all junctions.

### Key Observations
1. The diameter through a specific node = `height(left) + height(right)` (in edges). **Aha:** The longest path through node X uses the deepest leaf on each side.
2. The overall diameter is the maximum of "diameter through node" across ALL nodes. **Aha:** It does not necessarily pass through the root -- it could be entirely in one subtree.
3. Height computation is postorder. We can track the global max diameter while computing heights. **Aha:** Same single-pass postorder pattern as balanced tree check.

### Examples

```
        1          Diameter: 3 (path: 4->2->1->3)
       / \
      2   3
     / \
    4   5
```

```
        1          Diameter: 1 (path: 1->2)
       /
      2
```

| Input | Output |
|-------|--------|
| [1,2,3,4,5] | 3 |
| [1,2] | 1 |
| [1] | 0 |
| [] | 0 |

### Constraints
- 0 <= number of nodes <= 10^4
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (top-down) | Call stack + height function | At each node, compute diameter = height(left) + height(right). Take global max. |
| Optimal (bottom-up) | Call stack + variable | Single postorder pass: compute height, update max diameter. |
| Best (same as optimal) | Call stack | Cannot beat O(n). |

**Pattern cue:** "Longest path in tree" -> postorder DFS tracking a global maximum while computing heights.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Top-Down)
**Intuition:** At each node, compute the diameter passing through it as `height(left) + height(right)`. Check every node and take the maximum.

**Steps:**
1. Define `height(node)`: if null return 0, else `1 + max(height(left), height(right))`.
2. For each node, compute `diameter_through = height(left) + height(right)`.
3. Return `max(diameter_through, diameter(left), diameter(right))`.

| Metric | Value |
|--------|-------|
| Time | O(n^2) worst case -- height recomputed at every node |
| Space | O(h) recursion stack |

### BUD Transition
**Bottleneck:** Redundant height computation. **Upgrade:** Compute height bottom-up and track diameter as a side effect.

### Approach 2 -- Optimal (Bottom-Up Single Pass)
**Intuition:** During postorder height computation, at each node the "diameter through this node" is `leftHeight + rightHeight`. Track the global max in a variable.

**Steps:**
1. Initialize `maxDiameter = 0`.
2. Define `height(node)`:
   - If null, return 0.
   - `leftH = height(left)`, `rightH = height(right)`.
   - Update `maxDiameter = max(maxDiameter, leftH + rightH)`.
   - Return `1 + max(leftH, rightH)`.
3. Call `height(root)`. Return `maxDiameter`.

**Dry-Run Trace -- Tree [1,2,3,4,5]:**

| Call | Node | leftH | rightH | leftH+rightH | maxDiameter | Return (height) |
|------|------|-------|--------|---------------|-------------|-----------------|
| height(4) | 4 | 0 | 0 | 0 | 0 | 1 |
| height(5) | 5 | 0 | 0 | 0 | 0 | 1 |
| height(2) | 2 | 1 | 1 | 2 | 2 | 2 |
| height(3) | 3 | 0 | 0 | 0 | 2 | 1 |
| height(1) | 1 | 2 | 1 | **3** | **3** | 3 |

Result: maxDiameter = **3**.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node visited once |
| Space | O(h) recursion stack |

### Approach 3 -- Best (Same as Optimal)
O(n) is optimal since the diameter could pass through any node; all must be checked.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- O(log n) balanced, O(n) skewed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time:** Every node visited once in postorder. Height and diameter update are O(1) per node.
- **O(n^2) brute:** For a skewed tree, height() at each of n nodes takes O(n) work.
- **O(h) space:** Recursion stack depth = tree height.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null) | Diameter = 0 |
| Single node | Diameter = 0 (no edges) |
| Straight line (skewed) | Diameter = n-1 edges |
| Diameter not through root | Bottom-up catches it because we check every node |
| All nodes on one side | Left or right height is 0 at root |

**Common mistakes:**
- Returning number of nodes instead of edges (off by 1). Diameter in edges = leftH + rightH if height counts nodes.
- Only checking diameter at the root (misses paths entirely in a subtree).
- Confusing height (nodes) vs depth (edges). Be consistent: if height(null)=0 and height(leaf)=1, then diameter = leftH + rightH gives edge count automatically.

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Diameter = longest path in edges. May not pass through root.
- **Match:** Postorder DFS computing height, tracking global max.
- **Plan:** Single function, global/instance variable for max.
- **Implement:** Clean recursive with height return + side-effect update.
- **Review:** Trace through example showing diameter not at root.
- **Evaluate:** O(n) time, O(h) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| What if you need the actual path? | Track the nodes during recursion, backtrack to build the path. |
| Diameter of N-ary tree? | Same idea: sum of two largest child heights at each node. |
| What about weighted edges? | Use DFS from any node, then DFS from the farthest node found (two-BFS trick). |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Height of Binary Tree (LC #104) | Diameter builds on height computation |
| Balanced Binary Tree (LC #110) | Same postorder + side-effect pattern |
| Maximum Path Sum (LC #124) | Diameter of values instead of edges |
| Longest Univalue Path (LC #687) | Diameter with value constraint |
| Binary Tree Maximum Width (LC #662) | Another "longest" metric on trees |

---

## Real-World Use Case
**Network latency analysis:** In a data center network topology (modeled as a tree), the diameter represents the longest path between any two servers -- i.e., the worst-case communication latency. AWS uses this metric to optimize placement of services, ensuring no two dependent services are too far apart.
