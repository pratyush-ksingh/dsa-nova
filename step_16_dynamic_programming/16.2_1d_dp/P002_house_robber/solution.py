"""
House Robber (LeetCode #198)
All 4 DP approaches following Striver's progression.
Same core problem as "Max Sum of Non-Adjacent Elements" from Batch 1.
"""

from typing import List


# ============================================================
# APPROACH 1: Recursion (Brute Force)
# Time: O(2^n) | Space: O(n)
# ============================================================
def rob_recursion(nums: List[int]) -> int:
    """Try rob/skip at each house recursively."""
    n = len(nums)

    def solve(i: int) -> int:
        if i >= n:
            return 0
        # Skip house i OR rob house i and skip i+1
        return max(solve(i + 1), nums[i] + solve(i + 2))

    return solve(0)


# ============================================================
# APPROACH 2: Memoization (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def rob_memoization(nums: List[int]) -> int:
    """Top-down with caching -- only n unique states."""
    n = len(nums)
    memo = {}

    def solve(i: int) -> int:
        if i >= n:
            return 0
        if i in memo:
            return memo[i]

        memo[i] = max(solve(i + 1), nums[i] + solve(i + 2))
        return memo[i]

    return solve(0)


# ============================================================
# APPROACH 3: Tabulation (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# dp[i] = max money from houses 0..i
# ============================================================
def rob_tabulation(nums: List[int]) -> int:
    """Bottom-up DP with array."""
    n = len(nums)
    if n == 1:
        return nums[0]

    dp = [0] * n
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])

    for i in range(2, n):
        dp[i] = max(dp[i - 1], dp[i - 2] + nums[i])

    return dp[n - 1]


# ============================================================
# APPROACH 4: Space-Optimized (Two Variables)
# Time: O(n) | Space: O(1)
# ============================================================
def rob_space_optimized(nums: List[int]) -> int:
    """
    dp[i] depends only on dp[i-1] and dp[i-2].
    Replace entire array with two variables.
    """
    n = len(nums)
    if n == 1:
        return nums[0]

    prev2 = nums[0]                       # dp[i-2]
    prev1 = max(nums[0], nums[1])         # dp[i-1]

    for i in range(2, n):
        curr = max(prev1, prev2 + nums[i])
        prev2 = prev1
        prev1 = curr

    return prev1


# ============================================================
# TESTS
# ============================================================
def run_tests():
    test_cases = [
        ([1, 2, 3, 1], 4),
        ([2, 7, 9, 3, 1], 12),
        ([2, 1, 1, 2], 4),
        ([5], 5),
        ([1, 2], 2),
        ([0, 0, 0], 0),
        ([5, 5, 5, 5], 10),
        ([1, 2, 3, 4, 5], 9),
        ([100, 1, 1, 100], 200),
        ([1, 3, 1, 3, 100], 103),
    ]

    approaches = [
        ("Recursion", rob_recursion),
        ("Memoization", rob_memoization),
        ("Tabulation", rob_tabulation),
        ("SpaceOptimized", rob_space_optimized),
    ]

    print("=== House Robber ===\n")

    all_pass = True
    for i, (nums, expected) in enumerate(test_cases):
        results = [(name, fn(nums)) for name, fn in approaches]
        passed = all(r == expected for _, r in results)
        if not passed:
            all_pass = False
        status = "PASS" if passed else "FAIL"
        result_vals = ", ".join(f"{r}" for _, r in results)
        print(f"Test {i+1}: {status} | Input: {nums} | Expected: {expected} | Got: [{result_vals}]")

    print(f"\n{'All tests passed!' if all_pass else 'Some tests FAILED!'}")


if __name__ == "__main__":
    run_tests()
