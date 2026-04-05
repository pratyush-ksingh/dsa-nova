# Symmetric Tree

> **Step 13.13.2** | **Difficulty:** EASY | **XP:** 10 | **Status:** UNSOLVED

## UNDERSTAND

### Problem Statement
Given the root of a binary tree, check whether it is a **mirror of itself** (i.e., symmetric around its center). A tree is symmetric if the left subtree is a mirror reflection of the right subtree.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,2,3,4,4,3]` | `true` | Left subtree [2,3,4] mirrors right subtree [2,4,3] |
| `[1,2,2,null,3,null,3]` | `false` | Left has right child 3, right also has right child 3 -- not mirrored |
| `[]` | `true` | Empty tree is trivially symmetric |
| `[1]` | `true` | Single node is symmetric |

### Analogy
Imagine holding a tree diagram up to a mirror. If the reflection looks identical to the original, the tree is symmetric. You check this by comparing the left side with the right side: the left child of the left subtree should match the right child of the right subtree, and vice versa.

### 3 Key Observations
1. **"Aha" -- Compare cross-pairs:** For symmetry, compare `left.left` with `right.right` (outer pair) and `left.right` with `right.left` (inner pair). This is the mirror condition.
2. **"Aha" -- Null handling is crucial:** Both null = symmetric at that point. One null = not symmetric. This base case drives the recursion.
3. **"Aha" -- It's a modified "Same Tree" problem:** Instead of comparing two trees for equality, compare for mirror equality by swapping which children you compare.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Extra tree copy | Mirror + compare | Explicit but wasteful |
| Optimal | Recursion stack | Recursive mirror check | Clean, O(h) space |
| Best | Queue | Iterative mirror check | Avoids recursion depth issues |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Mirror Tree and Compare
**Intuition:** Create a mirrored copy of the tree (swap left and right at every node), then check if the original tree equals the mirrored tree using standard same-tree comparison.

**Steps:**
1. Create a mirrored copy: for each node, swap left and right children recursively.
2. Compare original tree with mirrored tree node by node.
3. If they are identical, the tree is symmetric.

**Dry-run trace** with `[1,2,2,3,4,4,3]`:
```
Original:     1(2(3,4), 2(4,3))
Mirror copy:  1(2(3,4), 2(4,3))  -- mirroring gives same tree!
Compare: identical -> true
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- two passes: mirror O(n) + compare O(n) |
| Space | O(n) -- extra tree copy |

---

### Approach 2: Optimal -- Recursive Mirror Comparison
**Intuition:** Instead of creating a mirror copy, directly compare the left and right subtrees for mirror symmetry. Two nodes are mirrors if their values match AND left's left mirrors right's right AND left's right mirrors right's left.

**Steps:**
1. Call `isMirror(root.left, root.right)`.
2. Base cases: both null = true, one null = false.
3. Check `left.val == right.val`.
4. Recurse: `isMirror(left.left, right.right) AND isMirror(left.right, right.left)`.

**Dry-run trace** with `[1,2,2,3,4,4,3]`:
```
isMirror(2, 2): vals match
  isMirror(3, 3): vals match
    isMirror(null, null): true
    isMirror(null, null): true
  -> true
  isMirror(4, 4): vals match
    isMirror(null, null): true
    isMirror(null, null): true
  -> true
-> true
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node once |
| Space | O(h) -- recursion stack depth |

---

### Approach 3: Best -- Iterative with Queue
**Intuition:** Use a queue to compare mirror pairs iteratively. Enqueue pairs that should be mirrors of each other. Pop two at a time and verify they match.

**Steps:**
1. Enqueue `root.left` and `root.right`.
2. While queue is not empty:
   - Dequeue two nodes (left, right).
   - Both null: continue. One null: return false.
   - Values differ: return false.
   - Enqueue mirror pairs: (left.left, right.right) and (left.right, right.left).
3. Return true.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node processed once |
| Space | O(n) -- queue can hold up to n/2 nodes at widest level |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** Every node must be checked. A tree could fail symmetry at any node, so we cannot skip any.

**Why recursive is O(h) space:** The recursion depth is bounded by the tree height. For balanced trees, O(log n). The iterative approach trades recursion stack for an explicit queue but uses O(n) in the worst case.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `true` | Trivially symmetric |
| Single node | `true` | No children to compare |
| All same values but not symmetric structure | `false` | Must check structure, not just values |
| Left-skewed tree | `false` | Asymmetric by structure |
| Perfect tree with all same values | `true` | Structure and values both match |

### Common Mistakes
- Comparing `left.left` with `right.left` instead of `right.right` (same-tree check, not mirror check).
- Forgetting to handle null nodes as a base case before accessing `.val`.
- Only checking values at each level without checking structural symmetry.

---

## INTERVIEW LENS

**Frequency:** Very high -- LeetCode 101, a top-asked easy problem.
**Follow-ups the interviewer might ask:**
- "Do it iteratively" (queue approach)
- "What about n-ary trees?" (Compare children lists in reverse)
- "How would you make a tree symmetric?" (Mirror one side to match the other)

**What they're really testing:** Understanding of tree structure, ability to reason about symmetry/mirroring, and clean recursive thinking.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Same Tree (LC 100) | Compare for equality instead of mirror |
| Invert Binary Tree (LC 226) | Creates the mirror; symmetric = tree equals its inversion |
| Subtree of Another Tree | Similar two-tree comparison pattern |
| Palindrome (strings) | Same concept: sequence equals its reverse |

### Real-World Use Case
**UI layout validation:** In responsive web design, symmetric layouts (navigation bars, dashboard grids) need to be validated. A component tree representing the UI can be checked for symmetry to ensure the left and right halves of a page mirror each other -- useful in automated testing of CSS frameworks and design systems.
