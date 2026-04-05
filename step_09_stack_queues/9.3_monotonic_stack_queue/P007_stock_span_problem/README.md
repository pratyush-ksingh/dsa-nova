# Online Stock Span

> **LeetCode 901** | **Step 09.9.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Design an algorithm that collects daily price quotes for some stock and returns the **span** of that stock's price for the current day.

The **span** of the stock's price on a given day is defined as the maximum number of consecutive days (starting from today and going backwards) for which the stock price was **less than or equal** to today's price.

Implement the `StockSpanner` class:
- `StockSpanner()` — initialises the object.
- `int next(int price)` — returns the **span** of the stock's price given today's price.

## Examples

| Day | Price | Span | Explanation |
|-----|-------|------|-------------|
| 1 | 100 | 1 | No prior days |
| 2 | 80  | 1 | 80 < 100 |
| 3 | 60  | 1 | 60 < 80 |
| 4 | 70  | 2 | 70 >= 60; stop at 80 |
| 5 | 60  | 1 | 60 < 70 |
| 6 | 75  | 4 | 75 >= 60, 70, 60; stop at 80 |
| 7 | 85  | 6 | 85 >= 75, 60, 70, 60, 80; stop at 100 |

**Input:** `[100, 80, 60, 70, 60, 75, 85]`
**Output:** `[1, 1, 1, 2, 1, 4, 6]`

## Constraints

- `1 <= price <= 10^5`
- At most `10^4` calls will be made to `next`.

---

## Approach 1: Brute Force — Linear Scan Backwards

**Intuition:** Maintain a history of all prices. For each new price, walk backwards through the history counting consecutive days where `price[i] <= today`. Stop at the first day where `price[i] > today`.

**Steps:**
1. Append `price` to `self.prices`.
2. Set `span = 1`, `i = len(prices) - 2`.
3. While `i >= 0` and `prices[i] <= price`: increment `span`, decrement `i`.
4. Return `span`.

| Metric | Value |
|--------|-------|
| Time   | O(n) per call, O(n²) total |
| Space  | O(n)  |

---

## Approach 2: Optimal — Monotonic Decreasing Stack

**Intuition:** The key observation: if today's price >= price[j], then every day that contributed to span[j] is also counted in today's span. So we can absorb span[j] directly and discard day j entirely. This is the classic monotonic stack pattern.

The stack maintains a **strictly decreasing** sequence of prices (from bottom to top). Each price enters and exits the stack at most once, giving amortised O(1) per call.

**Steps:**
1. Initialise `span = 1`.
2. While stack is not empty AND `stack.top().price <= price`:
   - `span += stack.pop().span`
3. Push `(price, span)` onto the stack.
4. Return `span`.

**Why store span in the stack?** When we pop `(p, s)`, those `s` days are all `<= p <= today`, so they are all counted in today's span. We accumulate them and discard the entry.

| Metric | Value |
|--------|-------|
| Time   | O(1) amortised per call |
| Space  | O(n)  |

---

## Approach 3: Best — Stack Stores (Price, Day Index)

**Intuition:** Instead of storing the pre-computed span, store the absolute day index alongside each price. When popping, compute `span = today_index - surviving_top_index`. This reveals not just the span but also **which exact previous day** had the nearest higher price — useful for follow-up questions.

**Steps:**
1. Increment `self.day` counter.
2. While stack not empty AND `stack.top().price <= price`: pop.
3. `span = self.day - (stack.top().index if stack else -1)`.
   - Empty stack means all prior days are `<=` today — span equals day index + 1 = `self.day - (-1)`.
4. Push `(price, self.day)` onto the stack.
5. Return `span`.

| Metric | Value |
|--------|-------|
| Time   | O(1) amortised per call |
| Space  | O(n)  |

---

## Real-World Use Case

**Financial momentum indicators**: The stock span is the core of simple momentum signals used in trading. Systems compute it in real time on a stream of ticks. The monotonic stack approach is directly how production systems process hundreds of thousands of price updates per second — each price pushed/popped at most once means the CPU cost per tick is effectively constant regardless of history length. Related computations (RSI, Bollinger bands, Williams %R) all rely on the same "how many consecutive bars satisfy condition X" pattern.

---

## Interview Tips

- The monotonic stack insight — "absorb the span of popped elements" — is the key idea. State it explicitly before coding.
- Be precise about **strict vs. non-strict** comparison. The problem says `<=` today's price counts, so we pop when `stack.top().price <= price`. If the problem said "strictly less than", pop only when `<`.
- Common mistake: popping when `>` instead of `<=`. Verify with `[1, 1, 1, 1]` — spans should be `[1, 2, 3, 4]`.
- Amortised analysis: each of the `n` elements is pushed once and popped at most once, giving total work O(2n) = O(n) over all `n` calls, so O(1) amortised per call.
- Follow-up: "Next Greater Element" (LeetCode 496/503) uses the exact same monotonic stack technique — familiarise yourself with the whole pattern family.
