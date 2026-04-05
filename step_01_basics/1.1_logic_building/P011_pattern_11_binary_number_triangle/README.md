# Pattern 11 - Binary Number Triangle

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a right-angled triangle of N rows where each cell contains either `1` or `0`. The rule: **odd-numbered rows** (1st, 3rd, 5th...) start with `1` and alternate; **even-numbered rows** (2nd, 4th, 6th...) start with `0` and alternate. Row `i` (1-indexed) has `i` elements.

**Example:**
```
Input: N = 5
Output:
1
0 1
1 0 1
0 1 0 1
1 0 1 0 1
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `1` | Row 1 (odd) starts with 1 |
| N=2   | `1`<br>`0 1` | Row 2 (even) starts with 0 |
| N=3   | `1`<br>`0 1`<br>`1 0 1` | Row 3 (odd) starts with 1 again |
| N=4   | `1`<br>`0 1`<br>`1 0 1`<br>`0 1 0 1` | Alternating start per row |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Think of a **checkerboard** being revealed one row at a time, where each new row is one square longer than the last. Odd rows start with a white square (1), even rows start with a black square (0), and colors alternate within each row. The binary triangle is a triangular slice of this checkerboard.

### Key Observations
1. The starting value for row `i` (1-indexed) is `i % 2` -- odd rows start with 1, even rows start with 0. Then the value toggles between 0 and 1 across columns.
2. Alternatively, the value at position `(i, j)` (both 1-indexed) is `(i + j + 1) % 2` or equivalently `(i + j) % 2` depending on your indexing. This gives a checkerboard pattern.
3. **Aha moment:** Instead of tracking a toggle variable, you can use `1 - val` to flip between 0 and 1, or simply use `(i + j) % 2`. The toggle trick (`val = 1 - val`) is a fundamental bit-manipulation pattern that appears in many problems.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
We produce a triangular grid. The outer loop selects the row (1 to N), the inner loop produces `i` values per row. The values follow a simple alternating rule, so no extra data structure is needed.

### Pattern Recognition
**Classification cue:** "Alternating values in a 2D pattern" --> nested loops with a toggle variable or modular arithmetic. The toggle pattern (`val = 1 - val`) is the same technique used in alternating linked list operations and zigzag traversals.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Toggle Variable per Row
**Intuition:** For each row, set the starting value based on whether the row is odd or even. Then toggle the value after printing each element.

**Steps:**
1. Loop `i` from 1 to N (each row).
2. Set `val = 1` if `i` is odd, else `val = 0`.
3. Inner loop `j` from 0 to i-1: print `val`, then `val = 1 - val`.
4. Print newline.

**Dry Run Trace (N=4):**

| i (row) | Start val | Values printed | Output |
|---------|-----------|---------------|--------|
| 1       | 1         | 1             | `1` |
| 2       | 0         | 0, 1          | `0 1` |
| 3       | 1         | 1, 0, 1       | `1 0 1` |
| 4       | 0         | 0, 1, 0, 1    | `0 1 0 1` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** We can replace the toggle variable with a direct formula to make the code even simpler.

---

### Approach 2: Optimal -- Direct Formula
**Intuition:** The value at row `i`, column `j` (both 0-indexed) is `(i + j) % 2`. No toggle variable needed -- just pure arithmetic.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Inner loop `j` from 0 to i: print `(i + j) % 2`.
3. Print newline.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire triangle as a string and print once.

**Steps:**
1. For each row, compute all values using the formula and join them.
2. Collect all rows and print in one call.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total elements = 1 + 2 + ... + N = N*(N+1)/2, which is O(N^2). Each element is computed in O(1) time and must be output.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Starting row 1 with 0 | Mixing up odd/even numbering | Row 1 is odd, starts with 1 |
| Not resetting the toggle each row | Carrying the toggle value across rows | Reset `val` at the start of each row based on row parity |
| Using `val ^= 1` incorrectly | XOR works, but only if val is 0 or 1 | Ensure val is initialized to 0 or 1 |

### Edge Cases Checklist
- N=1 --> single `1`
- N=2 --> `1` then `0 1`
- Large N=100 --> 100 rows, last row has 100 alternating values

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Triangle of N rows, alternating 1s and 0s. Odd rows start with 1, even rows start with 0."
2. **M**atch: "Nested loop pattern with a toggle or modular arithmetic."
3. **P**lan: "I can use `(i + j) % 2` to directly compute each cell, or toggle a variable within each row."
4. **I**mplement: Write the formula-based approach for cleaner code.
5. **R**eview: Dry-run with N=3 to verify alternation.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What if you need to print this as a matrix (all rows same length, padded with spaces)?" --> Pad shorter rows with trailing spaces.
- "Can you generalize to k values cycling instead of just 0 and 1?" --> Use `(i + j) % k` for cycling through k values.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle (basic triangle shape) |
| **Same pattern** | P003 Number Triangle (numbers instead of binary) |
| **Harder variant** | Pascal's Triangle (values depend on neighbors), zigzag matrix |
| **Unlocks** | Understanding modular arithmetic in grid patterns |
