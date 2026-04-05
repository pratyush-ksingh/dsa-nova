"""
Problem: Hotel Bookings Possible
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given arrive[], depart[], and K rooms, determine if all hotel bookings
can be accommodated without exceeding K rooms at any point in time.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(arrive: List[int], depart: List[int], K: int) -> bool:
    """
    For each booking, count how many other bookings overlap with it.
    If any booking has >= K overlapping bookings (including itself), return False.
    Overlap condition: arrive[i] <= depart[j] AND arrive[j] <= depart[i]
    """
    n = len(arrive)
    for i in range(n):
        count = 0
        for j in range(n):
            # Booking j overlaps with booking i
            if arrive[j] <= depart[i] and arrive[i] <= depart[j]:
                count += 1
        if count > K:
            return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL — Sweep Line (Sort Events)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(arrive: List[int], depart: List[int], K: int) -> bool:
    """
    Create events for arrivals (+1) and departures (-1).
    Sort all events by time; on tie, process departure before arrival
    (a guest leaving frees a room before the next guest checks in).
    Track current occupancy; if it ever exceeds K, return False.
    """
    events = []
    for a in arrive:
        events.append((a, 1))    # arrival: need a room
    for d in depart:
        events.append((d, -1))   # departure: free a room

    # Sort by time; on tie put departure (-1) before arrival (+1)
    events.sort(key=lambda x: (x[0], x[1]))

    current_rooms = 0
    for _, event_type in events:
        current_rooms += event_type
        if current_rooms > K:
            return False
    return True


# ============================================================
# APPROACH 3: BEST — Two Sorted Arrays (slight optimization)
# Time: O(n log n)  |  Space: O(1) extra (sort in-place)
# ============================================================
def best(arrive: List[int], depart: List[int], K: int) -> bool:
    """
    Sort arrive[] and depart[] independently. Use two pointers to
    simulate a sweep: advance arrival pointer when the next event is
    an arrival, else advance departure pointer.
    This avoids building a combined event list.
    """
    arrive_sorted = sorted(arrive)
    depart_sorted = sorted(depart)
    n = len(arrive_sorted)

    current_rooms = 0
    i = 0  # arrival pointer
    j = 0  # departure pointer

    while i < n:
        if arrive_sorted[i] <= depart_sorted[j]:
            # A new guest arrives; need one more room
            current_rooms += 1
            if current_rooms > K:
                return False
            i += 1
        else:
            # Someone departs before next arrival; free a room
            current_rooms -= 1
            j += 1
    return True


if __name__ == "__main__":
    print("=== Hotel Bookings Possible ===")
    arrive = [1, 3, 5]
    depart = [2, 6, 8]
    K = 1
    print(f"Input: arrive={arrive}, depart={depart}, K={K}")
    print(f"Brute:   {brute_force(arrive, depart, K)}")   # False
    print(f"Optimal: {optimal(arrive, depart, K)}")        # False
    print(f"Best:    {best(arrive, depart, K)}")           # False

    arrive2 = [1, 2, 3]
    depart2 = [2, 3, 4]
    K2 = 2
    print(f"\nInput: arrive={arrive2}, depart={depart2}, K={K2}")
    print(f"Brute:   {brute_force(arrive2, depart2, K2)}")  # True
    print(f"Optimal: {optimal(arrive2, depart2, K2)}")       # True
    print(f"Best:    {best(arrive2, depart2, K2)}")          # True
