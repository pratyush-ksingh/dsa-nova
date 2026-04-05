# Find Square Root

> **Batch 1 of 12** | **Topic:** Binary Search | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a non-negative integer `x`, return the **floor of the square root** of `x`. In other words, return the largest integer `r` such that `r * r <= x`. You must not use any built-in exponent function or operator (e.g., `pow(x, 0.5)` or `x ** 0.5`).

**LeetCode #69**

**Constraints:**
- `0 <= x <= 2^31 - 1` (i.e., up to ~2.1 billion)

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `x = 4` | `2` | sqrt(4) = 2.0 exactly |
| `x = 8` | `2` | sqrt(8) = 2.828..., floor is 2 |
| `x = 0` | `0` | sqrt(0) = 0 |
| `x = 1` | `1` | sqrt(1) = 1 |
| `x = 2147483647` | `46340` | 46340^2 = 2,147,395,600 <= x |

### Real-Life Analogy
> *You are building a square garden and you have 20 square tiles. You want the largest square you can make. You try 1x1 (uses 1 tile), 2x2 (uses 4), 3x3 (uses 9), 4x4 (uses 16) -- all good. But 5x5 needs 25 tiles, which you do not have. So the answer is 4. Instead of trying every size from 1 upward, you could jump to the middle of the range, check if it fits, and narrow down -- that is binary search on the answer.*

### Key Observations
1. We are searching for a value `r` in the range `[0, x]` such that `r*r <= x` but `(r+1)*(r+1) > x`.
2. The function `f(r) = r*r` is monotonically increasing. This means binary search applies.
3. The search space is not an array -- it is the range of possible answers `[0, x]`. This is "binary search on answers." <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No explicit data structure needed. The "array" is the implicit sequence of integers `[0, 1, 2, ..., x]`.
- We apply binary search on this conceptual sorted sequence.

### Pattern Recognition
- **Pattern:** Binary Search on Answer Space
- **Classification Cue:** "Whenever you see _find the largest/smallest value satisfying a monotonic condition_ where the search space is a range of numbers --> think _binary search on answers_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Try every integer from 0 upward. Return the last one whose square does not exceed x.

**Steps:**
1. For `r` from 0 to x:
   - If `r * r > x`, return `r - 1`.
2. Return `x` (covers x = 0 and x = 1).

**Why it works:** Exhaustive search over all candidates.

**BUD Transition -- Bottleneck:** We try up to sqrt(x) candidates linearly. Since the squares form a monotonic sequence, we can binary search instead.

| Time | Space |
|------|-------|
| O(sqrt(x)) | O(1) |

### Approach 2: Optimal -- Binary Search on Answer
**What changed:** Binary search the range `[0, x]`. If `mid*mid <= x`, move right (mid could be the answer). Otherwise move left.

**Steps:**
1. If `x < 2`, return `x`.
2. Set `lo = 1`, `hi = x / 2` (sqrt is at most x/2 for x >= 4).
3. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - If `mid <= x / mid` (avoids overflow vs `mid*mid <= x`): `ans = mid`, `lo = mid + 1`.
   - Else: `hi = mid - 1`.
4. Return `ans`.

**Dry Run:** `x = 8`

| Step | lo | hi | mid | mid*mid | Condition | ans | Action |
|------|----|----|-----|---------|-----------|-----|--------|
| Init | 1  | 4  | -   | -       | -         | 0   | - |
| 1    | 1  | 4  | 2   | 4       | 4 <= 8    | 2   | lo = 3 |
| 2    | 3  | 4  | 3   | 9       | 9 > 8     | 2   | hi = 2 |
| End  | 3  | 2  | -   | -       | lo > hi   | 2   | return 2 |

**Result:** 2

| Time | Space |
|------|-------|
| O(log x) | O(1) |

### Approach 3: Best -- Newton's Method (Integer Version)
**What changed:** Use Newton's iterative method for root finding. Start with a guess and refine: `r = (r + x/r) / 2`. Converges very quickly (quadratic convergence).

**Steps:**
1. If `x < 2`, return `x`.
2. Start with `r = x / 2`.
3. While `r * r > x`:
   - `r = (r + x / r) / 2`.
4. Return `r`.

**Note:** Newton's method converges in O(log(log(x))) iterations in practice, making it the fastest. However, the constant factor and simplicity of binary search often make Approach 2 preferred in interviews.

| Time | Space |
|------|-------|
| O(log(log(x))) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(log x) for binary search -- "The search space is [0, x]. Each step halves it, so we need log2(x) steps. For x up to 2^31, that is at most 31 iterations."
**Space:** O(1) -- "Only a few integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Integer overflow:** Computing `mid * mid` when mid is large overflows int. Use `mid <= x / mid` instead, or use `long`.
2. Forgetting `x = 0` and `x = 1` as special cases.
3. Setting `hi = x` instead of `hi = x / 2` -- works but wastes one iteration.

### Edge Cases to Test
- [ ] `x = 0` --> return 0
- [ ] `x = 1` --> return 1
- [ ] Perfect square `x = 16` --> return 4
- [ ] Non-perfect square `x = 15` --> return 3
- [ ] Maximum value `x = 2147483647` --> return 46340

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the floor of sqrt(x), without using built-in sqrt."
2. **Match:** "The square function is monotonic on [0, x], so I can binary search for the largest r where r*r <= x."
3. **Plan:** "Binary search on [1, x/2]. Use division to avoid overflow."
4. **Implement:** Code iterative binary search with overflow-safe comparison.
5. **Review:** Walk through dry run for x = 8.
6. **Evaluate:** "O(log x) time, O(1) space."

### Follow-Up Questions
- "What about finding the exact square root as a double?" --> Binary search on doubles with precision epsilon, or Newton's method with floating point.
- "What if x can be very large (beyond 64-bit)?" --> Use Newton's method with big integer arithmetic.
- "Can you use bit manipulation?" --> Yes: binary search bit by bit from the MSB down, setting each bit if the resulting square does not exceed x.

---

## CONNECTIONS
- **Prerequisite:** Binary Search (P001), understanding of search on answer space
- **Same Pattern:** Koko Eating Bananas (LC #875), Capacity to Ship (LC #1011)
- **Harder Variant:** Nth Root of M, Sqrt with decimal precision
- **This Unlocks:** All "binary search on answer" problems in Step 4.2
