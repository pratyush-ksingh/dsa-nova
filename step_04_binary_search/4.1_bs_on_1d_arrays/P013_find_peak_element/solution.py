"""
Problem: Find Peak Element (LeetCode 162)
Difficulty: MEDIUM | XP: 25

A peak element is strictly greater than its neighbors.
Given an integer array nums, find a peak and return its index.
The array may contain multiple peaks; return any peak's index.
Assume nums[-1] = nums[n] = -infinity (boundaries are virtual -inf).
Must run in O(log n) time.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Linear scan
# Time: O(n)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Scan through the array. The first element that is greater than
    both its neighbors (treating out-of-bounds as -infinity) is a peak.
    """
    n = len(nums)
    for i in range(n):
        left_ok  = (i == 0) or (nums[i] > nums[i - 1])
        right_ok = (i == n - 1) or (nums[i] > nums[i + 1])
        if left_ok and right_ok:
            return i
    return -1  # never reached for valid input


# ============================================================
# APPROACH 2: OPTIMAL - Binary search toward the rising side
# Time: O(log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Key insight: if nums[mid] < nums[mid+1], the slope is rising to the right.
    A peak MUST exist on the right half (even if nums eventually falls, it
    has to stop somewhere, and the last rising element is a peak).
    Similarly if nums[mid] > nums[mid+1], a peak exists on the left.

    This is guaranteed because boundaries are -infinity.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] < nums[mid + 1]:
            # Rising slope to the right -> peak is in (mid+1 .. hi)
            lo = mid + 1
        else:
            # Falling slope -> peak is in (lo .. mid)
            hi = mid

    return lo


# ============================================================
# APPROACH 3: BEST - Same binary search (cleaner termination check)
# Time: O(log n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Identical algorithm to Approach 2. Presented with explicit neighbor
    comparison to make the invariant crystal clear in interviews:
    always move toward the greater neighbor; convergence guarantees a peak.
    """
    lo, hi = 0, len(nums) - 1

    while lo < hi:
        mid = lo + (hi - lo) // 2
        if nums[mid] > nums[mid + 1]:
            # Left side has a peak (mid itself might be it)
            hi = mid
        else:
            # Right side has a peak
            lo = mid + 1

    # lo == hi, this index is a peak
    return lo


if __name__ == "__main__":
    print("=== Find Peak Element ===")
    test_cases = [
        ([1, 2, 3, 1], [2]),            # peak at index 2
        ([1, 2, 1, 3, 5, 6, 4], [1, 5]),  # peak at index 1 or 5
        ([1], [0]),
        ([2, 1], [0]),
        ([1, 2], [1]),
    ]
    for nums, valid_peaks in test_cases:
        b   = brute_force(nums)
        o   = optimal(nums)
        bst = best(nums)
        b_ok   = b   in valid_peaks
        o_ok   = o   in valid_peaks
        bst_ok = bst in valid_peaks
        status = "OK" if (b_ok and o_ok and bst_ok) else "FAIL"
        print(f"[{status}] nums={nums} | Brute={b}, Optimal={o}, Best={bst} (valid={valid_peaks})")
