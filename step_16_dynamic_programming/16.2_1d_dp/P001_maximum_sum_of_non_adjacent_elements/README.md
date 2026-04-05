# Maximum Sum of Non Adjacent Elements

> **Batch 1 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an array of positive integers `nums`, find the **maximum sum** of elements such that no two selected elements are **adjacent** (i.e., their indices differ by at least 2).

This is equivalent to the **House Robber** problem (LeetCode #198): a robber wants to maximize loot from a row of houses, but cannot rob two adjacent houses (alarms will trigger).

*(LeetCode #198)*

**Constraints:**
- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 400`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[2, 7, 9, 3, 1]` | `12` | Pick 2 + 9 + 1 = 12. Or 7 + 3 = 10. Best = 12 |
| `[1, 2, 3, 1]` | `4` | Pick 1 + 3 = 4. Or 2 + 1 = 3. Best = 4 |
| `[2, 1, 4, 5, 3, 1, 1, 3]` | `12` | Pick 2 + 5 + 1 + 3 = 11. Or 2 + 4 + 3 + 3 = 12. Best = 12 |

### Real-Life Analogy
> *You are walking down a street of houses with gold coins on the doorstep. If you pick up coins from one house, the neighbors on both sides get alerted and you cannot pick from them. You want to collect the maximum coins. At each house, you face a decision: take this house's coins (but skip the previous one) or skip this house (and keep whatever you had). This "pick or skip" decision at every step is the essence of the problem.*

### Key Observations
1. **Pick-or-skip decision:** For each element at index `i`, either include it (add `nums[i]` and skip `i-1`) or exclude it (carry forward the best from `i-1`).
2. **Recurrence:** `dp[i] = max(dp[i-1], dp[i-2] + nums[i])`.
   - `dp[i-1]`: skip element `i`, best sum up to index `i-1`
   - `dp[i-2] + nums[i]`: pick element `i`, best sum up to index `i-2` plus current
3. This recurrence depends on only the two previous states -- same Fibonacci-like pattern. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** The max sum at index `i` depends on max sums at `i-1` and `i-2`, which share subproblems.
- **Optimal substructure:** The optimal solution for the first `i` elements is built from optimal solutions for smaller prefixes.
- The 4-stage DP progression applies: Recursion -> Memo -> Tab -> Space.

### Pattern Recognition
- **Pattern:** 1D DP with pick/skip decisions
- **Classification Cue:** "When you see _maximize/minimize selection_ with _no two adjacent_ constraint --> think _pick-or-skip recurrence: dp[i] = max(dp[i-1], dp[i-2] + nums[i])_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** From the last index, make a choice: pick (add value, jump to `i-2`) or skip (go to `i-1`). Take the maximum.

**Steps:**
1. `solve(i)`: max sum considering elements 0..i.
2. Base cases: `solve(0) = nums[0]`, `solve(-1) = 0` (or handle `i < 0` returning 0).
3. `solve(i) = max(solve(i-1), nums[i] + solve(i-2))`.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Same subproblems recomputed exponentially. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Add `dp[]` cache. Each index computed at most once.

**Dry Run:** `nums = [2, 7, 9, 3, 1]`

| Call | i | Cached? | Compute | dp[] |
|------|---|---------|---------|------|
| 1 | 4 | No | max(solve(3), 1+solve(2)) | |
| 2 | 3 | No | max(solve(2), 3+solve(1)) | |
| 3 | 2 | No | max(solve(1), 9+solve(0)) | |
| 4 | 1 | No | max(solve(0), 7+solve(-1)) = max(2, 7) = 7 | dp[1]=7 |
| 5 | 0 | Base | 2 | dp[0]=2 |
| back | 2 | Store | max(7, 9+2) = max(7, 11) = 11 | dp[2]=11 |
| back | 3 | Store | max(11, 3+7) = max(11, 10) = 11 | dp[3]=11 |
| 6 | 2 | **Yes** | 11 | |
| back | 4 | Store | max(11, 1+11) = max(11, 12) = 12 | dp[4]=12 |

**Result:** 12

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Remove recursion overhead with bottom-up tabulation.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill dp array iteratively.

**Steps:**
1. `dp[0] = nums[0]`.
2. `dp[1] = max(nums[0], nums[1])`.
3. For `i = 2` to `n-1`: `dp[i] = max(dp[i-1], dp[i-2] + nums[i])`.
4. Return `dp[n-1]`.

**Dry Run:** `nums = [2, 7, 9, 3, 1]`

| i | nums[i] | dp[i-1] (skip) | dp[i-2]+nums[i] (pick) | dp[i] |
|---|---------|----------------|----------------------|-------|
| 0 | 2 | - | - | 2 |
| 1 | 7 | 2 | 7 | 7 (max(2,7)) |
| 2 | 9 | 7 | 2+9=11 | 11 |
| 3 | 3 | 11 | 7+3=10 | 11 |
| 4 | 1 | 11 | 11+1=12 | 12 |

**Result:** 12

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Only `dp[i-1]` and `dp[i-2]` are used. Two variables suffice.

### Approach 4: Space Optimized
**What changed:** Replace array with `prev2` and `prev1`.

**Steps:**
1. `prev2 = nums[0]`, `prev1 = max(nums[0], nums[1])`.
2. For `i = 2` to `n-1`:
   - `curr = max(prev1, prev2 + nums[i])`
   - `prev2 = prev1`
   - `prev1 = curr`
3. Return `prev1`.

**Dry Run:** `nums = [2, 7, 9, 3, 1]`

| i | nums[i] | prev2 | prev1 | curr |
|---|---------|-------|-------|------|
| - | - | 2 | 7 | - |
| 2 | 9 | 2 | 7 | max(7, 2+9)=11 |
| 3 | 3 | 7 | 11 | max(11, 7+3)=11 |
| 4 | 1 | 11 | 11 | max(11, 11+1)=12 |

**Result:** 12

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "At each element we branch into 2 choices (pick/skip), giving O(2^n) without caching."
**Memo/Tab:** "Each of n states computed once, O(1) work per state. Total O(n)."
**Space Optimized:** "Same O(n) time, but O(1) space since we only track two rolling values."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Setting dp[1] = nums[1] instead of max(nums[0], nums[1])** -- dp[1] represents the best sum from elements 0..1, not just nums[1].
2. **Forgetting to handle single-element arrays** -- if `n == 1`, return `nums[0]`.
3. **Negative values confusion** -- constraints say `nums[i] >= 0`, but if negatives were allowed, the pick/skip logic still works.

### Edge Cases to Test
- [ ] Single element `[5]` --> 5
- [ ] Two elements `[3, 7]` --> 7
- [ ] All same values `[5, 5, 5, 5]` --> 10 (pick indices 0 and 2)
- [ ] Alternating high-low `[100, 1, 100, 1]` --> 200
- [ ] One large element in middle `[1, 100, 1]` --> 100

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "No two adjacent elements can be picked? Array has non-negative values? Return the maximum sum?"
2. **Recurrence:** "At index i, I either pick it (adding nums[i] to the best from i-2) or skip it (keeping the best from i-1). dp[i] = max(dp[i-1], dp[i-2] + nums[i])."
3. **Progression:** Start with recursion -> show overlapping subproblems -> memoize -> tabulate -> space optimize.
4. **Code:** Write space-optimized version. Clean 10-line solution.

### Follow-Up Questions
- "What if the houses are in a circle?" --> House Robber II (LeetCode #337). Run the algorithm twice: once excluding the first house, once excluding the last. Take the max.
- "What if the houses form a tree?" --> House Robber III (LeetCode #337). Tree DP with (rob, skip) state at each node.
- "What is the actual subset, not just the sum?" --> Track choices and backtrack.

---

## CONNECTIONS
- **Prerequisite:** Climbing Stairs (P002), Frog Jump (P003) -- same 1D DP pattern
- **Same Pattern:** House Robber II (circular), Delete and Earn, Paint House
- **This Unlocks:** All 1D pick/skip DP problems, House Robber variants
- **Harder Variant:** House Robber II (circular constraint), House Robber III (tree structure)
