# Pattern 15 - Reverse Letter Triangle

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print an inverted triangle where row `i` (1-indexed) contains the first `N - i + 1` uppercase letters.

**Example:**
```
Input: N = 5
Output:
A B C D E
A B C D
A B C
A B
A
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=3   | `A B C`<br>`A B`<br>`A` | Row 1 has A-C, row 2 has A-B, row 3 has A |
| N=1   | `A` | Single letter |
| N=4   | `A B C D`<br>`A B C`<br>`A B`<br>`A` | Each row removes the last letter |

### Real-Life Analogy
Think of a **countdown display board** where text lines are progressively shortened. Like an airport departure board that removes destinations as flights take off -- each update shows fewer entries but always starts from the top of the list.

### Key Observations
1. Every row starts at 'A'; only the ending letter changes.
2. Row `i` (0-indexed) ends at letter `N - i - 1`, so it prints `N - i` letters.
3. **Aha moment:** This is the mirror of P014. Instead of the inner loop growing, it shrinks.

### Constraints
- 1 <= N <= 26

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Same reasoning as P014 but the inner loop bound decreases with each row. Outer loop = rows, inner loop = letters per row (N-i letters). No data structures required.

### Pattern Recognition
**Classification cue:** "Inverted triangle of characters" --> nested loops with a decreasing inner bound.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** Outer loop rows 0..N-1. Inner loop columns 0..N-i-1, printing `(char)('A' + j)`.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Loop `j` from 0 to `N - i - 1`, print `(char)('A' + j)`.
3. Print newline.

**Dry Run Trace (N=4):**

| i (row) | j range | Letters printed | Output |
|---------|---------|-----------------|--------|
| 0       | 0..3    | A B C D         | `A B C D\n` |
| 1       | 0..2    | A B C           | `A B C\n` |
| 2       | 0..1    | A B             | `A B\n` |
| 3       | 0..0    | A               | `A\n` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** Build the full row once and take shrinking substrings.

---

### Approach 2: Optimal -- Substring per Row
**Intuition:** Build the full N-letter row once. For each row, print a substring of decreasing length.

**Steps:**
1. Build `fullRow = "A B C ... (N-th letter)"`.
2. For row `i`, print `fullRow[0 : 2*(N-i) - 1]`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) for the full row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build every row via substring slicing of the precomputed full row, join with newlines, print once.

**Steps:**
1. Precompute `fullRow`.
2. Collect all row substrings into a list.
3. Join with `\n` and print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total letters printed = N + (N-1) + ... + 1 = N(N+1)/2 = O(N^2). Every letter must appear in the output, so no approach can do better.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Inner loop going in wrong direction | Confusing increasing vs decreasing triangle | Inner bound = `N - i`, not `i + 1` |
| Off-by-one on substring length | Forgetting spaces between letters | Substring length is `2*(N-i) - 1`, not `N-i` |
| Printing rows in wrong order | Not realizing row 0 is the longest | First row has N letters, last row has 1 |

### Edge Cases Checklist
- N=1 --> single `A`
- N=26 --> first row is entire alphabet
- N=0 --> print nothing

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: Clarify -- always uppercase? Spaces between letters?
2. **M**atch: "Inverted triangle, same skeleton as P014 but shrinking rows."
3. **P**lan: "Precompute full row, take shrinking substrings."
4. **I**mplement: Use substring/slice for clean code.
5. **R**eview: Dry run with N=3.
6. **E**valuate: "O(N^2) time, cannot do better."

### Follow-Up Questions
- "Can you do both P014 and P015 in one function?" --> Print increasing then decreasing in same loop.
- "What about right-aligned?" --> Add leading spaces: `N - i - 1` spaces before letters.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P014 Increasing Letter Triangle |
| **Same pattern** | P005 Inverted Right Triangle (same idea with stars) |
| **Harder variant** | P018 Alpha Triangle (rows start at different letters) |
| **Unlocks** | Diamond / hourglass letter patterns |
