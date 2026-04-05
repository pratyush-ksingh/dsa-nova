# Same Tree

> **Batch 3 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

## UNDERSTAND

### Problem Statement
Given the roots of two binary trees `p` and `q`, determine if they are structurally identical and have the same node values. Return `true` if both trees are the same, `false` otherwise. *(LeetCode #100)*

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `p = [1,2,3], q = [1,2,3]` | `true` | Both trees have identical structure and values |
| `p = [1,2], q = [1,null,2]` | `false` | Same values but different structure (left vs right child) |
| `p = [], q = []` | `true` | Two empty trees are the same |
| `p = [1], q = []` | `false` | One empty, one non-empty |

### Analogy
Think of comparing two family trees. You start at the patriarchs (roots) -- are they the same person? Then check: does each person have the same children in the same positions? You walk both trees side by side, and the moment one family has someone the other doesn't, you stop.

### 3 Key Observations
1. **"Aha" -- Simultaneous traversal:** You don't check one tree then the other. You walk both trees at the same time, node by node. If you ever find a mismatch (structure or value), you short-circuit to false.
2. **"Aha" -- Base cases drive everything:** Both null = same (true). One null, one not = different (false). Both non-null = compare values and recurse on children.
3. **"Aha" -- Any traversal order works:** Pre-order, in-order, level-order -- as long as you traverse both trees in the same order simultaneously, you'll catch every difference.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Two lists | Serialize both trees, compare lists | Simple but uses extra space |
| Optimal | Recursion stack | Simultaneous DFS | Natural tree comparison, O(h) space |
| Best | Explicit stack | Iterative simultaneous traversal | Same complexity, no recursion overhead |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Serialize & Compare
**Intuition:** Convert both trees to lists (using the same traversal including nulls), then compare the lists element by element.

**BUD Optimization:**
- **B**ottleneck: Building two full lists before comparing wastes memory and can't short-circuit early.
- **U**nnecessary: We're storing entire trees in memory just to compare them.
- **D**uplicate: Serialization + comparison is two passes when one pass would suffice.

**Steps:**
1. Serialize tree `p` into a list (pre-order, recording `null` for missing children).
2. Serialize tree `q` into a list the same way.
3. Compare both lists element by element.

**Dry-run trace** with `p = [1,2,3]`, `q = [1,2,3]`:
```
Serialize p: [1, 2, null, null, 3, null, null]
Serialize q: [1, 2, null, null, 3, null, null]
Compare: all elements match -> true
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node in both trees |
| Space | O(n) -- two serialized lists |

---

### Approach 2: Optimal -- Recursive Simultaneous DFS
**Intuition:** Compare both trees node-by-node using recursion. At each call, check: are both null? Is one null? Do values match? Then recurse on left and right children.

**BUD Optimization:**
- Eliminates the serialization step entirely.
- Short-circuits as soon as a mismatch is found (no need to visit remaining nodes).

**Steps:**
1. If both `p` and `q` are null, return `true`.
2. If only one is null, return `false`.
3. If `p.val != q.val`, return `false`.
4. Return `isSameTree(p.left, q.left) AND isSameTree(p.right, q.right)`.

**Dry-run trace** with `p = [1,2], q = [1,null,2]`:
```
isSameTree(1, 1) -> values match
  isSameTree(2, null) -> one is null -> false
  short-circuit -> return false
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- worst case visit all nodes |
| Space | O(h) -- recursion depth = tree height; O(log n) balanced, O(n) skewed |

---

### Approach 3: Best -- Iterative with Stack
**Intuition:** Same simultaneous comparison but using an explicit stack to avoid recursion overhead and potential stack overflow on deeply skewed trees.

**Steps:**
1. Push pair `(p, q)` onto a stack.
2. While stack is not empty:
   - Pop `(a, b)`.
   - If both null, continue.
   - If one null or values differ, return false.
   - Push `(a.left, b.left)` and `(a.right, b.right)`.
3. If we exhaust the stack, return true.

**Dry-run trace** with `p = [1,2,3], q = [1,2,3]`:
```
Stack: [(1,1)]
Pop (1,1) -> match. Push (2,2),(3,3)
Stack: [(2,2),(3,3)]
Pop (3,3) -> match. Push (null,null),(null,null)
Pop (2,2) -> match. Push (null,null),(null,null)
Pop (null,null) -> both null, continue (x4)
Stack empty -> true
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- stack depth bounded by tree height |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time is unavoidable:** In the worst case the trees ARE identical, so you must verify every single node. There's no way to confirm equality without checking all n nodes.

**Why O(h) space is optimal for DFS:** Each recursive call (or stack entry) corresponds to one level of the tree. You never hold more than the height of the tree in memory at once because you fully finish one subtree before exploring its sibling's deep descendants.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Both trees empty | `true` | Must handle null root |
| One empty, one not | `false` | Asymmetric null check |
| Same structure, one value differs deep | `false` | Must traverse fully |
| Single-node trees with same value | `true` | Simplest non-empty case |
| Mirrored trees (left/right swapped) | `false` | Structure matters, not just values |

### Common Mistakes
- Forgetting to check the case where only one node is null (NullPointerException).
- Checking values before checking nulls.
- Confusing "same tree" with "symmetric tree" -- same tree requires exact position match.

---

## INTERVIEW LENS

**Frequency:** High -- classic warm-up tree problem.
**Follow-ups the interviewer might ask:**
- "What if you need to check if one tree is a subtree of another?" (LeetCode #572 -- use this as a subroutine)
- "Can you solve it iteratively?" (Approach 3)
- "What about symmetric tree?" (Mirror comparison instead of same-side comparison)

**What they're really testing:** Your ability to write clean recursive code with proper base cases, and whether you understand tree traversal fundamentals.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Symmetric Tree (LC #101) | Same idea but compare left-right mirrored |
| Subtree of Another Tree (LC #572) | Uses isSameTree as subroutine at each node |
| Merge Two Binary Trees (LC #617) | Same simultaneous traversal pattern |
| Serialize/Deserialize Binary Tree (LC #297) | Brute force approach is essentially this |

### Real-World Use Case
**Configuration diffing:** In systems that represent hierarchical configurations as trees (e.g., XML/JSON config trees, file system snapshots), "same tree" comparison detects whether two configurations are identical -- critical for deployment tools that need to know if a config change actually occurred before triggering a restart.
