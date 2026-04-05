# Insert into a Binary Search Tree

> **Step 14 | 14.1** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #701 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a Binary Search Tree (BST) and an integer `val`, insert `val` into the BST such that the BST property is maintained and return the root of the modified tree. The input guarantees that `val` does not already exist in the tree.

**BST property:** For every node, all values in its left subtree are less, and all values in its right subtree are greater.

### Analogy
Think of a phone book stored as a BST. Inserting a new contact is like looking up where it alphabetically belongs -- go left if the name comes before the current entry, go right if it comes after -- until you find an empty slot, then insert it there.

### Key Observations
1. **Insertion always happens at a leaf.** There's no need to restructure the tree. Just find the correct null position and place the new node.
2. **BST property guides the traversal.** At each node, the comparison `val < node.val` tells us definitively which subtree to enter. This is O(h) total.
3. **Recursive elegance:** The recursive version "returns" the new node up from the null base case and naturally wires it to the parent via `node.left = recurse(...)`.

### Examples

```
Insert 5 into:
    4
   / \
  2   7
 / \
1   3

Go right from 4 (5>4), left from 7 (5<7), null -> place 5:
    4
   / \
  2   7
 / \ /
1  3 5

Inorder: [1, 2, 3, 4, 5, 7]  (sorted -- valid BST)
```

| Tree (level-order) | Insert | Result inorder |
|--------------------|--------|----------------|
| [4,2,7,1,3] | 5 | [1,2,3,4,5,7] |
| [40,20,60,10,30,50,70] | 25 | [10,20,25,30,40,50,60,70] |
| [] | 5 | [5] |
| [1] | 2 | [1,2] |

### Constraints
- 0 <= number of nodes <= 10^4
- -10^8 <= Node.val, val <= 10^8
- All node values are **unique**
- `val` does not exist in the original BST

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (iterative, inline) | Two pointers (current) | Walk BST and insert when a null child is found |
| Optimal (recursive) | Recursion | Elegant one-liner; leverages return value to link parent |
| Best (iterative + parent tracking) | Two pointers (parent + current) | O(1) space; explicit parent avoids any recursion overhead |

**Pattern cue:** "Insert into BST" -> traverse using BST ordering, insert at the null leaf position. Recursive and iterative are both O(h) time; iterative is O(1) space.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Iterative, inline null check)
**Intuition:** Walk the BST. At each node, check if we can place the new node as an immediate child. If the relevant child slot is null, place it. Otherwise, move into that child.

**Steps:**
1. If root is None, return new node as the root.
2. Create `new_node = TreeNode(val)`. Set `current = root`.
3. Loop forever:
   - If `val < current.val`:
     - If `current.left` is None, set `current.left = new_node` and break.
     - Else `current = current.left`.
   - Else:
     - If `current.right` is None, set `current.right = new_node` and break.
     - Else `current = current.right`.
4. Return root.

**Dry-Run -- Insert 5 into [4,2,7,1,3]:**
```
current=4: 5>4, right=7 (not null)
current=7: 5<7, left=null -> 7.left=5. Done.
```

| Metric | Value |
|--------|-------|
| Time | O(h) -- traverse to a leaf |
| Space | O(1) -- no extra structures |

---

### Approach 2 -- Optimal (Recursive)
**Intuition:** At each node, recurse into the correct subtree. The base case (null node) creates and returns the new node. The parent gets the return value and wires it up automatically.

**Steps:**
1. If root is None, return `TreeNode(val)`.
2. If `val < root.val`: `root.left = insert(root.left, val)`.
3. Else: `root.right = insert(root.right, val)`.
4. Return root.

**Dry-Run -- Insert 5 into [4,2,7,1,3]:**
```
insert(4, 5): 5>4, root.right = insert(7, 5)
  insert(7, 5): 5<7, root.left = insert(None, 5)
    insert(None, 5): return TreeNode(5)
  7.left = TreeNode(5), return 7
4.right = 7 (unchanged), return 4
```

| Metric | Value |
|--------|-------|
| Time | O(h) |
| Space | O(h) -- recursion depth = tree height |

---

### Approach 3 -- Best (Iterative + Explicit Parent, O(1) Space)
**Intuition:** Track both `parent` and `current`. When `current` becomes None, `parent` is the node that should adopt the new node. Track `is_left` to know which side.

**Steps:**
1. If root is None, return new node.
2. `parent = None`, `current = root`, `is_left = False`.
3. While current is not None:
   - `parent = current`.
   - If `val < current.val`: `current = current.left`, `is_left = True`.
   - Else: `current = current.right`, `is_left = False`.
4. After loop: if `is_left`, `parent.left = new_node`; else `parent.right = new_node`.
5. Return root.

| Metric | Value |
|--------|-------|
| Time | O(h) |
| Space | O(1) -- constant extra variables only |

---

## 4. COMPLEXITY INTUITIVELY

- **O(h) time for all:** We take exactly one path from root to the insertion leaf. Each step eliminates half the remaining nodes (in a balanced tree). Balanced: h = O(log n). Skewed: h = O(n).
- **Space:** Recursive = O(h) call stack. Iterative = O(1) always.
- **Why insertion is always at a leaf:** BST property guarantees unique ordering -- there is exactly one correct position for any new value, and that position is always an empty child slot.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree (root is None) | Return new node as root |
| Insert as leftmost node | All comparisons go left; works fine |
| Insert as rightmost node | All comparisons go right; works fine |
| Unbalanced/skewed BST | Still works; O(n) time in worst case |

**Common mistakes:**
- Returning `None` instead of `root` at the top level after iterative insert.
- Forgetting that the **return value** in the recursive approach is what links the new node to its parent -- do not ignore it.
- In Java: `list.remove(index)` vs `list.remove(Object)` when testing -- use `(Integer)` cast.

---

## 6. REAL-WORLD USE CASE

**Database index maintenance (B-Tree / BST):** Relational databases like MySQL use B-Trees (generalization of BSTs) for indexes. Every `INSERT` operation on an indexed column performs essentially this algorithm -- traverse the tree to find the correct position and insert a new leaf node. This keeps the index sorted, enabling O(log n) lookups.

**Auto-complete and sorted dictionaries:** Text editors and IDEs maintain a sorted data structure of known identifiers. When a new symbol is defined, it is inserted into the BST/B-Tree index to make future lookups fast.

---

## 7. INTERVIEW TIPS

- **Start with the recursive solution** -- it's the cleanest and most interview-friendly.
- **Then mention iterative for O(1) space** -- shows you understand the space vs elegance trade-off.
- **Inorder traversal validates BST property:** After insertion, inorder should still be sorted. Mention this as a verification strategy.
- **Follow-ups to anticipate:**
  - "What if the BST is unbalanced?" -> Insertion still works but becomes O(n). Self-balancing trees (AVL, Red-Black) restructure after insert.
  - "Can you insert without recursion?" -> Yes, the iterative O(1) approach.
  - "What if duplicates are allowed?" -> Typically go right (or left by convention); add a count field for frequency.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Search in BST (LC #700) | Same traversal logic, just read instead of insert |
| Delete Node in BST (LC #450) | More complex; requires handling 3 cases (leaf, one child, two children) |
| Validate BST (LC #98) | Insertion produces valid BST; validation checks property |
| Kth Smallest in BST (LC #230) | Uses inorder traversal which insert maintains |
| Construct BST from Preorder (LC #1008) | Repeatedly inserts preorder elements |
