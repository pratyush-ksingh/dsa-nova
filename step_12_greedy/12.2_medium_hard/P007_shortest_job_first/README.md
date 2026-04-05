# Shortest Job First (SJF) Scheduling

> **Batch 2 of 12** | **Topic:** Greedy | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of **burst times** for `n` processes, compute the **average waiting time** using the **Shortest Job First (SJF)** non-preemptive scheduling algorithm. All processes arrive at time 0. Waiting time for a process is the total time it spends waiting before its execution begins.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= burst[i] <= 10^4`

**Examples:**

| Input (Burst Times) | Output (Avg Wait) | Explanation |
|---------------------|--------------------|-------------|
| `[4, 3, 7, 1, 2]` | `4.0` | Sorted: [1,2,3,4,7]. Waits: 0,1,3,6,10. Avg = 20/5 = 4.0 |
| `[1, 2, 3]` | `1.0` | Already sorted. Waits: 0,1,3. Avg = 4/3 ~ 1.33 |
| `[5, 5, 5]` | `5.0` | All same. Waits: 0,5,10. Avg = 15/3 = 5.0 |

### Real-Life Analogy
> *Imagine you are at a photocopy shop with 5 people in line, each needing different numbers of pages copied. If you let the person with the fewest pages go first, they leave quickly and everyone behind them waits less. Putting the 100-page job first would make everyone else wait a very long time. SJF is the greedy strategy: always pick the shortest job next to minimize total (and thus average) waiting time.*

### Key Observations
1. **Sort by burst time:** Processing shortest jobs first minimizes total waiting time. This is provably optimal for minimizing average wait.
2. **Waiting time is a prefix sum:** Each process waits for the sum of all burst times before it. So waiting time for process `i` (after sorting) is `sum(burst[0..i-1])`.
3. **Total wait = sum of prefix sums:** Total wait = `burst[0]*0 + burst[0]*1 + (burst[0]+burst[1])*1 + ...` which simplifies to `sum(burst[j] * (n-1-j))` after sorting. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Greedy is optimal here:** SJF minimizes average waiting time. Proof by exchange argument: swapping a longer job before a shorter one always increases total wait.
- Sorting + prefix sum is the entire algorithm.
- No DP or complicated scheduling needed since all processes arrive at time 0 (non-preemptive, simultaneous arrival).

### Pattern Recognition
- **Pattern:** Sort then simulate (greedy scheduling)
- **Classification Cue:** "When you see _minimize average waiting time with simultaneous arrivals_ --> think _sort by burst time (SJF)_"

---

## APPROACH LADDER

### Approach 1: Sort + Prefix Sum (Optimal)
**Idea:** Sort burst times ascending. Compute waiting time as running prefix sum.

**Steps:**
1. Sort the burst times array.
2. Initialize `totalWait = 0`, `currentTime = 0`.
3. For each process `i` (0-indexed):
   - Waiting time for process `i` = `currentTime` (it waited for all previous jobs).
   - `totalWait += currentTime`.
   - `currentTime += burst[i]` (this job now runs).
4. Return `totalWait / n`.

**Dry Run:** `burst = [4, 3, 7, 1, 2]`

Sorted: `[1, 2, 3, 4, 7]`

| i | burst[i] | currentTime (wait) | totalWait | currentTime after |
|---|----------|-------------------|-----------|-------------------|
| 0 | 1 | 0 | 0 | 1 |
| 1 | 2 | 1 | 1 | 3 |
| 2 | 3 | 3 | 4 | 6 |
| 3 | 4 | 6 | 10 | 10 |
| 4 | 7 | 10 | 20 | 17 |

Average = 20 / 5 = **4.0**

| Time | Space |
|------|-------|
| O(n log n) | O(1) extra (in-place sort) |

This is optimal -- sorting is the bottleneck and we need at least O(n log n) to determine the order.

---

## COMPLEXITY -- INTUITIVELY
**O(n log n) time:** "Sorting dominates. The prefix sum pass is O(n)."
**O(1) space:** "We sort in-place and use only a few variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to use **floating point** division for the average.
2. Including the last process's burst time in the total wait (the last process's wait is its *waiting* time, not completion time).
3. Not sorting first -- the order matters for minimizing wait.

### Edge Cases to Test
- [ ] Single process --> average wait = 0
- [ ] All equal burst times --> order doesn't matter
- [ ] Already sorted --> same result, just no sorting needed
- [ ] Large burst times --> watch for integer overflow in total wait (use long)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "All processes arrive at time 0? Non-preemptive? Return average or total waiting time?"
2. **Greedy Proof:** "SJF is optimal because moving a shorter job before a longer one reduces the wait for the shorter job by `burst_long` but increases the wait for the longer job by `burst_short`. Since `burst_short < burst_long`, net change is negative (total wait decreases)."
3. **Code:** Sort + one loop with running sum.

### Follow-Up Questions
- "What if processes arrive at different times?" --> Need priority queue / preemptive SJF (SRTF). Much harder.
- "What about Round Robin or priority scheduling?" --> Different algorithms with different tradeoffs (fairness vs throughput).
- "Can you compute median waiting time?" --> Need to keep sorted order or use quickselect.

---

## CONNECTIONS
- **Prerequisite:** Sorting, prefix sums
- **Same Pattern:** Minimum platforms (sort arrivals/departures), job scheduling
- **This Unlocks:** Preemptive SJF (SRTF), Weighted Job Scheduling (DP)
- **Related:** Activity Selection, Meeting Rooms
