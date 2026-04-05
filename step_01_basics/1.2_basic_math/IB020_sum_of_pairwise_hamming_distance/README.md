# Sum of Pairwise Hamming Distance

> **Step 01 | 1.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an integer array `A` of `N` elements, find the sum of **bit differences** among all pairs `(i, j)` where `i != j` (ordered pairs). The bit difference of two numbers is the count of bit positions where they differ (Hamming distance).

Return the result modulo `10^9 + 7`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 3, 5]` | `8` | HD(1,3)=1, HD(1,5)=1, HD(3,5)=2. Ordered pairs double it: (1+1+2)*2 = 8 |
| `[4, 14, 2]` | `12` | 4=0100, 14=1110, 2=0010. Sum of all ordered pair HDs = 12 |
| `[1]` | `0` | Only one element, no pairs |

## Constraints

- `1 <= N <= 10^5`
- `0 <= A[i] <= 10^9`
- Result modulo `10^9 + 7`

---

## Approach 1: Brute Force

**Intuition:** For every ordered pair `(i, j)` with `i != j`, XOR the two numbers and count the set bits (which gives their Hamming distance). Accumulate all results.

**Steps:**
1. Iterate over all pairs `(i, j)` where `i != j`.
2. Compute `xor = A[i] ^ A[j]`.
3. Count set bits in `xor` using `popcount` / `bin(xor).count('1')`.
4. Add to total. Return `total % MOD`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2 * 32) |
| Space  | O(1) |

---

## Approach 2: Optimal — Bit-by-Bit Contribution

**Intuition:** Instead of looking at pairs, analyze each bit position independently. For bit `b`, if `c` numbers have bit `b` set and `(n-c)` have it clear, then every (one, zero) pair and every (zero, one) pair differs at bit `b`. There are `c * (n-c)` unordered pairs, so `2 * c * (n-c)` ordered pairs contribute 1 each to the total.

**Steps:**
1. For each bit position `b` from 0 to 31:
   a. Count `ones = count of numbers with bit b set`.
   b. `zeros = n - ones`.
   c. `contribution = 2 * ones * zeros`.
2. Sum all contributions modulo `10^9 + 7`.

| Metric | Value |
|--------|-------|
| Time   | O(32 * n) = O(n) |
| Space  | O(1) |

---

## Approach 3: Best — Same as Optimal

**Intuition:** The bit-by-bit observation is already optimal. No better asymptotic complexity exists since we must read all n elements. This approach is written with minor stylistic variation (using unsigned right shift `>>>` in Java for safety with sign bit).

**Steps:**
1. Same as Approach 2 — iterate 32 bits, count ones per bit, add `2 * ones * (n - ones)` to total.

| Metric | Value |
|--------|-------|
| Time   | O(32 * n) = O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Error detection and DNA analysis:** Hamming distance is used to measure how different two DNA sequences or binary codes are. The sum of pairwise Hamming distances over a whole dataset measures the total "diversity" or "spread" of the data — useful in genetics (measuring population genetic diversity) and communications (measuring average bit-error distance across a batch of transmitted messages).

## Interview Tips

- The key insight is the **per-bit decomposition**: treat each of the 32 bits independently. For bit `b`, the contribution to the total is `2 * c * (n-c)` where `c` is the count of 1s at that bit.
- This transforms an O(n^2) problem into O(n) by avoiding enumeration of pairs entirely.
- Remember the factor of 2 — the problem asks for **ordered** pairs (both `(i,j)` and `(j,i)` count).
- Don't forget modular arithmetic when `n` is large (n up to 10^5 means `2 * c * (n-c)` can be up to ~5 * 10^9).
- Related problems: Total Hamming Distance (LeetCode 477), finding the most diverse subset.
