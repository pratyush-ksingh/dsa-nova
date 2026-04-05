# Count Subsets with Sum K

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an array of non-negative integers `arr` and a target sum `k`, count the **number of subsets** whose elements sum to exactly `k`.

**Critical Corner Case: Zeros!** Each zero in the array can be either included or excluded without changing the sum. If there are `z` zeros, every valid subset can be combined with any subset of zeros, multiplying the count by `2^z`. Many implementations get this wrong.

**Constraints:**
- `1 <= n <= 1000`
- `0 <= arr[i] <= 1000`
- `0 <= k <= 1000`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [1, 2, 3], k = 3` | `2` | {1,2} and {3} |
| `arr = [1, 1, 1], k = 2` | `3` | {arr[0],arr[1]}, {arr[0],arr[2]}, {arr[1],arr[2]} |
| `arr = [0, 1, 2, 3], k = 3` | `4` | {1,2}, {3}, {0,1,2}, {0,3}. Zero doubles each subset! |
| `arr = [0, 0, 1], k = 1` | `4` | {1}, {0,1}, {0,1}, {0,0,1}. Two zeros = 4x multiplier |
| `arr = [5, 2, 3, 10, 6, 8], k = 10` | `3` | {5,2,3}, {2,8}, {10} |

### Real-Life Analogy
> *Imagine you are at a buffet with dishes that have specific calorie counts. You want to know how many different plate combinations total exactly K calories. If some dishes have zero calories (like water), you can add or remove them freely, doubling your options for each such dish. This is exactly what meal planning apps compute when suggesting combinations that hit a calorie target.*

### Key Observations
1. **State Definition:** `dp[i][j]` = number of subsets using the first `i` elements that sum to `j`.
2. **Recurrence:** For element `arr[i-1]`:
   - **Not take:** `dp[i-1][j]` (subsets without this element)
   - **Take:** `dp[i-1][j - arr[i-1]]` (if `j >= arr[i-1]`)
   - `dp[i][j] = dp[i-1][j] + dp[i-1][j - arr[i-1]]`
3. **Base Case:** `dp[0][0] = 1` (empty subset sums to 0).
4. **Zero Handling:** When `arr[i-1] == 0`, both take and not-take lead to `dp[i-1][j]`, so `dp[i][j] = 2 * dp[i-1][j]`. This correctly doubles the count for each zero.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Recursion** tries take/not-take for each element. 2^n subsets total.
- **Memoization** caches `(index, remaining_sum)` pairs.
- **Tabulation** fills a 2D table bottom-up.
- **Space Optimization** only needs the previous row, so 1D array traversed right-to-left.

### Pattern Recognition
- **Pattern:** 0/1 Knapsack variant (count instead of max/min)
- **Classification Cue:** "When you see _count subsets with given sum_ --> think _0/1 knapsack where dp[j] counts ways instead of max value_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** For each element, either include it (subtract from target) or exclude it. Count paths that reach target = 0.

**Steps:**
1. `solve(idx, target)`: number of subsets from `arr[idx..n-1]` that sum to `target`.
2. Base case: `idx == n`. If `target == 0`, return 1. Else return 0.
3. `notTake = solve(idx + 1, target)`.
4. `take = 0`. If `arr[idx] <= target`: `take = solve(idx + 1, target - arr[idx])`.
5. Return `take + notTake`.

**Why it is slow:** 2^n subsets explored, many overlapping states.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** `(idx, target)` pairs repeat. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache results in `dp[idx][target]`.

**Steps:**
1. Create `dp[n+1][k+1]`, initialized to -1.
2. Same recursion with cache lookup/store.

**Dry Run:** `arr = [1, 2, 3], k = 3`

| Call | idx | target | take | notTake | Result |
|------|-----|--------|------|---------|--------|
| solve(0, 3) | 0 | 3 | solve(1, 2) | solve(1, 3) | ? |
| solve(1, 2) | 1 | 2 | solve(2, 0) | solve(2, 2) | ? |
| solve(2, 0) | 2 | 0 | solve(3, -3)=0 | solve(3, 0)=1 | 1 |
| solve(2, 2) | 2 | 2 | -- 3>2 skip | solve(3, 2)=0 | 0 |
| back solve(1,2) | | | 1 + 0 | | 1 |
| solve(1, 3) | 1 | 3 | solve(2, 1) | solve(2, 3) | ? |
| solve(2, 1) | 2 | 1 | -- 3>1 skip | solve(3, 1)=0 | 0 |
| solve(2, 3) | 2 | 3 | solve(3, 0)=1 | solve(3, 3)=0 | 1 |
| back solve(1,3) | | | 0 + 1 | | 1 |
| back solve(0,3) | | | 1 + 1 | | **2** |

| Time | Space |
|------|-------|
| O(n * k) | O(n * k) cache + O(n) stack |

**BUD Transition:** Remove recursion, go bottom-up.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill `dp[i][j]` iteratively.

**Steps:**
1. `dp[0][0] = 1` (empty subset, sum 0).
2. For `i = 1` to `n`:
   - For `j = 0` to `k`:
     - `dp[i][j] = dp[i-1][j]` (not take)
     - If `j >= arr[i-1]`: `dp[i][j] += dp[i-1][j - arr[i-1]]` (take)
3. Return `dp[n][k]`.

**Dry Run:** `arr = [1, 2, 3], k = 3`

```
         j=0  j=1  j=2  j=3
