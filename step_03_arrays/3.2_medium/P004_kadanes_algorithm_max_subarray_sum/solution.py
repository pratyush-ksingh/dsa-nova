"""
Problem: Kadane's Algorithm - Maximum Subarray Sum
Difficulty: MEDIUM | XP: 25
LeetCode: 53

Given an integer array nums, find the contiguous subarray (containing at
least one number) which has the largest sum and return its sum.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Try every possible subarray [i..j]. For each starting index i,
    extend j to the right, accumulating the running sum. Track the global max.
    Avoids the O(n^3) triple-loop by computing sums incrementally.
    """
    n = len(nums)
    max_sum = float('-inf')
    for i in range(n):
        current_sum = 0
        for j in range(i, n):
            current_sum += nums[j]
            max_sum = max(max_sum, current_sum)
    return max_sum


# ============================================================
# APPROACH 2: OPTIMAL — Kadane's Algorithm
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    Kadane's insight: at each position, either extend the previous subarray
    or start a new one from the current element — whichever is larger.
    current_sum = max(nums[i], current_sum + nums[i])
    The global maximum over all positions is the answer.
    """
    max_sum = nums[0]
    current_sum = nums[0]
    for i in range(1, len(nums)):
        current_sum = max(nums[i], current_sum + nums[i])
        max_sum = max(max_sum, current_sum)
    return max_sum


# ============================================================
# APPROACH 3: BEST — Kadane's with Subarray Tracking
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same O(n) Kadane's algorithm but also tracks the start and end indices
    of the maximum subarray — useful for follow-up questions asking for
    the actual subarray, not just the sum.
    """
    max_sum = nums[0]
    current_sum = nums[0]
    start = end = 0
    temp_start = 0

    for i in range(1, len(nums)):
        if nums[i] > current_sum + nums[i]:
            current_sum = nums[i]
            temp_start = i
        else:
            current_sum += nums[i]

        if current_sum > max_sum:
            max_sum = current_sum
            start = temp_start
            end = i

    # The maximum subarray is nums[start:end+1]
    return max_sum  # return (max_sum, start, end) for full info


if __name__ == "__main__":
    print("=== Kadane's Algorithm - Maximum Subarray Sum ===")

    test1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    print(f"Input: {test1}")
    print(f"Brute:   {brute_force(test1)}")   # 6 ([4,-1,2,1])
    print(f"Optimal: {optimal(test1)}")        # 6
    print(f"Best:    {best(test1)}")           # 6

    test2 = [1]
    print(f"\nInput: {test2}")
    print(f"Brute:   {brute_force(test2)}")   # 1
    print(f"Optimal: {optimal(test2)}")        # 1
    print(f"Best:    {best(test2)}")           # 1

    test3 = [-1, -2, -3, -4]
    print(f"\nInput: {test3}")
    print(f"Brute:   {brute_force(test3)}")   # -1
    print(f"Optimal: {optimal(test3)}")        # -1
    print(f"Best:    {best(test3)}")           # -1
