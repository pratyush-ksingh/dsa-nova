# Pattern 16 - Alpha Ramp

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a triangle where row `i` (1-indexed) contains the `i`-th uppercase letter repeated `i` times.

**Example:**
```
Input: N = 5
Output:
A
B B
C C C
D D D D
E E E E E
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=3   | `A`<br>`B B`<br>`C C C` | Row 1: A once, Row 2: B twice, Row 3: C thrice |
| N=1   | `A` | Single letter |
| N=4   | `A`<br>`B B`<br>`C C C`<br>`D D D D` | Letter advances and count grows with each row |

### Real-Life Analogy
Imagine a **staircase where each step is labeled**. The first step has one tile labeled 'A', the second step has two tiles labeled 'B', and so on. Each step is wider and carries a different label -- like floors in a parking garage where each floor's spots are marked with that floor's letter.

### Key Observations
1. Row `i` (0-indexed) prints the letter `'A' + i` repeated `i + 1` times.
2. Unlike P014, the letter does NOT start at A each row -- it advances.
3. **Aha moment:** The row index determines BOTH which letter to print AND how many times to print it.

### Constraints
- 1 <= N <= 26

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Outer loop picks the row (and therefore the letter). Inner loop repeats that letter the appropriate number of times. Character arithmetic maps row index to letter.

### Pattern Recognition
**Classification cue:** "Repeated character triangle" --> nested loops with constant character per row, count = row number.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Outer loop `i` from 0 to N-1 determines the letter. Inner loop `j` from 0 to `i` prints that letter.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Compute `ch = (char)('A' + i)`.
3. Loop `j` from 0 to `i`, print `ch`.
4. Print newline.

**Dry Run Trace (N=4):**

| i (row) | ch  | j range | Output |
|---------|-----|---------|--------|
| 0       | A   | 0       | `A\n` |
| 1       | B   | 0..1    | `B B\n` |
| 2       | C   | 0..2    | `C C C\n` |
| 3       | D   | 0..3    | `D D D D\n` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** Use string repetition to eliminate the inner loop.

---

### Approach 2: Optimal -- String Repetition per Row
**Intuition:** For each row, build the string by repeating the character. In Python: `"X " * count`. In Java: use `String.valueOf(ch).repeat(count)` with join.

**Steps:**
1. For row `i`, compute `ch = 'A' + i`.
2. Build `row = ch` repeated `i + 1` times with spaces.
3. Print `row`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) for the row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build all rows into a single string, print once.

**Steps:**
1. For each row `i`, create the repeated-character string.
2. Collect all rows into a list.
3. Join with newlines, print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row 1 has 1 char, row 2 has 2, ..., row N has N. Total = N(N+1)/2 = O(N^2). This is the minimum work since every character must be output.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing same letter every row | Using `'A'` instead of `'A' + i` | Letter must advance with row index |
| Wrong repetition count | Off-by-one: repeating `i` times instead of `i+1` | 0-indexed row `i` needs `i+1` repetitions |
| Mixing up with P014 | P014 prints A..i-th letter; P016 repeats i-th letter | Key difference: one letter per row vs. sequence of letters |

### Edge Cases Checklist
- N=1 --> single `A`
- N=26 --> last row is `Z` repeated 26 times
- N=0 --> print nothing

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: Clarify -- one letter per row, repeated. Spaces between?
2. **M**atch: "Triangle with repeated characters, count = row number."
3. **P**lan: "Outer loop for row/letter, inner loop for repetition."
4. **I**mplement: `ch = (char)('A' + i)` maps index to letter cleanly.
5. **R**eview: Dry run with N=3.
6. **E**valuate: "O(N^2) time, O(1) space for brute."

### Follow-Up Questions
- "What if the letter wraps after Z?" --> Use `'A' + (i % 26)`.
- "What if each row repeats a user-given string?" --> Replace char with string, same loop structure.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle (same shape with stars) |
| **Same pattern** | P004 Right-Angled Number Triangle II (same idea with numbers) |
| **Related** | P014 Increasing Letter Triangle (different letters per row vs. repeated) |
| **Harder variant** | P017 Alpha Hill (pyramid layout) |
