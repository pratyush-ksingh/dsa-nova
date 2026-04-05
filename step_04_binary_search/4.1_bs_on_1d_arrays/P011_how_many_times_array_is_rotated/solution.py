"""
Problem: How Many Times Array is Rotated
Difficulty: MEDIUM | XP: 25

A sorted array (in ascending order) is rotated k times to the right.
Given the rotated array, find k (i.e., how many times it was rotated).

Key insight: The number of rotations equals the index of the minimum element.
If the array was not rotated (k=0), the minimum is at index 0.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Linear scan to find the index of the minimum element.
    The index of the minimum element equals the number of rotations.
    """
    min_val = nums[0]
    min_idx = 0
    for i in range(1, len(nums)):
        if nums[i] < min_val:
            min_val = nums[i]
            min_idx = i
    return min_idx


# ============================================================
# APPROACH 2: OPTIMAL — Binary Search for Minimum Element
# Time: O(log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Binary search for the index of the minimum element.

    In a rotated sorted array, one half is always sorted.
    The minimum element is in the UNSORTED half.

    If nums[mid] >= nums[lo]: the left half [lo..mid] is sorted.
        The minimum is in the right half → lo = mid + 1
    Else: the right half [mid..hi] is sorted.
        The minimum is in the left half → hi = mid

    When lo == hi, we've found the minimum's index.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] > nums[hi]:
            # Left half is sorted; minimum is in the right half
            lo = mid + 1
        else:
            # Right half is sorted (or mid is the minimum)
            hi = mid

    return lo  # index of minimum = number of rotations


# ============================================================
# APPROACH 3: BEST — Binary Search (same, with edge case clarity)
# Time: O(log n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same binary search, but compares mid with nums[hi] directly (as opposed to nums[lo]).
    Comparing with hi is preferred because it avoids the ambiguity when lo == mid
    (which can cause infinite loops). Also handles the "not rotated" case implicitly:
    if nums[0] <= nums[n-1], the entire array is already sorted and the minimum is
    at index 0 — the binary search naturally returns 0.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] > nums[hi]:
            # Minimum must be in (mid, hi]
            lo = mid + 1
        else:
            # Minimum is in [lo, mid]
            hi = mid

    # lo == hi == index of the minimum element
    return lo


if __name__ == "__main__":
    print("=== How Many Times Array is Rotated ===")

    # Original: [1,2,3,4,5], rotated 2 times → [3,4,5,1,2], min at index 3...
    # Wait: rotating [1,2,3,4,5] right k=2: [4,5,1,2,3], min at index 2 → k=2
    test1 = [4, 5, 6, 7, 0, 1, 2]  # original sorted [0,1,2,4,5,6,7], rotated 4 times
    print(f"Input: {test1}")
    print(f"Brute:   {brute_force(test1)}")   # 4
    print(f"Optimal: {optimal(test1)}")        # 4
    print(f"Best:    {best(test1)}")           # 4

    test2 = [1, 2, 3, 4, 5]  # not rotated
    print(f"\nInput: {test2}")
    print(f"Brute:   {brute_force(test2)}")   # 0
    print(f"Optimal: {optimal(test2)}")        # 0
    print(f"Best:    {best(test2)}")           # 0

    test3 = [3, 4, 5, 1, 2]  # rotated 3 times
    print(f"\nInput: {test3}")
    print(f"Brute:   {brute_force(test3)}")   # 3
    print(f"Optimal: {optimal(test3)}")        # 3
    print(f"Best:    {best(test3)}")           # 3

    test4 = [2]  # single element
    print(f"\nInput: {test4}")
    print(f"Brute:   {brute_force(test4)}")   # 0
    print(f"Optimal: {optimal(test4)}")        # 0
    print(f"Best:    {best(test4)}")           # 0
