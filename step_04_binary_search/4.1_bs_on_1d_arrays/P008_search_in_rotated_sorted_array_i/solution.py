"""
Problem: Search in Rotated Sorted Array I
LeetCode 33 | Difficulty: MEDIUM | XP: 25

A sorted array (no duplicates) is rotated at some pivot unknown to you.
Given the array and a target, return the index of target or -1 if not found.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Linear scan
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> int:
    """
    Simply iterate through every element and return its index if matched.
    """
    for i, val in enumerate(nums):
        if val == target:
            return i
    return -1


# ============================================================
# APPROACH 2: OPTIMAL  -  Modified binary search
# Time: O(log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> int:
    """
    Key insight: in any rotated array, at least one half around mid
    is always fully sorted.

    1. Compute mid.
    2. Determine which half is sorted by comparing nums[lo] <= nums[mid].
    3. Check if target lies within the sorted half; if yes, search there.
       Otherwise search the other half.
    """
    lo, hi = 0, len(nums) - 1

    while lo <= hi:
        mid = (lo + hi) // 2

        if nums[mid] == target:
            return mid

        # Left half is sorted
        if nums[lo] <= nums[mid]:
            if nums[lo] <= target < nums[mid]:
                hi = mid - 1
            else:
                lo = mid + 1
        # Right half is sorted
        else:
            if nums[mid] < target <= nums[hi]:
                lo = mid + 1
            else:
                hi = mid - 1

    return -1


# ============================================================
# APPROACH 3: BEST  -  Same modified binary search (iterative)
# Time: O(log n)  |  Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> int:
    """
    Identical logic to optimal; written slightly more compactly
    to show the clean interview-ready form.
    """
    lo, hi = 0, len(nums) - 1

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] == target:
            return mid
        if nums[lo] <= nums[mid]:           # left half sorted
            if nums[lo] <= target < nums[mid]:
                hi = mid - 1
            else:
                lo = mid + 1
        else:                               # right half sorted
            if nums[mid] < target <= nums[hi]:
                lo = mid + 1
            else:
                hi = mid - 1

    return -1


if __name__ == "__main__":
    print("=== Search in Rotated Sorted Array I ===")
    arr1, t1 = [4, 5, 6, 7, 0, 1, 2], 0
    arr2, t2 = [4, 5, 6, 7, 0, 1, 2], 3
    arr3, t3 = [1], 0
    print(f"Brute  {arr1}, target={t1}: {brute_force(arr1, t1)}")   # 4
    print(f"Optimal{arr1}, target={t2}: {optimal(arr1, t2)}")       # -1
    print(f"Best   {arr3}, target={t3}: {best(arr3, t3)}")          # -1
