# String to Integer Atoi

> **Step 05.5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Implement the `myAtoi(string s)` function, which converts a string to a 32-bit signed integer (similar to C/C++'s `atoi` function). The algorithm is:
1. Read and ignore any leading whitespace.
2. Check if the next character is `'-'` or `'+'`. Read it if present. Assume positive if neither.
3. Read digits until a non-digit character or end of string.
4. Convert digits to an integer. If no digits were read, return 0.
5. Clamp the integer to the 32-bit signed range `[-2^31, 2^31 - 1]`.

**LeetCode #8**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"42"` | `42` | No whitespace, no sign, just digits. |
| `"   -42"` | `-42` | Leading whitespace skipped, negative sign read. |
| `"4193 with words"` | `4193` | Reading stops at the space (non-digit). |
| `"words and 987"` | `0` | First non-whitespace is not a digit or sign. |
| `"-91283472332"` | `-2147483648` | Clamped to INT_MIN (-2^31). |

### Constraints
- `0 <= s.length <= 200`
- `s` consists of English letters (lower/upper), digits, `' '`, `'+'`, `'-'`, `'.'`

### Real-Life Analogy
Think of a cashier reading a price tag that might have extra text like "$  -42.50 USD". They skip the dollar sign and spaces, note the minus, read "42", stop at the dot (since we want integers), and record -42. If the number is astronomically large, they cap it at the register's max value.

### 3 Key Observations
1. **"aha" -- sequential phases:** The parsing has exactly 3 phases: skip whitespace, read sign, read digits. Each phase is simple on its own.
2. **"aha" -- overflow check before multiply:** You must check for overflow *before* `result = result * 10 + digit`, not after. Once overflow happens, the value is already corrupted.
3. **"aha" -- early termination:** Stop at the first non-digit character after the sign. Don't scan the rest of the string.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **No special data structure needed.** This is a pure parsing problem. A single integer accumulator, an index pointer, and a sign variable suffice.

### Pattern Recognition Cue
String parsing with multiple rules (whitespace, sign, digits, overflow). The key pattern is "state-based parsing" -- process the string character by character through well-defined states.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Strip + Parse with Many Checks
**Intuition:** Use string methods to strip whitespace, then manually check for sign and parse digit by digit with explicit overflow checks at each step.

**Steps:**
1. Strip leading whitespace with `lstrip()` or a while loop.
2. Check for `'+'` or `'-'` and record the sign. Advance index.
3. Iterate through remaining characters:
   - If digit: update `result = result * 10 + digit`.
   - If non-digit: break.
4. Apply sign, then clamp to `[INT_MIN, INT_MAX]`.

**BUD Transition:** The logic works but is spread across multiple if-else blocks. We can unify into a clean single-pass approach.

**Dry Run:** `s = "   -42"`
```
After strip: "-42", i=0
Sign: '-', sign=-1, i=1
i=1: '4' -> result=4
i=2: '2' -> result=42
Return -1 * 42 = -42
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 2: Optimal -- Clean Single Pass with Overflow Guard
**Intuition:** Process the string in one pass through three phases (whitespace, sign, digits). Check overflow *before* accumulating each digit using the condition `result > (INT_MAX - digit) / 10`.

**Steps:**
1. Initialize `i = 0`, `sign = 1`, `result = 0`.
2. **Phase 1:** While `s[i] == ' '`, increment `i`.
3. **Phase 2:** If `s[i]` is `'+'` or `'-'`, set sign accordingly, increment `i`.
4. **Phase 3:** While `s[i]` is a digit:
   - `digit = s[i] - '0'`
   - If `result > (INT_MAX - digit) / 10`: return `INT_MAX` if positive, else `INT_MIN`.
   - `result = result * 10 + digit`, increment `i`.
5. Return `sign * result`.

**BUD Transition from Brute:** Cleaner flow, overflow check is pre-emptive (before multiplication), no string methods needed.

**Dry Run:** `s = "-91283472332"`
```
Phase 1: no whitespace
Phase 2: sign = -1, i=1
Phase 3: result builds: 9,91,912,9128,91283,912834,9128347,91283472,912834723
  Next digit=3: 912834723 > (2147483647-3)/10 = 214748364 -> YES, overflow!
  sign=-1, return INT_MIN = -2147483648
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Regex (Python) / DFA State Machine
**Intuition:** Use a regex to extract the valid numeric prefix in one shot, then clamp. Alternatively, model the problem as a deterministic finite automaton (DFA) with states: START, SIGNED, DIGIT, END.

**Steps (Regex variant):**
1. Match pattern `^\s*([+-]?\d+)` against the string.
2. If match found, convert to integer and clamp to `[INT_MIN, INT_MAX]`.
3. If no match, return 0.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** We scan each character at most once. The three phases together visit each character exactly once.
- **Space O(1):** Only a few integer variables: index, sign, result, digit.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `""` (empty) | `0` | Must handle empty input |
| `"   "` (all spaces) | `0` | No digits after whitespace |
| `"+-12"` | `0` | Only one sign allowed; second char is non-digit |
| `"+1"` | `1` | Explicit positive sign |
| `"2147483648"` | `2147483647` | Overflow by 1 -- must clamp to INT_MAX |
| `"-2147483649"` | `-2147483648` | Underflow by 1 -- must clamp to INT_MIN |
| `"  0000042"` | `42` | Leading zeros |

**Common Mistakes:**
- Checking overflow *after* multiplication (too late -- integer already overflowed in languages like C++/Java).
- Allowing multiple signs like `"+-12"`.
- Forgetting to handle empty string or all-whitespace string.
- Using `long` to avoid overflow (works but interviewer may ask for pure 32-bit solution).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need to parse a string into a 32-bit integer, handling whitespace, sign, non-digit chars, and overflow."
2. **Match:** "Sequential parsing with three phases: whitespace, sign, digits."
3. **Plan:** Describe the single-pass approach with pre-emptive overflow check.
4. **Implement:** Write the solution, emphasizing the overflow guard.
5. **Review:** Walk through `"-91283472332"` showing overflow detection.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What if we need to support floating point?"* -- Add a phase for decimal point and fractional digits.
- *"What if the base is not 10?"* -- Generalize digit extraction: `digit < base`.
- *"Can you implement this as a finite state machine?"* -- Yes, 4 states: START, SIGNED, IN_NUMBER, END.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Reverse Integer (LC #7) -- same overflow handling |
| **Same Pattern** | Valid Number (LC #65) -- more complex parsing |
| **Harder** | Expression Evaluation problems |
| **Unlocks** | Integer to English Words (LC #273) |
