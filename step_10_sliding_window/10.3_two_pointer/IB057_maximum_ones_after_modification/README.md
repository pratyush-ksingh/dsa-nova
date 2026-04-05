# Maximum Ones After Modification

> **Step 10 | 10.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given a binary array `A` (containing only 0s and 1s) and an integer `B`, find the maximum number of consecutive 1s you can obtain if you are allowed to flip at most `B` zeros to ones.

**Source:** InterviewBit

**Constraints:**
- `1 <= |A| <= 10^5`
- `A[i]` is 0 or 1
- `0 <= B <= |A|`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A = [1,0,0,1,1,0,1,0,1,1,1], B = 2` | `8` | Flip zeros at indices 5 and 7: `[1,0,0,1,1,1,1,1,1,1,1]`, longest consecutive 1s = 8 |
| `A = [0,0,0,0], B = 2` | `2` | Flip any two zeros, max consecutive = 2 |
| `A = [1,1,1,1], B = 0` | `4` | Already all ones |
| `A = [0], B = 0` | `0` | Cannot flip any zero |

### Real-Life Analogy
> *Imagine a road with some potholes (0s) and smooth sections (1s). You have a budget to repair at most B potholes. You want to find the longest stretch of smooth road you can achieve. The sliding window is like driving along the road with a "repair zone" -- as you encounter more potholes than your budget allows, you slide the start of your zone forward, releasing earlier repairs.*

### Key Observations
1. The problem is equivalent to: "Find the longest subarray containing at most B zeros."
2. This is a classic sliding window / two-pointer problem.
3. We do not need to actually flip anything -- just find the window.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- **Sliding window** naturally handles "longest subarray with at most K of something" problems.
- Two pointers maintain a valid window in O(n) time.

### Pattern Recognition
- **Pattern:** Sliding Window with constraint count
- **Classification Cue:** "Whenever you see _longest subarray with at most K violations_ --> think _sliding window tracking violation count_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try Every Subarray
**Idea:** For each starting index, extend right while counting zeros. Stop when zeros exceed B.

**Steps:**
1. For each `i` from 0 to n-1:
   - Count zeros as `j` moves from `i` to n-1.
   - When zeros exceed B, break.
   - Track `max(j - i + 1)`.
2. Return max length.

**Why it works:** Exhaustive search over all subarrays.

**BUD Transition -- Bottleneck:** O(n^2) subarrays. A sliding window avoids recomputing zero counts from scratch.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

---

### Approach 2: Optimal -- Sliding Window
**What changed:** Use two pointers `left` and `right`. Expand `right` one step at a time. When zero count exceeds B, shrink from `left` until valid.

**Steps:**
1. Initialize `left = 0`, `zeros = 0`, `maxLen = 0`.
2. For each `right` from 0 to n-1:
   - If `A[right] == 0`, increment `zeros`.
   - While `zeros > B`: if `A[left] == 0`, decrement `zeros`. Increment `left`.
   - Update `maxLen = max(maxLen, right - left + 1)`.
3. Return `maxLen`.

**Dry Run:** `A = [1,0,0,1,1,0,1,0,1,1,1], B = 2`

| right | A[right] | zeros | left | window size |
|-------|----------|-------|------|-------------|
| 0 | 1 | 0 | 0 | 1 |
| 1 | 0 | 1 | 0 | 2 |
| 2 | 0 | 2 | 0 | 3 |
| 3 | 1 | 2 | 0 | 4 |
| 4 | 1 | 2 | 0 | 5 |
| 5 | 0 | 3->2 | 0->2 | 4 |
| 6 | 1 | 2 | 2 | 5 |
| 7 | 0 | 3->2 | 2->3 | 5 |
| ... continuing | | | | max = 8 |

**Result:** 8

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

### Approach 3: Best -- Non-Shrinking Sliding Window
**Intuition:** Instead of a `while` loop to shrink, use an `if` -- the window only grows or slides (never shrinks). When zeros exceed B, move `left` by exactly 1. The window size never decreases, so the final window size `n - left` equals the maximum.

This is a subtle optimization: we do not need to track `maxLen` explicitly. The window remembers its best size.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using `while` vs `if` confusion: `while` gives correct intermediate results, `if` gives correct final result only via `n - left`.
2. Forgetting `B = 0` case (no flips allowed, just find longest run of 1s).
3. Not handling empty array.

### Edge Cases to Test
- [ ] All zeros, B = 0 --> 0
- [ ] All ones, B = 0 --> n
- [ ] B >= number of zeros --> n (entire array)
- [ ] Single element array
- [ ] B larger than array length

---

## Real-World Use Case
**Network reliability analysis:** Given a sequence of time slots where a connection is up (1) or down (0), and you can apply redundancy/failover to at most B slots, what is the longest uninterrupted service window you can achieve? This directly maps to the sliding window approach.

## Interview Tips
- Immediately recognize this as "longest subarray with at most B zeros" -- a classic sliding window.
- Start with the brute force O(n^2), then optimize to sliding window O(n).
- The non-shrinking window variant is a nice bonus to mention -- shows deep understanding.
- Relate to similar problems: Max Consecutive Ones III (LeetCode 1004) is identical.
