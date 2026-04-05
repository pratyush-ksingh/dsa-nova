# Minimum Platforms

> **Step 12 | 12.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given two arrays `arrival[]` and `departure[]` representing the arrival and departure times of `n` trains at a railway station, find the **minimum number of platforms** required so that no train has to wait for a platform.

If two trains arrive and depart at the same time, they are considered to need separate platforms (i.e., `arrival[i] <= departure[j]` means overlap).

**Source:** GeeksForGeeks

**Constraints:**
- `1 <= n <= 10^5`
- `0 <= arrival[i], departure[i] <= 2359`
- `arrival[i] <= departure[i]` for each train

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [900,940,950,1100,1500,1800], dep = [910,1200,1120,1130,1900,2000]` | `3` | At time 950, trains arriving at 940, 950 and the one from 900 (departs 910... wait, 910<950) -- actually trains 940-1200, 950-1120, 1100-1130 overlap around time 1100 |
| `arr = [900,1100,1235], dep = [1000,1200,1240]` | `1` | No two trains overlap |
| `arr = [100,100,100], dep = [200,200,200]` | `3` | All three trains present simultaneously |

### Real-Life Analogy
> *This is literally the railway station problem! Indian Railways needs to figure out how many platforms to build at each station. The sweep line approach is like a stationmaster standing at the entrance, incrementing a counter when a train arrives and decrementing when one departs. The maximum counter value during the day is the number of platforms needed.*

### Key Observations
1. We do not care which specific train is on which platform -- only the maximum simultaneous occupancy.
2. Sorting arrivals and departures independently is valid because we only need counts, not pairings.
3. This is an interval scheduling / sweep line problem.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- **Sweep line** with sorted events is the standard technique for "maximum overlap" problems.
- Two-pointer variant avoids creating explicit event objects.

### Pattern Recognition
- **Pattern:** Sweep Line / Event Processing
- **Classification Cue:** "Whenever you see _intervals with start/end times_ and need _maximum overlap_ --> think _sort events and sweep_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Pairs
**Idea:** For each train, count how many other trains overlap with it. The maximum count is the answer.

**Steps:**
1. For each train `i`:
   - Initialize `count = 1` (the train itself).
   - For each other train `j`:
     - If `arrival[j] <= departure[i]` and `departure[j] >= arrival[i]`, they overlap. Increment count.
   - Update `maxPlatforms = max(maxPlatforms, count)`.
2. Return `maxPlatforms`.

**Why it works:** Directly checks overlap condition for every pair.

**BUD Transition -- Bottleneck:** O(n^2) pair checks. Sorting reduces this to O(n log n).

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

### Approach 2: Optimal -- Sort + Two Pointer Sweep
**What changed:** Sort arrivals and departures independently. Use two pointers: one for arrivals, one for departures.

**Steps:**
1. Sort `arrival[]` and `departure[]` independently.
2. Initialize `i = 0` (arrival pointer), `j = 0` (departure pointer), `platforms = 0`, `max = 0`.
3. While `i < n`:
   - If `arrival[i] <= departure[j]`: a train arrives before (or when) one departs. Need +1 platform. `platforms++`, `i++`.
   - Else: a train departs. Free a platform. `platforms--`, `j++`.
   - Update `max = max(max, platforms)`.
4. Return `max`.

**Why sorting independently works:** We only care about the count of overlapping intervals. The next event is either the earliest unprocessed arrival or the earliest unprocessed departure.

**Dry Run:** `arr = [900,940,950,1100,1500,1800], dep = [910,1200,1120,1130,1900,2000]`
Sorted: `arr = [900,940,950,1100,1500,1800]`, `dep = [910,1120,1130,1200,1900,2000]`

| Step | arr[i] | dep[j] | Action | platforms | max |
|------|--------|--------|--------|-----------|-----|
| 1 | 900 | 910 | 900<=910, arrive | 1 | 1 |
| 2 | 940 | 910 | 940>910, depart | 0 | 1 |
| 3 | 940 | 1120 | 940<=1120, arrive | 1 | 1 |
| 4 | 950 | 1120 | 950<=1120, arrive | 2 | 2 |
| 5 | 1100 | 1120 | 1100<=1120, arrive | 3 | 3 |
| 6 | 1500 | 1120 | 1500>1120, depart | 2 | 3 |
| 7 | 1500 | 1130 | 1500>1130, depart | 1 | 3 |
| 8 | 1500 | 1200 | 1500>1200, depart | 0 | 3 |
| 9 | 1500 | 1900 | 1500<=1900, arrive | 1 | 3 |
| 10 | 1800 | 1900 | 1800<=1900, arrive | 2 | 3 |

**Result:** 3

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) if in-place sort, O(n) for copies |

---

### Approach 3: Best -- Event-Based Sweep Line
**Intuition:** Create explicit events: `(time, +1)` for arrivals and `(time, -1)` for departures. Sort all events by time (arrivals before departures at same time to handle the overlap rule). Sweep through and track the running platform count.

This is conceptually cleaner and generalizes to more complex scheduling problems (e.g., weighted intervals, multi-resource scheduling).

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) for events array |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Not handling the tie case correctly: if a train arrives at the same time another departs, do they need the same platform? (Standard GFG: yes, they overlap.)
2. Sorting arrival-departure pairs together instead of independently -- the two-pointer approach requires independent sorting.
3. Forgetting that `j` pointer should not exceed `n` -- it will not, since every arrival has a matching departure.

### Edge Cases to Test
- [ ] Single train --> 1
- [ ] All trains at the same time --> n
- [ ] No overlaps at all --> 1
- [ ] Two trains: one arrives exactly when another departs

---

## Real-World Use Case
**Cloud server auto-scaling:** Given a log of request start and end times, determine the peak concurrent requests. This directly determines the minimum number of server instances needed. The sweep line algorithm is used in production monitoring dashboards to compute peak concurrency metrics.

## Interview Tips
- Immediately identify this as a sweep line / interval overlap problem.
- The two-pointer approach on independently sorted arrays is the cleanest to code in an interview.
- Clarify the tie-breaking rule (do simultaneous arrival and departure require separate platforms?).
- Mention the generalization: this technique applies to meeting rooms (LC 253), car pooling, etc.
- This is a must-know problem for FAANG interviews -- appears frequently at Amazon and Microsoft.
