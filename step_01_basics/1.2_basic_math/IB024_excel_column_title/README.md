# Excel Column Title

> **Step 01 | 1.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given a positive integer `n`, return its corresponding Excel column title as a string.

Excel columns are labeled: A, B, C, ..., Z, AA, AB, ..., AZ, BA, ..., ZZ, AAA, ...

This is a 1-indexed base-26 system: column 1 = "A", column 26 = "Z", column 27 = "AA".

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| 1     | "A"    | 1st column  |
| 26    | "Z"    | 26th column |
| 27    | "AA"   | 27 = 26 + 1 |
| 28    | "AB"   | 27 + 1 |
| 701   | "ZY"   | 26*27 - 1 = 701 |
| 702   | "ZZ"   | 26*27 = 702 |
| 703   | "AAA"  | 703rd column |

## Constraints

- 1 <= n <= 2^31 - 1

---

## Approach 1: Brute Force

**Intuition:** This is essentially a base-26 conversion, but 1-indexed instead of 0-indexed. The trick is to subtract 1 before taking modulo, which shifts the range from [1..26] to [0..25], mapping cleanly to [A..Z]. Prepend each discovered character to the front of the result string.

**Steps:**
1. While `n > 0`:
   a. Subtract 1 from `n` to make it 0-indexed.
   b. Compute `remainder = n % 26` — this is the index of the current (rightmost) character.
   c. Prepend `chr('A' + remainder)` to the result string.
   d. Set `n = n // 26` to process the next digit.
2. Return the result string.

| Metric | Value |
|--------|-------|
| Time   | O(log₂₆ n) |
| Space  | O(log₂₆ n) |

---

## Approach 2: Optimal

**Intuition:** Same algorithm as Brute Force, but instead of prepending to a string (which is O(k) per operation in many languages), we append characters to a list/StringBuilder and reverse at the end. This is the most efficient iterative version.

**Steps:**
1. Create an empty list `chars`.
2. While `n > 0`:
   a. Subtract 1 (0-index shift).
   b. Append `chr('A' + n % 26)` to `chars`.
   c. `n = n // 26`.
3. Reverse `chars` and join into a string.

| Metric | Value |
|--------|-------|
| Time   | O(log₂₆ n) |
| Space  | O(log₂₆ n) |

---

## Approach 3: Best

**Intuition:** Recursive version — most readable and interview-friendly. The base case `n == 0` returns an empty string. Each call contributes one character and delegates the prefix to recursion. The call stack naturally builds the string left-to-right.

**Steps:**
1. Base case: if `n == 0`, return `""`.
2. Subtract 1 from `n`.
3. Return `best(n // 26) + chr('A' + n % 26)`.

| Metric | Value |
|--------|-------|
| Time   | O(log₂₆ n) |
| Space  | O(log₂₆ n) call stack |

---

## Real-World Use Case

**Spreadsheet engines** (Excel, Google Sheets, LibreOffice Calc) use exactly this algorithm to generate column labels. Any time you programmatically generate or parse a spreadsheet — exporting reports, building pivot tables, or writing test automation for spreadsheet UIs — you need this conversion. It also appears in any system that needs human-readable labels for a large but finite indexed set (e.g., labeling diagram nodes or auto-naming database columns).

## Interview Tips

- The #1 mistake is forgetting to subtract 1 before the modulo. Without it, Z (=26) gives `26 % 26 = 0`, which has no valid character.
- Remember: this is NOT standard base-26. It's "bijective base-26" — there is no zero digit. A=1, Z=26, AA=27.
- If asked for the reverse (column title to number), use: `result = result * 26 + (char - 'A' + 1)` for each character left-to-right.
- The number of digits in the result is `floor(log₂₆(n)) + 1` approximately — mention this when discussing complexity.
- Recursive solution impresses in interviews; make sure to explain the base case clearly.
