# Count Total Set Bits from 1 to N

> **Step 08.8.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a positive integer `N`, find the **total count of set bits** (1s) in the binary representations of all integers from `1` to `N` (inclusive).

### Examples

| # | N | Binary representations | Total set bits |
|---|---|----------------------|----------------|
| 1 | 1 | 1 | 1 |
| 2 | 3 | 1, 10, 11 | 1+1+2 = 4 |
| 3 | 4 | 1, 10, 11, 100 | 1+1+2+1 = 5 |
| 4 | 7 | 1,10,11,100,101,110,111 | 1+1+2+1+2+2+3 = 12 |
| 5 | 8 | ...above + 1000 | 12+1 = 13 |

### Constraints
- `1 <= N <= 10^9`
- Result fits in a 64-bit integer

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Time |
|----------|-----------|------|
| Brute Force | Count bits per number, sum all | O(n log n) |
| Optimal | Recursive formula using highest power of 2 | O(log^2 n) |
| Best | Iterative version of the same formula | O(log n) |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Count Per Number

**Intuition:** Simply iterate every number from 1 to N and count its set bits. Use Brian Kernighan's trick: `x &= (x-1)` clears the lowest set bit, so the number of iterations per number equals its popcount.

**Steps:**
1. Initialize `total = 0`.
2. For `i` from `1` to `N`:
   - While `i != 0`: `total++`, `i &= (i-1)`.
3. Return `total`.

**Dry-Run Trace** (N=4):
```
i=1: 1 -> total=1
i=2: 10 -> 10&01=00 -> total=2
i=3: 11 -> 11&10=10 -> 10&01=00 -> total=4
i=4: 100 -> 100&011=000 -> total=5
```

| Metric | Value |
|--------|-------|
| Time   | O(n log n) -- each number needs O(log n) iterations |
| Space  | O(1) |

---

### Approach 2: Optimal -- Recursive Formula

**Intuition:** Observe the beautiful pattern in set bit counts:

```
Numbers 0..1  (2^1 - 1): 0,1       -> 1 set bit  = 1 * 2^0
Numbers 0..3  (2^2 - 1): 0,1,2,3   -> 4 set bits = 2 * 2^1
Numbers 0..7  (2^3 - 1): 0..7      -> 12 set bits = 3 * 2^2
Numbers 0..2^k-1:                  = k * 2^(k-1)
```

For any N = 2^k + r (where 0 <= r < 2^k):
- Contribution from 1..2^k-1: `k * 2^(k-1)` (full block formula)
- Contribution from MSB (bit k): it's set for all numbers from 2^k to N, that's `r+1` numbers
- Contribution from lower bits in 2^k..N: numbers 2^k to 2^k+r have lower bits 0..r, same as lower bits of 0..r

So: `total(N) = k * 2^(k-1) + (r+1) + total(r)`

**Steps:**
1. Find `k = floor(log2(N))`, `power = 2^k`, `r = N - power`.
2. If `N == power - 1`: return `k * power/2` (exact formula).
3. Else: return `k * power/2 + (r+1) + total(r)`.

**Dry-Run Trace** (N=6):
```
k=2, power=4, r=2
total(6) = 2*2 + 3 + total(2)
total(2): k=1, power=2, r=0
  total(2) = 1*1 + 1 + total(0) = 2
total(6) = 4 + 3 + 2 = 9  ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(log^2 n) -- O(log n) recursive calls, each O(log n) for bit_length |
| Space  | O(log n) recursion stack |

---

### Approach 3: Best -- Iterative Formula

**Intuition:** The recursive approach from Approach 2, converted to an iterative loop. At each iteration, we peel off the contribution of the highest bit of the current `n` and continue with the remainder `r`. This eliminates recursion overhead and achieves true O(log n).

**Steps:**
1. While `n > 0`:
   - Find `k = floor(log2(n))`, `power = 2^k`, `r = n - power`.
   - Add `k * (power/2)` to total (full block 1..2^k-1).
   - Add `r+1` to total (MSB column contribution).
   - Set `n = r`.
2. Return `total`.

**Dry-Run Trace** (N=7):
```
n=7: k=2, power=4, r=3
  total += 2*2=4 (block) + 4 (r+1=4) = 8; n=3
n=3: k=1, power=2, r=1
  total += 1*1=1 (block) + 2 (r+1=2) = 3; n=1
n=1: k=0, power=1, r=0
  total += 0*0=0 (block) + 1 (r+1=1) = 1; n=0
total = 8+3+1 = 12  ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(log n) -- one iteration per bit of N |
| Space  | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why the formula works:** In the range 0..2^k-1, each bit position i (0..k-1) alternates between 0s and 1s in blocks of 2^i. So bit position i is set in exactly half of the 2^k numbers = 2^(k-1) numbers. Summing over all k bit positions gives k * 2^(k-1) total set bits.

**Why O(log n):** N has at most log2(N)+1 bits. The iterative loop removes the MSB of N in each iteration, so it runs at most log2(N) times.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| N=1 | 1 | Base case; formula: k=0, power=1, r=0, result=0+1+0=1 |
| N=2^k (exact power) | formula value | r=0, MSB contribution = 1, no lower bits |
| N=2^k - 1 | k * 2^(k-1) | Full block, use formula directly |
| N=0 | 0 | Empty range |
| Large N (~10^9) | Large value | Need 64-bit integers; Java `long`, Python auto-bigint |

**Common Mistakes:**
- Integer overflow: `k * 2^(k-1)` can overflow 32-bit int for large k; use `long` in Java.
- Off-by-one: formula covers `1..2^k-1` (not including 0, since 0 has no set bits).
- Forgetting the MSB contribution `(r+1)` after finding the full block.
- Using `2^k` instead of `2^(k-1)` in the formula.

---

## 6. REAL-WORLD USE CASE

**Network packet counting:** In network monitoring, counting set bits across ranges of IP addresses (or port numbers) helps identify broadcast domain sizes, subnet utilization, and traffic load balancing patterns. The O(log n) formula is essential when the range spans millions of addresses.

**Hamming weight statistics:** In error-correction codes, knowing the total Hamming weight of all codewords in a range [1..N] is needed for code distance analysis and error probability calculations.

---

## 7. INTERVIEW TIPS

- **Always start with Brute Force** and mention Brian Kernighan's bit trick to show bit manipulation awareness.
- **Transition smoothly:** "I notice a pattern -- all numbers 0..2^k-1 contribute exactly k*2^(k-1) set bits. Let me use that."
- **Handle overflow** explicitly: mention `long` in Java or note Python uses arbitrary precision.
- **The iterative version (Best)** is the cleanest to code in an interview -- no recursion edge cases.
- **Verify with small examples:** N=3 -> 4, N=7 -> 12 are easy to verify mentally.
- **Follow-up:** "Count set bits in [L, R]" -> answer is `f(R) - f(L-1)` where `f(n) = total set bits in 1..n`.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Count Set Bits in Single Number | Building block for brute force |
| LeetCode 338: Counting Bits | Count set bits for each 0..n separately (DP approach) |
| Sum of Bit Differences in Array | Similar bit-position analysis |
| Brian Kernighan's Bit Trick | Used in brute force; x &= x-1 clears lowest set bit |
| Hamming Distance | Set bits in XOR of two numbers |

---

## Real-World Use Case

Network routers use bit-counting operations to calculate subnet masks and count active IP addresses in CIDR blocks. Database engines like ClickHouse use population count (popcount) instructions for bitmap index queries, enabling fast cardinality estimation over billions of rows.
