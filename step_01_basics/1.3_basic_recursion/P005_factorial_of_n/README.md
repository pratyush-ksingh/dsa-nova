# Factorial of N

> **Batch 2 of 12** | **Topic:** Basic Recursion | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a non-negative integer `N`, compute `N!` (N factorial), defined as:
- `N! = N * (N-1) * (N-2) * ... * 2 * 1`
- `0! = 1` (by convention)

Discuss the risks of stack overflow for large N when using recursion.

**Constraints:**
- `0 <= N <= 20` (result fits in a 64-bit integer; 20! = 2,432,902,008,176,640,000)
- For N > 20, use BigInteger (Java) or Python's arbitrary-precision int

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `N = 0` | `1` | 0! = 1 by definition |
| `N = 1` | `1` | 1! = 1 |
| `N = 5` | `120` | 5*4*3*2*1 = 120 |
| `N = 10` | `3628800` | 10! = 3,628,800 |
| `N = 20` | `2432902008176640000` | Largest N fitting in long |

### Real-Life Analogy
> *Imagine you are arranging 5 books on a shelf. For the first slot, you have 5 choices. For the second, 4 remain. Then 3, 2, 1. The total number of arrangements is 5 * 4 * 3 * 2 * 1 = 120. Factorial counts permutations -- the number of ways to arrange N distinct items. This is why it grows so explosively: 20 items have over 2 quintillion arrangements.*

### Key Observations
1. The recursive definition is `fact(N) = N * fact(N-1)` with base case `fact(0) = 1`. Each call reduces the problem by exactly one unit -- the simplest form of recursion. <-- This is the "aha" insight
2. Factorial grows extremely fast. 13! already exceeds 32-bit int range. 21! exceeds 64-bit long. Always choose your data type carefully.
3. Iterative and recursive versions do the same N multiplications. The difference is space: recursion uses O(N) stack frames, iteration uses O(1).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No data structure needed -- pure computation.
- The recursion uses the call stack implicitly. The iterative version uses a single accumulator variable.

### Pattern Recognition
- **Pattern:** Linear Recursion (single recursive call, reducing N by 1)
- **Classification Cue:** "f(N) defined in terms of f(N-1) with a base case --> textbook recursion or simple loop."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Iterative Loop
**Idea:** Multiply numbers from 1 to N in a loop.

**Steps:**
1. Initialize `result = 1`.
2. For `i` from 2 to N: `result *= i`.
3. Return `result`.

**Why it works:** Directly applies the definition of factorial by multiplying all integers from 1 to N.

**BUD Transition -- Unnecessary Work:** None for this problem. The loop is already O(N) time and O(1) space, which is optimal for computing factorial without a lookup table.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Head Recursion
**What changed:** Express the computation recursively. `fact(N) = N * fact(N-1)`, `fact(0) = 1`.

**Steps:**
1. Base case: if `N == 0`, return `1`.
2. Recursive case: return `N * fact(N - 1)`.

**Dry Run:** `fact(5)`

| Call | N | Waits For | Returns |
|------|---|-----------|---------|
| fact(5) | 5 | fact(4) | 5 * 24 = 120 |
| fact(4) | 4 | fact(3) | 4 * 6 = 24 |
| fact(3) | 3 | fact(2) | 3 * 2 = 6 |
| fact(2) | 2 | fact(1) | 2 * 1 = 2 |
| fact(1) | 1 | fact(0) | 1 * 1 = 1 |
| fact(0) | 0 | (base) | 1 |

Stack unwinds: 1 -> 1 -> 2 -> 6 -> 24 -> **120**

**Stack Overflow Risk:** For N > ~5000 (Python) or ~10000 (Java), the recursive version will overflow the call stack. For factorial, this is rarely a practical concern since 20! already exceeds long range, but it matters conceptually.

| Time | Space |
|------|-------|
| O(n) | O(n) recursion stack |

### Approach 3: Best -- Tail Recursion with Accumulator
**What changed:** Pass the accumulated product as a parameter so the recursive call is in tail position. Some compilers/runtimes optimize this to O(1) space (though Java and Python do not).

**Steps:**
1. Define `fact(N, acc)` with `acc` initialized to 1.
2. Base case: if `N == 0`, return `acc`.
3. Recursive case: return `fact(N - 1, acc * N)`.

**Why tail recursion matters:** The recursive call is the LAST operation -- no computation after it. A tail-call-optimizing compiler can reuse the current stack frame, making it equivalent to a loop.

| Time | Space |
|------|-------|
| O(n) | O(1) with TCO, O(n) without |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We perform exactly N multiplications regardless of approach."
**Space:** O(1) for iterative, O(n) for recursion -- "Each recursive call adds one stack frame. The loop uses only one variable."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting 0! = 1:** This is the base case. Getting it wrong breaks the recursion.
2. **Integer overflow:** 13! > 2^31. 21! > 2^63. Always use `long` in Java, or Python's unlimited integers.
3. **Negative input:** Factorial is undefined for negative numbers. Add an input check.
4. **No base case:** Causes infinite recursion and stack overflow.

### Edge Cases to Test
- [ ] N = 0 (should return 1)
- [ ] N = 1 (should return 1)
- [ ] N = 20 (largest N fitting in Java long)
- [ ] N = 25 (needs BigInteger in Java, works naturally in Python)
- [ ] Negative N (should handle gracefully)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Compute N factorial. N! = N * (N-1) * ... * 1, with 0! = 1."
2. **Match:** "Direct multiplication -- either iterative or recursive."
3. **Plan:** "I will show the iterative version for O(1) space, then the recursive version to demonstrate the pattern."
4. **Implement:** Write both. Mention overflow concerns.
5. **Review:** Trace fact(5) = 5*4*3*2*1 = 120.
6. **Evaluate:** "O(n) time for both. O(1) space iterative, O(n) recursive. Iterative is preferred in production."

### Follow-Up Questions
- "How would you compute factorial of very large N (e.g., 1000)?" --> Use BigInteger (Java) or Python's built-in arbitrary precision. Multiplication itself becomes expensive for huge numbers.
- "What is the time complexity of computing N! for very large N?" --> O(N * M) where M is the number of digits in the result, because multiplying large numbers is not O(1).
- "Trailing zeros in N!?" --> Count factors of 5: floor(N/5) + floor(N/25) + floor(N/125) + ...

---

## CONNECTIONS
- **Prerequisite:** Loops, function calls, base cases
- **Same Pattern:** Sum of First N Numbers (P004), Power function (x^n)
- **Harder Variant:** Trailing Zeros in Factorial (LC #172), Large Factorial (BigInteger)
- **This Unlocks:** Combinatorics (nCr = n! / (r! * (n-r)!)), recursion confidence for tree/graph problems
