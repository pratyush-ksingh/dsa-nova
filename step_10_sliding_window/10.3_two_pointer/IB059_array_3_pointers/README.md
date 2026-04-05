# Array 3 Pointers

> **Step 10 | 10.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
You are given 3 sorted arrays `A`, `B`, `C`. Find indices `i`, `j`, `k` such that:

**max(|A[i]-B[j]|, |B[j]-C[k]|, |C[k]-A[i]|) is minimized.**

Return the minimum possible value of this maximum pairwise absolute difference.

### Key Mathematical Insight
For three values `a`, `b`, `c`:
```
max(|a-b|, |b-c|, |c-a|) = max(a,b,c) - min(a,b,c)
```
This simplification is critical -- instead of computing 3 differences, we just compute `max - min` of the triplet.

**Proof:** WLOG assume `a <= b <= c`. Then `|c-a| = c-a = max-min` is the largest pairwise difference. And `max(|a-b|,|b-c|,|c-a|) = c-a = max-min`. QED.

### Examples

| # | A | B | C | Output | Explanation |
|---|---|---|---|--------|-------------|
| 1 | `[1,4,10]` | `[2,15,20]` | `[10,12]` | `5` | Best: i=2,j=0,k=0 or trace shows minimum spread=5 |
| 2 | `[1,2,3]` | `[4,5,6]` | `[7,8,9]` | `4` | Best: i=2,j=1,k=0 -> {3,5,7} -> max-min=4 or i=2,j=2,k=0 -> {3,6,7} -> 4 |
| 3 | `[1,10,20]` | `[2,10,20]` | `[1,10,20]` | `0` | A[1]=B[1]=C[1]=10 |
| 4 | `[1]` | `[1]` | `[1]` | `0` | All same |

### Constraints
- `1 <= |A|, |B|, |C| <= 10^5`
- Arrays are sorted in non-decreasing order
- `-10^9 <= A[i], B[j], C[k] <= 10^9`

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Time | Space |
|----------|-----------|------|-------|
| Brute Force | Try all triplets (i, j, k) | O(n*m*k) | O(1) |
| Optimal | Three pointers: always advance the minimum | O(n+m+k) | O(1) |
| Best | Same with min-heap (generalizes to K arrays) | O((n+m+k) log 3) | O(1) |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Try All Triplets

**Intuition:** For every combination of indices `(i, j, k)`, compute `max(A[i],B[j],C[k]) - min(A[i],B[j],C[k])` and track the global minimum.

**Steps:**
1. For each `a` in A, `b` in B, `c` in C:
   - `diff = max(a,b,c) - min(a,b,c)`.
   - Update global minimum.
2. Return global minimum.

| Metric | Value |
|--------|-------|
| Time   | O(n * m * k) |
| Space  | O(1) |

---

### Approach 2: Optimal -- Three Pointers

**Intuition:** We want to minimize `spread = max(A[i],B[j],C[k]) - min(A[i],B[j],C[k])`. At any step, the maximum cannot be decreased by moving forward (arrays are sorted ascending, so advancing any pointer can only increase values). The **minimum can be increased** by advancing the pointer pointing to the current minimum element. Increasing the minimum reduces the spread.

**Greedy choice:** At each step, advance the pointer of the array whose current element is the **minimum** of the three current elements. This is the only productive move.

**Why advancing the max pointer is wrong:** If `A[i]` is the max, advancing `i` increases `A[i]` even more, increasing the max and potentially the spread.

**Why advancing the min pointer is right:** If `A[i]` is the min, advancing `i` increases `A[i]` (since A is sorted). This can only decrease or maintain the spread. It's our best hope.

**Steps:**
1. Initialize `i = j = k = 0`, `result = INF`.
2. While none of the arrays is exhausted:
   - Compute `diff = max(A[i],B[j],C[k]) - min(A[i],B[j],C[k])`.
   - `result = min(result, diff)`.
   - If `result == 0`, return `0`.
   - Find which of `A[i], B[j], C[k]` is the minimum. Advance that pointer.
3. Return `result`.

**Dry-Run Trace** (`A=[1,4,10], B=[2,15,20], C=[10,12]`):
```
i=0,j=0,k=0: {1,2,10} -> diff=9. min=A[0]=1 -> i++
i=1,j=0,k=0: {4,2,10} -> diff=8. min=B[0]=2 -> j++
i=1,j=1,k=0: {4,15,10} -> diff=11. min=A[1]=4 -> i++
i=2,j=1,k=0: {10,15,10} -> diff=5. min=A[2]=C[0]=10 -> advance A -> i++
i=3: A exhausted. result = min(9,8,11,5) = 5.
```

