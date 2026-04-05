# Disjoint Set Union Find

> **Batch 3 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Implement a **Disjoint Set Union (DSU)** data structure (also called **Union-Find**) that supports three operations efficiently:
1. **`makeSet(n)`** -- Create `n` singleton sets `{0}, {1}, ..., {n-1}`
2. **`find(x)`** -- Return the representative (root) of the set containing element `x`
3. **`union(x, y)`** -- Merge the sets containing `x` and `y` into one set

Achieve near **O(1) amortized** time per operation using **path compression** and **union by rank** (or union by size).

### Examples

| Operations | Result | Explanation |
|---|---|---|
| `makeSet(5)` | Sets: {0},{1},{2},{3},{4} | Five singleton sets |
| `union(0,1)` | Sets: {0,1},{2},{3},{4} | Merged sets of 0 and 1 |
| `union(2,3)` | Sets: {0,1},{2,3},{4} | Merged sets of 2 and 3 |
| `find(1)==find(0)` | `true` | Both in same set |
| `find(1)==find(2)` | `false` | Different sets |
| `union(1,3)` | Sets: {0,1,2,3},{4} | Merging via representatives |
| `find(3)==find(0)` | `true` | Now all in same set |

### Real-World Analogy
Think of **social network friend groups**. Initially, everyone is their own group. When person A befriends person B, their entire friend groups merge. To check if two people are in the same group, you follow the "who introduced you" chain up to the group leader. **Path compression** is like introducing everyone directly to the group leader (skipping intermediaries). **Union by rank** is like always making the larger group absorb the smaller one, keeping the tree shallow.

### Three Key Observations

1. **Each set is a tree with a root as the representative** -- `parent[x]` points to x's parent; the root points to itself (`parent[root] == root`). `find(x)` walks up the tree to the root.
   - *Aha:* Without optimizations, `find` can take O(n) if the tree degenerates into a linked list.

2. **Path compression makes `find` nearly O(1) amortized** -- During `find(x)`, make every node on the path point directly to the root. Future `find` calls on these nodes are O(1).
   - *Aha:* One expensive `find` flattens the tree, making all subsequent finds cheap.

3. **Union by rank keeps trees shallow** -- Always attach the shorter tree under the taller tree's root. This keeps tree height at most O(log n), and combined with path compression gives O(alpha(n)) amortized -- practically O(1).
   - *Aha:* `alpha(n)` is the inverse Ackermann function. For any practical n (up to 10^80), `alpha(n) <= 4`.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---|---|---|
| Set representation | Forest of trees (`parent[]` array) | O(1) space per element |
| Find optimization | Path compression | Flattens tree for future O(1) lookups |
| Union optimization | Union by rank (or by size) | Keeps tree height logarithmic |
| Combined complexity | O(alpha(n)) amortized per operation | Nearly constant time |

---

## APPROACH LADDER

### Approach 1: Naive Union-Find (No Optimizations)

**Intuition:** Each element has a parent pointer. `find` walks up to root. `union` makes one root point to the other. Without optimizations, trees can degenerate.

**Steps:**
1. `parent[i] = i` for all elements
2. `find(x)`: while `parent[x] != x`, move to `parent[x]`
3. `union(x, y)`: `parent[find(x)] = find(y)` (arbitrary attachment)

**ASCII Diagram (Degenerate case):**
```
union(0,1), union(1,2), union(2,3), union(3,4):

Without optimization, each union attaches left root under right:
  0 -> 1 -> 2 -> 3 -> 4

find(0) walks 4 steps!  (linked list behavior)

Tree:     4
         /
        3
       /
      2
     /
    1
   /
  0
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(n) per find, O(n) per union | Tree height can be O(n) |
| Space  | O(n) | Parent array |

---

### Approach 2: Union by Rank (Without Path Compression)

**Intuition:** Track the "rank" (upper bound on height) of each tree. Always attach the shorter tree under the taller tree's root. This guarantees tree height is O(log n).

**Steps:**
1. `parent[i] = i`, `rank[i] = 0` for all elements
2. `find(x)`: walk up to root (no compression)
3. `union(x, y)`:
   - `rootX = find(x)`, `rootY = find(y)`
   - If `rank[rootX] < rank[rootY]`: `parent[rootX] = rootY`
   - If `rank[rootX] > rank[rootY]`: `parent[rootY] = rootX`
   - If equal: `parent[rootY] = rootX`, `rank[rootX]++`

**ASCII Diagram (Union by rank):**
```
union(0,1): rank[0]=0, rank[1]=0 -> equal, attach 1 under 0, rank[0]=1
    0
   /
  1

union(2,3): same -> attach 3 under 2, rank[2]=1
    2
   /
  3

union(0,2): rank[0]=1, rank[2]=1 -> equal, attach 2 under 0, rank[0]=2
      0
     / \
    1   2
       /
      3

Height = 2 = O(log 4) ✓  (vs O(4) in naive approach)
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(log n) per operation | Tree height bounded by O(log n) |
| Space  | O(n) | Parent + rank arrays |

---

### Approach 3: Path Compression + Union by Rank (Best)

**Intuition:** Combine both optimizations. Path compression: during `find(x)`, make every node on the path point directly to the root. Union by rank: attach shorter tree under taller. Together, amortized time is O(alpha(n)) per operation -- essentially O(1).

