"""
Problem: Find Prime Factorization using Sieve
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Dict


# ============================================================
# APPROACH 1: BRUTE FORCE — Trial division per query
# Time: O(sqrt(n)) per query  |  Space: O(log n) result
# ============================================================
def brute_force(n: int) -> List[int]:
    """
    Divide n by 2, then odd numbers up to sqrt(n).
    Returns sorted list of prime factors (with repetition).
    e.g. brute_force(12) -> [2, 2, 3]
    """
    factors: List[int] = []
    # Divide out 2s
    while n % 2 == 0:
        factors.append(2)
        n //= 2
    # Odd divisors
    d = 3
    while d * d <= n:
        while n % d == 0:
            factors.append(d)
            n //= d
        d += 2
    if n > 1:
        factors.append(n)
    return factors


# ============================================================
# APPROACH 2: OPTIMAL — Smallest Prime Factor (SPF) sieve
# Preprocessing: O(n log log n)  |  Per query: O(log n)
# Build SPF[i] = smallest prime that divides i, then factor
# any m <= n in O(log m) by repeated lookup.
# ============================================================
def build_spf(limit: int) -> List[int]:
    """Build Smallest Prime Factor sieve up to `limit`."""
    spf = list(range(limit + 1))   # spf[i] = i initially
    p = 2
    while p * p <= limit:
        if spf[p] == p:             # p is prime
            for multiple in range(p * p, limit + 1, p):
                if spf[multiple] == multiple:   # not yet assigned
                    spf[multiple] = p
        p += 1
    return spf


def optimal(n: int, spf: List[int]) -> List[int]:
    """
    Factorise n using prebuilt SPF table.
    Divide by spf[n] repeatedly until n == 1.
    """
    factors: List[int] = []
    while n > 1:
        factors.append(spf[n])
        n //= spf[n]
    return factors


# ============================================================
# APPROACH 3: BEST — SPF sieve returning factor -> exponent map
# Same preprocessing, result as a clean {prime: exp} dict.
# ============================================================
def best(n: int, spf: List[int]) -> Dict[int, int]:
    """
    Same O(log n) factorisation but returns a dictionary
    {prime: exponent} for convenient downstream use.
    e.g. best(12, spf) -> {2: 2, 3: 1}
    """
    factors: Dict[int, int] = {}
    while n > 1:
        p = spf[n]
        while n % p == 0:
            factors[p] = factors.get(p, 0) + 1
            n //= p
    return factors


if __name__ == "__main__":
    print("=== Find Prime Factorization using Sieve ===")
    LIMIT = 100
    spf_table = build_spf(LIMIT)

    test_cases = [12, 36, 97, 84, 100]
    for num in test_cases:
        bf = brute_force(num)
        opt = optimal(num, spf_table)
        bst = best(num, spf_table)
        print(f"n={num:3d} | Brute: {bf} | Optimal: {opt} | Best: {bst}")
