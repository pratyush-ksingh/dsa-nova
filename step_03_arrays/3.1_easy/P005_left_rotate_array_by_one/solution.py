"""
Problem: Left Rotate Array by One
Difficulty: EASY | XP: 10

Given an array, rotate it to the left by one position.
[1,2,3,4,5] -> [2,3,4,5,1]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Extra Array)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Create a new array with elements shifted left by one."""
    n = len(arr)
    if n <= 1:
        return arr[:]
    return arr[1:] + arr[:1]


# ============================================================
# APPROACH 2: OPTIMAL (In-Place Shift)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Save first element, shift everything left, place at end."""
    result = arr[:]
    n = len(result)
    if n <= 1:
        return result

    first = result[0]
    for i in range(n - 1):
        result[i] = result[i + 1]
    result[n - 1] = first
    return result


# ============================================================
# APPROACH 3: BEST (Pythonic Slice)
# Time: O(n) | Space: O(n) internally
# ============================================================
def best(arr: List[int]) -> List[int]:
    """Use Python slicing for a clean one-liner."""
    if len(arr) <= 1:
        return arr[:]
    return arr[1:] + arr[:1]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Left Rotate Array by One ===\n")

    test_cases = [
        ([1, 2, 3, 4, 5], [2, 3, 4, 5, 1]),
        ([7], [7]),
        ([3, 9], [9, 3]),
        ([5, 5, 5, 5], [5, 5, 5, 5]),
        ([10, 20, 30], [20, 30, 10]),
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
