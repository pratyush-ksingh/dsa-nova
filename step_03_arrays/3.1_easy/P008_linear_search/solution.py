"""
Problem: Linear Search
Difficulty: EASY | XP: 10

Find the index of a target in an unsorted array. Return -1 if not found.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Basic Linear Search)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(arr: List[int], target: int) -> int:
    """Scan left to right, return index on match."""
    for i in range(len(arr)):
        if arr[i] == target:
            return i
    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Sentinel Linear Search)
# Time: O(n) | Space: O(1) -- fewer comparisons per iteration
# ============================================================
def optimal(arr: List[int], target: int) -> int:
    """Place target as sentinel at end to skip bounds check in loop."""
    a = arr[:]
    n = len(a)
    if n == 0:
        return -1

    last = a[n - 1]
    a[n - 1] = target

    i = 0
    while a[i] != target:
        i += 1

    a[n - 1] = last

    if i < n - 1 or last == target:
        return i
    return -1


# ============================================================
# APPROACH 3: BEST (Search from Both Ends)
# Time: O(n) worst, ~n/2 avg | Space: O(1)
# ============================================================
def best(arr: List[int], target: int) -> int:
    """Check from both ends simultaneously."""
    left, right = 0, len(arr) - 1

    while left <= right:
        if arr[left] == target:
            return left
        if arr[right] == target:
            return right
        left += 1
        right -= 1

    return -1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Linear Search ===\n")

    test_cases = [
        ([4, 1, 7, 3, 9], 7, 2),
        ([4, 1, 7, 3, 9], 5, -1),
        ([10], 10, 0),
        ([10], 3, -1),
        ([2, 2, 2], 2, 0),
        ([5, 1, 2], 5, 0),
    ]

    for arr, target, expected in test_cases:
        b = brute_force(arr, target)
        o = optimal(arr, target)
        be = best(arr, target)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    {arr}, target={target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
