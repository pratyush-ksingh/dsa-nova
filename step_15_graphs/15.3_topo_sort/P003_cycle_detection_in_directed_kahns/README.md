# Cycle Detection in Directed Graph (Kahn's Algorithm)

> **Batch 3 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **directed graph** with `V` vertices and `E` edges, determine whether the graph contains a **cycle**. Use **Kahn's Algorithm** (BFS-based topological sort): if the topological sort cannot include all vertices, the graph has a cycle. Return `true` if a cycle exists, `false` otherwise. Optionally, identify which nodes are part of the cycle.

### Examples

| Input (V, edges) | Output | Explanation |
|---|---|---|
| `V=4, [[0,1],[1,2],[2,3]]` | `false` | Linear chain -- valid DAG, no cycle |
| `V=3, [[0,1],[1,2],[2,0]]` | `true` | 0->1->2->0 forms a cycle |
| `V=5, [[0,1],[1,2],[2,0],[3,4]]` | `true` | Nodes 0,1,2 form a cycle; 3,4 are acyclic |
| `V=1, []` | `false` | Single node, no edges, no cycle |

### Real-World Analogy
Think of a **build system** (like Make or Gradle) where modules depend on each other. If module A depends on B, B depends on C, and C depends on A, you have a **circular dependency** -- the build can never complete because each module waits for the other. Kahn's algorithm is like the build scheduler: it processes modules with zero unresolved dependencies. If some modules remain unprocessable, there's a circular dependency.

```
Normal (DAG):                 Circular dependency:
  A --> B --> C               A --> B
        |                     ^     |
        v                     |     v
        D                     C <---+

Build order: A, B, C, D      Cannot build! A waits for C,
                              C waits for B, B waits for A.
```

### Three Key Observations

1. **Kahn's algorithm naturally detects cycles** -- It processes all in-degree-0 nodes. Nodes in a cycle NEVER reach in-degree 0 because they mutually depend on each other.
   - *Aha:* If `processed_count < V`, the unprocessed nodes form one or more cycles.

2. **No extra work needed beyond standard Kahn's** -- Just run topo sort and check the count. Cycle detection is a "free" byproduct.
   - *Aha:* Compare this to DFS cycle detection which needs 3-color marking (white/gray/black). Kahn's is simpler conceptually.

3. **Nodes NOT in the topo order are the cyclic nodes** -- After Kahn's completes, any node with remaining in-degree > 0 is part of (or downstream of) a cycle.
   - *Aha:* This lets you identify WHICH nodes are involved in cycles, not just whether a cycle exists.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Graph storage | Adjacency list | Efficient traversal |
| Algorithm | Kahn's BFS | Natural cycle detection via count |
| Queue | FIFO queue | Process nodes level by level |
| Cycle detection | `count < V` check | Free byproduct of topo sort |

---

## APPROACH LADDER

### Approach 1: DFS with 3-Color Marking (Brute/Standard)

**Intuition:** Color nodes white (unvisited), gray (in current DFS path), black (fully processed). If DFS reaches a gray node, we've found a back edge = cycle.

**Steps:**
1. Initialize all nodes as `WHITE` (unvisited)
2. For each unvisited node, start DFS:
   - Mark node `GRAY` (entering)
   - Visit all neighbors:
     - If `GRAY`: cycle found (back edge)
     - If `WHITE`: recurse
   - Mark node `BLACK` (leaving -- fully processed)
3. If no back edges found, no cycle

**ASCII Diagram:**
```
DFS from 0:         Colors during DFS:
  0 -> 1 -> 2       0: WHITE -> GRAY
       ^    |        1: WHITE -> GRAY
       |    v        2: WHITE -> GRAY
       +--- 2        2 -> 0? 0 is GRAY! CYCLE!

Color meanings:
  WHITE: haven't touched yet
  GRAY:  currently on the DFS stack (ancestor in current path)
  BLACK: done processing (not on current path)
```

**Dry-Run Trace (cycle case):**
```
V=3, edges: 0->1, 1->2, 2->0
color = [WHITE, WHITE, WHITE]

dfs(0): color[0]=GRAY
  neighbor 1: WHITE -> dfs(1)
    color[1]=GRAY
    neighbor 2: WHITE -> dfs(2)
      color[2]=GRAY
      neighbor 0: GRAY -> CYCLE DETECTED!  ✓
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Visit each node and edge once |
| Space  | O(V) | Color array + recursion stack |

---

### Approach 2: Kahn's Algorithm -- BFS (Optimal)

**Intuition:** Run Kahn's topological sort. If the number of nodes processed equals V, the graph is a DAG (no cycle). If fewer than V nodes are processed, the remaining nodes are trapped in cycles.

**Steps:**
1. Build adjacency list and compute `inDegree[]` for every node
2. Enqueue all nodes with `inDegree == 0`
3. BFS: dequeue node, increment `count`, decrement neighbors' in-degrees; enqueue those reaching 0
4. After BFS: if `count == V`, no cycle. If `count < V`, cycle exists.

**ASCII Diagram:**
```
Example 1: No cycle                 Example 2: Has cycle
  0 -> 1 -> 2 -> 3                   0 -> 1
                                      ^    |
