"""
Problem: Print Prime Factors
Difficulty: EASY | XP: 10

Find all prime factors of a given number N (with multiplicity).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Every Number up to N
# Time: O(N) worst case  |  Space: O(log N)
# For each i from 2 to N, while i divides N, it's a factor.
# ============================================================
def brute_force(n: int) -> List[int]:
    factors = []
    i = 2
    while i <= n:
        while n % i == 0:
            factors.append(i)
            n //= i
        i += 1
    return factors


# ============================================================
# APPROACH 2: OPTIMAL -- Trial Division up to sqrt(N)
# Time: O(sqrt(N))  |  Space: O(log N)
# Only check divisors up to sqrt(N). If N > 1 after loop,
# it is itself a prime factor.
# ============================================================
def optimal(n: int) -> List[int]:
    factors = []
    i = 2
    while i * i <= n:
        while n % i == 0:
            factors.append(i)
            n //= i
        i += 1
    if n > 1:
        factors.append(n)  # remaining prime factor > sqrt(original N)
    return factors


# ============================================================
# APPROACH 3: BEST -- Optimized Trial Division (Skip Evens)
# Time: O(sqrt(N))  |  Space: O(log N)
# Handle 2 separately, then only check odd numbers 3,5,7,...
# Halves the number of iterations compared to Optimal.
# ============================================================
def best(n: int) -> List[int]:
    factors = []

    # Handle factor 2
    while n % 2 == 0:
        factors.append(2)
        n //= 2

    # Check odd factors from 3 to sqrt(n)
    i = 3
    while i * i <= n:
        while n % i == 0:
            factors.append(i)
            n //= i
        i += 2

    # If n > 1, it is a remaining prime factor
    if n > 1:
        factors.append(n)

    return factors


if __name__ == "__main__":
    test_cases = [60, 1, 13, 84, 100, 1024, 999999937]
    print("=== Print Prime Factors ===")

    for n in test_cases:
        print(f"N = {n}")
        print(f"  Brute Force: {brute_force(n)}")
        print(f"  Optimal:     {optimal(n)}")
        print(f"  Best:        {best(n)}")
