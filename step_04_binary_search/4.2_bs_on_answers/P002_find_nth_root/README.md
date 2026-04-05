# Find Nth Root

> **Batch 4 of 12** | **Topic:** Binary Search on Answers | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given two integers `n` and `m`, find the **integer nth root of m** -- that is, find `x` such that `x^n = m`. If no such integer exists, return `-1`. In some variants, return the floor of the nth root.

### Examples

| Input       | Output | Explanation                                       |
|-------------|--------|---------------------------------------------------|
| `n=3, m=27` | 3      | 3^3 = 27, so the cube root of 27 is 3            |
| `n=2, m=16` | 4      | 4^2 = 16, so the square root of 16 is 4          |
| `n=3, m=20` | -1     | No integer x where x^3 = 20 (2^3=8, 3^3=27)     |
| `n=4, m=81` | 3      | 3^4 = 81                                         |
| `n=2, m=1`  | 1      | 1^2 = 1                                          |

### Analogy
Imagine you are guessing a number. You know the answer is between 1 and m. You guess the middle, compute its nth power, and check: too high? Search lower half. Too low? Search upper half. This is binary search on the "answer space" -- instead of searching an array, you search a range of possible answers.

### 3 Key Observations
1. **"Aha" -- Binary search on the answer, not on data.** The answer lies in `[1, m]` (or more tightly `[1, m^(1/n)]`). We binary search this range, checking if `mid^n == m`.
2. **"Aha" -- Overflow danger.** Computing `mid^n` can overflow even long/int for large mid and n. Use early termination: if at any point during multiplication `result > m`, stop and return "too big."
3. **"Aha" -- Upper bound optimization.** The nth root of m is at most `m^(1/n)`, which is at most `m` for n >= 1. For n >= 2, it is at most `sqrt(m)`. Using `m` as the upper bound is safe but conservative.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm              | Why?                                      |
|------------------|---------------|------------------------|--------------------------------------------|
| Brute Force      | None          | Linear scan 1 to m     | Try each x, compute x^n                   |
| Optimal          | None          | Binary search on [1,m] | O(log(m) * n) with safe power computation |
| Best             | None          | Same + Newton's method  | Converges faster for large m              |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Intuition:** Try every integer x from 1 upward. Compute `x^n`. If it equals m, return x. If it exceeds m, return -1 (no exact root exists).

**Steps:**
1. For `x` from 1 to m:
   - Compute `power = x^n` (with overflow check).
   - If `power == m`, return `x`.
   - If `power > m`, return `-1`.
2. Return `-1`.

**Dry-Run Trace** with `n=3, m=27`:
```
x=1: 1^3=1 < 27, continue
x=2: 2^3=8 < 27, continue
x=3: 3^3=27 == 27, return 3
```

| Metric | Value                              |
|--------|------------------------------------|
| Time   | O(m^(1/n) * n) -- up to O(m * n)  |
| Space  | O(1)                               |

---

### Approach 2: Optimal -- Binary Search on Answer Space
**Intuition:** The function `f(x) = x^n` is monotonically increasing. So we can binary search for the `x` where `x^n == m`. Search range: `[1, m]`.

**Overflow-Safe Power Check:**
Instead of computing `mid^n` fully, multiply step by step. If the intermediate result ever exceeds `m`, return 2 (meaning "too big"). If it equals `m` at the end, return 1. Otherwise return 0 (too small).

**Steps:**
1. `lo = 1`, `hi = m`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`.
   - Compute `result = safePow(mid, n, m)`.
   - If `result == 1` (equals m), return `mid`.
   - If `result == 0` (less than m), `lo = mid + 1`.
   - If `result == 2` (greater than m), `hi = mid - 1`.
3. Return `-1`.

**Dry-Run Trace** with `n=3, m=27`:
```
lo=1, hi=27
mid=14: 14^3 = 2744 > 27 -> hi=13
mid=7:  7^3 = 343 > 27 -> hi=6
mid=3:  3^3 = 27 == 27 -> return 3
```

| Metric | Value                |
|--------|----------------------|
| Time   | O(n * log(m))        |
| Space  | O(1)                 |

---

### Approach 3: Best -- Newton's Method (Integer Version)
**Intuition:** Newton's method for `f(x) = x^n - m = 0` converges quadratically. The iteration is: `x_new = x - f(x)/f'(x) = x - (x^n - m)/(n * x^(n-1)) = ((n-1)*x + m/x^(n-1)) / n`. Start with `x = m` and iterate until convergence. This is particularly fast for very large `m`.

**Steps:**
1. Start with `x = m` (or a better initial guess).
2. Repeat:
   - `x_new = ((n-1) * x + m / x^(n-1)) / n`
   - If `x_new >= x`, break (converged or oscillating).
   - `x = x_new`.
3. Check if `x^n == m`. If yes, return `x`. Else return `-1`.

| Metric | Value                                          |
|--------|------------------------------------------------|
| Time   | O(n * log(log(m))) -- quadratic convergence    |
| Space  | O(1)                                           |

---

## COMPLEXITY INTUITIVELY

- **Why O(n log m) for binary search:** We do O(log m) binary search iterations. Each iteration computes `mid^n` in O(n) multiplications.
- **Why Newton's is faster:** Quadratic convergence means the number of iterations is O(log(log(m))) instead of O(log(m)). Each iteration still costs O(n) for the power, but there are far fewer iterations.
- **Practical note:** For the typical constraint m <= 10^9 and n <= 30, binary search does at most 30 * 30 = 900 operations. Newton's does maybe 30 * 5 = 150. Both are instant.

---

## EDGE CASES & MISTAKES

| Edge Case          | What Happens                                    |
|--------------------|-------------------------------------------------|
| `m = 1, any n`    | Answer is always 1 (1^n = 1).                   |
| `m = 0, any n`    | Answer is 0 (0^n = 0 for n > 0).                |
| `n = 1`           | Answer is m itself (x^1 = m -> x = m).          |
| No integer root    | Return -1 (e.g., n=2, m=5).                    |
| Large m, large n   | Overflow in `mid^n` -- must use safe power.     |

**Common Mistakes:**
- Integer overflow when computing `mid^n` without early termination.
- Forgetting the `n = 1` special case.
- Using floating-point `pow()` which loses precision for large numbers.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests the "binary search on answer space" paradigm, which is a powerful general technique. Also tests overflow handling.
- **Follow-ups:**
  - "Find the floor of the nth root (not exact)." (Change the return condition: track the last `lo` value before exceeding m.)
  - "Compute square root without using math library." (Special case n=2, same binary search.)
  - "What about fractional nth roots?" (Use floating-point binary search with epsilon tolerance.)
- **Communication tip:** Clearly state: "I'm doing binary search on the answer space [1, m], checking if mid^n == m." Mention overflow handling proactively.

---

## CONNECTIONS

| Related Problem               | How It Connects                                   |
|-------------------------------|--------------------------------------------------|
| Sqrt(x) (LC #69)             | Special case with n=2                            |
| Pow(x, n) (LC #50)           | Computing x^n efficiently -- needed as subroutine|
| Koko Eating Bananas (LC #875)| Another "binary search on answer space" problem  |
| Capacity to Ship (LC #1011)  | Same paradigm: binary search on possible answers |

---

## Real-World Use Case
**Scientific computing / Cryptography:** Computing integer roots is fundamental in RSA key generation (checking if a number is a perfect power) and in numerical algorithms. Binary search on answer space is used broadly in optimization problems where the objective function is monotonic (e.g., finding the minimum speed, capacity, or threshold that satisfies a constraint).
