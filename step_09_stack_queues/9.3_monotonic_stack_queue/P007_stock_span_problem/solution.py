"""
Problem: Online Stock Span (LeetCode 901)
Difficulty: MEDIUM | XP: 25

Design a class that collects daily price quotes for a stock and returns
the span of that stock's price for the current day.
The span is the maximum number of consecutive days (starting from today
and going backwards) for which the stock price was less than or equal
to today's price.

Example:
  prices = [100, 80, 60, 70, 60, 75, 85]
  spans  = [1,   1,  1,  2,  1,  4,  6]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²) total  |  Space: O(n) for stored prices
# For each new price, scan backwards through all previous prices
# until we find one that is strictly greater, counting days.
# ============================================================
class StockSpannerBrute:
    """
    Store all prices seen so far. For each call to next(price),
    walk backwards counting how many consecutive prices are <= price.
    Simple but O(n) per call, O(n^2) overall.
    """
    def __init__(self):
        self.prices: List[int] = []

    def next(self, price: int) -> int:
        self.prices.append(price)
        span = 1
        i = len(self.prices) - 2   # index of previous day
        while i >= 0 and self.prices[i] <= price:
            span += 1
            i -= 1
        return span


# ============================================================
# APPROACH 2: OPTIMAL  (monotonic decreasing stack)
# Time: O(n) amortised  |  Space: O(n)
# Maintain a stack of (price, span) pairs.
# Each price is pushed/popped at most once, giving O(1) amortised.
# When a new price arrives, pop all stack entries whose price is
# <= new price, accumulating their spans.
# ============================================================
class StockSpannerOptimal:
    """
    Monotonic decreasing stack stores (price, span) pairs.

    Key insight: if price[i] >= price[j] for some j < i, then
    every day counted in span[j] is also counted in span[i].
    So instead of re-scanning those days, we absorb span[j]
    directly and pop j from the stack — amortised O(1) per call.

    Stack invariant: prices strictly decrease from bottom to top.
    """
    def __init__(self):
        self.stack: List[tuple] = []   # (price, span)

    def next(self, price: int) -> int:
        span = 1
        while self.stack and self.stack[-1][0] <= price:
            _, prev_span = self.stack.pop()
            span += prev_span
        self.stack.append((price, span))
        return span


# ============================================================
# APPROACH 3: BEST  (same stack, with explicit index stored)
# Time: O(n) amortised  |  Space: O(n)
# Instead of storing cumulative span, store the (price, index)
# so we can compute the span as (current_index - stack_top_index).
# Functionally equivalent; useful when you also need to know
# the exact day index of the previous higher price.
# ============================================================
class StockSpannerBest:
    """
    Stack stores (price, day_index).  Span = current_day - stack[-1][1].
    Popping stops as soon as we see a price strictly greater than today.
    This version makes it easy to also answer "which previous day had
    a higher price?" — useful in interview follow-ups.
    """
    def __init__(self):
        self.stack: List[tuple] = []   # (price, index)
        self.day = -1

    def next(self, price: int) -> int:
        self.day += 1
        while self.stack and self.stack[-1][0] <= price:
            self.stack.pop()
        # If stack is empty, span covers all days up to and including today
        span = self.day - (self.stack[-1][1] if self.stack else -1)
        self.stack.append((price, self.day))
        return span


def simulate(SpannerClass, prices):
    spanner = SpannerClass()
    return [spanner.next(p) for p in prices]


if __name__ == "__main__":
    test_cases = [
        ([100, 80, 60, 70, 60, 75, 85], [1, 1, 1, 2, 1, 4, 6]),
        ([7, 2, 1, 2],                   [1, 1, 1, 3]),
        ([100],                          [1]),
        ([1, 1, 1, 1],                   [1, 2, 3, 4]),
        ([5, 3, 1, 2, 4],                [1, 1, 1, 2, 4]),
    ]

    print("=== Stock Span Problem ===\n")
    for prices, expected in test_cases:
        b   = simulate(StockSpannerBrute,   prices)
        o   = simulate(StockSpannerOptimal, prices)
        bst = simulate(StockSpannerBest,    prices)
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] Prices:  {prices}")
        print(f"       Brute:   {b}")
        print(f"       Optimal: {o}")
        print(f"       Best:    {bst}")
        print(f"       Expect:  {expected}\n")
