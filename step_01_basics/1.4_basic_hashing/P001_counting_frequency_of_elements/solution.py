"""
Problem: Counting Frequency of Elements
Difficulty: EASY | XP: 10

Given an array, count the frequency of each element using hashing.
"""
from typing import List, Dict
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Loop Counting
# Time: O(N^2)  |  Space: O(N)
# For each element, scan entire array to count. Track visited.
# ============================================================
def brute_force(arr: List[int]) -> Dict[int, int]:
    result = {}
    visited = [False] * len(arr)

    for i in range(len(arr)):
        if visited[i]:
            continue

        count = 0
        for j in range(i, len(arr)):
            if arr[j] == arr[i]:
                count += 1
                visited[j] = True
        result[arr[i]] = count

    return result


# ============================================================
# APPROACH 2: OPTIMAL -- HashMap (Single Pass)
# Time: O(N)  |  Space: O(K) where K = unique elements
# One pass: for each element, increment its count in the dict.
# ============================================================
def optimal(arr: List[int]) -> Dict[int, int]:
    freq = {}
    for x in arr:
        freq[x] = freq.get(x, 0) + 1
    return freq


# ============================================================
# APPROACH 3: BEST -- collections.Counter (Pythonic)
# Time: O(N)  |  Space: O(K)
# Python's built-in Counter does exactly this in one line.
# Under the hood it is the same as optimal, but idiomatic.
# ============================================================
def best(arr: List[int]) -> Dict[int, int]:
    return dict(Counter(arr))


if __name__ == "__main__":
    test_cases = [
        [1, 3, 2, 1, 3, 1, 2],
        [5],
        [7, 7, 7],
        [1, 2, 3, 4, 5],
    ]
    print("=== Counting Frequency of Elements ===")

    for arr in test_cases:
        print(f"\nInput: {arr}")
        print(f"  Brute Force: {brute_force(arr)}")
        print(f"  Optimal:     {optimal(arr)}")
        print(f"  Best:        {best(arr)}")
