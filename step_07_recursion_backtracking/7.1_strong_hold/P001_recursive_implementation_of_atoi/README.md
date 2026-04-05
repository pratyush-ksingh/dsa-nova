# Recursive Implementation of atoi

> **Batch 4 of 12** | **Topic:** Recursion (Strong Hold) | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
**(LeetCode #8 -- String to Integer)** Implement the `myAtoi(string s)` function, which converts a string to a 32-bit signed integer. The algorithm:
1. **Skip leading whitespace.**
2. **Read an optional sign** (`+` or `-`).
3. **Read digits** until a non-digit character or end of string.
4. **Clamp** the result to the 32-bit signed integer range: `[-2^31, 2^31 - 1]`.

The twist: implement this **recursively** rather than iteratively.

### Examples

| # | Input | Output | Explanation |
|---|-------|--------|-------------|
| 1 | `"42"` | 42 | Straightforward digits |
| 2 | `"   -42"` | -42 | Leading whitespace, negative sign |
| 3 | `"+123"` | 123 | Explicit positive sign |
| 4 | `"4193 with words"` | 4193 | Stop at first non-digit |
| 5 | `"words and 987"` | 0 | No leading digits after whitespace |
| 6 | `""` | 0 | Empty string |
| 7 | `"-91283472332"` | -2147483648 | Overflow clamped to INT_MIN |
| 8 | `"91283472332"` | 2147483647 | Overflow clamped to INT_MAX |
| 9 | `"  +0 123"` | 0 | Digits stop at space after 0 |

### Real-Life Analogy
Think of reading a **handwritten number** character by character. You skip any blank margin (whitespace), notice if there is a minus sign, then read each digit and mentally build up the number: "I have 4... now 41... now 419... now 4193... next character is a space, I stop." If the number gets absurdly large, you cap it at the maximum your "register" can hold.

### Three Key Observations (the "Aha!" Moments)
1. **Three distinct phases** -- (a) skip whitespace, (b) handle sign, (c) process digits. Each phase can be a separate step in recursion or a preprocessing step before the recursive digit-reading.
2. **Overflow detection must happen BEFORE multiplication** -- If `result > INT_MAX/10` or `result == INT_MAX/10 && digit > 7`, the next accumulation will overflow. You must check this before `result = result * 10 + digit`.
3. **Recursion on digit processing** -- The recursive structure processes one digit per call: `atoi_helper(s, index, result)` -> takes current result, processes `s[index]`, then recurses on `index+1`. Base case: index out of bounds OR non-digit character.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Iterative: preprocess whitespace/sign, loop over digits | Variables |
| Optimal | Recursive: preprocess whitespace/sign, recurse over digits | Call stack |
| Best | Fully recursive: even whitespace/sign handling via recursion | Call stack |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Implementation

**Intuition:** Process the string left to right in three phases using a single loop. This is the standard iterative approach that serves as the baseline.

**BUD Analysis:**
- **B**ottleneck: Single pass O(n), cannot do better.
- **U**nnecessary work: None.
- **D**uplicate work: None.

**Steps:**
1. Initialize `i = 0`. Skip whitespace: while `s[i] == ' '`, increment `i`.
2. Read sign: if `s[i]` is `+` or `-`, record the sign and increment `i`.
3. Read digits: while `s[i]` is a digit:
   - Check overflow before accumulating.
   - `result = result * 10 + digit`.
4. Apply sign and return.

**Dry-Run Trace** (`"   -42"`):
```
Skip whitespace: i=0(' '),1(' '),2(' ') -> i=3
Sign: s[3]='-', sign=-1, i=4
Digits: s[4]='4' -> result=4, i=5
        s[5]='2' -> result=42, i=6
End of string.
Return: -1 * 42 = -42
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

### Approach 2: Optimal -- Recursive Digit Processing

**Intuition:** Handle whitespace and sign iteratively (they are one-time preprocessing), then recurse over the digit portion. Each recursive call processes one digit and passes the accumulated result forward.

**Steps:**
1. Skip whitespace, read sign (iteratively, O(1) passes).
2. Call `recurse(s, i, 0)` where `i` is the index of the first digit.
3. `recurse(s, i, result)`:
   - Base case: `i >= len(s)` or `s[i]` is not a digit -> return `result`.
   - Overflow check: if adding digit would overflow, return `INT_MAX` or `INT_MIN`.
   - `result = result * 10 + digit`.
   - Return `recurse(s, i+1, result)`.
4. Apply sign to the returned value.

**Dry-Run Trace** (`"   -42"`):
```
Preprocess: skip whitespace -> i=3, sign=-1 -> i=4
recurse(s, 4, 0):
  s[4]='4', digit=4, result=0*10+4=4
  -> recurse(s, 5, 4):
       s[5]='2', digit=2, result=4*10+2=42
       -> recurse(s, 6, 42):
            i=6 >= len(s), return 42
Return: -1 * 42 = -42
```

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(d) where d = number of digits (call stack) |

---

### Approach 3: Best -- Fully Recursive

**Intuition:** Handle everything recursively, including whitespace skipping and sign detection. The main function dispatches to `skip_whitespace -> read_sign -> read_digits`, each implemented as a recursive function or as stages within a single recursive function using a state parameter.

**Steps:**
1. `atoi(s, i=0, state=WHITESPACE, sign=1, result=0)`:
   - WHITESPACE state: if `s[i] == ' '`, recurse with `i+1`. Else transition to SIGN state.
   - SIGN state: if `s[i]` is `+/-`, set sign, recurse with `i+1`, state=DIGIT. Else transition to DIGIT state (same i).
   - DIGIT state: if `s[i]` is a digit, accumulate with overflow check, recurse `i+1`. Else return `sign * result`.
2. Base case: `i >= len(s)` -> return `sign * result`.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) call stack (whitespace chars + digit chars) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n):** Each character is processed exactly once, either skipped (whitespace), used to set the sign, or accumulated as a digit. No backtracking occurs.

**Space trade-off:** The iterative approach uses O(1) space. The recursive approaches use O(d) or O(n) stack space. For interview purposes, the recursive version demonstrates recursion skills even though it is not space-optimal.

**Overflow:** The 32-bit range is [-2147483648, 2147483647]. The check `result > INT_MAX/10` or `(result == INT_MAX/10 && digit > 7)` prevents undefined behavior before it happens.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Empty string | 0 | No characters to process |
| Only whitespace `"   "` | 0 | No digits after whitespace |
| Leading zeros `"0032"` | 32 | Must handle without special case |
| Sign only `"+"` | 0 | No digits follow |
| Overflow positive | 2147483647 | Must clamp, not wrap |
| Overflow negative | -2147483648 | INT_MIN has larger magnitude than INT_MAX |
| `"-000123"` | -123 | Leading zeros with sign |
| Digits then letters `"123abc"` | 123 | Stop at first non-digit |

**Common Mistakes:**
- Forgetting the overflow check and getting wrong answers for large inputs.
- Checking overflow after multiplication (too late -- already overflowed).
- Not handling the asymmetry: `|-INT_MIN| > INT_MAX`.
- Processing whitespace in the digit loop (should stop at first non-digit including space).

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** Tests attention to detail (edge cases galore), overflow handling, and ability to implement recursion for a naturally iterative process.

**Follow-ups to expect:**
- "What if you cannot use long to detect overflow?" -> Use the `result > INT_MAX/10` trick.
- "What about floating-point atof?" -> Similar but with decimal point handling.
- "Implement itoa (integer to string) recursively." -> Reverse process, extract digits via `% 10`.

**Talking points:**
- Discuss how C's `atoi` has undefined behavior on overflow vs. the clamping behavior here.
- Mention that this is a state-machine problem in disguise (whitespace -> sign -> digits -> done).

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Reverse Integer (LC #7) | Same overflow detection pattern |
| String to Integer (iterative) | Non-recursive version of the same problem |
| Valid Number (LC #65) | More complex parsing of numeric strings |
| Integer to Roman (LC #12) | Reverse direction: number -> string |
| Pow(x, n) (LC #50) | Another recursion problem with overflow concerns |
