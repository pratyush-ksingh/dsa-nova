"""
Problem: Koko Eating Bananas (LeetCode #875)
Difficulty: MEDIUM | XP: 25

Koko can eat bananas at speed k per hour. Each pile takes ceil(pile/k) hours.
Given piles array and h hours, find minimum k so she can eat all within h hours.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(max(piles) * n) | Space: O(1)
# ============================================================
def brute_force(piles: List[int], h: int) -> int:
    """Try every k from 1 to max(piles). Return the first k that works."""

    def total_hours(k: int) -> int:
        return sum(math.ceil(p / k) for p in piles)

    for k in range(1, max(piles) + 1):
        if total_hours(k) <= h:
            return k
    return max(piles)


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on Answer)
# Time: O(n * log(max(piles))) | Space: O(1)
# ============================================================
def optimal(piles: List[int], h: int) -> int:
    """Binary search on k in [1, max(piles)]. Check feasibility at each mid."""

    def total_hours(k: int) -> int:
        return sum(math.ceil(p / k) for p in piles)

    lo, hi = 1, max(piles)
    ans = hi
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if total_hours(mid) <= h:
            ans = mid       # feasible, try smaller k
            hi = mid - 1
        else:
            lo = mid + 1    # too slow, need faster eating
    return ans


# ============================================================
# APPROACH 3: BEST (Binary Search + Early Termination)
# Time: O(n * log(max(piles))) | Space: O(1)
# ============================================================
def best(piles: List[int], h: int) -> int:
    """Binary search with early termination in feasibility check."""

    def can_finish(k: int) -> bool:
        hours = 0
        for p in piles:
            hours += (p + k - 1) // k  # ceil without float
            if hours > h:              # early termination
                return False
        return True

    lo, hi = 1, max(piles)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if can_finish(mid):
            hi = mid        # feasible, try smaller
        else:
            lo = mid + 1    # not feasible, need larger k
    return lo


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Koko Eating Bananas ===\n")

    test_cases = [
        ([3, 6, 7, 11], 8, 4),
        ([30, 11, 23, 4, 20], 5, 30),
        ([30, 11, 23, 4, 20], 6, 23),
        ([1], 1, 1),
        ([1000000000], 2, 500000000),
    ]

    for piles, h, expected in test_cases:
        b = brute_force(piles, h)
        o = optimal(piles, h)
        n = best(piles, h)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: piles={piles}, h={h}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
