# Swap Two Numbers

> **Step 08.8.1** | **Difficulty:** EASY | **XP:** 10 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given two integers `a` and `b`, swap their values without using a temporary variable.

**Constraints:**
- `-2^31 <= a, b <= 2^31 - 1`
- Must not use a third/temp variable (for the optimal solution)

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `a = 5, b = 10` | `a = 10, b = 5` | Values swapped |
| `a = 0, b = 7` | `a = 7, b = 0` | Works with zero |
| `a = -3, b = 4` | `a = 4, b = -3` | Works with negatives |
| `a = 8, b = 8` | `a = 8, b = 8` | Same values stay same |

### Real-Life Analogy
> *Imagine two cups of liquid -- one red, one blue. Normally you need a third cup to swap. But with XOR, it is like a reversible mixing process: mix them together, then unmix each one back out. The XOR operation is its own inverse, so applying it three times achieves a swap without any extra container.*

### Key Observations
1. XOR is self-inverse: `x ^ x = 0` and `x ^ 0 = x`. This property lets us "encode" both values into one variable and "decode" them back.
2. The arithmetic approach (`a+b`, `a-b`) also works but risks integer overflow.
3. When `a == b`, XOR swap still works: `a^a = 0`, then `0^a = a`, then `a^a = 0`... wait, actually it yields `(0, 0)` if a and b are the **same variable** (aliasing). For distinct variables holding the same value, it works fine.

---

## APPROACH LADDER

### Approach 1: Brute Force -- Using Temp Variable
**Intuition:** The textbook swap. Store one value in a temporary variable, overwrite it, then restore from temp.

**Steps:**
1. `temp = a`
2. `a = b`
3. `b = temp`

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1) -- uses one extra variable |

---

### Approach 2: Optimal -- XOR Swap
**Intuition:** XOR can encode two values into one. Since `x ^ x = 0` and `x ^ 0 = x`, we can extract the original values back with three XOR operations.

**Steps:**
1. `a = a ^ b` -- a now stores the combined XOR
2. `b = a ^ b` -- b = (a^b) ^ b = a (original value of a)
3. `a = a ^ b` -- a = (a^b) ^ a = b (original value of b)

**Dry Run:** `a = 5 (101), b = 3 (011)`
```
Step 1: a = 101 ^ 011 = 110 (6)
Step 2: b = 110 ^ 011 = 101 (5) --> b is now original a
Step 3: a = 110 ^ 101 = 011 (3) --> a is now original b
```

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1) -- no extra variable |

---

### Approach 3: Best -- Language-Specific One-Liner
**Intuition:** In Python, tuple unpacking `a, b = b, a` swaps without a temp variable at the language level. In Java, an arithmetic swap (`a = a+b; b = a-b; a = a-b`) avoids XOR but risks overflow. In interviews, the XOR approach is the expected "no temp variable" answer.

| Metric | Value |
|--------|-------|
| Time   | O(1)  |
| Space  | O(1)  |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Self-aliasing:** If `a` and `b` are references to the same memory location, XOR swap zeroes out the value. Always ensure they are distinct variables.
2. **Arithmetic overflow:** The `a+b` approach can overflow for large integers in languages with fixed-width integers (Java, C++).
3. **Forgetting XOR swap works with negatives:** XOR operates on bit patterns, so sign does not matter.

### Edge Cases
- `a == b` -- both approaches work correctly for distinct variables
- `a = 0` -- XOR with 0 is identity, works fine
- `a = INT_MIN, b = INT_MAX` -- XOR works; arithmetic overflows

---

## Real-World Use Case
XOR swap appears in **embedded systems** and **low-memory environments** where even one extra variable matters. It is also a classic **interview question** to test understanding of bitwise operations. In practice, modern compilers optimize standard swaps to be equally efficient, so XOR swap is more of a knowledge demonstration than a practical optimization.

## Interview Tips
- State the temp variable approach first, then offer XOR as the "no extra space" solution.
- Explain WHY it works: XOR is associative, commutative, and self-inverse.
- Mention the aliasing caveat -- shows awareness of edge cases.
- In Python, just say `a, b = b, a` and explain that it uses tuple packing/unpacking internally.
- Follow-up: "Can you swap two values in an array in-place?" -- same XOR trick with `arr[i] ^= arr[j]`.
