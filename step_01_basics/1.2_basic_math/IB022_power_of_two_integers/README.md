# Power of Two Integers

> **Step 01 | 1.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given a positive integer `A`, determine whether it can be expressed as `B^P` where `B >= 1` and `P >= 2`. Return `1` if yes, `0` otherwise.

**Special case:** `A = 1` can always be expressed as `1^P` for any `P >= 2`, so return `1`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `4`   | `1`    | 4 = 2^2 |
| `8`   | `1`    | 8 = 2^3 |
| `9`   | `1`    | 9 = 3^2 |
| `6`   | `0`    | 6 cannot be written as B^P for any B>=1, P>=2 |
| `1`   | `1`    | 1 = 1^2 |
| `27`  | `1`    | 27 = 3^3 |

## Constraints

- `1 <= A <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** Try every possible base `b` from `2` to `sqrt(A)`. For each base, compute `b^2, b^3, ...` until the value equals `A` (return true) or exceeds it (stop and try next base).

**Steps:**
1. Handle `A == 1` as a special case (return 1).
2. For `b` from `2` to `sqrt(A)`:
   a. Set `power = b * b`.
   b. While `power <= A`: if `power == A` return 1; else multiply `power *= b`.
3. If no match found, return 0.

| Metric | Value |
|--------|-------|
| Time   | O(sqrt(A) * log_b(A)) |
| Space  | O(1) |

---

## Approach 2: Optimal — Enumerate Exponents + Float Root

**Intuition:** Flip the perspective: instead of iterating over bases, iterate over exponents `p` from `2` to `32` (since `2^32 > 10^9`). For each `p`, compute the candidate base as `A^(1/p)` using floating point, then verify with exact integer arithmetic (checking `floor`, `ceil`, and `round` to handle float imprecision).

**Steps:**
1. Handle `A == 1` (return 1).
2. For `p` from `2` to `32`:
   a. Compute `b = round(A^(1/p))`.
   b. Check `b-1`, `b`, `b+1`: if any candidate `c >= 2` satisfies `c^p == A`, return 1.
3. Return 0.

| Metric | Value |
|--------|-------|
| Time   | O(32 * log(A)) |
| Space  | O(1) |

---

## Approach 3: Best — Enumerate Exponents + Binary Search

**Intuition:** Same as Approach 2 but use binary search instead of floating-point root to find the candidate base exactly. Binary search in `[2, A^(1/p)+2]` for each exponent. Long arithmetic prevents integer overflow.

**Steps:**
1. For each `p` from `2` to `32`, binary search for base `b` in `[2, A^(1/p)+2]`.
2. Compute `b^p` carefully (stop early if it exceeds `A` to avoid overflow).
3. Return 1 if any `b^p == A`, else 0.

| Metric | Value |
|--------|-------|
| Time   | O(32 * log(A)) |
| Space  | O(1) |

---

## Real-World Use Case

**Cryptography and hash functions:** Checking if a number is a perfect power arises in primality testing (e.g., Miller-Rabin preprocessing) and in number-theoretic algorithms. In cryptography, certain RSA attack scenarios require detecting whether a ciphertext is a perfect power. In competitive programming, perfect power checks appear frequently in factorization and number theory problems.

## Interview Tips

- The critical insight: only 32 possible exponents need to be checked since `2^32 > 10^9`.
- Floating point imprecision with `Math.pow` / `** (1/p)` is a classic pitfall — always check `floor`, `ceil`, and `round` of the computed root, or use binary search for exact results.
- Don't forget the special case `A = 1` (1 = 1^P for any P >= 2).
- For exponent `p=1`, every number qualifies — the constraint is `P >= 2`.
- Using `long` arithmetic in Java when computing `b^p` prevents overflow for large inputs.
- Brute force (iterating bases) only goes up to `sqrt(A)` because if `B^P = A` and `P >= 2`, then `B <= sqrt(A)`.
