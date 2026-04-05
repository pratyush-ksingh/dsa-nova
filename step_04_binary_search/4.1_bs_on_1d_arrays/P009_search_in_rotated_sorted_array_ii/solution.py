"""
Problem: Search in Rotated Sorted Array II
LeetCode 81 | Difficulty: MEDIUM | XP: 25

Same as LC-33 but the array may contain duplicates.
Return True if target exists, False otherwise.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Linear scan
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> bool:
    """
    Iterate and check every element directly.
    """
    return target in nums


# ============================================================
# APPROACH 2: OPTIMAL  -  Binary search with duplicate handling
# Time: O(log n) average, O(n) worst  |  Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> bool:
    """
    Modified binary search.
    When nums[lo] == nums[mid] == nums[hi] we cannot decide which half is
    sorted, so we shrink both ends by one (lo++, hi--) and continue.
    Otherwise, the same sorted-half logic as LC-33 applies.
    """
    lo, hi = 0, len(nums) - 1

    while lo <= hi:
        mid = (lo + hi) // 2

        if nums[mid] == target:
            return True

        # Can't determine sorted half -- shrink boundaries
        if nums[lo] == nums[mid] == nums[hi]:
            lo += 1
            hi -= 1
        # Left half is sorted
        elif nums[lo] <= nums[mid]:
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

    return False


# ============================================================
# APPROACH 3: BEST  -  Same algorithm, explicit comments
# Time: O(log n) average, O(n) worst  |  Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> bool:
    """
    Identical to optimal; written with clearer inline comments
    to aid understanding during an interview explanation.
    """
    lo, hi = 0, len(nums) - 1

    while lo <= hi:
        mid = lo + (hi - lo) // 2

        if nums[mid] == target:
            return True

        # Ambiguous case: both ends equal mid, just shrink
        if nums[lo] == nums[mid] and nums[mid] == nums[hi]:
            lo += 1
            hi -= 1
        elif nums[lo] <= nums[mid]:          # left half definitely sorted
            if nums[lo] <= target < nums[mid]:
                hi = mid - 1                 # target in left half
            else:
                lo = mid + 1                 # target in right half
        else:                                # right half definitely sorted
            if nums[mid] < target <= nums[hi]:
                lo = mid + 1                 # target in right half
            else:
                hi = mid - 1                 # target in left half

    return False


if __name__ == "__main__":
    print("=== Search in Rotated Sorted Array II ===")
    a1, t1 = [2, 5, 6, 0, 0, 1, 2], 0
    a2, t2 = [2, 5, 6, 0, 0, 1, 2], 3
    a3, t3 = [1, 0, 1, 1, 1], 0
    print(f"Brute  {a1}, target={t1}: {brute_force(a1, t1)}")    # True
    print(f"Optimal{a2}, target={t2}: {optimal(a2, t2)}")        # False
    print(f"Best   {a3}, target={t3}: {best(a3, t3)}")           # True
