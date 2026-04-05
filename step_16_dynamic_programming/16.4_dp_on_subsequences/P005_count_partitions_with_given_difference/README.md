# Count Partitions with Given Difference

> **Step 16.16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array `arr` and a difference `D`, count the number of ways to partition the array into two subsets `S1` and `S2` such that `S1 - S2 = D` (where S1 >= S2). Each element must belong to exactly one subset.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| arr=[5,2,6,4], D=3 | 1 | S1={6,4}=10, S2={5,2}=7, diff=3 |
| arr=[1,1,1,1], D=0 | 6 | 6 ways to split into equal-sum subsets |
| arr=[0,0,1], D=1 | 4 | Multiple ways due to zeros |

### Constraints
- `1 <= n <= 100`
- `0 <= arr[i] <= 1000`
- `0 <= D <= totalSum`

### Key Reduction
- S1 + S2 = totalSum and S1 - S2 = D
- Therefore S1 = (totalSum + D) / 2
- Problem reduces to: **count subsets with sum = target**
- If `(totalSum + D)` is odd or negative, answer is 0

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** For each element, either include it in subset S1 or not. Count how many ways we can reach the target sum = (totalSum + D) / 2.

**Steps:**
1. Compute `target = (totalSum + D) / 2`. If invalid, return 0
2. Recursively include/exclude each element
3. Base case at index 0: handle the element (special case for 0)
4. Return total count

```
Dry-run: arr=[5,2,6,4], D=3
total=17, target=(17+3)/2=10
Count subsets summing to 10: {6,4} -> 1 way
```

| Metric | Value |
|--------|-------|
| Time   | O(2^n) |
| Space  | O(n) |

---

### Approach 2: Optimal -- DP Tabulation

**Intuition:** Use 2D DP where `dp[i][s]` = number of ways to form sum `s` using the first `i+1` elements. For each element, we either pick it (subtract from sum) or skip it.

**Steps:**
1. Compute target. Initialize `dp[0][0]` and `dp[0][arr[0]]`
2. Handle `arr[0] == 0` specially: `dp[0][0] = 2` (pick or skip both give sum 0)
3. For each subsequent element, fill: `dp[i][s] = dp[i-1][s] + dp[i-1][s-arr[i]]`
4. Return `dp[n-1][target]`

| Metric | Value |
|--------|-------|
| Time   | O(n * target) |
| Space  | O(n * target) |

---

### Approach 3: Best -- Space-Optimized 1D DP

**Intuition:** Each row only depends on the previous row. Use two 1D arrays `prev` and `curr` (or iterate right-to-left with a single array for subset sum variants).

**Steps:**
1. Initialize `prev` array for index 0
2. For each element from index 1 to n-1:
   - Create `curr` array
   - `curr[s] = prev[s] + prev[s - arr[i]]`
   - Swap `prev = curr`
3. Return `prev[target]`

| Metric | Value |
|--------|-------|
| Time   | O(n * target) |
| Space  | O(target) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| (totalSum + D) is odd | Return 0 | No valid integer target |
| D > totalSum | Return 0 | Impossible partition |
| Array contains zeros | Zeros can go to either subset | Multiply count by 2^(number of zeros) |
| All elements are 0 | All go to either side | 2^n ways if D=0 |

**Common Mistakes:**
- Forgetting to handle zeros (arr[i]=0 can be picked or not, both give same sum)
- Not checking (totalSum + D) % 2 != 0
- Integer overflow with large arrays

---

## Real-World Use Case
**Team balancing:** Splitting a group of employees with skill ratings into two teams where the skill difference is exactly D. Counting valid splits helps evaluate team formation options.

## Interview Tips
- Key insight: reduce the two-subset problem to a single subset sum problem
- The mathematical reduction S1 = (totalSum + D) / 2 is the critical "aha" moment
- Handle zeros carefully -- they double the count for each zero in the array
- This is a variant of "Count Subsets with Sum K" -- master that first
- Space optimization is straightforward: prev/curr pattern
