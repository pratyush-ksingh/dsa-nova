# Quick Sort

> **Step 02.2.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Implement the Quick Sort algorithm to sort an integer array in ascending order in-place.

Quick Sort is a divide-and-conquer algorithm that selects a pivot element, partitions the array around the pivot (elements smaller to the left, larger to the right), and recursively sorts the two sub-arrays.

## Examples

| Input                    | Output                  | Explanation |
|--------------------------|-------------------------|-------------|
| [64, 34, 25, 12, 22, 11, 90] | [11, 12, 22, 25, 34, 64, 90] | Sorted ascending |
| [5, 4, 3, 2, 1]          | [1, 2, 3, 4, 5]         | Reverse sorted — worst case for naive pivot |
| [1, 2, 3, 4, 5]          | [1, 2, 3, 4, 5]         | Already sorted — worst case for first-element pivot |
| [3, 3, 3, 1, 2]          | [1, 2, 3, 3, 3]         | Duplicate elements |

## Constraints

- 1 <= n <= 10^5
- -10^9 <= arr[i] <= 10^9

---

## Approach 1: Brute Force (Lomuto — first element as pivot)

**Intuition:** The simplest Quick Sort uses the first element as pivot. We use Lomuto's partition scheme: swap the chosen pivot to the end, then walk through the range placing everything smaller than the pivot before a "store" index. Finally place the pivot at the store index. This is correct but gives O(n^2) on sorted or reverse-sorted input because the pivot is always the smallest (or largest), making one partition of size 0 and the other of size n-1 at every level.

**Steps:**
1. If `low >= high`, return (base case).
2. Move `arr[low]` (pivot) to `arr[high]` by swapping.
3. Walk index `i` from `low` to `high-1`; whenever `arr[i] <= pivot`, swap `arr[i]` with `arr[store]` and increment `store`.
4. Place pivot at `arr[store]` by swapping with `arr[high]`. Return `store`.
5. Recurse on `[low, store-1]` and `[store+1, high]`.

| Metric | Value |
|--------|-------|
| Time   | O(n²) worst, O(n log n) average |
| Space  | O(n) stack worst, O(log n) average |

---

## Approach 2: Optimal (Lomuto + Random Pivot)

**Intuition:** The only difference from Brute Force is how the pivot is chosen. By picking a random index in `[low, high]` and swapping it to `high` before partitioning, we eliminate the possibility of adversarial inputs (sorted arrays, reverse-sorted, all-equal) causing worst-case behaviour. The expected number of comparisons is `2n ln n`, giving expected O(n log n) on any input distribution.

**Steps:**
1. Pick a random index `r` in `[low, high]`, swap `arr[r]` with `arr[high]`.
2. Run Lomuto partition with `arr[high]` as pivot (same as Approach 1 thereafter).
3. Recurse on the two sub-arrays.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) expected, O(n²) worst (astronomically unlikely) |
| Space  | O(log n) expected stack |

---

## Approach 3: Best (Hoare Partition + Random Pivot)

**Intuition:** Hoare's original partition scheme uses two pointers starting at opposite ends. The left pointer scans right until it finds an element >= pivot; the right pointer scans left until it finds an element <= pivot. We swap those two elements and continue until the pointers cross. Hoare's scheme performs approximately 3x fewer swaps than Lomuto on average. The subtlety: the partition function returns an index `j` such that all elements in `[low..j]` are <= pivot and all in `[j+1..high]` are >= pivot — but the pivot itself is NOT necessarily at position `j`. Recursion must use `[low, j]` and `[j+1, high]`.

**Steps:**
1. Optionally randomise: swap a random element into `arr[low]`; set `pivot = arr[low]`.
2. Set `i = low - 1`, `j = high + 1`.
3. Loop: increment `i` while `arr[i] < pivot`; decrement `j` while `arr[j] > pivot`.
4. If `i >= j`, return `j`.
5. Swap `arr[i]` and `arr[j]`, continue loop.
6. Recurse on `[low, j]` and `[j+1, high]`.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) expected |
| Space  | O(log n) expected stack |

---

## Real-World Use Case

**Programming language standard libraries:** C's `qsort`, Java's `Arrays.sort` for primitives, and Python's Timsort all draw from Quick Sort ideas. In practice, modern implementations use "introsort" — Quick Sort that switches to heap sort when recursion depth exceeds O(log n), preventing worst-case O(n^2). Quick Sort is also used in **database query engines** for in-memory sorting of query results and in **cache-friendly data processing** pipelines because its in-place nature avoids extra memory allocation.

## Interview Tips

- Clearly distinguish Lomuto vs Hoare partition: Lomuto puts the pivot in its final place; Hoare does not. This changes the recursive calls (`pi+1` vs `pi`).
- Always mention random pivot to handle sorted input — interviewers will probe this.
- Quick Sort is NOT stable (equal elements may change relative order). If stability is required, use Merge Sort.
- Worst case is O(n^2), but with random pivot it's statistically negligible. Some interviewers ask: "Can you guarantee O(n log n)?" — answer: use Merge Sort or Heap Sort for guarantees.
- For arrays with many duplicates, 3-way partitioning (Dutch National Flag) reduces to O(n) on all-equal inputs.
- Space: O(log n) average stack depth with random pivot; O(n) worst without randomisation due to deep recursion.
- Hoare is preferred in practice; Lomuto is easier to explain and implement correctly.
