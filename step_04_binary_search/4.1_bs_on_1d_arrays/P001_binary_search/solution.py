"""
Problem: Binary Search (LeetCode #704)
Difficulty: EASY | XP: 10

Given a sorted array and a target, return the index of target or -1.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> int:
    """Scan every element until we find the target."""
    for i in range(len(nums)):
        if nums[i] == target:
            return i
    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Iterative Binary Search)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> int:
    """Iterative binary search -- halve the search space each step."""
    lo, hi = 0, len(nums) - 1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1
    return -1


# ============================================================
# APPROACH 3: BEST (Recursive Binary Search)
# Time: O(log n) | Space: O(log n) recursion stack
# ============================================================
def best(nums: List[int], target: int) -> int:
    """Recursive binary search -- elegant but uses stack space."""
    def helper(lo: int, hi: int) -> int:
        if lo > hi:
            return -1
        mid = lo + (hi - lo) // 2
        if nums[mid] == target:
            return mid
        if nums[mid] < target:
            return helper(mid + 1, hi)
        return helper(lo, mid - 1)

    return helper(0, len(nums) - 1)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Binary Search ===\n")

    test_cases = [
        ([-1, 0, 3, 5, 9, 12], 9, 4),
        ([-1, 0, 3, 5, 9, 12], 2, -1),
        ([5], 5, 0),
        ([5], -1, -1),
        ([2, 5], 5, 1),
        ([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 1, 0),
    ]

    for nums, target, expected in test_cases:
        b = brute_force(nums, target)
        o = optimal(nums, target)
        r = best(nums, target)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {nums}, target={target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Recurse:  {r}")
        print(f"  Expected: {expected}  [{status}]\n")
