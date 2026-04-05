# Sum of Subarray Ranges

> **Step 09 - 9.3 Monotonic Stack/Queue** | **LeetCode 2104** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums`, return the **sum of all subarray ranges**.

The **range** of a subarray is defined as the difference between its **maximum** and **minimum** element.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [1, 2, 3]` | `4` | Subarrays: [1]→0, [2]→0, [3]→0, [1,2]→1, [2,3]→1, [1,2,3]→2. Total = 4 |
| `nums = [1, 3, 3]` | `4` | [1,3]→2, [3,3]→0, [1,3,3]→2. Total = 4 |
| `nums = [4,-2,-3,4,1]` | `59` | Sum of all (max-min) for all 15 subarrays |

## Constraints

- `1 <= nums.length <= 1000`
- `-10^9 <= nums[i] <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** For every pair (i, j) that forms a subarray, track the running minimum and maximum as j expands. Accumulate `max - min` for each subarray.

**Steps:**
1. Outer loop i from 0 to n-1 (start of subarray).
2. Initialize `cur_min = cur_max = nums[i]`.
3. Inner loop j from i to n-1: update `cur_min`, `cur_max` with `nums[j]`.
4. Add `cur_max - cur_min` to the total.
5. Return total.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1)   |

---

## Approach 2: Optimal — Monotonic Stack Contribution

**Intuition:** Instead of computing each subarray independently, decompose:

```
Sum of ranges = Sum of subarray maximums - Sum of subarray minimums
```

For **sum of subarray minimums**: for each element `nums[i]`, count how many subarrays have it as the minimum. Using Previous Less Element (PLE) and Next Less Element (NLE):

- Left boundary = distance to previous smaller element (exclusive).
- Right boundary = distance to next smaller-or-equal element (exclusive, to avoid double-counting equal elements).
- Contribution = `nums[i] * left_count * right_count`.

A monotonic stack processes this in O(n). Same logic for maximums (flip comparisons).

**Steps:**
1. Compute `sum_min` using a monotone-increasing stack with a sentinel `-inf` at the end.
2. Compute `sum_max` using a monotone-decreasing stack with a sentinel `+inf` at the end.
3. When popping index `mid`, compute `left = prev element in stack (or -1)`, `right = i`.
4. Add `nums[mid] * (mid - left) * (right - mid)` to running sum.
5. Return `sum_max - sum_min`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Approach 3: Best — Single-Pass Two Stacks

**Intuition:** The optimal approach makes two separate O(n) passes (one for min, one for max). We can combine them into a single loop, maintaining a min-stack and a max-stack simultaneously. The asymptotic complexity is the same, but the constant factor is halved.

**Steps:**
1. Maintain `min_stk` (monotone increasing) and `max_stk` (monotone decreasing).
2. At each index i, drain both stacks when their top element is no longer a candidate.
3. Accumulate `sum_min` and `sum_max` during the drain.
4. At i == n (sentinel pass), drain remaining elements with sentinels.
5. Return `sum_max - sum_min`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Real-World Use Case

**Stock market volatility analysis:** Given daily closing prices, the sum of subarray ranges is proportional to total price volatility across all possible holding windows — useful for portfolio risk metrics and options pricing models.

## Interview Tips

- The "contribution technique" is a general pattern: instead of asking "what is the answer for this subarray?", ask "how much does this element contribute across all subarrays?" This converts O(n^2) enumeration to O(n).
- `Sum of ranges = Sum of maxes - Sum of mins` is the key decomposition; state it immediately to show problem-solving clarity.
- Handling duplicates carefully (strict `<` on one side, `<=` on the other) prevents double-counting. For mins, pop on `>=` from the right; for maxes, pop on `<=` from the right.
- The same monotonic stack technique powers LeetCode 907 (Sum of Subarray Minimums) — treat this problem as a direct extension.
- Always cast to `long` in Java when multiplying indices by values (potential overflow with large inputs).
