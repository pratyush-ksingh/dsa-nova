# Best Time to Buy and Sell Stock II

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are given an integer array `prices` where `prices[i]` is the price of a stock on day `i`. You may complete **as many transactions** as you like (buy one and sell one share of the stock multiple times). You must sell before you buy again (cannot hold multiple shares). Find the **maximum profit** you can achieve.

*(LeetCode #122)*

**Constraints:**
- `1 <= prices.length <= 3 * 10^4`
- `0 <= prices[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `prices = [7,1,5,3,6,4]` | `7` | Buy day 2 (1), sell day 3 (5) = +4. Buy day 4 (3), sell day 5 (6) = +3. Total = 7 |
| `prices = [1,2,3,4,5]` | `4` | Buy day 1 (1), sell day 5 (5) = +4. Same as collecting all upswings: 1+1+1+1 = 4 |
| `prices = [7,6,4,3,1]` | `0` | Prices only decrease, no profitable transaction |

### Real-Life Analogy
> *You are a day trader with perfect hindsight. You can see the entire price history and make unlimited trades. Your strategy is simple: buy before every upswing and sell at the peak. Every time the price goes up tomorrow compared to today, you pocket that difference. It is like collecting every single "step up" on a staircase -- you do not need to find the single biggest climb, you collect ALL the small climbs.*

### Key Observations
1. **Unlimited transactions = collect all upswings:** The greedy insight is that max profit = sum of all positive differences `prices[i] - prices[i-1]` where the difference is positive. <-- This is the "aha" insight
2. **DP state needs "holding" flag:** `dp[i][buy]` where `buy=1` means we can buy today, `buy=0` means we are holding and can sell. At each day, we either transact or skip.
3. **Greedy is equivalent to DP:** The greedy approach of collecting all positive differences gives the same answer as the DP approach. Both are O(n), but DP generalizes to limited transactions. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** The recursive approach recomputes `(day, canBuy)` states multiple times.
- **Optimal substructure:** Max profit from day `i` onward depends on optimal decisions from day `i+1` onward.
- The DP approach generalizes easily to "at most K transactions" -- a huge interview advantage.

### Pattern Recognition
- **Pattern:** DP on stocks with state = (day, holding/not-holding)
- **Classification Cue:** "When you see _buy/sell stock with unlimited transactions_ --> think _dp[i][buy] with two choices per state_ OR _greedy: sum all positive diffs_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** At each day, if we can buy, we choose to buy (pay price) or skip. If we are holding, we choose to sell (gain price) or skip. Recurse for all days.

**State Definition:** `solve(i, canBuy)` = max profit from day `i` to end, where `canBuy=1` means we can buy, `canBuy=0` means we must sell first.

**Recurrence:**
```
solve(i, canBuy):
  if i == n: return 0  (no more days)
  if canBuy:
    buy  = -prices[i] + solve(i+1, 0)   // buy today, next state: holding
    skip = solve(i+1, 1)                  // skip, still can buy
    return max(buy, skip)
  else:
    sell = prices[i] + solve(i+1, 1)     // sell today, next state: can buy
    skip = solve(i+1, 0)                  // skip, still holding
    return max(sell, skip)
```

**Steps:**
1. Call `solve(0, 1)` (start at day 0, can buy).
2. Each state branches into 2 choices.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Many overlapping (day, canBuy) states. Memoize.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][canBuy]` (n * 2 states total).

**Dry Run:** `prices = [7,1,5,3,6,4]`

| Call | i | canBuy | Choices | Result |
|------|---|--------|---------|--------|
| solve(0,1) | 0 | 1 | buy: -7+solve(1,0), skip: solve(1,1) | max(-7+..., ...) |
| solve(1,1) | 1 | 1 | buy: -1+solve(2,0), skip: solve(2,1) | |
| solve(2,0) | 2 | 0 | sell: 5+solve(3,1), skip: solve(3,0) | |
| ... tracing through ... | | | | |
| Final | 0 | 1 | | **7** |

The optimal path: buy at 1, sell at 5 (+4), buy at 3, sell at 6 (+3) = 7.

| Time | Space |
|------|-------|
| O(n * 2) = O(n) | O(n * 2) = O(n) |

**BUD Transition:** Build bottom-up to remove recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[n+1][2]` table from the last day backwards.

**Steps:**
1. `dp[n][0] = dp[n][1] = 0` (no more days, no profit).
2. For `i = n-1` down to `0`:
   - `dp[i][1] = max(-prices[i] + dp[i+1][0], dp[i+1][1])` // buy or skip
   - `dp[i][0] = max(prices[i] + dp[i+1][1], dp[i+1][0])`  // sell or skip
3. Return `dp[0][1]`.

**Dry Run:** `prices = [7,1,5,3,6,4]`

| i | dp[i][1] (can buy) | dp[i][0] (holding) |
|---|--------------------|--------------------|
| 6 (base) | 0 | 0 |
| 5 (price=4) | max(-4+0, 0) = 0 | max(4+0, 0) = 4 |
| 4 (price=6) | max(-6+4, 0) = 0 | max(6+0, 4) = 6 |
| 3 (price=3) | max(-3+6, 0) = 3 | max(3+0, 6) = 6 |
| 2 (price=5) | max(-5+6, 3) = 3 | max(5+3, 6) = 8 |
| 1 (price=1) | max(-1+8, 3) = 7 | max(1+3, 8) = 8 |
| 0 (price=7) | max(-7+8, 7) = 7 | max(7+7, 8) = 14 |

Answer = dp[0][1] = **7**

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Only dp[i+1] is needed. Use 4 variables.

### Approach 4: Space Optimized / Greedy
**What changed (DP):** Replace table with 4 variables: `aheadBuy, aheadSell, currBuy, currSell`.

**What changed (Greedy):** Simply sum all positive day-to-day differences:
```
profit = 0
for i = 1 to n-1:
    if prices[i] > prices[i-1]:
        profit += prices[i] - prices[i-1]
```

**Why greedy works:** Every upswing from day `i-1` to day `i` can be captured as a separate buy-sell transaction. Collecting all positive diffs is equivalent to making the optimal set of transactions.

**Dry Run (Greedy):** `prices = [7,1,5,3,6,4]`
- Day 1->2: 1-7 = -6 (skip)
- Day 2->3: 5-1 = +4 (take)
- Day 3->4: 3-5 = -2 (skip)
- Day 4->5: 6-3 = +3 (take)
- Day 5->6: 4-6 = -2 (skip)
- Total = 4 + 3 = **7**

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each day branches into 2 choices. Without caching, O(2^n)."
**Memo/Tab:** "Only n * 2 = 2n states, each O(1). Total O(n)."
**Space Optimized / Greedy:** "Single pass through the array, O(n) time, O(1) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Thinking you need to find optimal buy/sell pairs** -- you do not. Just collect all positive differences.
2. **Forgetting the canBuy state in DP** -- without it, you might buy twice without selling.
3. **Off-by-one in tabulation direction** -- if going right-to-left, base case is day n, not day n-1.
4. **Returning negative profit** -- if no profitable transaction exists, answer is 0.

### Edge Cases to Test
- [ ] Monotonically decreasing `[7,6,4,3,1]` --> 0
- [ ] Monotonically increasing `[1,2,3,4,5]` --> 4
- [ ] Single day `[5]` --> 0
- [ ] Two days, profit `[1,5]` --> 4
- [ ] Two days, loss `[5,1]` --> 0
- [ ] All same prices `[3,3,3,3]` --> 0
- [ ] Zigzag `[1,5,1,5]` --> 8

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Unlimited transactions? Must sell before buying again? Can buy and sell same day?"
2. **Start with DP:** Show the general framework `dp[i][canBuy]` -- this demonstrates strong foundations.
3. **Show greedy insight:** "For unlimited transactions, we just collect all positive differences."
4. **Mention generalization:** "If limited to K transactions, extend state to `dp[i][canBuy][transactionsLeft]`."

### Follow-Up Questions
- "At most 2 transactions?" --> LeetCode #123, extend state with transaction count.
- "At most K transactions?" --> LeetCode #188, `dp[i][buy][k]`.
- "With cooldown?" --> LeetCode #309, after selling must wait 1 day.
- "With transaction fee?" --> LeetCode #714, subtract fee on each sell.

---

## CONNECTIONS
- **Prerequisite:** Best Time to Buy and Sell Stock I (P001, single transaction)
- **Same Pattern:** Stock problems with state = (day, canBuy, txnCount)
- **This Unlocks:** Stock III (2 txns), Stock IV (K txns), With Cooldown, With Fee
- **Greedy Connection:** The greedy approach works ONLY for unlimited transactions. Adding constraints (K txns, cooldown) breaks greedy, requiring DP.
