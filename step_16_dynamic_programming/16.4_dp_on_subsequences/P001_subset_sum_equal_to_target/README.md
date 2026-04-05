# Subset Sum Equal to Target

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an array of **non-negative integers** `arr` and a **target** integer, determine whether there exists a **subset** of `arr` whose elements sum up to exactly `target`. Return `true` or `false`.

*(Coding Ninjas / GFG)*

**Constraints:**
- `1 <= n <= 10^3`
- `0 <= arr[i] <= 10^3`
- `0 <= target <= 10^3`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [1,2,3,4], target = 4` | `true` | Subset {4} or {1,3} sums to 4 |
| `arr = [2,3,7,8,10], target = 11` | `true` | Subset {3,8} sums to 11 |
| `arr = [1,2,3], target = 7` | `false` | No subset sums to 7 (max possible = 6) |
| `arr = [6,1,2,1], target = 4` | `true` | Subset {1,2,1} sums to 4 |

### Real-Life Analogy
> *You have coins of different denominations in your pocket. Your friend asks: "Can you pay exactly $11 with some combination of these coins?" You can choose to include or exclude each coin. This is the subset sum problem -- at each coin, you decide: take it (reduce remaining target) or leave it (keep target the same). If at any path the remaining target hits zero, the answer is yes.*

### Key Observations
1. **Binary choice per element:** For each element, either include it in the subset (subtract from target) or exclude it. This gives a natural recursion tree. <-- This is the "aha" insight
2. **State = (index, remaining sum):** `dp[i][s]` = can we form sum `s` using elements from index 0 to i? The recurrence is `dp[i][s] = dp[i-1][s] OR dp[i-1][s - arr[i]]`.
3. **Base cases:** `dp[i][0] = true` for all i (empty subset sums to 0). If `arr[0] <= target`, then `dp[0][arr[0]] = true`. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** Different branches of the recursion tree reach the same `(index, remainingSum)` state.
- **Optimal substructure:** If a subset summing to `target` exists using elements 0..i, it either includes `arr[i]` (needs sum `target - arr[i]` from 0..i-1) or does not (needs sum `target` from 0..i-1).
- This is the foundational **subset sum / knapsack** pattern.

### Pattern Recognition
- **Pattern:** 2D DP on subsequences (include/exclude)
- **Classification Cue:** "When you see _can you form a target sum from a subset_ --> think _dp[index][sum] with take/not-take choices_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** For each element, try including it (if it does not exceed remaining sum) or excluding it.

**State Definition:** `solve(i, target)` = can we form `target` using elements 0..i?

**Recurrence:**
```
solve(i, target) = solve(i-1, target)                          // exclude arr[i]
                 OR solve(i-1, target - arr[i])  (if arr[i] <= target)  // include arr[i]
Base cases:
  if target == 0: return true    (empty subset)
  if i == 0: return arr[0] == target
```

**Steps:**
1. Call `solve(n-1, target)`.
2. At each index, try excluding (recurse with same target) or including (recurse with reduced target).
3. If any path returns true, the answer is true.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Same (index, target) states are recomputed. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Store results in `dp[i][target]`. Each state computed at most once.

**Dry Run:** `arr = [1,2,3], target = 4`

| Call | i | target | Compute | Result |
|------|---|--------|---------|--------|
| solve(2,4) | 2 | 4 | solve(1,4) OR solve(1,1) | |
| solve(1,4) | 1 | 4 | solve(0,4) OR solve(0,2) | |
| solve(0,4) | 0 | 4 | arr[0]==4? No | false |
| solve(0,2) | 0 | 2 | arr[0]==2? No | false |
| back(1,4) | | | false OR false = false | false |
| solve(1,1) | 1 | 1 | solve(0,1) OR solve(0,-1) | |
| solve(0,1) | 0 | 1 | arr[0]==1? Yes! | **true** |
| back(1,1) | | | true (short-circuit) | true |
| back(2,4) | | | false OR true = **true** | **true** |

Subset found: {1, 3} = 4.

| Time | Space |
|------|-------|
| O(n * target) | O(n * target) |

**BUD Transition:** Remove recursion overhead with bottom-up iteration.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[n][target+1]` table iteratively.

