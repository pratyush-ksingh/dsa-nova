"""
Problem: Pick from Both Sides (InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an integer array A and integer B, pick exactly B elements from either
the left end or the right end of the array to maximize their sum.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Try all left/right splits)
# Time: O(B^2) | Space: O(1)
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """For each possible split (i from left, B-i from right), compute the sum."""
    n = len(A)
    max_sum = float('-inf')

    for left_count in range(B + 1):
        right_count = B - left_count
        # Sum first left_count elements + last right_count elements
        current_sum = sum(A[:left_count]) + sum(A[n - right_count:] if right_count > 0 else [])
        max_sum = max(max_sum, current_sum)

    return max_sum


# ============================================================
# APPROACH 2: OPTIMAL (Prefix sum + sliding window)
# Time: O(B) | Space: O(1)
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """
    Start by summing the first B elements (all from left).
    Then slide the window: remove one from the left end, add one from the right end.
    Track the maximum at each step.
    """
    n = len(A)

    # Initial window: all B elements from the left
    window_sum = sum(A[:B])
    max_sum = window_sum

    # Slide: remove A[B-1-i] from left window, add A[n-1-i] from right
    for i in range(1, B + 1):
        window_sum = window_sum - A[B - i] + A[n - i]
        max_sum = max(max_sum, window_sum)

    return max_sum


# ============================================================
# APPROACH 3: BEST (Same sliding window, cleaner form)
# Time: O(B) | Space: O(1)
# ============================================================
def best(A: List[int], B: int) -> int:
    """
    Build prefix sum of first B elements. Then iteratively replace the
    leftmost picked element with a new element from the right side.
    This is the same O(B) sliding window, written most cleanly.
    """
    n = len(A)

    # Start: take all B from left
    curr = sum(A[:B])
    result = curr

    # Swap one left element for one right element, B times
    for i in range(1, B + 1):
        # Remove A[B - i] (the rightmost of current left picks)
        # Add A[n - i] (the i-th element from the right)
        curr += A[n - i] - A[B - i]
        result = max(result, curr)

    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Pick from Both Sides ===\n")

    test_cases = [
        ([5, -2, 3, 1, 2], 3, 8),          # pick 5,-2,3 left? or 5 + 1+2=8 (1 left, 2 right)
        ([1, 2], 1, 2),                     # pick max(1, 2) = 2
        ([-1, -2, -3, -4, -5], 2, -3),     # best two are -1 and -2
        ([1, 2, 3, 4, 5], 2, 9),           # pick 5+4 from right = 9
        ([10, 1, 1, 1, 1, 1, 10], 2, 20),  # pick both 10s (left+right)
    ]

    # Recalculate expected for first case: all 3 splits:
    # left=0,right=3: 1+2+3=6  wait A=[5,-2,3,1,2], n=5
    # left=0,right=3: A[2]+A[3]+A[4]=3+1+2=6 wrong direction
    # right picks from end: left=0,right=3 -> A[-3:]=[3,1,2]=6
    # left=1,right=2: A[0]+A[-2]+A[-1]=5+1+2=8  YES
    # left=2,right=1: A[0]+A[1]+A[-1]=5-2+2=5
    # left=3,right=0: 5-2+3=6

    for A, B, expected in test_cases:
        b = brute_force(A[:], B)
        o = optimal(A[:], B)
        n = best(A[:], B)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: A={A}, B={B}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
