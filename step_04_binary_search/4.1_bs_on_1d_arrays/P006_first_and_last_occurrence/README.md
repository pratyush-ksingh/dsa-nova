# First and Last Occurrence

> **Batch 3 of 12** | **Topic:** Binary Search | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a sorted array of integers `nums` and a target value `target`, find the **starting and ending position** of `target` in the array. If `target` is not found, return `[-1, -1]`.

**LeetCode #34 -- Find First and Last Position of Element in Sorted Array**

**Constraints:**
- `0 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- `nums` is sorted in non-decreasing order
- `-10^9 <= target <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [5,7,7,8,8,10], target = 8` | `[3, 4]` | 8 first appears at index 3, last at 4 |
| `nums = [5,7,7,8,8,10], target = 6` | `[-1, -1]` | 6 does not exist |
| `nums = [], target = 0` | `[-1, -1]` | Empty array |
| `nums = [1], target = 1` | `[0, 0]` | Single element matches |

### Real-Life Analogy
> *Imagine a library shelf with books sorted by ISBN. You want to pull all copies of a specific book. Rather than scanning the entire shelf, you jump to the middle. If the ISBN is too low, go right; too high, go left. You do this twice: once to find the leftmost copy, once to find the rightmost copy. Two quick searches bracket the entire range.*

### Key Observations
1. All occurrences of `target` form a **contiguous block** in the sorted array.
2. We need two separate binary searches: one biased **left** (first occurrence), one biased **right** (last occurrence). <-- This is the "aha" insight
3. When `nums[mid] == target`, instead of returning immediately, we record the answer and continue searching in the appropriate direction.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- A **sorted array** allows O(log n) binary search for each boundary.
- No extra data structures needed -- just two pointer variables per search.

### Pattern Recognition
- **Pattern:** Binary Search for boundary (lower/upper bound)
- **Classification Cue:** "Whenever you see _first/last position in sorted array_ --> think _two binary searches with directional bias_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Walk through the array. Record the first and last indices where `nums[i] == target`.

**Steps:**
1. Initialize `first = -1`, `last = -1`.
2. For each index `i` from 0 to n-1:
   - If `nums[i] == target` and `first == -1`, set `first = i`.
   - If `nums[i] == target`, set `last = i`.
3. Return `[first, last]`.

**BUD Transition -- Bottleneck:** We scan every element even though the array is sorted. We can leverage sorted order to jump to boundaries in O(log n).

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Two Binary Searches
**What changed:** Use binary search twice. For the first occurrence, when `nums[mid] == target`, move `hi = mid - 1` to keep searching left. For the last occurrence, move `lo = mid + 1` to keep searching right.

**Steps:**
1. **findFirst:** lo=0, hi=n-1, ans=-1. While lo<=hi: mid = lo+(hi-lo)/2. If nums[mid]==target, ans=mid, hi=mid-1. If nums[mid]<target, lo=mid+1. Else hi=mid-1.
2. If first == -1, return [-1, -1].
3. **findLast:** lo=0, hi=n-1, ans=-1. While lo<=hi: mid = lo+(hi-lo)/2. If nums[mid]==target, ans=mid, lo=mid+1. If nums[mid]<target, lo=mid+1. Else hi=mid-1.
4. Return [first, last].

**Dry Run:** `nums = [5,7,7,8,8,10], target = 8`

Finding first occurrence:

| Step | lo | hi | mid | nums[mid] | Action |
|------|----|----|-----|-----------|--------|
| 1 | 0 | 5 | 2 | 7 | < target, lo=3 |
| 2 | 3 | 5 | 4 | 8 | == target, ans=4, hi=3 |
| 3 | 3 | 3 | 3 | 8 | == target, ans=3, hi=2 |
| done | lo > hi | | | | first = 3 |

Finding last occurrence:

| Step | lo | hi | mid | nums[mid] | Action |
|------|----|----|-----|-----------|--------|
| 1 | 0 | 5 | 2 | 7 | < target, lo=3 |
| 2 | 3 | 5 | 4 | 8 | == target, ans=4, lo=5 |
| 3 | 5 | 5 | 5 | 10 | > target, hi=4 |
| done | lo > hi | | | | last = 4 |

**Result:** [3, 4]

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Using Lower/Upper Bound (bisect)
**What changed:** Use `bisect_left(target)` and `bisect_right(target) - 1` to find boundaries directly. Verify the element at bisect_left actually equals target.

**Steps (Python):**
1. `left = bisect_left(nums, target)`.
2. If `left == len(nums)` or `nums[left] != target`, return [-1, -1].
3. `right = bisect_right(nums, target) - 1`.
4. Return [left, right].

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Two binary searches, each O(log n). Total = 2*log n = O(log n)."
**Space:** O(1) -- "Only a few integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Returning immediately when `nums[mid] == target` -- this gives any occurrence, not first/last.
2. Finding one occurrence then scanning linearly left/right -- degrades to O(n) when all elements equal target.
3. Off-by-one on the `bisect_right - 1` for the last position.

### Edge Cases to Test
- [ ] Empty array
- [ ] Target not in array
- [ ] Target appears once
- [ ] All elements equal target
- [ ] Target at first/last position
- [ ] Two-element array

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Find the first and last positions of target in a sorted array."
2. **Match:** "Sorted + boundary finding --> two binary searches."
3. **Plan:** "Binary search biased left for first, biased right for last."
4. **Implement:** Write findFirst and findLast helpers.
5. **Review:** Walk through the dry run.
6. **Evaluate:** "O(log n) time, O(1) space. Must be O(log n) per the constraint."

### Follow-Up Questions
- "How would you count occurrences?" --> `last - first + 1` (see P007).
- "What if the array has duplicates and you want all unique ranges?" --> Iterate using next lower_bound after each upper_bound.
- "What if the array is rotated?" --> Find pivot first, then binary search in appropriate half.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), Lower Bound (P002), Upper Bound (P003)
- **Same Pattern:** Count Occurrences (P007), Search Insert Position (P004)
- **Harder Variant:** Search in Rotated Sorted Array (P008)
- **This Unlocks:** Count occurrences, range queries on sorted data
