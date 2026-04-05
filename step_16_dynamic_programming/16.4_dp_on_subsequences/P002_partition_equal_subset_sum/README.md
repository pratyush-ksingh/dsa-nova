# Partition Equal Subset Sum

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a **non-empty** array `nums` of **positive integers**, determine if the array can be **partitioned** into two subsets such that the sum of elements in both subsets is **equal**.

*(LeetCode #416)*

**Constraints:**
- `1 <= nums.length <= 200`
- `1 <= nums[i] <= 100`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [1,5,11,5]` | `true` | {1,5,5} and {11} both sum to 11 |
| `nums = [1,2,3,5]` | `false` | Total = 11 (odd), impossible |
| `nums = [2,2,1,1]` | `true` | {2,1} and {2,1} both sum to 3 |
| `nums = [1,2,5]` | `false` | Total = 8, target = 4, no subset sums to 4 |

### Real-Life Analogy
> *You and a friend order several dishes at a restaurant and want to split the bill exactly in half. Each dish goes entirely to one person's tab -- you cannot split a single dish. If the total bill is odd, it is impossible. If even, you need to find a group of dishes costing exactly half the total. This is the Partition Equal Subset Sum problem.*

### Key Observations
1. **Reduce to Subset Sum:** If totalSum is odd, immediately return false. If even, the problem reduces to: "does a subset exist with sum = totalSum / 2?" This is the classic Subset Sum problem. <-- This is the "aha" insight
2. **State = (index, remaining target):** `dp[i][s]` = can we form sum `s` using elements 0..i? Recurrence: `dp[i][s] = dp[i-1][s] OR dp[i-1][s - nums[i]]`.
3. **Early exits save time:** If totalSum is odd, return false immediately. If any element exceeds totalSum/2, return false. If any element equals totalSum/2, return true. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** Different recursion branches reach the same `(index, remainingSum)` state.
- **Optimal substructure:** Answer for index i depends only on index i-1 results.
- Classic **0/1 Knapsack / Subset Sum** reduction -- one of the most important DP reductions.

### Pattern Recognition
- **Pattern:** DP on subsequences (include/exclude) with sum target
- **Classification Cue:** "When you see _partition into two equal parts_ --> think _subset sum = totalSum / 2_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Compute totalSum. If odd, return false. Otherwise check if a subset sums to totalSum/2 using take/not-take recursion.

**State Definition:** `solve(i, target)` = can we form `target` using elements 0..i?

**Recurrence:**
```
solve(i, target) = solve(i-1, target)                            // skip nums[i]
                OR solve(i-1, target - nums[i])  if nums[i] <= target  // take nums[i]
Base cases:
  if target == 0: return true
  if i == 0: return nums[0] == target
```

**Steps:**
1. Compute `totalSum`. If odd, return false.
2. Set `target = totalSum / 2`.
3. Call `solve(n-1, target)`.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Exponential due to repeated (index, target) states. Memoize them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][target]` so each state is computed at most once.

**Dry Run:** `nums = [1,5,11,5]`, totalSum = 22, target = 11

| Call | i | target | Compute | Result |
|------|---|--------|---------|--------|
| solve(3,11) | 3 | 11 | solve(2,11) OR solve(2,6) | |
| solve(2,11) | 2 | 11 | solve(1,11) OR solve(1,0) | |
| solve(1,0) | 1 | 0 | base: target==0 | **true** |
| back(2,11) | | | _ OR true | **true** |
| back(3,11) | | | true (short-circuit) | **true** |

Subset found: {11} sums to 11. Remaining {1,5,5} also sums to 11.

| Time | Space |
|------|-------|
| O(n * target) | O(n * target) |

**BUD Transition:** Eliminate recursion stack overhead with bottom-up iteration.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[n][target+1]` table iteratively.

**Steps:**
1. `dp[i][0] = true` for all i.
2. `dp[0][nums[0]] = true` if `nums[0] <= target`.
3. For `i = 1` to `n-1`, for `s = 1` to `target`:
   - `dp[i][s] = dp[i-1][s] OR (dp[i-1][s - nums[i]]` if `nums[i] <= s)`
4. Return `dp[n-1][target]`.

**Dry Run:** `nums = [1,5,11,5]`, target = 11

|   | s=0 | s=1 | s=5 | s=6 | s=11 |
|---|-----|-----|-----|-----|------|
| i=0 (1) | T | T | F | F | F |
| i=1 (5) | T | T | T | T | F |
| i=2 (11) | T | T | T | T | **T** |

dp[2][11] = dp[1][11] OR dp[1][0] = false OR true = **true**

| Time | Space |
|------|-------|
| O(n * target) | O(n * target) |

**BUD Transition:** Row `i` only depends on row `i-1`. Compress to 1D.

### Approach 4: Space Optimized
**What changed:** Use `prev[target+1]` and `curr[target+1]` arrays.

**Steps:**
1. `prev[0] = true`. If `nums[0] <= target`, set `prev[nums[0]] = true`.
2. For `i = 1` to `n-1`:
   - `curr[0] = true`
   - For `s = 1` to `target`: `curr[s] = prev[s] OR (prev[s - nums[i]]` if `nums[i] <= s)`
   - `prev = curr`
3. Return `prev[target]`.

| Time | Space |
|------|-------|
| O(n * target) | **O(target)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each element branches into 2 choices, giving O(2^n) paths."
**Memo/Tab:** "There are n * (target+1) states where target = totalSum/2. Each is O(1). Total: O(n * totalSum/2)."
**Space Optimized:** "Same time, but only keep one row of size target+1, so O(totalSum/2) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the odd-sum check** -- if totalSum is odd, no equal partition exists. Most important early exit.
2. **Not handling single-element arrays** -- `[1]` has odd sum, return false.
3. **Using 1D array iterating left-to-right** -- corrupts values needed later. Use two arrays or iterate right-to-left.
4. **Off-by-one on base case** -- `dp[0][nums[0]] = true` must check `nums[0] <= target`.

### Edge Cases to Test
- [ ] Odd total sum `[1,2,3,5]` --> false
- [ ] Single element `[1]` --> false
- [ ] Two equal elements `[3,3]` --> true
- [ ] All same elements `[4,4,4,4]` --> true
- [ ] Large single element dominates `[1,1,1,100]` --> false
- [ ] All ones `[1,1,1,1]` --> true (sum=4, target=2)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Partition means every element goes to exactly one subset? Elements positive?"
2. **Key Reduction:** "Equal partition means each subset sums to totalSum/2. If odd, impossible."
3. **Progression:** Recursion -> Memo -> Tab -> Space. Code the space-optimized version.
4. **Mention:** "This directly reuses the Subset Sum solution."

### Follow-Up Questions
- "Partition into K equal-sum subsets?" --> Backtracking with bitmask DP (LeetCode #698).
- "Minimize the difference between two partitions?" --> Subset Sum for all achievable sums, pick closest to totalSum/2.
- "What if elements can be negative?" --> Offset trick to handle negative indices.

---

## CONNECTIONS
- **Prerequisite:** Subset Sum Equal to Target (P001 in this section)
- **Same Pattern:** Target Sum, Count Subsets with Sum K
- **This Unlocks:** Minimum Subset Sum Difference, Partition problems
- **Harder Variant:** Partition to K Equal Sum Subsets (LeetCode #698)
