# Pattern 12 - Number Crown Pattern

> **Batch 4 of 12** | **Topic:** Patterns / Nested Loops | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, print a "number crown" pattern with N rows. Each row has three parts: (1) increasing numbers on the left from 1 to `i`, (2) spaces in the middle, and (3) mirror-decreasing numbers on the right from `i` down to 1. The total width of each row is `2*N` characters (numbers + spaces).

**Example:**
```
Input: N = 5
Output:
1                 1
1 2             2 1
1 2 3         3 2 1
1 2 3 4     4 3 2 1
1 2 3 4 5 5 4 3 2 1
```

| Input | Output | Explanation |
|-------|--------|-------------|
| N=1   | `1 1` | 1 number each side, 0 inner spaces |
| N=2   | `1     1`<br>`1 2 2 1` | Row 1: 1, spaces, 1. Row 2: full. |
| N=3   | `1         1`<br>`1 2     2 1`<br>`1 2 3 3 2 1` | Numbers grow left and right, spaces shrink |

### Constraints
- 1 <= N <= 100

### Real-Life Analogy
Imagine two people standing at opposite ends of a hallway, both counting out loud. Each second, they each take one step closer to each other and say the next number. Person A counts up (1, 2, 3...) from the left; Person B counts down (3, 2, 1) from the right. The space between them shrinks each row until they meet in the middle. The "crown" shape forms naturally from this converging count.

### Key Observations
1. Row `i` (1-indexed) has `i` numbers on the left, `2*(N-i)` spaces in the middle, and `i` numbers (reversed) on the right. Total width per row is always `2*N` positions.
2. The number of inner spaces decreases by 2 each row: `2*(N-1)`, `2*(N-2)`, ..., 0.
3. **Aha moment:** The crown pattern is really three concatenated sections per row: left numbers + middle spaces + right numbers. Once you see this decomposition, it is just three simple inner loops.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Nested Loops?
Each row has three distinct sections, each requiring its own inner loop (or string operation). The outer loop iterates over rows, and three inner loops produce the left numbers, spaces, and right numbers.

### Pattern Recognition
**Classification cue:** "Mirrored numbers with a gap" --> three-section row decomposition. This pattern appears in butterfly patterns and similar symmetric designs.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Three Inner Loops per Row
**Intuition:** For each row, use three separate loops: one for left-side numbers (1 to i), one for middle spaces, and one for right-side numbers (i to 1).

**Steps:**
1. Loop `i` from 1 to N.
2. Inner loop 1: print numbers 1, 2, ..., i.
3. Inner loop 2: print `2*(N - i)` spaces.
4. Inner loop 3: print numbers i, i-1, ..., 1.
5. Newline.

**Dry Run Trace (N=4):**

| i | Left nums | Spaces (count) | Right nums | Output |
|---|-----------|---------------|------------|--------|
| 1 | 1         | 6             | 1          | `1           1` |
| 2 | 1 2       | 4             | 2 1        | `1 2       2 1` |
| 3 | 1 2 3     | 2             | 3 2 1      | `1 2 3   3 2 1` |
| 4 | 1 2 3 4   | 0             | 4 3 2 1    | `1 2 3 4 4 3 2 1` |

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(1) |

**BUD Transition:** String operations can replace the inner loops for cleaner code.

---

### Approach 2: Optimal -- String Building per Row
**Intuition:** Build each row as a string: left part is `" ".join(range(1, i+1))`, space padding, right part is the reverse.

**Steps:**
1. Loop `i` from 1 to N.
2. Build `left = " ".join(str(x) for x in range(1, i+1))`.
3. Build `right = " ".join(str(x) for x in range(i, 0, -1))`.
4. Compute `gap = " " * (2 * (N - i))` (adjusted for spacing between numbers).
5. Print `left + gap + right`.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N) per row string |

---

### Approach 3: Best -- Full Grid String, Single Print
**Intuition:** Build the entire crown as a single string and print once.

**Steps:**
1. For each row, build the three-part string.
2. Collect all rows, join with newlines, print once.

| Metric | Value |
|--------|-------|
| Time   | O(N^2) |
| Space  | O(N^2) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N^2)?** Each row has O(N) characters (left numbers + spaces + right numbers always sum to about 2N positions). With N rows, total output is O(N^2).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Wrong space count | Forgetting to account for spaces between numbers | Each number takes 2 chars (digit + space), so inner gap is `2*(N-i)` spaces when using space-separated numbers |
| Off-by-one in the gap | The gap between left and right should separate them cleanly | Test with the last row (i=N): gap should be 0, numbers should be adjacent |
| Printing extra space between left and right | Adding separator that should not exist | When gap is 0, left and right meet directly |

### Edge Cases Checklist
- N=1 --> `1 1` (two 1s side by side)
- N=2 --> two rows
- Large N=100 --> first row has 1 on each side with 198 spaces between

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Crown pattern -- numbers on left count up, numbers on right count down, spaces fill the middle. Total width is 2N positions?"
2. **M**atch: "Three-section row pattern. I decompose each row into left + gap + right."
3. **P**lan: "Three inner loops per row: left numbers, spaces, right numbers."
4. **I**mplement: Write with clear variable names for each section.
5. **R**eview: Dry-run with N=3, verify spacing alignment.
6. **E**valuate: "O(N^2) time, O(1) space."

### Follow-Up Questions
- "What if we want the crown with stars instead of numbers?" --> Replace number printing with star printing.
- "Can you make this a full butterfly (crown + inverted crown)?" --> Add the reverse rows below.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | P003 Number Triangle, P002 Right-Angled Triangle |
| **Same pattern** | Butterfly pattern (crown + inverted crown) |
| **Harder variant** | Number Crown with palindromic sequences |
| **Unlocks** | Multi-section row decomposition technique |
