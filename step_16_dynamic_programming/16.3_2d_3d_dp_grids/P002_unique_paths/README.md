# Unique Paths

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement (LeetCode #62)
There is a robot on an `m x n` grid. The robot starts at the **top-left corner** `(0, 0)` and wants to reach the **bottom-right corner** `(m-1, n-1)`. The robot can only move **right** or **down** at any point.

Given `m` (rows) and `n` (columns), return the **number of unique paths** the robot can take.

### Examples

| # | Input | Output | Explanation |
|---|-------|--------|-------------|
| 1 | `m=3, n=7` | `28` | 28 distinct paths from top-left to bottom-right |
| 2 | `m=3, n=2` | `3` | Right-Down-Down, Down-Right-Down, Down-Down-Right |
| 3 | `m=1, n=1` | `1` | Already at the destination |

### Constraints
- `1 <= m, n <= 100`

### Real-Life Analogy
You're in a city with a perfect grid of streets (like Manhattan). You need to walk from the northwest corner to the southeast corner. You can only walk **east** or **south** (no backtracking, no shortcuts). How many different routes can you take? Every route requires exactly `m-1` south moves and `n-1` east moves -- you're just choosing their order.

### 3 Key Observations

1. **"Aha!" -- Every path has the same length:** Any valid path makes exactly `(m-1)` down moves and `(n-1)` right moves, totaling `(m+n-2)` moves. We're just choosing which of those moves are "down."

2. **"Aha!" -- Each cell = sum of paths from above + left:** The number of ways to reach cell `(i, j)` equals the ways to reach `(i-1, j)` (came from above) plus the ways to reach `(i, j-1)` (came from left).

3. **"Aha!" -- This is a combinatorics problem too:** We need to choose `(m-1)` positions out of `(m+n-2)` total moves to be "down." That's `C(m+n-2, m-1)`. This gives an O(min(m,n)) solution with no DP at all!

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Recursion | Call stack | Try right/down at each cell | Explores all paths |
| Memoization | 2D array | Top-down + cache | Eliminates recomputation |
| Tabulation | 2D array `dp[i][j]` | Bottom-up fill | Classic grid DP |
| Space-Optimized | 1D array | Row-by-row computation | Only need previous row |
| Combinatorics | None | `C(m+n-2, m-1)` | Pure math, O(min(m,n)) |

**Pattern cue:** "Count paths in a grid with restricted moves" --> 2D DP where `dp[i][j]` accumulates from valid previous cells.

---

## APPROACH LADDER

### Approach 1: Recursion (Brute Force)

**State definition:** `paths(i, j)` = number of unique paths from `(0, 0)` to `(i, j)`.

**Recurrence:**
```
paths(i, j) =
    if i == 0 AND j == 0: return 1      // reached start (destination in reverse)
    if i < 0 OR j < 0: return 0         // out of bounds

    return paths(i - 1, j) + paths(i, j - 1)   // from above + from left
```

**Dry-run trace** for `m=3, n=3` (reaching `(2,2)`):
```
paths(2,2)
  paths(1,2)                    paths(2,1)
    paths(0,2)   paths(1,1)       paths(1,1)   paths(2,0)
      p(0,1)       p(0,1) p(1,0)   p(0,1) p(1,0)  p(1,0)
      +p(-1,2)     ...             ...             +p(2,-1)

paths(0,j) = 1 for all j (only way is all-right)
paths(i,0) = 1 for all i (only way is all-down)

So: paths(2,2) = 6
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(2^(m+n)) | Binary branching at each cell, depth m+n |
| Space | O(m + n) | Recursion depth |

---

### Approach 2: Memoization (Top-Down DP)

**Key insight:** `paths(i, j)` is called many times for the same `(i, j)`. There are only `m * n` unique states.

**State:** `dp[i][j]` = number of unique paths from `(0, 0)` to `(i, j)`.

```
dp[i][j]:
    if cached, return dp[i][j]
    if i == 0 or j == 0: return 1    // first row/col: only one path
    dp[i][j] = paths(i-1, j) + paths(i, j-1)
    return dp[i][j]
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(m * n) | Each of m*n cells computed once |
| Space | O(m * n) | 2D memo + recursion stack |

---

### Approach 3: Tabulation (Bottom-Up DP)

**State:** `dp[i][j]` = number of unique paths to reach cell `(i, j)`.

**Base cases:**
- `dp[0][j] = 1` for all `j` (first row: only rightward)
- `dp[i][0] = 1` for all `i` (first column: only downward)

**Recurrence:**
```
dp[i][j] = dp[i-1][j] + dp[i][j-1]    for i >= 1, j >= 1
```

**Dry-run trace** for `m=3, n=7`:
```
dp grid:
    j=0  j=1  j=2  j=3  j=4  j=5  j=6
i=0 [ 1    1    1    1    1    1    1  ]
i=1 [ 1    2    3    4    5    6    7  ]
i=2 [ 1    3    6   10   15   21   28 ]

dp[2][6] = 28
```

Notice: each cell is the sum of the cell above and the cell to the left. The grid fills with **Pascal's triangle** values rotated 45 degrees!

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(m * n) | Fill every cell once |
| Space | O(m * n) | 2D dp table |

---

### Approach 4: Space-Optimized (1D Array)

**Key insight:** When computing row `i`, we only need row `i-1`. We can use a single 1D array and update it in-place.

```
dp = [1, 1, 1, ..., 1]   // n elements, all 1 (first row)

for i from 1 to m-1:
    for j from 1 to n-1:
        dp[j] = dp[j] + dp[j-1]
        //      ^above   ^left (already updated for current row)
```

**Why this works:** When we process `dp[j]`, the old `dp[j]` still holds the value from the row above (not yet overwritten), and `dp[j-1]` already holds the current row's value (just computed). Perfect!

**Dry-run trace** for `m=3, n=4`:
```
Initial: dp = [1, 1, 1, 1]

i=1: j=1: dp[1]=1+1=2. j=2: dp[2]=1+2=3. j=3: dp[3]=1+3=4.
     dp = [1, 2, 3, 4]

i=2: j=1: dp[1]=2+1=3. j=2: dp[2]=3+3=6. j=3: dp[3]=4+6=10.
     dp = [1, 3, 6, 10]

Answer = dp[3] = 10
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(m * n) | Same nested loops |
| Space | O(n) | Single 1D array (or O(m) if we pick the smaller dimension) |

---

### Approach 5 (Bonus): Combinatorics

**Key insight:** Every path consists of exactly `(m-1)` down moves and `(n-1)` right moves. Total moves = `m + n - 2`. We just need to choose which `(m-1)` of them are "down" (the rest are "right").

**Formula:**
```
Answer = C(m+n-2, m-1) = (m+n-2)! / ((m-1)! * (n-1)!)
```

**Efficient computation** (avoid overflow by computing iteratively):
```
result = 1
for i in range(1, min(m, n)):
    result = result * (m + n - 2 - i + 1) / i
```

We pick `min(m-1, n-1)` multiplications to minimize computation.

**Example:** `m=3, n=7`:
```
C(8, 2) = 8! / (2! * 6!) = (8 * 7) / (2 * 1) = 28
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(min(m, n)) | Single loop with min(m-1, n-1) iterations |
| Space | O(1) | Just arithmetic |

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Analogy |
|----------|------|-------|---------|
| Recursion | O(2^(m+n)) | O(m+n) | Walking every possible route in the city |
| Memoization | O(m*n) | O(m*n) | Marking each intersection with "# routes from here" |
| Tabulation | O(m*n) | O(m*n) | Filling a spreadsheet cell by cell |
| Space-Optimized | O(m*n) | O(n) | Only keeping the current row of the spreadsheet |
| Combinatorics | O(min(m,n)) | O(1) | Just doing the math with combinations |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out |
|-----------|----------|-----------|
| `m=1, n=1` | `1` | Already at destination |
| `m=1, n=k` | `1` | Only one path: all right |
| `m=k, n=1` | `1` | Only one path: all down |
| `m=100, n=100` | Very large | May overflow int -- use long |
| `m=2, n=2` | `2` | Right-Down or Down-Right |

**Common mistakes:**
- Off-by-one: Grid is `m x n`, so indices go from `0` to `m-1` and `0` to `n-1`
- In combinatorics: integer overflow during multiplication. Divide as you go, don't compute full factorials first
- Forgetting that `dp[i][0] = 1` and `dp[0][j] = 1` (not 0)
- In space-optimized: starting inner loop at `j=1` not `j=0` (dp[0] stays 1 throughout)

---

## INTERVIEW LENS

**What interviewers are looking for:**
1. Can you set up the 2D DP correctly with proper base cases?
2. Can you optimize space from O(m*n) to O(n)?
3. Do you see the combinatorics angle? (Bonus points!)

**Follow-up questions:**
- "What if some cells are blocked (obstacles)?" --> **Unique Paths II** (LC #63): Set `dp[i][j] = 0` for obstacles
- "What if you can also move diagonally?" --> `dp[i][j] = dp[i-1][j] + dp[i][j-1] + dp[i-1][j-1]`
- "Minimum cost path instead of count?" --> **Minimum Path Sum** (LC #64): `dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])`

**Time management:** Code tabulation first (safe, hard to mess up), mention combinatorics verbally.

---

## CONNECTIONS

| Related Problem | Connection |
|----------------|------------|
| Unique Paths II (LC #63) | Same grid + obstacles |
| Minimum Path Sum (LC #64) | Same grid, min cost instead of count |
| Pascal's Triangle (LC #118) | The DP grid IS Pascal's triangle rotated |
| Dungeon Game (LC #174) | Grid DP but computed bottom-right to top-left |
| Combinatorics (nCr) | The answer is literally C(m+n-2, m-1) |
| Lattice Paths | Classic combinatorics problem in discrete math |
