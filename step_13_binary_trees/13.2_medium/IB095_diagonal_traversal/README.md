# Diagonal Traversal

> **Batch 3 of 12** | **Topic:** Binary Trees | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a binary tree, return its **diagonal traversal**. Nodes that lie on the same diagonal (from upper-right to lower-left) should be grouped together. Within each diagonal, nodes appear in the order they are visited from top to bottom, left to right. Going right stays on the same diagonal; going left moves to the next diagonal. *(InterviewBit)*

### Examples

```
         8
        / \
       3   10
      / \    \
     1   6    14
        / \   /
       4   7 13
```

| Diagonal | Nodes | Explanation |
|----------|-------|-------------|
| 0 | `[8, 10, 14]` | Start at root, keep going right |
| 1 | `[3, 6, 7, 13]` | Left children of diagonal 0 nodes, then their right chains |
| 2 | `[1, 4]` | Left children of diagonal 1 nodes |

**Output:** `[[8,10,14], [3,6,7,13], [1,4]]`

### Analogy
Imagine rain falling at a 45-degree angle from upper-left to lower-right on a tree drawn on paper. Each "raindrop path" traces a diagonal. All nodes hit by the same raindrop belong to the same diagonal. Going right from any node keeps you on the same raindrop path. Going left starts a new raindrop path one level deeper.

