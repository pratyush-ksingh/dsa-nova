# Pattern 14 - Increasing Letter Triangle

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a triangle where row `i` (1-indexed) contains the first `i` uppercase letters of the alphabet.

**Example:**
```
Input: N = 5
Output:
A
A B
A B C
A B C D
A B C D E
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=3   | `A`<br>`A B`<br>`A B C` | Row 1 has A, row 2 has A-B, row 3 has A-C |
| N=1   | `A` | Single letter |
| N=5   | `A`<br>`A B`<br>`...`<br>`A B C D E` | Each row adds the next letter |

### Real-Life Analogy
Imagine a **classroom seating chart** being filled row by row. In the first row, only seat A is taken. In the second row, seats A and B are taken. Each new row extends further across the alphabet. You are building up from a single letter to a full row, like progressively revealing columns of a spreadsheet.

### Key Observations
1. Row `i` always starts at 'A' and ends at the `i`-th letter.
2. The letter at column `j` is simply `'A' + j` (0-indexed).
3. **Aha moment:** The inner loop count grows linearly with the row number -- this is the classic "growing triangle" skeleton applied to characters instead of numbers.

### Constraints
- 1 <= N <= 26 (limited by alphabet size)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
The outer loop selects the row (1 to N). The inner loop prints letters from 'A' up to the row index. Character math (`'A' + offset`) converts an integer to the corresponding letter. No data structure needed.

### Pattern Recognition
**Classification cue:** "Print a triangular shape of characters" --> nested loops where inner bound depends on outer index.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Two for-loops. Outer iterates rows 0..N-1. Inner iterates columns 0..i, printing `(char)('A' + j)` each time. Newline after each row.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Loop `j` from 0 to `i`, print `(char)('A' + j)`.
3. Print newline.

**Dry Run Trace (N=4):**

| i (row) | j values | Letters printed | Output so far |
|---------|----------|-----------------|---------------|
| 0       | 0        | A               | `A\n` |
| 1       | 0,1      | A B             | `A\nA B\n` |
| 2       | 0,1,2    | A B C           | `A\nA B\nA B C\n` |
| 3       | 0,1,2,3  | A B C D         | `A\nA B\nA B C\nA B C D\n` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** Can we avoid the inner loop? Yes -- build a row string with substring/slice.

---

### Approach 2: Optimal -- String Building per Row
**Intuition:** Maintain a growing string. On each row, append the next letter to the string and print it. This avoids reconstructing from 'A' each time.

**Steps:**
1. Initialize `row = ""`.
2. For each `i` from 0 to N-1, append `(char)('A' + i)` to `row`, then print `row`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) for the row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire output as one string using a list of row strings, then join and print once. Minimizes I/O calls.

**Steps:**
1. Precompute the full alphabet row: `"A B C ... Z"`.
2. For each row `i`, take the first `(2*i + 1)` characters (letters + spaces).
3. Join all rows with newlines and print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row 1 prints 1 letter, row 2 prints 2, ..., row N prints N. Total characters = 1+2+...+N = N(N+1)/2, which is O(N^2). No approach can beat this since every character must appear in the output.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Using `'A' + i` instead of `'A' + j` | Confusing row index with column index | Inner loop variable is the column offset |
| Missing space between letters | Forgetting separator | Print space before each letter except the first |
| Off-by-one: printing N+1 rows | Using `<=` with 0-based loop | Use `< N` for 0-based indexing |

### Edge Cases Checklist
- N=1 --> single `A`
- N=26 --> full alphabet on the last row
- N=0 --> print nothing (if allowed)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: Clarify -- spaces between letters? 0-indexed or 1-indexed rows?
2. **M**atch: "This is a nested loop pattern with character arithmetic."
3. **P**lan: "Outer loop rows, inner loop appends letters starting from A."
4. **I**mplement: Use `(char)('A' + j)` in Java, `chr(65 + j)` in Python.
5. **R**eview: Dry run with N=3.
6. **E**valuate: "O(N^2) time, O(1) space for brute. Cannot improve since output size is O(N^2)."

### Follow-Up Questions
- "What if letters should be lowercase?" --> Change `'A'` to `'a'`.
- "What if it wraps after Z?" --> Use `'A' + (j % 26)`.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle (same structure with stars) |
| **Same pattern** | P003 Right-Angled Number Triangle |
| **Inverse** | P015 Reverse Letter Triangle (decreasing rows) |
| **Harder variant** | P017 Alpha Hill (pyramid shape with letters) |
