# Articulation Points

> **Step 15.6** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

Given an undirected graph with `V` vertices (0-indexed) and `E` edges, find all **articulation points** (also called cut vertices). An articulation point is any vertex whose removal — along with all its incident edges — **disconnects the graph** (increases the number of connected components).

**Input:** Number of vertices `n`, adjacency list `adj`.
**Output:** Sorted list of vertex indices that are articulation points.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| n=5, edges=[(0,1),(1,2),(2,3),(3,4)] | [1,2,3] | Chain graph: removing any middle vertex splits it |
| n=5, edges=[(0,1),(0,2),(1,2),(1,3),(3,4)] | [1,3] | 0-1-2 form a triangle (so 0 and 2 are safe); 1 connects triangle to chain; 3 connects to leaf 4 |
| n=3, edges=[(0,1),(1,2),(0,2)] | [] | Triangle: no vertex is an AP |

## Constraints

- 1 <= V <= 10^5
- 0 <= E <= 2*10^5
- Graph may be disconnected
- No self-loops or parallel edges

---

## Approach 1: Brute Force

**Intuition:** Try removing each vertex and check whether the remaining graph is still connected using DFS/BFS. If removing vertex `v` disconnects the graph, then `v` is an articulation point.

**Steps:**
1. For each vertex `v` from 0 to n-1:
   a. Pick any vertex other than `v` as DFS start.
   b. Run DFS/BFS, skipping `v` and its edges.
   c. Count reachable vertices. If fewer than `n-1`, `v` is an AP.
2. Collect and return all APs.

| Metric | Value |
|--------|-------|
| Time   | O(V * (V + E)) |
| Space  | O(V + E) |

---

## Approach 2: Optimal — Tarjan's Algorithm (Recursive)

**Intuition:** A single DFS pass can identify all APs using two arrays:
- `disc[u]`: discovery time when `u` was first visited.
- `low[u]`: the minimum discovery time reachable from the subtree rooted at `u` (via tree edges or one back edge).

A vertex `u` is an AP if:
1. **Root of DFS tree** and has **>= 2 children** in the DFS tree, OR
2. **Non-root** and has a child `v` such that `low[v] >= disc[u]` — meaning no node in `v`'s subtree can reach an ancestor of `u` directly.

**Steps:**
1. Initialize `disc`, `low`, `visited`, `isAP` arrays and a global timer.
2. Run DFS from each unvisited vertex (handles disconnected graphs).
3. In DFS for vertex `u` with parent `p`:
   - Set `disc[u] = low[u] = timer++`, mark visited.
   - For each neighbor `v`:
     - If unvisited: recurse. After return, `low[u] = min(low[u], low[v])`. Check AP conditions.
     - If visited and `v != p`: back edge — `low[u] = min(low[u], disc[v])`.
4. Return all vertices where `isAP[u]` is true.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) |

---

## Approach 3: Best — Tarjan's Algorithm (Iterative)

**Intuition:** Identical to Approach 2 in logic and complexity, but implemented iteratively using an explicit stack to avoid Python/Java stack overflow on graphs with V up to 10^5 (which would overflow the default recursion limit).

**Steps:**
1. Replace recursive DFS with an explicit stack of frames: `[node, parent, neighbor_index, child_count]`.
2. On each iteration: if there are unprocessed neighbors, process the next one (push child or update back-edge low).
3. When all neighbors are processed (pop frame), update the parent's `low` value and check AP conditions on the parent.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) |

---

## Real-World Use Case

**Network reliability / Single points of failure:** Internet routers and network links can be modeled as graphs. Articulation points represent critical routers — if one fails, parts of the network become unreachable. ISPs use AP algorithms to identify infrastructure that needs redundancy (duplicate links/routers). Similarly, in power grids, APs are substations whose failure causes blackouts in isolated regions.

Other applications:
- **Social network analysis:** Identifying key individuals (connectors) whose removal fragments a community.
- **Supply chain:** Warehouses or hubs that, if closed, disconnect supplier from customer.
- **Biological networks:** Proteins or genes whose removal disrupts the interaction network.

---

## Interview Tips

- Always clarify whether the graph is directed or undirected. APs are defined for undirected graphs; for directed graphs the analogous concept is "strongly connected components" via Kosaraju's or Tarjan's SCC algorithm.
- The root vs. non-root AP conditions are easy to confuse. Root: `children > 1`. Non-root: `low[child] >= disc[node]` (not `>`).
- Back edges update `low` only when `v != parent`. In a multigraph (parallel edges), track parent edge index rather than parent vertex to correctly handle it.
- Tarjan's bridge-finding algorithm is almost identical: change the AP condition to `low[v] > disc[u]` (strict `>`) and you find bridges (edges whose removal disconnects the graph) instead of vertices.
- Be prepared to also implement bridge finding — interviewers often ask both together.
- The iterative version is safer for large inputs; always mention the recursion-limit concern in production code.
