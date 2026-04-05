# Valid Parenthesis String

> **Step 12 | 12.1** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given a string `s` containing only three types of characters: `'('`, `')'` and `'*'`, determine if the string is valid. The rules are:
- Every `'('` must have a corresponding `')'`.
- Every `')'` must have a corresponding `'('`.
- `'('` must come before its corresponding `')'`.
- `'*'` can be treated as `'('`, `')'`, or an empty string.

**Source:** LeetCode 678

**Constraints:**
- `0 <= s.length <= 100`
- `s` contains only `'('`, `')'` and `'*'`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `"()"` | `true` | Standard valid parentheses |
| `"(*)"` | `true` | `*` treated as empty or `)` then `(` -- many valid interpretations |
| `"(*))"` | `true` | `*` treated as `(`: becomes `(())` |
| `"(((*)"` | `false` | Not enough closing parens/stars to balance |
| `""` | `true` | Empty string is valid |
| `"*"` | `true` | `*` treated as empty |

### Real-Life Analogy
> *Think of a bouncer at a club with a "couples only" policy. Each '(' is a person entering, each ')' is a person leaving, and '*' is a flexible friend who can enter, leave, or just stand outside. The bouncer needs to ensure that at no point do more people leave than have entered, and everyone who entered has left by closing time. The min/max tracking is like the bouncer keeping a "best case / worst case" headcount.*

### Key Observations
1. Without stars, this is simple: track open count, ensure it never goes negative, and equals 0 at the end.
2. Stars introduce uncertainty: the open count is no longer a single number but a **range** `[lo, hi]`.
3. `lo` represents the minimum possible open count (stars used as `)` or empty).
4. `hi` represents the maximum possible open count (stars used as `(`).
5. If `hi < 0` at any point, even the most optimistic interpretation fails.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- **Greedy range tracking** avoids exponential branching over star choices.
- No extra data structure needed -- just two integers.

### Pattern Recognition
- **Pattern:** Greedy with range/interval tracking
- **Classification Cue:** "Whenever you see _wildcard that can be multiple things_ in a validation problem --> think _track min/max of the state variable_."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion
**Idea:** At each `'*'`, branch into 3 choices: `'('`, `')'`, or empty. Check if any branch produces a valid string.

**Steps:**
1. Recursively process each character with an `open_count`.
2. For `'('`: recurse with `open_count + 1`.
3. For `')'`: recurse with `open_count - 1`. If `open_count < 0`, prune.
4. For `'*'`: try all three options.
5. At end of string: return `open_count == 0`.

**Why it works:** Exhaustive search over all possible star interpretations.

**BUD Transition -- Bottleneck:** O(3^m) where m = number of stars. With memoization on `(idx, open_count)`, this improves to O(n^2), but we can do O(n) with greedy.

| Metric | Value |
|--------|-------|
| Time   | O(3^m) worst case, O(n^2) with memo |
| Space  | O(n) recursion stack |

---

### Approach 2: Optimal -- Min/Max Open Count (Greedy)
**What changed:** Instead of branching, track the range `[lo, hi]` of possible open counts.

**Steps:**
1. Initialize `lo = 0`, `hi = 0`.
2. For each character:
   - `'('`: `lo++`, `hi++`
   - `')'`: `lo--`, `hi--`
   - `'*'`: `lo--`, `hi++`
3. If `hi < 0`: return false (impossible to balance).
4. Clamp `lo = max(lo, 0)` (open count cannot be negative in valid prefix).
5. At end: return `lo == 0`.

**Dry Run:** `s = "(*))")`

| idx | char | lo | hi | Action |
|-----|------|----|----|--------|
| 0   | (    | 1  | 1  | |
| 1   | *    | 0  | 2  | lo=max(0,0) |
| 2   | )    | -1->0 | 1 | lo clamped to 0 |
| 3   | )    | -1->0 | 0 | lo clamped to 0 |

**Result:** lo == 0 --> true

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

### Approach 3: Best -- Same Two-Counter (Cleanest Form)
**Intuition:** The min/max greedy approach IS the optimal solution. No further improvement possible. Alternative formulation: two separate left-to-right and right-to-left passes, but the single-pass min/max is more elegant and achieves the same result.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to clamp `lo` to 0 -- negative `lo` means we are "borrowing" closes that do not exist.
2. Only checking at the end without checking `hi < 0` during iteration -- this misses early invalidity like `)(`.
3. Thinking a stack-based approach is needed -- it works but is O(n) space vs O(1).

### Edge Cases to Test
- [ ] Empty string --> true
- [ ] All stars --> true (treat all as empty)
- [ ] Single `(` --> false
- [ ] Single `)` --> false
- [ ] Stars at beginning/end only
- [ ] `)(` --> false (order matters)

---

## Real-World Use Case
**Template validation in compilers and parsers:** When parsing expressions with optional grouping (like SQL with optional parentheses around conditions), the compiler needs to validate balanced brackets where some tokens are ambiguous. The greedy range technique validates in linear time without backtracking.

## Interview Tips
- Start with the brute force recursion to show you understand the problem.
- The key insight is: "track min and max possible open count" -- say this clearly.
- Explain why `lo` is clamped to 0 (physical meaning: we cannot have negative unmatched opens).
- Explain why `hi < 0` means immediate failure (even using all stars as `(` is not enough).
- This is a classic Google/Meta interview question. The greedy approach is expected.
