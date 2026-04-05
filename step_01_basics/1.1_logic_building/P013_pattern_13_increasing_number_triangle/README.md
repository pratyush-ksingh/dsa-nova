# Pattern 13 - Increasing Number Triangle

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a right-angled triangle where numbers increase **continuously** across rows. Row 1 has 1 number, row 2 has 2 numbers, row 3 has 3 numbers, and so on. The numbers are: 1, then 2 3, then 4 5 6, then 7 8 9 10, etc.

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
|-------|--------|-------------|
| N=1   | `1` | Just the number 1 |
| N=2   | `1`<br>`2 3` | Numbers continue across rows |
| N=3   | `1`<br>`2 3`<br>`4 5 6` | Row 3 starts where row 2 left off |
| N=4   | `1`<br>`2 3`<br>`4 5 6`<br>`7 8 9 10` | Continuous numbering: 1 through 10 |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Imagine numbering seats in a **triangular auditorium**. The first row has 1 seat (seat 1), the second row has 2 seats (seats 2-3), the third has 3 seats (seats 4-6), and so on. You number them sequentially from front to back, left to right. The key is that the counter does not reset between rows -- it carries over.

### Key Observations
1. The counter is a **global variable** that persists across rows. Unlike most pattern problems where each row is independent, here the rows are linked by the running counter.
2. Row `i` (1-indexed) starts at value `i*(i-1)/2 + 1` and contains `i` consecutive numbers. But you do not need this formula -- just increment a counter.
3. **Aha moment:** The simplest approach uses one counter variable initialized to 1 before the loops. The inner loop prints the counter and increments it. The counter naturally carries the state between rows.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops with a Counter?
The triangular shape requires nested loops (outer for rows, inner for columns). The continuous numbering requires a single counter variable that survives across all iterations. No array or data structure is needed.

### Pattern Recognition
**Classification cue:** "Sequential numbering in a 2D layout" --> nested loops with a persistent counter. This is a variation of the basic number triangle where the counter is row-independent vs. global.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Counter Variable
**Intuition:** Maintain a counter starting at 1. For each row, print `i` numbers by printing and incrementing the counter.

**Steps:**
1. Initialize `counter = 1`.
2. Loop `i` from 1 to N.
3. Inner loop `j` from 0 to i-1: print `counter`, increment it.
4. Print newline.

**Dry Run Trace (N=4):**

| i (row) | counter at start | Values printed | counter at end |
|---------|-----------------|---------------|---------------|
| 1       | 1               | 1             | 2 |
| 2       | 2               | 2 3           | 4 |
| 3       | 4               | 4 5 6         | 7 |
| 4       | 7               | 7 8 9 10      | 11 |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** We can compute the starting value of each row directly using the triangular number formula, avoiding the need for a persistent counter.

---

### Approach 2: Optimal -- Direct Formula per Row
**Intuition:** Row `i` (1-indexed) starts at `i*(i-1)/2 + 1`. Use this formula to compute the starting value without tracking a counter.

**Steps:**
1. Loop `i` from 1 to N.
2. Compute `start = i * (i - 1) // 2 + 1`.
3. Print numbers `start, start+1, ..., start+i-1`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire triangle as a single string using the counter approach and print once.

**Steps:**
1. Initialize counter = 1.
2. Build each row string, collect all rows, join with newlines, print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total numbers printed = 1 + 2 + ... + N = N*(N+1)/2, which is O(N^2). Each number is computed and printed in O(1).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Resetting counter each row | Habit from other pattern problems | The counter must persist across rows |
| Multi-digit alignment | Numbers like 10, 11 are wider than 1, 2 | For display purposes this is usually fine; use `printf` with width if needed |
| Off-by-one in formula | Wrong triangular number formula | Verify: row 1 starts at 1, row 2 at 2, row 3 at 4 |

### Edge Cases Checklist
- N=1 --> just `1`
- N=2 --> `1` then `2 3`
- Large N=100 --> last row has 100 numbers (4951 to 5050), total 5050 numbers printed

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Triangle with N rows, continuously increasing numbers. No row resets."
2. **M**atch: "Nested loop pattern with a persistent counter."
3. **P**lan: "I will use a single counter variable, increment it in the inner loop."
4. **I**mplement: Clean, minimal code.
5. **R**eview: Dry-run with N=3 to verify 1, 2 3, 4 5 6.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What is the last number printed for a given N?" --> `N*(N+1)/2` (triangular number formula).
- "What if you want this in a centered pyramid shape?" --> Add leading spaces like in Pattern 7 but print numbers instead of stars.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P003 Number Triangle (row-independent numbering) |
| **Same pattern** | Floyd's Triangle (same problem, classic name) |
| **Harder variant** | Spiral matrix numbering, zigzag numbering |
| **Unlocks** | Understanding persistent state across loop iterations |
