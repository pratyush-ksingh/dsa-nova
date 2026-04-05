# Populate Next Right Pointers

> **Batch 4 of 12** | **Topic:** Binary Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a **perfect binary tree** where each node has an extra `next` pointer (initially null), populate each `next` pointer to point to its **next right node** at the same level. The rightmost node at each level should have `next = null`.

**LeetCode #116**

### Examples

**Example 1:**
```
Input:          After connecting:
     1               1 -> null
    / \              / \
   2   3            2 -> 3 -> null
  / \ / \          / \ / \
 4  5 6  7        4->5->6->7 -> null
```

**Example 2:**
```
Input:    After:
  1         1 -> null
```
Single node: next stays null.

### Real-Life Analogy
Imagine people standing in rows for a group photo. Each person should hold the hand of the person to their right. The person at the right end of each row has no one to hold. The "next" pointer is like each person knowing who stands to their right.

### 3 Key Observations
1. **"Aha!" -- BFS gives natural left-to-right order:** Level-order traversal processes each level left to right. Simply connect each node to the next one in the queue within the same level.
2. **"Aha!" -- Already-connected level enables O(1) space:** Once level L is connected via next pointers, you can traverse level L using next pointers to connect level L+1 -- no queue needed.
3. **"Aha!" -- Perfect binary tree simplifies things:** Every non-leaf has exactly two children. Left child's next = right child. Right child's next = parent's next's left child (if parent.next exists).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Queue (BFS) | Level-order traversal | Straightforward |
| Optimal | DFS recursive | Pre-order connecting children + cross-links | No extra DS |
| Best | Next pointers | Iterative O(1) space using established links | Perfect for perfect trees |

---

## APPROACH LADDER

### Brute Force: BFS with Queue
**Intuition:** Standard level-order BFS. For each level, connect consecutive nodes.

**Steps:**
1. Push root to queue
2. For each level: process all nodes, connect node[i].next = node[i+1]
3. Last node in each level gets next = null (already null by default)

**BUD Analysis:**
- **B**ottleneck: Queue uses O(N/2) = O(N) space for the last level
- **U**nnecessary: The queue itself -- we can use already-established next pointers
- **D**uplicate: None

**Dry-Run Trace:**
```
Queue: [1]
Level 0: node=1, next=null. Enqueue 2,3.
Queue: [2, 3]
Level 1: node=2, next=3. node=3, next=null. Enqueue 4,5,6,7.
Queue: [4,5,6,7]
Level 2: 4->5->6->7->null
```

### Optimal: Recursive DFS
**Intuition:** For each node, connect its left child to right child. Connect right child to the left child of the node's next (cross-link).

**Steps:**
1. Base case: if node is null or leaf, return
2. node.left.next = node.right
3. if node.next != null: node.right.next = node.next.left
4. Recurse left, then right

### Best: O(1) Space Iterative Using Next Pointers
**Intuition:** Process level by level. Use the already-connected current level to iterate and connect the next level's children. Like "stitching" the next level while walking across the current level.

**Steps:**
1. Start with leftmost = root
2. While leftmost.left exists (there is a next level):
   a. Walk across current level using next pointers
   b. For each node: connect left.next = right, and if node.next exists: right.next = node.next.left
   c. Move leftmost = leftmost.left (go to next level)

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N) | O(N) | BFS queue holds up to N/2 nodes (last level) |
| Optimal | O(N) | O(log N) | DFS recursion stack = tree height |
| Best | O(N) | O(1) | Only pointers, no extra data structures |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Null root | Return null | Base case |
| Single node | next = null | No connections to make |
| Two levels | Just connect root's children | root.left.next = root.right |
| Large perfect tree | All levels connected | Must handle cross-subtree links |

**Common Mistakes:**
- Forgetting the cross-link: node.right.next = node.next.left
- Null pointer when accessing node.next.left (must check node.next != null)
- For non-perfect trees, this O(1) approach needs modification (LeetCode #117)

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests O(1) space tree manipulation. The "use what you've built" insight (using next pointers to traverse) is a beautiful algorithmic technique.

**What they want to see:**
- Recognition that BFS works but wastes space
- The O(1) space insight using already-established next pointers
- Clean handling of cross-subtree connections

**Follow-ups to prepare for:**
- LeetCode #117: Populating Next Right Pointers II (arbitrary binary tree)
- Binary Tree Right Side View
- Level Order Traversal

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| LC #117 Next Pointers II | Same idea but for non-perfect trees (harder) |
| Level Order Traversal | BFS approach is identical |
| Right Side View | Related level-by-level processing |
| Flatten Binary Tree | Another "rewire pointers" problem |
