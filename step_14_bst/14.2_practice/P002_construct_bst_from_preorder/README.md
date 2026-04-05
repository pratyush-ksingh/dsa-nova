# Construct BST from Preorder Traversal

> **Batch 3 of 12** | **Topic:** Binary Search Trees | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given an array of integers `preorder` representing the preorder traversal of a BST, construct and return the root of the BST. It is guaranteed that there always exists a valid BST for the given preorder sequence. *(LeetCode #1008)*

### Examples

| Input | Output (Preorder) | Explanation |
|-------|-------------------|-------------|
| `[8,5,1,7,10,12]` | `[8,5,1,7,10,12]` | Root=8, left subtree [5,1,7], right subtree [10,12] |
| `[1,3]` | `[1,3]` | Root=1, right child=3 (3>1) |
| `[3,1]` | `[3,1]` | Root=3, left child=1 (1<3) |
| `[5]` | `[5]` | Single node |

### Analogy
Think of receiving arrival times at a security checkpoint. The first person is the boss (root). Everyone who arrives after with a smaller ID goes to the left wing, and everyone with a larger ID goes to the right wing. Within each wing, the first arrival is the sub-boss, and the same rule applies recursively. The "upper bound" approach is like the boss telling each wing: "don't accept anyone with an ID above this threshold."

### 3 Key Observations
1. **"Aha" -- First element is always the root:** In preorder (Root-Left-Right), the first element is the root. All subsequent smaller elements form the left subtree, all larger elements form the right subtree.
2. **"Aha" -- Upper bound trick eliminates searching:** Instead of scanning for the split point between left and right subtrees (O(n) per node), pass an upper bound to the recursive call. If the next element exceeds the bound, that subtree is done. This gives O(n) total.
3. **"Aha" -- A global index pointer advances linearly:** Use a single index that moves forward through the preorder array. Each element is consumed exactly once. The upper bound determines WHICH recursive call gets to consume the next element.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | BST insert | Insert elements one by one | Simple but O(n^2) for sorted input |
| Optimal | Recursion + split point | Find partition, recurse on halves | O(n log n) average |
| Best | Recursion + upper bound | Global index with bound checking | O(n) guaranteed |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Sequential BST Insertion
**Intuition:** The simplest approach: create an empty BST and insert elements one by one in the order they appear in the preorder array. Standard BST insertion.

**Steps:**
1. Create root from `preorder[0]`.
2. For each remaining element, do standard BST insert (walk left if smaller, right if larger).

**Dry-run trace** with `[8,5,1,7,10,12]`:
```
Insert 8: root = 8
Insert 5: 5 < 8, go left. 8.left = 5
Insert 1: 1 < 8, go left. 1 < 5, go left. 5.left = 1
Insert 7: 7 < 8, go left. 7 > 5, go right. 5.right = 7
Insert 10: 10 > 8, go right. 8.right = 10
Insert 12: 12 > 8, go right. 12 > 10, go right. 10.right = 12
```

| Metric | Value |
|--------|-------|
| Time | O(n*h) -- O(n log n) average, O(n^2) for sorted/skewed input |
| Space | O(h) -- tree height for insertion recursion |

---

### Approach 2: Optimal -- Recursive Partition
**Intuition:** First element is root. Scan to find where elements transition from "less than root" to "greater than root." Elements before the transition form the left subtree's preorder; elements after form the right subtree's preorder. Recurse on each half.

**BUD Optimization:**
- **B**ottleneck: Finding the partition point takes O(n) per level, leading to O(n log n) total for balanced trees (and O(n^2) for skewed).
- Can use binary search on the partition point to stay O(n log n).

**Steps:**
1. If array is empty, return null.
2. Root = first element.
3. Find index `i` where `preorder[i] > root.val` (partition point).
4. Left subtree = recurse on `preorder[1..i-1]`.
5. Right subtree = recurse on `preorder[i..end]`.

**Dry-run trace** with `[8,5,1,7,10,12]`:
```
Root=8. Partition at index 4 (10>8).
  Left: [5,1,7]. Root=5. Partition at index 2 (7>5? No, 7 is right... wait, 7>5). Partition at idx 2.
    Left: [1]. Root=1. No children.
    Right: [7]. Root=7. No children.
  Right: [10,12]. Root=10. Partition at idx 1 (12>10).
    Left: empty.
    Right: [12]. Root=12.
```

| Metric | Value |
|--------|-------|
| Time | O(n log n) average, O(n^2) worst (skewed) |
| Space | O(n) for subarrays + O(h) recursion |

---

### Approach 3: Best -- Upper Bound Recursion (O(n))
**Intuition:** Use a global index pointer and an upper bound. Each call creates a node from the current element if it's within the bound, then recursively builds left (with bound = current node's value) and right (with bound = parent's bound). The key insight: each element is consumed exactly once as the index only moves forward.

**Steps:**
1. Initialize global index `i = 0`.
2. `build(bound)`:
   - If `i >= n` or `preorder[i] > bound`, return null (this subtree is done).
   - Create node with `preorder[i]`. Increment `i`.
   - `node.left = build(node.val)` (left subtree: values must be < node).
   - `node.right = build(bound)` (right subtree: values must be < parent's bound).
   - Return node.
3. Call `build(infinity)`.

**Dry-run trace** with `[8,5,1,7,10,12]`:
```
build(INF): i=0, preorder[0]=8 <= INF. Create 8, i=1.
  8.left = build(8): i=1, preorder[1]=5 <= 8. Create 5, i=2.
    5.left = build(5): i=2, preorder[2]=1 <= 5. Create 1, i=3.
      1.left = build(1): i=3, preorder[3]=7 > 1. Return null.
      1.right = build(5): i=3, preorder[3]=7 > 5. Return null.
    5.right = build(8): i=3, preorder[3]=7 <= 8. Create 7, i=4.
      7.left = build(7): i=4, preorder[4]=10 > 7. Return null.
      7.right = build(8): i=4, preorder[4]=10 > 8. Return null.
  8.right = build(INF): i=4, preorder[4]=10 <= INF. Create 10, i=5.
    10.left = build(10): i=5, preorder[5]=12 > 10. Return null.
    10.right = build(INF): i=5, preorder[5]=12 <= INF. Create 12, i=6.
      12.left = build(12): i=6 >= n. Return null.
      12.right = build(INF): i=6 >= n. Return null.

Result: 8(5(1,7), 10(null,12))
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each element consumed exactly once |
| Space | O(h) -- recursion depth only |

---

## COMPLEXITY INTUITIVELY

**Why O(n) for the upper bound approach:** The global index `i` only moves forward, never backward. Each of the `n` elements is processed in exactly one recursive call. The bound just determines WHICH call gets to process the next element.

**Why O(n^2) is possible for naive approaches:** If the tree is skewed (sorted input), each insertion walks the entire height of the tree (which is O(n)), giving O(n) * O(n) = O(n^2).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Single element | Single node | Simplest BST |
| Sorted ascending | Right-skewed tree | O(n^2) for brute force |
| Sorted descending | Left-skewed tree | Same issue |
| Two elements | Root + one child | Direction depends on comparison |
| All same values | Not valid BST preorder | Problem guarantees valid input |

### Common Mistakes
- Forgetting to use a global/mutable index (using a local int that doesn't persist across calls).
- Setting wrong upper bounds: left subtree bound should be `node.val`, right subtree bound should be `parent_bound`.
- In Python, using `int` as index (immutable) -- need list `[0]` or `nonlocal`.
- Not handling the empty array case.

---

## INTERVIEW LENS

**Frequency:** High -- classic BST construction problem.
**Follow-ups the interviewer might ask:**
- "What if you're given inorder + preorder?" (Different problem -- use hash map for O(n))
- "Can you do it iteratively?" (Use a stack with monotonic property)
- "What about constructing from postorder?" (Same bound trick, iterate in reverse)
- "Prove your solution is O(n)" (Point to the global index advancing monotonically)

**What they're really testing:** Understanding of BST property + ability to design O(n) tree construction using bound-based recursion. This pattern appears in many tree-construction problems.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Construct BT from Preorder + Inorder (LC #105) | Similar partition idea, but needs inorder for split |
| Validate BST (LC #98) | Same upper/lower bound pattern |
| Serialize/Deserialize BST (LC #449) | Uses preorder; reconstruction = this problem |
| Convert Sorted Array to BST (LC #108) | Different input (sorted), uses midpoint split |

### Real-World Use Case
**Database index reconstruction:** When a database B-tree index is backed up as a sorted key traversal, restoring the index requires reconstructing the tree from the traversal. The O(n) upper-bound technique is used in database recovery tools to efficiently rebuild B-tree indices from their serialized form, crucial for fast disaster recovery.
