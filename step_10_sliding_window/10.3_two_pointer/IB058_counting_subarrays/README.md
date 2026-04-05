# Counting Subarrays

> **Step 10 | 10.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given an array `A` of N **non-negative** integers and an integer `B`, count the number of subarrays whose sum is **strictly less than B**.

A subarray is a contiguous portion of the array.

### Examples

| # | A | B | Output | Explanation |
|---|---|---|--------|-------------|
| 1 | `[2, 5, 6]` | `10` | `4` | Valid: [2]=2, [5]=5, [6]=6, [2,5]=7. Invalid: [5,6]=11, [2,5,6]=13 |
| 2 | `[1, 2, 3]` | `5` | `4` | Valid: [1],[2],[3],[1,2]=3. Invalid: [2,3]=5 (not strictly less), [1,2,3] |
| 3 | `[1, 11, 2]` | `5` | `2` | Valid: [1],[2]. [11] and all subarrays containing 11 are >= 5 |
| 4 | `[0, 0, 0]` | `1` | `6` | All 6 subarrays sum to 0 < 1 |
| 5 | `[5, 6, 7]` | `5` | `0` | No subarray has sum < 5 |

### Constraints
- `1 <= N <= 10^5`
- `0 <= A[i] <= 10^9`
- `1 <= B <= 10^10`
- All elements are **non-negative** (critical for sliding window)

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Time | Space |
|----------|-----------|------|-------|
| Brute Force | Check all subarrays | O(n^2) | O(1) |
| Optimal | Sliding window (two pointers) | O(n) | O(1) |
| Best | Same two pointer + edge cases | O(n) | O(1) |

The **key property** enabling O(n): all elements are non-negative, so the window sum is monotonically non-decreasing as we extend right, and monotonically non-increasing as we shrink left. This makes the two-pointer technique applicable.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check All Subarrays

**Intuition:** Try every possible (start, end) pair. For each starting index i, grow the subarray to the right, accumulating the sum. As soon as the sum reaches B, stop (since all further extensions will have sum >= B due to non-negative elements).

**Steps:**
1. For i from 0 to n-1:
   - Initialize `currSum = 0`.
   - For j from i to n-1:
     - `currSum += A[j]`.
     - If `currSum < B`: `count++`.
     - Else: `break` (no further j can help).
2. Return `count`.

**Dry-Run Trace** (`[2, 5, 6]`, B=10):
```
i=0: j=0: sum=2 <10 count=1; j=1: sum=7 <10 count=2; j=2: sum=13 >=10 break
i=1: j=1: sum=5 <10 count=3; j=2: sum=11 >=10 break
i=2: j=2: sum=6 <10 count=4
Total = 4
```

| Metric | Value |
|--------|-------|
| Time   | O(n^2) -- O(n^2/2) average with early break |
| Space  | O(1) |

---

### Approach 2: Optimal -- Sliding Window

**Intuition:** Use two pointers `left` and `right`. Maintain a window `[left, right]`. For each `right`, keep the window sum < B by shrinking from the left. Once the window is valid (sum < B), **every** subarray that ends at `right` and starts anywhere in `[left, right]` has sum < B. There are `(right - left + 1)` such subarrays.

**Why this works:** Since elements are non-negative:
- Extending right (adding A[right]) only increases the sum.
- Shrinking left (removing A[left]) only decreases the sum.
- So we can greedily maintain the invariant: window sum < B.

**Steps:**
1. Initialize `left = 0`, `windowSum = 0`, `count = 0`.
2. For `right` from 0 to n-1:
   - `windowSum += A[right]`.
   - While `windowSum >= B`: `windowSum -= A[left]; left++`.
   - `count += (right - left + 1)`.
3. Return `count`.

