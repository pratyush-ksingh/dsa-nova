"""
Problem: Union of Two Sorted Arrays
Difficulty: MEDIUM | XP: 25

Given two sorted arrays A and B, return their union as a sorted list
without duplicates.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O((m+n) log(m+n))  |  Space: O(m+n)
# Merge into a set, sort the result.
# ============================================================
def brute_force(A: List[int], B: List[int]) -> List[int]:
    return sorted(set(A) | set(B))


# ============================================================
# APPROACH 2: OPTIMAL — Two Pointer Merge
# Time: O(m + n)  |  Space: O(m + n)
# Simultaneously advance through both sorted arrays,
# always picking the smaller value and skipping duplicates.
# ============================================================
def optimal(A: List[int], B: List[int]) -> List[int]:
    result = []
    i, j = 0, 0

    while i < len(A) and j < len(B):
        a, b = A[i], B[j]
        if a < b:
            if not result or result[-1] != a:
                result.append(a)
            i += 1
        elif b < a:
            if not result or result[-1] != b:
                result.append(b)
            j += 1
        else:
            if not result or result[-1] != a:
                result.append(a)
            i += 1
            j += 1

    while i < len(A):
        if not result or result[-1] != A[i]:
            result.append(A[i])
        i += 1

    while j < len(B):
        if not result or result[-1] != B[j]:
            result.append(B[j])
        j += 1

    return result


# ============================================================
# APPROACH 3: BEST — Clean Two Pointer with unified dedup
# Time: O(m + n)  |  Space: O(m + n)
# Uses a single last-value check to suppress duplicates from
# either array naturally during the merge.
# ============================================================
def best(A: List[int], B: List[int]) -> List[int]:
    result = []
    i, j = 0, 0

    while i < len(A) and j < len(B):
        if A[i] <= B[j]:
            val = A[i]
            i += 1
            if j < len(B) and B[j] == val:
                j += 1
        else:
            val = B[j]
            j += 1

        if not result or result[-1] != val:
            result.append(val)

    while i < len(A):
        if not result or result[-1] != A[i]:
            result.append(A[i])
        i += 1

    while j < len(B):
        if not result or result[-1] != B[j]:
            result.append(B[j])
        j += 1

    return result


if __name__ == "__main__":
    print("=== Union of Two Sorted Arrays ===")
    tests = [
        ([1, 2, 3, 4, 5], [1, 2, 3]),
        ([1, 1, 2, 3],     [2, 3, 4, 4]),
        ([1],              [2]),
        ([1, 2, 4, 5, 6],  [2, 3, 4, 7]),
    ]
    for A, B in tests:
        bf = brute_force(A[:], B[:])
        op = optimal(A[:], B[:])
        be = best(A[:], B[:])
        print(f"A={A}, B={B}")
        print(f"  Brute:   {bf}")
        print(f"  Optimal: {op}")
        print(f"  Best:    {be}")
