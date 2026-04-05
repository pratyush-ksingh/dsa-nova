"""
Problem: Count Permutations of BST
Difficulty: HARD | XP: 50
Source: InterviewBit

Given an array A of N numbers, find the number of permutations of A that
correspond to a valid BST when inserted in order. Essentially, count the
number of distinct BSTs that can be formed with values 1..N (Catalan number
variant), then multiply by the number of ways to assign actual values.

Actually: given A, you insert elements in order into a BST. Count how many
permutations of A lead to the same BST structure, modulo 10^9+7.
"""
from typing import List, Optional
from math import comb


MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursive counting via BST structure
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def brute_force(n: int) -> int:
    """
    Count the number of structurally unique BSTs with n nodes (Catalan number).
    Uses recursive formula: C(n) = sum(C(i-1) * C(n-i)) for i=1..n.
    """
    if n <= 1:
        return 1

    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1

    for nodes in range(2, n + 1):
        for root in range(1, nodes + 1):
            left = root - 1
            right = nodes - root
            dp[nodes] = (dp[nodes] + dp[left] * dp[right]) % MOD

    return dp[n]


# ============================================================
# APPROACH 2: OPTIMAL — Catalan number via binomial coefficient
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(n: int) -> int:
    """
    The nth Catalan number: C(n) = C(2n, n) / (n+1).
    Use modular inverse for division under MOD.
    """
    if n <= 1:
        return 1

    # Compute C(2n, n) mod p using Fermat's little theorem
    # First compute factorials
    fact = [1] * (2 * n + 1)
    for i in range(1, 2 * n + 1):
        fact[i] = fact[i - 1] * i % MOD

    inv_fact = [1] * (2 * n + 1)
    inv_fact[2 * n] = pow(fact[2 * n], MOD - 2, MOD)
    for i in range(2 * n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD

    catalan = fact[2 * n] * inv_fact[n] % MOD * inv_fact[n] % MOD
    catalan = catalan * pow(n + 1, MOD - 2, MOD) % MOD
    return catalan


# ============================================================
# APPROACH 3: BEST — Count permutations of given array forming same BST
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def best(arr: List[int]) -> int:
    """
    Given a specific array, count the number of permutations that produce
    the same BST when elements are inserted in order.
    Recursively: root = arr[0], split into left (< root) and right (> root).
    Ways = C(n-1, |left|) * ways(left) * ways(right).
    """
    if len(arr) <= 1:
        return 1

    root = arr[0]
    left = [x for x in arr[1:] if x < root]
    right = [x for x in arr[1:] if x > root]

    n = len(arr) - 1
    k = len(left)

    # C(n, k) mod MOD
    ways_interleave = 1
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % MOD
    inv_fact_k = pow(fact[k], MOD - 2, MOD)
    inv_fact_nk = pow(fact[n - k], MOD - 2, MOD)
    ways_interleave = fact[n] * inv_fact_k % MOD * inv_fact_nk % MOD

    return ways_interleave * best(left) % MOD * best(right) % MOD


if __name__ == "__main__":
    print("=== Count Permutations of BST ===")

    # Catalan numbers: 1, 1, 2, 5, 14, 42, ...
    for n in range(6):
        print(f"Catalan({n}): brute={brute_force(n)}, optimal={optimal(n)}")

    # Permutations forming same BST as [2,1,3]
    print(f"Same BST as [2,1,3]: {best([2, 1, 3])}")  # 1 (only [2,1,3] works)
    print(f"Same BST as [3,1,2]: {best([3, 1, 2])}")  # 1
