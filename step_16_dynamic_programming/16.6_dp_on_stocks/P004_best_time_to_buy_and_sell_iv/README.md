# Best Time to Buy and Sell Stock IV

> **Step 16.6** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED
> **LeetCode:** [188. Best Time to Buy and Sell Stock IV](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/)

## Problem Statement

You are given an integer `k` and an integer array `prices` where `prices[i]` is the price of a given stock on day `i`.

Find the maximum profit you can achieve. You may complete **at most k transactions** (a transaction = one buy + one sell).

**Rules:**
- You must sell before you can buy again (no simultaneous holdings).
- If `k >= n/2` (where `n = len(prices)`), you can effectively make unlimited transactions because there are at most `n/2` profitable moves.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `k=2, prices=[2,4,1]` | `2` | Buy at 2, sell at 4 → profit 2. Only 1 transaction needed. |
| `k=2, prices=[3,2,6,5,0,3]` | `7` | Buy at 2, sell at 6 (+4). Buy at 0, sell at 3 (+3). Total = 7. |
| `k=1, prices=[1,2]` | `1` | Buy at 1, sell at 2 → profit 1. |
| `k=0, prices=[1,3,5]` | `0` | No transactions allowed. |

## Constraints

- `0 <= k <= 100`
- `0 <= prices.length <= 1000`
- `0 <= prices[i] <= 1000`

---

## Approach 1: Brute Force — Pure Recursion

**Intuition:** Explore all possible decisions at every day. At each day, if you're holding stock you can sell or skip; if you're not holding, you can buy or skip. Try every combination and return the maximum profit. This is the naive exponential solution — correct but too slow for large inputs.

**Steps:**
1. Define `recurse(day, txns_left, holding)` returning max profit from `day` onward.
2. Base case: `day == n` or `txns_left == 0` → return 0.
3. Always consider skipping the current day (`recurse(day+1, txns_left, holding)`).
4. If holding: also try selling today → `prices[day] + recurse(day+1, txns_left-1, False)`. Selling completes a transaction.
5. If not holding: also try buying today → `-prices[day] + recurse(day+1, txns_left, True)`.
6. Return the maximum of all options.

| Metric | Value |
|--------|-------|
| Time   | O(2^n) — two branches at each of n days |
| Space  | O(n) — recursion stack depth |

---

## Approach 2: Optimal — Tabulation DP (Rolling Array)

**Intuition:** Memoize/tabulate the recursive state `(day, txns_left, holding)`. Since transitions only look one day ahead, we can use a rolling array over days, keeping only the "current" and "next" day's DP tables. This converts the exponential recursion into polynomial DP.

**State definition:**
- `dp[t][0]` = max profit with `t` transactions remaining, NOT holding stock
- `dp[t][1]` = max profit with `t` transactions remaining, holding stock

**Transitions (filling from day n-1 to 0):**
- Not holding: `newDp[t][0] = max(dp[t][0],  -prices[day] + dp[t][1])`  (skip or buy)
- Holding:     `newDp[t][1] = max(dp[t][1],   prices[day] + dp[t-1][0])` (skip or sell — sell uses one transaction)

**Steps:**
1. If `k >= n/2`, run the unlimited-transactions greedy (sum all upward moves): O(n).
2. Initialize `dp[t][0] = dp[t][1] = 0` for all t (base: no more days).
3. Iterate `day` from `n-1` down to `0`, build `newDp` from `dp`.
4. Answer is `dp[k][0]` (k transactions left, not holding, at day 0).

| Metric | Value |
|--------|-------|
| Time   | O(n * k) |
| Space  | O(k) — two 1D arrays of size k+1 (rolling) |

---

## Approach 3: Best — Forward-Pass buy/sell Arrays

**Intuition:** Instead of simulating day-by-day backwards, track two arrays `buy[1..k]` and `sell[1..k]` as we sweep prices left to right.

- `buy[t]` = the best "net cash position" right after making the t-th purchase. Starting at -∞ (never bought).
- `sell[t]` = the best profit right after making the t-th sale. Starting at 0.

