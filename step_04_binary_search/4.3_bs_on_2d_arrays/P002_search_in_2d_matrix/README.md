# Search in 2D Matrix

> **Batch 4 of 12** | **Topic:** Binary Search on 2D Arrays | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are given an `m x n` matrix with the following properties:
- Each row is sorted in ascending order.
- The first element of each row is greater than the last element of the previous row.

Given a `target` integer, return `true` if it exists in the matrix, `false` otherwise. Write an algorithm with O(log(m * n)) time complexity.

**LeetCode #74**

### Examples

| Input                                              | Target | Output | Explanation                       |
|----------------------------------------------------|--------|--------|-----------------------------------|
| `[[1,3,5,7],[10,11,16,20],[23,30,34,60]]`        | 3      | true   | Found at row 0, col 1            |
| `[[1,3,5,7],[10,11,16,20],[23,30,34,60]]`        | 13     | false  | Not in the matrix                 |
| `[[1]]`                                            | 1      | true   | Single element match              |
| `[[1,3]]`                                          | 2      | false  | Not found                         |

### Analogy
The entire matrix, if you read row by row, forms a single sorted array. Searching it is like searching a book's index: first find the right chapter (row), then find the right page (column). Or even simpler: treat the whole matrix as one long sorted list and do a single binary search with index translation.

### 3 Key Observations
1. **"Aha" -- The matrix is a flattened sorted array.** Because each row is sorted AND each row starts after the previous row ends, reading left-to-right top-to-bottom gives a fully sorted sequence of `m * n` elements.
2. **"Aha" -- Map 1D index to 2D.** For a virtual index `idx` in `[0, m*n-1]`: `row = idx / n`, `col = idx % n`. This lets us binary search as if it were a 1D array.
3. **"Aha" -- Two binary searches also work.** First binary search on the first column to find the correct row, then binary search within that row. Same O(log(m*n)) = O(log m + log n).

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm                        | Why?                                    |
|------------------|---------------|----------------------------------|-----------------------------------------|
| Brute Force      | None          | Linear scan                      | O(m*n), ignores sorted property         |
| Optimal          | None          | Two binary searches              | O(log m + log n), conceptually clear    |
| Best             | None          | Single binary search (flattened) | O(log(m*n)), cleanest implementation    |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Intuition:** Check every element one by one.

**Steps:**
1. For each row `i`, for each column `j`:
   - If `matrix[i][j] == target`, return `true`.
2. Return `false`.

| Metric | Value  |
|--------|--------|
| Time   | O(m*n) |
| Space  | O(1)   |

---

### Approach 2: Optimal -- Two Binary Searches
**Intuition:** First, find which row the target could be in (the row where `row_start <= target <= row_end`). Then binary search within that row.

**Steps:**
1. **Find the row:** Binary search on the first column. Find the row `r` where `matrix[r][0] <= target <= matrix[r][n-1]`.
   - `lo = 0`, `hi = m - 1`
   - While `lo <= hi`:
     - `mid = (lo + hi) / 2`
     - If `target < matrix[mid][0]`: `hi = mid - 1`
     - If `target > matrix[mid][n-1]`: `lo = mid + 1`
     - Else: `r = mid`, break.
   - If no valid row found, return `false`.
2. **Search within row `r`:** Standard binary search for `target` in `matrix[r]`.

**Dry-Run Trace** with `matrix=[[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=3`:
```
Step 1 - Find row:
  lo=0 hi=2, mid=1: mat[1][0]=10 > 3 -> hi=0
  lo=0 hi=0, mid=0: mat[0][0]=1 <= 3 <= mat[0][3]=7 -> row=0

Step 2 - Search row 0: [1,3,5,7]
  lo=0 hi=3, mid=1: mat[0][1]=3 == 3 -> FOUND!
```

| Metric | Value            |
|--------|------------------|
| Time   | O(log m + log n) |
| Space  | O(1)             |

---

### Approach 3: Best -- Single Binary Search (Flattened)
**Intuition:** Treat the entire matrix as a sorted 1D array of length `m * n`. Binary search on virtual indices `[0, m*n - 1]`. Convert virtual index to 2D coordinates: `row = idx / n`, `col = idx % n`.

**Steps:**
1. `lo = 0`, `hi = m * n - 1`.
2. While `lo <= hi`:
   - `mid = (lo + hi) / 2`
   - `row = mid / n`, `col = mid % n`
   - `val = matrix[row][col]`
   - If `val == target`, return `true`.
   - If `val < target`, `lo = mid + 1`.
   - Else `hi = mid - 1`.
3. Return `false`.

**Dry-Run Trace** with `matrix=[[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=3`:
```
m=3, n=4, total=12
lo=0 hi=11, mid=5 -> row=1,col=1 -> val=11 > 3 -> hi=4
lo=0 hi=4,  mid=2 -> row=0,col=2 -> val=5 > 3  -> hi=1
lo=0 hi=1,  mid=0 -> row=0,col=0 -> val=1 < 3  -> lo=1
lo=1 hi=1,  mid=1 -> row=0,col=1 -> val=3 == 3  -> FOUND!
```

| Metric | Value         |
|--------|---------------|
| Time   | O(log(m*n))   |
| Space  | O(1)          |

---

## COMPLEXITY INTUITIVELY

- **O(log(m*n)) = O(log m + log n):** These are mathematically equivalent. log(m*n) = log(m) + log(n). So Approaches 2 and 3 have identical asymptotic complexity. Approach 3 is simpler to code.
- **Why O(1) space:** We only use index variables. No extra data structures.

---

## EDGE CASES & MISTAKES

| Edge Case              | What Happens                                      |
|------------------------|--------------------------------------------------|
| Single element matrix  | One comparison, return true/false.                |
| Target < smallest      | Binary search immediately goes to `hi < lo`.     |
| Target > largest       | Binary search immediately goes to `lo > hi`.     |
| Target between rows    | Two-BS approach: no valid row found, return false.|
| 1 row matrix           | Degenerates to standard binary search.            |
| 1 column matrix        | Degenerates to standard binary search.            |

**Common Mistakes:**
- Using integer division `mid / n` incorrectly (double-check the mapping).
- In two-BS approach, the row-finding binary search has three branches (not two), which is unusual.
- Confusing this with LC #240 (Search a 2D Matrix II) which has different sorted properties.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests ability to see the matrix as a flattened sorted array and apply index mapping. Also tests binary search precision.
- **Follow-ups:**
  - "What if each row is sorted and each column is sorted, but rows don't connect?" (LC #240 -- use staircase approach instead.)
  - "Find the kth smallest element in this matrix." (Binary search on value + counting.)
  - "What if the matrix is sorted in descending order?" (Reverse the comparison logic.)
- **Communication tip:** State the key insight immediately: "The matrix is effectively a flattened sorted array because each row starts after the previous row ends." Then present the index mapping formula.

---

## CONNECTIONS

| Related Problem                    | How It Connects                                |
|-----------------------------------|------------------------------------------------|
| Search a 2D Matrix II (LC #240)  | Different sorted property, uses staircase      |
| Find Row with Max 1s              | Same matrix structure, different query         |
| Kth Smallest in Sorted Matrix    | Binary search on 2D sorted matrices            |
| Binary Search (LC #704)          | Foundation -- this is just BS with index mapping|

---

## Real-World Use Case
**Spreadsheet / Database storage:** Tables stored in row-major order with sorted rows (like a B-tree leaf page) can be searched efficiently using this technique. Database engines perform exactly this kind of index-to-page-to-slot mapping when looking up records in a sorted file.
