"""
Problem: House Robber II (LeetCode #213)
Difficulty: MEDIUM | XP: 25

Houses in a circle. Run House Robber twice: skip first OR skip last.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def rob_recursive(nums: List[int]) -> int:
    """Break circle into two linear problems, solve each recursively."""
    n = len(nums)
    if n == 1:
        return nums[0]

    def solve(i: int, start: int) -> int:
        if i < start:
            return 0
        if i == start:
            return nums[start]
        return max(solve(i - 1, start), nums[i] + solve(i - 2, start))

    return max(solve(n - 2, 0), solve(n - 1, 1))


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def rob_memo(nums: List[int]) -> int:
    """Cache states for each linear sub-problem."""
    n = len(nums)
    if n == 1:
        return nums[0]

    def solve_linear(start: int, end: int) -> int:
        dp = {}

        def solve(i: int) -> int:
            if i < start:
                return 0
            if i == start:
                return nums[start]
            if i in dp:
                return dp[i]
            dp[i] = max(solve(i - 1), nums[i] + solve(i - 2))
            return dp[i]

        return solve(end)

    return max(solve_linear(0, n - 2), solve_linear(1, n - 1))


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def rob_tab(nums: List[int]) -> int:
    """Build dp array for each linear sub-problem."""
    n = len(nums)
    if n == 1:
        return nums[0]

    def rob_linear(start: int, end: int) -> int:
        length = end - start + 1
        if length == 1:
            return nums[start]

        dp = [0] * length
        dp[0] = nums[start]
        dp[1] = max(nums[start], nums[start + 1])

        for i in range(2, length):
            dp[i] = max(dp[i - 1], nums[start + i] + dp[i - 2])

        return dp[length - 1]

    return max(rob_linear(0, n - 2), rob_linear(1, n - 1))


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n) | Space: O(1)
# ============================================================
def rob_space(nums: List[int]) -> int:
    """Use two variables per linear sub-problem. O(1) space."""
    n = len(nums)
    if n == 1:
        return nums[0]

    def rob_linear(start: int, end: int) -> int:
        prev2, prev1 = 0, 0
        for i in range(start, end + 1):
            curr = max(prev1, nums[i] + prev2)
            prev2 = prev1
            prev1 = curr
        return prev1

    return max(rob_linear(0, n - 2), rob_linear(1, n - 1))


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== House Robber II ===\n")

    test_cases = [
        ([2, 3, 2], 3),
        ([1, 2, 3, 1], 4),
        ([1, 2, 3], 3),
        ([0], 0),
        ([5], 5),
        ([1, 2], 2),
        ([3, 3, 3, 3], 6),
        ([1, 100, 1, 100], 200),
    ]

    for nums, expected in test_cases:
        r = rob_recursive(nums)
        m = rob_memo(nums)
        t = rob_tab(nums)
        s = rob_space(nums)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"nums={nums}")
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
