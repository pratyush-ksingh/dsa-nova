# Kth Missing Positive Number

> **Batch 3 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array `arr` of positive integers sorted in a **strictly increasing order**, and an integer `k`, return the `k`-th positive integer that is **missing** from this array.

**LeetCode #1539**

**Constraints:**
- `1 <= arr.length <= 1000`
- `1 <= arr[i] <= 1000`
- `1 <= k <= 1000`
- `arr[i] < arr[i+1]` (strictly increasing)

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `arr = [2,3,4,7,11], k = 5` | `9` | Missing: 1,5,6,8,9,10,... The 5th missing is 9 |
| `arr = [1,2,3,4], k = 2` | `6` | Missing: 5,6,7,... The 2nd missing is 6 |
| `arr = [5,6,7,8,9], k = 3` | `3` | Missing: 1,2,3,4. The 3rd missing is 3 |

### Real-Life Analogy
> *Imagine numbered parking spots 1, 2, 3, 4, ... along a street. Some spots have cars in them (the array values). You walk along counting the empty spots. "That is empty spot #1, #2, ..." You want to find which parking spot number is the k-th empty one. Instead of walking one by one, you can jump to the middle of the street, count how many spots SHOULD be occupied vs. how many actually are, and deduce how many are missing before that point.*

### Key Observations
1. At index `i` (0-based), the ideal value without any missing numbers would be `i + 1`. The number of missing values before `arr[i]` is `arr[i] - (i + 1)`. <-- This is the "aha" insight
2. We can binary search on the array for the position where the missing count first reaches or exceeds `k`.
3. Once we find the right position, the answer is `k + position` (the k-th missing number, offset by how many array elements precede it).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- The "missing count" function `arr[i] - (i+1)` is **monotonically non-decreasing** across the sorted array. This makes it a perfect candidate for binary search.
- No extra data structure needed.

### Pattern Recognition
- **Pattern:** Binary Search on a derived monotonic function
- **Classification Cue:** "Whenever you have a sorted array and need to find a position based on a monotonic computed property --> binary search on that property."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Count One by One
**Idea:** Walk through positive integers 1, 2, 3, ... and skip those present in the array. Count until we reach the k-th missing one.

**Steps:**
1. Initialize `missing = 0`, `current = 0` (index into arr), `num = 0`.
2. Loop:
   - Increment `num`.
   - If `current < n` and `arr[current] == num`, advance `current`.
   - Else increment `missing`.
   - If `missing == k`, return `num`.

**Dry Run:** `arr = [2,3,4,7,11], k = 5`

| num | arr[current] | match? | missing | k reached? |
|-----|-------------|--------|---------|------------|
| 1 | 2 | no | 1 | no |
| 2 | 2 | yes | 1 | no |
| 3 | 3 | yes | 1 | no |
| 4 | 4 | yes | 1 | no |
| 5 | 7 | no | 2 | no |
| 6 | 7 | no | 3 | no |
| 7 | 7 | yes | 3 | no |
| 8 | 11 | no | 4 | no |
| 9 | 11 | no | 5 | yes! |

**Result:** 9

**BUD Transition -- Bottleneck:** We iterate up to `arr[n-1] + k` in the worst case. Can we jump directly to the answer?

| Time | Space |
|------|-------|
| O(n + k) | O(1) |

### Approach 2: Optimal -- Binary Search on Missing Count
**What changed:** For each index `i`, the number of missing integers before `arr[i]` is `arr[i] - (i + 1)`. This is monotonically non-decreasing. Binary search for the rightmost index where `missingCount < k`.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`.
   - `missingCount = arr[mid] - (mid + 1)`.
   - If `missingCount < k`: `lo = mid + 1` (need more missing numbers).
   - Else: `hi = mid - 1`.
3. At the end, `lo` is the first index where missingCount >= k.
4. Answer: `k + lo` (k missing numbers + lo array elements that come before them = the actual number).

**Why `k + lo`?** After the binary search, `hi` is the last index where fewer than k numbers are missing. The k-th missing number sits after `arr[hi]`. We know `arr[hi] = (hi + 1) + missingBeforeHi`. The answer is `arr[hi] + (k - missingBeforeHi)` = `(hi + 1) + missingBeforeHi + k - missingBeforeHi` = `hi + 1 + k` = `lo + k` (since `lo = hi + 1`).

**Dry Run:** `arr = [2,3,4,7,11], k = 5`

| Step | lo | hi | mid | arr[mid] | missing = arr[mid]-(mid+1) | Action |
|------|----|----|-----|----------|---------------------------|--------|
| 1 | 0 | 4 | 2 | 4 | 4-3=1 | 1 < 5, lo=3 |
| 2 | 3 | 4 | 3 | 7 | 7-4=3 | 3 < 5, lo=4 |
| 3 | 4 | 4 | 4 | 11 | 11-5=6 | 6 >= 5, hi=3 |
| done | lo=4 > hi=3 | | | | |

**Result:** `k + lo = 5 + 4 = 9`

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Same as Optimal (Cleaner Formulation)
**What changed:** The binary search on missing count IS the best approach. We present a slightly cleaner version that finds the lower bound directly.

**Steps (Python-style using bisect concept):**
1. For each index, define `f(i) = arr[i] - (i + 1)` = number of missing before index i.
2. Find the first index where `f(i) >= k` using lower bound binary search.
3. Answer = `k + that index`.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "We binary search over n array elements. Each step halves the search space."
**Space:** O(1) -- "Only a few variables for pointers."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Off-by-one with 0-based vs 1-based indexing: the ideal value at index `i` (0-based) is `i + 1`, not `i`.
2. Forgetting that the k-th missing number might be BEFORE the first array element (e.g., `arr = [5,6,7], k = 3` --> answer is 3).
3. Forgetting that the k-th missing number might be AFTER the last array element.
4. Using `arr[mid] - mid` instead of `arr[mid] - (mid + 1)` -- off by one.

### Edge Cases to Test
- [ ] k-th missing is before the first element (`arr = [5], k = 1` --> 1)
- [ ] k-th missing is after the last element (`arr = [1,2,3], k = 1` --> 4)
- [ ] No missing numbers in the array range (`arr = [1,2,3,4,5], k = 1` --> 6)
- [ ] Single element array
- [ ] Very large gap between elements

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the k-th positive integer missing from a sorted array."
2. **Match:** "The missing count at each index is monotonic --> binary search on that derived function."
3. **Plan:** "Compute `missing = arr[mid] - (mid+1)`. Binary search for where missing count crosses k. Answer is `k + lo`."
4. **Implement:** Write the binary search. Derive the formula `k + lo`.
5. **Review:** Walk through the dry run. Verify with edge cases.
6. **Evaluate:** "O(log n) time, O(1) space."

### Follow-Up Questions
- "What if the array is not sorted?" --> Sort first, then apply the same approach. O(n log n).
- "What if there can be duplicates?" --> The strictly increasing constraint is critical. With duplicates, the missing count formula breaks and you would need a different approach.
- "Can you solve it in O(1) if k > arr[n-1]?" --> Yes: answer = k + n (all n elements are before the answer).

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), understanding of monotonic functions
- **Same Pattern:** Search Insert Position (P004), First and Last Occurrence (P006)
- **Harder Variant:** Missing Element in Sorted Array (LC #1060), Kth Smallest Number in Multiplication Table (LC #668)
- **This Unlocks:** Binary search on computed/derived properties rather than raw values
