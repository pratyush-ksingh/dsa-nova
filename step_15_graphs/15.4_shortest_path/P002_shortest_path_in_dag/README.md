# Shortest Path in DAG

> **Batch 3 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **weighted Directed Acyclic Graph (DAG)** with `V` vertices and `E` edges, and a **source vertex `src`**, find the shortest distance from `src` to every other vertex. If a vertex is unreachable from `src`, its distance is infinity (`-1`). Edges can have **negative weights** (unlike Dijkstra, this works because there are no cycles).

### Examples

| Input | Output | Explanation |
|---|---|---|
| `V=6, src=0`<br>`edges: 0->(1,2), 0->(4,1), 1->(2,3), 2->(3,6), 4->(2,2), 4->(5,4), 5->(3,1)` | `[0, 2, 3, 6, 1, 5]` | Shortest paths from 0: direct 0->1 costs 2, 0->4->2 costs 3, 0->4->5->3 costs 6, etc. |
| `V=4, src=0`<br>`edges: 0->(1,1), 1->(2,2)` | `[0, 1, 3, -1]` | Node 3 is unreachable from 0 |

### Real-World Analogy
Imagine you're planning a **project timeline** where tasks have dependencies (DAG) and each task takes a certain amount of time (edge weight). You want to find the earliest possible start time for each task, given that task 0 starts at time 0. You must process tasks in dependency order (topological sort), and each task starts as soon as all its prerequisites finish. The "shortest path" here gives the minimum time to reach each task.

### Three Key Observations

1. **Topological order guarantees all predecessors are processed first** -- When we process node `u`, we've already finalized the shortest distance to `u`, because all paths leading to `u` come from earlier nodes in topo order.
   - *Aha:* This is why we don't need Dijkstra's heap -- topo order gives us the "right" processing order for free.

2. **Relaxation along topo order: each edge examined exactly once** -- For each node `u` in topo order, relax all edges `u -> v`: if `dist[u] + weight < dist[v]`, update `dist[v]`.
   - *Aha:* Unlike Bellman-Ford (V*E iterations), we only need ONE pass through the edges.

3. **Negative weights are fine in a DAG** -- No cycles means no negative-weight cycles, so shortest paths are always well-defined. Dijkstra fails with negative weights, but topo-sort relaxation handles them perfectly.
   - *Aha:* This is the only single-source shortest path algorithm that handles negative weights in O(V+E).

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Graph storage | Adjacency list with weights | Need to store (neighbor, weight) pairs |
| Processing order | Topological sort | Ensures predecessors processed first |
| Distance tracking | `dist[]` array initialized to infinity | O(1) lookup and update |
| Topo sort method | DFS + stack OR Kahn's BFS | Either works; DFS shown here |

---

## APPROACH LADDER

### Approach 1: BFS/DFS Explore All Paths (Brute Force)

**Intuition:** For each target node, enumerate ALL paths from `src` to that node and pick the minimum-weight path. Exponentially many paths in the worst case.

**Steps:**
1. From `src`, do DFS exploring all paths
2. For each complete path to a node, record its total weight
3. For each node, take the minimum across all discovered paths

**ASCII Diagram:**
```
     2       3
0 -------> 1 -------> 2
|                      ^
| 1        2           |
+-------> 4 ----------+

All paths from 0 to 2:
  Path 1: 0 -> 1 -> 2  (cost: 2 + 3 = 5)
  Path 2: 0 -> 4 -> 2  (cost: 1 + 2 = 3)  <-- shortest

Brute force tries BOTH paths.
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) to O(2^V) | Exponential in dense graphs with many paths |
| Space  | O(V) | Recursion stack |

---

### Approach 2: Topological Sort + Relaxation (Optimal)

**Intuition:** First, compute a topological ordering of all vertices. Then, process vertices in that order. For each vertex `u`, relax all outgoing edges `u -> v`. Since `u`'s distance is already finalized (all predecessors came before it in topo order), every relaxation is correct and final.

**Steps:**
1. Compute topological sort (using DFS + stack)
2. Initialize `dist[src] = 0`, all others = infinity
3. Pop nodes from topo stack one by one:
   - If `dist[u] != INF` (reachable):
     - For each edge `u -> (v, w)`: if `dist[u] + w < dist[v]`, update `dist[v]`
4. Return `dist[]` (replace INF with -1 for unreachable nodes)

**ASCII Diagram:**
```
Graph with weights:
        2       3       6
   0 ------> 1 ------> 2 ------> 3
   |                    ^         ^
   | 1                  | 2       | 1
   +--------> 4 -------+    5 --+
              |              ^
              | 4            |
              +--------------+

Topo order (one possibility): [0, 1, 4, 2, 5, 3]

Process in topo order:
  Node 0: dist[0]=0
    relax 0->1: dist[1] = min(INF, 0+2) = 2
    relax 0->4: dist[4] = min(INF, 0+1) = 1

  Node 1: dist[1]=2
    relax 1->2: dist[2] = min(INF, 2+3) = 5

  Node 4: dist[4]=1
    relax 4->2: dist[2] = min(5, 1+2) = 3   <-- improved!
    relax 4->5: dist[5] = min(INF, 1+4) = 5

  Node 2: dist[2]=3
    relax 2->3: dist[3] = min(INF, 3+6) = 9

  Node 5: dist[5]=5
    relax 5->3: dist[3] = min(9, 5+1) = 6   <-- improved!

  Node 3: no outgoing edges

  dist = [0, 2, 3, 6, 1, 5]  ✓
