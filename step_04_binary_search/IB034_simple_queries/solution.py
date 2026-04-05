"""
Problem: Simple Queries
Difficulty: HARD | XP: 50
Source: InterviewBit

For each index i in array A, compute the product (mod 1e9+7) of the
maximum element over every subarray that contains index i.

Key: contribution technique. For element A[j] which is the maximum in
subarrays of the range [L+1, R-1] (using monotonic stack for PGE/NGE),
the number of subarrays where A[j] is max AND contains i is (i-L)*(R-i).
So ans[i] *= A[j]^((i-L)*(R-i)) for each such j.
"""
from typing import List

MOD = 10**9 + 7


def power(base: int, exp: int, mod: int) -> int:
    result = 1
    base %= mod
    while exp > 0:
        if exp & 1:
            result = result * base % mod
        base = base * base % mod
        exp >>= 1
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^3)  |  Space: O(1)
# For each i, enumerate all subarrays containing i, find max, multiply.
# ============================================================
def brute_force(A: List[int]) -> List[int]:
    n = len(A)
    ans = [1] * n
    for i in range(n):
        for l in range(i + 1):
            for r in range(i, n):
                mx = max(A[l:r + 1])
                ans[i] = ans[i] * mx % MOD
    return ans


# ============================================================
# APPROACH 2: OPTIMAL — O(n^2) with precomputed max
# Time: O(n^2)  |  Space: O(n^2)
# Precompute max[l][r] using 2D DP, then multiply for each i.
# ============================================================
def optimal(A: List[int]) -> List[int]:
    n = len(A)
    # max_r[l][r] = max of A[l..r]
    max_r = [[0] * n for _ in range(n)]
    for l in range(n):
        max_r[l][l] = A[l]
        for r in range(l + 1, n):
            max_r[l][r] = max(max_r[l][r - 1], A[r])

    ans = [1] * n
    for i in range(n):
        for l in range(i + 1):
            for r in range(i, n):
                ans[i] = ans[i] * max_r[l][r] % MOD
    return ans


# ============================================================
# APPROACH 3: BEST — Monotonic Stack + Contribution
# Time: O(n^2) in this implementation (O(n log n) with Fenwick tree)
# Space: O(n)
#
# For each element A[j]:
#   - L = index of previous greater element (or -1)
#   - R = index of next greater or equal element (or n)
# A[j] contributes to position i in [L+1, R-1]:
#   exponent = (i - L) * (R - i)
#   ans[i] *= A[j]^exponent
# This correctly computes product of max for all subarrays containing i.
# ============================================================
def best(A: List[int]) -> List[int]:
    n = len(A)

    # Previous greater element indices
    pge = [-1] * n
    stack = []
    for i in range(n):
        while stack and A[stack[-1]] <= A[i]:
            stack.pop()
        pge[i] = stack[-1] if stack else -1
        stack.append(i)

    # Next greater or equal element indices
    nge = [n] * n
    stack = []
    for i in range(n - 1, -1, -1):
        while stack and A[stack[-1]] < A[i]:
            stack.pop()
        nge[i] = stack[-1] if stack else n
        stack.append(i)

    ans = [1] * n

    # For each j as the dominant maximum in its range
    for j in range(n):
        L = pge[j]
        R = nge[j]
        for i in range(L + 1, R):
            exp = (i - L) * (R - i)
            ans[i] = ans[i] * power(A[j], exp, MOD) % MOD

    return ans


if __name__ == "__main__":
    print("=== Simple Queries ===")
    tests = [
        [1, 2, 3],
        [3, 2, 1],
        [2, 2],
    ]
    for A in tests:
        bf = brute_force(A[:])
        op = optimal(A[:])
        be = best(A[:])
        match_bf_op = bf == op
        match_op_be = op == be
        print(f"A={A}")
        print(f"  Brute:   {bf}")
        print(f"  Optimal: {op} {'OK' if match_bf_op else 'MISMATCH'}")
        print(f"  Best:    {be} {'OK' if match_op_be else 'MISMATCH'}")
