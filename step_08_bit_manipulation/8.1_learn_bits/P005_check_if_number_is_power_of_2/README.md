# Check if Number is Power of 2

> **Batch 3 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an integer `n`, return `true` if it is a power of two, otherwise return `false`. An integer `n` is a power of two if there exists an integer `x` such that `n == 2^x`.

**LeetCode #231**

**Constraints:**
- `-2^31 <= n <= 2^31 - 1`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `n = 1` | `true` | 2^0 = 1 |
| `n = 16` | `true` | 2^4 = 16 (binary `10000`) |
| `n = 3` | `false` | Not a power of 2 |
| `n = 0` | `false` | 0 is not a power of 2 |
| `n = -4` | `false` | Negative numbers are not powers of 2 |

### Real-Life Analogy
> *Think of a pizza that you keep cutting in half. Starting from 1 whole pizza, each cut doubles the number of slices: 1, 2, 4, 8, 16... These are all powers of 2. In binary, each of these numbers has exactly ONE bit set -- like a single light bulb on in a row. If you see a number with more than one "light on," it cannot be a pure doubling from 1.*

### Key Observations
1. A power of 2 in binary has exactly **one** set bit: `1`, `10`, `100`, `1000`, etc. <-- This is the "aha" insight
2. `n & (n-1)` removes the rightmost set bit. If the result is `0`, there was only one set bit, meaning `n` is a power of 2.
3. We must guard against `n <= 0` because 0 and negative numbers are never powers of 2 (even though `-n` might have one set bit in two's complement, that is irrelevant).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- No data structure needed. A single bitwise check answers the question in O(1).
- This builds directly on Brian Kernighan's trick from the previous problem (P004).

### Pattern Recognition
- **Pattern:** Bit manipulation property check
- **Classification Cue:** "Whenever you need to check if a number has exactly one set bit --> `n & (n-1) == 0`."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Repeated Division
**Idea:** Keep dividing by 2 as long as the number is even. If you reach 1, it is a power of 2.

**Steps:**
1. If `n <= 0`, return `false`.
2. While `n` is even (`n % 2 == 0`), divide: `n = n / 2`.
3. Return `n == 1`.

**Dry Run:** `n = 16`

| Step | n | n % 2 | Action |
|------|---|-------|--------|
| 1 | 16 | 0 | n = 8 |
| 2 | 8 | 0 | n = 4 |
| 3 | 4 | 0 | n = 2 |
| 4 | 2 | 0 | n = 1 |
| 5 | 1 | 1 | stop, n == 1 --> true |

**Result:** true

**BUD Transition -- Bottleneck:** We loop log2(n) times. Can we do this in O(1)?

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 2: Optimal -- Bit Trick n & (n-1)
**What changed:** A power of 2 has exactly one set bit. Clearing the rightmost set bit via `n & (n-1)` should yield 0.

**Steps:**
1. Return `n > 0 && (n & (n - 1)) == 0`.

**Why it works:**
- `n = 16` = `10000`. `n-1 = 15` = `01111`. `n & (n-1) = 00000` = 0. Power of 2!
- `n = 12` = `1100`. `n-1 = 11` = `1011`. `n & (n-1) = 1000` = 8. Not 0. Not a power of 2.

**Dry Run:** `n = 16`

| Expression | Binary | Result |
|------------|--------|--------|
| n | `10000` | 16 |
| n - 1 | `01111` | 15 |
| n & (n-1) | `00000` | 0 |
| 0 == 0? | | true |

**Result:** true

| Time | Space |
|------|-------|
| O(1) | O(1) |

### Approach 3: Best -- Count Set Bits (Alternative Perspective)
**What changed:** Use `Integer.bitCount(n) == 1` (Java) or `bin(n).count('1') == 1` (Python). This expresses the intent more clearly: a power of 2 has exactly one set bit.

**Note:** Under the hood, `bitCount` uses bit-parallel tricks and runs in O(1) on modern CPUs. The `n & (n-1)` approach is the classic interview answer; `bitCount` is the "clarity" alternative.

| Time | Space |
|------|-------|
| O(1) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) -- "A constant number of bitwise operations. No loops, no recursion."
**Space:** O(1) -- "Only a boolean result. No extra memory."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting `n > 0` check: `n = 0` gives `0 & (-1) = 0`, which would falsely return true without the guard.
2. Not handling negative numbers: in two's complement, negative numbers can have `n & (n-1) == 0` for certain values (e.g., `Integer.MIN_VALUE`).
3. Confusing "exactly one set bit" with "no set bits" -- 0 has no set bits but is NOT a power of 2.

### Edge Cases to Test
- [ ] `n = 0` (false)
- [ ] `n = 1` (true, 2^0)
- [ ] `n = -1` (false)
- [ ] `n = Integer.MIN_VALUE` (-2^31, false -- negative)
- [ ] `n = 1073741824` (2^30, true -- largest power of 2 in 32-bit signed)
- [ ] `n = 6` (false, two set bits)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to check if n is a power of 2. Powers of 2 are 1, 2, 4, 8, ..."
2. **Match:** "A power of 2 has exactly one set bit in binary. I can use the bit trick `n & (n-1) == 0`."
3. **Plan:** "Guard against n <= 0, then check the bit condition."
4. **Implement:** One-liner with the guard clause.
5. **Review:** Walk through n=16 and n=12 to show both cases.
6. **Evaluate:** "O(1) time and space. This is optimal."

### Follow-Up Questions
- "What if you cannot use bit manipulation?" --> Repeated division by 2 (Approach 1).
- "What if n can be a long?" --> Same logic, just use `long` type.
- "Check if n is a power of 4?" --> `n > 0 && (n & (n-1)) == 0 && (n & 0xAAAAAAAA) == 0` (set bit must be in an even position).

---

## CONNECTIONS
- **Prerequisite:** Remove Last Set Bit (P004) -- uses the same `n & (n-1)` trick
- **Same Pattern:** Power of 3 (LC #326), Power of 4 (LC #342)
- **Harder Variant:** Counting Bits (LC #338), Bitwise AND of Numbers Range (LC #201)
- **This Unlocks:** Any problem that uses "single set bit" as a building block
