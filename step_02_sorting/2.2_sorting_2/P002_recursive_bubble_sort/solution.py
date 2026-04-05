"""
Problem: Recursive Bubble Sort
Difficulty: EASY | XP: 10

Implement bubble sort using recursion instead of nested loops.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Iterative Bubble Sort -- for comparison)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Standard iterative bubble sort."""
    result = arr[:]
    n = len(result)
    for i in range(n - 1, 0, -1):
        for j in range(i):
            if result[j] > result[j + 1]:
                result[j], result[j + 1] = result[j + 1], result[j]
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Recursive Bubble Sort)
# Time: O(n^2) | Space: O(n) recursion stack
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Recursive bubble sort: each call does one bubbling pass."""
    result = arr[:]

    def bubble_sort(n: int) -> None:
        if n <= 1:
            return

        # One pass: bubble largest to position n-1
        for j in range(n - 1):
            if result[j] > result[j + 1]:
                result[j], result[j + 1] = result[j + 1], result[j]

        # Recurse on remaining
        bubble_sort(n - 1)

    bubble_sort(len(result))
    return result


# ============================================================
# APPROACH 3: BEST (Recursive Bubble Sort with Early Termination)
# Time: O(n^2) worst, O(n) best | Space: O(n) recursion stack
# ============================================================
def best(arr: List[int]) -> List[int]:
    """Recursive bubble sort with swapped flag for early exit."""
    result = arr[:]

    def bubble_sort(n: int) -> None:
        if n <= 1:
            return

        swapped = False
        for j in range(n - 1):
            if result[j] > result[j + 1]:
                result[j], result[j + 1] = result[j + 1], result[j]
                swapped = True

        if not swapped:
            return

        bubble_sort(n - 1)

    bubble_sort(len(result))
    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Recursive Bubble Sort ===\n")

    test_cases = [
        ([64, 34, 25, 12, 22, 11, 90], [11, 12, 22, 25, 34, 64, 90]),
        ([5, 1, 4, 2, 8], [1, 2, 4, 5, 8]),
        ([1, 2, 3], [1, 2, 3]),
        ([3, 2, 1], [1, 2, 3]),
        ([5], [5]),
        ([2, 1], [1, 2]),
        ([3, 3, 3], [3, 3, 3]),
        ([-5, -1, -3], [-5, -3, -1]),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        be = best(arr)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    {arr}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
