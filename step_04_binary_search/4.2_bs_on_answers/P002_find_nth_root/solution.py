"""
Problem: Find Nth Root
Difficulty: MEDIUM | XP: 25

Key Insight: Binary search on the answer space [1, m].
Check if mid^n == m using overflow-safe power computation.
"""
from typing import List, Optional


def _safe_pow(base: int, exp: int, target: int) -> int:
    """
    Compute base^exp vs target safely.
    Returns: 0 if base^exp < target, 1 if ==, 2 if >.
    """
    result = 1
    for _ in range(exp):
        result *= base
        if result > target:
            return 2
    if result == target:
        return 1
    return 0


# ============================================================
# APPROACH 1: BRUTE FORCE -- Linear Scan
# Time: O(m^(1/n) * n)  |  Space: O(1)
#
# Try each x from 1 upward, compute x^n.
# ============================================================
def brute_force(n: int, m: int) -> int:
    if m == 0:
        return 0
    x = 1
    while x <= m:
        cmp = _safe_pow(x, n, m)
        if cmp == 1:
            return x
        if cmp == 2:
            return -1
        x += 1
    return -1


# ============================================================
# APPROACH 2: OPTIMAL -- Binary Search on Answer Space
# Time: O(n * log(m))  |  Space: O(1)
#
# Binary search [1, m]. For each mid, check mid^n vs m.
# ============================================================
def optimal(n: int, m: int) -> int:
    if m == 0:
        return 0
    if n == 1:
        return m

    lo, hi = 1, m
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        cmp = _safe_pow(mid, n, m)
        if cmp == 1:
            return mid
        elif cmp == 0:
            lo = mid + 1
        else:
            hi = mid - 1
    return -1


# ============================================================
# APPROACH 3: BEST -- Newton's Method (Integer)
# Time: O(n * log(log(m)))  |  Space: O(1)
#
# Quadratic convergence: x_new = ((n-1)*x + m // x^(n-1)) // n
# ============================================================
def best(n: int, m: int) -> int:
    if m == 0:
        return 0
    if m == 1 or n == 1:
        return m if n == 1 else 1

    # Initial guess
    x = int(m ** (1.0 / n)) + 1

    # Newton's iteration
    for _ in range(100):
        x_pow_nm1 = x ** (n - 1)
        if x_pow_nm1 == 0:
            break
        x_new = ((n - 1) * x + m // x_pow_nm1) // n
        if x_new >= x:
            break
        x = x_new

    # Check x and x+1 (Newton's can undershoot by 1)
    if x ** n == m:
        return x
    if (x + 1) ** n == m:
        return x + 1
    return -1


if __name__ == "__main__":
    print("=== Find Nth Root ===")
    tests = [(3, 27), (2, 16), (3, 20), (4, 81), (2, 1), (1, 100)]
    for n, m in tests:
        print(f"n={n}, m={m}")
        print(f"  Brute:   {brute_force(n, m)}")
        print(f"  Optimal: {optimal(n, m)}")
        print(f"  Best:    {best(n, m)}")
        print()
