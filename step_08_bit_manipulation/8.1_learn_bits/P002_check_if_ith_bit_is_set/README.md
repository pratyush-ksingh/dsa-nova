# Check if ith Bit is Set

> **Batch 1 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an integer `N` and a bit position `i` (0-indexed from the right / LSB), determine whether the ith bit of `N` is set (1) or not (0).

**Constraints:**
- `1 <= N <= 10^9`
- `0 <= i <= 30`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `N = 5 (101), i = 0` | `true` | Bit 0 is 1 |
| `N = 5 (101), i = 1` | `false` | Bit 1 is 0 |
| `N = 5 (101), i = 2` | `true` | Bit 2 is 1 |
| `N = 8 (1000), i = 3` | `true` | Bit 3 is 1 |
| `N = 8 (1000), i = 0` | `false` | Bit 0 is 0 |

### Real-Life Analogy
> *Imagine a control panel with 32 indicator lights numbered 0 to 31. Each light is either ON or OFF. You are asked: "Is light number i currently ON?" You walk directly to that light and check. You do not need to look at any other light. In bit manipulation, the "walk directly to" step is done by creating a mask that isolates that one bit.*

### Key Observations
1. There are exactly two clean ways to isolate a single bit: **left-shift a mask** or **right-shift the number**.
2. Method A: `(N & (1 << i)) != 0` -- create a mask with only bit i set, AND with N.
3. Method B: `(N >> i) & 1` -- shift the number right so bit i lands at position 0, then AND with 1. <-- This is the "aha" insight (both methods are O(1), choose whichever reads clearest)

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No data structure needed. Direct bitwise operations on the integer.
- Bitwise AND and shift are single-cycle CPU operations.

### Pattern Recognition
- **Pattern:** Bit Masking -- isolate a single bit
- **Classification Cue:** "Whenever you need to _check/read a specific bit_ --> AND with a mask or shift-then-AND."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Repeated Division
**Idea:** Repeatedly divide N by 2 to extract bits from the right. Count how many divisions until we reach position i, then check if that remainder is 1.

**Steps:**
1. Repeat `i` times: `N = N / 2` (discard the remainder, effectively right-shifting by 1).
2. Check if `N % 2 == 1`.

**Why it works:** Dividing by 2 strips the rightmost bit each time. After i divisions, the ith bit is now the rightmost bit.

**BUD Transition -- Unnecessary Work:** We perform `i` division operations. Bitwise shift does all `i` positions in one instruction.

| Time | Space |
|------|-------|
| O(i) | O(1) |

### Approach 2: Optimal -- Left-Shift Mask
**What changed:** Create a mask `1 << i` (only bit i is 1). AND it with N. If the result is non-zero, bit i is set.

**Steps:**
1. Compute `mask = 1 << i`.
2. Return `(N & mask) != 0`.

**Dry Run:** `N = 5 (101), i = 2`

| Step | Operation | Binary | Result |
|------|-----------|--------|--------|
| mask | 1 << 2 | 100 | 4 |
| N & mask | 101 & 100 | 100 | 4 (non-zero) |
| Answer | 4 != 0 | - | true |

| Time | Space |
|------|-------|
| O(1) | O(1) |

### Approach 3: Best -- Right-Shift Number
**What changed:** Instead of creating a mask, shift N right by i positions and check the lowest bit.

**Steps:**
1. Compute `shifted = N >> i`.
2. Return `(shifted & 1) == 1`.

**Dry Run:** `N = 5 (101), i = 2`

| Step | Operation | Binary | Result |
|------|-----------|--------|--------|
| N >> 2 | Right shift | 001 | 1 |
| 1 & 1 | AND with 1 | 001 | 1 |
| Answer | 1 == 1 | - | true |

**Note:** Both Approach 2 and 3 are O(1). Approach 3 is sometimes preferred because the result is directly 0 or 1 (no need for `!= 0` conversion).

| Time | Space |
|------|-------|
| O(1) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(1) -- "A shift and an AND, both constant-time CPU operations."
**Space:** O(1) -- "No extra memory."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Confusing 0-indexed vs 1-indexed bit positions. This problem uses 0-indexed (bit 0 = LSB).
2. Using `N & (1 << i) == 1` instead of `!= 0`. When i > 0, the result of `N & (1 << i)` is `2^i`, not `1`. Always compare with `!= 0` or use the right-shift approach.
3. In Java, `1 << i` with `i >= 31` can cause sign issues with int. Use `1L << i` for long if needed.

### Edge Cases to Test
- [ ] `i = 0` -- check LSB
- [ ] `N = 1, i = 0` --> true
- [ ] `N = 1, i = 1` --> false
- [ ] N is a power of 2 -- only one bit set
- [ ] `i` is larger than the highest set bit --> false
- [ ] All bits set: `N = 2^31 - 1, i = 30` --> true

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Given N and position i, check if bit i is 1."
2. **Match:** "Single-bit check --> bit mask with AND."
3. **Plan:** "Two equivalent methods: mask-and-compare, or shift-and-check-LSB."
4. **Implement:** Write both one-liners, explain the trade-off.
5. **Review:** Trace N=5, i=2.
6. **Evaluate:** "O(1) time, O(1) space. Cannot do better."

### Follow-Up Questions
- "How would you check if the ith bit is set in a very large number (big integer)?" --> Same logic, but use the language's big integer library which handles arbitrary-width shifts.
- "How would you check multiple bits at once?" --> Create a multi-bit mask and AND: `N & mask == mask` checks if ALL specified bits are set.
- "What is the relationship to this and permissions?" --> Unix permissions use this exact technique: `if (permissions & READ_BIT) != 0`.

---

## CONNECTIONS
- **Prerequisite:** Introduction to Bit Manipulation (P001)
- **Same Pattern:** Get Bit, Set Bit, Clear Bit, Toggle Bit
- **Harder Variant:** Count Set Bits (Brian Kernighan), Find the only set bit position
- **This Unlocks:** Subset enumeration with bitmasks, bit manipulation tricks in DP
