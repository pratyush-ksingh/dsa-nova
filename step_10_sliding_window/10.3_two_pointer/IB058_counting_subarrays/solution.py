"""
Problem: Counting Subarrays (InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array A of N non-negative integers and an integer B,
count the number of subarrays having sum strictly less than B.

Note: All elements are non-negative (enables sliding window).

Example:
  A = [2, 5, 6], B = 10
  Subarrays: [2]=2<10, [5]=5<10, [6]=6<10, [2,5]=7<10, [5,6]=11 no, [2,5,6]=13 no
  Count = 4
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
#
# Check all possible subarrays. For each pair (i, j),
# compute the subarray sum and check if it's < B.
# Since elements are non-negative, we can break early when sum >= B.
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """Check all subarrays O(n^2)."""
    n = len(A)
    count = 0
    for i in range(n):
        curr_sum = 0
        for j in range(i, n):
            curr_sum += A[j]
            if curr_sum < B:
                count += 1
            else:
                break  # non-negative elements: sum won't decrease
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- Sliding Window (Two Pointers)
# Time: O(n)  |  Space: O(1)
#
# Use two pointers left and right. For each right pointer,
# expand the window. If sum >= B, shrink from the left.
# For each valid right, all subarrays ending at right with
# start in [left..right] are valid. Add (right - left + 1).
#
# Works because all elements are non-negative: adding an element
# never decreases the sum, so once sum >= B we must shrink left.
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """Sliding window: for each right, find the leftmost valid left."""
    n = len(A)
    count = 0
    left = 0
    curr_sum = 0

    for right in range(n):
        curr_sum += A[right]
        # Shrink window from left while sum >= B
        while left <= right and curr_sum >= B:
            curr_sum -= A[left]
            left += 1
        # All subarrays ending at 'right' starting from 'left' to 'right' are valid
        count += (right - left + 1)

    return count


# ============================================================
# APPROACH 3: BEST -- Same Two Pointer (Already Optimal)
# Time: O(n)  |  Space: O(1)
#
# The two-pointer approach is already O(n). This variant makes the
# logic even more explicit by computing the count contribution per
# right pointer: each position right with valid left contributes
# exactly (right - left + 1) subarrays that start in [left, right].
# We also handle edge cases (B <= 0) explicitly.
# ============================================================
def best(A: List[int], B: int) -> int:
    """Two-pointer with explicit edge case handling."""
    if B <= 0:
        return 0  # No subarray sum can be < 0 with non-negative elements

    n = len(A)
    count = 0
    left = 0
    window_sum = 0

    for right in range(n):
        window_sum += A[right]

        # Move left pointer to maintain sum < B
        while window_sum >= B:
            window_sum -= A[left]
            left += 1

        # Valid subarrays ending at 'right': start can be left, left+1, ..., right
        # That's (right - left + 1) subarrays
        count += right - left + 1

    return count


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Counting Subarrays (Sum < B) ===\n")

    tests = [
        ([2, 5, 6],    10, 4),
        ([1, 2, 3],     5, 4),   # [1],[2],[3],[1,2] -> 4
        ([1, 11, 2],    5, 2),   # [1],[2] -> 2
        ([1, 2, 3, 4],  5, 6),   # [1],[2],[3],[4],[1,2],[2,3] -> 6? Let me verify
        ([0, 0, 0],     1, 6),   # all subarrays sum=0 < 1 -> 6 total
        ([5, 6, 7],     5, 0),   # nothing < 5
        ([1],           2, 1),
    ]

    # Verify [1,2,3,4] B=5: [1]=1<5, [2]=2<5, [3]=3<5, [4]=4<5, [1,2]=3<5, [2,3]=5 no, [3,4]=7 no
    # [1,2,3]=6 no, [2,3,4]=9 no, [1,2,3,4]=10 no -> count=5 not 6
    tests[3] = ([1, 2, 3, 4], 5, 5)

    for A, B, expected in tests:
        b = brute_force(A[:], B)
        o = optimal(A[:], B)
        r = best(A[:], B)
        status = "PASS" if b == o == r == expected else "FAIL"
        print(f"A={A}, B={B}  Expected={expected}  "
              f"Brute={b}  Optimal={o}  Best={r}  [{status}]")
