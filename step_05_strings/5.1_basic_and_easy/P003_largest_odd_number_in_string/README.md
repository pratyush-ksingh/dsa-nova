# Largest Odd Number in String

> **Batch 1 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
You are given a string `num` representing a large integer. Return the **largest-valued odd integer** (as a string) that is a **non-empty substring** of `num`. If no odd integer exists, return an empty string `""`.

A substring is a contiguous sequence of characters within a string. The result should not have leading zeros (but since we always take a prefix, and `num` has no leading zeros, this is guaranteed).

**LeetCode #1903**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"52"` | `"5"` | `"5"`, `"2"`, `"52"` are candidates. The odd ones: `"5"`. Largest is `"5"`. |
| `"4206"` | `""` | No digit is odd, so no odd substring exists. |
| `"35427"` | `"35427"` | The number itself ends in 7 (odd), so the whole string is the answer. |

### Constraints
- `1 <= num.length <= 10^5`
- `num` only consists of digits and does not contain leading zeros

### Real-Life Analogy
Imagine you have a long receipt number. You want the largest odd number you can form by keeping the leftmost digits and trimming from the right. You scan from the right end: the moment you find an odd digit, everything from the start up to that digit is your answer.

### 3 Key Observations
1. **"aha" -- a number is odd iff its last digit is odd:** So we need a substring whose last character is an odd digit (1, 3, 5, 7, 9).
2. **"aha" -- longer prefix = larger value:** Given that `num` has no leading zeros, any substring starting at index 0 and ending later is strictly larger than one ending earlier (when both represent numbers). So the largest odd substring is the longest prefix that ends on an odd digit.
3. **"aha" -- scan from the right:** Find the rightmost odd digit. Everything from index 0 to that position (inclusive) is the answer.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **No data structure needed.** A single backward scan and a string slice is all it takes.
- The key insight is mathematical (oddness depends only on the last digit), not structural.

### Pattern Recognition Cue
When a problem involves properties of numbers represented as strings, check if the property depends on just a suffix or prefix. Oddness depends on the last digit -- so scan from the end.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check All Substrings
**Intuition:** Generate every possible substring, convert to integer, check if odd, track the maximum.

**Steps:**
1. For each start index `i` from 0 to n-1:
   - For each end index `j` from `i+1` to n:
     - Extract `num[i:j]`, convert to integer, check if odd.
     - If odd and larger than current max, update max.
2. Return max as string (or "" if none found).

**Why this is bad:** Converting huge substrings to integers is O(n) per conversion. Total: O(n^3) time. Also, integers can overflow for 10^5-digit numbers.

**BUD Transition:** We do not need to compare numeric values. A substring starting at index 0 that is longer is always larger. And oddness depends only on the last digit.

| Metric | Value |
|--------|-------|
| Time   | O(n^3) or worse |
| Space  | O(n) for substring storage |

---

### Approach 2: Optimal -- Scan Right for Last Odd Digit
**Intuition:** The largest odd substring must start at index 0 (to maximize length/value). We just need the rightmost position where the digit is odd.

**Steps:**
1. Scan from right to left through `num`.
2. At the first odd digit found (at index `i`), return `num[0..i]` (inclusive).
3. If no odd digit is found, return `""`.

**BUD Transition from Brute:** Eliminated all substring generation and comparison. Single pass, O(1) logic.

**Dry Run:** `num = "4206"`
```
i=3 '6' -> even, skip
i=2 '0' -> even, skip
i=1 '2' -> even, skip
i=0 '4' -> even, skip
No odd digit found -> return ""
```

**Dry Run:** `num = "35427"`
```
i=4 '7' -> odd! return num[0..4] = "35427"
```

**Dry Run:** `num = "52"`
```
i=1 '2' -> even, skip
i=0 '5' -> odd! return num[0..0] = "5"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1) extra (excluding the output substring) |

---

### Approach 3: Best -- Same as Optimal (Proven Lower Bound)
**Intuition:** We must inspect digits to determine if any are odd. In the worst case (all even digits), we check every digit. O(n) is the lower bound. O(1) extra space is minimal. Approach 2 is provably optimal.

| Metric | Value |
|--------|-------|
| Time   | O(n) worst case, O(1) best case (if last digit is odd) |
| Space  | O(1) extra |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** We scan at most n digits from the right. Each check is O(1) -- just test if the digit is odd.
- **Space O(1):** We only store an index. The returned substring is required output.
- **Best case O(1):** If the last digit is already odd, we return immediately.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `"2"` | `""` | Single even digit -- no odd substring exists |
| `"9"` | `"9"` | Single odd digit |
| `"2468"` | `""` | All even digits |
| `"24681"` | `"24681"` | Only the last digit is odd, so the whole string is the answer |
| `"10"` | `"1"` | Tricky: the odd substring is `"1"`, not `"10"` |

**Common Mistakes:**
- Trying to convert the string to an integer -- overflows for 10^5-digit numbers.
- Checking substrings that don't start at index 0 -- they can never be the largest.
- Forgetting to return `""` when no odd digit exists.
- Returning the odd digit itself instead of the prefix ending at that digit.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need the largest-valued odd number that is a contiguous substring of the input."
2. **Match:** "Oddness depends only on the last digit. The longest prefix ending at an odd digit gives the largest value."
3. **Plan:** "Scan from right to left. First odd digit I find, return the prefix up to and including it."
4. **Implement:** 3 lines of code.
5. **Review:** Walk through `"52"` and `"4206"`.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What if leading zeros were allowed?"* -- Same algorithm works; the substring starting at 0 is still longest.
- *"What about the largest even number?"* -- Same idea but check for even last digit. Trivially the whole string if the last digit is even.
- *"What if we need the k-th largest odd substring?"* -- Much harder; would need to enumerate candidates.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Basic string traversal, modular arithmetic |
| **Same Pattern** | Check if a number string is divisible by some digit -- last-digit or digit-sum tricks |
| **Harder** | Largest Number (LC #179) -- custom sort for max concatenation |
| **Unlocks** | Number theory problems on strings, suffix-based decisions |
