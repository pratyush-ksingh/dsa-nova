"""
Problem: Two Sum (LeetCode #1)
Difficulty: EASY | XP: 10

Given an array of integers nums and an integer target, return indices
of the two numbers such that they add up to target.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Nested Loops)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> List[int]:
    """Check every pair of elements."""
    n = len(nums)
    for i in range(n - 1):
        for j in range(i + 1, n):
            if nums[i] + nums[j] == target:
                return [i, j]
    return []


# ============================================================
# APPROACH 2: OPTIMAL (Two-Pass Hash Map)
# Time: O(n) | Space: O(n)
# ============================================================
def optimal(nums: List[int], target: int) -> List[int]:
    """Build a value->index map first, then look up complements."""
    index_map = {}

    # Pass 1: Build the map
    for i, val in enumerate(nums):
        index_map[val] = i

    # Pass 2: Look up complements
    for i, val in enumerate(nums):
        complement = target - val
        if complement in index_map and index_map[complement] != i:
            return [i, index_map[complement]]

    return []


# ============================================================
# APPROACH 3: BEST (Single-Pass Hash Map)
# Time: O(n) | Space: O(n)
# ============================================================
def best(nums: List[int], target: int) -> List[int]:
    """For each element, check if its complement was already seen."""
    seen = {}

    for i, val in enumerate(nums):
        complement = target - val
        if complement in seen:
            return [seen[complement], i]
        seen[val] = i

    return []


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Two Sum ===\n")

    test_cases = [
        ([2, 7, 11, 15], 9, [0, 1]),
        ([3, 2, 4], 6, [1, 2]),
        ([3, 3], 6, [0, 1]),
        ([-1, -2, -3, -4, -5], -8, [2, 4]),
        ([0, 4, 3, 0], 0, [0, 3]),
    ]

    for nums, target, expected in test_cases:
        b = brute_force(nums, target)
        o = optimal(nums, target)
        be = best(nums, target)

        # Validate by checking the sum at returned indices
        b_valid = nums[b[0]] + nums[b[1]] == target
        o_valid = nums[o[0]] + nums[o[1]] == target
        be_valid = nums[be[0]] + nums[be[1]] == target
        status = "PASS" if b_valid and o_valid and be_valid else "FAIL"

        print(f"Input: {nums}, target = {target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
