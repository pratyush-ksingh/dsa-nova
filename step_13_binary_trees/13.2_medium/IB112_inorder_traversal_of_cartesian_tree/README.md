# Inorder Traversal of Cartesian Tree

> **Step 13 | 13.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

**InterviewBit:** inorder-traversal-of-cartesian-tree

---

## 1. UNDERSTAND

### Problem Statement
Given the **inorder traversal** of a Cartesian tree (an array of distinct integers), construct the Cartesian tree and return its root.

A **Cartesian tree** satisfies two properties simultaneously:
1. **Max-heap property:** Every node's value is greater than all values in its subtrees.
2. **BST inorder property:** An inorder traversal of the tree yields the original array.

In other words: the root is the **maximum element** of the array, the left subtree is a Cartesian tree of elements to the left of the max, and the right subtree is a Cartesian tree of elements to the right of the max.

### Analogy
Think of a mountain range: the highest peak becomes the root; left mountains form the left subtree, right mountains the right subtree -- recursively. The silhouette from left to right (inorder) exactly reproduces the original elevation profile.

### Key Observations
1. **Root = max element.** Because of the max-heap property, the root must be the largest value. Aha: this uniquely determines the tree given the inorder sequence.
2. **Divide-and-conquer by max.** Once you pick the max as root, the subproblems are independent: left subarray -> left subtree, right subarray -> right subtree. Same structure as building a segment tree.
3. **Stack-based insight.** Processing left-to-right: each new element pops all smaller stack entries (they become its left subtree) and becomes the right child of whatever remains on the stack. This mirrors how a monotone decreasing stack works.

### Examples

```
Input inorder: [5, 10, 40, 30, 28]

Cartesian tree:
        40
       /  \
     10    30
    /        \
   5          28

Inorder of tree: [5, 10, 40, 30, 28]  (original array -- PASS)
Preorder of tree: [40, 10, 5, 30, 28]
```

```
Input inorder: [1, 2, 3, 4, 5]

Cartesian tree (right-skewed, max is last):
1
 \
  2
   \
    3
     \
      4
       \
        5

Preorder: [5, 4, 3, 2, 1]   (root=5 has only left chain)
```

| Input (inorder) | Root value | Preorder output |
|----------------|-----------|-----------------|
| [5, 10, 40, 30, 28] | 40 | [40, 10, 5, 30, 28] |
| [1, 2, 3, 4, 5] | 5 | [5, 4, 3, 2, 1] |
| [3] | 3 | [3] |

### Constraints
- 1 <= number of elements <= 10^5
- All elements are distinct
- -10^9 <= element <= 10^9

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive) | Recursion, subarrays | Direct simulation of definition; O(n) scan per level |
| Optimal (sparse table) | Sparse Table + recursion | O(1) range-max queries; same recursion skeleton |
| Best (stack) | Monotone decreasing stack | Single left-to-right pass; amortized O(1) per element |

**Pattern cue:** "Build tree from array using max/min" -> think Sparse Table + divide-and-conquer, or monotone stack.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Recursive, Linear Max)
**Intuition:** Directly implement the definition: find the maximum element (O(n) scan), make it the root, recurse on left and right subarrays.

**Steps:**
1. Base case: empty subarray -> return None/null.
2. Scan `arr[lo..hi]` to find index `maxIdx` of the maximum value.
3. Create root node with `arr[maxIdx]`.
4. Recursively build left subtree from `arr[lo..maxIdx-1]`.
5. Recursively build right subtree from `arr[maxIdx+1..hi]`.
6. Return root.

