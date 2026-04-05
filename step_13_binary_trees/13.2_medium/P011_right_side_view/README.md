# Right Side View

> **Step 13 - 13.2 Medium Binary Trees** | **LeetCode 199** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the `root` of a binary tree, imagine yourself standing on the **right side** of it. Return the values of the nodes you can see, ordered from top to bottom.

A node is visible from the right side if it is the **rightmost node at its depth level**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, null, 5, null, 4]` | `[1, 3, 4]` | Level 0: 1. Level 1: rightmost is 3. Level 2: rightmost is 4 |
| `[1, null, 3]` | `[1, 3]` | Node 3 is visible at level 1 |
| `[]` | `[]` | Empty tree |
| `[1]` | `[1]` | Only root |

## Constraints

- Number of nodes in tree: `[0, 100]`
- `-100 <= Node.val <= 100`

---

## Approach 1: Brute Force — BFS, Last Node Per Level

**Intuition:** Use a standard level-order BFS. Process all nodes at each depth before moving to the next. The last node processed at a level is the rightmost one visible from the right side — add its value to the result.

**Steps:**
1. If root is null, return empty list.
2. Initialize queue with root.
3. While queue is not empty:
   - Record `level_size = len(queue)`.
   - Dequeue `level_size` nodes. Add children to queue.
   - Append the value of the **last** node dequeued at this level to result.
4. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — queue holds up to n/2 nodes at the widest level |

---

## Approach 2: Optimal — DFS Right-First, First Node at Each Depth

**Intuition:** Visit right child before left child in a pre-order DFS. Maintain a result list indexed by depth. The **first time** we visit any depth, it is always the rightmost node at that level (because we always go right first). Simply extend the result list whenever `depth == result.size()`.

**Steps:**
1. Define recursive `dfs(node, depth)`.
2. If node is null, return.
3. If `depth == len(result)`, append `node.val` (first visit = rightmost).
4. Recurse: `dfs(node.right, depth+1)` then `dfs(node.left, depth+1)`.
5. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(h) — h = tree height (recursion stack) |

---

## Approach 3: Best — Iterative DFS with Explicit Stack

**Intuition:** Same right-first DFS logic as Approach 2, but using an explicit stack instead of recursion. This avoids Python's default recursion limit (~1000) for deep/degenerate trees (e.g., a right-skewed tree of 10^4 nodes).

**Steps:**
1. Push `(root, depth=0)` onto the stack.
2. While stack is not empty: pop `(node, depth)`.
3. If `depth == len(result)`, append `node.val`.
4. Push `(node.left, depth+1)` then `(node.right, depth+1)` (push left first so right is popped first — LIFO).
5. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(h) |

---

## Real-World Use Case

**UI tree rendering / scene graphs:** In a game engine or GUI framework, a scene graph is a tree of visual elements. The "right side view" directly models which elements are visible to the camera: at each depth (z-layer), only the rightmost (front-most) element is visible. Computing this efficiently avoids rendering hidden elements.

## Interview Tips

- Both BFS and DFS solve this in O(n). Mention both approaches — interviewers often ask for the alternative after you present the first.
- The DFS trick ("right-first, record first visit at each depth") is elegant and uses O(h) space vs O(w) for BFS (where w is max width). For balanced trees, h ≈ log n < w ≈ n/2, so DFS is better on space.
- Don't confuse "last node in BFS order" with "rightmost node by value" — it's rightmost by position/index in the level, not by value.
- The iterative DFS in Approach 3 is worth knowing for Python specifically due to the recursion limit.
- Related: LeetCode 637 (Average of Levels), LeetCode 102 (Binary Tree Level Order Traversal) — all use the same BFS level-processing pattern.
