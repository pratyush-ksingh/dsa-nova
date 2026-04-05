# Print Prime Factors

> **Batch 1 of 12** | **Topic:** Math / Prime Factorization | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, find and print all its **prime factors** (the prime numbers that multiply together to give N). Each prime factor should appear as many times as it divides N.

**Example:**
```
Input: N = 60
Output: [2, 2, 3, 5]
Explanation: 60 = 2 * 2 * 3 * 5
```

| Input | Output            | Explanation                          |
|-------|-------------------|--------------------------------------|
| 60    | [2, 2, 3, 5]     | 60 = 2^2 * 3 * 5                    |
| 1     | []                | 1 has no prime factors               |
| 13    | [13]              | 13 is itself prime                   |
| 84    | [2, 2, 3, 7]     | 84 = 2^2 * 3 * 7                    |
| 100   | [2, 2, 5, 5]     | 100 = 2^2 * 5^2                     |

### Real-Life Analogy
Think of breaking down a LEGO structure into its smallest building blocks. A structure made of 60 pieces can be decomposed as: 2 groups of 2, then a group of 3, then a group of 5. You cannot break these groups down any further -- they are the "prime" building blocks. You start with the smallest block size (2) and keep splitting until nothing more can be divided.

### Key Observations
1. Start dividing N by the smallest prime (2). Keep dividing while it is divisible. Then try 3, then 5, etc.
2. You only need to check divisors up to sqrt(N). If N > 1 after the loop, the remaining N is itself a prime factor.
3. **Aha moment:** After removing all factors of 2, all remaining factors must be odd. So we can skip even numbers beyond 2, effectively halving our work. Furthermore, no factor greater than sqrt(N) can exist unless N itself is prime -- because if both factors were > sqrt(N), their product would exceed N.

### Constraints
- 1 <= N <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Trial Division?
The simplest and most intuitive factorization method. For numbers up to 10^9, trial division up to sqrt(N) is efficient enough (at most ~31623 iterations). No special data structure needed -- just a list to collect factors.

