# Merge Intervals

> **Step 12.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an array of `intervals` where `intervals[i] = [start_i, end_i]`, merge all **overlapping intervals** and return an array of the non-overlapping intervals that cover all the input intervals.

Two intervals `[a, b]` and `[c, d]` overlap if `a <= d` and `c <= b`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[1,3],[2,6],[8,10],[15,18]]` | `[[1,6],[8,10],[15,18]]` | [1,3] and [2,6] overlap → [1,6] |
| `[[1,4],[4,5]]` | `[[1,5]]` | Touching at 4 counts as overlap |
| `[[1,4],[2,3]]` | `[[1,4]]` | [2,3] is fully inside [1,4] |
| `[[1,4],[0,2],[3,5]]` | `[[0,5]]` | All three merge into one |

## Constraints

- `1 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= start_i <= end_i <= 10^4`

---

## Approach 1: Brute Force — Repeated Nested Merge

**Intuition:** Keep scanning all pairs of intervals and merge any two that overlap, repeating until no more merges are possible. Correct but very slow for large inputs.

**Steps:**
1. Repeat until no changes in a full pass:
   a. For every unmerged pair `(i, j)`, check if they overlap (`a[0] <= b[1] && b[0] <= a[1]`).
   b. If yes, replace both with their merged interval `[min(starts), max(ends)]`.
2. Sort result by start time.
3. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n²) per pass × multiple passes in worst case |
| Space  | O(n) |

---

## Approach 2: Optimal — Sort + Single Pass

**Intuition:** Once intervals are sorted by start time, any overlapping intervals must be **adjacent** in the sorted order. So one linear pass suffices after sorting.

If the current interval's start is ≤ the last merged interval's end, they overlap — extend the last interval's end. Otherwise, start a new merged interval.

**Steps:**
1. Sort `intervals` by `start_i`.
2. Initialize `result` with a copy of the first interval.
3. For each subsequent interval `[start, end]`:
   - If `start <= result[-1][1]`: overlap → update `result[-1][1] = max(result[-1][1], end)`.
   - Else: no overlap → append `[start, end]` to result.
4. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Approach 3: Best — Sort + Explicit Window Variables

**Intuition:** Same algorithm as Approach 2, but track the current merge window as explicit variables `[curStart, curEnd]` instead of mutating the last result element. This avoids indexing into the result list and is slightly cleaner to read.

**Steps:**
1. Sort intervals.
2. Initialize `curStart, curEnd = intervals[0]`.
3. For each subsequent interval:
   - If `start <= curEnd`: extend `curEnd = max(curEnd, end)`.
   - Else: flush `[curStart, curEnd]` to result; update `curStart, curEnd = start, end`.
4. Flush the final window to result.
5. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) for output; O(1) extra working space |

---

## Real-World Use Case

**Meeting Room Scheduling / Calendar Consolidation:** Given a list of busy time slots from multiple calendar sources, merge all overlapping slots to find a single list of "busy periods". This is used in Google Calendar, Outlook, and any scheduling tool that shows combined availability across multiple accounts.

**Network Packet Reassembly:** TCP/IP protocols receive packet fragments with byte ranges `[start, end]`. Merging overlapping byte ranges tells you which contiguous data blocks have been fully received — exactly the merge intervals algorithm.

## Interview Tips

- The key insight: after sorting by start, overlapping intervals are always adjacent. This reduces O(n²) to O(n log n).
- Sorting is the expensive step; the merge pass is O(n). So sorting dominates.
- Overlap condition: `current.start <= last.end` (not strictly less — touching counts as overlap per LeetCode definition).
- When merging, always take `max(last.end, current.end)` — not just `current.end` — because the current interval might be fully inside the last one.
- Edge cases: single interval (no merges needed), all intervals merge into one, no intervals overlap at all.
- Common mistake: forgetting to append the last active interval after the loop ends.
- Related problem: Insert Interval (LeetCode 57) — same merge logic but with a targeted insertion step first.
- Follow-up: "Can you do it without sorting?" — only possible if there's a special structure (e.g., intervals in a specific domain like timestamps, allowing bucket/radix approach).
