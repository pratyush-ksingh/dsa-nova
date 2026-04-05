# Pattern 2 - Right-Angled Triangle

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a right-angled triangle pattern of `*` characters with N rows. Row `i` (1-indexed) contains exactly `i` stars.

**Example:**
```
Input: N = 5
Output:
*
* *
* * *
* * * *
* * * * *
```

| Input | Output                                                | Explanation                          |
|-------|-------------------------------------------------------|--------------------------------------|
| N=1   | `*`                                                   | Single star                          |
| N=3   | `*`<br>`* *`<br>`* * *`                               | Row 1: 1 star, Row 2: 2, Row 3: 3   |
| N=4   | `*`<br>`* *`<br>`* * *`<br>`* * * *`                  | Each row i has i stars               |

### Real-Life Analogy
Imagine stacking blocks like a staircase: the first step has 1 block, the second has 2, and so on. Each level adds one more block than the previous. That is exactly this pattern -- a staircase growing from top to bottom.

### Key Observations
1. Row number `i` (1-indexed) has exactly `i` stars.
2. Total stars printed = 1 + 2 + ... + N = N*(N+1)/2.
3. **Aha moment:** The only difference from the rectangular pattern (P001) is that the inner loop bound changes with each row. Instead of a fixed M columns, the inner loop runs `i` times. This is the key step from "fixed grid" to "variable-width patterns."

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Same reasoning as the rectangle: we produce a 2D shape row by row. The only twist is the inner loop's upper bound depends on the outer loop variable.

### Pattern Recognition
**Classification cue:** "Print a triangular shape" --> nested loops where the inner bound is a function of the outer index. This is the second pattern in the family and the gateway to all variable-width patterns (inverted triangle, pyramid, diamond).

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Outer loop `i` from 1 to N. Inner loop `j` from 1 to `i`. Print a star for each j, then a newline.

**Steps:**
1. Loop `i` from 1 to N.
2. Loop `j` from 1 to `i`, printing `*` each time.
3. Print newline after inner loop.

**Dry Run Trace (N=4):**

| i (row) | Inner loop runs | Stars printed | Cumulative output             |
|---------|----------------|---------------|-------------------------------|
| 1       | j=1            | `*`           | `*\n`                         |
| 2       | j=1,2          | `* *`         | `*\n* *\n`                    |
| 3       | j=1,2,3        | `* * *`       | `*\n* *\n* * *\n`             |
| 4       | j=1,2,3,4      | `* * * *`     | `*\n* *\n* * *\n* * * *\n`    |

| Metric | Value          |
|--------|----------------|
| Time   | O(N^2)         |
| Space  | O(1)           |

**BUD Transition:** Can we avoid the inner loop using string repetition?

---

### Approach 2: Optimal -- String Repetition per Row
**Intuition:** For each row `i`, create the row string using `"* " * i` or `join`. This removes the explicit inner loop from our code, making it cleaner, though asymptotic time remains the same.

**Steps:**
1. Loop `i` from 1 to N.
2. Build row as `" ".join(["*"] * i)` and print.

| Metric | Value          |
|--------|----------------|
| Time   | O(N^2)         |
| Space  | O(N) for the largest row string |

---

### Approach 3: Best -- Single Print with Full Grid
**Intuition:** Build the entire triangle as one string and print once to minimize I/O overhead.

**Steps:**
1. For each row `i` from 1 to N, build `" ".join(["*"] * i)`.
2. Join all rows with `\n`.
3. Print the entire result at once.

| Metric | Value          |
|--------|----------------|
| Time   | O(N^2)         |
| Space  | O(N^2) for the full string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** The total number of stars is 1 + 2 + ... + N = N(N+1)/2, which is O(N^2). Since each star must be produced, we cannot beat O(N^2).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Inner loop from 0 to N instead of 0 to i | Copy-paste from rectangle | Make inner bound depend on outer variable |
| Off-by-one: printing N+1 rows | Confusing `<=` with `<` | Be consistent: 1-based uses `<=N`, 0-based uses `<N` |
| Trailing spaces | `"* " * i` has trailing space | Use `" ".join(["*"] * i)` |

### Edge Cases Checklist
- N=1 --> single `*`
- N=100 --> large triangle, last row has 100 stars

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Row i has i stars, 1-indexed? Spaces between stars?"
2. **M**atch: "Classic nested loop pattern with variable inner bound."
3. **P**lan: "Outer loop 1..N, inner loop 1..i."
4. **I**mplement: Clean, minimal code.
5. **R**eview: Trace with N=3.
6. **E**valuate: "O(N^2) time, O(1) space -- optimal since we must print N(N+1)/2 stars."

### Follow-Up Questions
- "Print it upside down (inverted triangle)?" --> Outer loop from N down to 1, or inner loop runs `N - i + 1` times.
- "Print numbers instead of stars?" --> Replace `*` with `j`.
- "Right-aligned triangle?" --> Add leading spaces: `N - i` spaces before the stars.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P001 Rectangular Star (fixed nested loops) |
| **Same pattern** | Inverted triangle, number triangle |
| **Harder variant** | Pyramid (centered triangle), diamond pattern |
| **Unlocks** | All variable-width pattern problems |
