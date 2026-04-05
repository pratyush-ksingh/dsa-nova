"""
Problem: Find Missing Number (LeetCode #268)
Difficulty: EASY | XP: 10

Array of n distinct numbers from [0, n]. Find the missing one.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashSet
# Time: O(n)  |  Space: O(n)
# Insert all into set, check which 0..n is missing.
# ============================================================
def brute_force(nums: List[int]) -> int:
    num_set = set(nums)
    n = len(nums)
    for i in range(n + 1):
        if i not in num_set:
            return i
    return -1  # unreachable


# ============================================================
# APPROACH 2: OPTIMAL -- Sum Formula (Gauss's Trick)
# Time: O(n)  |  Space: O(1)
# expected_sum - actual_sum = missing number.
# ============================================================
def optimal(nums: List[int]) -> int:
    n = len(nums)
    expected_sum = n * (n + 1) // 2
    actual_sum = sum(nums)
    return expected_sum - actual_sum


# ============================================================
# APPROACH 3: BEST -- XOR Cancellation
# Time: O(n)  |  Space: O(1)
# XOR all indices [0..n] with all array values. Pairs cancel.
# ============================================================
def best(nums: List[int]) -> int:
    n = len(nums)
    xor = 0

    # XOR with indices 0 to n
    for i in range(n + 1):
        xor ^= i

    # XOR with array elements
    for num in nums:
        xor ^= num

    return xor


if __name__ == "__main__":
    print("=== Find Missing Number ===")

    test1 = [3, 0, 1]
    test2 = [0, 1]
    test3 = [9, 6, 4, 2, 3, 5, 7, 0, 1]
    test4 = [0]
    test5 = [1]

    print("--- Brute Force ---")
    print(brute_force(test1))  # 2
    print(brute_force(test2))  # 2
    print(brute_force(test3))  # 8

    print("--- Optimal (Sum) ---")
    print(optimal(test1))      # 2
    print(optimal(test2))      # 2
    print(optimal(test3))      # 8

    print("--- Best (XOR) ---")
    print(best(test1))         # 2
    print(best(test4))         # 1
    print(best(test5))         # 0
