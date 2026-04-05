# Number of Distinct Islands

> **LeetCode 694** | **Step 15 — Graphs BFS/DFS** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## Problem Statement

Given a non-empty 2D binary grid `grid` of `0`s and `1`s, count the number of **distinct** islands. An island is a group of `1`s (land) connected 4-directionally. Two islands are considered the **same** if one island can be translated (shifted, not rotated or reflected) to equal the other.

---

## Examples

| Input Grid | Output | Explanation |
|------------|--------|-------------|
| `[[1,1,0,0,0],[1,1,0,0,0],[0,0,0,1,1],[0,0,0,1,1]]` | `1` | Both 2×2 squares are identical shapes |
| `[[1,1,0,1,1],[1,0,0,0,1],[0,0,0,0,1],[1,1,0,1,1]]` | `3` | Three differently shaped islands |

**Visual (grid1):**
```
1 1 0 0 0
1 1 0 0 0   Both top-left and bottom-right are 2x2 squares -> 1 distinct shape
0 0 0 1 1
0 0 0 1 1
```

---

## Constraints

- `m == grid.length`, `n == grid[i].length`
- `1 <= m, n <= 50`
- `grid[i][j]` is `0` or `1`

---

## Approach 1: Brute Force — Normalize Coordinates

**Intuition:** For each island, collect the absolute `(r, c)` coordinates of all its cells. Translate the entire set so the minimum row and column become 0 (place the top-left corner at the origin). Store the resulting frozenset of relative positions. Two islands are the same if their normalized sets are equal.

**Steps:**
1. DFS from each unvisited land cell; collect all `(r, c)` coordinates.
2. Compute `minR = min(r)` and `minC = min(c)` across the island.
3. Build normalized set: `{(r-minR, c-minC) for each cell}`.
4. Add to a global set of sets. The count of unique entries is the answer.

| Metric | Value |
|--------|-------|
| Time   | O(mn * k log k) where k = max island size |
| Space  | O(mn) for visited array and shape sets |

---

## Approach 2: Optimal — DFS Path Signature Encoding

**Intuition:** Encode the island's shape as the string of DFS traversal moves. When we visit a cell via direction `d`, append `d` to the path string. When we backtrack (return from recursion), append a special marker `B`. Two islands produce the same path string if and only if they have the same shape under translation.

The backtrack markers are critical: without them, different shapes can produce identical direction sequences. For example, an L-shaped and a T-shaped island might generate the same forward moves but different backtrack patterns.

**Steps:**
1. For each unvisited land cell, start DFS and initialize an empty path string.
2. On entering cell `(r, c)` from direction `d`: append `d`.
3. After exploring all 4 neighbors of `(r, c)`: append `B`.
4. Store the completed path string in a set.
5. Return the size of the set.

| Metric | Value |
|--------|-------|
| Time   | O(mn) — each cell visited once |
| Space  | O(mn) for visited array + path strings in the set |

---

## Approach 3: Best — DFS with Relative Offset Set

**Intuition:** A clean combination of Approaches 1 and 2. During DFS, record each cell's position as `(r - r0, c - c0)` where `(r0, c0)` is the DFS starting cell (the "anchor"). Store the set of offsets — this is equivalent to normalization but computed on the fly without sorting. No backtrack markers needed.

**Steps:**
1. For each unvisited land cell `(r0, c0)`, start DFS.
2. For each visited cell `(r, c)` in the island, record offset `(r-r0, c-c0)`.
3. Store the frozenset of offsets in a global set of shapes.
4. Return set size.

| Metric | Value |
|--------|-------|
| Time   | O(mn) |
| Space  | O(mn) |

---

## Real-World Use Case

**Logo/pattern recognition:** In printed circuit board (PCB) inspection, detecting duplicate component footprints on the board is equivalent to finding distinct island shapes. The same copper trace pattern may appear hundreds of times — counting distinct layouts helps validate designs.

**Satellite image analysis:** Geospatial systems count distinct lake or forest shapes in satellite imagery to track environmental patterns over time. Shape encoding by translation-invariant signatures is a standard pre-processing step.

---

## Interview Tips

- The path-signature approach (Approach 2) is the most elegant and is the standard interview answer — practice drawing the DFS tree and the resulting string on paper.
- Emphasize why backtrack markers matter: demonstrate a counterexample where two distinct islands produce the same direction string without them.
- The relative-offset approach (Approach 3) is easier to explain intuitively: "every island is just a set of offsets from its top-left corner."
- All three approaches are O(mn) in practice; the brute-force normalization is O(mn log mn) in the worst case due to set insertion with comparison.
- LeetCode 711 (Distinct Islands II with rotations/reflections) is a natural follow-up — same idea but normalizes under 8 symmetries.
