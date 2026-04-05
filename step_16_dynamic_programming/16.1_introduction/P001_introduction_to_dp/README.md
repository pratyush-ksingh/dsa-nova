# Introduction to DP

> **Batch 1 of 12** | **Topic:** Dynamic Programming | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Compute the Nth Fibonacci number using the four-stage DP progression:
1. **Recursion** (plain)
2. **Memoization** (top-down DP)
3. **Tabulation** (bottom-up DP)
4. **Space Optimized**

The Fibonacci sequence: `fib(0) = 0, fib(1) = 1, fib(n) = fib(n-1) + fib(n-2)`.

This problem is the gateway to understanding **Dynamic Programming** -- a technique for solving problems with **overlapping subproblems** and **optimal substructure**.

**Constraints:**
- `0 <= n <= 45`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `n = 0` | `0` | Base case |
| `n = 1` | `1` | Base case |
| `n = 5` | `5` | 0, 1, 1, 2, 3, **5** |
| `n = 10` | `55` | 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, **55** |

### Real-Life Analogy
> *Imagine you are climbing stairs and someone asks "how many ways to reach step 5?" You figure out step 5 by asking about step 4 and step 3. But step 4 also asks about step 3 and step 2. Notice step 3 gets asked about **twice**. Without DP, you would recompute it each time -- like calling the same friend to ask the same question over and over. With memoization, you write the answer on a sticky note the first time. With tabulation, you fill in all answers from the bottom up, so every answer is ready before it is needed.*

### Key Observations
1. **Overlapping Subproblems:** `fib(5)` calls `fib(4)` and `fib(3)`. But `fib(4)` also calls `fib(3)`. The same subproblems are solved repeatedly.
2. **Optimal Substructure:** The solution to `fib(n)` is built from solutions to `fib(n-1)` and `fib(n-2)`.
3. **The DP Progression:** Recursion -> Memoization (add cache) -> Tabulation (flip to bottom-up) -> Space Optimization (keep only what you need). <-- This is the "aha" insight: every DP problem follows this same 4-step ladder

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Recursion alone** is exponential because of redundant recomputation.
- **Memoization** caches results, turning exponential into linear -- at the cost of O(n) extra space and recursion overhead.
- **Tabulation** eliminates recursion overhead by building answers bottom-up.
- **Space Optimization** recognizes that `fib(n)` only depends on the last two values, so we only need O(1) space.

### Pattern Recognition
- **Pattern:** Linear DP (1D state, each state depends on previous 1-2 states)
- **Classification Cue:** "When you see _compute the Nth value_ where each value depends on a fixed number of previous values --> think _linear DP with space optimization_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Directly translate the recurrence: `fib(n) = fib(n-1) + fib(n-2)`.

**Steps:**
1. Base case: if `n <= 1`, return `n`.
2. Return `fib(n-1) + fib(n-2)`.

**Why it is slow:** The recursion tree has exponential nodes. `fib(5)` calls `fib(3)` twice, `fib(2)` three times, etc.

```
                fib(5)
               /      \
          fib(4)      fib(3)
         /     \      /    \
      fib(3) fib(2) fib(2) fib(1)
      /   \    ...    ...
   fib(2) fib(1)
```

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** **Unnecessary work** -- `fib(3)` is computed multiple times. Cache the results!

### Approach 2: Memoization (Top-Down DP)
**What changed:** Add a `dp[]` array. Before computing, check if `dp[n]` already has the answer. After computing, store the result.

**Steps:**
1. Create `dp[]` of size `n+1`, initialized to -1.
2. `fib(n)`: if `dp[n] != -1`, return `dp[n]`. Otherwise, compute, store, return.

**Dry Run:** `n = 5`

