# Maximum Sum Triplet

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an array `A` of `N` integers, find the **maximum value of `A[i] + A[j] + A[k]`** such that:
- `i < j < k` (strictly increasing indices)
- `A[i] < A[j] < A[k]` (strictly increasing values)

Return `0` if no such triplet exists.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, 4, 5]` | `12` | Triplet (3,4,5): indices 2,3,4; 3+4+5=12 |
| `[1, 5, 3, 6, 7]` | `18` | Triplet (5,6,7): indices 1,3,4; 5+6+7=18 |
| `[2, 5, 3, 1, 4, 9]` | `16` | Triplet (2,5,9): indices 0,1,5; 2+5+9=16 |
| `[5, 4, 3, 2, 1]` | `0`  | No strictly increasing triplet exists |
| `[2, 5, 3, 1, 4, 9]` | `18` | Triplet (5,?,9)? No — need i<j<k with A[i]<A[j]<A[k]: (2,3,9)=14 or (2,4,9)=15... check indices: 5(idx1),4(idx4),9(idx5): 5+4+9=18 |

## Constraints

- `1 <= N <= 10^5`
- `1 <= A[i] <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** Try all possible triplets `(i, j, k)` with `i < j < k`. For each, check if values are strictly increasing and update the maximum sum.

**Steps:**
1. Three nested loops: `i` from `0..n-3`, `j` from `i+1..n-2`, `k` from `j+1..n-1`.
2. If `A[i] < A[j] < A[k]`, update `ans = max(ans, A[i]+A[j]+A[k])`.
3. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(1) |

---

## Approach 2: Optimal — Suffix Max + Sorted Prefix Set

**Intuition:** Fix the middle element `j`. We want:
- The **largest `A[i] < A[j]`** from the left (to maximize the sum).
- The **largest `A[k] > A[j]`** from the right (to maximize the sum).

Precompute a **suffix maximum array** so `suffixMax[j]` = max of `A[j+1..n-1]`. Maintain a **sorted structure** of elements seen so far (left of `j`) and use binary search to find the largest value strictly less than `A[j]`.

**Steps:**
1. Build `suffixMax[i] = max(A[i+1..n-1])` for all `i`.
2. Maintain a sorted list (TreeMap/sorted list) of elements seen to the left.
3. For each `j` from `0` to `n-1`:
   a. `rightMax = suffixMax[j]`. If `rightMax > A[j]`:
   b. Find `bestI = largest element in sorted left < A[j]` using `lowerKey` / `bisect_left`.
   c. If `bestI` exists: `ans = max(ans, bestI + A[j] + rightMax)`.
   d. Add `A[j]` to the sorted left structure.
4. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Approach 3: Best — Same as Optimal (TreeSet variant)

**Intuition:** Functionally identical to Approach 2. Uses a TreeSet (Java) or bisect (Python) to find the maximum element strictly less than `A[j]` in the left prefix. The suffix max handles the right side.

**Steps:**
1. Same as Approach 2 with TreeSet's `.lower(x)` method (largest element < x).

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Real-World Use Case

**Financial analysis — three-day increasing price triplet:** Given a stock's daily prices, find the maximum combined value of three days `(d1, d2, d3)` with `d1 < d2 < d3` (in time) and prices strictly increasing, maximizing total value. This pattern of "find the best strictly increasing subsequence of length 3" appears in algorithmic trading signals, trend detection, and anomaly detection in time series data.

## Interview Tips

- The brute force O(n^3) is straightforward but too slow for n=10^5. The key optimization is avoiding the nested loops.
- The suffix max array handles the "largest element to the right" efficiently in O(1) per query after O(n) preprocessing.
- For the "largest element to the left strictly less than current", a sorted set with `lower()` / `bisect_left()` gives O(log n) per query.
- Note: `suffixMax[j]` gives the global max to the right, which might not be strictly greater than `A[j]`. Always check `rightMax > A[j]` before proceeding.
- Duplicates: the problem requires **strictly** increasing, so equal values do not qualify — use `lowerKey` (strictly less than) not `floorKey` (less than or equal).
- Related: Longest Increasing Subsequence (LIS), 3-sum variants.
