# Coin Change - Minimum Coins (LeetCode 322)

> **Step 16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `coins` representing denominations and an integer `amount`, return the **minimum number of coins** needed to make up exactly `amount`. If it is impossible to achieve that amount, return `-1`. You have an unlimited supply of each denomination.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| coins=[1,5,6,9], amount=11 | 2 | 5+6=11, only 2 coins |
| coins=[1,2,5], amount=11 | 3 | 5+5+1=11, 3 coins |
| coins=[2], amount=3 | -1 | Cannot make 3 with only 2s |
| coins=[1,2,5], amount=0 | 0 | No coins needed |

### Constraints
- `1 <= coins.length <= 12`
- `1 <= coins[i] <= 2^31 - 1`
- `0 <= amount <= 10^4`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** For each coin, either skip it (go to previous index) or take it (reduce amount by coin value, stay at same index for reuse). Minimize total coins taken. Return infinity for impossible paths, convert to -1.

**Steps:**
1. `solve(idx, remaining)`: min coins using denominations 0..idx
2. Base: remaining == 0 → return 0
3. Base (idx == 0): if `remaining % coins[0] == 0` return count, else return INF
4. `not_take = solve(idx-1, remaining)` — skip this denomination
5. `take = 1 + solve(idx, remaining - coins[idx])` if coin fits — stay at idx
6. Return `min(not_take, take)`; final answer: INF → -1

| Metric | Value |
|--------|-------|
| Time   | O(amount^n) -- exponential |
| Space  | O(amount) -- recursion depth |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** `dp[i][a]` = min coins to make amount `a` using denominations 0..i. Fill bottom-up. For take: stay on row i (reuse same denomination).

**Steps:**
1. Initialize all entries to INF; set `dp[i][0] = 0` for all i
2. Base row (i=0): `dp[0][a] = a / coins[0]` if `a % coins[0] == 0`, else INF
3. For i from 1 to n-1, for a from 0 to amount:
   - `not_take = dp[i-1][a]`
   - `take = 1 + dp[i][a - coins[i]]` if `coins[i] <= a` and not INF
   - `dp[i][a] = min(not_take, take)`
4. Return `dp[n-1][amount]` or -1 if INF

| Metric | Value |
|--------|-------|
| Time   | O(n * amount) |
| Space  | O(n * amount) |

---

### Approach 3: Best -- 1D Space-Optimized DP

**Intuition:** `dp[a]` = min coins to make amount `a`. Initialize `dp[0] = 0`, all others INF. Process each coin with left-to-right sweep enabling reuse.

**Steps:**
1. `dp = [INF] * (amount + 1)`, `dp[0] = 0`
2. For each coin c in coins:
   - For a from c to amount (left to right):
     - If `dp[a - c] != INF`: `dp[a] = min(dp[a], 1 + dp[a - c])`
3. Return `dp[amount]` if not INF, else -1

```
Dry-run: coins=[1,5,6,9], amount=11
After coin=1: dp=[0,1,2,3,4,5,6,7,8,9,10,11]
After coin=5: dp=[0,1,2,3,4,1,2,3,4,5, 2, 3] (11: 1+dp[6]=1+2=3)
After coin=6: dp=[0,1,2,3,4,1,1,2,3,4, 2, 2] (11: 1+dp[5]=1+1=2) ✓
After coin=9: dp=[0,1,2,3,4,1,1,2,3,1, 2, 2]
Result: dp[11] = 2
```

| Metric | Value |
|--------|-------|
| Time   | O(n * amount) |
| Space  | O(amount) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| amount = 0 | Return 0 | Base case dp[0] = 0 |
| All coins > amount | Return -1 | All dp[a] remain INF |
| Single coin divides amount | Return amount/coin | Works naturally |
| Coin equals amount | 1 coin | dp[amount] updated to 1 |

**Common Mistakes:**
- Returning INF instead of -1 (forgetting the final conversion)
- Using `INT_MAX` as infinity and doing `INF + 1` causing overflow — use `INT_MAX / 2` or a flag
- Right-to-left iteration (would limit each denomination to one use — 0/1 behavior)
- Not initializing dp[0] = 0 before the main loop

---

## Real-World Use Case
**ATM cash dispensing:** An ATM has bills of denominations {100, 50, 20, 10, 5, 1} and must dispense an exact amount with the fewest bills. Greedy works for standard denomination sets but not all arbitrary sets — DP is the general solution used in actual banking systems for non-standard currencies.

## Interview Tips
- Classic "greedy fails" example: coins=[1,3,4], amount=6. Greedy (largest first): 4+1+1=3. DP: 3+3=2
- Know both coin change variants: this (minimize count) vs Coin Change II (count combinations)
- The recurrence is identical to unbounded knapsack — just minimizing instead of maximizing
- Follow-up: reconstruct which coins were used — trace back where `dp[a] = 1 + dp[a - coin]` held
- BFS approach also works and gives O(amount * n) similarly, treating amount as graph distance
