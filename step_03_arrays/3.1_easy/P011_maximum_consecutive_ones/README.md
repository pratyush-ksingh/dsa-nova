# Maximum Consecutive Ones

> **Batch 4 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a binary array `nums` (containing only 0s and 1s), find the **maximum number of consecutive 1s** in the array.

**LeetCode #485**

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,1,0,1,1,1]` | `3` | The last three elements form the longest run of 1s |
| `[1,0,1,1,0,1]` | `2` | Two consecutive 1s at indices 2-3 |
| `[0,0,0]` | `0` | No 1s at all |
| `[1]` | `1` | Single 1 |
| `[1,1,1,1]` | `4` | All 1s |

### Constraints
- 1 <= nums.length <= 10^5
- nums[i] is 0 or 1

### Real-Life Analogy
Imagine you are watching a **traffic light** and counting how many consecutive green lights you hit while driving down a street. Each intersection is either green (1) or red (0). You keep a running count of consecutive greens, and whenever you hit a red, you reset your counter. At the end of the drive, your personal best streak is the answer.

### Key Observations
1. We need to track a **running count** of consecutive 1s and a **global maximum**. When we hit a 0, reset the running count.
2. This is a single-pass problem. No need to look back or forward -- just maintain state as you scan left to right.
3. **Aha moment:** This is the simplest instance of the "maximum consecutive" or "maximum streak" pattern. The same technique generalizes to finding the longest substring/subarray satisfying any condition -- reset on violation, update max on success.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Single-Pass Linear Scan?
We need the maximum of all consecutive runs. A single pass with two variables (current streak, best streak) captures everything. No sorting, no extra data structure.

### Pattern Recognition
**Classification cue:** "Find the longest consecutive run of X" --> single-pass with a counter that resets. This is a simplified sliding window where the window shrinks to 0 on violation.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check All Subarrays
**Intuition:** For each starting index, count how many consecutive 1s follow it. Track the maximum.

**Steps:**
1. For each `i` from 0 to n-1 where `nums[i] == 1`:
2. Count consecutive 1s starting from `i`.
3. Update global max.

**Dry Run (nums = [1,1,0,1,1,1]):**

| i | Start | Consecutive 1s | Max so far |
|---|-------|---------------|------------|
| 0 | 1     | 2 (indices 0,1) | 2 |
| 1 | 1     | 1 (index 1) | 2 |
| 3 | 1     | 3 (indices 3,4,5) | 3 |
| 4 | 1     | 2 (indices 4,5) | 3 |
| 5 | 1     | 1 (index 5) | 3 |

Result: 3

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(1) |

**BUD Transition:** Many subarrays overlap. We re-count 1s we already counted. A single pass avoids this redundancy.

---

### Approach 2: Optimal -- Single Pass with Running Counter
**Intuition:** Scan left to right. If current element is 1, increment the counter. If it is 0, reset the counter to 0. Update the maximum after each step.

**Steps:**
1. Initialize `count = 0`, `max_count = 0`.
2. For each element:
   - If `nums[i] == 1`: `count += 1`.
   - Else: `count = 0`.
   - `max_count = max(max_count, count)`.
3. Return `max_count`.

**Dry Run (nums = [1,1,0,1,1,1]):**

| i | nums[i] | count | max_count |
|---|---------|-------|-----------|
| 0 | 1       | 1     | 1 |
| 1 | 1       | 2     | 2 |
| 2 | 0       | 0     | 2 |
| 3 | 1       | 1     | 2 |
| 4 | 1       | 2     | 2 |
| 5 | 1       | 3     | 3 |

Result: 3

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

*This is already the best possible approach.*

---

### Approach 3: Best -- Same Logic, Pythonic One-Liner (Conceptual)
**Intuition:** In Python, you can split the array string by '0' and find the longest segment of '1's. Conceptually the same O(n) scan, but expressed differently.

**Steps (Python-specific):**
1. Convert array to string: `"".join(map(str, nums))`
2. Split by '0': `segments = s.split('0')`
3. Return `max(len(seg) for seg in segments)`

This is functionally identical to the single-pass counter but demonstrates string manipulation thinking. In Java, the explicit counter is cleaner.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for the string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal?** The maximum streak could end at any position, so we must examine every element. One pass is sufficient because the streak information at position `i` only depends on position `i-1`. No lookahead or lookback needed.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Not updating max_count when streak is at the end | If the array ends with a streak, the max might not be updated | Update max_count on every iteration, not just on 0 |
| Forgetting to reset count on 0 | Leads to counting non-consecutive 1s | Always reset count = 0 when nums[i] == 0 |
| Returning count instead of max_count | Current streak may not be the longest | Always return max_count |

### Edge Cases Checklist
- All 0s: `[0, 0, 0]` --> 0
- All 1s: `[1, 1, 1]` --> 3
- Single element: `[0]` --> 0, `[1]` --> 1
- Alternating: `[1, 0, 1, 0, 1]` --> 1
- Streak at end: `[0, 1, 1]` --> 2

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Binary array, find the longest consecutive run of 1s."
2. **M**atch: "Running counter / streak pattern."
3. **P**lan: "Single pass: increment on 1, reset on 0, track max."
4. **I**mplement: Three lines of logic inside the loop.
5. **R**eview: Walk through [1,1,0,1,1,1] showing count resets.
6. **E**valuate: "O(n) time, O(1) space. Cannot do better."

### Follow-Up Questions
- "What if you can flip at most one 0 to 1?" --> Sliding window with at most one 0 inside (LeetCode #487).
- "What if you can flip at most k 0s?" --> Sliding window with at most k 0s (LeetCode #1004).
- "What about consecutive 0s?" --> Same logic, just check `nums[i] == 0`.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic array traversal |
| **Same pattern** | Longest Substring Without Repeating Characters (streak/window) |
| **Harder variant** | Max Consecutive Ones II (one flip), Max Consecutive Ones III (k flips) |
| **Unlocks** | Sliding window technique, streak-based problems |
