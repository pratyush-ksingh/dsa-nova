# Topological Sort BFS (Kahn's Algorithm)

> **Batch 3 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **Directed Acyclic Graph (DAG)** with `V` vertices (numbered `0` to `V-1`) and `E` directed edges, find a **topological ordering** of the vertices. A topological ordering is a linear ordering such that for every directed edge `u -> v`, vertex `u` appears before `v`. Implement this using **BFS (Kahn's Algorithm)** which uses in-degree tracking. Also detect if a valid topological sort exists (i.e., detect cycles).

### Examples

| Input (V, edges) | Output | Explanation |
|---|---|---|
| `V=6, [[5,2],[5,0],[4,0],[4,1],[2,3],[3,1]]` | `[4, 5, 2, 0, 3, 1]` (one valid ordering) | Every edge `u->v` has `u` before `v` |
| `V=4, [[0,1],[1,2],[2,3]]` | `[0, 1, 2, 3]` | Linear chain -- only one valid ordering |
| `V=3, [[0,1],[1,2],[2,0]]` | `[]` (cycle detected) | Circular dependency -- no valid topo sort |

### Real-World Analogy
Think of a **university course prerequisite system**. "Data Structures" must come before "Algorithms", which must come before "Machine Learning". Kahn's algorithm works like a student picking courses: first, take any course with zero prerequisites (in-degree 0). After completing it, remove its prerequisite requirement from all dependent courses. Repeat. If you finish all courses, there's no circular dependency. If courses remain but all have unfulfilled prerequisites, there's a circular dependency (deadlock).

### Three Key Observations

1. **In-degree = number of unresolved dependencies** -- A node with in-degree 0 has no prerequisites and can be processed immediately.
   - *Aha:* Start by collecting ALL nodes with in-degree 0 into a queue -- these are "ready" nodes.

2. **Removing a processed node reduces neighbors' in-degrees** -- When we process node `u`, every edge `u -> v` means `v` loses one dependency. If `v`'s in-degree drops to 0, it's now ready.
   - *Aha:* This is like a domino chain -- processing one node may unlock several others.

3. **If the result has fewer than V nodes, there's a cycle** -- Nodes in a cycle never reach in-degree 0 because they depend on each other circularly.
   - *Aha:* `len(result) < V` is the cycle detection condition. Elegant and free.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Graph storage | Adjacency list | Efficient edge traversal for sparse graphs |
| Processing order | Queue (FIFO) | BFS processes level by level (all in-degree 0 nodes first) |
| Dependency tracking | `inDegree[]` array | O(1) lookup and decrement per edge |
| Cycle detection | Compare result size with V | If `result.size() < V`, cycle exists |

---

## APPROACH LADDER

### Approach 1: Naive Simulation (Brute Force)

**Intuition:** Repeatedly scan all nodes to find one with in-degree 0, add it to the result, and "remove" it (mark as processed and decrement neighbors' in-degrees). This is what a human might do manually.

**Steps:**
1. Compute in-degree for every node
2. Repeat V times:
   - Scan all nodes to find an unprocessed node with in-degree 0
   - Add it to result, mark as processed
   - Decrement in-degree of all its neighbors
3. If we can't find a node with in-degree 0 in any iteration, cycle exists

**ASCII Diagram:**
```
Graph:  5 -> 2 -> 3 -> 1
        5 -> 0       ^
        4 -> 0      /
        4 -> 1 ----

In-degrees:  0:2  1:2  2:1  3:1  4:0  5:0

Iteration 1: Scan all -> find 4 (indeg=0), add to result
             Remove 4: decrement 0's indeg to 1, 1's indeg to 1
Iteration 2: Scan all -> find 5 (indeg=0), add to result
             Remove 5: decrement 2's indeg to 0, 0's indeg to 0
...and so on (rescanning ALL nodes each time)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V^2 + E) | V iterations, each scanning V nodes; plus initial in-degree computation |
| Space  | O(V + E) | Adjacency list + in-degree array |

---

### Approach 2: Kahn's Algorithm -- BFS with Queue (Optimal)

**Intuition:** Instead of rescanning all nodes each iteration, maintain a **queue** of nodes with in-degree 0. When processing a node, check if any neighbor's in-degree drops to 0 and enqueue it immediately. Each node and edge is processed exactly once.

**Steps:**
1. Build adjacency list and compute `inDegree[]`
2. Enqueue all nodes with `inDegree[v] == 0`
3. While queue is not empty:
   - Dequeue node `u`, append to `result`
   - For each neighbor `v` of `u`: decrement `inDegree[v]`; if it becomes 0, enqueue `v`
4. If `result.size() == V`: return `result` (valid topo sort)
5. Else: cycle detected, return empty

**ASCII Diagram:**
```
Graph:                    In-degrees:        Queue processing:
  5 --> 2 --> 3           0: 2               Queue: [4, 5]
  |          |            1: 2
  v          v            2: 1               Deq 4 -> result=[4]
  0    4 --> 1            3: 1                 0: 2->1, 1: 2->1
  ^    |                  4: 0               Deq 5 -> result=[4,5]
  |    v                  5: 0                 2: 1->0, 0: 1->0
  +--- 0                                      Enq 2,0 -> Queue: [2, 0]
                                             Deq 2 -> result=[4,5,2]
                                               3: 1->0 -> Enq 3
                                             Deq 0 -> result=[4,5,2,0]
                                             Deq 3 -> result=[4,5,2,0,3]
                                               1: 1->0 -> Enq 1
                                             Deq 1 -> result=[4,5,2,0,3,1]

                                             result.size()=6 == V=6  -> valid!
```

**Dry-Run Trace:**
```
V=6, edges: 5->2, 5->0, 4->0, 4->1, 2->3, 3->1

adj = {0:[], 1:[], 2:[3], 3:[1], 4:[0,1], 5:[2,0]}
inDeg = [2, 2, 1, 1, 0, 0]

Initial queue: [4, 5] (in-degree 0)

Process 4: result=[4]
  neighbor 0: inDeg[0]=2->1
  neighbor 1: inDeg[1]=2->1

Process 5: result=[4,5]
  neighbor 2: inDeg[2]=1->0 -> enqueue 2
  neighbor 0: inDeg[0]=1->0 -> enqueue 0

Process 2: result=[4,5,2]
  neighbor 3: inDeg[3]=1->0 -> enqueue 3

Process 0: result=[4,5,2,0]
  (no neighbors)

Process 3: result=[4,5,2,0,3]
  neighbor 1: inDeg[1]=1->0 -> enqueue 1

Process 1: result=[4,5,2,0,3,1]
  (no neighbors)

result.size()=6 == V=6 -> Return [4,5,2,0,3,1]  ✓
```

**BUD Analysis:**
- **B**ottleneck: We process each node once and each edge once -- can't do better
- **U**nnecessary: The brute force rescans all nodes; the queue eliminates that
- **D**uplicate: Each edge is traversed exactly once during neighbor processing

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each node enqueued/dequeued once; each edge examined once |
| Space  | O(V + E) | Adjacency list + in-degree array + queue |

---

### Approach 3: Kahn's with All Valid Orderings (Best -- Extended)

**Intuition:** When multiple nodes have in-degree 0 simultaneously, any can go next. To enumerate ALL valid topological orderings, use backtracking: try each in-degree-0 node, recurse, then undo. For the basic problem, the standard Kahn's (Approach 2) is already optimal; this extension is useful for interview follow-ups.

**Steps:**
1. Same setup as Approach 2 (adjacency list + in-degree)
2. Use backtracking: at each step, try ALL current in-degree-0 nodes
3. For each choice: add to result, decrement neighbors' in-degrees, recurse
4. After recursion: undo (backtrack) -- remove from result, increment in-degrees

**ASCII Diagram (branching choices):**
```
V=4, edges: 0->2, 1->2, 2->3

In-degree: [0, 0, 2, 1]

         Start (in-deg 0: {0, 1})
        /                        \
    Pick 0                     Pick 1
    result=[0]                 result=[1]
    in-deg 0: {1}             in-deg 0: {0}
       |                          |
    Pick 1                     Pick 0
    result=[0,1]               result=[1,0]
    in-deg 0: {2}             in-deg 0: {2}
       |                          |
    Pick 2                     Pick 2
    result=[0,1,2]             result=[1,0,2]
       |                          |
    Pick 3                     Pick 3
    result=[0,1,2,3]           result=[1,0,2,3]

All valid orderings: [0,1,2,3] and [1,0,2,3]
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V! * V) worst case | Exponential -- enumerating all valid orderings |
| Space  | O(V + E) | Same structures; recursion depth up to V |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** Kahn's algorithm visits each node exactly once (enqueue + dequeue) and traverses each edge exactly once (when decrementing neighbor in-degrees). Since those are the only operations, total work is proportional to the graph size.

**Why not faster?** We must examine every edge at least once to know the dependency structure. O(V + E) matches the input size, so it's optimal.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Single node, no edges | `[0]` | Trivially valid |
| No edges at all (V nodes) | Any permutation of `0..V-1` | All in-degrees are 0 |
| Linear chain `0->1->2->...` | `[0, 1, 2, ...]` | Only one valid ordering |
| Graph with cycle | `[]` or "cycle detected" | `result.size() < V` |
| Disconnected DAG | Still valid | Each component's nodes appear in valid order |

**Common Mistakes:**
- Forgetting to handle nodes with no incoming OR outgoing edges (they still need in-degree initialized to 0)
- Using DFS topo sort logic with Kahn's (they're different: DFS uses post-order, Kahn's uses in-degree)
- Not checking `result.size() == V` for cycle detection
- Modifying in-degree array in a way that prevents cycle detection

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Why BFS over DFS for topo sort?" | BFS (Kahn's) naturally gives cycle detection via count check; DFS needs extra coloring logic |
| "Can there be multiple valid orderings?" | Yes, whenever multiple nodes have in-degree 0 simultaneously |
| "How do you detect a cycle?" | If result has fewer than V nodes, some nodes never reached in-degree 0 (stuck in a cycle) |
| "What if I need lexicographically smallest ordering?" | Replace the queue with a min-heap (priority queue). Same complexity, sorted output |
| "DFS vs Kahn's trade-offs?" | DFS: simpler for basic topo sort, harder cycle detection. Kahn's: natural cycle detection, easier to reason about |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Course Schedule (LC #207) | Cycle detection using Kahn's -- can we finish all courses? |
| Course Schedule II (LC #210) | Find one valid ordering using Kahn's |
| Alien Dictionary (LC #269) | Build graph from character ordering, then Kahn's |
| Parallel Courses (LC #1136) | BFS levels = minimum semesters needed |
| Cycle Detection in Directed Graph | `result.size() < V` in Kahn's = cycle |
| Shortest Path in DAG | Topological sort + relaxation |
