# Selection Sort

> **Batch 1 of 12** | **Topic:** Sorting fundamentals | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of integers, sort it **in-place** in ascending order using the **Selection Sort** algorithm. Selection Sort works by repeatedly finding the minimum element from the unsorted portion and placing it at the beginning.

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `[64, 25, 12, 22, 11]` | `[11, 12, 22, 25, 64]` | Each pass selects the minimum of the remaining unsorted portion |
| `[5, 1, 4, 2, 8]` | `[1, 2, 4, 5, 8]` | Standard unsorted array |
| `[1, 2, 3]` | `[1, 2, 3]` | Already sorted -- still does O(n^2) comparisons |
| `[1]` | `[1]` | Single element is trivially sorted |

### Constraints
- `0 <= arr.length <= 10^4`
- `-10^9 <= arr[i] <= 10^9`

### Real-Life Analogy
Imagine you have a hand of unsorted playing cards laid out on a table. You scan all of them, find the smallest card, and place it in the first position. Then scan the remaining cards, find the next smallest, place it second. Repeat until all cards are in order. That is exactly selection sort.

### 3 Key Observations
1. **"aha" -- select the minimum:** Each iteration scans the unsorted portion to find the minimum, then swaps it into place. The "selection" is the core idea.
2. **"aha" -- sorted boundary grows left-to-right:** After `i` iterations, the first `i` elements are in their final sorted positions. The boundary between sorted and unsorted moves one step right each pass.
3. **"aha" -- always O(n^2) comparisons:** Unlike Bubble Sort (which can short-circuit if no swaps occur), Selection Sort always makes ~n^2/2 comparisons regardless of input order. It is not adaptive.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **In-place array:** Selection Sort operates directly on the input array with O(1) extra space. No auxiliary structures needed.
- It is a comparison-based sort. The key operation is finding the minimum in a subarray.

### Pattern Recognition Cue
Selection Sort is the go-to when you need a simple, in-place sort with minimal swaps (exactly n-1 swaps). Use it when swap cost is high and comparison cost is low, or as a teaching algorithm to understand sorting fundamentals.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Naive Selection Sort (standard)
**Intuition:** This IS the brute force sorting approach. For each position `i`, find the minimum in `arr[i..n-1]` and swap it into position `i`.

**Steps:**
1. For `i` from 0 to n-2:
   a. Set `minIdx = i`.
   b. For `j` from `i+1` to `n-1`:
      - If `arr[j] < arr[minIdx]`, set `minIdx = j`.
   c. Swap `arr[i]` and `arr[minIdx]`.
2. Array is now sorted.

**Dry Run:** `arr = [64, 25, 12, 22, 11]`
```
Pass i=0: scan [64,25,12,22,11] -> min=11 at idx 4 -> swap(0,4) -> [11, 25, 12, 22, 64]
Pass i=1: scan [25,12,22,64]    -> min=12 at idx 2 -> swap(1,2) -> [11, 12, 25, 22, 64]
Pass i=2: scan [25,22,64]       -> min=22 at idx 3 -> swap(2,3) -> [11, 12, 22, 25, 64]
Pass i=3: scan [25,64]          -> min=25 at idx 3 -> no swap   -> [11, 12, 22, 25, 64]
Done: [11, 12, 22, 25, 64]
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) always |
| Space  | O(1) -- in-place |

---

### Approach 2: Optimal -- Selection Sort with Stability Variant
**Intuition:** Standard selection sort is **unstable** (equal elements may change relative order) because of the swap. To make it stable, instead of swapping, **shift** elements right and insert the minimum at position `i`. This preserves relative order of equal elements.

**Steps:**
1. For `i` from 0 to n-2:
   a. Find `minIdx` in `arr[i..n-1]`.
   b. Save `minVal = arr[minIdx]`.
   c. Shift `arr[i..minIdx-1]` one position to the right.
   d. Place `minVal` at position `i`.

**BUD Transition:** Same O(n^2) time, but now the sort is stable. The tradeoff is more memory moves per pass (shift vs single swap).

**Note:** For selection sort, O(n^2) is inherent. The "optimal" here means the best version of selection sort, not the best sorting algorithm overall.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) always |
| Space  | O(1) -- in-place |

---

### Approach 3: Best -- Double-Ended Selection Sort (Cocktail Selection)
**Intuition:** In each pass, find both the minimum AND the maximum. Place the minimum at the front and the maximum at the back. This halves the number of passes (though total comparisons are similar).

**Steps:**
1. Set `left = 0`, `right = n - 1`.
2. While `left < right`:
   a. Find `minIdx` and `maxIdx` in `arr[left..right]`.
   b. Swap `arr[left]` with `arr[minIdx]`.
   c. If `maxIdx == left`, then the max was moved to `minIdx` by the previous swap, so update `maxIdx = minIdx`.
   d. Swap `arr[right]` with `arr[maxIdx]`.
   e. `left++`, `right--`.

**BUD Transition from standard:** Fewer passes (n/2 vs n), same asymptotic complexity. Slightly better constant factor.

**Dry Run:** `arr = [64, 25, 12, 22, 11]`
```
Pass 1 (left=0, right=4):
  Scan [64,25,12,22,11]: min=11 at 4, max=64 at 0
  Swap arr[0] <-> arr[4] -> [11, 25, 12, 22, 64]
  maxIdx was 0, now updated to 4 (where 64 landed)
  Swap arr[4] <-> arr[4] -> no change -> [11, 25, 12, 22, 64]
  left=1, right=3

