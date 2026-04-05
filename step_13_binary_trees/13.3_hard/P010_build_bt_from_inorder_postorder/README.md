# Build Binary Tree from Inorder and Postorder Traversal

> **Step 13 | 13.3** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #106 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given two integer arrays `inorder` and `postorder` where:
- `inorder` is the inorder traversal of a binary tree
- `postorder` is the postorder traversal of the same binary tree

Construct and return the binary tree.

All values are **unique**.

### Analogy
Imagine you're assembling a jigsaw puzzle where:
- **Postorder** tells you which piece is the "frame" (root) for each section -- always the last piece placed.
- **Inorder** tells you how pieces are spatially arranged -- everything left of the frame piece goes in the left section, right goes in the right section.
Together, they uniquely reconstruct the picture.

### Key Observations
1. **Last element of postorder is always the root.** Postorder = [left][right][root].
2. **Root's position in inorder splits the array.** Everything before root's index in inorder = left subtree; everything after = right subtree.
3. **Subproblem sizes match.** If there are `k` elements in the inorder left portion, then the first `k` elements of postorder (excluding the root) are also the left subtree's postorder. This boundary is critical.
4. **Build right before left** when consuming postorder from the end (rightmost non-root is right subtree's root).

### Examples

```
inorder   = [9, 3, 15, 20, 7]
postorder = [9, 15, 7, 20, 3]

Step 1: root = postorder[-1] = 3
Step 2: 3 is at index 1 in inorder
        left  inorder = [9]         left  postorder = [9]
        right inorder = [15, 20, 7] right postorder = [15, 7, 20]

Step 3 (right): root = 20, splits [15] and [7]
Step 4 (left):  root = 9,  no children

Result:
      3
     / \
    9   20
       /  \
      15    7
```

| inorder | postorder | Root | Tree structure |
|---------|-----------|------|----------------|
| [9,3,15,20,7] | [9,15,7,20,3] | 3 | (see above) |
| [2,1,3] | [2,3,1] | 1 | 1 with left=2, right=3 |
| [1] | [1] | 1 | single node |

### Constraints
- 1 <= n <= 3000
- -3000 <= inorder[i], postorder[i] <= 3000
- All values are **unique**
- Guaranteed valid pair

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive + scan) | Recursion, array slices | Direct translation of observation; O(n) scan per node |
| Optimal (HashMap + recursion) | HashMap + index pointers | O(1) lookup; process boundaries without slicing |
| Best (HashMap + explicit postEnd) | HashMap + explicit indices | No global mutable state; cleaner and thread-safe |

**Pattern cue:** "Construct tree from two traversals" -> divide-and-conquer using one traversal to identify root, the other to find split boundaries. HashMap eliminates the O(n) search.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Recursive + Linear Search)
**Intuition:** Last element of postorder is root. Search for root in inorder to find left/right split. Recurse on subarrays.

**Steps:**
1. Base case: empty inorder or postorder -> return None.
2. `root_val = postorder[-1]`.
3. Find `mid = inorder.index(root_val)` (O(n) scan).
4. `left_size = mid` (number of elements in left subtree).
5. Left: recurse with `inorder[:mid]`, `postorder[:left_size]`.
6. Right: recurse with `inorder[mid+1:]`, `postorder[left_size:-1]`.