**Dry-Run -- [5, 10, 40, 30, 28]:**
```
build(0,4): max=40 at idx=2 -> root=40
  build(0,1): max=10 at idx=1 -> node=10
    build(0,0): max=5 -> node=5, no children
    build(2,1): lo>hi -> None
  build(3,4): max=30 at idx=3 -> node=30
    build(3,2): lo>hi -> None
    build(4,4): max=28 -> node=28, no children
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) worst case (skewed tree with linear scans) |
| Space | O(n) recursion stack (depth = tree height, up to n) |

---

### BUD Transition (Brute -> Optimal)
**Bottleneck:** The O(n) scan for each subarray max leads to O(n^2) total. We need O(1) range-max queries.

### Approach 2 -- Optimal (Sparse Table + Recursion)
**Intuition:** Precompute a Sparse Table for range-maximum index queries in O(n log n). Then the recursive structure stays the same but each "find max" step is O(1).

**Steps:**
1. Precompute Sparse Table on the inorder array: `table[k][i]` stores the index of max in `arr[i .. i + 2^k - 1]`.
2. Log table: `log2[n] = floor(log2(n))` for all n.
3. Range max query `(lo, hi)`: let `k = log2[hi - lo + 1]`; compare `table[k][lo]` vs `table[k][hi - 2^k + 1]`.
4. Use the same recursive build as Brute, but replace the O(n) scan with O(1) query.

| Metric | Value |
|--------|-------|
| Time | O(n log n) preprocessing + O(n) construction = O(n log n) total |
| Space | O(n log n) sparse table |

---

### Approach 3 -- Best (Monotone Stack, Single Pass)
**Intuition:** Process elements left-to-right. Maintain a stack whose values are decreasing (max-heap property). When we see a value greater than the stack top, those popped nodes form the LEFT subtree of the new node (since they appeared to the left in inorder and are smaller). The new node then becomes the RIGHT child of the remaining stack top (which is larger and appeared earlier = ancestor).

**Steps:**
1. Initialize empty stack, `root = None`, `lastPopped = None`.
2. For each value `val` in inorder:
   a. Create `node = TreeNode(val)`.
   b. Pop all nodes from stack with value `< val`; track the last one as `lastPopped`.
   c. Set `node.left = lastPopped` (rightmost subtree that's still smaller).
   d. If stack is non-empty: `stack.top().right = node`; else `root = node`.
   e. Push `node` onto stack.
3. Return `root`.

**Dry-Run -- [5, 10, 40, 30, 28]:**
```
Process 5:  stack=[], node=5, lastPopped=None, root=5,       stack=[5]
Process 10: pop 5 (5<10), lastPopped=5, stack empty -> root=10, 10.left=5, stack=[10]
Process 40: pop 10 (10<40), lastPopped=10, stack empty -> root=40, 40.left=10, stack=[40]
Process 30: stack top=40 (40>30), no pop, 40.right=30, stack=[40,30]
Process 28: stack top=30 (30>28), no pop, 30.right=28, stack=[40,30,28]

Result: root=40, 40.left=10, 10.left=5, 40.right=30, 30.right=28  CORRECT
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node pushed/popped at most once |
| Space | O(n) stack |

---

## 4. COMPLEXITY INTUITIVELY

- **Brute O(n^2):** In the worst case (sorted array), max is always the last element. Left half has n-1 elements, then n-2, ... -> T(n) = T(n-1) + n = O(n^2).
- **Sparse Table O(n log n):** Preprocessing fills a table of size O(n log n). Each cell takes O(1). Queries are O(1). Recursion has O(n) nodes total.
- **Stack O(n):** Each element is pushed once and popped once. Total operations = 2n = O(n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Single element | Return a single node with no children |
| Strictly increasing array | Each element is left child of next; right-skewed |
| Strictly decreasing array | Each element is right child of next; left-skewed |
| All equal | Problem states elements are distinct |

**Common mistakes:**
- Stack approach: forgetting to set `root = node` when stack is empty (first element or whenever stack drains completely).
- Sparse Table: off-by-one in query formula `hi - (1 << k) + 1`.
- Confusing "last popped" becomes `left` child (not right); inorder says it came before.

---

## 6. REAL-WORLD USE CASE

**Treap (Tree + Heap):** Cartesian trees are the backbone of the Treap data structure used in competitive programming and some databases. A Treap assigns random priorities to keys; the tree structure is exactly a Cartesian tree on those priorities. This gives expected O(log n) search, insert, and delete -- combining BST ordering with heap-like balance without rotations.

**Expression parsing / operator precedence:** Cartesian trees model operator precedence in arithmetic expressions. The operator with the lowest precedence becomes the root (it's evaluated last), naturally yielding the parse tree.

---

## 7. INTERVIEW TIPS

- **Start with the definition:** Root = max. The inorder array directly encodes the tree structure.
- **Mention the stack approach unprompted** -- it's the impressive O(n) solution. Interviewers love it.
- **Connect to Treap:** Shows broader CS knowledge.
- **Dry-run the stack** on a small example -- it's non-obvious and demonstrating it clearly will impress.
- **Key question to expect:** "Can you do it in O(n)?" -- Yes, with the monotone stack.
- **Follow-up:** What if we want min-Cartesian tree (min-heap property)? Same algorithms, just flip the comparison.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Build BST from Preorder (LC #1008) | Similar divide-and-conquer by root element |
| Largest Rectangle in Histogram (LC #84) | Uses same monotone stack pattern |
| Maximum Binary Tree (LC #654) | Exactly the same problem -- max element as root! |
| Treap data structure | Cartesian tree with random priorities |
| Range Min/Max Query | Sparse Table is reusable technique |

---

## Real-World Use Case

Cartesian trees are used in range-minimum-query (RMQ) data structures that power computational geometry libraries. Database systems use Cartesian tree properties for efficient range queries on indexed columns, and the treap data structure (a Cartesian tree variant) is used in Redis for ordered sets.
