# Pattern 18 - Alpha Triangle

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a **reverse alpha triangle**. Row `i` (0-indexed) contains characters from `chr('A' + N - 1 - i)` to `chr('A' + N - 1)`. In other words, the last row is `ABCDE...` (N letters), and each preceding row drops the leftmost character.

**Example (N=5, where the ending character is E):**
```
E
DE
CDE
BCDE
ABCDE
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `A`    | Single character |
| N=3   | `C`<br>`BC`<br>`ABC` | End char is C; each row adds one char on the left |
| N=5   | `E`<br>`DE`<br>`CDE`<br>`BCDE`<br>`ABCDE` | End char is E |

### Real-Life Analogy
Imagine a **waterfall cascading down steps**. The top step only catches the final trickle of water (one letter). Each step below catches more flow from further upstream, until the bottom step carries the full stream from source (A) to end.

### Key Observations
1. Every row ends at the same character: `chr('A' + N - 1)`.
2. Row `i` starts at `chr('A' + N - 1 - i)` and has `(i + 1)` characters.
3. **Aha moment:** The right column is fixed; only the starting point shifts left.

### Constraints
- 1 <= N <= 26 (limited by alphabet)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Standard 2D pattern problem. Outer loop = row, inner loop = characters in that row. Character values computed via arithmetic on ASCII codes.

### Pattern Recognition
**Classification cue:** "Right-aligned alphabet triangle with fixed end" --> compute start character per row, iterate to fixed end.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Explicit Nested Loops
**Intuition:** For each row `i`, calculate the starting offset from `'A'` as `(n-1-i)`, then loop `(i+1)` times printing consecutive characters.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Compute `start = n - 1 - i`.
3. Loop `j` from 0 to `i`: print `chr('A' + start + j)`.
4. Print newline.

**Dry Run Trace (N=4, end='D'):**

| i | start | Characters | Row Output |
|---|-------|-----------|------------|
| 0 | 3     | D         | `D`        |
| 1 | 2     | CD        | `CD`       |
| 2 | 1     | BCD       | `BCD`      |
| 3 | 0     | ABCD      | `ABCD`     |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Can we express the loop bounds more cleanly?

---

### Approach 2: Optimal -- Direct Character Range Loop
**Intuition:** Instead of counting with an index `j`, iterate directly over the character range from `startChar` to `endChar`. This makes the code self-documenting.

**Steps:**
1. Set `endChar = 'A' + n - 1`.
2. For row `i`, set `startChar = endChar - i`.
3. Loop `ch` from `startChar` to `endChar`, printing each.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

---

### Approach 3: Best -- String Slicing, Single Print
**Intuition:** Pre-build the full alphabet string `"ABCD...N"`. For each row, take a substring from the appropriate position to the end. Collect all rows and print once.

**Steps:**
1. Build `alpha = "ABCDE..."` of length N.
2. Row `i` = `alpha[n-1-i:]`.
3. Join all rows with newlines, single print.

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(N^2) for full string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row `i` has `(i+1)` characters. Total = 1 + 2 + ... + N = N*(N+1)/2 = O(N^2). Every character must be produced.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Starting from 'A' every row | Misreading the pattern | Start from `'A' + n - 1 - i` |
| Printing in wrong order (descending instead of ascending) | Confusing direction | Characters go left-to-right in ascending order |
| Off-by-one on end character | Using `n` instead of `n-1` | End offset is `n-1` (0-indexed) |

### Edge Cases Checklist
- N=1 --> just `A`
- N=26 --> full alphabet on last row

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Each row ends at the same letter and starts one letter earlier?"
2. **M**atch: "Simple nested loop pattern with character arithmetic."
3. **P**lan: "Compute start character per row, iterate to fixed end."
4. **I**mplement: Write clean code.
5. **R**eview: Dry run with N=3.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What if we want it right-aligned with spaces?" --> Add `(n-1-i)` leading spaces.
- "What if the starting letter is not A but given?" --> Parameterize the base character.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P013 Increasing Letter Triangle |
| **Same pattern** | P016 Alpha Ramp |
| **Harder variant** | P017 Alpha Hill (adds centering + palindrome) |
| **Unlocks** | Reverse string / substring manipulation intuition |
