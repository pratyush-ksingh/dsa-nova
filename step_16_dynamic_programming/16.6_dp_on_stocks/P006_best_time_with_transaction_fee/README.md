# Best Time to Buy and Sell Stock with Transaction Fee

> **Step 16.16.6** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #714)** You are given an array `prices` where `prices[i]` is the price of a given stock on the `i`-th day, and an integer `fee` representing a transaction fee. Find the maximum profit you can achieve. You may complete as many transactions as you like, but you need to pay the transaction fee for each transaction. (A transaction = one buy + one sell.)

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| prices=[1,3,2,8,4,9], fee=2 | 8 | Buy@1 sell@8 (profit=5), buy@4 sell@9 (profit=3), total=8 |
| prices=[1,3,7,5,10,3], fee=3 | 6 | Buy@1 sell@7 (profit=3), buy@5 sell@10 (profit=2)... Actually buy@1 sell@10-3=6 |
| prices=[1], fee=1 | 0 | Single day |

### Constraints
- `1 <= prices.length <= 5 * 10^4`
- `1 <= prices[i] <= 5 * 10^4`
- `0 <= fee <= 5 * 10^4`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion with State

**Intuition:** At each day, based on whether we currently hold a stock, decide to buy/sell or skip. The fee is deducted when selling.

**Steps:**
1. `solve(i, canBuy)`: max profit from day `i` onward
2. If `canBuy`: max(-prices[i] + solve(i+1, false), solve(i+1, true))
3. If holding: max(prices[i] - fee + solve(i+1, true), solve(i+1, false))
4. Base: `i >= n` returns 0

| Metric | Value |
|--------|-------|
| Time   | O(2^n) |
| Space  | O(n) |

---

### Approach 2: Optimal -- DP with Buy/Sell States

**Intuition:** Two states per day:
- `buy[i]`: max profit on day i while holding a stock
- `sell[i]`: max profit on day i while not holding a stock

Transitions:
- `buy[i] = max(buy[i-1], sell[i-1] - prices[i])` -- keep holding or buy today
- `sell[i] = max(sell[i-1], buy[i-1] + prices[i] - fee)` -- keep idle or sell today

**Steps:**
1. Base: `buy[0] = -prices[0]`, `sell[0] = 0`
2. Fill arrays using transitions
3. Return `sell[n-1]`

```
Dry-run: prices=[1,3,2,8,4,9], fee=2

Day 0: buy=-1, sell=0
Day 1: buy=max(-1, 0-3)=-1, sell=max(0, -1+3-2)=0
Day 2: buy=max(-1, 0-2)=-1, sell=max(0, -1+2-2)=0
Day 3: buy=max(-1, 0-8)=-1, sell=max(0, -1+8-2)=5
Day 4: buy=max(-1, 5-4)=1, sell=max(5, -1+4-2)=5
Day 5: buy=max(1, 5-9)=1, sell=max(5, 1+9-2)=8

Answer = 8
```

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

### Approach 3: Best -- Space-Optimized Two Variables

**Intuition:** Since each day only depends on the previous day, replace arrays with two variables: `hold` (holding stock) and `cash` (not holding stock).

**Steps:**
1. `hold = -prices[0]`, `cash = 0`
2. For each day: update both simultaneously
   - `newHold = max(hold, cash - prices[i])`
   - `newCash = max(cash, hold + prices[i] - fee)`
3. Return `cash`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| Single day | Return 0 | No transaction possible |
| Fee larger than any price difference | Return 0 | No profitable transaction |
| Decreasing prices | Return 0 | Never buy |
| Fee = 0 | Same as unlimited transactions (LC 122) | Degenerate case |

**Common Mistakes:**
- Applying the fee on both buy and sell (should be once per transaction)
- Not updating hold and cash simultaneously (using already-updated value)
- Confusing with the cooldown variant (no cooldown here, just fee)

---

## Real-World Use Case
**Brokerage fee optimization:** In actual stock trading, brokers charge a commission per trade. This algorithm helps determine the optimal trading strategy that maximizes returns after accounting for these fees, preventing over-trading on small price fluctuations.

## Interview Tips
- This is a simpler variant of the stock series -- only two states (hold/cash)
- The fee simply gets subtracted when selling: `prices[i] - fee`
- Compare with LC 122 (no fee, greedy works), LC 309 (cooldown, 3 states), LC 188 (k transactions)
- The O(1) space optimization is trivial once you see each day depends only on the previous day
- A greedy interpretation: only sell when profit exceeds fee, which is what the DP naturally computes
