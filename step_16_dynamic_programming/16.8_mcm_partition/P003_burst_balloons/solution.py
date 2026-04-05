"""
Problem: Burst Balloons  (LeetCode 312)
Difficulty: HARD | XP: 50

You are given n balloons, indexed from 0 to n-1. Each balloon is painted with
a number on it represented by array nums. You are asked to burst all the balloons.

If you burst balloon i, you get nums[i-1] * nums[i] * nums[i+1] coins. If i-1
or i+1 is out of range, treat that balloon as having 1.

Return the maximum coins you can collect by bursting the balloons wisely.

Key insight: Instead of thinking about which balloon to burst FIRST (hard, because
it changes neighbors), think about which balloon to burst LAST within each interval
[i, j]. When balloon k is burst last in (i, j), its neighbors are i and j
(the virtual boundaries), which haven't been burst yet.

dp[i][j] = max coins from bursting all balloons strictly between i and j.
dp[i][j] = max over k in (i+1..j-1) of: nums[i]*nums[k]*nums[j] + dp[i][k] + dp[k][j]
"""
from typing import List
from itertools import permutations
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE (Try all burst orders)
# Time: O(n! * n)  |  Space: O(n)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Try every permutation of balloon indices and simulate bursting in that order.
    For each burst, add coins based on current neighbors (using a list that shrinks).
    Track maximum total coins across all orderings.
    """
    n = len(nums)
    max_coins = 0

    for order in permutations(range(n)):
        balloons = list(nums)
        total = 0
        indices = list(range(n))   # active balloon positions

        for idx in order:
            # Find current position of idx in active list
            pos = indices.index(idx)
            left  = balloons[pos - 1] if pos > 0 else 1
            right = balloons[pos + 1] if pos < len(balloons) - 1 else 1
            total += left * balloons[pos] * right
            balloons.pop(pos)
            indices.pop(pos)

        max_coins = max(max_coins, total)

    return max_coins


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down Interval DP / Memoization)
# Time: O(n^3)  |  Space: O(n^2) memo + O(n) stack
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Add virtual 1s at both ends: new_nums = [1] + nums + [1].
    dp(i, j) = max coins from bursting all balloons STRICTLY between i and j.
    For each choice of "last balloon k to burst in (i,j)":
      coins from k = new_nums[i] * new_nums[k] * new_nums[j]  (i and j are still alive)
      + dp(i, k) + dp(k, j)  (coins from left and right sub-intervals)
    Base case: j <= i+1 (no balloons between them) -> 0.
    """
    new_nums = [1] + nums + [1]
    m = len(new_nums)
    memo = {}

    def dp(i: int, j: int) -> int:
        if j - i <= 1:
            return 0
        if (i, j) in memo:
            return memo[(i, j)]
        best = 0
        for k in range(i + 1, j):
            coins = (new_nums[i] * new_nums[k] * new_nums[j]
                     + dp(i, k) + dp(k, j))
            best = max(best, coins)
        memo[(i, j)] = best
        return best

    return dp(0, m - 1)


# ============================================================
# APPROACH 3: BEST (Bottom-Up Interval DP)
# Time: O(n^3)  |  Space: O(n^2) — no recursion overhead
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same recurrence as Approach 2, built iteratively by gap size.
    dp[i][j] = max coins from bursting all balloons strictly between i and j.
    Fill by increasing gap = j - i starting from 2.
    """
    new_nums = [1] + nums + [1]
    m = len(new_nums)
    dp = [[0] * m for _ in range(m)]

    for gap in range(2, m):           # gap = j - i
        for i in range(m - gap):
            j = i + gap
            for k in range(i + 1, j):
                coins = (new_nums[i] * new_nums[k] * new_nums[j]
                         + dp[i][k] + dp[k][j])
                dp[i][j] = max(dp[i][j], coins)

    return dp[0][m - 1]


if __name__ == "__main__":
    test_cases = [
        # (nums, expected)
        ([3, 1, 5, 8], 167),
        ([1, 5],       10),
        ([1],           1),
        ([7, 9, 8, 0, 7, 1, 3, 5, 5, 2, 3], 1654),
    ]

    print("=== Burst Balloons ===\n")
    for nums, expected in test_cases:
        # brute force only for small inputs
        b  = brute_force(nums) if len(nums) <= 8 else -1
        o  = optimal(nums)
        be = best(nums)
        status = "PASS" if o == be == expected else "FAIL"
        print(f"nums={nums}")
        print(f"  Brute:   {b}")
        print(f"  Optimal: {o}")
        print(f"  Best:    {be}")
        print(f"  Expected:{expected}  [{status}]\n")
