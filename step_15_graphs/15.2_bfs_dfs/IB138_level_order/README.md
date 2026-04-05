# Level Order Traversal

> **Step 15 | 15.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given the root of a binary tree, return the **level order traversal** of its nodes' values — i.e., from left to right, level by level.

Return a list of lists where `result[i]` contains all node values at depth `i`.

## Examples

**Example 1:**
```
Input:  [3, 9, 20, null, null, 15, 7]
        3
       / \
      9  20
        /  \
       15   7

Output: [[3], [9, 20], [15, 7]]
```

**Example 2:**
```
Input:  [1]
Output: [[1]]
```

**Example 3:**
```
Input:  []
Output: []
```

## Constraints

- Number of nodes: `0 <= n <= 2000`
- `-1000 <= Node.val <= 1000`

---

## Approach 1: Brute Force — DFS with Depth Tracking

**Intuition:** Use recursive DFS and pass the current depth as a parameter. Maintain a `result` list where `result[depth]` accumulates node values. This gives the right grouping even though we traverse in DFS order.

**Steps:**
1. Call `dfs(root, depth=0, result=[])`.
2. If `depth == len(result)`, add a new empty list (new level encountered).
3. Append `node.val` to `result[depth]`.
4. Recurse left with `depth+1`, then right with `depth+1`.
5. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(n) — visit every node once |
| Space  | O(n) for result + O(h) for recursion stack |

**Weakness:** Not intuitive — DFS does not naturally produce level-by-level output. The result is only level-ordered because of the depth indexing trick.

---

## Approach 2: Optimal — BFS with Queue Size Snapshot

**Intuition:** BFS naturally processes nodes level by level. At the start of each BFS iteration, the queue contains exactly one level's worth of nodes. Snapshot `len(queue)` to know how many nodes to process before moving to the next level.

**Steps:**
1. Initialize queue with `[root]`.
2. While queue not empty:
   a. `level_size = len(queue)`.
   b. For `i` in `0..level_size`: dequeue a node, add to current level list, enqueue its non-null children.
   c. Append `level` list to result.
3. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — queue holds at most one complete level (widest level = up to n/2 nodes) |

**This is the standard, expected interview answer.**

---

## Approach 3: Best — BFS with Null Sentinel

**Intuition:** An alternative BFS style using `null` (or `None`) as a level-separator marker inside the queue. When we dequeue a `null`, we know the previous batch was a complete level.

**Steps:**
1. Start queue with `[root, null]`.
2. While queue not empty:
   - If front is `null`: current level is complete; save it, clear buffer, push new `null` sentinel if more nodes remain.
   - Else: add node to current level buffer, enqueue children.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

**Same complexity as Approach 2.** This is an alternative implementation style, not a strictly "better" algorithm.

---

## Real-World Use Case

**Web crawling / social network friend suggestions:** BFS level-order is exactly how a crawler explores pages reachable from a seed URL. Level 0 is the seed, level 1 is all pages linked from it, level 2 is all pages linked from those, etc. This is also how LinkedIn's "People you may know" works — 1st degree = direct connections (level 1), 2nd degree = friends of friends (level 2). The level-grouping lets you apply different treatments to different "degrees of separation."

---

## Interview Tips

- BFS (Approach 2) is the canonical answer — always lead with it.
- Know why `level_size = len(queue)` is the key snapshot: if you don't snapshot it, the queue grows as you enqueue children and you lose track of the level boundary.
- DFS approach is a great follow-up to show you can think recursively: "There's also a DFS approach using depth as an index into the result array."
- Common variants: level order bottom-up (reverse result), zigzag level order (alternate left-right), right side view (take last element of each level), average of each level.
- The null-sentinel approach is common in C++ where `nullptr` is natural; in Java/Python, the size-snapshot style is cleaner.
