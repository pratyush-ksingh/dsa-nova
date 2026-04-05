# Recursive Insertion Sort

> **Step 02 - 2.2** | **Difficulty:** EASY | **XP:** 10 | **Tags:** sorting, recursion

---

## Problem Statement
Given an array of N integers, sort the array in ascending order using a **recursive** implementation of Insertion Sort. Each recursive call should sort the first n elements, then insert the n-th element into its correct position.

**Example:**
```
Input:  [12, 11, 13, 5, 6]
Output: [5, 6, 11, 12, 13]

Input:  [5, 4, 3, 2, 1]
Output: [1, 2, 3, 4, 5]
```

| Input | Output | Explanation |
|-------|--------|-------------|
| [12, 11, 13, 5, 6] | [5, 6, 11, 12, 13] | Recursively sort prefix, insert last |
| [1] | [1] | Single element already sorted |
| [2, 1] | [1, 2] | Insert 1 before 2 |
| [1, 2, 3] | [1, 2, 3] | Already sorted, each insert is at end |

### Real-Life Analogy
Think of sorting a hand of playing cards. You pick up cards one at a time. Each time, you slide the new card into the correct position among the cards you already hold. The recursive version says: "First, sort the first n-1 cards (recursion), then insert the n-th card into the right spot."

### Key Observations
1. Insertion sort naturally builds a sorted prefix one element at a time -- recursion fits this structure.
2. The recursive call sorts arr[0..n-2], then you insert arr[n-1] into the sorted portion.
3. The insertion step shifts elements right until the correct position is found.
4. Best case O(n) when array is already sorted -- each insert goes to the end.

### Constraints
- 1 <= N <= 10^4
- -10^9 <= arr[i] <= 10^9

---

## Approach 1: Brute Force -- Iterative Insertion Sort
**Intuition:** The standard iterative version with two nested loops. For each element, shift it left until it finds its correct position. This is the baseline we are converting to recursion.

**Steps:**
1. For i = 1 to n-1:
   - key = arr[i]
   - j = i - 1
   - While j >= 0 and arr[j] > key: shift arr[j] right, decrement j.
   - Place key at arr[j+1].

**Dry Run (arr = [12, 11, 13, 5, 6]):**

| i | key | Sorted prefix before | After insertion |
|---|-----|---------------------|-----------------|
| 1 | 11  | [12]                | [11, 12, 13, 5, 6] |
| 2 | 13  | [11, 12]            | [11, 12, 13, 5, 6] |
| 3 | 5   | [11, 12, 13]        | [5, 11, 12, 13, 6] |
| 4 | 6   | [5, 11, 12, 13]     | [5, 6, 11, 12, 13] |

| Metric | Value |
|--------|-------|
| Time   | O(n^2) worst, O(n) best |
| Space  | O(1) |

---

## Approach 2: Optimal -- Recursive Insertion Sort
**Intuition:** Replace the outer loop with recursion. To sort n elements: recursively sort the first n-1 elements, then insert the n-th element into the sorted prefix.

**Steps:**
1. Base case: if n <= 1, return.
2. Recursively sort first n-1 elements.
3. Insert arr[n-1] into sorted arr[0..n-2] using a loop that shifts elements right.

**Dry Run (arr = [12, 11, 13, 5, 6]):**
```
sort(5) -> sort(4) -> sort(3) -> sort(2) -> sort(1) [base]
  sort(2): sorted [12], insert 11 -> [11, 12]
  sort(3): sorted [11,12], insert 13 -> [11, 12, 13]
  sort(4): sorted [11,12,13], insert 5 -> [5, 11, 12, 13]
  sort(5): sorted [5,11,12,13], insert 6 -> [5, 6, 11, 12, 13]
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) worst, O(n) best |
| Space  | O(n) recursion stack |

---

## Approach 3: Best -- Fully Recursive (insertion step also recursive)
**Intuition:** Make the inner insertion step recursive too. Instead of using a while loop to shift elements, use a recursive function: if the last sorted element is bigger than key, move it right and recurse on the position before it.

**Steps:**
1. Base case for sort: if n <= 1, return.
2. Recursively sort first n-1 elements.
3. Call recursive_insert(arr, n-1) which:
   - If n == 0 or arr[n-1] <= arr[n], place arr[n] and return.
   - Otherwise, shift arr[n-1] right, recurse on n-1.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) recursion stack (sort) + O(n) recursion stack (insert) |

---

## Edge Cases
- Single element array -- base case, already sorted.
- Already sorted array -- O(n) since no shifting needed.
- Reverse sorted -- worst case O(n^2), every element shifts to front.
- All elements equal -- O(n), no shifting needed.
- Negative numbers -- comparison works the same.

## Interview Tips
- Insertion sort is the go-to example for "online" sorting -- it can sort as data arrives.
- Recursive insertion sort demonstrates understanding of how iterative patterns map to recursion.
- Always mention the O(n) best case -- it shows you understand adaptive sorting.
- Insertion sort is preferred over bubble/selection sort for nearly sorted data.
- For small arrays (n < 20), insertion sort often beats O(n log n) algorithms due to low overhead.
