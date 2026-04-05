# Sum of First N Numbers

> **Batch 2 of 12** | **Topic:** Basic Recursion | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a positive integer `N`, find the sum `1 + 2 + 3 + ... + N`.

Solve it using:
1. A simple loop (iterative).
2. Head recursion (natural recursive definition).
3. The closed-form mathematical formula (Gauss's formula).

**Constraints:**
- `1 <= N <= 10^6`
- The result fits in a 64-bit integer for N up to ~10^9

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `N = 5` | `15` | 1+2+3+4+5 = 15 |
| `N = 1` | `1` | Just the number itself |
| `N = 100` | `5050` | The classic Gauss example |
| `N = 10` | `55` | 1+2+...+10 = 55 |

### Real-Life Analogy
> *Imagine stacking boxes in a triangular pyramid. Layer 1 has 1 box, layer 2 has 2 boxes, ..., layer N has N boxes. How many boxes total? You could count each layer (loop), or you could pair them up: layer 1 with layer N, layer 2 with layer N-1 -- each pair sums to N+1, and there are N/2 pairs. Total = N*(N+1)/2. Young Gauss supposedly discovered this at age 7 when asked to sum 1 to 100.*

### Key Observations
1. The recursive definition is beautifully simple: `sum(N) = N + sum(N-1)`, with base case `sum(0) = 0`. This mirrors the mathematical definition. <-- This is the "aha" insight
2. The formula `N*(N+1)/2` gives O(1) time. But the recursive version teaches the core concept of breaking a problem into a smaller subproblem.
3. For large N, the recursive version will overflow the call stack. Tail recursion or the formula avoids this.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No data structure needed -- this is a pure computation problem.
- The choice is between recursion (uses implicit stack) and iteration/formula (uses O(1) space).

### Pattern Recognition
- **Pattern:** Linear Recursion (single recursive call reducing N by 1 each time)
- **Classification Cue:** "Compute something for N that depends on the answer for N-1 --> linear recursion or iterative accumulation."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Iterative Loop
**Idea:** Use a loop to accumulate the sum from 1 to N.

**Steps:**
1. Initialize `sum = 0`.
2. For `i` from 1 to N: `sum += i`.
3. Return `sum`.

**Why it works:** Directly computes the sum by visiting every number.

**BUD Transition -- Bottleneck:** We iterate N times. Can we avoid iterating entirely?

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Head Recursion
**What changed:** Express the sum recursively. `sum(N) = N + sum(N-1)`. Base case: `sum(0) = 0`.

**Steps:**
1. If `N == 0`, return `0`.
2. Return `N + sum(N - 1)`.

**Dry Run:** `sum(5)`

| Call | N | Returns | Computation |
|------|---|---------|-------------|
| sum(5) | 5 | 5 + sum(4) = 5 + 10 = 15 | waiting |
| sum(4) | 4 | 4 + sum(3) = 4 + 6 = 10 | waiting |
| sum(3) | 3 | 3 + sum(2) = 3 + 3 = 6 | waiting |
| sum(2) | 2 | 2 + sum(1) = 2 + 1 = 3 | waiting |
| sum(1) | 1 | 1 + sum(0) = 1 + 0 = 1 | waiting |
| sum(0) | 0 | 0 (base case) | returns |

Stack unwinds: 0 -> 1 -> 3 -> 6 -> 10 -> **15**

**Note:** This creates N stack frames. For N = 10^6, this will cause a StackOverflow in most languages.

| Time | Space |
|------|-------|
| O(n) | O(n) recursion stack |

### Approach 3: Best -- Gauss's Formula (O(1))
**What changed:** Use the closed-form formula: `sum = N * (N + 1) / 2`.

**Derivation:** Write the sum forwards and backwards:
```
S = 1 + 2 + 3 + ... + N
S = N + (N-1) + ... + 1
---
2S = (N+1) + (N+1) + ... + (N+1)  [N terms]
2S = N * (N+1)
S  = N * (N+1) / 2
```

**Steps:**
1. Return `N * (N + 1) / 2`.

**Note:** For very large N, use `N * (N + 1L) / 2` to avoid integer overflow. In Python, integers have arbitrary precision so no worry.

| Time | Space |
|------|-------|
| O(1) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) for formula, O(n) for loop/recursion -- "The formula does constant arithmetic. The loop and recursion both visit each number from 1 to N exactly once."
**Space:** O(1) for formula and loop. O(n) for recursion -- "Each recursive call adds a stack frame. N calls = N frames."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Integer overflow:** For large N, `N * (N+1)` can overflow 32-bit int. Use `long` in Java or rely on Python's big integers.
2. **Missing base case:** Forgetting `sum(0) = 0` causes infinite recursion.
3. **Off-by-one:** Computing `N*(N-1)/2` instead of `N*(N+1)/2`.

### Edge Cases to Test
- [ ] N = 0 (sum should be 0)
- [ ] N = 1 (sum is 1)
- [ ] Large N (test overflow handling)
- [ ] N at stack limit (recursion depth test)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the sum 1 + 2 + ... + N."
2. **Match:** "This has a direct formula: N*(N+1)/2. I can also show the recursive structure."
3. **Plan:** "I will implement the formula for O(1), then show recursion to demonstrate the pattern."
4. **Implement:** Write the formula first (one line), then the recursive version.
5. **Review:** Verify with N=5: 5*6/2 = 15. Recursive trace: 5+4+3+2+1+0 = 15.
6. **Evaluate:** "Formula is O(1) time, O(1) space. Recursion is O(n) time, O(n) space."

### Follow-Up Questions
- "What about tail recursion?" --> Pass the accumulator as a parameter: `sum(N, acc) = sum(N-1, acc+N)`. Some languages optimize tail calls to O(1) space.
- "Sum of first N odd numbers?" --> N^2. Sum of first N even numbers: N*(N+1).
- "What if you need sum of a range [a, b]?" --> sum(b) - sum(a-1) using the formula.

---

## CONNECTIONS
- **Prerequisite:** Basic loop, function calls
- **Same Pattern:** Factorial of N (P005), Fibonacci
- **Harder Variant:** Sum of series with conditions (sum of squares, cubes)
- **This Unlocks:** Understanding recursion as "trust the sub-problem" -- foundation for all recursive algorithms
