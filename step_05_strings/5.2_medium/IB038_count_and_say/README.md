# Count and Say

> **LeetCode 38** | **Step 05 - Strings (Medium)** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

The **count-and-say** sequence is a sequence of digit strings defined by the recursive formula:

- `countAndSay(1) = "1"`
- `countAndSay(n)` = the run-length encoding of `countAndSay(n - 1)`.

**Run-length encoding:** group consecutive identical characters, write the count followed by the character.

Example: `"1211"` → one `1`, one `2`, two `1`s → `"111221"`

Given a positive integer `n`, return the `n`th term of the count-and-say sequence.

## Examples

| n | Output | How it's derived |
|---|--------|-----------------|
| 1 | `"1"` | Base case |
| 2 | `"11"` | One 1 |
| 3 | `"21"` | Two 1s |
| 4 | `"1211"` | One 2, one 1 |
| 5 | `"111221"` | One 1, one 2, two 1s |
| 6 | `"312211"` | Three 1s, two 2s, one 1 |
| 7 | `"13112221"` | One 3, one 1, two 2s, two 1s |

## Constraints

- `1 <= n <= 30`

---

## Approach 1: Brute Force — Iterative Character-by-Character

**Intuition:** Start with `"1"`. For each of the next `n-1` steps, scan the current string from left to right, counting consecutive identical characters, and build the next string by appending `count + character`.

**Steps:**
1. `result = "1"`.
2. Repeat `n-1` times:
   a. `i = 0`, `next = []`.
   b. While `i < len(result)`:
      - Note `ch = result[i]`, `count = 0`.
      - While `result[i] == ch`: `count++`, `i++`.
      - Append `str(count) + ch` to `next`.
   c. `result = join(next)`.
3. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(n · L) where L = max string length in the sequence |
| Space  | O(L) |

---

## Approach 2: Optimal — Iterative with Two-Pointer Run Detection

**Intuition:** Same algorithm as Approach 1. Instead of an inner counter loop, use `start` and `end` pointers to delineate each run. When `result[end] != result[start]`, the run has ended. Run length = `end - start`.

**Steps:**
1. `result = "1"`.
2. Repeat `n-1` times:
   a. `start = 0`, `sb = StringBuilder`.
   b. While `start < len(result)`:
      - `end = start`.
      - Advance `end` while `result[end] == result[start]`.
      - Append `(end - start)` and `result[start]` to `sb`.
      - `start = end`.
   c. `result = sb.toString()`.
3. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(n · L) |
| Space  | O(L) |

---

## Approach 3: Best — Recursive with Memoization

**Intuition:** `countAndSay(n)` only depends on `countAndSay(n-1)`. Express this recursively and memoize computed terms so repeated calls with different `n` values are O(1) after the first computation.

**Steps:**
1. `memo[1] = "1"`.
2. `solve(n)`: if `memo[n]` exists return it; else compute from `solve(n-1)` using the run-length scan, store, and return.

| Metric | Value |
|--------|-------|
| Time   | O(n · L) first call; O(1) subsequent calls for same or smaller n |
| Space  | O(n · L) — all terms cached |

---

## Real-World Use Case

**Run-Length Encoding (RLE) compression:** The count-and-say sequence is a direct demonstration of RLE, one of the oldest and simplest lossless compression algorithms. RLE is used in: BMP/PCX image formats (compressing runs of the same pixel colour), fax transmission standards (ITU-T T.4), MPEG video for encoding runs of zero coefficients in DCT blocks, and DNA sequence storage where long runs of the same base pair are common.

## Interview Tips

- This is fundamentally a **run-length encoding** problem applied to itself iteratively — say that out loud.
- The iterative approach is preferred in interviews (no recursion depth risk for `n <= 30`).
- Using `itertools.groupby` (Python) or regex `(.)\1*` is elegant but know the manual loop too, as interviewers sometimes disallow library shortcuts.
- `n` is at most 30, so string lengths stay manageable (the longest term for `n=30` is around 5000 characters).
- A common mistake is off-by-one: the loop should run `n-1` times (not `n` times) since the first term is already `"1"`.
- The recursion + memoization variant is a useful follow-up showing you understand the structure of the recurrence.
