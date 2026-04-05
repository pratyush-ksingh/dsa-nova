"""
Problem: Single Element in Sorted Array (LeetCode 540)
Difficulty: MEDIUM | XP: 25

Every element appears exactly twice except for one element which appears once.
Find that single element. Must run in O(log n) time and O(1) space.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - XOR all elements
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    XOR all elements together. Duplicate elements cancel out (a ^ a = 0),
    leaving only the single element (0 ^ x = x).
    """
    result = 0
    for num in nums:
        result ^= num
    return result


# ============================================================
# APPROACH 2: OPTIMAL - Binary search on pair index pattern
# Time: O(log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Key insight: In a sorted array where every element appears twice,
    pairs occupy indices (0,1), (2,3), (4,5), ...
    Before the single element: nums[even] == nums[even+1]
    After the single element:  nums[even] == nums[even-1]

    Always check even indices to normalize position, then binary search.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        # Ensure mid is even so we always compare with the right partner
        if mid % 2 == 1:
            mid -= 1

        if nums[mid] == nums[mid + 1]:
            # Pair is intact -> single element is to the right
            lo = mid + 2
        else:
            # Pair is broken -> single element is at mid or to the left
            hi = mid

    return nums[lo]


# ============================================================
# APPROACH 3: BEST - Same binary search, slightly cleaner with XOR trick
# Time: O(log n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same O(log n) binary search but uses XOR to compute the even partner
    index without an if-branch: mid ^ 1 flips the last bit.
    - If mid is even: mid ^ 1 = mid + 1 (right partner)
    - If mid is odd:  mid ^ 1 = mid - 1 (left partner)
    This naturally keeps mid at the first of each pair.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        # XOR with 1: if mid is even -> compare with mid+1, if odd -> mid-1
        if nums[mid] == nums[mid ^ 1]:
            lo = mid + 1
        else:
            hi = mid

    return nums[lo]


if __name__ == "__main__":
    print("=== Single Element in Sorted Array ===")
    test_cases = [
        ([1, 1, 2, 3, 3, 4, 4, 8, 8], 2),
        ([3, 3, 7, 7, 10, 11, 11], 10),
        ([1], 1),
        ([1, 1, 2], 2),
        ([1, 2, 2], 1),
    ]
    for nums, expected in test_cases:
        b = brute_force(nums[:])
        o = optimal(nums[:])
        bst = best(nums[:])
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] nums={nums} | Brute={b}, Optimal={o}, Best={bst} (expected {expected})")
