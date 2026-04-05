"""
Problem: Introduction to DP -- Fibonacci Number
Difficulty: EASY | XP: 10

Compute Nth Fibonacci using all 4 DP approaches:
Recursion -> Memoization -> Tabulation -> Space Optimized
"""
import time


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n) recursion stack
# ============================================================
def fib_recursive(n: int) -> int:
    """Direct translation of recurrence. Exponential time."""
    if n <= 1:
        return n
    return fib_recursive(n - 1) + fib_recursive(n - 2)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def fib_memo(n: int) -> int:
    """Cache results to avoid redundant computation."""
    dp = [-1] * (n + 1)

    def solve(n: int) -> int:
        if n <= 1:
            return n
        if dp[n] != -1:
            return dp[n]  # already computed
        dp[n] = solve(n - 1) + solve(n - 2)
        return dp[n]

    return solve(n)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def fib_tab(n: int) -> int:
    """Fill dp array iteratively from base cases upward."""
    if n <= 1:
        return n

    dp = [0] * (n + 1)
    dp[0] = 0
    dp[1] = 1

    for i in range(2, n + 1):
        dp[i] = dp[i - 1] + dp[i - 2]

    return dp[n]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n) | Space: O(1)
# ============================================================
def fib_space(n: int) -> int:
    """Only keep the last two values -- O(1) space."""
    if n <= 1:
        return n

    prev2, prev1 = 0, 1  # fib(0), fib(1)

    for i in range(2, n + 1):
        curr = prev1 + prev2
        prev2 = prev1
        prev1 = curr

    return prev1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Introduction to DP: Fibonacci ===\n")

    test_cases = [
        (0, 0), (1, 1), (2, 1), (5, 5),
        (10, 55), (20, 6765), (30, 832040),
    ]

    print(f"{'n':<5} {'Recurse':<12} {'Memo':<12} {'Tab':<12} {'SpaceOpt':<12} {'Expected':<12} {'Pass':<6}")
    print("-" * 70)

    for n, expected in test_cases:
        r = fib_recursive(n) if n <= 25 else "skip"
        m = fib_memo(n)
        t = fib_tab(n)
        s = fib_space(n)

        passes = m == expected and t == expected and s == expected
        if n <= 25:
            passes = passes and (r == expected)

        print(f"{n:<5} {str(r):<12} {m:<12} {t:<12} {s:<12} {expected:<12} {passes}")

    # Timing demo
    print("\n--- Timing Demo ---")
    start = time.perf_counter()
    result = fib_tab(45)
    elapsed = (time.perf_counter() - start) * 1_000_000
    print(f"fib(45) = {result} [Tabulation: {elapsed:.0f} us]")

    start = time.perf_counter()
    result = fib_space(45)
    elapsed = (time.perf_counter() - start) * 1_000_000
    print(f"fib(45) = {result} [Space Opt:  {elapsed:.0f} us]")

    # Show overlapping subproblems visually
    print("\n--- Overlapping Subproblems Demo ---")
    call_count = {}

    def fib_counted(n):
        call_count[n] = call_count.get(n, 0) + 1
        if n <= 1:
            return n
        return fib_counted(n - 1) + fib_counted(n - 2)

    fib_counted(10)
    print(f"fib(10) recursive call counts:")
    for k in sorted(call_count.keys()):
        print(f"  fib({k}) called {call_count[k]} time(s)")
    print(f"Total calls: {sum(call_count.values())}")
