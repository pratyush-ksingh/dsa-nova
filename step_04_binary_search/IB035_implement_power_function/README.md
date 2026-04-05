# Implement Power Function

> **Batch 3 of 12** | **Topic:** Binary Search / Math | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement `pow(x, n)`, which calculates `x` raised to the power `n` (i.e., `x^n`). The exponent `n` can be negative.

**LeetCode #50 / InterviewBit: Implement Power Function**

**Constraints:**
- `-100.0 < x < 100.0`
- `-2^31 <= n <= 2^31 - 1`
- `n` is an integer
- Either `x` is not zero or `n > 0`
- `-10^4 <= x^n <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `x = 2.0, n = 10` | `1024.0` | 2^10 = 1024 |
| `x = 2.1, n = 3` | `9.261` | 2.1^3 = 9.261 |
| `x = 2.0, n = -2` | `0.25` | 2^(-2) = 1/(2^2) = 0.25 |
| `x = 1.0, n = 2147483647` | `1.0` | 1 raised to anything is 1 |

### Real-Life Analogy
> *Suppose you need to multiply a number by itself a billion times. Doing it one multiplication at a time would take a billion steps. But notice: x^8 = (x^4)^2 = ((x^2)^2)^2. Three squarings instead of seven multiplications! This "repeated squaring" trick -- also called binary exponentiation -- cuts the work from n steps to log(n) steps. It is the same idea behind fast modular exponentiation used in RSA encryption.*

### Key Observations
1. If `n` is even: `x^n = (x^(n/2))^2`. If `n` is odd: `x^n = x * x^(n-1)`. This halves the exponent each step. <-- This is the "aha" insight
2. Negative exponent: `x^(-n) = 1 / x^n`. Convert to positive and invert at the end.
3. Edge case: `n = Integer.MIN_VALUE` cannot be negated directly in 32-bit signed integers (overflow). Use `long` to handle this.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- No data structure needed. Pure math with divide-and-conquer.
- Binary exponentiation reduces O(n) multiplications to O(log n) by squaring.

### Pattern Recognition
- **Pattern:** Binary Exponentiation (Exponentiation by Squaring)
- **Classification Cue:** "Whenever you see _compute x^n efficiently_ --> binary exponentiation, O(log n)."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Multiply n Times
**Idea:** Multiply `x` by itself `|n|` times. If `n` is negative, take the reciprocal.

**Steps:**
1. If `n < 0`, set `x = 1/x` and `n = -n`.
2. Initialize `result = 1.0`.
3. For `i` from 0 to n-1: `result *= x`.
4. Return `result`.

**Warning:** This TLEs for large n (e.g., n = 2^31 - 1). Also, negating `Integer.MIN_VALUE` overflows.

**BUD Transition -- Bottleneck:** Each step does one multiplication, but we could square to halve the exponent.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Iterative Binary Exponentiation
**What changed:** Process the exponent bit by bit. If the current bit is 1, multiply the result by the current power of x. Always square x to advance to the next bit.

**Steps:**
1. Handle negative n: use `long N = n` to avoid overflow. If `N < 0`, set `x = 1/x` and `N = -N`.
2. Initialize `result = 1.0`.
3. While `N > 0`:
   - If `N` is odd (`N % 2 == 1`): `result *= x`.
   - `x *= x` (square the base).
   - `N /= 2` (halve the exponent).
4. Return `result`.

**Dry Run:** `x = 2.0, n = 10`

| Step | N | N odd? | result | x |
|------|---|--------|--------|---|
| init | 10 | | 1.0 | 2.0 |
| 1 | 10 | no | 1.0 | 4.0 (2^2) |
| 2 | 5 | yes | 4.0 | 16.0 (2^4) |
| 3 | 2 | no | 4.0 | 256.0 (2^8) |
| 4 | 1 | yes | 1024.0 | 65536.0 |
| 5 | 0 | | done | |

**Result:** 1024.0

**Why it works:** 10 in binary is `1010`. We multiply result by x^2 (bit 1) and x^8 (bit 3): 2^2 * 2^8 = 2^10 = 1024.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Recursive Binary Exponentiation
**What changed:** Same algorithm expressed recursively. Elegant and easy to reason about correctness.

**Steps:**
1. Base case: if `n == 0`, return `1.0`.
2. If `n < 0`: return `1.0 / pow(x, -n)` (use long to avoid overflow).
3. If `n` is even: `half = pow(x, n/2)`, return `half * half`.
4. If `n` is odd: return `x * pow(x, n-1)`.

**Note:** The iterative version (Approach 2) is preferred in interviews because it avoids O(log n) recursion stack and is easier to handle the overflow edge case.

| Time | Space |
|------|-------|
| O(log n) | O(log n) recursion stack |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Each step halves the exponent. From n, we reach 0 in log2(n) steps."
**Space:** O(1) iterative, O(log n) recursive -- "The iterative version uses constant extra space. The recursive version has log(n) stack frames."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Integer overflow when negating n:** `n = Integer.MIN_VALUE` = -2147483648. Negating gives 2147483648 which overflows int (max is 2147483647). Always cast to `long` first.
2. **Not handling x = 0:** `0^0` is typically defined as 1 in programming. `0^negative` is undefined (division by zero).
3. **Precision issues with floating point:** Accumulated floating-point errors. Not much we can do, but be aware.
4. **Forgetting the odd case:** If you only square without handling odd exponents, you skip necessary multiplications.

### Edge Cases to Test
- [ ] `n = 0` (any x^0 = 1)
- [ ] `n = 1` (x^1 = x)
- [ ] `n = -1` (x^-1 = 1/x)
- [ ] `n = Integer.MIN_VALUE` (overflow trap!)
- [ ] `x = 0, n > 0` (result is 0)
- [ ] `x = 1, n = huge` (result is 1)
- [ ] `x = -1, n = even` (result is 1)
- [ ] `x = -1, n = odd` (result is -1)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to compute x^n efficiently. n can be negative and very large."
2. **Match:** "Exponentiation by squaring -- process the exponent bit by bit, squaring the base each step."
3. **Plan:** "Handle negative n by inverting x and making n positive (as long). Then iteratively: if n is odd, multiply result by x; square x; halve n."
4. **Implement:** Write the iterative version. Highlight the `long` cast for overflow safety.
5. **Review:** Walk through x=2, n=10.
6. **Evaluate:** "O(log n) time, O(1) space. This is optimal."

### Follow-Up Questions
- "How would you compute x^n mod m?" --> Same algorithm, just take mod after each multiplication. This is modular exponentiation, used in cryptography (RSA, Diffie-Hellman).
- "What if n is a very large number stored as a string?" --> Parse bits from the string representation. Same squaring logic.
- "Can you compute matrix exponentiation?" --> Yes! Replace scalar multiplication with matrix multiplication. Used for Fibonacci in O(log n).

---

## CONNECTIONS
- **Prerequisite:** Understanding of binary representation, recursion
- **Same Pattern:** Fast Fibonacci via matrix exponentiation, modular exponentiation
- **Harder Variant:** Super Pow (LC #372), modular exponentiation with large exponents
- **This Unlocks:** Any algorithm that needs efficient repeated operations (matrix power, polynomial evaluation)
