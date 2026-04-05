# Bipartite Graph Check using DFS

> **Step 15 | 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED
> LeetCode 785

## Problem Statement

Given an undirected graph represented as an adjacency list `graph` where `graph[i]` is the list of nodes adjacent to node `i`, determine if the graph is **bipartite**.

A graph is bipartite if its nodes can be divided into two independent sets `A` and `B` such that every edge connects a node in `A` to a node in `B` (no edge within the same set).

Equivalently: the graph is bipartite if and only if it contains **no odd-length cycles** and is **2-colorable**.

## Examples

**Example 1 — Bipartite (even cycle):**
```
Input:  graph = [[1,3],[0,2],[1,3],[0,2]]

  0 - 1
  |   |
  3 - 2

Output: true
Explanation: Set A = {0, 2}, Set B = {1, 3}. No edge within a set.
```

**Example 2 — Not Bipartite (odd cycle):**
```
Input:  graph = [[1,2],[0,2],[0,1]]

  0
 / \
1 - 2

Output: false
Explanation: Triangle = odd cycle. Cannot 2-color without conflict.
```

## Constraints

- `1 <= n <= 100`
- `0 <= graph[u].length < n`
- `graph[u]` does not contain `u` (no self-loops).
- All edges are undirected (if `v` is in `graph[u]`, then `u` is in `graph[v]`).
- Graph may be **disconnected**.

---

## Approach 1: Brute Force — Try All 2^n Colorings

**Intuition:** There are only 2^n possible ways to assign one of two colors to n nodes. Check each assignment: if no edge has both endpoints the same color, it's a valid 2-coloring -> bipartite.

**Steps:**
1. For each bitmask from 0 to 2^n - 1: bit i = color of node i.
2. For every edge (u, v): check if color[u] == color[v]. If so, invalid.
3. If any mask passes all edge checks, return true.
4. If no mask works, return false.

| Metric | Value |
|--------|-------|
| Time   | O(2^n * (V+E)) — exponential, unusable for n > 20 |
| Space  | O(1) extra (beyond input) |

---

## Approach 2: Optimal — DFS 2-Coloring

**Intuition:** Assign colors greedily during DFS. Start with color 0 for a source node, then assign color 1 to all its neighbors, color 0 to their neighbors, etc. If we ever assign a color to a node that already has the opposite color, the graph is NOT bipartite.

**Steps:**
1. Initialize `color[]` array with -1 (unvisited).
2. For each unvisited node (handles disconnected graphs), call DFS with color 0.
3. In DFS: set `color[node] = c`. For each neighbor:
   - If unvisited: recurse with `1 - c`.
   - If visited with same color `c`: return false.
4. If all components pass, return true.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) — each node and edge visited once |
| Space  | O(V) — color array + O(h) recursion stack |

---

## Approach 3: Best — BFS 2-Coloring (Iterative)

**Intuition:** Same 2-coloring logic but using BFS. BFS is preferred because it avoids deep recursion (no stack overflow risk) and is slightly easier to reason about in interviews.

**Steps:**
1. For each unvisited node: enqueue it with color 0.
2. BFS: dequeue node, check each neighbor:
   - If unvisited: assign opposite color, enqueue.
   - If same color as current node: not bipartite, return false.
3. If all nodes processed without conflict: bipartite.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) — color array + queue |

**This is the standard interview answer** (BFS coloring).

---

## Real-World Use Case

**Stable matching / recommendation systems:** In a job-applicant matching system, the graph has applicants on one side and jobs on the other — a classic bipartite structure. Checking bipartiteness is used to verify that a relationship graph (e.g., "person liked product") has no "conflicts" (a person liking another person in a strictly professional network). Bipartite graphs also arise in scheduling (tasks vs. machines), assignment problems, and movie recommendation (users vs. movies).

---

## Interview Tips

- A graph is bipartite iff it has no odd-length cycles. Mention this upfront.
- A **tree** is always bipartite (no cycles at all).
- For disconnected graphs, you MUST start BFS/DFS from every unvisited component — easy to miss.
- BFS is safer than DFS in interviews: no recursion limit, easier to trace.
- Common follow-ups: "Can you do it with Union-Find?" (yes, using 2-coloring in DSU), "Is a graph with only even cycles bipartite?" (yes).
- LeetCode 886 (Possible Bipartition) is the same problem in disguise with a different story.