### 3 Key Observations
1. **"Aha" -- Right child = same diagonal, Left child = next diagonal:** This is the key insight. Assign diagonal number `d` to root. For any node with diagonal `d`: its right child has diagonal `d`, its left child has diagonal `d+1`.
2. **"Aha" -- Queue-based BFS works naturally:** Process nodes by diagonal. When you visit a node, follow its entire right chain (same diagonal). For each left child encountered along the way, enqueue it for the next diagonal.
3. **"Aha" -- This is equivalent to `row - col` grouping:** If you assign coordinates (row, col) where going left is (row+1, col-1) and going right is (row+1, col+1), then nodes on the same diagonal have the same `row - col` value (or equivalently, same "slope distance" from the root's diagonal).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | HashMap + DFS | Assign diagonal IDs, group by ID | Works but needs sorting of keys |
| Optimal | Queue | BFS: follow right chains, enqueue left children | Clean level-by-level diagonal processing |
| Best | Queue (optimized) | Same BFS, single-pass | Same complexity, streamlined code |

---

## APPROACH LADDER

### Approach 1: Brute Force -- HashMap + DFS
**Intuition:** Do a DFS, assigning each node a diagonal number. Root gets diagonal 0. Right child inherits parent's diagonal. Left child gets parent's diagonal + 1. Collect nodes into a map keyed by diagonal number.

**Steps:**
1. DFS from root with diagonal `d = 0`.
2. Add `node.val` to `map[d]`.
3. Recurse left with `d + 1`.
4. Recurse right with `d` (same diagonal).
5. After DFS, extract map entries sorted by key.

**Dry-run trace** with the example tree:
```
dfs(8, d=0): map[0]=[8]. Left: dfs(3,1). Right: dfs(10,0).
  dfs(3, d=1): map[1]=[3]. Left: dfs(1,2). Right: dfs(6,1).
    dfs(1, d=2): map[2]=[1]. No children.
    dfs(6, d=1): map[1]=[3,6]. Left: dfs(4,2). Right: dfs(7,1).
      dfs(4, d=2): map[2]=[1,4]. No children.
      dfs(7, d=1): map[1]=[3,6,7]. No children.
  dfs(10, d=0): map[0]=[8,10]. Right: dfs(14,0).
    dfs(14, d=0): map[0]=[8,10,14]. Left: dfs(13,1).
      dfs(13, d=1): map[1]=[3,6,7,13]. No children.

Result: [[8,10,14], [3,6,7,13], [1,4]]
```

| Metric | Value |
|--------|-------|
| Time | O(n) DFS + O(d log d) sorting diagonals (d = number of diagonals) |
| Space | O(n) for the map |

---

### Approach 2: Optimal -- BFS with Right-Chain Following
**Intuition:** Process diagonals one at a time. For each diagonal, process all nodes: follow right chains (same diagonal), and enqueue left children (next diagonal). The queue naturally groups nodes by diagonal.

**Steps:**
1. Start with root in the queue.
2. While queue is not empty:
   - Process all nodes currently in the queue (one diagonal's worth).
   - For each node, follow its right chain: add each node to current diagonal's list, and enqueue each left child encountered.
3. Each iteration of the outer loop produces one diagonal.

**Dry-run trace:**
```
Queue: [8]. Diagonal 0:
  Process 8 -> add 8. Left child 3 -> enqueue. Go right to 10.
  Process 10 -> add 10. No left. Go right to 14.
  Process 14 -> add 14. Left child 13 -> enqueue. No right.
  Diagonal 0 = [8, 10, 14]. Queue: [3, 13]

Queue: [3, 13]. Diagonal 1:
  Process 3 -> add 3. Left child 1 -> enqueue. Go right to 6.
  Process 6 -> add 6. Left child 4 -> enqueue. Go right to 7.
  Process 7 -> add 7. No left. No right.
  Process 13 -> add 13. No left. No right.
  Diagonal 1 = [3, 6, 7, 13]. Queue: [1, 4]

Queue: [1, 4]. Diagonal 2:
  Process 1 -> add 1. No children.
  Process 4 -> add 4. No children.
  Diagonal 2 = [1, 4]. Queue: []

Result: [[8,10,14], [3,6,7,13], [1,4]]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node visited once |
| Space | O(n) -- queue + result |

---

### Approach 3: Best -- Streamlined Queue BFS
**Intuition:** Same as Approach 2, but instead of processing diagonals in batches, use a simple queue where we enqueue left children as we encounter them, and always follow right chains immediately.

**Steps:**
1. Enqueue root.
2. While queue not empty:
   - Dequeue node.
   - While node is not null:
     - Add node.val to current diagonal.
     - If node.left exists, enqueue it.
     - Move to node.right.
3. Start a new diagonal when processing each dequeued node that begins a new chain.

Note: This is effectively the same algorithm as Approach 2. The distinction is code cleanliness -- we flatten the result into a single list if grouping isn't needed, or group by tracking diagonal boundaries.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** Each node is visited exactly once (either by following a right chain or by being dequeued). No node is processed twice.

**Why the queue holds at most O(n) nodes:** In the worst case (a complete binary tree), every node has a left child, and all those left children end up in the queue before being processed. But across the entire traversal, each node enters the queue at most once.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `[]` | Check root is null |
| Single node | `[[val]]` | One diagonal with one node |
| Right-skewed tree | `[[all nodes]]` | Everything on one diagonal |
| Left-skewed tree | `[[n1],[n2],[n3],...]` | Each node on its own diagonal |
| Complete tree | Multiple diagonals | Standard balanced case |

### Common Mistakes
- Confusing diagonal traversal with anti-diagonal -- direction matters.
- In BFS approach: not following the entire right chain (stopping after one node per dequeue).
- In DFS approach: visiting right child with `d+1` instead of `d` (should be same diagonal).
- Ordering within a diagonal: DFS preorder gives correct order if you go right before processing left's deeper nodes.

---

## INTERVIEW LENS

**Frequency:** Medium -- appears on InterviewBit and GFG, less common on LeetCode.
**Follow-ups the interviewer might ask:**
- "What about anti-diagonal traversal?" (Swap: left = same diagonal, right = next)
- "What if you need the sum of each diagonal instead of listing nodes?" (Same algorithm, aggregate values)
- "Vertical order traversal?" (Group by column instead of diagonal)

**What they're really testing:** Can you define a non-standard traversal order and implement it cleanly? Do you understand how "diagonal" maps to tree edge directions?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Vertical Order Traversal (LC #987) | Group by column instead of diagonal |
| Binary Tree Level Order (LC #102) | Group by level (row); diagonal groups by row-col |
| Boundary Traversal | Another non-standard traversal pattern |
| Right Side View (LC #199) | Follows right chains similarly |

### Real-World Use Case
**Pixel rendering in ray tracing:** In computer graphics, diagonal traversal patterns appear when rendering image tiles. GPUs process pixels along diagonals to maximize cache coherence -- adjacent pixels in a diagonal share similar data dependencies. This same "follow one direction, queue the other" pattern optimizes memory access patterns in image processing pipelines.
