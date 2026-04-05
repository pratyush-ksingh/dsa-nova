# Count Set Bits

> **Batch 4 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an unsigned integer `n`, return the number of **set bits** (1-bits) in its binary representation. Also known as the **Hamming weight** or **popcount**.

**LeetCode #191**

### Examples

| Input        | Binary             | Output | Explanation                      |
|-------------|--------------------|--------|----------------------------------|
| `11`        | `1011`             | 3      | Three 1-bits                     |
| `128`       | `10000000`         | 1      | Only the leading bit is set      |
| `255`       | `11111111`         | 8      | All 8 bits are set               |
| `0`         | `0`                | 0      | No bits set                      |

### Analogy
Imagine a row of light switches. Some are ON (1) and some are OFF (0). You want to count how many are ON. The naive way is to check each switch one by one. The clever way (Brian Kernighan's) is to turn off one switch at a time -- each "turn off" operation tells you exactly one switch was ON.

### 3 Key Observations
1. **"Aha" -- `n & 1` extracts the last bit.** Right-shifting by 1 and checking the last bit is the basic approach. Process all 32 bits (or until n becomes 0).
2. **"Aha" -- `n & (n-1)` clears the rightmost set bit.** This is Brian Kernighan's trick. Subtracting 1 flips all bits from the rightmost 1 onwards. ANDing with `n` clears that rightmost 1. Each operation removes exactly one set bit, so we loop only `k` times where `k` = number of set bits.
3. **"Aha" -- Built-in popcount exists.** Java has `Integer.bitCount()`, Python has `bin(n).count('1')`. Know these exist, but be ready to implement manually.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm              | Why?                                    |
|------------------|---------------|------------------------|-----------------------------------------|
| Brute Force      | None          | Check each bit         | O(32) = O(1), simple loop               |
| Optimal          | None          | Brian Kernighan's      | O(k) where k = set bits, faster average |
| Best             | Lookup table  | Precomputed nibble/byte| O(1) with 256-entry table, constant     |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check Each Bit
**Intuition:** Check each of the 32 bit positions one by one. Extract the last bit with `n & 1`, count it, then shift `n` right.

**Steps:**
1. Initialize `count = 0`.
2. While `n > 0`:
   - If `n & 1 == 1`: increment `count`.
   - Right-shift `n` by 1: `n >>= 1` (or use `n >>>= 1` in Java for unsigned).
3. Return `count`.

**Dry-Run Trace** with `n = 11` (binary `1011`):
```
n=1011: n&1=1, count=1, n>>1=101
n=0101: n&1=1, count=2, n>>1=010
n=0010: n&1=0, count=2, n>>1=001
n=0001: n&1=1, count=3, n>>1=000
n=0: stop
Answer: 3
```

| Metric | Value                              |
|--------|------------------------------------|
| Time   | O(32) = O(1) for 32-bit integers   |
| Space  | O(1)                               |

---

### Approach 2: Optimal -- Brian Kernighan's Algorithm
**Intuition:** The trick `n & (n-1)` clears the rightmost set bit. Each iteration removes exactly one 1-bit, so we loop only as many times as there are set bits. For sparse numbers (few 1s), this is much faster.

**Why does `n & (n-1)` work?**
- `n-1` flips all bits from the rightmost 1 to the end.
- Example: `n = 1010`, `n-1 = 1001`. `n & (n-1) = 1000`. The rightmost 1 (at position 1) is gone.

**Steps:**
1. Initialize `count = 0`.
2. While `n > 0`:
   - `n = n & (n - 1)` (clear rightmost set bit).
   - Increment `count`.
3. Return `count`.

**Dry-Run Trace** with `n = 11` (binary `1011`):
```
n=1011: n&(n-1) = 1011 & 1010 = 1010, count=1
n=1010: n&(n-1) = 1010 & 1001 = 1000, count=2
n=1000: n&(n-1) = 1000 & 0111 = 0000, count=3
n=0: stop
Answer: 3  (only 3 iterations instead of 4!)
```

| Metric | Value                                |
|--------|--------------------------------------|
| Time   | O(k) where k = number of set bits    |
| Space  | O(1)                                 |

---

### Approach 3: Best -- Lookup Table
**Intuition:** Precompute the popcount for every possible byte value (0-255). Then split the 32-bit integer into 4 bytes and sum up the precomputed values. This gives true O(1) with a small constant.

**Steps:**
1. Build a table of 256 entries: `table[i] = number of 1-bits in i`.
2. For the input `n`:
   - `count = table[n & 0xFF] + table[(n >> 8) & 0xFF] + table[(n >> 16) & 0xFF] + table[(n >> 24) & 0xFF]`
3. Return `count`.

| Metric | Value                                        |
|--------|----------------------------------------------|
| Time   | O(1) -- exactly 4 lookups                    |
| Space  | O(256) = O(1) for the precomputed table      |

---

## COMPLEXITY INTUITIVELY

- **Why O(k) beats O(32):** For `n = 1073741824` (only 1 bit set), Kernighan's does 1 iteration versus 31 for the bit-scan approach. On average for random 32-bit numbers, k ~ 16, so it is roughly 2x faster.
- **Why the lookup table is O(1):** Fixed number of table lookups (4), regardless of the input.

---

## EDGE CASES & MISTAKES

| Edge Case         | What Happens                                     |
|-------------------|--------------------------------------------------|
| `n = 0`           | Return 0. No bits set.                           |
| `n = 1`           | Return 1.                                        |
| `n = 2^31 - 1`    | All 31 bits set, return 31.                      |
| Negative numbers   | In Java, use `>>>` (unsigned right shift) to avoid infinite loop with `>>`. |

**Common Mistakes:**
- Using signed right shift `>>` instead of unsigned `>>>` in Java (negative numbers get stuck in infinite loop).
- Forgetting that `n & (n-1)` modifies `n` -- no separate shift needed.
- Not handling the `n = 0` case (though all approaches handle it naturally).

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests bit manipulation fundamentals. Brian Kernighan's trick is a classic "do you know the trick?" question.
- **Follow-ups:**
  - "Count bits for every number from 0 to n." (LC #338 -- DP with `dp[i] = dp[i & (i-1)] + 1`.)
  - "What's the Hamming distance between two integers?" (XOR them first, then count set bits.)
  - "Is the number a power of 2?" (`n & (n-1) == 0` and `n > 0`.)
- **Communication tip:** Show you know Kernighan's trick, explain WHY `n & (n-1)` clears the rightmost set bit with a concrete example.

---

## CONNECTIONS

| Related Problem              | How It Connects                                  |
|------------------------------|--------------------------------------------------|
| Power of Two (LC #231)       | Uses `n & (n-1) == 0` -- one set bit means power of 2 |
| Hamming Distance (LC #461)   | XOR + popcount                                   |
| Counting Bits (LC #338)      | Popcount for range [0, n] using DP               |
| Reverse Bits (LC #190)       | Another bit-by-bit processing pattern            |

---

## Real-World Use Case
**Networking & Error Detection:** Counting set bits is used in checksum algorithms, parity checks, and population counts in bitmap indexes (databases). CPUs have dedicated `POPCNT` instructions because this operation is so common in systems programming, cryptography, and data compression.
