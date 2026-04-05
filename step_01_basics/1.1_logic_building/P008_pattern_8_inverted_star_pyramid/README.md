# Pattern 8 - Inverted Star Pyramid

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print an **inverted centered star pyramid** with N rows. Row 1 has `(2*N - 1)` stars, row 2 has `(2*N - 3)` stars, and so on until row N has 1 star. Each row is centered with leading spaces. This is the vertical mirror of Pattern 7 (the upright star pyramid).

**Example:**
```
Input: N = 5
Output:
* * * * * * * * *
  * * * * * * *
    * * * * *
      * * *
        *
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `*` | Single star, no spaces needed |
| N=3   | `* * * * *`<br>`  * * *`<br>`    *` | 5 stars, then 3, then 1 -- each row indented by 2 more spaces |
| N=4   | `* * * * * * *`<br>`  * * * * *`<br>`    * * *`<br>`      *` | 7, 5, 3, 1 stars with increasing indentation |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Imagine a **fountain spray** viewed from the side. At the top, the water fans out wide, and as it falls downward, the spray narrows until it converges to a single point at the nozzle. Each "level" of the spray is a row of stars, getting narrower as you descend. Printing this inverted pyramid is like sketching that fountain cross-section row by row from top to bottom.

### Key Observations
1. Row `i` (0-indexed) has exactly `i` leading spaces and `(2*(N-i) - 1)` stars. The space count increases by 1 each row while the star count decreases by 2.
2. This is the exact reverse row order of the upright pyramid (Pattern 7). If you printed Pattern 7's rows in reverse, you get Pattern 8.
3. **Aha moment:** The key formula relationship is `spaces + stars = constant width`. Every row has total width `2*N - 1` characters (counting stars and spaces between them, plus leading spaces). Recognizing this "conservation of width" lets you derive the formula for any centered pattern instantly.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
We produce a 2D text pattern. The outer loop selects which row we are printing (N rows), and the inner loops handle printing the leading spaces and then the stars for that row. No data structure is needed -- we print directly.

### Pattern Recognition
**Classification cue:** "Print a centered 2D shape" --> nested loops with a spaces-loop and a stars-loop per row. The "inverted" part just means the star count starts high and decreases, while spaces start at 0 and increase.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Use three loops per row: one for leading spaces, one for stars, and the outer loop for rows. Directly encode the formulas for space count and star count.

**Steps:**
1. Loop `i` from 0 to N-1 (each row).
2. Inner loop 1: print `i` spaces.
3. Inner loop 2: print `(2*(N-i) - 1)` stars, with spaces between them.
4. Print a newline after each row.

**Dry Run Trace (N=4):**

| i (row) | Spaces | Stars | Output |
|---------|--------|-------|--------|
| 0       | 0      | 7     | `* * * * * * *` |
| 1       | 1      | 5     | ` * * * * *` |
| 2       | 2      | 3     | `  * * *` |
| 3       | 3      | 1     | `   *` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** The inner loops can be replaced with string repetition to simplify code.

---

### Approach 2: Optimal -- String Repetition per Row
**Intuition:** Instead of character-by-character loops, build each row as a string using repetition operators. Compute the space prefix and star segment for each row, concatenate, and print.

**Steps:**
1. Loop `i` from 0 to N-1.
2. `spaces = " " * i`
3. `stars = "* " * (N - i)` trimmed (or use join for `(2*(N-i)-1)` stars with spaces).
4. Print `spaces + stars`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) per row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire inverted pyramid as a single string and print it in one I/O call. This minimizes system call overhead, which is the real bottleneck in pattern-printing problems.

**Steps:**
1. For each row `i` from 0 to N-1, build `" " * i + " ".join(["*"] * (2*(N-i)-1))`.
2. Join all rows with newlines.
3. Print the full string once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** The total number of characters printed is the sum of an arithmetic series: `(2N-1) + (2N-3) + ... + 1 = N^2`. You cannot avoid producing each character, so O(N^2) time is the lower bound. Space depends on whether you buffer output: O(1) if printing character-by-character, O(N^2) if building the full grid string.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Wrong space count formula | Confusing 0-indexed vs 1-indexed rows | Stick with 0-indexed: row `i` has `i` spaces |
| Trailing spaces after stars | Printing `"* "` for every star including the last | Use `join` or print space only between stars |
| Confusing with upright pyramid | Copy-pasting Pattern 7 without reversing | Verify: row 0 should have the MOST stars |

### Edge Cases Checklist
- N=1 --> single star, no spaces
- N=2 --> two rows: `* * *` then ` *`
- Large N=100 --> first row has 199 stars, performance is fine

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "So I need to print an inverted pyramid -- widest row on top, narrowest at the bottom. Is the output space-separated stars? Trailing spaces OK?"
2. **M**atch: "This is a nested loop pattern problem -- the inverted version of the standard pyramid."
3. **P**lan: "Row i gets i leading spaces and (2*(N-i)-1) stars. I will use two inner loops."
4. **I**mplement: Write clean code with descriptive variable names.
5. **R**eview: Dry-run with N=3 to verify the first row has 5 stars and the last has 1.
6. **E**valuate: "O(N^2) time, O(1) space. Cannot do better since we must print N^2 characters."

### Follow-Up Questions
- "Can you print this using recursion instead of loops?" --> Yes, recurse with decreasing star count; base case is 1 star.
- "How would you combine this with the upright pyramid to make a diamond?" --> That is exactly Pattern 9 (Diamond).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P007 Star Pyramid (upright version) |
| **Same pattern** | P005 Inverted Triangle, P006 Inverted Numbered Triangle |
| **Combines with** | P007 to form P009 Diamond Star Pattern |
| **Unlocks** | Diamond patterns, hourglass patterns |
