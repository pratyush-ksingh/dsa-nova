# Two Sum in BST

> **Batch 1 of 12** | **Topic:** Binary Search Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #653**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a Binary Search Tree and a target integer `k`, return `true` if there exist **two distinct elements** in the BST such that their sum equals `k`.

### Analogy
Think of the classic **Two Sum** problem but on a BST instead of an array. Just like sorting an array enables a two-pointer approach, the BST's inorder traversal gives you a sorted sequence. You can then apply the exact same two-pointer technique on that sorted sequence.

### Key Observations
1. Inorder traversal of a BST gives a **sorted array**. **Aha:** Once you have the sorted array, this reduces to the classic Two Sum with sorted input -- two pointers from both ends.
2. Alternatively, for each node with value `v`, search for `k - v` in the BST. **Aha:** BST search is O(h), so checking all n nodes costs O(n * h). With a HashSet, lookup is O(1).
3. The HashSet approach with DFS is O(n) time and O(n) space, regardless of tree shape. **Aha:** This is the simplest and most robust approach.

### Examples

```
BST:     5
        / \
       3   6
      / \   \
     2   4   7

k=9 -> true  (2+7=9)
k=28 -> false (no pair sums to 28)
```

| Input (root, k) | Output |
|-----------------|--------|
| [5,3,6,2,4,null,7], k=9 | true |
| [5,3,6,2,4,null,7], k=28 | false |
| [2,1,3], k=4 | true (1+3=4) |
| [1], k=2 | false (only one element) |
| [1,null,2], k=3 | true |

### Constraints
- 1 <= number of nodes <= 10^4
- -10^4 <= Node.val <= 10^4
- All values are unique
- -10^5 <= k <= 10^5

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (check all pairs) | Nested DFS | For each node, search tree for complement. O(n * h). |
| Optimal (HashSet + DFS) | HashSet | DFS; for each node check if k-val is in set. O(n) time, O(n) space. |
| Best (inorder + two pointers) | Sorted array from inorder | O(n) time, O(n) space, but leverages BST property elegantly. |

**Pattern cue:** "Two sum" + "BST" = inorder to sorted array + two pointers, or HashSet during traversal.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (For Each Node, Search BST)
**Intuition:** For each node with value `v`, search the BST for `k - v`. If found and it is a different node, return true.

**Steps:**
1. DFS through every node.
2. For each node with value `v`, BST-search for `k - v`.
3. Ensure we do not match a node with itself (check `k - v != v` or different node reference).
4. If any pair found, return true.

| Metric | Value |
|--------|-------|
| Time | O(n * h) -- O(n log n) balanced, O(n^2) skewed |
| Space | O(h) recursion |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Searching the BST for each node is O(h) per node. A HashSet makes lookup O(1), reducing total to O(n).

### Approach 2 -- Optimal (HashSet + DFS)
**Intuition:** Traverse the tree (any order). For each node `v`, check if `k - v` is in a HashSet. If yes, return true. Otherwise, add `v` to the set.

**Steps:**
1. Initialize empty HashSet.
2. DFS/BFS through the tree.
3. For each node value `v`:
   - If `k - v` is in the set, return true.
   - Add `v` to the set.
4. Return false.

**Dry-Run Trace -- BST [5,3,6,2,4,null,7], k=9:**

| Node | v | k-v | In Set? | Set After |
|------|---|-----|---------|-----------|
| 5 | 5 | 4 | No | {5} |
| 3 | 3 | 6 | No | {5,3} |
| 2 | 2 | 7 | No | {5,3,2} |
| 4 | 4 | 5 | Yes! | return true |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) for HashSet |

### Approach 3 -- Best (Inorder + Two Pointers)
**Intuition:** Inorder traversal gives sorted array. Use two pointers (left at start, right at end) to find a pair summing to k.

**Steps:**
1. Perform inorder traversal to get sorted array.
2. Set `lo = 0`, `hi = len - 1`.
3. While `lo < hi`:
   - `sum = arr[lo] + arr[hi]`
   - If sum == k: return true.
   - If sum < k: lo++.
   - If sum > k: hi--.
4. Return false.

**Dry-Run Trace -- sorted=[2,3,4,5,6,7], k=9:**

| lo | hi | arr[lo] | arr[hi] | Sum | Action |
|----|----|---------|---------|-----|--------|
| 0 | 5 | 2 | 7 | 9 | 9==9, return true |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) for sorted array |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n) for both optimal approaches:** Visit each node once. HashSet lookups are O(1). Two-pointer scan is O(n).
- **O(n) space:** HashSet stores all values, or the sorted array has n elements. Both use O(n).
- A true O(h) space solution exists using **BST iterator** (two iterators: one forward, one reverse), but it is more complex to implement.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Single node | Cannot form a pair; return false |
| k = 2 * node.val for some node | Need two distinct nodes, not the same node twice. The two-pointer approach handles this naturally (lo < hi). HashSet handles it because k-v == v means we would need another node with value v, which does not exist (all unique). |
| Negative values | Works the same; sum can be negative |
| All negative, k positive | No pair sums to k; return false |
| Two nodes, k = sum | Return true |

**Common mistakes:**
- HashSet: counting a node as its own pair (e.g., k=10, node=5 -- finding 5 in set means it is the same node). With unique values this is not an issue, but be careful.
- Inorder: not completing the full traversal before applying two pointers.
- Forgetting that BST values are unique (simplifies the duplicate check).

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Which approach is best? | HashSet is simplest. Two-pointer is more elegant and shows BST knowledge. |
| Can we do O(h) space? | Yes, with two BST iterators (forward and reverse inorder). More complex but O(h) space. |
| What if there are duplicates? | Need to handle same-node-pair case. Hash with counts, or ensure pointers differ. |
| Difference from array Two Sum? | Array: hash map O(n). Sorted array: two pointers O(n). BST: inorder gives sorted, so two pointers. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Two Sum (LC #1) | Same idea, different data structure (array + hash map) |
| Two Sum II - Sorted (LC #167) | Exact same two-pointer technique on sorted data |
| Search in BST (LC #700) | BST search is used in the brute force approach |
| BST Iterator (LC #173) | Enables O(h) space two-pointer on BST |
| 3Sum (LC #15) | Extension: fix one element, two-sum on the rest |
