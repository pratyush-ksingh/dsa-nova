# Find Minimum in Rotated Sorted Array

> **Step 04 | 4.1** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 153 | **Status:** UNSOLVED

## Problem Statement

You are given a sorted array of **distinct** integers that has been rotated between 1 and n times.
Return the **minimum element** of this array.

You must write an algorithm that runs in **O(log n)** time.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3,4,5,1,2]` | `1` | Rotated at index 3; minimum is 1 |
| `[4,5,6,7,0,1,2]` | `0` | Rotated at index 4; minimum is 0 |
| `[11,13,15,17]` | `11` | Not rotated (or rotated n times); already sorted |
| `[2,1]` | `1` | Single rotation |

## Constraints

- `n == nums.length`
- `1 <= n <= 5000`
- `-5000 <= nums[i] <= 5000`
- All integers in `nums` are **unique**
- `nums` is a sorted array rotated between 1 and n times

---

## Approach 1: Brute Force

**Intuition:** Scan every element and track the running minimum. No cleverness needed — just look at everything.

**Steps:**
1. Initialise `min = nums[0]`.
2. Iterate through every element in the array.
3. Update `min` whenever a smaller value is found.
4. Return `min`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 2: Optimal — Binary Search on Right Boundary

**Intuition:** A rotated sorted array has a single "dip" — the pivot where the sequence resets.
At any midpoint, compare `nums[mid]` with `nums[right]`:

- If `nums[mid] > nums[right]`: the left half is sorted and entirely greater than the right half. The minimum must be somewhere to the right of `mid` → shrink window to `[mid+1, right]`.
- Otherwise (`nums[mid] <= nums[right]`): the right half is sorted. `mid` itself could be the minimum → shrink to `[left, mid]`.

When `left == right`, that index holds the minimum.

**Steps:**
1. Set `left = 0`, `right = n - 1`.
2. While `left < right`:
   - Compute `mid = left + (right - left) / 2`.
   - If `nums[mid] > nums[right]`: `left = mid + 1`.
   - Else: `right = mid`.
3. Return `nums[left]`.

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Approach 3: Best — Binary Search + Early Exit

**Intuition:** Same as Optimal. Add one micro-optimisation: if at any point `nums[left] < nums[right]`, the entire current window is already sorted, so `nums[left]` is the minimum — return immediately without further iterations.

**Steps:**
1. Set `left = 0`, `right = n - 1`.
2. While `left < right`:
   - **Early exit:** if `nums[left] < nums[right]` return `nums[left]`.
   - Compute `mid`.
   - If `nums[mid] > nums[right]`: `left = mid + 1`.
   - Else: `right = mid`.
3. Return `nums[left]`.

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Real-World Use Case

**Database index recovery / circular buffer reads.** Many systems store time-series data in a circular ring buffer (e.g., network packet logs, stock tick data). When the buffer wraps around, the oldest (minimum timestamp) entry is exactly the "rotation point." Binary search on the ring finds it in O(log n) rather than scanning the whole buffer — critical for high-frequency trading systems ingesting millions of ticks per second.

## Interview Tips

- The problem guarantees **no duplicates** — this is why the binary search on the right boundary always works. With duplicates (LC 154), `nums[mid] == nums[right]` is ambiguous and you must shrink `right--` defensively, degrading to O(n) in the worst case.
- Always use `right = mid` (not `mid - 1`) when keeping `mid` as a candidate; using `mid - 1` would skip the minimum.
- The invariant: **the minimum is always in the window `[left, right]`** — the loop contracts this window until it collapses to one element.
- Edge case: a fully sorted (non-rotated) array is handled correctly because `nums[mid] <= nums[right]` always, pushing `right` leftward until `left == 0`.
- Interviewers often ask follow-up: "What if duplicates exist?" — be ready to discuss LC 154.
