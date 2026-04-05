# Pascal's Triangle

> **Batch 1 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an integer `numRows`, return the first `numRows` rows of **Pascal's Triangle**.

In Pascal's Triangle:
- Each row starts and ends with `1`.
- Every other element is the sum of the two elements directly above it from the previous row.

**Constraints:**
- `1 <= numRows <= 30`

**Examples:**

| Input | Output |
|-------|--------|
| `numRows = 5` | `[[1], [1,1], [1,2,1], [1,3,3,1], [1,4,6,4,1]]` |
| `numRows = 1` | `[[1]]` |
| `numRows = 3` | `[[1], [1,1], [1,2,1]]` |

Visual for `numRows = 5`:
```
        1
       1 1
      1 2 1
     1 3 3 1
    1 4 6 4 1
```

### Real-Life Analogy
> *Imagine a pyramid of cups stacked in rows. Each cup on the ground level is labeled "1". For every cup above the ground that sits on top of two cups below it, its label equals the sum of the two cups it rests on. The top cup has no neighbors, so it is simply "1". Building this pyramid from top to bottom, row by row, is exactly what this problem asks.*

### Key Observations
1. Row `i` has exactly `i + 1` elements (0-indexed).
2. The first and last elements of every row are always `1`.
3. Element `row[j]` = `prevRow[j-1] + prevRow[j]`. Each row is fully determined by the row above it. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need a 2D structure where each row can have a different length. A **List of Lists** is the natural choice.
- You might think a 2D array works, but rows have varying lengths (row 0 has 1 element, row 4 has 5), making a jagged list more natural.

### Pattern Recognition
- **Pattern:** Iterative Row-by-Row Construction (each row depends only on the previous row)
- **Classification Cue:** "When you see _build a structure where each layer depends on the previous layer_ --> think _iterative construction with a previous-state reference_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Using Combinatorics / nCr)
**Idea:** Each element at position `(row, col)` in Pascal's Triangle equals `C(row, col)` = `row! / (col! * (row - col)!)`. Compute each element independently using the combination formula.

**Steps:**
1. For each row `r` from 0 to numRows - 1:
   - For each column `c` from 0 to r:
     - Compute `C(r, c)` and add it to the current row.
   - Add the current row to the result.

**Why it works:** By definition, Pascal's Triangle element at (r, c) is C(r, c).

**Why we move on:** **Unnecessary work** -- computing factorials or repeated multiplications for each element is wasteful when we can derive each element from its neighbors in the previous row in O(1).

| Time | Space |
|------|-------|
| O(numRows^2) per element if using naive factorial; O(numRows^2) total with optimized nCr | O(numRows^2) for the output |

### Approach 2: Optimal -- Iterative Row Construction
**What changed:** Build each row using the previous row. Element `j` of the current row = `prevRow[j-1] + prevRow[j]`, with boundary elements set to 1.

**Steps:**
1. Initialize `result = [[1]]` (first row).
2. For each row `r` from 1 to numRows - 1:
   - Start a new row with `[1]`.
   - For `j` from 1 to r - 1: append `prevRow[j-1] + prevRow[j]`.
   - Append `1` at the end.
   - Add this row to `result`.
3. Return `result`.

**Dry Run:** numRows = 5

| Row r | prevRow | Computation | New Row |
|-------|---------|-------------|---------|
| 0     | -       | Base case   | [1] |
| 1     | [1]     | [1, 1]      | [1, 1] |
| 2     | [1, 1]  | [1, 1+1, 1] | [1, 2, 1] |
| 3     | [1, 2, 1] | [1, 1+2, 2+1, 1] | [1, 3, 3, 1] |
| 4     | [1, 3, 3, 1] | [1, 1+3, 3+3, 3+1, 1] | [1, 4, 6, 4, 1] |

**Result:** `[[1], [1,1], [1,2,1], [1,3,3,1], [1,4,6,4,1]]`

| Time | Space |
|------|-------|
| O(numRows^2) | O(numRows^2) for the output |

### Approach 3: Best -- Row Construction with Optimized nCr
**Core idea in one sentence:** "Each element in a row can be computed from the previous element in the same row using `C(r, c) = C(r, c-1) * (r - c + 1) / c`, avoiding the need to reference the previous row at all."

**Steps:**
1. For each row `r`:
   - Start with `val = 1` (which is C(r, 0)).
   - For `c` from 1 to r: compute `val = val * (r - c + 1) / c`.
   - Collect all values into the row.

**Dry Run:** Row r = 4

| c | val (before) | Computation | val (after) |
|---|-------------|-------------|-------------|
| 0 | 1           | Base        | 1           |
| 1 | 1           | 1 * 4 / 1   | 4           |
| 2 | 4           | 4 * 3 / 2   | 6           |
| 3 | 6           | 6 * 2 / 3   | 4           |
| 4 | 4           | 4 * 1 / 4   | 1           |

Row = `[1, 4, 6, 4, 1]`

| Time | Space |
|------|-------|
| O(numRows^2) | O(numRows^2) for the output |

*Note:* All approaches are O(numRows^2) because the output itself has O(numRows^2) elements. The difference is in constant factors and elegance. Approach 2 is the most commonly expected in interviews. Approach 3 is a neat math trick.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(numRows^2) -- "Row r has r+1 elements. Total elements = 1 + 2 + ... + numRows = numRows * (numRows + 1) / 2, which is O(numRows^2)."
**Space:** O(numRows^2) -- "We store all elements of the triangle, which is O(numRows^2) total."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Off-by-one on row count --> `numRows = 1` should return `[[1]]`, not `[[1], [1, 1]]`. Be clear whether numRows is 0-indexed or 1-indexed.
2. Forgetting to handle `numRows = 0` --> Some variants ask for 0 rows; return an empty list.
3. Integer overflow in nCr approach --> For large rows, factorials overflow. The multiplicative formula (Approach 3) or iterative approach (Approach 2) avoids this.

### Edge Cases to Test
- [ ] `numRows = 1` --> `[[1]]`
- [ ] `numRows = 2` --> `[[1], [1, 1]]`
- [ ] `numRows = 30` (max constraint) --> large triangle, check last row values
- [ ] Verify symmetry: each row should be a palindrome

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is numRows 1-indexed (numRows = 1 means one row)? Do you want me to return the entire triangle or just the nth row?"
2. **Approach:** "I'll build the triangle row by row. Each row's interior elements are the sum of two adjacent elements from the previous row. This is O(numRows^2), which matches the output size."
3. **Code:** Narrate while writing. Say: "I initialize with [[1]]. For each subsequent row, I start and end with 1, and fill the middle by summing pairs from the previous row."
4. **Test:** Draw the first 5 rows on the whiteboard, showing how each element derives from the row above.

### Follow-Up Questions
- "What if I only need the nth row?" --> Use a single array and update it in-place from right to left, O(n) space.
- "What if I need element (r, c)?" --> Use the multiplicative nCr formula directly, O(c) time.
- "How does this relate to binomial coefficients?" --> Pascal's Triangle row r gives the coefficients of (a + b)^r.

---

## CONNECTIONS
- **Prerequisite:** Basic 2D array/list manipulation, understanding of nested loops
- **Same Pattern:** Building structures layer-by-layer (BFS levels, DP tables)
- **Harder Variant:** Pascal's Triangle II (only return nth row with O(n) space), Combinatorics-based problems
- **This Unlocks:** Understanding DP table construction, binomial coefficients, combinatorics
