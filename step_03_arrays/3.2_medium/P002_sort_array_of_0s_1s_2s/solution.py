"""
Problem: Sort Array of 0s 1s 2s (LeetCode #75)
Difficulty: MEDIUM | XP: 25

Given an array containing only 0s, 1s, and 2s, sort it in-place in ascending
order without using any library sort function.
Also known as the Dutch National Flag problem.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Count and overwrite)
# Time: O(2n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> None:
    """Count each value, then overwrite the array in two passes."""
    count = [0, 0, 0]
    for x in nums:
        count[x] += 1

    idx = 0
    for val in range(3):
        for _ in range(count[val]):
            nums[idx] = val
            idx += 1


# ============================================================
# APPROACH 2: OPTIMAL (Dutch National Flag - three pointers)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> None:
    """
    Three pointers: low, mid, high.
    - [0, low)  : all 0s
    - [low, mid): all 1s
    - (high, n) : all 2s
    - [mid, high]: unsorted region
    """
    low, mid, high = 0, 0, len(nums) - 1

    while mid <= high:
        if nums[mid] == 0:
            nums[low], nums[mid] = nums[mid], nums[low]
            low += 1
            mid += 1
        elif nums[mid] == 1:
            mid += 1
        else:  # nums[mid] == 2
            nums[mid], nums[high] = nums[high], nums[mid]
            high -= 1
            # Do NOT increment mid: the swapped element is unseen


# ============================================================
# APPROACH 3: BEST (Dutch National Flag - same, most readable)
# Time: O(n) | Space: O(1)
# ============================================================
def best(nums: List[int]) -> None:
    """
    Same Dutch National Flag algorithm with explicit invariant comments.
    Single pass, no extra space. This IS the best known solution.
    """
    lo, mid, hi = 0, 0, len(nums) - 1

    while mid <= hi:
        if nums[mid] == 0:
            # Move 0 to the left partition
            nums[lo], nums[mid] = nums[mid], nums[lo]
            lo += 1
            mid += 1          # both lo and mid advance: no new unknown element
        elif nums[mid] == 1:
            mid += 1          # 1 is already in correct region
        else:
            # Move 2 to the right partition
            nums[mid], nums[hi] = nums[hi], nums[mid]
            hi -= 1           # mid stays: element just swapped in is unknown


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Sort Array of 0s 1s 2s ===\n")

    import copy
    test_cases = [
        ([2, 0, 2, 1, 1, 0], [0, 0, 1, 1, 2, 2]),
        ([2, 0, 1], [0, 1, 2]),
        ([0], [0]),
        ([1, 1, 1], [1, 1, 1]),
        ([0, 0, 0], [0, 0, 0]),
        ([2, 2, 2], [2, 2, 2]),
        ([1, 0, 2, 1, 0, 2], [0, 0, 1, 1, 2, 2]),
    ]

    for original, expected in test_cases:
        b = copy.copy(original); brute_force(b)
        o = copy.copy(original); optimal(o)
        n = copy.copy(original); best(n)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input:    {original}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
