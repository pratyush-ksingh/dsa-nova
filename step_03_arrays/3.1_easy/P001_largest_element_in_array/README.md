# Largest Element in Array

> **Batch 1 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers `arr` of size `n`, find and return the largest element in the array.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3, 5, 7, 2, 8]` | `8` | 8 is the largest among all elements |
| `[10]` | `10` | Only one element, so it is the largest |
| `[5, 5, 5, 5]` | `5` | All elements are equal |

### Real-Life Analogy
> *Imagine you are a teacher collecting exam scores from a class. You flip through each paper one by one, keeping a mental note of the highest score you have seen so far. Every time you see a score higher than your current best, you update your mental note. After going through every paper, the score in your head is the highest in the class. This is exactly what the problem is asking.*

### Key Observations
1. We must look at every element at least once -- there is no shortcut to skip elements when the array is unsorted.
2. Sorting gives us the answer at position `n-1`, but sorting does more work than necessary.
3. A single pass with a running maximum is sufficient. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need sequential access to every element. An **Array** gives us that with O(1) indexed access.
- You might think a heap or priority queue works, but building a heap is O(n) and extracting the max is O(log n) -- overkill for a single query.

### Pattern Recognition
- **Pattern:** Linear Scan (track a running answer while iterating)
- **Classification Cue:** "When you see _find the min/max/first/last of an unsorted collection_ in a problem --> think _single-pass linear scan_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Sort)
**Idea:** Sort the array in ascending order. The last element is the largest.

**Steps:**
1. Sort the array.
2. Return `arr[n - 1]`.

**Why it works:** Sorting places elements in order; the maximum ends up at the end.

**Why we move on:** **Bottleneck** -- sorting does O(n log n) comparisons, but we only need the single largest value. That is unnecessary work.

| Time | Space |
|------|-------|
| O(n log n) | O(1) if in-place sort, O(n) otherwise |

### Approach 2: Optimal -- Single Pass Linear Scan
**What changed:** Instead of sorting, we scan once and track the maximum.

**Steps:**
1. Initialize `max_val = arr[0]`.
2. For each element `arr[i]` from index 1 to n-1:
   - If `arr[i] > max_val`, update `max_val = arr[i]`.
3. Return `max_val`.

**Dry Run:** Input = `[3, 5, 7, 2, 8]`

| Step | i | arr[i] | max_val | Action |
|------|---|--------|---------|--------|
| Init | - | -      | 3       | Set max_val = arr[0] |
| 1    | 1 | 5      | 5       | 5 > 3, update |
| 2    | 2 | 7      | 7       | 7 > 5, update |
| 3    | 3 | 2      | 7       | 2 < 7, skip |
| 4    | 4 | 8      | 8       | 8 > 7, update |

**Result:** 8

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* This is already the best possible approach -- you cannot find the max without examining every element. No Approach 3 needed.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We visit each element exactly once, doing a constant-time comparison each time."
**Space:** O(1) -- "We store only a single variable `max_val`, regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Initializing `max_val = 0` --> Fails if all elements are negative (though constraints say >= 1 here, it is a bad habit).
2. Starting the loop at index 0 instead of 1 --> Not wrong, just one redundant comparison.

### Edge Cases to Test
- [ ] Single element array `[42]` --> return 42
- [ ] All elements identical `[5, 5, 5]` --> return 5
- [ ] Already sorted ascending `[1, 2, 3, 4]` --> return 4
- [ ] Already sorted descending `[9, 7, 5, 3]` --> return 9
- [ ] Maximum constraints (n = 10^5, values up to 10^9)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Can the array be empty? Are there negative values? Is it guaranteed to have at least one element?"
2. **Approach:** "I'd note that sorting works but is O(n log n). Since we only need the max, a single O(n) scan with a running maximum is optimal."
3. **Code:** Narrate while writing. Say: "I'll initialize max_val to the first element, then iterate from the second element onward, updating whenever I find something larger."
4. **Test:** Walk through `[3, 5, 7, 2, 8]` verbally, showing max_val updates.

### Follow-Up Questions
- "What if you need the top-k largest?" --> Use a min-heap of size k, O(n log k).
- "What if the array is a stream?" --> Maintain a running max; each new element is O(1).

---

## CONNECTIONS
- **Prerequisite:** Basic array traversal
- **Same Pattern:** Find Minimum Element, Find Second Largest (P002)
- **Harder Variant:** Kth Largest Element (Quick Select / Heap)
- **This Unlocks:** Second Largest Element, understanding linear scan pattern
