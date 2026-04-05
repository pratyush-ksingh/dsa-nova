"""
Problem: Balance Array
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Find the number of "special" indices i such that if A[i] is removed,
the sum of elements at even positions equals the sum at odd positions
(0-indexed, after removal re-indexes remaining elements).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> int:
    """
    For each index i, simulate removal and compute even/odd sums of remaining array.
    Real-life: Checking balance conditions when removing an element from a dataset.
    """
    n = len(A)
    count = 0
    for skip in range(n):
        even_sum = odd_sum = 0
        pos = 0
        for i in range(n):
            if i == skip:
                continue
            if pos % 2 == 0:
                even_sum += A[i]
            else:
                odd_sum += A[i]
            pos += 1
        if even_sum == odd_sum:
            count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(A: List[int]) -> int:
    """
    Precompute prefix even/odd sums. For removing index i:
      newEven = prefixEven[i] + suffixOdd[i+1]   (right-side parities flip)
      newOdd  = prefixOdd[i]  + suffixEven[i+1]
    Real-life: Fast dataset rebalancing checks in distributed systems.
    """
    n = len(A)
    prefix_even = [0] * (n + 1)
    prefix_odd  = [0] * (n + 1)
    for i in range(n):
        prefix_even[i + 1] = prefix_even[i] + (A[i] if i % 2 == 0 else 0)
        prefix_odd[i + 1]  = prefix_odd[i]  + (A[i] if i % 2 == 1 else 0)

    total_even = prefix_even[n]
    total_odd  = prefix_odd[n]
    count = 0
    for i in range(n):
        suffix_even = total_even - prefix_even[i + 1]
        suffix_odd  = total_odd  - prefix_odd[i + 1]
        new_even = prefix_even[i] + suffix_odd
        new_odd  = prefix_odd[i]  + suffix_even
        if new_even == new_odd:
            count += 1
    return count


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(A: List[int]) -> int:
    """
    Maintain running left/right sums without prefix arrays.
    Real-life: In-place streaming analytics for balance checking.
    """
    n = len(A)
    right_even = sum(A[i] for i in range(0, n, 2))
    right_odd  = sum(A[i] for i in range(1, n, 2))
    left_even = left_odd = 0
    count = 0
    for i in range(n):
        if i % 2 == 0:
            right_even -= A[i]
        else:
            right_odd -= A[i]
        new_even = left_even + right_odd
        new_odd  = left_odd  + right_even
        if new_even == new_odd:
            count += 1
        if i % 2 == 0:
            left_even += A[i]
        else:
            left_odd += A[i]
    return count


if __name__ == "__main__":
    print("=== Balance Array ===")
    tests = [
        ([2, 1, 6, 4], 1),
        ([1, 2, 3],    1),
        ([1],          1),
        ([1, 1, 1],    3),
    ]
    for A, exp in tests:
        print(f"\nInput: {A}  =>  expected: {exp}")
        print(f"  Brute:   {brute_force(A)}")
        print(f"  Optimal: {optimal(A)}")
        print(f"  Best:    {best(A)}")
