"""
Problem: Power Function using Recursion (LeetCode #50)
Difficulty: MEDIUM | XP: 25

Implement pow(x, n) which calculates x raised to the power n.
"""
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE -- Linear Recursion
# Time: O(n)  |  Space: O(n)
#
# Simple recursive definition: x^n = x * x^(n-1).
# Too slow for large n but illustrates the concept.
# ============================================================
def brute_force(x: float, n: int) -> float:
    if n == 0:
        return 1.0
    if x == 0:
        return 0.0
    if x == 1:
        return 1.0
    if x == -1:
        return 1.0 if n % 2 == 0 else -1.0

    if n < 0:
        x = 1.0 / x
        n = -n

    result = 1.0
    for _ in range(n):
        result *= x
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Binary Exponentiation
# Time: O(log n)  |  Space: O(log n)
#
# x^n = (x^(n/2))^2 if even, x * (x^(n/2))^2 if odd.
# Halves n at each step for logarithmic depth.
# ============================================================
def optimal(x: float, n: int) -> float:
    if n < 0:
        x = 1.0 / x
        n = -n

    def fast_pow(base: float, exp: int) -> float:
        if exp == 0:
            return 1.0
        half = fast_pow(base, exp // 2)
        if exp % 2 == 0:
            return half * half
        else:
            return base * half * half

    return fast_pow(x, n)


# ============================================================
# APPROACH 3: BEST -- Iterative Binary Exponentiation
# Time: O(log n)  |  Space: O(1)
#
# Uses binary representation of n. For each set bit,
# multiply result by the corresponding power of x.
# ============================================================
def best(x: float, n: int) -> float:
    if n < 0:
        x = 1.0 / x
        n = -n

    result = 1.0
    current_product = x

    while n > 0:
        if n & 1:
            result *= current_product
        current_product *= current_product
        n >>= 1

    return result


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Power Function using Recursion ===\n")

    sys.setrecursionlimit(20000)

    tests = [
        (2.0, 10, 1024.0),
        (2.1, 3, 9.261),
        (2.0, -2, 0.25),
        (1.0, 2147483647, 1.0),
        (2.0, 0, 1.0),
        (-1.0, 3, -1.0),
        (0.5, -2, 4.0),
    ]

    for x, n, expected in tests:
        # Skip brute force for huge n
        b = brute_force(x, n) if abs(n) <= 10000 else None
        o = optimal(x, n)
        be = best(x, n)

        print(f"pow({x}, {n})  Expected: {expected:.4f}")
        if b is not None:
            print(f"  Brute:   {b:.4f}  {'PASS' if abs(b - expected) < 0.001 else 'FAIL'}")
        else:
            print(f"  Brute:   SKIPPED (n too large)")
        print(f"  Optimal: {o:.4f}  {'PASS' if abs(o - expected) < 0.001 else 'FAIL'}")
        print(f"  Best:    {be:.4f}  {'PASS' if abs(be - expected) < 0.001 else 'FAIL'}")
        print()
