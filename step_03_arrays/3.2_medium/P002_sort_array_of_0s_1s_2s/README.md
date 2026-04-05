# Sort Array of 0s 1s 2s

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #75 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array `nums` containing only `0`, `1`, and `2`, sort it **in-place** so that elements of the same kind are adjacent, in the order `0`, `1`, `2`.

You must do this **without** using the built-in sort function. Try to do it in a **single pass**.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[2, 0, 2, 1, 1, 0]` | `[0, 0, 1, 1, 2, 2]` | All 0s first, then 1s, then 2s |
| `[2, 0, 1]` | `[0, 1, 2]` | Already one of each |
| `[0]` | `[0]` | Single element, no change |

**Constraints:**
- `1 <= nums.length <= 300`
- `nums[i]` is either `0`, `1`, or `2`

### Real-Life Analogy
> *A flag sorting problem: you have a pile of red, white, and blue cards shuffled together. A brute approach counts each color, then lays them back in order. The Dutch National Flag trick does it in one pass: keep three regions growing from both ends -- confirmed reds on the left, confirmed blues on the right, and a shrinking middle of "unseen" cards. Every card you look at either gets pushed left, stays, or gets pushed right. When the middle collapses, the flag is sorted.*

### Key Observations
1. There are only 3 distinct values. A count array of size 3 is enough to sort in 2 passes.
2. For a single-pass solution: at any moment we can partition the array into `[all 0s | all 1s | unknown | all 2s]`. We shrink the unknown region.
3. The key insight for the three-pointer approach: when we swap with `high`, the swapped element is unknown (came from the right); when we swap with `low`, the swapped element is guaranteed to be 1 (since mid advanced past it already).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We sort in-place: no auxiliary array needed.
- Three integer pointers partition the array conceptually into four regions without extra memory.

### Pattern Recognition
- **Pattern:** Dutch National Flag / Three-way partition
- **Classification Cue:** "In-place sort of array with only k distinct values (k small) --> counting sort or k-way partition with pointers."

---

## APPROACH LADDER

### Approach 1: Brute Force (Count and overwrite)
**Idea:** Two passes. First count how many 0s, 1s, and 2s exist. Second overwrite the array in sorted order.

**Steps:**
1. Initialize `count[3] = {0, 0, 0}`.
2. Pass 1: for each element `x`, increment `count[x]`.
3. Pass 2: write `count[0]` zeros, then `count[1]` ones, then `count[2]` twos back into `nums`.

**Why it works:** We know the exact composition; we just reconstruct in sorted order.

**Why we move on:** Two passes. Can we do it in one pass without the count array?

| Time | Space |
|------|-------|
| O(2n) | O(1) |

---

### Approach 2: Optimal (Dutch National Flag - three pointers)
**What changed:** Single pass. Maintain three pointers to grow sorted partitions from both ends simultaneously.

**Invariant:**
- `nums[0..low-1]` = all 0s
- `nums[low..mid-1]` = all 1s
- `nums[mid..high]` = unknown (to be processed)
- `nums[high+1..n-1]` = all 2s

**Steps:**
1. `low = mid = 0`, `high = n - 1`.
2. While `mid <= high`:
   - If `nums[mid] == 0`: swap `nums[low]` and `nums[mid]`, increment both `low` and `mid`.
   - If `nums[mid] == 1`: increment `mid` (already in correct region).
   - If `nums[mid] == 2`: swap `nums[mid]` and `nums[high]`, decrement `high` (do NOT increment `mid`).
3. When `mid > high`, the array is sorted.

**Dry Run:** `[2, 0, 2, 1, 1, 0]`

| Step | lo | mid | hi | Action | Array |
|------|----|-----|----|--------|-------|
| Init | 0  | 0   | 5  | -      | [2,0,2,1,1,0] |
| 1    | 0  | 0   | 5  | nums[0]=2, swap(0,5), hi-- | [0,0,2,1,1,2] |
| 2    | 0  | 0   | 4  | nums[0]=0, swap(lo,mid), lo++, mid++ | [0,0,2,1,1,2] |
| 3    | 1  | 1   | 4  | nums[1]=0, swap(lo,mid), lo++, mid++ | [0,0,2,1,1,2] |
| 4    | 2  | 2   | 4  | nums[2]=2, swap(2,4), hi-- | [0,0,1,1,2,2] |
| 5    | 2  | 2   | 3  | nums[2]=1, mid++ | [0,0,1,1,2,2] |
| 6    | 2  | 3   | 3  | nums[3]=1, mid++ | [0,0,1,1,2,2] |
| End  | 2  | 4   | 3  | mid>hi, done | [0,0,1,1,2,2] |

**Result:** `[0, 0, 1, 1, 2, 2]`

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

### Approach 3: Best (Dutch National Flag - clearest form)
**What changed:** Same algorithm as Approach 2. Uses a `switch/case` (Java) or clean `if-elif-else` (Python) to make the three cases explicit. Asymptotically identical; purely a readability improvement.

*This IS the best possible solution: O(n) time, O(1) space, single pass.*

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "Each element is examined at most once. When mid moves right OR high moves left, the unknown region shrinks by 1. It can shrink at most n times."
**Space:** O(1) -- "Three integer variables; sorting is done in-place."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Incrementing mid after swapping with high:** When you swap `nums[mid]` with `nums[high]`, the new `nums[mid]` came from the right and has never been examined. If you increment `mid`, you skip it. This is the most common bug.
2. **Swap with low always works safely:** When you swap `nums[lo]` with `nums[mid]`, the element at `lo` must be 1 (since `mid` has moved past `lo`, everything in `[lo, mid-1]` is already 1). So swapping can only put a 1 at `mid`, which is fine -- `mid` safely advances.
3. **Off-by-one with high:** The boundary check is `mid <= high` (not `mid < high`), because `nums[high]` itself is still unknown until processed.

### Edge Cases to Test
- [ ] All same value: `[0,0,0]`, `[1,1,1]`, `[2,2,2]`
- [ ] Already sorted: `[0,1,2]`
- [ ] Reverse sorted: `[2,1,0]`
- [ ] Single element: `[1]`
- [ ] Two elements: `[2,0]`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Only 0, 1, 2. Sort in-place. Can I use extra space? (Hint: interviewer wants O(1).)"
2. **Brute approach first:** "Simplest is count each value, overwrite. O(2n) time, O(1) space."
3. **Optimize:** "Dutch National Flag does it in one pass. Three pointers partition into: 0s on left, 2s on right, unknown in middle."
4. **Key point to mention:** "When swapping with high, we cannot advance mid because the element we just received is unknown."
5. **Test:** Walk through `[2,0,2,1,1,0]`.

### Follow-Up Questions
- "What if there are k distinct values instead of 3?" --> k-way partition or counting sort (O(n) with O(k) space).
- "What if the array is huge and fits only partially in memory?" --> External sort / merge sort approach.
- "Can you do it with 2 pointers?" --> Possible but messier; DNF with 3 pointers is canonical.

---

## CONNECTIONS
- **Prerequisite:** Two-pointer technique, in-place swap
- **Same Pattern:** Partition step in QuickSort, Segregate Even/Odd, Move Zeros to End
- **Harder Variant:** Sort Colors with k categories, QuickSort partition
- **This Unlocks:** Understanding partition invariants which are the heart of QuickSort