**Steps:**
1. `parent[i] = i`, `rank[i] = 0`
2. `find(x)`: if `parent[x] != x`, set `parent[x] = find(parent[x])` (recursive compression)
3. `union(x, y)`: same rank-based attachment as Approach 2

**ASCII Diagram (Path compression in action):**
```
Before find(0):              After find(0):
      4                         4
     /                        / | \
    3                        3  2  0
   /                              |
  2                               1
 /
1
/
0

find(0) walks up: 0 -> 1 -> 2 -> 3 -> 4 (root)
Path compression: parent[0]=4, parent[1]=4, parent[2]=4, parent[3]=4

Now find(0), find(1), find(2), find(3) are ALL O(1)!
```

**Dry-Run Trace:**
```
makeSet(5): parent=[0,1,2,3,4], rank=[0,0,0,0,0]

union(0,1):
  find(0)=0, find(1)=1
  rank[0]=rank[1]=0 -> parent[1]=0, rank[0]=1
  parent=[0,0,2,3,4], rank=[1,0,0,0,0]

union(2,3):
  find(2)=2, find(3)=3
  rank equal -> parent[3]=2, rank[2]=1
  parent=[0,0,2,2,4], rank=[1,0,1,0,0]

union(3,4):
  find(3): parent[3]=2, parent[2]=2 -> root=2
           path compression: parent[3]=2 (already direct)
  find(4)=4
  rank[2]=1 > rank[4]=0 -> parent[4]=2
  parent=[0,0,2,2,2], rank=[1,0,1,0,0]

union(0,2):
  find(0)=0, find(2)=2
  rank[0]=1 = rank[2]=1 -> parent[2]=0, rank[0]=2
  parent=[0,0,0,2,2], rank=[2,0,1,0,0]

find(4):
  parent[4]=2, parent[2]=0, parent[0]=0 -> root=0
  path compression: parent[4]=0, parent[2]=0 (already)
  parent=[0,0,0,2,0]  <-- 4 now points directly to root!

All in same set? find(0)=find(1)=find(2)=find(3)=find(4)=0  ✓
```

**BUD Analysis:**
- **B**ottleneck: Without optimizations, `find` is O(n). Path compression eliminates this.
- **U**nnecessary: Walking long paths repeatedly. Compression makes them O(1) after first walk.
- **D**uplicate: Re-traversing the same ancestors. Compression removes duplicate traversals permanently.

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(alpha(n)) amortized per operation | Path compression + union by rank; alpha(n) <= 4 for all practical inputs |
| Space  | O(n) | Parent + rank arrays |

---

## COMPLEXITY INTUITIVELY

**What is alpha(n)?** It's the inverse Ackermann function. The Ackermann function grows ABSURDLY fast (faster than exponential, tower of powers, etc.). Its inverse grows absurdly slowly. For `n = 10^80` (more than atoms in the universe), `alpha(n) = 4`. For ALL practical purposes, it's constant.

**Why not just O(1)?** Technically, a single `find` CAN take O(log n) in the worst case. But AMORTIZED over m operations, total time is O(m * alpha(n)). The "alpha" acknowledges that rare expensive operations pay for many cheap future ones.

**Path compression alone gives O(log* n)** (iterated logarithm). Combined with union by rank, it improves to O(alpha(n)). Both are effectively constant.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out For |
|---|---|---|
| `union(x, x)` | No-op (same set) | Check `rootX == rootY` before merging |
| `find` on freshly created set | Returns itself | `parent[x] = x` initially |
| All elements in one set | One root | Tree depth bounded by O(log n) with union by rank |
| Repeated `find` on same element | O(1) after first call | Path compression makes subsequent calls instant |
| `n = 0` or `n = 1` | Empty or singleton | Handle edge case in constructor |

**Common Mistakes:**
- Forgetting the `rootX == rootY` check in `union` (creating self-loops or incrementing rank incorrectly)
- Using union by rank WITHOUT path compression (O(log n) instead of O(alpha(n)))
- Path compression WITHOUT union by rank (O(log* n) instead of O(alpha(n)))
- Confusing "rank" with "size" -- rank is an upper bound on height, not the count of elements
- Iterative path compression (two-pass) vs recursive (one-pass) -- both work, recursive is cleaner

---

## INTERVIEW LENS

| Question | Answer |
|---|---|
| "Why not just use a hash set for each group?" | Merging two sets takes O(min(n,m)) time. DSU does it in O(alpha(n)). |
| "Union by rank vs union by size?" | Both work. By rank: bounds tree height. By size: always merge smaller into larger. Same amortized complexity. |
| "Can you implement find iteratively?" | Yes: two passes. Pass 1: walk to root. Pass 2: walk again, setting parent to root. Same compression, no recursion. |
| "When would you use DSU over DFS?" | When edges arrive dynamically (online). DFS needs the full graph upfront; DSU processes edges one at a time. |
| "How do you count the number of connected components?" | Track a `count` variable. Start at n. Decrement on each successful union (where roots differ). |

---

## CONNECTIONS

| Related Problem | How It Connects |
|---|---|
| Number of Provinces (LC #547) | Count connected components using DSU |
| Number of Islands II (LC #305) | Dynamic connectivity -- DSU is optimal |
| Redundant Connection (LC #684) | Find the edge that creates a cycle using DSU |
| Kruskal's MST | Uses DSU to check if adding an edge creates a cycle |
| Accounts Merge (LC #721) | Group accounts by common emails using DSU |
| Largest Component Size by Common Factor (LC #952) | DSU to group numbers sharing prime factors |
