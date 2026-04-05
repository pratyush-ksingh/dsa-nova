# K Largest Elements

> **Step 11 - 11.2 Medium Heaps** | **InterviewBit** | **Difficulty:** EASY | **XP:** 10 | **Status:** UNSOLVED

## Problem Statement

Given an array of `N` integers and a number `k`, find the **k largest elements** from the array and return them in **descending order**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [3,1,4,1,5,9,2,6]`, `k = 3` | `[9, 6, 5]` | Three largest are 9, 6, 5 |
| `nums = [1,2,3,4,5]`, `k = 2` | `[5, 4]` | Two largest |
| `nums = [7,7,7]`, `k = 2` | `[7, 7]` | Duplicates are allowed |

## Constraints

- `1 <= k <= N <= 10^5`
- `-10^9 <= nums[i] <= 10^9`

---

## Approach 1: Brute Force — Full Sort

**Intuition:** Sort the entire array in descending order and slice the first `k` elements. The simplest correct solution; wastes O(n log n) sorting elements beyond position k.

**Steps:**
1. Sort `nums` in descending order.
2. Return `nums[0:k]`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) for sort output |

---

## Approach 2: Optimal — Min-Heap of Size k

**Intuition:** We only need to track the top k elements. Maintain a min-heap capped at size k. For each new element, push it onto the heap. If the heap exceeds k, pop the minimum. After processing all elements, the heap contains exactly the k largest.

**Steps:**
1. Initialize an empty min-heap.
2. For each `num` in `nums`: push `num`. If `heap.size() > k`, pop the minimum.
3. Convert heap to sorted descending list.
4. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n log k) |
| Space  | O(k) |

---

## Approach 3: Best — Quickselect

**Intuition:** Quickselect is the selection variant of Quicksort. After one partition step, the pivot lands in its correct sorted position. We recurse only on the half containing the target index, achieving O(n) average. Here the target is index `n - k` (the start of the top-k partition).

**Steps:**
1. Clone the array. Choose a random pivot index. Partition using Lomuto scheme.
2. If pivot index == `n - k`, stop. If less, recurse right half; otherwise left half.
3. Elements at indices `[n-k, n-1]` are the k largest (unsorted among themselves).
4. Sort those k elements descending and return.

| Metric | Value |
|--------|-------|
| Time   | O(n) average, O(n^2) worst |
| Space  | O(1) in-place (plus output array) |

---

## Real-World Use Case

**Real-time leaderboard (gaming / e-commerce):** A live game server processes millions of score updates per second. To display the top-k players, a min-heap of size k is maintained. Each update is O(log k), far cheaper than re-sorting the full user base.

## Interview Tips

- For a static array with one query, Quickselect (O(n) avg) beats the heap. For streaming data or repeated queries, the heap wins.
- Random pivot in Quickselect is essential — adversarial inputs can cause O(n^2) with fixed pivots.
- Python's `heapq` is a min-heap; negate values to simulate a max-heap, or use `heapq.nlargest(k, nums)` which uses a heap internally.
- The heap approach is preferred in interviews for its clear O(n log k) guarantee and simpler code. Mention Quickselect as the O(n) follow-up.
- Know `Arrays.sort` with a custom comparator for k-largest in Java; `sorted(reverse=True)[:k]` in Python for brute force.
