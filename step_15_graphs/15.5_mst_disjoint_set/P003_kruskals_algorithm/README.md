# Kruskal's Algorithm

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a connected, undirected, weighted graph with **V** vertices and **E** edges, find the **Minimum Spanning Tree (MST)** -- a subset of edges that connects all vertices with the minimum total weight, using exactly V-1 edges and no cycles.

Return the total weight of the MST (or the list of MST edges).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| V=4, edges=[(0,1,10),(0,2,6),(0,3,5),(1,3,15),(2,3,4)] | 19 | MST edges: (2,3,4)+(0,3,5)+(0,1,10) = 19 |
| V=3, edges=[(0,1,1),(1,2,2),(0,2,3)] | 3 | MST edges: (0,1,1)+(1,2,2) = 3 |
| V=2, edges=[(0,1,7)] | 7 | Only one edge, must include it |

```
Example Graph (V=4):

    (0)---10---(1)
     | \        |
     6   5     15
     |     \    |
    (2)---4---(3)

Sorted edges: (2,3,4) (0,3,5) (0,2,6) (0,1,10) (1,3,15)

Kruskal's picks:
  Step 1: (2,3,4)  -- no cycle, add       Components: {0} {1} {2,3}
  Step 2: (0,3,5)  -- no cycle, add       Components: {0,2,3} {1}
  Step 3: (0,2,6)  -- CYCLE (0,2 same set), skip
  Step 4: (0,1,10) -- no cycle, add       Components: {0,1,2,3}
  Done: 3 edges = V-1. MST weight = 4+5+10 = 19

MST:
    (0)---10---(1)
       \
        5
          \
    (2)---4---(3)
```

### Real-World Analogy
Imagine you are a city planner who needs to connect all neighborhoods with roads. Each possible road has a construction cost. Kruskal's is like sorting all possible roads by cost from cheapest to most expensive, then building them one by one -- but skipping any road that would create a redundant loop (both neighborhoods are already reachable from each other). You stop once all neighborhoods are connected.

### Three Key Observations

1. **Sort edges by weight, greedily pick the lightest that does not form a cycle** -- This greedy strategy works because the lightest crossing edge of any cut must be in the MST (Cut Property).
   - *Aha:* We process edges globally by weight, not vertex by vertex like Prim's.

2. **Union-Find efficiently detects cycles** -- Two vertices are in the same component if and only if they have the same root in Union-Find. Adding an edge between them would create a cycle.
   - *Aha:* With path compression + union by rank, each operation is nearly O(1) amortized -- O(alpha(V)).

3. **Stop after selecting V-1 edges** -- A spanning tree of V vertices always has exactly V-1 edges. Once we have that many, we are done.
   - *Aha:* Early termination can save time when E >> V.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Edge sorting | Sort edge list | O(E log E) -- dominates the total cost |
| Cycle detection | Union-Find (DSU) | O(alpha(V)) per query, nearly constant |
| Edge storage | List of (weight, u, v) tuples | Easy to sort by weight |
| MST tracking | Count edges added | Stop at V-1 |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try All Spanning Trees

**Intuition:** Enumerate all possible subsets of V-1 edges, check if each forms a spanning tree, and return the one with minimum weight.

**Steps:**
1. Generate all C(E, V-1) combinations of edges
2. For each combination, check if it forms a connected tree (no cycles, all vertices connected)
3. Track the minimum weight combination

**BUD Analysis:**
- **Bottleneck:** C(E, V-1) combinations -- exponential
- **Unnecessary:** We check many invalid combinations that contain cycles
- **Duplicated:** Same connectivity checks repeated across similar subsets

| Metric | Value |
|--------|-------|
| Time   | O(C(E,V-1) * V) -- exponential |
| Space  | O(V + E) |

---

### Approach 2: Optimal -- Kruskal's with Union-Find

**Intuition:** Sort all edges by weight. Greedily add the lightest edge that does not create a cycle (checked via Union-Find). Stop after V-1 edges.

**Steps:**
1. Sort all edges by weight ascending
2. Initialize Union-Find with V components
3. For each edge (u, v, w) in sorted order:
   a. If `find(u) != find(v)`: union them, add edge to MST, increment count
   b. If count == V-1: break
4. Return total MST weight

