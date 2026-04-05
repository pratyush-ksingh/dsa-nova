# Shortest Path in Undirected Unit Weight Graph

> **Batch 2 of 12** | **Topic:** Graphs | **Difficulty:** Medium | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an **undirected, unweighted** (unit weight) graph with `V` vertices and `E` edges, and a **source vertex `src`**, find the **shortest distance** from `src` to every other vertex. If a vertex is unreachable, its distance is `-1`. All edges have weight `1`.

### Real-World Analogy
Imagine a city metro system where every station-to-station connection takes exactly 1 minute. You want to know: "Starting from station X, what's the minimum number of stops to reach every other station?" BFS naturally solves this because it explores stations in order of increasing distance -- first all 1-stop stations, then all 2-stop stations, and so on.

### Three Key Observations

1. **BFS explores nodes in order of increasing distance** -- In an unweighted graph, BFS guarantees that when you first reach a node, you've found the shortest path to it. This is because BFS processes all nodes at distance `d` before any node at distance `d+1`.
   - *Aha:* BFS IS single-source shortest path for unweighted graphs. No need for Dijkstra.

2. **Initialize distances to infinity (or -1), source to 0** -- The distance array serves dual purpose: tracking shortest distance AND acting as the visited check (`dist[v] != INF` means visited).
   - *Aha:* We don't need a separate `visited[]` array -- the distance array itself tells us if a node has been reached.

3. **This is simpler than Dijkstra** -- Dijkstra handles weighted graphs with a priority queue (O((V+E) log V)). For unit weights, a simple queue suffices, giving O(V + E).
   - *Aha:* Using Dijkstra here would work but is overkill -- BFS is faster and simpler.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Algorithm | BFS | Optimal for unweighted shortest paths |
| Data structure | Queue (FIFO) | Processes nodes level by level |
| Distance tracking | int[] dist, initialized to -1 | -1 = unreachable, doubles as visited |
| Graph representation | Adjacency list | Efficient traversal |

---

## APPROACH LADDER

### Approach 1: BFS from Source (Optimal)

**Intuition:** BFS from the source naturally discovers nodes in order of increasing distance. The first time we reach any node is via the shortest path.

**Steps:**
1. Create `dist[]` of size V, initialize all to `-1`
2. Set `dist[src] = 0`
3. Create queue, enqueue `src`
4. While queue is not empty:
   - Dequeue `node`
   - For each neighbor `v` of `node`:
     - If `dist[v] == -1` (unvisited):
       - `dist[v] = dist[node] + 1`
       - Enqueue `v`
5. Return `dist[]`

**ASCII Graph Example:**
```
        0 --- 1 --- 2
        |           |
        3           4
              |
              5

Adjacency List:
0: [1, 3]
1: [0, 2]
2: [1, 4]
3: [0, 5]
4: [2]
5: [3]

Source = 0
```

**Dry-Run Trace:**
```
dist = [-1, -1, -1, -1, -1, -1]
dist[0] = 0, queue = [0]

Process 0 (dist=0):
  neighbor 1: dist=-1 -> dist[1]=1, queue=[1]
  neighbor 3: dist=-1 -> dist[3]=1, queue=[1, 3]

Process 1 (dist=1):
  neighbor 0: dist=0 (visited), skip
  neighbor 2: dist=-1 -> dist[2]=2, queue=[3, 2]

Process 3 (dist=1):
  neighbor 0: dist=0 (visited), skip
  neighbor 5: dist=-1 -> dist[5]=2, queue=[2, 5]

Process 2 (dist=2):
  neighbor 1: visited, skip
  neighbor 4: dist=-1 -> dist[4]=3, queue=[5, 4]

Process 5 (dist=2):
  neighbor 3: visited, skip

Process 4 (dist=3):
  neighbor 2: visited, skip

Final dist = [0, 1, 2, 1, 3, 2]
```

