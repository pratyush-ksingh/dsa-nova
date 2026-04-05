"""
Problem: Maximum Unsorted Subarray
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array A, find the shortest subarray [start, end] (0-indexed) such that
if you sort only A[start..end], the entire array becomes sorted.

Return [-1] if the array is already sorted.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n³)  |  Space: O(n)
# Try every (start, end) pair; check if sorting that segment sorts the whole array.
# ============================================================
def brute_force(A: List[int]) -> List[int]:
    """
    For every possible subarray [i..j], check:
    1. Sort A[i..j].
    2. Check if the resulting array equals sorted(A).
    Return the shortest valid [i, j]. Return [-1] if already sorted.
    """
    n = len(A)
    sorted_A = sorted(A)
    if A == sorted_A:
        return [-1]

    best_len = n + 1
    best_pair = [-1]

    for i in range(n):
        for j in range(i + 1, n):
            # Sort the subarray and check
            candidate = A[:i] + sorted(A[i:j + 1]) + A[j + 1:]
            if candidate == sorted_A:
                if (j - i + 1) < best_len:
                    best_len = j - i + 1
                    best_pair = [i, j]

    return best_pair


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# Two-pass approach:
#   Pass 1: find initial boundaries (first and last out-of-order elements).
#   Pass 2: expand boundaries to include any element outside [min, max] of subarray.
# ============================================================
def optimal(A: List[int]) -> List[int]:
    """
    Step 1: Find initial left boundary = first index where A[i] > A[i+1].
            Find initial right boundary = last index where A[j] < A[j-1].
    Step 2: Compute min and max of A[left..right].
    Step 3: Expand left leftward while A[left-1] > subarray_min.
            Expand right rightward while A[right+1] < subarray_max.
    """
    n = len(A)

    # Step 1: find raw boundaries
    left = -1
    for i in range(n - 1):
        if A[i] > A[i + 1]:
            left = i
            break

    if left == -1:
        return [-1]  # already sorted

    right = -1
    for j in range(n - 1, 0, -1):
        if A[j] < A[j - 1]:
            right = j
            break

    # Step 2: find min and max within [left..right]
    sub_min = min(A[left:right + 1])
    sub_max = max(A[left:right + 1])

    # Step 3: expand left
    while left > 0 and A[left - 1] > sub_min:
        left -= 1

    # Step 4: expand right
    while right < n - 1 and A[right + 1] < sub_max:
        right += 1

    return [left, right]


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# Same O(n) logic but done in a single conceptual pass:
# find left/right by comparing with running max/min from each end.
# ============================================================
def best(A: List[int]) -> List[int]:
    """
    Find right boundary by scanning left-to-right tracking the running max:
    the last position where A[i] < running_max is the right boundary.

    Find left boundary by scanning right-to-left tracking the running min:
    the last position where A[i] > running_min is the left boundary.

    Then expand for min/max as needed (same as Optimal step 3-4).
    Actually this combined scan already gives the final boundaries directly
    without needing a separate expand step.
    """
    n = len(A)

    # Find right: last index where element is less than max seen so far (from left)
    running_max = A[0]
    right = -1
    for i in range(1, n):
        if A[i] < running_max:
            right = i
        else:
            running_max = A[i]

    if right == -1:
        return [-1]  # already sorted

    # Find left: last index where element is greater than min seen so far (from right)
    running_min = A[n - 1]
    left = -1
    for i in range(n - 2, -1, -1):
        if A[i] > running_min:
            left = i
        else:
            running_min = A[i]

    return [left, right]


if __name__ == "__main__":
    print("=== Maximum Unsorted Subarray ===")
    test_cases = [
        ([1, 3, 2, 4, 5],        [1, 2]),
        ([1, 2, 3, 4, 5],        [-1]),
        ([5, 4, 3, 2, 1],        [0, 4]),
        ([2, 6, 4, 8, 10, 9, 15], [1, 5]),
        ([1],                     [-1]),
        ([1, 2],                  [-1]),
        ([2, 1],                  [0, 1]),
        ([1, 3, 5, 2, 6, 4, 8],  [1, 5]),
    ]
    for A, expected in test_cases:
        b  = brute_force(A[:])
        o  = optimal(A[:])
        be = best(A[:])
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"A={A} | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
