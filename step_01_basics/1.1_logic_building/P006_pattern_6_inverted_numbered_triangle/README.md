# Pattern 6 - Inverted Numbered Triangle

> **Batch 3 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print an inverted triangle where row `i` (1-indexed) contains the numbers `1 2 3 ... (N-i+1)`.

**Example:**
```
Input: N = 5
Output:
1 2 3 4 5
1 2 3 4
1 2 3
1 2
1
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=5   | `1 2 3 4 5`<br>`1 2 3 4`<br>`1 2 3`<br>`1 2`<br>`1` | Row 1: numbers 1-5, Row 2: 1-4, ..., Row 5: just 1 |
| N=1   | `1` | Single number |
| N=3   | `1 2 3`<br>`1 2`<br>`1` | 3 rows, decreasing from 3 to 1 |

### Real-Life Analogy
Picture a **countdown scoreboard** at a bowling alley. In the first frame, all 10 pins are standing and each is numbered. After each throw, you remove the rightmost pins. The display updates to show only the remaining pins: `1 2 3 4 5`, then `1 2 3 4`, then `1 2 3`, and so on. Each row of our pattern is like the scoreboard after another throw removes one more pin from the right.

### Key Observations
1. This is Pattern 5 (inverted star triangle) with `*` replaced by the column number `j`.
2. Each row always starts at 1 -- only the ending number shrinks. The inner loop is `j = 1 to (N - i + 1)`.
3. **Aha moment:** The transition from stars to numbers is trivial once you realize the inner loop variable `j` IS the number to print. This pattern teaches that any star pattern can be converted to a numbered pattern by printing the loop counter instead of a fixed character.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Same reasoning as all pattern problems: outer loop = rows, inner loop = content. The only difference from Pattern 5 is what we print inside the inner loop.

### Pattern Recognition
**Classification cue:** "Print a 2D shape with numbers" --> nested loops, inner loop prints `j`. This is Pattern 5 with `print(j)` instead of `print("*")`.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Outer loop iterates rows 1..N. Inner loop prints numbers from 1 to `(N - i + 1)`. After each row, print a newline.

**Steps:**
1. Loop `i` from 1 to N.
2. Loop `j` from 1 to `(N - i + 1)`, printing `j`.
3. Print newline after inner loop.

**Dry Run Trace (N=4):**

| i (row) | Numbers to print | Output |
|---------|-----------------|--------|
| 1       | 1 to 4          | `1 2 3 4` |
| 2       | 1 to 3          | `1 2 3` |
| 3       | 1 to 2          | `1 2` |
| 4       | 1 to 1          | `1` |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** We can replace the inner loop with a range/join construct.

---

### Approach 2: Optimal -- Range-Based String per Row
**Intuition:** For each row, generate the sequence `1..k` using a range, join with spaces, and print. Eliminates manual inner loop management.

**Steps:**
1. Loop `i` from 1 to N.
2. Build row string: join numbers `1` to `(N - i + 1)` with spaces.
3. Print row.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(N) per row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build all rows into one string, print once. Minimizes I/O calls.

**Steps:**
1. For each `i` from 1 to N, build row `"1 2 ... (N-i+1)"`.
2. Join all rows with newlines.
3. Single print.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(N^2) for grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total numbers printed = N + (N-1) + ... + 1 = N(N+1)/2 = O(N^2). Each number must appear on screen, so no approach can be sub-quadratic. Space ranges from O(1) for direct printing to O(N^2) for full grid buffering.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing row number `i` instead of column `j` | Confusing which variable to print | Print the inner loop variable `j`, not `i` |
| Starting numbers at 0 instead of 1 | Using 0-based inner loop and printing index | Start inner loop at 1, or print `j+1` |
| Trailing space on each row | Printing `" "` after every number | Use join, or skip space after last number |

### Edge Cases Checklist
- N=1 --> single `1`
- N=100 --> first row has numbers 1-100
- Verify spaces: exactly one space between numbers, none trailing

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Inverted triangle with numbers 1 to (N-i+1) per row. Spaces between numbers?"
2. **M**atch: "Same as inverted star triangle but printing loop counter."
3. **P**lan: "Outer loop 1..N, inner loop 1..(N-i+1), print j."
4. **I**mplement: Clean nested loops.
5. **R**eview: Trace N=3 to verify output.
6. **E**valuate: "O(N^2) time, unavoidable."

### Follow-Up Questions
- "Print numbers in reverse per row (e.g., 5 4 3 2 1)?" --> Inner loop counts down instead of up.
- "Print row number repeated (e.g., row 2: 2 2 2 2)?" --> Print `i` instead of `j`.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P005 Inverted Star Triangle (same structure, stars) |
| **Same pattern** | Any inverted triangle variant |
| **Harder variant** | Inverted triangle with row-relative numbering (N, N-1, ...) |
| **Unlocks** | Floyd's Triangle, Pascal's Triangle (numbered patterns with varying content) |
