# Eventual Safe States

> **Step 15 | 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED
> LeetCode 802

## Problem Statement

There is a directed graph with `n` nodes labeled `0` to `n-1`, where `graph[i]` is the list of nodes that node `i` has edges to.

A node is a **terminal node** if it has no outgoing edges.

A node is a **safe node** if every possible path starting from that node leads to a terminal node (or another safe node). In other words, a node is safe if it does **not** lie on a cycle and does not lead to any cycle.

Return a list of all safe nodes in **ascending order**.

## Examples

**Example 1:**
```
Input:  graph = [[1,2],[2,3],[5],[0],[5],[],[]]
                 0      1      2    3    4    5  6

Output: [2, 4, 5, 6]
Explanation:
  - Nodes 5, 6: terminal nodes (no outgoing edges). Safe.
  - Node 2: only leads to 5 (terminal). Safe.
  - Node 4: only leads to 5 (terminal). Safe.
  - Node 3: leads to 0 -> cycle (0->1->3->0). Unsafe.
  - Node 1: leads to 3 which is unsafe. Unsafe.
  - Node 0: part of cycle 0->1->2->3->0 (via 3->0). Unsafe.
```

**Example 2:**
```
Input:  graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
Output: [4]
```

## Constraints

- `n == graph.length`
- `1 <= n <= 10^4`
- `0 <= graph[i].length <= n`
- `0 <= graph[i][j] <= n - 1`
- `graph[i]` is sorted in strictly increasing order.
- The graph may contain self-loops.
- The number of edges may be large.

---

## Approach 1: Brute Force — Per-Node DFS with Memoization

**Intuition:** For each node, ask: "Are all paths from me cycle-free?" Use DFS with a per-call path set to detect cycles. Memoize results so each node is only fully analyzed once.

**Steps:**
1. `memo[node]`: -1=unknown, 0=unsafe, 1=safe.
2. For each node: call `isSafe(node, path_set)`.
   - If node in path_set: cycle detected -> unsafe (return false).
   - If memo[node] known: return it.
   - Add node to path_set. For each neighbor: if neighbor is unsafe -> current unsafe.
   - Remove from path_set. Set memo[node] = 1 (safe).
3. Collect all safe nodes.

| Metric | Value |
|--------|-------|
| Time   | O(V * (V + E)) worst case without careful memoization |
| Space  | O(V) |

---

## Approach 2: Optimal — DFS 3-Coloring

**Intuition:** Same 3-color DFS used for cycle detection, but repurposed: nodes that end up BLACK (color=2) are safe; nodes that remain GRAY or WHITE after DFS are unsafe.

**Key insight:** A node is safe if and only if all its neighbors are safe. If any neighbor leads to a cycle (stays GRAY or causes a GRAY encounter), the current node is unsafe.

**Steps:**
1. Colors: 0=WHITE(unvisited), 1=GRAY(in DFS stack), 2=BLACK(safe).
2. For each node, call DFS:
   - If GRAY: return false (cycle). If BLACK: return true.
   - Mark GRAY. Recurse into all neighbors.
   - If any neighbor returns false: this node is also unsafe (stays GRAY, return false).
   - Mark BLACK (safe). Return true.
3. Collect all nodes that DFS returned true for.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) — each node/edge visited once |
| Space  | O(V) — color array + O(V) recursion stack |

**This is the standard DFS-based interview answer.**

---

## Approach 3: Best — Reverse Graph + Kahn's Algorithm

**Intuition:** A node is safe if it cannot reach any cycle. Equivalently: in the reversed graph, a node is safe if it can be reached from a terminal node. This transforms the problem into: "find all nodes reachable from terminals in the reversed graph" — which is BFS/Kahn's on the reversed graph.

**Steps:**
1. Reverse all edges: build `reverse_graph`.
2. Compute `out_degree[v]` in the **original** graph.
3. Enqueue all nodes with `out_degree = 0` (terminal nodes) into BFS queue.
4. BFS: dequeue node, mark safe. For each predecessor in reverse graph:
   - Decrement their out_degree.
   - If out_degree becomes 0: they're now fully "connected to safe territory" -> enqueue.
5. Return all safe nodes sorted.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) — reverse graph storage + queue |

**Advantage over DFS:** Fully iterative, no recursion limit issues, and cleaner logic.

---

## Real-World Use Case

**Garbage collection reachability:** In a programming language runtime (JVM, CPython), the garbage collector needs to find "safe" memory objects — those that eventually reach a root (terminal) without being part of a reference cycle. Objects in reference cycles that don't connect to roots are garbage. The "eventual safe states" problem is exactly the GC reachability problem: find all nodes from which every path leads to a root (terminal node). The reverse-graph BFS approach mirrors what modern incremental GCs do.

---

## Interview Tips

- The key insight: a safe node is one with NO path to any cycle. Unsafe = on a cycle OR can reach a cycle.
- DFS 3-coloring (Approach 2) is the most elegant solution — explain the color semantics clearly.
- Reverse graph + Kahn's (Approach 3) is the best to know for interviews where "no recursion" is requested.
- The problem statement says "every possible path leads to a terminal" — this is equivalent to "the node is not part of and cannot reach any cycle."
- Edge case: a node pointing only to itself (self-loop) is always unsafe.
- A node pointing only to terminal nodes is always safe.
- This problem is a variant of "find nodes not involved in or leading to any cycle" — a useful mental model.
