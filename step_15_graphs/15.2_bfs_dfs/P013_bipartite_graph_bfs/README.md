# Is Graph Bipartite?

> **LeetCode 785** | **Step 15 — Graphs BFS/DFS** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

There is an **undirected** graph with `n` nodes, where each node is numbered from `0` to `n-1`. You are given a 2D array `graph`, where `graph[u]` is an array of nodes that node `u` is adjacent to.

Determine if the graph is **bipartite**: can its vertices be split into two independent sets `A` and `B` such that every edge connects a vertex in `A` to one in `B`?

Equivalently, a graph is bipartite if and only if it is **2-colorable** — can be colored with two colors such that no two adjacent vertices share the same color. A graph with no odd-length cycles is bipartite.

---

## Examples

| Input (adjacency list) | Output | Explanation |
|------------------------|--------|-------------|
| `[[1,3],[0,2],[1,3],[0,2]]` | `true` | Even 4-cycle: color 0→0, 1→1, 2→0, 3→1 |
| `[[1,2],[0,2],[0,1]]` | `false` | Triangle (3-cycle): odd cycle, cannot 2-color |
| `[[],[]]` | `true` | Two isolated nodes, trivially bipartite |

**Visual:**
```
4-cycle:              Triangle:
0 - 1                 0 - 1
|   |                  \ /
3 - 2                   2
Color: 0=A,1=B,2=A,3=B  No valid 2-coloring exists
```

---

## Constraints

- `n == graph.length`
- `1 <= n <= 100`
- `0 <= graph[u].length < n`
- `0 <= graph[u][i] <= n - 1`
- `graph[u]` does not contain `u` (no self-loops)
- All edges are undirected: if `v` is in `graph[u]`, then `u` is in `graph[v]`
- The graph may not be connected

---

## Approach 1: Brute Force — Try All 2^n Colorings

**Intuition:** By definition, a graph is bipartite if ANY valid 2-coloring exists. Enumerate all `2^n` possible assignments (bit `i` of the mask gives the color of vertex `i`). For each assignment, verify that every edge has endpoints of different colors.

Purely theoretical — demonstrates the definition directly but is wildly impractical.

**Steps:**
1. Iterate `mask` from `0` to `2^n - 1`.
2. For each mask, check every edge `(u, v)`: if `color[u] == color[v]`, this coloring is invalid.
3. If any mask passes all edges, return `true`.
4. If no mask works, return `false`.

| Metric | Value |
|--------|-------|
| Time   | O(2^n * (V + E)) |
| Space  | O(1) extra |

---

## Approach 2: Optimal — BFS 2-Coloring

**Intuition:** Attempt to 2-color the graph greedily via BFS. Start with any vertex, assign it color 0, then enqueue it. Process the queue: for each vertex, assign all uncolored neighbors the opposite color and enqueue them. If a neighbor already has the same color as the current vertex, we have an odd cycle — not bipartite.

Handle disconnected graphs by restarting BFS from each uncolored vertex.

**Steps:**
1. Initialize `color[v] = -1` for all vertices (uncolored).
2. For each uncolored vertex `start`:
   a. Assign `color[start] = 0`, enqueue it.
   b. BFS: dequeue `u`. For each neighbor `v`:
      - If uncolored: `color[v] = 1 - color[u]`, enqueue.
      - If `color[v] == color[u]`: return `false`.
3. Return `true` if all components are successfully 2-colored.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) for color array + BFS queue |

---

## Approach 3: Best — DFS 2-Coloring

**Intuition:** Same 2-coloring logic as BFS but implemented with DFS. Assign color `c` to the current vertex. Recurse with color `1-c` for uncolored neighbors. If a colored neighbor has color `c` (same as current), return `false`.

DFS is slightly more concise to code. BFS is preferable for very deep graphs to avoid Python/JVM recursion limits.

**Steps:**
1. Initialize `color[v] = -1`.
2. For each uncolored vertex, call `dfs(v, 0)`.
3. `dfs(u, c)`: set `color[u] = c`. For each neighbor `v`:
   - If uncolored: recurse `dfs(v, 1-c)`.
   - If `color[v] == c`: return `false`.
4. Return `true` if all components colored successfully.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V) for color array + recursion stack |

---

## Real-World Use Case

**Conflict scheduling / exam timetabling:** Students taking multiple exams are modeled as a graph where two students sharing an exam are connected. The graph is bipartite if we can schedule all exams in two slots without conflicts. Two-coloring directly gives the slot assignments.

**Recommendation systems / collaborative filtering:** User-item interaction graphs are inherently bipartite (users form one set, items the other). Checking bipartiteness is a first-pass validation that the graph hasn't been constructed incorrectly (e.g., user-user or item-item edges accidentally included).

---

## Interview Tips

- Bipartite is equivalent to "2-colorable" is equivalent to "no odd-length cycles" — know all three formulations and state them at the start.
- Mention handling disconnected graphs explicitly: the outer loop over all uncolored vertices is essential.
- The color array initialization to `-1` (uncolored) rather than `0/1` avoids confusion about which vertices have been processed.
- Common follow-up: "What if the graph is directed?" — bipartiteness applies to undirected graphs; for directed graphs the analogous property is 2-colorability of the underlying undirected graph.
- Related problems: Course Schedule (cycle detection), Social Network Friend/Foe (bipartite check), Possible Bipartition (LeetCode 886).
