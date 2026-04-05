"""
Problem: Maximum Consecutive Gap
Difficulty: HARD | XP: 50
Source: InterviewBit

Given an unsorted array, find the maximum difference between successive
elements in its sorted form. Must run in linear time and space (O(n)).

Pigeonhole Principle: With n elements in range [min, max], the maximum gap
is >= ceil((max-min)/(n-1)), so elements in the same bucket of that width
cannot form the max gap. Only cross-bucket gaps matter.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(n)
# Sort using insertion sort, then scan adjacent differences.
# ============================================================
def brute_force(A: List[int]) -> int:
    if len(A) < 2:
        return 0
    arr = A[:]
    # Insertion sort
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key
    return max(arr[i] - arr[i - 1] for i in range(1, len(arr)))


# ============================================================
# APPROACH 2: OPTIMAL — Library sort then scan
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(A: List[int]) -> int:
    if len(A) < 2:
        return 0
    arr = sorted(A)
    return max(arr[i] - arr[i - 1] for i in range(1, len(arr)))


# ============================================================
# APPROACH 3: BEST — Bucket Sort (Pigeonhole Principle)
# Time: O(n)  |  Space: O(n)
# Distribute elements into buckets of size ceil((max-min)/(n-1)).
# Max gap can only occur between consecutive non-empty buckets.
# Track only min/max per bucket.
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    if n < 2:
        return 0

    min_val, max_val = min(A), max(A)
    if min_val == max_val:
        return 0

    bucket_size = max(1, (max_val - min_val) // (n - 1))
    bucket_count = (max_val - min_val) // bucket_size + 1

    bucket_min = [float('inf')] * bucket_count
    bucket_max = [float('-inf')] * bucket_count
    occupied = [False] * bucket_count

    for x in A:
        idx = (x - min_val) // bucket_size
        occupied[idx] = True
        bucket_min[idx] = min(bucket_min[idx], x)
        bucket_max[idx] = max(bucket_max[idx], x)

    max_gap = 0
    prev_max = min_val
    for i in range(bucket_count):
        if not occupied[i]:
            continue
        max_gap = max(max_gap, bucket_min[i] - prev_max)
        prev_max = bucket_max[i]

    return max_gap


if __name__ == "__main__":
    print("=== Maximum Consecutive Gap ===")
    tests = [
        [1, 10, 5],           # sorted: 1,5,10 -> max gap = 5
        [3, 6, 9, 1],         # sorted: 1,3,6,9 -> max gap = 3
        [1, 2],               # gap = 1
        [1, 1000000],         # gap = 999999
        [9, 3, 1, 7, 2, 6],   # sorted: 1,2,3,6,7,9 -> max gap = 3
    ]
    for t in tests:
        bf = brute_force(t[:])
        op = optimal(t[:])
        be = best(t[:])
        print(f"A={t} -> Brute={bf}, Optimal={op}, Best={be}")
