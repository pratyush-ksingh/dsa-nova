# Bulls and Cows

> **LeetCode 299** | **Step 05 - Strings (Medium)** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

You are playing the **Bulls and Cows** game. You write a secret number and ask a friend to guess it. After each guess you provide a hint:

- **Bulls (A):** digits that are in the correct position.
- **Cows (B):** digits that are in the secret number but in the wrong position (and not already counted as a bull).

Given strings `secret` and `guess` (both are strings of digits, same length), return the hint in the format `"xAyB"`.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `secret="1807", guess="7810"` | `"1A3B"` | Bull: 8 at pos 1. Cows: 1, 7, 0 in wrong positions |
| `secret="1123", guess="0111"` | `"1A1B"` | Bull: 1 at pos 2. Cow: 1 (only one extra 1 in secret) |
| `secret="1", guess="0"` | `"0A0B"` | No matches |
| `secret="1122", guess="2211"` | `"0A4B"` | All in wrong positions |

## Constraints

- `1 <= secret.length <= 1000`
- `secret.length == guess.length`
- `secret` and `guess` consist of digits only.

---

## Approach 1: Brute Force — Two-Pass with Frequency Arrays

**Intuition:** First pass identifies all bulls and collects the remaining (non-bull) digit counts for both secret and guess. Second pass counts cows as the overlap (minimum of each digit's surplus in secret vs. guess).

**Steps:**
1. Iterate through both strings simultaneously.
2. If `secret[i] == guess[i]`: increment `bulls`.
3. Else: increment `secretCount[secret[i]]` and `guessCount[guess[i]]`.
4. Cows = `sum(min(secretCount[d], guessCount[d]))` for `d` in `0..9`.
5. Return `"bullsAcowsB"`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) — two digit arrays of size 10 |

---

## Approach 2: Optimal — Single Pass with Two Frequency Arrays

**Intuition:** Process everything in a single pass. For each non-bull pair `(s, g)`: if `s` was previously seen as a guess surplus, it creates a cow now; if `g` was previously seen as a secret surplus, it creates a cow now. Update the appropriate array for future matches.

**Steps:**
1. Initialise `sCount[10]` and `gCount[10]` to zero.
2. For each position `i`:
   - If `s == g`: `bulls++`.
   - Else:
     - If `gCount[s] > 0`: `cows++`, `gCount[s]--`. Else `sCount[s]++`.
     - If `sCount[g] > 0`: `cows++`, `sCount[g]--`. Else `gCount[g]++`.
3. Return `"bullsAcowsB"`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 3: Best — Single Pass with One Signed Frequency Array

**Intuition:** Use a single `freq[10]` array where positive values represent a surplus in `secret` and negative values represent a surplus in `guess`. When we see `freq[s] < 0`, digit `s` was previously a guess surplus — that is now a cow. When `freq[g] > 0`, digit `g` was previously a secret surplus — cow as well.

**Steps:**
1. `freq[10]` all zeros.
2. For each non-bull position: check `freq[s] < 0` and `freq[g] > 0` for cows, then `freq[s]++`, `freq[g]--`.
3. Return `"bullsAcowsB"`.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Password strength / code-breaking tools:** The Bulls and Cows logic is the foundation of the board game Mastermind and also appears in DNA sequence alignment (counting exact and approximate base matches), fuzzy string comparison in spell-checkers (counting characters in common vs. in the right position), and one-time password verification systems where partial credit for near-misses is used to guide users.

## Interview Tips

- The key subtlety is that a digit cannot be counted as both a bull AND a cow. Bulls are removed from consideration before counting cows.
- The single signed-array trick (Approach 3) is elegant and shows deep understanding — worth mentioning as a follow-up even if you code Approach 1 first.
- Edge cases: repeated digits (e.g., secret="1122", guess="2211") — verify your cow-counting does not double-count.
- The problem guarantees equal-length strings of digits, so no need to validate input.
- If extended to letters (Mastermind), the same O(1) space digit-array approach works with a 26-element array.
