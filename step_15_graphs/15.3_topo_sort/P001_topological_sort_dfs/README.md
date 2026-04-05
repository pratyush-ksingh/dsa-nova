# Topological Sort (DFS)

> **Batch 2 of 12** | **Topic:** Graphs | **Difficulty:** Medium | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **Directed Acyclic Graph (DAG)** with `V` vertices and `E` edges, return a **topological ordering** of the vertices. In a topological ordering, for every directed edge `u -> v`, vertex `u` appears before vertex `v`. If the graph contains a cycle, detect it and report that no valid ordering exists.

### Real-World Analogy
Think of university course prerequisites. "Data Structures" must come before "Algorithms," and "Algorithms" before "Machine Learning." A topological sort gives you a valid semester-by-semester plan where every prerequisite is satisfied. If there's a circular dependency (A requires B, B requires C, C requires A), no valid schedule exists -- that's a cycle.

### Three Key Observations

1. **Post-order DFS naturally produces reverse topological order** -- When DFS finishes processing all descendants of a node (returns from recursion), that node can safely go to the "end" of the ordering. Reversing this post-order gives topological order.
   - *Aha:* Push to stack AFTER all neighbors are processed (post-order), then pop the stack for the answer.

2. **Cycle detection requires tracking "in-progress" nodes** -- A simple `visited` boolean is not enough. We need three states: UNVISITED, IN_PROGRESS (on current DFS path), VISITED (fully processed). A back-edge to an IN_PROGRESS node means cycle.
   - *Aha:* This is the "gray/black" coloring scheme. White = unvisited, Gray = in-progress, Black = done.

3. **Multiple valid topological orders may exist** -- For the same DAG, different DFS starting points or neighbor orderings produce different valid sorts. All are correct.
   - *Aha:* The problem asks for ANY valid ordering, not a unique one.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Algorithm | DFS + post-order stack | Natural fit: finish descendants first, then push self |
| Cycle detection | 3-state coloring (0/1/2) | Detects back-edges to in-progress ancestors |
| Output structure | Stack (or reversed list) | Post-order reversal gives topological order |
| Alternative | Kahn's Algorithm (BFS) | Uses in-degree; equally valid but different pattern |

---

## APPROACH LADDER

### Approach 1: DFS with Post-Order Stack (Standard)

**Intuition:** Run DFS. When a node has NO more unvisited neighbors (all descendants processed), push it onto a stack. At the end, popping the stack gives topological order.

**Why does this work?** If there's an edge `u -> v`, DFS will finish `v` before `u` (v is pushed to stack first). So when we pop, `u` comes out before `v`. This is exactly the topological ordering requirement.

**Steps:**
1. Create `visited[]` array (0 = unvisited, 1 = in-progress, 2 = done)
2. Create an empty stack for the result
3. For each unvisited node `i`, call `dfs(i)`
4. In `dfs(node)`:
   - Mark `node` as in-progress (1)
   - For each neighbor `v` of `node`:
     - If `v` is in-progress (1): CYCLE DETECTED -- return false
     - If `v` is unvisited (0): recurse on `v`
   - Mark `node` as done (2)
   - Push `node` onto result stack
5. Pop all from stack to get topological order

**ASCII Graph Example:**
```
    5 --> 0 <-- 4
    |           |
    v           v
    2 --> 3 --> 1

Edges: 5->0, 5->2, 4->0, 4->1, 2->3, 3->1

Adjacency List:
0: []
1: []
2: [3]
3: [1]
4: [0, 1]
5: [0, 2]
```

