# Longest Increasing Subsequence

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement (LeetCode #300)
Given an integer array `nums`, return the **length of the longest strictly increasing subsequence**.

A **subsequence** is a sequence derived from the array by deleting some or no elements **without changing the order** of the remaining elements.

### Examples

| # | Input | Output | Explanation |
|---|-------|--------|-------------|
| 1 | `[10, 9, 2, 5, 3, 7, 101, 18]` | `4` | LIS is `[2, 3, 7, 18]` (or `[2, 3, 7, 101]`) |
| 2 | `[0, 1, 0, 3, 2, 3]` | `4` | LIS is `[0, 1, 2, 3]` |
| 3 | `[7, 7, 7, 7, 7]` | `1` | All elements equal -- strictly increasing means length 1 |

### Constraints
- `1 <= nums.length <= 2500`
- `-10^4 <= nums[i] <= 10^4`

### Real-Life Analogy
Imagine you're stacking boxes on a shelf, and each box must be **taller** than the one before it. You have a line of boxes arriving in random height order. You can **skip** any box but can't rearrange them. What's the tallest tower you can build? That's LIS -- finding the longest chain of strictly increasing values while preserving original order.

### 3 Key Observations

1. **"Aha!" -- Optimal substructure:** The LIS ending at index `i` depends only on the LIS ending at earlier indices `j < i` where `nums[j] < nums[i]`. Each position's answer builds on previous answers.

2. **"Aha!" -- Overlapping subproblems:** When we try all subsequences recursively, we recompute "what's the LIS ending here?" many times. This screams DP.

3. **"Aha!" -- Patience sorting insight:** We don't actually need to track every LIS. We can maintain a list of the **smallest tail element** for increasing subsequences of each length. Binary search on this list gives us O(n log n).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Recursion | Call stack | Try include/exclude at each index | Explores all subsequences |
| Memoization | 1D array `dp[i]` | Top-down recursion + cache | Eliminates recomputation |
| Tabulation | 1D array `dp[i]` | Bottom-up nested loops | No recursion overhead |
| Space-Optimized (Binary Search) | `tails[]` array | Patience sorting + binary search | O(n log n) -- can't do better |

**Pattern cue:** "Longest subsequence with ordering constraint" --> LIS pattern. Whenever you see "longest/shortest subsequence where elements satisfy some ordering," think LIS.

---

## APPROACH LADDER

### Approach 1: Recursion (Brute Force)

**State definition:** `solve(idx, prev_idx)` = length of LIS starting from index `idx`, where `prev_idx` is the index of the last element we picked (-1 if none picked yet).

**Recurrence:**
```
solve(idx, prev_idx) =
    if idx == n: return 0

    not_take = solve(idx + 1, prev_idx)                          // skip current
    take = 0
    if prev_idx == -1 OR nums[idx] > nums[prev_idx]:
        take = 1 + solve(idx + 1, idx)                           // include current

    return max(take, not_take)
```

**Why it works:** At every index, we have two choices -- take or skip. If we take, the current element becomes the new "previous." We try all 2^n combinations.

**Dry-run trace** for `[10, 9, 2, 5, 3, 7, 101, 18]`:
```
solve(0, -1)
  skip 10: solve(1, -1)
    skip 9: solve(2, -1)
      take 2: solve(3, 2)       // prev=2
        take 5: solve(4, 3)     // prev=5
          skip 3: solve(5, 3)
            take 7: solve(6, 5) // prev=7
              take 101: ... = 1
              take 18:  ... = 1
              best = 1
            = 1 + 1 = 2
          = 2
        = 1 + 2 = 3
      = 1 + 3 = 4  <-- this path gives LIS = 4: [2, 5, 7, 101] or [2, 5, 7, 18]
    ... (many more branches)
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(2^n) | Each element: take or skip = 2 choices |
| Space | O(n) | Recursion depth |

---

### Approach 2: Memoization (Top-Down DP)

**Key insight:** The recursive function `solve(idx, prev_idx)` has only `n * (n+1)` unique states (idx in 0..n-1, prev_idx in -1..n-1). We cache them.

**State:** `dp[idx][prev_idx + 1]` = LIS length starting from `idx` with `prev_idx` as last picked.

**Recurrence:** Same as recursion, but check cache before computing.

**Coordinate shift:** Since `prev_idx` can be -1, we store it as `prev_idx + 1` (0-indexed in the cache).

```
memo[idx][prev_idx + 1]:
    if already computed, return cached value
    not_take = solve(idx + 1, prev_idx)
    take = 0
    if prev_idx == -1 OR nums[idx] > nums[prev_idx]:
        take = 1 + solve(idx + 1, idx)
    memo[idx][prev_idx + 1] = max(take, not_take)
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n^2) | n * (n+1) states, O(1) per state |
| Space | O(n^2) | 2D memo table + O(n) stack |

---

### Approach 3: Tabulation (Bottom-Up DP)

**State redefinition (simpler):** `dp[i]` = length of LIS **ending at index i**.

**Base case:** `dp[i] = 1` for all i (every element alone is a subsequence of length 1).

**Recurrence:**
```
dp[i] = max(dp[j] + 1) for all j < i where nums[j] < nums[i]
```
In other words: for each `i`, look at all previous indices `j`. If `nums[j] < nums[i]`, then we can extend the LIS ending at `j` by appending `nums[i]`.

**Answer:** `max(dp[0], dp[1], ..., dp[n-1])`

