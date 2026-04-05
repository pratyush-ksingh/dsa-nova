"""
Longest Increasing Subsequence (LeetCode #300)
All 4 DP approaches following Striver's progression + Binary Search optimization
"""

from typing import List
import bisect


# ============================================================
# APPROACH 1: Recursion (Brute Force)
# Time: O(2^n) | Space: O(n)
# ============================================================
def lis_recursion(nums: List[int]) -> int:
    """Try all subsequences -- take or skip at each index."""
    n = len(nums)

    def solve(idx: int, prev_idx: int) -> int:
        # Base case: exhausted array
        if idx == n:
            return 0

        # Choice 1: Skip current
        not_take = solve(idx + 1, prev_idx)

        # Choice 2: Take current (only if strictly greater than previous)
        take = 0
        if prev_idx == -1 or nums[idx] > nums[prev_idx]:
            take = 1 + solve(idx + 1, idx)

        return max(take, not_take)

    return solve(0, -1)


# ============================================================
# APPROACH 2: Memoization (Top-Down DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def lis_memoization(nums: List[int]) -> int:
    """Top-down recursion with caching."""
    n = len(nums)
    memo = {}

    def solve(idx: int, prev_idx: int) -> int:
        if idx == n:
            return 0

        if (idx, prev_idx) in memo:
            return memo[(idx, prev_idx)]

        not_take = solve(idx + 1, prev_idx)

        take = 0
        if prev_idx == -1 or nums[idx] > nums[prev_idx]:
            take = 1 + solve(idx + 1, idx)

        memo[(idx, prev_idx)] = max(take, not_take)
        return memo[(idx, prev_idx)]

    return solve(0, -1)


# ============================================================
# APPROACH 3: Tabulation (Bottom-Up DP)
# Time: O(n^2) | Space: O(n)
# dp[i] = length of LIS ending at index i
# ============================================================
def lis_tabulation(nums: List[int]) -> int:
    """Classic O(n^2) bottom-up DP."""
    n = len(nums)
    dp = [1] * n  # Every element alone is LIS of length 1

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)

    return max(dp)


# ============================================================
# APPROACH 4: Space-Optimized -- Binary Search (Patience Sorting)
# Time: O(n log n) | Space: O(n)
# ============================================================
def lis_binary_search(nums: List[int]) -> int:
    """
    Maintain tails[] where tails[i] = smallest tail element of all
    increasing subsequences of length i+1.

    Key insight: tails is always sorted, so we can binary search.
    - If num > tails[-1]: append (extends longest IS)
    - Else: replace leftmost tails[j] >= num (keeps smallest tail)
    """
    tails = []

    for num in nums:
        # bisect_left finds leftmost position where tails[pos] >= num
        pos = bisect.bisect_left(tails, num)

        if pos == len(tails):
            tails.append(num)  # Extend
        else:
            tails[pos] = num  # Replace to keep smallest tail

    return len(tails)


# ============================================================
# TESTS
# ============================================================
def run_tests():
    test_cases = [
        ([10, 9, 2, 5, 3, 7, 101, 18], 4),
        ([0, 1, 0, 3, 2, 3], 4),
        ([7, 7, 7, 7, 7], 1),
        ([5], 1),
        ([1, 2, 3, 4, 5], 5),
        ([5, 4, 3, 2, 1], 1),
        ([-2, -1, 0], 3),
        ([3, 1, 4, 1, 5, 9, 2, 6], 5),
    ]

    approaches = [
        ("Recursion", lis_recursion),
        ("Memoization", lis_memoization),
        ("Tabulation", lis_tabulation),
        ("BinarySearch", lis_binary_search),
    ]

    print("=== Longest Increasing Subsequence ===\n")

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
