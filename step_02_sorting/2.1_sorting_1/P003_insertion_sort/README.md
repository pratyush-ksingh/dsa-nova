# Insertion Sort

> **Batch 4 of 12** | **Topic:** Sorting | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers, sort it in non-decreasing order using **Insertion Sort**. The algorithm builds the sorted array one element at a time by inserting each element into its correct position among the previously sorted elements.

### Examples

| Input               | Output              | Explanation                                          |
|---------------------|---------------------|------------------------------------------------------|
| `[12, 11, 13, 5, 6]`| `[5, 6, 11, 12, 13]`| Each element shifted into its correct position       |
| `[4, 3, 2, 1]`      | `[1, 2, 3, 4]`      | Worst case: every element moves to the front         |
| `[1, 2, 3, 4]`      | `[1, 2, 3, 4]`      | Best case: already sorted, no shifts needed          |
| `[5]`               | `[5]`               | Single element is trivially sorted                   |

### Analogy
Think of sorting a hand of playing cards. You pick up one card at a time from the table and insert it into the correct position in your hand. You slide the cards that are larger than the new card to the right to make room, then place it in the gap.

### 3 Key Observations
1. **"Aha" -- The left portion is always sorted.** After processing `i` elements, the subarray `[0..i-1]` is sorted. Element `i` just needs to find its spot in that sorted portion.
2. **"Aha" -- Best case is O(n) for nearly sorted data.** If each element is already in place (or close), the inner loop barely runs. This makes insertion sort the best choice for nearly-sorted or small arrays.
3. **"Aha" -- Stable and in-place.** Equal elements maintain their original relative order. No extra array is needed. This is why many language standard libraries use insertion sort for small subarrays within quicksort/mergesort hybrids.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm                    | Why?                                       |
|------------------|---------------|------------------------------|--------------------------------------------|
| Brute Force      | Array         | Naive insertion with swap    | Swap adjacent elements until positioned    |
| Optimal          | Array         | Key-and-shift insertion      | Save key, shift elements, place key once   |
| Best             | Array         | Binary search + shift        | Find position in O(log n), shift O(n)      |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Swap-Based Insertion
**Intuition:** For each element, repeatedly swap it with its left neighbor until it reaches its correct position. This is the simplest way to "bubble" the element into place.

**BUD Analysis:**
- **B**ottleneck: Each swap involves 3 assignments (temp, a=b, b=temp). We do many swaps when one shift + place would suffice.
- **U**nnecessary: Writing the element to intermediate positions when it will keep moving.

**Steps:**
1. For `i` from 1 to n-1:
   - Set `j = i`.
   - While `j > 0` and `arr[j] < arr[j-1]`:
     - Swap `arr[j]` and `arr[j-1]`.
     - Decrement `j`.

**Dry-Run Trace** with `[12, 11, 13, 5, 6]`:
```
i=1: key=11, swap(11,12) -> [11,12,13,5,6]
i=2: key=13, 13>12 no swap -> [11,12,13,5,6]
i=3: key=5,  swap(5,13) swap(5,12) swap(5,11) -> [5,11,12,13,6]
i=4: key=6,  swap(6,13) swap(6,12) swap(6,11) -> [5,6,11,12,13]
```

| Metric | Value               |
|--------|---------------------|
| Time   | O(n^2) worst/avg, O(n) best |
| Space  | O(1)                |

---

### Approach 2: Optimal -- Key-and-Shift
**Intuition:** Instead of swapping, save the current element as a "key," shift all larger elements one position to the right, and place the key in the gap. This reduces 3 assignments per swap to 1 assignment per shift.

**Steps:**
1. For `i` from 1 to n-1:
   - `key = arr[i]`
   - `j = i - 1`
   - While `j >= 0` and `arr[j] > key`:
     - `arr[j + 1] = arr[j]` (shift right)
     - Decrement `j`
   - `arr[j + 1] = key` (place key in gap)

