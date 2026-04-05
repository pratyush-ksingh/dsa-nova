# Best Time to Buy and Sell Stock III

> **Step 16.6** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

You are given an integer array `prices` where `prices[i]` is the price of a given stock on day `i`. You may complete **at most 2 transactions**. A transaction is one buy + one sell. You must sell the stock before buying it again. Return the **maximum profit** you can achieve. If no profit is possible, return `0`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3,3,5,0,0,3,1,4]` | `6` | Buy@0 sell@3 (profit 3) + Buy@1 sell@4 (profit 3) = 6 |
| `[1,2,3,4,5]` | `4` | Buy@1 sell@5 = 4 (one transaction is optimal) |
| `[7,6,4,3,1]` | `0` | Prices only fall — no profitable transaction exists |
| `[1]` | `0` | Single day — cannot complete a transaction |
| `[6,1,3,2,4,7]` | `7` | Buy@1 sell@3 (profit 2) + Buy@2 sell@7 (profit 5) = 7 |

## Constraints

- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^5`

---

## Approach 1: Brute Force — Enumerate Split Points

**Intuition:** Any valid pair of at most 2 transactions can be split at some day `k`: the first transaction occurs entirely in `prices[0..k]` and the second in `prices[k..n-1]`. For each split point compute the best single-transaction profit on each side using a linear scan, sum them, and take the maximum over all `k`.

**Steps:**
1. For each split point `k` from `0` to `n-1`:
   - `left = maxProfitRange(prices, 0, k)` — best profit with 1 transaction on left half.
   - `right = maxProfitRange(prices, k, n-1)` — best profit with 1 transaction on right half.
   - Update `ans = max(ans, left + right)`.
2. `maxProfitRange` runs in O(n) via a single pass tracking `minPrice`.
3. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) — outer loop O(n), inner scan O(n) |
| Space  | O(1) |

---

## Approach 2: Optimal — DP with 4 State Arrays

**Intuition:** Model the problem as a state machine with four phases:
1. **buy1**: have bought stock once, looking to sell.
2. **sell1**: have sold once, looking to buy a second time.
3. **buy2**: have bought the second time, looking to sell.
4. **sell2**: have completed both transactions.

For each day update all four states. The answer is `sell2[n-1]`.

**Steps:**
1. Initialize `buy1[0] = -prices[0]`, others `0`.
2. For each day `i >= 1`:
   - `buy1[i]  = max(buy1[i-1],  -prices[i])`
   - `sell1[i] = max(sell1[i-1], buy1[i-1] + prices[i])`
   - `buy2[i]  = max(buy2[i-1],  sell1[i-1] - prices[i])`
   - `sell2[i] = max(sell2[i-1], buy2[i-1] + prices[i])`
3. Return `sell2[n-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) — four arrays of length n |

---

## Approach 3: Best — Four Scalar Variables, O(1) Space

**Intuition:** Each of the four state arrays only reads from the previous index. Replace the arrays with four scalar variables updated in a single forward pass. Order of update matters: update `buy1` first so that `sell1` uses the freshest `buy1`, and so on.

**Steps:**
1. Initialize `buy1 = -INF, sell1 = 0, buy2 = -INF, sell2 = 0`.
2. For each `price` in `prices`:
   - `buy1  = max(buy1,  -price)`
   - `sell1 = max(sell1, buy1  + price)`
   - `buy2  = max(buy2,  sell1 - price)`
   - `sell2 = max(sell2, buy2  + price)`
3. Return `sell2`.

The update order is not a coincidence: we are using the already-updated `buy1` when computing `sell1` for the same day. Because we initialized `buy1 = -INF`, this only triggers when a valid buy has been seen, so it remains correct.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Algorithmic trading with a limited trade budget:** A hedge fund is allowed exactly two round-trip trades of a single asset in a quarter. Given the daily price history, find the optimal entry and exit points for both trades to maximise total return. The four-variable solution maps directly to the real trading system's state machine: "in first position / flat / in second position / fully closed".

Other applications:
- **Capital allocation:** A startup with two funding windows (two "buys") and two exit events (two "sells") wants to maximise the IRR.
- **Energy arbitrage:** A battery can charge at most twice and discharge at most twice in a day at fluctuating electricity prices — find optimal charge/discharge times.

## Interview Tips

- This is the canonical "at most k transactions" problem with k=2. Knowing the general k-transaction DP (O(n*k) time, O(k) space) lets you answer follow-ups instantly.
- The four-variable trick is the star of the show. Walk through a 3-day example on the whiteboard to demonstrate the state transitions.
- Initialize `buy1` and `buy2` as `-infinity` (or `Integer.MIN_VALUE`), not `0`. Using `0` would imply buying costs nothing, producing wrong answers for price arrays that only rise.
- The update order within the loop (`buy1 -> sell1 -> buy2 -> sell2`) effectively merges two passes into one. Explain that updating `buy1` before `sell1` simulates "I could buy and sell on the same day" which is equivalent to "no transaction" — the profit nets to zero — so the result is still correct.
- Edge cases: single element (return 0), monotonically decreasing array (return 0), only one profitable transaction exists (the DP naturally handles this since the second transaction can be a no-op with zero profit).
- Generalisation: for "at most k transactions" replace the 4 variables with two arrays of length k+1 (`buy[0..k]` and `sell[0..k]`) updated in the same pattern.
