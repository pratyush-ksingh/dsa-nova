# Maximum Edge Removal

> **Step 13 | 13.2** | **Difficulty:** HARD | **XP:** 50 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an **undirected tree** with `N` nodes (1-indexed), find the **maximum number of edges** that can be removed such that **every resulting connected component has an even number of nodes**.

**Constraints:**
- `1 <= N <= 10^5`
- Tree has exactly `N - 1` edges.
- N must be even for any solution to exist (if N is odd, answer is 0).

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `N=10, edges=[[2,1],[3,1],[4,3],[5,2],[6,1],[7,2],[8,6],[9,8],[10,8]]` | `2` | Remove 2 edges to create 3 even-sized components |
| `N=4, edges=[[1,2],[2,3],[3,4]]` | `1` | Remove edge (1,2) or (3,4) to get two components of size 2 |
| `N=8, edges=1-2-3-4-5-6-7-8 (path)` | `3` | Subtrees of sizes 2,4,6 are even; 3 edges can be removed |
| `N=3, edges=[[1,2],[1,3]]` | `0` | N is odd; impossible to split into all-even components |

### Real-Life Analogy
> *Imagine a city road network (a tree). You want to split it into self-contained districts, each with an even number of intersections (for two-way pairing of resources). A road can be cut if the district on one side has an even count of intersections — because the other side automatically has even count too (total is even). Count how many roads you can cut.*

### Key Observations
1. If N is odd, no solution exists: you cannot split an odd total into all-even pieces. Answer = 0.
2. Root the tree at node 1. Each edge `(parent, child)` separates the subtree rooted at `child` from the rest.
3. Removing edge `(parent, child)` is valid if `subtree_size(child)` is even — then the remaining component has `N - subtree_size(child)` nodes = even - even = even.
4. We can remove multiple edges independently. Each valid (even) subtree edge contributes one removal.
5. The total count is the number of non-root nodes whose subtree size is even.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why DFS with Subtree Sizes?
- A single post-order DFS computes the subtree size of every node in O(n).
- Post-order (process children before parent) allows bottom-up accumulation of sizes.
- Checking `subtree_size % 2 == 0` for each edge is O(1) per edge.

### Pattern Recognition
- **Pattern:** Tree DP / DFS with subtree aggregation.
- **Classification Cue:** "Property about subsets of tree nodes that changes upon edge removal → compute subtree sizes via DFS, check property per subtree."

---

## APPROACH LADDER

### Approach 1: Brute Force — DFS Subtree Count (Explicit Visited Array)
**Idea:** Same DFS algorithm as the optimal approach, but uses an explicit `visited[]` array instead of tracking parent to avoid revisiting. This is the "naive" implementation of the same O(n) algorithm.

**Steps:**
1. Build adjacency list.
2. DFS from node 1 with `visited[]` array. For each unvisited neighbor, recurse and accumulate child subtree sizes.
3. For each child, if `child_subtree_size % 2 == 0`, increment count.
4. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 2: Optimal — DFS with Parent Tracking (No Visited Array)
**What changed:** Instead of a visited array, pass the `parent` node to the DFS call. Skip the parent neighbor when expanding. Slightly cleaner and avoids the O(n) visited array initialization overhead (same asymptotically).

**Steps:**
1. Root the tree at node 1.
2. Call `dfs(node=1, parent=-1)` which returns the subtree size rooted at `node`.
3. For each `neighbor != parent`: recurse, get `child_size`. Accumulate into current node's size. If `child_size % 2 == 0`: increment answer.
4. Return the size of the current subtree.

**Dry Run:** N=4, path graph 1-2-3-4

Rooted at 1:
- `dfs(4, 3)` → size=1. Returns 1.
- `dfs(3, 2)`: child 4 returns 1 (odd, no cut). size=2. Returns 2.
- `dfs(2, 1)`: child 3 returns 2 (even → count=1). size=3. Returns 3.
- `dfs(1, -1)`: child 2 returns 3 (odd, no cut). Done.

Answer: 1 ✓

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) recursion stack |

---

### Approach 3: Best — Iterative DFS (Avoids Stack Overflow for Large N)
**What changed:** For large N (up to 10^5), a long chain (path graph) would cause recursion depth = N, crashing Python or deep-Java code. Iterative DFS with explicit post-order processing avoids this.

**Steps:**
1. Iterative DFS to produce post-order traversal and record each node's parent.
2. Process nodes in post-order (leaves first): accumulate `size[node]` into `size[parent[node]]`. If `size[node] % 2 == 0`, increment count.
3. Skip the root (the last element in post-order) — the root's "subtree" is the whole tree and removing a virtual edge to a nonexistent parent doesn't count.
4. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — explicit stack replaces recursion stack |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Including the root in the count: the root's subtree is the entire tree (size N). N is even but there's no edge above the root to cut — do not count it.
2. Returning non-zero for odd N — impossible to split odd total into all-even components.
3. Building directed edges only — must add both directions in the adjacency list for an undirected tree.
4. Confusing "subtree size" with "subtree height."

### Edge Cases to Test
- [ ] N = 2 (single edge): subtree of child has size 1 (odd) → NOT removable → answer = 0. Splitting into {1} and {1} gives odd components.
- [ ] N = 4 path: subtree sizes are 3,2,1 → only size 2 is even → answer = 1.
- [ ] N odd → always 0
- [ ] Star graph (one center, N-1 leaves)
- [ ] Path graph (long chain)
- [ ] Perfect binary tree

---

## Real-World Use Case
**Network partition for load balancing:** In a tree-shaped network (e.g., a corporate intranet organized as a spanning tree), you want to split it into segments of equal even size for symmetric load distribution. The DFS subtree-size algorithm quickly identifies which network links can be cut to achieve this — used in hierarchical cluster decomposition and network planning tools.

## Interview Tips
- State the key insight immediately: "Root the tree. A child edge is removable iff the child's subtree size is even, because even + even = even = total N."
- Prove it: "Removing k such edges gives k+1 components. Each removed subtree is even, and the remainder = N - sum_of_even_sizes = even. All components are even. ✓"
- Mention the N-must-be-even prerequisite — this is a quick early check that impresses interviewers.
- Walk through a small example (N=4, path graph) to show the DFS counting.
- For large N, mention the iterative DFS to avoid stack overflow — this shows production awareness.
