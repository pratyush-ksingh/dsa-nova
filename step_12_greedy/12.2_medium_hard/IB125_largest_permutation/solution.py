"""
Problem: Largest Permutation
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a permutation A of first N natural numbers and integer B (max swaps),
find the lexicographically largest permutation achievable in at most B swaps.

Greedy: scan left to right; at position i, bring the largest unused value
to position i using one swap (tracked by a position map for O(1) lookup).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Greedy with O(N) scan per position
# Time: O(N * B)  |  Space: O(N)
# At each position, do a linear scan to find the max element; swap it in
# ============================================================
def brute_force(A: List[int], B: int) -> List[int]:
    arr = list(A)
    n = len(arr)
    for i in range(n):
        if B == 0:
            break
        # Find maximum in arr[i:]
        max_val = max(arr[i:])
        max_idx = arr.index(max_val, i)
        if max_idx != i:
            arr[i], arr[max_idx] = arr[max_idx], arr[i]
            B -= 1
    return arr


# ============================================================
# APPROACH 2: OPTIMAL - Greedy + position map for O(1) swaps
# Time: O(N)  |  Space: O(N)
# pos[v] = current index of value v in arr.
# For each i, the desired value is n-i (1-indexed largest remaining).
# If it's not there, swap using O(1) position lookup and update map.
# ============================================================
def optimal(A: List[int], B: int) -> List[int]:
    arr = list(A)
    n = len(arr)
    pos = [0] * (n + 1)
    for i, v in enumerate(arr):
        pos[v] = i

    for i in range(n):
        if B == 0:
            break
        want = n - i  # largest value not yet settled (1-indexed)
        if arr[i] == want:
            continue
        j = pos[want]
        pos[arr[i]] = j   # old value at i moves to j
        pos[want] = i     # want moves to i
        arr[i], arr[j] = arr[j], arr[i]
        B -= 1

    return arr


# ============================================================
# APPROACH 3: BEST - Same O(N) greedy with early-exit optimization
# Time: O(N)  |  Space: O(N)
# If B >= N, no need to simulate: return sorted descending directly.
# ============================================================
def best(A: List[int], B: int) -> List[int]:
    n = len(A)
    if B >= n:
        return list(range(n, 0, -1))  # [n, n-1, ..., 1]
    return optimal(A, B)


if __name__ == "__main__":
    print("=== Largest Permutation ===")

    A, B = [1, 2, 3, 4], 1
    print(f"A={A}, B={B}")
    print(f"Brute:   {brute_force(A[:], B)}")   # [4, 2, 3, 1]
    print(f"Optimal: {optimal(A[:], B)}")
    print(f"Best:    {best(A[:], B)}")

    A, B = [3, 1, 2], 2
    print(f"\nA={A}, B={B}")
    print(f"Brute:   {brute_force(A[:], B)}")   # [3, 2, 1]
    print(f"Optimal: {optimal(A[:], B)}")
    print(f"Best:    {best(A[:], B)}")

    A, B = [2, 1, 3], 1
    print(f"\nA={A}, B={B}")
    print(f"Brute:   {brute_force(A[:], B)}")   # [3, 1, 2]
    print(f"Optimal: {optimal(A[:], B)}")
    print(f"Best:    {best(A[:], B)}")
