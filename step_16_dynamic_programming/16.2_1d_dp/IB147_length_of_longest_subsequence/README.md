# Length of Longest Subsequence

> **Step 16 | 16.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an array of integers `A`, find the **length of the longest subsequence** such that every pair of adjacent elements in the subsequence **differs by exactly 1** (each next element is exactly 1 more than the previous).

A subsequence is formed by deleting some (possibly zero) elements from the array **without changing the relative order** of the remaining elements.

**Input:** Array of integers `A`.
**Output:** Integer — length of the longest such subsequence.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [3,10,3,4,2,1,5] | 3 | Subsequence [3,4,5] at positions 2,3,6 |
| [1,2,3,4,5] | 5 | The entire array qualifies |
| [5,4,3] | 1 | No two elements have a +1 difference in the forward direction |
| [1,9,3,0,18,2] | 2 | Subsequence [1,2] at positions 0,5 (1 then 2 in array order) |

## Constraints

- 1 <= |A| <= 10^5
- -10^9 <= A[i] <= 10^9

---

## Approach 1: Brute Force

**Intuition:** Generate every possible subsequence and check whether consecutive elements differ by exactly +1. Track the maximum valid length.

**Steps:**
1. Use recursion to enumerate all 2^n subsequences.
2. For each subsequence, verify all consecutive pairs satisfy `A[i] - A[i-1] == 1`.
3. Update global maximum length when a valid subsequence is found.

| Metric | Value |
|--------|-------|
| Time   | O(2^n * n) |
| Space  | O(n) |

---

## Approach 2: Optimal — HashMap DP

**Intuition:** For each element `x`, the longest subsequence ending at `x` extends any chain ending at `x-1`. A hash map `dp` stores `dp[v]` = length of the longest valid subsequence ending at value `v`.

For each element `x` processed left to right: `dp[x] = dp.get(x-1, 0) + 1`. The array order guarantees the subsequence ordering is respected.

**Steps:**
1. Initialize empty hash map `dp` and `max_len = 1`.
2. For each value `x` in the array:
   - Compute `len = dp.get(x - 1, 0) + 1`.
   - Set `dp[x] = len`.
   - Update `max_len` if `len > max_len`.
3. Return `max_len`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Approach 3: Best — HashMap DP with Max-Merge

**Intuition:** Same O(n) idea, but explicitly handles duplicate values by keeping the **maximum** chain length for each endpoint value. If `x` appears multiple times, each occurrence may extend a different prior chain ending at `x-1`. Using `chain[x] = max(chain[x], prev_len + 1)` ensures correctness.

**Steps:**
1. Initialize `chain = {}`, `ans = 1`.
2. For each `val`:
   - `new_len = chain.get(val - 1, 0) + 1`
   - `chain[val] = max(chain.get(val, 0), new_len)`
   - `ans = max(ans, chain[val])`
3. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Real-World Use Case

**Version control and diff tools:** The classic Longest Increasing Subsequence (LIS) algorithm powers `git diff` and similar diff utilities. This problem is a constrained LIS variant (step must be exactly +1), analogous to finding the longest arithmetic progression with common difference 1. Such algorithms are used in bioinformatics for DNA sequence alignment, in text editors for change tracking, and in financial data analysis for detecting consecutive trend steps.

---

## Interview Tips

- This is explicitly an LIS variant — mentioning that connection shows algorithmic breadth.
- The HashMap approach reduces LIS from O(n log n) to O(n) because the "exactly +1" constraint eliminates the need for binary search; we only ever look up `dp[x-1]`.
- Do not confuse with **Longest Consecutive Sequence** (LeetCode 128): that problem does not care about array order, while this one is a true subsequence problem.
- Negative numbers are handled naturally since HashMap keys can be any integer.
- If the interviewer asks for the actual subsequence path, backtrack through the `chain` map from the maximum value down, decrementing by 1 each step.
- If "differ by exactly 1" is changed to "differ by at most 1", you also need `dp[x]` itself in the lookup, making it `dp[x] = max(dp[x-1], dp[x], dp[x+1]) + 1` — requires two passes or careful ordering.
