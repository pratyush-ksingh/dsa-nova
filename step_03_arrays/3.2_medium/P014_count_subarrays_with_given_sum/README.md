# Count Subarrays with Given Sum

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 560 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums` and an integer `k`, return the **total number of contiguous subarrays** whose elements sum to `k`.

The array may contain negative numbers, so a sliding window alone is insufficient.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| nums=[1,1,1], k=2 | 2 | Subarrays [1,1] (indices 0-1) and [1,1] (indices 1-2) |
| nums=[1,2,3], k=3 | 2 | Subarrays [3] (index 2) and [1,2] (indices 0-1) |
| nums=[-1,-1,1], k=0 | 1 | Subarray [-1,1] (indices 1-2) sums to 0 |

## Constraints

- 1 <= nums.length <= 2 * 10^4
- -1000 <= nums[i] <= 1000
- -10^7 <= k <= 10^7

---

## Approach 1: Brute Force

**Intuition:** Enumerate all subarrays by fixing the left endpoint i and extending the right endpoint j. Compute each subarray sum incrementally (not from scratch each time — that would be O(n^3)). Count subarrays where the sum equals k.

**Steps:**
1. Initialize `count = 0`
2. For each `i` from 0 to n-1:
   - Initialize `current_sum = 0`
   - For each `j` from i to n-1:
     - `current_sum += nums[j]`
     - If `current_sum == k`, increment `count`
3. Return `count`

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

## Approach 2: Optimal — Prefix Sum + HashMap

**Intuition:** Define `prefix_sum[j]` as the sum of `nums[0..j]`. A subarray `nums[i+1..j]` sums to k if and only if `prefix_sum[j] - prefix_sum[i] == k`, which means `prefix_sum[i] == prefix_sum[j] - k`. So as we scan from left to right, for each new prefix sum we just need to look up how many *previous* prefix sums equal `current_prefix - k`. A HashMap stores the frequency of each prefix sum seen so far.

**Key initialization:** Put `{0: 1}` in the map before the loop to handle subarrays that start from index 0 (where there's no actual preceding prefix sum, but conceptually `prefix_sum[-1] = 0`).

**Steps:**
1. Initialize `freq = {0: 1}`, `prefix_sum = 0`, `count = 0`
2. For each `num` in `nums`:
   - `prefix_sum += num`
   - `count += freq.get(prefix_sum - k, 0)`  — how many times the needed complement appeared
   - `freq[prefix_sum] += 1`
3. Return `count`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Approach 3: Best — Prefix Sum + HashMap (Same, Reinforced)

**Intuition:** The O(n) prefix-sum + HashMap approach IS the optimal solution for this problem as stated (with potential negatives). For the special case where all elements are non-negative, a two-pointer sliding window would give O(n) time and O(1) space — but it fails when negatives are present. This approach uses a slightly different variable naming (`running_sum`, `needed`) to make the complementary pattern explicit.

**Steps:**
1. Initialize `prefix_counts = {0: 1}`, `running_sum = 0`, `result = 0`
2. For each `x` in `nums`:
   - `running_sum += x`
   - `needed = running_sum - k`
   - `result += prefix_counts.get(needed, 0)`
   - `prefix_counts[running_sum] = prefix_counts.get(running_sum, 0) + 1`
3. Return `result`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Real-World Use Case

**Financial Transaction Monitoring:** Given a stream of transaction amounts (positive = deposits, negative = withdrawals), count how many contiguous windows of transactions sum to exactly a target amount (e.g., a refund equal to a prior purchase). Fraud detection systems use this to find suspicious matching sequences.

**Network Packet Analysis:** Count segments of a packet byte stream that sum to a specific checksum value — the same mathematical structure.

## Interview Tips

- The key insight to articulate: "A subarray [i+1..j] sums to k iff prefix[j] - prefix[i] == k. So for each j, I need count of previous prefix sums equal to prefix[j] - k."
- Always initialize the HashMap with `{0: 1}` before the loop. Forgetting this is the most common bug — it misses subarrays that start at index 0.
- If the interviewer says "all elements are non-negative," switch to the two-pointer sliding window for O(1) space. Mention this trade-off proactively.
- This pattern (prefix sum complement lookup) appears everywhere: number of subarrays with XOR = k, number of subarrays with product < k (log transform), longest subarray with sum k.
- The same technique extends to 2D: count submatrices with sum k using row-compressed prefix sums.
