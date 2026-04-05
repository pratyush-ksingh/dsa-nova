# Move Zeros to End

> **Batch 3 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an integer array `nums`, move all `0`s to the end of the array while maintaining the relative order of the non-zero elements. You must do this **in-place** without making a copy of the array.

**LeetCode #283**

**Constraints:**
- `1 <= n <= 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[0, 1, 0, 3, 12]` | `[1, 3, 12, 0, 0]` | Non-zeros maintain order: 1, 3, 12 |
| `[0]` | `[0]` | Single zero stays put |
| `[1, 2, 3]` | `[1, 2, 3]` | No zeros -- nothing changes |
| `[0, 0, 0, 1]` | `[1, 0, 0, 0]` | All zeros shift to end |

### Real-Life Analogy
> *Imagine a row of chairs in a waiting room. Some are empty (zeros) and some are occupied (non-zeros). You want to slide all occupied chairs to the front so there are no gaps, and leave all empty chairs at the back. You do this by having a "next available spot" pointer: each occupied chair scoots to that spot, and you advance the pointer.*

### Key Observations
1. We only care about the relative order of non-zero elements -- zeros just fill the remaining space.
2. This is a partitioning problem: non-zeros go left, zeros go right.
3. A two-pointer approach (write pointer + read pointer) lets us do this in one pass. <-- This is the "aha" insight. The write pointer marks "where the next non-zero should go."

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- In-place array manipulation with two pointers. No auxiliary structures needed.
- The write pointer technique is the classic tool for "compact elements that match a condition to the front."

### Pattern Recognition
- **Pattern:** Two Pointers (read/write or slow/fast)
- **Classification Cue:** "When you see _move/remove elements in-place maintaining order_ --> think _write pointer that only advances on the condition_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Extra Array)
**Idea:** Create a new array, copy non-zeros first, then fill zeros.

**Steps:**
1. Create a result array of size `n`.
2. First pass: copy all non-zero elements into `result` sequentially.
3. Fill remaining positions with `0`.
4. Copy `result` back to `nums`.

**Why it works:** Non-zeros are picked in order, zeros fill the rest.

**BUD -- Unnecessary Space:** We use O(n) extra space, but the problem asks for in-place.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 2: Optimal -- Two-Pointer (Write Pointer)
**What changed:** Use a `writeIdx` that tracks where the next non-zero should be placed. Iterate with a `readIdx`. After placing all non-zeros, fill the rest with zeros.

**Steps:**
1. Initialize `writeIdx = 0`.
2. For each `readIdx` from `0` to `n - 1`:
   - If `nums[readIdx] != 0`, set `nums[writeIdx] = nums[readIdx]` and increment `writeIdx`.
3. Fill `nums[writeIdx..n-1]` with zeros.

**Dry Run:** Input = `[0, 1, 0, 3, 12]`

| readIdx | nums[readIdx] | Action | writeIdx | Array State |
|---------|--------------|--------|----------|-------------|
| 0 | 0 | Skip | 0 | `[0, 1, 0, 3, 12]` |
| 1 | 1 | Write, advance | 1 | `[1, 1, 0, 3, 12]` |
| 2 | 0 | Skip | 1 | `[1, 1, 0, 3, 12]` |
| 3 | 3 | Write, advance | 2 | `[1, 3, 0, 3, 12]` |
| 4 | 12 | Write, advance | 3 | `[1, 3, 12, 3, 12]` |
| Fill | - | zeros from 3 | - | `[1, 3, 12, 0, 0]` |

**Result:** `[1, 3, 12, 0, 0]`

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Swap Variant (Single Pass, No Backfill)
**What changed:** Instead of a separate fill step, swap the non-zero element with the element at `writeIdx`. This moves zeros backward naturally as non-zeros are swapped forward.

**Steps:**
1. Initialize `writeIdx = 0`.
2. For each `i` from `0` to `n - 1`:
   - If `nums[i] != 0`, swap `nums[i]` and `nums[writeIdx]`, then increment `writeIdx`.

This achieves the same result in a single clean pass with no separate fill loop.

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan through the array once. Each element is read exactly once and written at most once."
**Space:** O(1) -- "We use only a single pointer variable. Everything is done in-place."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to backfill zeros in Approach 2 -- leaves stale values at the end.
2. Swapping when `i == writeIdx` -- not wrong but wastes a swap. Can add a guard.
3. Using `remove()` + `append()` in Python lists -- this is O(n) per removal, making total O(n^2).

### Edge Cases to Test
- [ ] No zeros `[1, 2, 3]` --> unchanged
- [ ] All zeros `[0, 0, 0]` --> unchanged
- [ ] Single element `[0]` or `[5]`
- [ ] Zeros at start `[0, 0, 1, 2]` --> `[1, 2, 0, 0]`
- [ ] Zeros at end `[1, 2, 0, 0]` --> already correct
- [ ] Negative numbers mixed `[-1, 0, 3, 0, -5]` --> `[-1, 3, -5, 0, 0]`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "In-place means I cannot create a new array? And I must maintain relative order of non-zeros?"
2. **Approach:** "I'll use a write pointer. Scan left to right -- every non-zero gets placed at the write pointer and the pointer advances. Then fill remaining spots with zeros."
3. **Code:** Write the loop, then mention the swap variant as an optimization.
4. **Test:** Walk through `[0, 1, 0, 3, 12]` showing pointer positions.

### Follow-Up Questions
- "What if you need to move a specific value (not just zero)?" --> Same technique, just change the condition.
- "What if order doesn't matter?" --> Swap zeros with last non-zero (two-pointer from both ends).
- "Can you do it with minimum writes?" --> The swap variant minimizes writes to non-zero positions.

---

## CONNECTIONS
- **Prerequisite:** Two-pointer technique basics
- **Same Pattern:** Remove Element (LeetCode #27), Remove Duplicates from Sorted Array
- **Harder Variant:** Sort Colors (Dutch National Flag -- three-way partition)
- **This Unlocks:** Understanding the write-pointer pattern used in countless in-place array problems
