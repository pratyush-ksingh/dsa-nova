"""
Problem: Fibonacci Number (LeetCode #509)
Difficulty: EASY | XP: 10

Compute the n-th Fibonacci number.
F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2).
"""
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE -- Naive Recursion
# Time: O(2^n)  |  Space: O(n) stack
# Directly implement the recurrence. Exponentially slow.
# ============================================================
def brute_force(n: int) -> int:
    if n <= 1:
        return n
    return brute_force(n - 1) + brute_force(n - 2)


# ============================================================
# APPROACH 2: OPTIMAL -- Memoized Recursion (Top-Down DP)
# Time: O(n)  |  Space: O(n) memo + stack
# Cache each F(k) on first computation. Eliminates redundancy.
# ============================================================
def optimal(n: int) -> int:
    memo = {}

    def fib(k: int) -> int:
        if k <= 1:
            return k
        if k in memo:
            return memo[k]
        memo[k] = fib(k - 1) + fib(k - 2)
        return memo[k]

    return fib(n)


# ============================================================
# APPROACH 3: BEST -- Iterative Bottom-Up (O(1) Space)
# Time: O(n)  |  Space: O(1)
# Only keep previous two values. No recursion, no memo.
# ============================================================
def best(n: int) -> int:
    if n <= 1:
        return n
    prev2, prev1 = 0, 1  # F(i-2), F(i-1)
    for _ in range(2, n + 1):
        curr = prev1 + prev2
        prev2 = prev1
        prev1 = curr
    return prev1


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [
        (0, 0), (1, 1), (2, 1), (3, 2), (5, 5),
        (10, 55), (20, 6765), (30, 832040),
    ]

    print("=== Fibonacci Number ===\n")
    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        bt = best(n)
        status = "PASS" if b == o == bt == expected else "FAIL"
        print(f"[{status}] n={n:<4} | Naive={b:<10} | Memo={o:<10} | Iterative={bt:<10} | Expected={expected:<10}")
