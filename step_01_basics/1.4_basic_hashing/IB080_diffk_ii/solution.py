"""
Problem: Diffk II
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a sorted array A and integer k, determine if there exist two
indices i != j such that A[i] - A[j] = k. Return 1 if yes, 0 otherwise.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Check every pair (i, j) where i != j.
# ============================================================
def brute_force(arr: List[int], k: int) -> int:
    n = len(arr)
    for i in range(n):
        for j in range(n):
            if i != j and arr[i] - arr[j] == k:
                return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL -- HashSet Lookup
# Time: O(N)  |  Space: O(N)
# For each element x, check if (x - k) exists in the set.
# A[i] - A[j] = k means A[j] = A[i] - k.
# ============================================================
def optimal(arr: List[int], k: int) -> int:
    seen = set()
    for x in arr:
        # Check if x - k or x + k is already in set
        # A[i] - A[j] = k => need A[j] = x - k (if x is A[i])
        # or A[i] = x + k (if x is A[j])
        if (x - k) in seen or (x + k) in seen:
            return 1
        seen.add(x)
    return 0


# ============================================================
# APPROACH 3: BEST -- Two Pointer (Leveraging Sorted Array)
# Time: O(N)  |  Space: O(1)
# Since array is sorted, use two pointers i and j.
# If A[i] - A[j] == k, found. If < k, advance i. If > k, advance j.
# ============================================================
def best(arr: List[int], k: int) -> int:
    n = len(arr)
    i, j = 0, 0
    while i < n and j < n:
        diff = arr[i] - arr[j]
        if diff == k and i != j:
            return 1
        elif diff < k:
            i += 1
        else:
            j += 1
            # Ensure j doesn't pass i causing issues
            if j == i:
                j += 1
    return 0


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3, 4, 5], 2),
        ([1, 3, 5], 4),
        ([1, 1], 0),
        ([1, 2, 3], 0),
        ([1, 5, 10], 5),
    ]
    print("=== Diffk II ===")
    for arr, k in test_cases:
        print(f"  arr={arr}, k={k}: brute={brute_force(arr, k)}, "
              f"optimal={optimal(arr, k)}, best={best(arr, k)}")
