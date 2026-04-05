# Bubble Sort

> **Batch 2 of 12** | **Topic:** Sorting | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers `arr` of size `n`, sort the array in ascending order using the **Bubble Sort** algorithm. Implement the optimized version with early termination if no swaps occur in a pass.

**Constraints:**
- `1 <= n <= 10^4`
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[64, 34, 25, 12, 22, 11, 90]` | `[11, 12, 22, 25, 34, 64, 90]` | Sorted ascending |
| `[5, 1, 4, 2, 8]` | `[1, 2, 4, 5, 8]` | Sorted ascending |
| `[1, 2, 3, 4, 5]` | `[1, 2, 3, 4, 5]` | Already sorted |
| `[2, 1]` | `[1, 2]` | Single swap needed |

### Real-Life Analogy
> *Imagine a line of people sorted by height. You start at the left end and compare each adjacent pair. If the taller person is on the left, they swap places. After one complete pass, the tallest person has "bubbled up" to the rightmost position -- just like a large air bubble rises to the top of water. You repeat the process for the remaining people, and after each pass, one more person is in their correct final position. If during a pass nobody needed to swap, the line is already sorted and you can stop early.*

### Key Observations
1. After each pass, the largest unsorted element "bubbles" to its correct position at the end. So after pass `i`, the last `i` elements are sorted.
2. If no swaps occur during a complete pass, the array is already sorted -- we can terminate early. <-- This is the "aha" insight (optimization)
3. Bubble sort is a **stable** sort: equal elements maintain their relative order, since we only swap when strictly greater.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Algorithm?
- Bubble sort is the simplest comparison-based sort to understand and implement.
- It is an **in-place, stable** sort -- important for understanding sorting fundamentals.
- Not used in practice for large data (O(n^2)), but excellent for learning and for nearly-sorted data.

### Pattern Recognition
- **Pattern:** Repeated Adjacent Swaps (comparison-based sorting)
- **Classification Cue:** "Bubble sort is the go-to when you need the simplest possible sort implementation, or when the array is nearly sorted (early termination makes it O(n))."

---

## APPROACH LADDER

### Approach 1: Basic Bubble Sort (No Optimization)
**Idea:** Run n-1 passes. In each pass, compare adjacent elements and swap if out of order.

**Steps:**
1. For `i` from 0 to n-2 (pass number):
   - For `j` from 0 to n-2-i (last i elements are already sorted):
     - If `arr[j] > arr[j+1]`, swap them.
2. Return the sorted array.

**Why we move on:** Even if the array becomes sorted early, we still run all n-1 passes. **Unnecessary work** for already-sorted or nearly-sorted arrays.

| Time | Space |
|------|-------|
| O(n^2) always | O(1) |

### Approach 2: Optimized Bubble Sort (Early Termination)
**What changed (BUD -- Unnecessary Work):** Track whether any swap occurred in a pass. If no swaps happened, the array is sorted -- stop immediately.

**Steps:**
1. For `i` from 0 to n-2:
   - Set `swapped = false`.
   - For `j` from 0 to n-2-i:
     - If `arr[j] > arr[j+1]`:
       - Swap `arr[j]` and `arr[j+1]`.
       - Set `swapped = true`.
   - If `swapped == false`, break (array is sorted).
2. Return the sorted array.

**Dry Run:** Input = `[5, 1, 4, 2, 8]`

**Pass 1 (i=0):**

| j | arr[j] | arr[j+1] | Swap? | Array After |
|---|--------|----------|-------|-------------|
| 0 | 5 | 1 | Yes | `[1, 5, 4, 2, 8]` |
| 1 | 5 | 4 | Yes | `[1, 4, 5, 2, 8]` |
| 2 | 5 | 2 | Yes | `[1, 4, 2, 5, 8]` |
| 3 | 5 | 8 | No  | `[1, 4, 2, 5, 8]` |

swapped = true, continue. (8 is now in correct position)

**Pass 2 (i=1):**

| j | arr[j] | arr[j+1] | Swap? | Array After |
|---|--------|----------|-------|-------------|
| 0 | 1 | 4 | No  | `[1, 4, 2, 5, 8]` |
| 1 | 4 | 2 | Yes | `[1, 2, 4, 5, 8]` |
| 2 | 4 | 5 | No  | `[1, 2, 4, 5, 8]` |

swapped = true, continue. (5, 8 in correct positions)

**Pass 3 (i=2):**

| j | arr[j] | arr[j+1] | Swap? | Array After |
|---|--------|----------|-------|-------------|
| 0 | 1 | 2 | No  | `[1, 2, 4, 5, 8]` |
| 1 | 2 | 4 | No  | `[1, 2, 4, 5, 8]` |

swapped = false --> **BREAK!** (early termination)

**Result:** `[1, 2, 4, 5, 8]` in 3 passes instead of 4.

| Time | Space |
|------|-------|
| O(n^2) worst, O(n) best (already sorted) | O(1) |

*Note:* Bubble sort is inherently O(n^2) in the average/worst case. The optimization only helps the best case. For better algorithms, see Merge Sort (O(n log n)) or Quick Sort.

---

## COMPLEXITY -- INTUITIVELY
**Time:**
- **Worst case O(n^2):** "Each of n passes scans up to n elements. Reverse-sorted input causes maximum swaps."
- **Best case O(n):** "One pass with zero swaps tells us the array is sorted. This happens for already-sorted input."

**Space:** O(1) -- "Only a few temporary variables (loop counters, swap flag). Sorting is done in-place."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to reduce the inner loop range by `i` -- without this, you redundantly compare already-sorted tail elements.
2. Not implementing the `swapped` flag -- missing the early termination optimization.
3. Using `>=` instead of `>` for the swap condition -- this breaks stability (swaps equal elements unnecessarily).

### Edge Cases to Test
- [ ] Single element `[42]` --> `[42]`
- [ ] Two elements sorted `[1, 2]` --> `[1, 2]` (1 pass, 0 swaps)
- [ ] Two elements reversed `[2, 1]` --> `[1, 2]` (1 swap)
- [ ] Already sorted `[1, 2, 3, 4, 5]` --> early termination after 1 pass
- [ ] Reverse sorted `[5, 4, 3, 2, 1]` --> worst case, n-1 passes
- [ ] All equal `[3, 3, 3]` --> `[3, 3, 3]` (no swaps, stable)
- [ ] Negative numbers `[-2, 3, -1, 5]` --> `[-2, -1, 3, 5]`
- [ ] Duplicates `[4, 2, 4, 1, 2]` --> `[1, 2, 2, 4, 4]` (stability preserved)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to sort an array using bubble sort. Should I implement the optimized version with early termination?"
2. **Match:** "Bubble sort repeatedly passes through the array, swapping adjacent elements."
3. **Plan:** "Outer loop for passes, inner loop for comparisons. Track a swapped flag; break if no swaps in a pass."
4. **Implement:** Write both loops and the swap logic.
5. **Review:** Trace through `[5, 1, 4, 2, 8]` showing each pass.
6. **Evaluate:** "O(n^2) worst, O(n) best. O(1) space. Stable sort."

### Follow-Up Questions
- "When would you use bubble sort over other algorithms?" --> Nearly sorted data (adaptive O(n) best case), or when simplicity matters more than performance.
- "How does it compare to selection sort?" --> Both are O(n^2), but bubble sort is stable and adaptive; selection sort does fewer swaps (exactly n-1).
- "How would you make it sort in descending order?" --> Change `arr[j] > arr[j+1]` to `arr[j] < arr[j+1]`.

---

## CONNECTIONS
- **Prerequisite:** Array traversal, swapping elements
- **Same Family:** Selection Sort, Insertion Sort (all O(n^2) comparison sorts)
- **Better Alternatives:** Merge Sort O(n log n), Quick Sort O(n log n avg)
- **This Unlocks:** Understanding comparison-based sorting, stability, adaptiveness; foundation for learning advanced sorts
