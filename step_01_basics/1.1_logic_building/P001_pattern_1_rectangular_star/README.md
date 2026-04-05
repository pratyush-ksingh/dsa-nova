# Pattern 1 - Rectangular Star

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given two integers **N** (rows) and **M** (columns), print a rectangle made of `*` characters with N rows and M columns.

**Example:**
```
Input: N = 4, M = 5
Output:
* * * * *
* * * * *
* * * * *
* * * * *
```

| Input     | Output                                      | Explanation                        |
|-----------|---------------------------------------------|------------------------------------|
| N=3, M=4  | `* * * *`<br>`* * * *`<br>`* * * *`         | 3 rows, each with 4 stars          |
| N=1, M=1  | `*`                                         | Single star                        |
| N=2, M=6  | `* * * * * *`<br>`* * * * * *`              | 2 rows, 6 stars each              |

### Real-Life Analogy
Think of a **brick wall**: a mason lays bricks row by row, and within each row, places one brick at a time from left to right. Once a row is complete, they move to the next row. That is exactly what nested loops do -- the outer loop picks the row, the inner loop fills in each column.

### Key Observations
1. The total number of stars printed is always `N * M`.
2. Each row is identical -- you print the same M stars every time.
3. **Aha moment:** This is the foundational "two nested loops" pattern. The outer loop controls *which row*, the inner loop controls *what goes in that row*. Every single 2D pattern problem is a variation of this skeleton.

### Constraints
- 1 <= N, M <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
We need to produce a 2D grid of output. A single loop can handle one dimension (one row of stars). To repeat that row N times, we wrap it in another loop. No data structure is needed -- we just print directly.

### Pattern Recognition
**Classification cue:** "Print a 2D shape" --> nested loops. Outer loop = rows, inner loop = columns. This is the simplest member of the pattern-printing family.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Use two `for` loops. The outer loop iterates N times (rows). The inner loop iterates M times (columns), printing a star each time. After the inner loop finishes, print a newline.

**Steps:**
1. Loop `i` from 0 to N-1 (each row).
2. Inside, loop `j` from 0 to M-1 (each column) and print `*`.
3. After the inner loop, print a newline to move to the next row.

**Dry Run Trace (N=3, M=4):**

| i (row) | j (col) | Action          | Output so far          |
|---------|---------|-----------------|------------------------|
| 0       | 0,1,2,3 | print 4 stars   | `* * * *\n`            |
| 1       | 0,1,2,3 | print 4 stars   | `* * * *\n* * * *\n`   |
| 2       | 0,1,2,3 | print 4 stars   | `* * * *\n* * * *\n* * * *\n` |

| Metric | Value   |
|--------|---------|
| Time   | O(N*M)  |
| Space  | O(1)    |

**BUD Transition:** Can we eliminate the inner loop? Yes -- string repetition.

---

### Approach 2: Optimal -- String Repetition
**Intuition:** Instead of looping through each column, build the entire row as a repeated string (`"* " * M`) and print it N times. This leverages built-in string repetition, reducing code complexity (though asymptotic time is the same since the string must still be constructed).

**Steps:**
1. Build `row = "* " * M` (or equivalent).
2. Loop N times, printing `row` each time.

| Metric | Value   |
|--------|---------|
| Time   | O(N*M)  |
| Space  | O(M) for the row string |

---

### Approach 3: Best -- Full String Repetition (Single Print)
**Intuition:** Build the entire grid as one string and print once. This minimizes I/O calls, which is the real bottleneck in pattern problems.

**Steps:**
1. Build `row = "* " * M` (trimmed).
2. Build `grid = (row + "\n") * N`.
3. Print `grid` once.

| Metric | Value   |
|--------|---------|
| Time   | O(N*M)  |
| Space  | O(N*M) for the full string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N*M)?** You must produce N*M stars on screen. You cannot skip any star -- each must be placed. So every approach is at least O(N*M) in time. The space varies: printing character-by-character is O(1) space, while building the full grid string is O(N*M).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Extra space/newline at end | Printing `"* "` leaves trailing space | Trim the row or use `join` |
| Off-by-one on loop bounds | Using `<=` instead of `<` (or vice versa) | Decide: 0-based with `<`, or 1-based with `<=` |
| Forgetting newline after each row | Inner loop prints on same line | Explicitly print `\n` after inner loop |

### Edge Cases Checklist
- N=1, M=1 --> single star
- N=1, M=100 --> single row of 100 stars
- N=100, M=1 --> 100 rows, each with 1 star (vertical line)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: Clarify -- are we printing to stdout? Spaces between stars? Trailing spaces OK?
2. **M**atch: "This is a nested loop pattern problem."
3. **P**lan: "Outer loop for rows, inner for columns."
4. **I**mplement: Write clean code with clear variable names.
5. **R**eview: Dry run with N=2, M=3.
6. **E**valuate: "O(N*M) time, O(1) space. Cannot do better since we must print every star."

### Follow-Up Questions
- "What if we want a hollow rectangle?" --> Only print stars on first/last row and first/last column.
- "What if we want to return a string instead of printing?" --> Use StringBuilder / list join.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic for-loop syntax |
| **Same pattern** | P002 Right-Angled Triangle (nested loops, variable inner bound) |
| **Harder variant** | Hollow rectangle, diamond patterns |
| **Unlocks** | All 2D pattern problems; matrix traversal intuition |
