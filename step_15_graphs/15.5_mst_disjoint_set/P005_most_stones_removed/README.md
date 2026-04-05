# Most Stones Removed with Same Row or Column

> **Step 15.15.5** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #947)** On a 2D plane, we place `n` stones at some integer coordinate points. Each coordinate point may have at most one stone. A stone can be removed if it shares either the **same row or the same column** as another stone that has not been removed. Return the **maximum number of stones** that can be removed.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]] | 5 | All connected via shared rows/cols; remove all but 1 |
| stones=[[0,0],[0,2],[1,1],[2,0],[2,2]] | 3 | Two components: {[0,0],[0,2],[2,0],[2,2]} and {[1,1]}. Remove 3+0=3 |
| stones=[[0,0]] | 0 | Single stone, cannot remove |

```
Example 1: stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]

  col: 0   1   2
row 0: X   X
row 1: X       X
row 2:     X   X

All 6 stones are connected (share rows/cols transitively).
One component of size 6 -> can remove 6-1 = 5 stones.

Example 2: stones=[[0,0],[0,2],[1,1],[2,0],[2,2]]

  col: 0   1   2
row 0: X       X
row 1:     X
row 2: X       X

Component 1: {[0,0],[0,2],[2,0],[2,2]} (connected via shared rows/cols)
Component 2: {[1,1]} (isolated)
Answer: (4-1) + (1-1) = 3
```

### Real-World Analogy
Think of servers in a data center grid. Each server that shares a network row-bus or column-bus with another server is in the same network segment. You want to decommission the maximum number of redundant servers while keeping at least one per segment alive for connectivity.

### Three Key Observations

1. **Graph model:** Stones sharing a row or column are "connected." This forms a graph where we need to count connected components.
   - *Aha:* From each component of size `k`, we can remove `k-1` stones (keep one as anchor).

2. **Answer = n - components:** Total removable stones = sum over all components of (size - 1) = n - number_of_components.

3. **Union-Find on coordinates:** Instead of comparing all pairs O(n^2), union stones by their row/column using maps. Even better: treat each row and column as a virtual node in Union-Find.

---

## APPROACH LADDER

### Approach 1: Brute Force -- DFS to Find Connected Components

**Intuition:** Build an implicit graph where stone `i` connects to stone `j` if they share a row or column. DFS to count connected components. Answer = n - components.

**Steps:**
1. For each unvisited stone `i`, start DFS
2. In DFS, visit all stones `j` sharing row or column with current stone
3. Count total components
4. Return `n - components`

```
Dry-run: stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]

DFS from stone 0 [0,0]:
  -> stone 1 [0,1] (same row 0)
    -> stone 4 [2,1] (same col 1)
      -> stone 5 [2,2] (same row 2)
        -> stone 3 [1,2] (same col 2)
          -> stone 2 [1,0] (same row 1)
All 6 visited in 1 component.
Answer = 6 - 1 = 5
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) -- each DFS step checks all n stones |
| Space  | O(n) -- visited array + recursion stack |

---

### Approach 2: Optimal -- Union-Find Grouping Stones

**Intuition:** Use row and column maps to union stones efficiently. For each stone, if another stone in the same row already exists, union them. Same for columns. This avoids O(n^2) pairwise comparison.

**Steps:**
1. Initialize Union-Find with n stones
2. Maintain `row_map` and `col_map`: maps from row/col to first stone index seen
3. For each stone `i` at `(r, c)`:
   - If row `r` already in `row_map`, union `i` with `row_map[r]`
   - If col `c` already in `col_map`, union `i` with `col_map[c]`
   - Otherwise store `i` as the representative
4. Count distinct roots, return `n - components`

```
Dry-run: stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]

Stone 0 [0,0]: row_map={0:0}, col_map={0:0}
Stone 1 [0,1]: row 0 exists -> union(1,0). col_map={0:0,1:1}
Stone 2 [1,0]: col 0 exists -> union(2,0). row_map={0:0,1:2}
Stone 3 [1,2]: row 1 exists -> union(3,2). col_map={0:0,1:1,2:3}
Stone 4 [2,1]: col 1 exists -> union(4,1). row_map={0:0,1:2,2:4}
Stone 5 [2,2]: row 2 exists -> union(5,4). col 2 exists -> union(5,3)

All in one component. Answer = 6 - 1 = 5
```

| Metric | Value |
|--------|-------|
| Time   | O(n * alpha(n)) ~ O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Union-Find with Coordinate Mapping

**Intuition:** Instead of indexing stones, treat each unique row and column as a node in Union-Find. For stone at `(r, c)`, union the row-node `r` with the column-node `c + OFFSET` (offset avoids row/col collision). Then count distinct components among all stone coordinates.

**Steps:**
1. Use a hashmap-based Union-Find (lazy initialization)
2. For each stone `(r, c)`: `union(r, c + 100001)`
3. Count distinct roots among all stone row-values
4. Return `n - components`

**Why this is elegant:** No need for row_map/col_map. Each stone automatically connects its row to its column. Transitive connections form naturally.

| Metric | Value |
|--------|-------|
| Time   | O(n * alpha(n)) ~ O(n) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Single stone | 0 removable | 1 component, n-1 = 0 |
| All stones same row | n-1 removable | 1 component |
| No shared rows or cols | 0 removable | n components |
| Large coordinates (10^4) | Offset must exceed max coordinate | Use 100001 |

**Common Mistakes:**
- Forgetting that connectivity is transitive (stones not directly sharing row/col can be in same component)
- Using O(n^2) union approach instead of row/col maps
- Row/column collision in coordinate-based Union-Find (must offset columns)

---

## Real-World Use Case
**Database deduplication:** In a system where records are linked if they share a common field (e.g., same email or same phone), finding connected components tells you how many unique entities exist. The "removable stones" analog is the number of duplicate records that can be merged.

## Interview Tips
- Start by explaining the key insight: answer = n - connected_components
- Clarify that "shares row or column" means transitive connectivity, not just direct adjacency
- Union-Find is the natural fit since we are merging sets based on shared attributes
- The coordinate-mapping approach is a great "level up" to impress interviewers
- Mention that DFS works but is O(n^2) due to all-pairs check; Union-Find with maps is O(n)
