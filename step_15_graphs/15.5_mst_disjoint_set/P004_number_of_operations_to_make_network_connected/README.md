# Number of Operations to Make Network Connected

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
**(LeetCode #1319)** There are `n` computers numbered `0` to `n-1` connected by ethernet cables. The `connections` array gives the existing cable connections, where `connections[i] = [a, b]` means computers `a` and `b` are directly connected.

You can remove a cable between two directly connected computers and place it between any pair of disconnected computers. Return the **minimum number of operations** (cable moves) to make all computers connected. If it is **impossible**, return `-1`.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| n=4, connections=[[0,1],[0,2],[1,2]] | 1 | Remove redundant 1-2 cable, connect to computer 3 |
| n=6, connections=[[0,1],[0,2],[0,3],[1,2],[1,3]] | 2 | 5 cables, 3 components -> need 2 moves |
| n=6, connections=[[0,1],[0,2],[0,3],[1,2]] | -1 | Only 4 cables for 6 computers, need at least 5 |

```
Example 1: n=4, connections=[[0,1],[0,2],[1,2]]

Before:                After:
  (0)---(1)              (0)---(1)
   |   /                  |
   |  /                   |
  (2)   (3)              (2)---(3)     <-- moved cable (1,2) to (2,3)

Components: {0,1,2} {3}          Components: {0,1,2,3}
Redundant cables: 1 (triangle    Operations needed: 1
  has 3 edges, needs only 2)     (components - 1 = 2 - 1 = 1)


Example 2: n=6, connections=[[0,1],[0,2],[0,3],[1,2],[1,3]]

    (0)---(1)
    /|\   /|
   / | \ / |
  (2)(3)   |             Components: {0,1,2,3}, {4}, {5}
      (4)  (5)            3 components -> need 2 cables
                          Redundant: 5 edges - 3 spanning = 2 extra
                          2 extra >= 2 needed -> possible! Answer: 2
```

### Real-World Analogy
Imagine a school with several computer labs. Some computers are networked, some are not. You have a fixed number of Ethernet cables (already installed). You can unplug a redundant cable (one that connects two computers already in the same network via other cables) and use it to connect an isolated computer. The question: how many cable moves do you need to connect everything? If you do not have enough cables total (need at least n-1), it is impossible.

### Three Key Observations

1. **Minimum cables needed = n-1** -- To connect n computers, you need at least n-1 cables (a spanning tree). If `len(connections) < n-1`, return -1 immediately.
   - *Aha:* This is the impossibility check. No matter how cleverly you rearrange cables, you cannot connect n nodes with fewer than n-1 edges.

2. **Answer = number of connected components - 1** -- Each component needs one cable to connect to the rest. If there are `k` components, you need `k-1` cables to bridge them all together.
   - *Aha:* The redundant cables within each component are sufficient to provide these k-1 cables. A component with `c` nodes and `e` edges has `e - (c-1)` redundant cables.

3. **Union-Find or DFS to count components** -- Both work in O(V+E). Union-Find is slightly more natural since we are already working with edge-based input.
   - *Aha:* With Union-Find, the number of components = n minus the number of successful unions.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Impossibility check | Cable count < n-1 | Minimum spanning tree needs n-1 edges |
| Component counting | Union-Find or DFS | Both O(V+E), Union-Find is cleaner here |
| Answer formula | components - 1 | Each merge reduces components by 1 |

---

## APPROACH LADDER

### Approach 1: Brute Force -- DFS to Count Connected Components

**Intuition:** Build an adjacency list, run DFS from each unvisited node to count connected components. If total cables < n-1, return -1. Otherwise, return components - 1.

**Steps:**
1. If `len(connections) < n - 1`: return -1
2. Build adjacency list from connections
3. DFS from each unvisited node, counting components
4. Return components - 1

```
Dry-run: n=4, connections=[[0,1],[0,2],[1,2]]

cables = 3 >= n-1 = 3 ✓

Adjacency list: 0:[1,2], 1:[0,2], 2:[0,1], 3:[]

DFS from 0: visits {0,1,2}  -> component 1
DFS from 3: visits {3}      -> component 2

Components = 2, answer = 2 - 1 = 1 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) -- adjacency list + visited array |

---

### Approach 2: Optimal -- Union-Find

**Intuition:** Use Union-Find to process all edges. Each successful union reduces the component count by 1. Start with n components, subtract successful unions. Return remaining components - 1.

**Steps:**
1. If `len(connections) < n - 1`: return -1
2. Initialize Union-Find with n nodes (n components)
3. For each connection [a, b]: `union(a, b)` -- decrement component count on success
4. Return components - 1

```
Dry-run: n=4, connections=[[0,1],[0,2],[1,2]]

cables = 3 >= 3 ✓
Components = 4

Union(0,1): success, components = 3
Union(0,2): success, components = 2
Union(1,2): FAIL (same set), components = 2

Answer = 2 - 1 = 1 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(E * alpha(V)) ~ O(E) |
| Space  | O(V) |

---

### Approach 3: Best -- Union-Find with Redundant Cable Counting

**Intuition:** Same Union-Find approach, but also explicitly count redundant cables to verify feasibility. A failed union means the cable is redundant. We need at least (components-1) redundant cables.

**Steps:**
1. If `len(connections) < n - 1`: return -1
2. Initialize Union-Find, track components and redundant cables
3. For each connection:
   - If union succeeds: components--
   - If union fails: redundant++
4. If redundant >= components - 1: return components - 1
   Else: return -1

**Note:** Step 4's else branch is actually unreachable when we already checked `len(connections) >= n-1`, because:
- Total edges = (n - components) + redundant [successful unions + failed unions]
- If edges >= n-1 and components > 1, then redundant = edges - (n - components) >= n-1 - n + components = components - 1

| Metric | Value |
|--------|-------|
| Time   | O(E * alpha(V)) ~ O(E) |
| Space  | O(V) |

---

## COMPLEXITY INTUITIVELY

**Why O(E * alpha(V))?** We process each of the E edges once, doing a Union-Find operation that costs O(alpha(V)) amortized. Since alpha(V) is effectively constant (at most 4 for any practical V), this is essentially O(E).

**Why O(V) space?** Union-Find uses parent[] and rank[] arrays of size V. No adjacency list is needed -- we process edges directly.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| n=1 | Already connected, return 0 | 0 components to merge |
| No connections, n>1 | Return -1 if n>1 (need n-1 cables) | Not enough cables |
| Fully connected | Return 0 | Already one component |
| All cables are redundant (self-loops or parallel edges) | Still possible if count >= n-1 | Union-Find handles both |
| n=100000, dense connections | Must be efficient | Union-Find is O(E) |

**Common Mistakes:**
- Forgetting the `len(connections) < n-1` check (impossible case)
- Counting components incorrectly (off-by-one)
- Not using path compression + union by rank (TLE on large inputs)

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Tests Union-Find mastery in a non-obvious application
- The key insight (answer = components - 1) requires clear thinking about graph structure
- The impossibility check (cables < n-1) tests mathematical reasoning

**Follow-ups to expect:**
1. "What if each cable move has a cost?" -- Weighted problem, MST-like approach
2. "What if some cables cannot be moved?" -- Modify the constraint, track which edges are redundant
3. "Minimize the total cable length moved?" -- More complex optimization
4. "What is the maximum number of components you can create by removing k cables?" -- Different problem, think about bridges

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Number of Provinces (LC 547) | Count connected components -- same core task |
| Redundant Connection (LC 684) | Find the one edge that creates a cycle in Union-Find |
| Kruskal's Algorithm | Uses Union-Find for MST construction, same DSU pattern |
| Graph Valid Tree (LC 261) | Check if graph is a single connected component with no cycles |
| Accounts Merge (LC 721) | Union-Find to merge overlapping groups |
| Smallest String With Swaps (LC 1202) | Union-Find to group indices, then sort within groups |
