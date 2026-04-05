# Pattern 22 - The Number Pattern

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** MEDIUM | **XP:** 15

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a `(2N-1) x (2N-1)` grid where the value at each cell equals **N minus the minimum distance from that cell to any of the four borders**.

**Example (N=4):**
```
4444444
4333334
4322234
4321234
4322234
4333334
4444444
```

| Input | Grid size | Outermost value | Centre value |
|-------|-----------|-----------------|--------------|
| N=1   | 1x1       | 1               | 1            |
| N=3   | 5x5       | 3               | 1            |
| N=4   | 7x7       | 4               | 1            |

### Real-Life Analogy
Imagine a **target (archery board)**: the outermost ring has the highest number (N), and the values decrease as you move inward. The centre is always 1. Each "ring" corresponds to one less than its outer neighbour -- exactly like how each ring is one step closer to the centre.

### Key Observations
1. The grid is square with side length `2*N - 1`.
2. For cell `(i, j)` (0-indexed): `val = N - min(i, j, (2N-2-i), (2N-2-j))`.
3. `min(i, j, size-1-i, size-1-j)` is the distance to the nearest border.
4. **Aha moment:** The pattern is perfectly symmetric across both axes and both diagonals. You only need the formula -- no special-casing.
5. The centre cell `(N-1, N-1)` has `min(N-1, N-1, N-1, N-1) = N-1`, so value = `N-(N-1)=1`. Correct!

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why the Border-Distance Formula?
Once you recognise that value = N - (distance to nearest border), the problem reduces to a trivial formula. No recursion, no BFS -- just arithmetic at each cell.

### Pattern Recognition
**Classification cue:** "Onion-ring / concentric frame numbered pattern" --> compute distance-to-border per cell.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Four Explicit Distance Variables
**Intuition:** For each cell explicitly compute all four border distances, then take the minimum. Most readable -- each distance has a clear name.

**Steps:**
1. Let `size = 2*N - 1`.
2. For each row `i` in 0..size-1, for each col `j` in 0..size-1:
   - `dist_top = i`, `dist_bottom = size-1-i`, `dist_left = j`, `dist_right = size-1-j`
   - `val = N - min(dist_top, dist_bottom, dist_left, dist_right)`
   - Print `val`.
3. Print newline after each row.

**Dry Run Trace (N=3, top-left quadrant):**

| i | j | dTop | dBot | dLeft | dRight | min | val |
|---|---|------|------|-------|--------|-----|-----|
| 0 | 0 | 0    | 4    | 0     | 4      | 0   | 3   |
| 0 | 1 | 0    | 4    | 1     | 3      | 0   | 3   |
| 1 | 0 | 1    | 3    | 0     | 4      | 0   | 3   |
| 1 | 1 | 1    | 3    | 1     | 3      | 1   | 2   |
| 2 | 2 | 2    | 2    | 2     | 2      | 2   | 1   |

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

**BUD Transition:** Inline the four variables into a single `min()` call.

---

### Approach 2: Optimal -- Inlined Formula, StringBuilder per Row
**Intuition:** Write `N - min(i, j, size-1-i, size-1-j)` directly. Collect digits per row in a list/StringBuilder then print. Reduces code without changing algorithm.

**Steps:**
1. `size = 2*N - 1`.
2. For each row `i`: build row string by appending `N - min(i, j, size-1-i, size-1-j)` for each `j`.
3. Print each row.

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(n)   |

---

### Approach 3: Best -- Full Grid Built then Single Print
**Intuition:** Assemble all rows in a list/StringBuilder and print in one call. Optimal I/O.

**Steps:**
1. Build each row string using the inlined formula.
2. Collect in `rows[]`.
3. `print("\n".join(rows))`.

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(n^2) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n^2)?** The grid has `(2N-1)^2 = O(n^2)` cells. Each cell requires O(1) to compute its value (one `min` of 4 numbers). Total work is O(n^2) and cannot be less since every cell must be printed.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                              | Why it happens                            | Fix                                          |
|-------------------------------------|-------------------------------------------|----------------------------------------------|
| Using `N` instead of `N-1` in size  | Forgetting grid side = 2N-1              | `size = 2*N - 1`                             |
| Formula gives 0 or negative         | Subtracting from wrong base              | Value = `N - minDist`; minDist in [0, N-1]   |
| Grid is N x N instead of (2N-1)x(2N-1) | Not accounting for the mirror half    | Both dimensions are `2*N - 1`               |

### Edge Cases Checklist
- N=1: 1x1 grid, single cell value = 1
- N=2: 3x3 grid, border=2, centre=1

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "What determines each cell's value?" --> Distance to nearest border.
2. **M**atch: "This is a formula-based 2D grid problem."
3. **P**lan: "size = 2N-1. Cell (i,j) = N - min(i, j, size-1-i, size-1-j)."
4. **I**mplement: Two nested loops with the formula.
5. **R**eview: Trace corners (should be N) and centre (should be 1).
6. **E**valuate: "O(n^2) optimal since we must write every cell."

### Follow-Up Questions
- "Can you solve it for a non-square grid (N rows, M cols)?" --> Generalise with separate row/col distances.
- "What if you want the values to increase toward the centre instead of decrease?" --> Use `minDist + 1` as value.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                           |
|-------------------|-------------------------------------------------------------------|
| **Prerequisite**  | P001 Rectangular Star, P021 Hollow Rectangle                     |
| **Same pattern**  | Spiral matrix (also a concentric frame traversal)                |
| **Harder variant**| Spiral number fill; distance-transform in image processing       |
| **Unlocks**       | Matrix distance problems; BFS multi-source distance grids        |
