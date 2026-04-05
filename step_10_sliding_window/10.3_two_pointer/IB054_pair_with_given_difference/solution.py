"""
Problem: Pair With Given Difference (InterviewBit)
Difficulty: EASY | XP: 10

Given array A and integer B, find if there exists a pair (i,j) with
A[i] - A[j] = B and i != j. Return 1 if exists, 0 otherwise.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Pairs)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """Try every pair (i, j) with i != j."""
    n = len(A)
    for i in range(n):
        for j in range(n):
            if i != j and A[i] - A[j] == B:
                return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL (Sort + Two Pointers)
# Time: O(n log n) | Space: O(n) for sorted copy
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """Sort, then walk two pointers to find the required difference."""
    n = len(A)
    if n < 2:
        return 0

    A = sorted(A)
    i, j = 1, 0

    while i < n:
        diff = A[i] - A[j]
        if diff == B and i != j:
            return 1
        elif diff < B:
            i += 1
        else:
            j += 1
            if j == i:
                i += 1
    return 0


# ============================================================
# APPROACH 3: BEST (HashSet Lookup)
# Time: O(n) | Space: O(n)
# ============================================================
def best(A: List[int], B: int) -> int:
    """For each element, check if (element - B) exists in a set."""
    n = len(A)
    if n < 2:
        return 0

    # Special case: B == 0 means we need duplicate values
    if B == 0:
        seen = set()
        for a in A:
            if a in seen:
                return 1
            seen.add(a)
        return 0

    # General case
    s = set(A)
    for a in s:
        if (a - B) in s:
            return 1
    return 0


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Pair With Given Difference ===\n")

    test_cases = [
        ([5, 10, 3, 2, 50, 80], 78, 1),
        ([-10, 20], 30, 1),
        ([1, 2, 3], 0, 0),
        ([5, 5, 3], 0, 1),
        ([1], 5, 0),
        ([1, 5, 4, 2], 3, 1),
        ([1, 2, 3, 4, 5], -1, 1),  # A[i]-A[j] = -1
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