| Call | n | Cached? | Compute | dp[] |
|------|---|---------|---------|------|
| 1 | 5 | No | fib(4) + fib(3) | - |
| 2 | 4 | No | fib(3) + fib(2) | - |
| 3 | 3 | No | fib(2) + fib(1) | - |
| 4 | 2 | No | fib(1) + fib(0) = 1 | dp[2]=1 |
| 5 | 1 | Base | return 1 | - |
| 6 | 0 | Base | return 0 | - |
| back | 3 | Store | 1 + 1 = 2 | dp[3]=2 |
| back | 4 | Store | 2 + 1 = 3 | dp[4]=3 |
| 7 | 3 | **Yes** | return dp[3]=2 | - |
| back | 5 | Store | 3 + 2 = 5 | dp[5]=5 |

| Time | Space |
|------|-------|
| O(n) | O(n) dp array + O(n) stack = O(n) |

**BUD Transition:** We still use recursion stack. Can we eliminate it?

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill the dp array iteratively from base cases upward. No recursion.

**Steps:**
1. Create `dp[0..n]`. Set `dp[0] = 0`, `dp[1] = 1`.
2. For `i = 2` to `n`: `dp[i] = dp[i-1] + dp[i-2]`.
3. Return `dp[n]`.

**Dry Run:** `n = 5`

| i | dp[i-2] | dp[i-1] | dp[i] |
|---|---------|---------|-------|
| 0 | - | - | 0 |
| 1 | - | - | 1 |
| 2 | 0 | 1 | 1 |
| 3 | 1 | 1 | 2 |
| 4 | 1 | 2 | 3 |
| 5 | 2 | 3 | 5 |

| Time | Space |
|------|-------|
| O(n) | O(n) dp array only |

**BUD Transition:** We only ever look at `dp[i-1]` and `dp[i-2]`. We do not need the entire array!

### Approach 4: Space Optimized
**What changed:** Replace the array with two variables: `prev2` and `prev1`.

**Steps:**
1. `prev2 = 0`, `prev1 = 1`.
2. For `i = 2` to `n`:
   - `curr = prev1 + prev2`
   - `prev2 = prev1`
   - `prev1 = curr`
3. Return `prev1` (or `prev2` if `n == 0`).

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "The recursion tree doubles in size at each level -- roughly 2^n nodes."
**Memo/Tab:** "Each of the n+1 states is computed exactly once, with O(1) work per state. Total: O(n)."
**Space Optimized:** "Same O(n) time, but we only keep 2 variables instead of n -- hence O(1) space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Memoization:** Forgetting to initialize dp array to -1 (0 is a valid Fibonacci value!).
2. **Tabulation:** Off-by-one in loop bounds -- loop should go from 2 to n inclusive.
3. **Space Optimized:** Returning `prev1` when `n == 0` -- need to handle `n == 0` separately since `prev1` starts as `fib(1) = 1`.

### Edge Cases to Test
- [ ] `n = 0` --> 0
- [ ] `n = 1` --> 1
- [ ] `n = 2` --> 1
- [ ] `n = 45` --> 1134903170 (verify no overflow in int)

---

## INTERVIEW LENS

### How to Present
1. **Start with recursion:** "The naive approach directly translates the recurrence. Time is O(2^n) due to redundant calls."
2. **Identify the DP property:** "I notice overlapping subproblems -- fib(3) is computed multiple times. I'll add memoization."
3. **Convert to tabulation:** "To avoid recursion overhead, I'll fill bottom-up."
4. **Optimize space:** "Since I only need the last two values, I can use O(1) space."

### Follow-Up Questions
- "Can you do it in O(log n) time?" --> Yes, matrix exponentiation: [[1,1],[1,0]]^n gives fib(n).
- "What about very large n (modular arithmetic)?" --> Compute fib(n) % MOD at each step.
- "How does this relate to other DP problems?" --> Every 1D DP problem follows the same 4-step progression.

---

## CONNECTIONS
- **Prerequisite:** Recursion basics, function call stack
- **Same Pattern:** Climbing Stairs (P002), Frog Jump (P003)
- **This Unlocks:** The entire DP chapter -- every DP problem uses this same 4-step ladder
- **Harder Variant:** Matrix exponentiation for O(log n), Tribonacci (3 previous values)
