"""
Problem: Power of Two Integers
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a positive integer A, return 1 if A can be expressed as B^P where
B >= 1 and P >= 2, otherwise return 0.

Special case: 1 = 1^P for any P, so return 1 for A=1.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(sqrt(A) * log(A))  |  Space: O(1)
# ============================================================
def brute_force(A: int) -> int:
    """
    Try every possible base b from 2 to sqrt(A).
    For each base b, keep multiplying (b^2, b^3, ...) until it equals A or exceeds it.
    Also check A == 1 as special case (1 = 1^2).
    """
    if A == 1:
        return 1
    for b in range(2, int(math.isqrt(A)) + 1):
        power = b * b
        while power <= A:
            if power == A:
                return 1
            power *= b
    return 0


# ============================================================
# APPROACH 2: OPTIMAL — Check each exponent with binary search
# Time: O(32 * log(A))  |  Space: O(1)
# ============================================================
def optimal(A: int) -> int:
    """
    For each exponent p from 2 to 32 (since 2^32 > 2^31), binary search
    for a base b such that b^p == A.
    Use float-based power check with careful integer rounding to avoid
    floating point errors.
    """
    if A == 1:
        return 1
    for p in range(2, 33):
        # The base b = A^(1/p); check floor and ceil
        b = int(round(A ** (1.0 / p)))
        for candidate in [b - 1, b, b + 1]:
            if candidate >= 2 and candidate ** p == A:
                return 1
    return 0


# ============================================================
# APPROACH 3: BEST — Same as optimal, clearly structured
# Time: O(32 * log(A))  |  Space: O(1)
# ============================================================
def best(A: int) -> int:
    """
    Iterate over all possible powers p (2..32). For each p, compute
    candidate base via integer root, then verify exactly with integer
    arithmetic to avoid float imprecision. Same asymptotic as optimal.
    """
    if A == 1:
        return 1

    def int_pow_check(A: int, p: int) -> bool:
        """Check if there exists integer b >= 2 such that b^p == A."""
        # Binary search for b in [2, A]
        lo, hi = 2, int(A ** (1.0 / p)) + 2
        while lo <= hi:
            mid = (lo + hi) // 2
            val = mid ** p
            if val == A:
                return True
            elif val < A:
                lo = mid + 1
            else:
                hi = mid - 1
        return False

    for p in range(2, 33):
        if int_pow_check(A, p):
            return 1
    return 0


if __name__ == "__main__":
    print("=== Power of Two Integers ===")
    tests = [1, 4, 8, 9, 16, 27, 6, 7, 64]
    for A in tests:
        print(f"A={A}: Brute={brute_force(A)}, Optimal={optimal(A)}, Best={best(A)}")
