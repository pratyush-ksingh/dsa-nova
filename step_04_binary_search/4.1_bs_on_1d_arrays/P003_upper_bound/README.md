# Upper Bound

> **Batch 2 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array `arr` of `n` integers and a target value `x`, find the **upper bound** of `x`. The upper bound is defined as the **smallest index** `i` such that `arr[i] > x`. If no such index exists, return `n` (one past the last valid index).

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i], x <= 10^9`
- Array is sorted in non-decreasing order

**Examples:**

| Input Array | x | Output | Explanation |
|-------------|---|--------|-------------|
| `[1, 2, 3, 5, 5, 5, 7]` | 5 | `6` | First element > 5 is 7 at index 6 |
| `[1, 2, 3, 5, 5, 5, 7]` | 4 | `3` | First element > 4 is 5 at index 3 |
| `[1, 2, 3, 5, 5, 5, 7]` | 7 | `7` | No element > 7, return n=7 |
| `[1, 2, 3, 5, 5, 5, 7]` | 0 | `0` | All elements > 0, return index 0 |

### Real-Life Analogy
> *Imagine a bookshelf where books are arranged by height from shortest to tallest. You want to find the first book that is strictly taller than a given height. You could scan from left to right (linear), but since the books are sorted, you can use a divide-and-conquer approach: check the middle book, and decide which half to search next. This is binary search for the upper bound.*

### Key Observations
1. Upper bound is NOT the same as "find the element." It is the first position where you could insert `x` and still keep values equal to `x` to the left.
2. If `arr[mid] <= x`, the upper bound must be to the right. If `arr[mid] > x`, this could be the answer, but there might be a smaller index, so search left.
3. The answer is always a valid insertion point in [0, n]. <-- This is the "aha" insight: upper bound is equivalent to `lower_bound(x+1)` conceptually.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- The array is **sorted**, which is the prerequisite for binary search.
- Binary search halves the search space each step, giving O(log n) vs O(n) for linear scan.

### Pattern Recognition
- **Pattern:** Binary Search on Sorted Array (boundary finding)
- **Classification Cue:** "When you see _first/last element satisfying a condition in a sorted array_ --> think _binary search with condition-based narrowing_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Linear Scan)
**Idea:** Scan from left to right. Return the first index where `arr[i] > x`.

**Steps:**
1. For `i` from 0 to n-1:
   - If `arr[i] > x`, return `i`.
2. If no such element found, return `n`.

**Why we move on:** **Bottleneck** -- O(n) when we can exploit the sorted property for O(log n).

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Binary Search
**What changed:** Use binary search to find the boundary between `<= x` and `> x`.

**Steps:**
1. Set `low = 0`, `high = n - 1`, `ans = n` (default: no element > x).
2. While `low <= high`:
   - `mid = low + (high - low) / 2`
   - If `arr[mid] > x`: this is a candidate. Set `ans = mid`, search left: `high = mid - 1`.
   - Else (`arr[mid] <= x`): search right: `low = mid + 1`.
3. Return `ans`.

**Dry Run:** `arr = [1, 2, 3, 5, 5, 5, 7]`, x = 5

| Step | low | high | mid | arr[mid] | Condition | Action | ans |
|------|-----|------|-----|----------|-----------|--------|-----|
| Init | 0   | 6    | -   | -        | -         | ans = 7 | 7 |
| 1    | 0   | 6    | 3   | 5        | 5 <= 5    | low = 4 | 7 |
| 2    | 4   | 6    | 5   | 5        | 5 <= 5    | low = 6 | 7 |
| 3    | 6   | 6    | 6   | 7        | 7 > 5     | ans = 6, high = 5 | 6 |
| 4    | 6   | 5    | -   | -        | low > high | stop | 6 |

**Result:** 6 (index of first element > 5, which is 7)

| Time | Space |
|------|-------|
| O(log n) | O(1) |

*Note:* This is the best possible approach -- you cannot find the boundary faster than O(log n) in a sorted array without additional information.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Each comparison eliminates half the remaining candidates. For n = 10^5, that is about 17 comparisons."
**Space:** O(1) -- "We use only three integer variables: low, high, and ans."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Confusing upper bound with lower bound: **lower bound** is `arr[i] >= x`, **upper bound** is `arr[i] > x`. The only difference is `>=` vs `>`.
2. Forgetting to initialize `ans = n` -- if all elements are <= x, the answer is n (past the end).
3. Using `mid = (low + high) / 2` instead of `mid = low + (high - low) / 2` -- the former can overflow in languages with fixed-width integers.

### Edge Cases to Test
- [ ] Target smaller than all elements --> return 0
- [ ] Target larger than all elements --> return n
- [ ] Target equals every element `[5, 5, 5]`, x=5 --> return 3
- [ ] Single element array, element > target --> return 0
- [ ] Single element array, element <= target --> return 1
- [ ] Target not present but falls between elements `x=4` in `[1, 3, 5, 7]` --> return 2

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Upper bound means the first element strictly greater than x, correct? What should I return if no such element exists?"
2. **Approach:** "Since the array is sorted, I will use binary search. I initialize ans = n. If arr[mid] > x, it is a candidate and I search left. Otherwise I search right."
3. **Code:** Write the clean binary search. Emphasize the `<= x` vs `> x` condition.
4. **Test:** Walk through `[1, 2, 3, 5, 5, 5, 7]` with x = 5.

### Follow-Up Questions
- "How does this relate to lower bound?" --> Lower bound uses `arr[mid] >= x` instead of `arr[mid] > x`. Upper bound of x = lower bound of x + 1 (for integers).
- "How would you implement C++'s `std::upper_bound`?" --> Exactly this algorithm.

---

## CONNECTIONS
- **Prerequisite:** Binary search basics (P001)
- **Same Pattern:** Lower Bound, Search Insert Position (P004), Floor and Ceil (P005)
- **Harder Variant:** Count Occurrences of x (upper_bound - lower_bound), Search in Rotated Array
- **This Unlocks:** Count of elements <= x, range queries, building blocks for more complex binary search problems
