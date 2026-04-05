"""
Problem: Count Subarrays with Given Sum
Difficulty: MEDIUM | XP: 25
LeetCode: 560 (Subarray Sum Equals K)

Given an integer array nums and an integer k, return the total number of
contiguous subarrays whose sum equals k.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    """
    Try every possible subarray [i..j] by fixing the start i and extending j.
    Accumulate the sum incrementally. Count subarrays where sum == k.
    """
    n = len(nums)
    count = 0
    for i in range(n):
        current_sum = 0
        for j in range(i, n):
            current_sum += nums[j]
            if current_sum == k:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL — Prefix Sum + HashMap
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    """
    Key insight: if prefix_sum[j] - prefix_sum[i] == k, then subarray [i+1..j]
    sums to k. So for each j, we need to count how many previous prefix sums
    equal (prefix_sum[j] - k). Store counts in a HashMap.

    Initialize the map with {0: 1} to handle subarrays starting at index 0.
    """
    count = 0
    prefix_sum = 0
    freq = defaultdict(int)
    freq[0] = 1  # empty prefix

    for num in nums:
        prefix_sum += num
        # How many times has (prefix_sum - k) appeared before?
        count += freq[prefix_sum - k]
        freq[prefix_sum] += 1

    return count


# ============================================================
# APPROACH 3: BEST — Prefix Sum + HashMap (same, with clear variable names)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(nums: List[int], k: int) -> int:
    """
    Same as optimal. The "best" label here refers to the fact that for
    arrays without negative numbers, a two-pointer/sliding-window approach
    would work in O(n) time and O(1) space. However, since this problem
    includes negative numbers, the prefix-sum + HashMap remains the
    definitive optimal solution. Written here with a slightly different
    style to reinforce the pattern.
    """
    prefix_counts = {0: 1}  # prefix_sum -> frequency
    running_sum = 0
    result = 0

    for x in nums:
        running_sum += x
        needed = running_sum - k
        result += prefix_counts.get(needed, 0)
        prefix_counts[running_sum] = prefix_counts.get(running_sum, 0) + 1

    return result


if __name__ == "__main__":
    print("=== Count Subarrays with Given Sum ===")

    nums1, k1 = [1, 1, 1], 2
    print(f"Input: nums={nums1}, k={k1}")
    print(f"Brute:   {brute_force(nums1, k1)}")   # 2
    print(f"Optimal: {optimal(nums1, k1)}")        # 2
    print(f"Best:    {best(nums1, k1)}")           # 2

    nums2, k2 = [1, 2, 3], 3
    print(f"\nInput: nums={nums2}, k={k2}")
    print(f"Brute:   {brute_force(nums2, k2)}")   # 2 ([1,2] and [3])
    print(f"Optimal: {optimal(nums2, k2)}")        # 2
    print(f"Best:    {best(nums2, k2)}")           # 2

    nums3, k3 = [-1, -1, 1], 0
    print(f"\nInput: nums={nums3}, k={k3}")
    print(f"Brute:   {brute_force(nums3, k3)}")   # 1
    print(f"Optimal: {optimal(nums3, k3)}")        # 1
    print(f"Best:    {best(nums3, k3)}")           # 1
