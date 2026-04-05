"""
Problem: Partition into Two Subsets Min Diff
Difficulty: HARD | XP: 50

Given an array of integers, partition it into two subsets such that
the absolute difference between their sums is minimized.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^n)  |  Space: O(n) recursion stack
# Generate every possible subset, compute both halves, track min diff
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Try all 2^n ways to assign each element to subset S1 or S2.
    For each partition compute |sum(S1) - sum(S2)| and track minimum.
    """
    total = sum(nums)
    n = len(nums)
    min_diff = float('inf')

    def recurse(index: int, s1: int) -> None:
        nonlocal min_diff
        if index == n:
            s2 = total - s1
            min_diff = min(min_diff, abs(s1 - s2))
            return
        # Include nums[index] in S1
        recurse(index + 1, s1 + nums[index])
        # Exclude from S1 (it goes to S2)
        recurse(index + 1, s1)

    recurse(0, 0)
    return min_diff


# ============================================================
# APPROACH 2: OPTIMAL — 2D DP boolean subset-sum
# Time: O(n * totalSum)  |  Space: O(n * totalSum)
# Build reachable-sum table, scan last row near totalSum/2
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    dp[i][j] = True if we can achieve sum j using the first i elements.
    After filling the table, look at dp[n][0..totalSum//2] and find
    the largest achievable s1 <= totalSum//2; answer = totalSum - 2*s1.
    """
    total = sum(nums)
    n = len(nums)

    # dp[i][j]: can we pick sum j from nums[0..i-1]?
    dp = [[False] * (total + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        dp[i][0] = True  # sum=0 always achievable (pick nothing)

    for i in range(1, n + 1):
        for j in range(total + 1):
            # Don't take nums[i-1]
            dp[i][j] = dp[i - 1][j]
            # Take nums[i-1] if feasible
            if j >= nums[i - 1]:
                dp[i][j] = dp[i][j] or dp[i - 1][j - nums[i - 1]]

    # Find largest s1 <= total//2 that is achievable
    min_diff = float('inf')
    for s1 in range(total // 2 + 1):
        if dp[n][s1]:
            min_diff = min(min_diff, total - 2 * s1)

    return min_diff


# ============================================================
# APPROACH 3: BEST — 1D bitset DP
# Time: O(n * totalSum)  |  Space: O(totalSum)
# Compress the boolean DP table to a single integer used as a bitset
# ============================================================
def best(nums: List[int]) -> int:
    """
    Use a Python integer as a bitset where bit j being set means
    sum j is achievable.  For each number x, dp |= (dp << x).
    This is the most memory-efficient O(totalSum/64) in practice.
    """
    total = sum(nums)
    # Start: only sum 0 is reachable → bit 0 set
    dp = 1  # binary ...0001
    for x in nums:
        dp |= (dp << x)

    # Find largest s1 <= total//2 with bit s1 set
    min_diff = float('inf')
    for s1 in range(total // 2, -1, -1):
        if dp & (1 << s1):
            min_diff = total - 2 * s1
            break

    return min_diff


if __name__ == "__main__":
    test_cases = [
        ([1, 6, 11, 5], 1),        # S1={1,5,6}=12, S2={11}=11 → diff=1
        ([3, 9, 7, 3], 2),         # S1={3,7}=10, S2={9,3}=12 → diff=2
        ([1, 2, 3, 4], 0),         # S1={1,4}=5, S2={2,3}=5 → diff=0
        ([10], 10),                # only one element
        ([1, 1], 0),
    ]

    print("=== Partition into Two Subsets Min Diff ===")
    for nums, expected in test_cases:
        b = brute_force(nums)
        o = optimal(nums)
        bst = best(nums)
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] nums={nums} | brute={b} optimal={o} best={bst} | expected={expected}")
