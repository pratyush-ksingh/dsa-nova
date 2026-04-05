# Largest Coprime Divisor

> **Step 01 | 1.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given two positive integers `A` and `B`, find the **largest divisor of `A`** that is **coprime to `B`** (i.e., `gcd(divisor, B) == 1`).

A number `d` is coprime to `B` if they share no common prime factors.

## Examples

| Input (A, B) | Output | Explanation |
|-------------|--------|-------------|
| `30, 12`    | `5`    | Divisors of 30: 1,2,3,5,6,10,15,30. gcd(5,12)=1, so 5. |
| `15, 3`     | `5`    | 15=3*5. Remove factor 3 (shared with B=3). Result: 5. |
| `7, 2`      | `7`    | 7 and 2 are coprime already, so 7 itself. |
| `100, 10`   | `1`    | 100=2^2*5^2, 10=2*5. Remove all factors: result=1. |
| `48, 36`    | `1`    | 48=2^4*3, 36=2^2*3^2. Shared: 2,3. Remove all: result=1. |

## Constraints

- `1 <= A <= 10^9`
- `1 <= B <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** Enumerate all divisors of `A` in descending order and return the first one that is coprime to `B`.

**Steps:**
1. Iterate `d` from `A` down to `1`.
2. If `A % d == 0` and `gcd(d, B) == 1`, return `d`.
3. `1` is always coprime to anything, so we will always find an answer.

| Metric | Value |
|--------|-------|
| Time   | O(A)  |
| Space  | O(1)  |

---

## Approach 2: Optimal — Repeated GCD Removal

**Intuition:** If `gcd(A, B) > 1`, then `A` shares some prime factor `p` with `B`. Divide `A` by `gcd(A, B)` to remove that shared factor. Repeat until `gcd(A, B) == 1`. The resulting `A` is the largest divisor of the original `A` coprime to `B`.

**Why it's correct:** Each step divides `A` only by a factor that is also in `B`, so the result remains a divisor of the original `A`. When `gcd == 1`, no more shared primes remain, so the result is coprime to `B`. It's the **largest** such divisor because we only remove the minimum necessary.

**Steps:**
1. Compute `g = gcd(A, B)`.
2. While `g != 1`: set `A = A / g`, then `g = gcd(A, B)`.
3. Return `A`.

| Metric | Value |
|--------|-------|
| Time   | O(log^2(A)) |
| Space  | O(1) |

---

## Approach 3: Best — Same Algorithm, Explicit Factor Removal

**Intuition:** Functionally identical to Approach 2. For each common factor `g`, fully remove all its occurrences from `A` before recomputing gcd. This is equivalent and has the same complexity.

**Steps:**
1. Compute `g = gcd(A, B)`.
2. If `g == 1`, stop and return `A`.
3. Divide `A` by `g` completely (`while A % g == 0: A //= g`).
4. Go back to step 1.

| Metric | Value |
|--------|-------|
| Time   | O(log^2(A)) |
| Space  | O(1) |

---

## Real-World Use Case

**Cryptography and fraction reduction:** In RSA and other number-theoretic cryptosystems, you often need to extract the "coprime part" of a number relative to a modulus. For example, when computing modular inverses or simplifying fractions, finding the largest factor of the numerator coprime to the denominator is equivalent to fully reducing the fraction. The repeated-GCD technique also appears in computing the "smooth part" of a number in factoring algorithms.

## Interview Tips

- The elegant insight: you don't need to factorize anything. Just repeatedly apply `gcd` and divide.
- The brute force O(A) is too slow for A up to 10^9 — always push toward the gcd approach.
- The repeated division terminates quickly because each step reduces `A` by at least a factor of 2.
- Corner case: if `A` and `B` are already coprime, return `A` directly (the while loop never executes).
- Corner case: if `B = 1`, then `gcd(A, 1) = 1` immediately, so the answer is `A`.
- The answer is always at least 1 (since `gcd(1, B) = 1` for any `B`).
