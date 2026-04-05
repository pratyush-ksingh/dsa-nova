"""
Problem: Jump Game (LeetCode #55)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive Backtracking
# Time: O(2^n)  |  Space: O(n) recursion
# Try all possible jumps from each position.
# ============================================================
def brute_force(nums: List[int]) -> bool:
    def can_reach(pos: int) -> bool:
        if pos >= len(nums) - 1:
            return True
        max_jump = min(nums[pos], len(nums) - 1 - pos)
        for j in range(1, max_jump + 1):
            if can_reach(pos + j):
                return True
        return False

    return can_reach(0)


# ============================================================
# APPROACH 2: OPTIMAL -- Bottom-Up DP
# Time: O(n^2)  |  Space: O(n)
# dp[i] = can we reach the last index from position i?
# ============================================================
def optimal(nums: List[int]) -> bool:
    n = len(nums)
    dp = [False] * n
    dp[n - 1] = True

    for i in range(n - 2, -1, -1):
        max_jump = min(nums[i], n - 1 - i)
        for j in range(1, max_jump + 1):
            if dp[i + j]:
                dp[i] = True
                break  # Found reachable, stop
    return dp[0]


# ============================================================
# APPROACH 3: BEST -- Greedy Max-Reach
# Time: O(n)  |  Space: O(1)
# Track farthest reachable index. Single pass.
# ============================================================
def best(nums: List[int]) -> bool:
    max_reach = 0
    n = len(nums)

    for i in range(n):
        if i > max_reach:
            return False  # Can't reach this index
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= n - 1:
            return True  # Can already reach the end

    return True


if __name__ == "__main__":
    print("=== Jump Game ===")

    nums1 = [2, 3, 1, 1, 4]
    print(f"Brute [2,3,1,1,4]:   {brute_force(nums1)}")   # True
    print(f"Optimal [2,3,1,1,4]: {optimal(nums1)}")        # True
    print(f"Best [2,3,1,1,4]:    {best(nums1)}")            # True

    nums2 = [3, 2, 1, 0, 4]
    print(f"Brute [3,2,1,0,4]:   {brute_force(nums2)}")   # False
    print(f"Optimal [3,2,1,0,4]: {optimal(nums2)}")        # False
    print(f"Best [3,2,1,0,4]:    {best(nums2)}")            # False

    # Edge cases
    print(f"Best [0]:            {best([0])}")              # True
    print(f"Best [2,0,0]:        {best([2, 0, 0])}")       # True
    print(f"Best [1,0,1]:        {best([1, 0, 1])}")       # False
