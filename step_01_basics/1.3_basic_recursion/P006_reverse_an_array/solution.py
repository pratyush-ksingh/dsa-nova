"""
Problem: Reverse an Array
Difficulty: EASY | XP: 10

Reverse an array in-place. Show iterative and recursive two-pointer approaches.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (New Array -- Extra Space)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Create a new reversed array (not in-place)."""
    n = len(arr)
    return [arr[n - 1 - i] for i in range(n)]


# ============================================================
# APPROACH 2: OPTIMAL (Iterative Two-Pointer Swap)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(arr: List[int]) -> None:
    """Two pointers from both ends, swap and converge. In-place."""
    left, right = 0, len(arr) - 1
    while left < right:
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1


# ============================================================
# APPROACH 3: BEST (Recursive Two-Pointer Swap)
# Time: O(n) | Space: O(n/2) = O(n) recursion stack
# ============================================================
def best(arr: List[int]) -> None:
    """Recursive version of two-pointer swap. In-place."""
    def helper(left: int, right: int) -> None:
        if left >= right:
            return  # base case: pointers crossed or met
        arr[left], arr[right] = arr[right], arr[left]
        helper(left + 1, right - 1)

    helper(0, len(arr) - 1)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Reverse an Array ===\n")

    test_cases = [
        ([1, 2, 3, 4, 5], [5, 4, 3, 2, 1]),
        ([1, 2, 3, 4],    [4, 3, 2, 1]),
        ([1],              [1]),
        ([],               []),
        ([1, 2],           [2, 1]),
        ([-3, 0, 5, -1, 7], [7, -1, 5, 0, -3]),
    ]

    for original, expected in test_cases:
        # Brute force (returns new array)
        b = brute_force(original[:])

        # Optimal (in-place)
        o_arr = original[:]
        optimal(o_arr)

        # Best (recursive in-place)
        r_arr = original[:]
        best(r_arr)

        status = "PASS" if b == expected and o_arr == expected and r_arr == expected else "FAIL"
        print(f"Input:     {original}")
        print(f"  Brute:     {b}")
        print(f"  Iterative: {o_arr}")
        print(f"  Recursive: {r_arr}")
        print(f"  Expected:  {expected}  [{status}]\n")
