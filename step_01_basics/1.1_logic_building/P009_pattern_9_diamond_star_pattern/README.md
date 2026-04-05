# Pattern 9 - Diamond Star Pattern

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a **full diamond** star pattern. The diamond consists of an upright pyramid of N rows followed immediately by an inverted pyramid of N rows, giving **2*N total rows**. The widest row has `(2*N - 1)` stars and appears twice (at the junction of the two halves).

**Example:**
```
Input: N = 5
Output:
        *
      * * *
    * * * * *
  * * * * * * *
* * * * * * * * *
* * * * * * * * *
  * * * * * * *
    * * * * *
      * * *
        *
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `*`<br>`*` | Two rows, each a single star |
| N=2   | `  *`<br>`* * *`<br>`* * *`<br>`  *` | 4 rows total |
| N=3   | `    *`<br>`  * * *`<br>`* * * * *`<br>`* * * * *`<br>`  * * *`<br>`    *` | 6 rows total |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Think of a **cut diamond gemstone** viewed from the side. The top half (the crown) tapers upward to a point, and the bottom half (the pavilion) tapers downward to a point. The widest part in the middle is the girdle. Printing this diamond is like drawing that cross-section line by line -- expanding from a point, reaching maximum width, then contracting back to a point.

### Key Observations
1. The diamond is simply **Pattern 7 (upright pyramid) stacked on top of Pattern 8 (inverted pyramid)**. If you can print both, you can print a diamond.
2. Both halves share the same maximum width `(2*N - 1)` characters at their meeting point.
3. **Aha moment:** Rather than deriving new formulas, decompose the problem into two solved sub-problems. This "divide and reuse" mindset is the key pattern-building skill that applies to all compound shapes.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
The diamond is a 2D text output with 2N rows. Each row needs a computed number of leading spaces and stars. Nested loops handle this naturally -- outer loop for rows, inner loops for spaces and stars.

### Pattern Recognition
**Classification cue:** "Symmetric 2D shape" --> decompose into upper half and lower half, each of which is a standard pyramid variant. This decomposition strategy applies to diamonds, hourglasses, and other compound shapes.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Two Separate Pyramid Loops
**Intuition:** Print the upright pyramid (N rows) using Pattern 7 logic, then print the inverted pyramid (N rows) using Pattern 8 logic. Two separate loop blocks, each with inner loops for spaces and stars.

**Steps:**
1. **Upper half (upright pyramid):** Loop `i` from 0 to N-1. Print `(N-1-i)` spaces, then `(2*i + 1)` stars.
2. **Lower half (inverted pyramid):** Loop `i` from 0 to N-1. Print `i` spaces, then `(2*(N-i) - 1)` stars.

**Dry Run Trace (N=3):**

| Half  | i | Spaces | Stars | Output |
|-------|---|--------|-------|--------|
| Upper | 0 | 2      | 1     | `    *` |
| Upper | 1 | 1      | 3     | `  * * *` |
| Upper | 2 | 0      | 5     | `* * * * *` |
| Lower | 0 | 0      | 5     | `* * * * *` |
| Lower | 1 | 1      | 3     | `  * * *` |
| Lower | 2 | 2      | 1     | `    *` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** The two loops can be merged into a single loop over 2N rows with a conditional formula, reducing code duplication.

---

### Approach 2: Optimal -- Single Loop with Conditional
**Intuition:** Use one loop from 0 to 2N-1. For the first N rows, use the upright pyramid formula; for the last N rows, use the inverted pyramid formula. This avoids code duplication.

**Steps:**
1. Loop `i` from 0 to 2N-1.
2. If `i < N`: spaces = `N - 1 - i`, stars = `2*i + 1`.
3. If `i >= N`: let `j = i - N`, spaces = `j`, stars = `2*(N - j) - 1`.
4. Print spaces followed by stars for each row.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) per row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build all 2N rows as strings, join with newlines, and print once to minimize I/O overhead.

**Steps:**
1. For each of 2N rows, compute spaces and stars using the conditional formula.
2. Collect all row strings into a list.
3. Join with newlines and print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** The upper half prints `1 + 3 + 5 + ... + (2N-1) = N^2` star characters. The lower half prints the same. Total output is proportional to `2*N^2`, which is O(N^2). No approach can do better since every character must be produced.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Off-by-one at the boundary | Widest row printed once or three times | Verify: both halves include the full-width row, giving exactly 2 widest rows |
| Using 2N-1 rows instead of 2N | Trying to avoid the duplicate middle row | The standard diamond has 2N rows with widest row appearing twice |
| Mixing up upper/lower formulas | Forgetting to reset the index for the lower half | Use `j = i - N` for the lower half |

### Edge Cases Checklist
- N=1 --> two rows, each with a single star
- N=2 --> four rows
- Large N=100 --> 200 rows, widest has 199 stars

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "The diamond has 2N rows -- an upright pyramid on top and an inverted one below. Both halves include the widest row. Correct?"
2. **M**atch: "This is a compound pattern problem. I will decompose it into two known sub-patterns."
3. **P**lan: "Two loops: first prints the upright pyramid, second prints the inverted pyramid."
4. **I**mplement: Clean code reusing pyramid logic.
5. **R**eview: Dry-run with N=3 to confirm 6 rows of output.
6. **E**valuate: "O(N^2) time, O(1) space. Optimal since we must print every character."

### Follow-Up Questions
- "What if the diamond should have only one widest row (2N-1 total rows)?" --> Skip the first row of the inverted half (start lower loop from i=1).
- "Can you make it hollow?" --> Only print stars on the first/last position of each row, plus the full widest row.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P007 Star Pyramid, P008 Inverted Star Pyramid |
| **Same pattern** | P010 Half Diamond (similar decomposition, no centering) |
| **Harder variant** | Hollow diamond, rotated diamond |
| **Unlocks** | Complex compound patterns, understanding shape decomposition |
