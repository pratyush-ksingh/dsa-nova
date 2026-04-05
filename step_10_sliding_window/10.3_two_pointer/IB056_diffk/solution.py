"""
Problem: Diffk (InterviewBit)
Difficulty: EASY | XP: 10

Given a sorted array A and integer B, find if there exists a pair
with absolute difference B. Return 1/0.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Pairs)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """Try every pair (i, j) where j > i."""
    n = len(A)
    for i in range(n):
        for j in range(i + 1, n):
            # Array sorted => A[j] >= A[i], so diff = A[j] - A[i]
            if A[j] - A[i] == B:
                return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL (Two Pointers -- Same Direction)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """Both pointers move left to right. Advance j to widen gap, i to shrink it."""
    n = len(A)
    if n < 2:
        return 0

    i, j = 0, 1
    while j < n:
        diff = A[j] - A[i]
        if diff == B and i != j:
            return 1
        elif diff < B:
            j += 1
        else:
            i += 1
            if i == j:
                j += 1
    return 0


# ============================================================
# APPROACH 3: BEST (HashSet -- general, works even unsorted)
# Time: O(n) | Space: O(n)
# ============================================================
def best(A: List[int], B: int) -> int:
    """For each element, check if element + B exists in a set."""
    n = len(A)
    if n < 2:
        return 0

    # B == 0: need duplicate values at different indices
    if B == 0:
        seen = set()
        for a in A:
            if a in seen:
                return 1
            seen.add(a)
        return 0

    s = set(A)
    for a in s:
        if (a + B) in s:
            return 1
    return 0


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Diffk ===\n")

    test_cases = [
        ([1, 2, 3, 5], 4, 1),
        ([1, 3, 5], 4, 1),
        ([1, 2, 3], 0, 0),
        ([1, 1, 3], 0, 1),
        ([1], 5, 0),
        ([1, 2, 3, 4, 5], 1, 1),
        ([1, 10, 100], 90, 1),
        ([1, 2, 3, 4], 5, 0),
    ]

    for A, B, expected in test_cases:
        b = brute_force(A[:], B)
        o = optimal(A[:], B)
        h = best(A[:], B)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: A={A}, B={B}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  HashSet:  {h}")
        print(f"  Expected: {expected}  [{status}]\n")
