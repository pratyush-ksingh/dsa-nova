# Cousins in Binary Tree

> **Batch 4 of 12** | **Topic:** Binary Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a binary tree with **unique values** and two node values `x` and `y`, determine if the two nodes are **cousins**. Two nodes are cousins if they are at the **same depth** but have **different parents**.

**LeetCode #993**

### Examples

**Example 1:**
```
      1
     / \
    2   3
   /
  4
```
- x=4, y=3: depth(4)=2, depth(3)=1 -> Different depths -> **false**

**Example 2:**
```
      1
     / \
    2   3
     \   \
      4   5
```
- x=4, y=5: depth(4)=2, depth(5)=2, parent(4)=2, parent(5)=3 -> Same depth, different parents -> **true**

**Example 3:**
```
      1
     / \
    2   3
   /     \
  4       5
```
- x=2, y=3: depth(2)=1, depth(3)=1, parent(2)=1, parent(3)=1 -> Same parent -> **false** (siblings, not cousins)

### Real-Life Analogy
In a family tree, cousins are people of the same generation (same depth from the common ancestor) but with different parents. Your brother is NOT your cousin (same parent), but your uncle's child IS your cousin (same generation, different parent).

### 3 Key Observations
1. **"Aha!" -- Two conditions must BOTH hold:** Same depth AND different parents. Missing either makes them not cousins.
2. **"Aha!" -- BFS naturally gives depth:** Level-order traversal processes all nodes at the same depth together. Check if both x and y appear in the same level.
3. **"Aha!" -- Parent check during BFS:** While processing a level, if a single node has both x and y as children, they are siblings, not cousins.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Two DFS passes | Find depth and parent of each node separately | Simple but wasteful |
| Optimal | Single DFS | Find depth + parent for both in one pass | One traversal |
| Best | BFS level-order | Check same level + not siblings in one BFS | Natural level grouping |

---

## APPROACH LADDER

### Brute Force: Two Separate DFS
**Intuition:** Find the depth and parent of x, then find the depth and parent of y. Compare.

**Steps:**
1. DFS to find (depth_x, parent_x)
2. DFS to find (depth_y, parent_y)
3. Return depth_x == depth_y AND parent_x != parent_y

**BUD Analysis:**
- **B**ottleneck: Two full traversals
- **U**nnecessary: Second traversal -- could find both in one pass
- **D**uplicate: Same tree traversed twice

**Dry-Run Trace (Example 2, x=4, y=5):**
```
DFS for x=4: root(1,d=0)->left(2,d=1)->right(4,d=2) FOUND! depth=2, parent=2
DFS for y=5: root(1,d=0)->right(3,d=1)->right(5,d=2) FOUND! depth=2, parent=3
depth_x(2) == depth_y(2) AND parent_x(2) != parent_y(3) -> true
```

### Optimal: Single DFS
**Intuition:** One DFS pass. Track depth and parent for both x and y simultaneously.

**Steps:**
1. DFS with parameters: (node, parent, depth)
2. When node.val == x, record (depth_x, parent_x)
3. When node.val == y, record (depth_y, parent_y)
4. After DFS, check both conditions

### Best: BFS Level-Order
**Intuition:** BFS processes level by level. For each level, check if both x and y exist. While adding children, check if any single parent has both x and y as children (siblings check).

**Steps:**
1. BFS with queue initialized with root
2. For each level:
   a. Before processing children, check each node's children -- if both are x and y, return false (siblings)
   b. Process the level, track if x found and y found
   c. If both found in this level, return true
   d. If only one found, return false (different depths guaranteed)
3. Return false if neither found at any common level

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N) | O(H) | Two DFS, each O(N) worst case |
| Optimal | O(N) | O(H) | Single DFS, recursion stack = height |
| Best | O(N) | O(W) | BFS, queue holds at most one level (W = max width) |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| x and y are siblings | false | Same depth but same parent |
| x is root | false | Root has no parent, can't be cousin |
| x or y not in tree | false | Handle gracefully |
| One node is ancestor of other | false | Different depths |
| Tree with only root | false | No cousins possible |

**Common Mistakes:**
- Forgetting the "different parents" check (just checking same depth)
- Not handling the case where x or y is the root
- Checking node values instead of node references for parent comparison

---

## INTERVIEW LENS

**Why interviewers ask this:** Simple BFS/DFS problem that tests attention to detail (two conditions) and clean implementation.

**What they want to see:**
- Clear understanding of "cousin" definition
- Choice between BFS (natural for level problems) and DFS
- Clean sibling check

**Follow-ups to prepare for:**
- LeetCode #1161: Maximum Level Sum of a Binary Tree
- All Nodes Distance K in Binary Tree
- Binary Tree Right Side View

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Same Tree / Symmetric Tree | Tree comparison patterns |
| Maximum Depth | Depth-finding is a subroutine here |
| Binary Tree Level Order | BFS level processing is the core technique |
| Lowest Common Ancestor | Related family-tree concept |
