# Single Number

> **Batch 4 of 12** | **Topic:** Arrays / Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a **non-empty** array of integers `nums`, every element appears **exactly twice** except for one element which appears **exactly once**. Find that single element.

You must implement a solution with **O(n) time** and **O(1) extra space**.

**LeetCode #136**

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[2, 2, 1]` | `1` | 2 appears twice, 1 appears once |
| `[4, 1, 2, 1, 2]` | `4` | 1 and 2 appear twice, 4 appears once |
| `[1]` | `1` | Only one element |
| `[-1, -1, -2]` | `-2` | Works with negative numbers too |

### Constraints
- 1 <= nums.length <= 3 * 10^4
- -3 * 10^4 <= nums[i] <= 3 * 10^4
- Each element appears exactly twice except one

### Real-Life Analogy
Imagine you are at a **sock-matching station** after doing laundry. Every sock has a pair except one lonely sock. Instead of sorting all socks (expensive), you use a magical XOR bin: toss each sock in. When two matching socks are in the bin, they annihilate each other (XOR cancellation). At the end, the only sock remaining is the unmatched one.

### Key Observations
1. Sorting would let you find the odd-one-out by scanning adjacent pairs, but that is O(n log n).
2. A hash map/set can count occurrences in O(n) time but uses O(n) space.
3. **Aha moment:** XOR has three magical properties: `a ^ a = 0` (self-cancellation), `a ^ 0 = a` (identity), and XOR is commutative + associative (order does not matter). XOR-ing all elements cancels every pair, leaving only the single number. This is O(n) time, O(1) space -- the holy grail.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why XOR over Hash Map?
A hash map uses O(n) space. The problem explicitly asks for O(1) space. XOR's self-cancellation property is tailor-made for this: every duplicate pair contributes 0, and the lone element survives.

### Pattern Recognition
**Classification cue:** "Every element appears twice except one" --> XOR all elements. This is the canonical XOR cancellation problem. It generalizes to: "every element appears k times except one" (requires a more advanced bit trick).

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashMap Counting
**Intuition:** Count occurrences of each element using a hash map. Return the element with count 1.

**Steps:**
1. Build a frequency map: for each element, increment its count.
2. Iterate the map to find the element with count == 1.

**Dry Run (nums = [4, 1, 2, 1, 2]):**
- Map: {4: 1, 1: 2, 2: 2}
- Element with count 1: 4

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) for the hash map |

**BUD Transition:** The space is unnecessary. We need a way to "cancel" duplicates without storing them.

---

### Approach 2: Optimal -- Sorting + Adjacent Scan
**Intuition:** Sort the array. Duplicates become adjacent. Scan in pairs of 2. If a pair does not match, the first element of the pair is the single number.

**Steps:**
1. Sort the array.
2. Check `nums[i] == nums[i+1]` for i = 0, 2, 4, ...
3. If they differ, return `nums[i]`. If no mismatch found, the last element is the answer.

**Dry Run (nums = [4, 1, 2, 1, 2]):**
- Sorted: [1, 1, 2, 2, 4]
- i=0: nums[0]==nums[1]? 1==1 yes.
- i=2: nums[2]==nums[3]? 2==2 yes.
- i=4: last element --> return 4.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(1) if in-place sort |

**BUD Transition:** Sorting is O(n log n). XOR gives us O(n) with O(1) space.

---

### Approach 3: Best -- XOR Cancellation
**Intuition:** XOR all elements together. Since `a ^ a = 0` and `a ^ 0 = a`, every pair cancels out, leaving only the single number.

**Steps:**
1. Initialize `result = 0`.
2. For each element: `result ^= nums[i]`.
3. Return `result`.

**Dry Run (nums = [4, 1, 2, 1, 2]):**

| Step | Element | result (binary) | result (decimal) |
|------|---------|-----------------|-----------------|
| Init | -       | 000             | 0 |
| 1    | 4       | 100             | 4 |
| 2    | 1       | 101             | 5 |
| 3    | 2       | 111             | 7 |
| 4    | 1       | 110             | 6 |
| 5    | 2       | 100             | 4 |

Result: 4

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is optimal?** The single element could be at any position. We must inspect every element at least once. O(n) is the lower bound. XOR achieves this in a single pass with just one variable.

**Why O(1) space?** XOR accumulates information from all elements into a single integer. No auxiliary storage needed.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Initializing result to nums[0] | Then you must loop from index 1 | Initialize to 0 and loop from index 0 (cleaner) |
| Thinking XOR only works for positive | XOR is a bitwise operation on the binary representation | Works for negatives too (two's complement) |
| Applying XOR when elements appear more than twice | XOR cancellation only works for pairs | For triples, use bit counting approach |

### Edge Cases Checklist
- Single element: `[42]` --> 42
- Negative numbers: `[-1, -1, -2]` --> -2
- Zero present: `[0, 1, 0]` --> 1
- Large array: 30000 elements, O(n) handles it easily

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Every element appears twice except one. Find the unique one. O(n) time, O(1) space required."
2. **M**atch: "XOR cancellation: a^a = 0, a^0 = a."
3. **P**lan: "XOR all elements. Pairs cancel, single number remains."
4. **I**mplement: Three lines of code.
5. **R**eview: Trace through [4, 1, 2, 1, 2] step by step.
6. **E**valuate: "O(n) time, O(1) space. Meets the constraint perfectly."

### Follow-Up Questions
- "What if every element appears three times except one?" --> Sum the bits at each position modulo 3 (LeetCode #137).
- "What if two elements appear once (rest appear twice)?" --> XOR all to get `a^b`. Use a differing bit to split into two groups. XOR each group separately (LeetCode #260).
- "Can you do this without bitwise operations?" --> Sorting O(n log n), or math: `2 * sum(set) - sum(array)` gives O(n) time, O(n) space.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic XOR properties, array traversal |
| **Same pattern** | P010 Find Missing Number (XOR cancellation) |
| **Harder variant** | Single Number II (thrice), Single Number III (two singles) |
| **Unlocks** | Bit manipulation mindset, XOR tricks for interview problems |
