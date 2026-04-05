# Dijkstra's Algorithm

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a weighted, undirected (or directed) graph with **V** vertices and **E** edges, and a source vertex `src`, find the **shortest distance** from `src` to every other vertex. All edge weights are **non-negative**.

Return an array `dist[]` where `dist[i]` is the shortest distance from `src` to vertex `i`. If a vertex is unreachable, its distance is infinity (or `Integer.MAX_VALUE`).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| V=5, E=6, src=0, edges=[(0,1,2),(0,3,6),(1,2,3),(1,3,8),(1,4,5),(2,4,7)] | [0, 2, 5, 6, 7] | 0->1=2, 0->1->2=5, 0->3=6, 0->1->4=7 |
| V=3, E=2, src=0, edges=[(0,1,1),(1,2,3)] | [0, 1, 4] | 0->1=1, 0->1->2=4 |
| V=2, E=0, src=0 | [0, INF] | No edges, vertex 1 unreachable |

```
Example Graph (V=5, src=0):

    (0)---2---(1)---3---(2)
     |         |         |
     6         8         7
     |         |         |
    (3)       (1)-------(4)
                   5

Shortest paths from 0:
  0 -> 0 : 0
  0 -> 1 : 2           (direct edge)
  0 -> 2 : 2+3 = 5     (via 1)
  0 -> 3 : 6           (direct edge)
  0 -> 4 : 2+5 = 7     (via 1)
```

### Real-World Analogy
Think of Google Maps finding the fastest route. Cities are vertices, roads are edges, and travel times are weights. Dijkstra always expands to the nearest unvisited city first -- like a ripple expanding outward from a stone dropped in water. The ripple reaches closer points first, guaranteeing that by the time it reaches any city, it has found the shortest path.

### Three Key Observations

1. **Greedy: always process the closest unfinalized vertex** -- If we always pick the vertex with the smallest tentative distance, we can "finalize" it because no shorter path can exist (all edges are non-negative).
   - *Aha:* This greedy choice is what makes Dijkstra work. Negative edges would break this guarantee.

2. **Relaxation is the core operation** -- For each neighbor `v` of the current vertex `u`, check if `dist[u] + weight(u,v) < dist[v]`. If so, update `dist[v]`.
   - *Aha:* Each edge is relaxed at most once (with a proper min-heap), giving us the E log V bound.

3. **A priority queue replaces the naive linear scan** -- Without it, finding the minimum-distance vertex costs O(V) per extraction. A min-heap reduces this to O(log V).
   - *Aha:* The naive O(V^2) approach is actually better for dense graphs (E ~ V^2) because the heap overhead dominates.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Core structure | Adjacency list | O(V+E) space, efficient neighbor iteration |
| Min extraction | Min-Heap / Priority Queue | O(log V) per extract-min vs O(V) linear scan |
| Distance tracking | dist[] array | O(1) lookup and update |
| Finalization | visited[] or lazy deletion | Avoid re-processing settled vertices |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Bellman-Ford Style Relaxation

**Intuition:** Relax ALL edges repeatedly. After V-1 passes, all shortest paths are found. This works even with negative edges but is overkill here.

**Steps:**
1. Initialize `dist[src] = 0`, all others = INF
2. Repeat V-1 times: for every edge (u, v, w), if `dist[u] + w < dist[v]`, update `dist[v]`
3. Return `dist[]`

```
Pass 1: dist = [0, 2, 5, 6, 7]   -- most get settled in pass 1 for this example
Pass 2: dist = [0, 2, 5, 6, 7]   -- no change, already optimal
...
```

**BUD Analysis:**
- **Bottleneck:** We relax every edge V-1 times even if most are already finalized
- **Unnecessary:** Edges from already-settled vertices are re-checked
- **Duplicated:** Same relaxations happen across multiple passes

| Metric | Value |
|--------|-------|
| Time   | O(V * E) |
| Space  | O(V) |

---

### Approach 2: Optimal -- Dijkstra with Min-Heap (Priority Queue)

**Intuition:** Instead of blindly relaxing all edges, use a greedy strategy. Always pick the unfinalized vertex with the smallest distance, relax its edges, and mark it done. A min-heap makes picking the minimum efficient.

**Steps:**
1. Initialize `dist[src] = 0`, all others = INF
2. Push `(0, src)` into min-heap
3. While heap is not empty:
   a. Pop `(d, u)` with smallest distance
   b. If `d > dist[u]`, skip (stale entry -- lazy deletion)
   c. For each neighbor `(v, w)` of `u`:
      - If `dist[u] + w < dist[v]`: update `dist[v]`, push `(dist[v], v)` to heap
4. Return `dist[]`

