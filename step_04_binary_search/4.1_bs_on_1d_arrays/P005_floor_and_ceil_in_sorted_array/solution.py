"""
Problem: Floor and Ceil in Sorted Array
Difficulty: EASY | XP: 10

Find the floor (largest <= target) and ceil (smallest >= target)
in a sorted array using binary search.
"""
from typing import List, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], target: int) -> Tuple[int, int]:
    """Scan left to right. Last <= target is floor, first >= target is ceil."""
    floor, ceil = -1, -1
    for num in nums:
        if num <= target:
            floor = num   # keep overwriting -- last is largest <= target
        if num >= target and ceil == -1:
            ceil = num    # first is smallest >= target
    return (floor, ceil)


# ============================================================
# APPROACH 2: OPTIMAL (Two Separate Binary Searches)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(nums: List[int], target: int) -> Tuple[int, int]:
    """Run one binary search for floor and one for ceil."""
    return (find_floor(nums, target), find_ceil(nums, target))


def find_floor(nums: List[int], target: int) -> int:
    """Find largest element <= target. Record candidate and go right."""
    lo, hi = 0, len(nums) - 1
    floor = -1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] <= target:
            floor = nums[mid]  # candidate -- try to find larger
            lo = mid + 1
        else:
            hi = mid - 1
    return floor


def find_ceil(nums: List[int], target: int) -> int:
    """Find smallest element >= target. Record candidate and go left."""
    lo, hi = 0, len(nums) - 1
    ceil = -1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] >= target:
            ceil = nums[mid]  # candidate -- try to find smaller
            hi = mid - 1
        else:
            lo = mid + 1
    return ceil


# ============================================================
# APPROACH 3: BEST (Single-Pass Binary Search)
# Time: O(log n) | Space: O(1)
# ============================================================
def best(nums: List[int], target: int) -> Tuple[int, int]:
    """One binary search updating both floor and ceil candidates."""
    lo, hi = 0, len(nums) - 1
    floor, ceil = -1, -1

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] == target:
            return (target, target)  # exact match
        elif nums[mid] < target:
            floor = nums[mid]  # best floor candidate so far
            lo = mid + 1
        else:
            ceil = nums[mid]   # best ceil candidate so far
            hi = mid - 1

    return (floor, ceil)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Floor and Ceil in Sorted Array ===\n")

    test_cases = [
        ([1, 2, 8, 10, 10, 12, 19], 5,  (2, 8)),
        ([1, 2, 8, 10, 10, 12, 19], 10, (10, 10)),
        ([1, 2, 8, 10, 10, 12, 19], 0,  (-1, 1)),
        ([1, 2, 8, 10, 10, 12, 19], 25, (19, -1)),
        ([5], 5, (5, 5)),
        ([5], 3, (-1, 5)),
        ([3, 7], 5, (3, 7)),
    ]

    for nums, target, expected in test_cases:
        b = brute_force(nums, target)
        o = optimal(nums, target)
        s = best(nums, target)
        status = "PASS" if b == expected and o == expected and s == expected else "FAIL"
        print(f"Input: {nums}, target={target}")
        print(f"  Brute:    floor={b[0]}, ceil={b[1]}")
        print(f"  Optimal:  floor={o[0]}, ceil={o[1]}")
        print(f"  Best:     floor={s[0]}, ceil={s[1]}")
        print(f"  Expected: floor={expected[0]}, ceil={expected[1]}  [{status}]\n")
