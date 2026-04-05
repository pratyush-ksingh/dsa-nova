# Smallest Sequence with Given Primes

> **Step 16 | 16.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given three prime numbers `A`, `B`, `C` and an integer `N`, return a sorted array of the first `N` **positive integers whose only prime factors are A, B, and C**.

These are called **A-B-C smooth numbers** (generalized ugly numbers). Numbers like 1 (no prime factors) are **excluded**; the sequence starts from the primes themselves.

**Input:** Primes A, B, C; count N.
**Output:** Sorted array of first N smooth numbers.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| A=2,B=3,C=5, N=5 | [2,3,4,5,6] | 2=2, 3=3, 4=2^2, 5=5, 6=2*3 |
| A=2,B=3,C=5, N=10 | [2,3,4,5,6,8,9,10,12,15] | Multiples of only 2,3,5 |
| A=2,B=3,C=7, N=5 | [2,3,4,6,7] | 5 is excluded (has prime factor 5) |
| A=3,B=5,C=7, N=4 | [3,5,7,9] | 9=3^2, no 1 in the sequence |

## Constraints

- 2 <= A, B, C <= 10^9, all are prime
- 1 <= N <= 10^5
- A, B, C may be equal

---

## Approach 1: Brute Force

**Intuition:** Iterate all integers starting from 2. For each number, divide out A, B, C repeatedly. If the remainder is 1, the number qualifies.

**Steps:**
1. For `candidate` starting at 2:
   a. Divide `candidate` by A as many times as possible.
   b. Divide by B as many times as possible.
   c. Divide by C as many times as possible.
   d. If remainder == 1, add to result.
2. Stop when `result` has N elements.

**Limitation:** Extremely slow when A, B, C are large primes (e.g., A=997, B=991, C=983) — the N-th smooth number can be astronomically large.

| Metric | Value |
|--------|-------|
| Time   | O(N * max_val^(1/min(A,B,C))) — practically very slow |
| Space  | O(N) |

---

## Approach 2: Optimal — Three-Pointer Merge

**Intuition:** This is the classic **Ugly Number II** algorithm generalized to arbitrary primes. We maintain three pointers `pa`, `pb`, `pc` into the result array, each tracking which previously generated number to multiply next by A, B, or C.

Key insight: every smooth number is produced by multiplying a previously generated smooth number by A, B, or C.

**Steps:**
1. Initialize `result[0] = min(A, B, C)`.
   Actually: seed with `next_a = A`, `next_b = B`, `next_c = C` (implicit first element = 1 * prime).
2. At each step `i`, pick `val = min(next_a, next_b, next_c)`. Append to result.
3. Advance **all** pointers whose candidate equals `val` (to handle duplicates when A==B or generated values coincide):
   - If `val == next_a`: advance `pa`, compute `next_a = result[pa] * A`.
   - Same for B and C.
4. Stop after N elements.

| Metric | Value |
|--------|-------|
| Time   | O(N) |
| Space  | O(N) |

---

## Approach 3: Best — Min-Heap with Visited Set

**Intuition:** Use a min-heap (priority queue). Seed it with A, B, C. Each time we pop the minimum value `x`, we push `x*A`, `x*B`, `x*C` (if not already seen). A `visited` set prevents duplicates.

This approach is slightly slower than three-pointer (O(N log N) vs O(N)) but handles edge cases like equal primes, and the code is very clean.

**Steps:**
1. Push distinct values from {A, B, C} into the heap and visited set.
2. Pop minimum `val`, append to result.
3. Push `val*A`, `val*B`, `val*C` into heap if not in visited.
4. Repeat until result has N elements.

| Metric | Value |
|--------|-------|
| Time   | O(N log N) |
| Space  | O(N) |

---

## Real-World Use Case

**Regular number theory / cryptography:** Smooth numbers (numbers with small prime factors) are central to factoring large integers. The **General Number Field Sieve (GNFS)** — the fastest known algorithm for factoring large composites — relies on finding B-smooth numbers in a sieve. This problem is also related to **Hamming numbers** (2-3-5 smooth) used in musical tuning systems and signal processing for efficient FFT sizes. Systems that need highly composite numbers (like FFT libraries choosing transform sizes) generate smooth number sequences exactly this way.

---

## Interview Tips

- This is a direct generalization of **Ugly Number II** (LeetCode 264). If you know that problem, say so and extend the approach.
- The three-pointer approach requires advancing **all** tied pointers simultaneously — a common bug is only advancing one pointer when multiple generate the same minimum.
- Using `long`/`int64` is important: `result[i] * prime` can overflow 32-bit int for large primes and large N.
- The min-heap approach is easier to code correctly in an interview; mention the three-pointer as a follow-up optimization.
- If A == B == C, the sequence is just [A, A^2, A^3, ...] — all three approaches handle this correctly.
- Ask whether 1 should be included; InterviewBit excludes it (starts from the primes themselves), while the classic ugly numbers problem includes 1.
