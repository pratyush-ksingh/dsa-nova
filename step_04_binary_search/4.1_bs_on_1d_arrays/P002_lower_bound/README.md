# Lower Bound

> **Batch 1 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted (ascending) array of integers `arr` of size `n` and an integer `target`, find the **lower bound** of `target`. The lower bound is defined as the **smallest index** `i` such that `arr[i] >= target`. If no such index exists (all elements are smaller than target), return `n`.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= arr[i] <= 10^9`
- `1 <= target <= 10^9`
- Array is sorted in non-decreasing order

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [1, 2, 2, 3, 3, 5], target = 2` | `1` | arr[1] = 2 is the first element >= 2 |
| `arr = [3, 5, 8, 15, 19], target = 9` | `3` | arr[3] = 15 is the first element >= 9 |
| `arr = [1, 2, 3], target = 4` | `3` | No element >= 4, return n = 3 |
| `arr = [3, 5, 8], target = 1` | `0` | arr[0] = 3 >= 1, so lower bound is index 0 |

### Real-Life Analogy
> *Imagine you are at a cinema ticket counter where seats are priced at fixed amounts: $5, $10, $15, $20. You have $12 and want the cheapest seat that costs at least $12. You skip $5 and $10 (too cheap), and the first seat that meets your budget threshold is $15. Finding that $15 seat is the "lower bound" -- the first option that is greater than or equal to your requirement.*

### Key Observations
1. The array is sorted, so all elements >= target are contiguous at the right end. We just need the leftmost one.
2. Unlike exact-match binary search, we do NOT stop when we find `target`. We keep searching left to find the first occurrence >= target.
3. The answer is always a valid index in `[0, n]` -- the value `n` acts as a sentinel meaning "nothing qualifies." <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **sorted array** allows binary search. The "lower bound" is essentially the C++ `std::lower_bound` / Python `bisect_left` operation.
- No extra data structure needed -- we are simply narrowing down an index range.

### Pattern Recognition
- **Pattern:** Binary Search -- Boundary Finding (find the first element satisfying a condition)
- **Classification Cue:** "Whenever you see _find first/last element satisfying a monotonic condition in sorted data_ --> think _binary search with boundary tracking_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Walk through the array from left to right. Return the first index where `arr[i] >= target`.

**Steps:**
1. For each index `i` from 0 to n-1:
   - If `arr[i] >= target`, return `i`.
2. Return `n`.

**Why it works:** By scanning left to right, the first element >= target is by definition the lower bound.

**BUD Transition -- Bottleneck:** We check up to n elements, but since the array is sorted, one comparison at the midpoint can eliminate half the candidates.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Binary Search with Answer Tracking
**What changed:** Use binary search. When `arr[mid] >= target`, record `mid` as a candidate answer and search left for an even smaller index. When `arr[mid] < target`, search right.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`, `ans = n` (default: no element qualifies).
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `arr[mid] >= target`: `ans = mid`, `hi = mid - 1` (search left for smaller index).
   - Else: `lo = mid + 1`.
3. Return `ans`.

**Dry Run:** `arr = [1, 2, 2, 3, 3, 5]`, `target = 2`

| Step | lo | hi | mid | arr[mid] | Condition | ans | Action |
|------|----|----|-----|----------|-----------|-----|--------|
| Init | 0  | 5  | -   | -        | -         | 6   | - |
| 1    | 0  | 5  | 2   | 2        | 2 >= 2    | 2   | hi = 1 |
| 2    | 0  | 1  | 0   | 1        | 1 < 2     | 2   | lo = 1 |
| 3    | 1  | 1  | 1   | 2        | 2 >= 2    | 1   | hi = 0 |
| End  | 1  | 0  | -   | -        | lo > hi   | 1   | return 1 |

**Result:** 1

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Built-in Library
**What changed:** Use the language's built-in binary search for lower bound.

**Steps:**
1. Java: `Collections.binarySearch` or manual (no direct lower-bound in standard library).
2. Python: `bisect.bisect_left(arr, target)`.
3. C++: `std::lower_bound(arr.begin(), arr.end(), target)`.

**Note:** In interviews, you should implement it manually first, then mention the library as a production shortcut.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Each iteration halves the search space. Starting from n elements, we need log2(n) iterations."
**Space:** O(1) -- "We use only three variables: lo, hi, ans."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Returning `mid` immediately when `arr[mid] == target` -- this gives *an* occurrence, not necessarily the *first* one >= target.
2. Forgetting to initialize `ans = n` -- when no element qualifies, the answer must be n, not -1.
3. Confusing lower bound with upper bound: lower bound is `>= target`, upper bound is `> target`.

### Edge Cases to Test
- [ ] Target smaller than all elements --> return 0
- [ ] Target larger than all elements --> return n
- [ ] Target equals the first element
- [ ] Target equals the last element
- [ ] All elements are the same and equal to target
- [ ] Single element array

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the first index where the element is >= target. If none exists, return n."
2. **Match:** "Sorted array + find boundary --> binary search with answer tracking."
3. **Plan:** "I will binary search: when mid qualifies (>= target), save it and go left. Otherwise go right."
4. **Implement:** Write the iterative approach with `ans = n` default.
5. **Review:** Trace through the dry run.
6. **Evaluate:** "O(log n) time, O(1) space."

### Follow-Up Questions
- "What is the difference between lower bound and upper bound?" --> Lower bound: first `>= target`. Upper bound: first `> target`.
- "How would you find the number of occurrences of target?" --> `upper_bound - lower_bound`.
- "What is `bisect_left` vs `bisect_right` in Python?" --> `bisect_left` = lower bound, `bisect_right` = upper bound.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001)
- **Same Pattern:** Upper Bound, Search Insert Position (LC #35), First/Last Position of Element (LC #34)
- **Harder Variant:** Find First and Last Position of Element in Sorted Array
- **This Unlocks:** Floor/Ceil in sorted array, count of occurrences, all boundary-based binary search problems