```
Dry-run (V=5, src=0):

Heap: [(0,0)]          dist: [0, INF, INF, INF, INF]

Pop (0,0): process node 0
  Relax 0->1: 0+2=2 < INF  => dist[1]=2, push (2,1)
  Relax 0->3: 0+6=6 < INF  => dist[3]=6, push (6,3)
Heap: [(2,1),(6,3)]    dist: [0, 2, INF, 6, INF]

Pop (2,1): process node 1
  Relax 1->2: 2+3=5 < INF  => dist[2]=5, push (5,2)
  Relax 1->3: 2+8=10 > 6   => skip
  Relax 1->4: 2+5=7 < INF  => dist[4]=7, push (7,4)
Heap: [(5,2),(6,3),(7,4)]  dist: [0, 2, 5, 6, 7]

Pop (5,2): process node 2
  Relax 2->4: 5+7=12 > 7   => skip
Heap: [(6,3),(7,4)]    dist: [0, 2, 5, 6, 7]

Pop (6,3): process node 3 -- no outgoing edges to improve
Pop (7,4): process node 4 -- no outgoing edges to improve

Final: [0, 2, 5, 6, 7]  ✓
```

**BUD Analysis:**
- Each vertex is extracted from heap at most O(V) times (with lazy deletion)
- Each edge is relaxed at most once per extraction of its source
- Total heap operations: O(E) insertions, O(V + E) extractions in worst case

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) |
| Space  | O(V + E) |

---

### Approach 3: Best -- Dijkstra with Indexed Priority Queue / Decrease-Key

**Intuition:** The standard min-heap approach uses "lazy deletion" -- stale entries stay in the heap. An indexed priority queue (or using a TreeSet that supports removal) allows true decrease-key, keeping the heap size at most V instead of potentially E.

**Steps:**
1. Same as Approach 2, but instead of pushing duplicate entries, call `decreaseKey(v, newDist)` on the indexed priority queue
2. Heap size never exceeds V, so each operation is O(log V)

**In practice**, the lazy deletion approach (Approach 2) is preferred because:
- Indexed priority queues are complex to implement
- The constant factor overhead often negates the theoretical improvement
- For sparse graphs, E ~ V so the difference is negligible

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) -- same asymptotic, better constant for dense graphs |
| Space  | O(V) for heap (vs O(E) with lazy deletion) |

---

## COMPLEXITY INTUITIVELY

**Why O((V+E) log V)?** Every vertex enters the heap (V inserts, each O(log V) = V log V). Every edge causes at most one relaxation that might push to heap (E inserts, each O(log V) = E log V). Total: **(V + E) log V**. Since E >= V-1 for connected graphs, this simplifies to **O(E log V)**.

**Why not O(V^2)?** That is the complexity of the naive approach without a heap. For sparse graphs (E ~ V), E log V << V^2. For dense graphs (E ~ V^2), V^2 < E log V, so the naive approach wins.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Disconnected graph | Some dist[i] = INF | Return INF, not -1 (problem-dependent) |
| Single vertex | dist = [0] | Don't forget src always has distance 0 |
| Self-loops | edge (u, u, w) | Relaxing u->u: dist[u]+w > dist[u]=0, so naturally skipped |
| Parallel edges | Multiple edges between same pair | Take the minimum during relaxation |
| src = any vertex | Works for any source | Don't hardcode src=0 |

**Common Mistakes:**
- Using Dijkstra with negative edges (use Bellman-Ford instead)
- Forgetting to skip stale heap entries (infinite loop or wrong answer)
- Using `visited[]` too early -- mark visited only when POPPING, not when pushing

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Tests understanding of greedy algorithms and their correctness proofs
- Priority queue usage is a key data structure skill
- Connects to real-world routing (GPS, network protocols like OSPF)

**Follow-ups to expect:**
1. "What if edges have negative weights?" -- Use Bellman-Ford O(VE) or SPFA
2. "What about negative cycles?" -- Bellman-Ford detects them in the V-th pass
3. "How to find the actual path, not just distance?" -- Maintain a `parent[]` array
4. "Dijkstra vs BFS?" -- BFS works only for unweighted (or unit-weight) graphs
5. "Time complexity for dense vs sparse?" -- Heap-based for sparse, O(V^2) for dense

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Bellman-Ford | Handles negative edges, O(VE), slower but more general |
| Floyd-Warshall | All-pairs shortest path, O(V^3) |
| BFS (unweighted) | Special case of Dijkstra where all weights = 1, no heap needed |
| A* Algorithm | Dijkstra + heuristic for faster goal-directed search |
| Network Delay Time (LC 743) | Direct application of Dijkstra |
| Shortest Path in Binary Maze | BFS variant since all weights are 1 |
