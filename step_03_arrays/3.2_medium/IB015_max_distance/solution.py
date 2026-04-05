"""Problem: Max Distance
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given array A, find max value of (j - i) such that j > i and A[j] >= A[i].
Return 0 if no such pair exists.
Real-life use: maximum profit window where prices don't fall below entry.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check all (i,j) pairs — O(n^2) but simple
# ============================================================
def brute_force(A: List[int]) -> int:
    n = len(A)
    max_dist = 0
    for i in range(n):
        for j in range(i + 1, n):
            if A[j] >= A[i]:
                max_dist = max(max_dist, j - i)
    return max_dist


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# 1. Build left_min[i] = min(A[0..i])
# 2. Build right_max[j] = max(A[j..n-1])
# 3. Two-pointer: advance j if left_min[i] <= right_max[j]
# ============================================================
def optimal(A: List[int]) -> int:
    n = len(A)
    left_min = [0] * n
    right_max = [0] * n

    left_min[0] = A[0]
    for i in range(1, n):
        left_min[i] = min(left_min[i - 1], A[i])

    right_max[n - 1] = A[n - 1]
    for j in range(n - 2, -1, -1):
        right_max[j] = max(right_max[j + 1], A[j])

    i = j = 0
    max_dist = 0
    while i < n and j < n:
        if left_min[i] <= right_max[j]:
            max_dist = max(max_dist, j - i)
            j += 1
        else:
            i += 1
    return max_dist


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# Same two-pointer logic; slightly different loop structure
# to make intention clearer.
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    left_min = A[:]
    for i in range(1, n):
        left_min[i] = min(left_min[i - 1], A[i])

    right_max = A[:]
    for j in range(n - 2, -1, -1):
        right_max[j] = max(right_max[j + 1], A[j])

    lo = hi = ans = 0
    while hi < n:
        if left_min[lo] <= right_max[hi]:
            ans = max(ans, hi - lo)
            hi += 1
        else:
            lo += 1
    return ans


if __name__ == "__main__":
    cases = [
        ([3, 5, 4, 2], 2),
        ([1, 2, 3, 4, 5], 4),
        ([5, 4, 3, 2, 1], 0),
        ([34, 8, 10, 3, 2, 80, 30, 33, 1], 6),
        ([1], 0),
    ]
    print("=== Max Distance ===")
    for A, exp in cases:
        b = brute_force(A[:])
        o = optimal(A[:])
        bst = best(A[:])
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"A={A} => {ok}  EXP={exp}")
