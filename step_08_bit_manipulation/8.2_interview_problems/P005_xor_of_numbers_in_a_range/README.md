# XOR of Numbers in a Range

> **Step 08 — 8.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given two non-negative integers `L` and `R`, return the **XOR of all integers** from `L` to `R` inclusive.

That is, compute `L XOR (L+1) XOR (L+2) XOR ... XOR R`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| L=1, R=5 | 1 | 1^2^3^4^5 = 1 |
| L=3, R=8 | 11 | 3^4^5^6^7^8 = 11 |
| L=0, R=0 | 0 | just 0 |
| L=2, R=4 | 5 | 2^3^4 = 5 |
| L=5, R=5 | 5 | just 5 |

## Constraints

- `0 <= L <= R <= 10^9`

---

## Approach 1: Brute Force — XOR All Numbers in Range

**Intuition:** Directly XOR every integer from L to R. Simple and correct, but O(n) where n = R - L + 1 can be up to 10^9 — too slow for large inputs.

**Steps:**
1. Initialize `result = 0`.
2. For `i` from `L` to `R`: `result ^= i`.
3. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(R - L + 1) = O(n) |
| Space  | O(1)  |

---

## Approach 2: Optimal — Prefix XOR with 4-Cycle Pattern

**Intuition:** Define `xorUpto(n)` = XOR of all integers from 0 to n. Then:

`XOR(L..R) = xorUpto(R) XOR xorUpto(L-1)`

because numbers 0 to L-1 appear in both prefix sums and cancel out (x XOR x = 0).

The key insight is that `xorUpto(n)` follows a **repeating pattern of period 4**:

| n % 4 | xorUpto(n) |
|-------|------------|
| 0     | n          |
| 1     | 1          |
| 2     | n + 1      |
| 3     | 0          |

**Why the 4-cycle?** XOR of any four consecutive integers `4k, 4k+1, 4k+2, 4k+3` equals 0: the upper bits cancel (same value XOR'd twice is 0) and the lower 2 bits: `00 XOR 01 XOR 10 XOR 11 = 00`. So `xorUpto(4k-1) = 0` for all k, and the remainder determines the result.

**Steps:**
1. Implement `xorUpto(n)` using the 4-cycle pattern.
2. Return `xorUpto(R) XOR xorUpto(L-1)`.

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1)  |

---

## Approach 3: Best — Same O(1) with Lookup Table Style

**Intuition:** Identical math as Approach 2. Presents the 4-cycle as a lookup array `[n, 1, n+1, 0]` indexed by `n % 4`. Compact and easy to remember during an interview.

**Steps:**
1. `prefixXor(n) = [n, 1, n+1, 0][n % 4]`
2. Return `prefixXor(R) XOR prefixXor(L-1)`.

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Error detection in storage:** A RAID controller XORs blocks in a stripe to produce a parity block. When verifying a range of block sequence numbers, this formula computes the expected parity checksum in O(1) rather than scanning all blocks — critical for fast integrity checks in terabyte-scale storage arrays.

**Embedded systems / competitive programming:** Computing XOR checksums over address ranges in firmware verification. The O(1) formula is essential when iterating over millions of 32-bit address ranges per second.

## Interview Tips

- The brute force is trivially correct — mention it quickly, then move to the pattern.
- Derive the 4-cycle: compute `xorUpto(0)` through `xorUpto(7)` manually (0, 1, 3, 0, 4, 1, 7, 0) to show the pattern. This demonstrates you understand it, not just memorized it.
- The prefix trick `XOR(L..R) = xorUpto(R) XOR xorUpto(L-1)` is the same idea as prefix sums for range sum queries — draw that parallel.
- Watch the edge case `L = 0`: `xorUpto(L-1) = xorUpto(-1)`. Handle it as 0 (XOR of empty range).
- This problem generalizes: XOR of any arithmetic sequence with constant difference 1 in a range can be solved similarly.
