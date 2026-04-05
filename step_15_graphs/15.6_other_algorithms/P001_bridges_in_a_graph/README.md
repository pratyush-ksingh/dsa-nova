# Bridges in a Graph (Critical Connections)

> **Step 15 | 15.6** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED
> LeetCode 1192

## Problem Statement

There are `n` servers numbered `0` to `n-1` connected by undirected server-to-server `connections`. A **critical connection** (bridge) is a connection that, if removed, will make some server unable to reach some other server.

Return all critical connections in the network in any order.

More formally: an edge `(u, v)` is a **bridge** if removing it increases the number of connected components in the graph.

## Examples

**Example 1:**
```
Input:  n=4, connections=[[0,1],[1,2],[2,0],[1,3]]

  0 --- 1 --- 3
  |     |
  +--2--+

Output: [[1, 3]]
Explanation: Removing edge [1,3] disconnects node 3 from the rest.
             The triangle 0-1-2 is biconnected (no bridges within it).
```

**Example 2:**
```
Input:  n=2, connections=[[0,1]]
Output: [[0,1]]
Explanation: Only one edge — removing it disconnects the graph.
```

**Example 3 — Path graph (all bridges):**
```
Input:  n=4, connections=[[0,1],[1,2],[2,3]]
Output: [[0,1],[1,2],[2,3]]
```

## Constraints

- `2 <= n <= 10^5`
- `n-1 <= connections.length <= 10^5`
- `0 <= a_i, b_i <= n-1`
- `a_i != b_i`
- No repeated connections.
- The graph is **connected**.

---

## Approach 1: Brute Force — Remove Each Edge, Check Connectivity

**Intuition:** For every edge in the graph, temporarily remove it and check if the graph remains connected using BFS/DFS. If removing the edge disconnects the graph, it's a bridge.

**Steps:**
1. Build adjacency list.
2. For each edge `(u, v)`:
   a. Remove the edge temporarily.
   b. Run BFS/DFS from node 0, count reachable nodes.
   c. If reachable count < n: edge is a bridge.
   d. Restore the edge.
3. Return all bridges.

| Metric | Value |
|--------|-------|
| Time   | O(E * (V + E)) — one BFS per edge |
| Space  | O(V + E) |

**Unusable for large inputs** (E up to 10^5 means 10^10 operations worst case).

---

## Approach 2: Optimal — Tarjan's Bridge-Finding Algorithm

**Intuition:** Tarjan's algorithm finds all bridges in a single DFS pass using two arrays:
- `disc[u]`: discovery time of node u (when was it first visited?).
- `low[u]`: minimum discovery time reachable from the subtree rooted at u via any back edge.

**The Bridge Condition:** Edge `(parent, child)` is a bridge if `low[child] > disc[parent]`.

This means: the child's subtree has no back edge that can "reach around" to the parent or any ancestor. Removing the parent-child edge would cut off the entire subtree.

**Steps:**
1. Initialize `disc[]` with -1 (unvisited), set `timer = 0`.
2. DFS from each unvisited node (pass `parent = -1`).
3. On visiting node `u`:
   - Set `disc[u] = low[u] = timer++`.
   - For each neighbor `v`:
     * If unvisited: recurse into `v`. After return: `low[u] = min(low[u], low[v])`. Check bridge: if `low[v] > disc[u]` -> bridge!
     * If `v != parent` (back edge): `low[u] = min(low[u], disc[v])`.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) — single DFS pass |
| Space  | O(V) — disc/low arrays + O(V) call stack |

**This is the standard algorithm for bridges.**

---

## Approach 3: Best — Tarjan's Iterative (Stack-Safe)

**Intuition:** Same Tarjan's algorithm but implemented iteratively using an explicit stack that tracks `(node, parent, current_neighbor_index)`. This avoids Python/Java recursion depth limits for large graphs.

**Steps:**
1. For each unvisited node: push `(node, -1, 0)` onto stack; set disc/low.
2. While stack not empty:
   - Peek top `(node, parent, idx)`.
   - If there are more neighbors to process (idx < len(adj[node])):
     * Advance idx. Get next neighbor.
     * If unvisited: push neighbor; set disc/low.
     * Else if not parent: update `low[node] = min(low[node], disc[neighbor])`.
   - Else (all neighbors done): pop. Update parent's low. Check bridge.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) — explicit stack |

---

## Real-World Use Case

**Network reliability and infrastructure planning:** Telecommunications companies use bridge-finding algorithms to identify **single points of failure** in fiber networks. A bridge in the network graph represents a cable whose failure would partition the network into disconnected regions. By identifying bridges, engineers know which cables need redundant backups. Similarly, in software microservices graphs, a bridge identifies a service whose failure would isolate parts of the system — critical for site reliability engineering (SRE) planning.

---

## Interview Tips

- Always clarify: "bridge" = critical edge (this problem). "Articulation point" = critical vertex (a related but different problem).
- The key insight of Tarjan's: `disc` tracks when, `low` tracks how far back we can reach. The bridge condition `low[child] > disc[parent]` is the single most important line.
- Multi-edges (parallel edges between u and v) complicate bridge detection: if there are 2 edges between u and v, neither is a bridge. Handle by passing edge index instead of parent node.
- For interviews: sketch the DFS tree, label disc/low values, and show how low[child] > disc[parent] identifies the bridge.
- Related problems: Articulation Points (LeetCode 1568, GFG), Strongly Connected Components (Tarjan's SCC, Kosaraju's).
- The iterative version is preferred in production code but DFS recursive version is much easier to explain and code correctly in an interview.
