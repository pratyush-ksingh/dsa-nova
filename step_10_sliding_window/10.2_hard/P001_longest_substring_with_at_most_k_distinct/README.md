# Longest Substring with At Most K Distinct Characters

> **Step 10 - 10.2 Hard Sliding Window** | **LeetCode 340** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

## Problem Statement

Given a string `s` and an integer `k`, return the length of the **longest substring** that contains **at most `k` distinct characters**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `s = "eceba"`, `k = 2` | `3` | Substring `"ece"` has 2 distinct chars and length 3 |
| `s = "aa"`, `k = 1` | `2` | Entire string has only 1 distinct char |
| `s = "abcadcacacaca"`, `k = 3` | `11` | Substring `"cadcacacaca"` has 3 distinct chars |

## Constraints

- `1 <= s.length <= 5 * 10^4`
- `0 <= k <= 50`

---

## Approach 1: Brute Force

**Intuition:** Try every possible starting index. For each start, extend rightward and track distinct characters using a set. Stop when the count exceeds `k`. Record the maximum valid length found.

**Steps:**
1. For each index `i` from 0 to n-1:
2. Initialize a `seen` set.
3. For each index `j` from `i` to n-1: add `s[j]` to `seen`.
4. If `len(seen) > k`, break.
5. Otherwise update `best = max(best, j - i + 1)`.
6. Return `best`.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(k)   |

---

## Approach 2: Optimal — Sliding Window + HashMap

**Intuition:** A fixed-start approach revisits characters unnecessarily. Instead, use two pointers `left` and `right` defining a window `[left, right]`. Expand `right` unconditionally. Whenever the window contains more than `k` distinct characters, shrink from `left` until the invariant is restored. This is valid because if `[left, right]` is invalid, any window starting at `left` and ending beyond `right` is also invalid.

**Steps:**
1. Maintain `freq: char -> count` for the current window.
2. Move `right` forward, incrementing `freq[s[right]]`.
3. If `len(freq) > k`, repeatedly: decrement `freq[s[left]]`, delete if zero, advance `left`.
4. Update `best = max(best, right - left + 1)`.
5. Return `best`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(k)  |

---

## Approach 3: Best — Sliding Window + OrderedDict / LinkedHashMap

**Intuition:** In the optimal approach, the inner `while` loop can scan many characters before finding the one to evict. By storing the **most-recent index** of each character in an OrderedDict (Python) or LinkedHashMap (Java), we can identify the character with the earliest occurrence in O(1) and jump `left` directly — making each shrink step O(1) amortised.

**Steps:**
1. Maintain `last_seen: char -> most_recent_index` as an ordered structure (insertion order = arrival order).
2. On seeing `s[right]`: remove and re-insert `s[right]` (refreshes its position to the end).
3. If `len(last_seen) > k`: pop the first entry (oldest char `c` at index `idx`). Set `left = idx + 1`.
4. Update `best = max(best, right - left + 1)`.
5. Return `best`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(k)  |

---

## Real-World Use Case

**Network packet inspection / intrusion detection:** An IDS engine analyses a byte stream looking for the longest contiguous segment that uses at most `k` distinct protocol identifiers or payload byte values. The sliding window finds the widest such segment in a single pass, enabling real-time analysis at line rate.

## Interview Tips

- Clarify edge cases first: `k = 0` (answer is 0), `k >= len(set(s))` (answer is `len(s)`).
- The sliding window template is: "expand right freely; shrink left until the invariant holds; update answer." Knowing this template lets you write the code in under 2 minutes.
- The LinkedHashMap trick (store last-seen index, jump left in O(1)) is a strong follow-up differentiator — mention it after presenting the HashMap solution.
- This problem is the general form of LeetCode 3 (k=1 distinct) and LeetCode 159 (k=2 distinct). The same template handles all three by changing one constant.
- Time complexity is O(n) because each character is added and removed from the map at most once — amortised O(1) per character.
