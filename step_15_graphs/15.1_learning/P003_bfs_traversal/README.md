# BFS Traversal

> **Batch 1 of 12** | **Topic:** Graphs | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an undirected graph with `V` vertices (0-indexed) and `E` edges represented as an adjacency list, and a source vertex `src`, perform a **Breadth-First Search** (BFS) traversal starting from `src`. Return the list of vertices in the order they are visited.

**Constraints:**
- `1 <= V <= 10^4`
- `0 <= E <= V*(V-1)/2`
- `0 <= src < V`
- Graph may be disconnected (for basic BFS, we only traverse the component containing `src`)

**Examples:**

| Input | Source | Output | Explanation |
|-------|--------|--------|-------------|
| `V=5, adj={0:[1,2], 1:[0,3], 2:[0,4], 3:[1], 4:[2]}` | `0` | `[0,1,2,3,4]` | Level 0: {0}, Level 1: {1,2}, Level 2: {3,4} |
| `V=4, adj={0:[1,2], 1:[0], 2:[0,3], 3:[2]}` | `0` | `[0,1,2,3]` | Visit 0, then neighbors 1,2, then 3 |
| `V=3, adj={0:[], 1:[2], 2:[1]}` | `0` | `[0]` | Node 0 is isolated |

### Real-Life Analogy
> *Imagine you drop a pebble into a pond. Ripples spread outward in concentric circles. BFS works the same way: starting from a source node, you visit all immediate neighbors first (first ripple), then all neighbors-of-neighbors (second ripple), and so on. You never jump to a distant node before visiting all closer ones. This "level-by-level" expansion is why BFS naturally finds shortest paths in unweighted graphs.*

### Key Observations
1. BFS uses a **queue** (FIFO) -- this ensures we process nodes in the order they were discovered, giving us level-by-level traversal.
2. We must track visited nodes to avoid revisiting and infinite loops in cyclic graphs.
3. Mark a node as visited **when you add it to the queue**, not when you process it. <-- This is the "aha" insight (prevents duplicates in the queue)

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Queue:** BFS is defined by FIFO processing. The queue holds nodes discovered but not yet explored. This guarantees level-order traversal.
- **Visited array:** Prevents revisiting nodes (critical for cyclic graphs).
- **Adjacency List:** Efficient neighbor access in O(degree).

### Pattern Recognition
- **Pattern:** BFS (Level-order Graph Traversal)
- **Classification Cue:** "When you see _shortest path in unweighted graph_, _level-by-level_, _minimum steps/moves_ --> think BFS"

---

## APPROACH LADDER

### Approach 1: Iterative BFS with Queue (Standard)
**Idea:** Use a queue. Enqueue source, mark visited. Dequeue a node, add it to result, enqueue all unvisited neighbors.

**Steps:**
1. Create a `visited` array of size `V`, all false.
2. Create an empty queue, enqueue `src`, mark `visited[src] = true`.
3. Create result list.
4. While queue is not empty:
   - Dequeue `node`.
   - Add `node` to result.
   - For each `neighbor` of `node`:
     - If not visited: mark visited, enqueue.
5. Return result.

**Dry Run:** `adj = {0:[1,2], 1:[0,3], 2:[0,4], 3:[1], 4:[2]}`, source = 0

| Step | Queue (front..back) | Dequeue | Result | Enqueue |
|------|---------------------|---------|--------|---------|
| Init | [0] | - | [] | 0 (mark visited) |
| 1 | [] | 0 | [0] | 1, 2 (mark visited) |
| 2 | [2] | 1 | [0,1] | 3 (mark visited; 0 already visited) |
| 3 | [3] | 2 | [0,1,2] | 4 (mark visited; 0 already visited) |
| 4 | [4] | 3 | [0,1,2,3] | (1 already visited) |
| 5 | [] | 4 | [0,1,2,3,4] | (2 already visited) |

**Result:** `[0, 1, 2, 3, 4]`

| Time | Space |
|------|-------|
| O(V + E) | O(V) for queue + visited |

*This is already optimal.* Every vertex and edge is processed exactly once. BFS has no brute-force vs optimal distinction -- there is one canonical way to do it.

### Approach 2: BFS for Disconnected Graph (All Components)
**What changed:** Wrap BFS in a loop over all vertices to handle disconnected graphs.

**Steps:**
1. For each node `i` from 0 to V-1:
   - If not visited, run BFS from `i`.
2. This ensures all components are traversed.

| Time | Space |
|------|-------|
| O(V + E) | O(V) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(V + E) -- "Each vertex enters and exits the queue exactly once (O(V)). For each vertex, we scan its adjacency list (total edges scanned = 2E for undirected). So total = V + E."
**Space:** O(V) -- "The queue can hold at most V nodes (imagine a star graph where all V-1 leaves are enqueued at once). The visited array is also V."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Marking visited when dequeuing instead of enqueuing** -- causes the same node to be added to the queue multiple times. Always mark visited at enqueue time.
2. Forgetting to handle disconnected graphs -- BFS from one source only visits its component.
3. Not handling the case where `src` is out of bounds.

### Edge Cases to Test
- [ ] Single node graph -- return `[0]`
- [ ] Disconnected graph -- BFS from source only returns reachable nodes
- [ ] Complete graph -- all nodes at distance 1 from source
- [ ] Linear graph (chain) -- BFS visits one node per level
- [ ] Star graph -- all leaves enqueued in one step

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is the graph connected? Which vertex to start from? Should I return all components or just one?"
2. **Approach:** "BFS with a queue. Mark visited on enqueue, not dequeue. Process level by level."
3. **Code:** Adjacency list + queue + visited array.
4. **Test:** Trace through the example showing queue state at each step.

### Follow-Up Questions
- "How do you find the shortest path with BFS?" --> Track distances: `dist[neighbor] = dist[node] + 1`.
- "BFS vs DFS?" --> BFS finds shortest path in unweighted graphs, uses more memory (queue can be wide). DFS uses less memory for deep graphs, does not guarantee shortest path.
- "Can BFS detect cycles?" --> Yes, if you encounter a visited node that is not the parent of the current node.

---

## CONNECTIONS
- **Prerequisite:** Graph Representation (P001), Queue data structure
- **Same Pattern:** Level-order traversal of a tree (BFS on a tree)
- **This Unlocks:** Shortest path in unweighted graph, Flood Fill (P003 in 15.2), Rotten Oranges, 0-1 Matrix
- **Harder Variant:** Multi-source BFS, 0-1 BFS (deque), Bidirectional BFS
