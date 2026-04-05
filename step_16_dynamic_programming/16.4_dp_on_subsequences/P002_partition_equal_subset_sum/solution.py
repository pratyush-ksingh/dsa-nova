"""
Problem: Partition Equal Subset Sum (LeetCode #416)
Difficulty: MEDIUM | XP: 25

Can array be partitioned into two subsets with equal sum?
Reduce to: Subset Sum = totalSum / 2
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def can_partition_recursive(nums: List[int]) -> bool:
    """Reduce to subset sum = totalSum/2, then recurse with take/not-take."""
    total = sum(nums)
    if total % 2 != 0:
        return False

    target = total // 2

    def solve(i: int, target: int) -> bool:
        if target == 0:
            return True
        if i == 0:
            return nums[0] == target

        not_take = solve(i - 1, target)
        take = False
        if nums[i] <= target:
            take = solve(i - 1, target - nums[i])

        return not_take or take

    return solve(len(nums) - 1, target)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n * target) | Space: O(n * target)
# ============================================================
def can_partition_memo(nums: List[int]) -> bool:
    """Cache (index, remaining_target) states."""
    total = sum(nums)
    if total % 2 != 0:
        return False

    target = total // 2
    n = len(nums)
    dp = {}

    def solve(i: int, target: int) -> bool:
        if target == 0:
            return True
        if i == 0:
            return nums[0] == target
        if (i, target) in dp:
            return dp[(i, target)]

        not_take = solve(i - 1, target)
        take = False
        if nums[i] <= target:
            take = solve(i - 1, target - nums[i])

        dp[(i, target)] = not_take or take
        return dp[(i, target)]

    return solve(n - 1, target)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n * target) | Space: O(n * target)
# ============================================================
def can_partition_tab(nums: List[int]) -> bool:
    """Build dp[n][target+1] table iteratively."""
    total = sum(nums)
    if total % 2 != 0:
        return False

    target = total // 2
    n = len(nums)
    dp = [[False] * (target + 1) for _ in range(n)]

    # Base case: sum = 0 always achievable
    for i in range(n):
        dp[i][0] = True

    # Base case: first element
    if nums[0] <= target:
        dp[0][nums[0]] = True

    # Fill table
    for i in range(1, n):
        for s in range(1, target + 1):
            not_take = dp[i - 1][s]
            take = False
            if nums[i] <= s:
                take = dp[i - 1][s - nums[i]]
            dp[i][s] = not_take or take

    return dp[n - 1][target]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n * target) | Space: O(target)
# ============================================================
def can_partition_space(nums: List[int]) -> bool:
    """Use two 1D arrays -- O(target) space."""
    total = sum(nums)
    if total % 2 != 0:
        return False

    target = total // 2
    n = len(nums)
    prev = [False] * (target + 1)

    # Base case
    prev[0] = True
    if nums[0] <= target:
        prev[nums[0]] = True

    for i in range(1, n):
        curr = [False] * (target + 1)
        curr[0] = True
        for s in range(1, target + 1):
            not_take = prev[s]
            take = False
            if nums[i] <= s:
                take = prev[s - nums[i]]
            curr[s] = not_take or take
        prev = curr

    return prev[target]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Partition Equal Subset Sum ===\n")

    test_cases = [
        ([1, 5, 11, 5], True),
        ([1, 2, 3, 5], False),
        ([2, 2, 1, 1], True),
        ([1, 2, 5], False),
        ([3, 3], True),
        ([1], False),
        ([1, 1, 1, 1], True),
    ]

    for nums, expected in test_cases:
        r = can_partition_recursive(nums)
        m = can_partition_memo(nums)
        t = can_partition_tab(nums)
        s = can_partition_space(nums)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"nums={nums}")
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
