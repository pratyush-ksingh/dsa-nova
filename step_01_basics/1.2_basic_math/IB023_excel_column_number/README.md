# Excel Column Number

> **Step 01 | 1.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given a column title as it appears in an Excel spreadsheet, return its corresponding column number.

```
A  -> 1
B  -> 2
Z  -> 26
AA -> 27
AB -> 28
AZ -> 52
ZY -> 701
```

| Input | Output | Explanation                              |
|-------|--------|------------------------------------------|
| "A"   | 1      | First column                             |
| "Z"   | 26     | 26th column                              |
| "AA"  | 27     | 26 + 1 = 27                              |
| "AB"  | 28     | 26 + 2 = 28                              |
| "AZ"  | 52     | 26 + 26 = 52                             |
| "ZY"  | 701    | 26*26 + 25 = 676 + 25 = 701              |

### Real-Life Analogy
Excel columns are a **base-26 numeral system** where the "digits" are letters A-Z. Unlike standard base-26 (which includes 0), here the digits are 1-26 (no zero digit). It's like a odometer that goes A, B, ..., Z, AA, AB, ..., instead of 0, 1, ..., 9, 10, 11.

### Key Observations
1. Each character represents a digit with value A=1, B=2, ..., Z=26.
2. The system is positional: leftmost character has the highest place value.
3. **Aha moment:** This is exactly polynomial evaluation -- `d[0]*26^(n-1) + d[1]*26^(n-2) + ... + d[n-1]*26^0`, which Horner's method evaluates in O(n) without computing powers.

### Constraints
- 1 <= s.length <= 7
- s consists only of uppercase English letters.
- s is a valid Excel column title (e.g., "A", "ZZZ").

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Base Conversion?
The problem is pure positional-numeral-system evaluation. No data structures needed -- a single accumulator variable suffices.

### Pattern Recognition
**Classification cue:** "Convert a string of letters to a number where each letter has a positional value" --> base-conversion pattern. Horner's method (scan left-to-right, `acc = acc * base + digit`) is the idiomatic solution.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Right-to-Left Positional Value
**Intuition:** Treat the title as digits in base-26. For each character, compute its place value as `digit * 26^position` where position counts from 0 at the rightmost character.

**Steps:**
1. Initialize `result = 0`.
2. For each index `i` from 0 to n-1:
   - Compute `value = char[i] - 'A' + 1`.
   - Compute `power = n - 1 - i`.
   - Add `value * 26^power` to `result`.
3. Return `result`.

**Dry Run ("AB"):**

| i | ch | value | power | contribution |
|---|----|-------|-------|--------------|
| 0 | A  | 1     | 1     | 1 * 26 = 26  |
| 1 | B  | 2     | 0     | 2 * 1  = 2   |

Result: 28

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

**BUD Transition:** Computing `26^power` is redundant work -- Horner's method avoids it entirely.

---

### Approach 2: Optimal -- Left-to-Right Horner's Method
**Intuition:** Horner's method evaluates a polynomial `a[0]*x^(n-1) + ... + a[n-1]*x^0` as `((...((a[0])*x + a[1])*x + a[2])*x + ...)`. Scan left-to-right, at each step multiply the running total by 26 and add the current digit's value.

**Steps:**
1. Initialize `result = 0`.
2. For each character `ch` in `s` (left to right):
   - `result = result * 26 + (ch - 'A' + 1)`
3. Return `result`.

**Dry Run ("AZ"):**

| Step | ch | digit | result before | result after |
|------|----|-------|---------------|--------------|
| 1    | A  | 1     | 0             | 0*26+1 = 1   |
| 2    | Z  | 26    | 1             | 1*26+26 = 52 |

Result: 52

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Functional Reduce (Same Complexity)
**Intuition:** The same Horner's accumulation expressed as a `reduce` fold over the characters. Mathematically identical to Approach 2; this is the one-liner form.

**Steps:**
1. Use `functools.reduce` with `lambda acc, ch: acc * 26 + (ord(ch) - ord('A') + 1)`.
2. Initial value is 0.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

Each character is visited exactly once, making all approaches O(n) time. The Brute Force computes `pow(26, k)` at each step (Python handles big ints natively, but it's still extra work). Optimal and Best use Horner's method which does only one multiply and one add per character.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Using A=0 instead of A=1 | Confusing with 0-indexed base-26 | Always `ch - 'A' + 1` |
| Integer overflow for long titles | 26^7 ~ 8 billion, exceeds 32-bit int | Use `long` in Java for safety |
| Off-by-one in power | Computing `26^n` instead of `26^(n-1)` for leftmost | Check dry run carefully |

### Edge Cases Checklist
- Single character "A" -> 1, "Z" -> 26
- Two characters "AA" -> 27, "AZ" -> 52, "ZZ" -> 702
- Maximum valid Excel column "XFD" -> 16384 (Excel 2007+)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "This is base-26 where A=1 not 0 -- confirm with 'AA'=27 example."
2. **M**atch: "Base conversion / polynomial evaluation pattern."
3. **P**lan: "Horner's method: scan left-to-right, result = result*26 + digit."
4. **I**mplement: 3-line loop.
5. **R**eview: Trace "AB" -> 28, "ZY" -> 701.
6. **E**valuate: O(n) time, O(1) space.

### Follow-Up Questions
- "What if you need to go the other way (number -> title)?" --> See IB024.
- "Handle lowercase input?" --> `toLowerCase()` first or use `ch | 32` trick.
- "What's the largest valid Excel column number?" --> Depends on version: 16384 for xlsx.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Inverse problem** | IB024 Excel Column Title (number -> string) |
| **Same pattern** | Any base conversion (binary, hex to decimal) |
| **Horner's method** | Polynomial evaluation, Rabin-Karp hash computation |
| **Builds on** | ASCII arithmetic (`ch - 'A'`) |
