# Floor and Ceil in Sorted Array

> **Batch 2 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array of integers `nums` and an integer `target`, find:
- **Floor:** The largest element in `nums` that is **less than or equal to** `target`. If no such element exists, return `-1`.
- **Ceil:** The smallest element in `nums` that is **greater than or equal to** `target`. If no such element exists, return `-1`.

Return both values as a pair `(floor, ceil)`.

**Constraints:**
- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- `nums` is sorted in non-decreasing order
- `-10^9 <= target <= 10^9`

**Examples:**

| Input | Floor | Ceil | Explanation |
|-------|-------|------|-------------|
| `nums = [1,2,8,10,10,12,19], target = 5` | `2` | `8` | 2 is the largest <= 5, 8 is the smallest >= 5 |
| `nums = [1,2,8,10,10,12,19], target = 10` | `10` | `10` | Target exists, so floor = ceil = target |
| `nums = [1,2,8,10,10,12,19], target = 0` | `-1` | `1` | No element <= 0, smallest element >= 0 is 1 |
| `nums = [1,2,8,10,10,12,19], target = 25` | `19` | `-1` | Largest element <= 25 is 19, no element >= 25 |

### Real-Life Analogy
> *You are shopping online with a budget of $50. The "floor" is the most expensive item you can actually afford -- the priciest thing at or under $50. The "ceil" is the cheapest item that meets or exceeds your budget -- the minimum spend to hit that mark. If all items cost more than $50, there is no floor. If all items cost less, there is no ceil.*

### Key Observations
1. Floor is the **rightmost element <= target** (go as far right as possible while staying <= target). Ceil is the **leftmost element >= target** (go as far left as possible while staying >= target). These are mirror-image searches. <-- This is the "aha" insight
2. When the target exists in the array, floor = ceil = target. Both searches converge to the same value.
3. Floor uses a binary search that records candidates and moves RIGHT. Ceil uses one that records candidates and moves LEFT. The direction of movement after recording is the key difference.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **sorted array** allows binary search, giving O(log n) lookup instead of O(n) linear scan.
- Random access by index is essential for jumping to midpoints.

### Pattern Recognition
- **Pattern:** Binary Search -- Bound Finding (floor tracks "last valid on left side", ceil tracks "first valid on right side")
- **Classification Cue:** "Sorted array + find nearest value satisfying a condition --> binary search with candidate tracking."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Scan the array. Track the largest value <= target (floor) and the smallest value >= target (ceil).

**Steps:**
1. Initialize `floor = -1`, `ceil = -1`.
2. For each element `nums[i]`:
   - If `nums[i] <= target`, update `floor = nums[i]` (keep overwriting -- the last one wins because array is sorted ascending).
   - If `nums[i] >= target` and `ceil == -1`, set `ceil = nums[i]` (first one wins).
3. Return `(floor, ceil)`.

**BUD Transition -- Bottleneck:** We traverse the entire array O(n), but sorting lets us binary search in O(log n).

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Two Separate Binary Searches
**What changed:** Run two independent binary searches -- one for floor, one for ceil.

**Floor Search (find largest <= target):**
1. Set `lo = 0`, `hi = n - 1`, `floor = -1`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] <= target`: update `floor = nums[mid]`, move `lo = mid + 1` (try to find a larger valid element).
   - Else: `hi = mid - 1`.

**Ceil Search (find smallest >= target):**
1. Set `lo = 0`, `hi = n - 1`, `ceil = -1`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] >= target`: update `ceil = nums[mid]`, move `hi = mid - 1` (try to find a smaller valid element).
   - Else: `lo = mid + 1`.

**Dry Run:** `nums = [1, 2, 8, 10, 10, 12, 19]`, `target = 5`

**Floor search:**

| Step | lo | hi | mid | nums[mid] | Condition | Action | floor |
|------|----|----|-----|-----------|-----------|--------|-------|
| 1    | 0  | 6  | 3   | 10        | 10 > 5    | hi=2 | -1 |
| 2    | 0  | 2  | 1   | 2         | 2 <= 5    | floor=2, lo=2 | 2 |
| 3    | 2  | 2  | 2   | 8         | 8 > 5     | hi=1 | 2 |
| end  | 2  | 1  | -   | -         | lo > hi   | return | **2** |

**Ceil search:**

| Step | lo | hi | mid | nums[mid] | Condition | Action | ceil |
|------|----|----|-----|-----------|-----------|--------|------|
| 1    | 0  | 6  | 3   | 10        | 10 >= 5   | ceil=10, hi=2 | 10 |
| 2    | 0  | 2  | 1   | 2         | 2 < 5     | lo=2 | 10 |
| 3    | 2  | 2  | 2   | 8         | 8 >= 5    | ceil=8, hi=1 | 8 |
| end  | 2  | 1  | -   | -         | lo > hi   | return | **8** |

**Result:** (floor=2, ceil=8)

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Single-Pass Binary Search
**What changed:** Combine both searches into one binary search pass. At each step, update both floor and ceil candidates.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`, `floor = -1`, `ceil = -1`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] == target`: both `floor = ceil = nums[mid]`, return immediately.
   - If `nums[mid] < target`: `floor = nums[mid]` (best floor candidate so far), `lo = mid + 1`.
   - If `nums[mid] > target`: `ceil = nums[mid]` (best ceil candidate so far), `hi = mid - 1`.
3. Return `(floor, ceil)`.

**Why this works:** Binary search partitions elements into "too small" (potential floors) and "too large" (potential ceilings). The last "too small" seen is the floor; the last "too large" seen is the ceil.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "Each comparison halves the search space. Two passes of O(log n) or one pass -- either way, O(log n)."
**Space:** O(1) -- "Only a handful of integer variables regardless of array size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Confusing floor/ceil movement directions: floor records and moves RIGHT (wants larger). Ceil records and moves LEFT (wants smaller).
2. Forgetting that when target is outside the array range, one of floor/ceil is -1.
3. Not updating the candidate before moving the pointer -- you lose the best-so-far value.

### Edge Cases to Test
- [ ] Target smaller than all elements (floor = -1, ceil = nums[0])
- [ ] Target larger than all elements (floor = nums[n-1], ceil = -1)
- [ ] Target exists in the array (floor = ceil = target)
- [ ] Target exists multiple times (duplicates)
- [ ] Single-element array, target matches
- [ ] Single-element array, target does not match
- [ ] Target falls exactly between two elements

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the largest element <= target (floor) and smallest element >= target (ceil) in a sorted array."
2. **Match:** "Sorted array + nearest value --> binary search with candidate tracking."
3. **Plan:** "For floor: when nums[mid] <= target, record and go right. For ceil: when nums[mid] >= target, record and go left. I can combine both into one pass."
4. **Implement:** Write the single-pass version. Explain the candidate tracking logic.
5. **Review:** Dry-run with target=5 in [1,2,8,10,10,12,19].
6. **Evaluate:** "O(log n) time, O(1) space."

### Follow-Up Questions
- "What if you need indices instead of values?" --> Track indices instead of values in the answer variables.
- "What about floor/ceil in a BST?" --> Same logic: follow left/right child pointers instead of computing mid.
- "How does this relate to C++ lower_bound/upper_bound?" --> ceil = `*lower_bound(target)`, floor = `*(upper_bound(target) - 1)` if valid.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), Lower Bound (P002), Upper Bound (P003)
- **Same Pattern:** Search Insert Position (P004), First and Last Occurrence (P006)
- **Harder Variant:** Floor/Ceil in BST, Find Closest Element in Sorted Array
- **This Unlocks:** Understanding how binary search finds "nearest" elements -- foundational for interpolation and approximation problems