Pass 2 (left=1, right=3):
  Scan [25,12,22]: min=12 at 2, max=25 at 1
  Swap arr[1] <-> arr[2] -> [11, 12, 25, 22, 64]
  Swap arr[3] <-> arr[1]? maxIdx=1, but arr[1] is now 12...
  Actually maxIdx was 1, but we swapped arr[1]<->arr[2], so if maxIdx==left(1), update maxIdx=2(minIdx). But maxIdx(1)==left(1), so maxIdx=minIdx=2.
  Swap arr[3] <-> arr[2] -> [11, 12, 22, 25, 64]
  left=2, right=2 -> stop

Done: [11, 12, 22, 25, 64]
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) -- same asymptotic, ~25% fewer comparisons in practice |
| Space  | O(1) -- in-place |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n^2):** In pass `i`, we scan `n - i` elements. Total comparisons: n + (n-1) + ... + 1 = n(n-1)/2. That is O(n^2) regardless of input order.
- **Space O(1):** We only use a few index variables. The sort is in-place.
- **Why not O(n log n)?** Selection sort does not divide the problem (no recursion, no partitioning). It is a simple nested loop. To achieve O(n log n), you need divide-and-conquer (merge sort) or a smart partitioning scheme (quicksort).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| Empty array `[]` | `[]` | Loop simply does not execute |
| Single element `[5]` | `[5]` | Trivially sorted |
| Already sorted `[1,2,3,4,5]` | `[1,2,3,4,5]` | Still O(n^2) comparisons -- selection sort is not adaptive |
| Reverse sorted `[5,4,3,2,1]` | `[1,2,3,4,5]` | Maximum number of swaps (n-1) |
| All duplicates `[7,7,7]` | `[7,7,7]` | No swaps needed, but still scans |
| Negative numbers `[-3,1,-5,2]` | `[-5,-3,1,2]` | Works fine -- comparison handles negatives |

**Common Mistakes:**
- Off-by-one: inner loop should start at `i+1`, not `i`.
- Swapping even when `minIdx == i` (not a bug, just wasteful -- add a guard if you want).
- Confusing selection sort with insertion sort (insertion sort shifts and inserts; selection sort finds min and swaps).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need to sort an array in-place using selection sort."
2. **Match:** "Selection sort: repeatedly select the minimum from the unsorted portion."
3. **Plan:** "Outer loop marks the boundary. Inner loop finds the minimum. Swap into place."
4. **Implement:** Write the nested loop.
5. **Review:** Dry-run on a small example.
6. **Evaluate:** O(n^2) time, O(1) space. Mention it always does n^2/2 comparisons but only n-1 swaps.

### Follow-Up Questions
- *"Is selection sort stable?"* -- No, by default. Swapping can change relative order of equal elements. Can be made stable with shifting instead of swapping.
- *"When would you prefer selection sort over other O(n^2) sorts?"* -- When the cost of swapping is very high (e.g., writing to flash memory), since selection sort does the fewest swaps (at most n-1).
- *"Can you make it O(n log n)?"* -- Not with selection sort. But you could use a heap to find the minimum in O(log n) each pass -- that is essentially heap sort.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Array basics, swap operation |
| **Same Pattern** | Bubble Sort, Insertion Sort -- all O(n^2) elementary sorts |
| **Harder** | Heap Sort (selection sort + heap = O(n log n)) |
| **Unlocks** | Understanding of comparison-based sort lower bounds, introduction to divide-and-conquer sorts |
