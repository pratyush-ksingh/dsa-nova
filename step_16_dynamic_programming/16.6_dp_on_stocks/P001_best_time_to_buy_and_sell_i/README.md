# Best Time to Buy and Sell Stock I

> **Batch 1 of 12** | **Topic:** Dynamic Programming | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
You are given an array `prices` where `prices[i]` is the price of a given stock on the `i`th day. You want to maximize your profit by choosing a **single day to buy** and a **different day in the future to sell**. Return the maximum profit. If no profit is possible, return `0`.

*(LeetCode #121)*

**Constraints:**
- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[7, 1, 5, 3, 6, 4]` | `5` | Buy on day 1 (price=1), sell on day 4 (price=6). Profit = 6-1 = 5 |
| `[7, 6, 4, 3, 1]` | `0` | Prices only decrease -- no profitable transaction |
| `[2, 4, 1]` | `2` | Buy day 0 (2), sell day 1 (4). Profit = 2 |

### Real-Life Analogy
> *You have a time machine and can look at a stock's daily prices for the past year. You want to pick the best day to buy and the best later day to sell. The catch: you must buy before you sell (no short selling). The trick is to keep track of the cheapest price you have seen so far as you scan forward. At each day, ask "if I sell today, what is my profit?" -- that is today's price minus the cheapest price seen before today. Keep the best answer.*

### Key Observations
1. We need to find `max(prices[j] - prices[i])` where `j > i`. This is a max difference with order constraint.
2. Brute force checks all pairs -- O(n^2). But we can do better.
3. As we scan left to right, track the minimum price seen so far. At each position, the best profit is `prices[i] - minSoFar`. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Single pass with running minimum:** We only need to remember one thing from the past (the minimum price). This gives O(1) extra space and O(n) time.
- No need for DP arrays, heaps, or sorting. Pure linear scan.

### Pattern Recognition
- **Pattern:** Track running min/max while scanning (Kadane's variant)
- **Classification Cue:** "When you see _maximize difference with order constraint (buy before sell)_ --> think _running minimum + compute profit at each step_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Pairs
**Idea:** For every pair `(i, j)` where `i < j`, compute `prices[j] - prices[i]`. Return the maximum.

**Steps:**
1. Initialize `maxProfit = 0`.
2. For `i = 0` to `n-2`:
   - For `j = i+1` to `n-1`:
     - `maxProfit = max(maxProfit, prices[j] - prices[i])`.
3. Return `maxProfit`.

**Dry Run:** `[7, 1, 5, 3, 6, 4]`

| i | j | prices[j]-prices[i] | maxProfit |
|---|---|---------------------|-----------|
| 0 | 1 | 1-7 = -6 | 0 |
| 0 | 2 | 5-7 = -2 | 0 |
| 0 | 4 | 6-7 = -1 | 0 |
| 1 | 2 | 5-1 = 4 | 4 |
| 1 | 4 | 6-1 = 5 | 5 |
| ... | ... | ... | 5 |

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

**BUD Transition:** **Bottleneck** -- for each sell day `j`, we recompute the minimum buy price by scanning back. Instead, track the running minimum as we go.

### Approach 2: Optimal -- Single Pass with Running Minimum
**What changed:** Maintain `minPrice` as we scan. At each day, compute profit if we sell today.

**Steps:**
1. `minPrice = prices[0]`, `maxProfit = 0`.
2. For `i = 1` to `n-1`:
   - `maxProfit = max(maxProfit, prices[i] - minPrice)`.
   - `minPrice = min(minPrice, prices[i])`.
3. Return `maxProfit`.

**Dry Run:** `[7, 1, 5, 3, 6, 4]`

| i | prices[i] | minPrice | profit today | maxProfit |
|---|-----------|----------|-------------|-----------|
| 0 | 7 | 7 | (init) | 0 |
| 1 | 1 | 1 | 1-7=-6 -> 0 | 0 |
| 2 | 5 | 1 | 5-1=4 | 4 |
| 3 | 3 | 1 | 3-1=2 | 4 |
| 4 | 6 | 1 | 6-1=5 | 5 |
| 5 | 4 | 1 | 4-1=3 | 5 |

**Result:** 5

| Time | Space |
|------|-------|
| O(n) | O(1) |

*This is the best possible.* We must examine every price at least once, so O(n) is optimal.

---

## COMPLEXITY -- INTUITIVELY
**Brute Force:** "We check all n*(n-1)/2 pairs. That is O(n^2)."
**Optimal:** "One pass through the array. At each element, we do O(1) work (compare, update min). Total: O(n). Space: just two variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to handle decreasing prices -- answer should be 0, not negative.
2. Updating `minPrice` before computing profit -- must compute profit first, then update min.
3. Trying to find both the global min and global max independently -- the min must come **before** the max. `[2, 4, 1]` has global min=1 at index 2, global max=4 at index 1, but you cannot buy at index 2 and sell at index 1.

### Edge Cases to Test
- [ ] All decreasing prices `[5, 4, 3, 2, 1]` --> 0
- [ ] All same prices `[3, 3, 3]` --> 0
- [ ] Minimum at the very end `[3, 1]` --> 0 (no sell day after last buy)
- [ ] Only two elements `[1, 5]` --> 4
- [ ] Single element `[5]` --> 0

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "One transaction only? Must buy before sell? Return 0 if no profit?"
2. **Brute Force:** "Check all pairs, O(n^2). But I notice we're recomputing the best buy price for each sell day."
3. **Optimize:** "Track running minimum price. At each day, check if selling today gives the best profit so far."
4. **Code:** Write the single-pass solution. Clean and short.

### Follow-Up Questions
- "What if you can do multiple transactions?" --> Best Time to Buy and Sell Stock II (greedy: buy every valley, sell every peak).
- "What if you can do at most K transactions?" --> DP with 2D state (day, transactions remaining).
- "What if there's a transaction fee?" --> Best Time to Buy and Sell Stock with Transaction Fee.

---

## CONNECTIONS
- **Prerequisite:** Array traversal, running min/max pattern
- **Same Pattern:** Maximum Subarray (Kadane's), Trapping Rain Water (prefix max)
- **This Unlocks:** Stock II (multiple transactions), Stock III (two transactions), Stock IV (k transactions)
- **Harder Variant:** Best Time to Buy and Sell Stock with Cooldown, with Transaction Fee
