# Search in Rotated Sorted Array II

> **LeetCode 81** | **Step 04 - Binary Search (1D Arrays)** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

There is an integer array `nums` sorted in ascending order (possibly with **duplicate** values). Prior to being passed to your function, `nums` is **possibly rotated** at an unknown pivot index `k`.

Given the possibly rotated array `nums` and an integer `target`, return `true` if `target` is in `nums`, or `false` otherwise.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums=[2,5,6,0,0,1,2], target=0` | `true` | 0 exists at indices 3 and 4 |
| `nums=[2,5,6,0,0,1,2], target=3` | `false` | 3 is not present |
| `nums=[1,0,1,1,1], target=0` | `true` | 0 exists at index 1 |
| `nums=[1,1,1,1,1], target=2` | `false` | Only 1s, no 2 |

## Constraints

- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- `nums` is an ascending array that is possibly rotated with possible duplicates.
- `-10^4 <= target <= 10^4`

---

## Approach 1: Brute Force — Linear Scan

**Intuition:** Iterate every element. Duplicates are irrelevant.

**Steps:**
1. Iterate through `nums`.
2. Return `true` if any element equals `target`.
3. Return `false`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 2: Optimal — Binary Search with Duplicate Handling

**Intuition:** Extend LC-33's binary search. The only new case that duplicates introduce is when `nums[lo] == nums[mid] == nums[hi]` — at this point we cannot determine which half is sorted. The safe move is to shrink both boundaries by one (`lo++`, `hi--`). All other cases are identical to LC-33.

**Steps:**
1. `lo = 0`, `hi = n-1`.
2. While `lo <= hi`:
   a. `mid = (lo + hi) / 2`.
   b. If `nums[mid] == target`, return `true`.
   c. If `nums[lo] == nums[mid] == nums[hi]`: `lo++`, `hi--` and continue.
   d. Else if `nums[lo] <= nums[mid]` (left half sorted):
      - If `nums[lo] <= target < nums[mid]`: `hi = mid-1`.
      - Else: `lo = mid+1`.
   e. Else (right half sorted):
      - If `nums[mid] < target <= nums[hi]`: `lo = mid+1`.
      - Else: `hi = mid-1`.
3. Return `false`.

| Metric | Value |
|--------|-------|
| Time   | O(log n) average; O(n) worst case (all identical elements) |
| Space  | O(1) |

---

## Approach 3: Best — Same Algorithm with Clear Comments

**Intuition:** Identical to Approach 2. Written with `lo + (hi - lo) / 2` overflow-safe mid and inline comments labelling each branch to make the decision tree explicit during a live interview.

**Steps:** Same as Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O(log n) average; O(n) worst |
| Space  | O(1) |

---

## Real-World Use Case

**Fault-tolerant distributed log search:** In distributed systems (e.g., Apache Kafka), partition logs can contain repeated sequence numbers during recovery (a broker crashes and re-sends messages). Searching whether a specific message ID exists in such a log corresponds exactly to searching in a rotated array with duplicates. The binary-search approach reduces the number of log segments scanned from O(n) to O(log n) in the common case, with graceful degradation to O(n) only when there are many consecutive identical sequence numbers.

## Interview Tips

- Always mention that this is the same as LC-33 with one extra case: the ambiguous `lo == mid == hi` boundary that forces linear shrinking.
- Worst case O(n) occurs only when all (or many) elements are identical, e.g., `[1,1,1,1,1]`. This is worth stating explicitly.
- The problem asks for `true`/`false` (existence), not the index — so no need to track position.
- Follow-up: "Find minimum in rotated sorted array II" (LC 154) uses the identical duplicate-handling trick.
- A common mistake is checking `nums[lo] == nums[mid]` without also checking `nums[hi]`; you need all three equal to be in the truly ambiguous case.
