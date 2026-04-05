# Second Largest Element

> **Batch 1 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers `arr` of size `n`, find and return the **second largest distinct** element. If no such element exists (e.g., all elements are the same or the array has fewer than 2 elements), return `-1`.

**Constraints:**
- `1 <= n <= 10^5`
- `0 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[12, 35, 1, 10, 34, 1]` | `34` | Largest is 35, second largest is 34 |
| `[10, 10, 10]` | `-1` | All elements are the same; no distinct second largest |
| `[5, 10]` | `5` | Two distinct elements, second largest is 5 |
| `[7]` | `-1` | Only one element, second largest does not exist |

### Real-Life Analogy
> *Imagine a race with several runners. The gold medal goes to the fastest. Now you need to award the silver medal -- you need the second-fastest **distinct** time. If everyone finished at the exact same time, there is no silver medal to give. This is exactly what the problem is asking.*

### Key Observations
1. The second largest must be **strictly less than** the largest (we want distinct values).
2. Sorting works but does more work than needed -- we only care about the top two values.
3. We can track both `first` (largest) and `second` (second largest) in a single pass. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need to scan every element. An **Array** with sequential access is sufficient.
- You might think a Set removes duplicates, but we do not need to remove all duplicates -- we just need the two largest distinct values.

### Pattern Recognition
- **Pattern:** Linear Scan with multiple tracking variables
- **Classification Cue:** "When you see _find the kth largest/smallest for small k_ in a problem --> think _track k variables in a single pass_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Sort)
**Idea:** Sort the array in descending order. Walk from the front to find the first element strictly smaller than `arr[0]`.

**Steps:**
1. Sort the array in descending order.
2. The largest element is `arr[0]`.
3. Iterate from index 1 onwards. Return the first element that is not equal to `arr[0]`.
4. If no such element exists, return `-1`.

**Why it works:** After sorting, all copies of the max are at the front. The first different value is the second largest.

**Why we move on:** **Bottleneck** -- sorting is O(n log n), but we only need 2 values out of n.

| Time | Space |
|------|-------|
| O(n log n) | O(1) if in-place sort |

### Approach 2: Optimal -- Two-Pass Linear Scan
**What changed:** Eliminate sorting. First pass finds the maximum; second pass finds the largest element strictly less than the maximum.

**Steps:**
1. Pass 1: Find `largest` by scanning the entire array.
2. Pass 2: Find `second_largest` by scanning again, considering only elements strictly less than `largest`.
3. If `second_largest` was never updated, return `-1`.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Single-Pass with Two Variables
**Core idea in one sentence:** "Track both `first` and `second` simultaneously in one pass."

**Steps:**
1. Initialize `first = -1` and `second = -1` (or negative infinity).
2. For each element `x` in the array:
   - If `x > first`: `second = first`, then `first = x` (new champion pushes old champion to silver).
   - Else if `x > second` and `x != first`: `second = x` (new silver medalist).
3. Return `second`.

**Dry Run:** Input = `[12, 35, 1, 10, 34, 1]`

| Step | x  | first | second | Action |
|------|----|-------|--------|--------|
| Init | -  | -1    | -1     | Starting state |
| 1    | 12 | 12    | -1     | 12 > -1, promote |
| 2    | 35 | 35    | 12     | 35 > 12, old first (12) becomes second |
| 3    | 1  | 35    | 12     | 1 < 12, skip |
| 4    | 10 | 35    | 12     | 10 < 12, skip |
| 5    | 34 | 35    | 34     | 34 > 12 and 34 != 35, update second |
| 6    | 1  | 35    | 34     | 1 < 34, skip |

**Result:** second = 34

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* Same asymptotic complexity as Approach 2 but does one pass instead of two -- half the constant factor.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We visit each element exactly once, making at most two comparisons per element."
**Space:** O(1) -- "We store only two variables (`first`, `second`) regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to handle **duplicates** of the largest element --> e.g., `[10, 10, 5]` should return 5, not 10.
2. Initializing `second = 0` --> Fails if all values are 0 or negative (in problems allowing negatives). Use `-1` or `-infinity`.
3. Not handling the "does not exist" case --> Must return `-1` when all elements are identical.

### Edge Cases to Test
- [ ] Single element `[7]` --> -1
- [ ] Two identical elements `[5, 5]` --> -1
- [ ] Two distinct elements `[3, 7]` --> 3
- [ ] All elements same `[4, 4, 4, 4]` --> -1
- [ ] Largest at the end `[1, 2, 3]` --> 2
- [ ] Largest at the start `[9, 1, 2]` --> 2
- [ ] Maximum constraints

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Should 'second largest' be strictly less than the largest, or can it be equal? What should I return if it does not exist?"
2. **Approach:** "Brute force sorts in O(n log n). I can do better with a single pass tracking the top two distinct values in O(n)."
3. **Code:** Narrate while writing. Say: "I'll maintain two variables. When I find a new max, the old max slides down to second. If a value beats second but not first, I update second."
4. **Test:** Walk through `[12, 35, 1, 10, 34, 1]`, showing both variables update.

### Follow-Up Questions
- "What if you need the kth largest?" --> Use a min-heap of size k for O(n log k), or Quickselect for O(n) average.
- "What about a stream of numbers?" --> Maintain two variables, same approach, O(1) per insertion.

---

## CONNECTIONS
- **Prerequisite:** Largest Element in Array (P001)
- **Same Pattern:** Find the two smallest elements, Tournament method
- **Harder Variant:** Kth Largest Element (LeetCode #215)
- **This Unlocks:** Understanding multi-variable tracking in linear scans
