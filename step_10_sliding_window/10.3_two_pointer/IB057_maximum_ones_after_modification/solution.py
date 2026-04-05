"""
Problem: Maximum Ones After Modification (InterviewBit)
Difficulty: MEDIUM | XP: 25

Given a binary array A and an integer B, find the maximum number of consecutive
1s you can obtain if you can flip at most B zeros to ones.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Try Every Subarray)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """For every starting index, expand right counting zeros. Track max length."""
    n = len(A)
    max_len = 0

    for i in range(n):
        zeros = 0
        for j in range(i, n):
            if A[j] == 0:
                zeros += 1
            if zeros > B:
                break
            max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL (Sliding Window)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """Sliding window: expand right, shrink left when zero count exceeds B."""
    n = len(A)
    left = 0
    zeros = 0
    max_len = 0

    for right in range(n):
        if A[right] == 0:
            zeros += 1

        while zeros > B:
            if A[left] == 0:
                zeros -= 1
            left += 1

        max_len = max(max_len, right - left + 1)
    return max_len


# ============================================================
# APPROACH 3: BEST (Sliding Window -- Non-Shrinking)
# Time: O(n) | Space: O(1)
# ============================================================
def best(A: List[int], B: int) -> int:
    """
    Sliding window that never shrinks -- only grows or slides.
    When zeros > B, move left by 1 (not while-loop). The window size
    only increases, so the final window size is the answer.
    """
    n = len(A)
    left = 0
    zeros = 0

    for right in range(n):
        if A[right] == 0:
            zeros += 1

        if zeros > B:
            if A[left] == 0:
                zeros -= 1
            left += 1

    # The window size at the end equals the maximum we ever achieved
    return n - left


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Maximum Ones After Modification ===\n")

    test_cases = [
        ([1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1], 2, 8),
        ([1, 0, 0, 1, 0, 1, 0, 1, 1, 1], 2, 7),
        ([0, 0, 0, 0], 2, 2),
        ([1, 1, 1, 1], 0, 4),
        ([0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1], 3, 10),
        ([1], 1, 1),
        ([0], 0, 0),
    ]

    for A, B, expected in test_cases:
        b = brute_force(A[:], B)
        o = optimal(A[:], B)
        h = best(A[:], B)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: A={A}, B={B}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
