# Non Overlapping Intervals

> **Step 12 - 12.2 Medium/Hard Greedy** | **LeetCode 435** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an array of intervals `intervals` where `intervals[i] = [start_i, end_i]`, return the **minimum number of intervals you need to remove** to make the rest of the intervals **non-overlapping**.

Two intervals `[a, b]` and `[c, d]` overlap if `c < b` (i.e., they share more than an endpoint; touching at `b == c` is allowed).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[1,2],[2,3],[3,4],[1,3]]` | `1` | Remove `[1,3]`; remaining `[1,2],[2,3],[3,4]` are non-overlapping |
| `[[1,2],[1,2],[1,2]]` | `2` | Keep one `[1,2]`, remove the other two |
| `[[1,2],[2,3]]` | `0` | Already non-overlapping (they touch but don't overlap) |
| `[[1,100],[11,22],[1,11],[2,12]]` | `2` | Keep `[1,11]` and `[11,22]` (or similar) |

## Constraints

- `1 <= intervals.length <= 10^5`
- `intervals[i].length == 2`
- `-5 * 10^4 <= start_i < end_i <= 5 * 10^4`

---

## Approach 1: Brute Force — Try All Subsets

**Intuition:** Try every possible subset of intervals and check if it is non-overlapping. The maximum-sized valid subset tells us how many to keep; the answer is total minus kept.

**Steps:**
1. Enumerate all 2^n subsets.
2. For each subset, sort by start and verify no two intervals overlap.
3. Track `max_kept` = size of the largest non-overlapping subset.
4. Return `n - max_kept`.

| Metric | Value |
|--------|-------|
| Time   | O(2^n * n) |
| Space  | O(n)       |

---

## Approach 2: Optimal — Greedy, Sort by End Time

**Intuition:** This is the classic **Activity Selection Problem**. The greedy insight: among all intervals that don't overlap with previously kept ones, always keep the one that **ends earliest**. An earlier end leaves maximum room for future intervals. This is provable by an exchange argument: any optimal solution can be transformed into the greedy solution without removing more intervals.

**Steps:**
1. If empty, return 0.
2. Sort intervals by end time ascending.
3. Initialize `last_end = -infinity`, `kept = 0`.
4. For each interval `[start, end]`: if `start >= last_end`, increment `kept`, update `last_end = end`.
5. Return `n - kept`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) |

---

## Approach 3: Best — Greedy, Count Removals Directly

**Intuition:** Same algorithm, slightly different framing. Instead of counting kept intervals, count removed ones. On an overlap, increment removals and discard the interval with the larger end (which is the current one, since we sorted by end). On no overlap, advance `last_end`.

**Steps:**
1. Sort by end time ascending.
2. For each interval `[start, end]`:
   - If `start < last_end`: `removals++` (drop current — it ends later).
   - Else: `last_end = end` (keep current).
3. Return `removals`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) |

---

## Real-World Use Case

**Meeting room scheduling:** Given a list of proposed meetings (each with a start/end time), find the minimum number of meetings to cancel so that no two meetings overlap — allowing a single conference room to host all remaining meetings. This is a direct application of the non-overlapping intervals greedy algorithm.

## Interview Tips

- Immediately state the equivalence: "minimum removals = total - maximum non-overlapping set" and connect to Activity Selection — this shows depth.
- **Sort by end time** (not start time). Sorting by start leads to suboptimal choices. Common mistake.
- The greedy proof uses an exchange argument: suppose an optimal solution doesn't pick the earliest-ending interval. Swap it in — the solution remains valid and is no worse.
- Edge cases: empty array (return 0), all intervals identical (return n-1), touching intervals `[1,2],[2,3]` (not overlapping — return 0).
- Related problems: LeetCode 452 (minimum arrows to burst balloons) is essentially the same greedy; LeetCode 56 (merge intervals) is the complement problem.
