"""
Problem: Count Number of Nice Subarrays
Difficulty: HARD | XP: 50

Given an integer array nums and integer k, return the number of
contiguous subarrays that contain exactly k odd numbers.

Key insight: exactly(k) = atMost(k) - atMost(k-1)
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE - Check every subarray
# Time: O(n^2)  |  Space: O(1)
# For every pair (i, j), count odds in nums[i..j]
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    n, count = len(nums), 0
    for i in range(n):
        odds = 0
        for j in range(i, n):
            if nums[j] % 2 != 0:
                odds += 1
            if odds == k:
                count += 1
            elif odds > k:
                break
    return count


# ============================================================
# APPROACH 2: OPTIMAL - Prefix sum of odd counts
# Time: O(n)  |  Space: O(n)
# prefix[i] = count of odds in nums[0..i-1]
# Subarrays [l,r] with exactly k odds: prefix[r+1] - prefix[l] == k
# Use freq map: count how many previous prefix values equal (current - k)
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    freq: dict[int, int] = defaultdict(int)
    freq[0] = 1
    odds = count = 0
    for num in nums:
        if num % 2 != 0:
            odds += 1
        count += freq[odds - k]
        freq[odds] += 1
    return count


# ============================================================
# APPROACH 3: BEST - Sliding window: exactly(k) = atMost(k) - atMost(k-1)
# Time: O(n)  |  Space: O(1)
# Two-pointer window counting subarrays with at most k odds
# ============================================================
def best(nums: List[int], k: int) -> int:
    def at_most(limit: int) -> int:
        if limit < 0:
            return 0
        left = odds = count = 0
        for right, num in enumerate(nums):
            if num % 2 != 0:
                odds += 1
            while odds > limit:
                if nums[left] % 2 != 0:
                    odds -= 1
                left += 1
            count += right - left + 1
        return count

    return at_most(k) - at_most(k - 1)


if __name__ == "__main__":
    print("=== Count Number of Nice Subarrays ===")

    nums, k = [1, 1, 2, 1, 1], 3
    print(f"nums={nums}, k={k}")
    print(f"Brute:   {brute_force(nums, k)}")   # 2
    print(f"Optimal: {optimal(nums, k)}")         # 2
    print(f"Best:    {best(nums, k)}")            # 2

    nums, k = [2, 4, 6], 1
    print(f"\nnums={nums}, k={k}")
    print(f"Brute:   {brute_force(nums, k)}")   # 0
    print(f"Optimal: {optimal(nums, k)}")
    print(f"Best:    {best(nums, k)}")

    nums, k = [2, 2, 2, 1, 2, 2, 1, 2, 2, 2], 2
    print(f"\nnums={nums}, k={k}")
    print(f"Brute:   {brute_force(nums, k)}")   # 16
    print(f"Optimal: {optimal(nums, k)}")
    print(f"Best:    {best(nums, k)}")
