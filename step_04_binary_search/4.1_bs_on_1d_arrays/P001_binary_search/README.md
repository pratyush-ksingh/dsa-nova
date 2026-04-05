# Binary Search

> **Batch 1 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted (ascending) array of integers `nums` and an integer `target`, return the index of `target` in the array. If `target` does not exist, return `-1`.

**LeetCode #704**

**Constraints:**
- `1 <= nums.length <= 10^4`
- `-10^4 < nums[i], target < 10^4`
- All integers in `nums` are unique
- `nums` is sorted in ascending order

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [-1,0,3,5,9,12], target = 9` | `4` | 9 exists at index 4 |
| `nums = [-1,0,3,5,9,12], target = 2` | `-1` | 2 does not exist in the array |
| `nums = [5], target = 5` | `0` | Single element matches |

### Real-Life Analogy
> *Imagine looking up a word in a physical dictionary. You do not start at page 1 and read every page. Instead, you open the dictionary roughly in the middle. If the word you want comes after the current page, you flip to the right half; otherwise you flip to the left half. Each flip eliminates half the remaining pages. After about 20 flips you can find any word among a million entries. This "open-the-middle, eliminate-half" strategy is binary search.*

### Key Observations
1. The array is **sorted** -- this precondition is what makes halving the search space valid.
2. A single comparison at the midpoint tells us which half the target lives in.
3. Each step halves the remaining elements, so we reach the answer in at most log2(n) steps. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **sorted array** supports O(1) random access by index, which is essential for jumping to the midpoint.
- A linked list, even if sorted, would not help because reaching the midpoint takes O(n).

### Pattern Recognition
- **Pattern:** Binary Search (Divide & Conquer on a sorted sequence)
- **Classification Cue:** "Whenever you see _sorted array + search for a value_ --> think _binary search_ before anything else."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Walk through every element and compare with the target.

**Steps:**
1. For each index `i` from 0 to n-1:
   - If `nums[i] == target`, return `i`.
2. Return `-1`.

**Why it works:** Exhaustive search guarantees we find the target if it exists.

**BUD Transition -- Bottleneck:** We are ignoring the fact that the array is sorted. We examine every element even though a single comparison can eliminate half the array.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Iterative Binary Search
**What changed:** Leverage the sorted order. Maintain two pointers `lo` and `hi`. Compare the middle element with the target and eliminate one half.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo <= hi`:
   - Compute `mid = lo + (hi - lo) / 2` (avoids integer overflow).
   - If `nums[mid] == target`, return `mid`.
   - If `nums[mid] < target`, set `lo = mid + 1`.
   - Else set `hi = mid - 1`.
3. Return `-1`.

**Dry Run:** `nums = [-1, 0, 3, 5, 9, 12]`, `target = 9`

| Step | lo | hi | mid | nums[mid] | Action |
|------|----|----|-----|-----------|--------|
| 1    | 0  | 5  | 2   | 3         | 3 < 9, lo = 3 |
| 2    | 3  | 5  | 4   | 9         | 9 == 9, return 4 |

**Result:** 4

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Recursive Binary Search
**What changed:** Same logic expressed recursively. Useful to understand the divide-and-conquer nature explicitly.

**Steps:**
1. Define `search(nums, target, lo, hi)`.
2. Base case: if `lo > hi`, return `-1`.
3. Compute `mid`, compare, and recurse on the appropriate half.

**Note:** The iterative version is generally preferred in interviews because it avoids O(log n) call-stack overhead.

| Time | Space |
|------|-------|
| O(log n) | O(log n) due to recursion stack |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Each comparison eliminates half the remaining elements. Starting from n elements, after k comparisons we have n/2^k left. We stop when n/2^k = 1, so k = log2(n)."
**Space:** O(1) iterative, O(log n) recursive -- "The iterative version uses only a few variables. The recursive version adds one stack frame per halving."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Computing mid as `(lo + hi) / 2` -- causes integer overflow for large indices. Use `lo + (hi - lo) / 2`.
2. Writing `lo < hi` instead of `lo <= hi` -- misses the case when the target is the only element left.
3. Forgetting to return `-1` after the loop.

### Edge Cases to Test
- [ ] Target is the first element
- [ ] Target is the last element
- [ ] Single element array, target matches
- [ ] Single element array, target does not match
- [ ] Target smaller than all elements
- [ ] Target larger than all elements

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "The array is sorted and has unique elements. I need to find the target's index or return -1."
2. **Match:** "Sorted array + search --> binary search."
3. **Plan:** "I will maintain lo and hi pointers, compute mid, and shrink the window by half each step."
4. **Implement:** Write the iterative version. Mention the overflow-safe mid calculation.
5. **Review:** Walk through the dry run table above.
6. **Evaluate:** "O(log n) time, O(1) space. This is optimal -- you cannot search a sorted array faster."

### Follow-Up Questions
- "What if there are duplicates and you need the first occurrence?" --> Use lower bound variant (see P002).
- "What if the array is rotated?" --> Modified binary search (LeetCode #33).
- "Can you do this on an infinite stream?" --> Exponential search: double the range, then binary search.

---

## CONNECTIONS
- **Prerequisite:** Array traversal, understanding of sorted order
- **Same Pattern:** Lower Bound (P002), Upper Bound, Search Insert Position (LC #35)
- **Harder Variant:** Search in Rotated Sorted Array (LC #33), Find Peak Element (LC #162)
- **This Unlocks:** All binary search variants, binary search on answer space (Step 4.2)
