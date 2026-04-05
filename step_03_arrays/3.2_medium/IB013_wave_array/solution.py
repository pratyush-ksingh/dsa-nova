"""
Problem: Wave Array
Difficulty: EASY | XP: 10
Source: InterviewBit

Sort the array in wave form: a[0] >= a[1] <= a[2] >= a[3] <= a[4] ...
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Sort + Swap Adjacent Pairs)
# Time: O(n log n)  |  Space: O(1) extra (in-place sort)
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Sort the array, then swap every adjacent pair to create wave pattern."""
    arr = arr[:]  # copy to avoid modifying input
    arr.sort()

    # Swap elements at indices (0,1), (2,3), (4,5), ...
    for i in range(0, len(arr) - 1, 2):
        arr[i], arr[i + 1] = arr[i + 1], arr[i]

    return arr


# ============================================================
# APPROACH 2: OPTIMAL (Sort + Swap -- Same as Brute, Simple & Correct)
# Time: O(n log n)  |  Space: O(1) extra
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Same approach as brute force. Sorting guarantees correctness of wave form
    since after sorting, swapping adjacent pairs always produces valid wave."""
    arr = arr[:]
    arr.sort()

    for i in range(0, len(arr) - 1, 2):
        arr[i], arr[i + 1] = arr[i + 1], arr[i]

    return arr


# ============================================================
# APPROACH 3: BEST (Single Pass -- Compare with Neighbors)
# Time: O(n)  |  Space: O(1) extra
# ============================================================
def best(arr: List[int]) -> List[int]:
    """Single pass: at every even index, ensure arr[i] >= neighbors.
    If arr[i] < arr[i-1], swap. If arr[i] < arr[i+1], swap."""
    arr = arr[:]
    n = len(arr)

    for i in range(0, n, 2):
        # If current is less than previous, swap
        if i > 0 and arr[i] < arr[i - 1]:
            arr[i], arr[i - 1] = arr[i - 1], arr[i]

        # If current is less than next, swap
        if i < n - 1 and arr[i] < arr[i + 1]:
            arr[i], arr[i + 1] = arr[i + 1], arr[i]

    return arr


def is_wave(arr: List[int]) -> bool:
    """Verify wave property: a[0]>=a[1]<=a[2]>=a[3]..."""
    for i in range(len(arr)):
        if i % 2 == 0:  # even index: should be >= neighbors
            if i > 0 and arr[i] < arr[i - 1]:
                return False
            if i < len(arr) - 1 and arr[i] < arr[i + 1]:
                return False
        else:  # odd index: should be <= neighbors
            if i > 0 and arr[i] > arr[i - 1]:
                return False
            if i < len(arr) - 1 and arr[i] > arr[i + 1]:
                return False
    return True


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Wave Array ===\n")

    test_cases = [
        [1, 2, 3, 4, 5],
        [10, 5, 6, 3, 2, 20, 100, 80],
        [1, 2, 3, 4],
        [5],
        [3, 3, 3, 3],
        [1, 2],
        [20, 10, 8, 6, 4, 2],
    ]

    for arr in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        r = best(arr)
        b_ok = is_wave(b)
        o_ok = is_wave(o)
        r_ok = is_wave(r)
        status = "PASS" if b_ok and o_ok and r_ok else "FAIL"
        print(f"Input:   {arr}")
        print(f"  Brute:    {b}  valid={b_ok}")
        print(f"  Optimal:  {o}  valid={o_ok}")
        print(f"  Best:     {r}  valid={r_ok}")
        print(f"  [{status}]\n")
