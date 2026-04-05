# Pattern 3 - Right Angled Number Triangle

> **Batch 2 of 12** | **Topic:** Logic Building (Patterns) | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer N, print a right-angled triangle pattern where **row i** contains **i copies of the number i** (1-indexed).

**Example:**
```
Input: N = 5
Output:
1
2 2
3 3 3
4 4 4 4
5 5 5 5 5
```

| Input | Output | Explanation |
|---|---|---|
| 1 | `1` | Single row with one '1' |
| 3 | `1` / `2 2` / `3 3 3` | Row i has i copies of i |
| 5 | See above | 5 rows, each printing its row number repeatedly |

### Real-Life Analogy
Imagine stacking labeled boxes in a warehouse. Shelf 1 gets 1 box labeled "1". Shelf 2 gets 2 boxes labeled "2". Shelf 3 gets 3 boxes labeled "3". The label on each box matches the shelf number, and the count of boxes matches the shelf number too. This creates a triangular arrangement when viewed from the side.

### Key Observations
1. This is a nested loop problem: the outer loop controls the row (1 to N), the inner loop controls how many times to print.
2. Unlike a star triangle, the printed character changes per row -- it is the row number itself.
3. **Aha moment:** The pattern is fully determined by one variable -- the row index `i`. Both the value printed AND the number of repetitions equal `i`. This makes the inner loop trivially `print i, repeated i times`.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Pattern problems are fundamentally about mapping (row, column) to an output character. A nested loop (outer for rows, inner for columns) naturally expresses this mapping. No data structure is needed beyond loop variables.

### Pattern Recognition
**Classification cue:** "Print a triangle pattern" --> nested loops. The key is figuring out the relationship between row index, column count, and the printed value. Here: value = row, count = row.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force / Optimal -- Nested Loops
**Intuition:** For each row `i` (1 to N), print the number `i` exactly `i` times, then move to the next line.

**Steps:**
1. Outer loop: `i` from 1 to N (rows).
2. Inner loop: `j` from 1 to `i` (columns in this row).
   - Print `i` followed by a space.
3. Print a newline after the inner loop.

**Dry Run Trace (N = 4):**

| Row (i) | Inner loop (j = 1..i) | Output |
|---------|----------------------|--------|
| 1 | j=1: print 1 | `1` |
| 2 | j=1,2: print 2 | `2 2` |
| 3 | j=1,2,3: print 3 | `3 3 3` |
| 4 | j=1,2,3,4: print 4 | `4 4 4 4` |

| Metric | Value |
|--------|-------|
| Time | O(N^2) -- total prints = 1+2+...+N = N(N+1)/2 |
| Space | O(1) |

**BUD Transition:** There is no way to reduce the number of characters printed (we must output every character), so O(N^2) is inherently optimal for this problem. The only variation is style, not complexity.

---

### Approach 2: String Repeat (Pythonic / StringBuilder)
**Intuition:** Instead of an inner loop, use string repetition to build each row in one expression.

**Steps:**
1. For each row `i` from 1 to N:
   - Build string `str(i) + " "` repeated `i` times.
   - Print the trimmed string.

| Metric | Value |
|--------|-------|
| Time | O(N^2) -- same total characters |
| Space | O(N) for the string of the current row |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row 1 prints 1 number, row 2 prints 2, ..., row N prints N. Total = 1 + 2 + ... + N = N(N+1)/2, which is O(N^2). You cannot print fewer characters because the problem demands exactly that many.

**Why O(1) space?** We only need loop variables. We print directly to output without storing the entire pattern.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing column index instead of row index | Confusing which variable to print | Print `i` (row), not `j` (column) |
| Off-by-one: starting from 0 | Using 0-indexed loop but pattern is 1-indexed | Start outer loop from 1, or adjust printed value |
| Trailing space issues | Extra space at end of row | Trim or use join |

### Edge Cases Checklist
- N = 1 --> single line: `1`
- N = 0 --> no output (empty pattern)
- Large N (100) --> works fine, just 5050 characters total

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Row i has i copies of the number i, right? 1-indexed?"
2. **M**atch: "Pattern printing --> nested loops."
3. **P**lan: "Outer loop rows 1..N, inner loop prints row number i times."
4. **I**mplement: Two loops, print i in the inner loop.
5. **R**eview: Trace N=3 to verify output.
6. **E**valuate: "O(N^2) time -- cannot improve since we must print that many characters."

### Follow-Up Questions
- "What if the pattern should be right-aligned?" --> Add leading spaces: N-i spaces before the numbers.
- "What if row i should print 1 to i instead of i repeated?" --> That is Pattern 4 (sequential numbers).
- "Can you do it with a single loop?" --> Yes, using string repetition, but time complexity is the same.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic loops, print formatting |
| **Same pattern** | Pattern 1 (star triangle), Pattern 2 (right-angled star) |
| **Harder variant** | Pattern 4 (sequential numbers), Diamond patterns |
| **Unlocks** | All nested-loop pattern problems, matrix traversal intuition |
