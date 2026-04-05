"""
Problem: Trailing Zeros in Factorial
Difficulty: EASY | XP: 10
Source: InterviewBit

Given an integer n, return the number of trailing zeros in n!.
Trailing zeros are created by pairs of factors 2 and 5.
Since factors of 2 always outnumber factors of 5, count factors of 5 in n!.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n log n)  |  Space: O(n) — factorial grows astronomically
# Compute n! directly then count trailing zeros
# NOTE: Only feasible for very small n; Python big integers handle it but
#       this approach is impractical for n > ~20 in most languages.
# ============================================================
def brute_force(n: int) -> int:
    """
    Actually compute n! using Python's arbitrary-precision integers,
    then count how many times 10 divides it (i.e., count trailing zeros).
    """
    factorial = math.factorial(n)   # Python handles big ints natively
    count = 0
    while factorial % 10 == 0:
        count += 1
        factorial //= 10
    return count


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log5 n)  |  Space: O(1)
# Count factors of 5 using Legendre's formula
# ============================================================
def optimal(n: int) -> int:
    """
    Trailing zeros come from factors of 10 = 2 * 5.
    Factors of 2 always exceed factors of 5 in n!, so count factors of 5.
    Multiples of 5 contribute 1 factor, multiples of 25 contribute 2, etc.
    Formula: floor(n/5) + floor(n/25) + floor(n/125) + ...
    """
    count = 0
    power_of_5 = 5
    while power_of_5 <= n:
        count += n // power_of_5
        power_of_5 *= 5
    return count


# ============================================================
# APPROACH 3: BEST
# Time: O(log5 n)  |  Space: O(1)
# Same formula but written more compactly by repeatedly dividing n
# ============================================================
def best(n: int) -> int:
    """
    Equivalent to Optimal but avoids tracking power_of_5 separately.
    Repeatedly divide n by 5, adding the quotient each time.
    This works because:
      floor(n/5) + floor(n/25) + ... = floor(n/5) + floor(floor(n/5)/5) + ...
    """
    count = 0
    while n >= 5:
        n //= 5
        count += n
    return count


if __name__ == "__main__":
    print("=== Trailing Zeros in Factorial ===")
    test_cases = [
        (5, 1),    # 5! = 120
        (10, 2),   # 10! = 3628800
        (25, 6),   # 25 has two 5s
        (100, 24),
        (0, 0),
        (1, 0),
        (30, 7),
    ]
    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        be = best(n)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"n={n:4d} | Brute: {b:3d} | Optimal: {o:3d} | Best: {be:3d} | Expected: {expected:3d} | {status}")
