# Merge K Sorted Arrays

> **Step 11 | 11.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given `k` sorted arrays each of size `n`, merge all of them into a single sorted array and return it.

**Constraints:**
- `1 <= k <= 10^3`
- `1 <= n <= 10^3`
- `-10^9 <= arr[i][j] <= 10^9`
- Each individual array is sorted in non-decreasing order.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[1,3,5,7],[2,4,6,8],[0,9,10,11]]` | `[0,1,2,3,4,5,6,7,8,9,10,11]` | Merge 3 sorted arrays of size 4 |
| `[[1,3],[2,4],[5,6]]` | `[1,2,3,4,5,6]` | Merge 3 sorted arrays of size 2 |
| `[[10],[5],[1]]` | `[1,5,10]` | Three single-element arrays |

### Real-Life Analogy
> *Imagine you have k sorted stacks of ranked resumes from k different recruiters. You want to create one master-sorted stack. The brute force way is to dump all resumes in a pile and re-sort. The smart way is to always pick the top resume from whichever recruiter's current top is the "best" — that's exactly what the min-heap does, maintaining just one candidate per recruiter at a time.*

### Key Observations
1. All k arrays are already sorted — we should exploit this instead of ignoring it.
2. At any point, the next element in the merged output must be the minimum of the current "front" elements of all k arrays.
3. A min-heap gives us that minimum in O(log k) time, and we do this nk times total.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why Min-Heap?
- We need to repeatedly find the minimum among k candidates (one per active array).
- A min-heap of size k gives O(log k) extraction and insertion.
- Total: nk elements × O(log k) per element = O(nk log k).

### Pattern Recognition
- **Pattern:** K-way merge using a min-heap.
- **Classification Cue:** "Whenever you must merge multiple sorted sequences into one — think min-heap with one representative per sequence."

---

## APPROACH LADDER

### Approach 1: Brute Force — Concatenate and Sort
**Idea:** Dump all elements from all arrays into one list, then sort it with a standard sort.

**Steps:**
1. Iterate over all k arrays and append every element to a single list.
2. Sort the combined list.
3. Return the sorted list.

**Why it works:** Sorting always produces the correct answer regardless of input structure.

**BUD Transition — Bottleneck:** We throw away the sorted property of each input array. The comparison sort cannot do better than O(nk log(nk)).

| Metric | Value |
|--------|-------|
| Time   | O(nk log(nk)) |
| Space  | O(nk) |

---

### Approach 2: Optimal — Min-Heap with (value, array_idx, elem_idx)
**What changed:** We use each array's sorted order. The minimum element in the entire merged output must be among the k first elements (one per array). A min-heap always gives us that minimum instantly.

**Steps:**
1. Push the first element of each non-empty array as `(value, array_index, element_index)` into a min-heap.
2. While the heap is non-empty:
   - Pop the minimum `(val, i, j)`.
   - Append `val` to the result.
   - If array `i` has a next element at index `j+1`, push `(arrays[i][j+1], i, j+1)`.
3. Return the result.

**Dry Run:** `arrays = [[1,3],[2,4],[5,6]]`

| Step | Heap | Pop | Result |
|------|------|-----|--------|
| Init | [(1,0,0),(2,1,0),(5,2,0)] | — | [] |
| 1 | [(2,1,0),(3,0,1),(5,2,0)] | (1,0,0) | [1] |
| 2 | [(3,0,1),(5,2,0),(4,1,1)] | (2,1,0) | [1,2] |
| 3 | [(4,1,1),(5,2,0)] | (3,0,1) | [1,2,3] |
| 4 | [(5,2,0)] | (4,1,1) | [1,2,3,4] |
| 5 | [(6,2,1)] | (5,2,0) | [1,2,3,4,5] |
| 6 | [] | (6,2,1) | [1,2,3,4,5,6] |

| Metric | Value |
|--------|-------|
| Time   | O(nk log k) |
| Space  | O(k) for heap + O(nk) for output |

---

### Approach 3: Best — Same Min-Heap (clean production form)
**Intuition:** Identical algorithm to Approach 2, written more cleanly. The heap always contains at most k elements (one per array). We extract the global minimum one at a time, replacing it with the next element from the same array. This is the standard production implementation of k-way merge.

| Metric | Value |
|--------|-------|
| Time   | O(nk log k) |
| Space  | O(k) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to store `(array_index, element_index)` in the heap — you need both to fetch the next element.
2. Not handling empty arrays in the input (skip them when initializing the heap).
3. Tuple comparison in Python: if two values are equal, Python will compare `array_idx` next — this is fine since indices are unique.

### Edge Cases to Test
- [ ] One of the k arrays is empty
- [ ] k = 1 (single array, already sorted)
- [ ] All arrays have length 1
- [ ] Arrays with duplicate values across different arrays
- [ ] Negative numbers

---

## Real-World Use Case
**Database merge joins and log aggregation:** Distributed databases (e.g., Google Bigtable, Apache Cassandra) store data in multiple sorted SSTables. Merging them during a compaction step uses exactly the k-way min-heap merge. Similarly, log aggregation systems that merge sorted log streams from k servers use this pattern to produce a globally sorted event timeline in O(n log k) time.

## Interview Tips
- State upfront: "Each input is already sorted, so I'll use a min-heap to exploit that — O(nk log k) instead of O(nk log(nk))."
- The heap stores a tuple of `(value, array_idx, elem_idx)`. Explain why all three fields are needed.
- Mention that Python's `heapq.merge` does exactly this under the hood.
- This is the core subroutine in external merge sort — say so if the interviewer seems interested in depth.
- Follow-up variant: "Merge K Sorted Linked Lists" (LeetCode 23) uses the same idea with nodes instead of indices.
