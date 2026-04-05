# Check if Array is Sorted

> **Batch 2 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers `arr` of size `n`, determine whether the array is sorted in **non-decreasing order** (i.e., each element is less than or equal to the next). Return `true` if sorted, `false` otherwise.

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, 4, 5]` | `true` | Each element <= next element |
| `[1, 3, 2, 4, 5]` | `false` | 3 > 2 violates non-decreasing order |
| `[5, 5, 5, 5]` | `true` | Equal elements are allowed (non-decreasing, not strictly increasing) |
| `[1]` | `true` | Single element is trivially sorted |

### Real-Life Analogy
> *Imagine you are checking whether people in a queue are arranged by height from shortest to tallest. You walk along the line and compare each person to the one in front of them. The moment you find someone taller standing behind someone shorter, you know the line is not sorted. If you reach the end without finding such a violation, the line is sorted.*

### Key Observations
1. We only need to find a **single violation** -- one adjacent pair where `arr[i] > arr[i+1]` -- to conclude "not sorted."
2. If no violation exists among all adjacent pairs, the entire array must be sorted (transitivity of <=). <-- This is the "aha" insight
3. A single-element or empty array is always sorted by definition.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need sequential access to adjacent elements. The input **Array** already provides this with O(1) indexed access.
- No auxiliary data structures needed.

### Pattern Recognition
- **Pattern:** Linear Scan with Early Exit (check a condition on adjacent pairs)
- **Classification Cue:** "When you see _verify a property over the entire array_ --> think _scan and check adjacent pairs_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Compare All Pairs
**Idea:** For every pair (i, j) where i < j, check if `arr[i] <= arr[j]`.

**Steps:**
1. For each i from 0 to n-2:
   - For each j from i+1 to n-1:
     - If `arr[i] > arr[j]`, return `false`.
2. Return `true`.

**Why it works:** Exhaustively checks the sorted property for all pairs.

**Why we move on:** **Unnecessary work** -- checking all O(n^2) pairs is redundant. If adjacent pairs satisfy the condition, all pairs do by transitivity.

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Optimal -- Single Pass Adjacent Comparison
**What changed (BUD -- Unnecessary Work):** We only check adjacent pairs instead of all pairs.

**Steps:**
1. For each index `i` from 0 to n-2:
   - If `arr[i] > arr[i+1]`, return `false`.
2. If we finish the loop, return `true`.

**Dry Run:** Input = `[1, 3, 2, 4, 5]`

| Step | i | arr[i] | arr[i+1] | arr[i] > arr[i+1]? | Action |
|------|---|--------|----------|---------------------|--------|
| 1 | 0 | 1 | 3 | No | Continue |
| 2 | 1 | 3 | 2 | Yes | Return false |

**Result:** `false` (early exit at index 1)

**Dry Run 2:** Input = `[1, 2, 3, 4, 5]`

| Step | i | arr[i] | arr[i+1] | arr[i] > arr[i+1]? |
|------|---|--------|----------|---------------------|
| 1 | 0 | 1 | 2 | No |
| 2 | 1 | 2 | 3 | No |
| 3 | 2 | 3 | 4 | No |
| 4 | 3 | 4 | 5 | No |

**Result:** `true` (loop finished without violation)

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* This is already optimal -- we must examine adjacent pairs, and there are n-1 of them. No Approach 3 needed.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan at most n-1 adjacent pairs, each taking O(1). With early exit, the best case is O(1) if the first pair is out of order."
**Space:** O(1) -- "No extra storage; just index variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using strict `<` instead of `<=` -- the problem asks for non-decreasing, so equal elements are allowed.
2. Forgetting that a single-element array is sorted.
3. Returning `true` too early (inside the loop) instead of only after the loop finishes.

### Edge Cases to Test
- [ ] Single element `[42]` --> `true`
- [ ] Two elements sorted `[1, 2]` --> `true`
- [ ] Two elements unsorted `[2, 1]` --> `false`
- [ ] All equal `[7, 7, 7]` --> `true`
- [ ] Violation at the very end `[1, 2, 3, 1]` --> `false`
- [ ] Descending array `[5, 4, 3, 2, 1]` --> `false`
- [ ] Negative numbers `[-3, -1, 0, 4]` --> `true`

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to check if the array is in non-decreasing order. Does sorted mean strictly increasing or non-decreasing?"
2. **Match:** "This is a linear scan problem -- just check adjacent pairs."
3. **Plan:** "I'll iterate through the array, comparing each element to the next. If I find arr[i] > arr[i+1], I return false. Otherwise, return true."
4. **Implement:** Write the simple loop.
5. **Review:** Walk through both a sorted and unsorted example.
6. **Evaluate:** "O(n) time, O(1) space. Can't beat O(n) since we must read all elements."

### Follow-Up Questions
- "What if you need to check both ascending and descending?" --> Two passes or track which direction is established by the first non-equal pair.
- "What if the array is sorted but rotated?" --> Different problem (search for the rotation point -- LC #33).
- "How would you count the number of inversions?" --> Merge sort-based approach, O(n log n).

---

## CONNECTIONS
- **Prerequisite:** Array traversal, comparison operators
- **Same Pattern:** Find first out-of-order element, verify heap property
- **Related:** Remove Duplicates from Sorted Array (P004) -- assumes sorted input
- **This Unlocks:** Binary search (requires sorted arrays), merge operations on sorted arrays
