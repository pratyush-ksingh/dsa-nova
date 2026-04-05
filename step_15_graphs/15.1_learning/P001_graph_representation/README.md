# Graph Representation

> **Batch 1 of 12** | **Topic:** Graphs | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement a graph using two standard representations:
1. **Adjacency Matrix** -- a 2D array where `matrix[i][j] = 1` if edge exists between node `i` and node `j`.
2. **Adjacency List** -- an array of lists where `adj[i]` contains all neighbors of node `i`.

Given `n` nodes (0-indexed) and a list of edges, build both representations. Also implement conversion between them.

**Constraints:**
- `1 <= n <= 1000`
- `0 <= edges.length <= n*(n-1)/2`
- Edges are undirected (bidirectional)
- No self-loops or duplicate edges

**Examples:**

| Input | Adjacency Matrix | Adjacency List |
|-------|-----------------|----------------|
| `n=4, edges=[[0,1],[1,2],[2,3]]` | `[[0,1,0,0],[1,0,1,0],[0,1,0,1],[0,0,1,0]]` | `{0:[1], 1:[0,2], 2:[1,3], 3:[2]}` |
| `n=3, edges=[[0,1],[0,2],[1,2]]` | `[[0,1,1],[1,0,1],[1,1,0]]` | `{0:[1,2], 1:[0,2], 2:[0,1]}` |

### Real-Life Analogy
> *Think of a city road map. The **adjacency matrix** is like a giant spreadsheet where every pair of intersections has a cell saying "road exists" or "no road." The **adjacency list** is like giving each intersection a sticky note listing only the intersections you can drive to directly. The spreadsheet is easy to look up any pair instantly, but wastes paper on empty cells. The sticky notes use less paper but you have to scan a list to check a specific connection.*

### Key Observations
1. An adjacency matrix uses O(V^2) space regardless of edge count -- wasteful for sparse graphs but gives O(1) edge lookup.
2. An adjacency list uses O(V + E) space -- efficient for sparse graphs, which most real-world graphs are.
3. For undirected graphs, both representations are symmetric: if `u->v` exists, `v->u` must too. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Adjacency Matrix** -- Use when you need O(1) edge existence checks or the graph is dense (E close to V^2). Example: Floyd-Warshall algorithm.
- **Adjacency List** -- Use for most graph problems (BFS, DFS, Dijkstra) because traversing neighbors is O(degree) instead of O(V).

### Pattern Recognition
- **Pattern:** Graph Construction
- **Classification Cue:** "When a problem says _graph_ or gives _edges/connections_ --> your first step is always _build a representation_. Default to adjacency list unless the problem demands matrix operations."

---

## APPROACH LADDER

### Approach 1: Adjacency Matrix
**Idea:** Create an `n x n` matrix initialized to 0. For each edge `(u, v)`, set `matrix[u][v] = 1` and `matrix[v][u] = 1`.

**Steps:**
1. Create `n x n` matrix filled with 0s.
2. For each edge `(u, v)`:
   - Set `matrix[u][v] = 1`
   - Set `matrix[v][u] = 1` (undirected)
3. To check if edge exists: return `matrix[u][v]`.

**Dry Run:** `n=4, edges=[[0,1],[1,2],[2,3]]`

| Step | Edge | Action | Matrix State (row 0-3) |
|------|------|--------|----------------------|
| Init | - | Create 4x4 zeros | `[0,0,0,0]` for all rows |
| 1 | (0,1) | Set [0][1]=1, [1][0]=1 | Row 0: `[0,1,0,0]`, Row 1: `[1,0,0,0]` |
| 2 | (1,2) | Set [1][2]=1, [2][1]=1 | Row 1: `[1,0,1,0]`, Row 2: `[0,1,0,0]` |
| 3 | (2,3) | Set [2][3]=1, [3][2]=1 | Row 2: `[0,1,0,1]`, Row 3: `[0,0,1,0]` |

| Time | Space |
|------|-------|
| O(V^2 + E) to build | O(V^2) |

**Why we move on:** The O(V^2) space is wasteful for sparse graphs. A graph with 10,000 nodes and 20 edges still needs a 10,000 x 10,000 matrix.

### Approach 2: Adjacency List
**What changed:** Instead of a full matrix, each node stores only its actual neighbors.

**Steps:**
1. Create an array of `n` empty lists.
2. For each edge `(u, v)`:
   - Append `v` to `adj[u]`
   - Append `u` to `adj[v]` (undirected)
3. To check if edge exists: scan `adj[u]` for `v` -- O(degree(u)).

**Dry Run:** `n=4, edges=[[0,1],[1,2],[2,3]]`

| Step | Edge | adj[0] | adj[1] | adj[2] | adj[3] |
|------|------|--------|--------|--------|--------|
| Init | - | [] | [] | [] | [] |
| 1 | (0,1) | [1] | [0] | [] | [] |
| 2 | (1,2) | [1] | [0,2] | [1] | [] |
| 3 | (2,3) | [1] | [0,2] | [1,3] | [2] |

| Time | Space |
|------|-------|
| O(V + E) to build | O(V + E) |

### Approach 3: Conversion Between Representations
**Matrix to List:** For each row `i`, scan columns. If `matrix[i][j] == 1`, add `j` to `adj[i]`.
**List to Matrix:** For each node `i`, for each neighbor `j` in `adj[i]`, set `matrix[i][j] = 1`.

| Time | Space |
|------|-------|
| O(V^2) for matrix->list | O(V + E) or O(V^2) for output |

---

## COMPLEXITY -- INTUITIVELY
**Adjacency Matrix:** "We allocate V^2 cells and fill E of them. Edge lookup is O(1) -- just check one cell. Neighbor traversal is O(V) -- must scan an entire row."
**Adjacency List:** "We allocate V lists totaling 2E entries (each edge stored twice). Neighbor traversal is O(degree) -- only visit actual neighbors. Edge lookup is O(degree) -- must scan a list."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to add both directions for undirected graphs -- `adj[u].add(v)` but missing `adj[v].add(u)`.
2. Off-by-one errors when nodes are 1-indexed vs 0-indexed.
3. Using adjacency matrix for large sparse graphs -- memory limit exceeded.

### Edge Cases to Test
- [ ] Graph with no edges (isolated nodes)
- [ ] Complete graph (every pair connected)
- [ ] Single node graph (`n=1, edges=[]`)
- [ ] Two nodes, one edge

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is the graph directed or undirected? Are nodes 0-indexed or 1-indexed? What's the expected node count?"
2. **Approach:** "I'll explain both representations with trade-offs, then implement adjacency list since it's the default for most graph algorithms."
3. **Code:** Build the adjacency list, demonstrate neighbor traversal.
4. **Trade-off:** "Matrix gives O(1) edge lookup but O(V^2) space. List gives O(V+E) space but O(degree) edge lookup. For interviews, default to list."

### Follow-Up Questions
- "When would you prefer a matrix?" --> Dense graphs, Floyd-Warshall, or when you need O(1) edge existence checks.
- "How would you handle weighted graphs?" --> Matrix: store weight instead of 1. List: store (neighbor, weight) pairs.
- "How about directed graphs?" --> Only add one direction per edge instead of both.

---

## CONNECTIONS
- **Prerequisite:** Arrays, Lists, HashMap basics
- **Same Pattern:** Any graph problem starts with building a representation
- **This Unlocks:** BFS Traversal (P003), DFS Traversal, Connected Components (P002), all graph algorithms
- **Harder Variant:** Weighted graphs, directed graphs, graph with edge metadata
