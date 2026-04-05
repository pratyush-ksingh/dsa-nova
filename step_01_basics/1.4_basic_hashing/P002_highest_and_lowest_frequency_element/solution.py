"""
Problem: Highest and Lowest Frequency Element
Difficulty: EASY | XP: 10

Given an array, find the elements with the highest and lowest frequency.
"""
from typing import List, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Loop Counting
# Time: O(N^2)  |  Space: O(N)
# For each unique element, scan entire array to count.
# Track max/min frequency as we go.
# ============================================================
def brute_force(arr: List[int]) -> Tuple[int, int]:
    visited = [False] * len(arr)
    max_freq, min_freq = 0, float('inf')
    max_elem, min_elem = arr[0], arr[0]

    for i in range(len(arr)):
        if visited[i]:
            continue

        count = 0
        for j in range(i, len(arr)):
            if arr[j] == arr[i]:
                count += 1
                visited[j] = True

        if count > max_freq:
            max_freq = count
            max_elem = arr[i]
        if count < min_freq:
            min_freq = count
            min_elem = arr[i]

    return max_elem, min_elem


# ============================================================
# APPROACH 2: OPTIMAL -- HashMap + Linear Scan
# Time: O(N)  |  Space: O(K) where K = unique elements
# Build frequency map in one pass, then scan for max/min.
# ============================================================
def optimal(arr: List[int]) -> Tuple[int, int]:
    freq = {}
    for x in arr:
        freq[x] = freq.get(x, 0) + 1

    max_freq, min_freq = 0, float('inf')
    max_elem, min_elem = arr[0], arr[0]

    for elem, count in freq.items():
        if count > max_freq:
            max_freq = count
            max_elem = elem
        if count < min_freq:
            min_freq = count
            min_elem = elem

    return max_elem, min_elem


if __name__ == "__main__":
    test_cases = [
        [10, 5, 10, 15, 10, 5],
        [1, 1, 1, 1],
        [7],
        [3, 2, 3, 2],
        [1, 2, 3, 4, 5],
    ]

    print("=== Highest and Lowest Frequency Element ===")
    for arr in test_cases:
        print(f"\nInput: {arr}")
        h, l = brute_force(arr)
        print(f"  Brute Force -> Highest: {h}, Lowest: {l}")
        h, l = optimal(arr)
        print(f"  Optimal     -> Highest: {h}, Lowest: {l}")
