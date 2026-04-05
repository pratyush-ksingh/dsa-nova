"""
Problem: Maximum Consecutive Ones (LeetCode #485)
Difficulty: EASY | XP: 10

Given a binary array, find the maximum number of consecutive 1s.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check All Starting Points
# Time: O(n^2)  |  Space: O(1)
# For each index, count consecutive 1s from that index.
# ============================================================
def brute_force(nums: List[int]) -> int:
    max_count = 0
    n = len(nums)

    for i in range(n):
        if nums[i] == 1:
            count = 0
            j = i
            while j < n and nums[j] == 1:
                count += 1
                j += 1
            max_count = max(max_count, count)

    return max_count


# ============================================================
# APPROACH 2: OPTIMAL -- Single Pass with Running Counter
# Time: O(n)  |  Space: O(1)
# Increment on 1, reset on 0, track max.
# ============================================================
def optimal(nums: List[int]) -> int:
    count = 0
    max_count = 0

    for num in nums:
        if num == 1:
            count += 1
        else:
            count = 0
        max_count = max(max_count, count)

    return max_count


# ============================================================
# APPROACH 3: BEST -- String Split (Pythonic)
# Time: O(n)  |  Space: O(n) for the string
# Convert to string, split by '0', find longest segment.
# ============================================================
def best(nums: List[int]) -> int:
    s = "".join(map(str, nums))
    segments = s.split("0")
    return max(len(seg) for seg in segments)


if __name__ == "__main__":
    print("=== Maximum Consecutive Ones ===")

    test1 = [1, 1, 0, 1, 1, 1]
    test2 = [1, 0, 1, 1, 0, 1]
    test3 = [0, 0, 0]
    test4 = [1]
    test5 = [1, 1, 1, 1]
    test6 = [1, 0, 1, 0, 1]

    print("--- Brute Force ---")
    print(brute_force(test1))  # 3
    print(brute_force(test2))  # 2
    print(brute_force(test3))  # 0

    print("--- Optimal ---")
    print(optimal(test1))      # 3
    print(optimal(test2))      # 2
    print(optimal(test3))      # 0

    print("--- Best (String Split) ---")
    print(best(test4))         # 1
    print(best(test5))         # 4
    print(best(test6))         # 1
