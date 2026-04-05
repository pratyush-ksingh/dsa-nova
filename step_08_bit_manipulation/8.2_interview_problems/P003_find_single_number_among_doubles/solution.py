"""
Problem: Find Single Number Among Doubles (LeetCode #136)
Difficulty: EASY | XP: 10

Every element appears twice except one. Find the single one.
"""
from typing import List
from collections import Counter
from functools import reduce
import operator


# ============================================================
# APPROACH 1: BRUTE FORCE (Nested Loop)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """For each element, scan the array to see if it has a pair."""
    for i in range(len(nums)):
        found = False
        for j in range(len(nums)):
            if i != j and nums[i] == nums[j]:
                found = True
                break
        if not found:
            return nums[i]
    return -1


# ============================================================
# APPROACH 2: SORTING
# Time: O(n log n) | Space: O(n) for sorted copy
# ============================================================
def sorting(nums: List[int]) -> int:
    """Sort and check adjacent pairs."""
    s = sorted(nums)
    for i in range(0, len(s) - 1, 2):
        if s[i] != s[i + 1]:
            return s[i]
    return s[-1]


# ============================================================
# APPROACH 3: HASH MAP
# Time: O(n) | Space: O(n)
# ============================================================
def hash_map(nums: List[int]) -> int:
    """Count frequencies and return the element with count 1."""
    freq = Counter(nums)
    for num, count in freq.items():
        if count == 1:
            return num
    return -1


# ============================================================
# APPROACH 4: BEST (XOR Cancellation)
# Time: O(n) | Space: O(1)
# ============================================================
def xor_solution(nums: List[int]) -> int:
    """XOR all elements. Pairs cancel (a^a=0), leaving the single number."""
    result = 0
    for num in nums:
        result ^= num
    return result


# One-liner alternative using reduce
def xor_oneliner(nums: List[int]) -> int:
    return reduce(operator.xor, nums)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Find Single Number Among Doubles ===\n")

    test_cases = [
        ([2, 2, 1], 1),
        ([4, 1, 2, 1, 2], 4),
        ([1], 1),
        ([-1, 3, 3], -1),
        ([5, 7, 5, 9, 7], 9),
    ]

    all_pass = True
    for nums, expected in test_cases:
        b = brute_force(nums)
        s = sorting(nums)
        h = hash_map(nums)
        x = xor_solution(nums)

        ok = b == s == h == x == expected
        all_pass &= ok
        print(f"Input: {str(nums):25s} | Brute={b:3d} Sort={s:3d} Hash={h:3d} XOR={x:3d} | "
              f"Expected={expected:3d} [{'PASS' if ok else 'FAIL'}]")

    print(f"\nAll pass: {all_pass}")
