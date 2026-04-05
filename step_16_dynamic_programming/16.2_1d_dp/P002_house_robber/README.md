# House Robber

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement (LeetCode #198)
You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed. The only constraint is that **adjacent houses have security systems connected** -- if two adjacent houses are broken into on the same night, the police are automatically alerted.

Given an integer array `nums` where `nums[i]` is the money at house `i`, return the **maximum amount of money** you can rob tonight **without alerting the police**.

### Connection to Batch 1
**This is EXACTLY the same problem as "Max Sum of Non-Adjacent Elements" from Batch 1!** The house robber story is just a skin. The core: pick elements from an array to maximize sum, no two elements can be adjacent. If you solved that one, you already know this.

### Examples

| # | Input | Output | Explanation |
|---|-------|--------|-------------|
| 1 | `[1, 2, 3, 1]` | `4` | Rob house 0 ($1) + house 2 ($3) = $4 |
| 2 | `[2, 7, 9, 3, 1]` | `12` | Rob house 0 ($2) + house 2 ($9) + house 4 ($1) = $12 |
| 3 | `[2, 1, 1, 2]` | `4` | Rob house 0 ($2) + house 3 ($2) = $4 |

### Constraints
- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 400`

### Real-Life Analogy
You're at a buffet and want to load your plate, but every dish is spicy-hot right out of the oven. After grabbing one dish, you need to **skip the next one** (your hands need to cool down) before grabbing another. Which dishes do you pick to maximize the total food on your plate?

### 3 Key Observations

1. **"Aha!" -- Binary choice at each house:** For each house, you either rob it (and skip the next) or don't rob it (and move to the next). This gives a clean recursive structure.

2. **"Aha!" -- Only the last decision matters:** Whether you rob house `i` depends ONLY on the best result up to house `i-1` (didn't rob) vs. house `i-2` (did rob). No need to remember the full history.

3. **"Aha!" -- Only two variables needed:** Since `dp[i]` depends only on `dp[i-1]` and `dp[i-2]`, we can compress the entire DP table into just two variables.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Recursion | Call stack | Try rob/skip at each house | Explores all valid combos |
| Memoization | 1D array | Top-down + cache | Eliminates recomputation |
| Tabulation | 1D array `dp[i]` | Bottom-up fill | Clean iterative solution |
| Space-Optimized | Two variables | Rolling computation | O(1) space, same logic |

**Pattern cue:** "Can't pick adjacent elements, maximize sum" --> 1D DP with `dp[i] = max(dp[i-1], dp[i-2] + val[i])`.

---

## APPROACH LADDER

### Approach 1: Recursion (Brute Force)

**State definition:** `rob(i)` = maximum money robbing from house `i` to the last house.

**Recurrence:**
```
rob(i) =
    if i >= n: return 0

    skip  = rob(i + 1)                // don't rob house i
    steal = nums[i] + rob(i + 2)      // rob house i, skip i+1

    return max(skip, steal)
```

**Dry-run trace** for `[2, 7, 9, 3, 1]`:
```
rob(0)
  skip: rob(1)
    skip: rob(2)
      skip: rob(3)
        skip: rob(4) = max(1+rob(6), rob(5)) = max(1, 0) = 1
        steal: 3 + rob(5) = 3
        = max(1, 3) = 3
      steal: 9 + rob(4) = 9 + 1 = 10
      = max(3, 10) = 10
    steal: 7 + rob(3) = 7 + 3 = 10
    = max(10, 10) = 10
  steal: 2 + rob(2) = 2 + 10 = 12
  = max(10, 12) = 12  <-- answer
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(2^n) | Two choices per house, tree branches |
| Space | O(n) | Recursion stack depth |

---

### Approach 2: Memoization (Top-Down DP)

**Key insight:** `rob(i)` is called multiple times with the same `i`. There are only `n` unique states.

**State:** `dp[i]` = max money from house `i` onward.

```
dp[i]:
    if cached, return dp[i]
    dp[i] = max(rob(i+1), nums[i] + rob(i+2))
    return dp[i]
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n) | Each of n states computed once |
| Space | O(n) | Memo array + recursion stack |

---

### Approach 3: Tabulation (Bottom-Up DP)

**State:** `dp[i]` = maximum money considering houses `0..i`.

**Recurrence:**
```
dp[0] = nums[0]
dp[1] = max(nums[0], nums[1])
dp[i] = max(dp[i-1], dp[i-2] + nums[i])    for i >= 2
```

**Meaning:**
- `dp[i-1]`: best if we skip house `i` (carry forward previous best)
- `dp[i-2] + nums[i]`: best if we rob house `i` (add to best that doesn't include house `i-1`)

**Dry-run trace** for `[2, 7, 9, 3, 1]`:
```
Index:   0    1    2    3    4
nums:  [ 2    7    9    3    1 ]

dp[0] = 2
dp[1] = max(2, 7) = 7
dp[2] = max(dp[1], dp[0] + 9) = max(7, 2+9) = 11
dp[3] = max(dp[2], dp[1] + 3) = max(11, 7+3) = 11
dp[4] = max(dp[3], dp[2] + 1) = max(11, 11+1) = 12

Answer = dp[4] = 12
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n) | Single pass |
| Space | O(n) | dp array |

