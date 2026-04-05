# Reverse Words in a String

> **LeetCode 151** | **Step 05.5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an input string `s`, reverse the order of the **words**.

A **word** is defined as a sequence of non-space characters. The words in `s` will be separated by at least one space.

Return a string of the words in reverse order concatenated by a single space.

The output must **not** contain leading or trailing spaces, and words must be separated by **exactly one space** (even if the input has multiple).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"the sky is blue"` | `"blue is sky the"` | Words reversed, single spaces |
| `"  hello world  "` | `"world hello"` | Leading/trailing spaces stripped |
| `"a good   example"` | `"example good a"` | Multiple internal spaces collapsed |

## Constraints

- `1 <= s.length <= 10^4`
- `s` contains English letters, digits, and spaces `' '`
- There is **at least one word** in `s`

---

## Approach 1: Brute Force — Split + Reverse + Join

**Intuition:** Python's `str.split()` (no argument) splits on any whitespace and filters empty strings automatically. So we get a clean word list in one call. Reverse the list and join with `" "`.

**Steps:**
1. Call `s.split()` — handles leading/trailing and multiple spaces, returns `["the","sky","is","blue"]`.
2. Reverse the list in-place (`words.reverse()`).
3. Return `" ".join(words)`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) — the word list |

---

## Approach 2: Optimal — In-Place Char Array (Two-Step Reversal)

**Intuition:** A classic trick used when only O(1) extra space is allowed on a char array: reverse the entire string first (word order reversed, but each word is backwards), then reverse each word individually (restores every word to its correct spelling). The space cleanup step is done beforehand to avoid managing extra spaces in the reversal logic.

**Steps:**
1. Strip leading/trailing spaces and collapse multiple spaces to single spaces (build clean char array).
2. **Reverse the entire array**: words appear in correct order but each word is spelled backwards.
3. **Reverse each individual word**: walk the array, find word boundaries by spaces, reverse each segment.

**Example:** `"the sky"` → clean: `"the sky"` → full reverse: `"yks eht"` → word reverse: `"sky the"`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) for char array; O(1) extra work beyond string representation |

---

## Approach 3: Best — Two Pointers Scanning from the End

**Intuition:** Instead of reversing anything, scan the string from right to left. Use two pointers `left` and `right` to extract word boundaries. Words are naturally collected in reversed order as we walk backwards. One pass, no double-reversal.

**Steps:**
1. Set `right = len(s) - 1`.
2. Skip trailing spaces (move `right` left while `s[right] == ' '`).
3. Set `left = right`, walk left until `s[left] == ' '` or `left < 0`.
4. Slice `s[left+1 : right+1]` — this is a word. Append to result list.
5. Set `right = left - 1`. Repeat from step 2.
6. Join result list with `" "`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) for result list |

---

## Real-World Use Case

**Search engine query normalisation**: User queries arrive with arbitrary spacing (copy-pasted text, voice-to-text output). Before indexing or matching, the query `"  blue   sky   the  "` is normalised to `"the sky blue"` by reversing and cleaning words — or the words are individually extracted in a controlled order for ranked matching. The two-pointer scan-from-end approach models how a streaming parser would consume tokens.

---

## Interview Tips

- The most important edge cases: multiple spaces between words, leading/trailing spaces, single-word input.
- Interviewers often follow up asking for an O(1) space solution — that is the two-step reversal on a char array (Approach 2).
- In Java, `String.trim().split("\\s+")` handles all space cleanup in one line. In Python, `s.split()` does the same.
- If asked "can you do it without built-in split?", pivot to Approach 3 (two pointers from the end) — it extracts words without any library call.
- When drawing the two-step reversal, always show the intermediate state after the full reverse to make the intuition concrete.
