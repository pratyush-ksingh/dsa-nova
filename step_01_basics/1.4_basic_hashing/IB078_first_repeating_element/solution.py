"""
Problem: First Repeating Element
Difficulty: EASY | XP: 10
Source: InterviewBit

Given an array, find the first element (by index of first occurrence)
that appears more than once. Return -1 if no such element exists.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Loops
# Time: O(N^2)  |  Space: O(1)
# For each element, scan the rest of the array for a duplicate.
# Track the earliest first-occurrence index that has a repeat.
# ============================================================
def brute_force(arr: List[int]) -> int:
    n = len(arr)
    for i in range(n):
        for j in range(i + 1, n):
            if arr[i] == arr[j]:
                return arr[i]
    return -1


# ============================================================
# APPROACH 2: OPTIMAL -- HashMap (Count Frequency, Then Scan)
# Time: O(N)  |  Space: O(N)
# First pass: count frequency of each element.
# Second pass: return first element with frequency > 1.
# ============================================================
def optimal(arr: List[int]) -> int:
    freq = {}
    for x in arr:
        freq[x] = freq.get(x, 0) + 1
    for x in arr:
        if freq[x] > 1:
            return x
    return -1


# ============================================================
# APPROACH 3: BEST -- Reverse Scan with Set
# Time: O(N)  |  Space: O(N)
# Scan from right to left. Maintain a set of seen elements.
# If current element is already in set, update the answer.
# The last update gives the leftmost repeating element.
# ============================================================
def best(arr: List[int]) -> int:
    seen = set()
    result = -1
    for i in range(len(arr) - 1, -1, -1):
        if arr[i] in seen:
            result = arr[i]
        else:
            seen.add(arr[i])
    return result


if __name__ == "__main__":
    test_cases = [
        [10, 5, 3, 4, 3, 5, 6],
        [6, 10, 5, 4, 9, 120],
        [1, 2, 3, 1, 2],
        [1],
    ]
    print("=== First Repeating Element ===")
    for tc in test_cases:
        print(f"  {tc}: brute={brute_force(tc)}, optimal={optimal(tc)}, best={best(tc)}")
