# Count Occurrences in Sorted Array

> **Batch 3 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array of integers `nums` and a target value `target`, find the **number of times** `target` appears in the array.

**Constraints:**
- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i], target <= 10^4`
- `nums` is sorted in non-decreasing order

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [1,1,2,2,2,2,3], target = 2` | `4` | 2 appears at indices 2, 3, 4, 5 |
| `nums = [1,1,2,2,2,2,3], target = 4` | `0` | 4 does not exist |
| `nums = [8,9,10,12,12,12], target = 12` | `3` | 12 appears at indices 3, 4, 5 |
| `nums = [5], target = 5` | `1` | Single element matches |

### Real-Life Analogy
> *Imagine a phone book sorted by last name. You want to count how many people share the last name "Smith." You do not scan every page -- instead you flip to the first "Smith" and the last "Smith," then subtract the positions: lastPos - firstPos + 1. Finding each boundary is a binary search. Two binary searches beat scanning the entire book.*

### Key Observations
1. In a sorted array, all occurrences of `target` are **contiguous**. So we just need the first and last positions. <-- This is the "aha" insight
2. `count = lastPosition - firstPosition + 1` (if target exists).
3. Finding the first and last positions is exactly the "First and Last Occurrence" problem (P006), solved with two binary searches.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- A **sorted array** allows binary search to find boundaries in O(log n).
- No extra data structure needed -- we just compute two indices and subtract.

### Pattern Recognition
- **Pattern:** Binary Search for boundary (lower/upper bound)
- **Classification Cue:** "Whenever you see _count in sorted array_ --> think _find first and last position_, then subtract."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Walk through the array and count matching elements.

**Steps:**
1. Initialize `count = 0`.
2. For each element, if it equals `target`, increment `count`.
3. Return `count`.

**BUD Transition -- Bottleneck:** We check every element even though the array is sorted. Once we pass the target range, the rest cannot match.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Two Binary Searches (First + Last Position)
**What changed:** Use binary search to find the first occurrence and the last occurrence. The count is `last - first + 1`.

**Steps:**
1. Find `first` = index of first occurrence of `target` (lower bound).
   - Binary search: when `nums[mid] == target`, move `hi = mid - 1` to search left.
2. If `first == -1` (target not found), return `0`.
3. Find `last` = index of last occurrence of `target` (upper bound).
   - Binary search: when `nums[mid] == target`, move `lo = mid + 1` to search right.
4. Return `last - first + 1`.

**Dry Run:** `nums = [1,1,2,2,2,2,3], target = 2`

Finding first occurrence:

| Step | lo | hi | mid | nums[mid] | Action |
|------|----|----|-----|-----------|--------|
| 1 | 0 | 6 | 3 | 2 | == target, ans=3, hi=2 |
| 2 | 0 | 2 | 1 | 1 | < target, lo=2 |
| 3 | 2 | 2 | 2 | 2 | == target, ans=2, hi=1 |
| done | lo > hi | | | | first = 2 |

Finding last occurrence:

| Step | lo | hi | mid | nums[mid] | Action |
|------|----|----|-----|-----------|--------|
| 1 | 0 | 6 | 3 | 2 | == target, ans=3, lo=4 |
| 2 | 4 | 6 | 5 | 2 | == target, ans=5, lo=6 |
| 3 | 6 | 6 | 6 | 3 | > target, hi=5 |
| done | lo > hi | | | | last = 5 |

**Result:** `5 - 2 + 1 = 4`

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Using Built-in Lower/Upper Bound
**What changed:** Use language built-ins: Java's `Collections.binarySearch` or Python's `bisect_left` and `bisect_right`. The count is `bisect_right(target) - bisect_left(target)`.

**Steps (Python):**
1. `left = bisect_left(nums, target)`.
2. `right = bisect_right(nums, target)`.
3. Return `right - left`.

**Note:** This is the same O(log n) algorithm wrapped in library calls. In interviews, implement it manually first, then mention the library.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Two binary searches, each halving the search space. 2 * log n = O(log n)."
**Space:** O(1) -- "Only a few pointer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using a single binary search that finds ANY occurrence, then scanning left/right -- this degrades to O(n) when all elements are the same.
2. Off-by-one when the target is at the very start or very end of the array.
3. Forgetting to handle the case where target does not exist (return 0, not -1).

### Edge Cases to Test
- [ ] Target not in array (return 0)
- [ ] Target appears once
- [ ] All elements are the target (count = n)
- [ ] Target is at the start
- [ ] Target is at the end
- [ ] Single element array, matches
- [ ] Single element array, does not match

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to count occurrences of target in a sorted array."
2. **Match:** "Sorted + count --> find first and last position using binary search, then subtract."
3. **Plan:** "Two binary searches: one biased left (first occurrence), one biased right (last occurrence). Count = last - first + 1."
4. **Implement:** Write the two binary search helpers. Handle the not-found case.
5. **Review:** Walk through the dry run.
6. **Evaluate:** "O(log n) time, O(1) space. Optimal for this problem."

### Follow-Up Questions
- "What if the array is not sorted?" --> Sort first O(n log n) + O(log n), or just linear scan O(n).
- "What if you need to count elements in a range [lo, hi]?" --> `bisect_right(hi) - bisect_left(lo)`.
- "What if the array has floating-point numbers?" --> Same binary search logic, be careful with equality comparisons.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), First and Last Occurrence (P006)
- **Same Pattern:** Lower Bound (P002), Upper Bound (P003), Search Insert Position
- **Harder Variant:** Count of Smaller Numbers After Self (LC #315)
- **This Unlocks:** Range-based counting queries on sorted data
