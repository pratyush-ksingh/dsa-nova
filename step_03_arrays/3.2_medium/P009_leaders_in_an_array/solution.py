"""
Problem: Leaders in an Array
Difficulty: EASY | XP: 10

Find all leaders: element is a leader if strictly greater than
all elements to its right. Rightmost element is always a leader.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Every Suffix
# Time: O(n^2)  |  Space: O(1) excluding output
# For each element, scan all to its right.
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    n = len(arr)
    leaders = []

    for i in range(n):
        is_leader = True
        for j in range(i + 1, n):
            if arr[j] >= arr[i]:
                is_leader = False
                break
        if is_leader:
            leaders.append(arr[i])

    return leaders


# ============================================================
# APPROACH 2: OPTIMAL -- Right-to-Left Single Pass
# Time: O(n)  |  Space: O(1) excluding output
# Track max_from_right, scan backwards, reverse result.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    n = len(arr)
    leaders = []
    max_from_right = float("-inf")

    for i in range(n - 1, -1, -1):
        if arr[i] > max_from_right:
            leaders.append(arr[i])
        max_from_right = max(max_from_right, arr[i])

    leaders.reverse()
    return leaders


# ============================================================
# APPROACH 3: BEST -- Stack-Based Variant
# Time: O(n)  |  Space: O(n) for the stack
# Right-to-left scan using a stack. Same time, more space.
# ============================================================
def best(arr: List[int]) -> List[int]:
    stack = []

    for i in range(len(arr) - 1, -1, -1):
        if not stack or arr[i] > stack[-1]:
            stack.append(arr[i])

    # Stack has leaders from top (leftmost) to bottom (rightmost)
    stack.reverse()
    return stack


if __name__ == "__main__":
    print("=== Leaders in an Array ===")

    test1 = [16, 17, 4, 3, 5, 2]
    test2 = [1, 2, 3, 4, 5]
    test3 = [5, 4, 3, 2, 1]
    test4 = [7]
    test5 = [5, 5, 5]

    print("--- Brute Force ---")
    print(brute_force(test1))  # [17, 5, 2]
    print(brute_force(test2))  # [5]
    print(brute_force(test3))  # [5, 4, 3, 2, 1]

    print("--- Optimal ---")
    print(optimal(test1))      # [17, 5, 2]
    print(optimal(test2))      # [5]
    print(optimal(test3))      # [5, 4, 3, 2, 1]

    print("--- Best (Stack) ---")
    print(best(test1))         # [17, 5, 2]
    print(best(test4))         # [7]
    print(best(test5))         # [5]
