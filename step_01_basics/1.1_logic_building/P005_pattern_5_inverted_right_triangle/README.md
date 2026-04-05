# Pattern 5 - Inverted Right Triangle

> **Batch 3 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print an inverted right-angled triangle of `*` characters. Row `i` (1-indexed) has `(N - i + 1)` stars.

**Example:**
```
Input: N = 5
Output:
* * * * *
* * * *
* * *
* *
*
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=5   | `* * * * *`<br>`* * * *`<br>`* * *`<br>`* *`<br>`*` | Row 1: 5 stars, Row 2: 4 stars, ..., Row 5: 1 star |
| N=1   | `*` | Single star |
| N=3   | `* * *`<br>`* *`<br>`*` | 3 rows, decreasing from 3 to 1 |

### Real-Life Analogy
Imagine a **pyramid of stacked cans** in a supermarket display, viewed from the side. The bottom row has the most cans, and each row above has one fewer. Now flip it upside down -- the top row is widest and each subsequent row shrinks by one. That is exactly this inverted triangle: row 1 is the widest (N stars) and it narrows by one star per row until a single star remains.

### Key Observations
1. Row `i` (1-indexed) has exactly `(N - i + 1)` stars -- the inner loop bound is a function of the outer loop variable.
2. This is the mirror image of Pattern 2 (right-angled triangle where row i has i stars). The only change is the inner loop bound flips from `i` to `N - i + 1`.
3. **Aha moment:** Every "inverted" pattern problem is the original pattern with a reversed loop bound. Once you see this, you can invert any pattern in seconds by replacing `j < i` with `j < N - i + 1` (or equivalently, iterating the outer loop in reverse).

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
We need a 2D shape on screen. The outer loop selects the row, the inner loop prints the correct number of stars for that row. No data structure needed -- output goes directly to stdout.

### Pattern Recognition
**Classification cue:** "Print a 2D triangle shape with varying row lengths" --> nested loops where the inner loop bound depends on the outer loop variable. This is Pattern 2 with the bound reversed.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Outer loop runs N times (rows). For row `i`, inner loop runs `(N - i + 1)` times, printing a star each iteration. After each row, print a newline.

**Steps:**
1. Loop `i` from 1 to N (each row).
2. Loop `j` from 1 to `(N - i + 1)`, printing `*` each time.
3. After inner loop, print newline.

**Dry Run Trace (N=4):**

| i (row) | Stars to print (N-i+1) | Output for this row |
|---------|----------------------|---------------------|
| 1       | 4                    | `* * * *`           |
| 2       | 3                    | `* * *`             |
| 3       | 2                    | `* *`               |
| 4       | 1                    | `*`                 |

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(1)    |

**BUD Transition:** The inner loop can be replaced by string repetition to simplify code.

---

### Approach 2: Optimal -- String Repetition per Row
**Intuition:** Instead of a character-by-character inner loop, build each row using string repetition. For row `i`, create a string of `(N - i + 1)` stars. This reduces code verbosity and leverages language built-ins.

**Steps:**
1. Loop `i` from 1 to N.
2. Build `row = "* " repeated (N - i + 1) times` (trimmed).
3. Print row.

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(N) for one row string |

**BUD Transition:** Can we eliminate the per-row print call? Yes -- build the entire grid as one string.

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire triangle as one string and issue a single print call. This minimizes I/O overhead, which is the real performance bottleneck in pattern printing.

**Steps:**
1. For each row `i` from 1 to N, build a row string of `(N - i + 1)` stars.
2. Join all rows with newlines into a single grid string.
3. Print once.

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(N^2) for full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** The total number of stars is N + (N-1) + ... + 1 = N*(N+1)/2, which is O(N^2). You cannot avoid printing every star, so every approach has the same time complexity. The space trade-off is: O(1) for character-by-character printing vs. O(N^2) for building the complete grid string.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing increasing triangle instead of inverted | Using `j < i` instead of `j < N - i + 1` | Double-check the inner loop bound |
| Trailing space on each row | Printing `"* "` for every star | Use `join` or skip space after last star |
| Off-by-one: N+1 or N-1 rows | Mixing 0-based and 1-based indexing | Pick one convention and stick with it |

### Edge Cases Checklist
- N=1 --> single star, single row
- N=100 --> first row has 100 stars, last row has 1
- Verify no trailing whitespace per row (some judges are strict)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Inverted triangle -- row 1 has N stars, row N has 1 star. Spaces between stars? Trailing spaces allowed?"
2. **M**atch: "Nested loop pattern. Inner bound is `N - i + 1`."
3. **P**lan: "Outer loop 1..N, inner loop prints decreasing stars."
4. **I**mplement: Write clean nested loops.
5. **R**eview: Dry run with N=3 to verify 3,2,1 stars.
6. **E**valuate: "O(N^2) time and cannot do better since we print N*(N+1)/2 characters."

### Follow-Up Questions
- "Print the same pattern with numbers instead of stars?" --> Change inner loop to print `j` (leads to Pattern 6).
- "Print it right-aligned (with leading spaces)?" --> Add a space-printing loop before the star loop.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle (same structure, increasing) |
| **Same pattern** | P006 Inverted Numbered Triangle (numbers instead of stars) |
| **Harder variant** | Right-aligned inverted triangle (add leading spaces) |
| **Unlocks** | Diamond patterns (combine upright + inverted triangles) |
