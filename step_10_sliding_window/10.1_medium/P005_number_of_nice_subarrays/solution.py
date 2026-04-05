"""
Problem: Number of Nice Subarrays
Difficulty: MEDIUM | XP: 25
LeetCode #1248

Key Insight: exactlyK = atMost(k) - atMost(k-1), each computed via sliding window.
"""
from typing import List, Optional
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- Enumerate All Subarrays
# Time: O(n^2)  |  Space: O(1)
#
# For each starting index, expand and count odds.
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    n = len(nums)
    result = 0
    for i in range(n):
        odd_count = 0
        for j in range(i, n):
            if nums[j] % 2 != 0:
                odd_count += 1
            if odd_count == k:
                result += 1
            if odd_count > k:
                break
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Prefix Sum + HashMap
# Time: O(n)  |  Space: O(n)
#
# Track running odd count. Use map to count prefix frequencies.
# For each j, add freq of (oddCount - k) to result.
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    prefix_count = defaultdict(int)
    prefix_count[0] = 1
    odd_count = 0
    result = 0

    for num in nums:
        if num % 2 != 0:
            odd_count += 1
        result += prefix_count[odd_count - k]
        prefix_count[odd_count] += 1

    return result


# ============================================================
# APPROACH 3: BEST -- Sliding Window: atMost(k) - atMost(k-1)
# Time: O(n)  |  Space: O(1)
#
# atMost(k) counts subarrays with <= k odds using sliding window.
# exactlyK = atMost(k) - atMost(k-1).
# ============================================================
def _at_most(nums: List[int], k: int) -> int:
    if k < 0:
        return 0
    left = 0
    odd_count = 0
    result = 0
    for right in range(len(nums)):
        if nums[right] % 2 != 0:
            odd_count += 1

        while odd_count > k:
            if nums[left] % 2 != 0:
                odd_count -= 1
            left += 1

        result += right - left + 1
    return result


def best(nums: List[int], k: int) -> int:
    return _at_most(nums, k) - _at_most(nums, k - 1)


if __name__ == "__main__":
    print("=== Number of Nice Subarrays ===")
    tests = [
        ([1, 1, 2, 1, 1], 3),
        ([2, 4, 6], 1),
        ([2, 2, 2, 1, 2, 2, 1, 2, 2, 2], 2),
    ]
    for nums, k in tests:
        print(f"nums={nums}, k={k}")
        print(f"  Brute:   {brute_force(nums, k)}")
        print(f"  Optimal: {optimal(nums, k)}")
        print(f"  Best:    {best(nums, k)}")
        print()
