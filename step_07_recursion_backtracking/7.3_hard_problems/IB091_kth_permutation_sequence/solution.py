"""
Problem: Kth Permutation Sequence
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given n and k, return the kth permutation sequence of 1..n (1-indexed).
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE - Generate all permutations, pick kth
# Time: O(N! * N)  |  Space: O(N!)
# ============================================================
def brute_force(n: int, k: int) -> str:
    """Generate all permutations of 1..n sorted and return kth."""
    from itertools import permutations
    perms = sorted(permutations(range(1, n + 1)))
    return ''.join(map(str, perms[k - 1]))


# ============================================================
# APPROACH 2: OPTIMAL - Factorial Number System (direct)
# Time: O(N^2)  |  Space: O(N)
# Avoid generating all perms. Determine each digit position by position.
# ============================================================
def optimal(n: int, k: int) -> str:
    """
    Factorial number system:
    k -= 1 to make 0-indexed.
    At position i (from n down to 1):
      idx = k // (i-1)!   -> pick the idx-th remaining digit
      k  %= (i-1)!
    """
    digits = list(range(1, n + 1))
    fact = [1] * n
    for i in range(1, n):
        fact[i] = fact[i - 1] * i

    k -= 1  # 0-indexed
    result = []
    for i in range(n - 1, -1, -1):
        idx = k // fact[i]
        result.append(str(digits[idx]))
        digits.pop(idx)
        k %= fact[i]
    return ''.join(result)


# ============================================================
# APPROACH 3: BEST - Same as optimal using math.factorial (cleaner)
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def best(n: int, k: int) -> str:
    """Clean implementation using math.factorial."""
    digits = list(range(1, n + 1))
    k -= 1
    result = []
    for i in range(n, 0, -1):
        f = math.factorial(i - 1)
        idx = k // f
        result.append(str(digits[idx]))
        digits.pop(idx)
        k %= f
    return ''.join(result)


if __name__ == "__main__":
    print("=== Kth Permutation Sequence ===")
    # n=3: [123,132,213,231,312,321]
    tests = [(3, 3), (3, 6), (4, 9), (1, 1)]
    expected = ["213", "321", "2314", "1"]
    for (n, k), exp in zip(tests, expected):
        b = brute_force(n, k)
        o = optimal(n, k)
        be = best(n, k)
        status = "OK" if b == o == be == exp else "FAIL"
        print(f"n={n},k={k}: {b} | {o} | {be}  [{status}]")
