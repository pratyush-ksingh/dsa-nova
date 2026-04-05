# Noble Integer

> **Batch 4 of 12** | **Topic:** Arrays / Sorting | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
Given an integer array `A`, find an element `x` such that the **number of integers strictly greater than x** in the array equals `x` itself. Return 1 if such a noble integer exists, otherwise return -1.

If multiple noble integers exist, return 1 (just confirm existence).

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3, 2, 1, 3]` | `1` | x=2: exactly 2 elements (3, 3) are strictly greater than 2. Noble! |
| `[5, 6, 2]` | `1` | x=2: exactly 2 elements (5, 6) are strictly greater than 2. Noble! |
| `[7, 3, 16, 10]` | `1` | x=3: exactly 3 elements (7, 16, 10) are strictly greater than 3. Noble! |
| `[1, 1, 1]` | `-1` | x=1: count(>1)=0, not equal to 1. No noble integer exists. |
| `[0]` | `1` | x=0: count(>0)=0. 0==0. Noble! |

### Constraints
- 1 <= |A| <= 10^5
- -10^9 <= A[i] <= 10^9

### Real-Life Analogy
Imagine a **popularity contest** where each participant has a score. A participant is "noble" if the number of participants who scored strictly higher than them equals their own score. For instance, if your score is 3, and exactly 3 people scored higher, you are noble. Sorting the scores makes it easy to count how many people are above you -- your rank from the top.

### Key Observations
1. For each element `x`, we need `count(elements > x)`. The brute force checks this for every element with an inner scan, giving O(n^2).
2. If we **sort** the array, the count of elements strictly greater than `A[i]` is `n - 1 - i` (for the last occurrence of that value). This converts an O(n) inner scan into an O(1) lookup.
3. **Aha moment:** After sorting, be careful with duplicates. If `A[i] == A[i+1]`, the count of elements greater than `A[i]` is not `n-1-i` but rather the count after all duplicates of `A[i]`. Skip duplicates by only checking the last occurrence of each value.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Sorting?
Sorting lets us compute "count of elements greater than x" in O(1) for each element by using its position. Without sorting, each check is O(n), giving O(n^2) total. Sorting costs O(n log n) but enables an O(n) scan afterward.

### Pattern Recognition
**Classification cue:** "Count of elements satisfying a condition relative to each element" --> sort first, then use index-based counting. Same idea appears in "count inversions" and "rank-based" problems.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check Each Element
**Intuition:** For each element x in the array, count how many elements are strictly greater than x. If that count equals x, return 1.

**Steps:**
1. For each element `A[i]`:
2. Count elements in array strictly greater than `A[i]`.
3. If count == `A[i]`, return 1.
4. If no noble integer found, return -1.

**Dry Run (A = [5, 6, 2]):**

| i | A[i] | Count(> A[i]) | Noble? |
|---|------|--------------|--------|
| 0 | 5    | 1 (just 6)   | 1 != 5, no |
| 1 | 6    | 0            | 0 != 6, no |
| 2 | 2    | 2 (5 and 6)  | 2 == 2, YES! |

Return 1.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

**BUD Transition:** The inner count is redundant work. Sorting gives us rank information to compute it in O(1).

---

### Approach 2: Optimal -- Sort + Index-Based Count
**Intuition:** Sort the array in ascending order. For element at index `i`, the number of elements strictly greater is `n - 1 - i` (everything to its right). But handle duplicates: for duplicates, only check the last occurrence.

**Steps:**
1. Sort array in ascending order.
2. For each index `i` from 0 to n-1:
   - Skip if `A[i] == A[i+1]` (not the last occurrence of this value).
   - Count of elements > A[i] = `n - 1 - i`.
   - If `A[i] == n - 1 - i`, return 1.
3. Return -1.

**Dry Run (A = [3, 2, 1, 3]):**
- Sorted: [1, 2, 3, 3], n = 4
- i=0: A[0]=1, count = 4-1-0 = 3. 1 != 3. Continue.
- i=1: A[1]=2, count = 4-1-1 = 2. 2 == 2. Return 1!

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) if in-place sort |

---

### Approach 3: Best -- Same as Optimal (Sorting is Necessary)
**Intuition:** Without sorting, the only alternative for O(1) count-of-greater queries is a hash-based frequency approach, which still requires O(n) space and is more complex. The sort-based approach is the cleanest and most practical. We can also use a counting/bucket approach if values are bounded, but this does not improve asymptotic complexity in the general case.

For the "best" variant, we present a clean single-pass after sorting that handles duplicates elegantly:

**Steps:**
1. Sort array.
2. Loop from left to right. For each `i`, if `A[i] != A[i+1]` (or `i == n-1`):
   - Check if `A[i] == n - 1 - i`.
3. Return 1 or -1.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n log n)?** Sorting dominates. After sorting, the scan is O(n). You cannot avoid O(n) time since you must read every element. Whether O(n log n) can be improved to O(n) depends on the value range -- with bounded values, counting sort gives O(n), but that is not generally applicable.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Ignoring duplicates | If [3, 3, 3] is sorted, index 0 says count=2 but actual count > 3 for A[0]=3 is 0 | Only check the last occurrence of each value |
| Negative noble values | x must equal count (non-negative). If x < 0, count cannot be negative, so skip. | Optimization: skip negative values |
| Not considering x = 0 | If the largest element is 0, count of elements > 0 is 0. Noble! | 0 is a valid noble integer |

### Edge Cases Checklist
- All same elements: `[2, 2, 2]` --> count(>2)=0, 2!=0. Return -1.
- Contains 0: `[0, 1, 2]` --> sorted [0,1,2]. x=0: count=2 != 0. x=1: count=1. 1==1. Return 1.
- Negative elements: `[-5, 0, 3]` --> x=0: count(>0)=1 != 0. x=-5 < 0, skip.
- Single element: `[0]` --> count(>0)=0. 0==0. Return 1.
- Large array with no noble integer.

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Find x in array where count of elements strictly greater than x equals x. Return 1 if exists, -1 otherwise."
2. **M**atch: "Rank-based counting. Sort to get positional rank."
3. **P**lan: "Sort ascending. For each element at index i, count_greater = n-1-i. If A[i] == count_greater, found. Handle duplicates by checking last occurrence."
4. **I**mplement: Sort + single pass with duplicate skip.
5. **R**eview: Trace through [3, 2, 1, 3] sorted as [1, 2, 3, 3].
6. **E**valuate: "O(n log n) time, O(1) space."

### Follow-Up Questions
- "What if we need to return the noble integer itself, not just 1/-1?" --> Same approach, return A[i] when found.
- "What if there are multiple noble integers?" --> Collect all. Each must satisfy count(>x)==x independently.
- "Can you do it in O(n) with bounded values?" --> Use counting sort + suffix sums.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Array sorting, counting elements |
| **Same pattern** | H-Index (similar rank-based counting), Count of Smaller Numbers After Self |
| **Harder variant** | H-Index II (binary search on sorted), Relative Ranks |
| **Unlocks** | Rank-based reasoning, sort-then-scan technique |
