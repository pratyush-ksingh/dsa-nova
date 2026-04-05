# Triangle Minimum Path Sum

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a `triangle` array, find the **minimum path sum** from top to bottom. At each step, you may move to an adjacent number on the row below. Specifically, if you are at index `j` in the current row, you can move to index `j` or `j+1` in the next row.

**LeetCode #120**

**Key Insight:** Start the DP from the **bottom** of the triangle, not the top. This avoids complex boundary handling and makes space optimization trivial -- the last row becomes the initial dp array, and we compress upward.

**Constraints:**
- `1 <= triangle.length <= 200`
- `triangle[i].length == i + 1`
- `-10^4 <= triangle[i][j] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[2],[3,4],[6,5,7],[4,1,8,3]]` | `11` | Path: 2->3->5->1 = 11 |
| `[[-10]]` | `-10` | Single element |
| `[[1],[2,3]]` | `3` | Path: 1->2 = 3 |

**Triangle visualization:**
```
    2
   3 4
  6 5 7
 4 1 8 3
```

Best path: 2 -> 3 -> 5 -> 1 = 11

### Real-Life Analogy
> *Imagine you are standing at the peak of a mountain, and you need to descend to the base. At each level, you can step to either of two adjacent footholds below you. Each foothold has a different "effort cost." You want the path that requires the least total effort. Starting from the bottom and computing upward is like asking each foothold: "what's the cheapest way to reach the base from here?"*

### Key Observations
1. **Variable width:** Row `i` has `i+1` elements. This is not a rectangular grid.
2. **Adjacent constraint:** From `triangle[i][j]`, you can go to `triangle[i+1][j]` or `triangle[i+1][j+1]`.
3. **Bottom-up is easier:** Define `dp[i][j]` = minimum path sum from `(i, j)` to any element in the bottom row. Then `dp[i][j] = triangle[i][j] + min(dp[i+1][j], dp[i+1][j+1])`.
4. **Answer:** `dp[0][0]`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Top-down recursion** starts from `(0, 0)` and explores all paths to the bottom. Exponential without caching.
- **Bottom-up DP** starts from the last row and works upward. Each cell only depends on the two cells directly below it.
- **Space optimization** is elegant: use the bottom row as the initial dp array, then fold upward in place.

### Pattern Recognition
- **Pattern:** Variable-width grid DP (triangle)
- **Classification Cue:** "When you see _min path in triangle, adjacent moves_ --> think _bottom-up DP, dp[j] = val + min(dp[j], dp[j+1])_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion (Top-Down)
**Idea:** Start at `(0, 0)`, try both children `(i+1, j)` and `(i+1, j+1)`. Return minimum.

**Steps:**
1. `solve(i, j)` = minimum path sum from `(i, j)` to the bottom.
2. Base case: `i == n-1` (bottom row), return `triangle[i][j]`.
3. Return `triangle[i][j] + min(solve(i+1, j), solve(i+1, j+1))`.

**Why it is slow:** Binary branching, 2^n paths.

```
        solve(0,0)
       /          \
  solve(1,0)    solve(1,1)
  /       \     /       \
s(2,0) s(2,1) s(2,1) s(2,2)
               ^ overlap!
```

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** `solve(2,1)` is called twice. Cache it.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][j]`.

**Steps:**
1. Create `dp[n][n]`, initialized to -1 (or use a hash map since rows have different lengths).
2. Same recursion with cache check.

**Dry Run:** `triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]`

| Call | (i,j) | Cached? | Compute | Result |
|------|--------|---------|---------|--------|
| solve(0,0) | (0,0) | No | 2 + min(solve(1,0), solve(1,1)) | ? |
| solve(1,0) | (1,0) | No | 3 + min(solve(2,0), solve(2,1)) | ? |
| solve(2,0) | (2,0) | No | 6 + min(solve(3,0), solve(3,1)) | 6+min(4,1)=7 |
| solve(2,1) | (2,1) | No | 5 + min(solve(3,1), solve(3,2)) | 5+min(1,8)=6 |
| back(1,0) | | | 3 + min(7, 6) = 9 | 9 |
| solve(1,1) | (1,1) | No | 4 + min(solve(2,1), solve(2,2)) | ? |
| solve(2,1) | (2,1) | **Yes** | return 6 | 6 |
| solve(2,2) | (2,2) | No | 7 + min(solve(3,2), solve(3,3)) | 7+min(8,3)=10 |
| back(1,1) | | | 4 + min(6, 10) = 10 | 10 |
| back(0,0) | | | 2 + min(9, 10) = **11** | 11 |

| Time | Space |
|------|-------|
| O(n^2) total cells | O(n^2) cache + O(n) stack |

**BUD Transition:** Go bottom-up to eliminate recursion.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Start from the bottom row and work upward.

**Steps:**
1. Create `dp[n][n]`. Copy bottom row: `dp[n-1][j] = triangle[n-1][j]`.
2. For `i = n-2` down to `0`:
   - For `j = 0` to `i`:
     - `dp[i][j] = triangle[i][j] + min(dp[i+1][j], dp[i+1][j+1])`.
3. Return `dp[0][0]`.

**Dry Run:**
```
Start:  dp[3] = [4, 1, 8, 3]

