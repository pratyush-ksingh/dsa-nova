# DFS Traversal

> **Batch 2 of 12** | **Topic:** Graphs | **Difficulty:** Easy | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an undirected graph represented as an adjacency list, perform a **Depth-First Search** starting from node `0`. Return all nodes in the order they are visited. The graph may be **disconnected** -- ensure every node is visited.

### Real-World Analogy
Think of exploring a maze. You pick one corridor and walk as deep as you can until you hit a dead end. Then you backtrack to the last junction and try the next unexplored corridor. DFS is exactly this: go deep first, backtrack, explore siblings later. For disconnected graphs, imagine multiple separate mazes -- once you finish one, you teleport to the entrance of the next unvisited maze.

### Three Key Observations

1. **"Go deep before going wide"** -- DFS explores one branch fully before moving to siblings. This naturally maps to a **stack** (explicit or via recursion).
   - *Aha:* The call stack during recursion IS the DFS stack -- recursion and DFS are deeply intertwined.

2. **A `visited` set prevents infinite loops** -- Graphs can have cycles unlike trees. Without marking nodes visited, you'd loop forever on `A -> B -> A -> B -> ...`.
   - *Aha:* This is the critical difference between DFS on a tree vs. DFS on a graph.

3. **Disconnected components need an outer loop** -- A single DFS from node 0 only reaches its connected component. To visit ALL nodes, loop through every node and start DFS from any unvisited one.
   - *Aha:* The outer loop is what makes this a "complete graph traversal" vs. "single-component DFS."

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Traversal order | DFS (depth-first) | Problem literally asks for DFS |
| Cycle handling | `visited[]` boolean array | O(1) lookup, prevents revisiting |
| Stack mechanism | Recursion (implicit stack) OR explicit stack | Both are O(V + E) |
| Graph representation | Adjacency list | Efficient for sparse graphs, standard input format |

---

## APPROACH LADDER

### Approach 1: Recursive DFS (Standard)

**Intuition:** Use recursion as a natural stack. For each node, mark it visited, add to result, then recurse on all unvisited neighbors.

**Steps:**
1. Create a `visited[]` array of size V, initialized to `false`
2. Create a `result` list
3. For each node `i` from `0` to `V-1`:
   - If `i` is not visited, call `dfs(i)`
4. In `dfs(node)`:
   - Mark `node` as visited
   - Append `node` to result
   - For each neighbor of `node`: if not visited, recurse

**ASCII Graph Example:**
```
    0 --- 1       3
    |     |       |
    2     4       5

Adjacency List:
0: [1, 2]
1: [0, 4]
2: [0]
3: [5]
4: [1]
5: [3]
```

**Dry-Run Trace:**
```
Start outer loop:
  i=0 not visited -> dfs(0)
    visit 0, result=[0]
    neighbor 1 not visited -> dfs(1)
      visit 1, result=[0,1]
      neighbor 0 visited, skip
      neighbor 4 not visited -> dfs(4)
        visit 4, result=[0,1,4]
        neighbor 1 visited, skip
        return
      return
    neighbor 2 not visited -> dfs(2)
      visit 2, result=[0,1,4,2]
      neighbor 0 visited, skip
      return
    return
  i=1 visited, skip
  i=2 visited, skip
  i=3 not visited -> dfs(3)     <-- disconnected component!
    visit 3, result=[0,1,4,2,3]
    neighbor 5 not visited -> dfs(5)
      visit 5, result=[0,1,4,2,3,5]
      neighbor 3 visited, skip
      return
    return
  i=4 visited, skip
  i=5 visited, skip

Final: [0, 1, 4, 2, 3, 5]
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Visit every vertex once, examine every edge once |
| Space  | O(V) | Visited array + recursion stack (worst case: V deep for a chain) |

---

### Approach 2: Iterative DFS (Explicit Stack)

**Intuition:** Replace recursion with an explicit stack. This avoids stack overflow for very deep graphs (V > 10,000).

**Steps:**
1. Create `visited[]` and `result` list
2. For each unvisited node `i`, push it onto a stack
3. While stack is not empty:
   - Pop node, if already visited skip, else mark visited and add to result
   - Push all unvisited neighbors onto stack

**BUD Analysis:**
- **B**ottleneck: Both approaches visit every edge and vertex once -- O(V+E) is optimal
- **U**nnecessary work: None; each node/edge processed exactly once
- **D**uplicate work: The explicit stack may push the same node multiple times (check visited on pop), but this doesn't change asymptotic complexity

> **Note:** Iterative DFS may produce a slightly different order than recursive DFS due to stack ordering (neighbors are pushed in reverse order). Both are valid DFS orderings.

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same as recursive |
| Space  | O(V) | Explicit stack replaces call stack |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** Imagine coloring nodes as you visit them. You touch each node exactly once (that's V). For each node, you scan its neighbor list -- across all nodes, the total neighbor-list length is 2E (each edge counted from both endpoints). So total work = V + 2E = O(V + E).

**Why not better?** You must visit every node at least once (lower bound is V). You must examine every edge to discover neighbors (lower bound is E). So O(V + E) is provably optimal.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out For |
|---|---|---|
| Single node, no edges | Return `[0]` | Don't return empty |
| Fully disconnected (no edges) | Return `[0, 1, 2, ..., V-1]` | Outer loop handles it |
| Complete graph (all connected) | DFS visits all from node 0 | Performance is fine: O(V^2) edges |
| Self-loop | Visited check prevents infinite loop | Make sure visited is checked |
| Very deep chain graph | Recursive DFS may stack overflow | Use iterative for V > ~10K |

**Common Mistakes:**
- Forgetting the outer loop (misses disconnected components)
- Not checking visited before recursing (infinite loop on cycles)
- Confusing adjacency matrix with adjacency list

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Can you do it iteratively?" | Yes, replace call stack with explicit stack (Approach 2) |
| "How do you handle disconnected graphs?" | Outer loop from 0 to V-1, start DFS from each unvisited node |
| "DFS vs BFS -- when to use which?" | DFS for path finding, cycle detection, topological sort. BFS for shortest path in unweighted graphs. |
| "What if the graph is directed?" | Same algorithm; just follow directed edges. Visited check still needed for cycles. |
| "Time complexity for adjacency matrix?" | O(V^2) instead of O(V+E) because scanning neighbors of one node is O(V) |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| BFS Traversal | Same goal, different order (level-by-level vs. deep-first) |
| Number of Connected Components | Count how many times the outer loop starts a new DFS |
| Cycle Detection | DFS + track "in-progress" nodes (gray/black coloring) |
| Topological Sort | DFS + post-order (push to stack after all neighbors done) |
| Path Finding | DFS naturally finds A path (not shortest) between two nodes |
