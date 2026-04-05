"""
Problem: Matrix Chain Multiplication
Difficulty: HARD | XP: 50

Given a chain of n matrices with dimensions represented by array arr[] where
matrix i has dimensions arr[i-1] x arr[i], find the minimum number of scalar
multiplications needed to compute the product of the entire chain.
"""
from typing import List
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE (Pure Recursion)
# Time: O(2^n)  |  Space: O(n) recursion stack
# ============================================================
def brute_force(arr: List[int]) -> int:
    """
    Try every possible parenthesization recursively.
    For n matrices there are Catalan(n-1) distinct parenthesizations.
    At each step, pick a split point k and recurse on left and right sub-chains.
    """
    n = len(arr) - 1  # number of matrices

    def recurse(i: int, j: int) -> int:
        # Base case: single matrix, zero multiplications needed
        if i == j:
            return 0
        min_cost = sys.maxsize
        # Try every split point k in range [i, j-1]
        for k in range(i, j):
            cost = (recurse(i, k)
                    + recurse(k + 1, j)
                    + arr[i - 1] * arr[k] * arr[j])
            min_cost = min(min_cost, cost)
        return min_cost

    return recurse(1, n)


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
# Time: O(n^3)  |  Space: O(n^2) dp table + O(n) stack
# ============================================================
def optimal(arr: List[int]) -> int:
    """
    Same recursion as brute force but cache results in a 2-D memo table.
    dp[i][j] = min multiplications to compute matrices i..j.
    Subproblems: O(n^2), each costs O(n) to solve -> O(n^3) total.
    """
    n = len(arr) - 1
    memo = [[-1] * (n + 1) for _ in range(n + 1)]

    def dp(i: int, j: int) -> int:
        if i == j:
            return 0
        if memo[i][j] != -1:
            return memo[i][j]
        best = sys.maxsize
        for k in range(i, j):
            cost = dp(i, k) + dp(k + 1, j) + arr[i - 1] * arr[k] * arr[j]
            best = min(best, cost)
        memo[i][j] = best
        return best

    return dp(1, n)


# ============================================================
# APPROACH 3: BEST (Bottom-Up DP on chain length)
# Time: O(n^3)  |  Space: O(n^2)  — no recursion overhead
# ============================================================
def best(arr: List[int]) -> int:
    """
    Iterative DP: fill the table by increasing sub-chain length L.
    dp[i][j] = minimum multiplications for matrices i..j (1-indexed).
    Fill diagonals from length 2 up to n so all sub-problems are solved
    before they are needed.
    """
    n = len(arr) - 1
    # dp[i][j] is 0 for i==j (single matrix), else computed below
    dp = [[0] * (n + 1) for _ in range(n + 1)]

    # L = chain length (number of matrices in the sub-chain)
    for length in range(2, n + 1):
        for i in range(1, n - length + 2):
            j = i + length - 1
            dp[i][j] = sys.maxsize
            for k in range(i, j):
                cost = dp[i][k] + dp[k + 1][j] + arr[i - 1] * arr[k] * arr[j]
                dp[i][j] = min(dp[i][j], cost)

    return dp[1][n]


if __name__ == "__main__":
    # arr[i-1] x arr[i] are dimensions of i-th matrix
    test_cases = [
        ([10, 30, 5, 60], 4500),       # Classic example: 3 matrices; (AB)C optimal
        ([40, 20, 30, 10, 30], 26000), # 4 matrices
        ([10, 20, 30], 6000),          # 2 matrices
        ([1, 2, 3, 4], 18),            # Small example
    ]

    print("=== Matrix Chain Multiplication ===\n")
    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        be = best(arr)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"arr={arr}")
        print(f"  Brute:   {b}")
        print(f"  Optimal: {o}")
        print(f"  Best:    {be}")
        print(f"  Expected:{expected}  [{status}]\n")
