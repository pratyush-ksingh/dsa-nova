# Number of Longest Increasing Subsequences (LeetCode 673)

> **Step 16.7** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `nums`, return the **number of longest increasing subsequences** (strictly increasing). If there are multiple LIS of the same maximum length, count all of them.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [1,3,5,4,7] | 2 | LIS length is 4. Two LIS: [1,3,5,7] and [1,3,4,7] |
| [2,2,2,2,2] | 5 | LIS length is 1. Five LIS: each individual element |
| [1,2,4,3,5,4,7,2] | 3 | LIS length is 5. Three paths to length 5 |
| [5] | 1 | Single element, trivially 1 LIS |

### Constraints
- `1 <= nums.length <= 2000`
- `-10^6 <= nums[i] <= 10^6`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Generate All Subsequences

**Intuition:** Use backtracking to generate all strictly increasing subsequences. Track the maximum length found and count how many achieve it.

**Steps:**
1. `backtrack(idx, last, length)`: extend current subsequence
2. At each step, update `max_len` and `count` accordingly
3. Try appending any `nums[i] > last` for i from idx to n-1
4. Return final `count`

| Metric | Value |
|--------|-------|
| Time   | O(2^n * n) |
| Space  | O(n) |

---

### Approach 2: Optimal -- DP with length[] and count[] Arrays

**Intuition:** For each index i, maintain two values:
- `dp_len[i]` = length of LIS ending at index i
- `dp_cnt[i]` = number of distinct LIS of that length ending at index i

When extending from j to i (where `nums[j] < nums[i]`):
- If `dp_len[j] + 1 > dp_len[i]`: found a longer LIS ending at i — reset count to `dp_cnt[j]`
- If `dp_len[j] + 1 == dp_len[i]`: another way to achieve same length — add `dp_cnt[j]` to count

**Steps:**
1. Initialize `dp_len[i] = 1`, `dp_cnt[i] = 1` for all i
2. For i from 1 to n-1:
   - For j from 0 to i-1:
     - If `nums[j] < nums[i]`:
       - If `dp_len[j] + 1 > dp_len[i]`: `dp_len[i] = dp_len[j]+1`, `dp_cnt[i] = dp_cnt[j]`
       - Elif `dp_len[j] + 1 == dp_len[i]`: `dp_cnt[i] += dp_cnt[j]`
3. `max_len = max(dp_len)`
4. Return `sum(dp_cnt[i] for all i where dp_len[i] == max_len)`

```
Dry-run: nums=[1,3,5,4,7]
         idx:  0  1  2  3  4

After processing all i:
  dp_len: [1, 2, 3, 3, 4]
  dp_cnt: [1, 1, 1, 1, 2]

Explanation for i=4 (nums[4]=7):
  j=0: 1<7, dp_len[0]+1=2 > dp_len[4]=1 → dp_len[4]=2, dp_cnt[4]=1
  j=1: 3<7, dp_len[1]+1=3 > 2 → dp_len[4]=3, dp_cnt[4]=1
  j=2: 5<7, dp_len[2]+1=4 > 3 → dp_len[4]=4, dp_cnt[4]=1
  j=3: 4<7, dp_len[3]+1=4 == 4 → dp_cnt[4]+=dp_cnt[3]=1 → dp_cnt[4]=2

max_len=4, count = dp_cnt[4] = 2 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) |

---

### Approach 3: Best -- Same DP (O(n log n) via Segment Tree Exists but is Overkill)

**Intuition:** Same as Approach 2. An O(n log n) solution exists using a segment tree or BIT on compressed coordinates (maintaining max-length and total-count in each node), but this is rarely expected in interviews. O(n^2) passes the LeetCode constraints (n ≤ 2000).

**Steps:** Identical to Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) -- O(n log n) with segment tree |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| All equal elements | LIS length = 1, count = n | Each element is its own LIS |
| Strictly decreasing | LIS length = 1, count = n | Same as all equal |
| Single element | Return 1 | Trivial base case |
| All distinct increasing | One LIS of length n, count = 1 | dp_cnt[n-1] = 1 |

**Common Mistakes:**
- Only tracking `dp_len` without `dp_cnt` (finds length but not count)
- Using `>=` instead of `>` for strict increase
- Forgetting to reset `dp_cnt[i]` when a strictly longer path is found (should not add, should replace)
- Off-by-one in the count update condition

---

## Real-World Use Case
**Version control branching:** In a git-like system, commits form a partial order. The number of longest chains of commits (LIS in terms of timestamps/version numbers) tells you how many independent development paths reached the latest milestone — useful in analyzing project histories and merge complexity.

## Interview Tips
- The key insight: maintain BOTH `len[]` and `count[]` simultaneously — most candidates only think of `len[]`
- Walk through the update logic carefully: "found longer → reset count; found equal → add count"
- For follow-up on O(n log n): mention segment tree approach exists but is complex; O(n^2) is perfectly fine for n ≤ 2000
- Related problem: Number of Longest Common Subsequences — same dual-array technique
- This is a natural progression from the standard LIS problem; frame it as "LIS + bookkeeping"
