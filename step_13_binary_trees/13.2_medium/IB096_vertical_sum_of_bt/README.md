# Vertical Sum of Binary Tree

> **Batch 4 of 12** | **Topic:** Binary Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a binary tree, find the **vertical sum** of nodes. Nodes on the same vertical line (same horizontal distance from root) should be summed together. Return the sums from leftmost to rightmost vertical line.

### Examples

**Example 1:**
```
        1
       / \
      2    3
     / \  / \
    4   5 6   7
```
Vertical lines:
- HD -2: [4] -> sum = 4
- HD -1: [2] -> sum = 2
- HD  0: [1, 5, 6] -> sum = 12
- HD +1: [3] -> sum = 3
- HD +2: [7] -> sum = 7
Output: [4, 2, 12, 3, 7]

**Example 2:**
```
    1
   /
  2
 /
3
```
Output: [3, 2, 1] (each node on its own vertical line)

### Real-Life Analogy
Imagine rain falling straight down on a tree. Each vertical column of rain catches certain branches. The "vertical sum" is the total weight of branches caught by each column. Branches at the same horizontal position get weighed together regardless of height.

### 3 Key Observations
1. **"Aha!" -- Horizontal distance defines grouping:** Root is at HD=0. Going left means HD-1, going right means HD+1. Two nodes share a vertical line iff they have the same HD.
2. **"Aha!" -- Any traversal works:** We just need to visit every node and record its HD. BFS or DFS both work since we only care about grouping by HD, not order within a group.
3. **"Aha!" -- Track min/max HD for O(1) ordering:** Instead of sorting keys or using a TreeMap, track minHD and maxHD during traversal, then iterate from min to max.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | HashMap + Sort | DFS + sort keys | Simple to implement |
| Optimal | TreeMap / SortedDict | DFS with HD tracking | Auto-sorted keys |
| Best | HashMap + min/max HD | BFS with HD tracking | O(N) total, no sorting needed |

---

## APPROACH LADDER

### Brute Force: DFS + HashMap + Sort Keys
**Intuition:** Traverse the tree, assign each node an HD. Use a HashMap to accumulate sums per HD. Sort keys at the end.

**Steps:**
1. Start DFS from root with HD = 0
2. For each node, add its value to map[HD]
3. Go left with HD-1, go right with HD+1
4. After traversal, sort the keys and collect values

**BUD Analysis:**
- **B**ottleneck: Sorting keys at the end (O(k log k) where k = number of distinct HDs)
- **U**nnecessary: The sorting step -- could use a sorted container or track range
- **D**uplicate: None

**Dry-Run Trace (Example 1):**
```
Node 1, HD=0  -> map: {0:1}
Node 2, HD=-1 -> map: {0:1, -1:2}
Node 4, HD=-2 -> map: {0:1, -1:2, -2:4}
Node 5, HD=0  -> map: {0:6, -1:2, -2:4}
Node 3, HD=1  -> map: {0:6, -1:2, -2:4, 1:3}
Node 6, HD=0  -> map: {0:12, -1:2, -2:4, 1:3}
Node 7, HD=2  -> map: {0:12, -1:2, -2:4, 1:3, 2:7}
Sort keys: [-2,-1,0,1,2] -> [4,2,12,3,7]
```

### Optimal: DFS + TreeMap (Auto-Sorted)
**Intuition:** Replace HashMap with TreeMap so keys stay sorted. Skip the sort step.

**Steps:**
1. Use TreeMap (Java) keyed by HD
2. DFS: for each node, map[HD] += node.val
3. Left child -> HD-1, right child -> HD+1
4. Return map.values() directly (already sorted)

### Best: BFS + HashMap + min/max HD
**Intuition:** Iterative BFS avoids recursion stack. Track min and max HD during traversal. Iterate from minHD to maxHD for output -- no sorting needed.

**Steps:**
1. Queue stores (node, HD) pairs. Track minHD = maxHD = 0
2. Poll node, add val to map[HD], update minHD/maxHD
3. Enqueue left child with HD-1, right child with HD+1
4. Iterate range(minHD, maxHD+1) and collect sums

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N + k log k) | O(N) | Visit all nodes + sort k distinct HDs |
| Optimal | O(N log k) | O(N) | TreeMap insert is O(log k) per node |
| Best | O(N) | O(N) | HashMap O(1) per insert, linear scan for output |

*N = number of nodes, k = number of distinct vertical lines (at most N but usually much less)*

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Empty tree (null root) | [] | Must handle null check |
| Single node | [node.val] | Only HD=0 exists |
| Left-skewed tree | Each node on separate vertical | HDs: 0, -1, -2, ... |
| Right-skewed tree | Each node on separate vertical | HDs: 0, 1, 2, ... |
| Zigzag tree (L-R-L-R) | Multiple nodes share HD=0 | All overlap on center line |

**Common Mistakes:**
- Forgetting to handle null root
- Using unsorted map and not sorting keys before returning
- Off-by-one in horizontal distance direction (left should decrease HD)

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests understanding of tree coordinate systems and combining traversal with hash maps. Shows you can think beyond simple traversal.

**What they want to see:**
- Immediate recognition that "vertical" = horizontal distance concept
- Clean recursive or iterative solution
- Awareness of output ordering requirement

**Follow-ups to prepare for:**
- Vertical Order Traversal (same idea but return nodes grouped, not summed)
- Top View / Bottom View of binary tree
- Diagonal Sum of binary tree

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Vertical Order Traversal | Same HD concept, but collect nodes instead of summing |
| Top/Bottom View | Same HD tracking, but keep first/last node per HD |
| Binary Tree Columns | Generalized vertical line problems |
| Diagonal Traversal | Uses d = row - col instead of just col |
