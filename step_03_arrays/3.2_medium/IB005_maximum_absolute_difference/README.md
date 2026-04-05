# Maximum Absolute Difference

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

Given an array `A` of `N` integers, find:

```
max over all (i, j) of  |A[i] - A[j]| + |i - j|
```

where `i` and `j` are valid indices (can be equal, but the max is trivially achieved when they differ).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 3, -1]` | `5` | i=1 (val=3), j=2 (val=-1): |3-(-1)| + |1-2| = 4+1=5 |
| `[1, 2, 3, 4, 5]` | `8` | i=0 (val=1), j=4 (val=5): |1-5|+|0-4|=4+4=8 |
| `[3, -2]` | `6` | |3-(-2)| + |0-1| = 5+1=6 |

## Constraints

- `1 <= N <= 10^5`
- `-10^9 <= A[i] <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** Try all pairs `(i, j)` and compute `|A[i]-A[j]| + |i-j|` directly, tracking the maximum.

**Steps:**
1. Initialize `ans = 0`.
2. For each pair `(i, j)`: `ans = max(ans, abs(A[i]-A[j]) + abs(i-j))`.
3. Return `ans`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

## Approach 2: Optimal — Expand Absolute Values

**Intuition:** The expression `|A[i] - A[j]| + |i - j|` has four cases based on the signs inside the absolute values:

```
Case 1: A[i] >= A[j] and i >= j:  (A[i] + i) - (A[j] + j)
Case 2: A[i] >= A[j] and i <  j:  (A[i] - i) - (A[j] - j)
Case 3: A[i] <  A[j] and i >= j:  (A[j] - j) - (A[i] - i)  [same as Case 2 with i,j swapped]
Case 4: A[i] <  A[j] and i <  j:  (A[j] + j) - (A[i] + i)  [same as Case 1 with i,j swapped]
```

So the maximum is:
```
max(max(A[i]+i) - min(A[j]+j),   max(A[i]-i) - min(A[j]-i))
```

**Steps:**
1. In one pass, track `maxPlus = max(A[i]+i)`, `minPlus = min(A[i]+i)`, `maxMinus = max(A[i]-i)`, `minMinus = min(A[i]-i)`.
2. Return `max(maxPlus - minPlus, maxMinus - minMinus)`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 3: Best — Compact Two-Family Ranges

**Intuition:** Equivalent to Approach 2. The answer is the maximum **range** (max - min) over two families of transformed values:
- Family 1: `{A[i] + i}` — value shifted by index
- Family 2: `{A[i] - i}` — value shifted by negative index

The maximum of `|f(i) - f(j)|` over all pairs is simply `max(f) - min(f)`. Apply this to both families and return the larger.

**Steps:**
1. Compute `max1 - min1` where `f1(i) = A[i] + i`.
2. Compute `max2 - min2` where `f2(i) = A[i] - i`.
3. Return `max(max1-min1, max2-min2)`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Geospatial distance optimization:** Imagine points on a 1D grid where position `i` represents time and `A[i]` represents elevation or cost. The expression `|A[i]-A[j]| + |i-j|` is a Manhattan-distance-like metric. Finding the maximum such distance is used in signal processing (finding the most "spread out" pair of samples), in time series analysis (finding the pair of timepoints most different in both value and time), and as a building block in more complex 2D Manhattan distance problems.

## Interview Tips

- The key trick is the **absolute value expansion**: `|x| + |y| = max(x+y, x-y, -x+y, -x-y)`. Applying this to `(A[i]-A[j])` and `(i-j)` gives four linear expressions whose max over all pairs reduces to tracking four scalars.
- This reduces an O(n^2) brute force to a single O(n) pass — a classic "remove absolute value by case analysis" technique.
- The two families `(A[i]+i)` and `(A[i]-i)` are the key insight. Write them down during interviews to show the derivation.
- Symmetry: `|A[i]-A[j]| + |i-j|` is symmetric in `i` and `j`, so each pair is counted the same way in both directions. You don't need to worry about ordered vs unordered pairs — the max is the same.
- This technique generalizes: `|A[i]-A[j]| + |B[i]-B[j]|` (2D Manhattan distance) similarly reduces to 4 linear expressions.
- Related: LeetCode 1131 (Maximum of Absolute Value Expression), 2D Manhattan distance problems.
