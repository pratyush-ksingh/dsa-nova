# Reverse a Number

> **Batch 1 of 12** | **Topic:** Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a signed 32-bit integer **x**, return **x** with its digits reversed. If reversing causes overflow beyond the signed 32-bit integer range [-2^31, 2^31 - 1], return 0. Negative numbers stay negative after reversal.

**LeetCode #7**

**Example:**
```
Input: x = 123
Output: 321

Input: x = -456
Output: -654

Input: x = 120
Output: 21
```

| Input        | Output       | Explanation                                  |
|-------------|-------------|----------------------------------------------|
| 123         | 321         | Straightforward reversal                     |
| -456        | -654        | Sign preserved, digits reversed              |
| 120         | 21          | Trailing zero in input becomes leading zero, dropped |
| 0           | 0           | Zero stays zero                              |
| 1534236469  | 0           | Reversed = 9646324351 > INT_MAX, return 0    |

### Real-Life Analogy
Imagine reading a phone number backwards. You start from the last digit, write it down, then the second-to-last, and so on. Mathematically, you "pop" the last digit of the original number (using % 10) and "push" it onto the reversed number (using * 10 + digit). It is like transferring items from one stack to another -- which reverses their order.

### Key Observations
1. `n % 10` gives the last digit; `n / 10` removes it.
2. Building the reversed number: `rev = rev * 10 + digit` "shifts" existing digits left and appends the new one.
3. **Aha moment:** The overflow check must happen BEFORE multiplying by 10, not after. If `rev > INT_MAX / 10` (or `rev < INT_MIN / 10`), then `rev * 10` will overflow. This pre-check is the critical insight for the LeetCode version.

### Constraints
- -2^31 <= x <= 2^31 - 1

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Math?
No data structure needed. We extract digits one by one from the right and build the result from the left. This is the classic "digit extraction + accumulation" pattern.

### Pattern Recognition
**Classification cue:** "Manipulate individual digits of a number" --> use the `n % 10` / `n / 10` loop. Same family as Count Digits, Palindrome Number, Armstrong Number.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- String Reversal
**Intuition:** Convert number to string, reverse the string (handling the sign separately), convert back to integer. Check for overflow.

**Steps:**
1. Record sign, work with absolute value as string.
2. Reverse the string.
3. Convert back to integer.
4. Apply sign. Check overflow bounds, return 0 if overflow.

| Metric | Value          |
|--------|----------------|
| Time   | O(d) where d = number of digits |
| Space  | O(d) for the string |

**BUD Transition:** We are allocating a string unnecessarily. Can we do it purely with math?

---

### Approach 2: Optimal -- Mathematical Digit-by-Digit
**Intuition:** Pop the last digit of x and push it to the back of rev. Loop until x is 0. Check for overflow before each push.

**Steps:**
1. Initialize `rev = 0`.
2. While x != 0:
   a. `digit = x % 10`
   b. `x = x / 10` (truncate toward zero)
   c. **Overflow check:** if `rev > INT_MAX/10` or `(rev == INT_MAX/10 and digit > 7)` --> return 0. Similarly for negative side.
   d. `rev = rev * 10 + digit`
3. Return rev.

**Dry Run Trace (x = -123):**

| Step | x    | digit | rev before | rev after | Overflow? |
|------|------|-------|------------|-----------|-----------|
| 1    | -123 | -3    | 0          | -3        | No        |
| 2    | -12  | -2    | -3         | -32       | No        |
| 3    | -1   | -1    | -32        | -321      | No        |
| 4    | 0    | --    | -321       | --        | Done      |

Result: -321.

**Dry Run Trace (x = 1534236469):**

| Step | x          | digit | rev before  | rev after   | Overflow?                           |
|------|------------|-------|-------------|-------------|-------------------------------------|
| ...  | ...        | ...   | ...         | ...         | ...                                 |
| 9    | 1          | 1     | 964632435   | 9646324351? | 964632435 > 214748364 (INT_MAX/10) --> YES, return 0 |

| Metric | Value          |
|--------|----------------|
| Time   | O(d)           |
| Space  | O(1)           |

---

### Approach 3: Best -- Same as Optimal
The mathematical approach IS the best approach. O(d) time and O(1) space is optimal since we must examine every digit at least once.

| Metric | Value          |
|--------|----------------|
| Time   | O(d)           |
| Space  | O(1)           |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(d)?** We must look at every digit to reverse them. A number with d digits requires d iterations of the pop-and-push loop. We cannot skip any digit. O(1) space because we only use a few integer variables.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Overflow after multiplication | Checking `rev * 10 + digit > INT_MAX` overflows during the check itself | Check BEFORE: `rev > INT_MAX / 10` |
| Forgetting negative overflow | INT_MIN has larger magnitude than INT_MAX (2^31 vs 2^31-1) | Check both positive and negative bounds |
| Wrong behavior with trailing zeros | x=120, reversed = "021" = 21 | Integer arithmetic naturally drops leading zeros |
| Using abs(INT_MIN) in Java | `Math.abs(Integer.MIN_VALUE)` overflows | Work with negative remainders directly, or use long |

### Edge Cases Checklist
- x = 0 --> 0
- x = -1 --> -1
- x = 120 --> 21 (trailing zeros)
- x = 1534236469 --> 0 (overflow)
- x = -2147483648 --> 0 (overflow when reversed)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "32-bit signed integer? Return 0 on overflow? Negative numbers?"
2. **M**atch: "Digit extraction using mod/div -- same pattern as palindrome check."
3. **P**lan: "Pop digits with %10, push onto rev with *10. Check overflow before pushing."
4. **I**mplement: Write the optimal approach directly. Mention string approach as alternative.
5. **R**eview: Trace with x=123 and x=1534236469 (overflow case).
6. **E**valuate: "O(d) time, O(1) space. The key subtlety is the pre-overflow check."

### Follow-Up Questions
- "How would you handle 64-bit integers?" --> Same approach, use the 64-bit max/min for overflow checks.
- "What if overflow should throw an exception?" --> Replace `return 0` with `throw`.
- "Can you check palindrome using this?" --> Reverse and compare to original.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Count Digits (digit extraction basics) |
| **Same pattern** | Palindrome Number (reverse half the digits) |
| **Harder variant** | String to Integer (atoi) -- similar overflow handling |
| **Unlocks** | Palindrome Number, digit-based problems |
