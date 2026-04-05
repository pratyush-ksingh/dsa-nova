"""
Problem: Largest Coprime Divisor
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given two positive integers A and B, find the largest divisor of A that is
coprime to B (i.e., gcd(divisor, B) == 1).

Key insight: repeatedly divide A by gcd(A, B) until gcd(A, B) == 1.
The resulting A is the answer.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(A)  |  Space: O(1)
# ============================================================
def brute_force(A: int, B: int) -> int:
    """
    Enumerate all divisors of A from largest to smallest.
    Return the first divisor d such that gcd(d, B) == 1.
    """
    for d in range(A, 0, -1):
        if A % d == 0 and math.gcd(d, B) == 1:
            return d
    return 1  # 1 is always coprime to anything


# ============================================================
# APPROACH 2: OPTIMAL — Repeated GCD removal
# Time: O(log^2(A))  |  Space: O(1)
# ============================================================
def optimal(A: int, B: int) -> int:
    """
    Idea: if gcd(A, B) > 1, then A shares prime factors with B.
    Remove all such shared factors from A by repeatedly dividing A by gcd(A, B).
    When gcd(A, B) == 1, A is the largest divisor of the original A that is
    coprime to B.

    This works because we only divide A by factors that are common with B,
    so the result is still a divisor of A, and has no common factors with B.
    """
    g = math.gcd(A, B)
    while g != 1:
        A //= g
        g = math.gcd(A, B)
    return A


# ============================================================
# APPROACH 3: BEST — Same algorithm, functionally identical
# Time: O(log^2(A))  |  Space: O(1)
# ============================================================
def best(A: int, B: int) -> int:
    """
    Same repeated-GCD-removal approach. For each prime factor p of B,
    remove all occurrences of p from A. The result is the largest divisor of
    A coprime to B. Expressed as a while loop over gcd for simplicity.
    """
    while True:
        g = math.gcd(A, B)
        if g == 1:
            break
        while A % g == 0:
            A //= g
    return A


if __name__ == "__main__":
    print("=== Largest Coprime Divisor ===")
    tests = [(30, 12), (15, 3), (7, 2), (100, 10), (48, 36)]
    for A, B in tests:
        print(f"A={A}, B={B}: Brute={brute_force(A,B)}, "
              f"Optimal={optimal(A,B)}, Best={best(A,B)}")
