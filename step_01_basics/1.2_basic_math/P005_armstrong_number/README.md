# Armstrong Number

> **Batch 3 of 12** | **Topic:** Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, determine whether it is an **Armstrong number** (also called a Narcissistic number). A number is Armstrong if the sum of each digit raised to the power of the total number of digits equals the number itself.

Formally: if N has `d` digits, then N is Armstrong when `d1^d + d2^d + ... + dk^d == N`.

**Example:**
```
Input: N = 153
Output: true
Explanation: 153 has 3 digits. 1^3 + 5^3 + 3^3 = 1 + 125 + 27 = 153.

Input: N = 9474
Output: true
Explanation: 9474 has 4 digits. 9^4 + 4^4 + 7^4 + 4^4 = 6561 + 256 + 2401 + 256 = 9474.

Input: N = 123
Output: false
Explanation: 1^3 + 2^3 + 3^3 = 1 + 8 + 27 = 36 != 123.
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 153   | true   | 1^3 + 5^3 + 3^3 = 153 |
| 9474  | true   | 9^4 + 4^4 + 7^4 + 4^4 = 9474 |
| 123   | false  | 1^3 + 2^3 + 3^3 = 36 |
| 0     | true   | 0^1 = 0 |
| 1     | true   | 1^1 = 1 |
| 10    | false  | 1^2 + 0^2 = 1 != 10 |

### Real-Life Analogy
Think of a **self-validating ID number**. Some systems embed a checksum inside the number itself -- the number "checks itself" by computing a formula on its own digits and confirming the result matches. An Armstrong number is the purest form of this: the number IS its own checksum. It is a mathematical narcissist -- it equals a function of its own parts.

### Key Observations
1. You need the digit count `d` before you can compute powers. This requires either a counting pass, string conversion, or log10.
2. All single-digit numbers (0-9) are trivially Armstrong: `x^1 = x`.
3. **Aha moment:** The exponent is NOT always 3. A common mistake is hardcoding the cube. The exponent is the total number of digits in N. For 3-digit numbers it happens to be 3, but 9474 uses exponent 4.

### Constraints
- 0 <= N <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Math?
The problem requires digit extraction (mod 10 / divide 10 loop) and exponentiation. No special data structure is needed.

### Pattern Recognition
**Classification cue:** "Check a property that depends on individual digits" --> digit extraction loop (`n % 10`, `n / 10`). Same family as Count Digits, Reverse Number, Palindrome Number.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Two-Pass with Division
**Intuition:** First pass: count digits by dividing by 10 until 0. Second pass: extract each digit, raise to power d, and accumulate. Compare sum to original.

**Steps:**
1. Save original value. Count digits `d` by looping `n / 10` until 0.
2. Reset to original. Extract each digit via `n % 10`, compute `digit^d`, add to sum, then `n = n / 10`.
3. Return `sum == original`.

**Dry Run Trace (N = 153):**

| Step | Action | Digit | digit^3 | Running Sum | Remaining |
|------|--------|-------|---------|-------------|-----------|
| Count | d=3 | - | - | - | - |
| 1 | extract | 3 | 27 | 27 | 15 |
| 2 | extract | 5 | 125 | 152 | 1 |
| 3 | extract | 1 | 1 | 153 | 0 |

153 == 153 --> true.

| Metric | Value |
|--------|-------|
| Time   | O(d) where d = number of digits |
| Space  | O(1) |

**BUD Transition:** The two-pass approach counts digits then extracts. We can compute d upfront with log10 or string length to simplify.

---

### Approach 2: Optimal -- Single Pass with Pre-computed Digit Count
**Intuition:** Compute `d = floor(log10(N)) + 1` (or string length) in O(1), then do a single extraction pass. This avoids the explicit counting loop.

**Steps:**
1. Compute `d` using `String.valueOf(n).length()` or `(int)Math.log10(n) + 1`.
2. Extract digits and compute power sum in one loop.
3. Return `sum == original`.

| Metric | Value |
|--------|-------|
| Time   | O(d) |
| Space  | O(1) |

---

### Approach 3: Best -- String-Based Pythonic Approach
**Intuition:** Convert N to a string, get its length as d, iterate characters, convert each back to int, raise to d, and sum. Extremely readable one-liner in Python.

**Steps:**
1. `s = str(N)`, `d = len(s)`.
2. `total = sum(int(ch)**d for ch in s)`.
3. Return `total == N`.

| Metric | Value |
|--------|-------|
| Time   | O(d) |
| Space  | O(d) for the string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(d)?** You must examine every digit at least once to compute the power sum. A number N has `d = floor(log10(N)) + 1` digits, at most 10 for 32-bit integers. The power computation `digit^d` takes O(d) via repeated multiplication, but since d <= 10, this is constant in practice. All approaches are effectively O(1) for int-range inputs.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Hardcoding exponent as 3 | Only testing with 3-digit Armstrong numbers | Always compute d dynamically |
| Forgetting N=0 | Loop `n > 0` never enters when n=0 | Handle n=0 as special case or count digits via string |
| Integer overflow in power sum | 9^10 = 3,486,784,401 exceeds int range | Use `long` in Java for the running sum |
| Modifying N without saving original | Need original for final comparison | Store original before extraction loop |

### Edge Cases Checklist
- N = 0 --> true (0^1 = 0)
- N = 1 through 9 --> all true (single-digit)
- N = 10 --> false (1 + 0 = 1)
- N = 9474 --> true (4-digit Armstrong)
- N = 999999999 --> false

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is the exponent always 3, or the number of digits? Can N be 0?"
2. **M**atch: "Digit extraction pattern -- mod 10 / divide 10."
3. **P**lan: "Count digits first, then extract each and compute power sum."
4. **I**mplement: Use long to avoid overflow. Save original N.
5. **R**eview: Trace N=153 and N=10.
6. **E**valuate: "O(d) time, O(1) space. d is at most 10 for int range."

### Follow-Up Questions
- "Find all Armstrong numbers in range [1, N]?" --> Loop and check each, or precompute (only 88 base-10 Armstrong numbers exist).
- "What about Armstrong numbers in other bases?" --> Replace 10 with base b in digit extraction.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Count Digits (digit extraction pattern) |
| **Same pattern** | Palindrome Number, Sum of Digits (all use n%10 / n/10 loop) |
| **Harder variant** | Find all Armstrong numbers up to N |
| **Unlocks** | Number theory problems, self-referential number checks |
