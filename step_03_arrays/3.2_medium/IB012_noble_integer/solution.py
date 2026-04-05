"""
Problem: Noble Integer (InterviewBit)
Difficulty: EASY | XP: 10

Find if there exists an integer x in the array such that the
count of numbers strictly greater than x equals x.
Return 1 if exists, -1 otherwise.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Each Element
# Time: O(n^2)  |  Space: O(1)
# For each element, count how many are strictly greater.
# ============================================================
def brute_force(A: List[int]) -> int:
    n = len(A)
    for i in range(n):
        count = sum(1 for j in range(n) if A[j] > A[i])
        if count == A[i]:
            return 1
    return -1


# ============================================================
# APPROACH 2: OPTIMAL -- Sort + Index-Based Count
# Time: O(n log n)  |  Space: O(n) for sorted copy
# After sorting, count_greater(A[i]) = n - 1 - i (last occurrence).
# ============================================================
def optimal(A: List[int]) -> int:
    A_sorted = sorted(A)
    n = len(A_sorted)

    for i in range(n):
        # Skip duplicates: only process the last occurrence
        if i < n - 1 and A_sorted[i] == A_sorted[i + 1]:
            continue
        # Count of elements strictly greater = n - 1 - i
        count_greater = n - 1 - i
        if A_sorted[i] == count_greater:
            return 1

    return -1


# ============================================================
# APPROACH 3: BEST -- Sort + Clean Single Pass
# Time: O(n log n)  |  Space: O(n)
# Same logic, explicitly handles negative values.
# ============================================================
def best(A: List[int]) -> int:
    A_sorted = sorted(A)
    n = len(A_sorted)

    for i in range(n):
        # Skip to last occurrence of this value
        if i < n - 1 and A_sorted[i] == A_sorted[i + 1]:
            continue
        count_greater = n - 1 - i
        # Noble integer must be non-negative (count is always >= 0)
        if A_sorted[i] >= 0 and A_sorted[i] == count_greater:
            return 1

    return -1


if __name__ == "__main__":
    print("=== Noble Integer ===")

    test1 = [3, 2, 1, 3]     # Noble: 2 (count > 2 = 2)
    test2 = [5, 6, 2]         # Noble: 2 (count > 2 = 2)
    test3 = [1, 1, 1]         # No noble integer
    test4 = [0]                # Noble: 0 (count > 0 = 0)
    test5 = [7, 3, 16, 10]    # Noble: 3 (count > 3 = 3)
    test6 = [0, 1, 2]         # Noble: 1 (count > 1 = 1)

    print("--- Brute Force ---")
    print(brute_force(test1))  # 1
    print(brute_force(test2))  # 1
    print(brute_force(test3))  # -1

    print("--- Optimal ---")
    print(optimal(test1))      # 1
    print(optimal(test2))      # 1
    print(optimal(test3))      # -1

    print("--- Best ---")
    print(best(test4))         # 1
    print(best(test5))         # 1
    print(best(test6))         # 1
