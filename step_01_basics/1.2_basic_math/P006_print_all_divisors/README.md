# Print All Divisors

> **Batch 3 of 12** | **Topic:** Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **N**, print all divisors of N in **sorted (ascending) order**. A divisor of N is any integer `d` such that `N % d == 0`.

**Example:**
```
Input: N = 36
Output: 1 2 3 4 6 9 12 18 36

Input: N = 7
Output: 1 7
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 36    | 1 2 3 4 6 9 12 18 36 | All numbers that divide 36 evenly |
| 7     | 1 7 | 7 is prime, only 1 and itself |
| 1     | 1 | Only divisor is 1 |
| 12    | 1 2 3 4 6 12 | Six divisors |

### Real-Life Analogy
Imagine you have **36 tiles** and want to arrange them into a perfect rectangle. The divisors of 36 tell you all possible row counts: 1 row of 36, 2 rows of 18, 3 rows of 12, 4 rows of 9, 6 rows of 6. Each divisor pairs with a complementary divisor (its partner) to form the rectangle. The sqrt optimization exploits exactly this pairing: once you find one side of the rectangle, you automatically know the other side.

### Key Observations
1. Every divisor `d` of N has a complementary partner `N/d` that is also a divisor. They come in pairs.
2. One of the pair is <= sqrt(N) and the other is >= sqrt(N). So we only need to check up to sqrt(N).
3. **Aha moment:** If `d` divides N, then `N/d` also divides N. By iterating only up to sqrt(N) and recording both `d` and `N/d`, we cut the work from O(N) to O(sqrt(N)). This is the foundational optimization for all divisor/factor problems.

### Constraints
- 1 <= N <= 10^9

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Simple Loop + List?
We iterate candidate divisors and collect results. A list is needed to store and sort divisors when using the sqrt optimization (since we find them out of order).

### Pattern Recognition
**Classification cue:** "Find all factors / divisors of a number" --> iterate 1..sqrt(N), check divisibility, collect both members of each pair. This sqrt boundary trick appears in prime checking, GCD computation, and sieve algorithms.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check Every Number 1 to N
**Intuition:** Loop from 1 to N. For each `i`, if `N % i == 0`, it is a divisor. Collect and print.

**Steps:**
1. Loop `i` from 1 to N.
2. If `N % i == 0`, add `i` to result.
3. Print all collected divisors.

**Dry Run Trace (N = 12):**

| i  | 12 % i | Divisor? | Collected |
|----|--------|----------|-----------|
| 1  | 0      | Yes      | [1] |
| 2  | 0      | Yes      | [1,2] |
| 3  | 0      | Yes      | [1,2,3] |
| 4  | 0      | Yes      | [1,2,3,4] |
| 5  | 2      | No       | [1,2,3,4] |
| 6  | 0      | Yes      | [1,2,3,4,6] |
| 7-11 | != 0 | No       | [1,2,3,4,6] |
| 12 | 0      | Yes      | [1,2,3,4,6,12] |

| Metric | Value |
|--------|-------|
| Time   | O(N) |
| Space  | O(d) where d = number of divisors |

**BUD Transition:** We iterate all the way to N, but divisors come in pairs. If `i` divides N, so does `N/i`. We only need to check up to sqrt(N).

---

### Approach 2: Optimal -- Iterate to sqrt(N)
**Intuition:** For each `i` from 1 to sqrt(N), if `N % i == 0`, both `i` and `N/i` are divisors. Collect both, then sort.

**Steps:**
1. Loop `i` from 1 to floor(sqrt(N)).
2. If `N % i == 0`, add `i`. If `i != N/i`, also add `N/i`.
3. Sort the collected divisors.
4. Print.

**Dry Run Trace (N = 36, sqrt(36) = 6):**

| i | 36 % i | Divisor? | Add i | Add 36/i | Collected |
|---|--------|----------|-------|----------|-----------|
| 1 | 0 | Yes | 1 | 36 | [1,36] |
| 2 | 0 | Yes | 2 | 18 | [1,36,2,18] |
| 3 | 0 | Yes | 3 | 12 | [1,36,2,18,3,12] |
| 4 | 0 | Yes | 4 | 9 | [1,36,2,18,3,12,4,9] |
| 5 | 1 | No | - | - | [1,36,2,18,3,12,4,9] |
| 6 | 0 | Yes | 6 | 6 (same, skip) | [1,36,2,18,3,12,4,9,6] |

After sort: [1, 2, 3, 4, 6, 9, 12, 18, 36].

| Metric | Value |
|--------|-------|
| Time   | O(sqrt(N) + d*log(d)) -- sqrt for iteration, sort for output |
| Space  | O(d) |

---

### Approach 3: Best -- sqrt(N) with Two-List Merge (No Sort)
**Intuition:** Instead of sorting at the end, maintain two lists: `low` for divisors <= sqrt(N), `high` for divisors > sqrt(N). Since we iterate i in order, `low` is already sorted ascending. Add the complementary divisors `N/i` to `high` in reverse order. Then concatenate `low + reversed(high)` for sorted output without a sort call.

**Steps:**
1. Loop `i` from 1 to floor(sqrt(N)).
2. If `N % i == 0`, append `i` to `low`. If `i != N/i`, prepend `N/i` to `high` (or append and reverse later).
3. Result = `low + high` (already sorted).

| Metric | Value |
|--------|-------|
| Time   | O(sqrt(N)) |
| Space  | O(d) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(sqrt(N))?** Divisors come in pairs `(d, N/d)`. One member of each pair is at most sqrt(N). So checking candidates up to sqrt(N) finds every pair. For N = 10^9, brute force checks 10^9 candidates. The sqrt approach checks only ~31,623 -- a 30,000x speedup. The number of divisors d is at most a few hundred even for large N (highly composite numbers), so the sort in Approach 2 is negligible.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Counting sqrt(N) twice when N is a perfect square | `i` and `N/i` are the same when `i = sqrt(N)` | Check `if i != N/i` before adding the partner |
| Unsorted output with sqrt approach | Divisors found out of order | Sort, or use two-list merge trick |
| Using `i * i <= N` with int overflow | For large N, `i * i` can overflow int | Use `i <= (int)Math.sqrt(N)` or compare `i <= N / i` |
| Missing divisor N itself | Stopping at sqrt(N) but forgetting to add the partner | The partner `N/1 = N` handles this automatically |

### Edge Cases Checklist
- N = 1 --> [1]
- N = prime (e.g., 7) --> [1, 7]
- N = perfect square (e.g., 36) --> do not duplicate sqrt(N)
- N = 10^9 --> verify sqrt approach handles it fast

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Print all divisors sorted? Range of N? Time constraints?"
2. **M**atch: "Divisor pairing -- if d divides N, so does N/d."
3. **P**lan: "Start with brute O(N), optimize to O(sqrt(N)) using pairing."
4. **I**mplement: Handle perfect square edge case carefully.
5. **R**eview: Trace N=36 showing all pairs.
6. **E**valuate: "O(sqrt(N)) time, which handles N up to 10^9 easily."

### Follow-Up Questions
- "Count the number of divisors?" --> Same loop, just count instead of collecting.
- "Sum of all divisors?" --> Same loop, accumulate sum.
- "Find divisors for all numbers 1..N?" --> Sieve of Divisors (O(N log N)).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Modulo operator, loop basics |
| **Same pattern** | Check for Prime (also uses sqrt(N) boundary) |
| **Harder variant** | Prime factorization (find prime divisors with multiplicity) |
| **Unlocks** | GCD/LCM, Sieve of Eratosthenes, number-theoretic problems |
