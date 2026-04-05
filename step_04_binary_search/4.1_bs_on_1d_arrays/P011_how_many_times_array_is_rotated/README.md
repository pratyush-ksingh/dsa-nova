# How Many Times Array is Rotated

> **Step 04 | 4.1** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

A sorted array (in strictly ascending order with all distinct elements) has been rotated to the right **k** times. Given the resulting rotated array, find **k** — the number of rotations.

A "right rotation by 1" moves the last element to the front: `[1,2,3,4,5]` → `[5,1,2,3,4]`.

**Key observation:** The number of rotations equals the **index of the minimum element** in the rotated array. If not rotated, the minimum is at index 0.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [4,5,6,7,0,1,2] | 4 | Minimum is 0 at index 4; original was [0,1,2,4,5,6,7] rotated 4 times |
| [3,4,5,1,2] | 3 | Minimum is 1 at index 3 |
| [1,2,3,4,5] | 0 | Not rotated; minimum at index 0 |
| [2] | 0 | Single element |
| [2,1] | 1 | Minimum at index 1 |

## Constraints

- 1 <= nums.length <= 5000
- -5000 <= nums[i] <= 5000
- All elements are **distinct**
- The array was originally sorted in **strictly ascending** order

---

## Approach 1: Brute Force

**Intuition:** Simply scan the entire array linearly to find the index of the minimum element. The minimum element is where the sorted order "breaks" — it comes after a larger element. Its index is exactly the number of rotations.

**Steps:**
1. Initialize `min_val = nums[0]`, `min_idx = 0`
2. For each index i from 1 to n-1:
   - If `nums[i] < min_val`: update `min_val = nums[i]`, `min_idx = i`
3. Return `min_idx`

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 2: Optimal — Binary Search for the Minimum

**Intuition:** In a rotated sorted array, exactly one of the two halves defined by any midpoint is always fully sorted. The minimum element is in the **unsorted** (rotated) half. Compare `nums[mid]` to `nums[hi]`:
- If `nums[mid] > nums[hi]`: the left half [lo..mid] is sorted, and the minimum is in the right half (mid, hi]. Move `lo = mid + 1`.
- Otherwise: the right half [mid..hi] is sorted, and the minimum is in the left half [lo, mid]. Move `hi = mid`.

When `lo == hi`, both pointers point to the minimum element.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`
2. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] > nums[hi]`: `lo = mid + 1`
   - Else: `hi = mid`
3. Return `lo` (= index of minimum = number of rotations)

| Metric | Value |
|--------|-------|
| Time   | O(log n) |
| Space  | O(1) |

---

## Approach 3: Best — Binary Search with Edge Case Handling

**Intuition:** Same binary search as Optimal, with one added optimization: if `nums[lo] <= nums[hi]`, the array is already sorted (not rotated or fully unbroken), so return 0 immediately. This early exit is an O(1) check that avoids even entering the loop for the common case of non-rotated arrays.

**Steps:**
1. If `nums[0] <= nums[n-1]`, return 0 immediately (array is sorted, 0 rotations)
2. Binary search: `lo = 0`, `hi = n - 1`
3. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] > nums[hi]`: `lo = mid + 1` (min in right half)
   - Else: `hi = mid` (min in left half including mid)
4. Return `lo`

| Metric | Value |
|--------|-------|
| Time   | O(log n) |
| Space  | O(1) |

---

## Real-World Use Case

**Circular Buffer Read Pointer:** Operating system circular buffers (ring buffers) store data in a fixed array with a rotating write pointer. When a crash dump is analyzed, determining how many times the buffer has "wrapped around" is equivalent to finding the minimum element's index — the point where the oldest data begins. This is the exact same algorithm used in OS kernel debugging tools.

**Log Rotation Analysis:** Server logs are often stored in rotating files. Detecting the oldest log entry in a rotated log array uses the same minimum-finding binary search.

## Interview Tips

- Clearly state the key insight upfront: "The number of rotations equals the index of the minimum element."
- Always compare `nums[mid]` with `nums[hi]`, not `nums[lo]`. Comparing with `lo` creates an edge case when `lo == mid` (2-element array) that causes an infinite loop.
- The "not rotated" case (`nums[0] < nums[n-1]`) returns 0 naturally from the binary search — no special case needed. But adding an explicit early exit (Approach 3) is a nice touch.
- This problem is closely related to LeetCode 153 (Find Minimum in Rotated Sorted Array) and 154 (with duplicates). Know all three.
- If the array can have **duplicates** (LeetCode 154), the binary search degrades to O(n) in the worst case — you must handle `nums[mid] == nums[hi]` by simply doing `hi--`.
- A common mistake: returning `nums[lo]` (the minimum value) instead of `lo` (the index = rotation count).
