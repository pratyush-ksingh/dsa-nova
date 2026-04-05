"""Problem: Assign Mice to Holes
Difficulty: EASY | XP: 10
Source: InterviewBit

N mice and N holes on a number line. Assign each mouse to exactly one hole
to minimize the maximum distance any mouse has to travel.
"""
from itertools import permutations
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N! * N)  |  Space: O(N)
# Try all assignments (permutations of holes), pick min max-dist
# ============================================================
def brute_force(mice: List[int], holes: List[int]) -> int:
    holes_sorted = sorted(holes)
    mice_sorted = sorted(mice)
    min_max = float('inf')
    for perm in permutations(range(len(holes_sorted))):
        max_dist = max(abs(mice_sorted[i] - holes_sorted[perm[i]]) for i in range(len(mice_sorted)))
        min_max = min(min_max, max_dist)
    return min_max


# ============================================================
# APPROACH 2: OPTIMAL - Sort both and pair in order
# Time: O(N log N)  |  Space: O(N)
# Key insight: sorting both and pairing i-th to i-th minimizes max distance.
# Exchange argument: swapping any two assignments cannot decrease max.
# ============================================================
def optimal(mice: List[int], holes: List[int]) -> int:
    m = sorted(mice)
    h = sorted(holes)
    return max(abs(m[i] - h[i]) for i in range(len(m)))


# ============================================================
# APPROACH 3: BEST - Same approach, more Pythonic
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def best(mice: List[int], holes: List[int]) -> int:
    return max(abs(m - h) for m, h in zip(sorted(mice), sorted(holes)))


if __name__ == "__main__":
    tests = [
        ([-4, 2, 3], [0, 1, 9], 6),   # IB example
        ([0, 3], [1, 4], 1),
        ([1], [5], 4),
        ([-1, -3, 1], [-2, 0, 2], 1),
    ]
    for mice, holes, expected in tests:
        bf = brute_force(mice, holes)
        opt = optimal(mice, holes)
        be = best(mice, holes)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] mice={mice} holes={holes} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
