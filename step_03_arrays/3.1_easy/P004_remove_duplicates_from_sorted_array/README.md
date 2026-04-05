# Remove Duplicates from Sorted Array

> **Batch 2 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array `nums` in non-decreasing order, remove the duplicates **in-place** such that each unique element appears only once. Return the number of unique elements `k`. The first `k` elements of `nums` should hold the unique elements in order. Elements beyond position `k` do not matter.

**LeetCode #26**

**Constraints:**
- `1 <= nums.length <= 3 * 10^4`
- `-100 <= nums[i] <= 100`
- `nums` is sorted in non-decreasing order.

**Examples:**

| Input | Output (k) | nums after | Explanation |
|-------|------------|------------|-------------|
| `[1, 1, 2]` | `2` | `[1, 2, _]` | Two unique elements |
| `[0,0,1,1,1,2,2,3,3,4]` | `5` | `[0,1,2,3,4,_,_,_,_,_]` | Five unique elements |
| `[1]` | `1` | `[1]` | Already unique |
| `[1, 1, 1, 1]` | `1` | `[1, _, _, _]` | All duplicates |

### Real-Life Analogy
> *Imagine you have a sorted stack of index cards with names on them, and many names appear more than once. You want to keep only one card per unique name. You place the first card on a new pile. Then, for each subsequent card, you compare it to the top of your new pile -- if it is different, you place it on top; if it is the same, you discard it. At the end, your new pile has exactly one card per unique name. The two-pointer technique does this in-place within the same array.*

### Key Observations
1. The array is **already sorted**, so all duplicates are adjacent. We never need to search for duplicates -- they are right next to each other.
2. We need to do this **in-place** -- no new array allowed. The "write pointer" technique lets us overwrite duplicate slots. <-- This is the "aha" insight
3. The first element is always unique, so we start our write pointer at index 1.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- The input is an **Array** and we must modify it in-place. Arrays give O(1) indexed access and overwrite, which is all we need.
- A HashSet could find unique elements, but would use O(n) extra space and lose the sorted order.

### Pattern Recognition
- **Pattern:** Two Pointers (read pointer + write pointer on the same array)
- **Classification Cue:** "When you see _remove/filter elements in-place from a sorted array_ --> think _two pointers: one reads, one writes_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Use Extra Set/Array
**Idea:** Copy unique elements to a new data structure, then copy them back.

**Steps:**
1. Create an auxiliary list `unique`.
2. Iterate through `nums`, adding each element to `unique` only if it differs from the last added element.
3. Copy `unique` back into `nums`.
4. Return `len(unique)`.

**Why it works:** The sorted property means we just compare each element to the previous unique one.

**Why we move on:** **Wasted space** -- we use O(n) extra space for the auxiliary list, but the problem asks for in-place.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 2: Optimal -- Two Pointers In-Place
**What changed (BUD -- Duplicated Work/Space):** Instead of an auxiliary array, use a write pointer `w` to track where the next unique element should go.

**Steps:**
1. If `nums` is empty, return 0.
2. Initialize write pointer `w = 1` (first element is always kept).
3. For read pointer `r` from 1 to n-1:
   - If `nums[r] != nums[r-1]` (new unique element found):
     - Set `nums[w] = nums[r]`
     - Increment `w`
4. Return `w`.

**Dry Run:** Input = `[0, 0, 1, 1, 1, 2, 2, 3, 3, 4]`

| Step | r | nums[r] | nums[r-1] | Different? | w | Action | Array State |
|------|---|---------|-----------|------------|---|--------|-------------|
| Init | - | - | - | - | 1 | - | `[0,0,1,1,1,2,2,3,3,4]` |
| 1 | 1 | 0 | 0 | No | 1 | Skip | - |
| 2 | 2 | 1 | 0 | Yes | 2 | Write 1 at [1] | `[0,1,1,1,1,2,2,3,3,4]` |
| 3 | 3 | 1 | 1 | No | 2 | Skip | - |
| 4 | 4 | 1 | 1 | No | 2 | Skip | - |
| 5 | 5 | 2 | 1 | Yes | 3 | Write 2 at [2] | `[0,1,2,1,1,2,2,3,3,4]` |
| 6 | 6 | 2 | 2 | No | 3 | Skip | - |
| 7 | 7 | 3 | 2 | Yes | 4 | Write 3 at [3] | `[0,1,2,3,1,2,2,3,3,4]` |
| 8 | 8 | 3 | 3 | No | 4 | Skip | - |
| 9 | 9 | 4 | 3 | Yes | 5 | Write 4 at [4] | `[0,1,2,3,4,2,2,3,3,4]` |

**Result:** `k = 5`, first 5 elements = `[0, 1, 2, 3, 4]`

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* This is optimal -- O(n) time is necessary to read all elements, and O(1) space is the best possible since we modify in-place.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "One pass through the array. Each element is read exactly once and written at most once."
**Space:** O(1) -- "Only two pointer variables (r and w), regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Comparing `nums[r]` to `nums[w-1]` instead of `nums[r-1]` -- both work, but comparing to `nums[r-1]` is simpler since the input is sorted and we haven't disturbed earlier positions.
2. Forgetting to handle the single-element case (though the loop naturally handles it since it won't execute).
3. Returning `w - 1` instead of `w` (off-by-one: `w` is already the count since it started at 1).

### Edge Cases to Test
- [ ] Single element `[1]` --> k = 1
- [ ] All same `[3, 3, 3, 3]` --> k = 1
- [ ] Already unique `[1, 2, 3, 4]` --> k = 4
- [ ] Two elements, duplicates `[1, 1]` --> k = 1
- [ ] Two elements, unique `[1, 2]` --> k = 2
- [ ] Negative numbers `[-1, -1, 0, 0, 1]` --> k = 3

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "The array is sorted, and I need to remove duplicates in-place, returning the count of unique elements."
2. **Match:** "Sorted + in-place removal = classic two-pointer pattern."
3. **Plan:** "I'll use a write pointer starting at 1. My read pointer scans from left to right. Whenever I see a new value, I write it at the write pointer and advance."
4. **Implement:** Write the concise loop.
5. **Review:** Trace through the example, showing the array transforming.
6. **Evaluate:** "O(n) time, O(1) space. This is optimal."

### Follow-Up Questions
- "What if duplicates are allowed up to 2 times?" --> LC #80. Same pattern but allow writing twice before skipping. Compare with `nums[w-2]`.
- "What if the array is unsorted?" --> Sort first (O(n log n)) or use a HashSet (O(n) time, O(n) space).
- "Can you do this for a linked list?" --> Same two-pointer idea, but with node pointers.

---

## CONNECTIONS
- **Prerequisite:** Two-pointer technique, array traversal
- **Same Pattern:** Remove Element (LC #27), Move Zeros (LC #283)
- **Harder Variant:** Remove Duplicates II (LC #80, allow at most 2)
- **This Unlocks:** Understanding in-place array modification, which appears in many interview problems
