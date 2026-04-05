# Number of Provinces

> **Batch 2 of 12** | **Topic:** Graphs | **Difficulty:** Medium | **XP:** 25

---

## UNDERSTAND

### Problem Statement
**(LeetCode #547)** There are `n` cities. Some of them are connected, some are not. If city `a` is connected directly to city `b`, and city `b` is connected directly to city `c`, then `a` and `c` are in the same **province**. Given an `n x n` adjacency matrix `isConnected` where `isConnected[i][j] = 1` means city `i` and city `j` are directly connected, return the **total number of provinces** (i.e., connected components).

### Real-World Analogy
Imagine a country where some cities have roads between them. A "province" is a group of cities where you can drive from any city to any other city within that group (possibly through intermediate cities). Two cities with no road path between them belong to different provinces. We need to count how many such isolated road networks exist.

### Three Key Observations

1. **This is "count connected components" in disguise** -- Each province is one connected component. The answer is the number of DFS/BFS calls needed to visit all nodes.
   - *Aha:* The input is an adjacency MATRIX, not a list. `isConnected[i][j] = 1` means edge between `i` and `j`.

2. **DFS from any unvisited node explores exactly one province** -- When DFS finishes, every city in that province is marked visited. The next unvisited city starts a new province.
   - *Aha:* The number of provinces = the number of times we start a new DFS from the outer loop.

3. **Union-Find is a natural alternative** -- Instead of DFS, union all connected pairs. The number of distinct roots = number of provinces.
   - *Aha:* Union-Find with path compression + union by rank gives near-O(1) per operation.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Graph representation | Adjacency matrix (given) | Input format; no conversion needed |
| Traversal | DFS or BFS | Visit all nodes in a component |
| Alternative | Union-Find (Disjoint Set) | Elegant for counting components |
| Visited tracking | boolean[] | O(1) per check |

---

## APPROACH LADDER

### Approach 1: DFS (Brute/Standard)

**Intuition:** For each unvisited city, launch a DFS that marks all reachable cities as visited. Count how many launches are needed.

**Steps:**
1. Initialize `visited[]` of size `n`, all `false`
2. Initialize `provinces = 0`
3. For `i = 0` to `n-1`:
   - If `!visited[i]`: increment `provinces`, call `dfs(i)`
4. `dfs(node)`: mark visited, then for each `j` where `isConnected[node][j] == 1` and `!visited[j]`, recurse

**ASCII Diagram:**
```
isConnected = [[1,1,0],       City Graph:
               [1,1,0],         0 --- 1     2
               [0,0,1]]
                               Province 1: {0, 1}
                               Province 2: {2}
                               Answer: 2
```

**Dry-Run Trace:**
```
n = 3, isConnected = [[1,1,0],[1,1,0],[0,0,1]]
visited = [F, F, F], provinces = 0

i=0: not visited -> provinces=1, dfs(0)
  visit 0, visited=[T, F, F]
  j=0: isConnected[0][0]=1 but visited[0]=T, skip (self-loop)
  j=1: isConnected[0][1]=1, not visited -> dfs(1)
    visit 1, visited=[T, T, F]
    j=0: visited, skip
    j=1: self, skip
    j=2: isConnected[1][2]=0, skip
  j=2: isConnected[0][2]=0, skip

i=1: visited, skip

i=2: not visited -> provinces=2, dfs(2)
  visit 2, visited=[T, T, T]
  j=0: 0, skip
  j=1: 0, skip
  j=2: self, skip

Return provinces = 2  ✓
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(n^2) | For each of n nodes, scan n columns in the adjacency matrix |
| Space  | O(n) | Visited array + recursion stack |

---

### Approach 2: BFS

**Intuition:** Same component-counting logic, but use BFS (queue) instead of DFS (stack/recursion).

**Steps:**
1. Same outer loop counting structure
2. Instead of recursion, use a queue: enqueue start node, process level by level

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(n^2) | Same matrix scanning |
| Space  | O(n) | Queue instead of stack |

---

### Approach 3: Union-Find (Optimal for this pattern)

**Intuition:** Initialize each city as its own province. For every pair `(i, j)` where `isConnected[i][j] = 1`, union them. At the end, count distinct roots.

**Steps:**
1. Initialize `parent[i] = i`, `rank[i] = 0` for each city
2. For each `i`, for each `j > i`:
   - If `isConnected[i][j] == 1`: `union(i, j)`
3. Count how many `i` have `find(i) == i` (they are roots)

**Dry-Run Trace (Union-Find):**
```
n=3, isConnected = [[1,1,0],[1,1,0],[0,0,1]]

Initial: parent = [0, 1, 2], rank = [0, 0, 0]

i=0, j=1: isConnected[0][1]=1 -> union(0,1)
  find(0)=0, find(1)=1, different
  rank equal -> parent[1]=0, rank[0]=1
  parent = [0, 0, 2]

i=0, j=2: isConnected[0][2]=0, skip
i=1, j=2: isConnected[1][2]=0, skip

Count roots: find(0)=0 ✓, find(1)=0 ✗, find(2)=2 ✓
Provinces = 2  ✓
```

**BUD Analysis:**
- **B**ottleneck: Reading the matrix is O(n^2) -- unavoidable since input is n x n
- **U**nnecessary: We only need upper triangle (j > i) since matrix is symmetric
- **D**uplicate: Union-Find with path compression avoids redundant `find` traversals

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(n^2 * α(n)) ≈ O(n^2) | Scan upper triangle; union/find are near-O(1) with path compression |
| Space  | O(n) | Parent and rank arrays |

---

## COMPLEXITY INTUITIVELY

**Why O(n^2)?** The input itself is an n x n matrix. Just reading the input requires O(n^2) operations. No algorithm can do better than reading its input, so O(n^2) is optimal for this problem.

**DFS vs Union-Find:** Both are O(n^2). DFS may be slightly faster in practice (simpler operations), but Union-Find is more versatile (supports dynamic edge additions).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| `n=1` | 1 province | Single city is its own province |
| All connected (complete graph) | 1 province | DFS visits everything from node 0 |
| No connections (diagonal only) | n provinces | Each city is its own province |
| `isConnected[i][i] = 1` always | Self-loops are irrelevant | Don't count self-connections as edges |

**Common Mistakes:**
- Treating the matrix as an edge list instead of adjacency matrix
- Forgetting that `isConnected[i][i] = 1` is always true (it doesn't mean city `i` connects to itself meaningfully)
- In Union-Find: forgetting path compression, leading to O(n^3) worst case

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Why not convert matrix to adjacency list first?" | Extra O(n^2) preprocessing for no gain -- we scan the matrix either way |
| "Which approach do you prefer?" | DFS for simplicity, Union-Find if follow-ups involve dynamic connectivity |
| "Can you do it in-place?" | Yes: use the matrix itself as visited by zeroing out rows/columns (destructive) |
| "What if cities are added dynamically?" | Union-Find shines -- just union new city with its connections |
| "Difference from Number of Islands?" | Same pattern! Islands uses a 2D grid, provinces uses an adjacency matrix |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Number of Islands (LC #200) | Same "count components" pattern on a 2D grid |
| Friend Circles | Literally the same problem, renamed |
| Connected Components in Graph | General version of this problem |
| Accounts Merge (LC #721) | Union-Find on string-keyed groups |
| Graph Valid Tree (LC #261) | Connected components = 1 AND no cycles |
