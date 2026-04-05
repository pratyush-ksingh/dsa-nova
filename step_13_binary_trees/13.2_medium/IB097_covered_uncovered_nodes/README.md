# Covered and Uncovered Nodes

> **Batch 4 of 12** | **Topic:** Binary Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a binary tree, find the **absolute difference** between the sum of all **uncovered nodes** (boundary nodes) and the sum of all **covered nodes** (non-boundary/interior nodes).

**Uncovered nodes** are the nodes visible from outside -- the left boundary, right boundary, and leaf nodes. **Covered nodes** are everything else (interior nodes that are not on any boundary path and are not leaves).

Formally: `|sum_uncovered - sum_covered|`

### Examples

**Example 1:**
```
         8
        / \
       3   10
      / \    \
     1   6    14
        / \   /
       4   7 13
```
- Total sum = 8+3+10+1+6+14+4+7+13 = 66
- Uncovered (boundary): Left boundary path: 8,3,1. Right boundary path: 8,10,14,13. Leaves: 1,4,7,13.
- Unique uncovered nodes: {8, 3, 1, 10, 14, 13, 4, 7} -> sum = 60
- Covered nodes: {6} -> sum = 6
- Difference: |60 - 6| = 54

**Example 2:**
```
    1
   / \
  2   3
```
All nodes are uncovered (boundary/leaves). Covered sum = 0. Answer = |6 - 0| = 6.

### Real-Life Analogy
Think of a building viewed from outside. The "uncovered" rooms are those with windows facing outside (perimeter rooms). The "covered" rooms are interior rooms with no exterior wall. You want the difference in total floor area between perimeter rooms and interior rooms.

### 3 Key Observations
1. **"Aha!" -- Uncovered = total - covered:** Instead of separately computing uncovered, note that `sum_uncovered = total_sum - sum_covered`. So the answer is `|total - 2 * covered|`.
2. **"Aha!" -- Left boundary goes left-first, right boundary goes right-first:** The left boundary follows left children (falling back to right if no left exists). Mirror for right boundary.
3. **"Aha!" -- Avoid double-counting:** The root, leftmost leaf, and rightmost leaf appear in multiple boundary definitions. Use a visited set or compute carefully.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | HashSet + DFS | Mark uncovered nodes, sum rest | Clear separation |
| Optimal | Single DFS | Compute total and uncovered in one pass | Less overhead |
| Best | Boundary walk | Directly compute uncovered sum, derive covered | Minimal work |

---

## APPROACH LADDER

### Brute Force: Mark Uncovered with HashSet
**Intuition:** Walk the left boundary, right boundary, and leaves. Mark all uncovered nodes. Sum everything not marked.

**Steps:**
1. Compute total sum of all nodes via DFS
2. Walk left boundary: from root, go left (or right if no left) until leaf
3. Walk right boundary: from root, go right (or left if no right) until leaf
4. Walk all leaves via DFS
5. Union all uncovered nodes in a set, compute their sum
6. covered_sum = total_sum - uncovered_sum
7. Return |uncovered_sum - covered_sum|

**BUD Analysis:**
- **B**ottleneck: Multiple traversals (boundary walks + full DFS)
- **U**nnecessary: HashSet overhead for marking -- we could compute sums directly
- **D**uplicate: Three separate walks could be merged

**Dry-Run Trace (Example 1):**
```
Total sum = 66
Left boundary: 8 -> 3 -> 1 (leaf, stop)
Right boundary: 8 -> 10 -> 14 -> 13 (leaf, stop)
Leaves: 1, 4, 7, 13
Uncovered set: {8, 3, 1, 10, 14, 13, 4, 7}, sum = 60
Covered sum = 66 - 60 = 6
Answer = |60 - 6| = 54
```

### Optimal: Compute Uncovered Sum Directly
**Intuition:** Walk left and right boundaries summing values. Add leaf sums. Avoid set by careful traversal.

**Steps:**
1. Compute total_sum with simple DFS
2. Compute uncovered_sum:
   - Walk left boundary (exclude leaf at the end)
   - Walk right boundary (exclude leaf at the end)
   - Add sum of all leaves
3. covered_sum = total_sum - uncovered_sum
4. Return |uncovered_sum - covered_sum|

### Best: Single-Pass with Boundary Flags
**Intuition:** Use `|total - 2 * covered|` = `|2 * uncovered - total|`. Compute uncovered sum by walking left boundary down, right boundary down, and leaves in one organized pass.

**Steps:**
1. uncovered_sum = sum of left boundary (root down-left to leaf, exclusive of leaf)
   + sum of right boundary (root down-right to leaf, exclusive of leaf)
   + sum of all leaves
   - root (counted twice in left and right boundary)
2. Answer = |total_sum - 2 * (total_sum - uncovered_sum)|

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N) | O(N) | Full traversal + HashSet for all nodes |
| Optimal | O(N) | O(H) | One full DFS + boundary walks (H = height) |
| Best | O(N) | O(H) | Same time, cleaner constant factors |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Single node | 0 | Root is uncovered, covered = 0, diff = |val - 0| = val... but root alone means uncovered=val, covered=0 |
| Only left children (skewed) | 0 | All nodes are on left boundary or are leaves -- all uncovered |
| Complete binary tree | Interior nodes are covered | Only boundary + leaves are uncovered |
| Two nodes (root + one child) | |parent - 0| if child is leaf | Both are uncovered |

**Common Mistakes:**
- Double-counting the root in left and right boundary
- Double-counting corner leaves (leftmost/rightmost leaf)
- Forgetting that leaf nodes on the boundary are already counted as leaves

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests boundary traversal understanding, careful bookkeeping, and set-based deduplication.

**What they want to see:**
- Clear definition of covered vs uncovered
- Careful boundary walking logic
- Handling of edge cases (skewed trees, single node)

**Follow-ups to prepare for:**
- Boundary Traversal of Binary Tree
- Left/Right View of Binary Tree
- Sum of all leaf nodes

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Boundary Traversal | Same boundary-walking logic |
| Left/Right View | Boundary concepts overlap |
| Sum of Leaf Nodes | Leaves are part of uncovered set |
| Vertical Sum | Different grouping strategy for tree nodes |
