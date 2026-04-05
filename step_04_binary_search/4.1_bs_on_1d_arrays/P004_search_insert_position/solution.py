"""
Problem: Search Insert Position (LeetCode #35)
Difficulty: EASY | XP: 10

Given a sorted array of distinct integers and a target, return the index
if found, else return the index where it would be inserted.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> int:
    """Walk through the array; return first index where nums[i] >= target."""
    for i in range(len(nums)):
        if nums[i] >= target:
            return i
    return len(nums)


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search with ans tracking)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> int:
    """Lower-bound binary search: find first index where nums[i] >= target."""
    lo, hi = 0, len(nums) - 1
    ans = len(nums)  # default: insert at end

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] >= target:
            ans = mid        # candidate answer
            hi = mid - 1     # look for earlier position
        else:
            lo = mid + 1

    return ans


# ============================================================
# APPROACH 3: BEST (Simplified -- lo converges to answer)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> int:
    """Standard binary search; when loop ends, lo IS the insertion point."""
    lo, hi = 0, len(nums) - 1

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1

    return lo  # lo is the correct insertion index


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Search Insert Position ===\n")

    test_cases = [
        ([1, 3, 5, 6], 5, 2),
        ([1, 3, 5, 6], 2, 1),
        ([1, 3, 5, 6], 7, 4),
        ([1, 3, 5, 6], 0, 0),
        ([1], 1, 0),
        ([1], 0, 0),
        ([1, 3], 2, 1),
    ]

    for nums, target, expected in test_cases:
        b = brute_force(nums, target)
        o = optimal(nums, target)
        s = best(nums, target)
        status = "PASS" if b == expected and o == expected and s == expected else "FAIL"
        print(f"Input: {nums}, target={target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {s}")
        print(f"  Expected: {expected}  [{status}]\n")
