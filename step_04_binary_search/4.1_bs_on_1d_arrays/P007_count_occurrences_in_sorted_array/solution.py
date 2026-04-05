"""
Problem: Count Occurrences in Sorted Array
Difficulty: EASY | XP: 10

Count how many times target appears in a sorted array.
Key insight: find first and last position via binary search, then subtract.
"""
from typing import List
from bisect import bisect_left, bisect_right


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> int:
    """Count by scanning every element."""
    count = 0
    for num in nums:
        if num == target:
            count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL (Two Binary Searches)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> int:
    """Find first and last occurrence, then subtract."""

    def find_first(nums: List[int], target: int) -> int:
        lo, hi = 0, len(nums) - 1
        ans = -1
        while lo <= hi:
            mid = lo + (hi - lo) // 2
            if nums[mid] == target:
                ans = mid
                hi = mid - 1  # search left
            elif nums[mid] < target:
                lo = mid + 1
            else:
                hi = mid - 1
        return ans

    def find_last(nums: List[int], target: int) -> int:
        lo, hi = 0, len(nums) - 1
        ans = -1
        while lo <= hi:
            mid = lo + (hi - lo) // 2
            if nums[mid] == target:
                ans = mid
                lo = mid + 1  # search right
            elif nums[mid] < target:
                lo = mid + 1
            else:
                hi = mid - 1
        return ans

    first = find_first(nums, target)
    if first == -1:
        return 0
    last = find_last(nums, target)
    return last - first + 1


# ============================================================
# APPROACH 3: BEST (Using bisect library)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> int:
    """bisect_right - bisect_left gives the count directly."""
    return bisect_right(nums, target) - bisect_left(nums, target)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Count Occurrences in Sorted Array ===\n")

    test_cases = [
        ([1, 1, 2, 2, 2, 2, 3], 2, 4),
        ([1, 1, 2, 2, 2, 2, 3], 4, 0),
        ([8, 9, 10, 12, 12, 12], 12, 3),
        ([5], 5, 1),
        ([5], 3, 0),
        ([2, 2, 2, 2, 2], 2, 5),
        ([1, 3, 5, 7], 1, 1),
    ]

    for nums, target, expected in test_cases:
        b = brute_force(nums, target)
        o = optimal(nums, target)
        r = best(nums, target)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {nums}, target={target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
