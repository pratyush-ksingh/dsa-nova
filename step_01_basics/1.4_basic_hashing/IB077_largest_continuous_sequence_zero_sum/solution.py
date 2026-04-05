"""
Problem: Largest Continuous Sequence Zero Sum
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Find the longest subarray whose elements sum to 0.
Return the subarray itself (empty list if none found).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> List[int]:
    """
    Try every subarray, compute its sum, track the longest zero-sum one.
    Real-life: Auditing financial transactions to find periods that cancel out.
    """
    n = len(A)
    max_len = 0
    start = -1
    for i in range(n):
        total = 0
        for j in range(i, n):
            total += A[j]
            if total == 0 and (j - i + 1) > max_len:
                max_len = j - i + 1
                start = i
    if start == -1:
        return []
    return A[start:start + max_len]


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(A: List[int]) -> List[int]:
    """
    Prefix sum + HashMap: if prefix[i] == prefix[j], subarray (i+1..j) sums to 0.
    Store the first occurrence index of each prefix sum.
    Real-life: Balance sheet analysis — finding date ranges with net-zero cash flow.
    """
    first_seen = {0: -1}  # prefix_sum -> first index
    prefix = 0
    max_len = 0
    start = -1
    for i, val in enumerate(A):
        prefix += val
        if prefix in first_seen:
            length = i - first_seen[prefix]
            if length > max_len:
                max_len = length
                start = first_seen[prefix] + 1
        else:
            first_seen[prefix] = i
    if start == -1:
        return []
    return A[start:start + max_len]


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(A: List[int]) -> List[int]:
    """
    Same O(n) prefix-sum approach — already optimal. Clean implementation
    that also correctly handles all-zero subarrays and multiple candidates.
    Real-life: Signal processing — finding longest balanced positive/negative interval.
    """
    first_seen = {0: -1}
    prefix = 0
    best_start = -1
    best_end = -1
    best_len = 0
    for i, val in enumerate(A):
        prefix += val
        if prefix in first_seen:
            length = i - first_seen[prefix]
            if length > best_len:
                best_len = length
                best_start = first_seen[prefix] + 1
                best_end = i
        else:
            first_seen[prefix] = i
    if best_start == -1:
        return []
    return A[best_start:best_end + 1]


if __name__ == "__main__":
    print("=== Largest Continuous Sequence Zero Sum ===")
    tests = [
        ([1, 2, -2, 4, -4],      "[2,-2,4,-4]"),
        ([1, 0, -1],              "[1,0,-1]"),
        ([0],                     "[0]"),
        ([1, 2, 3],               "[]"),
        ([3, -3, 1, -1, 2, -2],  "[3,-3,1,-1,2,-2]"),
        ([0, 0, 0],               "[0,0,0]"),
    ]
    for arr, exp in tests:
        print(f"\nInput:    {arr}")
        print(f"Expected: {exp}")
        print(f"Brute:    {brute_force(arr[:])}")
        print(f"Optimal:  {optimal(arr[:])}")
        print(f"Best:     {best(arr[:])}")
