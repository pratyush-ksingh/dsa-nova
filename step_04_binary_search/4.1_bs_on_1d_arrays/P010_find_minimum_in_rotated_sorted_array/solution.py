"""
Problem: Find Minimum in Rotated Sorted Array
Difficulty: MEDIUM | XP: 25
LeetCode: 153
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Linear scan - track the running minimum across the entire array.
    Simple but misses the sorted structure entirely.
    """
    minimum = nums[0]
    for num in nums:
        minimum = min(minimum, num)
    return minimum


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Binary search comparing mid with the right boundary.

    Key insight: in any rotation of a sorted array, exactly one
    half is always fully sorted. The minimum is either mid itself
    (if left half is unsorted and mid is the pivot) or lies in
    the unsorted half.

    Compare nums[mid] with nums[right]:
    - If nums[mid] > nums[right]: minimum is in the right half
      (current mid is too large, the dip must be to the right).
    - Otherwise: minimum is at mid or in the left half.
    """
    left, right = 0, len(nums) - 1
    while left < right:
        mid = (left + right) // 2
        if nums[mid] > nums[right]:
            # Min is strictly in the right portion
            left = mid + 1
        else:
            # Mid could be the answer; search left including mid
            right = mid
    return nums[left]


# ============================================================
# APPROACH 3: BEST
# Time: O(log n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same binary search as Optimal. Early-exit added: if the
    sub-array is already sorted (nums[left] <= nums[right]),
    the leftmost element is the minimum — skip further halving.
    This avoids unnecessary iterations on already-sorted segments.
    """
    left, right = 0, len(nums) - 1
    while left < right:
        # Sub-array already sorted — leftmost is minimum
        if nums[left] < nums[right]:
            return nums[left]
        mid = (left + right) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    return nums[left]


if __name__ == "__main__":
    test_cases = [
        ([3, 4, 5, 1, 2], 1),
        ([4, 5, 6, 7, 0, 1, 2], 0),
        ([11, 13, 15, 17], 11),
        ([2, 1], 1),
        ([1], 1),
    ]
    print("=== Find Minimum in Rotated Sorted Array ===")
    for nums, expected in test_cases:
        b = brute_force(nums)
        o = optimal(nums)
        bst = best(nums)
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] nums={nums} => Brute={b}, Optimal={o}, Best={bst} (expected {expected})")
