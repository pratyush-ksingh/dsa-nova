# Merge Sort

> **Step 02 - 2.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Tags:** sorting, divide-and-conquer

---

## Problem Statement
Given an array of N integers, sort the array in ascending order using the **Merge Sort** algorithm.

**Example:**
```
Input:  [5, 3, 8, 4, 2]
Output: [2, 3, 4, 5, 8]

Input:  [38, 27, 43, 3, 9, 82, 10]
Output: [3, 9, 10, 27, 38, 43, 82]
```

| Input | Output | Explanation |
|-------|--------|-------------|
| [5, 3, 8, 4, 2] | [2, 3, 4, 5, 8] | Divide, sort halves, merge |
| [1] | [1] | Single element already sorted |
| [2, 1] | [1, 2] | Swap the pair |
| [1, 2, 3] | [1, 2, 3] | Already sorted, no swaps needed |

### Real-Life Analogy
Imagine you have a huge pile of exam papers to sort by roll number. You split the pile in half, give each half to a friend, and ask them to sort their half. Once both halves are sorted, you **merge** them by comparing the top paper from each pile and picking the smaller one. This "divide the work, merge the results" strategy is exactly how merge sort operates.

### Key Observations
1. Merge sort is a **divide-and-conquer** algorithm: split array in half, recursively sort each half, merge.
2. The merge step is the core -- combining two sorted arrays into one sorted array in O(n).
3. It is a **stable** sort (equal elements keep their relative order).
4. Unlike quicksort, merge sort always guarantees O(n log n) -- no worst-case degradation.

### Constraints
- 1 <= N <= 10^5
- -10^9 <= arr[i] <= 10^9

---

## Approach 1: Brute Force -- Using Built-in Sort
**Intuition:** Use the language's built-in sort (typically Timsort or dual-pivot quicksort). This isn't merge sort itself but establishes the expected output baseline.

**Steps:**
1. Call `Arrays.sort()` or `sorted()`.
2. Return the sorted array.

**Dry Run (arr = [5, 3, 8, 4, 2]):**
Built-in sort produces [2, 3, 4, 5, 8].

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Approach 2: Optimal -- Classic Merge Sort (Top-Down Recursive)
**Intuition:** Recursively divide the array into two halves until each sub-array has one element. Then merge adjacent sorted sub-arrays back together. The merge step compares elements from both halves using two pointers.

**Steps:**
1. If array length <= 1, return (base case).
2. Find mid = (low + high) / 2.
3. Recursively sort left half: mergeSort(arr, low, mid).
4. Recursively sort right half: mergeSort(arr, mid+1, high).
5. Merge the two sorted halves using a temporary array.

**Dry Run (arr = [5, 3, 8, 4, 2]):**

```
Split: [5,3,8,4,2]
       /           \
   [5,3,8]       [4,2]
   /     \        /   \
 [5,3]  [8]    [4]   [2]
 / \
[5] [3]

Merge: [3,5] + [8] = [3,5,8]
       [2,4]
       [3,5,8] + [2,4] = [2,3,4,5,8]
```

| Metric | Value |
|--------|-------|
| Time   | O(n log n) -- log n levels, O(n) merge at each level |
| Space  | O(n) for temporary merge array + O(log n) recursion stack |

---

## Approach 3: Best -- Bottom-Up Iterative Merge Sort
**Intuition:** Instead of recursing top-down, iterate bottom-up. Start by merging pairs of size 1, then pairs of size 2, then 4, etc. This avoids recursion stack overhead.

**Steps:**
1. Start with width = 1.
2. For each width, merge adjacent sub-arrays of that width across the entire array.
3. Double the width and repeat until width >= n.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) for temp array, but O(1) recursion stack |

---

## Edge Cases
- Single element array -- already sorted.
- Already sorted array -- merge sort still does O(n log n) work.
- All elements identical -- stable sort preserves order.
- Array with negative numbers -- comparison still works.

## Interview Tips
- Always mention merge sort is **stable** and guarantees O(n log n).
- Explain the merge step clearly -- interviewers love dry-running the merge.
- Know the trade-off: merge sort uses O(n) extra space vs quicksort's O(log n).
- Bottom-up variant is asked less often but shows depth of understanding.
- Merge sort is preferred for **linked lists** (no random access needed, O(1) extra space).
