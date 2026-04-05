# Detect Cycle in Undirected Graph using BFS

> **Step 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an undirected graph with `V` vertices (numbered 0 to V-1) and `E` edges represented as an adjacency list, detect whether the graph contains a cycle or not. The graph may be disconnected.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| V=4, adj = [[1],[0,2,3],[1,3],[1,2]] | true | Cycle: 1-2-3-1 |
| V=3, adj = [[1,2],[0],[0]] | false | Tree structure, no cycle |
| V=5, adj = [[1,2],[0,2],[0,1],[4],[3]] | true | Cycle 0-1-2-0 in first component |
| V=1, adj = [[]] | false | Single node, no cycle possible |

## Constraints

- 1 <= V <= 10^5
- 0 <= E <= 10^5
- Graph is undirected (edge appears in both adjacency lists)
- No self-loops or multiple edges between same pair

---

## Approach 1: Brute Force (BFS back-edge check)

**Intuition:** In an undirected graph, a cycle exists if during BFS traversal we encounter a visited node that is NOT the parent of the current node. This "back edge" means there is an alternate path to reach that node, forming a cycle. We check all edges systematically through BFS from each unvisited component.

**Steps:**
1. Initialize a `visited[]` array of size V, all false
2. For each unvisited node, start a BFS storing `(node, parent)` pairs
3. For each neighbor of the current node:
   - If not visited: mark visited, enqueue with current node as parent
   - If visited AND not parent: cycle detected, return true
4. If BFS completes for all components without finding a cycle, return false

**Dry-Run Trace:**
```
V=3, adj = [[1,2], [0,2], [0,1]]

BFS from node 0 (parent=-1):
  visited=[T,F,F], queue=[(0,-1)]
  Process (0,-1): neighbors 1,2
    1 not visited -> visited=[T,T,F], queue=[(1,0),(2,0)]
    2 not visited -> visited=[T,T,T], queue=[(1,0),(2,0)]
  Process (1,0): neighbors 0,2
    0 visited, parent=0 -> skip (it IS parent)
    2 visited, parent=0, neighbor=2 != 0 -> CYCLE FOUND!
Return true
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each vertex and edge visited once in BFS |
| Space  | O(V + E) | Queue + visited array + adjacency list storage |

---

## Approach 2: Optimal (BFS with parent tracking - modular)

**Intuition:** Same BFS parent-tracking logic but organized into a clean helper function per component. For each connected component, run a dedicated BFS that returns true if a cycle is found within that component. The parent check distinguishes the trivial "going back to where we came from" from a genuine cycle.

**Key Insight:** In an undirected graph, every edge (u,v) appears twice in the adjacency list. When BFS reaches node `u` from parent `p`, seeing `p` in the adjacency list is expected. But seeing ANY OTHER already-visited node means we found a cycle.

**Steps:**
1. Initialize `visited[]` array
2. For each unvisited vertex i, call `bfs_check(i)`
3. `bfs_check(src)`:
   - BFS with `(node, parent)` pairs
   - If visited neighbor != parent -> return true (cycle)
   - Return false if BFS completes normally
4. If any component has a cycle, return true

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Standard BFS complexity |
| Space  | O(V) | Visited array + queue (at most V nodes) |

---

## Approach 3: Best (BFS with parent array)

**Intuition:** Instead of storing parent in the queue alongside each node, maintain a separate `parent[]` array. This uses the same logical approach but separates concerns: the queue holds only nodes, while the parent array tracks the BFS tree structure. This is cleaner and avoids tuple packing overhead.

**Steps:**
1. Initialize `visited[]` and `parent[]` arrays (parent initialized to -1)
2. For each unvisited node, start BFS
3. When visiting neighbor: set `parent[neighbor] = node`
4. If neighbor is visited and `parent[node] != neighbor` -> cycle found

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same BFS traversal |
| Space  | O(V) | Two arrays of size V + queue |

---

## Real-World Use Case

**Network Loop Detection:** In computer networks, switches use Spanning Tree Protocol (STP) to detect loops in Ethernet topologies. A cycle in the network can cause broadcast storms where packets loop indefinitely, crashing the network. BFS-based cycle detection mirrors how STP identifies and breaks loops to maintain a tree topology.

## Interview Tips

- Clarify whether the graph is directed or undirected -- cycle detection differs significantly between the two
- The parent check is the KEY insight: in undirected graphs, every edge creates a "back reference" to the parent, which is NOT a cycle
- Handle disconnected graphs by iterating over all vertices in the outer loop
- BFS is preferred over DFS here when the interviewer specifically asks for BFS; both give O(V+E)
- Common mistake: forgetting to handle disconnected components (missing the outer for loop)
- Follow-up: if asked to FIND the cycle (not just detect), you need to track parents and backtrack from the collision point
