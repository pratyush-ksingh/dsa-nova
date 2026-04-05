# FizzBuzz

> **Step 01 | 1.2** | **Difficulty:** EASY | **XP:** 10 | **Source:** InterviewBit

---

## 1. UNDERSTAND

### Problem Statement
Given a positive integer **n**, return a list of strings for each number from 1 to n:
- `"FizzBuzz"` if divisible by both 3 and 5
- `"Fizz"` if divisible by 3 only
- `"Buzz"` if divisible by 5 only
- The number itself as a string otherwise

**Example (n=15):**
```
1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz
```

| Input | Output (last 5 items)                     |
|-------|------------------------------------------|
| n=5   | ["1","2","Fizz","4","Buzz"]              |
| n=15  | [..., "11","Fizz","13","14","FizzBuzz"]  |
| n=1   | ["1"]                                    |

### Real-Life Analogy
Think of a **shift scheduling system**: every 3rd shift needs a "Fizz" (equipment check), every 5th shift needs a "Buzz" (safety review), and every 15th shift needs both. FizzBuzz codifies overlapping periodic events.

### Key Observations
1. The divisibility-by-15 check is just divisibility-by-3 AND divisibility-by-5.
2. **Aha moment:** Instead of special-casing 15, you can concatenate: if divisible by 3, append "Fizz"; if divisible by 5, append "Buzz". Result handles all four cases automatically.
3. The rule-table approach scales to additional rules (e.g., divisible by 7 -> "Jazz") with zero code restructuring.

### Constraints
- 1 <= n <= 10^4

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why a List/Array for Output?
We produce one string per number from 1 to n. A list preserves order and allows easy return or printing. The algorithm itself only needs O(1) state per iteration.

### Pattern Recognition
**Classification cue:** "Apply rules periodically over a range" --> modulo arithmetic. Multiple overlapping periods --> concatenation approach beats if-else chains.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- If-Else Chain
**Intuition:** Check the four conditions in order. Check divisibility by 15 first (most specific case must come before its subcases 3 and 5).

**Steps:**
1. For `i` in 1..n:
2. If `i % 15 == 0`: append "FizzBuzz".
3. Else if `i % 3 == 0`: append "Fizz".
4. Else if `i % 5 == 0`: append "Buzz".
5. Else: append `str(i)`.

**Dry Run (i = 1..6):**

| i | i%3 | i%5 | i%15 | Output |
|---|-----|-----|------|--------|
| 1 | 1   | 1   | 1    | "1"    |
| 2 | 2   | 2   | 2    | "2"    |
| 3 | 0   | 3   | 3    | "Fizz" |
| 4 | 1   | 4   | 4    | "4"    |
| 5 | 2   | 0   | 5    | "Buzz" |
| 6 | 0   | 1   | 6    | "Fizz" |

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

**BUD Transition:** The divisibility-by-15 check is redundant -- string concatenation handles it.

---

### Approach 2: Optimal -- String Concatenation
**Intuition:** Build label by appending: if `i%3==0` append "Fizz", if `i%5==0` append "Buzz". When both are true, the label becomes "FizzBuzz" automatically. No check for 15 needed.

**Steps:**
1. For `i` in 1..n:
2. `label = ""`
3. If `i % 3 == 0`: `label += "Fizz"`.
4. If `i % 5 == 0`: `label += "Buzz"`.
5. Append `label if label else str(i)`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 3: Best -- Rule-Table / Mapping Approach
**Intuition:** Store `(divisor, word)` pairs in a list. Iterate over rules to build the label. Adding a new rule requires only inserting a tuple -- the loop body never changes. This is the **open/closed principle** in action.

**Steps:**
1. Define `rules = [(3, "Fizz"), (5, "Buzz")]`.
2. For `i` in 1..n: `label = "".join(word for div, word in rules if i % div == 0)`.
3. Append `label if label else str(i)`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n)?** We visit each number exactly once. Each visit does O(1) work (a constant number of modulo checks). Output storage is O(n) strings of bounded length.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake                              | Why it happens                          | Fix                                          |
|-------------------------------------|-----------------------------------------|----------------------------------------------|
| Checking 3 before 15 in if-else     | Outputs "Fizz" for 15 instead of "FizzBuzz" | Check the most specific condition (15) first, or use concatenation |
| Using integer output for non-matches | Returns int instead of string           | Always convert: `str(i)`                    |
| Off-by-one in range                 | Starting from 0 or ending at n-1        | Range is 1..n inclusive                     |

### Edge Cases Checklist
- n=1: output = ["1"]
- n=3: ["1","2","Fizz"]
- n=15: last element is "FizzBuzz"

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Map integers 1..n to strings with three substitution rules."
2. **M**atch: "Modulo arithmetic, conditional mapping."
3. **P**lan: "Use concatenation approach -- no need to special-case 15."
4. **I**mplement: Approaches 2 or 3 preferred in interviews.
5. **R**eview: Verify i=15 gives "FizzBuzz", i=9 gives "Fizz", i=10 gives "Buzz".
6. **E**valuate: "O(n) time, O(n) space for output list."

### Follow-Up Questions
- "Extend to also mark multiples of 7 as 'Jazz'?" --> Add `(7, "Jazz")` to the rule table in Approach 3. Zero other changes.
- "Return as a single newline-separated string?" --> `"\n".join(result)`.
- "What if we need it to be thread-safe?" --> Build result list then return; avoid shared mutable state.

---

## 7. CONNECTIONS

| Relationship      | Problem                                                         |
|-------------------|-----------------------------------------------------------------|
| **Prerequisite**  | Modulo operator; basic loop and conditional                    |
| **Same pattern**  | Any periodic substitution over a range                        |
| **Harder variant*** | FizzBuzzBazz with many custom rules; LeetCode 412             |
| **Unlocks**       | Rule-based processing; open/closed design principle            |
