# Burst Balloons

> **Step 16.8** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 312

## Problem Statement

You are given `n` balloons indexed from `0` to `n-1`. Each balloon is painted with a number on it represented by the array `nums`. You are asked to burst all the balloons.

If you burst balloon `i`, you get `nums[i-1] * nums[i] * nums[i+1]` coins. If `i-1` or `i+1` is out of bounds, treat the missing neighbor as having value `1`.

Return the **maximum** coins you can collect by bursting all the balloons wisely.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [3,1,5,8]` | `167` | Burst order 1→5→3→8: 3*1*5 + 3*5*8 + 1*3*8 + 1*8*1 = 15+120+24+8 = 167 |
| `nums = [1,5]` | `10` | Burst 1 first: 1*1*5 = 5; then burst 5: 1*5*1 = 5; total = 10 |
| `nums = [1]` | `1` | Only one balloon: 1*1*1 = 1 |

## Constraints

- `n == nums.length`
- `1 <= n <= 300`
- `0 <= nums[i] <= 100`

---

## Approach 1: Brute Force (Try all burst orderings)

**Intuition:** There are `n!` orderings of how we can burst the balloons. For each ordering, simulate the process: maintain the currently active list of balloons, compute coins for each burst (using current neighbors), and accumulate the total. Track the maximum total.

**Steps:**
1. Use a recursive function that tries bursting each remaining balloon next.
2. At each step, the coins gained = left_neighbor * current * right_neighbor.
3. Remove the balloon and recurse on the remaining list.
4. Backtrack (restore the balloon) after recursing.
5. Track global maximum.

| Metric | Value |
|--------|-------|
| Time   | O(n! * n) — factorial, only feasible for n <= 10 |
| Space  | O(n) — recursion stack |

---

## Approach 2: Optimal (Top-Down Interval DP / Memoization)

**Intuition:** The key insight that makes this tractable is a **reversal of perspective**: instead of asking "which balloon do I burst first?" (hard, because it changes neighbors), ask "which balloon do I burst LAST in interval (i, j)?".

Pad `nums` with virtual `1`s at both ends: `newNums = [1] + nums + [1]`.

Define `dp(i, j)` = maximum coins from bursting all balloons **strictly between** indices `i` and `j`. When balloon `k` is the last to be burst in `(i, j)`, balloons `i` and `j` are still present (they are the boundaries, not yet burst at the time we consider this interval), so the coins from bursting `k` last = `newNums[i] * newNums[k] * newNums[j]`. Sub-problems on each side are independent.

`dp(i, j) = max over k in (i+1..j-1): newNums[i]*newNums[k]*newNums[j] + dp(i,k) + dp(k,j)`

**Steps:**
1. Build `newNums = [1] + nums + [1]`, length `m = n+2`.
2. Memoize `dp(i, j)` in a dictionary.
3. Base case: `j <= i+1` (no balloons strictly between them) → return 0.
4. Try each `k` as the last balloon burst; take maximum.
5. Return `dp(0, m-1)` for the full range.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) — O(n^2) states, O(n) per state |
| Space  | O(n^2) memo + O(n) call stack |

---

## Approach 3: Best (Bottom-Up Interval DP)

**Intuition:** Same recurrence as Approach 2, but built iteratively by increasing gap `g = j - i`. Start with gap 2 (no balloons between neighbors) — all these are 0. Then gap 3, 4, etc. Each sub-problem is guaranteed to be computed before it is needed. Eliminates recursion overhead and stack-overflow risk for large inputs.

**Steps:**
1. Build `newNums` as before.
2. Allocate `dp[m][m]`, initialized to 0.
3. Outer loop: `gap` from 2 to `m-1`.
4. Middle loop: `i` from 0 while `i + gap < m`; `j = i + gap`.
5. Inner loop: try each last-burst `k` in `(i+1..j-1)`, update `dp[i][j]` with max.
6. Return `dp[0][m-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(n^2) — no stack |

---

## Real-World Use Case

**Optimal job/task sequencing with interaction effects:** In manufacturing, shutting down machines in a certain order can yield different scrap values depending on what's running alongside. In financial derivatives, "unwinding" a portfolio of options one position at a time has a total PnL that depends on the order because prices are correlated. The "burst last" DP framework applies whenever combining items yields a reward that depends on the surviving neighbors at combination time, not the original neighbors.

---

## Interview Tips

- The hardest part of this problem is the **insight**: think about which balloon is burst LAST in any sub-interval, not first. This converts a problem with moving boundaries into a clean interval DP with fixed boundaries.
- Padding with virtual `1`s at both ends is essential to avoid messy boundary checks.
- The template is identical to Matrix Chain Multiplication (which split is processed last for the outer multiply?) — if you know one, you know the other.
- `dp[i][j]` covers balloons strictly **between** `i` and `j`, not including `i` and `j` themselves. This is what makes `i` and `j` available as stable neighbors when the last balloon `k` is burst.
- For `n <= 300`, O(n^3) = 27 million operations — well within the 1-second limit.
- Common mistake: treating `dp[i][j]` as "including" `i` and `j` — this breaks the recurrence because then `i` and `j`'s neighbor costs are double-counted.
