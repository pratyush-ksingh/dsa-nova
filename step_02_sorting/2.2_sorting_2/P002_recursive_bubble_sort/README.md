# Recursive Bubble Sort

> **Batch 3 of 12** | **Topic:** Sorting | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement Bubble Sort using **recursion** instead of nested loops. Given an array of integers, sort it in ascending order. Each recursive call should perform one pass of "bubbling" the largest element to the end, then recurse on the remaining unsorted portion.

**Constraints:**
- `1 <= n <= 10^3` (small due to recursion depth limits)
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[64, 34, 25, 12, 22, 11, 90]` | `[11, 12, 22, 25, 34, 64, 90]` | Standard sort |
| `[5, 1, 4, 2, 8]` | `[1, 2, 4, 5, 8]` | Each pass bubbles max to end |
| `[1, 2, 3]` | `[1, 2, 3]` | Already sorted |
| `[3, 2, 1]` | `[1, 2, 3]` | Reverse sorted -- worst case |

### Real-Life Analogy
> *Picture a line of students arranged by height. A teacher walks down the line comparing adjacent pairs: if the taller student is on the left, they swap. After one walk, the tallest student ends up at the far right. The teacher then repeats the walk, but only up to the second-to-last position (the tallest is already placed). Each "walk" is one recursive call. When the walk length shrinks to 1, everyone is in order.*

### Key Observations
1. Iterative bubble sort uses two nested loops: outer shrinks the range, inner does comparisons. Recursion replaces the outer loop.
2. Each recursive call handles one pass, placing the largest element in its final position at index `n - 1`.
3. Base case: when the range shrinks to 1 element, the array is sorted. <-- This is the "aha" insight for converting loops to recursion.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- In-place sorting on an array. No auxiliary structures needed.
- Recursion replaces the outer loop, teaching the concept of converting iteration to recursion.

### Pattern Recognition
- **Pattern:** Recursion replacing outer loop of a known algorithm
- **Classification Cue:** "When you see _implement X using recursion_ --> think _what does each level of recursion accomplish? (one pass/one reduction)_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Iterative Bubble Sort (for comparison)
**Idea:** The standard nested-loop version. This is the baseline to understand what we are converting.

**Steps:**
1. For `i` from `n - 1` down to `0`:
   - For `j` from `0` to `i - 1`:
     - If `arr[j] > arr[j + 1]`, swap them.

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Optimal -- Recursive Bubble Sort
**What changed:** Replace the outer loop with recursion. Each call does one bubbling pass over `arr[0..n-1]`, then recurses with `n - 1`.

**Steps:**
1. Base case: if `n <= 1`, return.
2. For `j` from `0` to `n - 2`:
   - If `arr[j] > arr[j + 1]`, swap them.
3. Recurse: `bubbleSort(arr, n - 1)`.

**Dry Run:** Input = `[5, 1, 4, 2, 8]`

**Call 1 (n=5):** Pass over indices 0..3
| j | Compare | Swap? | Array |
|---|---------|-------|-------|
| 0 | 5 > 1 | Yes | `[1, 5, 4, 2, 8]` |
| 1 | 5 > 4 | Yes | `[1, 4, 5, 2, 8]` |
| 2 | 5 > 2 | Yes | `[1, 4, 2, 5, 8]` |
| 3 | 5 > 8 | No | `[1, 4, 2, 5, 8]` |
--> 8 is in final position. Recurse with n=4.

**Call 2 (n=4):** Pass over indices 0..2
| j | Compare | Swap? | Array |
|---|---------|-------|-------|
| 0 | 1 > 4 | No | `[1, 4, 2, 5, 8]` |
| 1 | 4 > 2 | Yes | `[1, 2, 4, 5, 8]` |
| 2 | 4 > 5 | No | `[1, 2, 4, 5, 8]` |
--> 5 in place. Recurse with n=3.

**Call 3 (n=3):** No swaps needed --> early termination possible.

**Result:** `[1, 2, 4, 5, 8]`

| Time | Space |
|------|-------|
| O(n^2) | O(n) recursion stack |

### Approach 3: Best -- Recursive Bubble Sort with Early Termination
**What changed:** Track whether any swaps occurred during a pass. If no swaps happened, the array is already sorted -- stop early.

**Steps:**
1. Base case: if `n <= 1`, return.
2. Set `swapped = false`.
3. For `j` from `0` to `n - 2`:
   - If `arr[j] > arr[j + 1]`, swap and set `swapped = true`.
4. If `!swapped`, return (already sorted).
5. Recurse: `bubbleSort(arr, n - 1)`.

This does not improve worst-case complexity but makes the best case (already sorted) O(n) instead of O(n^2).

| Time | Space |
|------|-------|
| O(n^2) worst, O(n) best | O(n) recursion stack |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n^2) -- "Each recursive call does up to n comparisons, and there are up to n calls. Total comparisons: n + (n-1) + ... + 1 = n(n+1)/2."
**Space:** O(n) -- "Each recursive call adds a frame to the call stack. There are up to n levels of recursion. This is the key tradeoff vs the iterative version which uses O(1) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting the base case -- infinite recursion causing stack overflow.
2. Wrong base case: using `n == 0` instead of `n <= 1` -- array of size 1 is trivially sorted.
3. Not reducing `n` in the recursive call -- processing the same range forever.
4. Recursing on the inner loop instead of the outer -- this creates a different (less intuitive) recursion.

### Edge Cases to Test
- [ ] Already sorted `[1, 2, 3, 4]` --> no swaps needed (early termination)
- [ ] Reverse sorted `[4, 3, 2, 1]` --> maximum swaps
- [ ] Single element `[5]` --> return as-is
- [ ] Two elements `[2, 1]` --> one swap
- [ ] All identical `[3, 3, 3]` --> no swaps
- [ ] Negative numbers `[-5, -1, -3]` --> `[-5, -3, -1]`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Should I implement bubble sort specifically, or any recursive sort? Should it be in-place?"
2. **Approach:** "I'll replace the outer loop of bubble sort with recursion. Each call does one bubbling pass, placing the largest element at the end, then recurses on a smaller range. Base case is n <= 1."
3. **Code:** Write the function. Emphasize base case, pass, recursive call.
4. **Optimize:** "I can add a `swapped` flag for early termination."
5. **Test:** Walk through `[5, 1, 4, 2, 8]` showing each recursive call.

### Follow-Up Questions
- "What is the space complexity difference vs iterative?" --> O(n) stack vs O(1). Key tradeoff.
- "Can you make the inner loop recursive too?" --> Yes, but it makes the code harder to read with no algorithmic benefit.
- "Why not use merge sort or quick sort?" --> They are O(n log n) and naturally recursive. Bubble sort is O(n^2) -- this exercise teaches recursion, not optimal sorting.
- "What about tail recursion optimization?" --> The recursive call is in tail position, but Java/Python do not optimize tail calls. In languages that do (Scheme, Scala), this would use O(1) stack.

---

## CONNECTIONS
- **Prerequisite:** Iterative bubble sort, recursion basics (base case + reduction)
- **Same Pattern:** Recursive Insertion Sort, Recursive Selection Sort
- **Harder Variant:** Merge Sort (recursive divide-and-conquer), Quick Sort
- **This Unlocks:** Understanding how any iterative algorithm can be expressed recursively, and the space tradeoff that entails
