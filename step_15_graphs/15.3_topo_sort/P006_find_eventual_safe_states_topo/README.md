# Find Eventual Safe States

> **Step 15.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

**(LeetCode #802)** There is a directed graph of `n` nodes with each node labeled from `0` to `n - 1`. The graph is represented by a 0-indexed 2D integer array `graph` where `graph[i]` is an integer array of nodes adjacent to node `i`, meaning there is an edge from node `i` to each node in `graph[i]`.

A node is a **terminal node** if there are no outgoing edges. A node is a **safe node** if every possible path starting from that node leads to a terminal node (or another safe node).

Return an array containing all the safe nodes of the graph in ascending order.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| graph = [[1,2],[2,3],[5],[0],[5],[],[]] | [2,4,5,6] | Nodes 5,6 are terminal. Node 2->5 (safe). Node 4->5 (safe). Nodes 0,1,3 form/reach a cycle. |
| graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]] | [4] | Only node 4 is terminal and safe. All others can reach the cycle 0->3->0. |
| graph = [[],[],[]] | [0,1,2] | All nodes are terminal, hence safe. |
| graph = [[1],[2],[0]] | [] | All nodes are in a cycle, none are safe. |

## Constraints

- n == graph.length
- 1 <= n <= 10^4
- 0 <= graph[i].length <= n
- 0 <= graph[i][j] <= n - 1
- graph[i] is sorted in strictly increasing order
- The graph may contain self-loops

---

## Approach 1: Brute Force (DFS from each node)

**Intuition:** For each node, explore all possible paths using DFS. If any path leads to a cycle (revisits a node currently on the DFS stack), that starting node is unsafe. If all paths lead to terminal nodes, it is safe. We memoize results to avoid redundant work: once a node is determined safe or unsafe, we reuse that result.

**Steps:**
1. Initialize `state[]` array: 0=unvisited, 1=in-progress, 2=safe, 3=unsafe
2. For each node, call `isSafe(node)`
3. `isSafe(node)`:
   - If already determined (state 2 or 3), return cached result
   - Mark as in-progress (state 1)
   - For each neighbor: if `isSafe(neighbor)` returns false, mark unsafe
   - If all neighbors safe, mark safe
4. Collect all nodes with state = safe

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V * (V + E)) worst case without memo, O(V + E) with memo | Each node explored once with memoization |
| Space  | O(V) | State array + recursion stack |

---

## Approach 2: Optimal (DFS with 3-state coloring)

**Intuition:** Classic DFS cycle detection adapted for safety checking. Use white(0)/gray(1)/black(2) coloring:
- **White:** Not yet visited
- **Gray:** Currently on the DFS stack (being explored)
- **Black:** Fully processed and confirmed safe

If during DFS we encounter a gray node, we have found a cycle -- the current node is unsafe. If DFS completes without hitting any gray node, the node is safe (all paths lead to terminal/safe nodes). Each node is processed exactly once.

**Key Insight:** A node is safe if and only if it is NOT part of a cycle and does not lead to any cycle. The coloring scheme naturally handles this: gray nodes detect cycles, and we only mark a node black (safe) after confirming all its descendants are safe.

**Steps:**
1. Initialize `color[]` to all white (0)
2. For each node i from 0 to n-1:
   - Call `dfs(i)` which returns true if i is safe
3. `dfs(node)`:
   - If not white: return whether it is black (safe)
   - Mark gray
   - For each neighbor: if gray -> return false; if white -> recurse
   - Mark black, return true
4. Collect nodes where dfs returned true

**Dry-Run Trace:**
```
graph = [[1,2],[2,3],[5],[0],[5],[],[]]

dfs(0): color[0]=gray
  dfs(1): color[1]=gray
    dfs(2): color[2]=gray
      dfs(5): color[5]=gray, no neighbors -> color[5]=black, return true
    color[2]=black, return true (safe)
    dfs(3): color[3]=gray
      neighbor 0: gray! -> return false (cycle!)
    return false (1 is not safe)
  return false (0 is not safe)

dfs(4): color[4]=gray
  neighbor 5: black -> skip
  color[4]=black, return true (safe)

Results: 0=unsafe, 1=unsafe, 2=safe, 3=unsafe, 4=safe, 5=safe, 6=safe
Answer: [2, 4, 5, 6]
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Each node colored at most once; each edge visited once |
| Space  | O(V) | Color array + recursion stack |

---

## Approach 3: Best (Reverse Graph + Kahn's Algorithm)

**Intuition:** Think of it from the perspective of terminal nodes outward. Terminal nodes (outdegree 0) are trivially safe. A non-terminal node is safe if ALL its outgoing edges lead to safe nodes. This is essentially "peeling" safe nodes from the outside in -- exactly what Kahn's algorithm does on a reversed graph.

**The Trick:** Reverse all edges. In the reversed graph, terminal nodes (which had outdegree 0) now have indegree 0. Run Kahn's algorithm on the reversed graph, but instead of indegree, we track outdegree of the original graph. When all outgoing edges of a node lead to safe nodes, that node becomes safe too.

**Why it works:**
- Terminal nodes are safe (base case)
- A node is safe if all successors are safe
- By "removing" safe nodes (decrementing outdegree of predecessors), nodes whose outdegree drops to 0 have all successors safe -> they are safe too
- Cycle nodes never reach outdegree 0 because they always point to another cycle node

**Steps:**
1. Build reverse adjacency list and compute `outdegree[]` for original graph
2. Enqueue all nodes with outdegree 0 (terminal nodes)
3. BFS: process each node, mark as safe
   - For each predecessor (in reverse graph): decrement outdegree
   - If outdegree reaches 0: enqueue (all its paths lead to safe nodes)
4. Return sorted list of safe nodes

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Build reverse graph + Kahn's traversal |
| Space  | O(V + E) | Reverse adjacency list + outdegree array + queue |

---

## Real-World Use Case

**Garbage Collection (Mark-and-Sweep):** In programming language runtimes, objects reference other objects forming a directed graph. An object is "safe" to keep if it is reachable from a root reference; otherwise it is garbage. The safe states problem is analogous: determine which nodes (objects) are "eventually safe" (reachable from roots) versus those trapped in reference cycles (memory leaks). Modern GCs use graph traversal techniques similar to DFS coloring to identify and collect cyclic garbage.

## Interview Tips

- Recognize this as a "cycle reachability" problem: a node is safe if and only if it cannot reach any cycle
- The DFS coloring approach is the most common interview solution -- practice the white/gray/black pattern
- The Kahn's approach shows deeper understanding and impresses interviewers; mention it as an alternative
- Clarify: "safe" means ALL paths lead to terminal nodes, not just one path. A node with even one path to a cycle is unsafe
- The problem says output must be sorted -- both DFS (iterate 0 to n-1) and Kahn's (filter 0 to n-1) naturally produce sorted output
- Common mistake: confusing this with "find nodes NOT in a cycle" -- a node outside a cycle but pointing to one is also unsafe
- Self-loops: a node with a self-loop is always unsafe (it is a cycle of length 1)
