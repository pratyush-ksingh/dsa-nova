# Find Peak Element

> **Step 04.1** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 162

## Problem Statement

A peak element is an element that is strictly greater than its neighbors. Given a 0-indexed integer array `nums`, find a peak element and return its index. If the array contains multiple peaks, return the index of **any** peak.

You may imagine `nums[-1] = nums[n] = -∞`. In other words, an element is always considered a neighbor of a virtual negative infinity at both ends.

You must write an algorithm that runs in `O(log n)` time.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3,1]` | `2` | `nums[2] = 3` is a peak (3 > 2 and 3 > 1) |
| `[1,2,1,3,5,6,4]` | `5` | `nums[5] = 6` is a peak; `nums[1] = 2` is also valid |
| `[1]` | `0` | Only element, both neighbors are -∞ |
| `[2,1]` | `0` | `nums[0] = 2 > nums[1] = 1`, and left boundary is -∞ |

## Constraints

- `1 <= nums.length <= 1000`
- `-2^31 <= nums[i] <= 2^31 - 1`
- `nums[i] != nums[i+1]` for all valid `i` (no adjacent equal elements)

---

## Approach 1: Brute Force (Linear Scan)

**Intuition:** Iterate through the array. The first element that is strictly greater than both its neighbors (with boundaries treated as -∞) is a valid peak.

**Steps:**
1. For each index `i` from `0` to `n-1`:
   - `leftOk`  = `i == 0` OR `nums[i] > nums[i-1]`
   - `rightOk` = `i == n-1` OR `nums[i] > nums[i+1]`
   - If both are true, return `i`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 2: Optimal (Binary Search - Move Toward Rising Side)

**Intuition:** If `nums[mid] < nums[mid+1]`, the slope is rising to the right. Because the right boundary is -∞, the array must eventually come back down — and wherever it peaks is a valid answer. So a peak **must** exist in `[mid+1, hi]`. Conversely if `nums[mid] >= nums[mid+1]`, a peak exists in `[lo, mid]`. Binary search on this invariant converges to a peak.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo < hi`:
   - Compute `mid = lo + (hi - lo) / 2`.
   - If `nums[mid] < nums[mid+1]` → `lo = mid + 1`.
   - Else → `hi = mid`.
3. Return `lo` (which equals `hi`).

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Approach 3: Best (Same Binary Search - Explicit Direction)

**Intuition:** Identical correctness argument as Approach 2 — always move toward the greater neighbor. The invariant "a peak exists in [lo, hi]" is maintained throughout. When `lo == hi`, the invariant guarantees it is a peak.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo < hi`:
   - Compute `mid`.
   - If `nums[mid] > nums[mid+1]` → `hi = mid` (peak at mid or left).
   - Else → `lo = mid + 1` (peak must be right).
3. Return `lo`.

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Real-World Use Case

**Signal processing / local extrema detection:** In audio or sensor data analysis, finding local peaks (e.g., heartbeat peaks in an ECG, peaks in a frequency spectrum) is a common task. When data is large (millions of samples) and the requirement is to find any local maximum quickly (not necessarily the global one), binary search on local slope gives O(log n) detection — essential for real-time systems.

## Interview Tips

- The key insight that trips people up: you don't need to find the **global** maximum — any local peak is valid. This is what makes binary search applicable even though the array isn't globally sorted.
- The invariant is: "a peak always exists in [lo, hi]." Prove this to yourself before coding.
- The problem guarantees `nums[i] != nums[i+1]`, which eliminates plateaus and makes the binary search unambiguous.
- Edge cases: single-element array, strictly ascending array (peak at last index), strictly descending array (peak at first index).
- Follow-up: "What if the array can have equal adjacent elements (plateaus)?" — this breaks the simple binary search; you'd need a different approach.
