"""
Jump Game II (LeetCode #45)

Find minimum number of jumps to reach the last index.
nums[i] = maximum jump length from index i.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Dynamic Programming
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(nums: List[int]) -> int:
    n = len(nums)
    if n <= 1:
        return 0

    dp = [float('inf')] * n
    dp[0] = 0

    for i in range(1, n):
        for j in range(i):
            if j + nums[j] >= i and dp[j] != float('inf'):
                dp[i] = min(dp[i], dp[j] + 1)

    return dp[n - 1]


# ============================================================
# APPROACH 2: OPTIMAL -- BFS level-by-level greedy
# Time: O(N)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    n = len(nums)
    if n <= 1:
        return 0

    jumps = 0
    current_end = 0
    farthest = 0

    for i in range(n - 1):  # don't process last index
        farthest = max(farthest, i + nums[i])

        if i == current_end:
            jumps += 1
            current_end = farthest
            if current_end >= n - 1:
                break

    return jumps


# ============================================================
# APPROACH 3: BEST -- Same greedy, cleanest form
# Time: O(N)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    n = len(nums)
    if n <= 1:
        return 0

    jumps = cur_end = farthest = 0

    for i in range(n - 1):
        farthest = max(farthest, i + nums[i])
        if i == cur_end:
            jumps += 1
            cur_end = farthest
            if cur_end >= n - 1:
                return jumps

    return jumps


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    tests = [
        ([2, 3, 1, 1, 4], 2),
        ([2, 3, 0, 1, 4], 2),
        ([1, 1, 1, 1], 3),
        ([0], 0),
        ([1, 2], 1),
        ([5, 4, 3, 2, 1], 1),
    ]

    print("=== Jump Game II ===")
    for nums, expected in tests:
        b = brute_force(nums)
        o = optimal(nums)
        s = best(nums)
        print(f"{str(nums):25s} -> Brute: {b} | Optimal: {o} | Best: {s} (expected {expected})")
