# Check Palindrome

> **Batch 2 of 12** | **Topic:** Basic Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer `x`, return `true` if `x` is a **palindrome** -- i.e., it reads the same forward and backward. Negative numbers are not palindromes. *(LeetCode #9)*

**Example:**
```
Input: x = 121
Output: true  (121 reversed is 121)

Input: x = -121
Output: false (reads as 121- backward)

Input: x = 10
Output: false (01 is not 10)
```

| Input | Output | Explanation |
|---|---|---|
| 121 | true | Reads same forward and backward |
| -121 | false | Negative sign makes it non-palindrome |
| 10 | false | Reversed is 01, which is 1, not 10 |
| 0 | true | Single digit is always a palindrome |
| 1234321 | true | Symmetric around the middle digit |

### Real-Life Analogy
Think of a car's odometer reading. If you photograph the digits and flip the photo horizontally, does it show the same number? 12321 does, but 12345 does not. To check without a photo, you can either write the number backward and compare, or just check the first digit against the last, then move inward -- like checking if a word is the same from both ends.

### Key Observations
1. Negative numbers are never palindromes (the minus sign has no counterpart).
2. We can reverse the entire number and compare, or reverse only half.
3. **Aha moment:** Reversing only the second half avoids potential integer overflow and is slightly more elegant. When the reversed half equals (or exceeds by one digit) the remaining half, we have a palindrome.

### Constraints
- -2^31 <= x <= 2^31 - 1
- Follow-up: Can you solve it without converting to a string?

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Math (Digit Extraction)?
The problem asks us not to convert to a string. We extract digits using `% 10` and `/ 10` -- the same modular arithmetic used in reverse-number problems. No data structure needed.

### Pattern Recognition
**Classification cue:** "Palindrome number" or "reverse a number" --> digit extraction with modulo. This is the reverse-number pattern applied with a comparison step. The half-reversal optimization is a common interview trick.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- String Conversion
**Intuition:** Convert the number to a string, then check if the string equals its reverse.

**Steps:**
1. If `x < 0`, return false.
2. Convert `x` to string `s`.
3. Compare `s` with `reverse(s)`.
4. Return true if they are equal.

**Dry Run Trace (x = 121):**

| Step | Action | Value |
|------|--------|-------|
| 1 | x >= 0? | Yes, continue |
| 2 | s = "121" | "121" |
| 3 | reverse(s) = "121" | "121" |
| 4 | "121" == "121"? | true |

| Metric | Value |
|--------|-------|
| Time | O(D) where D = number of digits |
| Space | O(D) for the string |

**BUD Transition:** This uses O(D) extra space for the string. Can we avoid the string entirely? Yes -- reverse the number using math. And we can be even smarter by reversing only half the digits.

---

### Approach 2: Optimal -- Full Number Reversal (Math)
**Intuition:** Reverse the entire number using modular arithmetic. Compare the reversed number with the original.

**Steps:**
1. If `x < 0`, return false.
2. Save `original = x`. Set `reversed = 0`.
3. While `x > 0`:
   - `reversed = reversed * 10 + x % 10`
   - `x = x / 10`
4. Return `reversed == original`.

**Dry Run Trace (x = 1221):**

| Iteration | x | x % 10 | reversed |
|-----------|---|--------|----------|
| Start | 1221 | -- | 0 |
| 1 | 122 | 1 | 1 |
| 2 | 12 | 2 | 12 |
| 3 | 1 | 2 | 122 |
| 4 | 0 | 1 | 1221 |

`reversed (1221) == original (1221)` --> true

| Metric | Value |
|--------|-------|
| Time | O(D) where D = number of digits |
| Space | O(1) |

---

### Approach 3: Best -- Half Reversal (Math)
**Intuition:** Reverse only the second half of the number. When the reversed half equals the remaining first half, it is a palindrome. This avoids potential overflow issues with full reversal.

**Steps:**
1. If `x < 0` or (`x % 10 == 0` and `x != 0`), return false. (Numbers ending in 0 cannot be palindromes unless x=0.)
2. Set `reversedHalf = 0`.
3. While `x > reversedHalf`:
   - `reversedHalf = reversedHalf * 10 + x % 10`
   - `x = x / 10`
4. Return `x == reversedHalf` (even digits) or `x == reversedHalf / 10` (odd digits -- middle digit is in reversedHalf).

**Dry Run Trace (x = 12321):**

| Iteration | x | reversedHalf | Condition x > rH |
|-----------|---|-------------|-------------------|
| Start | 12321 | 0 | 12321 > 0: yes |
| 1 | 1232 | 1 | 1232 > 1: yes |
| 2 | 123 | 12 | 123 > 12: yes |
| 3 | 12 | 123 | 12 > 123: no, stop |

`x (12) == reversedHalf / 10 (123/10 = 12)` --> true (odd length, discard middle digit)

| Metric | Value |
|--------|-------|
| Time | O(D/2) = O(D) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(D)?** We process each digit exactly once. A number with D digits requires D division/modulo operations. D = log10(x), so this is also O(log N).

**Why O(1) space for math approaches?** We only use a few integer variables (original, reversed, reversedHalf). No arrays or strings allocated.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Not handling negative numbers | -121 reversed is 121-, not a valid number | Return false immediately for x < 0 |
| Integer overflow on full reversal | Reversed number might exceed INT_MAX | Use long, or use half-reversal approach |
| Forgetting numbers ending in 0 | 10, 100, etc. cannot be palindromes (leading zeros) | Early return false when x % 10 == 0 and x != 0 |
| Off-by-one in half reversal | Odd-length numbers have a middle digit | Check both `x == rH` and `x == rH / 10` |

### Edge Cases Checklist
- x = 0 --> true (single digit)
- x < 0 --> false
- x = single positive digit (1-9) --> true
- x ends in 0 (10, 100, 1000) --> false
- x is a large palindrome near INT_MAX --> half-reversal is safest

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Can x be negative? Should I avoid string conversion?"
2. **M**atch: "Palindrome check --> reverse and compare, or two-pointer on digits."
3. **P**lan: "Reverse half the digits using modulo. Compare halves."
4. **I**mplement: Handle edge cases first (negative, trailing zero), then half-reversal loop.
5. **R**eview: Trace 12321 and 1221 (odd and even length).
6. **E**valuate: "O(log N) time, O(1) space. Overflow-safe."

### Follow-Up Questions
- "What about string palindromes with spaces and punctuation?" --> Two-pointer approach, skip non-alphanumeric characters (LeetCode #125).
- "What about palindrome linked lists?" --> Reverse the second half in-place and compare (LeetCode #234).
- "Find the nearest palindrome to N?" --> Much harder, involves constructing candidates (LeetCode #564).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Reverse a Number (digit extraction) |
| **Same pattern** | Reverse Integer (LeetCode #7), digit manipulation |
| **Harder variant** | Valid Palindrome II (LeetCode #680), Palindrome Linked List |
| **Unlocks** | Palindrome string problems, palindrome partitioning |
