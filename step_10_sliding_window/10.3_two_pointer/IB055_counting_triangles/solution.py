"""
Problem: Counting Triangles (InterviewBit)
Difficulty: MEDIUM | XP: 25

Given an array of integers, count the number of triplets (i, j, k) such that
A[i], A[j], A[k] can form a valid triangle (sum of any two sides > third side).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Triplets)
# Time: O(n^3) | Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> int:
    """Check every triplet and validate triangle inequality."""
    n = len(A)
    count = 0
    for i in range(n):
        for j in range(i + 1, n):
            for k in range(j + 1, n):
                a, b, c = A[i], A[j], A[k]
                if a + b > c and a + c > b and b + c > a:
                    count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL (Sort + Two Pointer)
# Time: O(n^2) | Space: O(n) for sorted copy
# ============================================================
def optimal(A: List[int]) -> int:
    """Sort the array. For each largest side, use two pointers to count valid pairs."""
    A = sorted(A)
    n = len(A)
    count = 0

    # Fix the largest side as A[k]
    for k in range(2, n):
        left, right = 0, k - 1
        while left < right:
            if A[left] + A[right] > A[k]:
                # All pairs from left..right-1 with right are valid
                count += right - left
                right -= 1
            else:
                left += 1
    return count


# ============================================================
# APPROACH 3: BEST (Same Sort + Two Pointer, cleanest form)
# Time: O(n^2) | Space: O(n) for sorted copy
# ============================================================
def best(A: List[int]) -> int:
    """Sort + two pointer -- this IS the optimal approach for this problem."""
    A = sorted(A)
    n = len(A)
    count = 0

    for i in range(n - 1, 1, -1):  # i is the largest side index
        lo, hi = 0, i - 1
        while lo < hi:
            if A[lo] + A[hi] > A[i]:
                count += hi - lo  # all indices from lo to hi-1 paired with hi work
                hi -= 1
            else:
                lo += 1
    return count


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Counting Triangles ===\n")

    test_cases = [
        ([3, 4, 6, 7], 3),
        ([10, 21, 22, 100, 101, 200, 300], 6),
        ([1, 1, 1, 1], 4),
        ([1, 2, 3], 0),
        ([4, 6, 3, 7], 3),
        ([1], 0),
        ([1, 2], 0),
    ]

    for A, expected in test_cases:
        b = brute_force(A[:])
        o = optimal(A[:])
        h = best(A[:])
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: A={A}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