For each new price, update in order t = 1..k:
- `buy[t]  = max(buy[t],  sell[t-1] - price)` — buy now using profit banked from (t-1) previous sells.
- `sell[t] = max(sell[t], buy[t]   + price)` — sell now with the best t-th buy so far.

The answer is `sell[k]`. Only O(k) space, same O(n*k) time, but with a cleaner forward sweep.

**Special case (k >= n/2):** Collapse to O(n) greedy — add every positive price difference.

**Steps:**
1. Handle edge cases (n=0, k=0).
2. If k >= n//2, use greedy and return.
3. Initialize `buy[t] = -INF`, `sell[t] = 0` for t in 1..k.
4. For each `price` in `prices`:
   - For t = 1 to k: update `buy[t]`, then `sell[t]`.
5. Return `sell[k]`.

| Metric | Value |
|--------|-------|
| Time   | O(n * k) — but with smaller constant than Approach 2 (single pass, no 2D array) |
| Space  | O(k) — two arrays of size k+1 |

---

## Visualization (Example: k=2, prices=[3,2,6,5,0,3])

```
prices:    3    2    6    5    0    3
           |         |         |    |
           |  buy@2  |  sell@6 |  buy@0  sell@3
           |         |         |
           Transaction 1: +4   Transaction 2: +3  ==>  Total: 7
```

**buy/sell array evolution (Best approach, k=2):**
```
Initial: buy=[−∞, −∞], sell=[0, 0]
price=3: buy[1]=max(−∞, 0−3)=−3;   sell[1]=max(0, −3+3)=0
         buy[2]=max(−∞, 0−3)=−3;   sell[2]=max(0, −3+3)=0
price=2: buy[1]=max(−3, 0−2)=−2;   sell[1]=max(0, −2+2)=0
         buy[2]=max(−3, 0−2)=−2;   sell[2]=max(0, −2+2)=0
price=6: buy[1]=max(−2, 0−6)=−2;   sell[1]=max(0, −2+6)=4
         buy[2]=max(−2, 0−6)=−2;   sell[2]=max(0, −2+6)=4
price=5: buy[1]=max(−2, 0−5)=−2;   sell[1]=max(4, −2+5)=4
         buy[2]=max(−2, 4−5)=−1;   sell[2]=max(4, −1+5)=4
price=0: buy[1]=max(−2, 0−0)=0;    sell[1]=max(4,  0+0)=4
         buy[2]=max(−1, 4−0)=4;    sell[2]=max(4,  4+0)=4
price=3: buy[1]=max(0,  0−3)=0;    sell[1]=max(4,  0+3)=4
         buy[2]=max(4,  4−3)=4;    sell[2]=max(4,  4+3)=7
Answer: sell[2] = 7  ✓
```

---

## Real-World Use Case

**Algorithmic Trading with Limited Capital Deployment Events**

In quantitative finance, a fund manager may be restricted to a maximum of `k` round-trip trades per quarter (buy + sell = 1 round trip) due to regulatory constraints, transaction cost budgets, or risk management rules. This exact problem models finding the optimal buy/sell schedule under that constraint — maximizing P&L with at most `k` trades.

The forward-sweep `buy[]/sell[]` formulation maps directly to how trading systems maintain rolling "best entry price" and "best realized profit" states across a stream of market data.

---

## Interview Tips

- Always ask about the special case `k >= n/2` — this collapses to the unlimited-transactions problem (Stock II) solvable greedily in O(n). Mentioning this shows depth.
- The key insight is what counts as a "transaction": one BUY + one SELL. Decide whether to decrement the counter on buy or sell (either works, but stay consistent). This solution decrements on SELL.
- Connect to simpler variants: Stock I (k=1), Stock II (unlimited), Stock III (k=2). Stock IV is the generalization — know how each reduces to the next.
- The `buy[t] = sell[t-1] - price` formulation is elegant: it means "use all profit from `t-1` prior sells to fund this buy." Explaining this recurrence clearly impresses interviewers.
- If asked to optimize further: when `k >= n`, use greedy. When `k` is small, the O(n*k) DP is optimal. There's no known sub-quadratic algorithm for the general case.
- Watch for overflow: `buy` arrays initialized to `Integer.MIN_VALUE` — guard against adding a price to `MIN_VALUE` (use a null check or sentinel).
