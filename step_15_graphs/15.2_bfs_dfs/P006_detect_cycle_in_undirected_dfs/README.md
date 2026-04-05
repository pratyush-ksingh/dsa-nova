# Detect Cycle in Undirected Graph using DFS

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

## Approach 1: Brute Force (BFS with parent tracking)

**Intuition:** Use BFS as the traversal mechanism. During BFS from any node, track the parent (the node that added the current node to the queue). If we encounter a visited neighbor that is not the parent, a cycle exists. This serves as the baseline approach before exploring DFS.

**Steps:**
1. Initialize `visited[]` of size V, all false
2. For each unvisited node, start BFS with `(node, parent)` pairs
3. For each neighbor:
   - Not visited: mark visited, enqueue with current node as parent
   - Visited AND not parent: cycle found
4. Return false if no cycle found across all components

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each vertex and edge visited once |
| Space  | O(V) | Visited array + queue |

---

## Approach 2: Optimal (DFS with parent tracking)

**Intuition:** DFS naturally follows one path deep before backtracking. During DFS from node `u` with parent `p`, if we find a neighbor `v` that is already visited and `v != p`, then there is a back edge forming a cycle. DFS is more natural than BFS for this problem because the recursion stack implicitly tracks the current path.

**Key Insight:** In an undirected graph, when we DFS from `u` to `v`, the edge `(v, u)` will appear in v's adjacency list. We must skip this "parent edge" to avoid false cycle detection.

**Steps:**
1. Initialize `visited[]` of size V
2. For each unvisited vertex, call `dfs(node, parent=-1)`
3. `dfs(node, parent)`:
   - Mark node visited
   - For each neighbor:
     - Not visited: recurse `dfs(neighbor, node)`. If returns true, propagate true
     - Visited AND not parent: return true (cycle!)
   - Return false
4. Return false if no cycle detected

**Dry-Run Trace:**
```
V=3, adj = [[1,2], [0,2], [0,1]]

dfs(0, -1): visited=[T,F,F]
  neighbor 1: not visited -> dfs(1, 0)
    dfs(1, 0): visited=[T,T,F]
      neighbor 0: visited, parent=0 -> skip (IS parent)
      neighbor 2: not visited -> dfs(2, 1)
        dfs(2, 1): visited=[T,T,T]
          neighbor 0: visited, parent=1, 0 != 1 -> CYCLE FOUND!
          return true
        return true
      return true
    return true
  return true

Result: true (cycle exists)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each vertex visited once, each edge checked twice |
| Space  | O(V) | Visited array + recursion stack (worst case V deep) |

---

## Approach 3: Best (Union-Find with path compression)

**Intuition:** A fundamentally different approach. Initialize each node as its own set. Process each edge `(u, v)`: if `u` and `v` are already in the same set, adding this edge creates a cycle. Otherwise, union them. This is elegant and avoids explicit traversal.

**Why it works:** A tree with V vertices has exactly V-1 edges. If we process edges one by one and two endpoints are already connected, that edge would create a cycle.

**Steps:**
1. Initialize `parent[i] = i` and `rank[i] = 0` for all vertices
2. For each edge `(u, v)` where `u < v` (to avoid double-counting undirected edges):
   - `find(u)` and `find(v)` with path compression
   - If same root: cycle detected, return true
   - Otherwise: `union(u, v)` by rank
3. Return false if no cycle found

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E * alpha(V)) ~ O(V + E) | Path compression + union by rank gives near O(1) per op |
| Space  | O(V) | Parent and rank arrays |

---

## Real-World Use Case

**Deadlock Detection in Operating Systems:** In a resource allocation graph, processes and resources are nodes. If a cycle exists, it means a set of processes are waiting for each other in a circular chain -- a deadlock. OS kernels use cycle detection algorithms (essentially DFS with back-edge detection) to identify and resolve deadlocks by terminating one of the involved processes.

## Interview Tips

- Know BOTH BFS and DFS approaches -- interviewers may ask you to solve with a specific traversal
- The parent check is critical: without it, every undirected edge looks like a "cycle" back to the previous node
- Union-Find is a strong alternative that shows breadth of knowledge; mention it even if you implement DFS
- For DFS, watch out for stack overflow on very deep graphs (100K+ nodes in a line) -- iterative DFS fixes this
- Handle disconnected graphs: the outer for-loop over all vertices is essential
- Follow-up: "Can you find the actual cycle?" -- Yes, track the parent chain and backtrack from the collision
- Common mistake: using a global visited set but forgetting that DFS parent differs per recursive call
