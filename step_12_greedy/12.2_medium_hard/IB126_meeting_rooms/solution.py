"""
Problem: Meeting Rooms (Minimum Meeting Rooms Required)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a list of meeting intervals [start, end], find the minimum number of
conference rooms required to hold all meetings without overlap.

Real-world: calendar scheduling, resource allocation systems.
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE - Sort by start; check each room linearly
# Time: O(N^2)  |  Space: O(N)
# For each meeting, scan existing rooms for one that is free; open new if none
# ============================================================
def brute_force(intervals: List[List[int]]) -> int:
    if not intervals:
        return 0
    intervals.sort(key=lambda x: x[0])
    rooms: List[int] = []  # stores end time of each active room
    for start, end in intervals:
        freed = False
        for i, room_end in enumerate(rooms):
            if room_end <= start:
                rooms[i] = end
                freed = True
                break
        if not freed:
            rooms.append(end)
    return len(rooms)


# ============================================================
# APPROACH 2: OPTIMAL - Sort by start + min-heap of end times
# Time: O(N log N)  |  Space: O(N)
# Min-heap holds end times of active meetings.
# If heap.min <= current start, meeting ended — reuse room (replace end time).
# Otherwise allocate a new room.
# ============================================================
def optimal(intervals: List[List[int]]) -> int:
    if not intervals:
        return 0
    intervals.sort(key=lambda x: x[0])
    heap: List[int] = []  # min-heap of end times
    for start, end in intervals:
        if heap and heap[0] <= start:
            heapq.heapreplace(heap, end)  # reuse room: pop min end, push new end
        else:
            heapq.heappush(heap, end)     # need a new room
    return len(heap)


# ============================================================
# APPROACH 3: BEST - Sweep line with two sorted arrays
# Time: O(N log N)  |  Space: O(N)
# Intuition: meeting starts need a room, meeting ends free one.
# Sort starts and ends independently; two-pointer count max overlap.
# ============================================================
def best(intervals: List[List[int]]) -> int:
    if not intervals:
        return 0
    starts = sorted(x[0] for x in intervals)
    ends   = sorted(x[1] for x in intervals)
    rooms = max_rooms = e = 0
    for s in starts:
        if s < ends[e]:
            rooms += 1
        else:
            e += 1
        max_rooms = max(max_rooms, rooms)
    return max_rooms


if __name__ == "__main__":
    print("=== Meeting Rooms ===")

    intervals = [[0, 30], [5, 10], [15, 20]]
    print(f"intervals={intervals}")
    print(f"Brute:   {brute_force([i[:] for i in intervals])}")   # 2
    print(f"Optimal: {optimal([i[:] for i in intervals])}")        # 2
    print(f"Best:    {best(intervals)}")                           # 2

    intervals = [[7, 10], [2, 4]]
    print(f"\nintervals={intervals}")
    print(f"Brute:   {brute_force([i[:] for i in intervals])}")   # 1
    print(f"Optimal: {optimal([i[:] for i in intervals])}")
    print(f"Best:    {best(intervals)}")

    intervals = [[1,5],[2,6],[3,7],[4,8],[5,9]]
    print(f"\nintervals={intervals}")
    print(f"Brute:   {brute_force([i[:] for i in intervals])}")   # 4
    print(f"Optimal: {optimal([i[:] for i in intervals])}")
    print(f"Best:    {best(intervals)}")
