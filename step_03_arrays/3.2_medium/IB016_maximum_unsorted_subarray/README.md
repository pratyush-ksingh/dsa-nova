# Maximum Unsorted Subarray

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an integer array `A`, find the **shortest subarray** `[start, end]` (0-indexed, inclusive) such that sorting only `A[start..end]` makes the **entire array** sorted in non-decreasing order.

Return `[-1]` if the array is already sorted.

## Examples

| Input A                   | Output | Explanation |
|---------------------------|--------|-------------|
| [1, 3, 2, 4, 5]           | [1, 2] | Sort A[1..2] -> [1,2,3,4,5] |
| [1, 2, 3, 4, 5]           | [-1]   | Already sorted |
| [5, 4, 3, 2, 1]           | [0, 4] | Entire array must be sorted |
| [2, 6, 4, 8, 10, 9, 15]   | [1, 5] | Sort A[1..5]=[6,4,8,10,9] -> [4,6,8,9,10] |
| [2, 1]                    | [0, 1] | Swap needed |
| [1, 3, 5, 2, 6, 4, 8]     | [1, 5] | Sort A[1..5] to fix 3,5,2,6,4 |

## Constraints

- 1 <= n <= 10^5
- -10^9 <= A[i] <= 10^9

---

## Approach 1: Brute Force

**Intuition:** For every possible pair (i, j), sort the subarray A[i..j] in a copy of the array and check if the result equals `sorted(A)`. Track the shortest valid subarray found. Cubic time makes this impractical for n > 1000.

**Steps:**
1. Compute `sorted_A = sorted(A)`. If `A == sorted_A`, return `[-1]`.
2. For each pair `(i, j)` with `i < j`:
   a. Copy A, sort the subarray `A[i..j]` in the copy.
   b. If the copy equals `sorted_A`, record `[i, j]` as a candidate if it's shorter than the current best.
3. Return the shortest candidate.

| Metric | Value |
|--------|-------|
| Time   | O(n³) — O(n²) pairs * O(n log n) sort each |
| Space  | O(n) for copies |

---

## Approach 2: Optimal (Two-pass + expand)

**Intuition:** The unsorted subarray has two key properties:
1. Its leftmost element is the first place the array "goes wrong" (A[i] > A[i+1]).
2. Its rightmost element is the last place the array "goes wrong" (A[j] < A[j-1]).

But we also need to expand: any element to the left of `left` that is greater than `subarray_min` must also be included (it would be out of place). Similarly, any element to the right of `right` that is less than `subarray_max` must be included.

**Steps:**
1. Find `left` = first index where `A[i] > A[i+1]`. If none, return `[-1]`.
2. Find `right` = last index where `A[j] < A[j-1]`.
3. Compute `subMin = min(A[left..right])`, `subMax = max(A[left..right])`.
4. Expand `left` leftward while `A[left-1] > subMin`.
5. Expand `right` rightward while `A[right+1] < subMax`.
6. Return `[left, right]`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 3: Best (Running min/max scan)

**Intuition:** We can find the final boundaries without a separate expansion step:

- Scan **left to right** tracking the running maximum. Any element that is smaller than the running max so far is "out of order" and belongs in the unsorted region. The **last** such element is the right boundary.
- Scan **right to left** tracking the running minimum. Any element that is greater than the running min so far is "out of order". The **last** such element (from the right scan) is the left boundary.

This is equivalent to Optimal but expressed more elegantly in a single forward + backward pass with no expand step.

**Steps:**
1. Scan left to right: track `runMax`. Whenever `A[i] < runMax`, set `right = i`; else update `runMax = A[i]`.
2. If `right == -1`, return `[-1]`.
3. Scan right to left: track `runMin`. Whenever `A[i] > runMin`, set `left = i`; else update `runMin = A[i]`.
4. Return `[left, right]`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Database indexing / incremental sorting:** When data is mostly sorted but a batch of new insertions disrupts a small range, this algorithm identifies the minimal re-sort window. It's used in **version-controlled sorted structures** (like LSM trees) to find the "dirty" segment. Also used in **CI/CD pipeline tools** that check whether a changelog or version list remains in order — if not, they report exactly which entries need fixing.

## Interview Tips

- The Brute Force O(n^3) should be reduced to O(n^2) first (remove the sort: just check if subarray is sorted and matches the sorted array bounds) before jumping to O(n).
- The key insight for O(n): "the right boundary is the last position where an element is smaller than the running max from the left." Draw this on the board.
- Don't forget the expand step in Approach 2. A common mistake: taking `left` and `right` from the initial pass without expanding — this misses cases like `[3, 1, 2, 4, 5]` where the subarray min (1) is less than A[0]=3.
- Approach 3 (running min/max) subsumes the expand step implicitly — it's worth understanding why they are equivalent.
- Edge cases: single element, already sorted, reverse sorted (entire array is the answer).
- This problem appears as LeetCode 581 "Shortest Unsorted Continuous Subarray" — same problem, different name.
