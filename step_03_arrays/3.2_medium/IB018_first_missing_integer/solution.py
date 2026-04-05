"""Problem: First Missing Positive Integer
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Find the smallest missing positive integer in an unsorted array.
Must run in O(n) time and O(1) extra space.
Real-life use: finding the next available ID in a compact storage system.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n log n)  |  Space: O(1)
# Sort and scan for first missing positive
# ============================================================
def brute_force(A: List[int]) -> int:
    A.sort()
    missing = 1
    for v in A:
        if v == missing:
            missing += 1
        elif v > missing:
            break
    return missing


# ============================================================
# APPROACH 2: OPTIMAL (HashSet)
# Time: O(n)  |  Space: O(n)
# Store all positives in a set, probe 1,2,3...
# ============================================================
def optimal(A: List[int]) -> int:
    pos_set = set(v for v in A if v > 0)
    missing = 1
    while missing in pos_set:
        missing += 1
    return missing


# ============================================================
# APPROACH 3: BEST (In-place index marking)
# Time: O(n)  |  Space: O(1)
# Cyclic sort: place A[i] at index A[i]-1 if 1 <= A[i] <= n
# Then scan for index where A[i] != i+1
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    # Phase 1: correct placement
    for i in range(n):
        while 1 <= A[i] <= n and A[A[i] - 1] != A[i]:
            j = A[i] - 1
            A[i], A[j] = A[j], A[i]
    # Phase 2: find first wrong position
    for i in range(n):
        if A[i] != i + 1:
            return i + 1
    return n + 1


if __name__ == "__main__":
    cases = [
        ([1, 2, 0], 3),
        ([3, 4, -1, 1], 2),
        ([7, 8, 9, 11, 12], 1),
        ([1, 2, 3], 4),
        ([-5, -3, -1], 1),
        ([1], 2),
    ]
    print("=== First Missing Positive Integer ===")
    for A, exp in cases:
        b = brute_force(A[:])
        o = optimal(A[:])
        bst = best(A[:])
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"A={A} => {ok}  EXP={exp}")
