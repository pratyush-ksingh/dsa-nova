"""
Problem: Sieve of Eratosthenes
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Trial division for each number
# Time: O(n * sqrt(n))  |  Space: O(1) extra (result list aside)
# ============================================================
def _is_prime(n: int) -> bool:
    if n < 2:
        return False
    if n == 2:
        return True
    if n % 2 == 0:
        return False
    i = 3
    while i * i <= n:
        if n % i == 0:
            return False
        i += 2
    return True


def brute_force(n: int) -> List[int]:
    """
    Check each number 2..n individually with trial division.
    Returns list of all primes <= n.
    """
    return [i for i in range(2, n + 1) if _is_prime(i)]


# ============================================================
# APPROACH 2: OPTIMAL — Sieve of Eratosthenes
# Time: O(n log log n)  |  Space: O(n)
# ============================================================
def optimal(n: int) -> List[int]:
    """
    Classic sieve: mark multiples of each prime as composite,
    starting from p^2 (all smaller multiples already marked).
    """
    if n < 2:
        return []
    is_prime = [True] * (n + 1)
    is_prime[0] = is_prime[1] = False
    p = 2
    while p * p <= n:
        if is_prime[p]:
            # Mark multiples starting from p*p
            for multiple in range(p * p, n + 1, p):
                is_prime[multiple] = False
        p += 1
    return [i for i in range(2, n + 1) if is_prime[i]]


# ============================================================
# APPROACH 3: BEST — Linear Sieve (each composite marked once)
# Time: O(n)  |  Space: O(n)
# Maintains a list of known primes; marks n * p[i] composite
# exactly once by stopping when p[i] divides n.
# ============================================================
def best(n: int) -> List[int]:
    """
    Linear sieve guarantees every composite is visited exactly
    once, achieving true O(n) time.
    """
    if n < 2:
        return []
    is_prime = [True] * (n + 1)
    is_prime[0] = is_prime[1] = False
    primes: List[int] = []

    for i in range(2, n + 1):
        if is_prime[i]:
            primes.append(i)
        for p in primes:
            if i * p > n:
                break
            is_prime[i * p] = False
            if i % p == 0:
                # p is the smallest prime factor of i;
                # further multiples will be handled later
                break
    return primes


if __name__ == "__main__":
    print("=== Sieve of Eratosthenes ===")
    N = 30
    print(f"Primes up to {N}:")
    print(f"Brute:   {brute_force(N)}")
    print(f"Optimal: {optimal(N)}")
    print(f"Best:    {best(N)}")

    N2 = 50
    b = best(N2)
    print(f"\nPrimes up to {N2}: {b}  (count={len(b)})")
