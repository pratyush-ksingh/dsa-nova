# Kadane's Algorithm — Maximum Subarray Sum

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 53 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums`, find the contiguous subarray (containing at least one number) that has the largest sum, and return that sum.

A subarray is a contiguous portion of an array.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [-2,1,-3,4,-1,2,1,-5,4] | 6 | Subarray [4,-1,2,1] has the largest sum = 6 |
| [1] | 1 | Only one element |
| [5,4,-1,7,8] | 23 | Entire array |
| [-1,-2,-3,-4] | -1 | Must pick at least one element; -1 is the largest |

## Constraints

- 1 <= nums.length <= 10^5
- -10^4 <= nums[i] <= 10^4

---

## Approach 1: Brute Force

**Intuition:** Try all possible subarrays [i..j]. For a fixed left endpoint i, extend j to the right and accumulate the sum incrementally. This avoids the O(n^3) naïve approach (which would recompute sums from scratch) while still being simple.

**Steps:**
1. Initialize `max_sum = -infinity`
2. For each `i` from 0 to n-1:
   - Initialize `current_sum = 0`
   - For each `j` from i to n-1:
     - `current_sum += nums[j]`
     - `max_sum = max(max_sum, current_sum)`
3. Return `max_sum`

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

## Approach 2: Optimal — Kadane's Algorithm

**Intuition:** At each index i, we face a binary choice: should we extend the current subarray by including nums[i], or should we start fresh with just nums[i]? We take whichever gives a larger value. The key insight is that if the running sum goes negative, it can only hurt future subarrays — so we restart.

**Steps:**
1. Initialize `max_sum = nums[0]`, `current_sum = nums[0]`
2. For each `i` from 1 to n-1:
   - `current_sum = max(nums[i], current_sum + nums[i])`
     - If `current_sum + nums[i] < nums[i]`, the prefix was dragging us down — restart
   - `max_sum = max(max_sum, current_sum)`
3. Return `max_sum`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 3: Best — Kadane's with Subarray Index Tracking

**Intuition:** Same O(n) Kadane's algorithm, but also records the start and end indices of the best subarray found so far. Useful when interviewers ask "return the actual subarray" instead of just the sum.

**Steps:**
1. Initialize `max_sum = nums[0]`, `current_sum = nums[0]`, `start = end = temp_start = 0`
2. For each `i` from 1 to n-1:
   - If `nums[i] > current_sum + nums[i]`: restart — set `current_sum = nums[i]`, `temp_start = i`
   - Else: extend — `current_sum += nums[i]`
   - If `current_sum > max_sum`: update `max_sum`, `start = temp_start`, `end = i`
3. The maximum subarray is `nums[start..end]`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Stock Market Analysis:** Finding the subarray with the maximum sum is equivalent to finding the maximum profit period in a difference array of daily stock price changes. Hedge funds use variants of Kadane's algorithm to identify the most profitable holding period in historical data.

**Signal Processing:** In EEG/ECG signal analysis, identifying the segment of a signal with the highest cumulative amplitude (e.g., a spike burst) uses maximum subarray concepts.

## Interview Tips

- Always clarify: "must the subarray be non-empty?" (LeetCode 53 says yes — handle all-negative arrays by initializing with nums[0], not 0).
- The classic mistake is initializing `max_sum = 0`, which breaks for all-negative inputs.
- Kadane's is a form of dynamic programming: `dp[i] = max(nums[i], dp[i-1] + nums[i])`.
- Follow-up questions to prepare for: (1) Return the actual subarray indices. (2) Maximum circular subarray sum (LeetCode 918). (3) Maximum product subarray (LeetCode 152).
- The divide-and-conquer solution runs in O(n log n) — know it exists but prefer Kadane's in interviews.
