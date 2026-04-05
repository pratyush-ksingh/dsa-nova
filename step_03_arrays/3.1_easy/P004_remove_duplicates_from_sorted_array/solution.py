"""
Problem: Remove Duplicates from Sorted Array (LeetCode #26)
Difficulty: EASY | XP: 10

Remove duplicates in-place from a sorted array. Return new length.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Extra Space)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """Use a set to collect unique elements, then write back."""
    if not nums:
        return 0
    unique = list(dict.fromkeys(nums))  # preserves order
    for i, val in enumerate(unique):
        nums[i] = val
    return len(unique)


# ============================================================
# APPROACH 2: OPTIMAL (Two Pointers In-Place)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """Write pointer overwrites duplicates as read pointer advances."""
    if not nums:
        return 0
    w = 1  # write pointer; first element always stays
    for r in range(1, len(nums)):
        if nums[r] != nums[r - 1]:
            nums[w] = nums[r]
            w += 1
    return w


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Remove Duplicates from Sorted Array ===\n")

    test_cases = [
        ([1, 1, 2], 2),
        ([0, 0, 1, 1, 1, 2, 2, 3, 3, 4], 5),
        ([1], 1),
        ([3, 3, 3, 3], 1),
        ([1, 2, 3, 4], 4),
        ([-1, -1, 0, 0, 1], 3),
    ]

    for nums, expected in test_cases:
        # Test brute force on a copy
        arr1 = nums[:]
        bk = brute_force(arr1)

        # Test optimal on a copy
        arr2 = nums[:]
        ok = optimal(arr2)

        status = "PASS" if bk == expected and ok == expected else "FAIL"
        print(f"Input: {nums}")
        print(f"  Brute:    k={bk}, arr={arr1[:bk]}")
        print(f"  Optimal:  k={ok}, arr={arr2[:ok]}")
        print(f"  Expected: k={expected}  [{status}]\n")
