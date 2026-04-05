# Single Element in Sorted Array

> **Step 04.1** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 540

## Problem Statement

You are given a sorted array `nums` where every element appears exactly **twice**, except for one element which appears **exactly once**. Return the single element.

You must implement a solution with `O(log n)` runtime complexity and `O(1)` space.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,1,2,3,3,4,4,8,8]` | `2` | 2 appears only once |
| `[3,3,7,7,10,11,11]` | `10` | 10 appears only once |
| `[1]` | `1` | Only element |

## Constraints

- `1 <= nums.length <= 10^5`
- `0 <= nums[i] <= 10^5`
- `nums` is sorted in non-decreasing order
- Every element appears twice except for exactly one

---

## Approach 1: Brute Force (XOR)

**Intuition:** XOR has two useful properties: `a ^ a = 0` and `0 ^ x = x`. If you XOR all elements, every duplicate pair cancels to 0, and the single element remains.

**Steps:**
1. Initialize `result = 0`.
2. XOR every element in the array with `result`.
3. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 2: Optimal (Binary Search on Even Index Parity)

**Intuition:** In a sorted array where all elements are paired, pairs occupy index positions `(0,1), (2,3), (4,5), ...`. Before the single element, `nums[even] == nums[even+1]`. After the single element, this relationship breaks — `nums[even] != nums[even+1]`. Binary search on this property.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo < hi`:
   - Compute `mid`. If `mid` is odd, decrement it to make it even.
   - If `nums[mid] == nums[mid+1]`, the pair is intact → single element is to the right → `lo = mid + 2`.
   - Else, the pair is broken → single element is at `mid` or left → `hi = mid`.
3. Return `nums[lo]`.

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Approach 3: Best (Binary Search with XOR Partner Trick)

**Intuition:** Identical logic to Approach 2, but uses `mid ^ 1` to find the partner index without a branch. `mid ^ 1` flips the last bit: even `mid` → `mid+1` (right partner); odd `mid` → `mid-1` (left partner). If `nums[mid] == nums[mid ^ 1]`, the pair is intact and we move right; otherwise we move left.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo < hi`:
   - Compute `mid`.
   - If `nums[mid] == nums[mid ^ 1]` → `lo = mid + 1`.
   - Else → `hi = mid`.
3. Return `nums[lo]`.

| Metric | Value    |
|--------|----------|
| Time   | O(log n) |
| Space  | O(1)     |

---

## Real-World Use Case

**Data integrity / deduplication audits:** Suppose a log storage system guarantees every event ID is recorded exactly twice (for redundancy). If one backup is lost, you need to find the un-backed-up event ID quickly among millions of records — binary search on the sorted ID list finds it in O(log n).

## Interview Tips

- The XOR trick is a great opener to show you know bit manipulation, but always follow up with the O(log n) answer since the problem asks for it explicitly.
- The key insight is **parity of the index** — draw out a small example on paper (e.g., `[1,1,2,3,3]`) and label indices to visualize why even-index parity flips after the single element.
- The `mid ^ 1` trick is elegant and worth mentioning — interviewers love clean bitwise tricks.
- Edge cases: single-element array, single element at the very start or very end.
- Always verify: if `nums.length` is even the input is invalid (single element makes length always odd).
