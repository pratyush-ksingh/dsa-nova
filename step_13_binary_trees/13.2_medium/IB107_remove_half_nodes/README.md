# Remove Half Nodes

> **Batch 3 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

## UNDERSTAND

### Problem Statement
Given a binary tree, remove all **half nodes** -- nodes that have exactly one child (either only a left child or only a right child). When removing a half node, replace it with its single child. Leaf nodes (zero children) and full nodes (two children) remain untouched. Return the root of the modified tree. *(InterviewBit)*

### Examples

| Input Tree | Output Tree | Explanation |
|-----------|-------------|-------------|
| `2(7(2,6(5,11)),5(null,9(4,null)))` | `2(7(2,6(5,11)),4)` | Node 5 (right only: 9) removed, node 9 (left only: 4) removed |
| `1(2(null,3),4)` | `1(3,4)` | Node 2 has only right child 3; 2 is removed, 3 takes its place |
| `1(null,2(null,3))` | `3` | Both 1 and 2 are half nodes; keep peeling until we reach leaf 3 |
| `1(2,3)` | `1(2,3)` | No half nodes exist; tree unchanged |

### Analogy
Think of a corporate org chart. A "half node" is a manager who has exactly one direct report. That middle-management layer adds no branching value -- you can eliminate the manager and promote the single report directly. You keep doing this recursively until every remaining manager either has zero reports (individual contributor / leaf) or two+ reports (genuine branch point).

### 3 Key Observations
1. **"Aha" -- Bottom-up recursion:** Process children first, then decide about the current node. If you process top-down, you might remove a node before its subtree is cleaned, requiring multiple passes.
2. **"Aha" -- A half node is simply replaced by its child:** You don't delete the node and then re-attach; the return value of the recursive call on the child becomes the replacement. The removed node just gets garbage collected.
3. **"Aha" -- The root itself can be a half node:** Don't assume the root survives. If root has one child, the root changes. This is why the function returns a TreeNode (the new root).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Queue + repeated passes | BFS, remove half nodes per pass | Simple but wasteful with multiple passes |
| Optimal | Recursion stack | Post-order DFS | Single pass, bottom-up, clean |
| Best | Explicit stack + parent tracking | Iterative post-order | Avoids recursion, same O(n) time |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Repeated BFS Passes
**Intuition:** Do a BFS pass, find and remove all half nodes, repeat until no half nodes remain. This handles cascading removals (removing one half node may reveal another).

**BUD Optimization:**
- **B**ottleneck: Multiple passes. In worst case (chain of n half nodes), need O(n) passes.
- **U**nnecessary: Can solve in one pass if we go bottom-up.
- **D**uplicate: Re-scanning the whole tree each pass.

**Steps:**
1. BFS the tree. For each node, check if its children are half nodes.
2. If a child is a half node, replace it with the child's only child.
3. Repeat BFS until a pass makes no changes.
4. Handle root separately (root might be a half node).

**Dry-run trace** with `1(2(null,3),4)`:
```
Pass 1: BFS finds node 2 is a half node (only right child 3).
  Replace: 1.left = 3 (instead of 2).
  Tree now: 1(3,4). No more half nodes.
Pass 2: No changes. Done.
Result: 1(3,4)
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) worst case -- O(n) passes x O(n) BFS each |
| Space | O(n) -- BFS queue |

---

### Approach 2: Optimal -- Recursive Post-Order DFS
**Intuition:** Process left and right subtrees first (bottom-up). After recursion, check the current node: if it has exactly one child, return that child (effectively removing current node). If it's a leaf or full node, return itself.

**BUD Optimization:**
- Single pass, bottom-up. Cascading removals handled automatically because children are already cleaned before parent is checked.

**Steps:**
1. If node is null, return null.
2. Recursively clean left subtree: `node.left = remove(node.left)`.
3. Recursively clean right subtree: `node.right = remove(node.right)`.
4. If node has only left child (right is null), return `node.left`.
5. If node has only right child (left is null), return `node.right`.
6. Otherwise (leaf or full node), return `node`.

**Dry-run trace** with `1(null,2(null,3))`:
```
remove(1):
  1.left = remove(null) = null
  1.right = remove(2):
    2.left = remove(null) = null
    2.right = remove(3):
      3.left = null, 3.right = null -> leaf, return 3
    Now 2: left=null, right=3 -> half node! Return 3
  Now 1: left=null, right=3 -> half node! Return 3
Result: root = 3
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node exactly once |
| Space | O(h) -- recursion depth |

---

### Approach 3: Best -- Iterative Post-Order with Parent Tracking
**Intuition:** Same logic as optimal but without recursion. Use iterative post-order traversal. After processing a node, if it's a half node, tell its parent to replace the pointer.

**Steps:**
1. Use a stack for iterative post-order traversal.
2. Maintain a parent map (child -> parent + direction).
3. When visiting a node (post-order), check if it's a half node.
4. If yes, tell its parent to point to the half node's only child.
5. Handle root specially.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) -- parent map + stack |

Note: The recursive approach (Approach 2) is the cleanest and most commonly expected in interviews. The iterative version trades elegance for explicit stack control.

---

## COMPLEXITY INTUITIVELY

**Why O(n) is optimal:** Every node must be examined at least once to determine whether it's a half node. You can't skip any node since half nodes can appear anywhere.

**Why bottom-up works in one pass:** When you process children first, by the time you check the current node, its subtrees are already clean. So if removing the current node exposes a new half node, that node was already cleaned by a deeper recursive call.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `null` | Base case |
| Single node (leaf) | Same node | Leaf is not a half node |
| Root is a half node | Root changes | Must return new root |
| Chain of half nodes | Collapse to leaf | e.g., `1->2->3->4` becomes `4` |
| All full nodes | Tree unchanged | No removals needed |
| Half node deep inside | Only that node removed | Don't disturb rest of tree |

### Common Mistakes
- Forgetting that the root itself can be a half node (returning the old root).
- Processing top-down instead of bottom-up (missing cascading removals).
- Confusing "half node" with "leaf" -- a leaf has ZERO children, not one.
- Not updating the parent's pointer to skip the removed node.

---

## INTERVIEW LENS

**Frequency:** Medium -- popular on InterviewBit, tests tree modification skills.
**Follow-ups the interviewer might ask:**
- "What if you also remove leaf nodes?" (Keep removing until only full nodes remain)
- "Can you do it iteratively?" (Approach 3 -- more complex but shows depth)
- "What if the tree is a BST -- does removal preserve BST property?" (Yes, because we only remove intermediate nodes, not rearranging values)

**What they're really testing:** Can you modify a tree in-place using recursion? Do you understand post-order processing for tree mutations?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Delete Leaves with Given Value (LC #1325) | Similar bottom-up deletion, but for leaves |
| Prune Binary Tree (LC #814) | Remove subtrees that don't contain 1 |
| Flatten Binary Tree to Linked List (LC #114) | Another tree restructuring problem |
| Same Tree (LC #100) | Useful to verify result matches expected |

### Real-World Use Case
**DOM tree simplification:** In web rendering engines, wrapper elements that contain a single child with no additional styling can be "collapsed" to simplify the DOM tree. This reduces rendering overhead and memory usage -- exactly the same pattern as removing half nodes.
