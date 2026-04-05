# Set Clear Toggle ith Bit

> **Batch 2 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an integer `n` and a bit position `i` (0-indexed from the right), implement three operations:
1. **Set** the ith bit (make it 1)
2. **Clear** the ith bit (make it 0)
3. **Toggle** the ith bit (flip it: 0 becomes 1, 1 becomes 0)

**Constraints:**
- `0 <= i <= 30`
- `-2^31 <= n <= 2^31 - 1`

**Examples:**

| Operation | n (decimal) | n (binary) | i | Result (binary) | Result (decimal) |
|-----------|-------------|------------|---|-----------------|------------------|
| Set       | 9           | `1001`     | 2 | `1101`           | 13               |
| Clear     | 13          | `1101`     | 2 | `1001`           | 9                |
| Toggle    | 9           | `1001`     | 2 | `1101`           | 13               |
| Toggle    | 13          | `1101`     | 2 | `1001`           | 9                |

### Real-Life Analogy
> *Think of a row of light switches on a wall, numbered 0, 1, 2, ... from the right. "Set" means flipping a specific switch ON regardless of its current state. "Clear" means forcing a specific switch OFF. "Toggle" means walking up to the switch and flipping it to the opposite of whatever it currently is. Each operation only touches the one switch you are told about -- all others stay exactly as they are.*

### Key Observations
1. Each bit position is independent -- operating on bit `i` must not disturb any other bit.
2. OR with a 1-bit forces a bit to 1, AND with a 0-bit forces a bit to 0, XOR with a 1-bit flips a bit. <-- This is the "aha" insight
3. We can create a mask with exactly one bit set at position `i` using `1 << i`, then combine it with the right bitwise operator.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- Bitwise operations are O(1) single-instruction CPU operations -- no data structure needed.
- The key tool is a **bitmask**: a number with exactly one bit set at the target position, created by `1 << i`.

### Pattern Recognition
- **Pattern:** Bitmask Construction + Bitwise Operator Selection
- **Classification Cue:** "When you see _manipulate a specific bit position_ --> think _create a mask with `1 << i` and pick the right operator (OR / AND / XOR)_"

---

## APPROACH LADDER

### Approach 1: String Conversion (Brute Force)
**Idea:** Convert the number to a binary string, modify the character at position `i`, convert back.

**Steps:**
1. Convert `n` to a binary string.
2. Modify the character at the appropriate index.
3. Convert the string back to an integer.

**Why we move on:** **Unnecessary work** -- string conversion, memory allocation, and parsing are all wasteful when we can do this with a single CPU instruction.

| Time | Space |
|------|-------|
| O(32) = O(1) | O(32) = O(1) for the string |

### Approach 2: Optimal -- Bitwise Operations
**What changed:** Use bitwise operators directly with a mask `1 << i`.

**Steps:**

**Set bit i:**
1. Create mask = `1 << i` (a number with only bit `i` set to 1).
2. Return `n | mask`. OR with 1 forces the bit to 1; OR with 0 leaves other bits unchanged.

**Clear bit i:**
1. Create mask = `1 << i`.
2. Invert it: `~mask` (now every bit is 1 except bit `i` which is 0).
3. Return `n & ~mask`. AND with 0 forces the bit to 0; AND with 1 leaves other bits unchanged.

**Toggle bit i:**
1. Create mask = `1 << i`.
2. Return `n ^ mask`. XOR with 1 flips the bit; XOR with 0 leaves other bits unchanged.

**Dry Run:** Set bit 2 of `n = 9` (binary `1001`)

| Step | Operation | Binary | Decimal |
|------|-----------|--------|---------|
| Start | n = 9 | `1001` | 9 |
| Mask | 1 << 2 | `0100` | 4 |
| OR | 1001 \| 0100 | `1101` | 13 |

**Dry Run:** Clear bit 2 of `n = 13` (binary `1101`)

| Step | Operation | Binary | Decimal |
|------|-----------|--------|---------|
| Start | n = 13 | `1101` | 13 |
| Mask | 1 << 2 | `0100` | 4 |
| ~Mask | ~(0100) | `1011` | -5 (all 1s except bit 2) |
| AND | 1101 & 1011 | `1001` | 9 |

**Dry Run:** Toggle bit 2 of `n = 9` (binary `1001`)

| Step | Operation | Binary | Decimal |
|------|-----------|--------|---------|
| Start | n = 9 | `1001` | 9 |
| Mask | 1 << 2 | `0100` | 4 |
| XOR | 1001 ^ 0100 | `1101` | 13 |

| Time | Space |
|------|-------|
| O(1) | O(1) |

*Note:* This is already the best possible approach -- each operation is a single bitwise instruction. No Approach 3 needed.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) -- "Each operation is one shift and one bitwise operation, both constant-time CPU instructions."
**Space:** O(1) -- "We only use a single integer variable for the mask."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Confusing the direction of bit numbering -- bit 0 is the **rightmost** (least significant) bit, not the leftmost.
2. Using `1 << i` without considering that in languages like Java/C++, if `i = 31`, you need `1L << i` to avoid signed overflow.
3. Forgetting to negate the mask for clear: writing `n & mask` instead of `n & ~mask`.

### Edge Cases to Test
- [ ] Set a bit that is already 1 --> result should be unchanged
- [ ] Clear a bit that is already 0 --> result should be unchanged
- [ ] Toggle bit 0 (least significant bit) of an even number --> makes it odd
- [ ] Toggle bit 0 of an odd number --> makes it even
- [ ] i = 0 (rightmost bit)
- [ ] n = 0 (all bits are 0)

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is bit position 0-indexed from the right (LSB)? What range can `i` be?"
2. **Approach:** "For set I use OR with a mask, for clear I use AND with the inverted mask, for toggle I use XOR with the mask. The mask is always `1 << i`."
3. **Code:** Write all three as one-liners. Explain each operator's truth table briefly.
4. **Test:** Walk through set/clear/toggle on a small example like n=9, i=2.

### Follow-Up Questions
- "How would you set/clear/toggle a range of bits from position `i` to `j`?" --> Build a multi-bit mask.
- "How would you check if the ith bit is set?" --> `(n >> i) & 1` or `n & (1 << i) != 0`.

---

## CONNECTIONS
- **Prerequisite:** Binary number representation, bitwise operators (AND, OR, XOR, NOT, shifts)
- **Same Pattern:** Check if ith Bit is Set, Count Set Bits
- **Harder Variant:** Toggle all bits in a range, Bitmasking in DP
- **This Unlocks:** All bit manipulation interview problems that require constructing masks
