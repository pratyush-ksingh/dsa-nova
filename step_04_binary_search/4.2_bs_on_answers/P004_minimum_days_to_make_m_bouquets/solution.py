"""
Problem: Minimum Days to Make M Bouquets (LeetCode #1482)
Difficulty: MEDIUM | XP: 25

bloomDay[i] = day flower i blooms. Need m bouquets of k adjacent flowers.
Find minimum days to make m bouquets, or -1 if impossible.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Try Every Unique Day)
# Time: O(n * max(bloomDay)) | Space: O(1)
# ============================================================
def brute_force(bloomDay: List[int], m: int, k: int) -> int:
    """Try each day from 1 to max(bloomDay). Return first day with enough bouquets."""
    n = len(bloomDay)
    if m * k > n:
        return -1

    def count_bouquets(day: int) -> int:
        bouquets = 0
        consecutive = 0
        for bd in bloomDay:
            if bd <= day:
                consecutive += 1
                if consecutive == k:
                    bouquets += 1
                    consecutive = 0
            else:
                consecutive = 0
        return bouquets

    for day in range(1, max(bloomDay) + 1):
        if count_bouquets(day) >= m:
            return day
    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on Days)
# Time: O(n * log(max(bloomDay) - min(bloomDay))) | Space: O(1)
# ============================================================
def optimal(bloomDay: List[int], m: int, k: int) -> int:
    """Binary search on the day in [min, max]. Check feasibility at each mid."""
    n = len(bloomDay)
    if m * k > n:
        return -1

    def count_bouquets(day: int) -> int:
        bouquets = 0
        consecutive = 0
        for bd in bloomDay:
            if bd <= day:
                consecutive += 1
                if consecutive == k:
                    bouquets += 1
                    consecutive = 0
            else:
                consecutive = 0
        return bouquets

    lo, hi = min(bloomDay), max(bloomDay)
    ans = -1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if count_bouquets(mid) >= m:
            ans = mid
            hi = mid - 1    # feasible, try fewer days
        else:
            lo = mid + 1    # not enough bouquets, wait longer
    return ans


# ============================================================
# APPROACH 3: BEST (Binary Search + Early Termination)
# Time: O(n * log(max(bloomDay) - min(bloomDay))) | Space: O(1)
# ============================================================
def best(bloomDay: List[int], m: int, k: int) -> int:
    """Binary search with early termination in feasibility check."""
    n = len(bloomDay)
    if m * k > n:
        return -1

    def can_make(day: int) -> bool:
        bouquets = 0
        consecutive = 0
        for bd in bloomDay:
            if bd <= day:
                consecutive += 1
                if consecutive == k:
                    bouquets += 1
                    if bouquets >= m:    # early termination
                        return True
                    consecutive = 0
            else:
                consecutive = 0
        return False

    lo, hi = min(bloomDay), max(bloomDay)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if can_make(mid):
            hi = mid
        else:
            lo = mid + 1
    # Check if lo itself is feasible (handles edge case)
    return lo if can_make(lo) else -1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Minimum Days to Make M Bouquets ===\n")

    test_cases = [
        ([1, 10, 3, 10, 2], 3, 1, 3),
        ([1, 10, 3, 10, 2], 3, 2, -1),
        ([7, 7, 7, 7, 12, 7, 7], 2, 3, 12),
        ([1, 10, 2, 9, 3, 8, 4, 7, 5, 6], 4, 2, 9),
        ([1], 1, 1, 1),
    ]

    for bloomDay, m, k, expected in test_cases:
        b = brute_force(bloomDay, m, k)
        o = optimal(bloomDay, m, k)
        n = best(bloomDay, m, k)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input: bloomDay={bloomDay}, m={m}, k={k}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
