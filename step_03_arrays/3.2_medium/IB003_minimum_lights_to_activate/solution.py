"""
Problem: Minimum Lights to Activate
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array A of 0s and 1s (1 means a light is installed at position i)
and an integer B (the range each light covers: it illuminates positions
[i-B, i+B] inclusive), return the minimum number of lights to turn on to
illuminate every position in the array. Return -1 if it's impossible.

Positions are 1-indexed internally but the array is 0-indexed.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^k * n) where k = number of lights  |  Space: O(k)
# Try every subset of available lights; find minimum cover
# Only feasible for very small arrays.
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    """
    Enumerate all subsets of positions that have lights (A[i]==1).
    For each subset, check if it covers every position [0..n-1].
    Return the size of the smallest covering subset.
    """
    n = len(A)
    lights = [i for i in range(n) if A[i] == 1]
    k = len(lights)

    def covers_all(subset_mask: int) -> bool:
        covered = [False] * n
        for bit in range(k):
            if subset_mask & (1 << bit):
                center = lights[bit]
                for pos in range(max(0, center - B), min(n - 1, center + B) + 1):
                    covered[pos] = True
        return all(covered)

    # Try subsets in order of increasing size
    for size in range(k + 1):
        # Enumerate all subsets of given size
        from itertools import combinations
        for combo in combinations(range(k), size):
            mask = sum(1 << b for b in combo)
            if covers_all(mask):
                return size
    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Greedy — jump to farthest uncovered)
# Time: O(n)  |  Space: O(1)
# For each uncovered position, find the rightmost light in its [pos-B, pos+B]
# window, activate it, and jump past its coverage.
# ============================================================
def optimal(A: List[int], B: int) -> int:
    """
    Greedy approach:
    - Maintain a pointer `pos` = next uncovered position (0-indexed).
    - For position `pos`, look for a light in [pos-B .. pos+B].
    - Among all available lights in that window, pick the rightmost one
      (maximises coverage to the right).
    - Jump pos to (rightmost_light + B + 1).
    - If no light exists in the window, return -1.
    """
    n = len(A)
    count = 0
    pos = 0  # leftmost uncovered position

    while pos < n:
        # Search window: [pos-B .. pos+B], clamped to [0, n-1]
        left  = max(0, pos - B)
        right = min(n - 1, pos + B)

        # Find rightmost light in [left .. right]
        best_light = -1
        for i in range(right, left - 1, -1):
            if A[i] == 1:
                best_light = i
                break

        if best_light == -1:
            return -1  # no light can cover position `pos`

        count += 1
        # Jump past the coverage of the chosen light
        pos = best_light + B + 1

    return count


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# Same greedy as Optimal but scans from pos+B downward (slightly cleaner loop).
# Identical asymptotic complexity; included as "best" for clarity.
# ============================================================
def best(A: List[int], B: int) -> int:
    """
    Identical to Optimal; restructured for clarity.
    Start searching from the farthest reachable position and scan left
    to find the rightmost available light.
    """
    n = len(A)
    count = 0
    pos = 0

    while pos < n:
        # Farthest position reachable from pos is pos+B
        # Light must be in [pos-B, pos+B] to cover pos
        found = -1
        # Start from farthest right possible, scan leftward
        for i in range(min(pos + B, n - 1), max(pos - B - 1, -1), -1):
            if A[i] == 1:
                found = i
                break

        if found == -1:
            return -1

        count += 1
        pos = found + B + 1  # next uncovered position

    return count


if __name__ == "__main__":
    print("=== Minimum Lights to Activate ===")
    test_cases = [
        # (A, B, expected)
        ([0, 0, 1, 0, 0], 2, 1),     # light at idx 2 covers all 5 positions
        ([1, 0, 0, 1, 1, 0, 0, 1], 2, 3),  # lights at idx 0,4,7 cover all
        ([0, 0, 0, 1, 0, 0, 0], 1, -1),   # positions 0,1 unreachable
        ([1, 1, 1, 1, 1], 0, 5),           # B=0: each light covers only itself
        ([1, 0, 0, 0, 1], 1, -1),          # middle unreachable
        ([1, 0, 1, 0, 1], 1, 3),           # lights at idx 0,2,4 each needed
    ]
    for A, B, expected in test_cases:
        b  = brute_force(A[:], B)
        o  = optimal(A[:], B)
        be = best(A[:], B)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"A={A}, B={B} | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
