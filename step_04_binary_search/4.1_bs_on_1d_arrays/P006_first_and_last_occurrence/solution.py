"""
Problem: First and Last Occurrence (LC #34)
Difficulty: MEDIUM | XP: 25

Find starting and ending position of target in a sorted array.
Key insight: two binary searches -- one biased left, one biased right.
"""
from typing import List
from bisect import bisect_left, bisect_right


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> List[int]:
    """Scan every element, record first and last match."""
    first, last = -1, -1
    for i, num in enumerate(nums):
        if num == target:
            if first == -1:
                first = i
            last = i
    return [first, last]


# ============================================================
# APPROACH 2: OPTIMAL (Two Binary Searches)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> List[int]:
    """Binary search biased left for first, biased right for last."""

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
        return [-1, -1]
    last = find_last(nums, target)
    return [first, last]


# ============================================================
# APPROACH 3: BEST (Using bisect library)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> List[int]:
    """bisect_left for first, bisect_right - 1 for last."""
    left = bisect_left(nums, target)
    if left == len(nums) or nums[left] != target:
        return [-1, -1]
    right = bisect_right(nums, target) - 1
    return [left, right]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== First and Last Occurrence ===\n")

    test_cases = [
        ([5, 7, 7, 8, 8, 10], 8, [3, 4]),
        ([5, 7, 7, 8, 8, 10], 6, [-1, -1]),
        ([1], 1, [0, 0]),
        ([2, 2, 2, 2, 2], 2, [0, 4]),
        ([1, 3, 5, 7], 5, [2, 2]),
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
