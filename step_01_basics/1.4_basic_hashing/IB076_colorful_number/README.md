# Colorful Number

> **Step 01 | 1.4** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
A number is called **colorful** if the product of every contiguous subsequence of its digits is unique. Given a non-negative integer `A`, determine if it is colorful. Return **1** if yes, **0** otherwise.

**Contiguous subsequences** of digits means all subarrays of the digit array. For example, for `3245`, the digit array is `[3, 2, 4, 5]`, and the contiguous subsequences are:
- Length 1: `3, 2, 4, 5`
- Length 2: `3*2=6, 2*4=8, 4*5=20`
- Length 3: `3*2*4=24, 2*4*5=40`
- Length 4: `3*2*4*5=120`

All products `{3, 2, 4, 5, 6, 8, 20, 24, 40, 120}` are distinct, so 3245 is colorful.

**Example:**
```
Input: A = 23
Output: 1
Explanation: Subsequences: 2, 3, 2*3=6. All distinct.

Input: A = 99
Output: 0
Explanation: Subsequences: 9, 9 -> duplicate product 9.
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 3245  | 1      | All 10 products are unique |
| 23    | 1      | Products: 2, 3, 6 -- all unique |
| 99    | 0      | Products: 9, 9 -- duplicate |
| 263   | 1      | Products: 2, 6, 3, 12, 18, 36 -- all unique |

### Real-Life Analogy
Think of a **paint palette**. Each tube of paint (digit) produces a color (product). When you mix consecutive tubes together (contiguous subsequences), each mixture should produce a unique color. If any two mixtures produce the same color, the palette is NOT colorful.

### Key Observations
1. For a number with D digits, there are D*(D+1)/2 contiguous subsequences.
2. Products can grow large (up to 9^D), but D is at most ~10 for typical inputs.
3. **Aha moment:** We need to check all O(D^2) subarray products for uniqueness -- a perfect use case for a HashSet.

### Constraints
- 0 <= A <= 2^31 - 1
- D (number of digits) <= 10

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Hashing?
We need to check if any product has been seen before. A HashSet gives O(1) average lookup and insertion, making duplicate detection efficient.

### Pattern Recognition
**Classification cue:** "Check uniqueness among computed values" --> HashSet / HashMap.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Nested Loops with List Scan
**Intuition:** Generate every contiguous subsequence, compute its product with a third loop, and store in a list. Before adding, scan the list to check for duplicates. This is the most straightforward but has an extra O(n) per duplicate check.

**Steps:**
1. Convert number to digit array.
2. For each start `i` and end `j`, compute product via inner loop.
3. Check if product is in the list (O(n) scan). If yes, return 0.
4. Otherwise, add to list.
5. If all products are unique, return 1.

**Dry Run Trace (A=23):**

| i | j | Subarray | Product | List So Far  | Duplicate? |
|---|---|----------|---------|-------------|-----------|
| 0 | 1 | [2]      | 2       | [2]         | No        |
| 0 | 2 | [2,3]    | 6       | [2, 6]      | No        |
| 1 | 2 | [3]      | 3       | [2, 6, 3]   | No        |

Result: 1 (colorful)

| Metric | Value   |
|--------|---------|
| Time   | O(D^3)  |
| Space  | O(D^2)  |

**BUD Transition:** Replace list with set for O(1) lookups; compute product incrementally to eliminate the third loop.

---

### Approach 2: Optimal -- HashSet + Running Product
**Intuition:** For each starting index `i`, maintain a running product as we extend `j`. Use a HashSet for O(1) duplicate detection. This drops time from O(D^3) to O(D^2).

**Steps:**
1. Convert number to digit array.
2. Initialize empty HashSet.
3. For each start `i`: reset `product = 1`.
4. For each `j` from `i` to end: `product *= digits[j]`.
5. If product is in set, return 0. Else add to set.
6. Return 1 if no duplicates found.

| Metric | Value  |
|--------|--------|
| Time   | O(D^2) |
| Space  | O(D^2) |

---

### Approach 3: Best -- Cleanest Set-Based Code
**Intuition:** Same algorithmic approach as Optimal, written as concisely as possible. In Java, leverage `Set.add()` returning `false` on duplicate. In Python, check membership before adding.

**Steps:**
1. Same as Optimal with minimal code.

| Metric | Value  |
|--------|--------|
| Time   | O(D^2) |
| Space  | O(D^2) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(D^2)?** There are D*(D+1)/2 contiguous subsequences. Each product computation is O(1) with running product. HashSet operations are O(1) average. Since D <= 10 for int inputs, this is effectively constant time in practice.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting digit 0 | 0 in any product makes it 0; multiple 0-containing subarrays collide | Handle naturally -- 0 products will be caught by set |
| Integer overflow | Large digit products | Use `long` in Java |
| Single digit numbers | Always colorful (only one subsequence) | Works naturally |

### Edge Cases Checklist
- A=0 --> digits=[0], only one product (0), return 1
- A=1 --> return 1
- A=11 --> products: 1, 1 -- duplicate, return 0
- A=99 --> products: 9, 9 -- duplicate, return 0

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "All contiguous subarray products of digits must be unique?"
2. **M**atch: "Uniqueness check -> HashSet."
3. **P**lan: "Two nested loops for all subarrays, running product, set for dedup."
4. **I**mplement: Clean code with set.add() trick.
5. **R**eview: Dry run with 3245.
6. **E**valuate: "O(D^2) time and space, D <= 10 so effectively constant."

### Follow-Up Questions
- "What if we want to count how many colorful numbers exist in a range?" --> Iterate and check each, or use digit DP.
- "Can we extend to non-contiguous subsequences?" --> Exponential blowup, different approach needed.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic hashing, nested loops |
| **Same pattern** | Subarray sum problems (running sum + set) |
| **Harder variant** | Count distinct subarray products |
| **Unlocks** | Product-based hashing, sliding window with products |
