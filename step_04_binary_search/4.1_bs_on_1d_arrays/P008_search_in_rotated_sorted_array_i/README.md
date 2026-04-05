# Search in Rotated Sorted Array I

> **LeetCode 33** | **Step 04 - Binary Search (1D Arrays)** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

There is an integer array `nums` sorted in ascending order (with **distinct** values). Prior to being passed to your function, `nums` is **possibly rotated** at an unknown pivot index `k` such that the array becomes `[nums[k], nums[k+1], ..., nums[n-1], nums[0], ..., nums[k-1]]`.

Given the rotated array `nums` and an integer `target`, return the index of `target` if it is in `nums`, or `-1` if it is not.

You must write an algorithm with **O(log n)** runtime complexity.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums=[4,5,6,7,0,1,2], target=0` | `4` | 0 is at index 4 |
| `nums=[4,5,6,7,0,1,2], target=3` | `-1` | 3 is not present |
| `nums=[1], target=0` | `-1` | Only element is 1 |
| `nums=[1,3], target=3` | `1` | 3 is at index 1 |

## Constraints

- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- All values of `nums` are **unique**.
- `nums` is an ascending array that is possibly rotated.
- `-10^4 <= target <= 10^4`

---

## Approach 1: Brute Force — Linear Scan

**Intuition:** Simply check every element. No cleverness needed; always correct.

**Steps:**
1. Iterate `i` from `0` to `n-1`.
2. If `nums[i] == target`, return `i`.
3. Return `-1`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 2: Optimal — Modified Binary Search

**Intuition:** A rotated sorted array always has one fully sorted half around any `mid` point. Identify that sorted half, check whether `target` falls within it, and discard the other half. This keeps the O(log n) guarantee.

**Key observation:** If `nums[lo] <= nums[mid]`, the left half `[lo, mid]` is sorted. Otherwise the right half `[mid, hi]` is sorted.

**Steps:**
1. `lo = 0`, `hi = n-1`.
2. While `lo <= hi`:
   a. `mid = (lo + hi) / 2`.
   b. If `nums[mid] == target`, return `mid`.
   c. If `nums[lo] <= nums[mid]` (left half sorted):
      - If `nums[lo] <= target < nums[mid]`: search left (`hi = mid-1`).
      - Else: search right (`lo = mid+1`).
   d. Else (right half sorted):
      - If `nums[mid] < target <= nums[hi]`: search right (`lo = mid+1`).
      - Else: search left (`hi = mid-1`).
3. Return `-1`.

| Metric | Value |
|--------|-------|
| Time   | O(log n) |
| Space  | O(1) |

---

## Approach 3: Best — Same Modified Binary Search (Clean Form)

**Intuition:** Identical algorithm to Approach 2; written with `lo + (hi - lo) / 2` to prevent integer overflow and formatted for clean interview presentation.

**Steps:** Same as Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O(log n) |
| Space  | O(1) |

---

## Real-World Use Case

**Circular buffer / ring buffer search:** Operating system kernels and network drivers store data in circular buffers (e.g., Linux kernel ring buffer). Searching for a specific timestamp or sequence number in such a structure is equivalent to searching in a rotated sorted array. Database systems also encounter this when searching in a circular log file where the oldest and newest entries wrap around.

## Interview Tips

- The critical insight is: "one half is always sorted." State this clearly before coding.
- Be precise with the boundary conditions: `nums[lo] <= target < nums[mid]` (strict `<` on the right because `mid` is already checked).
- This problem has no duplicates; if duplicates are added (LC 81), the `nums[lo] == nums[mid] == nums[hi]` case forces a linear-step fallback — know both variants.
- Use `lo + (hi - lo) / 2` instead of `(lo + hi) / 2` in Java/C++ to avoid integer overflow for large arrays.
- Follow-up: "find the minimum in a rotated sorted array" (LC 153) uses similar sorted-half logic.
