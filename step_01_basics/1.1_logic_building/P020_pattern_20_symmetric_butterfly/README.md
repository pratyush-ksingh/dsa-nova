# Pattern 20 - Symmetric Butterfly

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a symmetric butterfly pattern of height **2N** rows. The pattern has a top half (rows 1..N) and a mirrored bottom half (rows N..1).

**Example (N=4):**
```
*      *
**    **
***  ***
********
********
***  ***
**    **
*      *
```

| Input | Output (shape description)                            | Explanation                              |
|-------|-------------------------------------------------------|------------------------------------------|
| N=1   | `*` (single star) x2                                 | 1 star, 0 spaces, 1 star -- twice        |
| N=3   | 6 rows; top half widens 1->3 stars, bottom mirrors   | Middle row is 6 stars wide              |
| N=4   | 8 rows; widest row is 8 stars (`*` * 4 + `*` * 4)   | No space gap in the middle row           |

### Real-Life Analogy
Think of a **butterfly spreading its wings**: at first the wings are folded (1 star each side with a big gap). As they open wider, the gap shrinks. Fully open = no gap at all. Then the wings fold back down. The symmetry axis is the centre.

### Key Observations
1. Row `i` (1-indexed, 1..N) prints `i` stars, then `2*(N-i)` spaces, then `i` stars again.
2. The total width of every row is exactly `2*N` characters.
3. **Aha moment:** The "gap" formula `2*(N-i)` shrinks by 2 each row: at i=1 gap=2*(N-1), at i=N gap=0.
4. The bottom half is simply the top half printed in reverse.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
We need to produce rows where width varies. The outer loop selects which row (and thus how many stars/spaces), the inner portion constructs the row content. No data structure is needed -- we print directly.

### Pattern Recognition
**Classification cue:** "Symmetric shape with widening/narrowing star blocks" --> variable inner loop bounds. The mirrored bottom half follows naturally from reversing the outer loop.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Character-by-Character Printing
**Intuition:** Use explicit inner loops to print each star and space one character at a time. Maximum verbosity, minimum abstraction.

**Steps:**
1. Loop `i` from 1 to N (upper half).
2. Inner loop: print `i` stars.
3. Inner loop: print `2*(N-i)` spaces.
4. Inner loop: print `i` stars.
5. Print newline.
6. Repeat steps 1-5 with `i` from N down to 1 (lower half).

**Dry Run Trace (N=3):**

| i | Stars | Spaces | Row Output |
|---|-------|--------|------------|
| 1 | 1     | 4      | `*    *`   |
| 2 | 2     | 2      | `**  **`   |
| 3 | 3     | 0      | `******`   |
| 3 | 3     | 0      | `******`   |
| 2 | 2     | 2      | `**  **`   |
| 1 | 1     | 4      | `*    *`   |

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

**BUD Transition:** Replace inner loops with string multiplication.

---

### Approach 2: Optimal -- String Multiplication per Row
**Intuition:** Python's `"*" * i` and Java's `"*".repeat(i)` replace the inner loops. Build each row as a single string and print it. Cleaner code, same asymptotic complexity.

**Steps:**
1. For each `i` in 1..N: print `"*"*i + " "*(2*(N-i)) + "*"*i`.
2. For each `i` in N..1: same formula.

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(n)   |

---

### Approach 3: Best -- Full Grid Built then Single Print
**Intuition:** Collect all row strings into a list and join with newlines before printing. Minimises I/O calls -- the bottleneck in competitive programming judge outputs.

**Steps:**
1. Build all 2N row strings using string multiplication.
2. Join with `"\n"`.
3. Single `print()` call.

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(n^2) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n^2)?** The total characters printed sum to `2 * sum(2*i for i in 1..n) = 2*n*(n+1)` which is O(n^2). You must produce every character, so O(n^2) is optimal.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                        | Why it happens                           | Fix                                       |
|-------------------------------|------------------------------------------|-------------------------------------------|
| Spaces = `2*(N-i)+1` instead  | Off-by-one thinking total width is odd  | Total row width = 2*N; gap = 2*(N-i)     |
| Printing N rows instead of 2N | Forgetting the bottom half              | Two separate loops or one loop 1..2N      |
| Bottom half starts at N-1     | Decrementing from wrong bound           | Lower loop starts at N (inclusive)        |

### Edge Cases Checklist
- N=1: `*` printed twice (no gap, no inner loop iterations for spaces)
- N=2: gap halves each row; middle rows have 0 spaces

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "For row i (1-indexed), how many stars and spaces?" Derive the formula on the whiteboard.
2. **M**atch: "This is a symmetric nested-loop pattern."
3. **P**lan: "Outer loop 1..N for top, N..1 for bottom. Stars = i, spaces = 2*(N-i)."
4. **I**mplement: Write clean code with descriptive variable names.
5. **R**eview: Trace N=3 by hand.
6. **E**valuate: "O(n^2) time, O(1) to O(n^2) space depending on approach."

### Follow-Up Questions
- "Can you do this without two separate loops?" --> Single loop from 1 to 2N using `min(i, 2N+1-i)` for star count.
- "How would you centre the pattern?" --> Add leading spaces before left wing.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                        |
|-------------------|----------------------------------------------------------------|
| **Prerequisite**  | P001 Rectangular Star (basic nested loops)                    |
| **Same pattern**  | Diamond patterns (stars grow then shrink)                     |
| **Harder variant**| Full symmetric diamond with hollow centre                     |
| **Unlocks**       | Any pattern with symmetric halves; palindrome intuition       |
