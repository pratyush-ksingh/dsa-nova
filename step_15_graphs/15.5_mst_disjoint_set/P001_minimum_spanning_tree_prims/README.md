# Minimum Spanning Tree (Prim's Algorithm)

> **Batch 2 of 12** | **Topic:** Graphs | **Difficulty:** Medium | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **connected, undirected, weighted** graph with `V` vertices and `E` edges, find the **Minimum Spanning Tree (MST)** -- a subset of edges that connects all vertices with the **minimum total edge weight**, without forming any cycle. Return the total MST weight and the edges in the MST. Use **Prim's algorithm**.

### Real-World Analogy
Imagine you're a city planner connecting `V` towns with roads. Building a road between towns `i` and `j` costs `w(i,j)` dollars. You want ALL towns connected (directly or indirectly) while spending the absolute minimum. You can't have extra roads creating loops -- that's wasted money. The MST is the cheapest road network that keeps everyone connected.

### Three Key Observations

1. **Prim's grows the MST one vertex at a time** -- Start from any node. At each step, pick the cheapest edge that connects a node IN the MST to a node NOT in the MST. This greedy choice is always optimal.
   - *Aha:* This is like Dijkstra but instead of tracking "distance from source," we track "cheapest edge to reach this node from the MST."

2. **The "cut property" guarantees correctness** -- For any cut (partition of vertices into two sets), the lightest edge crossing the cut is in SOME MST. Prim's repeatedly applies this property.
   - *Aha:* At each step, the MST-so-far vs. rest-of-graph is a cut. The cheapest crossing edge must be in the MST.

3. **A min-heap makes edge selection efficient** -- Without a heap, finding the minimum edge crossing the cut takes O(V) per step. A min-heap reduces this to O(log V), giving O(E log V) overall.
   - *Aha:* The heap stores (weight, node) pairs. When we pop the minimum, we get the cheapest way to add a new node to the MST.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Algorithm | Prim's | Grows MST greedily from a starting node |
| Optimization | Min-heap (priority queue) | Efficient extraction of minimum-weight edge |
| MST tracking | `inMST[]` boolean array | O(1) check if node is already in MST |
| Edge tracking | Store (parent, node, weight) when adding to MST | Reconstruct MST edges |
| Alternative | Kruskal's (sort edges + Union-Find) | Better for sparse graphs; Prim's better for dense |

---

## APPROACH LADDER

### Approach 1: Naive Prim's (No Heap)

**Intuition:** Maintain a `key[]` array where `key[v]` is the minimum weight edge connecting `v` to the current MST. At each step, scan ALL vertices to find the one with minimum key that's not yet in the MST.

**Steps:**
1. Initialize `key[0] = 0`, `key[i] = INF` for `i > 0`, `inMST[] = false`
2. Repeat V times:
   - Find vertex `u` with minimum `key[u]` where `inMST[u] = false` (O(V) scan)
   - Mark `u` as in MST
   - For each neighbor `(v, w)` of `u`:
     - If `!inMST[v]` and `w < key[v]`: update `key[v] = w`, `parent[v] = u`
3. Total MST weight = sum of `key[]`

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V^2) | V iterations, each scanning V vertices for minimum |
| Space  | O(V) | key[], inMST[], parent[] arrays |

> **Note:** O(V^2) is actually better than heap-based O(E log V) for **dense graphs** where E is close to V^2.

---

### Approach 2: Prim's with Min-Heap (Optimal for Sparse Graphs)

**Intuition:** Replace the O(V) minimum-finding scan with a min-heap. Push `(weight, node)` pairs into the heap. Always pop the lightest edge to an un-included node.

**Steps:**
1. Create min-heap, push `(0, 0)` (weight 0 for starting node)
2. Initialize `inMST[] = false`, `totalWeight = 0`
3. While heap is not empty:
   - Pop `(weight, node)` from heap
   - If `inMST[node]`: skip (stale entry)
   - Mark `inMST[node] = true`, add `weight` to `totalWeight`
   - For each neighbor `(v, w)` of `node`:
     - If `!inMST[v]`: push `(w, v)` to heap
4. Return `totalWeight`

**ASCII Graph Example:**
```
        (1)
    0 ------- 1
    |  \      |
 (4)|   \(3)  |(2)
    |    \    |
    3 ------- 2
        (5)

Edges: (0,1,1), (0,2,3), (0,3,4), (1,2,2), (2,3,5)
```

**Dry-Run Trace:**
```
Heap = [(0, 0)], inMST = [F,F,F,F], total = 0

Step 1: Pop (0, 0) -> inMST[0]=T, total=0
  Push neighbors: (1,1), (3,2), (4,3)
  Heap = [(1,1), (3,2), (4,3)]

Step 2: Pop (1, 1) -> inMST[1]=T, total=0+1=1
  Push neighbors: (2,2)  [skip 0, already in MST]
  Heap = [(2,2), (3,2), (4,3)]

Step 3: Pop (2, 2) -> inMST[2]=T, total=1+2=3
  Push neighbors: (5,3)  [skip 0,1 in MST]
  Heap = [(3,2), (4,3), (5,3)]

Step 4: Pop (3, 2) -> inMST[2]=T already! SKIP (stale)
  Heap = [(4,3), (5,3)]

Step 5: Pop (4, 3) -> inMST[3]=T, total=3+4=7
  No new neighbors to push
  Heap = [(5,3)]

Step 6: Pop (5, 3) -> inMST[3]=T already! SKIP
  Heap = []

MST total weight = 7
MST edges: 0-1 (w=1), 1-2 (w=2), 0-3 (w=4)
```