**Dry-run trace** for `[10, 9, 2, 5, 3, 7, 101, 18]`:
```
Index:  0    1    2    3    4    5    6     7
Value: 10    9    2    5    3    7   101   18
dp:   [ 1    1    1    1    1    1    1     1 ]  (initial)

i=1: j=0: 9 > 10? No.                    dp = [1, 1, ...]
i=2: j=0: 2 > 10? No.  j=1: 2 > 9? No.  dp = [1, 1, 1, ...]
i=3: j=0: 5 > 10? No.  j=1: 5 > 9? No.  j=2: 5 > 2? YES dp[3]=dp[2]+1=2
     dp = [1, 1, 1, 2, ...]
i=4: j=2: 3 > 2? YES dp[4]=2.  j=3: 3 > 5? No.
     dp = [1, 1, 1, 2, 2, ...]
i=5: j=2: 7>2 dp=2. j=3: 7>5 dp=3. j=4: 7>3 dp=3.
     dp = [1, 1, 1, 2, 2, 3, ...]
i=6: j=0: 101>10 dp=2. j=3: 101>5 dp=3. j=5: 101>7 dp=4.
     dp = [1, 1, 1, 2, 2, 3, 4, ...]
i=7: j=2: 18>2 dp=2. j=3: 18>5 dp=3. j=5: 18>7 dp=4.
     dp = [1, 1, 1, 2, 2, 3, 4, 4]

Answer = max(dp) = 4
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n^2) | Two nested loops |
| Space | O(n) | 1D dp array |

---

### Approach 4: Space-Optimized -- Binary Search + Patience Sorting

**Core idea:** Maintain an array `tails` where `tails[i]` = the smallest tail element of all increasing subsequences of length `i+1`.

**Key property:** `tails` is always **sorted** (proof: if we have an IS of length 3 ending in 5, we certainly have one of length 2 ending in something <= 5).

**Algorithm:**
```
tails = []
for each num in nums:
    if num > tails[-1] (or tails is empty):
        tails.append(num)       // extend longest subsequence
    else:
        find leftmost tails[j] >= num using binary search
        tails[j] = num          // replace to keep smallest tail
```

**Why replacing works:** We're not tracking the actual LIS. We're maintaining the **potential** for future extensions. A smaller tail gives more room for future elements to extend the subsequence.

**Dry-run trace** for `[10, 9, 2, 5, 3, 7, 101, 18]`:
```
num=10:  tails=[] -> append     tails=[10]
num=9:   9 < 10, replace at 0  tails=[9]
num=2:   2 < 9, replace at 0   tails=[2]
num=5:   5 > 2, append         tails=[2, 5]
num=3:   3 < 5, replace at 1   tails=[2, 3]
num=7:   7 > 3, append         tails=[2, 3, 7]
num=101: 101 > 7, append       tails=[2, 3, 7, 101]
num=18:  18 < 101, replace 3   tails=[2, 3, 7, 18]

Answer = len(tails) = 4
```

**Important:** `tails` is NOT the actual LIS! It's a bookkeeping structure. `[2, 3, 7, 18]` happens to be a valid LIS here, but that's coincidence.

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n log n) | One pass, binary search per element |
| Space | O(n) | tails array (at most n elements) |

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Analogy |
|----------|------|-------|---------|
| Recursion | O(2^n) | O(n) | Trying every outfit combination in your closet |
| Memoization | O(n^2) | O(n^2) | Writing down which outfits you already tried |
| Tabulation | O(n^2) | O(n) | Building the answer bottom-up, row by row |
| Binary Search | O(n log n) | O(n) | Playing patience (card game) -- each card goes on the leftmost valid pile |

**Why O(n log n) is optimal:** We must read every element at least once (O(n)). The binary search per element (O(log n)) is needed to find the right position. Information-theoretic lower bound suggests O(n log n) is tight.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out |
|-----------|----------|-----------|
| Single element `[5]` | `1` | Don't return 0 |
| Already sorted `[1,2,3,4,5]` | `5` | LIS = entire array |
| Reverse sorted `[5,4,3,2,1]` | `1` | Every element alone |
| All equal `[7,7,7,7]` | `1` | **Strictly** increasing, not non-decreasing |
| Negative numbers `[-2,-1,0]` | `3` | Works the same way |
| Two elements `[2,1]` | `1` | Check both directions |

**Common mistakes:**
- Using `>=` instead of `>` (must be **strictly** increasing)
- In binary search approach: using `bisect_right` instead of `bisect_left` (we want leftmost position >= num)
- Forgetting that `tails` array does NOT represent the actual LIS
- Off-by-one in the tabulation when comparing `dp[j] + 1`

---

## INTERVIEW LENS

**What interviewers are looking for:**
1. Can you identify the DP substructure? (LIS ending at each index)
2. Can you optimize from O(n^2) to O(n log n)?
3. Do you understand why the patience sorting approach works?

**Follow-up questions:**
- "Print the actual LIS, not just the length" --> Track parent pointers in O(n^2) approach, or track indices in the binary search approach
- "Count the number of LIS" --> Need additional `count[]` array alongside `dp[]`
- "Longest non-decreasing subsequence" --> Change `<` to `<=` and `bisect_left` to `bisect_right`

**Time management:** Start with O(n^2) tabulation (easiest to code correctly), then optimize to O(n log n) if time allows.

---

## CONNECTIONS

| Related Problem | Connection |
|----------------|------------|
| Longest Common Subsequence | LIS of A = LCS of A and sorted(A) |
| Russian Doll Envelopes (LC #354) | 2D LIS: sort by width, LIS on height |
| Number of LIS (LC #673) | Same DP + count tracking |
| Increasing Triplet Subsequence (LC #334) | LIS with k=3, can be O(n) space O(1) |
| Patience Sorting | The binary search approach IS patience sorting |
| Maximum Sum Increasing Subsequence | Same DP, track sum instead of length |
