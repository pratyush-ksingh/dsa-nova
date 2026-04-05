# Flood Fill

> **Batch 1 of 12** | **Topic:** Graphs | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
You are given an `m x n` integer grid `image` where `image[i][j]` represents the pixel value. You are also given three integers `sr`, `sc`, and `color`. Perform a **flood fill** on the image starting from the pixel `image[sr][sc]`.

To perform a flood fill: consider the starting pixel plus any pixels connected 4-directionally (up, down, left, right) to it that have the **same color** as the starting pixel. Replace the color of all these pixels with `color`.

Return the modified image.

*(LeetCode #733)*

**Constraints:**
- `1 <= m, n <= 50`
- `0 <= image[i][j], color <= 65535`
- `0 <= sr < m`
- `0 <= sc < n`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `image=[[1,1,1],[1,1,0],[1,0,1]], sr=1, sc=1, color=2` | `[[2,2,2],[2,2,0],[2,0,1]]` | Starting from (1,1)=1, fill all connected 1s with 2 |
| `image=[[0,0,0],[0,0,0]], sr=0, sc=0, color=0` | `[[0,0,0],[0,0,0]]` | New color = old color, no change needed |

### Real-Life Analogy
> *Think of the "paint bucket" tool in any image editor. You click on a region and all pixels of the same color that are connected to your click point get filled with the new color. The tool "floods" outward from where you clicked, stopping at boundaries where the color changes. This is exactly what flood fill does -- it is the algorithm behind that paint bucket.*

### Key Observations
1. This is a graph traversal problem on a 2D grid. Each cell is a node, and edges connect 4-directionally adjacent cells with the same color.
2. We only fill cells that match the **original color** of the starting pixel.
3. If the new color equals the original color, we can return immediately -- no work needed. <-- This is the "aha" insight (avoids infinite loops in DFS without visited array)

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **DFS (recursion or stack):** Natural fit -- explore as far as possible in one direction, then backtrack. The recursion stack handles backtracking automatically.
- **BFS (queue):** Also works -- explore level by level outward from the starting pixel.
- Both visit every connected pixel exactly once.

### Pattern Recognition
- **Pattern:** Grid DFS/BFS (connected region traversal)
- **Classification Cue:** "When you see _2D grid_ + _connected region_ + _fill/count/mark_ --> think _DFS/BFS on grid with 4-directional neighbors_"

---

## APPROACH LADDER

### Approach 1: DFS (Recursive)
**Idea:** From the starting pixel, recursively visit all 4 neighbors that have the original color. Change each visited pixel to the new color (this also serves as "visited" marking).

**Steps:**
1. Store `originalColor = image[sr][sc]`.
2. If `originalColor == color`, return image as-is (no work needed).
3. Call `dfs(sr, sc)`:
   - Set `image[r][c] = color`.
   - For each of 4 directions (up, down, left, right):
     - If in bounds and `image[nr][nc] == originalColor`, recurse.
4. Return image.

**Dry Run:** `image=[[1,1,1],[1,1,0],[1,0,1]], sr=1, sc=1, color=2`

originalColor = 1

| Step | Cell | Value | Action | Image State |
|------|------|-------|--------|-------------|
| 1 | (1,1) | 1 | Set to 2, explore neighbors | `[[1,1,1],[1,2,0],[1,0,1]]` |
| 2 | (0,1) | 1 | Set to 2, explore neighbors | `[[1,2,1],[1,2,0],[1,0,1]]` |
| 3 | (0,0) | 1 | Set to 2, explore neighbors | `[[2,2,1],[1,2,0],[1,0,1]]` |
| 4 | (1,0) | 1 | Set to 2, explore neighbors | `[[2,2,1],[2,2,0],[1,0,1]]` |
| 5 | (2,0) | 1 | Set to 2, no valid neighbors | `[[2,2,1],[2,2,0],[2,0,1]]` |
| 6 | (0,2) | 1 | Set to 2, no valid neighbors | `[[2,2,2],[2,2,0],[2,0,1]]` |

**Result:** `[[2,2,2],[2,2,0],[2,0,1]]`

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) recursion stack worst case |

### Approach 2: BFS (Iterative)
**What changed:** Use a queue instead of recursion. Avoids stack overflow for very large grids.

**Steps:**
1. Store `originalColor`. If same as `color`, return.
2. Enqueue `(sr, sc)`, set `image[sr][sc] = color`.
3. While queue is not empty:
   - Dequeue `(r, c)`.
   - For each 4-directional neighbor `(nr, nc)`:
     - If in bounds and `image[nr][nc] == originalColor`:
       - Set `image[nr][nc] = color`.
       - Enqueue `(nr, nc)`.
4. Return image.

| Time | Space |
|------|-------|
| O(m * n) | O(m * n) queue worst case |

*Both approaches are optimal.* Every pixel is visited at most once. BFS is preferred for very large grids to avoid stack overflow.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(m * n) -- "In the worst case, every pixel has the same color and we visit all m*n pixels exactly once."
**Space:** O(m * n) -- "The recursion stack (DFS) or queue (BFS) can hold all pixels in the worst case when the entire grid is one connected region."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the `originalColor == color` check** -- without this, DFS recurses infinitely because "visited" cells still match the original color (which is now the same as the new color).
2. Going out of bounds -- always check `0 <= nr < m` and `0 <= nc < n`.
3. Using a separate `visited` array when it is unnecessary -- coloring the pixel serves as the visited marker.

### Edge Cases to Test
- [ ] New color equals original color -- return immediately
- [ ] Starting pixel has no same-colored neighbors -- only one pixel changes
- [ ] Entire grid is one color -- fill everything
- [ ] 1x1 grid
- [ ] Starting pixel is at a corner

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Are diagonals included? (No, 4-directional.) Can the new color be the same as the old? (Yes, handle it.)"
2. **Approach:** "DFS from the starting pixel. Change color as we go -- this acts as both the fill and the visited marker. Key edge case: if new color equals old color, return immediately."
3. **Code:** Show the DFS version first (cleaner), mention BFS as alternative.
4. **Test:** Walk through the 3x3 example.

### Follow-Up Questions
- "What if diagonals count too?" --> Change 4 directions to 8 directions.
- "How would you handle a very large grid?" --> BFS to avoid stack overflow, or iterative DFS with explicit stack.
- "What about flood fill with tolerance?" --> Check `abs(image[nr][nc] - originalColor) <= tolerance` instead of exact match.

---

## CONNECTIONS
- **Prerequisite:** BFS Traversal (P003 in 15.1), DFS basics, 2D array traversal
- **Same Pattern:** Number of Islands, Surrounded Regions, Rotten Oranges
- **Harder Variant:** Surrounded Regions (O vs X), Number of Distinct Islands
- **This Unlocks:** Understanding grid as implicit graph, 4-directional BFS/DFS pattern
