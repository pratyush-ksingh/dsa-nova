# Find Row with Maximum 1s

> **Batch 4 of 12** | **Topic:** Binary Search on 2D Arrays | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a binary matrix (2D array) of size `m x n` where each row is sorted in non-decreasing order (all 0s come before all 1s), find the **row index** with the maximum number of 1s. If multiple rows have the same number of 1s, return the one with the smallest index.

### Examples

| Input                                          | Output | Explanation                             |
|------------------------------------------------|--------|-----------------------------------------|
| `[[0,1,1,1],[0,0,1,1],[1,1,1,1],[0,0,0,0]]`  | 2      | Row 2 has 4 ones (most)                 |
| `[[0,0],[0,1]]`                                | 1      | Row 1 has 1 one, row 0 has 0           |
| `[[1,1],[1,1]]`                                | 0      | Tie: return smallest index              |
| `[[0,0,0],[0,0,0]]`                            | 0      | No 1s anywhere; return 0 (or -1)       |

### Analogy
Imagine rows of seats in a theater where people stand up (1) or sit down (0). Each row has all the standing people on the right side. You want to find which row has the most people standing. Instead of counting every person, you can start from the top-right corner and navigate: if you see a standing person, move left (more standing people possible); if you see a seated person, move down (this row has fewer).

### 3 Key Observations
1. **"Aha" -- Each row is sorted, so the 1s form a suffix.** The number of 1s in a row = `n - firstIndexOf(1)`. Use binary search to find the first 1.
2. **"Aha" -- Binary search per row gives O(m log n).** For each row, binary search for the leftmost 1. Track the row with the smallest first-1 index.
3. **"Aha" -- The staircase approach gives O(m + n).** Start at the top-right corner. Move left while seeing 1, move down when seeing 0. You never visit more than m + n cells.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm             | Why?                                       |
|------------------|---------------|-----------------------|--------------------------------------------|
| Brute Force      | None          | Count 1s per row      | O(m*n), no use of sorted property          |
| Optimal          | None          | Binary search per row | O(m log n), exploits sorted rows           |
| Best             | None          | Staircase traversal   | O(m + n), single pass from top-right       |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Count Each Row
**Intuition:** Iterate through every cell. Count 1s in each row. Track the maximum.

**Steps:**
1. For each row `i`:
   - Count the number of 1s: `sum(matrix[i])`.
   - If count > maxCount, update maxCount and bestRow.
2. Return bestRow.

**Dry-Run Trace** with `[[0,1,1,1],[0,0,1,1],[1,1,1,1],[0,0,0,0]]`:
```
Row 0: count=3, maxCount=3, bestRow=0
Row 1: count=2, maxCount=3, bestRow=0
Row 2: count=4, maxCount=4, bestRow=2
Row 3: count=0, maxCount=4, bestRow=2
Answer: 2
```

| Metric | Value  |
|--------|--------|
| Time   | O(m*n) |
| Space  | O(1)   |

---

### Approach 2: Optimal -- Binary Search per Row
**Intuition:** Since each row is sorted, use binary search to find the index of the first 1. The number of 1s = `n - firstOneIndex`. Track the row with the largest count.

**Steps:**
1. For each row `i`:
   - Binary search for the leftmost 1 in `matrix[i]`.
   - Count = `n - firstOneIndex` (if no 1 found, count = 0).
   - If count > maxCount, update.
2. Return bestRow.

**Binary search for leftmost 1:** Standard lower_bound search for value 1.

**Dry-Run Trace** with `[[0,1,1,1],[0,0,1,1],[1,1,1,1],[0,0,0,0]]`:
```
Row 0: BS finds first 1 at index 1 -> count=3, best=(0,3)
Row 1: BS finds first 1 at index 2 -> count=2, best=(0,3)
Row 2: BS finds first 1 at index 0 -> count=4, best=(2,4)
Row 3: BS finds no 1 -> count=0, best=(2,4)
Answer: 2
```

