"""
Problem: All Divisors of a Number
Difficulty: EASY | XP: 10

Given a positive integer N, print all divisors of N in sorted order.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Every Number 1 to N
# Time: O(N)  |  Space: O(D) where D = number of divisors
# Iterate from 1 to N, check if each divides N.
# ============================================================
def brute_force(n: int) -> List[int]:
    divisors = []
    for d in range(1, n + 1):
        if n % d == 0:
            divisors.append(d)
    return divisors


# ============================================================
# APPROACH 2: OPTIMAL -- sqrt(N) Pair Collection + Sort
# Time: O(sqrt(N) + D log D)  |  Space: O(D)
# For each d up to sqrt(N), collect d and N/d, then sort.
# ============================================================
def optimal(n: int) -> List[int]:
    divisors = []
    d = 1
    while d * d <= n:
        if n % d == 0:
            divisors.append(d)
            if d != n // d:
                divisors.append(n // d)
        d += 1
    divisors.sort()
    return divisors


# ============================================================
# APPROACH 3: BEST -- sqrt(N) Two-List Trick (No Sort)
# Time: O(sqrt(N))  |  Space: O(D)
# Collect small divisors ascending, large divisors descending,
# reverse large, concatenate for sorted output.
# ============================================================
def best(n: int) -> List[int]:
    small = []
    large = []
    d = 1
    while d * d <= n:
        if n % d == 0:
            small.append(d)
            if d != n // d:
                large.append(n // d)
        d += 1
    large.reverse()
    return small + large


if __name__ == "__main__":
    test_cases = [36, 1, 12, 7, 100]

    print("=== All Divisors of a Number ===")
    for n in test_cases:
        print(f"\nN = {n}")
        print(f"  Brute Force: {brute_force(n)}")
        print(f"  Optimal:     {optimal(n)}")
        print(f"  Best:        {best(n)}")
