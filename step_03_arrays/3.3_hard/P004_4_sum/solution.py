"""
Problem: 4 Sum
Difficulty: HARD | XP: 35
LeetCode: 18

Given an array nums of n integers and a target, return all unique quadruplets
[nums[a], nums[b], nums[c], nums[d]] such that a, b, c, d are distinct indices
and nums[a] + nums[b] + nums[c] + nums[d] == target.

The solution set must not contain duplicate quadruplets.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^4)  |  Space: O(k) where k = number of results
# ============================================================
def brute_force(nums: List[int], target: int) -> List[List[int]]:
    """
    Try all combinations of 4 indices (a, b, c, d) with a < b < c < d.
    Check if their values sum to target. Use a set to deduplicate results.
    """
    n = len(nums)
    result_set = set()
    for a in range(n):
        for b in range(a + 1, n):
            for c in range(b + 1, n):
                for d in range(c + 1, n):
                    if nums[a] + nums[b] + nums[c] + nums[d] == target:
                        quad = tuple(sorted([nums[a], nums[b], nums[c], nums[d]]))
                        result_set.add(quad)
    return [list(q) for q in result_set]


# ============================================================
# APPROACH 2: OPTIMAL — Sort + Fix Two + Two Pointers
# Time: O(n^3)  |  Space: O(k)
# ============================================================
def optimal(nums: List[int], target: int) -> List[List[int]]:
    """
    Sort the array. Fix the first element with index i, then the second with
    index j (j > i). For each (i, j) pair, use two pointers lo=j+1, hi=n-1
    to find pairs that sum to (target - nums[i] - nums[j]).
    Skip duplicate values at each level to avoid duplicate quadruplets.
    """
    nums.sort()
    n = len(nums)
    result = []

    for i in range(n - 3):
        # Skip duplicate values for the first element
        if i > 0 and nums[i] == nums[i - 1]:
            continue

        for j in range(i + 1, n - 2):
            # Skip duplicate values for the second element
            if j > i + 1 and nums[j] == nums[j - 1]:
                continue

            lo, hi = j + 1, n - 1
            while lo < hi:
                total = nums[i] + nums[j] + nums[lo] + nums[hi]
                if total == target:
                    result.append([nums[i], nums[j], nums[lo], nums[hi]])
                    # Skip duplicates for third and fourth elements
                    while lo < hi and nums[lo] == nums[lo + 1]:
                        lo += 1
                    while lo < hi and nums[hi] == nums[hi - 1]:
                        hi -= 1
                    lo += 1
                    hi -= 1
                elif total < target:
                    lo += 1
                else:
                    hi -= 1

    return result


# ============================================================
# APPROACH 3: BEST — Sort + Fix Two + Two Pointers (with early termination)
# Time: O(n^3)  |  Space: O(k)
# ============================================================
def best(nums: List[int], target: int) -> List[List[int]]:
    """
    Same as optimal, plus two pruning steps per outer loop:
    1. If the four smallest elements from current i exceed target, break (no solution possible).
    2. If the four largest elements from current i are less than target, skip to next i.
    These early exits can dramatically reduce runtime for nearly-sorted or extreme inputs.
    """
    nums.sort()
    n = len(nums)
    result = []

    for i in range(n - 3):
        if i > 0 and nums[i] == nums[i - 1]:
            continue
        # Pruning: smallest possible sum with this i
        if nums[i] + nums[i+1] + nums[i+2] + nums[i+3] > target:
            break
        # Pruning: largest possible sum with this i
        if nums[i] + nums[n-1] + nums[n-2] + nums[n-3] < target:
            continue

        for j in range(i + 1, n - 2):
            if j > i + 1 and nums[j] == nums[j - 1]:
                continue
            # Pruning: smallest possible sum with this (i, j)
            if nums[i] + nums[j] + nums[j+1] + nums[j+2] > target:
                break
            # Pruning: largest possible sum with this (i, j)
            if nums[i] + nums[j] + nums[n-1] + nums[n-2] < target:
                continue

            lo, hi = j + 1, n - 1
            while lo < hi:
                total = nums[i] + nums[j] + nums[lo] + nums[hi]
                if total == target:
                    result.append([nums[i], nums[j], nums[lo], nums[hi]])
                    while lo < hi and nums[lo] == nums[lo + 1]:
                        lo += 1
                    while lo < hi and nums[hi] == nums[hi - 1]:
                        hi -= 1
                    lo += 1
                    hi -= 1
                elif total < target:
                    lo += 1
                else:
                    hi -= 1

    return result


if __name__ == "__main__":
    print("=== 4 Sum ===")

    nums1, target1 = [1, 0, -1, 0, -2, 2], 0
    print(f"Input: nums={nums1}, target={target1}")
    print(f"Brute:   {sorted(brute_force(nums1, target1))}")
    print(f"Optimal: {optimal(nums1[:], target1)}")
    print(f"Best:    {best(nums1[:], target1)}")

    nums2, target2 = [2, 2, 2, 2, 2], 8
    print(f"\nInput: nums={nums2}, target={target2}")
    print(f"Brute:   {sorted(brute_force(nums2, target2))}")
    print(f"Optimal: {optimal(nums2[:], target2)}")
    print(f"Best:    {best(nums2[:], target2)}")

    nums3, target3 = [1, 2, 3, 4], 10
    print(f"\nInput: nums={nums3}, target={target3}")
    print(f"Brute:   {sorted(brute_force(nums3, target3))}")
    print(f"Optimal: {optimal(nums3[:], target3)}")
    print(f"Best:    {best(nums3[:], target3)}")
