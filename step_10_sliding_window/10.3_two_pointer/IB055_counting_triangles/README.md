# Counting Triangles

> **Step 10 | 10.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array `A` of integers, count the number of triplets `(i, j, k)` where `i < j < k` such that `A[i]`, `A[j]`, `A[k]` can form a valid triangle. A valid triangle satisfies the **triangle inequality**: the sum of any two sides must be strictly greater than the third side.

**Source:** InterviewBit

**Constraints:**
- `1 <= |A| <= 2000`
- `1 <= A[i] <= 10^6`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A = [3, 4, 6, 7]` | `3` | Valid triplets: (3,4,6), (3,6,7), (4,6,7) |
| `A = [10, 21, 22, 100, 101, 200, 300]` | `6` | Multiple valid triplets among these values |
| `A = [1, 1, 1, 1]` | `4` | C(4,3) = 4 triplets, all equilateral |
| `A = [1, 2, 3]` | `0` | 1+2=3, not strictly greater, so no valid triangle |

### Real-Life Analogy
> *Imagine you are a carpenter with a box of wooden rods of various lengths. You want to know how many different sets of 3 rods you can pick to build triangular frames. The triangle inequality is a physical constraint -- if one rod is too long, the other two cannot reach each other to close the shape. Sorting the rods by length and working from the longest one makes it easy to figure out which shorter pairs can "reach" to form a triangle.*

### Key Observations
1. If the array is sorted, for three values `a <= b <= c`, we only need to check `a + b > c` (the other two inequalities are automatically satisfied).
2. This single-check property after sorting is what makes the two-pointer approach possible.
3. For a fixed largest side `c = A[k]`, all pairs `(A[i], A[j])` where `A[i] + A[j] > A[k]` form valid triangles.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- **Sorting** reduces the triangle inequality from 3 checks to 1 check.
- **Two pointers** on the subarray `[0..k-1]` efficiently counts all valid pairs for a fixed largest side.

### Pattern Recognition
- **Pattern:** Sort + Two Pointers (counting valid pairs)
- **Classification Cue:** "Whenever you see _count triplets with a constraint on sum of two vs third_ --> think _sort and fix the largest, then two-pointer count_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Triplets
**Idea:** Enumerate all triplets and check the triangle inequality for each.

**Steps:**
1. For each triplet `(i, j, k)` where `i < j < k`:
   - Check if `A[i] + A[j] > A[k]`, `A[i] + A[k] > A[j]`, and `A[j] + A[k] > A[i]`.
   - If all three hold, increment count.
2. Return count.

**Why it works:** Exhaustive search -- guaranteed correct.

**BUD Transition -- Bottleneck:** O(n^3) is too slow for n=2000. Sorting lets us reduce to a single check and use two pointers.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(1) |

---

### Approach 2: Optimal -- Sort + Two Pointer
**What changed:** Sort the array. For each index `k` (largest side), use two pointers `left=0` and `right=k-1` to count pairs where `A[left] + A[right] > A[k]`.

**Steps:**
1. Sort `A` in ascending order.
2. For each `k` from 2 to n-1 (fixing `A[k]` as the largest side):
   - Set `left = 0`, `right = k - 1`.
   - While `left < right`:
     - If `A[left] + A[right] > A[k]`: all indices from `left` to `right-1` paired with `right` are valid. Add `right - left` to count. Decrement `right`.
     - Else: increment `left`.
3. Return count.

**Dry Run:** `A = [3, 4, 6, 7]` (already sorted)

| k | A[k] | left | right | A[left]+A[right] | Action | count |
|---|------|------|-------|------------------|--------|-------|
| 2 | 6 | 0 | 1 | 3+4=7>6 | count += 1, right-- | 1 |
| 3 | 7 | 0 | 2 | 3+6=9>7 | count += 2, right-- | 3 |
| 3 | 7 | 0 | 1 | 3+4=7=7 | not >, left++ | 3 |

**Result:** 3

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) if in-place sort |

---

### Approach 3: Best -- Same Sort + Two Pointer
**Intuition:** This IS the optimal approach. The two-pointer technique after sorting is the best known algorithm for this problem. No further asymptotic improvement is possible since we need to consider O(n^2) potential pairs in the worst case.

The cleanest form iterates `i` from `n-1` down to `2` (largest side), with `lo=0`, `hi=i-1`. Same logic, just reversed iteration order for clarity.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) if in-place sort |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to sort the array first.
2. Checking all three triangle inequalities instead of just `a + b > c` after sorting.
3. Off-by-one in the two-pointer count: when `A[left] + A[right] > A[k]`, the count is `right - left`, not `right - left + 1`.
4. Degenerate triangle: `1 + 2 = 3` is NOT a valid triangle (need strictly greater).

### Edge Cases to Test
- [ ] Array with fewer than 3 elements --> 0
- [ ] All elements equal --> C(n,3) triangles
- [ ] Degenerate case: `[1, 2, 3]` --> 0 (equality, not strict)
- [ ] Already sorted vs unsorted input

---

## Real-World Use Case
**Structural engineering and mesh generation:** When building triangular meshes for 3D modeling or finite element analysis, you need to verify that proposed edge lengths can actually form valid triangles. Efficiently counting or enumerating valid triangles from a set of available edge lengths is a core geometric computation.

## Interview Tips
- Start by clarifying: "After sorting, for a <= b <= c, we only need a + b > c."
- Mention the brute O(n^3) first, then explain how sorting reduces it.
- The two-pointer counting trick (adding `right - left` at once) is the key insight to articulate.
- Time complexity: O(n^2) is optimal since the answer itself can be O(n^2) triplets.
