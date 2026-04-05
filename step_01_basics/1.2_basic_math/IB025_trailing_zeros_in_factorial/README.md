# Trailing Zeros in Factorial

> **Step 01 | 1.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given a non-negative integer `n`, return the number of trailing zeros in `n!` (n factorial).

A trailing zero is a zero at the end of the number. For example, 10! = 3628800 has 2 trailing zeros.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| 5     | 1      | 5! = 120, one trailing zero |
| 10    | 2      | 10! = 3628800, two trailing zeros |
| 25    | 6      | Extra zero from 25 = 5^2, contributing two 5s: 20+4+0=6 wait floor(25/5)+floor(25/25)=5+1=6 |
| 100   | 24     | floor(100/5)+floor(100/25)+floor(100/125) = 20+4+0 = 24 |
| 0     | 0      | 0! = 1, no trailing zeros |

## Constraints

- 0 <= n <= 10000

---

## Approach 1: Brute Force

**Intuition:** Compute n! directly using arbitrary-precision arithmetic, then count how many times 10 divides the result. This is correct but grows absurdly large — 100! has 158 digits. Only feasible in Python (native big integers) or Java (BigInteger) for small n.

**Steps:**
1. Compute `factorial = n!` using a loop (or `math.factorial`).
2. Initialize `count = 0`.
3. While `factorial % 10 == 0`: increment count, divide factorial by 10.
4. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) to compute + O(digits) to count zeros |
| Space  | O(n log n) bits to store the factorial |

---

## Approach 2: Optimal

**Intuition:** Trailing zeros are produced by factors of 10 = 2 x 5. In any n!, factors of 2 always outnumber factors of 5 (every even number contributes a 2, but only multiples of 5 contribute a 5). So the count of trailing zeros equals the total number of times 5 appears as a factor across all numbers 1 through n.

- Multiples of 5 contribute at least one 5.
- Multiples of 25 contribute an extra 5 (two total).
- Multiples of 125 contribute yet another, and so on.

Formula: `count = floor(n/5) + floor(n/25) + floor(n/125) + ...`

**Steps:**
1. Initialize `count = 0`, `power = 5`.
2. While `power <= n`: add `n // power` to count, multiply `power` by 5.
3. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(log5 n) |
| Space  | O(1) |

---

## Approach 3: Best

**Intuition:** Same as Optimal but avoids tracking a growing `power` variable. Instead, repeatedly divide `n` itself by 5. This works because `floor(n/5) + floor(n/25) + ... = floor(n/5) + floor((n/5)/5) + ...`. Each iteration reduces `n` to `n/5` and accumulates the result — fewer variables, no overflow risk.

**Steps:**
1. Initialize `count = 0`.
2. While `n >= 5`: set `n = n // 5`, add `n` to count.
3. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(log5 n) |
| Space  | O(1) |

---

## Real-World Use Case

**Combinatorics and number theory:** Legendre's formula (this very technique) is used to compute the exact exponent of any prime p in n!. It appears in computing binomial coefficients modulo a prime (Lucas' theorem), verifying whether a large factorial is divisible by a given power, and in competitive programming problems involving modular arithmetic. Database query planners also reason about the magnitude of intermediate results using similar digit-counting tricks.

## Interview Tips

- The key insight is that trailing zeros come from 2x5 pairs, and 5s are the bottleneck — state this immediately.
- Don't forget multiples of 25, 125, 625 each contribute *extra* 5-factors. The most common mistake is only computing `n/5`.
- The Best approach (repeatedly dividing n by 5) is the most concise — code it in 3 lines.
- Edge cases: n=0 returns 0 (0!=1, no zeros); n=4 returns 0 (4!=24, no zeros).
- Follow-up: "Trailing zeros in base 7?" — count factors of 7 the same way.
- Time complexity is O(log n) base 5 — mention this explicitly; interviewers appreciate precision.
