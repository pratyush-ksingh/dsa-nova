"""
Problem: Subset Sum Equal to Target
Difficulty: MEDIUM | XP: 25

Given array and target, check if any subset sums to target.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def subset_sum_recursive(arr: List[int], target: int) -> bool:
    """For each element: include (reduce target) or exclude."""
    def solve(i: int, target: int) -> bool:
        if target == 0:
            return True
        if i == 0:
            return arr[0] == target

        # Exclude arr[i]
        not_take = solve(i - 1, target)

        # Include arr[i]
        take = False
        if arr[i] <= target:
            take = solve(i - 1, target - arr[i])

        return not_take or take

    return solve(len(arr) - 1, target)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n * target) | Space: O(n * target)
# ============================================================
def subset_sum_memo(arr: List[int], target: int) -> bool:
    """Cache (index, remaining_target) states."""
    n = len(arr)
    dp = {}

    def solve(i: int, target: int) -> bool:
        if target == 0:
            return True
        if i == 0:
            return arr[0] == target
        if (i, target) in dp:
            return dp[(i, target)]

        not_take = solve(i - 1, target)
        take = False
        if arr[i] <= target:
            take = solve(i - 1, target - arr[i])

        dp[(i, target)] = not_take or take
        return dp[(i, target)]

    return solve(n - 1, target)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n * target) | Space: O(n * target)
# ============================================================
def subset_sum_tab(arr: List[int], target: int) -> bool:
    """Build dp[n][target+1] table iteratively."""
    n = len(arr)
    dp = [[False] * (target + 1) for _ in range(n)]

    # Base case: sum = 0 always achievable
    for i in range(n):
        dp[i][0] = True

    # Base case: first element
    if arr[0] <= target:
        dp[0][arr[0]] = True

    # Fill table
    for i in range(1, n):
        for s in range(1, target + 1):
            not_take = dp[i - 1][s]
            take = False
            if arr[i] <= s:
                take = dp[i - 1][s - arr[i]]
            dp[i][s] = not_take or take

    return dp[n - 1][target]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n * target) | Space: O(target)
# ============================================================
def subset_sum_space(arr: List[int], target: int) -> bool:
    """Use single 1D array -- O(target) space."""
    n = len(arr)
    prev = [False] * (target + 1)

    # Base case
    prev[0] = True
    if arr[0] <= target:
        prev[arr[0]] = True

    for i in range(1, n):
        curr = [False] * (target + 1)
        curr[0] = True  # empty subset
        for s in range(1, target + 1):
            not_take = prev[s]
            take = False
            if arr[i] <= s:
                take = prev[s - arr[i]]
            curr[s] = not_take or take
        prev = curr

    return prev[target]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Subset Sum Equal to Target ===\n")

    test_cases = [
        ([1, 2, 3, 4], 4, True),
        ([2, 3, 7, 8, 10], 11, True),
        ([1, 2, 3], 7, False),
        ([6, 1, 2, 1], 4, True),
        ([5], 5, True),
        ([5], 3, False),
        ([0, 1, 2], 3, True),
    ]

    for arr, target, expected in test_cases:
        r = subset_sum_recursive(arr, target)
        m = subset_sum_memo(arr, target)
        t = subset_sum_tab(arr, target)
        s = subset_sum_space(arr, target)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"arr={arr}, target={target}")
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
