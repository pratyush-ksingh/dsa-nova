# Sum Root to Leaf Numbers

> **Batch 4 of 12** | **Topic:** Binary Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a binary tree where each node contains a single digit (0-9), each root-to-leaf path represents a number. Find the **total sum** of all root-to-leaf numbers.

**LeetCode #129**

### Examples

**Example 1:**
```
    1
   / \
  2   3
```
- Path 1->2 = number 12
- Path 1->3 = number 13
- Total = 12 + 13 = **25**

**Example 2:**
```
      4
     / \
    9   0
   / \
  5   1
```
- Path 4->9->5 = 495
- Path 4->9->1 = 491
- Path 4->0 = 40
- Total = 495 + 491 + 40 = **1026**

### Real-Life Analogy
Think of a phone tree menu: "Press 4, then press 9, then press 5 to reach billing." Each complete sequence of button presses forms a number. You want the sum of all possible complete phone numbers you can dial by following every path to the end.

### 3 Key Observations
1. **"Aha!" -- Number builds as you go deeper:** At each node, the running number = parent_number * 10 + current_digit. This is exactly how decimal digits accumulate.
2. **"Aha!" -- Only leaf nodes contribute to the sum:** Non-leaf nodes are intermediate -- their value is part of a number but the number is only "complete" at a leaf.
3. **"Aha!" -- Top-down DFS is natural:** Pass the running number down. At a leaf, add it to the total. No need to build strings or collect paths.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | List of strings | DFS to collect all paths, convert to numbers, sum | Easy to understand |
| Optimal | DFS with running value | Top-down DFS passing accumulated number | No string overhead |
| Best | BFS / Morris | Iterative BFS with running numbers, or Morris for O(1) space | Avoids recursion stack |

---

## APPROACH LADDER

### Brute Force: Collect All Paths as Strings
**Intuition:** Collect every root-to-leaf path as a string of digits. Convert each string to an integer. Sum them all.

**Steps:**
1. DFS with a path string accumulator
2. At each leaf, add the path string to a list
3. Convert all path strings to integers and sum

**BUD Analysis:**
- **B**ottleneck: String concatenation at each level
- **U**nnecessary: Building full strings when we only need the numeric value
- **D**uplicate: Converting strings back to numbers

**Dry-Run Trace (Example 2):**
```
DFS(4, ""): path = "4"
  DFS(9, "4"): path = "49"
    DFS(5, "49"): path = "495" -> LEAF, add "495"
    DFS(1, "49"): path = "491" -> LEAF, add "491"
  DFS(0, "4"): path = "40" -> LEAF, add "40"
Paths: ["495", "491", "40"]
Sum: 495 + 491 + 40 = 1026
```

### Optimal: Top-Down DFS with Running Number
**Intuition:** Pass the running number (parent * 10 + current) down the recursion. At leaves, return the number. At internal nodes, return sum of left and right subtree results.

**Steps:**
1. DFS(node, currentNum): currentNum = currentNum * 10 + node.val
2. If leaf: return currentNum
3. Else: return DFS(left, currentNum) + DFS(right, currentNum)

### Best: Iterative BFS with Running Numbers
**Intuition:** Use a queue storing (node, running_number) pairs. Process level by level. At each leaf, add its number to the total sum.

**Steps:**
1. Queue initialized with (root, root.val)
2. While queue not empty:
   a. Poll (node, num)
   b. If leaf: add num to total
   c. Else: enqueue children with num * 10 + child.val

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N * H) | O(N * H) | String building O(H) per path, O(L) paths where L = leaves |
| Optimal | O(N) | O(H) | Visit each node once, recursion depth = height |
| Best | O(N) | O(W) | BFS queue holds at most one level width |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Single node (leaf = root) | node.val | The single digit IS the number |
| All left children | One path, one number | Linear chain forms one large number |
| Node value = 0 | Leading zeros ok | Path 1->0->2 = 102, not 12 |
| Very deep tree | Large numbers | May overflow int -- use long |

**Common Mistakes:**
- Returning currentNum at non-leaf nodes (should only return at leaves)
- Forgetting the null child case: if a node has only one child, the other is null but the node is NOT a leaf
- Integer overflow for deep trees (use long in Java)

---

## INTERVIEW LENS

**Why interviewers ask this:** Clean DFS problem. Tests top-down parameter passing and understanding of what constitutes a leaf.

**What they want to see:**
- The `num * 10 + digit` formula (immediate recognition)
- Correct leaf detection (both children null)
- Clean recursive or iterative solution

**Follow-ups to prepare for:**
- Path Sum I/II/III
- Binary Tree Maximum Path Sum
- Smallest String Starting From Leaf

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Path Sum | Same DFS path-tracking, different goal |
| Path Sum II | Collect paths instead of sum numbers |
| Binary Tree Paths | String version of path collection |
| Maximum Path Sum | Path value accumulation concept |
