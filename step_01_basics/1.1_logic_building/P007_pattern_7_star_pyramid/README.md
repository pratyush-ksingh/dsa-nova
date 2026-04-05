# Pattern 7 - Star Pyramid

> **Batch 3 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a centered star pyramid with N rows. Row `i` (1-indexed) has `(2*i - 1)` stars, preceded by `(N - i)` leading spaces.

**Example:**
```
Input: N = 5
Output:
    *
   ***
  *****
 *******
*********
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=5   | (pyramid above) | Row 1: 4 spaces + 1 star, Row 2: 3 spaces + 3 stars, ..., Row 5: 0 spaces + 9 stars |
| N=1   | `*` | Single star, no spaces |
| N=3   | `  *`<br>` ***`<br>`*****` | 3 rows centered |

### Real-Life Analogy
Think of building a **wedding cake** from top to bottom. Each tier is wider than the one above. To keep the cake centered on the table, you need to add equal "empty space" on either side of each tier. The top tier has the most empty space on each side and the fewest decorations (stars). Each subsequent tier has less empty space and more decorations, until the bottom tier spans the full width. The leading spaces are the empty table on the left side of each tier.

### Key Observations
1. Row `i` has `(N - i)` leading spaces and `(2*i - 1)` stars. The total width is always `2*N - 1`.
2. This pattern combines two sub-problems: printing spaces (a decreasing count) and printing stars (an increasing odd count).
3. **Aha moment:** The centered pyramid introduces the concept of leading spaces. Every "centered" pattern uses this trick: `spaces = total_width/2 - content_width/2`. Once you see this, you can center any pattern horizontally. The star count follows the odd number sequence: 1, 3, 5, 7, ... = `2*i - 1`.

### Constraints
- 1 <= N <= 100

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Each row has two components: spaces then stars. We use one inner loop for spaces and another for stars (or build the row as a string). No data structure is needed.

### Pattern Recognition
**Classification cue:** "Print a centered 2D shape" --> nested loops with a space loop + content loop. The space count and star count are both functions of the row index. This is the prototype for all centered/symmetric patterns.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Three Nested Loops
**Intuition:** For each row, use one loop to print leading spaces and another to print stars.

**Steps:**
1. Loop `i` from 1 to N.
2. Inner loop 1: print `(N - i)` spaces.
3. Inner loop 2: print `(2*i - 1)` stars (with spaces between if desired).
4. Print newline.

**Dry Run Trace (N=4):**

| i (row) | Spaces (N-i) | Stars (2i-1) | Output |
|---------|-------------|--------------|--------|
| 1       | 3           | 1            | `   *` |
| 2       | 2           | 3            | `  ***` |
| 3       | 1           | 5            | ` *****` |
| 4       | 0           | 7            | `*******` |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Replace inner loops with string repetition.

---

### Approach 2: Optimal -- String Repetition per Row
**Intuition:** Build each row as `" " * (N-i) + "*" * (2*i-1)` using built-in repetition. Cleaner code, same complexity.

**Steps:**
1. Loop `i` from 1 to N.
2. Build row: `spaces + stars` via string repetition.
3. Print row.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(N) per row |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build all rows into one string. Single I/O call minimizes overhead.

**Steps:**
1. For each `i` from 1 to N, build row `" "*(N-i) + "*"*(2*i-1)`.
2. Join with newlines.
3. Print once.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(N^2) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row i has `(N-i) + (2i-1) = N + i - 1` characters. Summing over all rows: sum of (N + i - 1) for i=1..N = N^2 + N(N+1)/2 - N = O(N^2). Every character must be produced, so this is optimal.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Wrong star count formula | Using `i` instead of `2*i-1` | Remember: centered pyramids use odd numbers |
| Trailing spaces on each row | Printing spaces after stars too | Only print leading spaces, not trailing |
| Off-by-one on spaces | `N-i` vs `N-i+1` | For 1-indexed i: `N-i` spaces. For 0-indexed: `N-1-i` |

### Edge Cases Checklist
- N=1 --> single star, no spaces
- N=100 --> base row has 199 stars
- Verify alignment: every row should appear centered

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Centered pyramid. Leading spaces needed? Trailing spaces OK?"
2. **M**atch: "Space loop + star loop per row. Stars = 2i-1, spaces = N-i."
3. **P**lan: "Two inner loops per row, or string repetition."
4. **I**mplement: Write clean formula-based code.
5. **R**eview: Trace N=3 to verify centering.
6. **E**valuate: "O(N^2) time, unavoidable since we print O(N^2) characters."

### Follow-Up Questions
- "Print an inverted pyramid?" --> Reverse: row 1 has N*2-1 stars and 0 spaces, row N has 1 star and N-1 spaces.
- "Print a diamond?" --> Combine upright pyramid (rows 1..N) + inverted pyramid (rows N-1..1).
- "Print hollow pyramid?" --> Only print stars at edges of each row.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle, P005 Inverted Triangle |
| **Same pattern** | Any centered pattern (diamond, hourglass) |
| **Harder variant** | Hollow pyramid, number pyramid |
| **Unlocks** | Diamond patterns, Christmas tree patterns |
