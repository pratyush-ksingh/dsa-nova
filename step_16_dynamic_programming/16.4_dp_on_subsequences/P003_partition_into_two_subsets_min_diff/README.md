# Partition into Two Subsets — Minimum Difference

> **Step 16.4** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

Given an array of non-negative integers `nums`, partition it into **two subsets** (every element goes to exactly one subset) such that the **absolute difference** between the sums of the two subsets is **minimized**. Return that minimum difference.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 6, 11, 5]` | `1` | S1={1,5,6}=12, S2={11}=11, diff=1 |
| `[3, 9, 7, 3]` | `2` | S1={3,7}=10, S2={9,3}=12, diff=2 |
| `[1, 2, 3, 4]` | `0` | S1={1,4}=5, S2={2,3}=5, diff=0 |
| `[10]` | `10` | S1={10}=10, S2={}=0, diff=10 |

## Constraints

- `1 <= nums.length <= 200`
- `0 <= nums[i] <= 100`
- `totalSum <= 20000`

---

## Approach 1: Brute Force — Enumerate All Partitions

**Intuition:** Every element can independently go into S1 or S2, giving 2^n possibilities. Explore the full binary decision tree, compute the difference at each leaf, and return the minimum.

**Steps:**
1. Compute `total = sum(nums)`.
2. Define `recurse(index, s1)`: at each step either add `nums[index]` to S1 or skip it (it goes to S2).
3. At the base case (`index == n`), compute `s2 = total - s1` and update `minDiff = min(minDiff, |s1 - s2|)`.
4. Return `minDiff`.

| Metric | Value |
|--------|-------|
| Time   | O(2^n) |
| Space  | O(n) — recursion depth |

---

## Approach 2: Optimal — 2D Boolean DP

**Intuition:** We only care about **which sums are achievable** for one of the two subsets. This is the classic 0/1 knapsack subset-sum problem. Build a DP table where `dp[i][j]` = "can we form sum `j` using elements `nums[0..i-1]`?". Once the table is filled, scan `dp[n][0..total/2]` and find the largest achievable `s1`; the answer is `total - 2*s1`.

**Steps:**
1. Initialize `dp[i][0] = True` for all `i` (sum 0 always achievable by picking nothing).
2. For each element `nums[i-1]` and each sum `j`:
   - `dp[i][j] = dp[i-1][j]` (skip element)
   - If `j >= nums[i-1]`: `dp[i][j] |= dp[i-1][j - nums[i-1]]` (take element)
3. Scan `s1` from `total//2` down to `0`; the first `True` cell gives the best balanced split.
4. Return `total - 2 * s1`.

| Metric | Value |
|--------|-------|
| Time   | O(n * totalSum) |
| Space  | O(n * totalSum) |

---

## Approach 3: Best — 1D Rolling DP (Space-Optimised)

**Intuition:** The 2D table can be compressed to a single 1D array because row `i` only ever reads from row `i-1`. Iterate `j` in **reverse** order so we never overwrite values we still need from the previous row. This halves memory from O(n*S) to O(S).

Alternatively, represent the DP as a **Python integer used as a bitset**: bit `j` is set iff sum `j` is reachable. Updating becomes a single `dp |= (dp << x)` per element — leveraging hardware integer arithmetic for an effective 64x speedup.

**Steps:**
1. Start with `dp = 1` (only bit 0 set, meaning sum 0 is reachable).
2. For each number `x`: `dp |= (dp << x)`.
3. Scan from `total//2` downward; the first set bit `s1` gives the answer `total - 2*s1`.

| Metric | Value |
|--------|-------|
| Time   | O(n * totalSum) — identical asymptotically, but ~64x faster in practice |
| Space  | O(totalSum / 64) words for the bitset; O(totalSum) for the boolean array variant |

---

## Real-World Use Case

**Load balancing across two servers:** Given a list of job execution times, split them between two machines so that finish times are as equal as possible — minimising the makespan. This is equivalent to minimising the sum difference of two subsets and is the core of many scheduling algorithms.

Other applications:
- Fair division of assets in a divorce settlement or estate partition.
- Splitting a dataset into two groups with equal total weight for A/B testing.
- Balancing two production lines so neither is a bottleneck.

## Interview Tips

- The key insight is that you only need to track **achievable sums for one subset** up to `total/2` — the other subset's sum is determined.
- Always derive the transformation: minimise `|S1 - S2|` = minimise `|total - 2*S1|` → maximise `S1 <= total/2`.
- The 1D DP must iterate `j` in **decreasing** order to prevent an element from being used more than once (0/1 knapsack, not unbounded).
- When using the bitset trick in Python, mention that `int` is arbitrary precision — the bit shift `dp << x` efficiently propagates all reachable sums at once.
- Distinguish this from "Partition Equal Subset Sum" (P002): that problem asks if diff = 0 is possible; this asks for the minimum possible diff.
- Edge cases: single element (answer = that element), all zeros (answer = 0), very large sums (use 1D DP to avoid MLE).
