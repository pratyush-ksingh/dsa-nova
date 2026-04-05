# Pattern 17 - Alpha Hill

> **Batch 1 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print an **alpha hill** pattern of N rows. Each row `i` (0-indexed) is centered and contains alphabets ascending from `A` to `chr('A'+i)` then descending back to `A`, forming a hill shape.

**Example (N=5):**
```
    A
   ABA
  ABCBA
 ABCDCBA
ABCDEDCBA
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `A`    | Single character |
| N=3   | `  A`<br>` ABA`<br>`ABCBA` | 3 rows, each row is a palindromic hill of alphabets |
| N=4   | `   A`<br>`  ABA`<br>` ABCBA`<br>`ABCDCBA` | 4 rows centered |

### Real-Life Analogy
Think of a **mountain range viewed from the front** -- each mountain has a peak in the center and symmetric slopes on both sides. The alphabets represent elevation markers, with `A` at the base and the highest letter at the peak of each ridge. Each successive row is a wider, taller mountain.

### Key Observations
1. Row `i` has `(n - i - 1)` leading spaces, then `(2*i + 1)` characters.
2. The characters form a **palindrome**: A...peak...A, where peak = `chr('A' + i)`.
3. **Aha moment:** The character at position `j` in the row can be determined by its distance from the center: `chr('A' + i - |i - j|)`.

### Constraints
- 1 <= N <= 26 (limited by alphabet)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
This is a 2D pattern. The outer loop selects the row; inner loop(s) fill in spaces and characters. No data structure is needed -- direct character computation suffices.

### Pattern Recognition
**Classification cue:** "Centered alphabetic palindrome per row" --> spaces for centering + mirrored character sequence.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Three Inner Loops
**Intuition:** For each row, use three separate inner loops: one for leading spaces, one for ascending characters (A to peak), and one for descending characters (peak-1 back to A).

**Steps:**
1. Loop `i` from 0 to N-1.
2. Print `(n - i - 1)` spaces.
3. Print characters `A` to `chr('A' + i)` (ascending).
4. Print characters `chr('A' + i - 1)` down to `A` (descending).
5. Print newline.

**Dry Run Trace (N=3):**

| i | Spaces | Ascending | Descending | Row Output |
|---|--------|-----------|------------|------------|
| 0 | 2      | A         | (none)     | `  A`      |
| 1 | 1      | AB        | A          | ` ABA`     |
| 2 | 0      | ABC       | BA         | `ABCBA`    |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Can we merge the ascending and descending loops into one?

---

### Approach 2: Optimal -- Single Inner Loop with Distance Formula
**Intuition:** Each row has `2*i + 1` character slots. The character at position `j` depends on its distance from the center (position `i`): `chr('A' + i - abs(i - j))`. This replaces two loops with one.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Print `(n - i - 1)` spaces.
3. Loop `j` from 0 to `2*i`: compute `dist = abs(i - j)`, print `chr('A' + i - dist)`.
4. Print newline.

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

---

### Approach 3: Best -- String Building with Centering
**Intuition:** Build each row as a palindromic string (ascending half + mirror), then use string centering to pad with spaces. Single print call for minimal I/O overhead.

**Steps:**
1. For row `i`, build `left = "ABC...peak"`.
2. Build `right = reverse(left[:-1])`.
3. Concatenate `left + right`, center to width `2*N - 1`.
4. Collect all rows, join with newlines, print once.

| Metric | Value   |
|--------|---------|
| Time   | O(N^2)  |
| Space  | O(N^2) for full grid string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Row `i` has `2*i + 1` characters. Summing over all rows: 1 + 3 + 5 + ... + (2N-1) = N^2. You must produce every character, so O(N^2) is optimal.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Off-by-one in descending loop | Starting at `i` instead of `i-1` | Descending starts at `i-1` to avoid printing peak twice |
| Wrong number of spaces | Miscounting leading spaces | Spaces = `n - i - 1` (0-indexed) |
| Characters exceed 'Z' | N > 26 | Constrain N <= 26 |

### Edge Cases Checklist
- N=1 --> single `A`
- N=26 --> full alphabet hill

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Each row is a centered palindrome of alphabets?"
2. **M**atch: "Nested loop pattern with character arithmetic."
3. **P**lan: "Leading spaces, then ascending chars, then descending."
4. **I**mplement: Clean code with clear loop bounds.
5. **R**eview: Dry run with N=3.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What if we want numbers instead of letters?" --> Replace `chr('A'+j)` with `j+1`.
- "What about a diamond (hill + inverted hill)?" --> Add mirrored rows below.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P002 Right-Angled Triangle, P013 Increasing Letter Triangle |
| **Same pattern** | P016 Alpha Ramp (alphabets without centering) |
| **Harder variant** | Diamond pattern (hill + inverted hill) |
| **Unlocks** | String palindrome intuition |
