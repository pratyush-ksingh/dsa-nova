"""
Problem: Target Sum (LeetCode 494)
Difficulty: MEDIUM | XP: 25

You are given an integer array nums and an integer target.
Assign either '+' or '-' to each element and return the number of
ways to reach exactly `target`.

Key reduction: let P = set of elements with '+', N = set with '-'
  sum(P) - sum(N) = target
  sum(P) + sum(N) = total
  => 2 * sum(P) = total + target
  => sum(P) = (total + target) / 2

So the problem reduces to: count subsets whose sum equals (total+target)/2.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — plain recursion
# Time: O(2^n)  |  Space: O(n) recursion stack
# Try every +/- assignment exhaustively
# ============================================================
def brute_force(nums: List[int], target: int) -> int:
    """
    At each index decide '+' or '-'. When all indices are consumed,
    check if the running total equals the target.
    """
    n = len(nums)

    def recurse(index: int, current_sum: int) -> int:
        if index == n:
            return 1 if current_sum == target else 0
        # Add the current number
        add = recurse(index + 1, current_sum + nums[index])
        # Subtract the current number
        sub = recurse(index + 1, current_sum - nums[index])
        return add + sub

    return recurse(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — 2D DP (count subsets with given sum)
# Time: O(n * S)  |  Space: O(n * S)   where S = (total+target)/2
# ============================================================
def optimal(nums: List[int], target: int) -> int:
    """
    Reduce to: count subsets with sum = (total + target) / 2.
    dp[i][j] = number of subsets of nums[0..i-1] that sum to j.
    """
    total = sum(nums)
    # Impossible cases
    if (total + target) % 2 != 0:
        return 0
    if abs(target) > total:
        return 0

    req = (total + target) // 2
    n = len(nums)

    # dp[i][j]: count of subsets from first i elements summing to j
    dp = [[0] * (req + 1) for _ in range(n + 1)]
    dp[0][0] = 1  # one way to achieve sum 0 with 0 elements: pick nothing

    for i in range(1, n + 1):
        for j in range(req + 1):
            dp[i][j] = dp[i - 1][j]          # don't take nums[i-1]
            if j >= nums[i - 1]:
                dp[i][j] += dp[i - 1][j - nums[i - 1]]  # take nums[i-1]

    return dp[n][req]


# ============================================================
# APPROACH 3: BEST — 1D DP (space-optimised count)
# Time: O(n * S)  |  Space: O(S)
# Compress 2D table to a single row by traversing j in reverse
# ============================================================
def best(nums: List[int], target: int) -> int:
    """
    Same recurrence as Approach 2 but using a single 1D array.
    Iterate j from req down to nums[i] to avoid counting an element twice.
    """
    total = sum(nums)
    if (total + target) % 2 != 0:
        return 0
    if abs(target) > total:
        return 0

    req = (total + target) // 2
    dp = [0] * (req + 1)
    dp[0] = 1  # empty subset sums to 0

    for x in nums:
        for j in range(req, x - 1, -1):
            dp[j] += dp[j - x]

    return dp[req]


if __name__ == "__main__":
    test_cases = [
        ([1, 1, 1, 1, 1], 3, 5),
        ([1], 1, 1),
        ([1], 2, 0),
        ([0, 0, 0, 0, 0, 0, 0, 0, 1], 1, 256),  # zeros double count paths
        ([1, 2, 3, 1], 3, 2),
    ]

    print("=== Target Sum ===")
    for nums, tgt, expected in test_cases:
        b = brute_force(nums, tgt)
        o = optimal(nums, tgt)
        bs = best(nums, tgt)
        status = "PASS" if b == o == bs == expected else "FAIL"
        print(f"[{status}] nums={nums} target={tgt} | brute={b} optimal={o} best={bs} | expected={expected}")
