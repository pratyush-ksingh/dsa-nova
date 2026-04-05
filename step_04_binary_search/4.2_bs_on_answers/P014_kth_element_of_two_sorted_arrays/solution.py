"""
Problem: Kth Element of Two Sorted Arrays
Difficulty: HARD | XP: 50

Given two sorted arrays A and B, find the k-th smallest element in the
merged sorted array (1-indexed).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(m + n)  |  Space: O(m + n)
# Merge both arrays fully, return the k-th element.
# ============================================================
def brute_force(A: List[int], B: List[int], k: int) -> int:
    merged = []
    i, j = 0, 0
    while i < len(A) and j < len(B):
        if A[i] <= B[j]:
            merged.append(A[i]); i += 1
        else:
            merged.append(B[j]); j += 1
    merged.extend(A[i:])
    merged.extend(B[j:])
    return merged[k - 1]


# ============================================================
# APPROACH 2: OPTIMAL — Recursive Binary Elimination
# Time: O(log(m + n))  |  Space: O(log(m + n)) stack
# Compare k//2-th elements; eliminate the smaller half.
# ============================================================
def optimal(A: List[int], B: List[int], k: int) -> int:
    def kth(a_start: int, b_start: int, k: int) -> int:
        if a_start >= len(A):
            return B[b_start + k - 1]
        if b_start >= len(B):
            return A[a_start + k - 1]
        if k == 1:
            return min(A[a_start], B[b_start])

        half = k // 2
        a_val = A[a_start + half - 1] if a_start + half - 1 < len(A) else float('inf')
        b_val = B[b_start + half - 1] if b_start + half - 1 < len(B) else float('inf')

        if a_val < b_val:
            return kth(a_start + half, b_start, k - half)
        else:
            return kth(a_start, b_start + half, k - half)

    return kth(0, 0, k)


# ============================================================
# APPROACH 3: BEST — Binary Search on Partition
# Time: O(log(min(m, n)))  |  Space: O(1)
# Binary search on how many elements to take from the smaller
# array. Valid partition: A[i-1] <= B[j] and B[j-1] <= A[i].
# ============================================================
def best(A: List[int], B: List[int], k: int) -> int:
    if len(A) > len(B):
        A, B = B, A
    m, n = len(A), len(B)

    lo = max(0, k - n)
    hi = min(k, m)

    while lo <= hi:
        i = (lo + hi) // 2
        j = k - i

        a_left  = A[i - 1] if i > 0 else float('-inf')
        a_right = A[i]     if i < m else float('inf')
        b_left  = B[j - 1] if j > 0 else float('-inf')
        b_right = B[j]     if j < n else float('inf')

        if a_left <= b_right and b_left <= a_right:
            return max(a_left, b_left)
        elif a_left > b_right:
            hi = i - 1
        else:
            lo = i + 1

    return -1


if __name__ == "__main__":
    print("=== Kth Element of Two Sorted Arrays ===")
    tests = [
        ([2, 3, 6, 7, 9], [1, 4, 8, 10], 5, 6),
        ([1, 2, 3],        [4, 5, 6],     4, 4),
        ([1],              [2, 3],        2, 2),
        ([1, 3],           [2],           3, 3),
    ]
    for A, B, k, expected in tests:
        bf = brute_force(A, B, k)
        op = optimal(A, B, k)
        be = best(A, B, k)
        ok = all(x == expected for x in [bf, op, be])
        print(f"A={A}, B={B}, k={k} -> Brute={bf}, Optimal={op}, Best={be} | Expected={expected} {'OK' if ok else 'FAIL'}")
