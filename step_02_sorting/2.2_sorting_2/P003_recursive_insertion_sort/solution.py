"""
Problem: Recursive Insertion Sort
Difficulty: EASY | XP: 10

Sort an array using recursive insertion sort.
"""
from typing import List
import sys
sys.setrecursionlimit(20000)


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Insertion Sort
# Time: O(n^2) worst, O(n) best  |  Space: O(1)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    result = arr[:]
    n = len(result)
    for i in range(1, n):
        key = result[i]
        j = i - 1
        while j >= 0 and result[j] > key:
            result[j + 1] = result[j]
            j -= 1
        result[j + 1] = key
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Insertion Sort
# Time: O(n^2) worst, O(n) best  |  Space: O(n) stack
# Recursively sort first n-1 elements, then insert n-th.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    result = arr[:]

    def recursive_insertion_sort(n: int):
        if n <= 1:
            return
        recursive_insertion_sort(n - 1)

        # Insert result[n-1] into sorted result[0..n-2]
        key = result[n - 1]
        j = n - 2
        while j >= 0 and result[j] > key:
            result[j + 1] = result[j]
            j -= 1
        result[j + 1] = key

    recursive_insertion_sort(len(result))
    return result


# ============================================================
# APPROACH 3: BEST -- Fully Recursive (insert step also recursive)
# Time: O(n^2)  |  Space: O(n) for sort + O(n) for insert
# No loops at all -- pure recursion.
# ============================================================
def best(arr: List[int]) -> List[int]:
    result = arr[:]

    def recursive_insert(pos: int):
        if pos == 0 or result[pos - 1] <= result[pos]:
            return
        result[pos], result[pos - 1] = result[pos - 1], result[pos]
        recursive_insert(pos - 1)

    def recursive_insertion_sort(n: int):
        if n <= 1:
            return
        recursive_insertion_sort(n - 1)
        recursive_insert(n - 1)

    recursive_insertion_sort(len(result))
    return result


if __name__ == "__main__":
    tests = [
        [12, 11, 13, 5, 6],
        [5, 4, 3, 2, 1],
        [1],
        [2, 1],
        [1, 2, 3, 4, 5],
        [-3, 4, -1, 0, 2],
    ]
    print("=== Recursive Insertion Sort ===")
    for t in tests:
        print(f"Input:  {t}")
        print(f"  Brute:   {brute_force(t)}")
        print(f"  Optimal: {optimal(t)}")
        print(f"  Best:    {best(t)}")
