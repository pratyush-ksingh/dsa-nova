# Search Insert Position

> **Batch 2 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array of distinct integers `nums` and a `target` value, return the index if the target is found. If not, return the index where it would be inserted in order.

You must write an algorithm with O(log n) runtime complexity.

**LeetCode #35**

**Constraints:**
- `1 <= nums.length <= 10^4`
- `-10^4 <= nums[i] <= 10^4`
- `nums` contains distinct values sorted in ascending order
- `-10^4 <= target <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [1,3,5,6], target = 5` | `2` | 5 is found at index 2 |
| `nums = [1,3,5,6], target = 2` | `1` | 2 would be inserted at index 1 (between 1 and 3) |
| `nums = [1,3,5,6], target = 7` | `4` | 7 would be inserted at the end |
| `nums = [1,3,5,6], target = 0` | `0` | 0 would be inserted at the beginning |

### Real-Life Analogy
> *Imagine you have a bookshelf where books are arranged alphabetically. A new book arrives and you need to find where it belongs. You do not scan every book left to right. Instead, you look at the middle book -- if the new book comes after it, you move to the right half of the shelf. You keep halving until you find either the exact spot or the gap where the new book fits. The "insert position" is just the first book that would come after the new one.*

### Key Observations
1. "Find where target would be inserted" is the same as "find the first element >= target" -- this is exactly the **lower bound** operation. <-- This is the "aha" insight
2. If the target exists, the lower bound IS the target's index. If it does not exist, the lower bound IS the insertion point. One algorithm, two answers.
3. After binary search terminates without a match, `lo` always points to the correct insertion position because `lo` accumulates all the "go right" decisions.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **sorted array** gives O(1) random access, enabling the midpoint jump that makes binary search possible.
- The problem explicitly requires O(log n), ruling out linear scan for the final solution.

### Pattern Recognition
- **Pattern:** Binary Search -- Lower Bound variant
- **Classification Cue:** "Sorted array + find position (whether present or not) --> lower bound binary search."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Walk through the array. Return the first index where `nums[i] >= target`.

**Steps:**
1. For each index `i` from 0 to n-1:
   - If `nums[i] >= target`, return `i`.
2. If no such element found, return `n` (insert at the end).

**Why it works:** The first element >= target is exactly where target either already sits or should be inserted.

**BUD Transition -- Bottleneck:** We scan O(n) elements, but the array is sorted. A single comparison can eliminate half the search space.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Iterative Binary Search (Lower Bound)
**What changed:** Use binary search. When `nums[mid] < target`, move right. Otherwise, record `mid` as a candidate answer and move left to find an even earlier position.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`, `ans = n` (default: insert at end).
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] >= target`: update `ans = mid`, set `hi = mid - 1` (look for earlier position).
   - Else: set `lo = mid + 1`.
3. Return `ans`.

**Dry Run:** `nums = [1, 3, 5, 6]`, `target = 2`

| Step | lo | hi | mid | nums[mid] | Condition | Action | ans |
|------|----|----|-----|-----------|-----------|--------|-----|
| init | 0  | 3  | -   | -         | -         | ans = 4 | 4 |
| 1    | 0  | 3  | 1   | 3         | 3 >= 2    | ans=1, hi=0 | 1 |
| 2    | 0  | 0  | 0   | 1         | 1 < 2     | lo=1 | 1 |
| end  | 1  | 0  | -   | -         | lo > hi   | return 1 | **1** |

**Result:** 1 (insert 2 between index 0 and 1)

| Time | Space |
|------|-------|
| O(log n) | O(1) |

### Approach 3: Best -- Simplified Binary Search (lo converges to answer)
**What changed:** Instead of tracking a separate `ans`, observe that when the loop ends, `lo` is always the insertion position. This simplifies the code.

**Steps:**
1. Set `lo = 0`, `hi = n - 1`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `nums[mid] == target`: return `mid`.
   - If `nums[mid] < target`: `lo = mid + 1`.
   - Else: `hi = mid - 1`.
3. Return `lo`.

**Why `lo` works:** When the loop exits, `lo > hi`. Everything before `lo` is < target, everything from `lo` onward is >= target. So `lo` is the correct insertion index.

| Time | Space |
|------|-------|
| O(log n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log n) -- "We halve the search space each iteration. Starting with n elements, after log2(n) comparisons, only one element remains."
**Space:** O(1) -- "We use only a few integer variables regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Returning `-1` when target is not found -- this is NOT a plain search, we need the insertion index.
2. Forgetting the case where target is larger than all elements -- the answer is `n`, not `n-1`.
3. Off-by-one: using `hi = mid` instead of `hi = mid - 1` -- causes infinite loop.

### Edge Cases to Test
- [ ] Target is smaller than all elements (insert at 0)
- [ ] Target is larger than all elements (insert at n)
- [ ] Target equals the first element
- [ ] Target equals the last element
- [ ] Single-element array, target matches
- [ ] Single-element array, target does not match

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I have a sorted array of distinct integers and a target. I need its index if present, or where it would be inserted."
2. **Match:** "Sorted array + position finding --> binary search. Specifically, this is a lower bound problem."
3. **Plan:** "I will run standard binary search. When the loop ends without finding the target, `lo` is the insertion point."
4. **Implement:** Write the simplified version (Approach 3). Mention overflow-safe mid.
5. **Review:** Dry-run with target=2 in [1,3,5,6] to show lo converges to 1.
6. **Evaluate:** "O(log n) time, O(1) space. This matches the required complexity."

### Follow-Up Questions
- "What if duplicates are allowed and you want the leftmost insertion point?" --> This becomes std::lower_bound. Use the `ans` tracking version (Approach 2).
- "What if you want the rightmost insertion point?" --> Upper bound: change `>=` to `>`.
- "How does this relate to `bisect_left` in Python?" --> `bisect_left` does exactly this -- returns the leftmost position to insert target.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), Lower Bound (P002), Upper Bound (P003)
- **Same Pattern:** Floor/Ceil in Sorted Array (P005), First and Last Occurrence (P006)
- **Harder Variant:** Search in Rotated Sorted Array (LC #33)
- **This Unlocks:** All binary search problems that return positions rather than boolean found/not-found
