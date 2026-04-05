# Implement StrStr

> **Step 05 | 5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **LeetCode:** 28

## Problem Statement

Implement `strStr()`. Given two strings `haystack` and `needle`, return the index of the **first occurrence** of `needle` in `haystack`, or `-1` if `needle` is not part of `haystack`.

If `needle` is an empty string, return `0`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `haystack="sadbutsad", needle="sad"` | `0` | First occurrence at index 0 |
| `haystack="leetcode", needle="leeto"` | `-1` | needle not found |
| `haystack="hello", needle="ll"` | `2` | First occurrence at index 2 |
| `haystack="mississippi", needle="issip"` | `4` | Found at index 4 |
| `haystack="a", needle=""` | `0` | Empty needle always returns 0 |

## Constraints

- `0 <= haystack.length, needle.length <= 5 * 10^4`
- `haystack` and `needle` consist of only lowercase English characters

---

## Approach 1: Brute Force (Sliding Window)

**Intuition:** For each possible starting index in `haystack`, try to match all characters of `needle`. If all `m` characters match, return the start index.

**Steps:**
1. For each `i` from `0` to `n - m`:
   - Compare `haystack[i..i+m-1]` with `needle` character by character.
   - If all characters match, return `i`.
2. Return `-1` if no match found.

| Metric | Value      |
|--------|------------|
| Time   | O(n * m)   |
| Space  | O(1)       |

---

## Approach 2: Optimal (KMP - Knuth-Morris-Pratt)

**Intuition:** The bottleneck of brute force is re-examining characters after a mismatch. KMP avoids this by preprocessing `needle` into an LPS (Longest Proper Prefix which is also a Suffix) array. When a mismatch occurs at position `j` in needle, the LPS tells us the longest prefix of needle that is also a suffix of the matched portion — so we can resume from there without losing progress in `haystack`.

**Steps:**
1. Build the `lps` array for `needle`:
   - `lps[i]` = length of longest proper prefix of `needle[0..i]` that is also a suffix.
   - Use two pointers `len` and `i` to fill iteratively in O(m).
2. Search using two pointers `i` (haystack) and `j` (needle):
   - If characters match, advance both.
   - If `j == m`, found at `i - j`.
   - On mismatch: if `j > 0`, set `j = lps[j-1]` (don't advance `i`); else advance `i`.

| Metric | Value    |
|--------|----------|
| Time   | O(n + m) |
| Space  | O(m)     |

---

## Approach 3: Best (Rabin-Karp Rolling Hash)

**Intuition:** Represent each window of `haystack` as a hash value. Compute the hash in O(1) per slide using polynomial rolling: remove the contribution of the leftmost character and add the new rightmost character. On a hash match, verify with actual string comparison to handle collisions.

**Steps:**
1. Choose `BASE = 31`, `MOD = 10^9 + 7`.
2. Compute `needleHash` and initial `windowHash` for `haystack[0..m-1]`.
3. If hashes match and strings equal, return `0`.
4. For each `i` from `1` to `n - m`:
   - Roll: `windowHash = (windowHash - charVal(haystack[i-1]) * BASE^(m-1)) * BASE + charVal(haystack[i+m-1])`.
   - On hash match, verify with substring equals.
5. Return `-1` if not found.

| Metric | Value              |
|--------|--------------------|
| Time   | O(n + m) average  |
| Space  | O(1)              |

---

## Real-World Use Case

**Full-text search engines:** When a search engine indexes documents and answers queries like "find all occurrences of 'algorithm' in this 10 MB text file," KMP or Rabin-Karp is used under the hood. grep, text editors' "Find" feature, and database LIKE queries all rely on efficient substring search. For multiple pattern search (like virus scanners checking for known signatures), this extends to Aho-Corasick (multi-pattern KMP).

## Interview Tips

- Start with brute force — it demonstrates understanding and is correct.
- KMP is the canonical interview answer for O(n+m). Be ready to explain the LPS array intuitively: "it tells us how far back to fall if a match fails, without throwing away what we already matched."
- A common mistake: in KMP search, when there's a mismatch and `j > 0`, you reset `j = lps[j-1]` but do NOT advance `i`. Many candidates accidentally advance both.
- Rabin-Karp is great for mentioning in context of multiple patterns (e.g., find any of k needles).
- Know Python's built-in: `haystack.find(needle)` and Java's `haystack.indexOf(needle)` — the interviewer may ask how they're implemented internally.
- Edge cases: `needle` longer than `haystack`, empty `needle`, `haystack == needle`.
