"""
Problem: Subarray with B Odd Numbers
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Count the number of subarrays of array A that contain exactly B odd numbers.

Key insight: exactly(B) = atMost(B) - atMost(B-1)
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check every subarray by counting odd numbers inside it.
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    n = len(A)
    count = 0
    for i in range(n):
        odds = 0
        for j in range(i, n):
            if A[j] % 2 != 0:
                odds += 1
            if odds == B:
                count += 1
            elif odds > B:
                break
    return count


# ============================================================
# APPROACH 2: OPTIMAL — Prefix Count + HashMap
# Time: O(n)  |  Space: O(n)
# Track prefix count of odd numbers. For index j, count of
# subarrays ending at j with exactly B odds equals
# freq[prefixOdds[j] - B].
# ============================================================
def optimal(A: List[int], B: int) -> int:
    count = 0
    odd_count = 0
    freq = defaultdict(int)
    freq[0] = 1  # empty prefix

    for x in A:
        if x % 2 != 0:
            odd_count += 1
        count += freq[odd_count - B]
        freq[odd_count] += 1

    return count


# ============================================================
# APPROACH 3: BEST — Sliding Window: atMost(B) - atMost(B-1)
# Time: O(n)  |  Space: O(1)
# atMost(k) counts subarrays with at most k odd numbers.
# exactly(B) = atMost(B) - atMost(B-1).
# Constant extra space, two clean passes.
# ============================================================
def _at_most(A: List[int], k: int) -> int:
    if k < 0:
        return 0
    count = 0
    left = 0
    odds = 0
    for right in range(len(A)):
        if A[right] % 2 != 0:
            odds += 1
        while odds > k:
            if A[left] % 2 != 0:
                odds -= 1
            left += 1
        count += right - left + 1
    return count


def best(A: List[int], B: int) -> int:
    return _at_most(A, B) - _at_most(A, B - 1)


if __name__ == "__main__":
    print("=== Subarray with B Odd Numbers ===")
    tests = [
        ([1, 2, 3, 4, 5], 2),
        ([2, 2, 2, 2],    1),
        ([1, 1, 1],        2),
        ([1, 2, 1, 2],     1),
    ]
    for A, B in tests:
        bf = brute_force(A[:], B)
        op = optimal(A[:], B)
        be = best(A[:], B)
        print(f"A={A}, B={B} -> Brute={bf}, Optimal={op}, Best={be}")
