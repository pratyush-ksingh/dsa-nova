# Wave Array

> **Step 03 | 3.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array of integers, sort it in **wave form** such that `a[0] >= a[1] <= a[2] >= a[3] <= a[4] ...`. In other words, elements at even indices should be greater than or equal to their neighbors, and elements at odd indices should be less than or equal to their neighbors.

Multiple valid answers may exist; any valid wave arrangement is accepted.

**Examples:**

| Input | One Valid Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, 4, 5]` | `[2, 1, 4, 3, 5]` | 2>=1, 1<=4, 4>=3, 3<=5 |
| `[10, 5, 6, 3, 2, 20, 100, 80]` | `[5, 3, 10, 2, 20, 6, 100, 80]` | Wave property holds at every position |
| `[1, 2, 3, 4]` | `[2, 1, 4, 3]` | Simple swap of adjacent pairs after sorting |

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

### Real-Life Analogy
> *Think of a picket fence where posts alternate between tall and short. You have a pile of posts of different heights and need to arrange them so each even-positioned post is at least as tall as its neighbors. You could sort all posts by height first and swap adjacent pairs, or you could walk along the fence and just fix any position that violates the pattern.*

### Key Observations
1. After sorting, adjacent elements differ by the minimum amount. Swapping pairs `(0,1), (2,3), ...` creates a valid wave because the swapped element is always >= its new neighbors.
2. Without sorting, we can enforce the wave property locally: at each even index, make sure it is the local maximum. One pass suffices because fixing position `i` does not break position `i-2`.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Sort + Swap Adjacent Pairs
**Intuition:** Sort the array so elements are in non-decreasing order. Then swap every pair of adjacent elements: `(arr[0], arr[1])`, `(arr[2], arr[3])`, etc. After sorting, `arr[i] <= arr[i+1]`, so after swapping, `arr[i] >= arr[i+1]` and the wave property is satisfied.

**Steps:**
1. Sort the array in ascending order.
2. Iterate with step 2: for i = 0, 2, 4, ...
   - Swap `arr[i]` and `arr[i+1]` (if i+1 exists).
3. Return the modified array.

**Dry Run:** `[1, 2, 3, 4, 5]`
```
After sort: [1, 2, 3, 4, 5]
Swap (0,1): [2, 1, 3, 4, 5]
Swap (2,3): [2, 1, 4, 3, 5]
Result: [2, 1, 4, 3, 5]  -->  2>=1 <= 4>=3 <= 5  (valid wave)
```

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) extra (in-place sort) |

---

### Approach 2: Optimal -- Sort + Swap (Same Approach)
**Intuition:** The sort + swap approach is already simple and correct. For many competitive programming judges, this is the expected "optimal" solution since it guarantees a unique lexicographically smallest wave form (when sorting gives the smallest arrangement first).

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) extra |

---

### Approach 3: Best -- Single Pass (No Sorting)
**Intuition:** We do not need to sort at all. Simply traverse the array and at every even index, ensure it is a local maximum. If `arr[i] < arr[i-1]`, swap them. If `arr[i] < arr[i+1]`, swap them. This works because each swap only increases the even-index value, and odd-index positions naturally become local minima.

**Steps:**
1. Iterate with step 2: for i = 0, 2, 4, ...
   - If `i > 0` and `arr[i] < arr[i-1]`: swap `arr[i]` and `arr[i-1]`.
   - If `i < n-1` and `arr[i] < arr[i+1]`: swap `arr[i]` and `arr[i+1]`.
2. Return the modified array.

**Why it works:** At even index `i`, after ensuring `arr[i] >= arr[i-1]` and `arr[i] >= arr[i+1]`, the odd-index neighbors are automatically <= their even-index neighbors. This does not disturb previously fixed positions because we process left to right and the swap at `i` only affects `i` and one neighbor.

**Dry Run:** `[1, 2, 3, 4, 5]`
```
i=0: arr[0]=1 < arr[1]=2 -> swap -> [2, 1, 3, 4, 5]
i=2: arr[2]=3 < arr[1]=1? No. arr[2]=3 < arr[3]=4? Yes -> swap -> [2, 1, 4, 3, 5]
i=4: arr[4]=5 < arr[3]=3? No. No next element.
Result: [2, 1, 4, 3, 5] (valid wave)
```

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) extra |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Off-by-one with odd-length arrays:** The last element (if at an even index) has no right neighbor. Check bounds before swapping.
2. **Modifying during wrong pass direction:** Processing right to left can break previously fixed positions.
3. **Confusing wave direction:** Some problems define wave as `a[0] <= a[1] >= a[2] ...` (inverted). Read the problem statement carefully.

### Edge Cases
- Single element: already a valid wave
- Two elements: swap if needed so `arr[0] >= arr[1]`
- All elements equal: already a valid wave, no swaps needed
- Already sorted ascending or descending

---

## Real-World Use Case
Wave sort is used in **signal processing** to create alternating high-low patterns for testing filters. It also appears in **UI design** where alternating row heights create a visual rhythm. The O(n) single-pass approach is a good example of how **local constraints can be enforced globally** in a single traversal.

## Interview Tips
- Start with the sort + swap approach -- it is easy to explain and prove correct.
- Then optimize: "Do we really need to sort? We only need local maximums at even indices."
- The O(n) solution is a classic interview trick: enforce local invariants in a single pass.
- Note that the sort-based approach gives a unique/deterministic output (useful when the problem asks for lexicographically smallest), while the O(n) approach may give different valid outputs.
- Follow-up: "What if you need the lexicographically smallest wave array?" -- Then you must sort first.
