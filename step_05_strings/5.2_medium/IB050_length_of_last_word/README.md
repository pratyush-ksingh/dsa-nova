# Length of Last Word

> **Batch 4 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a string `s` consisting of words separated by spaces, return the **length of the last word** in the string. A word is a maximal substring consisting of non-space characters. The string may have trailing spaces.

**LeetCode #58**

### Examples

| Input                      | Output | Explanation                                |
|----------------------------|--------|--------------------------------------------|
| `"Hello World"`            | 5      | Last word is "World", length 5             |
| `"   fly me   to   the moon  "` | 4 | Last word is "moon", length 4              |
| `"luffy is still joyboy"`  | 6      | Last word is "joyboy", length 6            |
| `"a"`                      | 1      | Single character, last word is "a"         |

### Analogy
Imagine reading a sentence written on a blackboard from right to left. You skip any trailing chalk dust (spaces), then start counting letters until you hit the next gap or reach the beginning. That count is your answer.

### 3 Key Observations
1. **"Aha" -- Traverse from the right.** The last word is at the end, so scanning from the right avoids processing the entire string. Skip trailing spaces, then count non-space characters.
2. **"Aha" -- Trailing spaces are the trap.** The string `"Hello "` has a last word of length 5, not 0. You must skip trailing spaces before counting.
3. **"Aha" -- No need to extract the word.** We only need the length, not the word itself. Just count characters without creating a substring.

---

## DS & ALGO CHOICE

| Approach         | Data Structure | Algorithm                  | Why?                                  |
|------------------|---------------|----------------------------|----------------------------------------|
| Brute Force      | String[]      | Split by spaces, take last | Simple but creates extra arrays        |
| Optimal          | None          | Scan from right end        | O(k) where k = trailing spaces + word |
| Best             | None          | Same as optimal            | Already minimal work                   |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Split and Take Last
**Intuition:** Split the string by spaces, filter out empty strings, return the length of the last element.

**BUD Analysis:**
- **B**ottleneck: `split()` processes the entire string O(n) and allocates an array of all words.
- **U**nnecessary work: We only need the last word but we split and store all of them.

**Steps:**
1. Trim the string (remove leading/trailing spaces).
2. Split by one or more spaces.
3. Return the length of the last element in the resulting array.

**Dry-Run Trace** with `"   fly me   to   the moon  "`:
```
trim -> "fly me   to   the moon"
split -> ["fly", "me", "to", "the", "moon"]
last element = "moon", length = 4
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) -- stores all words |

---

### Approach 2: Optimal -- Scan from Right
**Intuition:** Start at the end of the string. Skip any trailing spaces. Then count non-space characters until you hit a space or the beginning. The count is the answer.

**Steps:**
1. Set `i = len(s) - 1`.
2. While `i >= 0` and `s[i] == ' '`: decrement `i`. (Skip trailing spaces.)
3. Set `length = 0`.
4. While `i >= 0` and `s[i] != ' '`: increment `length`, decrement `i`.
5. Return `length`.

**Dry-Run Trace** with `"   fly me   to   the moon  "`:
```
i=28: ' ' -> skip    i=27: ' ' -> skip
i=26: 'n' -> len=1   i=25: 'o' -> len=2
i=24: 'o' -> len=3   i=23: 'm' -> len=4
i=22: ' ' -> STOP
Answer: 4
```

| Metric | Value                                    |
|--------|------------------------------------------|
| Time   | O(k) where k = trailing spaces + word len|
| Space  | O(1)                                     |

---

### Approach 3: Best -- Same as Optimal
**Intuition:** The right-scan is already optimal. We cannot find the last word without scanning at least its characters plus any trailing spaces. This approach does exactly that -- no wasted work.

| Metric | Value |
|--------|-------|
| Time   | O(k) -- best case O(1), worst case O(n) |
| Space  | O(1)  |

---

## COMPLEXITY INTUITIVELY

- **Why O(k) is optimal:** We must read every character of the last word to know its length. We must also skip trailing spaces. Together that is exactly `k` characters. We cannot do better.
- **Why O(1) space:** We only use an index variable and a counter.

---

## EDGE CASES & MISTAKES

| Edge Case               | What Happens                                  |
|-------------------------|-----------------------------------------------|
| `"a"`                   | Single char, no spaces -- return 1.           |
| `"a   "`                | Trailing spaces -- skip them, return 1.       |
| `"   "`                 | All spaces -- return 0 (no word exists).      |
| `""`                    | Empty string -- return 0.                     |
| No trailing spaces      | Phase 1 (skip) does nothing, go straight to counting. |

**Common Mistakes:**
- Forgetting to handle trailing spaces (returning 0 for `"Hello "`).
- Using `split(" ")` which produces empty strings between consecutive spaces.
- Off-by-one when converting from 0-indexed to length.

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests careful string handling, edge-case awareness (trailing spaces), and whether you reach for builtins versus writing a clean loop.
- **Follow-ups:**
  - "Return the last word itself, not just its length." (Same scan, collect characters.)
  - "Return the length of the k-th word from the end." (Generalize the scan.)
- **Communication tip:** Clarify upfront: "Can the string have leading/trailing spaces? Multiple consecutive spaces?" Then describe the two-phase right-scan.

---

## CONNECTIONS

| Related Problem                | How It Connects                              |
|-------------------------------|----------------------------------------------|
| Reverse Words in a String     | Same trailing-space handling + word parsing   |
| Valid Palindrome               | Character-by-character scanning with skips   |
| String Compression             | Counting consecutive characters              |

---

## Real-World Use Case
**Text editors / Command-line parsing:** Extracting the last token from user input (e.g., the filename from a file path, the last argument in a shell command) requires exactly this kind of right-to-left scan with space-skipping logic.
