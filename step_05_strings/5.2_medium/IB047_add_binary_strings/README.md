# Add Binary Strings

> **Batch 4 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given two binary strings `a` and `b`, return their **sum** as a binary string. The inputs may have different lengths and may contain leading zeros.

**LeetCode #67**

### Examples

| Input                    | Output   | Explanation                                 |
|--------------------------|----------|---------------------------------------------|
| `a="11"`, `b="1"`       | `"100"`  | 3 + 1 = 4 = "100" in binary                |
| `a="1010"`, `b="1011"`  | `"10101"`| 10 + 11 = 21 = "10101" in binary            |
| `a="0"`, `b="0"`        | `"0"`    | 0 + 0 = 0                                  |

### Analogy
Think of how you add two decimal numbers on paper: you start from the rightmost digit, add corresponding digits plus any carry, write down the result digit, and propagate the carry left. Binary addition is identical, except each digit is 0 or 1, and `1 + 1 = 10` (write 0, carry 1).

### 3 Key Observations
1. **"Aha" -- Process right-to-left with carry.** Just like elementary school addition. The sum of two bits plus carry is at most `1 + 1 + 1 = 3`, so carry is at most 1.
2. **"Aha" -- Handle unequal lengths gracefully.** When one string is exhausted, treat its missing digits as 0. The loop continues until both strings and the carry are all done.
3. **"Aha" -- Build result in reverse, then flip.** Appending to the end of a list is O(1); prepending to a string is O(n). Build backwards, reverse once at the end.

---

## DS & ALGO CHOICE

| Approach         | Data Structure    | Algorithm              | Why?                                     |
|------------------|------------------|------------------------|-------------------------------------------|
| Brute Force      | BigInteger/int   | Convert, add, convert  | Straightforward but fails on huge inputs  |
| Optimal          | StringBuilder    | Right-to-left carry    | Handles arbitrary length, O(n) time       |
| Best             | StringBuilder    | Same + bit operations  | Same complexity, uses & and ^ for adds    |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Integer Conversion
**Intuition:** Convert both binary strings to integers, add them, convert back to binary.

**BUD Analysis:**
- **B**ottleneck: Integer conversion. If strings are very long (thousands of digits), this overflows fixed-width types.
- **U**nnecessary: Converting the entire number when we can process digit-by-digit.

**Steps:**
1. Parse `a` and `b` as integers (base 2).
2. Add them.
3. Convert the sum back to a binary string.

**Dry-Run Trace** with `a="11"`, `b="1"`:
```
int_a = int("11", 2) = 3
int_b = int("1", 2) = 1
sum = 4
bin(4) = "100"
```

| Metric | Value                           |
|--------|---------------------------------|
| Time   | O(n) for parsing + O(n) output  |
| Space  | O(n) for the result string      |

**Limitation:** Fails when string length exceeds the integer size (e.g., 10000-digit binary strings in Java).

---

### Approach 2: Optimal -- Right-to-Left Addition with Carry
**Intuition:** Simulate the exact same process you use to add numbers on paper. Walk from the rightmost character of both strings, add corresponding bits plus carry, record the result bit.

**Steps:**
1. Initialize two pointers `i = len(a) - 1`, `j = len(b) - 1`, `carry = 0`.
2. While `i >= 0` or `j >= 0` or `carry > 0`:
   - `digitA = a[i] - '0'` if `i >= 0` else `0`. Decrement `i`.
   - `digitB = b[j] - '0'` if `j >= 0` else `0`. Decrement `j`.
   - `total = digitA + digitB + carry`
   - Append `total % 2` to result.
   - `carry = total / 2`.
3. Reverse the result and return.

**Dry-Run Trace** with `a="1010"`, `b="1011"`:
```
Step 1: i=3 j=3 -> 0+1+0=1 -> result=[1], carry=0
Step 2: i=2 j=2 -> 1+1+0=2 -> result=[1,0], carry=1
Step 3: i=1 j=1 -> 0+0+1=1 -> result=[1,0,1], carry=0
Step 4: i=0 j=0 -> 1+1+0=2 -> result=[1,0,1,0], carry=1
Step 5: carry=1 -> result=[1,0,1,0,1], carry=0
Reverse: "10101"
```

| Metric | Value                                |
|--------|--------------------------------------|
| Time   | O(max(len(a), len(b)))               |
| Space  | O(max(len(a), len(b))) for result    |

---

### Approach 3: Best -- Bitwise Simulation
**Intuition:** Instead of arithmetic (`total / 2`, `total % 2`), use bitwise operators. For two bits `x, y` and carry `c`: result bit = `x ^ y ^ c`, new carry = `(x & y) | (x & c) | (y & c)`. Same complexity, but reinforces bit manipulation thinking and mirrors how hardware adders actually work.

**Steps:**
1. Same loop structure as Approach 2.
2. `resultBit = digitA ^ digitB ^ carry`
3. `carry = (digitA & digitB) | (digitA & carry) | (digitB & carry)`
4. Append `resultBit`, reverse at end.

| Metric | Value                                |
|--------|--------------------------------------|
| Time   | O(max(len(a), len(b)))               |
| Space  | O(max(len(a), len(b))) for result    |

---

## COMPLEXITY INTUITIVELY

- **Why O(n) is optimal:** We must produce every digit of the output. The output has at most `max(len(a), len(b)) + 1` digits. Reading input alone is O(n).
- **Why O(n) space is unavoidable:** The output string itself requires O(n) space.

---

## EDGE CASES & MISTAKES

| Edge Case               | What Happens                                       |
|-------------------------|---------------------------------------------------|
| `a="0"`, `b="0"`       | Result is `"0"`, not `""`.                        |
| One string empty `""`   | Treat as "0" or return the other string.          |
| Unequal lengths         | Shorter string pads with leading zeros implicitly.|
| Final carry `a="1" b="1"`| Result is `"10"` -- don't forget the last carry. |
| Very long strings (10^4) | Integer conversion overflows; use Approach 2/3.  |

**Common Mistakes:**
- Forgetting the final carry (dropping the leading 1).
- Building the result left-to-right and getting the digits backwards.
- Off-by-one on pointer indices.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests fundamental understanding of binary arithmetic and the carry mechanism. Also tests string manipulation skills.
- **Follow-ups:**
  - "Now multiply two binary strings." (Grade-school multiplication.)
  - "Add two numbers represented as linked lists." (Same carry logic, different data structure.)
  - "What if the base is arbitrary (e.g., base 7)?" (Generalize `% base` and `/ base`.)
- **Communication tip:** Mention the right-to-left processing and carry immediately. Draw out one iteration on the whiteboard.

---

## CONNECTIONS

| Related Problem                  | How It Connects                                    |
|----------------------------------|---------------------------------------------------|
| Add Two Numbers (Linked Lists)   | Same carry-based digit-by-digit addition          |
| Multiply Strings (LC #43)        | Extends addition to multiplication                |
| Plus One (LC #66)                | Single-number increment with carry propagation    |
| Add to Array-Form of Integer     | Same addition loop on array representation        |

---

## Real-World Use Case
**Hardware design / ALU circuits:** Binary addition is literally how CPUs work. The carry-propagation logic you implement here mirrors a ripple-carry adder circuit. Understanding this is foundational to computer architecture and designing arithmetic logic units.