**Trace Table:**
```
Step | Queue      | Node | Action                    | dist[]
-----|------------|------|---------------------------|------------------
  0  | [0]        |  --  | Initialize                | [0,-1,-1,-1,-1,-1]
  1  | [1,3]      |  0   | Visit neighbors 1, 3      | [0, 1,-1, 1,-1,-1]
  2  | [3,2]      |  1   | Visit neighbor 2           | [0, 1, 2, 1,-1,-1]
  3  | [2,5]      |  3   | Visit neighbor 5           | [0, 1, 2, 1,-1, 2]
  4  | [5,4]      |  2   | Visit neighbor 4           | [0, 1, 2, 1, 3, 2]
  5  | [4]        |  5   | No unvisited neighbors     | [0, 1, 2, 1, 3, 2]
  6  | []         |  4   | No unvisited neighbors     | [0, 1, 2, 1, 3, 2]
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each vertex dequeued once, each edge examined once |
| Space  | O(V) | Distance array + queue |

---

### Approach 2: Dijkstra (Overkill but Works)

**Intuition:** Dijkstra's algorithm with a priority queue works for any non-negative weights, including unit weights. However, when all weights are 1, the priority queue degenerates to a FIFO queue, making Dijkstra equivalent to BFS but with extra overhead.

**Why it's overkill:**
- Priority queue operations are O(log V) each
- Total: O((V + E) log V) vs. BFS's O(V + E)
- For unit weights, no priority ordering is needed -- FIFO suffices

**BUD Analysis:**
- **B**ottleneck: BFS is already optimal -- O(V + E)
- **U**nnecessary: Dijkstra's priority queue adds log V factor for no benefit
- **D**uplicate: None in BFS; each node processed exactly once

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O((V + E) log V) | Priority queue overhead |
| Space  | O(V) | Distance array + priority queue |

---

### Approach 3: Reconstructing the Actual Path

**Intuition:** To find not just the distance but the actual shortest path, maintain a `parent[]` array. When updating `dist[v]`, set `parent[v] = node`. Trace back from destination to source using `parent[]`.

**Steps:**
1. Same BFS as Approach 1
2. Additionally: `parent[v] = node` when `dist[v]` is set
3. To get path to node `t`: follow parent pointers from `t` back to `src`, then reverse

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same BFS + O(path length) for reconstruction |
| Space  | O(V) | Additional parent array |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** BFS uses a queue. Each of the V nodes enters and leaves the queue exactly once. For each node, we examine its adjacency list -- the sum of all adjacency list lengths is 2E (each undirected edge counted twice). Total work: V + 2E = O(V + E).

**Why is BFS correct for shortest paths?** BFS processes nodes in "waves." Wave 0 is the source. Wave 1 is all nodes 1 edge away. Wave k is all nodes k edges away. Since edges have unit weight, the first time a node is discovered IS the shortest path.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Source only (no edges) | `dist = [0, -1, -1, ...]` | All others unreachable |
| Disconnected graph | Unreachable nodes get `-1` | BFS only reaches source's component |
| Source = V-1 (last node) | Distances measured from last node | Don't assume source is 0 |
| Self-loop | Doesn't affect shortest paths | BFS skips visited nodes |
| Single node graph | `dist = [0]` | Handle V=1 |

**Common Mistakes:**
- Using DFS instead of BFS (DFS does NOT find shortest paths)
- Forgetting to mark distance before enqueueing (could process same node multiple times)
- Not initializing distances to -1/infinity
- Using Dijkstra when BFS suffices (slower for no reason)

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "What if edges have different weights?" | Use Dijkstra (positive weights) or Bellman-Ford (negative weights) |
| "What if you only need distance to one target?" | BFS still, but early-terminate when target is dequeued |
| "Can you reconstruct the path?" | Yes, maintain parent[] array (Approach 3) |
| "Bidirectional BFS?" | Start BFS from both source and target; meet in the middle. Reduces search space. |
| "What about weighted unit-cost edges (0 or 1)?" | Use 0-1 BFS with a deque: push weight-0 neighbors to front, weight-1 to back |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| BFS Traversal | BFS is the foundation; this adds distance tracking |
| Dijkstra's Algorithm | Generalization for weighted graphs |
| Bellman-Ford | Handles negative weights (this problem has none) |
| Word Ladder (LC #127) | BFS shortest path where "nodes" are words, "edges" are 1-char changes |
| Shortest Path in Binary Matrix (LC #1091) | BFS on a grid with 8-directional movement |
| 0-1 BFS | Handles edges with weight 0 or 1 using deque |
