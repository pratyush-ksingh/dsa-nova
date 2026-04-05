# Maximum Width of Binary Tree

> **Step 13.3** | **Difficulty:** HARD | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the root of a binary tree, return the **maximum width** of the tree.

The **width** of one level is defined as the length between the leftmost and rightmost non-null nodes in the level, **including all null nodes** that exist between them.

The answer is guaranteed to fit in a 32-bit integer.

## Examples

```
        1
       / \
      3   2
     / \   \
    5   3   9
```
Level 0: [1]          → width 1
Level 1: [3, 2]       → width 2
Level 2: [5, 3, _, 9] → width 4 (null between 3 and 9 counts)
**Max width = 4**

```
        1
       / \
      3   2
     /     \
    5       9
   /         \
  6           7
```
Level 3: [6, _, _, _, _, _, _, 7] → width 8
**Max width = 8**

| Input | Output |
|-------|--------|
| `[1,3,2,5,3,null,9]` | `4` |
| `[1,3,2,5,null,null,9,6,null,7]` | `7` |
| `[1]` | `1` |
| `[1,3,null,5,3]` | `2` |

## Constraints

- Number of nodes: `[1, 3000]`
- `-100 <= Node.val <= 100`

---

## Approach 1: Brute Force — BFS with Null Node Padding

**Intuition:** Physically include `null` nodes in the BFS queue to represent empty positions. At each level, strip leading and trailing nulls, then measure the remaining queue size as the width. Children of null nodes are also null, preserving positional structure.

**Problem:** For a tree of height `h`, the last level can have `2^h` slots. A skewed tree of 3000 nodes can have height 3000, making this approach require `2^3000` nodes — completely infeasible.

**Steps:**
1. Enqueue root.
2. At each level: trim leading/trailing nulls, measure remaining size.
3. Enqueue `node.left` and `node.right` (or two nulls if the node is null).
4. Track maximum size across all levels.

| Metric | Value |
|--------|-------|
| Time   | O(2^h) — exponential in tree height |
| Space  | O(2^h) |

> This approach TLEs on large inputs. It demonstrates the problem with null-padding and motivates the index-based solution.

---

## Approach 2: Optimal — BFS with Index Tracking + Normalization

**Intuition:** Instead of storing null nodes, assign each node a **virtual position index** as in a complete binary tree (heap numbering):
- Root = 1
- Left child of node at index `i` = `2i`
- Right child = `2i + 1`

Width of a level = `last_index - first_index + 1`.

**Key issue — overflow:** In a tree with many levels, indices can reach `2^n`. To prevent overflow, **normalize indices per level**: subtract the first index of each level from all indices in that level. Width is invariant under this shift (it's a difference).

**Steps:**
1. BFS queue stores `(node, index)` pairs. Start: `(root, 1)`.
2. At the start of each level, record `first_index` of the level.
3. For each node in the level:
   - Normalize: `idx = idx - first_index`
   - Track `level_first` and `level_last` (normalized indices).
   - Enqueue children: left at `2*(idx + first_index)`, right at `2*(idx + first_index) + 1`.
4. Width = `level_last - level_first + 1`.
5. Track maximum width.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — queue holds at most one level |

---

## Approach 3: Best — BFS with Per-Level 0-Based Normalization

**Intuition:** Same as Approach 2 but normalize at child-enqueue time using 0-based indices within each level. This ensures the index of any node never exceeds the number of nodes at that level — completely eliminating overflow concerns.

When enqueuing children, use the normalized index directly: `left = 2*idx`, `right = 2*idx + 1`. This is simpler and cleaner.

**Steps:**
1. Enqueue `(root, 0)` — 0-indexed.
2. At level start, record `level_first = queue.front().index`.
3. For each node at index `idx`:
   - Normalize: `idx -= level_first`
   - Track `start` (first) and `end` (last) normalized index.
   - Enqueue children: `(node.left, 2*idx)` and `(node.right, 2*idx + 1)`.
4. Width = `end - start + 1`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Real-World Use Case

**Database B-Tree Analysis:** The "width" of a B-tree level tells you how many leaf nodes (data pages) exist at a given depth — critical for estimating I/O cost of range queries. A wide, shallow tree is more I/O-efficient than a narrow, deep one. Database query optimizers compute level widths to estimate page reads.

**Network Topology Layout:** When rendering a hierarchical network diagram, the maximum width across all levels determines the horizontal space needed for the visualization — used in tools like graphviz, Mermaid, and AWS infrastructure diagrams.

## Interview Tips

- The naive null-padding approach is important to mention first — it shows you understand WHY nulls count, then explain why it's infeasible.
- The **index trick** is the core insight: number nodes like a heap array. Width = last_idx - first_idx + 1 at each level.
- **Integer overflow is a real concern**: without normalization, a 32-level tree reaches indices of 2^32, exceeding `int` range. Use `long` / Python's big int. Normalization (per-level subtraction) keeps values bounded.
- Approach 3's cleaner normalization (0-based at child-enqueue time) is preferred in production code.
- Width includes the gaps (null nodes) — this is what makes it different from just counting actual nodes per level.
- Test with: right-skewed tree (width = 1 at all levels), complete binary tree, "zigzag" shaped tree.
- Follow-up: "What if you want the level with maximum width?" — trivial extension: track the level number alongside the maximum.
