# Stock Buy and Sell

> **Batch 3 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array `prices` where `prices[i]` is the price of a stock on day `i`, find the **maximum profit** you can achieve. You may complete as many transactions as you like (buy and sell multiple times), but you must sell before buying again. You cannot hold multiple shares at once.

**Constraints:**
- `1 <= n <= 10^5`
- `0 <= prices[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[7, 1, 5, 3, 6, 4]` | `7` | Buy at 1, sell at 5 (profit 4); buy at 3, sell at 6 (profit 3). Total = 7 |
| `[1, 2, 3, 4, 5]` | `4` | Buy at 1, sell at 5. One long uptrend = hold through |
| `[7, 6, 4, 3, 1]` | `0` | Prices only decrease -- no profitable transaction exists |

### Real-Life Analogy
> *Imagine you have a time machine and can see tomorrow's fruit prices at a market. Every morning you decide: should I buy mangoes today to sell tomorrow, or sit tight? If tomorrow's price is higher, you buy today and sell tomorrow. If it is lower, you skip. Summing every upward day-over-day movement gives you the maximum possible earnings -- you are capturing every profitable "wave."*

### Key Observations
1. We do NOT need to find actual buy/sell pairs. We just need the total profit.
2. Every consecutive price increase (`prices[i] > prices[i-1]`) represents profit we can capture.
3. Summing all positive day-over-day differences gives the maximum profit. <-- This is the "aha" insight. A multi-day uptrend can be decomposed into single-day gains.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- Plain array traversal -- no auxiliary structures needed.
- A greedy approach works because each local gain contributes independently to the global optimum.

### Pattern Recognition
- **Pattern:** Greedy (capture every local gain)
- **Classification Cue:** "When you see _maximize total profit with unlimited transactions_ --> think _sum of all positive consecutive differences_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Enumerate All Subsets of Transactions)
**Idea:** Try every possible combination of buy/sell pairs using recursion/backtracking.

**Steps:**
1. At each day, you have two choices: buy (if not holding) or sell (if holding) or skip.
2. Recursively explore all combinations.
3. Return the maximum profit found.

**Why it works:** Exhaustive search guarantees finding the optimal set of transactions.

**BUD -- Unnecessary Work:** Most branches are wasteful. The recursion tree has 2^n leaves. We are doing exponential work for something that has greedy structure.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

### Approach 2: Optimal -- Greedy (Sum of Positive Differences)
**What changed:** We realize that the maximum profit is simply the sum of all upward movements. If `prices[i] > prices[i-1]`, we add the difference.

**Steps:**
1. Initialize `profit = 0`.
2. For `i` from `1` to `n - 1`:
   - If `prices[i] > prices[i - 1]`, add `prices[i] - prices[i - 1]` to `profit`.
3. Return `profit`.

**Why this is correct:** Any multi-day gain (buy Monday, sell Friday) equals the sum of daily gains (Mon->Tue + Tue->Wed + ... + Thu->Fri). So collecting every upward step is equivalent to the best possible set of trades.

**Dry Run:** Input = `[7, 1, 5, 3, 6, 4]`

| Day (i) | prices[i] | prices[i]-prices[i-1] | Add? | profit |
|---------|-----------|----------------------|------|--------|
| 1 | 1 | 1 - 7 = -6 | No | 0 |
| 2 | 5 | 5 - 1 = +4 | Yes | 4 |
| 3 | 3 | 3 - 5 = -2 | No | 4 |
| 4 | 6 | 6 - 3 = +3 | Yes | 7 |
| 5 | 4 | 4 - 6 = -2 | No | 7 |

**Result:** 7

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Valley-Peak (Same Complexity, Explicit Trades)
**What changed:** Instead of summing daily diffs, we identify explicit valley-peak pairs. Find each local minimum (buy) and the next local maximum (sell). This gives the same answer but also tells you the actual trade days.

**Steps:**
1. Initialize `i = 0`, `profit = 0`.
2. While `i < n - 1`:
   - Find valley: while `i < n - 1` and `prices[i] >= prices[i + 1]`, increment `i`. Set `valley = prices[i]`.
   - Find peak: while `i < n - 1` and `prices[i] <= prices[i + 1]`, increment `i`. Set `peak = prices[i]`.
   - Add `peak - valley` to `profit`.
3. Return `profit`.

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan the prices array exactly once. Each element is visited once, doing O(1) work."
**Space:** O(1) -- "We only maintain a running profit counter. No extra arrays or data structures."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Trying to find the single best buy/sell pair -- that is a different problem (Best Time to Buy and Sell Stock I, one transaction only).
2. Forgetting you can buy and sell on the same day (sell morning, buy afternoon). This does not affect the greedy approach but is important conceptually.
3. Thinking you need DP -- greedy is sufficient here because transactions are unlimited.

### Edge Cases to Test
- [ ] Monotonically increasing `[1, 2, 3, 4, 5]` --> profit = 4 (one long hold)
- [ ] Monotonically decreasing `[5, 4, 3, 2, 1]` --> profit = 0
- [ ] Single element `[5]` --> profit = 0 (no trade possible)
- [ ] Two elements `[1, 5]` --> profit = 4
- [ ] All same price `[3, 3, 3]` --> profit = 0
- [ ] Alternating `[1, 5, 1, 5]` --> profit = 8

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Can I make unlimited transactions? Must I sell before buying again? Can I buy and sell the same day?"
2. **Approach:** "Since transactions are unlimited, every upward movement is profit I can capture. I'll sum all positive consecutive differences."
3. **Code:** Write the 5-line greedy loop. Mention that this is provably optimal because any multi-day gain decomposes into daily gains.
4. **Test:** Walk through `[7, 1, 5, 3, 6, 4]` showing the two profitable segments.

### Follow-Up Questions
- "What if you can only make one transaction?" --> Track min-so-far, maximize `price - min_so_far` (LeetCode #121).
- "What if you can make at most k transactions?" --> DP with states `(day, transactions_left, holding)` (LeetCode #188).
- "What if there is a cooldown period?" --> DP with states for cooldown (LeetCode #309).

---

## CONNECTIONS
- **Prerequisite:** Basic array traversal, greedy intuition
- **Same Pattern:** Gas Station (greedy accumulation), Jump Game (greedy forward progress)
- **Harder Variant:** Best Time to Buy and Sell Stock III (at most 2 transactions), Stock with Cooldown
- **This Unlocks:** Understanding greedy vs DP tradeoff in optimization problems
