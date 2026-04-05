# Remove Last Set Bit

> **Batch 3 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a non-negative integer `n`, turn off (clear) the **rightmost set bit** (the lowest-order bit that is `1`) and return the resulting number.

**Constraints:**
- `0 <= n <= 2^31 - 1`

**Examples:**

| Input | Binary | Output | Binary Result | Explanation |
|-------|--------|--------|---------------|-------------|
| `n = 12` | `1100` | `8` | `1000` | Rightmost set bit at position 2 is cleared |
| `n = 7` | `0111` | `6` | `0110` | Rightmost set bit at position 0 is cleared |
| `n = 16` | `10000` | `0` | `00000` | Only one set bit, clearing it gives 0 |
| `n = 0` | `0000` | `0` | `0000` | No set bit to clear |

### Real-Life Analogy
> *Imagine a row of light switches, some ON and some OFF. You want to turn off the very first switch you encounter that is ON, scanning from the right. If the switches read ON-OFF-ON-ON (binary 1011), you flip the rightmost ON switch to get ON-OFF-ON-OFF (binary 1010). The trick `n & (n-1)` is like a master-reset button that targets exactly that first ON switch from the right.*

### Key Observations
1. Subtracting 1 from `n` flips all bits from the rightmost set bit downward: `n = ...1000` becomes `n-1 = ...0111`. <-- This is the "aha" insight
2. AND-ing `n` with `n-1` therefore zeros out the rightmost set bit while leaving all higher bits unchanged.
3. This operation is called **Brian Kernighan's trick** and is the foundation of many bit manipulation algorithms (counting set bits, checking power of 2, etc.).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- A single bitwise expression `n & (n-1)` does the job in O(1). No data structure is needed.
- The alternative -- finding the bit position, then clearing it -- requires extra steps that the bit trick avoids.

### Pattern Recognition
- **Pattern:** Bit manipulation identity
- **Classification Cue:** "Whenever you see _rightmost set bit_ or _lowest set bit_ --> think of `n & (n-1)` and `n & (-n)`."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Find Position Then Clear
**Idea:** Find the position of the rightmost set bit by checking each bit, then clear it with a mask.

**Steps:**
1. Scan bits from position 0 upward.
2. When you find the first `1` bit (at position `p`), clear it: `n = n ^ (1 << p)`.
3. Return `n`.

**Dry Run:** `n = 12` (binary `1100`)

| Step | pos | bit at pos | Action |
|------|-----|------------|--------|
| 1 | 0 | 0 | skip |
| 2 | 1 | 0 | skip |
| 3 | 2 | 1 | found! n = 12 ^ (1<<2) = 12 ^ 4 = 8 |

**Result:** 8

**BUD Transition -- Unnecessary work:** We are scanning bit by bit when a single expression can do this directly.

| Time | Space |
|------|-------|
| O(log n) worst case (up to 32 iterations) | O(1) |

### Approach 2: Optimal -- Brian Kernighan's Trick
**What changed:** Use the identity `n & (n-1)` which directly clears the rightmost set bit in one operation.

**Why it works:**
- `n` has the form `...XY10...0` (some prefix, then a `1`, then all `0`s to the right).
- `n-1` flips that `1` to `0` and all the `0`s below it to `1`: `...XY01...1`.
- AND-ing: the prefix `...XY` is preserved, the rightmost `1` becomes `0`, and everything below stays `0`.

**Steps:**
1. Return `n & (n - 1)`.

**Dry Run:** `n = 12` (binary `1100`)

| Expression | Binary | Decimal |
|------------|--------|---------|
| n | `1100` | 12 |
| n - 1 | `1011` | 11 |
| n & (n-1) | `1000` | 8 |

**Result:** 8

| Time | Space |
|------|-------|
| O(1) | O(1) |

### Approach 3: Best -- Same as Optimal (Single Expression)
**What changed:** For this problem, the optimal solution IS the best solution. We present it as a one-liner for maximum clarity. In interviews, you can also mention the alternative `n - (n & -n)` which first isolates the rightmost set bit via `n & -n` (two's complement trick) and then subtracts it.

**Alternative expression:** `n - (n & -n)`
- `n & -n` isolates the rightmost set bit (e.g., 12 & -12 = 4, binary `0100`).
- Subtracting it from n clears that bit: 12 - 4 = 8.

| Time | Space |
|------|-------|
| O(1) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) -- "A fixed number of bitwise operations regardless of the magnitude of n."
**Space:** O(1) -- "Only a few variables; no extra memory."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting `n = 0`: `0 & (0-1)` gives `0 & 0xFFFFFFFF` which in unsigned is a huge number. Guard with an `if n == 0` check or rely on language-specific behavior.
2. Confusing `n & (n-1)` (clears rightmost set bit) with `n & (-n)` (isolates rightmost set bit).
3. Assuming the result changes the leftmost bit -- it always affects the rightmost.

### Edge Cases to Test
- [ ] `n = 0` (no set bits)
- [ ] `n = 1` (single bit, result is 0)
- [ ] `n` is a power of 2 (only one set bit, result is 0)
- [ ] `n = Integer.MAX_VALUE` (all bits set except sign)
- [ ] `n` has only the rightmost bit set

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to clear the lowest-order 1-bit in the binary representation of n."
2. **Match:** "This is the classic Brian Kernighan bit trick: `n & (n-1)`."
3. **Plan:** "Subtracting 1 flips the rightmost 1 and all trailing 0s. AND-ing with the original clears just that bit."
4. **Implement:** One-liner. Mention the `n = 0` edge case.
5. **Review:** Walk through `n = 12`: `1100 & 1011 = 1000 = 8`.
6. **Evaluate:** "O(1) time and space. This is optimal -- you cannot do better than a constant number of operations."

### Follow-Up Questions
- "How would you count the number of set bits?" --> Loop `n = n & (n-1)` until `n = 0`, counting iterations. That is Brian Kernighan's algorithm for popcount.
- "How would you check if n is a power of 2?" --> `n > 0 && (n & (n-1)) == 0`. (See next problem P005)

---

## CONNECTIONS
- **Prerequisite:** Understanding binary representation and bitwise AND
- **Same Pattern:** Check Power of 2 (P005), Count Set Bits (Brian Kernighan)
- **Harder Variant:** Counting bits (LC #338), Hamming Distance (LC #461)
- **This Unlocks:** All problems that rely on manipulating individual bits efficiently
