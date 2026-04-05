# Count Digits

> **Batch 1 of 12** | **Topic:** Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **N**, count the number of digits in N. For negative numbers, count the digits of the absolute value (ignore the sign).

**Example:**
```
Input: N = 12345
Output: 5

Input: N = 0
Output: 1
```

| Input   | Output | Explanation                          |
|---------|--------|--------------------------------------|
| 12345   | 5      | Digits: 1, 2, 3, 4, 5               |
| 0       | 1      | Zero has 1 digit                     |
| -987    | 3      | Ignore sign, digits: 9, 8, 7        |
| 1000000 | 7      | Leading 1 + six zeros                |

### Real-Life Analogy
Think of a **digital odometer** in a car. Each slot holds one digit. If the odometer reads 12345, it uses 5 slots. To figure out how many slots you need, you can either count them one by one (divide by 10 repeatedly until you hit 0), or just look at the magnitude -- a number in the ten-thousands needs 5 slots. The logarithm gives you that magnitude directly.

### Key Observations
1. Dividing a number by 10 removes its last digit. Repeating until 0 gives the count.
2. The number of digits in N is `floor(log10(|N|)) + 1` (for N != 0).
3. **Aha moment:** Converting to a string and taking its length is O(d) where d is the digit count -- same as the loop approach. But the log10 formula is O(1) mathematically, though floating-point precision can be tricky for very large numbers.

### Constraints
- -10^9 <= N <= 10^9 (fits in 32-bit int)

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Math?
No data structure needed. The problem is purely arithmetic -- either iteratively peel digits or compute the answer via logarithm.

### Pattern Recognition
**Classification cue:** "Extract/count digits of a number" --> use the `n = n / 10` loop pattern. This is the foundational digit-manipulation technique used in reverse number, palindrome check, Armstrong number, etc.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Division
**Intuition:** Keep dividing by 10 and incrementing a counter until the number becomes 0. Each division strips one digit.

**Steps:**
1. Handle special case: if N == 0, return 1.
2. Take absolute value of N.
3. Initialize `count = 0`.
4. While N > 0: increment count, set N = N / 10.
5. Return count.

**Dry Run Trace (N = 4729):**

| Step | N    | N / 10 | count |
|------|------|--------|-------|
| 1    | 4729 | 472    | 1     |
| 2    | 472  | 47     | 2     |
| 3    | 47   | 4      | 3     |
| 4    | 4    | 0      | 4     |

Result: 4 digits.

| Metric | Value           |
|--------|-----------------|
| Time   | O(log10(N)) = O(d) where d = number of digits |
| Space  | O(1)            |

**BUD Transition:** The loop runs d times. Can we get the answer without looping? Yes -- use logarithm.

---

### Approach 2: Optimal -- Logarithm Formula
**Intuition:** The number of digits in a positive integer N is `floor(log10(N)) + 1`. For example, log10(999) = 2.999..., floor = 2, +1 = 3 digits. log10(1000) = 3.0, floor = 3, +1 = 4 digits.

**Steps:**
1. Handle special case: if N == 0, return 1.
2. Return `floor(log10(|N|)) + 1`.

**Dry Run Trace (N = 4729):**

| Expression      | Value   |
|----------------|---------|
| log10(4729)    | 3.6747  |
| floor(3.6747)  | 3       |
| 3 + 1          | 4       |

Result: 4 digits.

| Metric | Value          |
|--------|----------------|
| Time   | O(1)           |
| Space  | O(1)           |

**Caveat:** Floating-point precision issues near powers of 10 (e.g., log10(1000) might evaluate to 2.9999... in some languages). Add a small epsilon or validate with the division approach for edge cases.

---

### Approach 3: Best -- String Conversion
**Intuition:** Convert the number to a string and return the length. Simple, readable, and commonly accepted in interviews as a valid one-liner (though interviewers may then ask for the math approach).

**Steps:**
1. Convert `|N|` to string.
2. Return its length.

| Metric | Value          |
|--------|----------------|
| Time   | O(d)           |
| Space  | O(d) for the string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(d) or O(1)?** A number N has d = floor(log10(N)) + 1 digits. The division loop visits each digit exactly once: O(d). The logarithm approach computes the result with a single math operation: O(1). Since d is at most 10 for a 32-bit integer, all approaches are effectively constant time in practice.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Returning 0 for N=0 | Loop condition `N > 0` is never true when N=0 | Handle N=0 as special case returning 1 |
| Forgetting negative numbers | -123 / 10 behaves differently | Take absolute value first |
| log10(0) is undefined | log10(0) = -infinity | Handle N=0 before calling log |
| Float precision with log10 | log10(10^k) might be 2.999... | Use `(int)(Math.log10(n)) + 1` carefully, or prefer integer approach |

### Edge Cases Checklist
- N = 0 --> 1 digit
- N = -1 --> 1 digit
- N = Integer.MAX_VALUE (2147483647) --> 10 digits
- N = Integer.MIN_VALUE (-2147483648) --> 10 digits (careful with abs!)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Can N be negative? What about 0? Range of N?"
2. **M**atch: "Digit extraction pattern -- divide by 10 repeatedly."
3. **P**lan: "Loop: divide by 10, count iterations. Or use log10."
4. **I**mplement: Start with the loop (safer), mention log10 as optimization.
5. **R**eview: Trace with N=0 and N=4729.
6. **E**valuate: "O(d) time with loop, O(1) with log. Both O(1) space."

### Follow-Up Questions
- "What about counting digits that divide N evenly?" --> Different problem; iterate digits, check N % digit == 0.
- "Handle very large numbers (beyond int range)?" --> Use long/BigInteger, or string approach.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Integer division and modulo basics |
| **Same pattern** | Reverse a Number, Palindrome Number (all use n/10 loop) |
| **Harder variant** | Count digits that evenly divide N |
| **Unlocks** | All digit-manipulation problems |
