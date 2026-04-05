# N Meetings in One Room

> **Batch 1 of 12** | **Topic:** Greedy | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given `n` meetings with their **start** and **end** times, find the **maximum number** of meetings that can be held in a single room. A meeting can start at the same time another ends (i.e., if meeting A ends at time `t`, meeting B can start at time `t`). This is the classic **Activity Selection Problem**.

### Analogy
Think of a **conference room booking system**. You have many meeting requests and one room. To fit the most meetings, pick the one that finishes earliest first -- it frees up the room the soonest for the next meeting. This is like a sprinter finishing their turn on the track so the next runner can start as early as possible.

### Key Observations
1. If we always pick the meeting that **ends earliest** and does not conflict with the last picked meeting, we maximize the number of meetings. **Aha:** Finishing early leaves the most room for future meetings -- classic greedy exchange argument.
2. Sorting by end time is crucial. Sorting by start time does NOT work (a meeting starting early but lasting very long blocks many others). **Aha:** The *end* time determines when the room frees up.
3. After sorting by end time, a single scan with one variable (`lastEnd`) suffices to greedily select meetings. **Aha:** O(n log n) sort + O(n) scan.

### Examples

| start[] | end[] | Output | Selected Meetings |
|---------|-------|--------|-------------------|
| [1,3,0,5,8,5] | [2,4,6,7,9,9] | 4 | meetings at [1,2], [3,4], [5,7], [8,9] |
| [1,1,1] | [2,3,4] | 1 | Any one of the three (they all overlap) |
| [1,3,5] | [2,4,6] | 3 | All three (no overlap) |

### Constraints
- 1 <= n <= 10^5
- 1 <= start[i] < end[i] <= 10^6

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (all subsets) | Bitmask / backtracking | Try every subset of non-overlapping meetings, find max. Exponential. |
| Optimal (sort by end time + greedy) | Array of meetings sorted by end | Greedy: always pick earliest-ending non-conflicting meeting. O(n log n). |
| Best (same as optimal) | Sorted array | The sorting lower bound is Omega(n log n); greedy scan is O(n). |

**Pattern cue:** "Maximum number of non-overlapping intervals" -> sort by end time, greedy selection.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (All Subsets)
**Intuition:** Generate all subsets of meetings. For each subset, check if all meetings are mutually non-overlapping. Track the largest valid subset.

**Steps:**
1. Generate all 2^n subsets.
2. For each subset, sort by start time and verify no overlaps.
3. Return the size of the largest non-overlapping subset.

| Metric | Value |
|--------|-------|
| Time | O(2^n * n) |
| Space | O(n) |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Exponential subsets. The greedy insight is that sorting by end time and always taking the next compatible meeting yields the optimal answer. Proof: if any other meeting were chosen instead, it ends later, leaving less room -- swapping it out for the earlier-ending one never worsens the count.

### Approach 2 -- Optimal (Sort by End Time + Greedy)
**Intuition:** Sort meetings by end time. Greedily pick each meeting whose start time >= the end time of the last picked meeting.

**Steps:**
1. Create tuples `(start[i], end[i], i)` for each meeting.
2. Sort by `end` time (break ties by `start` time).
3. Initialize `lastEnd = -1`, `count = 0`.
4. For each meeting in sorted order:
   - If `start >= lastEnd`: select it, update `lastEnd = end`, increment count.
5. Return count.

**Dry-Run Trace -- start=[1,3,0,5,8,5], end=[2,4,6,7,9,9]:**

After sorting by end: [(1,2), (3,4), (0,6), (5,7), (8,9), (5,9)]

| Step | Meeting | start>=lastEnd? | Action | lastEnd | Count |
|------|---------|-----------------|--------|---------|-------|
| 1 | (1,2) | 1>=-1 Yes | Select | 2 | 1 |
| 2 | (3,4) | 3>=2 Yes | Select | 4 | 2 |
| 3 | (0,6) | 0>=4 No | Skip | 4 | 2 |
| 4 | (5,7) | 5>=4 Yes | Select | 7 | 3 |
| 5 | (8,9) | 8>=7 Yes | Select | 9 | 4 |
| 6 | (5,9) | 5>=9 No | Skip | 9 | 4 |

Result: 4

| Metric | Value |
|--------|-------|
| Time | O(n log n) |
| Space | O(n) for the meeting array |

### Approach 3 -- Best (Same as Optimal)
Cannot improve beyond O(n log n) since sorting is required. The greedy scan is O(n).

| Metric | Value |
|--------|-------|
| Time | O(n log n) |
| Space | O(n) |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n log n):** Dominated by sorting. The greedy scan visits each meeting exactly once.
- **O(n) space:** We store the meetings with their indices. Could be O(1) extra if we sort in-place.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Single meeting | Always return 1 |
| All meetings overlap | Return 1 (pick the one ending earliest) |
| No overlaps at all | Return n (all meetings selected) |
| Meetings where one ends as another starts | Both can be selected (start == lastEnd is OK) |
| Identical start and end times | Pick one, skip others |

**Common mistakes:**
- Sorting by start time instead of end time.
- Using strict inequality (`start > lastEnd`) instead of `>=`. The problem says a meeting can start when another ends.
- Forgetting to track meeting indices if the problem asks *which* meetings to select.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Prove greedy is optimal? | Exchange argument: replace any selected meeting with an earlier-ending one -- count stays same or improves. |
| What if we need minimum rooms for ALL meetings? | Different problem: "Meeting Rooms II" (LC #253). Use min-heap on end times. |
| What if meetings have weights/values? | Weighted activity selection: use DP + binary search, not greedy. |
| Can start == end? | Problem states start < end, but if allowed, a zero-length meeting fits anywhere. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Meeting Rooms (LC #252) | Check if ANY overlap exists (simpler: just sort and scan) |
| Meeting Rooms II (LC #253) | Count minimum rooms needed (min-heap approach) |
| Non-overlapping Intervals (LC #435) | Minimize removals = n - max non-overlapping (same greedy) |
| Minimum Platforms (Railway) | Similar to Meeting Rooms II: track arrivals/departures |
| Assign Cookies | Same greedy pattern: sort + match |
