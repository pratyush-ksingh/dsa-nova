# Detect Cycle in Directed Graph using DFS

> **Step 15 | 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED
> GFG / Striver Graph Series

## Problem Statement

Given a directed graph with `V` vertices and `E` edges represented as an adjacency list, determine if the graph contains a **cycle**.

A cycle in a directed graph is a path that starts and ends at the same node, following edge directions.

**Important:** An undirected graph cycle check is different — in an undirected graph, you track a single `visited` array. In a **directed** graph, you need to distinguish between nodes in the current DFS path and nodes that were visited in a previous DFS path (which are safe).

## Examples

**Example 1 — Has cycle:**
```
Input:  V=4, adj = [[1],[2],[3],[1]]

  0 -> 1 -> 2 -> 3
       ^_________/

Output: true  (cycle: 1 -> 2 -> 3 -> 1)
```

**Example 2 — No cycle (DAG):**
```
Input:  V=4, adj = [[1,2],[2],[3],[]]

  0 -> 1 -> 2 -> 3
  |_________|

Output: false  (valid DAG)
```

**Example 3 — Self-loop:**
```
Input:  V=1, adj = [[0]]
Output: true  (node 0 points to itself)
```

## Constraints

- `1 <= V <= 10^5`
- `0 <= E <= 10^5`
- Graph may be disconnected.
- May have self-loops and multiple edges.

---

## Approach 1: Brute Force — Independent DFS from Each Node

**Intuition:** For each node, launch a fresh DFS and track the current path. If we revisit any node in the current path, we found a back edge (cycle). This is correct but wasteful: nodes are re-explored from each starting point.

**Steps:**
1. For each node `v` in `0..V-1`:
   - Maintain a `path` set (current DFS stack) and a `visited` set (fully explored).
   - If we reach a node in `path` -> cycle found.
   - If we reach a node in `visited` -> skip (no cycle via this node).
2. Return true if any starting node finds a cycle.

| Metric | Value |
|--------|-------|
| Time   | O(V * (V + E)) — restarts DFS for every node |
| Space  | O(V) |

---

## Approach 2: Optimal — DFS with 3-Color Marking

**Intuition:** Use 3 states for each node:
- **WHITE (0):** Not yet visited.
- **GRAY (1):** Currently in the DFS recursion stack (being explored).
- **BLACK (2):** Fully processed — no cycle exists through this node.

A back edge (edge to a GRAY node) indicates a cycle. If we reach a BLACK node, it's already been confirmed cycle-free and we skip it.

**Steps:**
1. Initialize all nodes as WHITE.
2. For each WHITE node, start DFS.
3. Mark current node GRAY. Explore neighbors:
   - WHITE neighbor: recurse.
   - GRAY neighbor: back edge -> cycle!
   - BLACK neighbor: skip.
4. After all neighbors processed: mark node BLACK.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) — each node/edge visited exactly once |
| Space  | O(V) — vis array + O(V) recursion stack |

**This is the standard interview answer for directed cycle detection.**

---

## Approach 3: Best — Kahn's Algorithm (Topological Sort)

**Intuition:** A directed graph has a cycle if and only if it does NOT have a valid topological ordering. Kahn's algorithm computes topological sort via BFS (in-degree reduction). If fewer than V nodes are processed, some nodes were stuck in a cycle and never reached in-degree 0.

**Steps:**
1. Compute `in_degree[v]` for all nodes.
2. Enqueue all nodes with `in_degree = 0`.
3. BFS: dequeue a node, reduce in-degree of its neighbors. If any hit 0, enqueue.
4. Count processed nodes. If `count < V` -> cycle exists.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) — in-degree array + queue |

**Bonus:** Kahn's also gives the actual topological order if no cycle. DFS approach only detects the cycle but doesn't give topological order directly.

---

## Real-World Use Case

**Deadlock detection in operating systems:** When processes hold locks and wait for other locks, a directed graph models "process A is waiting for resource held by process B." A cycle in this graph = deadlock. OS deadlock detection algorithms (like Banker's algorithm) are fundamentally DFS-based directed cycle detection. Similarly, build systems (Maven, npm, Gradle) detect circular dependencies using the same technique.

---

## Interview Tips

- Stress the critical difference: directed vs. undirected cycle detection. In undirected graphs, a single `visited` array suffices (check `neighbor != parent`). In directed graphs, you NEED the recursion stack (gray state) to distinguish back edges from cross edges.
- The 3-color analogy (traffic light: white/gray/black) is memorable and correct — use it.
- Kahn's is a great alternative to demonstrate breadth of knowledge: "You can also detect cycles as a byproduct of topological sort."
- Self-loops: in Approach 2, when you mark node GRAY and then find the same node as a neighbor -> GRAY node reached -> cycle. Correctly handles self-loops.
- For very deep graphs in Python, Approach 3 (Kahn's) avoids recursion limit issues entirely.