**Dry-Run Trace:**
```
Stack = [], visited = [0,0,0,0,0,0]

Outer loop: i=0
  dfs(0): mark 0 in-progress [1,0,0,0,0,0]
    no neighbors
    mark 0 done [2,0,0,0,0,0], push 0
    Stack = [0]

i=1: dfs(1): mark 1 in-progress -> no neighbors -> done
    Stack = [0, 1]

i=2: dfs(2): mark 2 in-progress [2,2,1,0,0,0]
    neighbor 3: unvisited -> dfs(3)
      dfs(3): mark 3 in-progress [2,2,1,1,0,0]
        neighbor 1: done (2), skip
        mark 3 done, push 3
        Stack = [0, 1, 3]
    mark 2 done, push 2
    Stack = [0, 1, 3, 2]

i=3: done, skip
i=4: dfs(4): mark 4 in-progress
    neighbor 0: done, skip
    neighbor 1: done, skip
    mark 4 done, push 4
    Stack = [0, 1, 3, 2, 4]

i=5: dfs(5): mark 5 in-progress
    neighbor 0: done, skip
    neighbor 2: done, skip
    mark 5 done, push 5
    Stack = [0, 1, 3, 2, 4, 5]

Pop all: [5, 4, 2, 3, 1, 0]

Verify: 5->0 ✓ (5 before 0)
        5->2 ✓ (5 before 2)
        4->0 ✓ (4 before 0)
        4->1 ✓ (4 before 1)
        2->3 ✓ (2 before 3)
        3->1 ✓ (3 before 1)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Visit each vertex once, examine each edge once |
| Space  | O(V) | Visited array + recursion stack + result stack |

---

### Approach 2: Kahn's Algorithm (BFS / In-Degree)

**Intuition:** Repeatedly remove nodes with in-degree 0 (no prerequisites). These nodes can go first. When removed, decrement in-degree of their neighbors. Repeat.

**Steps:**
1. Compute in-degree for all vertices
2. Enqueue all vertices with in-degree 0
3. While queue not empty:
   - Dequeue node `u`, add to result
   - For each neighbor `v` of `u`: decrement in-degree of `v`
   - If in-degree of `v` becomes 0, enqueue `v`
4. If result size < V: cycle detected

**Cycle Detection:** If a cycle exists, no node in the cycle ever reaches in-degree 0. So the result will have fewer than V nodes.

**BUD Analysis:**
- **B**ottleneck: Both DFS and BFS approaches are O(V + E) -- optimal
- **U**nnecessary: Nothing; both are clean
- **D**uplicate: No redundant work in either approach

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same as DFS approach |
| Space  | O(V) | In-degree array + queue + result |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** Every vertex is processed exactly once (V). For each vertex, we scan its adjacency list -- total edges scanned across all vertices is E. So total = V + E.

**Can we do better?** No. We must examine every edge at least once (to know the dependency structure), and every vertex at least once (to include it in the output). Lower bound is Omega(V + E).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| Single node, no edges | `[0]` | Valid topological order |
| Linear chain: 0->1->2->3 | `[0, 1, 2, 3]` | Only one valid order |
| Graph with cycle | Return empty / indicate cycle | DFS: back-edge to gray node; BFS: result.size() < V |
| Disconnected DAG | Valid order exists | Outer loop handles components |
| Node with no edges | Can appear anywhere | In-degree 0, so it's a valid start |

**Common Mistakes:**
- Using simple `visited` boolean instead of 3-state coloring (misses cycles)
- Pushing to stack BEFORE processing neighbors instead of AFTER (wrong order)
- Forgetting the outer loop (only processes one component)
- Confusing "no valid order" with "multiple valid orders"

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "DFS vs Kahn's -- which to use?" | DFS is more natural if you're comfortable with recursion. Kahn's is more intuitive ("remove nodes with no deps"). Both O(V+E). |
| "How do you detect a cycle?" | DFS: back-edge to in-progress node. Kahn's: result has fewer than V nodes. |
| "Can topological sort work on undirected graphs?" | No. Topological ordering is only defined for directed graphs. |
| "What if multiple valid orderings exist?" | Return any one. For lexicographically smallest, use min-heap in Kahn's. |
| "Real-world applications?" | Build systems (Make), package managers (npm), task scheduling, course planning |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Course Schedule (LC #207) | Cycle detection in a directed graph = is topological sort possible? |
| Course Schedule II (LC #210) | Return the actual topological ordering |
| Alien Dictionary (LC #269) | Build a graph from constraints, then topological sort |
| Kahn's Algorithm (BFS) | Alternative to DFS-based topological sort |
| Detect Cycle in Directed Graph | Subset of topological sort logic |
