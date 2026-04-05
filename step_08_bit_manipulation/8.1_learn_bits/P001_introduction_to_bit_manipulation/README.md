# Introduction to Bit Manipulation

> **Batch 1 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Implement the following fundamental bit operations on a 32-bit integer `n` at bit position `i` (0-indexed from the right):
1. **Get Bit:** Return the value of the ith bit (0 or 1).
2. **Set Bit:** Set the ith bit to 1 and return the result.
3. **Clear Bit:** Clear the ith bit to 0 and return the result.
4. **Toggle Bit:** Flip the ith bit and return the result.
5. **Check Power of 2:** Return true if `n` is a power of 2.

**Constraints:**
- `0 <= n <= 2^31 - 1`
- `0 <= i <= 30`

**Examples:**

| Operation | Input | Output | Explanation |
|-----------|-------|--------|-------------|
| Get Bit | `n=13 (1101), i=2` | `1` | Bit at position 2 is 1 |
| Get Bit | `n=13 (1101), i=1` | `0` | Bit at position 1 is 0 |
| Set Bit | `n=9 (1001), i=2` | `13 (1101)` | Setting bit 2 turns 1001 into 1101 |
| Clear Bit | `n=13 (1101), i=2` | `9 (1001)` | Clearing bit 2 turns 1101 into 1001 |
| Toggle Bit | `n=13 (1101), i=1` | `15 (1111)` | Toggling bit 1 turns 1101 into 1111 |
| Power of 2 | `n=16 (10000)` | `true` | 16 = 2^4 |
| Power of 2 | `n=18 (10010)` | `false` | Not a power of 2 |

### Real-Life Analogy
> *Think of an integer as a row of light switches, each either ON (1) or OFF (0). "Get bit" is peeking at one switch. "Set bit" is flipping a switch ON. "Clear bit" is forcing a switch OFF. "Toggle bit" is flipping whatever state the switch is in. "Power of 2" checks if exactly one switch is ON in the entire row -- a number like 10000 has only one light on.*

### Key Observations
1. Left-shifting 1 by `i` positions creates a **mask** with only the ith bit set: `1 << i`. This mask is the universal tool for all bit operations.
2. AND extracts bits, OR sets bits, XOR toggles bits, AND-with-complement clears bits.
3. A power of 2 in binary has exactly one 1-bit, so `n & (n-1) == 0`. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No data structure needed. We work directly on the integer's binary representation using **bitwise operators**.
- Bitwise operations execute in O(1) -- a single CPU instruction.

### Pattern Recognition
- **Pattern:** Bit Masking
- **Classification Cue:** "Whenever you see _manipulate individual bits_ or _flags/permissions_ --> think _create a mask with shift, then combine with AND/OR/XOR_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- String Conversion
**Idea:** Convert the number to a binary string, manipulate the character at the desired position, convert back.

**Steps (Get Bit example):**
1. Convert `n` to binary string: `bin(n)` --> `"0b1101"`.
2. Pad to 32 characters.
3. Read the character at position `31 - i`.
4. Return `int(char)`.

**Why we move on:** **Unnecessary work.** String conversion allocates memory and iterates over all bits. Bitwise operators do this in a single CPU cycle.

| Time | Space |
|------|-------|
| O(32) = O(1) | O(32) = O(1) for the string |

### Approach 2: Optimal -- Bitwise Mask Operations
**What changed:** Use a bitmask `1 << i` and combine with the appropriate bitwise operator.

**Steps:**

**Get Bit:** `(n >> i) & 1` -- shift the ith bit to position 0, then AND with 1.

**Set Bit:** `n | (1 << i)` -- OR with mask forces bit i to 1, others unchanged.

**Clear Bit:** `n & ~(1 << i)` -- AND with inverted mask forces bit i to 0, others unchanged.

**Toggle Bit:** `n ^ (1 << i)` -- XOR with mask flips bit i, others unchanged.

**Power of 2:** `n > 0 && (n & (n - 1)) == 0` -- subtracting 1 flips all bits below the lowest set bit. If n is a power of 2, there is only one set bit, so AND gives 0.

**Dry Run: Get Bit** `n = 13 (1101), i = 2`

| Step | Operation | Binary | Result |
|------|-----------|--------|--------|
| n | - | 1101 | 13 |
| n >> 2 | Right shift by 2 | 0011 | 3 |
| 3 & 1 | AND with 1 | 0001 | 1 |

**Dry Run: Power of 2** `n = 16 (10000)`

| Step | Operation | Binary | Result |
|------|-----------|--------|--------|
| n | - | 10000 | 16 |
| n - 1 | - | 01111 | 15 |
| n & (n-1) | AND | 00000 | 0 |
| Result | 0 == 0 | - | true |

| Time | Space |
|------|-------|
| O(1) | O(1) |

### Approach 3: Best -- Same as Optimal
**Note:** Bitwise operations are already the most efficient approach. There is no further optimization possible -- these are single-instruction operations. The three "classes" below demonstrate alternative equivalent expressions for completeness.

| Time | Space |
|------|-------|
| O(1) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) -- "Each operation is a constant number of bitwise instructions, regardless of the magnitude of n."
**Space:** O(1) -- "No extra memory allocated."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting that bit positions are 0-indexed from the right (LSB = position 0).
2. Not checking `n > 0` for power-of-2: `n = 0` satisfies `n & (n-1) == 0` but 0 is not a power of 2.
3. In Java, using `~` on an int produces a 32-bit complement. Be careful with negative numbers if working with signed ints.
4. Shifting by more than 31 in a 32-bit integer leads to undefined/wrapped behavior in some languages.

### Edge Cases to Test
- [ ] `n = 0` -- all bits are 0, power-of-2 is false
- [ ] `n = 1` -- power of 2, bit 0 is set
- [ ] `i = 0` -- least significant bit
- [ ] `i = 30` -- near the sign bit for 32-bit int
- [ ] Already-set bit for set operation (should be no-op)
- [ ] Already-clear bit for clear operation (should be no-op)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need to implement get/set/clear/toggle on individual bits, plus a power-of-2 check."
2. **Match:** "Bit masking pattern -- create mask with `1 << i`, combine with AND/OR/XOR."
3. **Plan:** "For each operation: shift to create mask, apply the right bitwise operator."
4. **Implement:** Write each function as a one-liner.
5. **Review:** Trace get-bit for n=13, i=2.
6. **Evaluate:** "All O(1). These are CPU-level primitives."

### Follow-Up Questions
- "How do you count the number of set bits?" --> Brian Kernighan's algorithm: repeatedly do `n = n & (n-1)`, count iterations.
- "How do you find the lowest set bit?" --> `n & (-n)` isolates the lowest set bit.
- "What are practical uses?" --> Permissions (Unix file modes), feature flags, compression, networking (subnet masks).

---

## CONNECTIONS
- **Prerequisite:** Understanding of binary number system
- **Same Pattern:** Check Ith Bit (P002), Count Set Bits, Single Number (LC #136)
- **Harder Variant:** Bitwise AND of Numbers Range (LC #201), Maximum XOR (LC #421)
- **This Unlocks:** All bit manipulation problems, understanding of XOR tricks, subset generation with bitmasks
