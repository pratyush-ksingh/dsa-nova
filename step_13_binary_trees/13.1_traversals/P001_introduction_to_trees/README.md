# Introduction to Trees

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Understand the fundamental concepts of **tree data structures**. Implement a **binary tree node** and build a simple binary tree. Learn core terminology: root, parent, child, leaf, depth, height, subtree, level.

A **tree** is a connected, acyclic graph. A **binary tree** is a tree where each node has at most two children (left and right).

### Analogy
A tree is like a **family genealogy chart**. The founder (root) is at the top. Each person (node) can have at most two children. People with no children are **leaves**. The number of generations from the founder to the furthest descendant is the **height** of the tree. Any person along with all their descendants forms a **subtree**.

### Key Observations
1. Trees are **recursive** by nature: every subtree rooted at any node is itself a valid tree. **Aha:** This recursive structure makes recursion the most natural way to process trees.
2. A binary tree with `n` nodes has exactly `n - 1` edges. **Aha:** Each node except the root has exactly one parent edge, giving n - 1 total edges.
3. The maximum number of nodes at level `d` is `2^d`, and a complete binary tree of height `h` has `2^(h+1) - 1` nodes. **Aha:** Trees give logarithmic height for balanced structures, which is why BSTs, heaps, and segment trees are so efficient.

### Key Terminology

| Term | Definition |
|------|-----------|
| Root | The topmost node (no parent) |
| Parent | The node directly above in the hierarchy |
| Child | A node directly below (left child, right child) |
| Leaf | A node with no children |
| Depth | Number of edges from root to the node |
| Height | Number of edges on the longest path from node to a leaf |
| Level | Set of all nodes at the same depth |
| Subtree | A node and all its descendants |
| Degree | Number of children a node has (0, 1, or 2 for binary) |

### Example Tree
```
        1          <- root, depth=0, height=2
       / \
      2   3        <- depth=1
     / \
    4   5          <- leaves, depth=2, height=0
```
- Root: 1
- Leaves: 3, 4, 5
- Height of tree: 2
- Parent of 4: 2
- Subtree rooted at 2: {2, 4, 5}

### Constraints
- This is a conceptual / learning problem. No LeetCode submission.

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Linked nodes | TreeNode with val, left, right | Standard representation; flexible for any shape. O(1) insert at known position. |
| Array representation | Array where index i has children at 2i+1, 2i+2 | Great for complete trees (heaps). Wastes space for sparse trees. |

**Pattern cue:** Almost all tree problems use the linked-node representation with recursive algorithms.

---

## 3. APPROACH LADDER

### Approach 1 -- Build Tree Manually (Direct Construction)
**Intuition:** Create nodes one by one and link them via left/right pointers.

**Steps:**
1. Define a `TreeNode` class with `val`, `left`, `right`.
2. Create the root node.
3. Set `root.left` and `root.right` to child nodes.
4. Continue linking until the tree is built.

**Dry-Run: Building the example tree [1,2,3,4,5]:**

| Step | Action | Tree State |
|------|--------|-----------|
| 1 | Create node(1) | root = 1 |
| 2 | Create node(2), root.left = 2 | 1->left=2 |
| 3 | Create node(3), root.right = 3 | 1->right=3 |
| 4 | Create node(4), node(2).left = 4 | 2->left=4 |
| 5 | Create node(5), node(2).right = 5 | 2->right=5 |

### Approach 2 -- Build Tree from Array (Level-Order)
**Intuition:** Given a level-order array (like LeetCode format), use a queue to build the tree.

**Steps:**
1. Create root from `arr[0]`.
2. Use a queue. Enqueue root.
3. For each dequeued node, assign left child from next array element, then right child.
4. Enqueue non-null children. Use `null` / `-1` for missing nodes.

| Metric | Value |
|--------|-------|
| Time | O(n) to build |
| Space | O(n) for the tree + O(w) for the queue where w = max width |

### Approach 3 -- Utility Functions
Implement helper functions: `countNodes`, `height`, `isLeaf`, `printTree` to validate the tree.

| Metric | Value |
|--------|-------|
| Time | O(n) per traversal |
| Space | O(h) recursion stack |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n)** for any full traversal: you must visit every node at least once.
- **O(h)** recursion stack space: recursion depth equals the height. For balanced trees h = log n; for skewed trees h = n.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (null root) | Return 0 for count/height, handle null checks |
| Single node tree | Height = 0, it is both root and leaf |
| Skewed tree (linked list) | Height = n-1, all operations degrade to linear |
| Complete vs. full vs. perfect | Know the distinctions (interview vocab) |

**Tree types:**
- **Full:** Every node has 0 or 2 children.
- **Complete:** All levels filled except possibly last, which is filled left to right.
- **Perfect:** All internal nodes have 2 children and all leaves at same level.
- **Balanced:** Height is O(log n).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Why trees over arrays? | Trees provide O(log n) search/insert in balanced form; arrays need O(n) for insert. |
| Tree vs. graph? | Tree is acyclic connected graph with n-1 edges. No cycles, unique path between any two nodes. |
| When are trees bad? | When they become skewed (degenerate to linked list). Self-balancing trees (AVL, Red-Black) fix this. |
| Most important tree skill? | Recursive thinking: base case (null node) + recursive case (process left/right subtrees). |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Binary Tree Representation | Array vs. linked node representation |
| Inorder/Preorder/Postorder Traversal | Fundamental ways to visit all nodes |
| Height of Binary Tree | Direct application of tree recursion |
| BST Search | Trees + ordering constraint = efficient search |
| Heap | Complete binary tree stored as array |
