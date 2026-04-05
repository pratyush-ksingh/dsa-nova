"""
Problem: Single Number (LeetCode #136)
Difficulty: EASY | XP: 10

Every element appears twice except one. Find the single number.
"""
from typing import List
from functools import reduce
from operator import xor


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashMap Counting
# Time: O(n)  |  Space: O(n)
# Count occurrences, return element with count 1.
# ============================================================
def brute_force(nums: List[int]) -> int:
    freq = {}
    for num in nums:
        freq[num] = freq.get(num, 0) + 1
    for num, count in freq.items():
        if count == 1:
            return num
    return -1  # unreachable


# ============================================================
# APPROACH 2: OPTIMAL -- Sorting + Adjacent Scan
# Time: O(n log n)  |  Space: O(n) for sorted copy
# Sort, then check pairs. Unpaired element is the answer.
# ============================================================
def optimal(nums: List[int]) -> int:
    nums_sorted = sorted(nums)
    n = len(nums_sorted)

    # Check pairs (i, i+1)
    i = 0
    while i < n - 1:
        if nums_sorted[i] != nums_sorted[i + 1]:
            return nums_sorted[i]
        i += 2

    # Last element is the single number
    return nums_sorted[-1]


# ============================================================
# APPROACH 3: BEST -- XOR Cancellation
# Time: O(n)  |  Space: O(1)
# XOR all elements. Pairs cancel (a^a=0), single remains.
# ============================================================
def best(nums: List[int]) -> int:
    result = 0
    for num in nums:
        result ^= num
    return result


if __name__ == "__main__":
    print("=== Single Number ===")

    test1 = [2, 2, 1]
    test2 = [4, 1, 2, 1, 2]
    test3 = [1]
    test4 = [-1, -1, -2]
    test5 = [0, 1, 0]

    print("--- Brute Force ---")
    print(brute_force(test1))  # 1
    print(brute_force(test2))  # 4

    print("--- Optimal (Sort) ---")
    print(optimal(test1))      # 1
    print(optimal(test2))      # 4

    print("--- Best (XOR) ---")
    print(best(test3))         # 1
    print(best(test4))         # -2
    print(best(test5))         # 1
