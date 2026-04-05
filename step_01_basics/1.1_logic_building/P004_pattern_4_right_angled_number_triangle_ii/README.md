# Pattern 4 - Right Angled Number Triangle II

> **Batch 2 of 12** | **Topic:** Logic Building (Patterns) | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer N, print a right-angled triangle where numbers are printed **sequentially** (1, 2, 3, ...) across all rows. Row `i` has `i` numbers.

**Example:**
```
Input: N = 5
Output:
1
2 3
4 5 6
7 8 9 10
11 12 13 14 15
```

| Input | Output | Explanation |
|---|---|---|
| 1 | `1` | Single number |
| 3 | `1` / `2 3` / `4 5 6` | Numbers 1-6 fill 3 rows |
| 4 | `1` / `2 3` / `4 5 6` / `7 8 9 10` | Numbers 1-10 fill 4 rows |

### Real-Life Analogy
Imagine numbering seats in a theater with triangular seating. Row 1 has 1 seat, row 2 has 2 seats, row 3 has 3 seats, and so on. You number them sequentially starting from 1, going left to right, row by row. The number does not reset at each row -- it keeps incrementing.

### Key Observations
1. Unlike Pattern 3 (where the printed value = row number), here the printed value is a **running counter** that increments across all positions.
2. The total numbers printed is 1 + 2 + ... + N = N(N+1)/2.
3. **Aha moment:** We need a single counter variable that persists across rows. The outer loop controls rows and the inner loop controls columns, but the counter is independent of both -- it just increments on every print.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why a Running Counter?
The key insight is that the value printed is not determined by the row or column index alone -- it is a global counter. We maintain one variable `num` that starts at 1 and increments with each print. The nested loops simply control the shape (how many numbers per row).

### Pattern Recognition
**Classification cue:** "Sequential numbers across rows" --> nested loops with a running counter. Contrast with Pattern 3 where the value depends only on the row index.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force / Optimal -- Nested Loops with Counter
**Intuition:** Use nested loops for the triangular shape. Maintain a running counter that increments on every print, independent of row/column indices.

**Steps:**
1. Initialize `num = 1`.
2. Outer loop: `i` from 1 to N (rows).
3. Inner loop: `j` from 1 to `i` (columns in this row).
   - Print `num`, then increment `num`.
4. Print a newline after the inner loop.

**Dry Run Trace (N = 4):**

| Row (i) | j values | num before | Output | num after |
|---------|----------|------------|--------|-----------|
| 1 | 1 | 1 | `1` | 2 |
| 2 | 1, 2 | 2 | `2 3` | 4 |
| 3 | 1, 2, 3 | 4 | `4 5 6` | 7 |
| 4 | 1, 2, 3, 4 | 7 | `7 8 9 10` | 11 |

| Metric | Value |
|--------|-------|
| Time | O(N^2) -- total prints = N(N+1)/2 |
| Space | O(1) |

**BUD Transition:** O(N^2) is inherently optimal because we must print N(N+1)/2 numbers. No approach can do fewer prints.

---

### Approach 2: Formula-Based (Compute Starting Number per Row)
**Intuition:** Instead of a running counter, compute the starting number of each row using the formula: row `i` starts at `i*(i-1)/2 + 1`. This allows each row to be computed independently (useful for parallel printing or random access).

**Steps:**
1. For each row `i` from 1 to N:
   - Compute `start = i * (i - 1) / 2 + 1`.
   - Print numbers from `start` to `start + i - 1`.

| Metric | Value |
|--------|-------|
| Time | O(N^2) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total numbers printed = 1 + 2 + ... + N = N(N+1)/2. Each number requires a print operation. This is unavoidably O(N^2).

**Why O(1) space?** We only use a counter variable (or compute from a formula). No arrays or strings stored.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Resetting counter each row | Confusing with Pattern 3 where each row prints its row number | Keep `num` outside the outer loop, never reset it |
| Using row index as value | Copy-pasting Pattern 3 code | Print `num++` instead of `i` |
| Off-by-one in formula approach | Wrong formula for row start | Row i starts at i*(i-1)/2 + 1 (1-indexed) |

### Edge Cases Checklist
- N = 1 --> single line: `1`
- N = 0 --> no output
- Large N --> total numbers = N(N+1)/2, verify no integer overflow for the counter

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Numbers are sequential across rows, not restarting each row?"
2. **M**atch: "Pattern printing with running counter --> nested loops + global counter."
3. **P**lan: "Maintain a counter starting at 1. Outer loop for rows, inner loop prints and increments."
4. **I**mplement: Two loops, one counter variable.
5. **R**eview: Trace N=3 to verify 1 / 2 3 / 4 5 6.
6. **E**valuate: "O(N^2) time, O(1) space. Cannot improve since we must print every number."

### Follow-Up Questions
- "What if you need the number at position (row, col) without printing the whole triangle?" --> Formula: value = row*(row-1)/2 + col.
- "What if the triangle should be inverted (N numbers in row 1, 1 in row N)?" --> Reverse the inner loop bounds, adjust counter direction.
- "What if numbers go in zigzag (left-to-right on odd rows, right-to-left on even)?" --> Store the row in an array, reverse on even rows.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Pattern 3 (row-number triangle) |
| **Same pattern** | Any sequential-fill pattern, spiral matrix filling |
| **Harder variant** | Pascal's Triangle, Zigzag patterns |
| **Unlocks** | Matrix traversal (row-major order), spiral order print |
