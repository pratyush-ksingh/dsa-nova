# Search in BST

> **Batch 1 of 12** | **Topic:** Binary Search Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #700**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a Binary Search Tree (BST) and an integer `val`, find the node in the BST whose value equals `val`. Return the **subtree** rooted at that node. If such a node does not exist, return `null`.

**BST Property:** For every node, all values in the left subtree are strictly less, and all values in the right subtree are strictly greater.

### Analogy
Searching a BST is exactly like **looking up a word in a dictionary**. You open to the middle. If your word comes before the current page, you go left (earlier pages). If after, you go right (later pages). You never need to check every page -- each comparison eliminates half the remaining pages.

### Key Observations
1. The BST property means at each node, you know **which half** to search. **Aha:** This is binary search on a tree structure -- O(log n) for balanced trees.
2. If `val < node.val`, the answer must be in the left subtree. If `val > node.val`, it must be in the right subtree. **Aha:** Only one path from root to the target node is ever explored.
3. Both recursive and iterative solutions are trivial. **Aha:** Unlike general tree search, BST search never needs backtracking -- you always go one direction.

### Examples

```
BST:     4
        / \
       2   7
      / \
     1   3

val=2 -> Return subtree rooted at 2: [2,1,3]
val=5 -> Return null (not found)
```

| Input (root, val) | Output |
|-------------------|--------|
| [4,2,7,1,3], val=2 | [2,1,3] |
| [4,2,7,1,3], val=5 | [] |
| [1], val=1 | [1] |
| [], val=1 | [] |

### Constraints
- 1 <= number of nodes <= 5000
- 1 <= Node.val <= 10^7
- All values are unique
- `val` is guaranteed to be a valid integer

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (traverse entire tree) | DFS/BFS | Ignore BST property, check every node. O(n). |
| Optimal (BST-guided recursive) | Recursive calls | Use BST property to go left or right. O(h). |
| Best (BST-guided iterative) | While loop | Same logic, no recursion stack. O(h) time, O(1) space. |

**Pattern cue:** "Search in BST" -> compare with current, go left or right. Binary search on tree.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Ignore BST Property)
**Intuition:** Traverse every node (DFS or BFS) and check if any node has `val`.

**Steps:**
1. Perform DFS/BFS on the entire tree.
2. If any node.val == val, return that node.
3. If traversal completes, return null.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visits every node |
| Space | O(h) recursion or O(w) BFS queue |

### BUD Transition (Brute -> Optimal)
**Unnecessary work:** We are visiting nodes we do not need to. The BST property tells us exactly which direction to go at each node. If val < node.val, the answer cannot be in the right subtree -- skip it entirely.

### Approach 2 -- Optimal (Recursive BST Search)
**Intuition:** At each node, compare val with node.val. Go left if smaller, right if larger, return if equal.

**Steps:**
1. If node is null, return null.
2. If val == node.val, return node.
3. If val < node.val, return search(node.left, val).
4. If val > node.val, return search(node.right, val).

**Dry-Run Trace -- BST [4,2,7,1,3], val=2:**

| Step | Node | Compare | Action |
|------|------|---------|--------|
| 1 | 4 | 2 < 4 | go left |
| 2 | 2 | 2 == 2 | found! return node(2) |

**Dry-Run Trace -- val=5:**

| Step | Node | Compare | Action |
|------|------|---------|--------|
| 1 | 4 | 5 > 4 | go right |
| 2 | 7 | 5 < 7 | go left |
| 3 | null | - | return null |

| Metric | Value |
|--------|-------|
| Time | O(h) -- O(log n) balanced, O(n) skewed |
| Space | O(h) recursion stack |

### Approach 3 -- Best (Iterative BST Search)
**Intuition:** Same logic but with a while loop. No recursion overhead.

**Steps:**
1. Set `current = root`.
2. While current is not null:
   - If val == current.val, return current.
   - If val < current.val, current = current.left.
   - Else, current = current.right.
3. Return null.

| Metric | Value |
|--------|-------|
| Time | O(h) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(h) time:** We follow one path from root to the target (or to a leaf). In a balanced BST, h = log n. In a skewed BST, h = n.
- **O(1) iterative space:** Just one pointer variable moving down the tree.
- **O(h) recursive space:** One stack frame per level.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return null |
| val at root | Return root immediately |
| val not in tree | Return null after reaching a leaf |
| Single node, val matches | Return the node |
| Single node, val does not match | Return null |

**Common mistakes:**
- Searching both left and right (ignoring BST property) -- O(n) instead of O(h).
- Forgetting to return null when node is null (causes NullPointerException).
- Returning just the value instead of the subtree (problem asks for the subtree).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| What if duplicates exist? | Define convention: e.g., duplicates go left. Adjust the comparison. |
| How to guarantee O(log n)? | Use a self-balancing BST (AVL, Red-Black). Standard BST has no guarantee. |
| Can we do better than O(log n)? | Not with a BST. Hash table gives O(1) but loses ordering. |
| What if the tree is not a BST? | Must traverse all nodes -- O(n). BST property is the enabler. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Insert into BST (LC #701) | Same navigation logic; insert at the null position |
| Delete Node in BST (LC #450) | Find node first (search), then handle 3 deletion cases |
| Validate BST (LC #98) | Uses BST property for range checking |
| Two Sum in BST (LC #653) | Search for complement of each node |
| Closest Value in BST (LC #270) | Search path, track closest value seen |

---

## Real-World Use Case
**Autocomplete in search engines:** Google's search bar uses a BST-like trie structure where searching follows the BST property (left < root < right) to narrow down suggestions. Each keystroke prunes half the search space, giving O(log N) lookup time for instant suggestions from billions of queries.
