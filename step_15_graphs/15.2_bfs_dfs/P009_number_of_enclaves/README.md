# Number of Enclaves

> **LeetCode 1020** | **Step 15 — Graphs BFS/DFS** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

You are given an `m x n` binary matrix `grid`, where `0` represents water and `1` represents land.

A **move** consists of walking from one land cell to a 4-directionally adjacent land cell or walking off the boundary of the grid.

Return the number of land cells in `grid` from which you **cannot** walk off the boundary of the grid in any number of moves.

---

## Examples

| Input Grid | Output | Explanation |
|------------|--------|-------------|
| `[[0,0,0,0],[1,0,1,0],[0,1,1,0],[0,0,0,0]]` | `3` | Cells (1,2),(2,1),(2,2) are inland; (1,0) touches the left boundary |
| `[[0,1,1,0],[0,0,1,0],[0,0,1,0],[0,0,0,0]]` | `0` | All land cells connect to the top boundary |

**Visual (grid1):**
```
0 0 0 0
1 0 1 0   <-- cell (1,0) touches boundary; cells (1,2),(2,1),(2,2) are enclosed
0 1 1 0
0 0 0 0
```
The 3 inland cells are **enclaves** — no path to the boundary exists.

---

## Constraints

- `m == grid.length`, `n == grid[0].length`
- `1 <= m, n <= 500`
- `grid[i][j]` is either `0` or `1`

---

## Approach 1: Brute Force — BFS from Every Land Cell

**Intuition:** Treat each unvisited land cell as a potential enclave. BFS outward from it to discover its connected component. If any cell in the component sits on the grid boundary, the entire component is NOT an enclave; otherwise add its size to the answer.

**Steps:**
1. Maintain a `visited` matrix.
2. For each cell `(r, c)` with `grid[r][c] == 1` and not yet visited:
   a. Run BFS and collect all cells in its connected component.
   b. Check if any cell has `r == 0`, `r == m-1`, `c == 0`, or `c == n-1`.
   c. If no boundary cell found, add component size to `ans`.
3. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O((mn)^2) worst case — each component BFS touches O(mn) cells and there can be O(mn) components |
| Space  | O(mn) for `visited` + BFS queue |

---

## Approach 2: Optimal — Multi-Source BFS from Boundary

**Intuition:** Flip the perspective. Instead of asking "does this cell reach the boundary?", ask "which cells can the boundary reach?". Seed BFS from **all** boundary land cells simultaneously. Any land cell reachable this way is not an enclave. Count the rest.

**Steps:**
1. Enqueue all cells on the four boundary edges where `grid[r][c] == 1`. Mark them visited.
2. Run standard BFS, expanding to unvisited land neighbors.
3. After BFS, iterate the grid and count cells where `grid[r][c] == 1` and `!visited[r][c]`.

| Metric | Value |
|--------|-------|
| Time   | O(mn) |
| Space  | O(mn) for visited array and queue |

---

## Approach 3: Best — Multi-Source DFS with In-Place Marking

**Intuition:** Same as Approach 2 but avoids a separate `visited` array by overwriting boundary-reachable land cells with `0` directly in the grid. After the flood, the grid's remaining `1`s are exactly the enclaves.

**Steps:**
1. For every cell on the four boundaries, if `grid[r][c] == 1`, call DFS to flood-fill with `0`.
2. DFS: set `grid[r][c] = 0` and recurse in all 4 directions.
3. Sum all remaining `1`s in the grid.

| Metric | Value |
|--------|-------|
| Time   | O(mn) |
| Space  | O(mn) implicit recursion stack; O(1) extra if iterative DFS used |

---

## Real-World Use Case

**Isolated region detection in maps:** GIS systems use this technique to find inland water bodies (lakes) that have no outlet to the ocean. The grid models elevation/drainage and the algorithm identifies cells that cannot drain to the map boundary — the "enclaves" of water.

**Network isolation analysis:** In a grid-based network topology, find nodes that are completely cut off from the perimeter gateways — useful in chip design (identifying floating nets) and building safety systems (isolated rooms with no exit route).

---

## Interview Tips

- The "reverse thinking" pattern (flood from boundary → count leftovers) is the key insight that turns an O(mn)² brute force into O(mn). Mention this flip explicitly.
- The same pattern solves "Surrounded Regions" (LeetCode 130) and "Pacific Atlantic Water Flow" (417) — group these together in your mental model.
- The in-place DFS (Approach 3) is clean for interviews but note it mutates input — mention this and ask the interviewer if that's acceptable.
- BFS (Approach 2) is preferred over DFS when the grid is large to avoid recursion stack overflow.
- Edge cases: all water (return 0), no inland land (return 0), grid of size 1×1 (always on boundary, return 0).
