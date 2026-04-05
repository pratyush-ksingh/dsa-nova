# Bottom View of Binary Tree

> **Step 13.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

The **bottom view** of a binary tree consists of the nodes visible when the tree is viewed from the bottom. For each **horizontal distance (column)**, we report the **last node** encountered during a top-down, left-to-right traversal — i.e., the deepest node at that column. If two nodes are at the same depth at the same column, the rightmost one is chosen.

**Horizontal Distance (HD) rules:**
- Root: HD = 0
- Left child: HD = parent's HD - 1
- Right child: HD = parent's HD + 1

Return the bottom-view nodes in left-to-right order (sorted by HD).

## Examples

```
        1
       / \
      2   3
     / \ / \
    4  5 6  7
```

| Input (tree) | Output | Explanation |
|---|---|---|
| Above tree | `[4, 2, 6, 3, 7]` | HD=-2→4, HD=-1→2, HD=0→6 (5 and 6 both at depth 2; 6 is processed last in BFS), HD=+1→3, HD=+2→7 |

```
       20
      /  \
     8    22
    / \
   5   3
      / \
     10  14
```

| Input (tree) | Output | Explanation |
|---|---|---|
| Above tree | `[5, 10, 3, 14, 22]` | 10 is deepest at HD=-1, 14 at HD=+1 |

## Constraints

- Number of nodes: `1 <= n <= 10^5`
- Node values: `-10^5 <= val <= 10^5`

---

## Approach 1: Brute Force — DFS with Depth Tracking

**Intuition:** Use DFS to visit every node and record `(depth, value)` for each horizontal distance. The node with the greatest depth at each HD is the bottom-view node. If depths are tied, the node processed later (right side in DFS) wins because we use `>=` comparison.

**Steps:**
1. Maintain a map: `hd -> (max_depth, value)`.
2. DFS with parameters `(node, hd, depth)`:
   - If `depth >= map[hd].depth`: update map.
   - Recurse left with `hd-1, depth+1` and right with `hd+1, depth+1`.
3. Sort map by HD and extract values.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) — DFS O(n) + sort O(k log k) where k = number of distinct HDs |
| Space  | O(n) for recursion stack + map |

---

## Approach 2: Optimal — BFS with Map (Overwrite Strategy)

**Intuition:** BFS processes nodes level by level, left to right. For any given HD, the last node encountered during BFS is the one at the **greatest depth** (or rightmost at the same depth). Simply overwriting `hd_map[hd] = node.val` at every BFS step automatically keeps the correct bottom-view node.

**Steps:**
1. Use a queue storing `(node, hd)` pairs; start with `(root, 0)`.
2. While queue is not empty:
   - Dequeue `(node, hd)`, set `hd_map[hd] = node.val`.
   - Enqueue left child with `hd-1` and right child with `hd+1`.
3. Sort `hd_map` by key (HD) and return values.

Use a `TreeMap` (Java) or `sorted()` (Python) for the final ordering.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) — BFS O(n) + sort O(k log k) |
| Space  | O(n) |

---

## Approach 3: Best — BFS Level-by-Level (Explicit Level Processing)

**Intuition:** Identical to Approach 2 but processes one complete level at a time using level-size tracking. This makes the "deeper nodes overwrite shallower ones" behavior explicit and is easier to explain in an interview.

**Steps:**
1. Enqueue `(root, 0)`.
2. While queue not empty:
   - Snapshot current level size.
   - Process exactly that many nodes, updating `hd_map[hd] = val` for each.
   - Enqueue children for the next level.
3. Sort by HD and return values.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Real-World Use Case

**Rendering a Hierarchical Structure (Shadow Projection):** If you think of a binary tree as a 3D structure and shine a light from directly above, the "shadow" cast on the ground is exactly the bottom view — only the lowest (deepest) node in each column is visible from below. This concept applies to rendering dependency trees in build systems, where you want to show only the leaf-most artifact at each "position" in the dependency graph.

**File System Depth Visualization:** In a folder tree, the bottom view shows the deepest folder at each horizontal "column offset", useful for generating visual project structure summaries.

## Interview Tips

- Horizontal Distance is the core concept: define it clearly before coding (root=0, left-1, right+1).
- Bottom view differs from **top view** only in which write wins: top view keeps the **first** node at each HD (shallowest), bottom view keeps the **last** (deepest). In BFS, "first write" vs "always overwrite" captures this difference.
- Bottom view also differs from **left/right view** — those are per-level, not per-column.
- BFS is cleaner to reason about than DFS for this problem because BFS naturally processes deeper nodes later.
- With DFS, you must use `>=` (not `>`) for depth comparison to handle the "rightmost node wins on ties" rule correctly.
- `TreeMap` in Java and `sorted(dict.items())` in Python both provide the HD ordering needed for the output.
- Follow-up: "What about the Top View?" — same algorithm, but only write to the map if the HD key doesn't exist yet (first write wins).
