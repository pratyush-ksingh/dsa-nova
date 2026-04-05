# Climbing Stairs

> **Batch 1 of 12** | **Topic:** Dynamic Programming | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
You are climbing a staircase. It takes `n` steps to reach the top. Each time you can either climb **1 step** or **2 steps**. In how many **distinct ways** can you climb to the top?

*(LeetCode #70)*

**Constraints:**
- `1 <= n <= 45`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `n = 1` | `1` | 1 way: {1} |
| `n = 2` | `2` | 2 ways: {1+1, 2} |
| `n = 3` | `3` | 3 ways: {1+1+1, 1+2, 2+1} |
| `n = 5` | `8` | 8 distinct ways |

### Real-Life Analogy
> *You are standing at the bottom of a staircase. At each step you decide: take a small step (1 stair) or a big step (2 stairs). To figure out how many ways to reach step 5, notice that you must have just come from step 4 (took 1 step) or step 3 (took 2 steps). So the number of ways to reach step 5 = ways to reach step 4 + ways to reach step 3. This is exactly the Fibonacci recurrence! The staircase naturally decomposes into smaller identical subproblems.*

### Key Observations
1. To reach step `n`, you either came from step `n-1` (1-step) or step `n-2` (2-step). So `ways(n) = ways(n-1) + ways(n-2)`.
2. This is literally the Fibonacci sequence shifted by 1: `ways(1)=1, ways(2)=2, ways(3)=3, ways(4)=5, ways(5)=8...`
3. The 4-stage DP progression applies perfectly: Recursion -> Memo -> Tab -> Space. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** `ways(5)` needs `ways(4)` and `ways(3)`. `ways(4)` also needs `ways(3)`. Redundant calls explode exponentially.
- **Optimal substructure:** The answer for `n` is composed of answers for `n-1` and `n-2`.
- This is the simplest "real" DP problem -- the Fibonacci pattern in disguise.

### Pattern Recognition
- **Pattern:** Linear 1D DP (each state depends on previous 2 states)
- **Classification Cue:** "When you see _how many ways to reach position N_ with _choices that move you forward by fixed amounts_ --> think _1D DP / Fibonacci variant_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** `ways(n) = ways(n-1) + ways(n-2)`. Base cases: `ways(1) = 1`, `ways(2) = 2`.

**Steps:**
1. If `n == 1`, return 1.
2. If `n == 2`, return 2.
3. Return `climbStairs(n-1) + climbStairs(n-2)`.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Same subproblems recomputed exponentially. Add a cache.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Store computed results in `dp[]` to avoid recomputation.

**Steps:**
1. Create `dp[n+1]` initialized to -1.
2. `solve(n)`: if `dp[n] != -1`, return cached value. Otherwise compute and store.

**Dry Run:** `n = 5`

| Call | n | Cached? | Result | dp[] |
|------|---|---------|--------|------|
| 1 | 5 | No | solve(4)+solve(3) | |
| 2 | 4 | No | solve(3)+solve(2) | |
| 3 | 3 | No | solve(2)+solve(1) | |
| 4 | 2 | Base | 2 | dp[2]=2 |
| 5 | 1 | Base | 1 | dp[1]=1 |
| back | 3 | Store | 2+1=3 | dp[3]=3 |
| back | 4 | Store | 3+2=5 | dp[4]=5 |
| 6 | 3 | **Yes** | 3 | |
| back | 5 | Store | 5+3=8 | dp[5]=8 |

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Eliminate recursion overhead with bottom-up iteration.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build dp array iteratively from base cases.

**Steps:**
1. `dp[1] = 1`, `dp[2] = 2`.
2. For `i = 3` to `n`: `dp[i] = dp[i-1] + dp[i-2]`.
3. Return `dp[n]`.

**Dry Run:** `n = 5`

| i | dp[i-2] | dp[i-1] | dp[i] |
|---|---------|---------|-------|
| 1 | - | - | 1 |
| 2 | - | - | 2 |
| 3 | 1 | 2 | 3 |
| 4 | 2 | 3 | 5 |
| 5 | 3 | 5 | 8 |

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** We only use `dp[i-1]` and `dp[i-2]`. Drop the array.

### Approach 4: Space Optimized
**What changed:** Two variables instead of an array.

**Steps:**
1. `prev2 = 1` (ways to reach step 1), `prev1 = 2` (ways to reach step 2).
2. For `i = 3` to `n`:
   - `curr = prev1 + prev2`
   - `prev2 = prev1`
   - `prev1 = curr`
3. Return `prev1` (handle `n==1` separately).

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "The call tree branches in two at every level -- about 2^n total calls."
**Memo/Tab/Space:** "We compute each of the n states exactly once with O(1) work per state. Space depends on whether we use an array (O(n)) or just two variables (O(1))."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using `dp[0] = 1` as base case. While mathematically valid (0 stairs = 1 way = "do nothing"), it can confuse. Safer to use `dp[1] = 1, dp[2] = 2`.
2. Forgetting to handle `n = 1` in the space-optimized version (when `prev1` is initialized to `ways(2) = 2`).
3. Integer overflow for very large `n` -- not an issue with n <= 45 for int.

### Edge Cases to Test
- [ ] `n = 1` --> 1
- [ ] `n = 2` --> 2
- [ ] `n = 3` --> 3
- [ ] `n = 45` --> 1836311903

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Can I take 0 steps? Only 1 or 2? Is n always >= 1?"
2. **Observe:** "To reach step n, I must have come from n-1 or n-2. This gives the recurrence `f(n) = f(n-1) + f(n-2)` -- it's Fibonacci!"
3. **Progression:** Start with recursion, show it's O(2^n), optimize to memo, then tab, then O(1) space.
4. **Code:** Write the space-optimized version as the final answer.

### Follow-Up Questions
- "What if you can take 1, 2, or 3 steps?" --> `f(n) = f(n-1) + f(n-2) + f(n-3)`. Space: keep 3 variables.
- "What if the step sizes are given as an array?" --> Generalized coin-change variant: `f(n) = sum(f(n-step) for step in steps)`.
- "Can you do O(log n)?" --> Matrix exponentiation on the Fibonacci recurrence.

---

## CONNECTIONS
- **Prerequisite:** Introduction to DP (P001 -- Fibonacci)
- **Same Pattern:** Fibonacci, Frog Jump (P003), House Robber
- **This Unlocks:** Frog Jump (add costs), Min Cost Climbing Stairs, all 1D DP
- **Harder Variant:** K-step climbing, Min Cost Climbing Stairs (LeetCode #746)
