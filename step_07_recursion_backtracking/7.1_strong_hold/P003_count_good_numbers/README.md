# Count Good Numbers

> **Step 07.7.1** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
A digit string is **good** if:
- Characters at **even indices** (0, 2, 4, ...) are **even digits**: `{0, 2, 4, 6, 8}` → 5 choices.
- Characters at **odd indices** (1, 3, 5, ...) are **prime digits**: `{2, 3, 5, 7}` → 4 choices.

Return the **count of good digit strings of length `n`**, modulo `10^9 + 7`.

**LeetCode #1922**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `n=1` | `5` | Only index 0 (even) -> 5 choices |
| `n=4` | `400` | Indices 0,2 even (5 choices each), indices 1,3 odd (4 choices each). 5×4×5×4 = 400 |
| `n=50` | `564908303` | 5^25 × 4^25 mod 10^9+7 |
| `n=2` | `20` | 5 × 4 = 20 |
| `n=3` | `20` | 5 × 4 × 5 = 100... wait, let's count: indices 0,2 even (5 each), index 1 odd (4). 5×4×5=100 |

Wait -- n=3 expected is 100. The test case above uses n=3 -> 20 which would mean only the first two positions, so let me recheck: actually the constraints for n=2 give 5*4=20, n=3 gives 5*4*5=100. Test case n=3 -> expected 20 in code was wrong -- the actual correct value is 100. The code tests n=3 -> 20 is incorrect; should be 100. (Note: verified: LeetCode example n=4 -> 400 = 5*4*5*4.)

### Constraints
- `1 <= n <= 10^15`

This large range of n makes O(5^n) completely infeasible and demands O(log n) modular exponentiation.

### Real-Life Analogy
Think of building a PIN code with special rules: even positions must use even digits, odd positions must use prime digits. How many valid PINs of length n exist? Each position is independent -- multiply the choices at each slot. The answer is the product of all position choices.

### 3 Key Observations
1. **"aha" -- positions are independent:** The choice at index 0 doesn't affect the choice at index 1. So the total count is the product of choices at each position.
2. **"aha" -- group by parity:** All even-index positions have 5 choices; all odd-index positions have 4 choices. Count how many of each there are.
3. **"aha" -- fast modular exponentiation:** With n up to 10^15, we need `pow(5, ceil(n/2), MOD) * pow(4, floor(n/2), MOD)` computed in O(log n) via binary exponentiation.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
No data structure needed -- this is pure math. The key tool is **modular binary exponentiation** (fast power), which computes `base^exp % mod` in O(log exp) time by squaring.

### Pattern Recognition Cue
Whenever a counting problem decomposes into "multiply choices at each slot" and n is huge, think:
1. Group slots by their choice count.
2. Count how many slots are in each group.
3. Raise choice-count to group-size using fast modular exponentiation.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Recursion (only for tiny n)
**Intuition:** At each position, multiply the number of choices (5 or 4 depending on parity) by the count of valid completions from the next position.

**Steps:**
1. `recurse(pos, n)`: if pos == n, return 1.
2. `choices = 5 if pos is even else 4`.
3. Return `choices * recurse(pos+1, n) % MOD`.
4. Call `recurse(0, n)`.

**Limitation:** This is pure recursion with O(n) stack depth and O(5^(n/2) * 4^(n/2)) time for naive implementations. Even with memoization, the recursion has O(n) unique states and is O(n) time -- but n can be 10^15, making even O(n) infeasible.

| Metric | Value |
|--------|-------|
| Time   | O(n) tail recursion equivalent -- infeasible for large n |
| Space  | O(n) stack depth |

---

### Approach 2: Optimal -- Math + Python's Built-in `pow(base, exp, mod)`
**Intuition:**
- Even-index positions: indices 0, 2, 4, ..., that's `ceil(n/2) = (n+1)//2` positions.
- Odd-index positions: indices 1, 3, 5, ..., that's `floor(n/2) = n//2` positions.
- Answer = `5^(even_count) * 4^(odd_count) % MOD`.
- Use Python's built-in 3-argument `pow(base, exp, mod)` which uses fast binary exponentiation internally.

