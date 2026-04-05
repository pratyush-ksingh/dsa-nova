# Reverse Words in a String

> **Batch 1 of 12** | **Topic:** Strings | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND

### Problem Statement
Given an input string `s`, reverse the order of the **words**. A word is defined as a sequence of non-space characters. The words in `s` will be separated by at least one space. Return a string of the words in reverse order concatenated by a single space. The result should not contain leading or trailing spaces, and words should be separated by only a single space.

**LeetCode #151**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `"the sky is blue"` | `"blue is sky the"` | Four words reversed |
| `"  hello world  "` | `"world hello"` | Leading/trailing spaces removed |
| `"a good   example"` | `"example good a"` | Multiple spaces reduced to single |

### Constraints
- `1 <= s.length <= 10^4`
- `s` contains English letters, digits, and spaces `' '`
- There is at least one word in `s`

### Real-Life Analogy
Think of a sentence written on index cards, one word per card. You pick up the deck and flip the order of the cards. Extra blank cards (spaces) are discarded, and you only put one gap between each card when laying them out again.

### 3 Key Observations
1. **"aha" -- split and reverse:** Splitting by whitespace naturally handles multiple spaces, then reversing the resulting array gives the answer.
2. **"aha" -- two-pointer in-place:** If working with a char array, you can reverse the entire string, then reverse each word individually -- the double-reversal trick.
3. **"aha" -- trim as you go:** A single backward scan collecting words avoids needing a separate trim/split step.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **StringBuilder / List:** Efficiently builds the reversed result without repeated string concatenation.
- **Two pointers:** For the in-place approach (char array), two pointers identify word boundaries.

### Pattern Recognition Cue
When a problem asks to rearrange tokens in a string, think split-reverse-join or the double-reversal trick for in-place solutions.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Split, Reverse, Join
**Intuition:** Use built-in split to tokenize by spaces, reverse the list, and join.

**Steps:**
1. Split `s` by whitespace (handles multiple spaces).
2. Filter out empty strings.
3. Reverse the list.
4. Join with a single space.

**Dry Run:** `s = "the sky is blue"`
```
split -> ["the", "sky", "is", "blue"]
reverse -> ["blue", "is", "sky", "the"]
join -> "blue is sky the"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 2: Optimal -- Backward Scan with Two Pointers
**Intuition:** Scan the string from the end. Each time we find a word boundary, extract the word and append to result. Avoids splitting and reversing.

**Steps:**
1. Set pointer `i` to end of string.
2. Skip trailing spaces. Mark `end = i`.
3. Move `i` backward until a space or start of string.
4. Append `s[i+1..end+1]` to result.
5. Repeat until `i < 0`.

**Dry Run:** `s = "  hello world  "`
```
i=14 skip spaces -> i=12 (end=12, 'd')
i backward to 8 (space) -> word = s[9..13] = "world"
i=8 skip spaces -> i=6 (end=6, 'o')
i backward to 1 (space) -> word = s[2..7] = "hello"
Result: "world hello"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) for output |

---

### Approach 3: Best -- In-Place Double Reversal (Char Array)
**Intuition:** Convert to char array. Reverse the entire array. Then reverse each individual word. Finally, compact to remove extra spaces.

**Steps:**
1. Trim and convert to char array.
2. Reverse the entire array.
3. Reverse each word (bounded by spaces).
4. Clean up multiple spaces by compacting with a write pointer.

**Dry Run:** `s = "the sky is blue"`
```
char array: ['t','h','e',' ','s','k','y',' ','i','s',' ','b','l','u','e']
reverse all: ['e','u','l','b',' ','s','i',' ','y','k','s',' ','e','h','t']
reverse words: ['b','l','u','e',' ','i','s',' ','s','k','y',' ','t','h','e']
result: "blue is sky the"
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1) extra (if mutable string; O(n) in Java/Python due to immutable strings) |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** Every character is visited a constant number of times across all approaches.
- **Space:** All approaches need O(n) for the output. The double-reversal trick is O(1) extra in languages with mutable strings.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `"  a  "` | `"a"` | Only leading/trailing spaces |
| `"word"` | `"word"` | Single word, no reversal needed |
| `"a b"` | `"b a"` | Minimal two-word case |
| All spaces between words | Collapsed | Must not output multiple spaces |

**Common Mistakes:**
- Not handling multiple consecutive spaces between words.
- Leaving a trailing space in the output.
- Off-by-one when extracting word boundaries in the backward scan.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Reverse word order, collapse spaces."
2. **Match:** "Token reversal -- split-reverse-join or double-reversal."
3. **Plan:** Start with split approach, then optimize to backward scan.
4. **Implement:** Write the backward scan approach.
5. **Review:** Trace through `"  hello world  "`.
6. **Evaluate:** O(n) time, O(n) space.

### Follow-Up Questions
- *"Can you do it in-place?"* -- Use the double-reversal trick on a char array.
- *"What if the string is a stream?"* -- Read words into a stack, then pop them out.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Reverse a String (LC #344) |
| **Same Pattern** | Reverse Words in a String II (LC #186) -- in-place version |
| **Harder** | Text Justification (LC #68) |