**Trace Table:**
```
Step | Pop        | inMST?  | Action           | total | MST edges so far
-----|------------|---------|------------------|-------|------------------
  1  | (0, node0) | New     | Add node 0       |   0   | --
  2  | (1, node1) | New     | Add edge 0-1     |   1   | {0-1}
  3  | (2, node2) | New     | Add edge 1-2     |   3   | {0-1, 1-2}
  4  | (3, node2) | Stale   | Skip             |   3   | {0-1, 1-2}
  5  | (4, node3) | New     | Add edge 0-3     |   7   | {0-1, 1-2, 0-3}
  6  | (5, node3) | Stale   | Skip             |   7   | {0-1, 1-2, 0-3}
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(E log V) | Each edge pushed/popped from heap at most once; heap ops are O(log V) |
| Space  | O(V + E) | Heap can hold up to E entries |

---

### Approach 3: Prim's with MST Edge Collection

**Intuition:** Same as Approach 2 but also track which edges are selected for the MST by recording the parent of each node when it joins the MST.

**Steps:**
1. Same heap-based approach
2. Push `(weight, node, parent)` triples
3. When popping a new node, record `(parent, node, weight)` as an MST edge

**BUD Analysis:**
- **B**ottleneck: Heap operations dominate -- O(E log V)
- **U**nnecessary: Stale heap entries are popped and discarded (acceptable)
- **D**uplicate: Could use decrease-key to avoid stale entries, but lazy deletion is simpler and equally efficient in practice

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(E log V) | Same as Approach 2 |
| Space  | O(V + E) | Same as Approach 2 + edge list |

---

## COMPLEXITY INTUITIVELY

**Why O(E log V)?** Each edge is considered at most twice (once from each endpoint). Each consideration involves a heap push (O(log V)). Each vertex is popped from the heap once. Total: O(E) heap pushes * O(log V) each = O(E log V).

**Prim's O(V^2) vs O(E log V):**
- Dense graph (E ~ V^2): O(V^2) is better (no log factor)
- Sparse graph (E ~ V): O(E log V) ~ O(V log V) is better
- In practice, most graphs are sparse, so heap-based Prim's is standard

**MST has exactly V-1 edges** -- A tree with V nodes always has V-1 edges. This is a useful sanity check.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Single node (V=1) | Weight = 0, no edges | Return empty edge list |
| Two nodes, one edge | MST = that edge | Simplest non-trivial case |
| Multiple edges same weight | Any valid MST (may not be unique) | All MSTs have same total weight |
| Disconnected graph | MST doesn't exist | Prim's only spans one component; need to check |
| Negative edge weights | Prim's handles them correctly | Unlike Dijkstra, Prim's works with negative weights |

**Common Mistakes:**
- Forgetting to skip stale heap entries (nodes already in MST)
- Using Dijkstra's relaxation (`dist[u] + w`) instead of Prim's (`w` alone)
- Not handling disconnected graphs (Prim's silently produces a spanning forest of one component)
- Confusing Prim's with Dijkstra -- they look similar but optimize different things

**Prim's vs Dijkstra -- Key Difference:**
```
Dijkstra: key[v] = min(key[v], dist[u] + w(u,v))   // total distance from source
Prim's:   key[v] = min(key[v], w(u,v))              // just the edge weight
```

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Prim's vs Kruskal's?" | Prim's grows from a node (good for dense/adjacency matrix). Kruskal's sorts edges globally (good for sparse/edge list). Both O(E log V). |
| "Is MST unique?" | Only if all edge weights are distinct. Otherwise, multiple MSTs with same total weight. |
| "Can you use Prim's on a directed graph?" | No. MST is defined for undirected graphs. For directed: use Chu-Liu/Edmonds' algorithm (minimum spanning arborescence). |
| "What if graph is disconnected?" | No spanning tree exists. Prim's gives a spanning tree of one component. Use Kruskal's to build a minimum spanning forest. |
| "Prove the greedy choice is optimal?" | Cut property: for any cut, the min-weight crossing edge is in some MST. Prim's always selects such edges. |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Kruskal's Algorithm | Alternative MST algorithm using sorted edges + Union-Find |
| Dijkstra's Algorithm | Same structure (min-heap + greedy) but for shortest paths, not MST |
| Network Delay Time (LC #743) | Dijkstra application; similar code structure |
| Min Cost to Connect All Points (LC #1584) | Direct Prim's application on coordinate points |
| Connecting Cities With Min Cost (LC #1135) | Classic MST problem |
| Second Minimum Spanning Tree | Find MST, then try replacing each MST edge |
