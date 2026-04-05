# Sort a Nearly Sorted Array

> **Step 11 | 11.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array of `n` elements where each element is at most `k` positions away from its target position in the sorted array, sort the array efficiently.

An array is "k-sorted" if for every element at index `i` in the unsorted array, its correct position in the sorted array is in the range `[i-k, i+k]`.

**Constraints:**
- `1 <= n <= 10^6`
- `0 <= k <= n`
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [6,5,3,2,8,10,9], k = 3` | `[2,3,5,6,8,9,10]` | Each element is at most 3 positions from its sorted position |
| `arr = [10,9,8,7,4,70,60,50], k = 4` | `[4,7,8,9,10,50,60,70]` | Each element is at most 4 positions from sorted |
| `arr = [1,2,3,4,5], k = 0` | `[1,2,3,4,5]` | Already sorted (k=0) |

### Real-Life Analogy
> *Imagine a nearly-sorted conveyor belt of packages at a warehouse. Each package is roughly in the right spot but might be off by at most k positions. Instead of re-sorting the entire belt (expensive), you only need to look at a small window of k+1 packages at a time -- pick the lightest from that window, place it, and slide the window forward. This is exactly the min-heap approach.*

### Key Observations
1. The element that belongs at position `i` must be somewhere in `arr[i-k..i+k]`. In particular, it must be among the first `k+1` unplaced elements.
2. A min-heap of size `k+1` always contains the next element to be placed.
3. Each element enters and leaves the heap exactly once: O(n log k) total.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **Min-Heap (Priority Queue)** gives O(log k) extraction of the minimum from a window of k+1 elements.
- This exploits the "nearly sorted" property that a general comparison sort cannot.

### Pattern Recognition
- **Pattern:** Heap for streaming/windowed minimum
- **Classification Cue:** "Whenever you see _k-sorted_ or _each element at most k positions away_ --> think _min-heap of size k+1_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Regular Sort
**Idea:** Ignore the k-sorted property entirely and use a standard sort.

**Steps:**
1. Call `sort(arr)`.
2. Return sorted array.

**Why it works:** Sorting always produces the correct result.

**BUD Transition -- Bottleneck:** O(n log n) does not exploit the k-sorted property. We can do O(n log k) with a heap.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) if in-place |

---

### Approach 2: Optimal -- Min-Heap of Size k+1
**What changed:** Maintain a min-heap of the next k+1 candidates. The minimum of these is guaranteed to be the next element in sorted order.

**Steps:**
1. Insert the first `k+1` elements into a min-heap.
2. For each remaining element `arr[i]` (from index `k+1` to `n-1`):
   - Pop the minimum from the heap and place it at the next output position.
   - Push `arr[i]` into the heap.
3. Drain the remaining elements from the heap into the output.

**Dry Run:** `arr = [6,5,3,2,8,10,9], k = 3`

| Step | Heap contents | Pop | Output so far |
|------|---------------|-----|---------------|
| Init | [2,5,3,6] (heapified) | - | [] |
| i=4 (8) | [3,5,8,6] | 2 | [2] |
| i=5 (10) | [5,6,8,10] | 3 | [2,3] |
| i=6 (9) | [6,9,8,10] | 5 | [2,3,5] |
| Drain | [8,9,10] | 6 | [2,3,5,6] |
| Drain | [9,10] | 8 | [2,3,5,6,8] |
| Drain | [10] | 9 | [2,3,5,6,8,9] |
| Drain | [] | 10 | [2,3,5,6,8,9,10] |

**Result:** `[2,3,5,6,8,9,10]`

| Metric | Value |
|--------|-------|
| Time   | O(n log k) |
| Space  | O(k) |

---

### Approach 3: Best -- Same Min-Heap (In-Place Write-Back)
**Intuition:** Same algorithm, but instead of creating a separate result array, write back directly into the input array. This saves one array allocation. The approach is identical in complexity but cleaner for production code.

| Metric | Value |
|--------|-------|
| Time   | O(n log k) |
| Space  | O(k) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using a heap of size `k` instead of `k+1` -- off by one.
2. Not handling `k = 0` (array is already sorted) or `k >= n` (use all elements).
3. Forgetting to drain the heap after processing all elements.

### Edge Cases to Test
- [ ] k = 0 --> already sorted, return as-is
- [ ] k = n-1 --> fully unsorted, heap holds entire array (degrades to heap sort)
- [ ] Single element array
- [ ] Array with duplicates
- [ ] Negative numbers

---

## Real-World Use Case
**Merging k sorted streams (logs, sensor data):** When multiple sorted data sources feed into a single pipeline, the merged stream is approximately sorted (each element is at most k positions off). A min-heap of size k+1 sorts this stream in O(n log k), which is essential for real-time log aggregation in distributed systems.

## Interview Tips
- Immediately mention that k-sorted arrays should trigger the "min-heap of size k+1" pattern.
- Explain WHY k+1: the correct element for position i is guaranteed to be in the next k+1 unplaced elements.
- Compare with regular sort: O(n log k) vs O(n log n). When k is small (e.g., k=10, n=10^6), this is a massive improvement.
- This problem is a favorite at Amazon and Google interviews.
