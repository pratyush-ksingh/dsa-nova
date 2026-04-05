# Hotel Bookings Possible

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given two integer arrays `arrive[]` and `depart[]` representing the arrival and departure days of N hotel guests, and an integer `K` representing the total number of rooms, determine whether all bookings can be accommodated.

A booking is possible only if, at no point in time, the number of guests present simultaneously exceeds K.

**Note:** A guest departing on day D and another arriving on day D do NOT overlap (the departing guest checks out before the arriving guest checks in).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| arrive=[1,3,5], depart=[2,6,8], K=1 | false | Guest 2 (days 3-6) and Guest 3 (days 5-8) overlap; 2 rooms needed but only 1 available |
| arrive=[1,2,3], depart=[2,3,4], K=2 | true | At most 2 guests present simultaneously, which fits within K=2 |
| arrive=[1,2], depart=[3,4], K=2 | true | Both guests arrive before either departs; 2 rooms needed, 2 available |

## Constraints

- 1 <= N <= 10^5
- 1 <= arrive[i], depart[i] <= 10^9
- arrive[i] <= depart[i] for all i
- 1 <= K <= 10^5

---

## Approach 1: Brute Force

**Intuition:** For each booking, explicitly count how many other bookings overlap with it. Two bookings (i, j) overlap if `arrive[i] <= depart[j] AND arrive[j] <= depart[i]`. If any booking has more than K overlapping bookings (including itself), the answer is false.

**Steps:**
1. For each booking `i` from 0 to n-1:
   - Initialize a counter `count = 0`
   - For each booking `j` from 0 to n-1:
     - If `arrive[j] <= depart[i]` AND `arrive[i] <= depart[j]`, they overlap — increment `count`
   - If `count > K`, return false
2. Return true

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

## Approach 2: Optimal — Sweep Line with Combined Event List

**Intuition:** Treat each arrival as a +1 event and each departure as a -1 event. Sort all 2N events by time (with departures before arrivals on the same day). Sweep through them maintaining current occupancy. If occupancy ever exceeds K, return false.

**Steps:**
1. Build an events array with (day, +1) for arrivals and (day, -1) for departures
2. Sort by day; break ties by putting departures (-1) before arrivals (+1) — this ensures a guest checking out on the same day another arrives doesn't cause a false overflow
3. Iterate through events, maintaining `current_rooms`
4. If `current_rooms > K` at any point, return false
5. Return true

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Approach 3: Best — Two Sorted Arrays + Two Pointers

**Intuition:** Instead of building a combined events array, sort arrive[] and depart[] independently. Use two pointers to simulate the sweep. At each step, compare the next arrival against the next departure. If the arrival comes first (or same day), increment occupancy; otherwise free a room. This avoids building an extra data structure.

**Steps:**
1. Sort `arrive[]` and `depart[]` independently
2. Initialize `current_rooms = 0`, `i = 0` (arrival pointer), `j = 0` (departure pointer)
3. While `i < n`:
   - If `arrive[i] <= depart[j]`: a guest arrives — increment `current_rooms`. If `> K`, return false. Increment `i`.
   - Else: a guest departs — decrement `current_rooms`. Increment `j`.
4. Return true

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) extra (in-place sort or small clone) |

---

## Real-World Use Case

**Cloud Resource Scheduling:** In cloud computing, tasks have start and end times, and a cluster has a fixed number of machines. This exact problem determines whether a batch of jobs can be scheduled on K machines without overprovisioning. The sweep-line approach is the foundation of scheduling algorithms in Kubernetes, AWS Batch, and similar systems.

**Conference Room Booking:** A company with K meeting rooms receives N booking requests with start/end times. This algorithm answers whether all meetings can be accommodated — the same logic used in Google Calendar's room finder.

## Interview Tips

- Always clarify whether a departure and arrival on the same day overlap — the typical assumption (used here) is they do NOT (checkout before check-in).
- The key insight is converting the interval overlap problem into a timeline event problem. This transforms O(n^2) pair-checking into O(n log n) sorting.
- The two-pointer approach (Approach 3) is the most interview-friendly: it avoids extra space while being clean and easy to explain.
- This problem is identical in structure to "Meeting Rooms II" (LeetCode 253) — recognizing the pattern earns bonus points with interviewers.
- If asked about the minimum number of rooms needed (not just a yes/no), the answer is the maximum value of `current_rooms` during the sweep.
