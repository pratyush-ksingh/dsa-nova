# All Divisors of a Number

> **Batch 2 of 12** | **Topic:** Advanced Maths | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer N, print **all divisors** of N in sorted order. A divisor of N is any integer `d` such that `N % d == 0`.

**Example:**
```
Input: N = 36
Output: 1 2 3 4 6 9 12 18 36
```

| Input | Output | Explanation |
|---|---|---|
| 36 | 1 2 3 4 6 9 12 18 36 | All numbers that divide 36 evenly |
| 1 | 1 | Only divisor of 1 is itself |
| 12 | 1 2 3 4 6 12 | Six divisors |
| 7 | 1 7 | Prime number -- only 1 and itself |

### Real-Life Analogy
You have 36 identical tiles and want to know all the ways to arrange them into a perfect rectangle. A 4x9 arrangement works (4 divides 36), but a 5x? arrangement does not (5 does not divide 36). Each valid rectangle width is a divisor. Notice that divisors come in pairs (4 and 9), so you only need to check widths up to sqrt(36)=6 and get the paired height for free.

### Key Observations
1. Brute force checks every number from 1 to N, which is O(N).
2. Divisors come in **pairs**: if `d` divides N, then `N/d` also divides N. So we only need to check up to sqrt(N).
3. **Aha moment:** By iterating only up to sqrt(N) and collecting both `d` and `N/d`, we reduce from O(N) to O(sqrt(N)) -- a massive improvement for large N (e.g., 31,623 iterations instead of 1,000,000,000 for N=10^9).

### Constraints
- 1 <= N <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why sqrt(N) iteration?
Divisors pair up around the square root. For every divisor `d <= sqrt(N)`, there is a corresponding divisor `N/d >= sqrt(N)`. This means we can find all divisors by only checking the range [1, sqrt(N)].

### Pattern Recognition
**Classification cue:** "Find all divisors" or "factors of N" --> iterate up to sqrt(N) collecting pairs. This sqrt-decomposition idea also appears in prime checking, prime factorization, sieve optimizations, and number-theoretic problems.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check Every Number 1 to N
**Intuition:** Simply iterate from 1 to N and check if each number divides N evenly.

**Steps:**
1. For `d` from 1 to N:
   - If `N % d == 0`, add `d` to the result list.
2. Return the list (already sorted since we iterate in order).

**Dry Run Trace (N = 12):**

| d | N % d | Divisor? | Result so far |
|---|-------|----------|---------------|
| 1 | 0 | Yes | [1] |
| 2 | 0 | Yes | [1, 2] |
| 3 | 0 | Yes | [1, 2, 3] |
| 4 | 0 | Yes | [1, 2, 3, 4] |
| 5 | 2 | No | [1, 2, 3, 4] |
| 6 | 0 | Yes | [1, 2, 3, 4, 6] |
| 7-11 | != 0 | No | [1, 2, 3, 4, 6] |
| 12 | 0 | Yes | [1, 2, 3, 4, 6, 12] |

| Metric | Value |
|--------|-------|
| Time | O(N) |
| Space | O(1) extra (excluding output) |

**BUD Transition:** The **B**ottleneck is iterating all the way to N. Since divisors come in pairs (e.g., 3 and 4 for N=12), we only need to search up to sqrt(N) and derive the partner divisor mathematically.

---

### Approach 2: Optimal -- sqrt(N) Pair Collection
**Intuition:** For every divisor `d` in [1, sqrt(N)], its partner `N/d` is also a divisor. Collect both, then sort.

**Steps:**
1. Create an empty list `divisors`.
2. For `d` from 1 to sqrt(N):
   - If `N % d == 0`:
     - Add `d`.
     - If `d != N/d`, also add `N/d` (avoid duplicate when d = sqrt(N)).
3. Sort `divisors` and return.

**Dry Run Trace (N = 36, sqrt(36) = 6):**

| d | N % d | Divisor? | Add d | Add N/d | divisors |
|---|-------|----------|-------|---------|----------|
| 1 | 0 | Yes | 1 | 36 | [1, 36] |
| 2 | 0 | Yes | 2 | 18 | [1, 36, 2, 18] |
| 3 | 0 | Yes | 3 | 12 | [1, 36, 2, 18, 3, 12] |
| 4 | 0 | Yes | 4 | 9 | [1, 36, 2, 18, 3, 12, 4, 9] |
| 5 | 1 | No | -- | -- | [1, 36, 2, 18, 3, 12, 4, 9] |
| 6 | 0 | Yes | 6 | 6 (=d, skip) | [1, 36, 2, 18, 3, 12, 4, 9, 6] |

After sorting: [1, 2, 3, 4, 6, 9, 12, 18, 36]

| Metric | Value |
|--------|-------|
| Time | O(sqrt(N)) for finding + O(D log D) for sorting where D = number of divisors |
| Space | O(D) for storing divisors |

---

### Approach 3: Best -- sqrt(N) with Two-List Trick (No Sort Needed)
**Intuition:** Collect divisors <= sqrt(N) in ascending order into `small`, and divisors > sqrt(N) into `large` (in reverse). Then concatenate. The result is already sorted without a separate sort step.

**Steps:**
1. `small = []`, `large = []`.
2. For `d` from 1 to sqrt(N):
   - If `N % d == 0`:
     - Append `d` to `small`.
     - If `d != N/d`, append `N/d` to `large`.
3. Reverse `large`.
4. Result = `small + large`.

| Metric | Value |
|--------|-------|
| Time | O(sqrt(N)) |
| Space | O(D) for storing divisors |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N) for brute force?** We check every number from 1 to N. That is N modulo operations.

**Why O(sqrt(N)) for optimal?** Divisors pair up: if d divides N, so does N/d. The smaller of each pair is at most sqrt(N). So we only iterate up to sqrt(N) and get the larger partner for free. For N = 10^9, this means ~31,623 iterations instead of 1,000,000,000.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting d == N/d check (perfect square) | When N is a perfect square, sqrt(N) divides N and N/sqrt(N) = sqrt(N) -- adding it twice | Check `if d != N/d` before adding the partner |
| Using `d * d <= N` with integer overflow | For large N near INT_MAX, d*d can overflow | Use `long` for the multiplication or compare d <= sqrt(N) |
| Not sorting the output | Problem asks for sorted order, but pair collection is unordered | Sort after collection, or use the two-list trick |

### Edge Cases Checklist
- N = 1 --> output: [1]
- N is prime --> output: [1, N]
- N is a perfect square --> careful not to double-count sqrt(N)
- N is very large (10^9) --> brute force TLE, must use sqrt approach

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Sorted order? Include 1 and N itself? What is the range of N?"
2. **M**atch: "Divisor finding --> sqrt(N) pair decomposition."
3. **P**lan: "Loop d from 1 to sqrt(N), collect d and N/d, sort output."
4. **I**mplement: Simple loop with one condition for perfect squares.
5. **R**eview: Trace with N=36.
6. **E**valuate: "O(sqrt(N)) time, far better than O(N) for large inputs."

### Follow-Up Questions
- "How would you count the number of divisors without listing them?" --> Same sqrt loop, just count instead of collect.
- "How would you find the sum of all divisors?" --> Same pattern, sum instead of collect.
- "How would you find the prime factorization?" --> Divide N by each prime up to sqrt(N) repeatedly.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Modulo operation, loops |
| **Same pattern** | Check Prime (sqrt optimization), Prime Factorization |
| **Harder variant** | Count Divisors for Range of Numbers, Sieve of Divisors |
| **Unlocks** | GCD algorithms, Perfect Number check, Euler Totient |
