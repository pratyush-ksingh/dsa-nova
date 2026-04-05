# House Robber II

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are a robber planning to rob houses arranged **in a circle**. Each house has a certain amount of money. Adjacent houses have connected security systems -- if two adjacent houses are robbed on the same night, the police will be alerted. The first and last houses are also adjacent (circular arrangement). Return the **maximum amount** of money you can rob without alerting the police.

*(LeetCode #213)*

**Constraints:**
- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 1000`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [2,3,2]` | `3` | Cannot rob house 1 (2) and house 3 (2) since they are adjacent in circle. Rob house 2 (3) |
| `nums = [1,2,3,1]` | `4` | Rob house 1 (1) + house 3 (3) = 4 |
| `nums = [1,2,3]` | `3` | Rob house 3 (3) only |
| `nums = [0]` | `0` | Single house with 0 money |

### Real-Life Analogy
> *Houses are arranged in a cul-de-sac (circular street). The first and last houses can see each other. The trick: if you might rob house 1, you definitely cannot rob house N. If you might rob house N, you definitely cannot rob house 1. So run the linear House Robber problem TWICE: once excluding the last house, once excluding the first. Take the maximum of the two results.*

### Key Observations
1. **Circular constraint reduces to two linear problems:** Since houses form a circle, house 0 and house n-1 are adjacent. We can never rob both. Solution: `max(rob(0..n-2), rob(1..n-1))`. <-- This is the "aha" insight
2. **Each linear sub-problem is standard House Robber I:** `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` -- take current (add to i-2 result) or skip (carry i-1 result).
3. **Single element edge case:** If `n == 1`, answer is `nums[0]`. The two-range trick does not apply. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** The decision at house `i` depends on decisions at house `i-1` and `i-2`.
- **Optimal substructure:** Max money from houses 0..i can be computed from 0..i-1 and 0..i-2.
- The circular constraint is elegantly handled by running the linear solution twice.

### Pattern Recognition
- **Pattern:** 1D DP with circular constraint (break circle into two linear segments)
- **Classification Cue:** "When you see _circular arrangement with adjacent constraint_ --> think _run linear DP twice: skip first OR skip last_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Break into two linear problems. For each, recursively decide: rob house `i` (skip i-1) or skip house `i` (consider i-1).

**State Definition:** `solve(i)` = max money robbing from houses 0..i (linear).

**Recurrence:**
```
solve(i):
  if i < 0: return 0
  if i == 0: return nums[0]
  return max(solve(i-1), nums[i] + solve(i-2))
```

**Steps:**
1. If `n == 1`, return `nums[0]`.
2. Run `solve(n-2)` on `nums[0..n-2]` (skip last).
3. Run `solve(n-2)` on `nums[1..n-1]` (skip first).
4. Return max of both.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Many overlapping states. Memoize.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i]` for each linear sub-problem.

**Dry Run:** `nums = [1,2,3,1]`

Sub-problem 1: `[1,2,3]` (skip last)
| Call | i | Compute | Result |
|------|---|---------|--------|
| solve(2) | 2 | max(solve(1), 3+solve(0)) = max(2, 3+1) = 4 | 4 |
| solve(1) | 1 | max(solve(0), 2+solve(-1)) = max(1, 2) = 2 | 2 |
| solve(0) | 0 | 1 | 1 |

Sub-problem 2: `[2,3,1]` (skip first)
| Call | i | Compute | Result |
|------|---|---------|--------|
| solve(2) | 2 | max(solve(1), 1+solve(0)) = max(3, 1+2) = 3 | 3 |
| solve(1) | 1 | max(solve(0), 3+solve(-1)) = max(2, 3) = 3 | 3 |
| solve(0) | 0 | 2 | 2 |

Answer = max(4, 3) = **4**

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Build bottom-up to remove recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[n]` array iteratively for each linear sub-problem.

**Steps (for one linear range):**
1. `dp[0] = nums[start]`
2. `dp[1] = max(nums[start], nums[start+1])`
3. For `i = 2` to `end - start`:
   - `dp[i] = max(dp[i-1], nums[start+i] + dp[i-2])`
4. Return `dp[end - start]`.

**Run twice:** once for `[start=0, end=n-2]`, once for `[start=1, end=n-1]`.

**Dry Run:** `nums = [2,3,2]`

Sub-problem 1: `[2,3]` --> dp[0]=2, dp[1]=max(2,3)=3 --> result = 3
Sub-problem 2: `[3,2]` --> dp[0]=3, dp[1]=max(3,2)=3 --> result = 3
Answer = max(3, 3) = **3**

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Only dp[i-1] and dp[i-2] are needed. Use two variables.

### Approach 4: Space Optimized
**What changed:** Replace dp array with two variables `prev2` and `prev1`.

**Steps (for one linear range):**
1. `prev2 = 0, prev1 = 0`
2. For each house in range:
   - `curr = max(prev1, nums[i] + prev2)`
   - `prev2 = prev1`
   - `prev1 = curr`
3. Return `prev1`.

**Run twice:** skip-last and skip-first. Return max.

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each house branches into rob/skip, giving O(2^n)."
**Memo/Tab:** "n states, each O(1). Run twice: O(n) total."
**Space Optimized:** "Same O(n) time but only 2 variables per run: O(1) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the n == 1 edge case** -- with only one house, the two-range trick gives empty ranges. Handle separately.
2. **Off-by-one in ranges** -- skip-last is `[0, n-2]`, skip-first is `[1, n-1]`. Getting these bounds wrong is the most common bug.
3. **Not initializing prev2 and prev1 correctly** -- start both at 0, then iterate through the range.
4. **Applying circular logic to n == 2** -- with two houses, just take max(nums[0], nums[1]). The two-range trick handles this naturally.

### Edge Cases to Test
- [ ] Single house `[5]` --> 5
- [ ] Two houses `[1,2]` --> 2
- [ ] All same values `[3,3,3,3]` --> 6
- [ ] All zeros `[0,0,0]` --> 0
- [ ] Alternating `[1,100,1,100]` --> 200
- [ ] Decreasing `[4,3,2,1]` --> max(4+2, 3+1) = 6

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Circular means first and last are adjacent? Can I rob zero houses?"
2. **Key insight:** "Circular constraint means house 0 and house n-1 cannot both be robbed. Run linear House Robber twice: skip first or skip last."
3. **Code the space-optimized version** with a helper function for linear robber.
4. **Mention:** "This reduces a circular problem to two linear problems -- a common technique."

### Follow-Up Questions
- "What if houses are in a line?" --> Standard House Robber I (simpler).
- "What if you can rob houses 2 apart (skip exactly 1)?" --> Different recurrence.
- "What about a tree structure?" --> House Robber III (LeetCode #337), use DFS + DP.

---

## CONNECTIONS
- **Prerequisite:** House Robber I / Maximum Sum of Non-Adjacent Elements (P001 in this section)
- **Same Pattern:** Circular DP reduced to two linear DP runs
- **This Unlocks:** House Robber III (trees), Paint House II (circular variant)
- **Harder Variant:** House Robber III on binary tree (LeetCode #337)