In-degree: [0, 1, 1, 1]              |    v
Queue: [0]                            2 <--+
Process 0 -> 1 -> 2 -> 3
count=4 == V=4 -> NO CYCLE          In-degree: [1, 1, 1]
                                     Queue: [] (empty! no in-degree 0)
                                     count=0 < V=3 -> CYCLE!

Example 3: Partial cycle
  0 -> 1 -> 2      3 -> 4
       ^    |
       |    v
       +----+

In-degree: [0, 2, 1, 0, 1]
Queue: [0, 3]
Process 0: inDeg[1]-- -> inDeg[1]=1 (not 0 yet!)
Process 3: inDeg[4]-- -> inDeg[4]=0, enqueue 4
Process 4: done
count=3 < V=5 -> CYCLE (nodes 1,2 stuck in cycle)
```

**Dry-Run Trace:**
```
V=4, edges: 0->1, 1->2, 2->1, 2->3

adj = {0:[1], 1:[2], 2:[1,3], 3:[]}
inDeg = [0, 2, 1, 1]

Queue: [0] (only node 0 has in-degree 0)

Process 0: count=1
  neighbor 1: inDeg[1]=2->1 (not 0, don't enqueue)

Queue empty! count=1 < V=4 -> CYCLE DETECTED  ✓

Cyclic nodes: 1, 2 (mutually dependent: 1->2->1)
Node 3 is downstream of the cycle, also stuck.
```

**BUD Analysis:**
- **B**ottleneck: Each node and edge processed at most once -- O(V+E), optimal
- **U**nnecessary: DFS 3-color approach requires understanding stack state; Kahn's is more intuitive
- **D**uplicate: No redundant processing -- each edge examined exactly once

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Standard Kahn's complexity |
| Space  | O(V + E) | Adjacency list + in-degree array + queue |

---

### Approach 3: Kahn's with Cycle Node Identification (Best)

**Intuition:** Beyond just detecting a cycle, identify exactly WHICH nodes are part of cycles. After Kahn's completes, any node NOT in the result has `inDegree > 0` and is either in a cycle or reachable only through a cycle.

**Steps:**
1. Run standard Kahn's algorithm
2. Collect the set of processed nodes
3. Any node NOT in the processed set is "stuck" -- involved in or downstream of a cycle
4. To find nodes DIRECTLY in cycles (not just downstream), check which stuck nodes have all predecessors also stuck

**ASCII Diagram (identifying cyclic nodes):**
```
Graph:
  0 -> 1 -> 2 -> 5
       ^    |
       |    v
       +--- 3 -> 4

Kahn's processes: 0 (in-deg 0)
After processing 0: inDeg[1]=2->1 (still not 0)
Queue empty.

Processed: {0}     count=1 < V=6
Stuck: {1, 2, 3, 4, 5}

Directly in cycle: {1, 2, 3}  (1->2->3->1)
Downstream of cycle: {4, 5}   (reachable from cycle but not in it)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same as standard Kahn's |
| Space  | O(V + E) | Plus a set for tracking processed nodes |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** Kahn's processes each node at most once (enqueue/dequeue) and each edge at most once (decrement in-degree). That's it -- no repeated work.

**Kahn's vs DFS for cycle detection:** Both are O(V + E). Kahn's advantage: the cycle check is a single integer comparison (`count < V`). DFS needs 3-color state management. Kahn's disadvantage: it cannot easily identify a specific cycle path (just which nodes are stuck).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| No edges | `false` | All nodes have in-degree 0; all get processed |
| Self-loop (node -> itself) | `true` | In-degree of that node is 1; it never reaches 0 |
| Two-node cycle (A -> B -> A) | `true` | Both have in-degree 1; neither reaches 0 |
| Disconnected graph with one cyclic component | `true` | Acyclic components process fine; cyclic ones get stuck |
| Single node, no edges | `false` | In-degree 0, processed immediately |

**Common Mistakes:**
- Forgetting that a self-loop creates in-degree 1 (counts as a cycle)
- Assuming all unprocessed nodes are directly in cycles (some are just downstream)
- Using Kahn's for undirected graphs (Kahn's is for directed graphs only; undirected cycle detection uses different methods)
- Not initializing in-degree correctly when there are nodes with no incoming edges

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Why Kahn's over DFS?" | Kahn's: simpler cycle detection (count check). DFS: can trace the actual cycle path. |
| "Can Kahn's find the actual cycle path?" | Not directly. It tells you WHICH nodes are stuck, but not the exact cycle. Use DFS for that. |
| "What about undirected graphs?" | Use Union-Find or DFS with parent tracking. Kahn's in-degree approach is for directed graphs only. |
| "What's the relationship to Course Schedule?" | LC #207 is exactly this problem. Can you finish all courses = is the prerequisite graph acyclic? |
| "What if I need to handle dynamic edges?" | For online cycle detection, maintain in-degrees incrementally. Or use DFS with re-checking. |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Course Schedule (LC #207) | Exactly this problem: cycle in prerequisite graph = can't finish |
| Course Schedule II (LC #210) | Topo sort if no cycle; empty if cycle |
| Topological Sort (Kahn's BFS) | Same algorithm; cycle detection is a free byproduct |
| Detect Cycle in Directed Graph (DFS) | Alternative approach using 3-color DFS |
| Detect Cycle in Undirected Graph | Different problem -- use Union-Find or DFS with parent |
| Deadlock Detection | Real-world application: resource dependency cycles |
