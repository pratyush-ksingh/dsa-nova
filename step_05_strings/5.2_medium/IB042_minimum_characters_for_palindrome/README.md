# Minimum Characters to Add for Palindrome

> **Step 05 | 5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit

## Problem Statement

Given a string `A`, find the **minimum number of characters** that need to be **prepended** (added to the front) to make `A` a palindrome.

**Key insight:** The answer equals `n - (length of longest palindromic prefix of A)`. You only need to prepend the reverse of the suffix that is NOT part of the palindromic prefix.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"ABC"` | `2` | Prepend "CB" → "CBABC" |
| `"abcd"` | `3` | Prepend "dcb" → "dcbabcd" |
| `"aba"` | `0` | Already a palindrome |
| `"aab"` | `1` | Prepend "b" → "baab" |
| `"ab"` | `1` | Prepend "b" → "bab" |

## Constraints

- `1 <= |A| <= 10^6`
- `A` consists of lowercase English letters

---

## Approach 1: Brute Force (Check Each Prefix)

**Intuition:** For every prefix length `k` from `n` down to `1`, check if `A[0..k-1]` is a palindrome. The first (longest) palindromic prefix tells us we need to prepend `n - k` characters.

**Steps:**
1. For `k = n` down to `1`:
   - Check if `A[0..k-1]` is a palindrome using two pointers.
   - If yes, return `n - k`.
2. Return `n` (worst case: no palindromic prefix other than a single character — actually we'd return n-1 for single char).

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

---

## Approach 2: Optimal (KMP Failure Function on `s + "#" + reverse(s)`)

**Intuition:** We want the longest palindromic prefix of `s`. A palindromic prefix reads the same forwards and backwards — meaning it is both a prefix of `s` AND a prefix of `reverse(s)` (which makes it a suffix of `reverse(s)`).

Construct `combined = s + "#" + reverse(s)`. The KMP LPS value at the last position of `combined` gives the length of the longest proper prefix of `s` that matches a suffix of `reverse(s)` — which is exactly the longest palindromic prefix.

The `#` separator prevents the LPS from inadvertently crossing the `s` / `reverse(s)` boundary.

**Steps:**
1. Compute `combined = s + "#" + reverse(s)`.
2. Build the KMP LPS (failure function) array for `combined`.
3. Return `n - lps[last]`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Approach 3: Best (Same KMP — Cleaner Loop)

**Intuition:** Identical to Approach 2. Written with a single loop KMP builder (the "while-based" variant) instead of the two-pointer variant. The separator `#` must be a character not present in the alphabet of `s` to guarantee correctness.

**Steps:**
1. Build `combined = s + "#" + reverse(s)`.
2. Compute LPS with `j` pointer (standard while-loop form).
3. Return `n - lps[combined.length - 1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Real-World Use Case

**Autocomplete and string repair in bioinformatics:** In DNA sequence analysis, researchers need to find the minimal edit to make a sequence "self-complementary" (a palindrome in the biological sense). The KMP combined-string technique applies directly. Similarly, in data compression and error-correction coding, detecting the maximal palindromic structure efficiently is a key subroutine.

## Interview Tips

- This problem is less about knowing the answer and more about **reducing it** to a known problem (KMP LPS). The reduction step is what interviewers want to see.
- The key reduction: "longest palindromic prefix" = "longest prefix of `s` that equals its own reverse" = "longest string that is both a prefix of `s` and a suffix of `reverse(s)`" = KMP LPS at end of `s + "#" + rev(s)`.
- Always explain **why the `#` separator is needed**: without it, the LPS could match across the s/rev(s) boundary and give an inflated result.
- Dry-run with `"aab"`: combined = `"aab#baa"`, lps = `[0,1,0,0,0,1,2]`, lps[-1] = 2, answer = 3-2 = 1. Verify: prepend "b" → "baab". Correct.
- Follow-up: "What if you can append instead of prepend?" — same answer by symmetry (run on `reverse(s) + "#" + s`).
- Follow-up: "What if both prepend and append are allowed?" — then the answer is `n - (length of longest palindromic subsequence that is also a prefix/suffix)`, which is more complex.
