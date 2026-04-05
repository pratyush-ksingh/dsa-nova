# Max Consecutive Ones III

> **Batch 4 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a binary array `nums` and an integer `k`, return the **maximum number of consecutive 1s** in the array if you can flip at most `k` 0s to 1s.

**LeetCode #1004**

### Examples

| Input                                | Output | Explanation                                           |
|--------------------------------------|--------|-------------------------------------------------------|
| `nums=[1,1,1,0,0,0,1,1,1,1,0], k=2` | 6      | Flip zeros at index 5 and 10: [1,1,1,0,0,**1**,1,1,1,1,**1**] -> 6 consecutive |
| `nums=[0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], k=3` | 10 | Flip 3 zeros for max window |
| `nums=[1,1,1,1], k=0`               | 4      | Already all 1s                                        |
| `nums=[0,0,0], k=0`                 | 0      | Cannot flip any, no 1s                                |

### Analogy
Imagine a fence with some white (1) and black (0) planks. You have `k` cans of white paint. You want to find the longest segment of the fence that can become all white by painting at most `k` black planks. You slide a window along the fence: if you see a black plank, you paint it (use a can). When you run out of cans, you slide the left end until you "unpaint" a plank.

### 3 Key Observations
1. **"Aha" -- Reframe the problem.** "Max consecutive 1s after flipping k zeros" = "longest subarray with at most k zeros." This is a classic sliding window problem.
2. **"Aha" -- The window tracks zero count.** Expand right. When `zeroCount > k`, shrink left. The window always represents a valid segment (at most k zeros inside).
3. **"Aha" -- We never actually flip anything.** We just find the longest window. The "flipping" is conceptual.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm                  | Why?                                     |
|------------------|---------------|----------------------------|-------------------------------------------|
| Brute Force      | None          | Double loop                | Try every subarray, count zeros           |
| Optimal          | None          | Sliding window (shrinking) | Classic expand-right, shrink-left pattern |
| Best             | None          | Sliding window (non-shrink)| Window only grows or slides, never shrinks|

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try All Subarrays
**Intuition:** For each starting index, expand right counting zeros. Track the longest subarray where `zeroCount <= k`.

**Steps:**
1. For each `i` from 0 to n-1:
   - `zeroCount = 0`, `maxLen = 0`
   - For each `j` from `i` to n-1:
     - If `nums[j] == 0`, increment `zeroCount`.
     - If `zeroCount > k`, break.
     - Update `maxLen = max(maxLen, j - i + 1)`.
2. Return `maxLen`.

**Dry-Run Trace** with `nums=[1,1,0,0,1,1], k=1`:
```
i=0: j=0(1) j=1(1) j=2(0,z=1) j=3(0,z=2>1 break) -> maxLen=3
i=1: j=1(1) j=2(0,z=1) j=3(0,z=2>1 break) -> maxLen=3
i=2: j=2(0,z=1) j=3(0,z=2>1 break) -> maxLen=3
i=3: j=3(0,z=1) j=4(1) j=5(1) -> maxLen=3
...
Answer: 3
```

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

---

### Approach 2: Optimal -- Sliding Window (Shrinking)
**Intuition:** Maintain a window `[left, right]` with at most `k` zeros. Expand `right` one step at a time. When `zeroCount > k`, shrink `left` until `zeroCount <= k`. Track the maximum window size.

**Steps:**
1. `left = 0`, `zeroCount = 0`, `maxLen = 0`.
2. For `right` from 0 to n-1:
   - If `nums[right] == 0`, increment `zeroCount`.
   - While `zeroCount > k`:
     - If `nums[left] == 0`, decrement `zeroCount`.
     - Increment `left`.
   - `maxLen = max(maxLen, right - left + 1)`.
3. Return `maxLen`.