---

### Approach 4: Space-Optimized (Two Variables)

**Key insight:** `dp[i]` only needs `dp[i-1]` and `dp[i-2]`. Replace the array with two variables.

```
prev2 = nums[0]               // dp[i-2]
prev1 = max(nums[0], nums[1]) // dp[i-1]

for i from 2 to n-1:
    curr = max(prev1, prev2 + nums[i])
    prev2 = prev1
    prev1 = curr

return prev1
```

**Dry-run trace** for `[2, 7, 9, 3, 1]`:
```
prev2=2, prev1=7

i=2: curr = max(7, 2+9) = 11.  prev2=7, prev1=11
i=3: curr = max(11, 7+3) = 11. prev2=11, prev1=11
i=4: curr = max(11, 11+1) = 12. prev2=11, prev1=12

Answer = prev1 = 12
```

| Metric | Value | Why |
|--------|-------|-----|
| Time | O(n) | Single pass |
| Space | O(1) | Just two variables |

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Analogy |
|----------|------|-------|---------|
| Recursion | O(2^n) | O(n) | Trying every valid selection of houses |
| Memoization | O(n) | O(n) | Writing answers on sticky notes at each house |
| Tabulation | O(n) | O(n) | Walking the street once, computing best-so-far |
| Space-Optimized | O(n) | O(1) | Only remembering the last two houses' answers |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Watch Out |
|-----------|----------|-----------|
| Single house `[5]` | `5` | Just rob it |
| Two houses `[1, 2]` | `2` | Pick the bigger one |
| All zeros `[0, 0, 0]` | `0` | Valid -- nothing to rob |
| Equal values `[5, 5, 5, 5]` | `10` | Rob houses 0 and 2 (or 1 and 3) |
| Increasing `[1, 2, 3, 4, 5]` | `9` | Houses 0, 2, 4: 1+3+5=9 |

**Common mistakes:**
- Forgetting the base case for `n == 1` (only one house)
- Setting `dp[1] = nums[1]` instead of `max(nums[0], nums[1])`
- Confusing "rob from i onward" vs "rob from 0..i" formulations (both work, just be consistent)
- Not seeing the connection to "Max Sum Non-Adjacent Elements" -- they are identical problems

---

## INTERVIEW LENS

**What interviewers are looking for:**
1. Recognize the 1D DP pattern immediately
2. Define state and recurrence clearly
3. Optimize from O(n) space to O(1)

**Follow-up questions:**
- "What if the houses are in a circle?" --> **House Robber II** (LC #213): Run the algorithm twice, once excluding house 0 and once excluding house n-1
- "What about a binary tree of houses?" --> **House Robber III** (LC #337): Tree DP with (rob_this, skip_this) pairs
- "Return which houses to rob, not just the max" --> Backtrack through dp array

**Time management:** This is a warm-up DP. Code the space-optimized version directly -- it's only 10 lines.

---

## CONNECTIONS

| Related Problem | Connection |
|----------------|------------|
| Max Sum Non-Adjacent Elements | **Identical problem** with different story |
| House Robber II (LC #213) | Circular constraint: two passes |
| House Robber III (LC #337) | Tree DP variant |
| Delete and Earn (LC #740) | Reduce to House Robber after frequency counting |
| Climbing Stairs (LC #70) | Same recurrence `f(n) = f(n-1) + f(n-2)`, different semantics |
| Paint House (LC #256) | Generalized: k choices per position instead of 2 |
