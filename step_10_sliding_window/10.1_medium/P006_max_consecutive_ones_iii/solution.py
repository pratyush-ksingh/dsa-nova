"""
Problem: Max Consecutive Ones III
Difficulty: MEDIUM | XP: 25
LeetCode #1004

Key Insight: "Flip at most k zeros" = "longest subarray with at most k zeros."
Classic sliding window.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Try All Subarrays
# Time: O(n^2)  |  Space: O(1)
#
# For each start index, expand right counting zeros.
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    n = len(nums)
    max_len = 0
    for i in range(n):
        zero_count = 0
        for j in range(i, n):
            if nums[j] == 0:
                zero_count += 1
            if zero_count > k:
                break
            max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL -- Sliding Window (Shrinking)
# Time: O(n)  |  Space: O(1)
#
# Expand right, shrink left when zeroCount > k.
# Track max window size.
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    left = 0
    zero_count = 0
    max_len = 0

    for right in range(len(nums)):
        if nums[right] == 0:
            zero_count += 1

        while zero_count > k:
            if nums[left] == 0:
                zero_count -= 1
            left += 1

        max_len = max(max_len, right - left + 1)

    return max_len


# ============================================================
# APPROACH 3: BEST -- Non-Shrinking Sliding Window
# Time: O(n)  |  Space: O(1)
#
# Window only grows or slides. Use 'if' instead of 'while'.
# Final answer is n - left.
# ============================================================
def best(nums: List[int], k: int) -> int:
    left = 0
    zero_count = 0

    for right in range(len(nums)):
        if nums[right] == 0:
            zero_count += 1

        if zero_count > k:
            # Slide: move left by exactly 1 (if, not while)
            if nums[left] == 0:
                zero_count -= 1
            left += 1

    # Window never shrinks, so final size = max size
    return len(nums) - left


if __name__ == "__main__":
    print("=== Max Consecutive Ones III ===")
    tests = [
        ([1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0], 2),
        ([0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1], 3),
        ([1, 1, 1, 1], 0),
        ([0, 0, 0], 0),
    ]
    for nums, k in tests:
        print(f"nums={nums}, k={k}")
        print(f"  Brute:   {brute_force(nums, k)}")
        print(f"  Optimal: {optimal(nums, k)}")
        print(f"  Best:    {best(nums, k)}")
        print()
