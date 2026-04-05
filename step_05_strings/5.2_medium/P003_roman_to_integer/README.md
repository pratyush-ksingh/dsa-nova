# Roman to Integer

> **Batch 2 of 12** | **Topic:** Strings | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a string `s` representing a Roman numeral, convert it to an integer.

Roman numerals use seven symbols: `I=1, V=5, X=10, L=50, C=100, D=500, M=1000`.

Subtractive notation: when a smaller value appears **before** a larger value, it is subtracted (e.g., `IV = 4`, `IX = 9`, `XL = 40`, `XC = 90`, `CD = 400`, `CM = 900`).

**LeetCode #13**

**Constraints:**
- `1 <= s.length <= 15`
- `s` contains only the characters `I, V, X, L, C, D, M`
- `s` is a valid Roman numeral in the range `[1, 3999]`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `"III"` | `3` | 1 + 1 + 1 |
| `"LVIII"` | `58` | 50 + 5 + 1 + 1 + 1 |
| `"MCMXCIV"` | `1994` | 1000 + 900 + 90 + 4 = 1000 + (1000-100) + (100-10) + (5-1) |
| `"IV"` | `4` | 5 - 1 (subtractive) |
| `"IX"` | `9` | 10 - 1 (subtractive) |

### Real-Life Analogy
> *Think of reading a price tag where digits can sometimes cancel each other out. Normally you read left to right, adding each digit's value. But if you see a small coin placed before a large coin, it means "subtract the small one." For example, if someone places a penny before a nickel, it means 4 cents, not 6. You scan left to right: if the current symbol is smaller than the next one, subtract it; otherwise, add it.*

### Key Observations
1. Roman numerals are read left to right, and normally you just add values.
2. The only twist is the **subtractive rule**: if `value(current) < value(next)`, subtract instead of add. <-- This is the "aha" insight
3. The last character is always added (it has no "next" to compare against).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **HashMap/Dictionary** to map characters to their integer values -- O(1) lookup per character.
- The string itself is our input; we just need sequential access.

### Pattern Recognition
- **Pattern:** Single-pass string scan with lookahead (compare current character to the next)
- **Classification Cue:** "When you see _convert a notation system with context-dependent rules_ --> think _scan with lookahead_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- Handle All 13 Cases Explicitly
**Idea:** Check for all two-character subtractive combinations first (IV, IX, XL, XC, CD, CM), then handle single characters.

**Steps:**
1. Initialize `result = 0`, `i = 0`.
2. While `i < len(s)`:
   - If `s[i:i+2]` matches a two-character pattern (IV, IX, XL, XC, CD, CM): add corresponding value, advance `i` by 2.
   - Else: add value of `s[i]`, advance `i` by 1.
3. Return `result`.

**Why it works:** Exhaustively handles every valid combination.

**Why we move on:** **Duplicated logic** -- many if/elif branches. The pattern can be captured by a single rule.

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 2: Optimal -- Subtraction Rule (Lookahead)
**What changed (BUD -- Duplicated Work):** Instead of enumerating all 13 cases, use one rule: if `value(s[i]) < value(s[i+1])`, subtract; otherwise, add.

**Steps:**
1. Create a map: `{'I':1, 'V':5, 'X':10, 'L':50, 'C':100, 'D':500, 'M':1000}`.
2. Initialize `result = 0`.
3. For each index `i` from 0 to n-1:
   - If `i < n-1` and `map[s[i]] < map[s[i+1]]`: subtract `map[s[i]]` from result.
   - Else: add `map[s[i]]` to result.
4. Return `result`.

**Dry Run:** Input = `"MCMXCIV"`

| Step | i | s[i] | val | s[i+1] | next_val | val < next? | Action | result |
|------|---|------|-----|--------|----------|-------------|--------|--------|
| 1 | 0 | M | 1000 | C | 100 | No | +1000 | 1000 |
| 2 | 1 | C | 100 | M | 1000 | Yes | -100 | 900 |
| 3 | 2 | M | 1000 | X | 10 | No | +1000 | 1900 |
| 4 | 3 | X | 10 | C | 100 | Yes | -10 | 1890 |
| 5 | 4 | C | 100 | I | 1 | No | +100 | 1990 |
| 6 | 5 | I | 1 | V | 5 | Yes | -1 | 1989 |
| 7 | 6 | V | 5 | - | - | No (last) | +5 | 1994 |

**Result:** `1994`

| Time | Space |
|------|-------|
| O(n) | O(1) -- the map has a fixed 7 entries |

### Approach 3: Elegant Variant -- Right-to-Left Scan
**Idea:** Scan from right to left. If `value(s[i]) < value(s[i+1])`, subtract; else add. Eliminates the boundary check for the last element.

**Steps:**
1. Start with `result = map[s[n-1]]` (rightmost character).
2. For `i` from n-2 down to 0:
   - If `map[s[i]] < map[s[i+1]]`: result -= map[s[i]]
   - Else: result += map[s[i]]
3. Return `result`.

Same complexity, slightly cleaner in some implementations.

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We scan each character exactly once, doing an O(1) map lookup and comparison each time. Since n <= 15, this is effectively constant."
**Space:** O(1) -- "The character-to-value map has exactly 7 fixed entries. No scaling with input."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting the subtractive rule entirely -- just summing all values gives wrong answers for IV, IX, etc.
2. Off-by-one in the lookahead: accessing `s[i+1]` when `i == n-1` causes an index error.
3. Not handling all six subtractive combinations (IV, IX, XL, XC, CD, CM) if using the brute force approach.

### Edge Cases to Test
- [ ] Single character `"V"` --> 5
- [ ] All subtractive pairs `"MCMXCIV"` --> 1994
- [ ] Maximum value `"MMMCMXCIX"` --> 3999
- [ ] Minimum value `"I"` --> 1
- [ ] Repeated characters `"III"` --> 3
- [ ] Adjacent subtractive + additive `"XIV"` --> 14 (X=10, I before V = 4)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I'm converting a Roman numeral string to an integer. The key rule is the subtractive notation."
2. **Match:** "This is a string scan with a simple lookahead rule."
3. **Plan:** "I'll map each character to its value, then scan left to right. If current < next, I subtract; otherwise I add."
4. **Implement:** Write the map and the loop.
5. **Review:** Trace through "MCMXCIV" showing each step.
6. **Evaluate:** "O(n) time, O(1) space. Practically O(1) everything since the input is bounded at 15 characters."

### Follow-Up Questions
- "How about Integer to Roman?" --> LC #12. Use a greedy approach with a value-symbol table from largest to smallest.
- "What if the input could be invalid?" --> Validate that subtractive pairs are only the six allowed ones.
- "Could you do this with no map, just if-else?" --> Yes, but the map is cleaner and more extensible.

---

## CONNECTIONS
- **Prerequisite:** String traversal, HashMap basics
- **Same Pattern:** Any scan-with-lookahead problem (e.g., run-length encoding)
- **Companion:** Integer to Roman (LC #12) -- the reverse problem
- **This Unlocks:** Understanding notation/encoding conversion problems
