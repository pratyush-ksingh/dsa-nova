# Find Missing Number

> **Batch 4 of 12** | **Topic:** Arrays / Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array `nums` containing `n` distinct numbers taken from the range `[0, n]`, find the **one number** in the range that is missing from the array.

**LeetCode #268**

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[3, 0, 1]` | `2` | Range is [0,3], numbers present are 0,1,3. Missing: 2 |
| `[0, 1]` | `2` | Range is [0,2], missing: 2 |
| `[9,6,4,2,3,5,7,0,1]` | `8` | Range is [0,9], missing: 8 |
| `[0]` | `1` | Range is [0,1], missing: 1 |

### Constraints
- n == nums.length
- 1 <= n <= 10^4
- 0 <= nums[i] <= n
- All numbers are **unique**

### Real-Life Analogy
Imagine a teacher doing roll call for a class of N students numbered 0 through N. She calls each number and checks off who is present. At the end, exactly one student is absent. Instead of going through the entire checklist, she could use a clever shortcut: she knows the sum of all numbers 0 to N (the formula N*(N+1)/2). She adds up all the numbers she heard and subtracts from the expected sum. The difference is the missing student's number.

### Key Observations
1. The array has `n` elements from the range `[0, n]` -- that is `n+1` possible values but only `n` slots. Exactly one is missing.
2. **Sum formula:** Expected sum = `n*(n+1)/2`. Actual sum = sum of array. Missing = expected - actual. O(n) time, O(1) space.
3. **Aha moment (XOR):** XOR of a number with itself is 0. XOR all indices `[0..n]` with all array values. Pairs cancel, leaving only the missing number. This avoids potential integer overflow from the sum approach.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Math/XOR over Hash Set?
A hash set would work (insert all, then check which is missing) but uses O(n) space. The sum formula and XOR approaches both achieve O(1) space. XOR is preferred in interviews because it avoids overflow (in languages with fixed-size integers).

### Pattern Recognition
**Classification cue:** "Find the one missing/extra element in a known range" --> think XOR cancellation or sum formula. This is a classic "pairing" trick where every element has a partner except one.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashSet
**Intuition:** Put all numbers in a set. Then check which number from 0 to n is not in the set.

**Steps:**
1. Insert all array elements into a HashSet.
2. Loop `i` from 0 to n. The first `i` not in the set is the answer.

**Dry Run (nums = [3, 0, 1]):**
- Set = {3, 0, 1}, n = 3
- Check 0: in set. Check 1: in set. Check 2: NOT in set. Return 2.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for the set |

**BUD Transition:** We are using O(n) extra space just to detect one missing number. Can we avoid that?

---

### Approach 2: Optimal -- Sum Formula (Gauss's Trick)
**Intuition:** The sum of 0 + 1 + 2 + ... + n = n*(n+1)/2. Subtract the actual array sum. The difference is the missing number.

**Steps:**
1. Compute `expected = n * (n + 1) / 2`.
2. Compute `actual = sum(nums)`.
3. Return `expected - actual`.

**Dry Run (nums = [3, 0, 1]):**
- n = 3, expected = 3*4/2 = 6
- actual = 3 + 0 + 1 = 4
- missing = 6 - 4 = 2

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

**BUD Transition:** The sum approach can overflow for very large n in languages with fixed-size integers. XOR avoids this.

---

### Approach 3: Best -- XOR Cancellation
**Intuition:** XOR all numbers from 0 to n, and XOR all array elements. Since `a XOR a = 0` and `a XOR 0 = a`, all paired numbers cancel out, leaving only the missing number.

**Steps:**
1. Initialize `xor_result = 0`.
2. XOR with each index from 0 to n: `xor_result ^= i`.
3. XOR with each array element: `xor_result ^= nums[i]`.
4. Return `xor_result`.

**Dry Run (nums = [3, 0, 1]):**
- XOR indices: 0 ^ 1 ^ 2 ^ 3 = (0^1) ^ (2^3) = 1 ^ 1 = 0... let me trace step by step:
  - Start: 0
  - ^0 = 0, ^1 = 1, ^2 = 3, ^3 = 0 (these are 0..n)
  - ^3 = 3, ^0 = 3, ^1 = 2 (these are array elements)
- Result: 2

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal?** You must look at every element at least once -- the missing number could depend on any element being present or absent. O(n) is the lower bound. The sum and XOR approaches both achieve this with O(1) extra space.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Integer overflow in sum | n*(n+1)/2 can overflow int for large n | Use long in Java, or use XOR approach |
| Off-by-one in range | Forgetting the range is [0, n] not [1, n] | n = len(nums), range is 0 to n inclusive |
| Assuming sorted input | The array is not necessarily sorted | All approaches work regardless of order |

### Edge Cases Checklist
- Missing 0: `[1]` --> 0
- Missing n: `[0]` --> 1
- Single element: `[0]` --> 1 or `[1]` --> 0
- Large n with no overflow concern (XOR approach)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Array of n elements from [0, n], one missing. Find it. All unique."
2. **M**atch: "Sum formula or XOR cancellation pattern."
3. **P**lan: "I will use XOR: XOR all indices 0..n with all array values. Pairs cancel, missing number remains."
4. **I**mplement: Simple loop, very clean.
5. **R**eview: Trace through [3, 0, 1].
6. **E**valuate: "O(n) time, O(1) space. Overflow-safe."

### Follow-Up Questions
- "What if two numbers are missing?" --> XOR alone is not enough. Use sum + sum-of-squares to get two equations, or XOR to get a^b then split by differing bit.
- "What if the range is [1, n+1] instead of [0, n]?" --> Same approach, adjust the expected range.
- "Can you do it in O(n) time with sorting?" --> Cycle sort places each number at its index, then scan for mismatch. O(n) time, O(1) space, but modifies input.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic array traversal, XOR properties |
| **Same pattern** | P012 Single Number (XOR cancellation) |
| **Harder variant** | Find Two Missing Numbers, Find Duplicate Number |
| **Unlocks** | XOR tricks for interview problems, Gauss's sum formula applications |
