# Root to Node Path in Binary Tree

> **Step 13 | 13.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** Striver A2Z | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree and a target integer value, return the **path from the root to the node** containing the target value as a list of node values. Return an empty list if the node is not found.

### Analogy
Think of a city road network as a tree. You want driving directions from the city center (root) to a specific address (target node). You need every intersection (node) you pass through, in order.

### Key Observations
1. **DFS naturally builds the path.** As we recurse deeper, we're building the path step by step. If we reach the target, we're done; otherwise, we backtrack.
2. **Backtracking is essential.** When a subtree does not contain the target, we must "undo" adding that node to the path.
3. **Return value signals success.** The recursive function returns `True`/`false` to indicate whether the target was found in that subtree, propagating the found signal up.

### Examples

```
Tree:
        1
       / \
      2   3
     / \ / \
    4  5 6  7
```

| Input (target) | Output (path) |
|---------------|---------------|
| 5 | [1, 2, 5] |
| 7 | [1, 3, 7] |
| 1 | [1] |
| 4 | [1, 2, 4] |
| 9 | [] (not found) |

### Constraints
- 1 <= number of nodes <= 10^4
- -10^9 <= Node.val <= 10^9
- Node values may not be unique (handle by first occurrence in DFS order)
- Target may not exist in the tree

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (BFS + parent map) | Queue + HashMap | BFS visits all nodes; parent map allows backtracking to reconstruct path |
| Optimal (DFS recursive) | Recursion stack | Backtracking naturally builds the path; stops early on find |
| Best (Iterative DFS) | Explicit stack with path | Avoids recursion limit; same O(h) space in practice |

**Pattern cue:** "Path from root to node" -> DFS with backtracking. The key insight is returning a boolean to signal whether the target was found.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (BFS + Parent Map)
**Intuition:** BFS explores all nodes level-by-level. Track each node's parent. Once the target is found, backtrack through the parent chain to build the path.

**Steps:**
1. Start BFS from root, recording `parent[node] = parent_node` for each visited node.
2. When `node.val == target`, stop BFS and save `targetNode`.
3. Backtrack: starting from `targetNode`, follow `parent` pointers to root, collecting values.
4. Reverse the collected values to get root-to-target order.

**Dry-Run -- target=5:**
```
BFS: visit 1 -> parent[2]=1, parent[3]=1
     visit 2 -> parent[4]=2, parent[5]=2
     visit 3 -> parent[6]=3, parent[7]=3
     visit 4
     visit 5 -> FOUND! targetNode=5

Backtrack: 5 -> parent[5]=2 -> parent[2]=1 -> parent[1]=None
Collected: [5, 2, 1] -> reversed: [1, 2, 5]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- BFS visits all nodes in worst case |
| Space | O(n) -- parent map stores entry for every node |

---

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Parent map stores all n nodes even though we only need the path (O(h) nodes). DFS with backtracking builds the path in-place without the map.

### Approach 2 -- Optimal (DFS Recursive + Backtracking)
**Intuition:** Add the current node to the path. If it's the target, return True (success). Otherwise, try left and right subtrees. If neither contains the target, pop the current node (backtrack) and return False.

**Steps:**
1. Base case: if `node == None`, return `False`.
2. Append `node.val` to `path`.
3. If `node.val == target`, return `True`.
4. If `dfs(left)` or `dfs(right)` returns `True`, propagate `True` upward (path is correct).
5. Otherwise, `path.pop()` (backtrack) and return `False`.

**Dry-Run -- target=5:**
```
dfs(1): path=[1], not target
  dfs(2): path=[1,2], not target
    dfs(4): path=[1,2,4], not target
      dfs(None): False
      dfs(None): False
    pop 4, path=[1,2], return False
    dfs(5): path=[1,2,5], == target! return True
  return True (left subtree found it)
return True
Final path: [1, 2, 5]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visits each node once |
| Space | O(h) -- recursion depth = tree height; path holds at most h elements |

---

### Approach 3 -- Best (Iterative DFS with Explicit Stack)
**Intuition:** Simulate the recursive DFS with an explicit stack. Each stack entry carries the current node AND the path to that node. When we find the target, we immediately return the stored path.

**Steps:**
1. Push `(root, [root.val])` onto the stack.
2. While stack is non-empty:
   a. Pop `(node, path)`.
   b. If `node.val == target`, return `path`.
   c. Push right child with `path + [right.val]` (if exists).
   d. Push left child with `path + [left.val]` (if exists).
3. If stack empties, return `[]`.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) DFS stack depth; O(n) worst case for path copies |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) time for all:** Every node is visited at most once.
- **O(n) space for BFS brute:** The parent map has one entry per node.
- **O(h) space for DFS approaches:** The recursion stack / explicit DFS stack has depth equal to tree height. For balanced trees h = O(log n); skewed trees h = O(n).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return [] immediately |
| Target is root | Return [root.val] |
| Target not in tree | Return [] (DFS backtracks completely) |
| Duplicate values | Returns path to first match in DFS order |

**Common mistakes:**
- Forgetting to `pop` after failed subtree exploration (backtracking mistake).
- In Java: `list.remove(index)` vs `list.remove(Integer.valueOf(x))` -- use index to remove last element.
- In iterative approach: forgetting to push right before left (left should be processed first in DFS, so push right first).

---

## 6. REAL-WORLD USE CASE

**File system path resolution:** In an operating system's directory tree, finding the absolute path to a file is exactly this problem. The root is `/`, each directory is an internal node, each file is a leaf. When a user opens a file by name, the OS traverses the directory tree and returns the full path.

**Network routing:** In a tree-shaped network topology (like a corporate network), routing packets from a core router (root) to a specific device (target node) requires knowing the exact path through intermediate routers.

---

## 7. INTERVIEW TIPS

- **Clarify uniqueness:** Ask if values are unique. If not, does the interviewer want the path to the first occurrence?
- **State the backtracking insight clearly:** "I add the node to the path, and only remove it if neither subtree found the target."
- **Mention the return value trick:** Returning a boolean from the recursive call is the key that makes backtracking work cleanly.
- **Space trade-off:** BFS+parent map is O(n) space; DFS backtracking is O(h). In an interview, lead with DFS.
- **Follow-up:** "Can you find the path between any two nodes?" -> Find paths root->A and root->B, find their LCA, combine.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Path Sum (LC #112) | DFS with path tracking; returns bool instead of path |
| Path Sum II (LC #113) | Collect ALL root-to-leaf paths with target sum; same backtracking |
| Lowest Common Ancestor (LC #236) | Uses root-to-node paths for both nodes, then finds divergence |
| Binary Tree Paths (LC #257) | Enumerate all root-to-leaf paths; same DFS + backtrack |
| Distance Between Nodes | LCA + two root-to-node paths |
