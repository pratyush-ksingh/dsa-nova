# Pattern 10 - Half Diamond Star

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a **half diamond** (left-aligned) star pattern. The pattern has `(2*N - 1)` rows. Stars increase from 1 to N (one per row), then decrease from N back to 1. No leading spaces are needed since the pattern is left-aligned.

**Example:**
```
Input: N = 5
Output:
*
* *
* * *
* * * *
* * * * *
* * * *
* * *
* *
*
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `*` | Single star, single row |
| N=2   | `*`<br>`* *`<br>`*` | 3 rows: 1 star, 2 stars, 1 star |
| N=3   | `*`<br>`* *`<br>`* * *`<br>`* *`<br>`*` | 5 rows total |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Imagine a **breathing exercise**: you inhale gradually -- each breath deeper than the last -- until you reach maximum lung capacity. Then you exhale gradually, each breath shallower than the last, until you are back to the starting point. The number of stars on each row is like the depth of each breath: increasing to a peak, then decreasing symmetrically.

### Key Observations
1. The pattern has `(2*N - 1)` rows. The middle row (row N) has the most stars. Unlike the full diamond (Pattern 9), the peak row appears only once.
2. For the first N rows, row `i` (1-indexed) has `i` stars. For the remaining `N-1` rows, the star count mirrors the first half.
3. **Aha moment:** You can compute the star count for any row using `N - abs(N - 1 - i)` where `i` is 0-indexed over `(2*N - 1)` rows. This single formula handles both halves without a conditional, using the distance from the center row.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Each row has a variable number of stars. An outer loop iterates over `(2*N - 1)` rows, and an inner loop prints the stars for each row. No data structure needed -- direct output.

### Pattern Recognition
**Classification cue:** "Pattern that grows then shrinks" --> think of it as two triangles (increasing + decreasing) sharing the peak row. The `abs()` distance trick is a powerful general technique for symmetric patterns.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Two Separate Loops
**Intuition:** Print the increasing half (rows 1 to N) with one loop, then the decreasing half (rows N-1 down to 1) with another loop.

**Steps:**
1. Loop `i` from 1 to N: print `i` stars.
2. Loop `i` from N-1 down to 1: print `i` stars.

**Dry Run Trace (N=4):**

| Phase      | i | Stars | Output |
|------------|---|-------|--------|
| Increasing | 1 | 1     | `*` |
| Increasing | 2 | 2     | `* *` |
| Increasing | 3 | 3     | `* * *` |
| Increasing | 4 | 4     | `* * * *` |
| Decreasing | 3 | 3     | `* * *` |
| Decreasing | 2 | 2     | `* *` |
| Decreasing | 1 | 1     | `*` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** The two loops can be unified into one loop using the absolute-value formula.

---

### Approach 2: Optimal -- Single Loop with abs() Formula
**Intuition:** Use one loop over `(2*N - 1)` rows. The star count for row `i` (0-indexed) is `N - abs(N - 1 - i)`. This gives 1, 2, ..., N, ..., 2, 1 naturally.

**Steps:**
1. Loop `i` from 0 to `2*N - 2`.
2. Compute `stars = N - abs(N - 1 - i)`.
3. Print `stars` stars on each row.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) per row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire half diamond as a single string using the abs() formula and print once.

**Steps:**
1. For each row, compute star count and build the star string.
2. Collect all rows, join with newlines, print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) for the full grid |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Total stars = 1 + 2 + ... + N + (N-1) + ... + 1 = N^2. Each star must be printed, so O(N^2) is the minimum time. Space varies by approach.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Printing the peak row twice | Using two loops that both include N | First loop goes 1..N, second goes N-1..1 |
| Off-by-one in the total rows | Expecting 2N rows instead of 2N-1 | The peak row is shared; total = 2N - 1 |
| Wrong abs() formula | Getting the center index wrong | Center is at index N-1 (0-indexed) |

### Edge Cases Checklist
- N=1 --> single row with 1 star
- N=2 --> 3 rows: 1, 2, 1 stars
- Large N=100 --> 199 rows

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Half diamond, left-aligned, stars go 1 to N and back to 1. Total 2N-1 rows. No centering needed?"
2. **M**atch: "Symmetric growth-shrink pattern. I can split into two loops or use an abs() trick."
3. **P**lan: "I will use a single loop with the formula stars = N - |N - 1 - i|."
4. **I**mplement: Write the single-loop version.
5. **R**eview: Dry-run with N=3 to verify 5 rows.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "How would you center this to make a full diamond?" --> Add leading spaces: `(N - stars)` spaces per row. That gives Pattern 9.
- "What if the pattern uses numbers instead of stars?" --> Replace `*` with a counter or row number.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle (increasing stars) |
| **Same pattern** | P009 Diamond (centered version of this) |
| **Harder variant** | Half diamond with numbers, hollow half diamond |
| **Unlocks** | Understanding the abs() trick for symmetric patterns |
