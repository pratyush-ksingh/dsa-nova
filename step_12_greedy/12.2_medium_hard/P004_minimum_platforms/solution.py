"""
Problem: Minimum Platforms (GeeksForGeeks)
Difficulty: MEDIUM | XP: 25

Given arrival and departure times of trains at a station, find the minimum
number of platforms needed so that no train has to wait.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Trains Against Each Other)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(arrival: List[int], departure: List[int]) -> int:
    """For each train, count how many other trains overlap with it."""
    n = len(arrival)
    max_platforms = 0

    for i in range(n):
        count = 1  # current train needs a platform
        for j in range(n):
            if i != j:
                # Train j overlaps with train i if it arrives before i departs
                # and departs after i arrives
                if arrival[j] <= departure[i] and departure[j] >= arrival[i]:
                    count += 1
        max_platforms = max(max_platforms, count)

    return max_platforms


# ============================================================
# APPROACH 2: OPTIMAL (Sort + Sweep Line / Two Pointer)
# Time: O(n log n) | Space: O(n) for sorted copies
# ============================================================
def optimal(arrival: List[int], departure: List[int]) -> int:
    """
    Sort arrivals and departures separately. Use two pointers to
    sweep through events. When an arrival comes before (or at) a
    departure, a new platform is needed. Otherwise, a platform is freed.
    """
    arr = sorted(arrival)
    dep = sorted(departure)
    n = len(arr)

    platforms = 0
    max_platforms = 0
    i, j = 0, 0

    while i < n:
        if arr[i] <= dep[j]:
            platforms += 1
            max_platforms = max(max_platforms, platforms)
            i += 1
        else:
            platforms -= 1
            j += 1

    return max_platforms


# ============================================================
# APPROACH 3: BEST (Same Sweep Line -- Cleanest Form)
# Time: O(n log n) | Space: O(n)
# ============================================================
def best(arrival: List[int], departure: List[int]) -> int:
    """
    Event-based sweep line. Create events: +1 for arrival, -1 for departure.
    Sort events (tie-break: departure before arrival if at same time,
    or arrival before departure -- depends on problem definition).
    Sweep and track running sum.
    """
    events = []
    for a in arrival:
        events.append((a, 1))    # arrival: need a platform
    for d in departure:
        events.append((d + 0.5, -1))  # departure: free a platform (after arrival at same time)

    events.sort()

    platforms = 0
    max_platforms = 0

    for _, delta in events:
        platforms += delta
        max_platforms = max(max_platforms, platforms)

    return max_platforms


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Minimum Platforms ===\n")

    test_cases = [
        ([900, 940, 950, 1100, 1500, 1800],
         [910, 1200, 1120, 1130, 1900, 2000], 3),
        ([900, 1100, 1235],
         [1000, 1200, 1240], 1),
        ([100, 200, 300, 400],
         [150, 250, 350, 450], 1),
        ([100, 100, 100],
         [200, 200, 200], 3),
        ([900],
         [1000], 1),
        ([940, 950, 950, 1100, 1500, 1800],
         [910, 1200, 1120, 1130, 1900, 2000], 3),
    ]

    for arrival, departure, expected in test_cases:
        b = brute_force(arrival[:], departure[:])
        o = optimal(arrival[:], departure[:])
        h = best(arrival[:], departure[:])
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: arr={arrival}, dep={departure}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
