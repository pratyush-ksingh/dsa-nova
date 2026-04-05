# Pick from Both Sides

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `A` of length `n` and an integer `B`, pick exactly `B` elements total from either the **left end** or the **right end** of the array (in any combination) to **maximize** their sum.

You must pick exactly `B` elements. If you pick `i` from the left, you pick `B - i` from the right.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A = [5, -2, 3, 1, 2], B = 3` | `8` | Pick 1 from left (5) + 2 from right (1, 2) = 5+1+2=8 |
| `A = [1, 2], B = 1` | `2` | Pick 1 from right (2) |
| `A = [-1, -2, -3, -4, -5], B = 2` | `-3` | Best: pick -1 + -2 = -3 |
| `A = [1, 2, 3, 4, 5], B = 2` | `9` | Pick 2 from right: 4+5=9 |
| `A = [10, 1, 1, 1, 1, 1, 10], B = 2` | `20` | Pick 1 from each end: 10+10=20 |

**Constraints:**
- `1 <= len(A) <= 10^5`
- `1 <= B <= len(A)`
- `-10^4 <= A[i] <= 10^4`

### Real-Life Analogy
> *Imagine a buffet line where dishes are arranged in a row and you can only grab from the two ends. You have exactly B tokens to spend. You want to maximize taste. You would not try every combination manually -- instead you start by tentatively grabbing all B dishes from the left, then progressively swap the leftmost grab for a dish from the right end, checking if that improves your total. That is exactly the sliding window idea.*

### Key Observations
1. The picked elements always form two contiguous groups: a prefix of length `i` and a suffix of length `B - i`, where `0 <= i <= B`.
2. There are only `B + 1` possible splits, so even brute force is manageable for small B, but O(B) is achievable.
3. Sliding window insight: start with the prefix of length B, then iteratively replace the leftmost prefix element with the next right-end element. The sum changes by `A[n-i] - A[B-i]` at step `i`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No extra data structure needed. We track a running window sum with a single integer variable.
- The sliding window implicitly represents "take i from left, B-i from right" as we slide from all-left to all-right.

### Pattern Recognition
- **Pattern:** Sliding Window on circular ends
- **Classification Cue:** "Pick elements from both ends to optimize a value --> sliding window or prefix sum from both ends."

---

## APPROACH LADDER

### Approach 1: Brute Force (Try all splits)
**Idea:** For each possible `leftCount` from 0 to B, sum the first `leftCount` elements plus the last `B - leftCount` elements. Track the maximum.

**Steps:**
1. For `leftCount` in range 0 to B (inclusive):
   - `current = sum(A[0..leftCount-1]) + sum(A[n-(B-leftCount)..n-1])`
   - Update `maxSum = max(maxSum, current)`.
2. Return `maxSum`.

**Why it works:** Exhaustively checks all B+1 valid configurations.

**Why we move on:** Each iteration recomputes the sums from scratch in O(B), giving O(B^2) total. We can maintain a running sum instead.

| Time | Space |
|------|-------|
| O(B^2) | O(1) |

---

### Approach 2: Optimal (Prefix sum + sliding window)
**What changed:** Compute the initial sum of the first B elements once in O(B), then slide the window in O(1) per step.

**Steps:**
1. Compute `windowSum = sum(A[0..B-1])`. Set `maxSum = windowSum`.
2. For `i` from 1 to B:
   - Remove `A[B - i]` from the window (no longer picked from left).
   - Add `A[n - i]` to the window (newly picked from right).
   - Update `maxSum`.
3. Return `maxSum`.

**Dry Run:** `A = [5, -2, 3, 1, 2], B = 3`

| i | Remove (left) | Add (right) | windowSum | maxSum |
|---|--------------|-------------|-----------|--------|
| - | -            | -           | 5-2+3=6   | 6      |
| 1 | A[2]=3       | A[4]=2      | 6-3+2=5   | 6      |
| 2 | A[1]=-2      | A[3]=1      | 5-(-2)+1=8| 8      |
| 3 | A[0]=5       | A[2]=3      | 8-5+3=6   | 8      |

**Result:** 8

| Time | Space |
|------|-------|
| O(B) | O(1) |

---

### Approach 3: Best (Same sliding window, clearest form)
**What changed:** Identical O(B) algorithm written with cleaner variable names and a single loop that is easier to read at a glance.

**Steps:**
1. `curr = sum(A[0..B-1])`, `result = curr`.
2. For `i` from 1 to B: `curr += A[n-i] - A[B-i]`, `result = max(result, curr)`.
3. Return `result`.

*The loop invariant: after iteration i, `curr` equals the sum when taking `B-i` elements from the left and `i` elements from the right.*

| Time | Space |
|------|-------|
| O(B) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(B) -- "We scan through the B boundary positions exactly once."
**Space:** O(1) -- "Only a constant number of integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Off-by-one in window slide:** When removing from the left side, the index to remove is `A[B - i]`, NOT `A[B - i - 1]`. Draw the array for B=3, n=5 to verify.
2. **When rightCount = 0:** The suffix sum is empty -- make sure your code handles `sum(A[n-0:])` as 0 (Python handles it; Java loop `for i = n; i < n` runs zero times).
3. **Negative all-elements case:** Max sum may still be negative. Initialize `maxSum` to the first window sum (or `Integer.MIN_VALUE`), not 0.

### Edge Cases to Test
- [ ] B = n: must pick all elements; sum is fixed
- [ ] B = 1: pick max(A[0], A[n-1])
- [ ] All negative values: still find least negative
- [ ] Single element array with B = 1
- [ ] Array with all equal elements

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Exactly B elements from left or right ends in any split. Maximize sum."
2. **Approach:** "B+1 possible splits. Brute force is O(B^2). Sliding window: start with all-left, swap one left element for one right element per step. O(B)."
3. **Code:** "Initialize window sum as first B elements. Loop i=1 to B: subtract A[B-i], add A[n-i]. Track max."
4. **Test:** Walk through A=[5,-2,3,1,2], B=3.

### Follow-Up Questions
- "What if you could pick from the middle too?" --> Becomes a different problem; consider max subarray of complement.
- "What if B > n/2?" --> Same algorithm still works; just note that left and right windows may overlap when B > n, but the problem guarantees B <= n.
- "How would you return the actual elements picked, not just the sum?" --> Record which `i` (leftCount) gives the maximum, then output A[0..i-1] and A[n-(B-i)..n-1].

---

## CONNECTIONS
- **Prerequisite:** Prefix sums, sliding window basics
- **Same Pattern:** Maximum Sum of Subarray of size K, Minimum Size Subarray Sum
- **Harder Variant:** Pick from Both Sides with constraints on which elements can be adjacent
- **This Unlocks:** All "sliding window on answer space" problems; complement-window thinking
