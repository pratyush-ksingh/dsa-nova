"""Problem: Hotel Service
Difficulty: MEDIUM | XP: 25"""

from typing import List
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * m)  |  Space: O(1)
# For each customer, scan all hotels and find the nearest one.
# ============================================================
def brute_force(hotels: List[int], customers: List[int]) -> List[int]:
    result = []
    for c in customers:
        nearest = min(hotels, key=lambda h: (abs(h - c), h))
        result.append(nearest)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O((n + m) log n)  |  Space: O(n)
# Sort hotels; binary search for each customer; compare neighbors.
# ============================================================
def optimal(hotels: List[int], customers: List[int]) -> List[int]:
    sorted_hotels = sorted(hotels)
    result = []

    for c in customers:
        idx = bisect.bisect_left(sorted_hotels, c)
        # Candidates: sorted_hotels[idx-1] and sorted_hotels[idx]
        best_hotel = None
        best_dist = float('inf')

        for i in [idx - 1, idx]:
            if 0 <= i < len(sorted_hotels):
                d = abs(sorted_hotels[i] - c)
                if d < best_dist or (d == best_dist and sorted_hotels[i] < best_hotel):
                    best_dist = d
                    best_hotel = sorted_hotels[i]

        result.append(best_hotel)
    return result


# ============================================================
# APPROACH 3: BEST
# Time: O((n + m) log n)  |  Space: O(n)
# Same binary search; written as a clean helper function.
# ============================================================
def _find_nearest(sorted_hotels: List[int], customer: int) -> int:
    idx = bisect.bisect_left(sorted_hotels, customer)
    if idx == 0:
        return sorted_hotels[0]
    if idx == len(sorted_hotels):
        return sorted_hotels[-1]
    left  = sorted_hotels[idx - 1]
    right = sorted_hotels[idx]
    # On tie, prefer the smaller (left) hotel
    return left if abs(left - customer) <= abs(right - customer) else right


def best(hotels: List[int], customers: List[int]) -> List[int]:
    sorted_hotels = sorted(hotels)
    return [_find_nearest(sorted_hotels, c) for c in customers]


if __name__ == "__main__":
    tests = [
        ([1, 5, 9, 15], [3, 7, 10, 12], [1, 5, 9, 9]),
        ([2, 8],        [1, 5, 7, 10],  [2, 2, 8, 8]),
        ([3],           [1, 3, 5, 100], [3, 3, 3, 3]),
        ([10, 20, 30],  [5, 15, 25],    [10, 10, 20]),
    ]
    print("=== Hotel Service ===")
    for hotels, customers, expected in tests:
        b  = brute_force(hotels, customers)
        o  = optimal(hotels, customers)
        be = best(hotels, customers)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"Hotels: {hotels}, Customers: {customers}")
        print(f"  Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
