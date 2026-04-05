"""
Problem: Upper Bound
Difficulty: EASY | XP: 10

Find the smallest index i such that arr[i] > target in a sorted array.
If no such index exists, return n.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(arr: List[int], x: int) -> int:
    """Scan left to right, return first index where arr[i] > x."""
    for i in range(len(arr)):
        if arr[i] > x:
            return i
    return len(arr)


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search)
# Time: O(log n) | Space: O(1)
# ============================================================
def upper_bound(arr: List[int], x: int) -> int:
    """Binary search for the first element strictly greater than x."""
    low, high = 0, len(arr) - 1
    ans = len(arr)  # default: no element > x

    while low <= high:
        mid = low + (high - low) // 2
        if arr[mid] > x:
            ans = mid       # candidate, search left for smaller index
            high = mid - 1
        else:
            low = mid + 1   # arr[mid] <= x, search right

    return ans


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Upper Bound ===\n")

    arr = [1, 2, 3, 5, 5, 5, 7]

    test_cases = [
        (5, 6),   # first > 5 is 7 at index 6
        (4, 3),   # first > 4 is 5 at index 3
        (7, 7),   # no element > 7, return n=7
        (0, 0),   # all elements > 0, return 0
        (1, 1),   # first > 1 is 2 at index 1
        (6, 6),   # first > 6 is 7 at index 6
        (10, 7),  # no element > 10, return 7
    ]

    all_pass = True
    for x, expected in test_cases:
        b = brute_force(arr, x)
        o = upper_bound(arr, x)
        ok = b == o == expected
        all_pass &= ok
        print(f"x={x:2d} | Brute={b} Optimal={o} | Expected={expected} [{'PASS' if ok else 'FAIL'}]")

    # Edge cases
    for a, x, exp in [([5], 5, 1), ([5], 3, 0), ([5, 5, 5], 5, 3)]:
        b = brute_force(a, x)
        o = upper_bound(a, x)
        ok = b == o == exp
        all_pass &= ok
        print(f"arr={a}, x={x} | Brute={b} Optimal={o} | Expected={exp} [{'PASS' if ok else 'FAIL'}]")

    print(f"\nAll pass: {all_pass}")
