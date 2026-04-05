# Diffk II

> **Step 01 | 1.4** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
Given a **sorted** integer array `A` and a non-negative integer `k`, determine if there exist two indices `i` and `j` such that `i != j` and `A[i] - A[j] = k`. Return **1** if such a pair exists, **0** otherwise.

**Example:**
```
Input:  A = [1, 2, 3, 4, 5], k = 2
Output: 1
Explanation: A[2] - A[0] = 3 - 1 = 2 = k.

Input:  A = [1, 3, 5], k = 4
Output: 1
Explanation: A[2] - A[0] = 5 - 1 = 4 = k.
```

| Input | k | Output | Explanation |
|-------|---|--------|-------------|
| [1, 2, 3, 4, 5] | 2 | 1 | 3-1=2 |
| [1, 3, 5] | 4 | 1 | 5-1=4 |
| [1, 1] | 0 | 1 | A[0]-A[1]=0, i!=j |
| [1, 2, 3] | 0 | 0 | No duplicates, k=0 needs duplicates |
| [1, 5, 10] | 5 | 1 | 10-5=5 or 5-0? No, but 10-5=5 works |

### Real-Life Analogy
Imagine a **staircase** where each step has a height marked on it (sorted). You want to find two steps whose height difference is exactly `k` -- like jumping from one step to another that is exactly `k` units higher. Since the steps are in order (sorted), you can efficiently slide two markers along the staircase.

### Key Observations
1. The array is **sorted** -- this is a huge hint for two-pointer technique.
2. `A[i] - A[j] = k` means we need a pair with exact difference `k`.
3. When `k = 0`, we need two distinct indices with the same value (duplicates).
4. **Aha moment:** Since the array is sorted, a two-pointer approach gives O(N) time with O(1) space -- better than the hash-based O(N) space solution.

### Constraints
- 1 <= |A| <= 10^6
- 0 <= k <= 10^9
- Array is sorted in non-decreasing order

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Two Pointers / Hashing?
- **HashSet:** For each element, check if `element - k` or `element + k` has been seen. O(N) time, O(N) space.
- **Two Pointers:** Since sorted, maintain `i` (larger) and `j` (smaller). If diff too small, advance `i`; if too big, advance `j`. O(N) time, O(1) space.

### Pattern Recognition
**Classification cue:** "Sorted array + find pair with given difference" --> two pointers. If unsorted, use HashSet.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Loops
**Intuition:** Check every pair `(i, j)` where `i != j`. If `A[i] - A[j] == k`, return 1.

**Steps:**
1. Loop `i` from 0 to N-1.
2. Loop `j` from 0 to N-1.
3. If `i != j` and `A[i] - A[j] == k`, return 1.
4. Return 0.

**Dry Run Trace (A = [1, 2, 3, 4, 5], k = 2):**

| i | j | A[i]-A[j] | Match? |
|---|---|-----------|--------|
| 0 | 0 | 0 | i==j, skip |
| ... | ... | ... | ... |
| 2 | 0 | 3-1=2 | Yes! Return 1 |

| Metric | Value  |
|--------|--------|
| Time   | O(N^2) |
| Space  | O(1)   |

**BUD Transition:** Use hashing for O(1) lookups instead of inner loop.

---

### Approach 2: Optimal -- HashSet Lookup
**Intuition:** For each element `x`, we need `x - k` or `x + k` to exist in the array (at a different index). Insert elements into a set as we go. Before inserting `x`, check if `x - k` or `x + k` is already in the set.

**Steps:**
1. Initialize empty set.
2. For each `x` in array:
   - If `(x - k)` in set OR `(x + k)` in set, return 1.
   - Add `x` to set.
3. Return 0.

Note: When `k = 0`, we need `x` itself in the set. Since we check before inserting, this correctly requires a previous occurrence (distinct index).

| Metric | Value |
|--------|-------|
| Time   | O(N)  |
| Space  | O(N)  |

---

### Approach 3: Best -- Two Pointers (Leveraging Sorted Property)
**Intuition:** Since the array is sorted, use two pointers `i` and `j` (both starting at 0, with `i >= j`). Compute `diff = A[i] - A[j]`. If `diff == k` and `i != j`, found. If `diff < k`, increase it by advancing `i`. If `diff > k` (or `i == j`), decrease it by advancing `j`. This uses no extra space.

**Steps:**
1. Initialize `i = 0, j = 0`.
2. While both in bounds:
   - `diff = A[i] - A[j]`.
   - If `diff == k` and `i != j`, return 1.
   - If `diff < k`, increment `i`.
   - Else, increment `j`. If `j == i`, increment `j` again.
3. Return 0.

**Dry Run Trace (A = [1, 3, 5], k = 4):**

| i | j | A[i] | A[j] | diff | Action |
|---|---|------|------|------|--------|
| 0 | 0 | 1 | 1 | 0 | diff < k, i++ |
| 1 | 0 | 3 | 1 | 2 | diff < k, i++ |
| 2 | 0 | 5 | 1 | 4 | diff == k, i!=j, return 1 |

| Metric | Value |
|--------|-------|
| Time   | O(N)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N)?** Each pointer moves forward at most N times. Total work is at most 2N = O(N). Two-pointer works because sorted order guarantees that moving `i` right increases the difference, and moving `j` right decreases it -- monotonic behavior enables linear scan.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting `i != j` check | When k=0, A[i]-A[i]=0 but same index | Always verify `i != j` |
| Only checking `A[i]-A[j]` not `A[j]-A[i]` | Missing pairs | For hash approach, check both `x-k` and `x+k` |
| Two pointers stuck when `i == j` | Infinite loop | Skip `j` past `i` when they collide |

### Edge Cases Checklist
- k=0 with duplicates --> return 1
- k=0 without duplicates --> return 0
- Single element array --> return 0
- All elements same, k=0 --> return 1
- k larger than max-min --> return 0

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Sorted array, find pair with difference k, distinct indices?"
2. **M**atch: "Sorted + pair search -> two pointers for O(1) space."
3. **P**lan: "Two pointers, adjust based on diff vs k."
4. **I**mplement: Clean two-pointer code.
5. **R**eview: Dry run, especially k=0 case.
6. **E**valuate: "O(N) time, O(1) space. Optimal for sorted input."

### Follow-Up Questions
- "What if the array is unsorted?" --> Use HashSet approach, O(N) time O(N) space.
- "What if we need to find all such pairs?" --> Same two-pointer, collect results instead of early return.
- "What about three numbers with given sum?" --> Extend to 3Sum pattern.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Two Sum, binary search basics |
| **Same pattern** | Two Sum (sorted), pair with given sum |
| **Harder variant** | 3Sum, 4Sum, count pairs with difference k |
| **Unlocks** | Two-pointer mastery on sorted arrays |