i=0 (-)    1    0    0    0
i=1 (1)    1    1    0    0     (j=1: dp[0][0]=1)
i=2 (2)    1    1    1    1     (j=2: dp[1][0]=1, j=3: dp[1][1]=1)
i=3 (3)    1    1    1    2     (j=3: dp[2][3] + dp[2][0] = 1+1 = 2)
```

Answer: `dp[3][3] = 2`

**Dry Run with zeros:** `arr = [0, 1, 2, 3], k = 3`

```
         j=0  j=1  j=2  j=3
i=0 (-)    1    0    0    0
i=1 (0)    2    0    0    0     (j=0: dp[0][0]+dp[0][0]=1+1=2. Zero doubles!)
i=2 (1)    2    2    0    0
i=3 (2)    2    2    2    2
i=4 (3)    2    2    2    4     (j=3: dp[3][3]+dp[3][0]=2+2=4)
```

Answer: `dp[4][3] = 4` (correct!)

| Time | Space |
|------|-------|
| O(n * k) | O(n * k) |

**BUD Transition:** Row `i` only depends on row `i-1`. Use 1D.

### Approach 4: Space Optimized
**What changed:** Single 1D array, traverse `j` from right to left (to avoid using updated values from the same row).

**Steps:**
1. `dp[0] = 1`, rest 0.
2. For each element `num` in `arr`:
   - For `j = k` down to `num`: `dp[j] += dp[j - num]`.
   - Special: if `num == 0`, then `dp[j] += dp[j]` for all j, which means `dp[j] *= 2`. Handle separately.
3. Return `dp[k]`.

**Handling zeros properly:** Process zeros separately. Count them, then multiply final answer by `2^z`.

| Time | Space |
|------|-------|
| O(n * k) | **O(k)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "2 choices per element, 2^n subsets total."
**Memo/Tab:** "n elements x (k+1) possible sums = O(n*k) states, O(1) work each."
**Space Optimized:** "Same time, but only one row of size k+1."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Zeros:** The biggest pitfall. `arr = [0, 0, 1], k = 1` has 4 subsets, not 1. Each zero doubles the count.
2. **dp[0][0] initialization:** Must be 1 (empty subset sums to 0). Forgetting this gives all-zero answers.
3. **Right-to-left in space optimization:** Going left-to-right would use the same element multiple times (unbounded knapsack).
4. **Target = 0:** The answer is `2^(count of zeros)` if no other elements can sum to 0 on their own.

### Edge Cases to Test
- [ ] `k = 0` --> count subsets summing to 0 (at least 1: the empty set, plus all zero-containing subsets)
- [ ] All zeros --> `2^n` (every subset sums to 0)
- [ ] No subset sums to k --> 0
- [ ] Single element equal to k --> 1
- [ ] Array with zeros and k > 0

---

## INTERVIEW LENS

### How to Present
1. **Start with recursion:** "For each element, I can include or exclude it. Count paths where remaining sum reaches 0."
2. **Add memoization:** "State is (index, remaining_sum). Many overlapping states."
3. **Convert to tabulation:** "dp[i][j] = ways using first i elements to reach sum j."
4. **Optimize space:** "Only previous row needed. 1D array, right-to-left."
5. **Mention zeros explicitly:** "Zeros are tricky -- each zero doubles the count because it can be freely added to any valid subset."

### Follow-Up Questions
- "What about negative numbers?" --> Can't use standard knapsack. Use offset or hash map for states.
- "Count subsets with sum in a range [lo, hi]?" --> `count(hi) - count(lo - 1)`.
- "What if elements can be used multiple times?" --> Unbounded knapsack: traverse j left-to-right.

---

## CONNECTIONS
- **Prerequisite:** Subset Sum (boolean version), 0/1 Knapsack
- **Same Pattern:** Partition Equal Subset Sum, Target Sum
- **This Unlocks:** Partition into K equal subsets, Coin Change (count ways)
- **Harder Variant:** Count subsets with given difference, Partition with minimum difference
