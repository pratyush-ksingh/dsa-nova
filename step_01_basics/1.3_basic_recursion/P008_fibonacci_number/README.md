# Fibonacci Number (Recursion)

> **Batch 3 of 12** | **Topic:** Recursion | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an integer **n**, compute the **n-th Fibonacci number** where:
- F(0) = 0, F(1) = 1
- F(n) = F(n-1) + F(n-2) for n >= 2

**LeetCode #509**

**Example:**
```
Input: n = 5
Output: 5
Explanation: F(0)=0, F(1)=1, F(2)=1, F(3)=2, F(4)=3, F(5)=5.

Input: n = 10
Output: 55
```

| Input | Output | Explanation |
|-------|--------|-------------|
| 0     | 0      | Base case |
| 1     | 1      | Base case |
| 2     | 1      | 0 + 1 |
| 5     | 5      | 0, 1, 1, 2, 3, 5 |
| 10    | 55     | Sequence: 0,1,1,2,3,5,8,13,21,34,55 |
| 30    | 832040 | Naive recursion is extremely slow here |

### Real-Life Analogy
Fibonacci numbers model **rabbit population growth**. Start with one pair of baby rabbits. Each month, every mature pair produces a new pair, and baby pairs take one month to mature. Month 0: 0 pairs producing. Month 1: 1 pair. Month 2: 1 pair (the original, now mature). Month 3: 2 pairs (original + new). Each month's count is the sum of the previous two months, which is exactly the Fibonacci recurrence. In nature, Fibonacci appears in sunflower spirals, pinecone scales, and the Golden Ratio.

### Key Observations
1. The naive recursive definition directly translates to code, but it recomputes the same subproblems exponentially many times.
2. F(n) only depends on F(n-1) and F(n-2) -- we only need the last two values, not the entire history.
3. **Aha moment:** The call tree for naive recursion on F(5) has ~15 calls, and for F(30) has ~2.7 million calls. Many are duplicates (F(3) is computed 3 times for F(5)). Memoization or iteration eliminates ALL redundancy, going from O(2^n) to O(n). This is the gateway to dynamic programming -- if you understand why memoized Fibonacci is fast, you understand the core DP insight.

### Constraints
- 0 <= n <= 30 (LeetCode constraint)
- F(30) = 832040 fits in int

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Recursion / DP?
The problem has optimal substructure (F(n) depends on smaller Fibonacci values) and overlapping subproblems (same F(k) computed many times). This makes it the textbook introduction to memoization and dynamic programming.

### Pattern Recognition
**Classification cue:** "Compute F(n) where F(n) depends on previous F values" --> recursion with memoization, or bottom-up DP. This is the simplest member of the DP family and the gateway problem for understanding state, transitions, and memo tables.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Naive Recursion
**Intuition:** Directly implement the mathematical definition. F(n) = F(n-1) + F(n-2) with base cases F(0)=0, F(1)=1.

**Steps:**
1. If n <= 1, return n.
2. Return fib(n-1) + fib(n-2).

**Dry Run Trace (n=5) -- Call Tree:**
```
                    fib(5)
                /          \
           fib(4)          fib(3)
          /      \        /      \
      fib(3)   fib(2)  fib(2)  fib(1)
      /    \   /    \   /    \
  fib(2) fib(1) fib(1) fib(0) fib(1) fib(0)
  /    \
fib(1) fib(0)
```

Total calls: 15. fib(2) computed 3 times, fib(3) computed 2 times.

| Metric | Value |
|--------|-------|
| Time   | O(2^n) -- exponential, each call branches into two |
| Space  | O(n) recursion stack depth |

**BUD Transition:** Massive redundancy -- same subproblems solved repeatedly. Store results in a cache (memoization).

---

### Approach 2: Optimal -- Memoized Recursion (Top-Down DP)
**Intuition:** Same recursive structure, but store each F(k) in a memo table when first computed. Before recursing, check if the answer is already cached.

**Steps:**
1. Create memo array/map of size n+1.
2. If n <= 1, return n.
3. If memo[n] is set, return it.
4. memo[n] = fib(n-1) + fib(n-2).
5. Return memo[n].

**Dry Run Trace (n=5):**

