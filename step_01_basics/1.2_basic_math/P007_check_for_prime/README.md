# Check for Prime

> **Batch 3 of 12** | **Topic:** Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, determine whether N is a **prime number**. A prime number is greater than 1 and has no divisors other than 1 and itself.

**Example:**
```
Input: N = 7
Output: true
Explanation: 7 is only divisible by 1 and 7.

Input: N = 12
Output: false
Explanation: 12 is divisible by 2, 3, 4, 6 (among others).

Input: N = 1
Output: false
Explanation: 1 is not prime by definition.
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 7     | true   | Divisors: 1, 7 only |
| 12    | false  | Divisible by 2, 3, 4, 6 |
| 1     | false  | Not prime by definition |
| 2     | true   | Smallest and only even prime |
| 0     | false  | Not prime |
| 97    | true   | No divisor from 2 to 9 (sqrt(97) ~ 9.8) |

### Real-Life Analogy
Think of prime numbers as **indivisible atoms**. Just as an atom cannot be split into smaller atoms (in classical chemistry), a prime cannot be expressed as a product of smaller positive integers. The number 12 is like a molecule -- it breaks down into 2 * 2 * 3 (its prime factors). The number 7 is an atom -- it cannot be broken down further. Testing primality is like asking: "Is this substance an element or a compound?"

### Key Observations
1. If N has any divisor between 2 and N-1, it is not prime. The brute force checks all of them.
2. If N has a divisor `d > sqrt(N)`, then `N/d < sqrt(N)` is also a divisor. So you only need to check up to sqrt(N).
3. **Aha moment:** The sqrt(N) trick is the same insight as in Print All Divisors -- divisors come in pairs, and one is always <= sqrt(N). For N = 10^9, this reduces checks from ~10^9 to ~31,623. Further optimization: after checking 2, skip all even numbers (check only odd candidates).

### Constraints
- 0 <= N <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Loop?
We just need to find if any divisor exists between 2 and sqrt(N). A single loop with early return suffices. No data structure needed.

### Pattern Recognition
**Classification cue:** "Is N prime?" --> check for divisors up to sqrt(N). This is the entry point to all number theory: Sieve of Eratosthenes, prime factorization, and cryptography all build on this foundation.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check 2 to N-1
**Intuition:** Try every number from 2 to N-1 as a potential divisor. If any divides N, it is not prime.

**Steps:**
1. If N <= 1, return false.
2. Loop `i` from 2 to N-1.
3. If `N % i == 0`, return false.
4. If loop completes, return true.

**Dry Run Trace (N = 7):**

| i | 7 % i | Divisor? |
|---|-------|----------|
| 2 | 1 | No |
| 3 | 1 | No |
| 4 | 3 | No |
| 5 | 2 | No |
| 6 | 1 | No |

No divisor found --> true (prime).

| Metric | Value |
|--------|-------|
| Time   | O(N) |
| Space  | O(1) |

**BUD Transition:** We check up to N-1, but divisors pair up. We only need to check up to sqrt(N).

---

### Approach 2: Optimal -- Check up to sqrt(N)
**Intuition:** If N has a factor `d` where 2 <= d <= sqrt(N), then N is composite. If no such factor exists, N is prime.

**Steps:**
1. If N <= 1, return false.
2. If N <= 3, return true.
3. Loop `i` from 2 to floor(sqrt(N)).
4. If `N % i == 0`, return false.
5. Return true.

**Dry Run Trace (N = 97, sqrt(97) ~ 9.8):**

| i | 97 % i | Divisor? |
|---|--------|----------|
| 2 | 1 | No |
| 3 | 1 | No |
| 4 | 1 | No |
| 5 | 2 | No |
| 6 | 1 | No |
| 7 | 6 | No |
| 8 | 1 | No |
| 9 | 7 | No |

No divisor found --> true. Only 8 checks instead of 95.

| Metric | Value |
|--------|-------|
| Time   | O(sqrt(N)) |
| Space  | O(1) |

---

### Approach 3: Best -- sqrt(N) with 2/3 Skip
**Intuition:** After handling 2 and 3 as special cases, every prime is of the form `6k +/- 1`. So we check divisibility by 2, then by 3, then iterate `i = 5, 7, 11, 13, 17, 19, ...` (i.e., `i` and `i+2`, incrementing by 6). This skips multiples of 2 and 3, checking only ~1/3 of candidates up to sqrt(N).

**Steps:**
1. If N <= 1, return false. If N <= 3, return true.
2. If N % 2 == 0 or N % 3 == 0, return false.
3. Loop `i` from 5 while `i * i <= N`, step by 6.
4. If `N % i == 0` or `N % (i + 2) == 0`, return false.
5. Return true.

| Metric | Value |
|--------|-------|
| Time   | O(sqrt(N)) with ~3x constant factor improvement |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(sqrt(N))?** If N is composite, it must have a factor <= sqrt(N). This is because if both factors were > sqrt(N), their product would exceed N. So checking up to sqrt(N) is sufficient. For N = 10^9, we check ~31,623 candidates (brute force checks ~10^9). The 6k+/-1 optimization further reduces the constant by 3x but does not change the asymptotic class.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Treating 1 as prime | 1 has exactly one divisor, not two | Handle N <= 1 as not prime |
| Treating 2 as not prime | Special-casing even numbers before checking 2 | Return true for N == 2 before even-number check |
| Using `i * i <= n` with overflow | For large N near INT_MAX | Use `i <= n / i` or cast to long |
| Loop starts at 1 instead of 2 | Every number is divisible by 1 | Start at 2 |

### Edge Cases Checklist
- N = 0 --> false
- N = 1 --> false
- N = 2 --> true (smallest prime, only even prime)
- N = 3 --> true
- N = 4 --> false (smallest composite)
- N = Integer.MAX_VALUE (2147483647) --> true (it is prime!)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Just check if N is prime? What range? Can N be 0 or 1?"
2. **M**atch: "Divisor checking -- factor pairs up to sqrt(N)."
3. **P**lan: "Start brute O(N), optimize to O(sqrt(N)), mention 6k+/-1."
4. **I**mplement: Handle edge cases (0, 1, 2, 3) first, then the loop.
5. **R**eview: Trace with N=7 and N=12.
6. **E**valuate: "O(sqrt(N)) time, O(1) space."

### Follow-Up Questions
- "Find all primes up to N?" --> Sieve of Eratosthenes, O(N log log N).
- "Factorize N into primes?" --> Trial division up to sqrt(N).
- "Is there a faster primality test?" --> Miller-Rabin (probabilistic, O(k * log^2 N)).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Print All Divisors (same sqrt pairing insight) |
| **Same pattern** | Any problem checking divisibility up to sqrt(N) |
| **Harder variant** | Prime Factorization, Count Primes (Sieve) |
| **Unlocks** | Sieve of Eratosthenes, GCD (Euclidean), cryptography basics |
