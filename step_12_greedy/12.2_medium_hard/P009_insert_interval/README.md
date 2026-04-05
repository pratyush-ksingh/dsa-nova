# Insert Interval

> **Step 12.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

You are given an array of **non-overlapping** intervals `intervals` where `intervals[i] = [start_i, end_i]`, sorted in ascending order by `start_i`.

You are also given a `newInterval = [start, end]` that you need to **insert** into `intervals`.

Insert `newInterval` into `intervals` such that `intervals` is still sorted in ascending order by `start_i` and has no overlapping intervals (merge overlapping if needed).

Return the resulting interval list.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `intervals=[[1,3],[6,9]], newInterval=[2,5]` | `[[1,5],[6,9]]` | [2,5] overlaps [1,3]; merge to [1,5] |
| `intervals=[[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval=[4,8]` | `[[1,2],[3,10],[12,16]]` | [4,8] overlaps [3,5],[6,7],[8,10]; merge all |
| `intervals=[], newInterval=[5,7]` | `[[5,7]]` | Empty input |
| `intervals=[[1,5]], newInterval=[2,3]` | `[[1,5]]` | newInterval fully inside existing |

## Constraints

- `0 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= start_i <= end_i <= 10^5`
- `intervals` is sorted by `start_i` and non-overlapping
- `newInterval.length == 2`
- `0 <= start <= end <= 10^5`

---

## Approach 1: Brute Force — Add, Sort, Merge

**Intuition:** Don't think about where to insert. Just throw `newInterval` into the list, re-sort everything by start time, and apply the standard merge-intervals algorithm. Simple but wastes the fact that input is already sorted.

**Steps:**
1. Append `newInterval` to `intervals`.
2. Sort the combined list by `interval[0]` (start time).
3. Initialize `result` with the first interval.
4. For each subsequent interval:
   - If its start <= last result's end: overlap, extend last result's end.
   - Else: no overlap, append as a new interval.
5. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) due to sort |
| Space  | O(n) |

---

## Approach 2: Optimal — Linear 3-Phase Scan

**Intuition:** Since the input is already sorted, we can process it in three distinct phases without sorting:
- **Phase 1:** Everything that ends before newInterval starts — copy directly.
- **Phase 2:** Everything that overlaps newInterval — merge into newInterval.
- **Phase 3:** Everything that starts after newInterval ends — copy directly.

**Steps:**
1. While `intervals[i][1] < newInterval[0]`: add to result (before).
2. While `intervals[i][0] <= newInterval[1]`: expand newInterval (overlap).
3. Append the expanded newInterval to result.
4. Append all remaining intervals (after).

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Approach 3: Best — Single-Pass with Insertion Flag

**Intuition:** Same logic as Approach 2 but as a clean single loop that classifies each interval into one of three cases. An `inserted` flag tracks whether `newInterval` has been added yet.

**Steps:**
1. For each existing interval:
   - If `interval[1] < newInterval[0]`: it ends before new starts → append as-is.
   - If `interval[0] > newInterval[1]`: it starts after new ends → insert newInterval (if not yet), then append interval.
   - Else: overlaps → expand newInterval: `start = min(starts)`, `end = max(ends)`.
2. If newInterval was never inserted (all existing intervals came before it), append it at the end.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## Real-World Use Case

**Calendar Event Insertion:** When a user adds a new meeting to their calendar, any overlapping meetings need to be merged into a single blocked time block. The "insert interval" operation is exactly what calendar applications perform — insert a new time block and merge with adjacent/overlapping appointments.

Also used in **network firewall rule management**: inserting a new IP range rule and merging with existing overlapping IP ranges to produce a clean, non-redundant ruleset.

## Interview Tips

- Clarify: "Are the input intervals guaranteed to be sorted and non-overlapping?" — yes for LeetCode 57.
- The brute force is O(n log n); knowing the input is sorted makes O(n) possible — mention this trade-off.
- The 3-phase approach is easiest to explain clearly in an interview. Walk through each phase with an example.
- Two overlap conditions to get right:
  - `interval[1] < newInterval[0]` means strictly BEFORE (no overlap)
  - `interval[0] > newInterval[1]` means strictly AFTER (no overlap)
  - Everything else overlaps
- Edge cases: empty input, newInterval before all, newInterval after all, newInterval fully inside one existing, newInterval engulfs all.
- This is a building block for Merge Intervals (LeetCode 56) — they share the same core merge logic.