**Steps:**
1. `dp[i][0] = true` for all i (empty subset).
2. `dp[0][arr[0]] = true` if `arr[0] <= target`.
3. For `i = 1` to `n-1`, for `s = 1` to `target`:
   - `notTake = dp[i-1][s]`
   - `take = dp[i-1][s - arr[i]]` if `arr[i] <= s`
   - `dp[i][s] = notTake OR take`
4. Return `dp[n-1][target]`.

**Dry Run:** `arr = [1,2,3], target = 4`

|   | s=0 | s=1 | s=2 | s=3 | s=4 |
|---|-----|-----|-----|-----|-----|
| i=0 (arr=1) | T | T | F | F | F |
| i=1 (arr=2) | T | T | T | T | F |
| i=2 (arr=3) | T | T | T | T | **T** |

dp[2][4] = dp[1][4] OR dp[1][1] = false OR true = **true**

| Time | Space |
|------|-------|
| O(n * target) | O(n * target) |

**BUD Transition:** Row `i` only depends on row `i-1`. Use a single 1D array.

### Approach 4: Space Optimized
**What changed:** Use a single 1D array `prev[target+1]`. Update from right to left (or use two arrays).

**Steps:**
1. `prev[0] = true`. If `arr[0] <= target`, `prev[arr[0]] = true`.
2. For `i = 1` to `n-1`:
   - Create `curr[target+1]`, set `curr[0] = true`.
   - For `s = 1` to `target`:
     - `curr[s] = prev[s] OR (prev[s - arr[i]]` if `arr[i] <= s)`
   - `prev = curr`
3. Return `prev[target]`.

| Time | Space |
|------|-------|
| O(n * target) | **O(target)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each element branches into 2 choices (take/skip), giving O(2^n) paths."
**Memo/Tab:** "There are n * (target+1) unique states, each O(1) to compute. Total O(n * target)."
**Space Optimized:** "Same time, but we only keep one row of size target+1, so O(target) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting `dp[i][0] = true`** -- the empty subset always sums to 0.
2. **Not checking `arr[i] <= s` before taking** -- leads to negative index access.
3. **Using 1D array and iterating left to right** -- this overwrites values needed later. Either use two arrays or iterate right to left.
4. **Handling arr[0] = 0** -- `dp[0][0]` is true by default, no special case needed.

### Edge Cases to Test
- [ ] `target = 0` --> always true (empty subset)
- [ ] Single element equals target `[5], target=5` --> true
- [ ] Single element does not equal target `[5], target=3` --> false
- [ ] Array contains zeros `[0,1,2], target=3` --> true
- [ ] All elements same `[3,3,3], target=6` --> true
- [ ] Target exceeds total sum --> false

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Subset, not subarray? Elements are non-negative? Can elements be zero?"
2. **Recurrence:** "For each element: include it (reduce target by arr[i]) or exclude it. dp[i][s] = dp[i-1][s] OR dp[i-1][s-arr[i]]."
3. **Progression:** Recursion -> Memo -> Tab -> Space. Code the space-optimized version.
4. **Quick optimization:** "If total sum < target, immediately return false."

### Follow-Up Questions
- "Count the number of subsets?" --> Change OR to addition: `dp[i][s] = dp[i-1][s] + dp[i-1][s-arr[i]]`.
- "Partition into two equal-sum subsets?" --> Check if totalSum is even, then solve subsetSum(arr, totalSum/2).
- "What if elements can be negative?" --> Shift indices: use `dp[i][s + offset]`.

---

## CONNECTIONS
- **Prerequisite:** 0/1 Knapsack concept (take/not-take decisions)
- **Same Pattern:** Partition Equal Subset Sum, Count Subsets with Sum K
- **This Unlocks:** Partition problems, Coin Change, Target Sum, all subset DP
- **Harder Variant:** Count Subsets with Sum K, Minimum Subset Sum Difference