| Metric | Value |
|--------|-------|
| Time   | O(n + m + k) -- total pointer advances = n+m+k |
| Space  | O(1) |

---

### Approach 3: Best -- Min-Heap (Generalizes to K Arrays)

**Intuition:** Same three-pointer logic, but use a min-heap to elegantly find the minimum element without explicit if-else chains. The heap holds one element from each array: `(value, arrayId, indexInArray)`. At each step: pop the minimum, advance that pointer, push the next element, track the running maximum. Compute spread = currentMax - heapMin.

**Why track currentMax separately?** The heap gives us the minimum, but the maximum can only increase as we advance pointers. So we track `currentMax = max(currentMax, new element)`.

**Steps:**
1. Initialize heap with `(A[0], 0, 0), (B[0], 1, 0), (C[0], 2, 0)`.
2. `currentMax = max(A[0], B[0], C[0])`.
3. `result = currentMax - heap.min`.
4. While True:
   - Pop `(minVal, arrayId, idx)`.
   - `nextIdx = idx + 1`.
   - If `nextIdx >= arrays[arrayId].length`: break.
   - Push `(arrays[arrayId][nextIdx], arrayId, nextIdx)`.
   - `currentMax = max(currentMax, arrays[arrayId][nextIdx])`.
   - `result = min(result, currentMax - heap.peek())`.
5. Return `result`.

| Metric | Value |
|--------|-------|
| Time   | O((n+m+k) * log 3) = O(n+m+k) since log(3) is constant |
| Space  | O(1) -- heap size is always 3 |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n+m+k):** The three pointers collectively advance at most n+m+k times total before one reaches the end. Each advance is O(1) (Approach 2) or O(log 3) = O(1) (Approach 3).

**Why the greedy works:** Consider any optimal solution (i*, j*, k*). The three-pointer algorithm is guaranteed to encounter this triplet (or a triplet with equal or smaller spread) because:
- The algorithm only skips a triplet by advancing the minimum pointer.
- Skipping the minimum (while keeping max and mid the same) is safe because the new triplet has equal or smaller spread.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| All arrays have one element | `\|A[0]-B[0]\| + \|B[0]-C[0]\|` type calc | Just compute directly; the loop runs once |
| One array has a much larger range | Algorithm still works | The min pointer in that array will advance rapidly |
| Duplicate values across arrays | 0 | Algorithm terminates early when diff == 0 |
| Equal minimum: two pointers tied | Advance either | Both are correct; break the tie arbitrarily |

**Common Mistakes:**
- **Advancing the max pointer instead of min:** This is wrong -- it increases the spread.
- **Forgetting to check all three pointers for the minimum** (only advancing A's pointer, for example).
- **Not simplifying max(|a-b|,|b-c|,|c-a|) to max-min** -- leads to more complex code.
- **Off-by-one in the termination condition:** Stop when ANY array is exhausted (not all).

---

## 6. REAL-WORLD USE CASE

**Database Query Merging:** Given three sorted result sets from different database indices, find the "closest" matching records across all three sets. The spread = how well the records align. The three-pointer approach is used in merge joins.

**GPS Positioning / Trilateration:** Given distance measurements from three stations (sorted by time), find the time window where all three measurements are most consistent (minimum spread in time). This is equivalent to the 3-array pointer problem.

**Version Control:** Given commit timestamps from three branches, find the point in time where all three branches were most "in sync" (closest commit timestamps).

---

## 7. INTERVIEW TIPS

- **State the key insight immediately:** `max(|a-b|,|b-c|,|c-a|) = max(a,b,c) - min(a,b,c)`. This simplification often surprises interviewers.
- **Explain the greedy choice:** "We always advance the minimum pointer because it's the only way to potentially reduce the spread."
- **Why can't we advance the maximum?** "Advancing the max only makes it larger, which can't help."
- **Generalization:** "This naturally extends to K sorted arrays using a min-heap of size K -- O((n*K) log K) time."
- **Tie-breaking:** When two elements are the minimum, advancing either one is correct (they'll give the same current spread, and future steps will handle the rest).
- **Draw the three lines** on a number line to visualize: you're trying to bring three points as close together as possible by sliding them forward.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Merge K Sorted Arrays | Same K-pointer / heap technique |
| Smallest Range Covering Elements from K Lists (LC #632) | Exact generalization: find smallest range [x,y] containing at least one element from each of K sorted lists |
| 3Sum Closest (LC #16) | Minimize absolute difference from target for 3 numbers from one array |
| Two Sum in Sorted Array (LC #167) | Two-pointer technique in one array |
| Counting Subarrays (IB058) | Same two-pointer family |
