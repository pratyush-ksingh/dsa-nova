# Last Node in Complete Binary Tree

> **Step 13 | 13.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## UNDERSTAND

### Problem Statement
Given a **complete binary tree**, find the value of the **last node** -- the rightmost node in the last level. A complete binary tree has all levels fully filled except possibly the last, which is filled from left to right.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3,4,5,6]` | `6` | Last level has [4,5,6], rightmost is 6 |
| `[1,2,3,4,5,6,7]` | `7` | Perfect tree, last node is 7 |
| `[1,2,3]` | `3` | Last level [2,3], rightmost is 3 |
| `[1]` | `1` | Single node is last node |

### Analogy
Imagine a stadium with numbered seats filled row by row from left to right. You want to find the last occupied seat. Instead of checking every seat (O(n)), you can use a binary strategy: check if the left section is completely full. If yes, the last seat must be in the right section. Otherwise, it is in the left section. Each check takes O(log n) and you do O(log n) checks.

### 3 Key Observations
1. **"Aha" -- Height comparison reveals completeness:** In a complete tree, if the left subtree height equals the right subtree height, the left subtree is a perfect tree. The last node must be in the right subtree.
2. **"Aha" -- Height can be computed in O(log n):** Since the tree is complete, the height is found by always going left.
3. **"Aha" -- Binary search gives O(log^2 n):** We make O(log n) decisions (one per level), each costing O(log n) for height computation, totaling O(log^2 n).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Queue | BFS level order | Simple but O(n) |
| Optimal | Recursion | Binary search via height comparison | O(log^2 n) |
| Best | Iterative | Same binary search, no recursion | O(log^2 n), O(1) extra space |

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS Level Order
**Intuition:** Traverse the entire tree level by level. The last node visited is the answer.

**Steps:**
1. Push root to queue.
2. While queue is not empty, dequeue node, track it as `last`.
3. Enqueue left and right children.
4. Return `last.val`.

**Dry-run trace** with `[1,2,3,4,5,6]`:
```
Queue: [1] -> process 1, enqueue 2,3
Queue: [2,3] -> process 2, enqueue 4,5; process 3, enqueue 6
Queue: [4,5,6] -> process 4, 5, 6. last=6
Return 6
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node |
| Space | O(n) -- queue holds up to n/2 nodes at last level |

---

### Approach 2: Optimal -- Binary Search via Height Comparison
**Intuition:** At each node, compare left and right subtree heights. If equal, the left subtree is a perfect binary tree (all levels full), so the last node is in the right subtree. If left is taller, the right subtree is perfect but one level shorter, so the last node is in the left subtree.

**Steps:**
1. If node is a leaf, return its value.
2. Compute left subtree height and right subtree height (by going left repeatedly).
3. If heights are equal, recurse into right subtree.
4. If left is taller, recurse into left subtree.

**Dry-run trace** with `[1,2,3,4,5,6]`:
```
Node 1: leftH=3 (1->2->4), rightH=2 (1->3->6). leftH > rightH -> go left
Node 2: leftH=2 (2->4), rightH=2 (2->5). Equal -> go right
Node 5: leaf -> return 5
Wait -- that gives 5, but answer is 6!
```

Actually let me re-examine. The tree `[1,2,3,4,5,6]` means:
```
       1
      / \
     2   3
    / \ /
   4  5 6
```
Node 1: left subtree height (go left from node.left=2): 2->4, h=2. Right subtree height (go left from node.right=3): 3->6, h=2. Equal -> go right.
Node 3: left=6 (leaf), right=null. leftH=1, rightH=0. leftH > rightH -> go left.
Node 6: leaf -> return 6.

| Metric | Value |
|--------|-------|
| Time | O(log^2 n) -- log n levels, each height check is O(log n) |
| Space | O(log n) -- recursion depth |

---

### Approach 3: Best -- Iterative Binary Search
**Intuition:** Same height-comparison logic, but replace recursion with a while loop.

**Steps:**
1. Set `curr = root`.
2. While `curr` has a left child:
   - Compare left and right subtree heights.
   - If equal, move to right child. Else, move to left child.
3. Return `curr.val`.

| Metric | Value |
|--------|-------|
| Time | O(log^2 n) -- same as optimal |
| Space | O(1) -- no recursion stack (height computation uses O(1)) |

---

## COMPLEXITY INTUITIVELY

**Why O(log^2 n) is better than O(n):** The tree has ~n nodes but only log n levels. At each level, we compute a height in O(log n) using the complete tree property. Total: O(log n) * O(log n) = O(log^2 n).

**Why we cannot do better than O(log^2 n):** Without random access to nodes, we need at least O(log n) height checks, each costing O(log n).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Single node | That node's value | Both children null |
| Perfect tree | Rightmost leaf | Always goes right at every level |
| Only left child filled | Leftmost of last level | Height difference at root |
| Empty tree | -1 | Handle null root |

### Common Mistakes
- Computing height incorrectly (must go left for complete trees, not max of both sides).
- Confusing "left subtree height == right subtree height" meaning (left is perfect, NOT right).
- Off-by-one in height calculation.

---

## INTERVIEW LENS

**Frequency:** Medium -- InterviewBit specific, tests complete tree properties.
**Follow-ups the interviewer might ask:**
- "Count total nodes in a complete binary tree?" (Same O(log^2 n) approach -- LeetCode 222)
- "What if the tree is not complete?" (Must do BFS, O(n))
- "Can you do it in O(log n)?" (Not without parent pointers or array representation)

**What they're really testing:** Understanding of complete binary tree properties and ability to exploit structure for sub-linear algorithms.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Count Complete Tree Nodes (LC 222) | Same height-comparison binary search |
| Binary Search | Same divide-and-conquer principle |
| Check Completeness of BT | Prerequisite understanding |
| Heap Operations | Heaps are complete trees; finding last node is key for delete |

### Real-World Use Case
**Heap data structures:** Binary heaps are stored as complete binary trees. When implementing a heap with linked nodes (not arrays), finding the last node is required for deletion. This O(log^2 n) approach is used in linked-list based priority queues where array indexing is not available.
