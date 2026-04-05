# Convert to Palindrome

> **Step 05 | 5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **LeetCode:** 680

## Problem Statement

Given a non-empty string `s`, check if it can be made into a **palindrome** by **removing at most one character**.

Return `1` if possible, `0` otherwise.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"aba"` | `1` | Already a palindrome |
| `"abca"` | `1` | Remove `'c'` → `"aba"` |
| `"abc"` | `0` | No single removal makes it a palindrome |
| `"deeee"` | `1` | Remove `'d'` → `"eeee"` |
| `"aab"` | `1` | Remove first `'a'` → `"ab"`... wait, remove `'b'` → `"aa"`. Yes. |

## Constraints

- `1 <= s.length <= 10^5`
- `s` consists of lowercase English letters

---

## Approach 1: Brute Force (Try Removing Each Character)

**Intuition:** Try every possible single character deletion. If any resulting string is a palindrome (or the original is), return `1`.

**Steps:**
1. If `s` is already a palindrome, return `1`.
2. For each index `i` from `0` to `n-1`:
   - Construct `candidate = s[0..i-1] + s[i+1..n-1]`.
   - If `candidate` is a palindrome, return `1`.
3. Return `0`.

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(n)   |

---

## Approach 2: Optimal (Two Pointers — One Skip Budget)

**Intuition:** Use two pointers from both ends. As long as `s[left] == s[right]`, both characters belong to any palindrome arrangement — advance both. The moment there is a mismatch, we have exactly **one skip**. Try two options:
- Skip the left character: is `s[left+1..right]` a palindrome?
- Skip the right character: is `s[left..right-1]` a palindrome?

If either is true, return `1`. If neither, return `0`.

**Steps:**
1. Initialize `left = 0`, `right = n - 1`.
2. While `left < right`:
   - If `s[left] == s[right]`, advance both pointers.
   - Else: return `isPalindrome(s, left+1, right) OR isPalindrome(s, left, right-1)`.
3. Return `1` (all chars matched — already a palindrome).

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best (Two Pointers — Explicit Helper)

**Intuition:** Identical to Approach 2. The helper `check(lo, hi)` validates a range `[lo, hi]` as a palindrome. This makes the skip decision at line 2 crystal clear: when we encounter a mismatch, we immediately spend our skip budget by calling the helper on both possibilities.

**Steps:**
1. Walk from both ends with `lo`, `hi`.
2. On first mismatch: `check(lo+1, hi) || check(lo, hi-1)`.
3. If no mismatch found, return `1`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Data validation with typo tolerance:** In form validation (e.g., enter a palindrome-format confirmation code), you may want to accept inputs that are "almost palindromes" — off by one character due to a typo. This algorithm provides a single-pass O(n) check for exactly that use case. Similarly, in bioinformatics, checking if a DNA sequence is "nearly self-complementary" with one mutation is a direct application.

## Interview Tips

- The brute force O(n^2) approach is easy to state but string slicing in Python creates O(n) copies — emphasize space cost.
- For the optimal approach, the key insight is: **you get exactly ONE mismatch before the answer is determined**. After that one mismatch, both `check(lo+1, hi)` and `check(lo, hi-1)` run straight without any further skipping.
- A common mistake: trying to implement "skip with a flag" inline instead of extracting the range-check helper. The helper is cleaner and avoids logic errors.
- Edge cases: single character (always `1`), two same characters (`1`), two different characters (`1` — remove either one → single char = palindrome), very long string with mismatch near the center.
- If the interviewer asks about removing **at most k characters**: this becomes a harder DP problem. One skip = O(n) two-pointer; k skips = O(n * k) DP.