```
Dry-run (V=4, edges sorted):

Union-Find: parent=[0,1,2,3], rank=[0,0,0,0]

Edge (2,3,4): find(2)=2, find(3)=3 => different, UNION
  parent=[0,1,2,2], MST=[(2,3,4)], weight=4, count=1

Edge (0,3,5): find(0)=0, find(3)=2 => different, UNION
  parent=[2,1,2,2], MST=[(2,3,4),(0,3,5)], weight=9, count=2

Edge (0,2,6): find(0)=2, find(2)=2 => SAME, skip (cycle!)

Edge (0,1,10): find(0)=2, find(1)=1 => different, UNION
  parent=[2,2,2,2], MST=[(2,3,4),(0,3,5),(0,1,10)], weight=19, count=3

count=3 = V-1=3 => DONE. MST weight = 19 ✓
```

**BUD Analysis:**
- Sorting is O(E log E) = O(E log V) since E <= V^2
- Each Union-Find operation is O(alpha(V)) amortized
- Total: dominated by sorting

| Metric | Value |
|--------|-------|
| Time   | O(E log E) |
| Space  | O(V + E) |

---

### Approach 3: Best -- Kruskal's with Optimized Union-Find

**Intuition:** Same algorithm, but with both **path compression** and **union by rank** optimizations in Union-Find. This gives O(alpha(V)) amortized per operation where alpha is the inverse Ackermann function (practically constant, <= 4 for any real input).

**Steps:**
1. Same as Approach 2
2. Union-Find with path compression: `find(x)` flattens the tree
3. Union by rank: attach shorter tree under taller tree

**Kruskal's vs Prim's Comparison:**

| Aspect | Kruskal's | Prim's |
|--------|-----------|--------|
| Strategy | Edge-centric (sort all edges) | Vertex-centric (grow from one vertex) |
| Best for | Sparse graphs (E ~ V) | Dense graphs (E ~ V^2) |
| Data structure | Union-Find | Priority Queue |
| Time | O(E log E) | O(E log V) with heap, O(V^2) without |
| Parallelizable | Yes (edges are independent) | No (sequential growth) |

| Metric | Value |
|--------|-------|
| Time   | O(E log E + E * alpha(V)) ~ O(E log E) |
| Space  | O(V + E) |

---

## COMPLEXITY INTUITIVELY

**Why O(E log E)?** Sorting E edges dominates. After sorting, we iterate through edges once, doing O(alpha(V)) Union-Find operations per edge. Since alpha(V) <= 4 for all practical purposes, the Union-Find part is effectively O(E). Total = O(E log E + E) = O(E log E).

**Note:** O(E log E) = O(E log V) because E <= V^2, so log E <= 2 log V.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Already a tree (E = V-1) | All edges included | MST = the graph itself |
| Disconnected graph | Cannot form spanning tree | Return -1 or handle (count < V-1) |
| All edges same weight | Any spanning tree is MST | Kruskal's picks arbitrarily |
| Single vertex | MST weight = 0, no edges | Handle V=1 as base case |
| Parallel edges | Same pair, different weights | Sort handles this; lighter one picked first |

**Common Mistakes:**
- Forgetting to check if MST has exactly V-1 edges (graph might be disconnected)
- Not implementing both path compression AND union by rank (one alone is slower)
- Sorting edges by vertex index instead of weight

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Tests knowledge of greedy algorithms and their correctness (Cut Property)
- Union-Find is a fundamental data structure -- demonstrates mastery
- Comparing Kruskal's vs Prim's shows deep understanding of trade-offs

**Follow-ups to expect:**
1. "When is Kruskal's better than Prim's?" -- Sparse graphs (E ~ V), or when edges are given as a list
2. "Prove correctness" -- Cut Property: lightest edge crossing any cut must be in MST
3. "Can you find a maximum spanning tree?" -- Sort edges in descending order
4. "What if edges arrive in a stream?" -- Online MST algorithms, or re-run Kruskal's
5. "Unique MST?" -- If all edge weights are distinct, the MST is unique

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Prim's Algorithm | Alternative MST algorithm, vertex-centric, better for dense graphs |
| Union-Find / DSU | Core data structure enabling cycle detection in O(alpha(V)) |
| Boruvka's Algorithm | Third MST approach, parallelizable, O(E log V) |
| Network Connected (LC 1319) | Uses Union-Find to count components, similar DSU pattern |
| Min Cost to Connect All Points (LC 1584) | Direct MST application with coordinate-based weights |
| Redundant Connection (LC 684) | Find the edge that creates a cycle -- Union-Find again |
