# Bellman-Ford Algorithm

> **Step 15 — Graphs: Shortest Path** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

Given a directed weighted graph with `V` vertices (numbered `0` to `V-1`) and `E` edges (represented as `[u, v, weight]` triples, where weight may be negative), find the **shortest distance** from a given source vertex `src` to all other vertices.

If a vertex is unreachable from `src`, its distance is infinity. If a vertex's shortest path passes through a **negative-weight cycle**, report its distance as `-infinity`.

---

## Examples

| V | Edges | src | Output (distances from src) |
|---|-------|-----|-----------------------------|
| 5 | `[[0,1,4],[0,2,5],[1,3,-3],[2,1,-4],[3,4,2]]` | 0 | `[0, 1, 5, -2, 0]` |
| 3 | `[[0,1,1],[1,2,-2],[2,1,1]]` | 0 | `[0, -inf, -inf]` (cycle 1→2→1 has weight -1) |

**Visual (example 1):**
```
0 --4--> 1 ---(-3)--> 3 --2--> 4
 \       ^
  5    (-4)
   \   /
    > 2
Shortest path to 3: 0->2->1->3 = 5 + (-4) + (-3) = -2
```

---

## Constraints

- `1 <= V <= 500`
- `0 <= E <= V*(V-1)`
- `-1000 <= weight <= 1000`
- `0 <= src < V`
- Edges are directed; the graph may contain negative-weight edges and negative cycles

---

## Approach 1: Brute Force — DFS over All Simple Paths

**Intuition:** From the source, explore all possible paths using DFS. Maintain a running cost; prune branches that cannot improve the current best distance. The shortest path to each vertex is the minimum cost seen across all explorations.

Fails on graphs with negative cycles (infinite exploration) and is exponentially slow on dense graphs.

**Steps:**
1. Build adjacency list from edge list.
2. Initialize `dist[src] = 0`, all others = infinity.
3. DFS from source with `(current_vertex, cumulative_cost, visited_set)`.
4. If `cost >= dist[u]`, prune.
5. Otherwise update `dist[u] = cost`, then recurse to all unvisited neighbors.

| Metric | Value |
|--------|-------|
| Time   | O(V! * E) worst case |
| Space  | O(V) recursion stack |

---

## Approach 2: Optimal — Classic Bellman-Ford (V-1 Relaxations)

**Intuition:** A shortest path with no negative cycles visits at most `V-1` edges (any more and it would revisit a vertex, forming a cycle). So we perform exactly `V-1` passes, each relaxing every edge in the graph. After pass `i`, all shortest paths using at most `i` edges are correct.

After `V-1` passes, run **one more pass**: if any distance still decreases, there is a negative cycle. Mark those vertices as `-infinity`.

**Steps:**
1. `dist[src] = 0`, all others = `INF`.
2. Repeat `V-1` times:
   - For every edge `(u, v, w)`: if `dist[u] + w < dist[v]`, update `dist[v] = dist[u] + w`.
   - Break early if no update occurred (converged).
3. Run a `V`-th pass. If any edge still relaxes, set `dist[v] = -INF`.
4. Return `dist`.

| Metric | Value |
|--------|-------|
| Time   | O(V * E) |
| Space  | O(V) |

---

## Approach 3: Best — Bellman-Ford with Full Negative-Cycle Propagation

**Intuition:** Same as Approach 2, but after detecting the initial set of vertices on or adjacent to a negative cycle, **BFS propagates the `-INF` marking** to ALL vertices reachable from those nodes. This correctly identifies every vertex whose true shortest distance is `-infinity` — not just the first vertex the V-th pass touches.

**Steps:**
1–3. Same as Approach 2 through the V-th relaxation pass.
4. Collect all vertices `v` where `dist[u] + w < dist[v]` into set `negCycle`.
5. BFS from all vertices in `negCycle`: add every reachable vertex to `negCycle`.
6. For all vertices in `negCycle`, set `dist[v] = -INF`.
7. Return `dist`.

| Metric | Value |
|--------|-------|
| Time   | O(V * E) |
| Space  | O(V + E) for BFS adjacency list |

---

## Real-World Use Case

**Currency arbitrage detection:** In foreign exchange markets, model each currency as a vertex and each exchange rate as a directed edge with weight `-log(rate)`. Bellman-Ford on this graph detects negative cycles, which correspond to profitable arbitrage loops (a sequence of trades that gains money). Banks and trading systems use this to find or prevent exploitable cycles.

**Network routing (RIP protocol):** The Routing Information Protocol uses Bellman-Ford to compute shortest paths in computer networks. Each router knows its direct neighbors' costs and propagates distance vectors — exactly the edge-relaxation model. Negative cycles correspond to routing loops, detected and eliminated by the algorithm.

---

## Interview Tips

- State the key limitation of Dijkstra upfront: "Dijkstra fails with negative edges because it assumes the greedy nearest-node is always optimal — a later negative edge could create a shorter path." Bellman-Ford avoids this by re-examining all edges in every pass.
- The V-1 pass count is derived from the simple observation: any path in a V-node graph visits at most V nodes, so at most V-1 edges. This is the most important insight to explain.
- Early termination (break if no update in a pass) reduces average-case runtime significantly — always mention it.
- Distinguish between "negative-weight edges" (Bellman-Ford handles fine) and "negative-weight cycles" (makes shortest path undefined — report -infinity).
- Time complexity O(VE) is fine for sparse graphs but slow for dense graphs (O(V^3)). For dense graphs without negative edges, Dijkstra with a Fibonacci heap is better at O(V log V + E).
- Follow-up: SPFA (Shortest Path Faster Algorithm) is an optimization of Bellman-Ford using a queue to only relax vertices whose neighbors need updating — average case O(E) but worst case still O(VE).
