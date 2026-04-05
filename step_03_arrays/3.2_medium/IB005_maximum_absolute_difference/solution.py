"""
Problem: Maximum Absolute Difference
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array A of N integers, find:
    max over all i,j of |A[i] - A[j]| + |i - j|

Key mathematical insight:
  |A[i]-A[j]| + |i-j|
  = max of the 4 cases (by expanding absolute values):
    (A[i]+i) - (A[j]+j)
    (A[j]+j) - (A[i]+i)
    (A[i]-i) - (A[j]-j)
    (A[j]-j) - (A[i]-i)

  So the answer = max(
    max(A[i]+i) - min(A[j]+j),
    max(A[i]-i) - min(A[j]-i)
  )
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> int:
    """
    Try all pairs (i, j) and compute |A[i]-A[j]| + |i-j|, tracking the max.
    """
    n = len(A)
    ans = 0
    for i in range(n):
        for j in range(n):
            val = abs(A[i] - A[j]) + abs(i - j)
            ans = max(ans, val)
    return ans


# ============================================================
# APPROACH 2: OPTIMAL — Track 4 extremes in one pass
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(A: List[int]) -> int:
    """
    |A[i]-A[j]| + |i-j| is maximized by one of 4 expressions.
    Two sets of (value+index) and (value-index):
      Case 1: (A[i]+i) - (A[j]+j)  => maximize A[i]+i, minimize A[j]+j
      Case 2: (A[i]-i) - (A[j]-j)  => maximize A[i]-i, minimize A[j]-j

    Track max and min of (A[i]+i) and (A[i]-i) in a single pass.
    """
    max_plus  = float('-inf')  # max of A[i]+i
    min_plus  = float('inf')   # min of A[i]+i
    max_minus = float('-inf')  # max of A[i]-i
    min_minus = float('inf')   # min of A[i]-i

    for i, x in enumerate(A):
        max_plus  = max(max_plus,  x + i)
        min_plus  = min(min_plus,  x + i)
        max_minus = max(max_minus, x - i)
        min_minus = min(min_minus, x - i)

    return max(max_plus - min_plus, max_minus - min_minus)


# ============================================================
# APPROACH 3: BEST — Same O(n), expressed more compactly
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(A: List[int]) -> int:
    """
    Equivalent to optimal. The expression |A[i]-A[j]| + |i-j| equals
    max of (A+i) range and (A-i) range.
    Written as a single compact pass for clarity.
    """
    n = len(A)
    # (value + index) and (value - index) extremes
    max1 = max(A[i] + i for i in range(n))
    min1 = min(A[i] + i for i in range(n))
    max2 = max(A[i] - i for i in range(n))
    min2 = min(A[i] - i for i in range(n))
    return max(max1 - min1, max2 - min2)


if __name__ == "__main__":
    print("=== Maximum Absolute Difference ===")
    tests = [
        ([1, 3, -1], 5),    # |(-1)-(1)| + |2-0| = 2+2=4? or |3-(-1)| + |1-2|=4+1=5
        ([55, -8, 43, 52, 8, 59, -91, -79, -18, 27], None),
        ([1, 2, 3, 4, 5], None),
    ]
    for A, expected in tests:
        b = brute_force(A)
        o = optimal(A)
        be = best(A)
        print(f"A={A}: Brute={b}, Optimal={o}, Best={be}"
              + (f" (expected={expected})" if expected else ""))