i=2:    dp[2][0] = 6 + min(4,1) = 7
        dp[2][1] = 5 + min(1,8) = 6
        dp[2][2] = 7 + min(8,3) = 10

i=1:    dp[1][0] = 3 + min(7,6) = 9
        dp[1][1] = 4 + min(6,10) = 10

i=0:    dp[0][0] = 2 + min(9,10) = 11
```

| Time | Space |
|------|-------|
| O(n^2) | O(n^2) |

**BUD Transition:** Row `i` only depends on row `i+1`. Use a single array!

### Approach 4: Space Optimized
**What changed:** Use a single 1D array initialized to the bottom row. Update in place going upward.

**Steps:**
1. `dp = copy of bottom row` (length = n).
2. For `i = n-2` down to `0`:
   - For `j = 0` to `i`:
     - `dp[j] = triangle[i][j] + min(dp[j], dp[j+1])`.
3. Return `dp[0]`.

**Why this works without conflicts:** We process `j` from 0 to `i`. `dp[j]` uses `dp[j]` and `dp[j+1]` from the row below. Since we update `dp[j]` first and `dp[j+1]` hasn't been touched yet, there are no overwrites.

**Dry Run:**
```
dp = [4, 1, 8, 3]   (bottom row)

i=2: dp[0] = 6 + min(4,1) = 7      -> [7, 1, 8, 3]
     dp[1] = 5 + min(1,8) = 6      -> [7, 6, 8, 3]
     dp[2] = 7 + min(8,3) = 10     -> [7, 6, 10, 3]

i=1: dp[0] = 3 + min(7,6) = 9      -> [9, 6, 10, 3]
     dp[1] = 4 + min(6,10) = 10    -> [9, 10, 10, 3]

i=0: dp[0] = 2 + min(9,10) = 11    -> [11, 10, 10, 3]

Answer: dp[0] = 11
```

| Time | Space |
|------|-------|
| O(n^2) | **O(n)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each node branches into 2, tree has n levels: O(2^n) paths."
**Memo/Tab:** "n^2 / 2 cells in the triangle, O(1) work per cell: O(n^2)."
**Space Optimized:** "Same O(n^2) time. Only store one row of at most n elements: O(n) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Top-down vs bottom-up direction:** Top-down DP (from peak to base) requires tracking variable starting points at the bottom. Bottom-up (from base to peak) naturally converges to a single answer at `dp[0][0]`.
2. **Adjacent indices:** From `(i, j)`, you go to `(i+1, j)` and `(i+1, j+1)`, NOT `(i+1, j-1)` and `(i+1, j+1)`.
3. **Negative values:** The triangle can contain negative numbers. Don't initialize dp to 0.
4. **Space optimization direction:** Process `j` left to right (0 to i), since `dp[j+1]` is needed and hasn't been overwritten yet.

### Edge Cases to Test
- [ ] Single element --> return that element
- [ ] Two rows --> compare two paths
- [ ] All negative numbers --> still works (min of negatives)
- [ ] Very tall, thin triangle (200 rows)

---

## INTERVIEW LENS

### How to Present
1. **Clarify adjacency:** "From position j, I can move to j or j+1 in the next row."
2. **Choose direction:** "I'll process bottom-up because it naturally gives one answer at the top, and space optimization is straightforward."
3. **Build the DP:** "Initialize dp with the bottom row. For each row above, dp[j] = triangle[i][j] + min(dp[j], dp[j+1])."
4. **Optimize space:** "I only need the row below, so I update dp in place. Process left-to-right to avoid overwriting dp[j+1] before it's used."

### Follow-Up Questions
- "Can you do this in O(1) extra space?" --> Yes, modify the input triangle in-place. But mention the trade-off.
- "What if you need the actual path?" --> Track choices in a separate array, or backtrack from the top comparing dp values.
- "What if moves include diagonal left?" --> Add `dp[i+1][j-1]` (with bounds check) as a third option.

---

## CONNECTIONS
- **Prerequisite:** Minimum Path Sum (rectangular grid)
- **Same Pattern:** Dungeon Game, Paint House
- **This Unlocks:** Maximum path sum in triangle, Falling Squares
- **Harder Variant:** Minimum Falling Path Sum (LeetCode 931), Falling Path Sum II (1289)
