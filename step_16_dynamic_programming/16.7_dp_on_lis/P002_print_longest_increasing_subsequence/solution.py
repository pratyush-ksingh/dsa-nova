"""
Problem: Print Longest Increasing Subsequence
Difficulty: MEDIUM | XP: 25

Not just LENGTH but actually PRINT the LIS.
Backtrack using parent array from O(n^2) tabulation.
Approaches: Recursion -> Memo -> Tab + Parent -> Binary Search (length)
"""
from typing import List
import bisect


# ============================================================
# APPROACH 1: PLAIN RECURSION (Length only)
# Time: O(2^n) | Space: O(n)
# ============================================================
def lis_recursive(nums: List[int]) -> int:
    """Try take/skip for each element with prev index tracking."""
    n = len(nums)

    def solve(i: int, prev_idx: int) -> int:
        if i == n:
            return 0

        not_take = solve(i + 1, prev_idx)
        take = 0
        if prev_idx == -1 or nums[i] > nums[prev_idx]:
            take = 1 + solve(i + 1, i)

        return max(take, not_take)

    return solve(0, -1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def lis_memo(nums: List[int]) -> int:
    """Cache (i, prev_idx) states."""
    n = len(nums)
    dp = {}

    def solve(i: int, prev_idx: int) -> int:
        if i == n:
            return 0
        if (i, prev_idx) in dp:
            return dp[(i, prev_idx)]

        not_take = solve(i + 1, prev_idx)
        take = 0
        if prev_idx == -1 or nums[i] > nums[prev_idx]:
            take = 1 + solve(i + 1, i)

        dp[(i, prev_idx)] = max(take, not_take)
        return dp[(i, prev_idx)]

    return solve(0, -1)


# ============================================================
# APPROACH 3: TABULATION + PARENT TRACKING (PRINT the LIS)
# Time: O(n^2) | Space: O(n)
# ============================================================
def print_lis(nums: List[int]) -> List[int]:
    """dp[i] = LIS length ending at i, parent[i] = prev index in LIS."""
    n = len(nums)
    dp = [1] * n          # Each element is an LIS of length 1
    parent = list(range(n))  # Each element is its own parent

    max_len = 1
    max_idx = 0

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i] and dp[j] + 1 > dp[i]:
                dp[i] = dp[j] + 1
                parent[i] = j
        if dp[i] > max_len:
            max_len = dp[i]
            max_idx = i

    # Backtrack from max_idx to reconstruct LIS
    result = []
    idx = max_idx
    while parent[idx] != idx:
        result.append(nums[idx])
        idx = parent[idx]
    result.append(nums[idx])  # Add the starting element

    result.reverse()
    return result


# ============================================================
# APPROACH 4: BINARY SEARCH (Length only) - O(n log n)
# Time: O(n log n) | Space: O(n)
# ============================================================
def lis_binary_search(nums: List[int]) -> int:
    """Patience sorting: maintain tails array with binary search."""
    tails = []

    for num in nums:
        pos = bisect.bisect_left(tails, num)
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num

    return len(tails)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Print Longest Increasing Subsequence ===\n")

    test_cases = [
        ([10, 9, 2, 5, 3, 7, 101, 18], 4),
        ([0, 1, 0, 3, 2, 3], 4),
        ([7, 7, 7, 7], 1),
        ([5, 4, 3, 2, 1], 1),
        ([1, 2, 3, 4], 4),
        ([5], 1),
        ([-1, 3, -2, 5], 3),
    ]

    for nums, exp_len in test_cases:
        r = lis_recursive(nums)
        m = lis_memo(nums)
        printed = print_lis(nums)
        b = lis_binary_search(nums)

        passes = (r == exp_len and m == exp_len
                  and len(printed) == exp_len and b == exp_len)
        print(f"nums={nums}")
        print(f"  Rec={r} | Memo={m} | BS={b} | Print={printed}")
        print(f"  Expected len={exp_len} | Pass={passes}\n")
