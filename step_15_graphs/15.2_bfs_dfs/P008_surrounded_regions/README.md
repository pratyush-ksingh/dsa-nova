# Surrounded Regions

> **Step 15.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #130)** You are given an `m x n` matrix of characters `'X'` and `'O'`. **Capture** all regions of `'O'` that are **completely surrounded** by `'X'` by flipping those `'O'`s to `'X'`.

A region is **NOT** captured if any `'O'` in it:
- Is on the **border** of the board, OR
- Is **4-directionally connected** to a border `'O'`

Modify the board **in-place**.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]] | [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]] | Inner O's captured; bottom-left O connected to border, survives |

```
Before:          After:
X X X X          X X X X
X O O X   -->    X X X X
X X O X          X X X X
X O X X          X O X X

The O at (3,1) is on the border => safe.
The O's at (1,1),(1,2),(2,2) are not connected to any border O => captured.
```

### Constraints
- `m == board.length`, `n == board[0].length`
- `1 <= m, n <= 200`
- `board[i][j]` is `'X'` or `'O'`

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Key insight | Invert the problem | Find safe O's (border-connected) rather than captured O's |
| Traversal | BFS or DFS from borders | All border O's and their components are safe by definition |
| Marking | Temporary '#' sentinel | Distinguishes safe O's from captured O's during processing |

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS per O-component, check border touch

**Intuition:** For each group of connected `'O'`s, check whether any cell in that group touches the border. If not, flip the whole group. The check is correct but wastes time re-exploring cells that belong to the same component as already-visited cells.

**Steps:**
1. Maintain a `processed` set (or array)
2. For each unvisited `'O'`, BFS to find its entire connected component
3. While BFS-ing, check if any cell is on the border (safe flag)
4. If `safe == False`, flip all cells in the component to `'X'`
5. Mark all cells in the component as processed

**BUD Analysis:**
- **Bottleneck:** Each O-cell can trigger a new BFS
- **Unnecessary:** Processing each component independently is O(m*n) per component
- **Duplicated:** Border-adjacent cells are re-examined from every interior neighbor

| Metric | Value |
|--------|-------|
| Time   | O((m*n)^2) worst case -- many small disjoint O-regions each taking O(m*n) |
| Space  | O(m*n) -- component storage and visited array |

---

### Approach 2: Optimal -- BFS from border O's (Inverse Marking)

**Intuition:** Flip the question: instead of asking "which O's are captured?", ask "which O's are SAFE?". An O is safe if and only if it is reachable from a border O. This is much easier: just BFS from all border O's and mark everything reachable. Everything unmarked is captured.

**Steps:**
1. For every `'O'` on the 4 borders, launch BFS and mark all reachable `'O'`s as `'#'` (safe sentinel)
2. Single pass over the board:
   - `'O'` (not reached) -> `'X'` (captured)
   - `'#'` (safe) -> `'O'` (restore)
   - `'X'` -> stays `'X'`

```
Dry-run: board above

Border O's: (3,1)
BFS from (3,1): no neighboring O's => marks only (3,1) as '#'

Board after marking:
X X X X
X O O X
X X O X
X # X X

Flip/restore:
- (1,1),(1,2),(2,2) are still 'O' => flip to 'X'
- (3,1) is '#' => restore to 'O'

Final:
X X X X
X X X X
X X X X
X O X X  correct
```

| Metric | Value |
|--------|-------|
| Time   | O(m*n) -- each cell visited at most twice (mark pass + flip pass) |
| Space  | O(m*n) -- BFS queue in worst case (entire board is O's) |

---

### Approach 3: Best -- Iterative DFS from border O's

**Intuition:** Identical algorithm to Approach 2 but uses an explicit stack instead of a BFS queue. The DFS order does not matter for correctness (we just need to mark all reachable cells). Iterative DFS avoids Python's recursion limit on large grids.

**Steps:** Same as Approach 2, replacing the BFS queue with a DFS stack.

| Metric | Value |
|--------|-------|
| Time   | O(m*n) |
| Space  | O(m*n) -- stack depth bounded by number of O cells |

---

## COMPLEXITY INTUITIVELY

**Why O(m*n)?** Each cell is touched at most a constant number of times: once during the border seeding scan, at most once during BFS/DFS marking, and once during the final flip pass. No cell is ever enqueued more than once because marking it `'#'` prevents it from being re-enqueued.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| All O's | Every O is border-connected (via the border row/column), so nothing is flipped | Handled correctly |
| 1x1 board with O | O is on the border, safe | Returns unchanged |
| No O's on border | No BFS is seeded; all O's get flipped | Correct |
| Entirely X's | Nothing changes | Correct |
| O region partially on border | Entire region stays (whole component is safe) | BFS propagates from the border cell through the whole component |

**Common Mistakes:**
- Doing BFS from interior O's to find border (brute force direction) -- O(n^2) cost
- Forgetting to restore `'#'` back to `'O'` in the final pass
- Using a separate visited array when the `'#'` sentinel already acts as a visited marker
- Treating only corner cells as border cells (all 4 edges count)

---

## Real-World Use Case

**Flood Fill / Enclosed Region Detection:** In image processing, identify fully enclosed shapes. In game maps (Reversi/Othello), this exact algorithm captures enclosed opponent pieces. In circuit design, identify isolated regions of conductor surrounded by insulator.

**Geographic Enclave Detection:** In geopolitical maps, find land territories completely surrounded by another country's territory (like Lesotho inside South Africa) -- exactly the "surrounded region" problem.

---

## Interview Tips
- The key insight is **inverting the question**: find safe O's (border-connected) rather than captured O's. State this clearly upfront.
- The `'#'` sentinel trick avoids a separate visited array. Explain why this is valid (we restore it at the end).
- Connect to **Pacific Atlantic Water Flow** (LC 417) -- same border-seeding BFS idea.
- Follow-up: "What if 8-directional adjacency?" -- Just add the 4 diagonal directions to DIRS.
- Follow-up: "Can you do it with Union-Find?" -- Yes, map each cell to its component root; safe components are those containing a border cell.
