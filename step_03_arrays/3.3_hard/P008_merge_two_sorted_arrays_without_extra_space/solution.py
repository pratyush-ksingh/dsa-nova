"""
Problem: Merge Two Sorted Arrays Without Extra Space
Difficulty: HARD | XP: 50

Given two sorted arrays A (size m) and B (size n), merge them in-place
so that A contains the m smallest elements sorted, and B contains the rest sorted.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O((m+n) log(m+n))  |  Space: O(m+n)
# ============================================================
def brute_force(A: List[int], B: List[int]) -> None:
    """
    Combine both arrays, sort, then split back — uses extra space.
    Shown as reference for correctness.
    Real-life: Merging two sorted customer databases.
    """
    combined = sorted(A + B)
    m, n = len(A), len(B)
    A[:] = combined[:m]
    B[:] = combined[m:]


# ============================================================
# APPROACH 2: OPTIMAL  (Two-pointer swap)
# Time: O(m * n)  |  Space: O(1)
# ============================================================
def optimal(A: List[int], B: List[int]) -> None:
    """
    Two pointers i=end of A, j=start of B.
    If A[i] > B[j]: swap, fix B by bubbling right, fix A by bubbling left.
    Real-life: In-place merge in embedded systems with no heap allocation.
    """
    m, n = len(A), len(B)
    i, j = m - 1, 0
    while i >= 0 and j < n:
        if A[i] > B[j]:
            A[i], B[j] = B[j], A[i]
            # Fix A: bubble down
            k = i - 1
            while k >= 0 and A[k] > A[k + 1]:
                A[k], A[k + 1] = A[k + 1], A[k]
                k -= 1
            # Fix B: bubble up
            k = j + 1
            while k < n and B[k] < B[k - 1]:
                B[k], B[k - 1] = B[k - 1], B[k]
                k += 1
        i -= 1
        j += 1


# ============================================================
# APPROACH 3: BEST  (Gap / Shell method)
# Time: O((m+n) log(m+n))  |  Space: O(1)
# ============================================================
def best(A: List[int], B: List[int]) -> None:
    """
    Gap algorithm (Shell sort inspired): start with gap = ceil((m+n)/2),
    compare elements gap apart (treating A and B as a virtual combined array),
    swap if out of order. Halve gap each round.
    Real-life: Cache-efficient in-place merge used in timsort variants.
    """
    m, n = len(A), len(B)
    total = m + n
    gap = (total + 1) // 2

    def get(idx: int) -> int:
        return A[idx] if idx < m else B[idx - m]

    def put(idx: int, val: int) -> None:
        if idx < m:
            A[idx] = val
        else:
            B[idx - m] = val

    while gap > 0:
        left = 0
        while left + gap < total:
            right = left + gap
            lv, rv = get(left), get(right)
            if lv > rv:
                put(left, rv)
                put(right, lv)
            left += 1
        if gap == 1:
            break
        gap = (gap + 1) // 2


if __name__ == "__main__":
    print("=== Merge Two Sorted Arrays Without Extra Space ===")
    tests = [
        ([1, 3, 5, 7], [0, 2, 6, 8, 9]),
        ([1],           [2]),
        ([10, 12],      [5, 18, 20]),
    ]
    for A_orig, B_orig in tests:
        print(f"\nA={A_orig}  B={B_orig}")
        a1, b1 = A_orig[:], B_orig[:]
        brute_force(a1, b1)
        print(f"  Brute:   A={a1}  B={b1}")
        a2, b2 = A_orig[:], B_orig[:]
        optimal(a2, b2)
        print(f"  Optimal: A={a2}  B={b2}")
        a3, b3 = A_orig[:], B_orig[:]
        best(a3, b3)
        print(f"  Best:    A={a3}  B={b3}")
