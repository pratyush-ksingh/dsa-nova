"""
Problem: Potions
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

N potions in a row. Mixing two adjacent potions produces strength = sum.
If the resulting strength is NEGATIVE, you must drink it (adds |strength| penalty).
Find the minimum penalty to merge all potions into one.

Interval DP:
  prefix[i] = sum of potions[0..i-1]
  sum(i,j)  = prefix[j+1] - prefix[i]
  dp[i][j]  = min penalty to fully merge potions i..j

Recurrence: dp[i][j] = min over k in [i,j-1] of:
  dp[i][k] + dp[k+1][j] + max(0, -sum(i,j))

Note: penalty(sum(i,j)) is the same for all k, so it's added once per (i,j).

Real-life use case: Optimal merge-order problems in simulations where
combining negative-valued entities incurs mandatory cost.
"""
from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^3) amortized (without memo, O(3^n) worst recursion)  |  Space: O(n^2 stack)
# Pure recursion without memoization - demonstrates structure, impractical for large n.
# ============================================================
def brute_force(p: List[int]) -> int:
    n = len(p)
    if n <= 1:
        return 0
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + p[i]

    def subsum(i: int, j: int) -> int:
        return prefix[j + 1] - prefix[i]

    def solve(i: int, j: int) -> int:
        if i == j:
            return 0
        pen = max(0, -subsum(i, j))
        best = float('inf')
        for k in range(i, j):
            cost = solve(i, k) + solve(k + 1, j) + pen
            best = min(best, cost)
        return best

    return solve(0, n - 1)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n^3)  |  Space: O(n^2)
# Top-down with memoization. Each (i,j) subproblem computed exactly once.
# ============================================================
def optimal(p: List[int]) -> int:
    n = len(p)
    if n <= 1:
        return 0
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + p[i]

    memo = {}

    def solve(i: int, j: int) -> int:
        if i == j:
            return 0
        if (i, j) in memo:
            return memo[(i, j)]
        pen = max(0, -(prefix[j + 1] - prefix[i]))
        best = float('inf')
        for k in range(i, j):
            cost = solve(i, k) + solve(k + 1, j) + pen
            best = min(best, cost)
        memo[(i, j)] = best
        return best

    return solve(0, n - 1)


# ============================================================
# APPROACH 3: BEST
# Time: O(n^3)  |  Space: O(n^2)
# Bottom-up tabulation. Fill dp by increasing interval length.
# No recursion overhead; straightforward cache-friendly iteration.
# ============================================================
def best(p: List[int]) -> int:
    n = len(p)
    if n <= 1:
        return 0

    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + p[i]

    dp = [[0] * n for _ in range(n)]
    # Base: dp[i][i] = 0 (single potion, no merge needed)

    for length in range(2, n + 1):          # interval length
        for i in range(n - length + 1):
            j = i + length - 1
            range_sum = prefix[j + 1] - prefix[i]
            pen = max(0, -range_sum)
            dp[i][j] = float('inf')
            for k in range(i, j):
                cost = dp[i][k] + dp[k + 1][j] + pen
                dp[i][j] = min(dp[i][j], cost)

    return dp[0][n - 1]


if __name__ == "__main__":
    print("=== Potions ===")

    # Test 1: [1, -1, -1]
    # Best: merge [1,-1] => sum=0, no penalty; then merge [0,-1] => sum=-1, penalty=1 => total=1
    t1 = [1, -1, -1]
    print(f"Test 1 [1,-1,-1]:")
    print(f"  Brute:   {brute_force(t1)}")   # 1
    print(f"  Optimal: {optimal(t1)}")        # 1
    print(f"  Best:    {best(t1)}")            # 1

    # Test 2: [1, 2, 3] all positive
    t2 = [1, 2, 3]
    print(f"\nTest 2 [1,2,3] (all positive):")
    print(f"  Brute:   {brute_force(t2)}")   # 0
    print(f"  Optimal: {optimal(t2)}")        # 0
    print(f"  Best:    {best(t2)}")            # 0

    # Test 3: [-1, -2, -3]
    # dp[0][1]=3, dp[1][2]=5; dp[0][2]: split at 1 => 3+0+6=9, split at 0 => 0+5+6=11 => 9
    t3 = [-1, -2, -3]
    print(f"\nTest 3 [-1,-2,-3]:")
    print(f"  Brute:   {brute_force(t3)}")   # 9
    print(f"  Optimal: {optimal(t3)}")        # 9
    print(f"  Best:    {best(t3)}")            # 9

    # Test 4: single element
    t4 = [5]
    print(f"\nTest 4 single [5]: {best(t4)}")   # 0
