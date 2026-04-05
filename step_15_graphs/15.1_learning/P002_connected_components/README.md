# Connected Components

> **Batch 1 of 12** | **Topic:** Graphs | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given `n` nodes labeled from `0` to `n-1` and a list of undirected edges, find the number of connected components in the graph.

A **connected component** is a group of nodes where every node can reach every other node through some path.

*(LeetCode #323 equivalent)*

**Constraints:**
- `1 <= n <= 2000`
- `0 <= edges.length <= 5000`
- `edges[i].length == 2`
- `0 <= edges[i][0], edges[i][1] < n`
- No duplicate edges

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `n=5, edges=[[0,1],[1,2],[3,4]]` | `2` | Component 1: {0,1,2}, Component 2: {3,4} |
| `n=5, edges=[[0,1],[1,2],[2,3],[3,4]]` | `1` | All nodes connected in one chain |
| `n=4, edges=[]` | `4` | Every node is its own component |

### Real-Life Analogy
> *Imagine a social network where each person is a node and friendships are edges. A connected component is a "friend group" -- a cluster of people who are all connected through chains of friendships. If Alice knows Bob and Bob knows Charlie, they are all in the same group even if Alice does not know Charlie directly. The question asks: how many separate friend groups exist?*

### Key Observations
1. A node with no edges forms its own component -- isolated nodes count.
2. We need to "explore" from each unvisited node, marking all reachable nodes as part of the same component.
3. Each time we start a new exploration from an unvisited node, we have found a new component. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Adjacency List** for graph storage -- O(V + E) space, efficient neighbor traversal.
- **Visited array** to track which nodes have been explored.
- **DFS/BFS** to explore all nodes in a component.
- Alternative: **Union-Find** merges components by processing edges directly.

### Pattern Recognition
- **Pattern:** Connected Components via DFS/BFS or Union-Find
- **Classification Cue:** "When you see _how many groups/clusters/islands_ in an undirected graph --> think _count DFS/BFS launches_ or _Union-Find with component counting_"

---

## APPROACH LADDER

### Approach 1: DFS from Each Unvisited Node
**Idea:** Build adjacency list. For each unvisited node, launch a DFS to visit all reachable nodes. Each launch = one new component.

**Steps:**
1. Build adjacency list from edges.
2. Create a `visited` boolean array of size `n`.
3. Initialize `count = 0`.
4. For each node `i` from 0 to n-1:
   - If `visited[i]` is false:
     - Increment `count`.
     - DFS/BFS from `i`, marking all reachable nodes as visited.
5. Return `count`.

**Dry Run:** `n=5, edges=[[0,1],[1,2],[3,4]]`

| Step | Node | visited? | Action | count |
|------|------|----------|--------|-------|
| 1 | 0 | No | DFS: visit 0->1->2 | 1 |
| 2 | 1 | Yes | Skip | 1 |
| 3 | 2 | Yes | Skip | 1 |
| 4 | 3 | No | DFS: visit 3->4 | 2 |
| 5 | 4 | Yes | Skip | 2 |

**Result:** 2

| Time | Space |
|------|-------|
| O(V + E) | O(V + E) |

*This is already optimal.* Each node and edge visited exactly once.

### Approach 2: Union-Find (Disjoint Set Union)
**What changed:** Instead of traversal, we process edges to merge nodes into groups. Count distinct roots at the end.

**Steps:**
1. Initialize each node as its own parent: `parent[i] = i`.
2. Start with `components = n`.
3. For each edge `(u, v)`:
   - Find root of `u` and root of `v`.
   - If different roots, union them and decrement `components`.
4. Return `components`.

**BUD Transition:** Not a complexity improvement (both O(V+E)), but Union-Find is better for **online** edge additions -- no need to rebuild the graph.

**Dry Run:** `n=5, edges=[[0,1],[1,2],[3,4]]`

| Step | Edge | parent[] | components | Action |
|------|------|----------|------------|--------|
| Init | - | [0,1,2,3,4] | 5 | Each node is own root |
| 1 | (0,1) | [0,0,2,3,4] | 4 | Union 0,1 |
| 2 | (1,2) | [0,0,0,3,4] | 3 | Union 1,2 (root 0 absorbs 2) |
| 3 | (3,4) | [0,0,0,3,3] | 2 | Union 3,4 |

**Result:** 2

| Time | Space |
|------|-------|
| O(V + E * alpha(V)) ~ O(V + E) | O(V) |

---

## COMPLEXITY -- INTUITIVELY
**DFS/BFS:** "We build the graph in O(V+E) and visit every node and edge exactly once during traversal. Total: O(V+E)."
**Union-Find:** "We process each edge once with near-O(1) union/find operations. Same asymptotic cost, but uses less space (no adjacency list needed)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to count isolated nodes (nodes with no edges) as their own components.
2. Not marking nodes as visited before pushing to stack/queue -- causes duplicate processing.
3. In Union-Find, not using path compression -- degrades to O(V) per find.

### Edge Cases to Test
- [ ] No edges at all: answer = n
- [ ] Fully connected graph: answer = 1
- [ ] Single node: answer = 1
- [ ] Graph is already a single chain
- [ ] Multiple isolated nodes mixed with connected groups

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is the graph undirected? Can there be isolated nodes? Are edges guaranteed valid?"
2. **Approach:** "I'll use DFS -- iterate through nodes, and each time I find an unvisited node, I launch a DFS to mark its entire component. The number of launches is the answer."
3. **Code:** Build adjacency list, iterate with visited check, DFS helper.
4. **Test:** Walk through the 5-node example.

### Follow-Up Questions
- "Can you do it with Union-Find?" --> Yes, process edges with DSU, count distinct roots.
- "What if edges are added dynamically?" --> Union-Find is better -- supports incremental updates.
- "What about directed graphs?" --> Use Kosaraju's or Tarjan's for strongly connected components.

---

## CONNECTIONS
- **Prerequisite:** Graph Representation (P001), DFS/BFS basics
- **Same Pattern:** Number of Islands (grid version), Number of Provinces
- **Harder Variant:** Strongly Connected Components (directed), Critical Connections
- **This Unlocks:** Understanding of graph traversal as a grouping mechanism
