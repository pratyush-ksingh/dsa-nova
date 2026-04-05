"""
Problem: Capacity to Ship Packages Within D Days (LeetCode #1011)
Difficulty: MEDIUM | XP: 25

A conveyor belt has packages that must be shipped from one port to another
within D days. The i-th package has a weight of weights[i]. Each day we
load the ship with packages in order (must not split a package across days).
Find the minimum weight capacity of the ship to ship all packages within D days.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Try every capacity from max to sum)
# Time: O(n * (sum - max)) | Space: O(1)
# ============================================================
def brute_force(weights: List[int], days: int) -> int:
    """Try every possible capacity from max(weights) to sum(weights). Return the first that works."""

    def can_ship(capacity: int) -> bool:
        current_load = 0
        days_needed = 1
        for w in weights:
            if current_load + w > capacity:
                days_needed += 1
                current_load = 0
            current_load += w
        return days_needed <= days

    min_cap = max(weights)   # must fit the heaviest package
    max_cap = sum(weights)   # worst case: ship everything in one day

    for cap in range(min_cap, max_cap + 1):
        if can_ship(cap):
            return cap
    return max_cap


# ============================================================
# APPROACH 2: OPTIMAL (Binary search on capacity)
# Time: O(n * log(sum - max)) | Space: O(1)
# ============================================================
def optimal(weights: List[int], days: int) -> int:
    """
    Binary search on capacity in [max(weights), sum(weights)].
    For a given capacity c, compute the minimum days needed greedily.
    If days_needed <= D, try a smaller capacity; else try larger.
    """

    def days_needed(capacity: int) -> int:
        total_days = 1
        current = 0
        for w in weights:
            if current + w > capacity:
                total_days += 1
                current = 0
            current += w
        return total_days

    lo, hi = max(weights), sum(weights)
    ans = hi
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if days_needed(mid) <= days:
            ans = mid       # feasible; try smaller capacity
            hi = mid - 1
        else:
            lo = mid + 1    # not feasible; need larger capacity
    return ans


# ============================================================
# APPROACH 3: BEST (Binary search + lo < hi template)
# Time: O(n * log(sum - max)) | Space: O(1)
# ============================================================
def best(weights: List[int], days: int) -> int:
    """
    Same binary search with the lo < hi template (no separate ans variable).
    The feasibility check is inlined for clarity.
    """

    def can_ship(capacity: int) -> bool:
        days_needed = 1
        current = 0
        for w in weights:
            if current + w > capacity:
                days_needed += 1
                current = 0
                if days_needed > days:  # early exit
                    return False
            current += w
        return True

    lo, hi = max(weights), sum(weights)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if can_ship(mid):
            hi = mid        # feasible; try smaller
        else:
            lo = mid + 1    # not feasible; need larger
    return lo


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Capacity to Ship Packages Within D Days ===\n")

    test_cases = [
        ([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 5, 15),
        ([3, 2, 2, 4, 1, 4], 3, 6),
        ([1, 2, 3, 1, 1], 4, 3),
        ([10], 1, 10),
        ([1, 1, 1, 1, 1, 1, 1, 1, 1, 1], 10, 1),
    ]

    for weights, days, expected in test_cases:
        b = brute_force(weights, days)
        o = optimal(weights, days)
        n = best(weights, days)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: weights={weights}, days={days}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
