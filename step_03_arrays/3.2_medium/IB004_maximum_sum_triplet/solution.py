"""
Problem: Maximum Sum Triplet
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array A of N integers, find max A[i] + A[j] + A[k] such that
i < j < k and A[i] < A[j] < A[k]. Return 0 if no such triplet exists.
"""
from typing import List
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^3)  |  Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> int:
    """
    Try all triplets (i, j, k) with i < j < k. Check the strictly increasing
    condition and track the maximum sum.
    """
    n = len(A)
    ans = 0
    for i in range(n - 2):
        for j in range(i + 1, n - 1):
            if A[j] <= A[i]:
                continue
            for k in range(j + 1, n):
                if A[k] > A[j]:
                    ans = max(ans, A[i] + A[j] + A[k])
    return ans


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(A: List[int]) -> int:
    """
    Precompute suffix_max[j] = max value in A[j+1..n-1].
    For each j (middle element), we need the largest A[i] < A[j] from the left
    and the largest A[k] > A[j] from the right.

    Use a sorted list (via bisect) to track elements seen so far on the left.
    For the right, use a precomputed suffix maximum array.
    But suffix max might equal A[j], so we need suffix max strictly greater.

    Better: for each j, find the largest element strictly less than A[j] from
    the left using a sorted structure (binary search), and find the largest
    element strictly greater than A[j] from the right using suffix max.

    Note: suffix_max just stores the overall max to the right; if it's > A[j],
    that's our k value. Together with the best i from the left sorted list.
    """
    n = len(A)
    if n < 3:
        return 0

    # suffix_max[i] = max of A[i+1 .. n-1]
    suffix_max = [0] * n
    suffix_max[n - 1] = 0
    for i in range(n - 2, -1, -1):
        suffix_max[i] = max(A[i + 1], suffix_max[i + 1])

    sorted_left = []  # sorted list of elements to the left of j
    ans = 0

    for j in range(1, n - 1):
        # Best k: suffix_max[j] if it is strictly > A[j]
        best_k = suffix_max[j] if suffix_max[j] > A[j] else 0

        if best_k > 0:
            # Best i: largest value in sorted_left strictly < A[j]
            idx = bisect.bisect_left(sorted_left, A[j]) - 1
            if idx >= 0:
                best_i = sorted_left[idx]
                ans = max(ans, best_i + A[j] + best_k)

        bisect.insort(sorted_left, A[j - 1])

    return ans


# ============================================================
# APPROACH 3: BEST — Same as optimal, slight structural cleanup
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def best(A: List[int]) -> int:
    """
    Identical to optimal. For each candidate middle element j:
    1. Look left: largest element strictly less than A[j] (sorted list + bisect).
    2. Look right: max element strictly greater than A[j] (suffix max array).
    Combine to get candidate sum.
    """
    n = len(A)
    if n < 3:
        return 0

    # Build suffix max (max of A[j+1..n-1] for each j)
    suffix_max = [0] * n
    for i in range(n - 2, -1, -1):
        suffix_max[i] = max(A[i + 1], suffix_max[i + 1])

    left_sorted = []
    ans = 0

    for j in range(n):
        right_max = suffix_max[j]
        if right_max > A[j]:
            # Find largest element < A[j] in left_sorted
            pos = bisect.bisect_left(left_sorted, A[j]) - 1
            if pos >= 0:
                ans = max(ans, left_sorted[pos] + A[j] + right_max)
        bisect.insort(left_sorted, A[j])

    return ans


if __name__ == "__main__":
    print("=== Maximum Sum Triplet ===")
    tests = [
        ([2, 5, 3, 1, 4, 9], 16),      # 2+5+9=16 or 3+4+9=16 (need strict i<j<k and A[i]<A[j]<A[k])
        ([1, 2, 3, 4, 5], 12),
        ([5, 4, 3, 2, 1], 0),
        ([1, 5, 3, 6, 7], 18),          # 1+5+... or 3+6+7=16 or 1+6+7=14 or 5+6+7=18
    ]
    for A, expected in tests:
        b = brute_force(A)
        o = optimal(A)
        be = best(A)
        print(f"A={A}: Brute={b}, Optimal={o}, Best={be} (expected={expected})")
