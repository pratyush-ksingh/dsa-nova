"""
Problem: Sorted Permutation Rank
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a string A, find the rank of the string among all its permutations
sorted lexicographically. Since the answer can be large, return it modulo
(10^6 + 3).

Note: The string may have duplicate characters.
"""
from typing import List
from itertools import permutations

MOD = 10**6 + 3


def mod_factorial(n: int, mod: int) -> int:
    """Compute n! % mod."""
    result = 1
    for i in range(2, n + 1):
        result = result * i % mod
    return result


def mod_inverse(a: int, mod: int) -> int:
    """Compute modular inverse using Fermat's little theorem (mod must be prime)."""
    return pow(a, mod - 2, mod)


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n! * n)  |  Space: O(n!)
# ============================================================
def brute_force(A: str) -> int:
    """
    Generate all permutations of the string, sort them, then find the index
    of the input string A. Return (index + 1) % MOD.
    Only feasible for very short strings (n <= 8).
    """
    perms = sorted(set(permutations(A)))
    target = tuple(A)
    for i, p in enumerate(perms):
        if p == target:
            return (i + 1) % MOD
    return -1


# ============================================================
# APPROACH 2: OPTIMAL — Count smaller characters per position
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def optimal(A: str) -> int:
    """
    For each position i, count how many unused characters are smaller than A[i].
    Each such character, if placed at position i, would produce n-i-1 more
    arrangements (accounting for duplicates by dividing by frequencies of
    remaining characters).

    rank = 1 + sum over i of:
        (count of remaining chars < A[i]) * (n-i-1)! / (product of freq[c]!)
    All operations are mod 10^6+3.
    """
    n = len(A)
    rank = 1
    chars = list(A)

    for i in range(n):
        # Count characters smaller than chars[i] in chars[i+1:]
        remaining = chars[i + 1:]
        smaller_count = sum(1 for c in remaining if c < chars[i])

        # Factorial of remaining positions
        suffix_len = n - i - 1
        fact = mod_factorial(suffix_len, MOD)

        # Divide by factorials of duplicate counts in remaining
        from collections import Counter
        freq = Counter(remaining)
        denom = 1
        for cnt in freq.values():
            denom = denom * mod_factorial(cnt, MOD) % MOD

        inv_denom = mod_inverse(denom, MOD)
        rank = (rank + smaller_count * fact % MOD * inv_denom) % MOD

    return rank


# ============================================================
# APPROACH 3: BEST — Fenwick Tree (BIT) for counting smaller elements
# Time: O(n log n)  |  Space: O(sigma) where sigma = alphabet size
# ============================================================
def best(A: str) -> int:
    """
    Use a Binary Indexed Tree (BIT) over the 26-character alphabet to
    efficiently count how many of the remaining characters are less than
    the current character. This brings the inner loop from O(n) to O(log n).
    Overall O(n log n) with O(26) BIT space.
    """
    from collections import Counter

    n = len(A)
    MOD_VAL = MOD

    # Precompute factorials mod MOD
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % MOD_VAL

    # BIT over alphabet (size 26); need SIZE > 32 to handle BIT index propagation
    SIZE = 64
    bit = [0] * SIZE

    def bit_update(pos: int, delta: int):
        pos += 1  # 1-indexed
        while pos < SIZE:
            bit[pos] += delta
            pos += pos & (-pos)

    def bit_query(pos: int) -> int:
        pos += 1  # 1-indexed
        s = 0
        while pos > 0:
            s += bit[pos]
            pos -= pos & (-pos)
        return s

    # Initialize BIT with all character counts
    freq = Counter(A)
    for c, cnt in freq.items():
        bit_update(ord(c) - ord('a'), cnt)

    rank = 1

    # Precompute inverse factorials
    inv_fact = [1] * (n + 1)
    inv_fact[n] = pow(fact[n], MOD_VAL - 2, MOD_VAL)
    for i in range(n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD_VAL

    # Frequency dict for denominator tracking
    freq_remaining = Counter(A)

    for i in range(n):
        c = A[i]
        ci = ord(c) - ord('a')

        # Number of remaining chars smaller than c
        smaller = bit_query(ci - 1) if ci > 0 else 0

        # Suffix length
        suffix = n - i - 1

        # Numerator: suffix!
        numerator = fact[suffix]

        # Denominator: product of freq! for remaining chars (after removing c)
        freq_remaining[c] -= 1

        denom = 1
        for cnt in freq_remaining.values():
            if cnt > 0:
                denom = denom * fact[cnt] % MOD_VAL
        inv_denom = pow(denom, MOD_VAL - 2, MOD_VAL)

        rank = (rank + smaller * numerator % MOD_VAL * inv_denom) % MOD_VAL

        # Remove c from BIT
        bit_update(ci, -1)

    return rank


if __name__ == "__main__":
    print("=== Sorted Permutation Rank ===")
    tests = [("ABC", 1), ("BAC", 3), ("ACB", 2), ("CBA", 6)]
    for s, expected in tests:
        b = brute_force(s) if len(s) <= 8 else "N/A"
        o = optimal(s)
        be = best(s)
        print(f"String='{s}': Brute={b}, Optimal={o}, Best={be}"
              + (f" (expected {expected})" if expected else ""))
