"""
Problem: Maximum Sum of Non Adjacent Elements / House Robber (LeetCode #198)
Difficulty: MEDIUM | XP: 25

Find maximum sum of non-adjacent elements in array.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def rob_recursive(nums: List[int]) -> int:
    """At each index, pick (jump to i-2) or skip (go to i-1). Take max."""
    def solve(i: int) -> int:
        if i < 0:
            return 0
        if i == 0:
            return nums[0]

        pick = nums[i] + solve(i - 2)
        skip = solve(i - 1)
        return max(pick, skip)

    return solve(len(nums) - 1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def rob_memo(nums: List[int]) -> int:
    """Cache each index's result to avoid recomputation."""
    n = len(nums)
    dp = [-1] * n

    def solve(i: int) -> int:
        if i < 0:
            return 0
        if i == 0:
            return nums[0]
        if dp[i] != -1:
            return dp[i]

        pick = nums[i] + solve(i - 2)
        skip = solve(i - 1)
        dp[i] = max(pick, skip)
        return dp[i]

    return solve(n - 1)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def rob_tab(nums: List[int]) -> int:
    """Fill dp array iteratively from base cases."""
    n = len(nums)
    if n == 1:
        return nums[0]

    dp = [0] * n
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])

    for i in range(2, n):
        dp[i] = max(
            dp[i - 1],                 # skip i
            dp[i - 2] + nums[i]        # pick i
        )

    return dp[n - 1]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n) | Space: O(1)
# ============================================================
def rob_space(nums: List[int]) -> int:
    """Only keep two previous values -- O(1) space."""
    n = len(nums)
    if n == 1:
        return nums[0]

    prev2 = nums[0]                         # dp[0]
    prev1 = max(nums[0], nums[1])           # dp[1]

    for i in range(2, n):
        curr = max(prev1, prev2 + nums[i])
        prev2 = prev1
        prev1 = curr

    return prev1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Maximum Sum of Non Adjacent Elements (House Robber) ===\n")

    test_cases = [
        ([2, 7, 9, 3, 1], 12),
        ([1, 2, 3, 1], 4),
        ([2, 1, 4, 5, 3, 1, 1, 3], 12),
        ([5], 5),
        ([3, 7], 7),
        ([5, 5, 5, 5], 10),
        ([100, 1, 100, 1], 200),
        ([1, 100, 1], 100),
    ]

    for nums, expected in test_cases:
        r = rob_recursive(nums)
        m = rob_memo(nums)
        t = rob_tab(nums)
        s = rob_space(nums)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"nums = {nums}")
        print(f"  Recursive: {r} | Memo: {m} | Tab: {t} | Space: {s}")
        print(f"  Expected: {expected} | Pass: {passes}\n")