**Dry-Run Trace** with `nums=[1,1,1,0,0,0,1,1,1,1,0], k=2`:
```
r=0: 1, z=0, window=[0,0] len=1, max=1
r=1: 1, z=0, window=[0,1] len=2, max=2
r=2: 1, z=0, window=[0,2] len=3, max=3
r=3: 0, z=1, window=[0,3] len=4, max=4
r=4: 0, z=2, window=[0,4] len=5, max=5
r=5: 0, z=3>2, shrink: l=0(1) l=1(1) l=2(1) l=3(0,z=2), left=4
     window=[4,5] len=2, max=5
r=6: 1, z=2, window=[4,6] len=3, max=5
r=7: 1, z=2, window=[4,7] len=4, max=5
r=8: 1, z=2, window=[4,8] len=5, max=5
r=9: 1, z=2, window=[4,9] len=6, max=6
r=10: 0, z=3>2, shrink: l=4(0,z=2), left=5
      window=[5,10] len=6, max=6
Answer: 6
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Non-Shrinking Sliding Window
**Intuition:** Instead of shrinking the window when it becomes invalid, just slide it (move left by 1 when we move right by 1). The window size never decreases -- it either grows or stays the same. This works because we only care about the maximum, so once we have found a window of size `W`, we never need a smaller one.

**Steps:**
1. `left = 0`, `zeroCount = 0`.
2. For `right` from 0 to n-1:
   - If `nums[right] == 0`, increment `zeroCount`.
   - If `zeroCount > k`:
     - If `nums[left] == 0`, decrement `zeroCount`.
     - Increment `left`.
     - (Note: `if`, not `while` -- we move left by exactly 1.)
3. Return `n - left` (the final window size, which is the max).

**Why this works:** The window `right - left + 1` never shrinks. If the current window is invalid, we slide both ends by 1. The maximum valid window size is captured by the final value of `right - left + 1 = n - left`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## COMPLEXITY INTUITIVELY

- **Why O(n):** Each element is visited by `right` exactly once. The `left` pointer only moves forward and visits each element at most once. Total work: at most 2n.
- **Non-shrinking variant:** Even simpler -- `left` and `right` each advance exactly n times total.
- **Why O(1) space:** Only a few integer variables.

---

## EDGE CASES & MISTAKES

| Edge Case              | What Happens                                    |
|------------------------|-------------------------------------------------|
| `k = 0`               | No flips allowed. Find longest run of 1s.       |
| All 1s                 | Entire array is the answer (length n).          |
| All 0s, k >= n         | Flip everything, answer is n.                   |
| All 0s, k < n          | Answer is k (flip k zeros).                     |
| `k >= total zeros`     | Entire array, answer is n.                      |
| Single element         | Return 1 if it's 1 or k >= 1; else 0.          |

**Common Mistakes:**
- Using `while` in the non-shrinking variant (should be `if`).
- Returning `maxLen` instead of `n - left` in the non-shrinking variant.
- Off-by-one in the window size calculation.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Classic sliding window problem. Tests the ability to reframe "flip at most k" as "window with at most k zeros."
- **Follow-ups:**
  - "What if you can flip at most k 1s to 0s?" (Same idea, count 1s instead of 0s.)
  - "What if k is very large?" (Answer is just n.)
  - "Return the actual indices, not just the length." (Track the best left/right.)
- **Communication tip:** Start by reframing: "This is equivalent to finding the longest subarray with at most k zeros." Then describe the sliding window.

---

## CONNECTIONS

| Related Problem                    | How It Connects                                |
|-----------------------------------|------------------------------------------------|
| Max Consecutive Ones (LC #485)    | Simpler version with k=0                       |
| Max Consecutive Ones II (LC #487) | Same problem with k=1                          |
| Longest Repeating Char Replacement| Same pattern: window with at most k "bad" chars|
| Minimum Window Substring          | Another sliding window with constraint         |

---

## Real-World Use Case
**Telecommunications / Signal processing:** In a noisy binary signal, you want to find the longest stretch of "good" signal (1s) if you can tolerate (correct) at most `k` bit errors (0s). This sliding window approach processes the signal in real-time with O(1) per sample.
