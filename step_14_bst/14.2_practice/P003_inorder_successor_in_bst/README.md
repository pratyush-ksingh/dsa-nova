# Inorder Successor in BST

> **Step 14.14.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## UNDERSTAND

### Problem Statement
Given a **Binary Search Tree** and a target node, find the **inorder successor** of the target node. The inorder successor is the node with the smallest key greater than the target's key. If no successor exists (target is the largest), return null.

### Examples

| Input | Target | Output | Explanation |
|-------|--------|--------|-------------|
| `[20,8,22,4,12,null,null,null,null,10,14]` | `8` | `10` | Inorder: [4,8,10,12,14,20,22]. Next after 8 is 10 |
| `[20,8,22,4,12,null,null,null,null,10,14]` | `14` | `20` | Next after 14 is 20 |
| `[20,8,22,4,12,null,null,null,null,10,14]` | `22` | `null` | 22 is the largest, no successor |
| `[2,1,3]` | `1` | `2` | Next after 1 is 2 |

### Analogy
Imagine a sorted list of employees by ID. Given an employee, the "inorder successor" is the next employee in the sorted order. In a BST, the tree structure encodes this sorted order. If the employee has subordinates to their right (right subtree), the successor is the leftmost/smallest of those. If not, you walk up to find the first boss (ancestor) who is greater.

### 3 Key Observations
1. **"Aha" -- Two cases:** If the node has a right subtree, the successor is the leftmost node in that subtree. If not, the successor is the lowest ancestor for which the target is in the left subtree.
2. **"Aha" -- BST walk from root:** Instead of needing parent pointers, walk from root. Every time you go left (current > target), that node is a successor candidate. The last such candidate is the answer.
3. **"Aha" -- The "best" approach simplifies:** Just find the smallest node with value > target.val. Walk BST: if current > target, save as candidate and go left; else go right. This handles both cases uniformly.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Array (inorder list) | Full inorder + linear scan | Simple but O(n) |
| Optimal | BST walk | Case-based (right subtree or ancestor) | O(h), explicit two cases |
| Best | BST walk | Unified search for smallest > target | O(h), cleaner single logic |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Full Inorder Traversal
**Intuition:** Perform a complete inorder traversal to get all nodes in sorted order. Find the target in this sorted list and return the next element.

**Steps:**
1. Perform inorder traversal, collecting all nodes in a list.
2. Find the target node in the list.
3. Return the next node (index + 1), or null if target is last.

**Dry-run trace** with BST `[20,8,22,4,12]`, target=8:
```
Inorder: [4, 8, 12, 20, 22]
Find 8 at index 1.
Return node at index 2 -> 12
Wait, that's wrong for the detailed tree. With 10,14 under 12:
Inorder: [4, 8, 10, 12, 14, 20, 22]
Find 8 at index 1.
Return node at index 2 -> 10
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- traverse all nodes + linear search |
| Space | O(n) -- store entire inorder list |

---

### Approach 2: Optimal -- BST Property (Two Cases)
**Intuition:** Exploit BST structure. Case 1: If target has a right child, the successor is the leftmost node in the right subtree. Case 2: If no right child, walk from root, tracking the last node where we went left (that ancestor is greater than target).

**Steps:**
1. If target has right subtree: go right, then keep going left until null. That node is the successor.
2. Else: start from root. At each node:
   - If target < current: current is a candidate successor, go left.
   - If target > current: go right.
   - If target == current: stop (successor already tracked).
3. Return the last saved candidate.

**Dry-run trace** with target=14:
```
14 has no right child.
Walk from root (20): 14 < 20 -> successor=20, go left.
At 8: 14 > 8 -> go right.
At 12: 14 > 12 -> go right.
At 14: found target. Return successor=20.
```

| Metric | Value |
|--------|-------|
| Time | O(h) -- follow one path from root + possibly one path down right subtree |
| Space | O(1) -- only a few pointers |

---

### Approach 3: Best -- Unified BST Search
**Intuition:** Simply find the smallest value in the BST that is strictly greater than target.val. Walk the BST: if current > target, it is a candidate (save it), go left to find a smaller candidate. If current <= target, go right. This handles both cases (with and without right subtree) uniformly.

**Steps:**
1. Initialize `successor = null`, `curr = root`.
2. While curr is not null:
   - If `curr.val > target.val`: save as successor, go left.
   - Else: go right.
3. Return successor.

**Why this works for both cases:**
- If target has a right subtree, the walk will eventually enter that subtree and find the leftmost node.
- If target has no right subtree, the walk naturally tracks the lowest ancestor where target is in the left subtree.

| Metric | Value |
|--------|-------|
| Time | O(h) -- single path through BST |
| Space | O(1) -- constant extra space |

---

## COMPLEXITY INTUITIVELY

**Why O(h) is optimal:** The successor is always either in the right subtree (depth h) or an ancestor (distance h from target). Either way, we traverse at most h nodes. For balanced BSTs, h = O(log n). For skewed BSTs, h = O(n).

**Why O(1) space:** We only need a pointer to track the current best successor candidate. No extra data structures needed.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Target is the largest node | `null` | No successor exists |
| Target is the smallest node | Second smallest | Successor is easy to find |
| Target has right subtree | Leftmost in right subtree | Must go all the way left |
| Target is root with no right child | Null (if root is max) or left-path ancestor | Depends on tree structure |
| Single node tree, target is root | `null` | No other nodes |

### Common Mistakes
- Forgetting to handle the case where target has no right child (must walk from root).
- Using parent pointers (not always available) -- walk from root instead.
- Returning the node with value == target instead of strictly greater.
- Off-by-one: returning target itself when target.val equals some ancestor.

---

## INTERVIEW LENS

**Frequency:** High -- classic BST problem, LeetCode 285.
**Follow-ups the interviewer might ask:**
- "What about inorder predecessor?" (Mirror logic: find largest < target)
- "What if we don't have the root, only parent pointers?" (Walk up to first right-parent ancestor)
- "What about inorder successor in a general binary tree?" (Must do full inorder, O(n))
- "Delete a node in BST" (Uses inorder successor to replace deleted node)

**What they're really testing:** Understanding of BST properties, ability to handle two cases cleanly, and recognizing the unified search approach.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Inorder Predecessor in BST | Mirror problem: find largest < target |
| Delete Node in BST | Uses successor to replace deleted node |
| BST Iterator | Returns nodes in inorder; `next()` is finding the successor |
| Validate BST | Same BST walk pattern |

### Real-World Use Case
**Database B-tree indexing:** In database engines, B-trees (generalized BSTs) support range queries. Finding the "next record after X" is exactly the inorder successor operation. When a SQL query like `SELECT * FROM users WHERE id > 100 LIMIT 1` executes, the database engine finds the inorder successor of key 100 in the B-tree index, using the same O(log n) walk described here.