| Metric | Value      |
|--------|------------|
| Time   | O(m log n) |
| Space  | O(1)       |

---

### Approach 3: Best -- Staircase (Top-Right Traversal)
**Intuition:** Start at the top-right corner `(0, n-1)`. If the current cell is 1, move left (the current row might have more 1s, and this column is "beaten"). If it is 0, move down (this row cannot beat the current best). This way, we traverse at most m + n cells.

**Why it works:** Moving left means the current row has at least one more 1 than we previously knew. Moving down means we skip a row that cannot beat the current best. We effectively find the row whose 1-region extends furthest left.

**Steps:**
1. `row = 0`, `col = n - 1`, `bestRow = 0`.
2. While `row < m` and `col >= 0`:
   - If `matrix[row][col] == 1`:
     - `bestRow = row` (this row has 1s extending at least to col).
     - Move left: `col--`.
   - Else:
     - Move down: `row++`.
3. Return `bestRow`.

**Dry-Run Trace** with `[[0,1,1,1],[0,0,1,1],[1,1,1,1],[0,0,0,0]]`:
```
Start: (0,3) -> mat[0][3]=1, bestRow=0, move left -> (0,2)
       (0,2) -> mat[0][2]=1, bestRow=0, move left -> (0,1)
       (0,1) -> mat[0][1]=1, bestRow=0, move left -> (0,0)
       (0,0) -> mat[0][0]=0, move down -> (1,0)
       (1,0) -> mat[1][0]=0, move down -> (2,0)
       (2,0) -> mat[2][0]=1, bestRow=2, move left -> (2,-1)
       col < 0, stop
Answer: 2
```

| Metric | Value   |
|--------|---------|
| Time   | O(m+n)  |
| Space  | O(1)    |

---

## COMPLEXITY INTUITIVELY

- **Why O(m+n) is optimal:** In the worst case, we need to look at all m rows and all n columns to determine which row wins. The staircase does exactly that in a single sweep.
- **Why it beats O(m log n):** For large n, `m + n < m * log(n)`. The staircase avoids redundant binary searches on rows that cannot beat the current best.

---

## EDGE CASES & MISTAKES

| Edge Case              | What Happens                                      |
|------------------------|--------------------------------------------------|
| All zeros matrix       | No 1s anywhere. Return 0 (or -1 per problem).   |
| All ones matrix        | First row wins (smallest index for ties).        |
| Single row             | Binary search or scan that one row.              |
| Single column          | Check each row's single element.                 |
| Multiple rows tied     | Return the smallest row index.                   |

**Common Mistakes:**
- Starting the staircase from the wrong corner (must be top-right or bottom-left).
- Not updating `bestRow` when moving left (each leftward step means current row is better).
- Binary search returning `n` when no 1 exists (must handle this as count = 0).

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests ability to exploit sorted structure in 2D matrices and the staircase traversal pattern.
- **Follow-ups:**
  - "What if rows are not sorted?" (Must count each row, O(m*n).)
  - "Find row with maximum 0s." (Mirror: start top-left, similar logic.)
  - "Search for a target in this matrix." (Related problem, also uses staircase.)
- **Communication tip:** Describe the staircase approach as "pruning rows that cannot beat the current champion." Draw the movement on the matrix.

---

## CONNECTIONS

| Related Problem                 | How It Connects                                 |
|--------------------------------|------------------------------------------------|
| Search in 2D Matrix (LC #74)  | Same sorted-matrix structure, different query  |
| Search in 2D Matrix II (LC #240)| Uses the same staircase approach              |
| Kth Smallest in Sorted Matrix  | Another 2D exploitation of sorted properties  |

---

## Real-World Use Case
**Database indexing / Bitmap indexes:** In column-store databases, each attribute's bitmap is a sorted binary row. Finding the attribute with the most "true" entries is analogous to this problem. The staircase technique corresponds to efficient index scanning in B-tree structures.