**Dry-Run Trace** with `[12, 11, 13, 5, 6]`:
```
i=1: key=11, j=0: arr[0]=12>11, shift right -> [12,12,13,5,6]
     j=-1: place key -> [11,12,13,5,6]

i=2: key=13, j=1: arr[1]=12<13, stop
     place key at j+1=2 -> [11,12,13,5,6]

i=3: key=5,  j=2: 13>5 shift -> [11,12,13,13,6]
     j=1: 12>5 shift -> [11,12,12,13,6]
     j=0: 11>5 shift -> [11,11,12,13,6]
     j=-1: place key -> [5,11,12,13,6]

i=4: key=6,  j=3: 13>6 shift -> [5,11,12,13,13]
     j=2: 12>6 shift -> [5,11,12,12,13]
     j=1: 11>6 shift -> [5,11,11,12,13]
     j=0: 5<6, stop -> place key at j+1=1 -> [5,6,11,12,13]
```

| Metric | Value               |
|--------|---------------------|
| Time   | O(n^2) worst/avg, O(n) best |
| Space  | O(1)                |

---

### Approach 3: Best -- Binary Search + Shift
**Intuition:** The left portion `[0..i-1]` is sorted, so we can use binary search to find the insertion position in O(log n) instead of linear scan. However, we still need O(n) to shift elements, so the overall complexity remains O(n^2). The improvement is in the number of comparisons (from O(n^2) total to O(n log n) total), which matters when comparisons are expensive.

**Steps:**
1. For `i` from 1 to n-1:
   - `key = arr[i]`
   - Binary search for the correct position `pos` in `arr[0..i-1]`.
   - Shift `arr[pos..i-1]` one position right.
   - `arr[pos] = key`.

| Metric | Value                                           |
|--------|-------------------------------------------------|
| Time   | O(n^2) shifts, but only O(n log n) comparisons  |
| Space  | O(1)                                            |

---

## COMPLEXITY INTUITIVELY

- **Why O(n^2) worst case:** In a reverse-sorted array, each element `i` must shift past all `i` previous elements. Total shifts: 1+2+...+(n-1) = n(n-1)/2.
- **Why O(n) best case:** If the array is already sorted, the inner loop never executes (each key is already >= the element before it). We just scan once.
- **Why insertion sort is chosen for small arrays:** The constant factors are tiny (no recursion, no auxiliary arrays, simple comparisons). For n < ~20, insertion sort beats quicksort and mergesort in practice.

---

## EDGE CASES & MISTAKES

| Edge Case              | What Happens                                   |
|------------------------|------------------------------------------------|
| Empty array `[]`       | Nothing to sort, return immediately.           |
| Single element `[5]`   | Already sorted, loop doesn't execute.          |
| Already sorted         | O(n) -- inner loop never triggers.             |
| Reverse sorted         | O(n^2) -- worst case, every element moves.     |
| All duplicates `[3,3,3]`| O(n) -- no shifts needed, stable.             |

**Common Mistakes:**
- Using `>=` instead of `>` in the comparison (breaks stability).
- Forgetting to place the key after the while loop (`arr[j+1] = key`).
- Starting the outer loop at `i=0` instead of `i=1`.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests understanding of basic sorting mechanics, stability, and best/worst case analysis. Also a stepping stone to understanding more complex sorts.
- **Follow-ups:**
  - "When would you choose insertion sort over quicksort?" (Small arrays, nearly sorted data, online/streaming data.)
  - "Is insertion sort stable? Why?" (Yes, because we use `>` not `>=` -- equal elements don't move past each other.)
  - "How does TimSort use insertion sort?" (For small runs < 32-64 elements within the hybrid mergesort.)
- **Communication tip:** Emphasize the O(n) best case and the stability property -- these are what make insertion sort practically useful.

---

## CONNECTIONS

| Related Problem              | How It Connects                                  |
|------------------------------|--------------------------------------------------|
| Bubble Sort                  | Also O(n^2), but insertion sort does fewer swaps |
| Selection Sort               | Same O(n^2), but not stable and always O(n^2)   |
| Sort an Almost Sorted Array  | Insertion sort is ideal (O(nk) for k-displaced)  |
| Merge Sort                   | Hybrid sorts use insertion sort for small chunks |

---

## Real-World Use Case
**Online sorting / Streaming data:** When data arrives one element at a time and you need to maintain a sorted collection, insertion sort is the natural choice -- each new element is inserted into its correct position in O(n). Libraries like Python's `bisect.insort` and Java's `Arrays.sort` (for small arrays) use this approach internally.