```

**Dry-Run Trace:**
```
V=6, src=0
adj: 0->[(1,2),(4,1)], 1->[(2,3)], 2->[(3,6)], 4->[(2,2),(5,4)], 5->[(3,1)]

DFS topo sort:
  Visit 0 -> visit 1 -> visit 2 -> visit 3 (push 3)
  Back to 2 (push 2), back to 1 (push 1)
  Back to 0 -> visit 4 -> 2 already visited
  Visit 5 -> 3 already visited (push 5)
  Back to 4 (push 4), back to 0 (push 0)
  Stack (top to bottom): [0, 4, 5, 1, 2, 3]

dist = [0, INF, INF, INF, INF, INF]

Pop 0: relax 0->1: dist[1]=2, relax 0->4: dist[4]=1
Pop 4: relax 4->2: dist[2]=3, relax 4->5: dist[5]=5
Pop 5: relax 5->3: dist[3]=6
Pop 1: relax 1->2: dist[2]=min(3,5)=3 (no change)
Pop 2: relax 2->3: dist[3]=min(6,9)=6 (no change)
Pop 3: no edges

Result: [0, 2, 3, 6, 1, 5]  ✓
```

**BUD Analysis:**
- **B**ottleneck: Topo sort is O(V+E), relaxation is O(V+E) -- total O(V+E), optimal
- **U**nnecessary: We skip relaxation for nodes with `dist = INF` (unreachable)
- **D**uplicate: Each edge relaxed exactly once (no repeated processing like Bellman-Ford)

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Topo sort O(V+E) + one-pass relaxation O(V+E) |
| Space  | O(V + E) | Adjacency list + dist array + topo stack |

---

### Approach 3: Topo Sort + Relaxation with Kahn's BFS (Best Variant)

**Intuition:** Same algorithm but use Kahn's BFS instead of DFS for the topological sort. Advantage: no recursion stack, and naturally handles cycle detection if needed.

**Steps:**
1. Compute in-degrees, enqueue all in-degree-0 nodes
2. BFS: dequeue node `u`, add to topo order, decrement neighbors' in-degrees
3. Process topo order sequentially, relaxing edges as before
4. If topo order has fewer than V nodes, the graph has a cycle (invalid DAG)

**Key Difference from DFS approach:**
```
DFS topo sort:  Uses recursion + post-order stack reversal
Kahn's BFS:     Uses in-degree queue, natural forward order
Both produce valid topo orderings; same relaxation step follows.
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same asymptotic complexity |
| Space  | O(V + E) | Queue instead of recursion stack; same adjacency list |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** The topological sort visits each node once and each edge once. The relaxation phase also visits each node once and each edge once. Total: 2(V + E) = O(V + E).

**Why is this better than Dijkstra?** Dijkstra is O((V+E) log V) due to the priority queue. In a DAG, topological order replaces the heap, saving the log V factor AND handling negative weights.

**Why is this better than Bellman-Ford?** Bellman-Ford runs V-1 relaxation rounds over ALL edges: O(V * E). Topo sort + relax does it in ONE round: O(V + E).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Source has no outgoing edges | `[0, -1, -1, ...]` | Only source reachable |
| Disconnected nodes | `-1` for unreachable | Skip relaxation if `dist[u] == INF` |
| Negative edge weights | Still correct | DAG has no cycles, so no negative cycles |
| Single node graph | `[0]` | Trivially solved |
| All edges from source | Direct distances | One relaxation step suffices |
| Source is not node 0 | Adjust accordingly | Topo sort includes ALL nodes, but only relax from source |

**Common Mistakes:**
- Relaxing from unreachable nodes (dist = INF + weight can overflow or give wrong results)
- Forgetting that topo sort processes ALL nodes, not just reachable ones from source
- Using Dijkstra or Bellman-Ford when this simpler O(V+E) approach exists for DAGs
- Not handling the "source in the middle of topo order" case (nodes before source stay at INF)

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Why not just use Dijkstra?" | Dijkstra is O((V+E) log V) and fails with negative weights. Topo sort + relax is O(V+E) and handles negatives. |
| "Can this find longest path too?" | Yes! Negate all weights, find shortest path, negate result. Or change `min` to `max` in relaxation. |
| "What if the graph has a cycle?" | Then it's not a DAG. Use Bellman-Ford (handles negative weights) or Dijkstra (positive only). |
| "How does this relate to critical path?" | Critical path in project scheduling = longest path in DAG = same algorithm with max relaxation. |
| "What if there are multiple shortest paths?" | This finds the distance, not the path. To reconstruct: store `parent[]` array during relaxation. |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Topological Sort (LC #210) | First step of this algorithm |
| Dijkstra's Algorithm | General shortest path; unnecessary overhead for DAGs |
| Bellman-Ford | Handles negative weights + cycles, but O(VE) instead of O(V+E) |
| Longest Path in DAG | Same algorithm with max relaxation (or negate weights) |
| Critical Path Method (CPM) | Longest path in task DAG = minimum project duration |
| Course Schedule II (LC #210) | Produces topo order that this algorithm uses |
