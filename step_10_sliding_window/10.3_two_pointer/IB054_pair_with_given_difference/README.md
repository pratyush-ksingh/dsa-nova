# Pair With Given Difference

> **Batch 1 of 12** | **Topic:** Two Pointers | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array `A` of integers and an integer `B`, find if there exist two indices `i` and `j` such that `A[i] - A[j] = B` and `i != j`. Return `1` if such a pair exists, `0` otherwise.

**Source:** InterviewBit

**Constraints:**
- `1 <= |A| <= 10^5`
- `-10^8 <= A[i] <= 10^8`
- `-10^8 <= B <= 10^8`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A = [5, 10, 3, 2, 50, 80], B = 78` | `1` | 80 - 2 = 78 |
| `A = [-10, 20], B = 30` | `1` | 20 - (-10) = 30 |
| `A = [1, 2, 3], B = 0` | `1` | Any element minus itself, but i != j: need two equal elements. Here no two are equal, but wait -- A[0]-A[0] is not valid (i!=j). Actually 1-1 needs same value at different indices. This returns 0 since all distinct. **Correction:** if B=0 we need duplicates. |
| `A = [5, 5, 3], B = 0` | `1` | A[0] - A[1] = 0, indices 0 and 1 |
| `A = [1], B = 5` | `0` | Only one element, no pair possible |

### Real-Life Analogy
> *You have a list of exam scores and want to know if any two students scored exactly B points apart. The brute way is to compare every pair. A smarter way: sort the scores, then use two pointers -- one trailing and one leading. If the gap is too small, advance the leader. If too large, advance the trailer. If exactly B, you found your pair. This is like two people walking along a number line trying to maintain an exact distance.*

### Key Observations
1. The problem asks for `A[i] - A[j] = B`, which is equivalent to `A[i] = A[j] + B`. For any element, we need to check if `element + B` exists.
2. Sorting + two pointers works because after sorting, differences increase monotonically as we move pointers apart.
3. A HashSet approach also works: for each element, check if `element - B` exists in the set. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- **HashSet** for O(1) lookups when checking if a complement exists.
- **Sorted Array + Two Pointers** as the classic interview technique for pair-difference problems.

### Pattern Recognition
- **Pattern:** Two Pointers on Sorted Array (or HashSet for complement lookup)
- **Classification Cue:** "Whenever you see _find a pair with a given sum/difference_ --> think _sort + two pointers_ or _HashSet complement_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Pairs
**Idea:** Try every pair (i, j) where i != j, and check if A[i] - A[j] == B.

**Steps:**
1. For each i from 0 to n-1:
   - For each j from 0 to n-1:
     - If `i != j` and `A[i] - A[j] == B`, return 1.
2. Return 0.

**Why it works:** Exhaustive search over all ordered pairs.

**BUD Transition -- Bottleneck:** O(n^2) comparisons. We can either sort and use two pointers (O(n log n)) or use a HashSet (O(n)).

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Optimal -- Sort + Two Pointers
**What changed:** Sort the array. Use two pointers `i` and `j` (both starting at 0, with `i > j` or `i` ahead). Compute `A[i] - A[j]` and adjust pointers.

**Steps:**
1. Sort `A`.
2. Initialize `i = 1, j = 0`.
3. While `i < n`:
   - `diff = A[i] - A[j]`
   - If `diff == B` and `i != j`: return 1.
   - If `diff < B`: increment `i` (need a larger difference).
   - Else: increment `j` (need a smaller difference). If `j == i`, also increment `i`.
4. Return 0.

**Important:** We need `i != j` because the problem requires distinct indices. After sorting, if B=0 we need two equal adjacent elements.

**Dry Run:** `A = [5, 10, 3, 2, 50, 80], B = 78` --> sorted: `[2, 3, 5, 10, 50, 80]`

| Step | i | j | A[i] | A[j] | diff | Action |
|------|---|---|------|------|------|--------|
| 1    | 1 | 0 | 3    | 2    | 1    | 1 < 78, i++ |
| 2    | 2 | 0 | 5    | 2    | 3    | 3 < 78, i++ |
| 3    | 3 | 0 | 10   | 2    | 8    | 8 < 78, i++ |
| 4    | 4 | 0 | 50   | 2    | 48   | 48 < 78, i++ |
| 5    | 5 | 0 | 80   | 2    | 78   | 78 == 78, return 1 |

**Result:** 1

| Time | Space |
|------|-------|
| O(n log n) | O(1) if in-place sort |

### Approach 3: Best -- HashSet Lookup
**What changed:** Store all elements in a HashSet. For each element `a`, check if `a - B` is in the set. Handle B=0 specially (need duplicate check).

**Steps:**
1. If `B == 0`: check if any element appears more than once (use a frequency map or sort and check adjacent). Return 1 if yes.
2. Create a HashSet from A.
3. For each element `a` in the set:
   - If `a - B` is in the set, return 1.
4. Return 0.

**Why this is best:** Single pass to build the set, single pass to query. O(n) total.

| Time | Space |
|------|-------|
| O(n) | O(n) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) for HashSet, O(n log n) for sort + two pointers.
**Space:** O(n) for HashSet, O(1) for sort + two pointers (if in-place sort allowed).

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Not handling `B = 0`: you need two distinct indices with the same value, not just `a - a = 0` for the same index.
2. Not handling negative `B`: `A[i] - A[j] = -5` is valid. After sorting, you might need to search for `A[j] - A[i] = 5`.
3. Forgetting that `i != j` is required -- same element at the same index does not count.

### Edge Cases to Test
- [ ] `B = 0` with all distinct elements --> 0
- [ ] `B = 0` with duplicates --> 1
- [ ] Negative `B` --> `A[i] - A[j] = B` means A[i] < A[j]
- [ ] Single element array --> 0
- [ ] Large positive and negative values
- [ ] All elements the same

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to find if any ordered pair (i,j) with i!=j has A[i]-A[j]=B."
2. **Match:** "Pair with given difference --> sort + two pointers or HashSet."
3. **Plan:** "I will sort, then walk two pointers maintaining the right difference."
4. **Implement:** Write the sort + two-pointer approach. Mention HashSet as O(n) alternative.
5. **Review:** Trace through the dry run.
6. **Evaluate:** "O(n log n) time with O(1) space for two pointers, or O(n) time with O(n) space for HashSet."

### Follow-Up Questions
- "What if you need to find all such pairs?" --> Collect results in a list; same pointer logic but do not return early.
- "What if the array is already sorted?" --> Skip the sort step; O(n) with two pointers.
- "How does this differ from two-sum?" --> Two-sum checks `A[i] + A[j] = target`; conceptually similar but difference requires careful pointer movement direction.

---

## CONNECTIONS
- **Prerequisite:** Two Sum (LC #1), sorting
- **Same Pattern:** Two Sum, 3Sum, Diffk (IB056)
- **Harder Variant:** Count pairs with given difference, K-diff pairs (LC #532)
- **This Unlocks:** Understanding of two-pointer technique for difference problems
