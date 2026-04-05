# Ceil in BST

> **Batch 2 of 12** | **Topic:** Binary Search Trees | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND

### Problem Statement
Given a BST and a key value, find the **ceil** of the key. The ceil is the **smallest value in the BST that is greater than or equal to the key**. If no such value exists, return -1.

### Analogy
Imagine you are at a **bus stop** and buses arrive at times corresponding to BST node values. You arrive at time `key`. The ceil is the **next bus you can catch** -- the earliest bus that departs at or after your arrival time. If no bus comes after your arrival, you are stuck (return -1). The BST ordering lets you efficiently skip entire branches of the schedule.

### Key Observations
1. If `node.val == key`, the ceil is the node itself. **Aha:** Exact match is the trivial case.
2. If `node.val < key`, the ceil cannot be in this node or its left subtree (all values < key). **Aha:** Go right to find larger values.
3. If `node.val > key`, this node is a **candidate** ceil, but there might be a smaller valid ceil in the left subtree. **Aha:** Record this node as a candidate and go left to try to find a tighter (smaller) ceil.

### Examples

```
        8           key = 5 -> Ceil = 5 (exact match)
       / \          key = 7 -> Ceil = 8 (7 not in tree, next >= is 8)
      4  12         key = 3 -> Ceil = 4
     / \  / \       key = 13 -> Ceil = -1 (no value >= 13)
    2  6 10 14      key = 9 -> Ceil = 10
```

| Key | Ceil | Explanation |
|-----|------|-------------|
| 5 | 5 | Exact match at node 5 (if present) |
| 7 | 8 | 7 not in tree; next larger is 8 |
| 3 | 4 | 3 not in tree; next larger is 4 |
| 13 | 14 | 13 not in tree; next larger is 14 |
| 15 | -1 | No value >= 15 in the BST |
| 1 | 2 | Smallest node >= 1 is 2 |

### Constraints
- 1 <= number of nodes <= 10^5
- 1 <= Node.val <= 10^5
- Node values are unique

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (inorder to sorted array) | Array | Collect all values sorted, binary search for ceil. O(n). |
| Optimal (BST traversal) | Single variable | Navigate the BST with a candidate variable. O(h). |
| Best (same as optimal, iterative) | Single variable | O(h) time, O(1) space. Cannot do better. |

**Pattern cue:** "Find closest value with constraint in BST" -> directed search keeping a running candidate.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Inorder + Search)
**Intuition:** Perform inorder traversal to get all values in sorted order. Then find the first value >= key using linear or binary search.

**Steps:**
1. Inorder traversal -> sorted array.
2. Linear scan (or binary search) for the first element >= key.
3. If found, return it. Otherwise, return -1.

| Metric | Value |
|--------|-------|
| Time | O(n) for inorder + O(log n) for binary search = O(n) |
| Space | O(n) for the array |

### BUD Transition
**Unnecessary work:** We collect ALL values even though BST ordering lets us prune. **Upgrade:** Navigate the BST directly, keeping a candidate for the ceil.

### Approach 2 -- Optimal (BST-Guided Search, Recursive)
**Intuition:** Start at root. If node value == key, return it. If node value < key, go right (need larger). If node value > key, record it as a candidate and go left (try to find a smaller ceil).

**Steps:**
1. If root is null, return -1.
2. If `root.val == key`, return key.
3. If `root.val < key`, recurse into right subtree.
4. If `root.val > key`, set candidate = root.val, recurse into left subtree. If left returns a valid ceil, use it; otherwise, use candidate.

| Metric | Value |
|--------|-------|
| Time | O(h) |
| Space | O(h) recursion stack |

### Approach 3 -- Best (BST-Guided Iterative)
**Intuition:** Same logic as recursive, but use a while loop. Maintain a `ceil` variable initialized to -1.

**Steps:**
1. Set `ceil = -1`. Start at root.
2. While node is not null:
   - If `node.val == key`, return key.
   - If `node.val < key`, move to `node.right`.
   - If `node.val > key`, update `ceil = node.val`, move to `node.left`.
3. Return ceil.

**Dry-Run Trace -- BST [8,4,12,2,6,10,14], key=7:**

| Step | Node | node.val vs key(7) | Action | ceil |
|------|------|-------------------|--------|------|
| 1 | 8 | 8 > 7 | ceil=8, go left | 8 |
| 2 | 4 | 4 < 7 | go right | 8 |
| 3 | 6 | 6 < 7 | go right | 8 |
| 4 | null | -- | stop | **8** |

Trace for key=9:

| Step | Node | node.val vs key(9) | Action | ceil |
|------|------|-------------------|--------|------|
| 1 | 8 | 8 < 9 | go right | -1 |
| 2 | 12 | 12 > 9 | ceil=12, go left | 12 |
| 3 | 10 | 10 > 9 | ceil=10, go left | 10 |
| 4 | null | -- | stop | **10** |

| Metric | Value |
|--------|-------|
| Time | O(h) -- O(log n) balanced, O(n) skewed |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) brute:** Must traverse all nodes to build sorted array.
- **O(h) optimal/best:** At each node, we make one comparison and move one level down. Total comparisons = height of BST.
- **O(1) iterative space:** Just a `ceil` variable and a pointer. No recursion overhead.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Key exists in BST | Return key itself (exact match) |
| Key larger than all values | Return -1 (no ceil exists) |
| Key smaller than all values | Ceil = minimum value in BST |
| Single node BST | If node.val >= key, return it; else -1 |
| Skewed BST | O(n) worst case for O(h) solution |

**Common mistakes:**
- Returning the parent when you should return the left subtree result (forgetting to update candidate).
- Not handling the exact match case (going right when node.val == key).
- Returning -1 prematurely when the left subtree has no ceil (should fallback to the candidate).

---

## 6. INTERVIEW LENS

### UMPIRE Presentation
- **Understand:** Ceil = smallest value >= key. Return -1 if none exists.
- **Match:** BST navigation with running candidate for "best so far."
- **Plan:** Iterative: if val > key, record and go left; if val < key, go right; if equal, done.
- **Implement:** Simple while loop with 3 branches.
- **Review:** Trace key=7 and key=15 examples.
- **Evaluate:** O(h) time, O(1) space.

### Follow-Ups
| Question | Answer |
|----------|--------|
| What about floor? | Mirror logic: if val < key, candidate = val, go right. If val > key, go left. |
| Ceil in a sorted array? | Binary search: `lower_bound`. Same O(log n) idea. |
| What if duplicates are allowed? | Ceil logic still works; just find first val >= key. |
| Insert operation maintains BST? | Ceil gives you the position to insert if key is missing. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Floor in BST | Mirror of ceil: largest value <= key |
| Search in BST (LC #700) | Same O(h) traversal pattern |
| LCA of BST (LC #235) | Same BST-guided left/right decision |
| Inorder Successor in BST (LC #285) | Ceil of node.val after visiting the node |
| Lower Bound in Sorted Array | Ceil is the tree analog of lower_bound |

---

## Real-World Use Case
**Price matching in e-commerce:** Amazon's pricing engine finds the ceiling price (smallest price >= target) for "price match" guarantees. The BST ceil operation efficiently locates the next-higher price point in a sorted product catalog, enabling real-time competitive pricing across millions of SKUs.
