"""
Problem: Find the Smallest Divisor (LeetCode #1283)
Difficulty: MEDIUM | XP: 25

Given an array of integers nums and an integer threshold, find the smallest
positive integer divisor such that the sum of ceil(nums[i] / divisor) for
all i is <= threshold.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear scan from 1 to max)
# Time: O(max(nums) * n) | Space: O(1)
# ============================================================
def brute_force(nums: List[int], threshold: int) -> int:
    """Try every divisor from 1 to max(nums). Return first that works."""

    def compute_sum(divisor: int) -> int:
        return sum(math.ceil(x / divisor) for x in nums)

    max_val = max(nums)
    for d in range(1, max_val + 1):
        if compute_sum(d) <= threshold:
            return d
    return max_val


# ============================================================
# APPROACH 2: OPTIMAL (Binary search on divisor)
# Time: O(n * log(max(nums))) | Space: O(1)
# ============================================================
def optimal(nums: List[int], threshold: int) -> int:
    """
    Binary search on divisor in [1, max(nums)].
    For a given divisor d, sum = sum(ceil(x/d)) is monotonically decreasing in d.
    Find the smallest d where the sum <= threshold.
    """

    def compute_sum(divisor: int) -> int:
        return sum(math.ceil(x / divisor) for x in nums)

    lo, hi = 1, max(nums)
    ans = hi
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if compute_sum(mid) <= threshold:
            ans = mid       # feasible; try a smaller divisor
            hi = mid - 1
        else:
            lo = mid + 1    # sum too large; need bigger divisor
    return ans


# ============================================================
# APPROACH 3: BEST (Binary search + integer ceil)
# Time: O(n * log(max(nums))) | Space: O(1)
# ============================================================
def best(nums: List[int], threshold: int) -> int:
    """
    Same binary search but uses integer arithmetic for ceil:
      ceil(a/b) = (a + b - 1) // b
    Avoids floating point entirely. Uses lo < hi template.
    """

    def feasible(divisor: int) -> bool:
        total = 0
        for x in nums:
            total += (x + divisor - 1) // divisor
            if total > threshold:   # early exit
                return False
        return True

    lo, hi = 1, max(nums)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if feasible(mid):
            hi = mid        # feasible; try smaller
        else:
            lo = mid + 1    # not feasible; need larger
    return lo


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Find the Smallest Divisor ===\n")

    test_cases = [
        ([1, 2, 5, 9], 6, 5),
        ([44, 22, 33, 11, 1], 5, 44),
        ([1, 1, 1, 1], 4, 1),
        ([2, 3, 5, 7, 11], 11, 3),
        ([1000000], 1, 1000000),
    ]

    for nums, threshold, expected in test_cases:
        b = brute_force(nums, threshold)
        o = optimal(nums, threshold)
        n = best(nums, threshold)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: nums={nums}, threshold={threshold}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
