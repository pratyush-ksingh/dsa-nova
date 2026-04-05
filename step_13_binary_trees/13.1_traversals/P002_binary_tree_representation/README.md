# Binary Tree Representation

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Implement two ways to represent a binary tree:
1. **Array (sequential) representation:** Store nodes in an array using index arithmetic. For node at index `i`: left child = `2i + 1`, right child = `2i + 2`, parent = `(i - 1) / 2`.
2. **Linked node representation:** Each node is an object with `val`, `left`, `right` pointers.

Implement conversion between both representations.

### Analogy
Think of two ways to organize **books on shelves**:
- **Array representation:** A numbered shelf system. Book #0 is at position 0, its "children" are at positions 1 and 2. Like a heap, the position tells you the relationship. Empty slots waste space.
- **Linked representation:** Each book has a sticky note pointing to its two related books. No wasted space, but you need to follow the chain to find anything.

### Key Observations
1. Array representation is **perfect for complete binary trees** (like heaps) because no space is wasted. **Aha:** A heap is just an array with implicit tree structure via index math.
2. For **sparse/unbalanced trees**, array representation wastes exponential space. A right-skewed tree of height h needs 2^(h+1) - 1 array slots but only h + 1 nodes. **Aha:** Linked representation uses exactly n node objects.
3. Converting from array to linked uses **BFS** (queue-based, level by level). Converting from linked to array also uses **BFS**. **Aha:** Level-order traversal naturally maps to array indices.

### Examples

**Tree:**
```
      1
     / \
    2   3
   / \
  4   5
```

**Array representation:** `[1, 2, 3, 4, 5, null, null]`
- Index 0: val=1 (root)
- Index 1: val=2 (left of 0)
- Index 2: val=3 (right of 0)
- Index 3: val=4 (left of 1)
- Index 4: val=5 (right of 1)
- Index 5,6: null (no children of node 3)

**Linked representation:**
```
TreeNode(1) -> left: TreeNode(2), right: TreeNode(3)
TreeNode(2) -> left: TreeNode(4), right: TreeNode(5)
TreeNode(3) -> left: null,        right: null
```

### Constraints
- 0 <= n <= 10^4
- -10^4 <= Node.val <= 10^4

---

## 2. DS & ALGO CHOICE

| Approach | Representation | When to Use |
|----------|---------------|-------------|
| Array-based | Integer array with null markers | Complete/near-complete trees (heaps, segment trees) |
| Linked nodes | TreeNode objects with pointers | General trees, especially unbalanced ones |
| Conversion: Array -> Linked | BFS with queue | LeetCode input parsing |
| Conversion: Linked -> Array | BFS with index tracking | Serialization / debugging |

**Pattern cue:** LeetCode gives trees as arrays. Know how to build linked representation from that format.

---

## 3. APPROACH LADDER

### Approach 1 -- Array Representation
**Intuition:** Store the tree level by level in an array. Use index math for parent-child navigation.

**Steps:**
1. Root at index 0.
2. For node at index `i`: left child at `2*i + 1`, right child at `2*i + 2`.
3. Parent of index `i` (i > 0): `(i - 1) / 2`.
4. Use `null` / sentinel for absent nodes.

**Index relationships for tree [1,2,3,4,5]:**

| Index | Value | Parent Index | Left Child | Right Child |
|-------|-------|-------------|------------|-------------|
| 0 | 1 | none (root) | 1 | 2 |
| 1 | 2 | 0 | 3 | 4 |
| 2 | 3 | 0 | 5 | 6 |
| 3 | 4 | 1 | 7 | 8 |
| 4 | 5 | 1 | 9 | 10 |

| Metric | Value |
|--------|-------|
| Time -- access parent/child | O(1) |
| Space | O(2^(h+1)) worst case |

### Approach 2 -- Linked Node Representation
**Intuition:** Each node stores its value and pointers to left and right children.

| Metric | Value |
|--------|-------|
| Time -- access child | O(1) |
| Space | O(n) exactly |

### Approach 3 -- Conversion Functions

**Array -> Linked (BFS):**
1. Create root from `arr[0]`.
2. Enqueue root. Index pointer `i = 1`.
3. Dequeue node: assign `arr[i]` as left child, `arr[i+1]` as right child. Enqueue non-null children. Advance i by 2.

**Linked -> Array (BFS):**
1. BFS traversal. For each node at logical index `idx`, set `arr[idx] = node.val`.
2. Enqueue children with indices `2*idx+1` and `2*idx+2`.
3. Fill gaps with null.

**Dry-Run Trace -- Array [1,2,3,4,5] -> Linked:**

| Step | Queue Front | i | Action |
|------|------------|---|--------|
| 1 | Node(1) | 1 | left=Node(2), right=Node(3), enqueue both |
| 2 | Node(2) | 3 | left=Node(4), right=Node(5), enqueue both |
| 3 | Node(3) | 5 | i>=len, no children |
| 4 | Node(4) | 5 | i>=len, no children |
| 5 | Node(5) | 5 | i>=len, no children |

| Metric | Value |
|--------|-------|
| Time | O(n) for both conversions |
| Space | O(n) for the output + O(w) for the queue |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n)** for conversions: visit each node exactly once in BFS.
- **O(1)** for parent/child access in array form: pure arithmetic, no traversal.
- Array wastes space for sparse trees: a right-skewed tree of n nodes needs O(2^n) array space.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return empty array or null |
| Single node | Array = [val], linked = TreeNode(val) |
| Sparse tree with many nulls | Array has many null entries; linked is compact |
| Complete binary tree | Array has no wasted space -- ideal case |

**Common mistakes:**
- Using 1-indexed formulas (left = 2i, right = 2i+1) with 0-indexed arrays.
- Not handling null entries when converting sparse array to linked nodes.
- Forgetting to enqueue null children properly (they should not be enqueued but their array positions should be skipped).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| When is array better? | Complete trees like heaps. Cache-friendly, no pointer overhead. |
| When is linked better? | Sparse trees where array would waste massive space. |
| How does LeetCode encode trees? | Level-order array with null for missing nodes. |
| Space comparison? | Array: O(2^h). Linked: O(n). For balanced, both ~O(n). For skewed, array is O(2^n). |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Introduction to Heaps | Heaps use array representation exclusively |
| Level Order Traversal (LC #102) | BFS produces the array representation naturally |
| Serialize/Deserialize Tree (LC #297) | Converting between string and linked representation |
| Segment Tree | Uses array representation for range queries |

---

## Real-World Use Case
**JSON/XML parsing engines:** Libraries like Jackson (Java) and `json` (Python) parse nested data into a tree representation. Each JSON object becomes a node whose children are its key-value pairs. This tree representation enables O(depth) lookups and powers every REST API response parser.