**Dry-Run -- inorder=[9,3,15,20,7], postorder=[9,15,7,20,3]:**
```
build(in=[9,3,15,20,7], post=[9,15,7,20,3]):
  root=3, mid=1, left_size=1
  left:  build(in=[9],         post=[9])         -> root=9
  right: build(in=[15,20,7],   post=[15,7,20])
    root=20, mid=1, left_size=1
    left:  build(in=[15], post=[15]) -> root=15
    right: build(in=[7],  post=[7])  -> root=7
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) -- linear scan at each of n recursive calls |
| Space | O(n^2) -- array slices create copies at each level |

---

### BUD Transition (Brute -> Optimal)
**Bottleneck 1:** Linear scan in inorder at every call -> use a HashMap.
**Bottleneck 2:** Array slicing creates O(n) copies -> use index ranges (lo, hi) instead.

### Approach 2 -- Optimal (HashMap + Recursion)
**Intuition:** Pre-build `idx_map[value] = index` for inorder. Use a global pointer into postorder (decrement from the end). Process **right subtree first** because postorder's second-to-last root is the right subtree's root.

**Steps:**
1. Build `idx_map` from inorder: O(n).
2. `post_idx = len(postorder) - 1` (global mutable pointer).
3. `build(in_lo, in_hi)`:
   a. If `in_lo > in_hi`, return None.
   b. `root_val = postorder[post_idx--]`.
   c. `mid = idx_map[root_val]`.
   d. Build **right** first: `root.right = build(mid+1, in_hi)`.
   e. Build **left**: `root.left = build(in_lo, mid-1)`.
4. Return root.

**Why right first?** Postorder is `[left][right][root]`. Reading from the end: after root, the next element is the rightmost subtree's root, then left subtree's root. So decrement post_idx, and right subtree uses the next portion.

| Metric | Value |
|--------|-------|
| Time | O(n) -- O(1) lookup + one call per node |
| Space | O(n) -- HashMap + O(h) recursion stack |

---

### Approach 3 -- Best (HashMap + Explicit postEnd)
**Intuition:** Same as Optimal but passes `post_end` as a parameter instead of using a global variable. Cleaner and thread-safe.

**Steps:**
1. Build `idx_map`.
2. `build(in_lo, in_hi, post_end)`:
   a. `root_val = postorder[post_end]`, `mid = idx_map[root_val]`.
   b. `right_size = in_hi - mid`.
   c. Right subtree postorder ends at `post_end - 1`; spans `right_size` elements.
   d. Left subtree postorder ends at `post_end - right_size - 1`.
   e. Build right: `build(mid+1, in_hi, post_end - 1)`.
   f. Build left:  `build(in_lo, mid-1, post_end - right_size - 1)`.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) HashMap + O(h) stack |

---

## 4. COMPLEXITY INTUITIVELY

- **Brute O(n^2):** At each of n nodes, scan O(n) inorder. T(n) = 2T(n/2) + O(n) -> O(n log n) for balanced trees, but O(n^2) for skewed.
- **Optimal/Best O(n):** HashMap gives O(1) root lookup. Each node is created exactly once. Total work = O(n).
- **Space:** HashMap = O(n). Recursion stack = O(h). For balanced tree h = O(log n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Single node | Postorder has one element; inorder has one element; return that node |
| Left-skewed tree | All elements are left children; right recursion always gets empty range |
| Right-skewed tree | Symmetric |
| Negative values | Works the same; HashMap handles any int |

**Common mistakes:**
- **Building left before right** in Optimal approach: postorder is consumed right-to-left, so the right subtree's root comes before the left subtree's root when reading backwards. Must process right first.
- **Index boundary off-by-one:** Left subtree postorder is `[postLo .. postLo + leftSize - 1]`, right is `[postLo + leftSize .. postHi - 1]`.
- **Confusing with preorder+inorder:** In preorder+inorder, the **first** element is root and you build **left first**. The directions are mirrored.

---

## 6. REAL-WORLD USE CASE

**Serialization and deserialization of trees:** Databases and distributed systems serialize tree structures (e.g., expression trees, parse trees, decision trees) for storage or network transfer. Given two traversals, the tree can be exactly reconstructed -- this is a fundamental lossless encoding/decoding scheme.

**Compiler expression trees:** A compiler may store an expression tree's inorder (for display) and postorder (for evaluation order). Reconstructing the tree from both allows the compiler to re-analyze the expression structure.

---

## 7. INTERVIEW TIPS

- **Draw the key insight immediately:** "Last of postorder = root. Find root in inorder = left|right split."
- **State the right-before-left subtlety** in the optimal solution -- this trips up many candidates.
- **HashMap is the key optimization:** O(n^2) to O(n) by replacing the linear scan.
- **Compare with preorder+inorder (LC #105):** Structural mirror -- preorder root is at front, left subtree built first.
- **Follow-up:** "Can you do it with preorder+postorder?" -> Only possible if the tree is a full binary tree.
- **Edge case to mention:** Single element, all-left or all-right chain.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Build Tree from Preorder+Inorder (LC #105) | Mirror: first of preorder is root, build left first |
| Serialize/Deserialize Binary Tree (LC #297) | Tree reconstruction; can use single traversal with null markers |
| Verify Preorder Sequence in BST (LC #255) | Uses BST properties to reconstruct from single traversal |
| Maximum Binary Tree (LC #654) | Build tree from single array using max as root (like Cartesian tree) |

---

## Real-World Use Case

Compiler front-ends reconstruct abstract syntax trees (ASTs) from tokenized expressions using techniques similar to tree reconstruction from traversals. Serialization libraries like Protocol Buffers and Avro use tree reconstruction when deserializing nested data structures from flat byte streams.
