"""Problem: WoodCutting Made Easy
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given tree heights A and required wood B, find the maximum cut height H
such that the total wood collected (sum of max(0, tree-H)) >= B.
A sawmill cuts every tree above height H; you collect the excess.
Real-life use: optimal resource harvesting with minimum impact.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(max_height * n)  |  Space: O(1)
# Try every possible height from max down to 0
# ============================================================
def brute_force(A: List[int], B: int) -> int:
    max_h = max(A)
    for h in range(max_h, -1, -1):
        wood = sum(tree - h for tree in A if tree > h)
        if wood >= B:
            return h
    return 0


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on Answer)
# Time: O(n log(max_height))  |  Space: O(1)
# Binary search on cut height; for each mid compute total wood.
# Maximise h while wood >= B.
# ============================================================
def optimal(A: List[int], B: int) -> int:
    def compute_wood(h: int) -> int:
        return sum(tree - h for tree in A if tree > h)

    lo, hi = 0, max(A)
    ans = 0
    while lo <= hi:
        mid = (lo + hi) // 2
        if compute_wood(mid) >= B:
            ans = mid
            lo = mid + 1  # try cutting higher
        else:
            hi = mid - 1
    return ans


# ============================================================
# APPROACH 3: BEST
# Time: O(n log(max_height))  |  Space: O(1)
# Same binary search; use generator expression for clean code.
# Also handles large B (use integer arithmetic, no overflow risk in Python).
# ============================================================
def best(A: List[int], B: int) -> int:
    lo, hi, ans = 0, max(A), 0
    while lo <= hi:
        mid = (lo + hi) // 2
        wood = sum(t - mid for t in A if t > mid)
        if wood >= B:
            ans = mid
            lo = mid + 1
        else:
            hi = mid - 1
    return ans


if __name__ == "__main__":
    cases = [
        ([20, 15, 10, 17], 7, 15),
        ([4, 42, 40, 26, 46], 20, 36),
        ([1, 2], 3, 0),      # not enough wood at all
        ([100, 200, 300], 100, 200),
    ]
    print("=== WoodCutting Made Easy ===")
    for A, B, exp in cases:
        b   = brute_force(A[:], B)
        o   = optimal(A[:], B)
        bst = best(A[:], B)
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"A={A}  B={B} => {ok}  EXP={exp}")
