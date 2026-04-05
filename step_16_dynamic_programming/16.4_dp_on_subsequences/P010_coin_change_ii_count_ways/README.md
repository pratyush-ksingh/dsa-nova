# Coin Change II - Count Ways (LeetCode 518)

> **Step 16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `coins` representing different denominations and an integer `amount`, return the **number of combinations** that make up that amount. You may use each coin denomination an unlimited number of times. The order of coins does not matter — (1+2) and (2+1) are the same combination.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| coins=[1,2,5], amount=5 | 4 | {5}, {2,2,1}, {2,1,1,1}, {1,1,1,1,1} |
| coins=[2], amount=3 | 0 | Cannot make 3 with only 2s |
| coins=[10], amount=10 | 1 | Only one way: {10} |
| coins=[1,2,5], amount=0 | 1 | One way: take no coins |

### Constraints
- `1 <= coins.length <= 300`
- `1 <= coins[i] <= 5000`
- All values in `coins` are distinct
- `0 <= amount <= 5000`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** Count all combinations (not permutations) to make `amount`. Process coins one at a time: either skip current denomination or take one and stay at same index (enabling repeated use). By fixing the order in which we consider coins, we avoid counting the same combination twice.

**Steps:**
1. `solve(idx, remaining)`: count ways using denominations 0..idx
2. Base: remaining == 0 → return 1 (found a valid combination)
3. Base (idx == 0): return 1 if `remaining % coins[0] == 0`, else 0
4. `not_take = solve(idx-1, remaining)` — skip denomination idx
5. `take = solve(idx, remaining - coins[idx])` if coin fits — stay at idx
6. Return `not_take + take`

```
Dry-run: coins=[1,2,5], amount=5
solve(2,5) = solve(1,5) + solve(2,0)=1
  solve(1,5) = solve(0,5) + solve(1,3)
    solve(0,5) = 1 (5 ones)
    solve(1,3) = solve(0,3) + solve(1,1)
      solve(0,3) = 1 (3 ones)
      solve(1,1) = solve(0,1) + solve(1,-1 skip) = 1
    solve(1,3) = 1+1 = 2
  solve(1,5) = 1+2 = 3
solve(2,5) = 3+1 = 4 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(2^(amount/min_coin)) |
| Space  | O(amount) |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** `dp[i][a]` = number of ways to make amount `a` using coins 0..i. Fill bottom-up. For take: reference `dp[i][a - coins[i]]` (same row = reuse same coin).

**Steps:**
1. Base: `dp[i][0] = 1` for all i (one way to make 0: pick nothing)
2. Base row (i=0): `dp[0][a] = 1` if `a % coins[0] == 0`, else 0
3. For i from 1 to n-1, for a from 0 to amount:
   - `not_take = dp[i-1][a]`
   - `take = dp[i][a - coins[i]]` if `coins[i] <= a`
   - `dp[i][a] = not_take + take`
4. Return `dp[n-1][amount]`

| Metric | Value |
|--------|-------|
| Time   | O(n * amount) |
| Space  | O(n * amount) |

---

### Approach 3: Best -- 1D Space-Optimized DP

**Intuition:** `dp[a]` = number of ways to make amount `a`. Initialize `dp[0] = 1`. Left-to-right sweep enables reuse; outer loop over coins ensures combinations (not permutations — if we looped amount outside and coins inside, we'd count permutations).

**Steps:**
1. `dp = [0] * (amount + 1)`, `dp[0] = 1`
2. For each coin c in coins (outer loop = coin):
   - For a from c to amount (left to right):
     - `dp[a] += dp[a - c]`
3. Return `dp[amount]`

**Why coin as outer loop?** Fixing coins in order means once we're past coin c, we never reintroduce it. This prevents counting (coin1+coin2) and (coin2+coin1) as separate, ensuring combinations only.

| Metric | Value |
|--------|-------|
| Time   | O(n * amount) |
| Space  | O(amount) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| amount = 0 | Return 1 | dp[0] = 1 is the key base case |
| Impossible | Return 0 | dp[amount] stays 0 |
| Single coin | 1 if divides evenly, 0 otherwise | Works naturally |

**Common Mistakes:**
- Swapping outer/inner loop: looping amount outside and coins inside gives **permutations** not combinations
  - `for a in range(amount): for coin in coins:` → counts [1,2] and [2,1] separately
  - `for coin in coins: for a in range(coin, amount+1):` → combinations only (correct)
- Initializing dp[0] = 0 instead of 1 — this kills everything downstream
- Confusing this with Coin Change (minimum): here we ADD counts; there we MINIMIZE

---

## Real-World Use Case
**Financial transactions:** How many ways can you make change for a certain amount using available bill denominations? This is used in point-of-sale systems to inform cashiers of possible change combinations, ensuring customer satisfaction without requiring exact change.

## Interview Tips
- The loop order (coin outer, amount inner) is the most asked follow-up: "why not swap them?"
- Know the exact answer: swapping gives permutations — demonstrate with coins=[1,2], amount=3: correct=2, permutation=3
- This is the "count" variant of coin change; the "min" variant uses `min()` instead of `+`
- The `dp[0] = 1` initialization means "there is exactly 1 way to make 0: take nothing"
- Large amounts can overflow in languages without big integers — mention this in interviews
