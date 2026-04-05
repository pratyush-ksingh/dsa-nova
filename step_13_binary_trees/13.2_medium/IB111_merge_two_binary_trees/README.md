# Merge Two Binary Trees

> **Batch 3 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

## UNDERSTAND

### Problem Statement
Given two binary trees `root1` and `root2`, merge them into a new binary tree. The merge rule is: if two nodes overlap (both exist at the same position), sum their values. If only one exists, use that node. Return the root of the merged tree. *(LeetCode #617)*

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `t1=[1,3,2,5], t2=[2,1,3,null,4,null,7]` | `[3,4,5,5,4,null,7]` | Root: 1+2=3, left: 3+1=4, right: 2+3=5, etc. |
| `t1=[1], t2=[1,2]` | `[2,2]` | Root: 1+1=2, t1 has no left but t2 has 2, so left=2 |
| `t1=[], t2=[1]` | `[1]` | One tree empty, result is the other tree |
| `t1=[], t2=[]` | `[]` | Both empty |

### Analogy
Think of overlaying two transparent maps. Where both maps have markings, you combine them (add the numbers). Where only one map has a marking, you keep it as-is. Where neither has a marking, the result is blank. You're essentially performing a position-by-position merge of two layered drawings.

### 3 Key Observations
1. **"Aha" -- Simultaneous traversal (again):** Just like "Same Tree," you walk both trees at the same time. But instead of comparing, you're combining.
2. **"Aha" -- One null = use the other:** If one node is null and the other isn't, you don't need to recurse further into that subtree. Just attach the entire non-null subtree.
3. **"Aha" -- You can modify in-place or create new nodes:** Modifying `t1` in-place saves memory but destroys the original. Creating new nodes is cleaner. Both are valid in interviews -- clarify with the interviewer.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | New tree + recursion | Create new node at each step | Clean but extra memory |
| Optimal | Modify t1 in-place | Recursive DFS, sum into t1 | No extra nodes, O(h) stack |
| Best | Queue | Iterative BFS merge into t1 | No recursion, level by level |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Create New Tree
**Intuition:** At each position, create a brand new node. If both nodes exist, sum values. If only one exists, copy it. This preserves both original trees.

**Steps:**
1. If both nodes are null, return null.
2. If one is null, return a deep copy of the other (or just return the other if we don't need to preserve originals).
3. Create new node with `t1.val + t2.val`.
4. New node's left = merge(t1.left, t2.left).
5. New node's right = merge(t1.right, t2.right).

**Dry-run trace** with `t1=[1,3], t2=[2,1,3]`:
```
merge(1,2): new node val=3
  left: merge(3,1): new node val=4
    left: merge(null,null) = null
    right: merge(null,null) = null
  right: merge(null,3): return node 3 from t2
Result: 3(4, 3)
```

| Metric | Value |
|--------|-------|
| Time | O(min(n1, n2)) for overlapping, O(n1+n2) total if deep copying |
| Space | O(min(h1, h2)) recursion + O(n) for new nodes |

---

### Approach 2: Optimal -- Recursive Merge into t1
**Intuition:** Instead of creating new nodes, modify `t1` in-place. Add `t2`'s values to `t1`. Where `t1` is missing a subtree, attach `t2`'s subtree directly.

**BUD Optimization:**
- **U**nnecessary: Creating new nodes when t1 already exists. Just update t1's value.
- Saves memory by reusing existing nodes.

**Steps:**
1. If `t1` is null, return `t2` (attach t2's subtree).
2. If `t2` is null, return `t1` (nothing to merge).
3. Add `t2.val` to `t1.val`.
4. `t1.left = merge(t1.left, t2.left)`.
5. `t1.right = merge(t1.right, t2.right)`.
6. Return `t1`.

**Dry-run trace** with `t1=[1,3,2], t2=[2,1,3]`:
```
merge(1,2): t1.val = 1+2 = 3
  left: merge(3,1): t1.val = 3+1 = 4
    left: merge(null,null) = null
    right: merge(null,null) = null
  right: merge(2,3): t1.val = 2+3 = 5
    left: merge(null,null) = null
    right: merge(null,null) = null
Result: 3(4, 5)
```

| Metric | Value |
|--------|-------|
| Time | O(min(n1, n2)) -- only visit overlapping nodes |
| Space | O(min(h1, h2)) -- recursion depth bounded by shorter tree |

---

### Approach 3: Best -- Iterative BFS Merge
**Intuition:** Use a queue to process node pairs level by level. For each pair, sum values into t1. If t1 is missing a child but t2 has one, attach t2's subtree. Only enqueue pairs where both have children (since non-overlapping subtrees are already handled).

**Steps:**
1. If `t1` is null, return `t2`.
2. Push pair `(t1, t2)` onto queue.
3. While queue not empty:
   - Pop `(n1, n2)`. Add `n2.val` to `n1.val`.
   - If both have left children, enqueue `(n1.left, n2.left)`.
   - If n1 has no left but n2 does, set `n1.left = n2.left`.
   - Same logic for right children.
4. Return `t1`.

**Dry-run trace** with `t1=[1,3,2], t2=[2,1,3]`:
```
Queue: [(1,2)]
Pop (1,2): t1.val=3. Both have left -> enqueue (3,1). Both have right -> enqueue (2,3).
Queue: [(3,1),(2,3)]
Pop (3,1): t1.val=4. No children on either side.
Pop (2,3): t1.val=5. No children on either side.
Done -> 3(4,5)
```

| Metric | Value |
|--------|-------|
| Time | O(min(n1, n2)) |
| Space | O(min(n1, n2)) -- queue holds at most one level's overlapping nodes |

---

## COMPLEXITY INTUITIVELY

**Why O(min(n1, n2)) time:** You only need to visit positions where BOTH trees have nodes (overlapping region). Where one tree is null, you just attach the other's subtree without traversal.

**Why recursion space is O(min(h1, h2)):** The recursion only goes as deep as the shorter tree at each position. Once one tree runs out (null), you return immediately.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Both empty | `null` | Double-null base case |
| One empty | Return the other | Don't traverse the non-null tree |
| No overlap at all | One tree grafted onto other | e.g., t1 only has left subtree, t2 only has right |
| Very unbalanced trees | Attached subtrees can be deep | Memory concern for iterative approach |
| Same tree merged with itself | All values doubled | Valid but tricky if modifying in-place |

### Common Mistakes
- Forgetting to handle the case where `t1` is null but `t2` isn't (must return `t2`, not null).
- In iterative approach: only enqueueing when BOTH children exist, but forgetting to attach t2's child when t1's child is missing.
- Modifying both trees accidentally (only modify t1, leave t2 untouched).

---

## INTERVIEW LENS

**Frequency:** High -- classic easy tree problem.
**Follow-ups the interviewer might ask:**
- "Can you do it without modifying either tree?" (Approach 1 -- new nodes)
- "Can you do it iteratively?" (Approach 3 -- BFS)
- "What if merge means max instead of sum?" (Same structure, change the combine operation)
- "What if there are 3 or more trees?" (Generalize the merge function)

**What they're really testing:** Simultaneous tree traversal pattern and handling asymmetric null cases.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Same Tree (LC #100) | Same simultaneous traversal pattern |
| Symmetric Tree (LC #101) | Simultaneous traversal, mirrored |
| Construct from Preorder + Inorder | Tree construction from combined info |
| Clone Graph (LC #133) | Deep copy pattern similar to Approach 1 |

### Real-World Use Case
**Configuration merging:** When deploying software, you often merge a default config tree with user overrides. Overlapping keys get the override value, non-overlapping keys from either side are kept. Docker Compose's `extends` feature, Kubernetes Helm value merging, and CSS cascade all follow this exact tree-merge pattern.
