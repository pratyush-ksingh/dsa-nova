"""
Problem: Ways to Form Max Heap
Difficulty: HARD | XP: 50
Source: InterviewBit

Count distinct max-heaps that can be built from elements 1..n.
The largest element (n) is always the root. We decide which elements
go into the left vs right subtree, both must also be max-heaps.

Recurrence: dp[n] = C(n-1, L(n)) * dp[L(n)] * dp[n-1-L(n)]
where L(n) = size of left subtree in a complete binary tree of n nodes.
Answer modulo 1e9+7.
"""
from typing import List
from math import comb
from functools import lru_cache

MOD = 10**9 + 7


def left_subtree_size(n: int) -> int:
    """
    In a complete binary tree with n nodes, return the size of the left subtree.
    Height h = floor(log2(n)). Bottom row has n - (2^h - 1) nodes.
    Left subtree gets at most 2^(h-1) of those bottom nodes.
    """
    if n <= 1:
        return 0
    h = n.bit_length() - 1    # floor(log2(n))
    max_bottom = 1 << (h - 1)  # max bottom nodes in left subtree
    total_bottom = n - ((1 << h) - 1)
    left_bottom = min(total_bottom, max_bottom)
    return (1 << (h - 1)) - 1 + left_bottom


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive with memoization
# Time: O(N^2)  |  Space: O(N^2)
# ============================================================
def brute_force(n: int) -> int:
    """Memoized recursion: dp[n] = C(n-1, L) * dp[L] * dp[R]."""
    # Precompute Pascal's triangle mod MOD
    C = [[0] * (n + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        C[i][0] = 1
        for j in range(1, i + 1):
            C[i][j] = (C[i-1][j-1] + C[i-1][j]) % MOD

    from functools import lru_cache
    @lru_cache(maxsize=None)
    def dp(size: int) -> int:
        if size <= 1:
            return 1
        L = left_subtree_size(size)
        R = size - 1 - L
        return C[size - 1][L] * dp(L) % MOD * dp(R) % MOD

    return dp(n)


# ============================================================
# APPROACH 2: OPTIMAL - Bottom-up DP
# Time: O(N^2)  |  Space: O(N^2) for binomial table
# ============================================================
def optimal(n: int) -> int:
    """Iterative DP: compute dp[1..n] bottom-up."""
    C = [[0] * (n + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        C[i][0] = 1
        for j in range(1, i + 1):
            C[i][j] = (C[i-1][j-1] + C[i-1][j]) % MOD

    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1
    for i in range(2, n + 1):
        L = left_subtree_size(i)
        R = i - 1 - L
        dp[i] = C[i-1][L] * dp[L] % MOD * dp[R] % MOD
    return dp[n]


# ============================================================
# APPROACH 3: BEST - DP using Python's math.comb with modular reduction
# Time: O(N^2)  |  Space: O(N)
# Same logic but uses Python's big integers then applies mod.
# For competitive-use precomputed factorial / inverse factorial is ideal,
# but for clarity this uses a minimal approach.
# ============================================================
def best(n: int) -> int:
    """DP with precomputed factorials and modular inverse for C(n,r) mod p."""
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = fact[i-1] * i % MOD

    inv_fact = [1] * (n + 1)
    inv_fact[n] = pow(fact[n], MOD - 2, MOD)
    for i in range(n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD

    def comb_mod(a: int, b: int) -> int:
        if b < 0 or b > a:
            return 0
        return fact[a] * inv_fact[b] % MOD * inv_fact[a - b] % MOD

    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1
    for i in range(2, n + 1):
        L = left_subtree_size(i)
        R = i - 1 - L
        dp[i] = comb_mod(i - 1, L) * dp[L] % MOD * dp[R] % MOD
    return dp[n]


if __name__ == "__main__":
    print("=== Ways to Form Max Heap ===")
    cases = [(1,1),(2,1),(3,2),(4,3),(5,8),(6,20),(10,3360)]
    for n, exp in cases:
        b = brute_force(n)
        o = optimal(n)
        be = best(n)
        status = "OK" if b == o == be == exp else f"FAIL(b={b},o={o},be={be})"
        print(f"n={n}: {b}|{o}|{be} (exp={exp}) [{status}]")
