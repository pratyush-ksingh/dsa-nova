# Prime Sum (Goldbach's Conjecture)

> **Step 01 | 1.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
Given an **even** integer **n >= 4**, find any two prime numbers **p** and **q** such that **p + q = n**. Return them as a pair. Goldbach's conjecture (unproven for all n, but verified for all even numbers up to 4 × 10^18) guarantees a solution always exists.

**Example:**
```
Input:  n = 10
Output: [3, 7]   (because 3 + 7 = 10, both prime)

Input:  n = 28
Output: [5, 23]  (because 5 + 23 = 28, both prime)
```

| Input | Output  | Explanation                      |
|-------|---------|----------------------------------|
| 4     | [2, 2]  | 2 + 2 = 4, smallest even primes  |
| 10    | [3, 7]  | 3 + 7 = 10                       |
| 28    | [5, 23] | 5 + 23 = 28                      |
| 100   | [3, 97] | 3 + 97 = 100                     |

### Real-Life Analogy
Think of **currency decomposition**: you have a bill worth n cents (even) and you want to express it as exactly two "prime denomination" coins. The Sieve approach pre-computes which denominations are prime, then picks the right pair instantly.

### Key Observations
1. n is guaranteed even, so we only need to scan p in [2, n-2].
2. Since p + q = n, checking `is_prime(p)` and `is_prime(n-p)` is sufficient.
3. The Sieve of Eratosthenes precomputes primality for all numbers up to n in O(n log log n).
4. **Aha moment:** Once the sieve is built, finding the pair is just a linear scan -- total time dominated by sieve construction.

### Constraints
- 4 <= n <= 10^7 (typical for sieve-based solutions)
- n is always even

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why a Boolean Array (Sieve)?
Trial division is O(sqrt(n)) per number, and we may need to check up to n/2 numbers, giving O(n*sqrt(n)) overall. The Sieve of Eratosthenes computes primality for all numbers 0..n at once in O(n log log n) -- dramatically faster for large n.

### Pattern Recognition
**Classification cue:** "Find primes up to N, then use them" --> Sieve of Eratosthenes. "Check one number's primality" --> trial division.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Trial Division per Pair
**Intuition:** Iterate p from 2 to n-2. For each p, compute q = n-p, and check if both are prime using trial division (test divisibility by all integers from 2 to sqrt(p)). Return first valid pair.

**Steps:**
1. For `p` in range(2, n-1):
2. Compute `q = n - p`.
3. Check `is_prime(p)` and `is_prime(q)` using trial division.
4. If both prime, return `[p, q]`.

**Dry Run (n=10):**
- p=2: is_prime(2)=T, is_prime(8)=F. Skip.
- p=3: is_prime(3)=T, is_prime(7)=T. **Return [3, 7].**

| Metric | Value           |
|--------|-----------------|
| Time   | O(n * sqrt(n))  |
| Space  | O(1)            |

**BUD Transition:** Sieve precomputes all primality in one pass.

---

### Approach 2: Optimal -- Sieve + Linear Scan
**Intuition:** Build a boolean sieve `is_prime[0..n]` marking each index as prime or not. Then scan p from 2 upward; when `is_prime[p] and is_prime[n-p]`, return the pair.

**Steps:**
1. Build `sieve[0..n]`: mark 0 and 1 false, then for each prime p, mark all multiples p^2, p^2+p, ... as false.
2. Scan `p` from 2 to n-2; return `[p, n-p]` on first hit.

**Sieve Construction Trace (n=10, showing key steps):**
- Start: all True from 2..10.
- p=2: mark 4,6,8,10 False.
- p=3: mark 9 False.
- p>=4: 4 is composite, skip.
- Final primes: {2, 3, 5, 7}.

Scan: p=2 -> is_prime[2]=T, is_prime[8]=F. p=3 -> is_prime[3]=T, is_prime[7]=T. Return [3,7].

| Metric | Value              |
|--------|--------------------|
| Time   | O(n log log n)     |
| Space  | O(n)               |

---

### Approach 3: Best -- Sieve + Half-Range Scan
**Intuition:** Since (p, q) and (q, p) are the same pair, only scan p in [2, n//2]. Identical algorithm to Approach 2, just scans half as many candidates. Same Big-O but constant factor improvement.

**Steps:**
1. Build sieve same as Approach 2.
2. Scan `p` from 2 to `n//2` (inclusive): return first `[p, n-p]` where both are prime.

| Metric | Value              |
|--------|--------------------|
| Time   | O(n log log n)     |
| Space  | O(n)               |

---

## 4. COMPLEXITY INTUITIVELY

**Sieve time O(n log log n):** The inner loop for prime p marks approximately n/p composites. Summing over all primes up to n: sum(n/p for primes p <= n) ≈ n * log(log(n)) by Mertens' theorem. This grows extremely slowly -- for n=10^6, log(log(10^6)) ≈ 2.9.

**Brute vs Sieve:** For n=10^6, brute does ~500,000 * 1000 = 5 * 10^8 operations. Sieve does ~3 * 10^6. The difference is enormous.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                              | Why it happens                     | Fix                                          |
|-------------------------------------|------------------------------------|----------------------------------------------|
| Sieve array size n instead of n+1   | Off-by-one                        | `is_prime = [True] * (n + 1)`                |
| Starting sieve outer loop at 1      | 1 is not prime                    | Start marking composites from p=2            |
| Not initialising sieve[0]=sieve[1]=False | Default True catches 0 and 1 | Explicitly set False after initialisation    |
| Inner sieve loop starts at 2*p      | Correct but slower than p*p       | Safe but start at `p*p` to skip known composites |

### Edge Cases Checklist
- n=4: [2, 2] -- only pair; both equal
- n=6: [3, 3] -- both equal
- Very large even n: sieve handles up to 10^7 comfortably

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Find two primes summing to n. n is even and >= 4."
2. **M**atch: "Need primality for many numbers up to n --> Sieve of Eratosthenes."
3. **P**lan: "Build sieve O(n log log n), linear scan for first valid pair."
4. **I**mplement: Sieve + scan.
5. **R**eview: Trace n=10, verify [3,7].
6. **E**valuate: "O(n log log n) time, O(n) space. Brute is O(n*sqrt(n))."

### Follow-Up Questions
- "What if n is odd?" --> Odd Goldbach: every odd number > 5 is sum of 3 primes (Goldbach's weak conjecture, proven).
- "Return all valid pairs?" --> Collect all (p, n-p) with p <= n/2 and both prime.
- "What's the time complexity of building the sieve?" --> O(n log log n). Explain Mertens' theorem if pressed.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                            |
|-------------------|--------------------------------------------------------------------|
| **Prerequisite**  | Basic prime check; understanding of trial division                |
| **Same pattern**  | Count primes (LeetCode 204) -- also uses Sieve                    |
| **Harder variant**| Goldbach's partitions (count all prime pairs); prime gaps         |
| **Unlocks**       | Any problem requiring fast bulk primality: prime factorisation, segmented sieve |
