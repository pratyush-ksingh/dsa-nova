# Sorted Permutation Rank

> **Step 01 | 1.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given a string `A`, find the **rank** of the string when all its permutations are sorted in lexicographic order. Since the answer can be very large, return it modulo `10^6 + 3`.

The string may contain **duplicate characters**. Duplicate permutations are counted only once.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"ABC"` | `1` | Sorted permutations: ABC, ACB, BAC, BCA, CAB, CBA. ABC is 1st. |
| `"ACB"` | `2` | ACB is the 2nd permutation in sorted order. |
| `"BAC"` | `3` | BAC is the 3rd permutation. |
| `"CBA"` | `6` | CBA is the 6th (last) permutation. |
| `"BACA"` | `5` | With duplicates, only unique permutations counted. |

## Constraints

- `1 <= len(A) <= 500`
- `A` contains only uppercase letters `A-Z`
- Answer modulo `10^6 + 3` (which is a prime, enabling modular inverse)

---

## Approach 1: Brute Force

**Intuition:** Generate all unique permutations of the string, sort them lexicographically, then find the position (1-indexed) of the input string `A`. Feasible only for very small strings (n <= 8).

**Steps:**
1. Generate all permutations of `A`.
2. Deduplicate and sort them.
3. Find the 1-based index of `A` in the sorted list.
4. Return `index % MOD`.

| Metric | Value |
|--------|-------|
| Time   | O(n! * n) |
| Space  | O(n!) |

---

## Approach 2: Optimal — Count Smaller Characters Per Position

**Intuition:** For each position `i`, count how many permutations come before `A` due to a smaller character being placed at position `i` while positions `0..i-1` match `A`. This count is `(# remaining chars < A[i]) * (n-i-1)! / (product of factorials of remaining char frequencies)`. The denominator accounts for duplicates. Sum over all positions and add 1.

**Steps:**
1. Precompute `fact[0..n]` modulo `10^6 + 3`.
2. Track remaining character frequencies in an array of size 26.
3. For each position `i`:
   a. Count `smaller` = sum of `freq[x]` for `x < A[i]`.
   b. Compute `suffix_fact = (n-i-1)!`.
   c. Compute `denom = product of fact[freq[c]]` for all chars.
   d. `rank += smaller * suffix_fact * modular_inverse(denom) % MOD`.
   e. Decrement `freq[A[i]]`.
4. Return `rank + 1`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) |

---

## Approach 3: Best — Fenwick Tree for O(n log n)

**Intuition:** Identical formula to Approach 2, but use a Binary Indexed Tree (BIT/Fenwick Tree) over the 26-character alphabet to compute "count of remaining characters less than `A[i]`" in `O(log 26)` instead of `O(26)`. For the alphabet size of 26 this is a constant improvement, but the technique scales to large alphabets.

**Steps:**
1. Initialize a BIT of size 26 with the frequency of each character.
2. For each position `i`:
   a. Query BIT for prefix sum up to `A[i]-1` to get `smaller`.
   b. Compute rank contribution as in Approach 2.
   c. Update BIT: remove `A[i]` with `update(A[i], -1)`.
3. Return accumulated rank.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(26) = O(1) |

---

## Real-World Use Case

**Combinatorial numbering systems:** Ranking and unranking permutations is the foundation of the **Lehmer code** used in combinatorics. Practical applications include: encoding board game states (e.g., 15-puzzle, Rubik's cube) as compact integers for storage in lookup tables, generating the k-th permutation directly without enumeration, and building compressed representations of ordered data in databases.

## Interview Tips

- The formula involves **factorial with duplicates** (like counting arrangements of repeated letters): `n! / (n1! * n2! * ... * nk!)`.
- Since `10^6 + 3` is **prime**, modular inverse can be computed via Fermat's little theorem: `a^(MOD-2) % MOD`.
- A common mistake is not handling duplicate characters — if you forget the denominator, you'll overcount ranks where identical characters could be swapped.
- Another pitfall: remember to decrement the frequency of the current character after processing each position.
- The BIT optimization matters when the alphabet is large (e.g., Unicode characters). For 26 letters, both O(26) and O(log 26) are effectively constant.
- This is fundamentally the **Factoradic number system** — a mixed-radix positional numbering system for permutations.