### Pattern Recognition
**Classification cue:** "Find prime factors" or "factorize" --> trial division. The sqrt(N) optimization is the standard technique. For very large numbers (cryptographic scale), more advanced algorithms (Pollard's rho, quadratic sieve) are needed, but trial division is the interview standard.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check Every Number up to N
**Intuition:** For each number `i` from 2 to N, while `i` divides N, it is a prime factor (add it, divide N by it). This works because by the time we reach `i`, all factors smaller than `i` have already been divided out, so `i` must be prime.

**Steps:**
1. Initialize empty list `factors`.
2. For `i` from 2 to N:
   a. While N % i == 0: add i to factors, set N = N / i.
3. Return factors.

**Why does this give only primes?** When we reach i=4, all factors of 2 have already been removed. So N is no longer divisible by 4. Same logic for all composites.

**Dry Run Trace (N = 60):**

| i | N before | N % i == 0? | Action          | factors        | N after |
|---|----------|-------------|-----------------|----------------|---------|
| 2 | 60       | Yes         | add 2, N=30    | [2]            | 30      |
| 2 | 30       | Yes         | add 2, N=15    | [2,2]          | 15      |
| 2 | 15       | No          | move on         | [2,2]          | 15      |
| 3 | 15       | Yes         | add 3, N=5     | [2,2,3]        | 5       |
| 3 | 5        | No          | move on         | [2,2,3]        | 5       |
| 4 | 5        | No          | move on         | [2,2,3]        | 5       |
| 5 | 5        | Yes         | add 5, N=1     | [2,2,3,5]      | 1       |

| Metric | Value          |
|--------|----------------|
| Time   | O(N) worst case (when N is prime) |
| Space  | O(log N) for the factors list |

**BUD Transition:** The bottleneck is iterating all the way to N. Since any factor must be <= sqrt(N) (except possibly N itself), we can stop at sqrt(N).

---

### Approach 2: Optimal -- Trial Division up to sqrt(N)
**Intuition:** Only check divisors from 2 to sqrt(N). After the loop, if N > 1, then N itself is a remaining prime factor.

**Steps:**
1. Initialize empty list `factors`.
2. For `i` from 2 while `i * i <= N`:
   a. While N % i == 0: add i to factors, set N = N / i.
3. If N > 1: add N to factors (it is a prime factor larger than sqrt(original N)).
4. Return factors.

**Dry Run Trace (N = 84):**

| i | i*i <= N? | N   | N%i==0? | Action       | factors     |
|---|-----------|-----|---------|--------------|-------------|
| 2 | 4<=84 Y   | 84  | Yes     | add 2, N=42 | [2]         |
| 2 | 4<=42 Y   | 42  | Yes     | add 2, N=21 | [2,2]       |
| 2 | 4<=21 Y   | 21  | No      | next i       | [2,2]       |
| 3 | 9<=21 Y   | 21  | Yes     | add 3, N=7  | [2,2,3]     |
| 3 | 9<=7 N    | 7   | --      | exit loop    | [2,2,3]     |
| -- | --       | 7>1 | --      | add 7        | [2,2,3,7]   |

| Metric | Value          |
|--------|----------------|
| Time   | O(sqrt(N))     |
| Space  | O(log N)       |

---

### Approach 3: Best -- Optimized Trial Division (Skip Evens)
**Intuition:** Handle factor 2 separately, then only check odd numbers from 3 to sqrt(N). This halves the number of iterations.

**Steps:**
1. While N % 2 == 0: add 2, divide N by 2.
2. For `i = 3` while `i * i <= N`, step by 2:
   a. While N % i == 0: add i, divide N by i.
3. If N > 1: add N.
4. Return factors.

**Dry Run Trace (N = 100):**

| Phase    | i | N   | Action       | factors     |
|----------|---|-----|--------------|-------------|
| Even     | 2 | 100 | add 2, N=50 | [2]         |
| Even     | 2 | 50  | add 2, N=25 | [2,2]       |
| Even     | 2 | 25  | 25%2!=0     | [2,2]       |
| Odd loop | 3 | 25  | 25%3!=0     | [2,2]       |
| Odd loop | 5 | 25  | add 5, N=5  | [2,2,5]     |
| Odd loop | 5 | 5   | add 5, N=1  | [2,2,5,5]   |
| Remainder| - | 1   | N not > 1   | [2,2,5,5]   |

| Metric | Value          |
|--------|----------------|
| Time   | O(sqrt(N))     |
| Space  | O(log N)       |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(sqrt(N))?** If N has a factor `f` where `f > sqrt(N)`, then `N/f < sqrt(N)`, so `N/f` would have been found first. After dividing out all factors up to sqrt(N), the remaining N is either 1 or a single prime. So we never need to check beyond sqrt(N).

**Why O(log N) space?** Each prime factor is at least 2. So the number of prime factors (with multiplicity) is at most log2(N). For N = 10^9, that is at most about 30 factors.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting the `N > 1` check after loop | Misses the largest prime factor | Always check if N > 1 after the sqrt loop |
| Using `i <= N` instead of `i*i <= N` | Makes the algorithm O(N) instead of O(sqrt(N)) | Use `i*i <= N` as the loop condition |
| Integer overflow in `i*i` | For large N, `i*i` can overflow int | Use `(long)i * i <= N` or compare `i <= N/i` |
| Not handling N=1 | 1 has no prime factors | Return empty list |

### Edge Cases Checklist
- N = 1 --> [] (no prime factors)
- N = 2 --> [2] (smallest prime)
- N = a large prime (e.g., 999999937) --> [999999937] -- the loop exits quickly, and the remainder is the prime itself
- N = power of 2 (e.g., 1024) --> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2] (ten 2s)
- N = product of two large primes --> the sqrt check finds one, the remainder is the other

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Find all prime factors with multiplicity? Return a list? N always positive?"
2. **M**atch: "Trial division pattern -- divide by increasing primes up to sqrt(N)."
3. **P**lan: "Check 2 separately, then odd numbers 3, 5, 7, ... up to sqrt(N). If N>1 at the end, it is a remaining prime factor."
4. **I**mplement: Write the optimized version directly. Clean code.
5. **R**eview: Trace with N=60 and N=13 (prime input).
6. **E**valuate: "O(sqrt(N)) time, O(log N) space. Efficient for N up to 10^18."

### Follow-Up Questions
- "Find only distinct prime factors?" --> Same approach, but add each prime once (check if it was already added, or add only the first time in the while loop).
- "Factorize many numbers efficiently?" --> Use Sieve of Eratosthenes to precompute smallest prime factor (SPF) for each number, then factorize in O(log N) per query.
- "How to check if N is prime?" --> If the only prime factor is N itself (factors list has length 1 and that factor equals original N).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Understanding of prime numbers, division |
| **Same pattern** | GCD (Euclidean algorithm), count divisors |
| **Harder variant** | Sieve of Eratosthenes, Smallest Prime Factor sieve |
| **Unlocks** | GCD/LCM using prime factorization, number theory problems |