| Call | Cache Hit? | Compute | Cache State |
|------|-----------|---------|-------------|
| fib(5) | No | fib(4) + fib(3) | - |
| fib(4) | No | fib(3) + fib(2) | - |
| fib(3) | No | fib(2) + fib(1) | - |
| fib(2) | No | fib(1) + fib(0) = 1 | {2:1} |
| fib(3) | No | 1 + 1 = 2 | {2:1, 3:2} |
| fib(4) | No | 2 + 1 = 3 | {2:1, 3:2, 4:3} |
| fib(3) | Yes! | return 2 | {2:1, 3:2, 4:3} |
| fib(5) | No | 3 + 2 = 5 | {2:1, 3:2, 4:3, 5:5} |

Total unique computations: 4 (instead of 15).

| Metric | Value |
|--------|-------|
| Time   | O(n) -- each F(k) computed once |
| Space  | O(n) for memo + O(n) stack = O(n) |

---

### Approach 3: Best -- Iterative Bottom-Up (Space-Optimized)
**Intuition:** Since F(n) only depends on the previous two values, maintain just two variables and iterate from 0 to n. No recursion, no memo table, no stack overhead.

**Steps:**
1. If n <= 1, return n.
2. Set `prev2 = 0`, `prev1 = 1`.
3. Loop from 2 to n: `curr = prev1 + prev2`, shift: `prev2 = prev1`, `prev1 = curr`.
4. Return `prev1`.

**Dry Run Trace (n=5):**

| Step | prev2 | prev1 | curr (prev1+prev2) |
|------|-------|-------|-------------------|
| i=2  | 0     | 1     | 1                 |
| i=3  | 1     | 1     | 2                 |
| i=4  | 1     | 2     | 3                 |
| i=5  | 2     | 3     | 5                 |

Return 5.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) -- just two variables |

---

## 4. COMPLEXITY INTUITIVELY

**Why is naive recursion O(2^n)?** Each call makes two more calls. The call tree is a binary tree of depth n, which has ~2^n nodes. For n=30, that is ~1 billion calls.

**Why is memoization O(n)?** Each of the n+1 unique states F(0)..F(n) is computed exactly once and cached. All other "calls" are O(1) cache lookups.

**Why is iteration O(1) space?** F(n) only depends on F(n-1) and F(n-2). We do not need to remember F(n-3) or earlier. Two variables suffice.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Using naive recursion for large n | Works for n=10, TLE for n=40+ | Use memoization or iteration |
| Forgetting base case F(0)=0 | Starting the sequence at 1,1 | Handle n=0 explicitly |
| Integer overflow for large n | F(46) > 10^9, F(93) > long max | Use BigInteger or note constraints |
| Memoizing with wrong key | Off-by-one in array indexing | Use n directly as index |

### Edge Cases Checklist
- n = 0 --> 0
- n = 1 --> 1
- n = 2 --> 1
- n = 30 --> 832040

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Return the n-th Fibonacci number. 0-indexed? F(0)=0, F(1)=1?"
2. **M**atch: "Classic recursion with overlapping subproblems -- DP pattern."
3. **P**lan: "Start with naive recursion, explain why it is O(2^n), optimize with memo, then show iterative O(1) space."
4. **I**mplement: Write all three to show optimization journey.
5. **R**eview: Trace call tree for n=5 to show redundancy.
6. **E**valuate: "Naive O(2^n), memo O(n) time O(n) space, iterative O(n) time O(1) space."

### Follow-Up Questions
- "Can you do better than O(n)?" --> Matrix exponentiation: O(log n) time. [[1,1],[1,0]]^n gives F(n).
- "What about Fibonacci modulo M?" --> Pisano period; same algorithm, just take mod at each step.
- "Climbing stairs problem?" --> Exactly Fibonacci! (ways to climb n stairs taking 1 or 2 steps at a time).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic recursion (factorial, sum of N) |
| **Same pattern** | Climbing Stairs (LeetCode 70), Tribonacci Number |
| **Harder variant** | Matrix exponentiation Fibonacci O(log n), Fibonacci modulo M |
| **Unlocks** | Dynamic Programming (this IS the gateway DP problem), memoization concept |
