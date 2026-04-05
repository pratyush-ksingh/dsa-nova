# Highest Product

> **Step 12 - 12.2 Medium/Hard Greedy** | **InterviewBit** | **Difficulty:** EASY | **XP:** 10 | **Status:** UNSOLVED

## Problem Statement

Given an array of integers (which may include negatives and zeros), find the **maximum product of any 3 numbers** from the array.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3]` | `6` | 1 * 2 * 3 = 6 |
| `[-10, -3, 5, 6, -2]` | `180` | (-10) * (-3) * 6 = 180, beats 3 * 5 * 6 = 90 |
| `[0, -1, 3, 100, 50]` | `15000` | 100 * 50 * 3 = 15000 |
| `[-1, -2, -3]` | `-6` | Only choice: (-1) * (-2) * (-3) = -6 |

## Constraints

- `3 <= nums.length <= 10^4`
- `-10^3 <= nums[i] <= 10^3`

---

## Approach 1: Brute Force — All Triplets

**Intuition:** Try every combination of 3 distinct indices. Return the maximum product found.

**Steps:**
1. Three nested loops over indices i < j < k.
2. Compute `nums[i] * nums[j] * nums[k]`.
3. Track and return the maximum.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(1)   |

---

## Approach 2: Optimal — Sort, Two Candidates

**Intuition:** After sorting, the maximum product of 3 numbers can only come from two configurations:
1. **Three largest:** `nums[-1] * nums[-2] * nums[-3]`
2. **Two smallest (most negative) + largest:** `nums[0] * nums[1] * nums[-1]`

No other combination can beat these two (proof by contradiction: picking any other trio gives a product no larger than these candidates).

**Steps:**
1. Sort the array in ascending order.
2. Compute `cand1 = nums[-1] * nums[-2] * nums[-3]`.
3. Compute `cand2 = nums[0] * nums[1] * nums[-1]`.
4. Return `max(cand1, cand2)`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) |

---

## Approach 3: Best — Single Pass O(n)

**Intuition:** We don't need to sort the full array. We only need the 3 largest values and the 2 smallest values. Track these 5 quantities in one linear scan.

**Steps:**
1. Initialize `max1 = max2 = max3 = -inf` and `min1 = min2 = +inf`.
2. For each `num`: update `max1, max2, max3` (maintaining descending order) and `min1, min2` (ascending order).
3. Return `max(max1 * max2 * max3, min1 * min2 * max1)`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Financial risk modelling:** Given a portfolio of daily returns (positive and negative), find three assets whose combined return (product as a growth multiplier) is maximised. Two losing assets can multiply to a gain, combining with one strong gainer — exactly the two-negatives scenario.

## Interview Tips

- The critical insight that trips people up: **two negatives * one positive can beat three positives**. Always mention this first.
- The sort solution has only two candidates because multiplication is monotone: you want either the three largest or the two most-extreme negatives paired with the largest positive.
- The single-pass solution is O(n) vs O(n log n) — worth mentioning as the optimal. The implementation requires careful update order (update max3 before max2 before max1).
- Watch for integer overflow in Java: use `long` arithmetic when multiplying values up to 10^9.
- This extends to k numbers: sort gives O(n log n), linear selection of top-k and bottom-k gives O(n). The number of candidates grows with k but remains constant for fixed k.
