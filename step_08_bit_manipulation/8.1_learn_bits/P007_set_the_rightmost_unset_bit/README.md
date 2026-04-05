# Set the Rightmost Unset Bit

> **Batch 4 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a non-negative integer `n`, set the **rightmost 0-bit** (the least significant bit that is 0) to 1 and return the result. If all bits are already 1 (e.g., `n = 7 = 111`), return `n` unchanged.

### Examples

| Input (decimal) | Binary   | Output (decimal) | Binary   | Explanation                        |
|-----------------|----------|------------------|----------|------------------------------------|
| `6`             | `110`    | `7`              | `111`    | Rightmost 0 is at position 0, set it |
| `9`             | `1001`   | `11`             | `1011`   | Rightmost 0 is at position 1, set it |
| `15`            | `1111`   | `15`             | `1111`   | All bits set, no change            |
| `0`             | `0`      | `1`              | `1`      | Rightmost 0 is at position 0       |
| `10`            | `1010`   | `11`             | `1011`   | Rightmost 0 is at position 0       |

### Analogy
Imagine a row of checkboxes where some are checked (1) and some are unchecked (0). You want to check the first unchecked box from the right. The trick is finding that first unchecked box efficiently.

### 3 Key Observations
1. **"Aha" -- `~n` flips all bits.** The rightmost 0 in `n` becomes the rightmost 1 in `~n`. We can then isolate it.
2. **"Aha" -- `~n & (n+1)` or `n | (n+1)` gives the answer.** `n+1` flips the rightmost 0 and all trailing 1s. ORing `n` with `n+1` sets that rightmost 0 while preserving everything else.
3. **"Aha" -- Handle all-ones edge case.** If `n = 2^k - 1` (all bits are 1), there is no 0-bit to set. Check this or let `n | (n+1)` naturally handle it (it adds a new bit, which may or may not be desired).

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm                 | Why?                                  |
|------------------|---------------|---------------------------|----------------------------------------|
| Brute Force      | None          | Iterate bit positions     | Check each bit from LSB, set first 0  |
| Optimal          | None          | `n | (n+1)`               | Single expression, O(1)               |
| Best             | None          | `n | (~n & (n+1))`        | Explicit isolation of rightmost 0 bit |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Bit-by-Bit Scan
**Intuition:** Walk through each bit position from the least significant. Find the first 0-bit, set it, and return.

**Steps:**
1. For each bit position `pos` from 0 to 31:
   - If bit at position `pos` is 0 (i.e., `(n >> pos) & 1 == 0`):
     - Set that bit: `n = n | (1 << pos)`.
     - Return `n`.
2. If no 0-bit found (all 1s), return `n`.

**Dry-Run Trace** with `n = 9` (binary `1001`):
```
pos=0: (1001 >> 0) & 1 = 1 (set, skip)
pos=1: (1001 >> 1) & 1 = 0 (unset! found it)
       n = 1001 | 0010 = 1011 = 11
Answer: 11
```

| Metric | Value                              |
|--------|------------------------------------|
| Time   | O(32) = O(1) worst case            |
| Space  | O(1)                               |

---

### Approach 2: Optimal -- `n | (n + 1)`
**Intuition:** Adding 1 to `n` flips the rightmost 0 to 1 and resets all trailing 1s to 0. ORing with `n` restores those trailing 1s while keeping the newly set bit.

**Why does this work?**
- `n = ...X0111` (X is some prefix, rightmost 0 followed by trailing 1s)
- `n+1 = ...X1000` (rightmost 0 flipped to 1, trailing 1s become 0)
- `n | (n+1) = ...X1111` (all bits from the prefix preserved, rightmost 0 now 1, trailing 1s preserved from n)

**Steps:**
1. If all bits are set (e.g., `(n & (n+1)) == 0` and `n > 0`), return `n`.
2. Return `n | (n + 1)`.

**Dry-Run Trace** with `n = 6` (binary `110`):
```
n   = 110
n+1 = 111
n | (n+1) = 111 = 7
Answer: 7
```

With `n = 9` (binary `1001`):
```
n   = 1001
n+1 = 1010
n | (n+1) = 1011 = 11
Answer: 11
```

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Isolate with `~n & (n + 1)`
**Intuition:** `~n` flips all bits, making the rightmost 0 into a 1. `(n+1)` also has that bit set. `~n & (n+1)` isolates just the rightmost 0-bit position (as a power of 2). Then OR it into `n`.

**Steps:**
1. Compute `mask = ~n & (n + 1)`. This is a single bit: the rightmost 0 position.
2. Return `n | mask`.

**Dry-Run Trace** with `n = 9` (binary `1001`):
```
~n     = ...0110
n+1    = ...1010
~n&(n+1) = ...0010  (isolated the rightmost 0 at position 1)
n | mask = 1001 | 0010 = 1011 = 11
```

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1)  |

---

## COMPLEXITY INTUITIVELY

- **Why O(1):** All approaches work on fixed-width integers (32 or 64 bits). Even the "brute force" scan is bounded by the word size.
- **Approaches 2 and 3 are O(1) operations count:** Literally 2-3 bitwise operations. No loops.

---

## EDGE CASES & MISTAKES

| Edge Case            | What Happens                                          |
|----------------------|------------------------------------------------------|
| `n = 0` (all zeros)  | Rightmost 0 is at position 0. Result = 1.           |
| `n = 15` (1111)      | All bits set. `n|(n+1)` = `15|16` = 31 = `11111`. Note: this ADDS a new bit. If the problem says "return unchanged," check first. |
| `n = 1` (01)         | Rightmost 0 is at position 1. Result = 3 = `11`.    |
| Negative numbers      | Depends on representation. Most problems use non-negative. |

**Common Mistakes:**
- Confusing "rightmost unset" with "rightmost set" (which uses `n & (-n)`).
- Not handling the all-ones case when the problem requires returning `n` unchanged.
- Forgetting that `n | (n+1)` may increase the bit width (e.g., 7 -> 15).

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests understanding of bit tricks and the relationship between `n`, `n+1`, `~n`, and common bit idioms.
- **Follow-ups:**
  - "Clear the rightmost set bit." (`n & (n-1)` -- Brian Kernighan's trick.)
  - "Isolate the rightmost set bit." (`n & (-n)` or `n & ~(n-1)`.)
  - "Toggle the rightmost set bit." (`n ^ (n & (-n))`.)
- **Communication tip:** Draw out the binary representation for a small example and show how `n+1` flips the rightmost 0 and trailing 1s. This makes the OR trick self-evident.

---

## CONNECTIONS

| Related Problem                 | How It Connects                                |
|--------------------------------|------------------------------------------------|
| Count Set Bits (LC #191)       | Uses `n & (n-1)` -- the "clear" counterpart    |
| Power of Two (LC #231)         | `n & (n-1) == 0` checks single set bit         |
| Rightmost Set Bit Position     | `n & (-n)` isolates rightmost set bit          |
| Number Complement (LC #476)    | Uses `~n` to flip all bits                     |

---

## Real-World Use Case
**Memory allocators / Bitmap allocation:** In OS kernels and memory managers, bitmaps track free/allocated blocks. "Set the rightmost unset bit" directly corresponds to "allocate the first available block." The O(1) bit-trick version is used in high-performance allocators like buddy systems.
