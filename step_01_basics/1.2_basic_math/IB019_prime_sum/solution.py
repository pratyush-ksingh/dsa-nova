"""
Problem: Prime Sum (Goldbach's Conjecture)
Difficulty: EASY | XP: 10
Source: InterviewBit

Given an even integer n >= 4, find two prime numbers p and q such that p + q = n.
By Goldbach's conjecture, such a pair always exists for even n >= 4.

Example:
  n = 10  ->  [3, 7]  (or [5, 5])
  n = 28  ->  [5, 23]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Trial Division Check for All Pairs
# Time: O(n * sqrt(n))  |  Space: O(1)
# For every i from 2..n-2, check if both i and n-i are prime.
# isPrime uses trial division O(sqrt(n)) per number.
# ============================================================
def _is_prime_trial(num: int) -> bool:
    if num < 2:
        return False
    if num == 2:
        return True
    if num % 2 == 0:
        return False
    i = 3
    while i * i <= num:
        if num % i == 0:
            return False
        i += 2
    return True


def brute_force(n: int) -> List[int]:
    for p in range(2, n):
        q = n - p
        if _is_prime_trial(p) and _is_prime_trial(q):
            return [p, q]
    return []


# ============================================================
# APPROACH 2: OPTIMAL -- Sieve of Eratosthenes + Single Pass
# Time: O(n log log n)  |  Space: O(n)
# Build a sieve up to n, then scan from 2..n//2 for first pair.
# ============================================================
def _sieve(limit: int) -> List[bool]:
    is_prime = [True] * (limit + 1)
    is_prime[0] = is_prime[1] = False
    p = 2
    while p * p <= limit:
        if is_prime[p]:
            for multiple in range(p * p, limit + 1, p):
                is_prime[multiple] = False
        p += 1
    return is_prime


def optimal(n: int) -> List[int]:
    is_prime = _sieve(n)
    for p in range(2, n):
        if is_prime[p] and is_prime[n - p]:
            return [p, n - p]
    return []


# ============================================================
# APPROACH 3: BEST -- Sieve + Two-Pointer Style Scan
# Time: O(n log log n)  |  Space: O(n)
# Same sieve; scan only up to n//2 since pairs are symmetric.
# This avoids checking mirror duplicates and is cleaner.
# ============================================================
def best(n: int) -> List[int]:
    is_prime = _sieve(n)
    for p in range(2, n // 2 + 1):
        q = n - p
        if is_prime[p] and is_prime[q]:
            return [p, q]
    return []


if __name__ == "__main__":
    tests = [4, 10, 28, 100]
    print("=== Prime Sum (Goldbach's Conjecture) ===")
    for num in tests:
        print(f"n={num}  Brute: {brute_force(num)}  "
              f"Optimal: {optimal(num)}  Best: {best(num)}")