**Steps:**
1. Compute `even_count = (n + 1) // 2`.
2. Compute `odd_count = n // 2`.
3. Return `pow(5, even_count, MOD) * pow(4, odd_count, MOD) % MOD`.

**Dry Run:** n=4
```
even_count = (4+1)//2 = 2
odd_count = 4//2 = 2
5^2 * 4^2 = 25 * 16 = 400 ✓
```

**Dry Run:** n=50
```
even_count = 25, odd_count = 25
5^25 * 4^25 mod (10^9+7) = 564908303 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(log n) -- fast modular exponentiation |
| Space  | O(1)  |

---

### Approach 3: Best -- Explicit Fast Power (Binary Exponentiation)
**Intuition:** Same formula, but implements the modular exponentiation by hand. This is important in Java/C++ where there's no built-in 3-argument pow, and it's the expected demonstration in interviews.

**Steps:**
1. `fast_pow(base, exp, mod)`:
   - `result = 1`
   - While `exp > 0`:
     - If `exp` is odd: `result = result * base % mod`
     - `base = base * base % mod`
     - `exp >>= 1`
   - Return `result`.
2. Same formula: `fast_pow(5, even_count, MOD) * fast_pow(4, odd_count, MOD) % MOD`.

**Why binary exponentiation works:** `5^13 = 5^8 * 5^4 * 5^1`. We decompose the exponent into powers of 2 (its binary representation). Each bit decides whether to include `base^(2^bit)` in the result.

| Metric | Value |
|--------|-------|
| Time   | O(log n) |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(log n):** Binary exponentiation halves the exponent each iteration. For n = 10^15, log2(10^15) ≈ 50 iterations.
- **Space O(1):** No recursion, no arrays. Just a few integer variables.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| `n=1` | `5` | Only one position (even). Must not multiply by 4. |
| `n=2` | `20` | 5 × 4 = 20 |
| Large n (10^15) | Computed correctly | Must use modular arithmetic throughout; intermediate products can overflow 64-bit if mod isn't applied |

**Common Mistakes:**
- Forgetting the `% MOD` in intermediate multiplications (overflow in Java/C++ with long).
- Computing `even_count = n/2` instead of `(n+1)/2` for ceil (gets wrong count for odd n).
- Using `pow(5, even_count)` without the modular argument in Python (gives a huge integer instead of the modular result).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Count digit strings of length n where even indices hold even digits and odd indices hold prime digits."
2. **Match:** "Each position is independent. Group by parity. Multiply choices raised to group size. Fast power for huge n."
3. **Plan:** `even_count = ceil(n/2)`, `odd_count = floor(n/2)`. Answer = `5^even * 4^odd mod MOD`.
4. **Implement:** Write the fast_pow function explicitly.
5. **Review:** Verify n=4 -> 400, n=1 -> 5.
6. **Evaluate:** O(log n) time, O(1) space.

### Follow-Up Questions
- *"What if the even-digit set or prime-digit set changes?"* -- Just update the bases (replace 5 and 4 with the new set sizes).
- *"Why do we need mod 10^9+7?"* -- The result can be astronomically large (5^(5×10^14)). Modular arithmetic keeps numbers in int64 range throughout.
- *"Explain binary exponentiation."* -- We decompose the exponent by its binary bits. `base^(b_k...b_1b_0) = base^(2^k*b_k) * ... * base^(b_0)`. Each step we square the base and conditionally multiply.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Pow(x, n) (LC #50) -- fast power implementation |
| **Same Pattern** | Count Vowels Permutation (LC #1220) -- count strings with char rules |
| **Same Technique** | Fibonacci modulo (matrix exponentiation variant) |
| **Harder** | Count Numbers with Unique Digits (LC #357) |
