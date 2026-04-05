# Best Time to Buy and Sell Stock with Cooldown

> **Step 16.16.6** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #309)** You are given an array `prices` where `prices[i]` is the price of a given stock on the `i`-th day. Find the maximum profit you can achieve. You may complete as many transactions as you like with the following restriction: After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [1,2,3,0,2] | 3 | Buy@1, sell@3, cooldown, buy@0, sell@2 -> profit=2+2=3 (actually buy@1 sell@2... let's trace: buy day0, sell day2: profit=2, cool day3, buy day3, sell day4: profit=2, total=4... Actually: buy@0(1), sell@1(2)=1, cool, buy@3(0), sell@4(2)=2, total=3) |
| [1] | 0 | Single day, no transaction possible |
| [5,4,3,2,1] | 0 | Decreasing prices, no profitable transaction |

### Constraints
- `1 <= prices.length <= 5000`
- `0 <= prices[i] <= 1000`

### State Machine Model
```
         buy
  COOL -------> HOLD
   ^              |
   |   cooldown   | sell
   |              v
   +---------- SOLD
```

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion with State

**Intuition:** At each day, decide based on current state. If we can buy: buy or skip. If we hold: sell (with cooldown skip) or skip. Try all combinations recursively.

**Steps:**
1. `solve(i, canBuy)`: max profit from day `i` onward
2. If `canBuy`: max(buy and recurse, skip)
3. If holding: max(sell and skip to i+2, skip to i+1)
4. Base: `i >= n` returns 0

| Metric | Value |
|--------|-------|
| Time   | O(2^n) -- two choices per day |
| Space  | O(n) -- recursion depth |

---

### Approach 2: Optimal -- DP with Three States

**Intuition:** Define three arrays representing the three states of the state machine on each day:
- `buy[i]`: max profit on day i if we hold a stock (bought on or before day i)
- `sell[i]`: max profit on day i if we just sold
- `cool[i]`: max profit on day i if we are in cooldown or idle

**Steps:**
1. Base: `buy[0] = -prices[0]`, `sell[0] = 0`, `cool[0] = 0`
2. Transitions:
   - `buy[i] = max(buy[i-1], cool[i-1] - prices[i])` -- hold or buy from cooldown
   - `sell[i] = buy[i-1] + prices[i]` -- sell what we hold
   - `cool[i] = max(cool[i-1], sell[i-1])` -- stay cool or transition from sold
3. Answer: `max(sell[n-1], cool[n-1])`

```
Dry-run: [1,2,3,0,2]

Day 0: buy=-1, sell=0,  cool=0
Day 1: buy=-1, sell=1,  cool=0
Day 2: buy=-1, sell=2,  cool=1
Day 3: buy=1,  sell=-1, cool=2
Day 4: buy=1,  sell=3,  cool=2

Answer = max(3, 2) = 3
```

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Space-Optimized 3 Variables

**Intuition:** Since each day only depends on the previous day's values, replace the arrays with three variables: `prevBuy`, `prevSell`, `prevCool`.

**Steps:**
1. Initialize: `prevBuy = -prices[0]`, `prevSell = 0`, `prevCool = 0`
2. For each day, compute new values simultaneously
3. Update prev variables
4. Return `max(prevSell, prevCool)`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Single day | Return 0 | Cannot complete a transaction |
| Two days | Buy day 0, sell day 1 if profitable | No cooldown issue |
| Decreasing prices | Return 0 | No profitable transaction |
| All same prices | Return 0 | No profit possible |

**Common Mistakes:**
- Forgetting the cooldown: after selling on day i, next buy is day i+2 (not i+1)
- Not computing all three state values simultaneously (using updated values for same-day computation)
- Confusing this with the "k transactions" variant

---

## Real-World Use Case
**Algorithmic trading with settlement periods:** In real stock markets, there is often a T+1 or T+2 settlement period where proceeds from a sale are not immediately available. This problem models a simplified version where you must wait one day after selling before buying again.

## Interview Tips
- Draw the state machine diagram -- it makes the transitions crystal clear
- The three states (buy/sell/cooldown) are the key insight
- Show that the brute force has overlapping subproblems (same (i, canBuy) called multiple times)
- Space optimization from O(n) to O(1) is straightforward since only previous day matters
- Compare with LC 122 (no cooldown) and LC 714 (with fee) to show the pattern
