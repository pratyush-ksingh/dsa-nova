# GCD or HCF

> **Batch 2 of 12** | **Topic:** Basic Math | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given two positive integers A and B, find their **Greatest Common Divisor** (GCD), also known as the **Highest Common Factor** (HCF). The GCD is the largest positive integer that divides both A and B.

**Example:**
```
Input: A = 12, B = 18
Output: 6
```

| Input (A, B) | Output | Explanation |
|---|---|---|
| (12, 18) | 6 | Divisors of 12: {1,2,3,4,6,12}. Divisors of 18: {1,2,3,6,9,18}. Common: {1,2,3,6}. Max = 6 |
| (7, 13) | 1 | Both prime, coprime to each other |
| (24, 24) | 24 | Same number, GCD is itself |
| (0, 5) | 5 | GCD(0, n) = n by convention |
| (48, 18) | 6 | GCD(48,18) = GCD(18,12) = GCD(12,6) = GCD(6,0) = 6 |

### Real-Life Analogy
You have two ropes of length 12m and 18m and want to cut them into pieces of equal length with no rope left over. What is the longest piece length? You need a length that divides both 12 and 18. The longest such length is 6m -- you get 2 pieces from the 12m rope and 3 from the 18m rope. That length is the GCD.

### Key Observations
1. Brute force: try every number from 1 to min(A,B) and track the largest that divides both. O(min(A,B)).
2. Euclid's insight: `GCD(A, B) = GCD(B, A % B)` because any common divisor of A and B also divides their remainder.
3. **Aha moment:** The Euclidean algorithm replaces a potentially huge number (A) with a much smaller one (A % B) at each step, converging in O(log(min(A,B))) steps. This is exponentially faster than brute force.

### Constraints
- 0 <= A, B <= 10^9
- At least one of A, B is positive

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Euclidean Algorithm?
The key mathematical property is: `GCD(A, B) = GCD(B, A mod B)`. This reduces the problem size by at least half at every step (since `A mod B < A/2` when `B <= A/2`, and `A mod B < B` otherwise). No data structure needed -- just repeated division.

### Pattern Recognition
**Classification cue:** "GCD", "HCF", "greatest common divisor" --> Euclidean algorithm. This is one of the oldest known algorithms (300 BC) and appears as a subroutine in fraction simplification, modular inverse, LCM computation, and extended GCD.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Linear Search
**Intuition:** Check every number from 1 to min(A, B). The largest that divides both is the GCD.

**Steps:**
1. Set `gcd = 1`.
2. For `d` from 1 to min(A, B):
   - If `A % d == 0` and `B % d == 0`, update `gcd = d`.
3. Return `gcd`.

**Dry Run Trace (A = 12, B = 18):**

| d | A%d | B%d | Both 0? | gcd |
|---|-----|-----|---------|-----|
| 1 | 0 | 0 | Yes | 1 |
| 2 | 0 | 0 | Yes | 2 |
| 3 | 0 | 0 | Yes | 3 |
| 4 | 0 | 2 | No | 3 |
| 5 | 2 | 3 | No | 3 |
| 6 | 0 | 0 | Yes | 6 |
| 7-12 | ... | ... | No | 6 |

| Metric | Value |
|--------|-------|
| Time | O(min(A, B)) |
| Space | O(1) |

**BUD Transition:** The **B**ottleneck is checking every number up to min(A,B). Euclid discovered that if d divides both A and B, it also divides A-B (and hence A%B). This lets us replace the problem with a smaller one: GCD(B, A%B).

---

### Approach 2: Optimal -- Euclidean Algorithm (Iterative)
**Intuition:** Repeatedly replace the larger number with the remainder of dividing the larger by the smaller. When the remainder hits 0, the other number is the GCD.

**Steps:**
1. While `B != 0`:
   - `temp = B`
   - `B = A % B`
   - `A = temp`
2. Return `A`.

**Dry Run Trace (A = 48, B = 18):**

| Step | A | B | A % B |
|------|---|---|-------|
| 1 | 48 | 18 | 12 |
| 2 | 18 | 12 | 6 |
| 3 | 12 | 6 | 0 |
| 4 | 6 | 0 | done |

GCD = 6

| Metric | Value |
|--------|-------|
| Time | O(log(min(A, B))) |
| Space | O(1) |

---

### Approach 3: Best -- Euclidean Algorithm (Recursive)
**Intuition:** Same logic as iterative, expressed recursively: `GCD(A, B) = GCD(B, A % B)` with base case `GCD(A, 0) = A`.

**Steps:**
1. If `B == 0`, return `A`.
2. Otherwise, return `GCD(B, A % B)`.

| Metric | Value |
|--------|-------|
| Time | O(log(min(A, B))) |
| Space | O(log(min(A, B))) for recursion stack |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(min(A,B)) for brute force?** We check every candidate from 1 to min(A,B). That is min(A,B) iterations.

**Why O(log(min(A,B))) for Euclidean?** At each step, the remainder `A % B` is strictly less than B. Moreover, after two consecutive steps, the value drops by at least half (this can be proven). So the number of steps is at most 2 * log2(min(A,B)). For A=10^9, that is about 60 steps instead of 1,000,000,000.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Not handling A=0 or B=0 | Division by zero in A%B | GCD(0, n) = n; check B==0 as base case |
| Forgetting to swap when A < B | A%B = A when A < B, wasting a step | Euclidean algorithm self-corrects: first step swaps them |
| Infinite loop in iterative | Wrong variable update order | Use a temp variable or simultaneous assignment |

### Edge Cases Checklist
- A == B --> GCD = A
- A == 0 --> GCD = B
- B == 0 --> GCD = A
- A and B are coprime (GCD = 1) --> e.g., (7, 13)
- One is a multiple of the other --> GCD = smaller one
- Very large values (10^9) --> Euclidean handles in ~60 steps

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Are both positive? Can either be 0? Integer range?"
2. **M**atch: "GCD --> Euclidean algorithm."
3. **P**lan: "Iterative: while B != 0, replace (A, B) with (B, A%B). Return A."
4. **I**mplement: 3-line loop.
5. **R**eview: Trace GCD(48, 18) = GCD(18, 12) = GCD(12, 6) = GCD(6, 0) = 6.
6. **E**valuate: "O(log min(A,B)) time, O(1) space. Essentially instant for any 32-bit input."

### Follow-Up Questions
- "How to compute LCM?" --> `LCM(A, B) = A * B / GCD(A, B)`. Use `A / GCD * B` to avoid overflow.
- "What is the Extended Euclidean Algorithm?" --> Also finds x, y such that Ax + By = GCD(A, B). Used for modular inverse.
- "GCD of an array of numbers?" --> Fold: `GCD(a, b, c) = GCD(GCD(a, b), c)`.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Modulo operation, division |
| **Same pattern** | LCM computation, fraction simplification |
| **Harder variant** | Extended GCD, Modular Inverse, GCD of array |
| **Unlocks** | Fraction problems, number theory, cryptography (RSA) |
