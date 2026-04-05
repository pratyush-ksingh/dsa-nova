# Power Function using Recursion

> **Batch 4 of 12** | **Topic:** Recursion (Strong Hold) | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
**(LeetCode #50 -- Pow(x, n))** Implement `pow(x, n)`, which calculates `x` raised to the power `n` (i.e., `x^n`). The exponent `n` can be negative, zero, or positive.

### Examples

| # | Input | Output | Explanation |
|---|-------|--------|-------------|
| 1 | x=2.0, n=10 | 1024.0 | 2^10 = 1024 |
| 2 | x=2.1, n=3 | 9.261 | 2.1^3 = 9.261 |
| 3 | x=2.0, n=-2 | 0.25 | 2^(-2) = 1/4 = 0.25 |
| 4 | x=1.0, n=2147483647 | 1.0 | 1 raised to anything is 1 |
| 5 | x=2.0, n=0 | 1.0 | Anything raised to 0 is 1 |
| 6 | x=2.0, n=-2147483648 | 0.0 | Very large negative exponent |

### Real-Life Analogy
Think of **compound interest**: your money doubles every year. After 10 years you have 2^10 = 1024x your original amount. But computing this by multiplying 2 ten times is slow. Instead, you realize: "2^10 = (2^5)^2 and 2^5 = 2 * (2^2)^2." This halving trick is exactly fast exponentiation.

### Three Key Observations
1. **Naive multiplication is O(n)** -- multiplying x by itself n times works but is too slow for n = 2 billion.
2. **Binary exponentiation halves the problem** -- x^n = (x^(n/2))^2 if n is even, x * (x^(n/2))^2 if n is odd. This gives O(log n).
3. **Negative exponents** -- x^(-n) = 1 / x^n. Handle by inverting x and negating n, but watch out for n = INT_MIN overflow.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Multiply x by itself n times | Variables |
| Optimal | Recursive binary exponentiation | Call stack |
| Best | Iterative binary exponentiation | Variables |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Linear Recursion

**Intuition:** The simplest recursive definition: x^n = x * x^(n-1), with base case x^0 = 1. Each call reduces n by 1.

**Steps:**
1. If n == 0, return 1.
2. If n < 0, return 1 / pow(x, -n).
3. Return x * pow(x, n - 1).

**Dry-Run Trace** (x=2, n=5):
```
pow(2,5) = 2 * pow(2,4)
         = 2 * 2 * pow(2,3)
         = 2 * 2 * 2 * pow(2,2)
         = 2 * 2 * 2 * 2 * pow(2,1)
         = 2 * 2 * 2 * 2 * 2 * pow(2,0)
         = 2 * 2 * 2 * 2 * 2 * 1 = 32
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) call stack |

---

### Approach 2: Optimal -- Recursive Binary Exponentiation

**Intuition:** Halve the exponent at each step. If n is even, x^n = (x^(n/2))^2. If n is odd, x^n = x * (x^(n/2))^2. This reduces n by half each call, giving O(log n) depth.

**Steps:**
1. Base case: n == 0 -> return 1.
2. If n < 0: return 1.0 / power(x, -n). Handle INT_MIN by converting to long.
3. Compute half = power(x, n / 2).
4. If n is even: return half * half.
5. If n is odd: return x * half * half.

**Dry-Run Trace** (x=2, n=10):
```
power(2,10): half = power(2,5), return half*half
  power(2,5): half = power(2,2), return 2 * half*half
    power(2,2): half = power(2,1), return half*half
      power(2,1): half = power(2,0), return 2 * half*half
        power(2,0): return 1
      return 2 * 1 * 1 = 2
    return 2 * 2 = 4
  return 2 * 4 * 4 = 32
return 32 * 32 = 1024
```

| Metric | Value |
|--------|-------|
| Time | O(log n) |
| Space | O(log n) call stack |

---

### Approach 3: Best -- Iterative Binary Exponentiation

**Intuition:** Convert the recursive approach to iterative using the binary representation of n. For each bit of n (from LSB to MSB), if the bit is set, multiply the result by the current power of x. Square x at each step.

**Steps:**
1. If n < 0: x = 1/x, n = -n (use long for n to handle INT_MIN).
2. Initialize result = 1.
3. While n > 0:
   - If n is odd (n & 1): result *= x.
   - x *= x (square x for next bit).
   - n >>= 1 (shift right).
4. Return result.

**Dry-Run Trace** (x=2, n=10, binary 1010):
```
n=10 (1010): even, x=4, n=5
n=5  (101):  odd,  result=4, x=16, n=2
n=2  (10):   even, x=256, n=1
n=1  (1):    odd,  result=4*256=1024, n=0
Return 1024
```

| Metric | Value |
|--------|-------|
| Time | O(log n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(log n):** Each step halves n. Starting from n, we need log2(n) halvings to reach 0. For n = 2 billion, that is only ~31 steps instead of 2 billion multiplications.

**Space trade-off:** The recursive approach uses O(log n) stack space. The iterative approach eliminates this, achieving O(1) space while maintaining the same time complexity.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| n = 0 | 1.0 | Any base to the 0th power is 1 |
| n = 1 | x | Should not recurse unnecessarily |
| x = 0, n > 0 | 0.0 | 0 raised to positive power |
| x = 0, n < 0 | Infinity | Division by zero, handle gracefully |
| n = -2147483648 | Handle carefully | -INT_MIN overflows int |
| x = 1 or x = -1 | Quick return possible | Avoid unnecessary computation |
| Very large n with x=1 | 1.0 | Must not TLE |

**Common Mistakes:**
- Not handling n = INT_MIN (-2147483648) -- negating it overflows a 32-bit int.
- Using int instead of long for the exponent when negating.
- Forgetting the odd case: computing half * half but missing the extra x multiplication.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** Tests understanding of divide-and-conquer, recursion-to-iteration conversion, and overflow handling.

**Follow-ups:**
- "Can you do it iteratively?" -> Binary exponentiation with bit manipulation.
- "What about modular exponentiation?" -> Same approach with `% mod` at each step, used in cryptography.
- "What if both x and n are integers and you need an integer result?" -> Same logic, but integer overflow concerns.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Count Good Numbers (LC #1922) | Uses modular fast exponentiation |
| Super Pow (LC #372) | Exponentiation with array exponent |
| Sqrt(x) (LC #69) | Inverse operation, also uses binary search |
| Recursive Multiply | Similar divide-and-conquer on multiplication |
