# Root to Leaf Paths With Sum

> **Step 13 | 13.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## UNDERSTAND

### Problem Statement
Given a binary tree and a sum, determine if the tree has a **root-to-leaf path** such that adding up all the values along the path equals the given sum. A leaf is a node with no children.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| Tree: `[5,4,8,11,null,13,4,7,2,null,null,null,1]`, sum=22 | `true` | Path 5->4->11->2 = 22 |
| Tree: `[5,4,8,11,null,13,4,7,2,null,null,null,1]`, sum=26 | `true` | Path 5->8->13 = 26 |
| Tree: `[1,2,3]`, sum=5 | `false` | Paths are 1->2=3 and 1->3=4, neither is 5 |
| Tree: `[]`, sum=0 | `false` | Empty tree has no paths |
| Tree: `[1]`, sum=1 | `true` | Single node is both root and leaf |

### Analogy
Imagine you are hiking and you have a budget of calories to spend. At each trail fork, you spend some calories (node value). You want to know if there is any path from the trailhead (root) to a campsite (leaf) where you spend exactly your full budget.

### 3 Key Observations
1. **"Aha" -- Subtract instead of accumulate:** Instead of tracking running sums, subtract each node's value from the target. At a leaf, check if you hit exactly zero. This avoids carrying extra state.
2. **"Aha" -- Must reach a LEAF:** A common mistake is checking any node where sum matches. The problem specifically requires the path to end at a leaf (no children).
3. **"Aha" -- Early termination possible:** If you find one valid path, return true immediately. No need to explore the entire tree.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | List of paths | DFS collect all paths, check sums | Explicit but wasteful |
| Optimal | Recursion stack | DFS with subtraction | No extra storage for paths |
| Best | Explicit stack | Iterative DFS with (node, remaining) pairs | Same logic, no recursion |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Collect All Paths, Check Sums
**Intuition:** First collect every root-to-leaf path as a list of values. Then iterate over all paths and check if any path's sum equals the target.

**Steps:**
1. DFS through the tree, maintaining current path.
2. At each leaf, save a copy of the current path.
3. Backtrack (remove last element) when returning.
4. After collecting all paths, check each path's sum against target.

**Dry-run trace** with tree `[5,4,8]`, target=9:
```
DFS(5, path=[5])
  DFS(4, path=[5,4]) -> leaf, save [5,4]
  DFS(8, path=[5,8]) -> leaf, save [5,8]
Paths: [[5,4], [5,8]]
Check: sum([5,4])=9 == 9? YES -> return true
```

| Metric | Value |
|--------|-------|
| Time | O(n * h) -- n nodes traversed, paths up to length h |
| Space | O(n * h) -- storing all paths |

---

### Approach 2: Optimal -- DFS with Running Sum Subtraction
**Intuition:** Instead of storing paths, subtract the current node's value from the target as you recurse. At a leaf, check if the remaining value is zero. This eliminates all path storage.

**Steps:**
1. If root is null, return false.
2. Subtract root's value from target.
3. If root is a leaf (no children), return `remaining == 0`.
4. Recurse on left child OR right child with updated remaining.

**Dry-run trace** with tree `[5,4,8,11,null,13,4,7,2,null,null,null,1]`, target=22:
```
hasPath(5, 22): remaining=17, not leaf
  hasPath(4, 17): remaining=13, not leaf
    hasPath(11, 13): remaining=2, not leaf
      hasPath(7, 2): remaining=-5, leaf, -5!=0 -> false
      hasPath(2, 2): remaining=0, leaf, 0==0 -> TRUE!
    returns true
  returns true
returns true
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit each node at most once |
| Space | O(h) -- recursion stack depth |

---

### Approach 3: Best -- Iterative DFS with Stack
**Intuition:** Same subtraction logic but iteratively. Store `(node, remaining_sum)` pairs on a stack. This avoids recursion depth limits.

**Steps:**
1. Push `(root, target - root.val)` onto stack.
2. While stack is not empty:
   - Pop (node, remaining).
   - If leaf and remaining == 0, return true.
   - Push right child with updated remaining.
   - Push left child with updated remaining.
3. Return false.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node processed once |
| Space | O(h) -- stack depth bounded by tree height |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** In the worst case, no path matches and we must check every node. Early termination can make it faster on average.

**Why O(h) space:** The recursion/stack depth equals the longest root-to-leaf path, which is the tree height. For balanced trees this is O(log n), for skewed trees O(n).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `false` | No paths exist, even for sum=0 |
| Single node, val=target | `true` | Single node is a leaf |
| Single node, val!=target | `false` | Must match exactly |
| Negative values in tree | Still works | Subtraction handles negatives correctly |
| Target = 0, tree has path summing to 0 | `true` | Works with subtraction approach |

### Common Mistakes
- Checking non-leaf nodes (internal nodes where remaining happens to be 0 but path continues).
- Returning false for null tree with sum=0 (empty tree has NO root-to-leaf path).
- Not handling negative node values.

---

## INTERVIEW LENS

**Frequency:** Very high -- LeetCode 112 variant, InterviewBit staple.
**Follow-ups the interviewer might ask:**
- "Return all paths with the sum, not just existence" (Path Sum II -- collect paths)
- "What if the path doesn't have to start at root or end at leaf?" (Path Sum III -- prefix sums)
- "Return the actual path values" (Track path in recursion)

**What they're really testing:** Clean recursive thinking, understanding of tree path problems, and the leaf-check requirement.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Path Sum II (LeetCode 113) | Return all paths, not just boolean |
| Path Sum III (LeetCode 437) | Any-start, any-end paths with sum |
| Sum Root to Leaf Numbers | Similar traversal, different aggregation |
| Maximum Path Sum | Hardest path problem -- any path in tree |

### Real-World Use Case
**Network routing cost verification:** In network topology trees, each edge has a cost. Verifying if there exists a route from the central hub (root) to an endpoint (leaf) within a specific budget is exactly this problem. Network administrators use this to validate QoS (Quality of Service) constraints in hierarchical network designs.
