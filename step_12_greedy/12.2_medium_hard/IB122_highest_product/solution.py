"""
Problem: Highest Product
InterviewBit | Difficulty: EASY | XP: 10

Given an array of integers, find the maximum product of any 3 numbers.
The array may contain negative numbers — two large negatives * one positive
can beat three large positives.

Key Insight: After sorting, the answer is max(last 3 elements, first 2 * last 1).
             Can also track 3 largest and 2 smallest in one pass.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  (All triplets)
# Time: O(n^3)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Try every combination of 3 distinct indices and return the maximum product.
    """
    n = len(nums)
    best_val = float('-inf')
    for i in range(n):
        for j in range(i + 1, n):
            for k in range(j + 1, n):
                best_val = max(best_val, nums[i] * nums[j] * nums[k])
    return int(best_val)


# ============================================================
# APPROACH 2: OPTIMAL  (Sort)
# Time: O(n log n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Sort the array. Two candidates:
      1. Product of the three largest: nums[-1] * nums[-2] * nums[-3]
      2. Product of two smallest (most negative) * the largest: nums[0] * nums[1] * nums[-1]
    Return the maximum of the two.
    """
    nums_sorted = sorted(nums)
    return max(nums_sorted[-1] * nums_sorted[-2] * nums_sorted[-3],
               nums_sorted[0] * nums_sorted[1] * nums_sorted[-1])


# ============================================================
# APPROACH 3: BEST  (Single pass — O(n))
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Track the 3 largest values (max1 >= max2 >= max3) and
    the 2 smallest values (min1 <= min2) in a single scan.
    The answer is max(max1*max2*max3, min1*min2*max1).
    """
    max1 = max2 = max3 = float('-inf')
    min1 = min2 = float('inf')

    for num in nums:
        # Update three largest (descending)
        if num >= max1:
            max3, max2, max1 = max2, max1, num
        elif num >= max2:
            max3, max2 = max2, num
        elif num > max3:
            max3 = num
        # Update two smallest (ascending)
        if num <= min1:
            min2, min1 = min1, num
        elif num < min2:
            min2 = num

    return int(max(max1 * max2 * max3, min1 * min2 * max1))


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3], 6),
        ([-10, -3, 5, 6, -2], 180),   # (-10)*(-3)*6 = 180
        ([0, -1, 3, 100, 50], 15000),  # 100*50*3 = 15000
        ([-1, -2, -3], -6),
        ([1, 2, 3, 4], 24),
    ]
    print("=== Highest Product ===")
    for nums, expected in test_cases:
        b   = brute_force(nums[:])
        o   = optimal(nums[:])
        bst = best(nums[:])
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  nums={nums} => brute={b}, optimal={o}, best={bst} (expected {expected}) [{status}]")
