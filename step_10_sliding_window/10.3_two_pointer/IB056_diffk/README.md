# Diffk

> **Batch 1 of 12** | **Topic:** Two Pointers | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a sorted array `A` of integers and an integer `B`, find if there exists a pair of indices `(i, j)` such that `|A[i] - A[j]| = B` and `i != j`. The array is already sorted in ascending order. Return `1` if such a pair exists, `0` otherwise.

**Source:** InterviewBit

**Constraints:**
- `1 <= |A| <= 10^5`
- `0 <= A[i] <= 10^8` (array elements are non-negative)
- `0 <= B <= 10^8`
- Array `A` is sorted in ascending order

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A = [1, 2, 3, 5], B = 4` | `1` | `|5 - 1| = 4` |
| `A = [1, 3, 5], B = 4` | `1` | `|5 - 1| = 4` |
| `A = [1, 5, 3], B = 2` | `1` | `|3 - 1| = 2` or `|5 - 3| = 2` |
| `A = [1, 2, 3], B = 0` | `0` | All elements distinct, no pair with diff 0 (but see: if duplicates existed, B=0 would return 1) |
| `A = [1, 1, 3], B = 0` | `1` | `|A[0] - A[1]| = 0` |

### Real-Life Analogy
> *You are organizing a race and need to find if any two runners finished exactly B seconds apart. The finish times are already sorted on a scoreboard. You place one finger on the first time and another finger further down the list. If the gap is too small, slide the right finger down to a later (larger) time. If the gap is too large, slide the left finger down. If the gap is exactly B, you found your pair. Because the list is sorted, you never need to go backward -- a classic two-pointer sweep.*

### Key Observations
1. The array is **already sorted**. This is the key advantage -- no need to sort first, so we go straight to two pointers in O(n).
2. Since the array is sorted and we want `A[i] - A[j] = B` where `i > j` (to keep the difference non-negative), we always have `A[i] >= A[j]`.
3. Two pointers both move forward. The right pointer widens the gap; the left pointer shrinks it. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- The array is already sorted, so **two pointers** is the natural O(n) approach.
- A HashSet also works in O(n) but uses O(n) extra space, which is unnecessary when the data is sorted.

### Pattern Recognition
- **Pattern:** Two Pointers (same-direction) on a sorted array for pair-difference problems
- **Classification Cue:** "Whenever you see _sorted array + find pair with given difference_ --> think _two pointers, both starting from left, moving in the same direction_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Pairs
**Idea:** Try every pair (i, j) and check if the absolute difference equals B.

**Steps:**
1. For each i from 0 to n-1:
   - For each j from i+1 to n-1:
     - If `A[j] - A[i] == B`, return 1.
2. Return 0.

**Note:** Since array is sorted and A[j] >= A[i] for j > i, we use `A[j] - A[i]` (no absolute value needed).

**BUD Transition -- Bottleneck:** O(n^2) pairs. Since the array is sorted, we can skip pairs intelligently with two pointers.

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Optimal -- Two Pointers (Same Direction)
**What changed:** Both pointers start from the left and move right. If the gap is too small, advance the right pointer. If too large, advance the left pointer.

**Steps:**
1. Initialize `i = 0, j = 1`.
2. While `j < n`:
   - `diff = A[j] - A[i]`
   - If `diff == B` and `i != j`: return 1.
   - If `diff < B`: `j++` (need bigger gap).
   - Else: `i++`. If `i == j`: `j++`.
3. Return 0.

**Dry Run:** `A = [1, 2, 3, 5], B = 4`

| Step | i | j | A[i] | A[j] | diff | Action |
|------|---|---|------|------|------|--------|
| 1    | 0 | 1 | 1    | 2    | 1    | 1 < 4, j++ |
| 2    | 0 | 2 | 1    | 3    | 2    | 2 < 4, j++ |
| 3    | 0 | 3 | 1    | 5    | 4    | 4 == 4, return 1 |

**Result:** 1

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- HashSet (When Array is Not Sorted)
**What changed:** If the array were not sorted, use a HashSet. For each element `a`, check if `a + B` or `a - B` exists. Since here the array IS sorted, Approach 2 is strictly better (O(1) space). We include this for completeness.

**Steps:**
1. Build a set from A.
2. If B == 0: scan for duplicates (sort or count-based).
3. For each `a` in set: if `a + B` in set, return 1.
4. Return 0.

| Time | Space |
|------|-------|
| O(n) | O(n) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "Each pointer moves at most n steps to the right. Total work is at most 2n pointer advances."
**Space:** O(1) -- "Only two pointer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Not ensuring `i != j` -- when `B = 0`, both pointers could be on the same index. You need to advance one.
2. Forgetting `B = 0` edge case -- need two equal elements at different indices.
3. Using absolute value when the array is sorted -- unnecessary. Since sorted, `A[j] >= A[i]` for `j >= i`, so `diff = A[j] - A[i]` is always non-negative.
4. Moving pointers backward -- in this pattern, both pointers always move forward.

### Edge Cases to Test
- [ ] `B = 0` with no duplicates --> 0
- [ ] `B = 0` with duplicates --> 1
- [ ] Single element array --> 0
- [ ] Pair at the very ends of the array
- [ ] All elements the same, B = 0 --> 1
- [ ] Consecutive elements with difference B

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Sorted array, find if any pair has absolute difference B."
2. **Match:** "Sorted + pair difference --> two pointers, same direction."
3. **Plan:** "Start both pointers at left. Advance j to increase diff, advance i to decrease diff."
4. **Implement:** Write the two-pointer loop with i != j check.
5. **Review:** Trace [1,2,3,5] with B=4.
6. **Evaluate:** "O(n) time, O(1) space. This is optimal since we must examine each element at least once."

### Follow-Up Questions
- "What if the array is not sorted?" --> Sort first (O(n log n)) then two pointers, or use HashSet (O(n) time, O(n) space).
- "What if you need to count all such pairs?" --> Same pointer logic, but track count instead of returning early. Handle duplicates carefully.
- "How is this different from IB054 (Pair With Given Difference)?" --> IB054 uses unsorted arrays and directional difference (A[i]-A[j]=B, not absolute). Diffk uses sorted arrays and absolute difference.

---

## CONNECTIONS
- **Prerequisite:** Two Sum, basic two-pointer technique
- **Same Pattern:** Pair With Given Difference (IB054), K-diff Pairs (LC #532)
- **Harder Variant:** 3Sum, Count pairs with given difference
- **This Unlocks:** Mastery of same-direction two-pointer technique for difference problems
