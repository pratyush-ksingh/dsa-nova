# Pattern 21 - Hollow Rectangle

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given integers **N** (rows) and **M** (columns), print an N x M rectangle made of `*` where only the **border** cells contain stars; interior cells are spaces.

**Example (N=4, M=5):**
```
*****
*   *
*   *
*****
```

| Input     | Output                                              | Explanation                            |
|-----------|-----------------------------------------------------|----------------------------------------|
| N=1, M=5  | `*****`                                             | Single row -- entire row is border     |
| N=2, M=4  | `****`<br>`****`                                    | Two rows -- both are border rows       |
| N=4, M=5  | `*****` / `*   *` / `*   *` / `*****`              | First & last row solid; middle hollow  |
| N=5, M=1  | `*` x5                                              | Single column -- all cells are border  |

### Real-Life Analogy
Think of a **picture frame**: a frame has a thick wooden border but is empty in the middle where the picture goes. The outer rows and columns are the frame; the interior is empty.

### Key Observations
1. A cell is a border cell if: `row == 0` OR `row == N-1` OR `col == 0` OR `col == M-1`.
2. The first and last rows are fully filled; middle rows are `*<spaces>*`.
3. **Aha moment:** Instead of checking each cell individually, just pre-build two row strings (border and middle) and select which to print.
4. Special cases: N=1 or N=2 have no true "middle" rows.

### Constraints
- 1 <= N, M <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops / Pre-built Strings?
We need a 2D output. The hollow property means we can separate rows into two types (border vs. middle). Pre-building each row type eliminates re-computing the same string N-2 times.

### Pattern Recognition
**Classification cue:** "Print a bordered/hollow 2D shape" --> boundary condition on row/column indices, or pre-compute two row types.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Cell-by-Cell Boundary Check
**Intuition:** For every cell (i, j), decide if it is on the border using four conditions. If yes, print `*`; otherwise print a space. The most explicit approach -- no pre-computation.

**Steps:**
1. Loop `i` from 0 to N-1 (rows).
2. Loop `j` from 0 to M-1 (columns).
3. If `i==0 or i==N-1 or j==0 or j==M-1`, print `*`; else print ` `.
4. After inner loop, print newline.

**Dry Run Trace (N=3, M=4):**

| i | j=0 | j=1 | j=2 | j=3 |
|---|-----|-----|-----|-----|
| 0 | *   | *   | *   | *   |
| 1 | *   | sp  | sp  | *   |
| 2 | *   | *   | *   | *   |

| Metric | Value  |
|--------|--------|
| Time   | O(N*M) |
| Space  | O(1)   |

**BUD Transition:** Build two strings once rather than recomputing conditions.

---

### Approach 2: Optimal -- Pre-built Row Strings
**Intuition:** Build `border_row = "*" * M` and `middle_row = "*" + " "*(M-2) + "*"` once. Then for each row, just print the right string. No inner loop needed at all.

**Steps:**
1. Build `border_row` (M stars) and `middle_row` (star + M-2 spaces + star).
2. Loop `i` from 0 to N-1: print `border_row` if i==0 or i==N-1, else `middle_row`.

| Metric | Value  |
|--------|--------|
| Time   | O(N*M) |
| Space  | O(M)   |

---

### Approach 3: Best -- Full Grid Built then Single Print
**Intuition:** Collect all rows into a list and join with newlines. One print call = one I/O operation. Useful when output is buffered to a string or returned.

**Steps:**
1. Same row construction as Approach 2.
2. Push each row string to a list.
3. `print("\n".join(rows))` -- single I/O call.

| Metric | Value  |
|--------|--------|
| Time   | O(N*M) |
| Space  | O(N*M) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N*M)?** Every cell must be assigned either `*` or ` ` and printed. The minimum number of operations is N*M regardless of strategy.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                          | Why it happens                        | Fix                                          |
|---------------------------------|---------------------------------------|----------------------------------------------|
| Interior cells show `*`         | Forgot inner boundary check (j==0)    | All four border conditions must be checked   |
| Middle row is `* *` (1 space)   | Used `M-1` instead of `M-2` for gap  | Inner spaces = M-2                           |
| N=1 prints hollow (` ` inside)  | Middle logic applied to single row    | When N==1, only border rows exist            |

### Edge Cases Checklist
- N=1, any M --> single fully solid row
- N=2, any M --> both rows are border rows
- N=any, M=1 --> single column of stars (no interior)
- N=any, M=2 --> two columns of stars (no interior)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Print a rectangle where only the border has stars."
2. **M**atch: "2D pattern with conditional fill -- boundary check."
3. **P**lan: "Two row types: border (all stars) and middle (star-spaces-star). Select based on row index."
4. **I**mplement: Build both strings first; single-loop for row selection.
5. **R**eview: Trace N=3, M=4.
6. **E**valuate: "O(N*M) time unavoidable. O(M) space with pre-built rows."

### Follow-Up Questions
- "What if the hollow interior should be filled with `.`?" --> Change interior char.
- "How would you extend to a 3D hollow box?" --> Add a depth dimension.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                         |
|-------------------|-----------------------------------------------------------------|
| **Prerequisite**  | P001 Rectangular Star (solid rectangle first)                  |
| **Same pattern**  | Hollow diamond, hollow triangle                                |
| **Harder variant**| Nested hollow rectangles (like a border inside a border)       |
| **Unlocks**       | Matrix boundary traversal; spiral order matrix problems        |
