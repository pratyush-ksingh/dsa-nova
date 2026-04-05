# Pattern 19 - Symmetric Void Pattern

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a **symmetric void pattern** -- two mirrored triangles of stars with an expanding then contracting gap of spaces in between. The pattern has **2*N rows**, each of width **2*N** characters.

**Example (N=5):**
```
**********
****  ****
***    ***
**      **
*        *
*        *
**      **
***    ***
****  ****
**********
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `**`<br>`**` | Minimal: 2 rows of 2 stars |
| N=2   | `****`<br>`*  *`<br>`*  *`<br>`****` | 4 rows, void opens and closes |
| N=3   | `******`<br>`**  **`<br>`*    *`<br>`*    *`<br>`**  **`<br>`******` | 6 rows |

### Real-Life Analogy
Imagine two **elevator doors** closing and opening. Initially they are fully closed (all stars), then they part in the middle creating a widening gap, reach maximum opening, then close back symmetrically. The stars are the door panels; the spaces are the gap between them.

### Key Observations
1. Total rows = 2*N. Width of each row = 2*N (constant).
2. **Upper half (rows 0 to N-1):** stars decrease from N to 1 on each side; spaces increase from 0 to 2*(N-1).
3. **Lower half (rows N to 2N-1):** mirror of upper half.
4. **Aha moment:** Row `i` has `stars = n - i` (upper) or `stars = i - n + 1` (lower), and `spaces = 2*N - 2*stars`.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
2D pattern with predictable star and space counts per row. No data structures needed.

### Pattern Recognition
**Classification cue:** "Symmetric shape with void" --> compute stars and spaces per half, mirror the halves.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Separate Top and Bottom Halves
**Intuition:** Write two separate loop blocks -- one for the upper half (stars shrinking, spaces growing) and one for the lower half (stars growing, spaces shrinking). Each row has three inner loops: left stars, middle spaces, right stars.

**Steps:**
1. **Upper half:** Loop `i` from 0 to N-1.
   - Left stars: `(n - i)` stars.
   - Middle spaces: `2 * i` spaces.
   - Right stars: `(n - i)` stars.
2. **Lower half:** Loop `i` from 0 to N-1.
   - Left stars: `(i + 1)` stars.
   - Middle spaces: `2 * (n - 1 - i)` spaces.
   - Right stars: `(i + 1)` stars.

**Dry Run Trace (N=3):**

| Half  | i | L-Stars | Spaces | R-Stars | Row Output |
|-------|---|---------|--------|---------|------------|
| Upper | 0 | 3       | 0      | 3       | `******`   |
| Upper | 1 | 2       | 2      | 2       | `**  **`   |
| Upper | 2 | 1       | 4      | 1       | `*    *`   |
| Lower | 0 | 1       | 4      | 1       | `*    *`   |
| Lower | 1 | 2       | 2      | 2       | `**  **`   |
| Lower | 2 | 3       | 0      | 3       | `******`   |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Can we unify the two halves into one loop?

---

### Approach 2: Optimal -- Unified Loop with Conditional
**Intuition:** Use a single loop from 0 to 2N-1. For `i < N`, use upper-half formula; for `i >= N`, use lower-half formula. This eliminates code duplication.

**Steps:**
1. Loop `i` from 0 to `2*N - 1`.
2. If `i < N`: `stars = n - i`, `spaces = 2 * i`.
3. Else: `stars = i - n + 1`, `spaces = 2 * (2*n - 1 - i)`.
4. Print: `"*" * stars + " " * spaces + "*" * stars`.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

---

### Approach 3: Best -- String Multiplication, Single Print
**Intuition:** Same unified formula, but build all rows as strings and print once to minimize I/O overhead.

**Steps:**
1. Use the same star/space formula as Optimal.
2. Build each row as a string, collect in a list.
3. Join with newlines and print once.

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(N^2) for full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Each of the 2N rows has exactly 2N characters. Total characters = 2N * 2N = 4N^2 = O(N^2).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Wrong space count | Forgetting to multiply by 2 | Spaces = `2 * i` (upper), `2 * (n-1-i)` (lower) |
| Asymmetric halves | Different logic for upper/lower | Verify lower half mirrors upper |
| Missing middle rows | Off-by-one between halves | Upper: 0..N-1, Lower: 0..N-1 (total 2N rows) |

### Edge Cases Checklist
- N=1 --> `**` / `**` (two rows, no gap)
- N=2 --> 4 rows, max gap of 2 spaces

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Two halves, each N rows. Stars on the sides, space in the middle?"
2. **M**atch: "Symmetric pattern -- compute stars and spaces per row."
3. **P**lan: "Unified loop: upper half shrinks stars, lower half grows them."
4. **I**mplement: Write clean conditional logic.
5. **R**eview: Dry run N=3.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What if we want a butterfly pattern (with no void)?" --> Fill the middle with a different character.
- "What about a hollow diamond?" --> Similar structure but only print border characters.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P010 Half Diamond Star, Inverted triangle patterns |
| **Same pattern** | Butterfly pattern (dual triangles) |
| **Harder variant** | Hollow diamond, number-filled void |
| **Unlocks** | Matrix border traversal, 2D symmetry |