**Dry-Run Trace** (`[1, 2, 3]`, B=5):
```
right=0: sum=1 <5. count += (0-0+1)=1. count=1
right=1: sum=3 <5. count += (1-0+1)=2. count=3
right=2: sum=6 >=5. Remove A[0]=1, sum=5. Still >=5. Remove A[1]=2, sum=3. left=2.
         count += (2-2+1)=1. count=4
Result: 4
```

**Count contribution per right:**
- right=0: subarrays [0..0] -> 1 subarray
- right=1: subarrays [0..1], [1..1] -> 2 subarrays
- right=2: subarrays [2..2] -> 1 subarray (left moved to 2)
Total: 4

| Metric | Value |
|--------|-------|
| Time   | O(n) -- each element enters and leaves the window at most once |
| Space  | O(1) |

---

### Approach 3: Best -- Same Two Pointer (Explicit Edge Cases)

**Intuition:** Identical algorithm to Approach 2, with:
- Early return for `B <= 0` (no subarray of non-negative elements can have sum < 0).
- Cleaner inner while loop without bounds check (safe because `left <= right` is guaranteed when `windowSum >= B` after adding `A[right]`).

**Why `left <= right` is guaranteed:** When `A[right] >= B` (a single element exceeds B), the while loop will advance `left` to `right + 1`, making `right - left + 1 = 0` (no valid subarrays ending at right). This is correct.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) for sliding window:** Each element is added to the window exactly once (when `right` visits it) and removed from the window at most once (when `left` passes it). Total operations = 2n = O(n).

**Two-pointer amortized argument:** The `left` pointer only moves right, never left. It advances at most n times total across the entire outer loop. So the total work of the inner while loop across all iterations of the outer loop is O(n).

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| B = 0 | 0 | No non-negative sum is < 0; return 0 early |
| Single zero element, B = 1 | 1 | [0] has sum 0 < 1 |
| A = [B], single large element | 0 | Single element equals B, not strictly less |
| All zeros | n*(n+1)/2 | All subarrays have sum 0, valid if B > 0 |
| A[i] > B for some element | Subarrays with that element excluded | The break in brute force handles this; window shrinks past it |

**Common Mistakes:**
- Using `<=` instead of `<` in the count condition (overcounts subarrays with sum == B).
- Checking `left < right` instead of `left <= right` in the while loop (can cause infinite loop or missing cases when a single element >= B).
- Integer overflow: sums of large arrays can exceed int range; use `long`.

---

## 6. REAL-WORLD USE CASE

**Network Traffic Monitoring:** Given network packet sizes over time, count the number of time windows where the total data transmitted was under a threshold B (e.g., "how many consecutive sequences of packets fit within a bandwidth limit?").

**Budget Analysis:** Given daily spending amounts (non-negative), count how many spending periods (consecutive days) have a total spend strictly below a budget B.

---

## 7. INTERVIEW TIPS

- **Non-negative constraint is the key:** Immediately mention this enables the sliding window. For arbitrary integers, you'd need prefix sums + sorted structure -> O(n log n).
- **Count contribution per right:** The insight "for each right, add (right - left + 1) subarrays" is the core idea. Explain it clearly.
- **Strictly less vs. at most:** Know both variants. "Count subarrays with sum <= B" uses the same approach but the while condition is `> B` instead of `>= B`.
- **Follow-up: Count subarrays with sum >= B:** Answer = `n*(n+1)/2 - countSubarrays(A, B)`.
- **Follow-up: With negative numbers:** Use prefix sums + binary search or merge sort -> O(n log n).

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Subarray Sum Equals K (LC #560) | Prefix sums for exact sum (not sliding window; handles negatives) |
| Minimum Size Subarray Sum (LC #209) | Sliding window for minimum length with sum >= S |
| Max Consecutive Ones III (LC #1004) | Sliding window with at-most-k constraint |
| Fruit Into Baskets (LC #904) | Sliding window with at-most-2 distinct elements |
| Array 3 Pointers (IB059) | Same two-pointer family, different constraint |
